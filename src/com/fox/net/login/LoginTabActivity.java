package com.fox.net.login;

import java.util.Date;

import com.fox.picfox.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;



public class LoginTabActivity extends TabActivity implements android.widget.TabHost.TabContentFactory {


	private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTab();
	}

	private void setTab(){
		TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.login_tab, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("¿ìËÙµÇÂ¼").setContent(new Intent(this, LoginQuickActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("µÇÂ¼×¢²á").setContent(new Intent(this, LoginRegisterActivity.class)));

        //tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("ÎÂÜ°ÌáÊ¾").setContent(R.id.login_tab_tv));	
	}
	
	@Override
	public View createTabContent(String tag) {
		// TODO Auto-generated method stub
		tv=(TextView) findViewById(R.id.login_tab_tv);
		tv.append("\n"+new Date().toString());
		return tv;
	}

}
