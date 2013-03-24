/**
 * 本地浏览图片
 */
package com.fox.utils.async.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.fox.picfox.R;
import com.fox.picfox.ShowImage;
import com.fox.utils.async.loader.AsyncImageLoader;
import com.fox.utils.async.loader.AsyncImageLoader.ImageCallback;

public class ShowImageAdapter extends ArrayAdapter<ShowImg>{
	 
	private GridView grid;
	private AsyncImageLoader asyncImageLoader;
 
	ImageView imageView;
	Bitmap cachedImage;
	public ShowImageAdapter(Activity activity, List<ShowImg> image, GridView grid) {
		super(activity, 0, image);
		this.grid = grid;
		asyncImageLoader = new AsyncImageLoader();
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
 
		System.out.println("here is getView:"+position+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		
		// 创建xml  item子项
				View rowView = convertView;
				ShowImgCache ShowImgCache;
		 		if (rowView == null) {
					LayoutInflater inflater = activity.getLayoutInflater();//user to instant xml layout
					rowView = inflater.inflate(R.layout.showimage_item, null);
					ShowImgCache = new ShowImgCache(rowView);
					rowView.setTag(ShowImgCache);
				} else {
					ShowImgCache = (ShowImgCache) rowView.getTag();
				}
			if(position > ShowImage.atLocal-10 && position < ShowImage.atLocal+20){
				
				ShowImg ShowImg = getItem(position);
		 
				// 通过异步操作获得图片bitmap
				String imageUrl = ShowImg.getPath();
				imageView = ShowImgCache.getImageView();
				imageView.setTag(imageUrl);
				
				cachedImage = asyncImageLoader.loadLocalBitmap(activity,imageUrl, 
						new ImageCallback() {
							public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
								//grid中对应的imageview的引用!!!
								ImageView imageViewByTag = (ImageView) grid.findViewWithTag(imageUrl);
								if (imageViewByTag != null) {
									imageViewByTag.setImageBitmap(imageDrawable);
							}
						}//callback end
				});
				if (cachedImage == null) {
					//imageView.setImageResource(R.drawable.default_image);
				}else{
					imageView.setImageBitmap(cachedImage);
				}
			}
			return rowView;
		
	}
	
 
}