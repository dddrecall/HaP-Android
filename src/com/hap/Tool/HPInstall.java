package com.HaP.Tool;
import android.content.*;
import java.io.*;
import com.HaP.Byml.*;
import android.os.*;
import android.widget.*;

public class HPInstall extends Thread
{
	private Context C;
	private SuperUser Su;
	
	private Handler hl;
	private String Install_Path,File_Path;
  
	public HPInstall(Context Con, SuperUser Su, Handler hl,String Install_Path, String File_Path)
	{
		this.C = Con;
		this.Su = Su;
		this.hl=hl;
		this.Install_Path = Install_Path;
		this.File_Path = File_Path;
		
		
	}
	public void run(){
	  File mode=new File(Install_Path + "/mode");
	  if (!mode.exists())
		try{
		  if (!mode.mkdirs())
		  {
			Su.msg("无法创建Mode文件夹 任务终止 ！", -1);
			return ;
		  }
		}catch (SecurityException Se){
		  Su.msg("无法创建Mode文件夹 任务终止 ！具体信息:" + Se.toString(), -1);
		  return ;					
		}
  
		String shell="echo '正在执行安装前操作，下面的错误提示可忽略.\n'\n";
		shell += "chmod 0777 " + Install_Path + "\n";
		shell +="cd " + Install_Path+ "\n./H-stop ./haproxy.pid\n";
		shell +="sh ./stop.sh\n";
		shell +="chmod 0777 "+Install_Path+"/mode \n";
		Su.Stmp = Su.Shell(shell, false);
		Su.Stmp.start();
		try
		{
			Su.Stmp.join(3000);
		}
		catch (InterruptedException e) {}
	    shell="";
		
		if (File_Path.equals("assets"))
		{
			try
			{
				FileTool.CopyFileFromAssets(C, "HaP", Install_Path, "HaP");
				FileTool.CopyFileFromAssets(C, "start.sh", Install_Path, "start.sh");
				FileTool.CopyFileFromAssets(C, "stop.sh", Install_Path, "stop.sh");
				FileTool.CopyFileFromAssets(C, "status.sh", Install_Path, "status.sh");
			    shell += "chmod 0777 " + Install_Path + "/start.sh\n";
			    shell += "chmod 0777 " + Install_Path + "/stop.sh\n";
			    shell += "chmod 0777 " + Install_Path + "/status.sh\n";
			  
			    //	FileTool.CopyFileFromAssets(C, "H-delete", Install_Path, "H-delete");
		
			    //	FileTool.CopyFileFromAssets(C, "t.cfg", Install_Path, "t.cfg");

				FileTool.CopyFileFromAssets(C, "global.hp", Install_Path + "/mode", "global.hp");
				FileTool.CopyFileFromAssets(C, "defaults.hp", Install_Path + "/mode", "defaults.hp");
				FileTool.CopyFileFromAssets(C, "frontend.hp", Install_Path + "/mode", "frontend.hp");
				FileTool.CopyFileFromAssets(C, "get.hp", Install_Path + "/mode", "get.hp");
				FileTool.CopyFileFromAssets(C, "post.hp", Install_Path + "/mode", "post.hp");
				FileTool.CopyFileFromAssets(C, "connection.hp", Install_Path + "/mode", "connection.hp");
				FileTool.CopyFileFromAssets(C, "pstart.sh", Install_Path + "/mode", "pstart.sh");
				FileTool.CopyFileFromAssets(C, "pstop.sh", Install_Path + "/mode", "pstop.sh");
				FileTool.CopyFileFromAssets(C, "pstatus.sh", Install_Path + "/mode", "pstatus.sh");

				
				
			}
			catch (IOException e) {
				Su.msg(e.toString(), -1);	
				return ;
			}
		}else{
			try
			{
				FileTool.CopyFile(File_Path + "/HaP", Install_Path + "/HaP");
			//	FileTool.CopyFile(File_Path + "/H-start", Install_Path + "/H-start");				
			//	FileTool.CopyFile(File_Path + "/H-stop", Install_Path + "/H-stop");
			//	FileTool.CopyFile(File_Path + "/H-status", Install_Path + "/H-status");
			//	FileTool.CopyFile(File_Path + "/H-delete", Install_Path + "/H-delete");
		   /*
	      
		       try{
					FileTool.CopyFile(File_Path + "/t.cfg", Install_Path + "/t.cfg");
				}catch (Exception e){
					Su.msg("复制t.cfg出现错误，可忽略.，以自动从Apk提取文件....\n具体错误信息:" + e.toString(), -1);
					FileTool.CopyFileFromAssets(C, "t.cfg", Install_Path, "t.cfg");
				}
	  
				FileTool.CopyFile(File_Path + "/global.hp", Install_Path + "/mode/global.hp");
				FileTool.CopyFile(File_Path + "/defaults.hp", Install_Path + "/mode/defaults.hp");
				FileTool.CopyFile(File_Path + "/forntend.hp", Install_Path + "/mode/frontend.hp");
				FileTool.CopyFile(File_Path + "/get.hp", Install_Path + "/mode/get.hp");
				FileTool.CopyFile(File_Path + "/post.hp", Install_Path + "/mode/post.hp");
				FileTool.CopyFile(File_Path + "/connection.hp", Install_Path + "/mode/connection.hp");
				//FileTool.CopyFile(File_Path + "", Install_Path + "");
				*/
				
			  FileTool.CopyFileFromAssets(C, "start.sh", Install_Path, "start.sh");
			  FileTool.CopyFileFromAssets(C, "stop.sh", Install_Path, "stop.sh");
			  FileTool.CopyFileFromAssets(C, "status.sh", Install_Path, "status.sh");
			  shell += "chmod 0777 " + Install_Path + "/start.sh\n";
			  shell += "chmod 0777 " + Install_Path + "/stop.sh\n";
			  shell += "chmod 0777 " + Install_Path + "/status.sh\n";

			  //	FileTool.CopyFileFromAssets(C, "H-delete", Install_Path, "H-delete");

			  //	FileTool.CopyFileFromAssets(C, "t.cfg", Install_Path, "t.cfg");

			  FileTool.CopyFileFromAssets(C, "global.hp", Install_Path + "/mode", "global.hp");
			  FileTool.CopyFileFromAssets(C, "defaults.hp", Install_Path + "/mode", "defaults.hp");
			  FileTool.CopyFileFromAssets(C, "frontend.hp", Install_Path + "/mode", "frontend.hp");
			  FileTool.CopyFileFromAssets(C, "get.hp", Install_Path + "/mode", "get.hp");
			  FileTool.CopyFileFromAssets(C, "post.hp", Install_Path + "/mode", "post.hp");
			  FileTool.CopyFileFromAssets(C, "connection.hp", Install_Path + "/mode", "connection.hp");
			  FileTool.CopyFileFromAssets(C, "pstart.sh", Install_Path + "/mode", "pstart.sh");
			  FileTool.CopyFileFromAssets(C, "pstop.sh", Install_Path + "/mode", "pstop.sh");
			  FileTool.CopyFileFromAssets(C, "pstatus.sh", Install_Path + "/mode", "pstatus.sh");
			  
				
			}
			catch (IOException e) {
				Su.msg(e.toString(), -1);
			}				
		}

		Su.msg("开始执行命令 ....!\n每次更新前都会关闭HaP核心，然后更新 !");
		
		//shell = "";
		shell += "chmod 777 " + Install_Path + "/HaP\n";
		//shell += "chmod 777 " + Install_Path + "/H-start\n";
		//shell += "chmod 777 " + Install_Path + "/H-stop\n";
		//shell += "chmod 777 " + Install_Path + "/H-status\n";
	    //shell += "chmod 777 " + Install_Path + "/H-delete\n";
    
		shell += "echo '文件权限设置命令执行完成.'\n";
		Su.Stmp = Su.Shell(shell, true);
		Su.Stmp.start();
		try
		{
			Su.Stmp.join();
		}
		catch (InterruptedException e) {}



		if (new File(Install_Path+"/HaP").exists())
		{
			try
			{
				new File(Install_Path + "/" +SuperUser.version).createNewFile();
				MainActivity.sp.edit().putBoolean("install_"+SuperUser.version, true).commit();
				try{				
					if(new File(Install_Path + "/" + "默认透明代理").mkdirs())
					{
					  try
					  {
						FileTool.CopyFile(Install_Path+ "/mode/get.hp", Install_Path+ "/默认透明代理/get.hp");
						FileTool.CopyFile(Install_Path + "/mode/post.hp", Install_Path+"/默认透明代理/post.hp");
						FileTool.CopyFile(Install_Path + "/mode/connection.hp", Install_Path+ "/默认透明代理/connection.hp");
						Message r1_Message = new Message();
						r1_Message.obj = "";
						r1_Message.what = 1;
						hl.sendMessage(r1_Message);		
					  }
					  catch (Exception e) {
						Su.msg("创建默认模式失败，详细信息_install:" + e.toString(), -1);
						return ;
					  }
					  
					  
					}
				}catch (Exception e){
					Su.msg("无法创建默认模式，请检查私有目录是否有可写权限！\n详细信息_install:" + e.toString(), -1);
					return ;
				}
				
							
			}
			catch (Exception e)
			{
				//Toast.makeText(mCont,"卧槽(#ﾟДﾟ)坑爹啊，创建记录文件失败了!\n看看是啥错误:"+e.toString(),Toast.LENGTH_LONG).show();
				Su.msg("卧槽(#ﾟДﾟ)坑爹啊，创建记录文件失败了!\n看看是啥错误:" + e.toString());
				e.printStackTrace();
				Su.msg("记录文件创建失败!请检查软件是否安装成功 ……");
				return ;
			}		
			Su.msg("安装完成 !文件以全部解压 ……");
		}
		else
			Su.msg("没有检测到程序成功安装后的文件，请检查/data是否有读写权限并且>5M\n欢迎去八云官网:http://byml.net 反馈\n");
		
		
	}


}
