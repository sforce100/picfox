package com.fox.local.editimage;

import com.fox.local.editimage.ColorPickerDialog.OnColorChangedListener;
import com.fox.net.weibo.Authorize;
import com.fox.picfox.R;
import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import com.fox.utils.MyDialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class EditActivity extends Activity{

	public static final int ACTION_NORMAL=1;
	public static final int ACTION_DRAW=2;
	
	

	private LinearLayout painLayout,edit_eraser,penLayout;
	private EditView ev;
	private Button eraser_commit,but_bigger,but_smaller;
	private Button eraser,pen,reset;
	private SeekBar eraser_size;
	private Bitmap bmp;
	private PenStyle penSet;//画笔
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editimage);
		//初始化控件
		InitEditView();
		//编辑图片
		//bmp=BitmapFactory.decodeFile(Common.PATH);
		bmp=BitmapUtils.decodeBitmap(EditActivity.this, Common.PATH);
		ev=new EditView(this, bmp);
		//把画板view放到布局里面
		painLayout.addView(ev);
		//hs.addView(ev);//不成功
		
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "分享");
		menu.add(0, 1, 1, "保存");
		menu.add(0, 2, 2, "设置");
		menu.add(0,3,3,"上传");
		if(!Common.IsConnectNet){
			menu.getItem(0).setEnabled(false);
			menu.getItem(3).setEnabled(false);
			
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case 0:
			//发送微博   test
			Authorize auth=new Authorize(EditActivity.this);
			auth.share();
			break;
		case 1:
			//保存图片
			BitmapUtils.addImage(this, ev);
			Toast.makeText(EditActivity.this, "已经成功保存到SD卡", Toast.LENGTH_LONG).show();
			break;
		case 2:
		
			penLayout.setVisibility(View.VISIBLE);
			break;
		case 3:
			//上传图片
			MyDialog.pre_upload_ShowImage(EditActivity.this).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 初始化
	 */
	private void InitEditView(){
		//画笔
		penSet=new PenStyle();
		//初始化工具栏控件
		eraser=(Button)findViewById(R.id.era);
		eraser.setOnClickListener(new imageButtonListener());
		pen=(Button)findViewById(R.id.pen);
		pen.setOnClickListener(new imageButtonListener());
		reset=(Button)findViewById(R.id.ret);
		reset.setOnClickListener(new imageButtonListener());
		//按钮
		//pen_commit=(Button)findViewById(R.id.edit_pen_commit);
		eraser_commit=(Button)findViewById(R.id.edit_eraser_commit);		
		eraser_commit.setOnClickListener(new buttonListener());
		
		but_bigger=(Button)findViewById(R.id.but_big);
		but_smaller=(Button)findViewById(R.id.but_small);
		but_bigger.setOnClickListener(new EditState());
		but_smaller.setOnClickListener(new EditState());
		
		//画笔的颜色和size

		eraser_size=(SeekBar)findViewById(R.id.eraser_size);
		eraser_size.setOnSeekBarChangeListener(new PenChange());
		
		//
		
		//初始化layout

		painLayout=(LinearLayout)findViewById(R.id.paintLayout);
		edit_eraser=(LinearLayout)findViewById(R.id.edit_eraser_layout);
		penLayout=(LinearLayout)findViewById(R.id.penLayout);


		
	}
	/**
	 * 设置editimage按钮监听
	 * @author Administrator
	 *
	 */
	class buttonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.edit_eraser_commit:
				ev.UpPenStyle(penSet, "eraser");
				edit_eraser.setVisibility(View.INVISIBLE);
				break;
				
			}
			//penLayout.setVisibility(View.INVISIBLE);
		}
		
	}
	/**
	 * 设置工具栏监听
	 * @author Administrator
	 *
	 */
	class imageButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.era:
				if(edit_eraser.isShown())edit_eraser.setVisibility(View.INVISIBLE);
				else edit_eraser.setVisibility(View.VISIBLE);
				break;
			case R.id.pen:
				new ColorPickerDialog(EditActivity.this, new OnColorChangedListener() {
					public void colorChanged(int color,int size) {
						penSet.setpColor(color);
						penSet.setpSize(size);
						ev.UpPenStyle(penSet, "pen");
						
						//toolsPopupWindow.dismiss();
					}
				}, penSet.getpColor()).show();
//				if(edit_pen.isShown())edit_pen.setVisibility(View.INVISIBLE);
//				else edit_pen.setVisibility(View.VISIBLE);
				break;
			case R.id.ret:
				ev.initBiamap(bmp);
				break;
			}
			//penLayout.setVisibility(View.INVISIBLE);
		}
		
	}
	/**
	 * 放大缩小按钮监听
	 * @author Administrator
	 *
	 */
	class EditState implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch( v.getId()){
			case R.id.but_big:
				ev.setState(EditActivity.ACTION_DRAW);
				
				break;
			case R.id.but_small:
				ev.setState(EditActivity.ACTION_NORMAL);
				break;
			}
			//penLayout.setVisibility(View.INVISIBLE);
		}
	
	}
	/**
	 * 设置senkbar监听
	 * @author Administrator
	 *
	 */
	class PenChange implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

			switch(seekBar.getId()){
			
			case R.id.eraser_size:
				penSet.setpSize(progress);
				break;
			}
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		
			
		}


	}
}
