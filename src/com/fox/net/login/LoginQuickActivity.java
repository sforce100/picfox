package com.fox.net.login;

import java.util.ArrayList;
import com.fox.picfox.R;
import com.fox.utils.Common;
import com.fox.utils.MyDialog;
import com.fox.utils.sql.SQLiteUserOperator;
import com.fox.utils.sql.UserTable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LoginQuickActivity extends Activity{

	private ListView quickLogin;
	private String skin = "0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_quicklogin);
		//¿ìËÙµÇÂ¼
		quickLogin=(ListView)findViewById(R.id.login_userlist);
		ListuserAdapter la=new ListuserAdapter(this);
		quickLogin.setAdapter(la);
		quickLogin.setOnItemClickListener(new ListSelected());
		initSkin();
	}

	public void initSkin() {
		SharedPreferences sp = getSharedPreferences("PICFORSKIN",
													MODE_WORLD_READABLE);
		skin = sp.getString("SKIN", null);
		if (skin.equals("0")) {
			getWindow().setBackgroundDrawableResource(R.drawable.skyblue);
		} else if (skin.equals("1")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_g);
		} else if (skin.equals("2")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_r);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initSkin();
	}

	
	class ListSelected implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int line,
				long arg3) {
			// TODO Auto-generated method stub
			TextView tv=(TextView) view;		
			SQLiteUserOperator sql=new SQLiteUserOperator(LoginQuickActivity.this);
			Common.DIR=sql.queryUserByID(tv.getText().toString());
			sql.closeUserHelper();
			MyDialog.loginQuickListClick(LoginQuickActivity.this).show();
			
			
		}
		
	}
	class ListuserAdapter extends BaseAdapter{
		SQLiteUserOperator sql;
		ArrayList<UserTable> list;
		ArrayList<String> dirs;
		Context c;
		public ListuserAdapter(Context c){
			dirs=new ArrayList<String>();
			this.c=c;
			sql=new SQLiteUserOperator(LoginQuickActivity.this);
			list=sql.queryUser();
			for(int i=0;i<list.size();i++){
				dirs.add(list.get(i).getUsername());
			}
			sql.closeUserHelper();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dirs.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv=new TextView(c);
			tv.setTextSize(35);
			tv.setWidth(400);
			tv.setText(dirs.get(position));
			return tv;
		}
		
	}
}
