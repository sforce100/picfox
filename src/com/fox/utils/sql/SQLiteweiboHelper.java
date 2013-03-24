package com.fox.utils.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteweiboHelper extends SQLiteOpenHelper{
	
	public static final String TB_NAME="weibo"; 
	public static final String ID="ID";
	public static final String USERID="USERID";
	public static final String TOKEN="TOKEN";
	public static final String TOKENSECRET="TOKENSECRET";
	public static final String ACCESS="ACCESS";
	public static final String ACCESSSECRET="ACCESSSECRET";

	public SQLiteweiboHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public SQLiteweiboHelper(Context context,String name,CursorFactory factory){
		this(context, name, factory, 1);
	}
	public SQLiteweiboHelper(Context context){
		this(context,com.fox.utils.Common.DATABASENAME, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		   db.execSQL("CREATE TABLE IF NOT EXISTS "+ 
				  TB_NAME+"(" +
				  ID +" integer primary key,"+ 
				  USERID +" varchar(20),"+
				  TOKEN +" varchar(20),"+
				  TOKENSECRET +" varchar(20),"+
				  ACCESS +" varchar(20),"+
				  ACCESSSECRET +" varchar(20)" +
				  ")" 
				 ); 
		   Log.e("Database","onCreate"); 
		} 
		//更新表 
		@Override 
		 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
			 db.execSQL("DROP TABLE IF EXISTS " + TB_NAME); 
			 onCreate(db); 
			 Log.e("Database","onUpgrade"); 
		 } 
		//更新列 
		 public void updateColumn(SQLiteDatabase db, String oldColumn, String
		    newColumn, String typeColumn){ 
			try{ 
				 db.execSQL("ALTER TABLE " + 
					       TB_NAME + " CHANGE " + 
						   oldColumn + " "+ newColumn + 
						   " " + typeColumn ); 
			}catch(Exception e){ 
				 e.printStackTrace(); } 
	}
}
