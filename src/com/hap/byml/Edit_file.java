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
import com.HaP.Byml.*;
import com.HaP.Tool.*;
import java.io.*;
import java.util.regex.*;

public class Edit_file extends Activity
{

	private int rcode;
	private Intent i;

	private String file_path,file_name,t,sh_name;

	private boolean name_edit;

	private Button cancel,uid,save;

	private EditText e_name,e_content;

	private b_listener bl;
	
	private SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_file);
		i = this.getIntent();
		init();
	}
	private void init()
	{
		sp=MainActivity.sp;
		cancel = (Button)this.findViewById(R.id.e_fcancel);
		uid = (Button)this.findViewById(R.id.e_fuid);
		save = (Button)this.findViewById(R.id.e_fsave);
		bl = new b_listener();
		cancel.setOnClickListener(bl);
		uid.setOnClickListener(bl);
		save.setOnClickListener(bl);
		e_name = (EditText)this.findViewById(R.id.e_fname);
		e_content = (EditText)this.findViewById(R.id.e_fcontent);
		e_content.addTextChangedListener(mTextWatcher);
		file_path = i.getStringExtra("file_path");
		file_name = i.getStringExtra("file_name");
		sh_name=i.getStringExtra("sh_name");
		name_edit = i.getBooleanExtra("name_edit", false);
		rcode=i.getIntExtra("rcode",1);
		e_name.setText(file_name);
		if (!name_edit)
		{
			e_name.setFocusable(false);		
		}else if(file_name.indexOf(".sh")==-1){
			file_name += ".sh";			
		}
		t = FileTool.readSDFile(file_path + file_name);
		if (t.indexOf("FileNotFoundException") != -1)
		{
			try
			{
				new File(file_path + file_name).createNewFile();
			}
			catch (IOException e) {
				Toast.makeText(this, "创建空白文件:" + file_name + "失败，详细信息:" + e.toString(), Toast.LENGTH_LONG).show();
			}
			t = "";
		}
		
		e_content.setText(t);
		

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
	  //if(s.charAt(start)=='\n')
		//Log.e("on_edit", "start:" + start + " before:" + before + " count:" + count +"s:"+s.toString());
	}  

	public void afterTextChanged(Editable s)
	{  
	  String str=s.toString();
	  CharacterStyle span=null;
	  String code[]={
		"(请求|http-request)",
		"\\[(.*?)]",
		"%\\[(.*?)]",
	    "^#![^\n].*$",	  
	  };
	  int color[]={
		
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
	@Override
	public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas)
	{
		// TODO: Implement this method
		return super.onCreateThumbnail(outBitmap, canvas);
	}


	private class b_listener implements OnClickListener{

		@Override
		public void onClick(View v)
		{
			// TODO: Implement this method
			switch (v.getId())
			{
				case R.id.e_fcancel:
					Edit_file.this.finish();
					break;
				case R.id.e_fuid:		
					i=new Intent(Edit_file.this, Appinfo.class);					
					startActivity(i);
				  break;
				case R.id.e_fsave:
				
					t=e_name.getText().toString();
					MainActivity.sp.edit().putString(sh_name,t).commit();
					
					String content=e_content.getText().toString();
				    
					if(name_edit)
					{
						t+=".sh";																
					}
					new File(file_path+file_name).delete();		
					
					if(!file_name.equals(e_name.getText().toString()))
						 file_name=e_name.getText().toString();
					
					
				    if(FileTool.writeSDFile(content,file_path+t))			
					   Toast.makeText(Edit_file.this,"保存文件:"+file_name+"完成 ！",Toast.LENGTH_LONG).show();
					else
				       Toast.makeText(Edit_file.this,"保存文件:"+file_name+"失败 ！",Toast.LENGTH_LONG).show();
				
					i = new Intent();
					i.putExtra("name",e_name.getText().toString());
					Edit_file.this.setResult(rcode,i);
					Edit_file.this.finish();
					break;
			}
		}		
	}




}

