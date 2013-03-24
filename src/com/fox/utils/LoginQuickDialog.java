package com.fox.utils;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.fox.net.upanddown.UploadTask;
import com.fox.picfox.R;
import com.fox.utils.sql.SQLiteUserOperator;
import com.fox.utils.sql.UserTable;


/**
 * 快速登录对话框
 * @author Administrator
 *
 */
public class LoginQuickDialog extends Dialog{
	
	ScrollView sv;
	Button commit,cancle;
	SQLiteUserOperator sql;
	ArrayList<UserTable> list;
	RadioButton rb;
	RadioGroup rg;
	Context c;
	public LoginQuickDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		c=context;
		sql=new SQLiteUserOperator(context);
		list=new ArrayList<UserTable>();
		list=sql.queryUser();
		sql.closeUserHelper();
		rg=new RadioGroup(context);
		for(int i=0;i<list.size();i++){
			UserTable user=list.get(i);
			rb=new RadioButton(context);
			rb.setId(i);
			rb.setText(user.getUsername());
			rg.addView(rb);				
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydialog_quicklogin);
		commit=(Button)findViewById(R.id.dialog_commit);
		cancle=(Button)findViewById(R.id.dialog_cancle);
		if(list.size()==0)commit.setEnabled(false);//若用户目录为空,提交按钮不能用
		
		sv=(ScrollView)findViewById(R.id.dialog_scrollview);
		sv.addView(rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				//快速登陆---即设置Common.DIR
				RadioButton b=(RadioButton) group.getChildAt(checkedId);
				Common.DIR=b.getText().toString();
			}
		});
		
		commit.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginQuickDialog.this.cancel();
				//执行上传任务
                UploadTask fileuploadtask = new UploadTask(c);  
                fileuploadtask.execute();
				
			}

		});
		cancle.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginQuickDialog.this.cancel();
				
			}

		});
	}
}	