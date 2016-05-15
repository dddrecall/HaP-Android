package com.HaP.Byml;

import android.app.*;
import android.os.*;
import android.webkit.*;

public class Code_help extends Activity
{
	WebView webview;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ApphaveTitle);
		setContentView(R.layout.help);
		webview = (WebView)findViewById(R.id.h_web);	 
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSavePassword(true);
		webview.getSettings().setDefaultTextEncodingName("utf-8");  
	    webview.getSettings().setSupportZoom(true);          //支持缩放
	    webview.getSettings().setBuiltInZoomControls(true);  //启用内置缩放装置
	  
		
		webview.loadUrl("file:///android_asset/code_help.html");
	}
	
}
