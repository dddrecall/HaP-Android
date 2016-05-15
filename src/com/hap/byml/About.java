package com.HaP.Byml;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.HaP.Byml.*;
import com.HaP.Tool.*;
import java.io.*;

public class About extends Activity
{

  private Button b_dinstall,b_and5_0,b_fix,b_hx,b_hxinfo,button;

  private listener b_listenr;
  
  EditText e;
  
  private SuperUser Su;
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
	super.onCreate(savedInstanceState);
	setTheme(R.style.ApphaveTitle);
	//	setTheme(android.R.style.Widget_Holo_Light_ExpandableListView);
	setContentView(R.layout.about);
	b_dinstall = (Button)findViewById(R.id.b_dinstall);
	b_and5_0=(Button)findViewById(R.id.b_and5_0);
	b_fix=(Button)findViewById(R.id.b_fix);
	button=(Button)findViewById(R.id.aboutButton1);
	b_hx=(Button)findViewById(R.id.b_newhx);
	b_hxinfo=(Button)findViewById(R.id.b_hxinfo);
	e=(EditText)findViewById(R.id.aboutEditText1);
	b_listenr=new listener();
	b_dinstall.setOnClickListener(b_listenr);
	b_fix.setOnClickListener(b_listenr);
	b_and5_0.setOnClickListener(b_listenr);  
	b_hxinfo.setOnClickListener(b_listenr);
	b_hx.setOnClickListener(b_listenr);
	Su=new SuperUser(this);
	boolean root=SuperUser.checkRoot();
	//Su.setIs_root(root);
	//Su.setIs_root(root);
  }


  private class listener implements OnClickListener
  {

	@Override
	public void onClick(View p1)
	{
	  // TODO: Implement this method
	  switch (p1.getId())
	  {
		case R.id.b_dinstall:

		  new AlertDialog.Builder(About.this).setTitle("是否删除安装完成标识文件？").setMessage("此操作会导致下次启动HaP的时候提示安装，如果不安装 这个提示在每次启动HaP都会提示").setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("删除" , new DialogInterface.OnClickListener(){

			  @Override
			  public void onClick(DialogInterface p1, int p2)
			  {
				// TODO: Implement this method

				if (new File(About.this.getFilesDir().getPath() + "/" + SuperUser.version).delete())
				  Toast.makeText(About.this, "删除安装记录文件成功！\n下次启动的时候会提示安装 ！", Toast.LENGTH_LONG).show();
				else
				  Toast.makeText(About.this, "删除安装记录文件失败！\n请检查是否安装过 !", Toast.LENGTH_LONG).show();

			  }

			})
			.setNegativeButton("返回" , null).show();			

		  break;
		 
		  case R.id.b_and5_0:
		  DownThread dt=new DownThread(About.this,Su,"http://byml.net/HaP/hx5.zip",Environment.getExternalStorageDirectory().getPath(),"HaP_hx5.zip");
		  dt.execute();
			
			break;
		  case R.id.b_newhx:
		  DownThread dx=new DownThread(About.this,Su,"http://byml.net/HaP/hx.zip",Environment.getExternalStorageDirectory().getPath(),"HaP_hx.zip");
		  dx.execute();
			break;
		 case R.id.b_fix:
		
		  new AlertDialog.Builder(About.this).setTitle(About.this.getString(R.string.newfix_title)).setMessage(About.this.getString(R.string.newfix)).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定" , null)
			.setNegativeButton("返回" , null).show();			
		  
		   break;
		  case R.id.aboutButton1:
		  try
		  {
			DesUtils des=new DesUtils("byml.net");
			e.setText(des.encrypt(e.getText().toString()));
			
		  }
		  catch (Exception e)
		  {}
			
			break;
		  case R.id.b_hxinfo:
		  Su.DShell("cd "+About.this.getFilesDir().getPath()+"\n./HaP -v\n",true).start();
			
			break;
	  }
	  
	}



  }

}
