package com.HaP.Byml;

import android.content.*;
import android.os.*;
import android.widget.*;
import com.HaP.Tool.*;
import android.util.*;


/* android.intent.action.BOOT_COMPLETEDReceiver摸 */
public class HippoStartupIntentReceiver extends BroadcastReceiver
{

	private SuperUser Su;
	private Handler handler=null;
	@Override
	public void onReceive(final Context context, Intent intent)
	{
		SharedPreferences sp=null;
		Su = new SuperUser(context);
		sp = context.getSharedPreferences("HaP", context.MODE_PRIVATE);
		if (sp.getBoolean("isStart", false))
		{
			if (SuperUser.checkRoot())
			{
			 
			//	Su.setHave_root(true);
		    //    Su.setIs_root(true);
			}
			//Toast.makeText(contet, "开机自启成功啦...😄--来自:HaProxy", Toast.LENGTH_SHORT).show();	
			//St.Exec(sp.getString("sshell", "echo '自启命令啥都木有！快去添加吧！'\n")).start();
			Su.msg("开机自启成功啦--来自HaP");
		    String priv_path=context.getFilesDir().getPath();
			String now_name=sp.getString("now_name", "默认透明代理");
			HPStart h_s = new HPStart(context, Su, sp, context.getFilesDir().getPath(), now_name, false, false);
			h_s.execute("start");
		    // h_s = new HPStart(MainActivity.this, Su, sp, Ft.getFILESPATH()+"/mode",sp.getString("sh_pstart", "pstart.sh") , false);
		    //h_s.execute("sh");
		    String st=sp.getString("sshell", "echo '自启命令啥都木有！");
			String sh="";
			int it=0;
			if((it=st.indexOf("sh ./"))>0)
			{	  
			  String stt[]=st.split("sh ./");
			  for(int i=0;i<stt.length;i++)
			  {
				int is=0;
				String sname="";
				if((is=stt[i].indexOf(".sh"))>0)
				{
				  sname=stt[i].substring(0,is+3);
				  sh+="\n"+FileTool.readSDFile(priv_path+"/mode/"+sname)+"\n";
				 // Log.e("HaP",sh);
				 }
			
			  }
			  sh=SuperUser.replace_code(context,sh,sp);		  
			}else
			sh="echo '什么都没有 ～～～'";
		    //FileTool.writeSDFile(sp.getString("sshell", "echo '自启命令啥都木有！快去添加吧！'\n"),"");
		    Su.Shell(sh,true).start();				
		}

	}


}
