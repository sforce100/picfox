package com.fox.net.netmanager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

public class ConManager {
	
	private Context c;
	private WifiAdmin wifi;
	public ConManager(Context c){
		this.c=c;
		wifi=new WifiAdmin(c);
	}

	public void wifiCon(){
		//打开wifi
		wifi.openWifi();
		//扫描网络
		wifi.startScan();
		//连接
		wifi.tryConnect();
	}
	
	public void wifiClose(){
		
		 ConnectivityManager conMan = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		 //wifi Network
        State state = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //Log.d(wifi.toString(), null);
        
        //如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if(state==State.CONNECTED)
        	wifi.closeWifi();
	}
	
	
	public String isConnected()
    {

        //mobile 3G Network
        //State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        //Log.d(mobile.toString(), null);
        //wifi Network
        //State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //Log.d(wifi.toString(), null);
        
//        NetworkInfo info=conMan.getActiveNetworkInfo();
//        if (info != null && info.isAvailable()){
//            //do something
//            //能联网
//             return true;
//       }else{
//            //do something
//            //不能联网
//             return false;
//       }
        
    		try {
    			for (Enumeration<NetworkInterface> en = NetworkInterface
    					.getNetworkInterfaces(); en.hasMoreElements();) {
    				NetworkInterface intf = en.nextElement();
    				for (Enumeration<InetAddress> enumIpAddr = intf
    						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
    					InetAddress inetAddress = enumIpAddr.nextElement();
    					if (!inetAddress.isLoopbackAddress()) {
    						return inetAddress.getHostAddress().toString();
    					}
    				}
    			}
    		} catch (SocketException ex) {
    			Log.e("WifiPreference IpAddress", ex.toString());
    		}
    		return null;
    	

        //如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
//        if(mobile==State.CONNECTED||wifi==State.CONNECTED)
//            return true;
     
        //c.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));//进入无线网络配置界面
        //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //进入手机中的wifi网络设置界面
        
    }
	public boolean isWifi(){
		  
		return wifi.isConnected();//返回true时表示存在
	}
	
}
