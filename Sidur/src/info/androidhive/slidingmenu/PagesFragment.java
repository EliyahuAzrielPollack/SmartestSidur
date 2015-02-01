package info.androidhive.slidingmenu;

import java.util.Timer;
import java.util.TimerTask;
import DB_management.BF;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PagesFragment extends Fragment {
	
	public PagesFragment(){}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html><head><link rel=\"stylesheet\" href=\"file:///android_asset/sidur_background_color.css\" type=\"text/css\">");
	    builder.append("</link>");
	    builder.append("<script>");
	    builder.append("function scrollAnchor(id) {");
	    builder.append("window.location.hash = id;}");
	    builder.append("</script>");
	    builder.append("</head><body lang=EN-US>");
	    builder.append(BF.getInstance(getActivity()).getPray("arvit_a", "id1"));
	    builder.append("</body></html>");

	    View rootView = inflater.inflate(R.layout.fragment_pages, container, false);
	    final WebView webContents = (WebView)rootView.findViewById(R.id.webView1);
	    WebSettings webSettings = webContents.getSettings();
	    webSettings.setJavaScriptEnabled(true);
		webContents.loadDataWithBaseURL("file:///android_asset/", builder.toString(), "text/html", "utf-8", null);

		webContents.setWebViewClient(new WebViewClient(){

		    @Override
		    public void onPageFinished(WebView view, String url) {
		          String id = "é881234";
		          webContents.loadUrl("javascript:scrollAnchor(" + id + ");");
		    }

		});
        return rootView;
    }
}
