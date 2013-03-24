/**
 * 这个类是为Adapter服务的
 * 用线程异步加载图片
 * 1.网络相册
 * 2.本地相册
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
	  * 网络读取
	  * @param imageUrl
	  * @param imageCallback
	  * @return
	  */
	public Bitmap loadBitmap(final String imageUrl, final boolean isNet,final ImageCallback imageCallback) {
		/**
		 * 有bitmap就返回   没有就线程加载    handler回调
		 */
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
	     }
		/**
		 * 接收thread的消息并执行图片加载操作	
		 */
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		/**
		 * 把获得的网络略缩图保存在imageCache里面
		 */
		new Thread() {
			@Override
			public void run() {
				if(isNet){
					Bitmap drawable = loadImageFromNet(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);//发送消息给handler
					drawable=null;
				}else{
					Bitmap drawable = loadImageFromSD(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);//发送消息给handler
					drawable=null;
				}
			  }
			}.start();
			return null;
	}
	
/////////////////////////////华丽分割线//////////////////////////////////////////////////////////
	/**
	 * 用于本地异步获取小图片
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
				h.sendMessage(message);//发送消息给handler
				drawable=null;
			  }
			}.start();
		return null;
	}
/////////////////////////////华丽分割线//////////////////////////////////////////////////////////
/**
* 用于本地异步获取原图图片
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
			h.sendMessage(message);//发送消息给handler
			drawable=null;
		}
	}.start();
	return null;
}
	///////////////////////////////////////更加华丽!!越下越精彩/////////////////////////////////////
	 /**
	  * 获得我们可爱的网络略缩图
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
	 * 获得本机sd卡图片  获取的是略缩图
	 * @author Administrator
	 *
	 */
	 public  Bitmap loadImageFromSD(String path){
		return  BitmapUtils.zoomBitmap(path,2,95,90);
	 }
	 /**
	  * 为GridView订制尺寸!!!!!!!
	  * @param context
	  * @param path
	  * @return
	  */
	 public  Bitmap loadForGridView(Context context,String path){
		 System.out.println("loadForGridView:"+path);
		 return BitmapUtils.zoomBitmap(context, path, 10);
	 }
	 /**
	  * 获得原图
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
