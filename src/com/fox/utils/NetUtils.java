/**
 * 1�������ڷ�װ�������������ͨ�ŵĺ���
 * 2���ѺϷ���Ŀ¼�����뱣������
 * 3��������shared perferences
 */
package com.fox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.fox.net.upanddown.ShowImgListActivity;
import com.fox.utils.sql.SQLiteUserOperator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class NetUtils {
	
	private static final int ZOOMUPLOADSIZE1 = 200;
	private static final int ZOOMUPLOADSIZE2 = 400;
	//connection���ӷ�ʽ
	//private HttpURLConnection con;
	//private URL url;
	private InputStream is;
	private OutputStream os;
	//client���ӷ�ʽ
	private HttpClient client;
	private HttpPost post;
	private HttpResponse response;
	private HttpEntity entity;
	
	ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();//��ŷ��͵��ַ�����
	/**
	 * ��½����:
	 * ����½�ɹ���php�᷵��OK,ʧ�ܵ�ʱ�򷵻�FALSE
	 * ��Ŀ¼���浽���ݿ�
	 * 
	 * ��¼�ɹ��󷵻�ͼƬ�б�
	 * @param dir
	 * @param pw
	 * @return
	 */
	public  boolean login(Context context,String dir,String pw){
		
			SQLiteUserOperator sql=new SQLiteUserOperator(context);
			
		try {
			post=new HttpPost(Common.login);
			client=new DefaultHttpClient();
			params.clear();
			params.add(new BasicNameValuePair("dir",dir));
			params.add(new BasicNameValuePair("pw",pw));
			entity=new UrlEncodedFormEntity(params,"gb2312");
			post.setEntity(entity);
			
			response=client.execute(post);
			
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				
				String result=EntityUtils.toString(response.getEntity()).trim();
				
				if(result.equalsIgnoreCase("OK")){
					
					if(!sql.isRepeat(dir))
						sql.addUser(dir, pw);//���ظ��ͱ���
					
					else{
						Toast.makeText(context.getApplicationContext(), 
								"�Ѿ�������Ϣ!!���Կ��ٵ�½!!", 
								Toast.LENGTH_LONG).show();
						}

					//�����ϴ�Ŀ¼
					Common.DIR=dir;System.out.println(Common.DIR);
					//��¼�ɹ�   ��ת��ʾͼƬ�б�
					Intent intent=new Intent();
					intent.setClass(context,ShowImgListActivity.class);
					context.startActivity(intent);
				}
				else if(result.equalsIgnoreCase("FALSE")){
					System.out.println("login page return false!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					Toast.makeText(context.getApplicationContext(), "��½ʧ��!!��ȷ�������������!!", Toast.LENGTH_LONG).show();
				}
				System.out.println(result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			sql.closeUserHelper();
		}
		return true;
	}
	/**
	 * ע�ắ��:
	 * �����������Ŀ¼�ǿ���ע��,ע��ɹ�����OK,ʧ�ܷ���FALSE
	 * ��Ŀ¼���浽���ݿ�
	 * @param context
	 * @param dir
	 * @param pw
	 * @return
	 */
	public  boolean register(Context context,String dir,String pw){
		
			SQLiteUserOperator sql=new SQLiteUserOperator(context);
		try {
			post=new HttpPost(Common.register);
			client=new  DefaultHttpClient();
			params.clear();
			params.add(new BasicNameValuePair("dir",dir));
			params.add(new BasicNameValuePair("pw",pw));
			entity=new UrlEncodedFormEntity(params,"gb2312");
			post.setEntity(entity);
			
			response=client.execute(post);
			
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				
				String result=EntityUtils.toString(response.getEntity());
				
				if(result.equalsIgnoreCase("OK")){
					
					if(!sql.isRepeat(dir))
						sql.addUser(dir, pw);//���ظ��ͱ���
					else{
						Toast.makeText(context.getApplicationContext(), 
								"ע��ɹ�!!\n�Ѿ�������Ϣ!!���Կ��ٵ�½!!", 
								Toast.LENGTH_LONG).show();}
			
				}
				else if(result.equalsIgnoreCase("FALSE")){
					Toast.makeText(context.getApplicationContext(), 
							"ע��ʧ��!!������Ѿ���ռ��!!", 
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			sql.closeUserHelper();
			
		}
		return true;
	}
	/**
	 * ��¼��..����������ͼƬ�б�
	 * 
	 */
	public ArrayList<String> getList(String dir){
		
		try {
			ArrayList<String> list=new ArrayList<String>();
			client=new DefaultHttpClient();
			post=new HttpPost(Common.imglist);
			params.clear();
			params.add(new BasicNameValuePair("dir",dir));
			entity=new UrlEncodedFormEntity(params,"gb2312");
			post.setEntity(entity);
			response=client.execute(post);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String result=EntityUtils.toString(response.getEntity());
				String strs[]=result.split("\n");
				for(int i=0;i<strs.length;i++){
					if(strs[i]!="")
					list.add(strs[i]);
					System.out.println("NetUtilsNetUtilsNetUtilsNetUtils:"+strs[i]);
				}
				return list;
			}
			else
				return null;
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;	
	}
	/**
	 * �������
	 * ��ȡһ��ͼƬ�Ļ���
	 */
	public Bitmap predownload(String path){
		try {
			client=new DefaultHttpClient();
			post=new HttpPost(Common.predownload);
			params.clear();
			params.add(new BasicNameValuePair("path",path));
			//�����ַ�����������
			entity=new UrlEncodedFormEntity(params,"gb2312");
			//����post����ʵ��
			post.setEntity(entity);
			//ִ�����󲢷�����Ӧ
			response=client.execute(post);
			//��÷���
			System.out.println(path);
			byte bmp[]=EntityUtils.toByteArray(response.getEntity());
			return BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
//			HttpEntity result=response.getEntity();
//			is=result.getContent();
//			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}
	/**
	 * ����һ��ͼƬ
	 * @param path
	 * @return
	 */
	public Bitmap download(String p){

 		int index=p.indexOf(Common.DIR);
 		String path=p.substring(index);
		try {
			client=new DefaultHttpClient();
			post=new HttpPost(Common.download);
			params.clear();
			params.add(new BasicNameValuePair("path",path));
			entity=new UrlEncodedFormEntity(params,"gb2312");
			post.setEntity(entity);
			response=client.execute(post);
			//��÷���
			System.out.println(path);
			byte bmp[]=EntityUtils.toByteArray(response.getEntity());
			return BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
//			HttpEntity result=response.getEntity();
//			is=result.getContent();
//			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}
	/**
	 * ���Ԥ������ͼ
	 * @param path
	 * @return
	 */
	public Bitmap getCashImg(String path){
		try {
			client = new DefaultHttpClient();
			post = new HttpPost(Common.listimg);
			
			params.clear();
			params.add(new BasicNameValuePair("path",path));
			
			entity = new UrlEncodedFormEntity(params,"gb2312");
			
			post.setEntity(entity);
			
			response = client.execute(post);
			
			//��÷���
			
			byte bmp[] = EntityUtils.toByteArray(response.getEntity());
			//�����ڻ����ļ�������
			
			File temp = new File(Common.NetTempDir+path);
			if(!temp.exists()){
				temp.createNewFile();
				FileOutputStream os = new FileOutputStream(temp);
				os.write(bmp);
			}
			return BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
//			HttpEntity result=response.getEntity();
//			is=result.getContent();
//			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			
				try {
					if(is!=null)
						is.close();
					if(os!=null)
						os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}
	/**
	 * �ϴ�ͼƬ
	 * MultipartEntity�ֱ���Լ���XXXBody
	 * @return
	 */
	
	 public static boolean upload(){
	    	try {
	    		
	    	int size=0;	
			HttpPost httpPost = new HttpPost(Common.upload);
			MultipartEntity multiEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			if (!Common.PATH.equals("")) {	
				//ѹ��ͼƬ..·���ض���tempĿ¼
				if(Common.IsWifiConnect){
					size = ZOOMUPLOADSIZE2;
				}else{
					size = ZOOMUPLOADSIZE1;
				}
					
				Common.PATH = BitmapUtils.zoomBitmap(Common.PATH, size);
				
                FileBody file = new FileBody(new File(Common.PATH));
                multiEntity.addPart("uploadedfile", file);
	                
	        }

	        StringBody dir = new StringBody(Common.DIR);
	        multiEntity.addPart("dir", dir);
	        
	        httpPost.setEntity(multiEntity);
	        HttpClient client=new DefaultHttpClient();
	        HttpResponse response=client.execute(httpPost);
	        int serverResponseCode=response.getStatusLine().getStatusCode();
	        if(serverResponseCode==HttpStatus.SC_OK){
            	return true;
            }
            else
            {
            	return false;
            }
	    	} catch (Exception e) {
				// TODO: handle exception
	    		e.printStackTrace();
			}
//	    	finally{
//	    		File f=new File(Common.ZIPTEMP);
//	    		if(f.exists())f.delete();
//	    	}
	    	return false;
	    	
	    } 
	 /**
	  * �ϴ�ǰѹ��,������ѹ���ļ�·��
	  */
	 @SuppressWarnings("unused")
	private static void ZipFiles(){
		 FileOutputStream fos = null;
		 ZipOutputStream zos = null;
		 FileInputStream inputStream =null;
		 
			try {
				android.util.Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
				File f=new File(Common.ZIPTEMP);
				f.createNewFile();
			    fos=new FileOutputStream(Common.ZIPTEMP);
				zos=new ZipOutputStream(fos);
				if(zos == null)
					return;
				
				java.io.File file = new java.io.File(Common.PATH);
				
				//�ж��ǲ����ļ�
				if (file.isFile()) {

					ZipEntry zipEntry =  new ZipEntry(Common.ZIPTEMP);
				    inputStream = new FileInputStream(file);
					zos.putNextEntry(zipEntry);
					
					int len;
					byte[] buffer = new byte[4096];
					
					while((len=inputStream.read(buffer)) != -1)
					{
						zos.write(buffer, 0, len);
					}
					
					zos.closeEntry();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					inputStream.close();
					zos.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}//end of func


   

	/**
	 * ����ͼƬ
	 * 
	 */
	 public static void download(Context context,Bitmap bmp){
		 BitmapUtils.addImage(context, bmp);
	 }

	 /**
	  * ����ɾ��ͼƬ����
	  */
	 public static boolean delete(Context context,String p){
		 		BitmapUtils.deleteImage(context, p);
		
		 		int index=p.indexOf(Common.DIR);
		 		String path=p.substring(index);
				try {
					HttpClient client=new DefaultHttpClient();
					HttpPost post=new HttpPost(Common.delete);
					ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
					params.clear();
					params.add(new BasicNameValuePair("path",path));
					HttpEntity entity=new UrlEncodedFormEntity(params,"gb2312");
					post.setEntity(entity);
					HttpResponse response=client.execute(post);
					if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
						String result=EntityUtils.toString(response.getEntity());
						if(result.equalsIgnoreCase("\r\nOK")){
							return true;
						}
						
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;

	 }
	/**
	 * �ر����������
	 */
	public void close(){
		
	}

}
