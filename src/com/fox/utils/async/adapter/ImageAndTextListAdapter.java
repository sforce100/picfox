/**
 * 异步加载网络相册的略缩图
 */
package com.fox.utils.async.adapter;

import java.util.List;

import com.fox.picfox.R;
import com.fox.utils.async.loader.AsyncImageLoader;
import com.fox.utils.async.loader.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
 
public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {
 
	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
 
	public ImageAndTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
 
		// 创建xml  item子项
		View rowView = convertView;
		ViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();//user to instant xml layout
			rowView = inflater.inflate(R.layout.down_list_item, null);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		ImageAndText imageAndText = getItem(position);
 
		// 通过异步操作获得图片bitmap
		String imageUrl = imageAndText.getImageUrl();
		ImageView imageView = viewCache.getImageView();
		imageView.setTag(imageUrl);
		Bitmap cachedImage = asyncImageLoader.loadBitmap(imageUrl,imageAndText.isOnNet(), new ImageCallback() {
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
				//grid中对应的imageview的引用!!!
				ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
				if (imageViewByTag != null) {
					imageViewByTag.setImageBitmap(imageDrawable);
				}
			}
		});
		if (cachedImage == null) {
			//imageView.setImageResource(R.drawable.default_image);
		}else{
			imageView.setImageBitmap(cachedImage);
		}
		// Set the text on the TextView
		TextView textView = viewCache.getTextView();
		textView.setText(imageAndText.getText());
		
		return rowView;
	}
 
}