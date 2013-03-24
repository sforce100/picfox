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
	//��Common.PATHΪ��ʱ����..
	public static Dialog Img_path_null(Context context){
		Dialog notice=new AlertDialog.Builder(context)
			.setTitle("��ʾ!")
			.setMessage("�㻹ûѡ��ͼƬ!!��ѡ����Ҫ������ͼƬ!!")
			.setPositiveButton("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return notice;
	}
	//ShowImage��ѡ��ɾ��ͼƬ����
	public static Dialog delete_img(final Context context,final String path){
		Dialog delete=new AlertDialog.Builder(context)
			.setTitle("��ʾ!!")
			.setMessage("��ȷ��Ҫɾ��ͼƬ��?")
			.setPositiveButton("ȷ��", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//ɾ��ͼƬ
					BitmapUtils.deleteImage(context, path);
					//����ת��showiamge
					Intent refresh=new Intent();
					refresh.setClass(context, context.getClass());
					context.startActivity(refresh);
					dialog.cancel();
				}
			})
			.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return delete;
	}
	//ShowImage��ѡ���ϴ�ͼƬ����
	public static Dialog pre_upload_ShowImage(final Context context){
		Dialog uplodDialog=new AlertDialog.Builder(context)
			.setTitle("��ʾ!!")
			.setMessage("��ȷ���ϴ�����ǰ��½Ŀ¼:"+(Common.DIR.equals("")?"null":Common.DIR))
			.setPositiveButton("ȷ��", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					if(!Common.DIR.equals("")){
						//ִ���ϴ�����
		                UploadTask fileuploadtask = new UploadTask(context);  
		                fileuploadtask.execute();
					}
					else if(!Common.PATH.equals("")){
						LoginQuickDialog loginQuick=new LoginQuickDialog(context);
						loginQuick.setTitle("��ѡ���ϴ��û�Ŀ¼");
						loginQuick.show();
					}
					else{MyDialog.Img_path_null(context).show();}
					
				}
			}).setNeutralButton("���ٵ�½", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					Intent to_LoginTabActivity=new Intent();
//					to_LoginTabActivity.setClass(context, LoginTabActivity.class);
//					context.startActivity(to_LoginTabActivity);
					dialog.cancel();
					LoginQuickDialog loginQuick=new LoginQuickDialog(context);
					loginQuick.setTitle("��ѡ���ϴ��û�Ŀ¼");
					loginQuick.show();
					
				}
			})
			.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return uplodDialog;
	}
	//LoginQuickActivity�е��Ŀ¼Listʱ����
	public static Dialog loginQuickListClick(final Context context){
		Dialog uplodDialog=new AlertDialog.Builder(context)
			.setTitle("��ʾ!!")
			.setMessage("��ǰѡ��Ŀ¼:"+Common.DIR)
			.setPositiveButton("��¼", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				
					Intent to_ShowImgListActivity=new Intent();
					to_ShowImgListActivity.setClass(context, ShowImgListActivity.class);
					context.startActivity(to_ShowImgListActivity);
				
				}
			}).setNeutralButton("ɾ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SQLiteUserOperator sql=new SQLiteUserOperator(context);
					if(sql.deleteUser(Common.DIR)==1)Toast.makeText(context.getApplicationContext(), "�ɹ�ɾ��Ŀ¼!!", Toast.LENGTH_LONG);
					sql.closeUserHelper();
					Intent refush=new Intent();
					refush.setClass(context, LoginTabActivity.class);
					context.startActivity(refush);
				}
			})
			.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
		return uplodDialog;
	}
	//ShowImage��ѡ��ɾ��ͼƬ����
		public static Dialog saveTemp(final Context context,final String path){
			Dialog save=new AlertDialog.Builder(context)
				.setTitle("��ʾ!!")
				.setMessage("�㻹û����ͼƬ,�Ƿ�Ҫ����?")
				.setPositiveButton("ȷ��", new OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						BitmapUtils.addImage(context);
						dialog.cancel();
					}
				})
				.setNegativeButton("ȡ��", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				}).create();
			return save;
		}
	
}


