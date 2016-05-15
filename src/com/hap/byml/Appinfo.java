package com.HaP.Byml;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.HaP.View.*;
import java.util.*;

import android.view.View.OnCreateContextMenuListener;

public class Appinfo extends Activity
{

  private ListView listView;   

  private ListViewAdapter listViewAdapter;   
  private List<Map<String, Object>> listItems;   

  private int posid;

  private ClipboardManager clipboardManager ;

  private load_info l_t;
  // private List<String> data = new ArrayList<String>();	    
  @Override   
  public void onCreate(Bundle savedInstanceState)
  {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.listview);   
	listView = (ListView)findViewById(R.id.listviewListView1);   
	listItems = new ArrayList<Map<String, Object>>();
	listViewAdapter = new ListViewAdapter(this, listItems); //创建适配器   
	listView.setAdapter(listViewAdapter);   
	listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long id)
		{
		  Toast.makeText(Appinfo.this,
						 "已经复制:" + listItems.get(position).get("title").toString() + "的UID",
						 Toast.LENGTH_SHORT).show();

		  ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  
		  clipboardManager.setPrimaryClip(ClipData.newPlainText(null, listItems.get(position).get("uid").toString()));  
		  if (clipboardManager.hasPrimaryClip())
		  {  
			clipboardManager.getPrimaryClip().getItemAt(0).getText();  
		  }  
		}
	  });

	listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

		public void onCreateContextMenu(ContextMenu menu, View v,
										ContextMenuInfo menuInfo)
		{
		  menu.add(0, 0, 0, "复制应用名");
		  menu.add(0, 1, 0, "复制应用包名");
		  menu.add(0, 2, 0, "复制应用UID");
		  AdapterContextMenuInfo menuinfo2 = (AdapterContextMenuInfo) menuInfo;

		  posid = menuinfo2.position;//这个就是要获得的item的position

		}
	  });
	l_t = new load_info();
	l_t.start();
  }		



  private class load_info extends Thread
  {
	public void run()
	{
	  LoadListItems();
	}
  }

  // 长按菜单响应函数
  public boolean onContextItemSelected(MenuItem item)
  {

	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
	  .getMenuInfo();
	int MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

	switch (item.getItemId())
	{
	  case 0:
		// 应用名
		Toast.makeText(Appinfo.this,
					   "复制了:" + listItems.get(posid).get("title").toString() + "的应用名", Toast.LENGTH_SHORT).show();

		clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  
		clipboardManager.setPrimaryClip(ClipData.newPlainText(null, listItems.get(posid).get("title").toString()));  
		if (clipboardManager.hasPrimaryClip())
		{  
		  clipboardManager.getPrimaryClip().getItemAt(0).getText();  
		}  

		break;

	  case 1:
		// 应用包名

		Toast.makeText(Appinfo.this,
					   "复制了:" + listItems.get(posid).get("title").toString() + "的包名", Toast.LENGTH_SHORT).show();
		clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  
		clipboardManager.setPrimaryClip(ClipData.newPlainText(null, listItems.get(posid).get("info").toString()));  
		if (clipboardManager.hasPrimaryClip())
		{  
		  clipboardManager.getPrimaryClip().getItemAt(0).getText();  
		}  

		break;

	  case 2:
		// 应用UID
		Toast.makeText(Appinfo.this,
					   "复制了:" + listItems.get(posid).get("title").toString() + "的UID", Toast.LENGTH_SHORT).show();
		clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  
		clipboardManager.setPrimaryClip(ClipData.newPlainText(null, listItems.get(posid).get("uid").toString()));  
		if (clipboardManager.hasPrimaryClip())
		{  
		  clipboardManager.getPrimaryClip().getItemAt(0).getText();  
		}  

		break;


	  default:
		break;
	}

	return super.onContextItemSelected(item);

  }

  public Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg)
	{
	  switch (msg.what)
	  {
		case 0:					
		  Map<String, Object> map = (Map<String, Object>)msg.obj;
		  listItems.add(map);				
		  listViewAdapter.notifyDataSetChanged();

		  break;
		default:
		  super.handleMessage(msg);
	  }
	}
  };


  /**  
   * 初始化
   */  
  private void LoadListItems()
  {   
	//List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();   
	List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
	int j=0;
	PackageInfo packageInfo;
	Map<String, Object> map ;
	Message r1_Message;
	for (int i=0;i < packages.size();i++)
	{ 
	  j++;
	  if(j>20)
	  {
		j=0;
		System.gc();
	  }
	  packageInfo = packages.get(i); 
	  map = new HashMap<String, Object>();	  
	  map.put("title", packageInfo.applicationInfo.loadLabel(getPackageManager()).toString().trim());
	  map.put("uid", packageInfo.applicationInfo.uid);
	  map.put("info", packageInfo.packageName.trim());
	  map.put("img", packageInfo.applicationInfo.loadIcon(getPackageManager()));
	  r1_Message = new Message();
	  r1_Message.what = 0;
	  r1_Message.obj = map;
	  handler.sendMessage(r1_Message);	
	  

	}   
  }   




}

