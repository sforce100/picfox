package com.fox.net.upanddown;



import com.fox.picfox.R;
import com.fox.utils.BitmapUtils;
import com.fox.utils.NetUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class ShowImgOnline extends Activity{
	ImageView iv;
	Bitmap bmp;
	NetUtils net;
	String path;
	DownLoadTask down;
	byte[] bitmapByte;
	Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.onlineimg);
		
		
		//获得网络图片路径
		Intent intent=getIntent();
		path=intent.getStringExtra("path");
		//获得临时bitmap
		bundle=intent.getBundleExtra("bundle");
		bitmapByte=bundle.getByteArray("bitmap");
		bmp=BitmapUtils.byteArrayToBitmap(bitmapByte);

		iv=(ImageView)findViewById(R.id.online_imageview);
		iv.setImageBitmap(bmp);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "下载");
		menu.add(0, 1, 1, "删除");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case 0:
			//下载
			down=new DownLoadTask(ShowImgOnline.this, path);			
			down.execute();	
			break;
		case 1:
			//删除
			if(NetUtils.delete(ShowImgOnline.this,path))
				Toast.makeText(this.getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
			//返回图片列表Activity
			Intent to_ShowImgListActivity=new Intent();
			to_ShowImgListActivity.setClass(this, ShowImgListActivity.class);
			startActivity(to_ShowImgListActivity);
			this.finish();
			break;
		}
		//销毁窗口
		this.finish();
		return super.onOptionsItemSelected(item);
	}


}
