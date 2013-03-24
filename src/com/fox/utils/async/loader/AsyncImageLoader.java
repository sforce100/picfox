/**
 * �������ΪAdapter�����
 * ���߳��첽����ͼƬ
 * 1.�������
 * 2.�������
 */
package com.fox.utils.async.loader;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import com.fox.utils.NetUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
 
public class AsyncImageLoader {
 
private HashMap<String, SoftReference<Bitmap>> imageCache;
 
	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	 /**
	  * �����ȡ
	  * @param imageUrl
	  * @param imageCallback
	  * @return
	  */
	public Bitmap loadBitmap(final String imageUrl, final boolean isNet,final ImageCallback imageCallback) {
		/**
		 * ��bitmap�ͷ���   û�о��̼߳���    handler�ص�
		 */
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
	     }
		/**
		 * ����thread����Ϣ��ִ��ͼƬ���ز���	
		 */
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		/**
		 * �ѻ�õ���������ͼ������imageCache����
		 */
		new Thread() {
			@Override
			public void run() {
				if(isNet){
					Bitmap drawable = loadImageFromNet(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);//������Ϣ��handler
					drawable=null;
				}else{
					Bitmap drawable = loadImageFromSD(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);//������Ϣ��handler
					drawable=null;
				}
			  }
			}.start();
			return null;
	}
	
/////////////////////////////�����ָ���//////////////////////////////////////////////////////////
	/**
	 * ���ڱ����첽��ȡСͼƬ
	 */
	
	public Bitmap loadLocalBitmap(final Context context,final String imageUrl, final ImageCallback imageCallback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> soft=imageCache.get(imageUrl);
			Bitmap drawable = soft.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler h=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				imageCallback.imageLoaded((Bitmap) msg.obj, imageUrl);
			}
			
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap drawable = loadForGridView(context,imageUrl);
				imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
				Message message = h.obtainMessage(0, drawable);
				h.sendMessage(message);//������Ϣ��handler
				drawable=null;
			  }
			}.start();
		return null;
	}
/////////////////////////////�����ָ���//////////////////////////////////////////////////////////
/**
* ���ڱ����첽��ȡԭͼͼƬ
*/

public Bitmap loadLocalRealBitmap(final String imageUrl, final ImageCallback imageCallback){
	if(imageCache.containsKey(imageUrl)){
		SoftReference<Bitmap> soft=imageCache.get(imageUrl);
		Bitmap drawable = soft.get();
		if (drawable != null) {
			return drawable;
		}
	}
	final Handler h=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			imageCallback.imageLoaded((Bitmap) msg.obj, imageUrl);
		}

	};
	new Thread() {
		@Override
		public void run() {
			Bitmap drawable = loadImageFromRealSD(imageUrl);
			imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
			Message message = h.obtainMessage(0, drawable);
			h.sendMessage(message);//������Ϣ��handler
			drawable=null;
		}
	}.start();
	return null;
}
	///////////////////////////////////////���ӻ���!!Խ��Խ����/////////////////////////////////////
	 /**
	  * ������ǿɰ�����������ͼ
	  * @param url
	  * @return
	  */
	public  Bitmap loadImageFromNet(String url) {
		
		System.out.println(url+"   \n network state:"+Common.IsConnectNet);
		
		NetUtils net=new NetUtils();
		Bitmap bmp=net.getCashImg(url);
		bmp=Bitmap.createScaledBitmap(bmp, 95, 90, true);
		return bmp;
	}
	/**
	 * ��ñ���sd��ͼƬ  ��ȡ��������ͼ
	 * @author Administrator
	 *
	 */
	 public  Bitmap loadImageFromSD(String path){
		return  BitmapUtils.zoomBitmap(path,2,95,90);
	 }
	 /**
	  * ΪGridView���Ƴߴ�!!!!!!!
	  * @param context
	  * @param path
	  * @return
	  */
	 public  Bitmap loadForGridView(Context context,String path){
		 System.out.println("loadForGridView:"+path);
		 return BitmapUtils.zoomBitmap(context, path, 10);
	 }
	 /**
	  * ���ԭͼ
	  * @author Administrator
	  *
	  */
	 public  Bitmap loadImageFromRealSD(String path){
			return  BitmapUtils.getBitmap(path);
		 }
	public interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, String imageUrl);
	}
	
}
