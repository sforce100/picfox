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
		
		
		//�������ͼƬ·��
		Intent intent=getIntent();
		path=intent.getStringExtra("path");
		//�����ʱbitmap
		bundle=intent.getBundleExtra("bundle");
		bitmapByte=bundle.getByteArray("bitmap");
		bmp=BitmapUtils.byteArrayToBitmap(bitmapByte);

		iv=(ImageView)findViewById(R.id.online_imageview);
		iv.setImageBitmap(bmp);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "����");
		menu.add(0, 1, 1, "ɾ��");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case 0:
			//����
			down=new DownLoadTask(ShowImgOnline.this, path);			
			down.execute();	
			break;
		case 1:
			//ɾ��
			if(NetUtils.delete(ShowImgOnline.this,path))
				Toast.makeText(this.getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_LONG).show();
			//����ͼƬ�б�Activity
			Intent to_ShowImgListActivity=new Intent();
			to_ShowImgListActivity.setClass(this, ShowImgListActivity.class);
			startActivity(to_ShowImgListActivity);
			this.finish();
			break;
		}
		//���ٴ���
		this.finish();
		return super.onOptionsItemSelected(item);
	}


}
