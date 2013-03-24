package com.fox.picfox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewSwitcher.ViewFactory;

import com.fox.utils.ShowPhotoDialog;
import com.fox.utils.BitmapUtils;
import com.fox.utils.Common;
import com.fox.utils.async.adapter.ShowImageAdapter;
import com.fox.utils.async.adapter.ShowImg;


public class ShowImage extends Activity implements ViewFactory{

	public static int atLocal=0;
	
	ShowImageAdapter imageAdapter;
	GridView grid;
	HashMap<Integer, String> isMap;
	ListView lv;
	List<ShowImg> list;
	boolean isScrollGridView=false;
	int size=0;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showimage);
		
		grid=(GridView)findViewById(R.id.showimage_gridview);
		init();
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Common.PATH=isMap.get(arg2);
				System.out.println(Common.PATH);
//				Intent i=new Intent();
//				i.putExtra("site", arg2);
//				i.setClass(ShowImage.this, ShowImageOne.class);
//				startActivity(i);
				ShowPhotoDialog dialog=new ShowPhotoDialog(ShowImage.this);
				dialog.setTitle(BitmapUtils.getImageInfo(Common.PATH));
				dialog.show();
			}
		});
		grid.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

					atLocal=firstVisibleItem;
			}
		});
		
	
    }


    	private void init(){
    		isMap=BitmapUtils.getAllBitmap(this);
			list = new ArrayList<ShowImg>();
			size=isMap.size();
		    for(int i=0;i<size;i++){
		    	
		        	list.add(new ShowImg(isMap.get(i)));
		        	
		        }
		    
		    grid.setAdapter(new ShowImageAdapter(this, list, grid));
    	}
    	
    	
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			isMap=BitmapUtils.getAllBitmap(this);
			if(size!=isMap.size()){
				list = new ArrayList<ShowImg>();
				size=isMap.size();
			    for(int i=0;i<size;i++){
			    	
			        	list.add(new ShowImg(isMap.get(i)));
			        	
			        }
			    
			    grid.setAdapter(new ShowImageAdapter(this, list, grid));
			}
		}
		

		//use for imageSwitch
		@Override
		public View makeView() {
				// TODO Auto-generated method stub
			return new ImageView(this);
		}
		
		
}




