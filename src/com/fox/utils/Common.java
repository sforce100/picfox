package com.fox.utils;

import android.os.Environment;

public class Common {
	
	public static final String login="http://1.myfirst100.sinaapp.com/login.php";//��¼���
	public static final String register="http://1.myfirst100.sinaapp.com/register.php";//ע�����
	public static final String upload="http://1.myfirst100.sinaapp.com/uploadone.php";//ͼƬ�ϴ�
	public static final String download="http://1.myfirst100.sinaapp.com/download.php";//ͼƬ����
	public static final String listimg="http://1.myfirst100.sinaapp.com/listimg.php";//���Ԥ������ͼ�б�
	public static final String predownload="http://1.myfirst100.sinaapp.com/predownload.php";//ͼƬ����ǰ��Ԥ��
	public static final String show="http://1.myfirst100.sinaapp.com/";//�������
	public static final String imglist="http://1.myfirst100.sinaapp.com/storelist.php";//��������б�
	public static final String delete="http://1.myfirst100.sinaapp.com/delete.php";//ɾ�����ͼƬ
	public static final String DATABASENAME="PicFoxDB.db";//���ݿ�����
	public static final String SharedPreferencesName="PicFoxSharedPreferences";
	public static final String ZIPTEMP="/mnt/sdcard/temp.zip";//ѹ��·��
	public static final String IMGTMPDIR=Environment.getExternalStorageDirectory().getPath() + "/"+"temp";//ͼƬ��ʱĿ¼
	public static final String IMGIMPNAME="temp";//ͼƬ��ʱ����
	public static String DIR="";//��ǰ���������..ֻ����login��������
	public static String PATH="";//ѡ�в����ı���ͼƬ·��
	public static String NetTempDir=Environment.getExternalStorageDirectory().getPath()+"/"+"nettemp/";//��ǰ��¼������������ʱĿ¼
	public static boolean IsConnectNet=false;
	public static boolean IsWifiConnect=false;
	

}
