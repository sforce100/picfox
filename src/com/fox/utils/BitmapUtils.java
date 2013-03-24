/**
 * ר�Ŷ�bitmap�������
 */
package com.fox.utils;

import java.io.*;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import com.fox.local.editimage.EditView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;



public class BitmapUtils {
	public static float scale=1.0f;//ͼƬ�Ŵ���Сָ��
	
	/***
	 * ���SD������ͼƬ��·��
	 * @param c
	 * @return
	 */
	public static HashMap<Integer,String> getAllBitmap(Context context){
		HashMap<Integer,String> map=new HashMap<Integer, String>();
		Cursor cursor;
		String path;
		int mapIndex=0;
		String[] base={MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA};
		//String[] base={MediaStore.Images.Thumbnails._ID,MediaStore.Images.Thumbnails.DATA};
		//��ѯ������  ����ID
		cursor=((Activity) context).managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, base, null, null, null);
		//ͼƬ������ֵ
		File file;
		
		while(cursor.moveToNext()){
			//��ȡ�к�
			int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA);
			//��ȡ�к�����
			path=cursor.getString(column_index);
			file=new File(path);
			if(file.exists()&&file.length()!=0&&path.contains("Camera")&&path.contains("jpg")){
			map.put(mapIndex, path);
			mapIndex++;
			System.out.println(mapIndex+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+path);
			}
		}
		cursor.close();
//		String dir=Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera";
//		File files=new File(dir);
//		if(files.isDirectory()&&files.list().length!=0){
//			String list[]=files.list();
//			for(int i=0;i<1;i++){
//				//int last=list[i].lastIndexOf(".");
//				if(list[i].contains("jpg")){
//					map.put(mapIndex,dir+"/"+list[i]);
//					mapIndex++;
//				}
//			}
//		}
		return map;
	}
	/**
	 * ͨ��ͼƬID���ͼƬURI
	 * @param c
	 * @param imageID
	 * @return
	 */
	public static Uri getSelect(Context context,int imageID){

		Uri retUri;
		String[] base={MediaStore.Images.Media._ID};
		String[] selectionArgs = new String[] { imageID + "" };
		
		Cursor cursor=((Activity) context).managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				base, MediaStore.Images.Media._ID+"=?", selectionArgs, null);
		int image_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media._ID);
		
		retUri=Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				cursor.getInt(image_index)+"");
		cursor.close();
		return retUri;
		
	}
	/**
	 * �����������
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmap(String path){
		   BitmapFactory.Options options=new Options();
		   options.inJustDecodeBounds=true;
		   Bitmap bitmap = BitmapFactory.decodeFile(path, options); //��ʱ����bmΪ��
	       options.inJustDecodeBounds = false;
	       //�������ű�
	       int be = (int)(options.outHeight / (float)200);
	       if (be <= 0)be = 1;
	       options.inSampleSize = be;
	       //���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ falseŶ
	       bitmap=BitmapFactory.decodeFile(path,options);
		   return bitmap;
	}
	/**
	 * ����Ļ��������
	 * @param context
	 * @param path
	 * @return
	 */
	public static Bitmap decodeBitmap(Context context,String path) {
		//��ȡ��Ļ��С
		DisplayMetrics dm=new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		int displayWidth=dm.heightPixels,displayHeight=dm.widthPixels;
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // ��ȡ�ߴ���Ϣ
		// ��ȡ������С
		int wRatio = (int) Math.ceil(op.outWidth / (float) displayWidth);
		int hRatio = (int) Math.ceil(op.outHeight / (float) displayHeight);
		// �������ָ����С������С��Ӧ�ı���
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
				
			} else {
				op.inSampleSize = hRatio;
			}
		}
		int x=op.outWidth,y=op.outHeight;
		float z=0.0f;
		int a=op.outWidth-displayWidth;
		int b=op.outHeight-displayHeight;
		if(a>0||b>0){
			z=x/displayWidth;
			x=displayWidth;
			y/=z;
		}
		
		op.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, op);
		return Bitmap.createScaledBitmap(bmp, x, y, true);
	}
	/**
	 * �Զ����������
	 * @param path
	 * @param Size
	 * @param dstWidth
	 * @param dstHeight
	 * @return
	 */
	public static Bitmap zoomBitmap(String path,int Size,int dstWidth, int dstHeight){
		
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // ��ȡ�ߴ���Ϣ		
//		int w=op.outWidth;
//		int h=op.outHeight;
		op.inSampleSize=Size;	
		op.inJustDecodeBounds = false;
		bmp=BitmapFactory.decodeFile(path, op);
		bmp= Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, true);
//		Matrix matrix=new Matrix();
//		float scaleWidth=((float)60/w);
//		float scaleHeight=((float)65/h);
//		matrix.postScale(scaleWidth, scaleHeight);
//		Bitmap result=Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
		return bmp;
	}
	
	public static Bitmap zoomBitmap(Context context,String path,int Size){
		//��ȡ��Ļ��С
		DisplayMetrics dm=new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		int displayWidth=dm.heightPixels/3,displayHeight=dm.widthPixels/2;//��Ļ���
		
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // ��ȡ�ߴ���Ϣ		
		op.inSampleSize=Size;	
		op.inJustDecodeBounds = false;
		bmp=BitmapFactory.decodeFile(path, op);
		bmp= Bitmap.createScaledBitmap(bmp, displayWidth, displayHeight, true);
//		Matrix matrix=new Matrix();
//		float scaleWidth=((float)60/w);
//		float scaleHeight=((float)65/h);
//		matrix.postScale(scaleWidth, scaleHeight);
//		Bitmap result=Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
		return bmp;
	}
	
	/**
	 * �����ϴ�ͼƬʱѹ��ͼƬ
	 * @param path
	 * @param outHeigth ����ȷ��ͼƬ���Ը߶�outHeigth px
	 * @return
	 */
	public static String zoomBitmap(String path, float outHeight){
		
		if(new File(Common.PATH).length()>5*1024){
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(path, op);
			//int w = op.outWidth;
			int h = op.outHeight;
			//����ͼƬ����
			int be = (int)(h/outHeight);
			if(be <= 0)
				be = 1;
			op.inSampleSize = be;
			op.inJustDecodeBounds = false;
			//���¶���ͼƬ
			bitmap = BitmapFactory.decodeFile(path, op);
			
			//���浽�ϴ���ʱ�ļ���
			File dir = new File(Common.IMGTMPDIR);
			if(!dir.exists() || !dir.isDirectory()){
				dir.mkdirs();
			}
			String name = System.currentTimeMillis()+".jpeg";
			File file = new File(Common.IMGTMPDIR,name);
			try {
				FileOutputStream out = new FileOutputStream(file);
				if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return Common.IMGTMPDIR+"/"+name;
		}
		return Common.PATH;
	}
	/**
	 * Bitmap��Byte[]�Ļ���ת��
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitampToByteArray(Bitmap bitmap) {
		byte[] array = null;
		try {
			if (null != bitmap) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
				array = os.toByteArray();
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return array;
	}

	public static Bitmap byteArrayToBitmap(byte[] array) {
		if (null == array) {
			return null;
		}

		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}
    /**
     * ɾ��ͼƬ
     * @param path
     */
	public static void deleteImage(Context context,String path){
		File file=new File(path);
		if(file.exists()){
			//�������ṩ��ɾ��
		     String where = MediaStore.Images.Media.DATA + " LIKE '" + path + "%'"; 
		     context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);

		}
	}
	public static void deleteTempImage(Context context){
		File file=new File(Common.IMGTMPDIR);
		if(file.isDirectory()){
			File files[]=file.listFiles();
			for(int i=0;i<files.length;i++){
				 String path=files[i].getAbsolutePath();
				 //�������ṩ��ɾ��
			     String where = MediaStore.Images.Media.DATA + " LIKE '" + path + "%'"; 
			     context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
	            
			}
		}
	}
	/**
	 * ����ͼƬ
	 * @param c
	 */
	public static void addImage(Context context){
		String fileUrl =  MediaStore.Images.Media.insertImage(context.getApplicationContext().getContentResolver(),
				BitmapFactory.decodeFile(Common.PATH), "photo", "photo");
		//�㲥֪ͨ������ͼƬ  ��ʱ����
		Uri picUri=Uri.parse(fileUrl);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,picUri));
	}
	public static void addImage(Context context,EditView ev){
		String fileUrl = MediaStore.Images.Media.insertImage(context.getApplicationContext().getContentResolver(),
				BitmapUtils.toConformBitmap(ev.getBgBitmap(), ev.getTempBitmap()), "save", "save");
		//�㲥֪ͨ������ͼƬ  ��ʱ����
				Uri picUri=Uri.parse(fileUrl);
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,picUri));
	}
	public static void addImage(Context context,Bitmap bmp){
		String fileUrl = MediaStore.Images.Media.insertImage(context.getApplicationContext().getContentResolver(), 
				bmp, "download", "download");
		//�㲥֪ͨ������ͼƬ  ��ʱ����
		Uri picUri=Uri.parse(fileUrl);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,picUri));
	}
	/**
	 * ͨ��Matrix��ͼƬ����
	 * @param source
	 * @return
	 */
	public static Bitmap bigger(Bitmap source){
		int w=source.getWidth(),h=source.getHeight();
		scale+=0.1;
		Matrix matrix=new Matrix();
		matrix.postScale(scale, scale);
		Bitmap result=Bitmap.createBitmap(source, 0, 0, w, h, matrix, true);
		return result;
	}
	public static Bitmap smaller(Bitmap source){
		int w=source.getWidth(),h=source.getHeight();
		scale-=0.1;
		Matrix matrix=new Matrix();
		matrix.postScale(scale, scale);
		Bitmap result=Bitmap.createBitmap(source, 0, 0, w, h, matrix, true);
		return result;
	}
	/**
	 * ������Bitmap�ں�
	 * @param background
	 * @param foreground
	 * @return
	 */
	public static Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        if( background == null ) {   
           return null;   
        }   
  
        int bgWidth = background.getWidth();   
        int bgHeight = background.getHeight();   
        //int fgWidth = foreground.getWidth();   
        //int fgHeight = foreground.getHeight();   
        //create the new blank bitmap ����һ���µĺ�SRC���ȿ��һ����λͼ    
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);  
        Canvas cv = new Canvas(newbmp);   
        //draw bg into   
        cv.drawBitmap(background, 0, 0, null);//�� 0��0���꿪ʼ����bg   
        //draw fg into   
        cv.drawBitmap(foreground, 0, 0, null);//�� 0��0���꿪ʼ����fg �����Դ�����λ�û���
        //save all clip   
        cv.save(Canvas.ALL_SAVE_FLAG);//����   
        //store   
        cv.restore();//�洢   
        return newbmp;   
   }
	/**
	 * Uriת��δpath
	 * @param uri
	 * @return
	 */
	  public static String getAbsoluteImagePath(Context context,Uri uri) 
	   {
	       // can post image
	       String [] proj={MediaStore.Images.Media.DATA};
	       Cursor cursor = ((Activity) context).managedQuery( uri,
	                       proj,                 // Which columns to return
	                       null,       // WHERE clause; which rows to return (all rows)
	                       null,       // WHERE clause selection arguments (none)
	                       null);                 // Order-by clause (ascending by name)
	       
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       cursor.moveToFirst();
	        
	       return cursor.getString(column_index);
	   }
	  /**
	   * ���ͼƬ��Ϣ
	   * @param path
	   * @return
	   */
	  public static String getImageInfo(String path){
		  float size=0;
		  String info="";
		  File file=new File(path);
		  if(file.exists()){
			  size=file.length()/1024;
			  info="size:"+size+"K  name:"+file.getName();
		  }
		  return info;
	  }
	  
	  public static Bitmap rotateImage(String path,int d,SoftReference<Bitmap> soft){
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(path, op); // ��ȡ�ߴ���Ϣ		
			int w=op.outWidth;
			int h=op.outHeight;	
			op.inJustDecodeBounds = false;
			bmp=BitmapFactory.decodeFile(path, op);
			Matrix matrix=new Matrix();
			matrix.setRotate(d);
			Bitmap bitmap=Bitmap.createBitmap(soft.get(), 0, 0, w, h, matrix, true);
			//bmp.recycle();
			//System.gc();
			return bitmap;
		}

}
