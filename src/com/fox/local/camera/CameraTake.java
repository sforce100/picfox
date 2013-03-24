package com.fox.local.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fox.picfox.R;
import com.fox.utils.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * 拍照
 * @author Administrator
 */
public class CameraTake extends Activity implements SurfaceHolder.Callback{
	
	
	
	private SurfaceView surfaceView;   //相机画布
	private SurfaceHolder surfaceHolder; 
	private Button takePicView,exitView;
	private int displayWidth,displayHeight;
	private Camera mCamera;    //照相机
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_take);
//这里我在AndroidManifest.xml的activity中添加了android:theme="@android:style/Theme.NoTitleBar.Fullscreen"

        /**
         * 隐藏状态栏和标题栏
         
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        */
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  //横屏
      /**
       * 获取屏幕大小
       */
      		DisplayMetrics dm=new DisplayMetrics();
      		this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
      		displayWidth=dm.heightPixels;
      		displayHeight=dm.widthPixels;
        /**
         * 获取Button并且设置事件监听
         */
        takePicView = (Button)this.findViewById(R.id.takepic);
        takePicView.setOnClickListener(TakePicListener);
        exitView = (Button)this.findViewById(R.id.exit);
        exitView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        surfaceView = (SurfaceView)this.findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFixedSize(displayWidth, displayHeight);
        
        checkSoftStage();  //首先检测SD卡是否存在
    }
	
	/**
	 * 检测手机是否存在SD卡,网络连接是否打开
	 */
	private void checkSoftStage(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //判断是否存在SD卡
//			String rootPath = Environment.getExternalStorageDirectory().getPath();  //获取SD卡的根目录
			File file = new File(Common.IMGTMPDIR);
			if(!file.exists()){
				file.mkdir();
			}
		}else{
			new AlertDialog.Builder(this).setMessage("检测到手机没有存储卡！请插入手机存储卡再开启本应用。")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
		}
	}
	
	/**
	 * 点击拍照按钮,启动拍照
	 */
	private final OnClickListener TakePicListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			mCamera.autoFocus(new AutoFoucus());  //自动对焦
		}
	};
	
	/**
	 * 自动对焦后拍照
	 * @author aokunsang
	 * @Date 2011-12-5
	 */
	private final class AutoFoucus implements AutoFocusCallback{
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if(success && mCamera!=null){
				mCamera.takePicture(mShutterCallback, null, mPictureCallback);
			}
		}
	}
	/**
	 * 重点对象、 此处实例化了一个本界面的PictureCallback
	 * 当用户拍完一张照片的时候触发，这时候对图片处理并保存操作。
	 * 
	 */
	private final PictureCallback mPictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				String fileName = System.currentTimeMillis()+".jpg";
				File file = new File(Common.IMGTMPDIR,fileName);
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
				bos.flush();
				bos.close();
               //////////////////////////////设置选中图片路径/////////////////////////////////////////////////
				Common.PATH=file.getPath();
				Intent intent = new Intent(CameraTake.this,Preview.class);
				startActivity(intent);
				
				CameraTake.this.finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	/** 
	* 在相机快门关闭时候的回调接口，通过这个接口来通知用户快门关闭的事件， 
	* 普通相机在快门关闭的时候都会发出响声，根据需要可以在该回调接口中定义各种动作， 例如：使设备震动 
	*/  
	private final ShutterCallback mShutterCallback = new ShutterCallback() {  
		public void onShutter() {  
			Log.d("ShutterCallback", "...onShutter...");  
		}  
	};
	
	@Override
	/**
	 * 初始化相机参数，比如相机的参数: 像素, 大小,格式
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters param = mCamera.getParameters();
		
		/**
		 * 设置拍照图片格式
		 */
		param.setPictureFormat(PixelFormat.JPEG);
		/**
		 * 设置预览尺寸【这里需要注意：预览尺寸有些数字正确，有些会报错，不清楚为啥】
		 */
		 Size s = param.getPictureSize();
		  double w = s.width;
		  double h = s.height;


		  if( width>height )
		  {
			  surfaceView.setLayoutParams(new LinearLayout.LayoutParams( (int)(height*(w/h)), height) );
			  
		  }else{
			  
			  surfaceView.setLayoutParams(new LinearLayout.LayoutParams( width, (int)(width*(h/w)) ) );
		  }
		  param.setPreviewSize(width, height); // 设置预览图像大小

		/**
		 * 设置图片大小
		 */
		//param.setPictureSize(320, 240);
		mCamera.setParameters(param);
		/**
		 * 开始预览
		 */
		mCamera.startPreview();
	}
	@Override
	/**
	 * 打开相机,设置预览
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();  //打开摄像头
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	/**
	 * 预览界面被关闭时，或者停止相机拍摄;释放相机资源
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		if(mCamera!=null) mCamera.release();
		mCamera = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_CAMERA){   //按下相机实体按键，启动本程序照相功能
			mCamera.autoFocus(new AutoFoucus());  //自动对焦
			return true;
		}else{
			return false;
		}
	}
}