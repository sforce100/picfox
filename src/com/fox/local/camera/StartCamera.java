package com.fox.local.camera;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class StartCamera {

	public static final int IMAGE_CAPTURE = 0;
	private Uri imageUri;
	private Context c;
	public StartCamera(Context c){
		this.c=c;
	}
	public void startCamera() {

		Log.d("ANDRO_CAMERA", "Starting camera on the phone...");
		String fileName = System.currentTimeMillis()+".jpg";
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,
		"Image capture by camera");
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		
		imageUri = c.getContentResolver().insert(
		MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		((Activity)c).startActivityForResult(intent,IMAGE_CAPTURE);
		
	}
	
	public Uri getImageUri() {
		return imageUri;
	}
	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	
}
