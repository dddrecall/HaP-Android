package com.HaP.Byml;
import android.app.*;
import android.os.*;
import android.content.*;

public class logo extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
	super.onCreate(savedInstanceState);
	setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
	setContentView(R.layout.logo);
	new Thread(){
	  public void run()
	  {
		try
		{
		  sleep(1000);
		}
		catch (InterruptedException e)
		{
		  
		}
		Intent i=new Intent(logo.this, MainActivity.class);	
		startActivity(i);
		finish();
	  }
	  
	}.start();
	}
  
}
