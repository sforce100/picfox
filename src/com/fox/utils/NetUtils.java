/**
 * 1该类用于封装与服务器端连接通信的函数
 * 2并把合法的目录和密码保存起来
 * 3检查网络和shared perferences
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
	//connection连接方式
	//private HttpURLConnection con;
	//private URL url;
	private InputStream is;
	private OutputStream os;
	//client连接方式
	private HttpClient client;
	private HttpPost post;
	private HttpResponse response;
	private HttpEntity entity;
	
	ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();//存放发送的字符命令
	/**
	 * 登陆函数:
	 * 当登陆成功是php会返回OK,失败的时候返回FALSE
	 * 将目录名存到数据库
	 * 
	 * 登录成功后返回图片列表
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
						sql.addUser(dir, pw);//不重复就保存
					
					else{
						Toast.makeText(context.getApplicationContext(), 
								"已经保存信息!!可以快速登陆!!", 
								Toast.LENGTH_LONG).show();
						}

					//设置上传目录
					Common.DIR=dir;System.out.println(Common.DIR);
					//登录成功   跳转显示图片列表
					Intent intent=new Intent();
					intent.setClass(context,ShowImgListActivity.class);
					context.startActivity(intent);
				}
				else if(result.equalsIgnoreCase("FALSE")){
					System.out.println("login page return false!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					Toast.makeText(context.getApplicationContext(), "登陆失败!!请确认相册名和密码!!", Toast.LENGTH_LONG).show();
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
	 * 注册函数:
	 * 当不存在这个目录是可以注册,注册成功返回OK,失败返回FALSE
	 * 将目录名存到数据库
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
						sql.addUser(dir, pw);//不重复就保存
					else{
						Toast.makeText(context.getApplicationContext(), 
								"注册成功!!\n已经保存信息!!可以快速登陆!!", 
								Toast.LENGTH_LONG).show();}
			
				}
				else if(result.equalsIgnoreCase("FALSE")){
					Toast.makeText(context.getApplicationContext(), 
							"注册失败!!相册名已经被占用!!", 
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
	 * 登录后..获得网络相册图片列表
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
	 * 在线浏览
	 * 获取一张图片的缓存
	 */
	public Bitmap predownload(String path){
		try {
			client=new DefaultHttpClient();
			post=new HttpPost(Common.predownload);
			params.clear();
			params.add(new BasicNameValuePair("path",path));
			//设置字符集参数对象
			entity=new UrlEncodedFormEntity(params,"gb2312");
			//设置post请求实体
			post.setEntity(entity);
			//执行请求并返回响应
			response=client.execute(post);
			//获得返回
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
	 * 下载一张图片
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
			//获得返回
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
	 * 获得预览略缩图
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
			
			//获得返回
			
			byte bmp[] = EntityUtils.toByteArray(response.getEntity());
			//保存在缓冲文件夹里面
			
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
	 * 上传图片
	 * MultipartEntity分别可以加入XXXBody
	 * @return
	 */
	
	 public static boolean upload(){
	    	try {
	    		
	    	int size=0;	
			HttpPost httpPost = new HttpPost(Common.upload);
			MultipartEntity multiEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			if (!Common.PATH.equals("")) {	
				//压缩图片..路径重定向到temp目录
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
	  * 上传前压缩,并返回压缩文件路径
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
				
				//判断是不是文件
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
	 * 下载图片
	 * 
	 */
	 public static void download(Context context,Bitmap bmp){
		 BitmapUtils.addImage(context, bmp);
	 }

	 /**
	  * 发送删除图片命令
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
	 * 关闭输入输出流
	 */
	public void close(){
		
	}

}
