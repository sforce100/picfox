package com.fox.utils;

import java.lang.ref.SoftReference;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.fox.local.editimage.EditActivity;
import com.fox.net.weibo.Authorize;
import com.fox.picfox.R;


/**
 * 拍照之后弹出的对话款
 * @author Administrator
 *
 */
public class ShowPhotoDialog extends Dialog{

	Button upload,weibo,edit,delete,rotate;
	ImageView iv;
	Context c;
	//
	Matrix matrix ;
	Rect rect;
	private ImageButton zoom_in,zoom_out;
	private PointF mid;
	
	Bitmap temp;
	int rotateD=0;
	
	SoftReference<Bitmap> soft;
	public ShowPhotoDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		c=context;
		temp=BitmapFactory.decodeFile(Common.PATH);
		soft=new SoftReference<Bitmap>(temp);
		//temp.recycle();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydialog_showphoto);
		init();
	    setListener();
	}
	public void init(){
		
		rotate=(Button)findViewById(R.id.showphoto_button_rotate);		
		edit=(Button)findViewById(R.id.showphoto_button_edit);
		delete=(Button)findViewById(R.id.showphoto_button_del);	
		edit.setOnClickListener(new PotoListener());
		delete.setOnClickListener(new PotoListener());
		rotate.setOnClickListener(new PotoListener());		
		iv = (ImageView) findViewById(R.id.showphoto_imageView);  
		iv.setImageBitmap(soft.get());
    	zoom_in = (ImageButton) findViewById(R.id.showphoto_zoom_in);
    	zoom_out = (ImageButton) findViewById(R.id.showphoto_zoom_out);		
    	matrix = new Matrix();
    	mid = new PointF();
    	
    	upload=(Button)findViewById(R.id.showphoto_button_upload);
		weibo=(Button)findViewById(R.id.showphoto_button_weibo);
		upload.setOnClickListener(new PotoListener());
		weibo.setOnClickListener(new PotoListener());
    	if(!Common.IsConnectNet){
    		upload.setEnabled(false);
    		weibo.setEnabled(false);
    	}
		
	}
	
    
    private void setListener(){
    	iv.setOnTouchListener(new MulitPointTouchListener());
    	 //放大
    	 zoom_in.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				matrix.set(iv.getImageMatrix());
				setMid();//设置放大的中心					
				matrix.postScale(1.3f, 1.3f, mid.x,mid.y);
				iv.setImageMatrix(matrix);
				iv.invalidate();
			}
		});
    	 //缩小
    	 zoom_out.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				matrix.set(iv.getImageMatrix());
				setMid();////设置缩小的中心			
				matrix.postScale(0.8f, 0.8f, mid.x,mid.y);
				iv.setImageMatrix(matrix);
				iv.invalidate();
			}
		});
    }
    private void setMid(){
    	rect = iv.getDrawable().getBounds();
		mid.x = iv.getDrawable().getBounds().centerX();
		mid.y = iv.getDrawable().getBounds().centerY();		
    }
    
    
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		soft.clear();
		temp.recycle();
	}


	class PotoListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.showphoto_button_weibo:
				//发送微博   test
				Authorize auth=new Authorize(c);
				auth.share();
				break;
			case R.id.showphoto_button_edit:
				//转向编辑菜单
				Intent to_editActivity=new Intent();
				to_editActivity.setClass(c, EditActivity.class);
				c.startActivity(to_editActivity);
				break;
			case R.id.showphoto_button_del:
				//删除
				MyDialog.delete_img(c, com.fox.utils.Common.PATH).show();
				break;
			case R.id.showphoto_button_upload:
				//上传图片
				MyDialog.pre_upload_ShowImage(c).show();
				break;
			case R.id.showphoto_button_rotate:
				rotateD+=45;
				rotateD%=360;
				//iv.setImageBitmap(BitmapUtils.rotateImage(Common.PATH, rotateD,soft));
				setMid();
				matrix.set(iv.getImageMatrix());			
				matrix.setRotate(rotateD,mid.x,mid.y);
				iv.setImageMatrix(matrix);
				iv.invalidate();
			}
		}	
	}
	
	
}