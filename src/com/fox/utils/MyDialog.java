package com.fox.utils;

import com.fox.net.login.LoginTabActivity;
import com.fox.net.upanddown.*;
import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import com.fox.utils.sql.SQLiteUserOperator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class MyDialog {
	//当Common.PATH为空时弹出..
	public static Dialog Img_path_null(Context context){
		Dialog notice=new AlertDialog.Builder(context)
			.setTitle("提示!")
			.setMessage("你还没选中图片!!请选中你要操作的图片!!")
			.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return notice;
	}
	//ShowImage中选择删除图片弹出
	public static Dialog delete_img(final Context context,final String path){
		Dialog delete=new AlertDialog.Builder(context)
			.setTitle("提示!!")
			.setMessage("你确定要删除图片吗?")
			.setPositiveButton("确定", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//删除图片
					BitmapUtils.deleteImage(context, path);
					//重新转载showiamge
					Intent refresh=new Intent();
					refresh.setClass(context, context.getClass());
					context.startActivity(refresh);
					dialog.cancel();
				}
			})
			.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return delete;
	}
	//ShowImage中选择上传图片弹出
	public static Dialog pre_upload_ShowImage(final Context context){
		Dialog uplodDialog=new AlertDialog.Builder(context)
			.setTitle("提示!!")
			.setMessage("你确定上传到当前登陆目录:"+(Common.DIR.equals("")?"null":Common.DIR))
			.setPositiveButton("确定", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					if(!Common.DIR.equals("")){
						//执行上传任务
		                UploadTask fileuploadtask = new UploadTask(context);  
		                fileuploadtask.execute();
					}
					else if(!Common.PATH.equals("")){
						LoginQuickDialog loginQuick=new LoginQuickDialog(context);
						loginQuick.setTitle("请选择上传用户目录");
						loginQuick.show();
					}
					else{MyDialog.Img_path_null(context).show();}
					
				}
			}).setNeutralButton("快速登陆", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					Intent to_LoginTabActivity=new Intent();
//					to_LoginTabActivity.setClass(context, LoginTabActivity.class);
//					context.startActivity(to_LoginTabActivity);
					dialog.cancel();
					LoginQuickDialog loginQuick=new LoginQuickDialog(context);
					loginQuick.setTitle("请选择上传用户目录");
					loginQuick.show();
					
				}
			})
			.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return uplodDialog;
	}
	//LoginQuickActivity中点解目录List时弹出
	public static Dialog loginQuickListClick(final Context context){
		Dialog uplodDialog=new AlertDialog.Builder(context)
			.setTitle("提示!!")
			.setMessage("当前选中目录:"+Common.DIR)
			.setPositiveButton("登录", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				
					Intent to_ShowImgListActivity=new Intent();
					to_ShowImgListActivity.setClass(context, ShowImgListActivity.class);
					context.startActivity(to_ShowImgListActivity);
				
				}
			}).setNeutralButton("删除", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SQLiteUserOperator sql=new SQLiteUserOperator(context);
					if(sql.deleteUser(Common.DIR)==1)Toast.makeText(context.getApplicationContext(), "成功删除目录!!", Toast.LENGTH_LONG);
					sql.closeUserHelper();
					Intent refush=new Intent();
					refush.setClass(context, LoginTabActivity.class);
					context.startActivity(refush);
				}
			})
			.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return uplodDialog;
	}
	//ShowImage中选择删除图片弹出
		public static Dialog saveTemp(final Context context,final String path){
			Dialog save=new AlertDialog.Builder(context)
				.setTitle("提示!!")
				.setMessage("你还没保存图片,是否要保存?")
				.setPositiveButton("确定", new OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						BitmapUtils.addImage(context);
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				}).create();
			return save;
		}
	
}


