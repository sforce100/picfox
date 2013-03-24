package com.fox.utils.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUserOperator {

	SQLiteUserHelper userHelper;
	SQLiteDatabase db;
	ContentValues cv;
	Cursor cursor;
	public SQLiteUserOperator(Context context){
		userHelper=new SQLiteUserHelper(context);
		
	}
	//�������Ŀ¼
	public void addUser(String username,String password){
		cv=new ContentValues();
		cv.put(SQLiteUserHelper.USERNAME, username);
		cv.put(SQLiteUserHelper.PASSWORD, password);
		db=userHelper.getWritableDatabase();
		db.insert(SQLiteUserHelper.TB_NAME, null, cv);	
			
	}
	//��ѯĿ¼
	public ArrayList<UserTable> queryUser(){
		ArrayList<UserTable> list=new ArrayList<UserTable>();
		db=userHelper.getReadableDatabase();
		cursor=db.query(SQLiteUserHelper.TB_NAME, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
			UserTable info=new UserTable();
			info.setUsername(cursor.getString(cursor.getColumnIndex(SQLiteUserHelper.USERNAME)));
			info.setPassword(cursor.getString(cursor.getColumnIndex(SQLiteUserHelper.PASSWORD)));
			cursor.moveToNext();
			list.add(info);
		}
		//cursor.close();
		return list;
	}
	//���ٵ�¼��ʱ����
	public String queryUserByID(String dir){
		db=userHelper.getReadableDatabase();
		cursor=db.query(SQLiteUserHelper.TB_NAME, null,SQLiteUserHelper.USERNAME+ "=?", new String[]{dir}, null, null, null);
		cursor.moveToFirst();
		return cursor.getString(cursor.getColumnIndex(SQLiteUserHelper.USERNAME));
	}
	//ɾ��Ŀ¼
	public int deleteUser(String dir){
		db=userHelper.getWritableDatabase();
		//db.execSQL("DELETE FROM "+SQLiteUserHelper.TB_NAME+" WHERE "+SQLiteUserHelper.USERNAME+"="+dir);
		return db.delete(SQLiteUserHelper.TB_NAME, SQLiteUserHelper.USERNAME+"=?", new String[]{dir});

	}
	public boolean isRepeat(String dir){
		ArrayList<UserTable> list=queryUser();
		UserTable user;
		for(int i=0;i<list.size();i++){
			user=list.get(i);
			if(user.getUsername().equalsIgnoreCase(dir)){
				return true;
			}
		}	
		return false;
	}
	public void closeUserHelper(){
		if(userHelper!=null)userHelper.close();
		if(db!=null)db.close();
		if(cursor!=null)cursor.close();
	}
	


}
