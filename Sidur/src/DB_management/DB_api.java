package DB_management;

import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishDate;
import android.R.bool;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DB_api {
	
	data_base_helper myDbHelper;
	String chosen_string = "";
	JewishCalendar jc = new JewishCalendar();
	HebrewDateFormatter hdf = new HebrewDateFormatter();
	enum Pray {shaharit , minha , arvit};
	
	public  DB_api(Context context){
		
	  //super(context, SQLite_names.DATABASE_NAME, null, SQLite_names.DATABASE_VERSION);
	  jc.setInIsrael(true); //default false for clarity but not needed. Set to true for Israel
	  hdf.setHebrewFormat(true);
	  myDbHelper = new data_base_helper(context);
	
		    try {
		    	myDbHelper.createDataBase();
		
			} catch (IOException ioe) {
		
				throw new Error("Unable to create database");
			}
		
			try {
				myDbHelper.openDataBase();
		
			} catch(SQLException sqle){
		
				throw sqle;
		}
	}
	
	public String getPray (String Pray , String select) {
		
	   String selectQuery = "SELECT * FROM " + Pray +
	   " WHERE " + Pray + ".id IN (SELECT " + select + " FROM select_prayer)";   

	   SQLiteDatabase db = myDbHelper.getReadableDatabase();
	   Cursor cursor = db.rawQuery(selectQuery, null);
	   String res ="";
	   // looping through all rows and adding to list
	   if (cursor.moveToFirst()) {
	        do {
	        	res += cursor.getString(4);
	        } while (cursor.moveToNext());
	    }
		return res;
	}
	
	public String getPray1(String Pray, String Select){
		
       String res = "";
	
	   String selectQuery = "SELECT * FROM " + Pray ;   
	   SQLiteDatabase db = myDbHelper.getReadableDatabase();
	   Cursor cursor = db.rawQuery(selectQuery, null);
	   ArrayList<String> pray = new ArrayList<String>();
	   // looping through all rows and adding to list
	   if (cursor.moveToFirst()) {
	        do {
	        	pray.add(cursor.getString(2));
	        } while (cursor.moveToNext());
	   }
	   
	   selectQuery = "SELECT " + Select + " FROM select_prayer";   
	   db = myDbHelper.getReadableDatabase();
	   cursor = db.rawQuery(selectQuery, null);
	   ArrayList<Integer> select = new ArrayList<Integer>();
	   // looping through all rows and adding to list
	   if (cursor.moveToFirst()) {
	        do {
	        	select.add(cursor.getInt(0));
	        } while (cursor.moveToNext());
	   }
	   
	   for(int i = 1 ;i < select.size();i++)
       {
		   res +=  pray.get(select.get(i));
       }
       
	   return res;
	}
	
	//פונקציה הבודקת האם מדובר בחורף
	public boolean isWinter(){
		//אם התאריך הוא מעבר לכ"ב תשרי מדובר בחורף
		return ((jc.getJewishMonth() == JewishDate.TISHREI && jc.getJewishDayOfMonth() >= 22) 
				|| (jc.getJewishMonth() > JewishDate.TISHREI)
				|| (jc.getJewishMonth() == JewishDate.NISSAN && jc.getJewishDayOfMonth() <= 15));
	}
	
	
    
	/*String get_shaharit (){
	
	}
	
	String get_minha (){
		
	}*/
	
	public String get_arvit(){
		
		String arvit_string = "";
		
		if(isWinter()){//חורף - מכ"ב תשרי עד חג הפסח
			//מכ"ב תשרי עד סוף החודש - משיב הרוח 
            if(jc.getJewishMonth() == JewishDate.TISHREI && jc.getJewishDayOfMonth() >= 22)
            	arvit_string = "";
            //ר"ח חשוון - משיב הרוח ותן ברכה יעלה ויבוא
		    else if(jc.isRoshChodesh() && jc.getJewishMonth() == JewishDate.CHESHVAN)
		    	arvit_string = "";
            //עד ז'חשוון - משיב הרוח ותן ברכה
			else if(jc.getJewishMonth() == JewishDate.CHESHVAN && 
					jc.getJewishDayOfMonth() < 7)
				arvit_string = "";
            //מ ז'חשוון עד סוף החודש - משיב הרוח ותן טל ומטר
			else if(jc.getJewishMonth() == JewishDate.CHESHVAN && 
					jc.getJewishDayOfMonth() < 7)
				arvit_string = "";
            //חנוכה יום רגיל -משיב הרוח ותן טל ומטר ועל הניסים
			else if(jc.isChanukah() && !jc.isRoshChodesh())
				arvit_string = "";
            //חנוכה ר"ח טבת -משיב הרוח ותן טל ומטר על הניסים ויעלה ויבוא
			else if(jc.isChanukah() && jc.isRoshChodesh())
				arvit_string = "";
            //כל שאר ראשי חודשים עד סוף החורף -משיב הרוח ותן טל ומטר ויעלה ויבוא
			else if (jc.isRoshChodesh())
				arvit_string = "";
            //פורים דפרזים ודמוקפים - על הניסים 
			else if(!jc.isJewishLeapYear() && jc.getJewishMonth() == JewishDate.ADAR 
					&& (jc.getJewishDayOfMonth() == 14 || jc.getJewishDayOfMonth() == 15))
				arvit_string = "";
            //ערבית חורפית רגילה - משיב הרוח , ותן טל ומטר 
			else
				arvit_string = "";	
		}
		
		else if (!isWinter()) {//קיץ - מחג הפסח עד כ"ב תשרי
			//חול המועד  פסח - יעלה ויבוא בחג המצות,מוריד הטל,ותן ברכה,ספירת העומר
			if(jc.getJewishMonth() == JewishDate.NISSAN && jc.getJewishDayOfMonth() >= 16 && jc.isCholHamoed())
				arvit_string = "";
			//שאר חודש ניסן ואייר  ותחילת סיוון - מוריד הטל,ותן ברכה,ספירת העומר
			else if ((jc.getJewishMonth() == JewishDate.NISSAN && jc.getJewishDayOfMonth() >= 16 && !jc.isCholHamoed())
		              || jc.getJewishMonth() == JewishDate.NISSAN || (jc.getJewishMonth() == JewishDate.SIVAN && 
		              jc.getJewishDayOfMonth() <= 5))
				arvit_string = "";
			//תשעה באב - מוריד הטל,ותן ברכה,קינות,מגילת איכה
			else if (jc.getJewishMonth() == JewishDate.AV && jc.getJewishDayOfMonth() == 9)
				arvit_string = "";
			//חודש אלול - מוריד הטל,ותן ברכה,לנוסח אשכנז לדוד ה'אורי
			else if (jc.getJewishMonth() == JewishDate.ELUL)
				arvit_string = "";
			//חודש תשרי עשי"ת - מוריד הטל,ותן ברכה,תוספות לעשי"ת,לדוד ה'אורי
			else if(jc.getJewishMonth() == JewishDate.TISHREI && jc.getJewishDayOfMonth() <= 10)
				arvit_string = "";
			//חודש תשרי בין יו"כ לסוכות - מוריד הטל,ותן ברכה,לדוד ה'אורי
			else if(jc.getJewishMonth() == JewishDate.TISHREI && jc.getJewishDayOfMonth() > 10 && jc.getJewishDayOfMonth() < 15)
				arvit_string = "";
			//חול המועד סוכות - מוריד הטל,ותן ברכה,יעלה ויבוא,לדוד ה'אורי (אופציונאלי:מזמורי בית השואבה) 
			else if(jc.getJewishMonth() == JewishDate.TISHREI && jc.isCholHamoed())
				arvit_string = "";
			//ערבית קייצית רגילה - מוריד הטל,ותן ברכה
			else
				arvit_string = "";	
		}
		//הוספת קידוש לבנה בימים הרלוונטיים בחודש
		if(jc.getJewishDayOfMonth() >= 7 && jc.getJewishDayOfMonth() <=15)
			return arvit_string += "";
		else
			return arvit_string;
	}
  }

