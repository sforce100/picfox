package com.fox.net.upanddown;


import java.util.ArrayList;
import java.util.List;
import com.fox.picfox.R;
import com.fox.utils.NetUtils;
import com.fox.utils.async.adapter.ImageAndText;
import com.fox.utils.async.adapter.ImageAndTextListAdapter;
import com.fox.utils.async.adapter.ViewCache;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;


public class ShowImgListActivity extends Activity{
	
	ListView lv;
	ArrayList<String> dirs;
	NetUtils net;
	NetImageList imageList;
	List<ImageAndText> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.down_list);
		lv=(ListView)findViewById(R.id.down_listview);
		lv.setOnItemClickListener(new LVonItemClickListener());	
		init();
        lv.setAdapter(new ImageAndTextListAdapter(this, list, lv));
		
	}
	public void init(){
		
		imageList=new NetImageList();
		imageList.compareList();
		//创建适配器
        list = new ArrayList<ImageAndText>();
        //获取网络图片
        for(int i=0;i<imageList.getNetList().size();i++){
        	
        	list.add(new ImageAndText(imageList.getNetList().get(i),imageList.getNetList().get(i),true));
        }
        //获取本地缓冲图片
        for(int j=0;j<imageList.getLocalList().size();j++){
        	list.add(new ImageAndText(imageList.getLocalList().get(j), imageList.getLocalList().get(j), false));
        }
	}
	class LVonItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View rowView, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String path=list.get(arg2).getText();
			Intent to_ShowImgOnline=new Intent();
			//传送下载路径
			to_ShowImgOnline.putExtra("path", path);
			//从ImageView提取Bitmap
			ViewCache vc=(ViewCache) rowView.getTag();
			ImageView iv=vc.getImageView();			
			Bitmap bitmap=((BitmapDrawable)iv.getDrawable()).getBitmap();  
			
			byte[] value=com.fox.utils.BitmapUtils.bitampToByteArray(bitmap);
			
			Bundle b=new Bundle();
			b.putByteArray("bitmap", value);
			to_ShowImgOnline.putExtra("bundle",b);
			to_ShowImgOnline.setClass(ShowImgListActivity.this, ShowImgOnline.class);
			ShowImgListActivity.this.startActivity(to_ShowImgOnline);
		}
		
	}
}
