package com.HaP.Tool;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.HaP.Byml.*;
import com.HaP.View.*;
import java.io.*;
import java.net.*;

public class DownThread extends AsyncTask<String, Integer, String>
{
  private Context con;
  private String url,file_path,file_name,t;
  private DLoader dloader;
  private SuperUser Su;
  

  private String TAG="HaP_downT";

  private EditText et1;
  public DownThread(Context con, SuperUser Su,String url, String file_path, String file_name)
  {
	this.con = con;
	this.Su = Su;
	this.url = url;
	this.file_path = file_path;
	this.file_name = file_name;

  }
  //onPreExecute方法用于在执行后台任务前做一些UI操作
  @Override
  protected void onPreExecute()
  {
	dloader = new DLoader(con, "正在开始下载...", "正在下载...", 0);	
	dloader.show();
	dloader.setPnos(5);
	dloader.append("请求网址:"+url+"\n");
  }

  @Override
  protected synchronized String doInBackground(String[] p1)
  {
	InputStream in = null;
	FileOutputStream fos=null;
	int file_len=0,now_len=0;
	try
	{
	  URL url  = new URL(this.url);
	  HttpURLConnection httpURLconnection = (HttpURLConnection) url
		.openConnection();
	  httpURLconnection.setDoInput(true);
	  httpURLconnection.connect();
	  if (httpURLconnection.getResponseCode() == 200)
	  {
		in = httpURLconnection.getInputStream();
		file_len=httpURLconnection.getContentLength();
		t="\t状态码正常，开始下载.....\n\tHaP核心包大小:"+ String.format("%.2f",(float)file_len/(1024*1024))+"M\n";
		publishProgress(1314);
		File hap=new File(file_path+"/"+file_name);
		hap.delete();
		fos=new FileOutputStream(hap);
		byte[] buffer = new byte[4096];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
		  fos.write(buffer, 0, len);
		  now_len+=len;
		  publishProgress(now_len*100/file_len);
		
		}
		fos.close();
		in.close();
		if(!FileTool.DeleteFile(file_path+"/HaP"))
		  Log.e(TAG,"删除/HaP文件失败，>"); 
		ZipUtil.unZipFiles(file_path+"/"+file_name,file_path+"/");		
	  }
	  else
	  {
		Log
		  .e(TAG, "下载失败，状态码是："
			 + httpURLconnection.getResponseCode()+"\n网址:"+this.url);
		t="下载失败，状态码异常:"+httpURLconnection.getResponseCode()+"\n网址:"+this.url+"\n"; 
		publishProgress(1314);
		return "";
	  }
	  return "ok";
	  
	}
	catch (Exception e)
	{
	  Log.e(TAG, "下载失败，原因是：" + e.toString());
	  t="下载失败，异常信息:"+e.toString()+"\n";
	  publishProgress(1314);
	  e.printStackTrace();
	}
	finally
	{
	  if (in != null)
	  {
		try
		{
		  in.close();
		}
		catch (IOException e)
		{
		  e.printStackTrace();
		}
	  }
	  if (fos != null)
	  {
		try
		{
		  fos.close();
		}
		catch (IOException e)
		{
		  e.printStackTrace();
		}
	  }
	  
	}
	return "";
  }
  //onProgressUpdate方法用于更新进度信息
  @Override
  protected void onProgressUpdate(Integer... progresses)
  {
	if(progresses[0]!=1314)
	{
	  dloader.setPnos(progresses[0]);
	}else{
	  dloader.append(t);
	}
  }

  //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
  @Override
  protected void onPostExecute(String result)
  {
	if(!dloader.isShowing())
	  dloader.Showagain();
	if(result.equals("ok"))
	{
	  dloader.append("下载完成~是否进行安装？");
	  dloader.setInfo("下载完成 ");
	  dloader.setPnos(100);
	  
	  et1 = new EditText(con);
	  et1.setHint("本地安装目录");
	  et1.setText(file_path + "/HaP");
	  
	  new AlertDialog.Builder(con).setTitle("本地安装").setView(et1).setMessage("注意:注意区分下载的是Android5.0的核心包还是最新核心\n最新核心不支持5.0用户，以免出现异常导致无法使用").setIcon(
		android.R.drawable.ic_dialog_info)
		.setPositiveButton("关闭" , null)
		.setNegativeButton("安装" , new DialogInterface.OnClickListener(){

		  @Override
		  public void onClick(DialogInterface p1, int p2)
		  {
			// TODO: Implement this method
			new HPInstall(con,Su, new Handler(), con.getFilesDir().getPath(), et1.getText().toString()).start();										
		  }
		  		  		  
		}).show();						
	 
	}else{
	  dloader.append("下载失败~请检查网络是否正常");
	  dloader.setInfo("下载失败  ");
	  dloader.setPnos(100);  
	}
	dloader.setPToast(false);
	
  }


}
