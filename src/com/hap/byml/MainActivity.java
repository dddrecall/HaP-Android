package com.HaP.Byml;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.ViewGroup.*;
import android.webkit.*;
import android.widget.*;
import cn.domob.android.ads.*;
import cn.domob.android.ads.AdManager.*;
import com.HaP.Byml.*;
import com.HaP.Byml.FileBrowser.*;
import com.HaP.Tool.*;
import com.HaP.View.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.HaP.Byml.R;
import com.HaP.utils.*;

public class MainActivity extends Activity
{
  private ViewPagerIndicatorView viewPagerIndicatorView;

  //首页选项视图
  private View v1,v2,v3;

  private ExpandableListView mListView;

//26ffffff
  public static final String PUBLISHER_ID = "56OJxRPYuN74JsXLfm";

  public static final String InlinePPID = "16TLeK3aAp8skNUdxJs8T94s";

  public ExpandeAdapter mAdapter = null;

  private  int[] mImageIds = { R.drawable.run,
	R.drawable.menu, R.drawable.move_to,
	R.drawable.remove};

  private  String[] childs = {"1.启用模式","2.编辑模式","3.导出模式","4.删除模式"};

  private  String[] details = {"启动当前模式","编辑当前模式配置文件","把模式导出成文件方便传递","删除当前模式"};

  public  List<List<Item>> mData =new ArrayList<List<Item>>();;

  //private String[] stitle={"1","2","3","4","5","6","7","8","9","10","11"};


  public static SharedPreferences sp;

  //root工具类
  private SuperUser Su;

  //File工具类
  private FileTool Ft;

  //全局
  public int i=0;

  //             添加   导入    启动    状态     停止
  private Button badd,bimport,bstart,bstatus,bstop;

  //选项          global  defaults frontent  自定义1  自定义2 自定义3 保存
  private Button bglobal,bdefaults,bfrontend,bdiy1,bdiy2,bdiy3,bpsave;

  //自定义命令     启动      状态      停止
  private Button sbstart,sbstatus,sbstop;

  //防跳按钮       启动    关闭   状态
  private Button pstart,pstop,pstatus;

  //进入apn设置
  private Button goapn;

  //button的点击事件
  private b_listener blistener;

  //button的长按事件
  private b_longlisteter blonglistener;

  //dialog内的点击时间
  private D_listener 
  d_1,
  d_2,
  d_3,
  d_4,
  d_5,
  d_6,//添加模式
  d_7,
  d_8,//
  d_9,//root check
  d_10,//开机自启
  d_11,
  d_12,
  d_13;
  //启动线程
  private HPStart h_s;

  //当前listview未知
  private int now_i=0;

  //当前listview栏目名称
  private String now_name="默认透明代理";

  private EditText et1,elocalport,epip,eport,eedit;

  private EditText ephost1,ephost2;	

  private TextView 
  tnownet,//当前网络
  tapn,//APN信息
  tlocalnet,ttapn;//本地网络

  private String file_path;

  private AlertDialog ad;

  
  private NetWork_info netinfo;

  private RelativeLayout mAdContainer;

  private AdView mAdview;


  //开机自启的控件

  private EditText estart_hap;

  private CheckBox cft,cdiy1;

  private HPInstall hstart=null;
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_sample);

	//set ViewPagerIndicatorView
	this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.viewpager_indicator_view);
	final Map<String, View> map = new HashMap<String, View>();
	v1 = LayoutInflater.from(this).inflate(R.layout.activity_sample_pager_0, null);
	v2 = LayoutInflater.from(this).inflate(R.layout.activity_sample_pager_1, null);
	v3 = LayoutInflater.from(this).inflate(R.layout.activity_sample_pager_2, null);
	map.put("首页", v1);
	map.put("选项", v2);
	map.put("更多", v3);
	this.viewPagerIndicatorView.onPageSelected(1);
	this.viewPagerIndicatorView.setupLayout(map);	
	view_init();
	data_init();
	//highlight hi=new highlight();
	//hi.init();
	
  }

  private void view_init()
  {

	mListView = (ExpandableListView)v1.findViewById(R.id.pager_0_list);
	mListView.setOnChildClickListener(new mlist_listener());

	//list_initData(stitle);
	mAdapter = new ExpandeAdapter(MainActivity.this, mData);		
	mListView.setAdapter(mAdapter);
	mAdapter.notifyDataSetChanged();	
	//视图1绑定button
	badd = (Button)v1.findViewById(R.id.badd);
	bimport = (Button)v1.findViewById(R.id.bimport);
	bstop = (Button)v1.findViewById(R.id.bstop);
	bstart = (Button)v1.findViewById(R.id.bstart);
	bstatus = (Button)v1.findViewById(R.id.bstatus);
	//视图2绑定button
	bglobal = (Button)v2.findViewById(R.id.bglobal);
	bdefaults = (Button)v2.findViewById(R.id.bdefaults);
	bfrontend = (Button)v2.findViewById(R.id.bfrontend);

	eedit = (EditText)v3.findViewById(R.id.elport);

	elocalport = (EditText)v2.findViewById(R.id.elport);

	epip = (EditText)v2.findViewById(R.id.epip);
	eport = (EditText)v2.findViewById(R.id.eport);

	ephost1 = (EditText)v2.findViewById(R.id.e_phost1);
	ephost2 = (EditText)v2.findViewById(R.id.e_phost2);

	bpsave = (Button)v2.findViewById(R.id.bpsave);
	bdiy1 = (Button)v2.findViewById(R.id.bdiy1);
	bdiy2 = (Button)v2.findViewById(R.id.bdiy2);
	bdiy3 = (Button)v2.findViewById(R.id.bdiy3);

	sbstop = (Button)v2.findViewById(R.id.sbstop);
	sbstart = (Button)v2.findViewById(R.id.sbstart);
	sbstatus = (Button)v2.findViewById(R.id.sbstatus);

	pstart = (Button)v2.findViewById(R.id.bfstart);

	pstop = (Button)v2.findViewById(R.id.bfstop);

	pstatus = (Button)v2.findViewById(R.id.bfstatus);


	tnownet = (TextView)v3.findViewById(R.id.t_nownet);
	tapn = (TextView)v3.findViewById(R.id.t_apn);
	tlocalnet = (TextView)v3.findViewById(R.id.t_localnet);
	ttapn = (TextView)v3.findViewById(R.id.activitysamplepager2TextView3);

	goapn = (Button)v3.findViewById(R.id.b_goapn);

	//tname = (TextView)v3.findViewById(R.id.t_tname);

	//	bjrd = (Button)v3.findViewById(R.id.bfrontend);

//		eedit = (EditText)v3.findViewById(R.id);

	//Button绑定点击事件
	blistener = new b_listener();
	badd.setOnClickListener(blistener);
	bimport.setOnClickListener(blistener);
	bstop.setOnClickListener(blistener);
	bstart.setOnClickListener(blistener);
	bstatus.setOnClickListener(blistener);


	new Thread(){

	  public void run()
	  {

		try
		{
		  sleep(1200);
		}
		catch (InterruptedException e)
		{

		}

		Message msg=new Message();
		msg.obj = "";
		msg.what = 5;
		handler.sendMessage(msg);	
	  }

	}.start();
  }

  private void data_init()
  {
	sp = MainActivity.this.getSharedPreferences("HaP", MODE_PRIVATE);		    
	Su = new SuperUser(this);
	Ft = new FileTool(this);		

	if (SuperUser.checkRoot())
	{
	 // SuperUser.setHave_root(true);
	  sp.edit().putBoolean("have_root", true).commit();
	 // Su.setIs_root(true);

	}
	else if (sp.getBoolean("have_root", true))
	{
	  d_9 = new D_listener();
	  ad = new AlertDialog.Builder(this).setTitle("请允许root权限 !").setMessage("如果没有root权限去> http://byml.net 八云官网看看").setIcon(
		android.R.drawable.ic_dialog_info)
		.setPositiveButton("重新检测" , d_9)
		.setNegativeButton("退出软件" , d_9)
		.setNeutralButton("免ROOT模式", d_9).show();	
	  try
	  {
		Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
		field.setAccessible(true);								
		field.set(ad, false);

	  }
	  catch (Exception e)
	  {}
	  return;

	}
	//	Su.Shell(sp.getString("sshell", "echo '自启命令啥都木有！快去添加吧！'\n"), true).start();				



	NetWork_info net=new NetWork_info(this);
	String n_status=net.getNetStatus();
	tnownet.setText(n_status);
	if (!n_status.equals("无网络"))
	{
	  if (n_status.equals("手机网络"))
	  {
		tnownet.append(" " + net.getMobSubName());
		tapn.setText("APN:" + net.getMobAPNName() + " IP:" + net.getApnIP() + " Port:" + net.getApnPort());
		tlocalnet.setText(net.getLocalIpAddress());
	  }
	  else
	  {
		ttapn.setText("Wifi信息");
		tapn.setText(net.getWifiInfo());
		tlocalnet.setText(net.getLocalIpAddress());
		
	  }
	}
	file_path = Ft.getFILESPATH();


	install_init();
	String str="";
	while (true)
	{
	  if ((str = sp.getString("mode_" + i, null)) == null || str.length() < 1)
	  {
		break;
	  }
	  if (!new File(file_path + "/" + str + "/get.hp").exists())
	  {
		str += "(文件丢失)";
	  }
	  list_initData(str);
	  i++;
	}
	
	if (i == 0&&hstart==null)
	{
	  
	  try
	  {		
	    Ft.deleteSDFile(Ft.getFILESPATH()+"/默认透明代理");
		if (new File(Ft.getFILESPATH() + "/" + "默认透明代理").mkdirs())
		{
		  FileTool.CopyFile(file_path + "/mode/get.hp", file_path + "/默认透明代理/get.hp");
		  FileTool.CopyFile(file_path + "/mode/post.hp", file_path + "/默认透明代理/post.hp");
		  FileTool.CopyFile(file_path + "/mode/connection.hp", file_path + "/默认透明代理/connection.hp");
		  list_initData("默认透明代理");
		  sp.edit().putString("mode_" + i, "默认透明代理").commit();
		  i++;
		  
		}
	  }
	  catch (Exception e)
	  {
		Su.msg("无法创建默认模式，请检查私有目录是否有可写权限！\n详细信息:" + e.toString(), -1);
		return ;
	  }


	}

	Intent intent = getIntent();
	Uri u;
	if ((u = intent.getData()) != null)
	{		
	  if (!MimeTypeMap.getSingleton().getFileExtensionFromUrl(u.toString()).equals("mhp"))
		Su.msg("不支持格式:" + MimeTypeMap.getSingleton().getFileExtensionFromUrl(u.toString()), -1);
	  else
	  {
		try
		{
		  FileInputStream fis=new FileInputStream(u.getPath());
		  ObjectInputStream ois=new ObjectInputStream(fis);
		  MHP m=(MHP)ois.readObject();
		  if (m != null && m.getName() != null)	 
		  {
			h_s = new HPStart(MainActivity.this, Su, sp, file_path, m.getName(), true);
			h_s.setHandler(handler);
			h_s.setIPos(i);
			h_s.execute("mhp", m.getConnection(), m.getGet(), m.getPost());	
		  }
		  else
		  {
			Su.msg("文件以损坏，无法读取数据 ！", -1);
		  }


		}
		catch (Exception e)
		{
		  Su.msg("导入文件已经损坏，错误信息:" + e.toString(), -1);
		}
	  }
	}
	now_i = sp.getInt("now_i", now_i);
	now_name = sp.getString("now_name",now_name);	
	//Su.msg(get_processname(9999));
	elocalport.setText(sp.getString("localport", "8787"));
	epip.setText(sp.getString("p_ip", "10.0.0.172"));
	eport.setText(sp.getString("p_port", "80"));
	ephost1.setText(sp.getString("p_host1", "byml.net"));
	ephost2.setText(sp.getString("p_host2", "byml.net"));
	bstart.setText("启动:" + now_name);		
	bdiy1.setText(sp.getString("sh_diy1", "自定义①"));
	bdiy2.setText(sp.getString("sh_diy2", "自定义②"));
	bdiy3.setText(sp.getString("sh_diy3", "自定义③"));

	mAdapter.notifyDataSetChanged();

  }

  private void install_init()
  {
	if (!sp.getBoolean("newfix_" + SuperUser.version, false))
	{
	  d_1 = new D_listener();
	  new AlertDialog.Builder(this).setTitle(this.getString(R.string.newfix_title)).setMessage(this.getString(R.string.newfix)).setIcon(
		android.R.drawable.ic_dialog_info)
		.setPositiveButton("确定" , d_1)
		.setNegativeButton("返回" , d_1).show();			
	}	
	
   
	if (!new File(Ft.getFILESPATH() + "/" + SuperUser.version).exists())
	{
	  Su.msg("开始解压程序资源.....");
	  sp.edit().putString("_start", "cd "+file_path+"\nsh ./start.sh\n").commit();
	  sp.edit().putString("_stop", "cd "+file_path+"\nsh ./stop.sh\n").commit();
	  sp.edit().putString("_status","cd "+file_path+"\nsh ./status.sh\n").commit();
	  hstart=new HPInstall(MainActivity.this, Su, handler, Ft.getFILESPATH(), "assets");
	  hstart.start();
	  
	  
	}
	
	
  }
  public void list_initData(String s[])
  {
	for (int i = 0; i < s.length; i++)
	{
	  List<Item> list = new ArrayList<Item>();
	  for (int j = 0; j < childs.length; j++)
	  {
		Item item = new Item(s[i], mImageIds[j], childs[j], details[j]);
		list.add(item);
	  }
	  mData.add(list);
	}
  }
  public  void list_initData(String s)
  {
	List<Item> list = new ArrayList<Item>();
	for (int j = 0; j < childs.length; j++)
	{
	  Item item = new Item(s, mImageIds[j], childs[j], details[j]);
	  list.add(item);	
	}
	mData.add(list);
  }
  public  List<Item> list_init(String s)
  {
	List<Item> list = new ArrayList<Item>();
	for (int j = 0; j < childs.length; j++)
	{
	  Item item = new Item(s, mImageIds[j], childs[j], details[j]);
	  list.add(item);	
	}
	return list;
  }
  
  
  private String get_processname(int pid) {
	ActivityManager mgr = (ActivityManager)getApplicationContext().getSystemService(
	  Context.ACTIVITY_SERVICE);
	String pname = "";
	List<ActivityManager.RunningAppProcessInfo> apps = mgr.getRunningAppProcesses();
	for(ActivityManager.RunningAppProcessInfo pinfo : apps) {
	  if(pinfo.pid == pid) {
		pname = pinfo.processName;
		break;
	  }
	}

	return pname; 
  }
  
  
  
  
  
  private class mlist_listener implements ExpandableListView.OnChildClickListener
  {

	@Override
	public boolean onChildClick(ExpandableListView parent, final View v,
								final int groupPosition, int childPosition, long id)
	{


	  // TODO: Implement this method
	  now_i = groupPosition;
	  now_name = mAdapter.getChild(groupPosition, childPosition).getTitel();

	  switch (childPosition)
	  {
		  //启动模式
		case 0:
		  //Msg("启动模式", false);
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH(), now_name, false);
		  h_s.setHandler(handler);
		  h_s.execute("start");
		  bstart.setText("启动:" + now_name);
		  sp.edit().putInt("now_i", now_i).commit();
		  sp.edit().putString("now_name", now_name).commit();				

		  break;
		  //编辑模式
		case 1:
		  //Msg("编辑模式", false);
		  Intent i=new Intent(MainActivity.this, Edit_mode.class);
		  i.putExtra("m_name", now_name);
		  i.putExtra("i_n", now_i);
		  i.putExtra("file_path", file_path);
		  i.putExtra("is_add", false);
		  startActivityForResult(i, 1);
		  overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);    
		  //overridePendingTransition(Android.R.anim.fade_in,android.R.anim.fade_out);
		  
		  
		  
		  break;
		  //导出模式
		case 2:
		  d_8 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setHint("导出目录");
		  et1.setText(Ft.getSDPATH() + "/" + now_name + ".mhp");
		  ad = new AlertDialog.Builder(MainActivity.this).setTitle("导出模式文件位置").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定" , d_8)
			.setNegativeButton("返回" , d_8).show();						
		  break;
		  //删除模式
		case 3:
		  //Msg("删除模式", false);
		  d_7 = new D_listener();
		  ad = new AlertDialog.Builder(MainActivity.this).setTitle("是否删除当前模式？").setMessage("是否删除:" + now_name).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定" , d_7)
			.setNegativeButton("取消" , d_7).show();	


		  break;

	  }

	  return false;
	}

  }


  public Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg)
	{
	  switch (msg.what)
	  {

		case 1:
		  list_initData("默认透明代理");
		  sp.edit().putString("mode_" + i, "默认透明代理").commit();			
		  mAdapter.notifyDataSetChanged();
		  i++;
		  break;
		case 2:
		  list_initData((String)msg.obj);
		  sp.edit().putString("mode_" + i, (String)msg.obj).commit();			
		  mAdapter.notifyDataSetChanged();
		  i++;
		  break;
		case 5:
		  //视图二绑定button点击事件
		  bglobal.setOnClickListener(blistener);
		  bdefaults.setOnClickListener(blistener);
		  bfrontend.setOnClickListener(blistener);
		  bpsave.setOnClickListener(blistener);
		  bdiy1.setOnClickListener(blistener);
		  bdiy2.setOnClickListener(blistener);
		  bdiy3.setOnClickListener(blistener);

		  //命令
		  sbstop.setOnClickListener(blistener);
		  sbstart.setOnClickListener(blistener);
		  sbstatus.setOnClickListener(blistener);

		  //防跳

		  pstart.setOnClickListener(blistener);

		  pstop.setOnClickListener(blistener);

		  pstatus.setOnClickListener(blistener);

		  goapn.setOnClickListener(blistener);

//		bjrd.setOnClickListener(blistener);
		  //button绑定长按事件		
		  blonglistener = new b_longlisteter();
		  bdiy1.setOnLongClickListener(blonglistener);
		  bdiy2.setOnLongClickListener(blonglistener);
		  bdiy3.setOnLongClickListener(blonglistener);
		  bstop.setOnLongClickListener(blonglistener);
		  bstart.setOnLongClickListener(blonglistener);
		  bstatus.setOnLongClickListener(blonglistener);
		  pstart.setOnLongClickListener(blonglistener);
		  pstop.setOnLongClickListener(blonglistener);
		  pstatus.setOnLongClickListener(blonglistener);

		  
		  mAdContainer = (RelativeLayout) v3.findViewById(R.id.activitysamplepager2RelativeLayout1);
		  // Create ad view
		  mAdview = new AdView(MainActivity.this, PUBLISHER_ID, InlinePPID);
		  mAdview.setKeyword("game");
		  mAdview.setAdEventListener(new AdEventListener() {
			  @Override
			  public void onAdOverlayPresented(AdView adView)
			  {
				Log.i("DomobSDKDemo", "overlayPresented");
			  }
			  @Override
			  public void onAdOverlayDismissed(AdView adView)
			  {
				Log.i("DomobSDKDemo", "Overrided be dismissed");
			  }
			  @Override
			  public void onAdClicked(AdView arg0)
			  {
				Log.i("DomobSDKDemo", "onDomobAdClicked");
			  }
			  @Override
			  public void onLeaveApplication(AdView arg0)
			  {
				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
			  }
			  @Override
			  public Context onAdRequiresCurrentContext()
			  {
				return MainActivity.this;
			  }
			  @Override
			  public void onAdFailed(AdView arg0, ErrorCode arg1)
			  {
				Log.i("DomobSDKDemo", "onDomobAdFailed");
			  }
			  @Override
			  public void onEventAdReturned(AdView arg0)
			  {
				Log.i("DomobSDKDemo", "onDomobAdReturned");
			  }
			});

		  RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
																			   LayoutParams.WRAP_CONTENT);
		  layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		  mAdview.setLayoutParams(layout);
		  mAdContainer.addView(mAdview);
		  Updater.checkUpdate(MainActivity.this, PUBLISHER_ID); 

		  break;
		default:
		  super.handleMessage(msg);
	  }
	}
  };



  private class b_listener implements OnClickListener
  {

	private Intent i;

	//button点击事件
	@Override
	public void onClick(View v)
	{
	  // TODO: Implement this method
	  switch (v.getId())
	  {
		case R.id.badd:
		  d_6 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setHint("输入模式名称");
		  ad = new AlertDialog.Builder(MainActivity.this).setTitle("添加新的模式？").setMessage("添加一个新的模式").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定" , d_6)
			.setNegativeButton("取消" , d_6).show();	

		  break;
		 
		case R.id.bstop:
		 // Utils.runCommand("echo 'stop'\n");
		 // Utils.runCommand(sp.getString("_stop", "cd " + file_path + " \n./H-stop ./haproxy.pid\n"));
		Su.DStmp=Su.DShell(sp.getString("_stop", "cd " + file_path + " \n./H-stop ./haproxy.pid\n"), true);
	    Su.DStmp.start();
   	  break;
		case R.id.bstart:
		  if (now_name.equals(""))
			now_name = mAdapter.getChild(now_i, 0).getTitel();
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH(), now_name, false);
		  h_s.execute("start");
		  bstart.setText("启动:" + now_name);
		  sp.edit().putInt("now_i", now_i).commit();
		  sp.edit().putString("now_name", now_name).commit();
		  break;
		case R.id.bstatus:
		  Su.DShell(sp.getString("_status", "cd " + file_path + " \n./H-status ./haproxy.pid\n"), true).start();

		  break;

		  //视图2
		case R.id.bglobal:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", "global.hp");
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", false);
		  i.putExtra("sh_name", "sh_");
		  i.putExtra("rcode", 1);
		  startActivityForResult(i, 5);

		  break;
		case R.id.bdefaults:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", "defaults.hp");
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", false);
		  i.putExtra("sh_name", "sh_");
		  i.putExtra("rcode", 1);

		  startActivityForResult(i, 5);		
		  break;
		case R.id.bfrontend:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", "frontend.hp");
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", false);	
		  i.putExtra("sh_name", "sh_");
		  i.putExtra("rcode", 1);

		  startActivityForResult(i, 5);		

		  break;
		case R.id.bpsave:
		  String localport,p_ip,p_port,p_host1,p_host2;
		  localport = elocalport.getText().toString();
		  if (localport.length() < 1)
		  {
			Su.msg("监听端口框不能为空 !", -1);
			return ;
		  }
		  p_ip = epip.getText().toString();
		  if (p_ip.length() < 1)
		  {
			Su.msg("代理服务器IP不能为空 !", -1);
			return ;
		  }
		  p_port = eport.getText().toString();
		  if (p_port.length() < 1)
		  {
			Su.msg("代理服务器端口不能为空 !", -1);
			return ;
		  }

		  p_host1 = ephost1.getText().toString();

		  if (p_host1.length() < 1)
		  {
			Su.msg("伪装Host1不能为空 ！");
			return ;
		  }
		  p_host2 = ephost2.getText().toString();					
		  if (p_host2.length() < 1)
		  {
			Su.msg("伪装Host2不能为空 !");					
			return ;
		  }

		  sp.edit().putString("localport", localport).commit();
		  sp.edit().putString("p_ip", p_ip).commit();
		  sp.edit().putString("p_port", p_port).commit();
		  sp.edit().putString("p_host1", p_host1).commit();
		  sp.edit().putString("p_host2", p_host2).commit();
		  Su.msg("保存完成 !");
		  break;
		case R.id.bdiy1:
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode", sp.getString("sh_diy1", "自定义①")+".sh" , false);
		  h_s.execute("sh");
		  
		 // Su.DShell("cd " + file_path + "/mode\nsh ./" ++ ".sh\n", true, SuperUser.have_root).start();

		  break;
		 
		case R.id.bdiy2:
		  
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode", sp.getString("sh_diy2", "自定义②") + ".sh", false);
		  h_s.execute("sh");
		  
		  
		 // Su.DShell("cd " + file_path + "/mode\nsh ./" + sp.getString("sh_diy2", "自定义②") + ".sh\n", true, SuperUser.have_root).start();

		  break;
		case R.id.bdiy3:
		  
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode", sp.getString("sh_diy3", "自定义③") + ".sh", false);
		  h_s.execute("sh");
		  
		 // Su.DShell("cd " + file_path + "/mode\nsh ./" + sp.getString("sh_diy3", "自定义③") + ".sh\n", true, SuperUser.have_root).start();

		  break;

		case R.id.sbstart:
		  d_3 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setText(sp.getString("_start", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n"));
		  new AlertDialog.Builder(MainActivity.this).setTitle("编辑:启动命令").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("保存" , d_3)
			.setNegativeButton("返回" , d_3).show();			

		  break;
		case R.id.sbstatus:

		  d_4 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setText(sp.getString("_status", "cd " + file_path + " \n./H-status ./haproxy.pid\n"));
		  new AlertDialog.Builder(MainActivity.this).setTitle("编辑:状态命令").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("保存", d_4)
			.setNegativeButton("返回" , d_4).show();			

		  break;
		case R.id.sbstop:
		  d_5 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setText(sp.getString("_stop", "cd " + file_path + " \n./H-stop ./haproxy.pid\n"));
		  new AlertDialog.Builder(MainActivity.this).setTitle("编辑:停止命令").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("保存" , d_5)
			.setNegativeButton("返回" , d_5).show();			


		  break;

		case R.id.bfstart:
		 // Su.DShell("cd " + file_path + "/mode\nsh ./" + sp.getString("sh_pstart", "pstart.sh") + "\n", true, SuperUser.have_root).start();
			
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode",sp.getString("sh_pstart", "pstart.sh") , false);
		  h_s.execute("sh");
		  
		  break;

		case R.id.bfstatus:

		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode",sp.getString("sh_pstatus", "pstatus.sh") , false);
		  h_s.execute("sh");
		  
		  
		  //Su.DShell("cd " + file_path + "/mode\nsh ./" + sp.getString("sh_pstatus", "pstatus.sh") + "\n", true, SuperUser.have_root).start();

		  break;
		case R.id.bfstop:
		  h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode", sp.getString("sh_pstop", "pstop.sh") , false);
		  h_s.execute("sh");
		  
		  
		 // Su.DShell("cd " + file_path + "/mode\nsh ./" + sp.getString("sh_pstop", "pstop.sh") + "\n", true, SuperUser.have_root).start();
		  break;
		case R.id.bimport:

		  Intent in=new Intent(MainActivity.this, FileMain.class);	 
		  in.putExtra("IPath", sp.getString("IPath",(Ft.getSDPATH())));
		  startActivityForResult(in, 21);

		  break;

		case R.id.b_goapn:
		  Intent localIntent = new Intent("android.settings.APN_SETTINGS");					

		  startActivity(localIntent);




		  break;

	  }
	}

  }

  private class b_longlisteter implements OnLongClickListener
  {

	private Intent i;
	//button长按事件
	@Override
	public boolean onLongClick(View v)
	{
	  // TODO: Implement this method
	  switch (v.getId())
	  {
		case R.id.bdiy1:

		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", sp.getString("sh_diy1", "自定义①"));
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", true);				
		  i.putExtra("sh_name", "sh_diy1");
		  i.putExtra("rcode", 5);

		  startActivityForResult(i, 5);		


		  break;
		case R.id.bdiy2:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", sp.getString("sh_diy2", "自定义②"));
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", true);		
		  i.putExtra("sh_name", "sh_diy2");
		  i.putExtra("rcode", 6);

		  startActivityForResult(i, 6);		


		  break;
		case R.id.bdiy3:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", sp.getString("sh_diy3", "自定义③"));
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", true);				
		  i.putExtra("sh_name", "sh_diy3");
		  i.putExtra("rcode", 7);

		  startActivityForResult(i, 7);		


		  break;

		case R.id.bstart:
		  d_3 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setText(sp.getString("_start", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n"));
		  new AlertDialog.Builder(MainActivity.this).setTitle("编辑:启动命令").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("保存" , d_3)
			.setNegativeButton("返回" , d_3).show();			

		  break;
		case R.id.bstatus:

		  d_4 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setText(sp.getString("_status", "cd " + file_path + " \n./H-status ./haproxy.pid\n"));
		  new AlertDialog.Builder(MainActivity.this).setTitle("编辑:状态命令").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("保存" , d_4)
			.setNegativeButton("返回" , d_4).show();			

		  break;
		case R.id.bstop:
		  d_5 = new D_listener();
		  et1 = new EditText(MainActivity.this);
		  et1.setText(sp.getString("_stop", "cd " + file_path + " \n./H-stop ./haproxy.pid\n"));
		  new AlertDialog.Builder(MainActivity.this).setTitle("编辑:停止命令").setView(et1).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("保存" , d_5)
			.setNegativeButton("返回" , d_5).show();
		  break;

		case R.id.bfstart:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", "pstart.sh");
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", false);				
		  i.putExtra("sh_name", "sh_pstart");
		  i.putExtra("rcode", 8);

		  startActivityForResult(i, 8);		
		  break;
		case R.id.bfstatus:

		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", "pstatus.sh");
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", false);				
		  i.putExtra("sh_name", "sh_pstatus");
		  i.putExtra("rcode", 8);

		  startActivityForResult(i, 8);		

		  break;

		case R.id.bfstop:
		  i = new Intent(MainActivity.this, Edit_file.class);
		  i.putExtra("file_name", "pstop.sh");
		  i.putExtra("file_path", file_path + "/mode/");
		  i.putExtra("name_edit", false);				
		  i.putExtra("sh_name", "sh_pstop");
		  i.putExtra("rcode", 8);

		  startActivityForResult(i, 8);		


		  break;


	  }
	  return false;
	}

  }

  private class D_listener implements DialogInterface.OnClickListener
  {

	@Override
	public void onClick(DialogInterface p1, int p2)
	{

	  // TODO: Implement this method
	  //Su.msg("p1:"+p1+" d_1:"+d_1);

	  if (this.equals(d_1))
	  {

		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative");
			sp.edit().putBoolean("newfix_" + SuperUser.version, true).commit();

			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			//		Su.msg("positive");
			sp.edit().putBoolean("newfix_" + SuperUser.version, true).commit();
			break;
		  default:
			Su.msg("Default");
			break;

		}
	  }
	  else
	  if (this.equals(d_2))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			//		Su.msg("positive 2");
			new HPInstall(MainActivity.this, Su, handler, Ft.getFILESPATH(), "assets").start();
			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }
	  else
	  if (this.equals(d_3))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			sp.edit().putString("_start", et1.getText().toString()).commit();
			Su.msg("保存:启动执行命令完成.....");

			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }	
	  else
	  if (this.equals(d_4))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			//		Su.msg("positive 2");
			sp.edit().putString("_status", et1.getText().toString()).commit();
			Su.msg("保存:状态执行命令完成.....");


			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }			
	  else
	  if (this.equals(d_5))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			//		Su.msg("positive 2");
			sp.edit().putString("_stop", et1.getText().toString()).commit();
			Su.msg("保存:停止执行命令完成.....");

			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }

	  else
	  if (this.equals(d_6))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			//	ad.dismiss();
			//关闭对话框   
			try
			{  
			  Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
			  field.setAccessible(true);
			  field.set(ad, true);   
			}
			catch (Exception e)
			{ 
			  e.printStackTrace(); 
			}


			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			if (et1.getText().toString().length() < 1 || new File(file_path + "/" + et1.getText().toString()).exists())
			{
			  try
			  {
				Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
				field.setAccessible(true);								
				field.set(ad, false);

			  }
			  catch (Exception e)
			  {}
			  et1.setText("");
			  et1.setHint("请输入>=1个字符！或者已经有相同的名称");
			  return ;
			}

			Intent i=new Intent(MainActivity.this, Edit_mode.class);
			i.putExtra("m_name", et1.getText().toString());
			i.putExtra("i_n", MainActivity.this.i);
			i.putExtra("file_path", file_path);
			i.putExtra("is_add", true);
			startActivityForResult(i, 1);
			//关闭对话框   
			try
			{  
			  Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
			  field.setAccessible(true);
			  field.set(ad, true);   
			}
			catch (Exception e)
			{ 
			  e.printStackTrace(); 
			}

			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }
	  else
	  if (this.equals(d_7))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			if (i != now_i + 1)
			{
			  sp.edit().putString(sp.getString("mode_" + now_i, ""), "").commit();
			  for (int i1=now_i;i1 < i;i1++)
			  {
				sp.edit().putString("mode_" + i1, sp.getString("mode_" + (i1 + 1), "")).commit();
			  }
			  //sp.edit().putString("mode_" + i, "").commit();

			}
			else
			{
			  sp.edit().putString("mode_" + now_i, "").commit();
			}
			i--;
			mData.remove(now_i);
			mAdapter.notifyDataSetChanged();
			if (FileTool.deleteSDFile(file_path + "/" + now_name))
			  Su.msg("删除:" + now_name + ":" + now_i + "完成 !");
			else
			  Su.msg("删除:" + now_name + ":" + now_i + "失败！", -1);

			if (bstart.getText().toString().indexOf(now_name) != -1)
			  bstart.setText("模式以被删除");
			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }
	  else
	  if (this.equals(d_8))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			try
			{
			  DesUtils des=new DesUtils("byml.net");
			  String path=file_path + "/" + now_name;
			  FileOutputStream fos=new FileOutputStream(et1.getText().toString());
			  ObjectOutputStream oos=new ObjectOutputStream(fos);
			  MHP m=new MHP(now_name, des.encrypt(FileTool.readSDFile(path + "/get.hp")), des.encrypt(FileTool.readSDFile(path + "/post.hp")), des.encrypt(FileTool.readSDFile(path + "/connection.hp")));
			  oos.writeObject(m);
			  oos.close();
			  fos.close();
			  Su.msg("模式:" + now_name + "导出成功 ！请到:" + et1.getText().toString() + " 查看！");
			}
			catch (Exception e)
			{
			  Su.msg(e.toString(), -1);
			}

			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }
	  else
	  if (this.equals(d_9))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");

			try
			{  
			  Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
			  field.setAccessible(true);
			  field.set(ad, true);   
			}
			catch (Exception e)
			{ 
			  e.printStackTrace(); 
			}
			finish();
			System.exit(0);
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			//Su.msg("neutral 2");
			try
			{  
			  Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
			  field.setAccessible(true);
			  field.set(ad, true);   
			}
			catch (Exception e)
			{ 
			  e.printStackTrace(); 
			}
			sp.edit().putBoolean("have_root", false).commit();

			data_init();
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			if (SuperUser.checkRoot())
			{
			  data_init();
			  try
			  {  
				Field field = ad.getClass().getSuperclass().getDeclaredField("mShowing");
				field.setAccessible(true);
				field.set(ad, true);   
			  }
			  catch (Exception e)
			  { 
				e.printStackTrace(); 
			  }
			}
			else
			{
			  Toast.makeText(MainActivity.this, "任然获取root失败……！去八云官网:http://byml.net 看看吧！", Toast.LENGTH_LONG).show();
			}
			break;


		  default:
			Su.msg("Default 2");
			break;

		}
	  }
	  if (this.equals(d_10))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			//			Su.msg("negative 2");
			//	ad.dismiss();
			sp.edit().putBoolean("isStart", false).commit();
			Su.msg("关闭了开机自启~");

			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:
			String t=estart_hap.getText().toString();
			if (t.length() < 3)
			{
			  Su.msg("HaP启动命令字符太少，请重新输入 !");
			  return;
			}
			sp.edit().putString("_start", t);
			t = "";
			if (cft.isChecked())
			{
			  sp.edit().putBoolean("zq_cft", true).commit();
			  t += "\ncd " + file_path + "/mode\nsh ./" + sp.getString("sh_pstart", "pstart.sh") + "\n";
			}
			else
			  sp.edit().putBoolean("zq_cft", false).commit();

			if (cdiy1.isChecked())
			{
			  sp.edit().putBoolean("zq_cdiy1", true).commit();
			  t += "\ncd " + file_path + "/mode\nsh ./" + sp.getString("sh_diy1", "自定义①") + ".sh\n";
			}
			else
			  sp.edit().putBoolean("zq_cdiy1", false).commit();

			sp.edit().putString("sshell", t).commit();
			sp.edit().putBoolean("isStart", true).commit();
			Su.msg("开启了开机自启~");


			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }

	  if (this.equals(d_11))
	  {
		switch (p2)
		{
		  case DialogInterface.BUTTON_NEGATIVE:
			break;
		  case DialogInterface.BUTTON_NEUTRAL:
			Su.msg("neutral 2");
			break;
		  case DialogInterface.BUTTON_POSITIVE:						
			new HPInstall(MainActivity.this, Su, handler, file_path, et1.getText().toString()).start();						
			break;
		  default:
			Su.msg("Default 2");
			break;

		}
	  }





	}

  }

  //点击Menu出现的菜单
  public boolean onCreateOptionsMenu(Menu m)
  {
	m.add(0, 0, 0, "自启");	
	m.add(0, 1, 0, "帮助");
	m.add(0, 2, 0, "关于");//.setIcon(android.R.drawable.ic_lock_power_off);
	m.add(0, 3, 0, "优化");
    m.add(0, 4, 0, "安装");
	m.add(0, 5, 0, "退出");
	return true;
  }


  //菜单点击事件
  public boolean onOptionsItemSelected(MenuItem menu)
  {
	Intent i=new Intent();
	switch (menu.getItemId())
	{
	  case 0:
		d_10 = new D_listener();
		//et1 = new EditText(MainActivity.this);
		View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.kjzq, null);		
		//et1.setHint("开机自启命令");
		if (sp.getString("_start", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n").length() < 3)
		  sp.edit().putString("_start", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n").commit();
		estart_hap = (EditText)view.findViewById(R.id.kjzqEditText1);
		cft = (CheckBox)view.findViewById(R.id.kjzqCheckBox1);
		cdiy1 = (CheckBox)view.findViewById(R.id.kjzqCheckBox2);
		cft.setChecked(sp.getBoolean("zq_cft", false));
		cdiy1.setChecked(sp.getBoolean("zq_cdiy1", false));
		TextView t1=(TextView)view.findViewById(R.id.kjzqTextView1);
		TextView t2=(TextView)view.findViewById(R.id.kjzqTextView2);
		estart_hap.setText(sp.getString("_start", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n"));	
		t1.setText("开启防跳");
		t2.setText(sp.getString("sh_diy1", "自定义①"));

		//	et1.setText(sp.getString("sshell", "cd " + file_path + "\n./H-start " + file_path + " ./haproxy.pid\n"));

		ad = new AlertDialog.Builder(MainActivity.this).setTitle("设置开机自启命令").setView(view).setIcon(
		  android.R.drawable.ic_dialog_info)
		  .setPositiveButton("开启" , d_10)
		  .setNegativeButton("关闭" , d_10).show();						

		break;
	  case 1:
		i = new Intent(MainActivity.this, Help.class);
		startActivity(i);
		
		//overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
		break;
	  case 2:
		i = new Intent(MainActivity.this, About.class);
		startActivity(i);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		
		break;
	  case 3:
		Su.DShell("sync\necho 3 > /proc/sys/vm/drop_caches\necho '\t优化内存碎片命令执行完成...\n\t快去查看下内存剩余多少吧..!\n\n注:本命令会回收内存碎片，如果没有碎片剩余内存不会变动的\n适用于手机长时间使用，内存占用大清理不掉.'\n", true).start();

		break;
	  case 4:
		d_11 = new D_listener();
		et1 = new EditText(MainActivity.this);
		et1.setHint("本地安装目录");
		et1.setText(Ft.getSDPATH() + "/HaP");
		ad = new AlertDialog.Builder(MainActivity.this).setTitle("本地安装").setView(et1).setMessage("注意:需要在官网下载其他位数的安装包\n然后把压缩文件里的文件解压到SD卡下/HaP目录，然后在这点击安装").setIcon(
		  android.R.drawable.ic_dialog_info)
		  .setPositiveButton("安装" , d_11)
		  .setNegativeButton("关闭" , d_11).show();						

		break;
	  case 5:
		finish();
		System.exit(0);
		break;
	}
	return true;
  }

  

  long current=0;
  public void onBackPressed()
  {

	if (System.currentTimeMillis() - current > 2000)
	{
	  current = System.currentTimeMillis();
	  Toast.makeText(this, "再点一次退出", Toast.LENGTH_SHORT).show();   
	}
	else
	{
	   
     finish();
     System.exit(0);   
	}
  }

  // 回调方法，从第二个页面回来的时候会执行这个方法
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
	// 根据上面发送过去的请求吗来区别
	switch (resultCode)
	{


	  case 5:
		bdiy1.setText(data.getStringExtra("name"));
		break;

	  case 6:

		bdiy2.setText(data.getStringExtra("name"));
		break;

	  case 7:

		bdiy3.setText(data.getStringExtra("name"));
		break;

	  case 19:				
		mData.set(data.getIntExtra("i_n", 0), list_init(data.getStringExtra("title")));
		mAdapter.notifyDataSetChanged();
		Toast.makeText(this, "修改:" + data.getStringExtra("title") + " 完成！", Toast.LENGTH_SHORT).show();   
		break;		
	  case 20:
		i++;
		list_initData(data.getStringExtra("title"));
		mAdapter.notifyDataSetChanged();
		Toast.makeText(this, "添加: " + data.getStringExtra("title") + " 完成 ！In:" + i , Toast.LENGTH_SHORT).show();   
		
		break;
	  case 21:

		if (!data.getStringExtra("File_Path").endsWith(".mhp"))
		  Su.msg("不支持格式:" + MimeTypeMap.getSingleton().getFileExtensionFromUrl(data.getStringExtra("File_Path")), -1);
		else
		{
		  try
		  {
			FileInputStream fis=new FileInputStream(data.getStringExtra("File_Path"));
			ObjectInputStream ois=new ObjectInputStream(fis);
			MHP m=(MHP)ois.readObject();
			if (m != null && m.getName() != null)	 
			{
			  sp.edit().putString("IPath",new File(data.getStringExtra("File_Path")).getParent()).commit();
			  h_s = new HPStart(MainActivity.this, Su, sp, file_path, m.getName(), true);
			  h_s.setHandler(handler);
			  h_s.setIPos(i);
			  h_s.execute("mhp", m.getConnection(), m.getGet(), m.getPost());	
			}
			else
			{
			  Su.msg("文件以损坏，无法读取数据 ！", -1);
			}


		  }
		  catch (Exception e)
		  {
			Su.msg("导入文件已经损坏，错误信息:" + e.toString(), -1);
		  }
		}

		break;
	  default:
		break;
	}
  }




}
	


