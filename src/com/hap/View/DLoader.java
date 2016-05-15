package com.HaP.View;
import android.app.*;
import android.content.*;
import android.text.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;
import com.HaP.Byml.*;
import android.util.*;
import android.content.ClipboardManager;
import android.transition.*;
public class DLoader
{
	private Context con;
	private AlertDialog.Builder loader;

	private View vloader;

	private ImageView iv;
	private TextView loadText,loadText2;

	private ProgressBar loadPb,loadPbc; 

	private AlertDialog dialog;

	private String title,info,t1="";
	
	private int pos;
	
	
	public DLoader(Context con)
	{
		this.con = con;			
		init("正在处理...", "处理中....", 0);
		append("处理中.....\n");
	}
	public DLoader(Context con, String title, String info, int pos){
		this.con = con;			
		init(title, info, pos);
		
	}

	public void init(String title, String info, int pos){
		vloader = LayoutInflater.from(con).inflate(R.layout.load, null);
		
		
	  loader =	new  AlertDialog.Builder(con)  
			.setTitle(title)  
			.setIcon(android.R.drawable.ic_dialog_info)  
			.setView(vloader)  
			.setPositiveButton("隐藏" , null)
			.setNegativeButton("复制", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					
					ClipboardManager clipboardManager = (ClipboardManager)con.getSystemService(Context.CLIPBOARD_SERVICE);  
					clipboardManager.setPrimaryClip(ClipData.newPlainText(null,loadText2.getText().toString()));  
					/*if (clipboardManager.hasPrimaryClip()){  
						clipboardManager.getPrimaryClip().getItemAt(0).getText();  
					} */ 
					Toast.makeText(con,"复制完成 !",Toast.LENGTH_LONG).show();
					
				}
				
				
			});
		loadText = (TextView)vloader.findViewById(R.id.loadTextView1);
		loadText2 = (TextView)vloader.findViewById(R.id.loadTextView2);
		loadText2.setMovementMethod(new ScrollingMovementMethod()) ;
		loadPb = (ProgressBar)vloader.findViewById(R.id.loadProgressBar2);
		loadPbc=(ProgressBar)vloader.findViewById(R.id.loadProgressBar1);
		iv=(ImageView)vloader.findViewById(R.id.loadImageView1);
		loadText.setText(info+"\n");
		loadPb.setProgress(pos);
		
		loadText2.setText(info+"\n");
		this.title=title;
		this.info=info;

	}
	public AlertDialog show(){
		try{
			dialog = loader.show();
			return dialog;
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}

	public void dimss(){
		try{
			dialog.dismiss();
		}
		catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	public void append(String str){
		str=str.replaceAll("\n","<br>");		
		Log.e("append_",str);
		t1+=str;
	  loadText2.append(Html.fromHtml(str));
	}

	public void setTitle(String str){
		dialog.setTitle(str);
		title=str;
	}

	public void setInfo(String str){
		loadText.setText(str);
		info=str;
	}

	public void setPnos(int pos)
	{
		loadPb.setProgress(pos);
		this.pos=pos;
	}
	public void setPToast(boolean toast)
	{
	  if(toast)
	  {
		loadPbc.setVisibility(View.VISIBLE);
		iv.setVisibility(View.INVISIBLE);
	  }
	  else
	  {
		loadPbc.setVisibility(View.INVISIBLE);
		iv.setVisibility(View.VISIBLE);
	  }
	  
	}
	public boolean isShowing()
	{
		return dialog.isShowing();
	}
	
	public void Showagain()
	{
		this.init(title,info,pos);
		this.append(t1);
		this.show();
	}
	
	public void dnotify ()
	{
		synchronized (con) {
			try {
				dialog.wait(0);
			}catch (Exception e) {
				// TODO: handle exception
			}
			dialog.notifyAll();
		}
		
	}
}
