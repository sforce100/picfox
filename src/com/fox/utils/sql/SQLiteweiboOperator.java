package com.fox.utils.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteweiboOperator {
	SQLiteweiboHelper weiboHelper;
	SQLiteDatabase db;
	ContentValues cv;
	Cursor cursor;
	public SQLiteweiboOperator(Context context){
		weiboHelper=new SQLiteweiboHelper(context);
		
	}

	//Î¢²©±í²Ù×÷
		public void addWeibo(String userid,String token,String tokensecret,String access,String accessecret){
			cv=new ContentValues();
			cv.put(SQLiteweiboHelper.USERID, userid);
			cv.put(SQLiteweiboHelper.TOKEN, token);
			cv.put(SQLiteweiboHelper.TOKENSECRET, tokensecret);
			cv.put(SQLiteweiboHelper.ACCESS, access);
			cv.put(SQLiteweiboHelper.ACCESSSECRET, accessecret);
			db=weiboHelper.getWritableDatabase();
			db.insert(SQLiteweiboHelper.TB_NAME, null,cv );	
		}
		public ArrayList<WeiboTable> queryWeibo(){
			ArrayList<WeiboTable> list=new ArrayList<WeiboTable>();
			db=weiboHelper.getReadableDatabase();
			cursor=db.query(SQLiteweiboHelper.TB_NAME, null, null, null, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
				WeiboTable info=new WeiboTable();
				info.setID(cursor.getString(cursor.getColumnIndex(SQLiteweiboHelper.ID)));
				info.setUSERID(cursor.getString(cursor.getColumnIndex(SQLiteweiboHelper.USERID)));
				info.setTOKEN(cursor.getString(cursor.getColumnIndex(SQLiteweiboHelper.TOKEN)));
				info.setTOKENSECRET(cursor.getString(cursor.getColumnIndex(SQLiteweiboHelper.TOKENSECRET)));
				info.setACCESS(cursor.getString(cursor.getColumnIndex(SQLiteweiboHelper.ACCESS)));
				info.setACCESSSECRET(cursor.getString(cursor.getColumnIndex(SQLiteweiboHelper.ACCESSSECRET)));
				list.add(info);
				cursor.moveToNext();
			}
			return list;
		}
		public void closeWeiboHelper(){
			weiboHelper.close();
			db.close();
			cursor.close();
		}

}
