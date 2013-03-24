package com.fox.net.weibo;

import android.app.Activity;
import android.os.Bundle;

import com.fox.picfox.R;
import com.weibo.net.AccessToken;
import com.weibo.net.Oauth2AccessToken;
import com.weibo.net.RequestToken;
import com.weibo.net.Weibo;

public class OAuth extends Activity{
	public final static String CONSUMER_KEY="4254845672";
	public final static String CONSUMER_SECRET="aff5e66ac6e46297f51362680228a3cb";
	public final static String CALLBACK_URL="http://.weibo.com";
	public String username="13726590394";
	public String passwd="84869073";
	RequestToken  request;
	AccessToken   access2;
	Oauth2AccessToken  access;
	Weibo weibo;

	public void oauthTest(){
		try {
			weibo=Weibo.getInstance();
			request=weibo.getRequestToken(OAuth.this, CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
			access2=weibo.generateAccessToken(this, request);
//			access2=weibo.getOauth2AccessToken(this, CONSUMER_KEY, CONSUMER_SECRET, username, passwd);
			System.out.println("token:"+access2.getToken()+"\nVerifier"+access2.getVerifier()+"\nSecret"+access2.getSecret());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//oauthTest();
		setContentView(R.layout.main);
	}

}
