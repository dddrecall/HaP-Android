package com.HaP.Tool;

import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.HaP.*;
import com.HaP.View.*;
import java.io.*;
import java.util.*;

import java.lang.Process;
import com.HaP.utils.*;

public class SuperUser
{
  public Shell Stmp;
  public DShell DStmp;
  private Context C;
  public static String version="1.4.5";
  private static boolean is_root=false;
  private static String TAG="HaP";
  public final static String SHELL = "/system/bin/sh";
  public final static String ROOT = "/system/bin/su";
  public static String privpath;
  private static int si;
  public static boolean have_root=false;

  public static void setHave_root(boolean r)
  {
	have_root = r;
  }

  public static boolean isHave_root()
  {
	return have_root;
  }


  public void setIs_root(boolean is_root)
  {
	this.is_root = is_root;
  }

  public boolean is_root()
  {
	return is_root;
  }

  public static interface putline
  {
	public void rline(String line);	
  }
  public SuperUser(Context con)
  {
	this.C = con;
	this.privpath=con.getFilesDir().getPath();
  }	

  public synchronized DShell DShell(String shell, boolean istoast)
  {
	return new DShell(shell, istoast);		
  }
  public synchronized DShell DShell(String shell, boolean istoast, boolean d_root)
  {
	return new DShell(shell, istoast, d_root);		
  }
  public synchronized Shell Shell(String shell, boolean istoast)
  {
	return new Shell(shell, istoast);		
  }
  public synchronized Shell Shell(String shell, putline pl)
  {
	Shell sshell=new Shell(shell, false, pl);
	return sshell;
  }




  public class DShell extends Thread
  {
	private boolean istoast;
	private DLoader dloader;
	private boolean d_root;
	private String scripts;
	private int di=0;
	private String tfile="";
   
	
	public DShell(String scripts, boolean istoast)
	{
	  this.scripts = scripts;
	  this.istoast = istoast;
	  dloader = new DLoader(C, "执行命令中...", "正在执行...", 30);
	  if (istoast)
		dloader.show();	
	  d_root = is_root;
	}

	public DShell(String scrips, boolean istoast, boolean d_root)
	{
	  this.scripts = scrips;
	  this.istoast = istoast;
	  dloader = new DLoader(C, "执行命令中...", "正在执行...", 30);
	  if (istoast)
		dloader.show();	
	  this.d_root = d_root;
	  }


	public void run()
	{	
	  si++;
	  di=si;
	  tfile=privpath+"/d_shell_"+di+".sh";
	  scripts = scripts.replace("\nsh\n", "").replace("\nsu\n", "");
	  FileTool.writeSDFile(scripts,tfile);
	  
	  Message r1_Message;
	 
	  Process r0_Process = null;
	  InputStream localInputStream1 = null ;
	  InputStream localInputStream2 = null ;
	  
	  InputStreamReader r3_InputStreamReader = null;
	  InputStreamReader r4_InputStreamReader = null;
	  try
	  {

		if (is_root)
		  r0_Process = Runtime.getRuntime().exec("su\n");
		else
		  r0_Process = Runtime.getRuntime().exec("sh\n");

		if (r0_Process == null)
		{
		  r1_Message = new Message();
		  r1_Message.obj = "\n执行出错了...请检测Root权限是否拥有 !";
		  r1_Message.what = -1;					
		  handler.sendMessage(r1_Message);

		}
		else
		{
		  OutputStream r3_OutputStream = r0_Process.getOutputStream();
		  r3_OutputStream.write(("sh "+tfile+"\nexit\n").getBytes());
		  //r3_OutputStream.write("\nexit\n".getBytes());
		  r3_OutputStream.flush();
		  r3_OutputStream.close();
		}
		r0_Process.waitFor();
		new File(tfile).delete();
		localInputStream1 = r0_Process.getErrorStream();
	    localInputStream2 = r0_Process.getInputStream();
		StringBuffer localStringBuffer = new StringBuffer();
		int i = localInputStream1.available();
		if (i > 0)
		{
		  byte[] arrayOfByte1 = new byte[i];
		  localInputStream1.read(arrayOfByte1);
		  localStringBuffer.append(new String(arrayOfByte1));
		}
		int j = localInputStream2.available();
		if (j > 0)
		{
		  byte[] arrayOfByte2 = new byte[j];
		  localInputStream2.read(arrayOfByte2);
		  localStringBuffer.append(new String(arrayOfByte2));
		}
		localInputStream1.close();
		localInputStream2.close();
		r0_Process.destroy();	
		msg(localStringBuffer.toString(), 1122);
		msg("\n命令执行完成....", 1122);
		
		/*
		r3_InputStreamReader = new InputStreamReader(r0_Process.getErrorStream());
		r4_InputStreamReader = new InputStreamReader(r0_Process.getInputStream());
		int i=0;
        while (true)
		{
          i++;
		  Log.e("Sh_line_2", i + "_:" + r2_StringBuffer.toString());
		  //pl.rline(r2_StringBuffer.toString());
		  int r0i = r3_InputStreamReader.read(r1_char_A);
		  if (r0i == -1)
		  {
			r3_InputStreamReader.close();
			while (true)
			{
			  r0i = r4_InputStreamReader.read(r1_char_A);
			  if (r0i == -1)
			  {
				r0_String = r2_StringBuffer.toString().trim();

				if (r0_String == "" || r0_String == null || r0_String.length() <= 1)
				{
				  r0_String = "木有结果..。┐（─__─）┌(汗哒哒..…)";
				  //pl.rline(r0_String);
				}
				msg(r0_String, 1122);
				msg("\n命令执行完成....", 1122);
				break;
			  }
			  else
			  {
				r2_StringBuffer.append(r1_char_A, 0, r0i);
				//	pl.rline(new String(r1_char_A, 0, r0i));
			  }

			}
			r4_InputStreamReader.close();
			break;
		  }
		  else
		  {
			r2_StringBuffer.append(r1_char_A, 0, r0i);
			//	pl.rline(new String(r1_char_A, 0, r0i));
		  }
		}*/

	  }
	  catch (Exception e)
	  {
		e.printStackTrace();

	  }
      finally
	  {
		try
		{
		  r3_InputStreamReader.close();
		}
		catch (Exception e)
		{

		}
		try
		{
		  r4_InputStreamReader.close();
		}
		catch (Exception e)
		{

		}
		try
		{
		  r0_Process.destroy();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
		Log.e(TAG, "dshell_finally");
	  }
	}




	public Handler handler = new Handler() {
	  @Override
	  public void handleMessage(Message msg)
	  {
		switch (msg.what)
		{
		  case 1121:
			dloader.append(msg.obj.toString().replace("\n", "\n\t") + "\n");	
			break;
		  case 1122:
			if (!dloader.isShowing())
			  dloader.Showagain();
			dloader.setTitle("命令执行完成...");
			dloader.setInfo("命令执行完成...");
			dloader.setPnos(100);			
			dloader.append("\t" + msg.obj.toString().replace("\n", "\n\t"));
			dloader.setPToast(false);
			break;
		  default:
			super.handleMessage(msg);
		}
	  }
	};

	//发送提示信息 带what
	public void msg(String str, int what)
	{
	  Message msg=new Message();
	  msg.obj = str;
	  msg.what = what;
	  handler.sendMessage(msg);	
	}

  }

  public static boolean checkRoot1()
  {
	is_root = Utils.isRoot();
	return is_root;
  }

  public synchronized static boolean checkRoot()
  {
	Process localProcess = null;
	OutputStream localOutputStream;
	InputStream localInputStream1 = null;
	InputStream localInputStream2 = null;
	
    try
    {
      localProcess = Runtime.getRuntime().exec("su");
      if (localProcess == null)
        return false;
	  localOutputStream  = localProcess.getOutputStream();
      localOutputStream.write("echo root\nexit\n".getBytes());
      localOutputStream.flush();
      localOutputStream.close();
      localProcess.waitFor();
      localInputStream1 = localProcess.getErrorStream();
      localInputStream2 = localProcess.getInputStream();
      StringBuffer localStringBuffer = new StringBuffer();
      int i = localInputStream1.available();
      if (i > 0)
      {
        byte[] arrayOfByte1 = new byte[i];
        localInputStream1.read(arrayOfByte1);
        localStringBuffer.append(new String(arrayOfByte1));
      }
      int j = localInputStream2.available();
      if (j > 0)
      {
        byte[] arrayOfByte2 = new byte[j];
        localInputStream2.read(arrayOfByte2);
        localStringBuffer.append(new String(arrayOfByte2));
      }
      localInputStream1.close();
      localInputStream2.close();
      localProcess.destroy();
	  int k = localStringBuffer.toString().indexOf("root");
      if (k > -1)
	  {
		is_root = true;
        return true;
	  }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
	finally{
	  try
	  {
		localInputStream1.close();
	  }
	  catch (Exception e)
	  {

	  }
	  try
	  {
		localInputStream2.close();
	  }
	  catch (Exception e)
	  {

	  }
	  try
	  {
		localProcess.destroy();
	  }
	  catch (Exception e)
	  {
		e.printStackTrace();
	  }
	  Log.e(TAG, "root_finally");
	  
	  
	}
    return false;
  }




  public class Shell extends Thread
  {
	private String cmd=null;
	private boolean istoast;
	private int ssi=0;
	private String tfile="";
	private putline pl=new putline(){

	  @Override
	  public void rline(String line)
	  {
		// TODO: Implement this method
		Log.e("rline_d", "rline:" + line);
	  }


	};

	public Shell(String cmd, boolean istoast)
	{
	  this.cmd = cmd;
	  this.istoast = istoast;
	}
	public Shell(String cmd, boolean istoast, putline pl)
	{
	  this.cmd = cmd;
	  this.istoast = istoast;
	  this.pl = pl;
	}


	public void run()
	{
	  si++;
	  ssi=si;
	  tfile=privpath+"/s_shell_"+ssi+".sh";
	  cmd = cmd.replace("\nsh\n", "").replace("\nsu\n", "");
	  FileTool.writeSDFile(cmd,tfile);
	  Message r1_Message;
	  
	  Process r0_Process = null;
	  InputStream localInputStream1 = null ;
	  InputStream localInputStream2 = null ;
	  try
	  {

		if (is_root)
		  r0_Process = Runtime.getRuntime().exec("su\n");
		else
		  r0_Process = Runtime.getRuntime().exec("sh\n");

		if (r0_Process == null)
		{
		  r1_Message = new Message();
		  r1_Message.obj = "\n执行出错了...请检测Root权限是否拥有 !";
		  pl.rline((String)r1_Message.obj);
		  r1_Message.what = -1;					
		  handler.sendMessage(r1_Message);

		}
		else
		{
		  OutputStream r3_OutputStream = r0_Process.getOutputStream();
		  r3_OutputStream.write(("sh "+tfile+"\nexit\n").getBytes());
		  r3_OutputStream.close();
		  r3_OutputStream.flush();
		}
		r0_Process.waitFor();
		new File(tfile).delete();
		localInputStream1 = r0_Process.getErrorStream();
	    localInputStream2 = r0_Process.getInputStream();
		StringBuffer localStringBuffer = new StringBuffer();
		int i = localInputStream1.available();
		if (i > 0)
		{
		  byte[] arrayOfByte1 = new byte[i];
		  localInputStream1.read(arrayOfByte1);
		  localStringBuffer.append(new String(arrayOfByte1));
		}
		int j = localInputStream2.available();
		if (j > 0)
		{
		  byte[] arrayOfByte2 = new byte[j];
		  localInputStream2.read(arrayOfByte2);
		  localStringBuffer.append(new String(arrayOfByte2));
		}
		localInputStream1.close();
		localInputStream2.close();
		r0_Process.destroy();

		if (istoast)
		{
		  r1_Message = new Message();
		  r1_Message.obj = localStringBuffer;
		  r1_Message.what = 1;
		  handler.sendMessage(r1_Message);
		}
		
		pl.rline(localStringBuffer.toString());


	  }
	  catch (Exception e)
	  {
		e.printStackTrace();

	  }
	  finally
	  {
		try
		{
		  localInputStream1.close();
		}
		catch (Exception e)
		{}
		try
		{
		  localInputStream2.close();
		}
		catch (Exception e)
		{}
		try
		{
		  r0_Process.destroy();
		}
		catch (Exception e)
		{

		}
		Log.e(TAG, "shell_finally");
	  }

	}


  }
  public Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg)
	{
	  switch (msg.what)
	  {
		case -1:
		  Toast.makeText(C, "错误信息:" + msg.obj.toString(), Toast.LENGTH_SHORT).show();	 
		  break;

		case 0:
		  msg.obj = msg.obj.toString().replaceAll("<font color='#ff0000'>", "").replaceAll("</font>", "");
		  // msg.obj=HtmlUtil.getTextFromHtml(msg.obj.toString());
		  //msg.obj=msg.obj.toString().replaceAll("<font color='#ff0000'>","");
		  Toast.makeText(C, msg.obj.toString(), Toast.LENGTH_SHORT).show();      
		  break;
		case 1: 
		  msg.obj = msg.obj.toString().replaceAll("<font color='#ff0000'>", "").replaceAll("</font>", "");
		  Toast.makeText(C, "执行结果:" + msg.obj.toString(), Toast.LENGTH_SHORT).show();      
		  break;
		case 1121:

		  break;
		case 1122:

		  break;
		default:
		  super.handleMessage(msg);
	  }
	}
  };
  public String msgr(String str)
  {		
	Message r1_Message = new Message();
	r1_Message.obj = str;
	r1_Message.what = 0;
	handler.sendMessage(r1_Message);		
	return str;
  }
  public String msgr(String str, int what)
  {		
	Message r1_Message = new Message();
	r1_Message.obj = str;
	r1_Message.what = what;
	handler.sendMessage(r1_Message);		
	return str;
  }

  public void msg(String str)
  {		
	Message r1_Message = new Message();
	r1_Message.obj = str;
	r1_Message.what = 0;
	handler.sendMessage(r1_Message);		
  }
  //发送提示信息 带what
  public void msg(String str, int what)
  {
	Message msg=new Message();
	msg.obj = str;
	msg.what = what;
	handler.sendMessage(msg);	
  }
  //com.tencent.mobileqq


  public static int[] getuid(Context con)
  {
	int i=0;
	int[] uid=new int[3];
	String[] pack={
	  con.getPackageName(),
	  "com.tencent.mobileqq",
	  "com.tencent.qqlite"
	};
	try
	{
	  PackageManager pm = con.getPackageManager();
	  for (i = 0;i < uid.length;i++)
	  {
		ApplicationInfo ai = pm.getApplicationInfo(pack[i], PackageManager.GET_ACTIVITIES);
		if (ai != null)
		  uid[i] = ai.uid;
		else
		  uid[i] = -1;
	  }
	}
	catch (Exception e)
	{
	  e.printStackTrace();
	  uid[i] = -1;
	}
	return uid;
  }

  public static String replace_code(Context con, String str, SharedPreferences sp)
  {
	int uid[]=getuid(con);
	String code[]={"p_ip","p_port","localport","p_host1","p_host2","myuid","qquid","qqluid"};
	String value[]={	
	  sp.getString("p_ip", "10.0.0.172"),
	  sp.getString("p_port", "80"),
	  sp.getString("localport", "8787"),
	  sp.getString("p_host1", "byml.net"),
	  sp.getString("p_host2", "byml.net"),
	  "" + uid[0],
	  "" + uid[1],
	  "" + uid[2]
	};
	for (int i=0;i < code.length;i++)
	{
	  //Log.e("HaP",value[i]);
      str = str.replace("[" + code[i] + "]", value[i]);
	  //Log.e("HaP", str.replaceAll("" + code[i] + "", value[i]));
	}
	return str;
  }
  public static String replace_str(String code[], String value[], String str)
  {
	for (int i=0;i < code.length;i++)
	{
	  str = str.replaceAll("[" + code[i] + "]", value[i]);
	}
	return str;
  }

}
