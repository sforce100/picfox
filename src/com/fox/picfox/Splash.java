package com.fox.picfox;

import com.fox.net.netmanager.ConManager;
import com.fox.utils.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageInfo;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.graphics.PixelFormat;

import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;

import android.view.WindowManager;

import android.widget.TextView;

public class Splash extends Activity {

	private ConManager con;
	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);

		con = new ConManager(this);
		
		getWindow().setFormat(PixelFormat.RGBA_8888);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {

			public void run() {

				/* Create an Intent that will start the Main WordPress Activity. */
				
				
				if(con.isConnected()!=null){
					Common.IsConnectNet = true;
					if(con.isWifi()){
						Common.IsWifiConnect = true;
					}
				}

				
				Intent mainIntent = new Intent(Splash.this,
						PicFoxActivity.class);
				Splash.this.startActivity(mainIntent);

				Splash.this.finish();
				
				SharedPreferences sp = getSharedPreferences("PICFORSKIN",
						MODE_WORLD_READABLE);
				SharedPreferences.Editor editor = sp.edit();
				String skin = sp.getString("SKIN", null);
				if(skin ==null || skin ==""){
					editor.putString("SKIN", "0");
					editor.commit();
				}
			}

		}, 4900); // 4900 for release

	}

}
