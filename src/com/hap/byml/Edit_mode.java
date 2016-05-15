package com.HaP.Byml;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.HaP.Tool.*;
import com.HaP.View.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Edit_mode extends Activity
{

  private ViewPagerIndicatorView viewPagerIndicatorView;

  private View v1,v2,v3;

  private Button save,mr,rename;

  private EditText e1,e2,e3;

  private b_listener bl;

  private Intent in;

  private SharedPreferences sp;

  private String m_name,file_path,s1="",s2="",s3="";

  private boolean is_add,is_save,is_rename;

  private HPStart hs;

  private SuperUser Su;

  private int i=0;

  private SpannableStringBuilder sp1,sp2,sp3;


  @Override
  public void onCreate(Bundle savedInstanceState)
  {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.edit_mode);
	//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mtitlebar); 
	//set ViewPagerIndicatorView
	this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.edit_viewpager_indicator_view);
	final Map<String, View> map = new HashMap<String, View>();
	v1 = LayoutInflater.from(this).inflate(R.layout.edit_1, null);
	v2 = LayoutInflater.from(this).inflate(R.layout.edit_1, null);
	v3 = LayoutInflater.from(this).inflate(R.layout.edit_1, null);
	map.put("HTTPS", v1);
	map.put("GET", v2);
	map.put("POST", v3);
	this.viewPagerIndicatorView.onPageSelected(1);
	this.viewPagerIndicatorView.setupLayout(map);	
	view_init();
	data_init();


  }

  private void data_init()
  {
	this.in = getIntent();
	sp = this.getSharedPreferences("HaP", MODE_PRIVATE);
	Su = new SuperUser(this);
	i = in.getIntExtra("i_n", 0);
	m_name = in.getStringExtra("m_name");
	file_path = in.getStringExtra("file_path");
	is_add = in.getBooleanExtra("is_add", false);	
	//Toast.makeText(this, file_path, Toast.LENGTH_LONG).show();
	if (!is_add)
	{
	  this.setTitle("编辑模式:" + m_name);
	  s1 = FileTool.readSDFile(file_path + "/" + m_name + "/connection.hp");
	  s2 = FileTool.readSDFile(file_path + "/" + m_name + "/get.hp");
	  s3 = FileTool.readSDFile(file_path + "/" + m_name + "/post.hp");			
      sp1 = SpText(s1);
	  sp2 = SpText(s2);
	  sp3 = SpText(s3);
	  e1.setText(sp1);
	  e2.setText(sp2);
	  e3.setText(sp3);

	}
	else
	{
	  this.setTitle("添加模式:" + m_name);
	  s1 = FileTool.readSDFile(file_path + "/mode/connection.hp");
	  s2 = FileTool.readSDFile(file_path + "/mode/get.hp");
	  s3 = FileTool.readSDFile(file_path + "/mode/post.hp");			
	  e1.setText(s1);
	  e2.setText(s2);
	  e3.setText(s3);
	}
	//is_add=false;
  }

  private void view_init()
  {
	// TODO: Implement this method
	save = (Button)this.findViewById(R.id.e_bsave);
	mr = (Button)this.findViewById(R.id.e_bmr);
	rename = (Button)this.findViewById(R.id.e_brename);
	bl = new b_listener();
	save.setOnClickListener(bl);
	mr.setOnClickListener(bl);
	rename.setOnClickListener(bl);

	e1 = (EditText)v1.findViewById(R.id.e_e1);
	e2 = (EditText)v2.findViewById(R.id.e_e1);
	e3 = (EditText)v3.findViewById(R.id.e_e1);
	//2.setSpannableFactory((Editable.Factory)ef);
	//e2.setEditableFactory(ef);
	//e2.set
	e1.addTextChangedListener(mTextWatcher);	
	e2.addTextChangedListener(mTextWatcher);
	e3.addTextChangedListener(mTextWatcher);
	

	/*e2.addTextChangedListener(new TextWatcher() {
	 @Override
	 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	 }

	 @Override
	 public void onTextChanged(CharSequence s, int start, int before, int count) {

	 }

	 @Override
	 public void afterTextChanged(Editable s) {


	 Iterator<String> iterator = key.keySet().iterator();
	 String text = s.toString();
	 while (iterator.hasNext()) {
	 String k = iterator.next();
	 if (text.contains(k)) {
	 int index = 0;
	 while ((index = text.indexOf(k, index)) != -1) {
	 s.setSpan(new ForegroundColorSpan(key.get(k)), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	 System.out.println("text " + text + ", start " + index + ", end " + (index + 1));
	 index += k.length();
	 }
	 }
	 }


	 }
	 });*/

	hs = new HPStart(Edit_mode.this, Su, sp, file_path, m_name, is_add);


  }

  private class b_listener implements OnClickListener
  {


	@Override
	public void onClick(View v)
	{
	  // TODO: Implement this method
	  switch (v.getId())
	  {
		case R.id.e_bsave:
		  s1 = e1.getText().toString();
		  s2 = e2.getText().toString();
		  s3 = e3.getText().toString();
		  hs = new HPStart(Edit_mode.this, Su, sp, file_path, m_name, is_add);
		  hs.setHandler(handler);
		  hs.setIPos(i);
		  if (is_save)
			hs.setIs_save(is_save);
		  hs.execute("save", s1, s2, s3);
		  is_save = true;
		  break;
		case R.id.e_bmr:
		  new AlertDialog.Builder(Edit_mode.this).setTitle("是否使用默认模板？").setMessage("使用默认模板会清空当前模式所有内容！\n并把透明代理的模式内容复制到当前3种请求当中!").setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			  @Override
			  public void onClick(DialogInterface p1, int p2)
			  {
				// TODO: Implement this method
				e1.setText(FileTool.readSDFile(file_path + "/mode/connection.hp"));
				e2.setText(FileTool.readSDFile(file_path + "/mode/get.hp"));
				e3.setText(FileTool.readSDFile(file_path + "/mode/post.hp"));

			  }
			}
		  )
			.setNegativeButton("返回" , null).show();			

		  break;
		case R.id.e_brename:
		  final EditText tn=new EditText(Edit_mode.this);
		  tn.setText(m_name);
		  new AlertDialog.Builder(Edit_mode.this).setTitle("更改模式名称").setView(tn).setIcon(
			android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			  @Override
			  public void onClick(DialogInterface p1, int p2)
			  {
				// TODO: Implement this method		

				String new_name=tn.getText().toString();	
				if (new File(file_path + "/" + m_name).renameTo(new File(file_path + "/" + new_name)))
				{
				  Edit_mode.this.setTitle("编辑模式:" + new_name);
				  sp.edit().putString("mode_" + i, new_name).commit();
				  Su.msg("修改:" + new_name + " 完成！", Toast.LENGTH_SHORT);	
				  is_rename = true;
				  m_name = new_name;
				}
				else
				{
				  //m_name = new_name;
				  if (new File(file_path + "/" +  new_name).exists())
				  {
					Su.msg("骚年，已经警告有相同名称的了，换个吧！");
					return ;
				  }
				  else
				  {
					s1 = e1.getText().toString();
					s2 = e2.getText().toString();
					s3 = e3.getText().toString();
					hs = new HPStart(Edit_mode.this, Su, sp, file_path, new_name, is_add);
					hs.setIPos(i);
					hs.execute("save", s1, s2, s3);
					is_save = true;									

				  }

				}
			  }
			}
		  )
			.setNegativeButton("返回" , null).show();			

		  break;
	  }
	}		
  }
  private TextWatcher mTextWatcher = new TextWatcher(){  
	Toast mToast = null;  
	public void beforeTextChanged(CharSequence s, int start,   
								  int count, int after)
	{  
	  //  Log.e("before_edit",""+s.toString());


	}  

	public void onTextChanged(CharSequence s, int start,   int before, int count)
	{ 
	if(s.charAt(start)=='\n')
	  Log.e("on_edit", "start:" + start + " before:" + before + " count:" + count +"s:"+s.toString());
	}  

	public void afterTextChanged(Editable s)
	{  
	  String str=s.toString();
	  CharacterStyle span=null;
	  String code[]={
		"(请求|http-request) ^.*$\n",  
		"(请求|http-request)( .*? ).*!\\ \n",  
		"(请求|http-request)( .*? )\n",
		"(请求|http-request)",
		"\\[(.*?)]",
		"%\\[(.*?)]",
		"#(.*?)\n",	  
	  };
	  int color[]={
		Color.rgb(130, 80, 100),
		Color.rgb(100, 50, 180),
		getResources().getColor(R.color.c_gjz),
		Color.rgb(50, 100, 255),
		Color.YELLOW,
		Color.GREEN, 
		getResources().getColor(R.color.c_comment)

	  };
	  for (int i=0;i < code.length;i++)
	  {
		Pattern p = Pattern.compile(code[i]);

		Matcher m = p.matcher(str);

		while (m.find())
		{

		  span = new ForegroundColorSpan(color[i]);//需要重复！

		  //span = new ImageSpan(drawable,ImageSpan.XX);//设置现在图片

		  s.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
	  }

	}  
  };  

  private SpannableStringBuilder SpText(String str)
  {
	SpannableStringBuilder spannable = new SpannableStringBuilder(str);
	CharacterStyle span=null;
	String code[]={
	  "(请求|http-request) .*\n",  
	  "(请求|http-request)( .*? ).*!\\ ",  
      "(请求|http-request)( .*? )",
	  " (请求|http-request) ",
	  "\\[(.*?)]",
	  "%\\[(.*?)]",
	  "#(.*?)\n",	  
	};
	int color[]={
	  Color.rgb(130, 80, 100),
	  Color.rgb(100, 50, 180),
	  getResources().getColor(R.color.c_gjz),
	  Color.rgb(50, 100, 255),
	  Color.YELLOW,
	  Color.GREEN, 
	  getResources().getColor(R.color.c_comment)

	};
	for (int i=0;i < code.length;i++)
	{
	  Pattern p = Pattern.compile(code[i]);

	  Matcher m = p.matcher(str);

	  while (m.find())
	  {

		span = new ForegroundColorSpan(color[i]);//需要重复！

		//span = new ImageSpan(drawable,ImageSpan.XX);//设置现在图片

		spannable.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	  }
	}
	return spannable;

  }


  public Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg)
	{
	  switch (msg.what)
	  {
		case 0:
		  int code=-1;

		  if (is_rename && hs.is_done())
			code = 19;

		  if (is_add && is_save && hs.is_done())
			code = 20;

		  in = new Intent();
		  in.putExtra("title", m_name);
		  in.putExtra("i_n", i);
		  Edit_mode.this.setResult(code, in);
		  finish();

		  break;

		case 1:

		  break;
		case 2:
		  break;

		default:
		  super.handleMessage(msg);
	  }
	}
  };
  private class edit_change extends AsyncTask<String, Integer, String>
  {

	private String str;
	private EditText e;
	public edit_change(String str, EditText e)
	{
	  this.str = str;
	  this.e = e;
	  Log.e("e_c", e.toString());

	}
	@Override
	protected String doInBackground(String[] p1)
	{
	  // TODO: Implement this method
	  return null;
	}

	//onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(String result)
	{
	  e.setText(str); 
	}


  }



//点击Menu出现的菜单
  public boolean onCreateOptionsMenu(Menu m)
  {
	m.add(0, 0, 0, "语法帮助");	
	//m.add(0, 1, 0, "帮助");
	//m.add(0, 2, 0, "关于");//.setIcon(android.R.drawable.ic_lock_power_off);
	//m.add(0, 3, 0, "优化");
    //m.add(0, 4, 0, "安装");
	//m.add(0, 5, 0, "退出");
	return true;
  }




  //菜单点击事件
  public boolean onOptionsItemSelected(MenuItem menu)
  {
	Intent i;
	switch (menu.getItemId())
	{
	  case 0:
		i = new Intent(Edit_mode.this, Code_help.class);
		startActivity(i);				
		break;
	}
	return true;
  }


  long current=0;
  public void onBackPressed()
  {
	new AlertDialog.Builder(Edit_mode.this).setTitle("是否返回首页？").setIcon(
	  android.R.drawable.ic_dialog_info)
	  .setPositiveButton("确定", new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface p1, int p2)
		{
		  // TODO: Implement this method
		  int code=-1;
		  if (is_rename && hs.is_done())
			code = 19;

		  if (is_add && is_save && hs.is_done())
			code = 20;

		  in = new Intent();
		  in.putExtra("title", m_name);
		  in.putExtra("i_n", i);
		  Edit_mode.this.setResult(code, in);
		  finish();

		}
	  }
	)
	  .setNegativeButton("返回" , null).show();			
  }



}
