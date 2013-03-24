package com.fox.net.netmanager;

import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class WifiTestActivity extends Activity{
	WifiManager wifimanager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public void scan(){
		//实例化WifiManager对象
		wifimanager=(WifiManager) getSystemService(Context.WIFI_SERVICE);
		//启动扫描
		wifimanager.startScan();
		
		//注册针对SCAN_PRESULTS_AVAILABLE intent的BroadcastReceiver
		registerReceiver(rcvWifiScan, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	//调用getScanResult()方法获取ScanResult对象列表
	//遍历结果,利用他们实现一些功能
	BroadcastReceiver rcvWifiScan = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			List<ScanResult> resultList=wifimanager.getScanResults();//获得扫描实例集
			int foundCount=resultList.size();
			
			Toast.makeText(WifiTestActivity.this, "scan done, "+foundCount+" found", Toast.LENGTH_LONG).show();
			ListIterator<ScanResult> results=resultList.listIterator();//转化为Iterator
			String fullInfo="scan result:\n";
			while(results.hasNext()){
				ScanResult info=results.next();//取出扫描结果
				String wifiInfo="name:"+info.BSSID+";  capabilities="+info.capabilities+"; sig str="+info.level+"dBm";
				Log.v("wifi", wifiInfo);
				fullInfo+=wifiInfo+"\n";
			}
		}
	};
	

}
