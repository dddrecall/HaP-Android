package com.HaP.Byml.FileBrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.KeyEvent;
import com.HaP.Byml.*;


public class FileMain extends ListActivity
{
 private TextView _filePath;
 private List<FileInfo> _files = new ArrayList<FileInfo>();
 private String _rootPath = FileUtil.getSDPath();
 private String _currentPath = _rootPath;
 private final String TAG = "Main";
 private final int MENU_RENAME = Menu.FIRST;
 private final int MENU_COPY = Menu.FIRST + 3;
 private final int MENU_MOVE = Menu.FIRST + 4;
 private final int MENU_DELETE = Menu.FIRST + 5;
 private final int MENU_INFO = Menu.FIRST + 6;
 private BaseAdapter adapter = null;

 /** Called when the activity is first created. */
 @Override
 public void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);

  setContentView(R.layout.file_main);

  _filePath = (TextView) findViewById(R.id.file_path);

  // 绑定长按事件
  // getListView().setOnItemLongClickListener(_onItemLongClickListener);

  // 注册上下文菜单
  registerForContextMenu(getListView());

  // 绑定数据
  adapter = new FileAdapter(this, _files);
  setListAdapter(adapter);

  // 获取当前目录的文件列表
  Intent i=this.getIntent();
  String IPath=i.getStringExtra("IPath");
  if (IPath != null)
  {
   _currentPath = IPath;
  }
  setTheme(android.R.style.Animation_Dialog);
  viewFiles(_currentPath);
 }

 /** 上下文菜单 **/
 @Override
 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
 {
  super.onCreateContextMenu(menu, v, menuInfo);

  AdapterView.AdapterContextMenuInfo info = null;

  try
  {
   info = (AdapterView.AdapterContextMenuInfo) menuInfo;
  }
  catch (ClassCastException e)
  {
   Log.e(TAG, "bad menuInfo", e);
   return;
  }

  FileInfo f = _files.get(info.position);
  menu.setHeaderTitle(f.Name);
  menu.add(0, MENU_RENAME, 1, getString(R.string.file_rename));
  menu.add(0, MENU_COPY, 2, getString(R.string.file_copy));
  menu.add(0, MENU_MOVE, 3, getString(R.string.file_move));
  menu.add(0, MENU_DELETE, 4, getString(R.string.file_delete));
  menu.add(0, MENU_INFO, 5, getString(R.string.file_info));
 }

 /** 上下文菜单事件处理 **/
 public boolean onContextItemSelected(MenuItem item)
 {
  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
  FileInfo fileInfo = _files.get(info.position);
  File f = new File(fileInfo.Path);
  switch (item.getItemId())
  {
   case MENU_RENAME:
	FileActivityHelper.renameFile(FileMain.this, f, renameFileHandler);
	return true;
   case MENU_COPY:
	pasteFile(f.getPath(), "COPY");
	return true;
   case MENU_MOVE:
	pasteFile(f.getPath(), "MOVE");
	return true;
   case MENU_DELETE:
	FileUtil.deleteFile(f);
	viewFiles(_currentPath);
	return true;
   case MENU_INFO:
	FileActivityHelper.viewFileInfo(FileMain.this, f);
	return true;
   default:
	return super.onContextItemSelected(item);
  }
 }

 /** 行被点击事件处理 **/
 @Override
 protected void onListItemClick(ListView l, View v, int position, long id)
 {
  final FileInfo f = _files.get(position);

  if (f.IsDirectory)
  {
   viewFiles(f.Path);
  }
  else
  {

   new AlertDialog.Builder(FileMain.this).setTitle(f.Name).setMessage("导入文件:" + f.Name + "\n仅支持.mhp后戳文件的模式文件")
	.setPositiveButton("选择", new DialogInterface.OnClickListener() {
	 @Override
	 public void onClick(DialogInterface dialog, int which)
	 {
	  Intent intent = new Intent();
	
	  intent.putExtra("File_Path",f.Path);   
	  setResult(21, intent);//返回参数
	  finish();//关闭Activity
	  
	 }
	}).setNegativeButton("打开", new DialogInterface.OnClickListener() {
	 @Override
	 public void onClick(DialogInterface dialog, int which)
	 {
	  openFile(f.Path);
//	  dialog.cancel();
	 }
	}).show();


  }
 }

 /** 重定义返回键事件 **/
 @Override
 public boolean onKeyDown(int keyCode, KeyEvent event)
 {
  // 拦截back按键
  if (keyCode == KeyEvent.KEYCODE_BACK)
  {
   File f = new File(_currentPath);
   String parentPath = f.getParent();
   if (parentPath != null)
   {
	viewFiles(parentPath);
   }
   else
   {
	exit();
   }
   return true;
  }
  return super.onKeyDown(keyCode, event);
 }

 /** 获取从PasteFile传递过来的路径 **/
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data)
 {
  if (Activity.RESULT_OK == resultCode)
  {
   Bundle bundle = data.getExtras();
   if (bundle != null && bundle.containsKey("CURRENTPATH"))
   {
	viewFiles(bundle.getString("CURRENTPATH"));
   }
  }
 }

 /** 创建菜单 **/
 public boolean onCreateOptionsMenu(Menu menu)
 {
  MenuInflater inflater = this.getMenuInflater();
  inflater.inflate(R.menu.main_menu, menu);
  return true;
 }

 /** 菜单事件 **/
 public boolean onOptionsItemSelected(MenuItem item)
 {
  switch (item.getItemId())
  {
   case R.id.mainmenu_home:
	viewFiles(_rootPath);
	break;
   case R.id.mainmenu_refresh:
	viewFiles(_currentPath);
	break;
   case R.id.mainmenu_createdir:
	FileActivityHelper.createDir(FileMain.this, _currentPath, createDirHandler);
	break;
   case R.id.mainmenu_exit:
	exit();
	break;
   default:
	break;
  }
  return true;
 }

 /** 获取该目录下所有文件 **/
 private void viewFiles(String filePath)
 {
  ArrayList<FileInfo> tmp = FileActivityHelper.getFiles(FileMain.this, filePath);
  if (tmp != null)
  {
   // 清空数据
   _files.clear();
   _files.addAll(tmp);
   tmp.clear();

   // 设置当前目录
   _currentPath = filePath;
   _filePath.setText(filePath);

   // this.onContentChanged();
   adapter.notifyDataSetChanged();
  }
 }

 /** 长按事件处理 **/
 /**
  * private OnItemLongClickListener _onItemLongClickListener = new
  * OnItemLongClickListener() {
  * 
  * @Override public boolean onItemLongClick(AdapterView<?> parent, View
  *           view, int position, long id) { Log.e(TAG, "position:" +
  *           position); return true; } };
  **/

 /** 打开文件 **/
 private void openFile(String path)
 {
  Intent intent = new Intent();
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  intent.setAction(android.content.Intent.ACTION_VIEW);

  File f = new File(path);
  String type = FileUtil.getMIMEType(f.getName());
  intent.setDataAndType(Uri.fromFile(f), type);
  startActivity(intent);
 }

 /** 重命名回调委托 **/
 private final Handler renameFileHandler = new Handler() {
  @Override
  public void handleMessage(Message msg)
  {
   if (msg.what == 0)
	viewFiles(_currentPath);
  }
 };

 /** 创建文件夹回调委托 **/
 private final Handler createDirHandler = new Handler() {
  @Override
  public void handleMessage(Message msg)
  {
   if (msg.what == 0)
	viewFiles(_currentPath);
  }
 };

 /** 粘贴文件 **/
 private void pasteFile(String path, String action)
 {
  Intent intent = new Intent();
  Bundle bundle = new Bundle();
  bundle.putString("CURRENTPASTEFILEPATH", path);
  bundle.putString("ACTION", action);
  intent.putExtras(bundle);
  intent.setClass(FileMain.this, FilePasteFile.class);
  // 打开一个Activity并等待结果
  startActivityForResult(intent, 0);
 }

 /** 退出程序 **/
 private void exit()
 {

  new AlertDialog.Builder(FileMain.this).setMessage(R.string.confirm_exit).setCancelable(false)
   .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
	 Intent intent = new Intent();
	 intent.putExtra("File_Path",_currentPath);   
	 setResult(2, intent);//返回参数
	 finish();//关闭Activity
	 
	 FileMain.this.finish();
	}
   }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
	 dialog.cancel();
	}
   }).show();
 }
}
