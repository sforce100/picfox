package com.fox.utils.async.adapter;

public class ImageAndText {
	private String imageUrl;
	private String text;
	private boolean onNet;
	public ImageAndText(String imageUrl, String text,boolean onNet) {
		this.imageUrl = imageUrl;
		this.text = text;
		this.onNet=onNet;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getText() {
		return text;
	}
	public boolean isOnNet() {
		return onNet;
	}

}
