package com.HaP.Tool;
import android.content.*;
import android.net.*;
import android.net.wifi.*;
import android.os.*;
import android.util.*;
import java.net.*;
import java.util.*;

import android.net.Proxy;
//import android.database.sqlite.SqliteWrapper;
public class NetWork_info
{
  private Context con;
	NetworkInfo mobNetInfoActivity;
  ConnectivityManager connectivityManager;


	public NetWork_info()
	{

	}
	public NetWork_info(Context con)
	{
		this.con = con;	
		init();
	}


	public String getWifiInfo()
	{
		WifiManager wifi = (WifiManager)con.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String maxText = info.getMacAddress();
		String ipText = intToIp(info.getIpAddress());
		String status = "";
		if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
		{
			status = "已经连接";
		}
		else if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLING)
		{
			status = "连接中....";
		}
		else if (wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
		{
			status = "未连接";
		}
		else if (wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLING)
		{
			status = "断开连接中....";
		}
		else if (wifi.getWifiState() == WifiManager.WIFI_STATE_UNKNOWN)
		{
			status = "未知";
		}
		String ssid = info.getSSID();
		int networkID = info.getNetworkId();
		int speed = info.getLinkSpeed();
		return "Mac地址： " + maxText.trim() + "\n"
			+ "本地IP地址： " + ipText.trim() + "\n"
			+ "Wifi状态: " + status.trim() + "\n"
			+ "SSID: " + ssid.trim() + "\n"
			+ "Network id :" + networkID + "\n"
			+ "Connection Speed:" + speed + "\n"
			;
	}
	private String intToIp(int ip)
	{
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
			+ ((ip >> 24) & 0xFF);
	}

	public String getMobAPNName()
	{
		return mobNetInfoActivity.getExtraInfo();	
	}

	public String getMobtypeName()
	{
		return mobNetInfoActivity.getTypeName();
	}

	public String getMobSubName()
	{
		return mobNetInfoActivity.getSubtypeName();
	}

	public String getApnIP()
	{
		return Proxy.getDefaultHost();
	}

	public int getApnPort()
	{
		return Proxy.getDefaultPort();
	}
	public String getNetStatus()
	{
		if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) { 
			return "无网络";
		}else{

			int netType = mobNetInfoActivity.getType(); 
			if (netType == ConnectivityManager.TYPE_WIFI) { 
				// wifi net处理 		 
				return "wifi";
			} else if (netType == ConnectivityManager.TYPE_MOBILE) { 
			  //name = mobNetInfoActivity.getTypeName() + ":" + mobNetInfoActivity.getSubtypeName() + mobNetInfoActivity.getExtraInfo();
				return "手机网络";
			}else{
				return "获取失败";
			}
		}


	}
	private void init()
	{
		connectivityManager = (ConnectivityManager)con
			.getSystemService(Context.CONNECTIVITY_SERVICE); 
		mobNetInfoActivity = connectivityManager 
			.getActiveNetworkInfo(); 	

	}
	public static String getLocalIpAddress()
	{    

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {

			try
			{     
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()){
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			}
			catch (SocketException ex)
			{     
				Log.e("WifiPreference IpAddress", ex.toString());     
			}     
			return "获取失败 ";   	
	  }else{
			try{
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)){
							return inetAddress.getHostAddress().toString();
						}
					}
				}		
			}catch (Exception e)
			{			
				//return e.toString();
			}
			return "获取失败 ";
		}  
	}

}
