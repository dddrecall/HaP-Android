package com.HaP.Tool;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipUtil
{
  /**
   * 压缩文件-由于out要在递归调用外,所以封装一个方法用来
   * 调用ZipFiles(ZipOutputStream out,String path,File... srcFiles)
   * @param zip
   * @param path
   * @param srcFiles
   * @throws IOException
   * @author isea533
   */
  public static void ZipFiles(File zip,String path,File... srcFiles) throws IOException{
	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
	ZipFiles(out,path,srcFiles);
	out.close();
	System.out.println("*****************压缩完毕*******************");
  }
  /**
   * 压缩文件-File
   * @param zipFile  zip文件
   * @param srcFiles 被压缩源文件
   * @author isea533
   */
  public static void ZipFiles(ZipOutputStream out,String path,File... srcFiles){
	path = path.replaceAll("\\*", "/");
	if(!path.endsWith("/")){
	  path+="/";
	}
	byte[] buf = new byte[1024];
	try {
	  for(int i=0;i<srcFiles.length;i++){
		if(srcFiles[i].isDirectory()){
		  File[] files = srcFiles[i].listFiles();
		  String srcPath = srcFiles[i].getName();
		  srcPath = srcPath.replaceAll("\\*", "/");
		  if(!srcPath.endsWith("/")){
			srcPath+="/";
		  }
		  out.putNextEntry(new ZipEntry(path+srcPath));
		  ZipFiles(out,path+srcPath,files);
		}
		else{
		  FileInputStream in = new FileInputStream(srcFiles[i]);
		  System.out.println(path + srcFiles[i].getName());
		  out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
		  int len;
		  while((len=in.read(buf))>0){
			out.write(buf,0,len);
		  }
		  out.closeEntry();
		  in.close();
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }
  /**
   * 解压到指定目录
   * @param zipPath
   * @param descDir
   * @author isea533
   */
  public static void unZipFiles(String zipPath,String descDir)throws IOException{
	unZipFiles(new File(zipPath), descDir);
  }
  /**
   * 解压文件到指定目录
   * @param zipFile
   * @param descDir
   * @author isea533
   */
  @SuppressWarnings("rawtypes")
  public static void unZipFiles(File zipFile,String descDir)throws IOException{
	File pathFile = new File(descDir);
	if(!pathFile.exists()){
	  pathFile.mkdirs();
	}
	ZipFile zip = new ZipFile(zipFile);
	
	for(Enumeration entries = zip.entries();entries.hasMoreElements();){
	  ZipEntry entry = (ZipEntry)entries.nextElement();
	  String zipEntryName = entry.getName();
	  InputStream in = zip.getInputStream(entry);
	  String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
	  //判断路径是否存在,不存在则创建文件路径
	  File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
	  if(!file.exists()){
		file.mkdirs();
	  }
	  //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
	  if(new File(outPath).isDirectory()){
		continue;
	  }
	  //输出文件路径信息
	  System.out.println(outPath);

	  OutputStream out = new FileOutputStream(outPath);
	  byte[] buf1 = new byte[1024];
	  int len;
	  while((len=in.read(buf1))>0){
		out.write(buf1,0,len);
	  }
	  in.close();
	  out.close();
	}
	System.out.println("******************解压完毕********************");
  }
  
}
