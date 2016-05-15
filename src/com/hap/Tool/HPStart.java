package com.HaP.Tool;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;
import com.HaP.Byml.*;
import java.io.*;
import com.HaP.View.*;

public class HPStart extends AsyncTask<String, Integer, String>
{
	private Context con;

	private String file_path,m_name;

	//private AlertDialog.Builder loader;

//	private View vloader;

	//private TextView loadText,loadText2;

//	private ProgressBar loadPb;
	private SharedPreferences sp;
  
	private String t_line;
	//private AlertDialog dialog;

	private SuperUser Su;

	
    private boolean is_add;

	private int ipos;

	private boolean is_save;

	private boolean is_continue=true;

	private Handler handler;

	private SuperUser.putline pl;

	private DLoader dloader;

	private static int i=0;

	private boolean is_toast=true;
	public HPStart(Context con, SuperUser Su, SharedPreferences sp, String file_path, String m_name, boolean is_add)
	{
		this.con = con;
		this.sp = sp;
		this.Su = Su;
		this.file_path = file_path;
		this.m_name = m_name;
		this.is_add = is_add;
		pl = new SuperUser.putline(){

			@Override
			public void rline(String line)
			{
				// TODO: Implement this method
				++i;
				synchronized (t_line)
				{
					t_line = line;
					publishProgress(520);
				}
			}


		};
	}
	public HPStart(Context con, SuperUser Su, SharedPreferences sp, String file_path, String m_name, boolean is_add, boolean istoast)
	{
		this.con = con;
		this.sp = sp;
		this.Su = Su;
		this.file_path = file_path;
		this.m_name = m_name;
		this.is_add = is_add;
		this.is_toast = istoast;
		pl = new SuperUser.putline(){

			@Override
			public void rline(String line)
			{
				// TODO: Implement this method
				++i;
				synchronized (line)
				{
					t_line = line;
					if(is_toast)
				  	    publishProgress(520);
					else
						publishProgress(1314);
				}
			}


		};
	}

	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	public Handler getHandler()
	{
		return handler;
	}
	public void setIPos(int ipos)
	{
		this.ipos = ipos;
	}
	public void setIs_save(boolean is_save)
	{
		this.is_save = is_save;
	}
	public boolean is_done()
	{		
		return is_continue;
	}

	public void set_done(boolean is)
	{
		this.is_continue = is;
	}


	//onPreExecute方法用于在执行后台任务前做一些UI操作
	@Override
	protected void onPreExecute() {
		if (is_toast)
		{	
			dloader = new DLoader(con, "正在处理...", "处理中...", 0);
			dloader.show();
		}

		if (is_add && !is_save)
		{
			if (new File(file_path + "/" + m_name).mkdirs())
			{
				sp.edit().putString("mode_" + ipos, m_name).commit();
			}else
			{
				t_line = "<font color='#ff0000'>" + Su.msgr("目录:" + m_name + " 创建失败 !请检测是否有相同文件模式 ！", -1) + "</font>";		 
				publishProgress(520);		
				is_continue = false;		

			}
		}

	}

	@Override
	protected synchronized String doInBackground(String[] p1)
	{
		if (!is_continue)
			return "";
		// TODO: Implement this method

		if (p1[0] == "start")
		{
	        String s1,s2,s3,sg,sd,sf;
			publishProgress(20);
			if(!new File(file_path+"/"+m_name).isDirectory())
			{
			  t_line = "\t\t模式:<font color='#ff0000'>" + m_name + "</font>文件不存在，启动失败！\n";
			  publishProgress(520);
			  publishProgress(100);	
			  return "";
			  
			}else
			{		  
			  t_line = "\t\t正在启动模式:<font color='#ff0000'>" + m_name + "</font>\n";
			  publishProgress(520);
			  
			}
			s1 = FileTool.readSDFile(file_path + "/" + m_name + "/connection.hp");
			s2 = FileTool.readSDFile(file_path + "/" + m_name + "/get.hp");
			s3 = FileTool.readSDFile(file_path + "/" + m_name + "/post.hp");
			sg = FileTool.readSDFile(file_path + "/mode/global.hp");
			sd = FileTool.readSDFile(file_path + "/mode/defaults.hp");
			sf = FileTool.readSDFile(file_path + "/mode/frontend.hp");
			publishProgress(40);
			s1 = SuperUser.replace_code(con,s1,sp)+ "\n";
			s2 = SuperUser.replace_code(con,s2,sp) + "\n";
			s3 = SuperUser.replace_code(con,s3,sp) + "\n";	
			publishProgress(50);
			sg = SuperUser.replace_code(con,sg,sp) + "\n";
			sd = SuperUser.replace_code(con,sd,sp) + "\n";
			sf = SuperUser.replace_code(con,sf,sp) + "\n";				 
			FileTool.writeSDFile(sg + sd + sf + s2 + s3 + s1, file_path + "/run.cfg");						
			Su.Stmp = Su.Shell(sp.getString("_stop", "cd " + file_path + "\n./H-stop ./haproxy.pid\n"), false);

			try
			{
				Su.Stmp.start();
				publishProgress(60);

				Su.Stmp.join(3000);
			}			
			catch (InterruptedException e) {

			}
			publishProgress(70);	
			 Su.Stmp = Su.Shell(sp.getString("_start", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n"), pl);		
				try
				{
					Su.Stmp.start();
					publishProgress(80);							
					Su.Stmp.join(3000);
				}
				catch (Exception e) {}
			publishProgress(100);			
			return "2";
		}
		else if (p1[0] == "save"){	
			FileTool.writeSDFile(p1[1], file_path + "/" + m_name + "/connection.hp");
			publishProgress(30);
			FileTool.writeSDFile(p1[2], file_path + "/" + m_name + "/get.hp");
			publishProgress(60);
			FileTool.writeSDFile(p1[3], file_path + "/" + m_name + "/post.hp");
			publishProgress(100);
			return "1";
		}
		else if (p1[0] == "mhp")
		{
			try
			{
				DesUtils des=new DesUtils("byml.net");
				FileTool.writeSDFile(des.decrypt(p1[1]), file_path + "/" + m_name + "/connection.hp");
				publishProgress(30);
				FileTool.writeSDFile(des.decrypt(p1[2]), file_path + "/" + m_name + "/get.hp");
				publishProgress(60);
				FileTool.writeSDFile(des.decrypt(p1[3]), file_path + "/" + m_name + "/post.hp");
				publishProgress(100);					
			}
			catch (Exception e) {
				t_line = "<font color='#ff0000'>" + e.toString() + "</font>";
				publishProgress(520);	
				return e.toString();
			}
			return "3";
		}else if(p1[0]=="sh")
		{
		  t_line = "\t\t正在启动脚本:<font color='#ff0000'>" + m_name + "</font>\n";
		  publishProgress(520);
		  String sh=FileTool.readSDFile(file_path+"/"+m_name);  
		  publishProgress(20);
		  sh=SuperUser.replace_code(con,sh,sp);   
	      FileTool.writeSDFile(sh,file_path+"/"+m_name+"_run_sh.sh");
		  publishProgress(50);		  
		  Su.Stmp = Su.Shell("cd "+file_path+"\nsh ./"+m_name+"_run_sh.sh\n", pl);		
		  try
		  {
			Su.Stmp.start();
			publishProgress(80);							
			Su.Stmp.join(5000);
		  }
		  catch (InterruptedException e) {
			t_line = "\t\t执行错误:<font color='#ff0000'>" + e.toString()+ "</font>\n";
			publishProgress(520);
			
		  }
		  publishProgress(100);	
		  
		  return "4";
		  
		}
		
		return "";
	}

	//onProgressUpdate方法用于更新进度信息
	@Override
	protected void onProgressUpdate(Integer... progresses) {
		//Log.i(TAG, "onProgressUpdate(Progress... progresses) called");	
		synchronized(progresses){
		if (is_toast)
			if (progresses[0] != 520)
			{
				dloader.setPnos(progresses[0]);	 	
				dloader.setInfo("正在处理...(" + progresses[0] + "%)");		 
			}
			else{
			    synchronized(t_line){
				  dloader.append("\t" + t_line.replace("\n", "\n\t") );
				  t_line="";
				}
			}
		else 
			if(progresses[0]==1314)
		   Su.msg("\t" + t_line.replaceAll("<font color='#ff0000'>","").replaceAll("</font>",""));
		}	
	}

	
	//onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(String result) {
		Message r1_Message;

		if (is_toast)
		{
			if (!dloader.isShowing())
			{			
				dloader.Showagain();
			}
			dloader.setTitle("处理完成....");
			dloader.setInfo("处理完成....");
			dloader.setPnos(100);
			dloader.setPToast(false);
	  }
	  if (result == "1"){
			if(is_toast)
			  dloader.append(Su.msgr("模式:" + m_name + "保存完成 !"));
			else
				Su.msgr("模式:" + m_name + "保存完成 !");
			r1_Message = new Message();
			r1_Message.obj = m_name;
			//保存模式
			r1_Message.what = 0;
			handler.sendMessage(r1_Message);				

		}else if (result == "2"){

		}
		else if (result == "3"){
			r1_Message = new Message();
			r1_Message.obj = m_name;
			//导入模式
			r1_Message.what = 2;
			handler.sendMessage(r1_Message);						
			if(is_toast)
			dloader.append(Su.msgr("导入模式: " + m_name + "完成 ！"));
			else
				Su.msgr("导入模式: " + m_name + "完成 ！");
		}else if(result=="4"){
		  
		}
		else{
			Su.msg(result, -1);
	  }
	}

}
