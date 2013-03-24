package com.fox.net.login;

import com.fox.picfox.R;
import com.fox.utils.NetUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginRegisterActivity extends Activity {


	private EditText dir,pw;
	private Button login,register,canel;
	private NetUtils net;
	private String skin = "0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_loginregister);
		net=new NetUtils();//网络操作类
		dir=(EditText)findViewById(R.id.register_dir);
		pw=(EditText)findViewById(R.id.register_pw);
		
		login=(Button)findViewById(R.id.register_login);
		register=(Button)findViewById(R.id.register_register);
		canel=(Button)findViewById(R.id.register_cancel);
		login.setOnClickListener(new ButtonListener());
		register.setOnClickListener(new ButtonListener());
		canel.setOnClickListener(new ButtonListener());
		
		initSkin();
	}

	public void initSkin() {
		SharedPreferences sp = getSharedPreferences("PICFORSKIN",
													MODE_WORLD_READABLE);
		skin = sp.getString("SKIN", null);
		if (skin.equals("0")) {
			getWindow().setBackgroundDrawableResource(R.drawable.skyblue);
			canel.setBackgroundColor(R.drawable.skyblue);
		} else if (skin.equals("1")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_g);
			canel.setBackgroundColor(R.drawable.skyg);
		} else if (skin.equals("2")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_r);
			canel.setBackgroundColor(R.drawable.skyr);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initSkin();
	}	
	
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!dir.getText().toString().trim().equals("") && !pw.getText().toString().trim().equals("")){
				switch(v.getId()){
				case R.id.register_login:
					net.login(LoginRegisterActivity.this, dir.getText().toString(), pw.getText().toString());
					break;
				case R.id.register_register:
					net.register(LoginRegisterActivity.this, dir.getText().toString(), pw.getText().toString());
					break;
				case R.id.register_cancel:
					break;
				}
			}
			else{
				System.out.println("check it null");
				new AlertDialog.Builder(LoginRegisterActivity.this)
					.setTitle("提示!!")
					.setMessage("用户名和密码不能为空!!")
					.setPositiveButton("OK", null)
					.create()
					.show();
			}
			
		}
		
	}
}
