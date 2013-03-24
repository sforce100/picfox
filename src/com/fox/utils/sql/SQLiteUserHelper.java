package com.fox.utils.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteUserHelper extends SQLiteOpenHelper {
	public static final String TB_NAME="users";
	public static final String ID="ID";
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	

	public SQLiteUserHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public SQLiteUserHelper(Context context, String name,
			CursorFactory factory) {
		this(context, name, factory,1);
		// TODO Auto-generated constructor stub
	}
	public SQLiteUserHelper(Context context) {
		this(context,com.fox.utils.Common.DATABASENAME, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+
		TB_NAME+"("+
		ID+" integer primary key,"+
		USERNAME+" varchar(20),"+
		PASSWORD+" varchar(20)"+
		")");
	    Log.e("Database","onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME); 
		 onCreate(db); 
		 Log.e("Database","onUpgrade"); 
		
	}
	

}
