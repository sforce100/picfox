package com.fox.utils;

import android.os.Environment;

public class Common {
	
	public static final String login="http://1.myfirst100.sinaapp.com/login.php";//登录相册
	public static final String register="http://1.myfirst100.sinaapp.com/register.php";//注册相册
	public static final String upload="http://1.myfirst100.sinaapp.com/uploadone.php";//图片上传
	public static final String download="http://1.myfirst100.sinaapp.com/download.php";//图片下载
	public static final String listimg="http://1.myfirst100.sinaapp.com/listimg.php";//获得预览略缩图列表
	public static final String predownload="http://1.myfirst100.sinaapp.com/predownload.php";//图片下载前得预览
	public static final String show="http://1.myfirst100.sinaapp.com/";//在线浏览
	public static final String imglist="http://1.myfirst100.sinaapp.com/storelist.php";//在线相册列表
	public static final String delete="http://1.myfirst100.sinaapp.com/delete.php";//删除相册图片
	public static final String DATABASENAME="PicFoxDB.db";//数据库名字
	public static final String SharedPreferencesName="PicFoxSharedPreferences";
	public static final String ZIPTEMP="/mnt/sdcard/temp.zip";//压缩路径
	public static final String IMGTMPDIR=Environment.getExternalStorageDirectory().getPath() + "/"+"temp";//图片临时目录
	public static final String IMGIMPNAME="temp";//图片临时名字
	public static String DIR="";//当前操作相册名..只能在login里面设置
	public static String PATH="";//选中操作的本地图片路径
	public static String NetTempDir=Environment.getExternalStorageDirectory().getPath()+"/"+"nettemp/";//当前登录的网络相册的临时目录
	public static boolean IsConnectNet=false;
	public static boolean IsWifiConnect=false;
	

}
