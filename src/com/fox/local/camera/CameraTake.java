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
 * ����
 * @author Administrator
 */
public class CameraTake extends Activity implements SurfaceHolder.Callback{
	
	
	
	private SurfaceView surfaceView;   //�������
	private SurfaceHolder surfaceHolder; 
	private Button takePicView,exitView;
	private int displayWidth,displayHeight;
	private Camera mCamera;    //�����
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_take);
//��������AndroidManifest.xml��activity�������android:theme="@android:style/Theme.NoTitleBar.Fullscreen"

        /**
         * ����״̬���ͱ�����
         
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        */
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  //����
      /**
       * ��ȡ��Ļ��С
       */
      		DisplayMetrics dm=new DisplayMetrics();
      		this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
      		displayWidth=dm.heightPixels;
      		displayHeight=dm.widthPixels;
        /**
         * ��ȡButton���������¼�����
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
        
        checkSoftStage();  //���ȼ��SD���Ƿ����
    }
	
	/**
	 * ����ֻ��Ƿ����SD��,���������Ƿ��
	 */
	private void checkSoftStage(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //�ж��Ƿ����SD��
//			String rootPath = Environment.getExternalStorageDirectory().getPath();  //��ȡSD���ĸ�Ŀ¼
			File file = new File(Common.IMGTMPDIR);
			if(!file.exists()){
				file.mkdir();
			}
		}else{
			new AlertDialog.Builder(this).setMessage("��⵽�ֻ�û�д洢����������ֻ��洢���ٿ�����Ӧ�á�")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
		}
	}
	
	/**
	 * ������հ�ť,��������
	 */
	private final OnClickListener TakePicListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			mCamera.autoFocus(new AutoFoucus());  //�Զ��Խ�
		}
	};
	
	/**
	 * �Զ��Խ�������
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
	 * �ص���� �˴�ʵ������һ���������PictureCallback
	 * ���û�����һ����Ƭ��ʱ�򴥷�����ʱ���ͼƬ�������������
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
               //////////////////////////////����ѡ��ͼƬ·��/////////////////////////////////////////////////
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
	* ��������Źر�ʱ��Ļص��ӿڣ�ͨ������ӿ���֪ͨ�û����Źرյ��¼��� 
	* ��ͨ����ڿ��Źرյ�ʱ�򶼻ᷢ��������������Ҫ�����ڸûص��ӿ��ж�����ֶ����� ���磺ʹ�豸�� 
	*/  
	private final ShutterCallback mShutterCallback = new ShutterCallback() {  
		public void onShutter() {  
			Log.d("ShutterCallback", "...onShutter...");  
		}  
	};
	
	@Override
	/**
	 * ��ʼ�������������������Ĳ���: ����, ��С,��ʽ
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters param = mCamera.getParameters();
		
		/**
		 * ��������ͼƬ��ʽ
		 */
		param.setPictureFormat(PixelFormat.JPEG);
		/**
		 * ����Ԥ���ߴ硾������Ҫע�⣺Ԥ���ߴ���Щ������ȷ����Щ�ᱨ�������Ϊɶ��
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
		  param.setPreviewSize(width, height); // ����Ԥ��ͼ���С

		/**
		 * ����ͼƬ��С
		 */
		//param.setPictureSize(320, 240);
		mCamera.setParameters(param);
		/**
		 * ��ʼԤ��
		 */
		mCamera.startPreview();
	}
	@Override
	/**
	 * �����,����Ԥ��
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();  //������ͷ
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	/**
	 * Ԥ�����汻�ر�ʱ������ֹͣ�������;�ͷ������Դ
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		if(mCamera!=null) mCamera.release();
		mCamera = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_CAMERA){   //�������ʵ�尴�����������������๦��
			mCamera.autoFocus(new AutoFoucus());  //�Զ��Խ�
			return true;
		}else{
			return false;
		}
	}
}