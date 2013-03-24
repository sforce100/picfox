package com.fox.utils.async.adapter;

import android.view.View;
import android.widget.ImageView;

import com.fox.picfox.R;

public class ShowImgCache{
	private View baseView;
	private ImageView imageView;
 
	public ShowImgCache(View baseView) {
		this.baseView = baseView;
	}
	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.showimage_item_imageview);
		}
		return imageView;
	}
}