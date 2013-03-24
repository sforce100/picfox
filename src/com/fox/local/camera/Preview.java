package com.fox.local.camera;


import com.fox.local.editimage.EditActivity;
import com.fox.net.weibo.Authorize;
import com.fox.picfox.R;
import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import com.fox.utils.MulitPointTouchListener;
import com.fox.utils.MyDialog;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class Preview extends Activity{

	private ImageView iv;
	private boolean isSave=false;
	private StartCamera camera;
	private Matrix matrix ;
	private PointF mid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bitmap bmp=BitmapFactory.decodeFile(Common.PATH);
		setContentView(R.layout.pic_take_preview);
		iv=(ImageView)findViewById(R.id.preview_imageview);
		iv.setImageBitmap(bmp);
		matrix=new Matrix();
		matrix.set(iv.getImageMatrix());
		matrix.setRotate(90);	
		mid=new PointF();
		setMid();
		DisplayMetrics dm=new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
		int displayWidth=dm.heightPixels;
		matrix.postTranslate(mid.x+displayWidth,mid.y);
		iv.setImageMatrix(matrix);
		iv.setOnTouchListener(new MulitPointTouchListener());
		iv.invalidate();
		
		camera=new StartCamera(this);
	}

	 private void setMid(){
	    	iv.getDrawable().getBounds();
			mid.x = iv.getDrawable().getBounds().centerX();
			mid.y = iv.getDrawable().getBounds().centerY();		
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "分享");
		menu.add(0, 1, 1, "编辑");
		menu.add(0, 2, 2, "保存");
		menu.add(0,3,3,"上传");
		menu.add(0, 4, 4, "重拍");
		if(!Common.IsConnectNet){
			menu.getItem(0).setEnabled(false);
			menu.getItem(3).setEnabled(false);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == camera.IMAGE_CAPTURE) {

			if (resultCode == RESULT_OK){
				Common.PATH=BitmapUtils.getAbsoluteImagePath(Preview.this, camera.getImageUri());
				Intent intent = new Intent(Preview.this,Preview.class);
				startActivity(intent);
			}

			}
	
	}
    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case 0:
			//发送微博   test
//			Intent share=new Intent();
//			share.setClass(Preview.this, com.fox.net.weibo.AuthorizeActivity.class);
//			Preview.this.startActivity(share);
//			this.finish();
			Authorize auth=new Authorize(Preview.this);
			auth.share();
			break;
		case 1:
			//转向编辑菜单
			editImage();
			break;
		case 2:
			//保存
			BitmapUtils.addImage(this);
			isSave=true;
			//关闭预览窗口--->照相			
			break;
		case 3:
			//上传图片
			MyDialog.pre_upload_ShowImage(Preview.this).show();
			break;
		case 4:
			camera.startCamera();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		if(!isSave){
			MyDialog.saveTemp(this, Common.PATH);
			super.onDestroy();
		}else{
			super.onDestroy();
		}
		
	}
	public void takeAgain(){
		Intent to_CameraTake=new Intent();
		to_CameraTake.setClass(Preview.this, CameraTake.class);
		startActivity(to_CameraTake);
		this.finish();
	}
	public void editImage(){
		Intent to_editActivity=new Intent();
		to_editActivity.setClass(Preview.this, EditActivity.class);
		Preview.this.startActivity(to_editActivity);
		this.finish();
	}
	
}
