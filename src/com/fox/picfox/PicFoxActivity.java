package com.fox.picfox;


import com.fox.local.camera.Preview;
import com.fox.local.camera.StartCamera;
import com.fox.net.login.LoginTabActivity;
import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;


public class PicFoxActivity extends Activity {
 
	private ImageButton online,local,takephoto;
	private Uri photoUri ;
	private StartCamera camera;
	private String skin = "0";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        online=(ImageButton)findViewById(R.id.main_online);
        local=(ImageButton)findViewById(R.id.main_local);
        takephoto=(ImageButton)findViewById(R.id.main_takephoto);
        online.setOnTouchListener(onlineListener);
		local.setOnTouchListener(localListener);
		takephoto.setOnTouchListener(takephotoListener);
		
        //初始化皮肤
        initSkin();
        
        camera=new StartCamera(this);
        
        if(Common.IsWifiConnect)
        	Toast.makeText(this, "已启动wifi优化", Toast.LENGTH_LONG);
    }
    
    
    public void initSkin() {
		SharedPreferences sp = getSharedPreferences("PICFORSKIN",
													MODE_WORLD_READABLE);
		skin = sp.getString("SKIN", null);
		if (skin.equals("0")) {
			getWindow().setBackgroundDrawableResource(R.drawable.skyblue);
			online.setImageResource(R.drawable.interneta);
			takephoto.setImageResource(R.drawable.cameraa);
			local.setImageResource(R.drawable.abluma);
		} else if (skin.equals("1")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_g);
			online.setImageResource(R.drawable.interneta_g);
			takephoto.setImageResource(R.drawable.cameraa_g);
			local.setImageResource(R.drawable.abluma_g);
		} else if (skin.equals("2")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_r);
			online.setImageResource(R.drawable.interneta_r);
			takephoto.setImageResource(R.drawable.cameraa_r);
			local.setImageResource(R.drawable.abluma_r);
		}
	}
    
    /**
	 * 创建底部菜单
	 * */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 1, "更换皮肤");
		return true;
	}

	/**
	 * 点击底部菜单项时的处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent setSking = new Intent();
			setSking.setClass(PicFoxActivity.this, SetLevel.class);
			startActivity(setSking);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		if(!Common.IsConnectNet)
			online.setEnabled(false);
		initSkin();
		super.onResume();
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == camera.IMAGE_CAPTURE) {
				if (resultCode == RESULT_OK){
					Common.PATH=BitmapUtils.getAbsoluteImagePath(PicFoxActivity.this, camera.getImageUri());
					Intent intent = new Intent(PicFoxActivity.this,Preview.class);
					startActivity(intent);
				}
			}
	}
    
    
	/**
	 * 当主程序退出时,把拍照等临时文件删除;
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BitmapUtils.deleteTempImage(this);
	}
	
	
	
	OnTouchListener onlineListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				online.setImageResource(R.drawable.internetb);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (skin.equals("0")) {
					online.setImageResource(R.drawable.interneta);
				} else if (skin.equals("1")) {
					online.setImageResource(R.drawable.interneta_g);
				} else if (skin.equals("2")) {
					online.setImageResource(R.drawable.interneta_r);
				}
			}	
			Intent to_LoginRegisterActivity=new Intent();
			to_LoginRegisterActivity.setClass(PicFoxActivity.this, LoginTabActivity.class);
			startActivity(to_LoginRegisterActivity);

			return false;
	  }
	};
	
	OnTouchListener localListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				local.setImageResource(R.drawable.ablumb);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (skin.equals("0")) {
					local.setImageResource(R.drawable.abluma);
				} else if (skin.equals("1")) {
					local.setImageResource(R.drawable.abluma_g);
				} else if (skin.equals("2")) {
					local.setImageResource(R.drawable.abluma_r);
				}
				
			}
			Intent to_showImage=new Intent();
			to_showImage.setClass(PicFoxActivity.this, ShowImage.class);
			startActivity(to_showImage);
			return false;
		}
	};
	
	OnTouchListener takephotoListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				takephoto.setImageResource(R.drawable.camerab);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (skin.equals("0")) {
					takephoto.setImageResource(R.drawable.cameraa);
				} else if (skin.equals("1")) {
					takephoto.setImageResource(R.drawable.cameraa_g);
				} else if (skin.equals("2")) {
					takephoto.setImageResource(R.drawable.cameraa_r);
				}
				
			}
			//调用内置相机
			camera.startCamera();
			return false;
		}
	};
}