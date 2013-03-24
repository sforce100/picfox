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
		//��wifi
		wifi.openWifi();
		//ɨ������
		wifi.startScan();
		//����
		wifi.tryConnect();
	}
	
	public void wifiClose(){
		
		 ConnectivityManager conMan = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		 //wifi Network
        State state = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //Log.d(wifi.toString(), null);
        
        //���3G�����wifi���綼δ���ӣ��Ҳ��Ǵ�����������״̬ �����Network Setting���� ���û�������������
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
//            //������
//             return true;
//       }else{
//            //do something
//            //��������
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
    	

        //���3G�����wifi���綼δ���ӣ��Ҳ��Ǵ�����������״̬ �����Network Setting���� ���û�������������
//        if(mobile==State.CONNECTED||wifi==State.CONNECTED)
//            return true;
     
        //c.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));//���������������ý���
        //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //�����ֻ��е�wifi�������ý���
        
    }
	public boolean isWifi(){
		  
		return wifi.isConnected();//����trueʱ��ʾ����
	}
	
}
