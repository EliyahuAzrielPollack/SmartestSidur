package DB_management;

import android.content.Context;

public final class BF {
	
	static DB_api instance = null;
	public final static DB_api getInstance(Context context) {
		if(instance == null)
		   instance = new DB_api(context);
		return instance;	
    }
}
