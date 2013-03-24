package com.fox.net.weibo;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.ShareActivity;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

public class Authorize {
	
	private static final String CONSUMER_KEY = "4254845672";	
	
	private static final String CONSUMER_SECRET = "aff5e66ac6e46297f51362680228a3cb";
	
	private static final int ZOOMWEIBOSIZE1 = 100;
	private static final int ZOOMWEIBOSIZE2 = 300;
	private Weibo weibo; 
	static Context c;
	public Authorize(Context c){
		this.c=c;
	}
	public  void share(){
		weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

		// Oauth2.0
		
		weibo.setRedirectUrl("http://www.sina.com");
			
		weibo.authorize((Activity) c,new AuthDialogListener());
	}
	
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);

			//调用分享窗口
			preShare();
			
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(c.getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(c.getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(c.getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}
	
	/**
	 * 分享
	 */
	private void preShare(){
		  int size = 0;
		  File picFile = new File(Common.PATH);
          if (!picFile.exists()) {
              Toast.makeText(c, "鍥剧墖" + Common.PATH + "涓嶅瓨鍦紒", Toast.LENGTH_SHORT)
                      .show();
              Common.PATH = null;
          }
          try {
        	  if(Common.IsWifiConnect){
        		  size = ZOOMWEIBOSIZE2;
        	  }else{
        		  size = ZOOMWEIBOSIZE1;
        	  }
        	  //压缩图片..路径重定向到temp目录
        	  Common.PATH = BitmapUtils.zoomBitmap(Common.PATH, size);
        	  
              share2weibo("abc", Common.PATH);
              Intent i = new Intent(c, ShareActivity.class);
              ((Activity) c).startActivityForResult(i, Activity.RESULT_OK);

          } catch (WeiboException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          } finally {

          }
	}
	 private void share2weibo(String content, String picPath) throws WeiboException {
		 
	        Weibo weibo = Weibo.getInstance();
	        weibo.share2weibo((Activity)c, weibo.getAccessToken().getToken(), weibo.getAccessToken()
	                .getSecret(), content, picPath);
	    }

}
