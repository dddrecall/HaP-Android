package com.HaP.Tool;

import android.content.*;
import android.os.*;
import java.io.*;

public class FileTool
{

  private Context context;
  /** SD卡是否存在**/
  private boolean hasSD = false;
  /** SD卡的路径**/
  private String SDPATH;
  /** 当前程序包的路径**/
  private String FILESPATH;
  public FileTool(Context context)
  {
	this.context = context;
	hasSD = Environment.getExternalStorageState().equals(
	  android.os.Environment.MEDIA_MOUNTED);
	SDPATH = Environment.getExternalStorageDirectory().getPath();
	FILESPATH = this.context.getFilesDir().getPath();
  }

  /**
   * 在SD卡上创建文件
   *
   * @throws IOException
   */
  public File createSDFile(String fileName) throws IOException
  {
	File file = new File(fileName);
	if (!file.exists())
	{
	  file.createNewFile();
	}
	return file;
  }

  /**
   * 删除SD卡上的文件
   *
   * @param fileName
   */
  public static boolean deleteSDFile(String fileName)
  {
	File file = new File(fileName);
	if (file == null || !file.exists())
	  return false;
	if (file.isDirectory())
	{
	  for (File f:file.listFiles())
		f.delete();
	}
	return file.delete();
  }
  /**
   * 删除SD卡上的文件
   *
   * @param fileName
   */
  public static boolean DeleteFile(String fileName)
  {
	File file = new File(fileName);
	if (file == null || !file.exists())
	  return false;
	if (file.isDirectory())
	{
	  for (File f:file.listFiles())
		f.delete();
	}
	return file.delete();
  }
  

  //要复制的目录下的所有非子目录(文件夹)文件拷贝
  public static boolean CopyFile(String fromFile, String toFile) throws FileNotFoundException, IOException
  {
	InputStream fosfrom = new FileInputStream(fromFile);
	OutputStream fosto = new FileOutputStream(toFile);
	byte bt[] = new byte[1024];
	int c;
	while ((c = fosfrom.read(bt)) > 0) 
	{
	  fosto.write(bt, 0, c);
	}
	fosfrom.close();
	fosto.close();
	return true;

  }


  /**
   * 写入内容到SD卡中的txt文本中
   * str为内容
   */
  public static boolean writeSDFile(String str, String fileName)
  {
	try
	{
	  FileWriter fw = new FileWriter(fileName);
	  File f = new File(fileName);
	  fw.write(str);
	  fw.close();
	  /*
	   FileOutputStream os = new FileOutputStream(f);
	   DataOutputStream out = new DataOutputStream(os);
	   out.writeShort(2);
	   out.writeUTF("");
	   fw.flush();
	   fw.close();
	   os.close();
	   out.close();*/
	}
	catch (Exception e)
	{
	  return false;
	}
	return true;
  }

  /**
   * 读取SD卡中文本文件
   *
   * @param fileName
   * @return
   */

  public static String readSDFile(String fileName)
  {
	InputStream is = null;
	String buf=null;
	try
	{
	  is = new FileInputStream(fileName);
	  int length = is.available();
	  byte[] outBuf = new byte[length];
	  is.read(outBuf, 0, length);
	  buf = new String(outBuf, "utf8");
	  is.close();

	}
	catch (FileNotFoundException e)
	{
	  e.printStackTrace();
	  return "\n" + fileName + ":" + e.toString();
	}
	catch (IOException e)
	{
	  e.printStackTrace();
	  return "\n" + fileName + ":" + e.toString();
	}     
	return buf;
  }

  
  public static void CopyFileFromAssets(Context myContext, String ASSETS_NAME,
										String savePath, String saveName) throws FileNotFoundException, IOException
  {
	String filename = savePath + "/" + saveName; 
	File dir = new File(savePath);
	// 如果目录不中存在，创建这个目录
	if (!dir.exists())
	  dir.mkdirs();

	if (new File(filename).exists())
	{
	  new File(filename).delete();
	}
    InputStream is = myContext.getResources().getAssets()
	  .open(ASSETS_NAME);
    FileOutputStream fos = new FileOutputStream(filename);
    byte[] buffer = new byte[7168];
    int count = 0;
    while ((count = is.read(buffer)) > 0)
	{
	  fos.write(buffer, 0, count);
    }
    fos.close();
    is.close();


  }


  public String getFILESPATH()
  {
	return FILESPATH;
  }
  public String getSDPATH()
  {
	if (hasSD)
	  return SDPATH;
	else
	  return "没有SD卡！";
  }
  public boolean hasSD()
  {
	return hasSD;
  }



}
