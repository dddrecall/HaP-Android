package com.HaP.Byml;

import android.app.*;
import android.os.*;
import android.webkit.*;
import android.util.*;

public class Help extends Activity
{
  AlertDialog Alog;
  
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
		webview.setWebChromeClient(
			new WebChromeClient(){
				@Override
				public boolean onJsAlert(WebView view, String url, String message, JsResult result)
				{
					//Log.e("Webview","url:"+url+" message:"+message+" result:"+result.toString());				
				   Alog=new AlertDialog.Builder(Help.this).setTitle("帮助").setMessage(message).setIcon(
					android.R.drawable.ic_dialog_info)
					.setPositiveButton("隐藏",null).show();	
				    result.cancel();
					return Alog.isShowing();
					//return super.onJsAlert(view,"帮助", message, result);
				
				}
			}

		);

		webview.loadUrl("file:///android_asset/help.html");
	}

}
