/**
 * 通过AsyncTask使上传在后台运行..防止UI阻塞
 */
package com.fox.net.upanddown;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fox.utils.Common;
import com.fox.utils.NetUtils;

public class UploadTask extends AsyncTask<Object, Integer, Void> {  
	  
	private static final long FILESIZE=4*1024*1024;
    private ProgressDialog dialog = null;  
    private boolean resulted=false; 
    private  Context context;
    public UploadTask(Context context){
    	this.context=context;
    }
    @Override  
    protected void onPreExecute() {  
        dialog = new ProgressDialog(context);  
        dialog.setMessage("正在上传...");  
        dialog.setIndeterminate(false);  
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        dialog.setProgress(0);  
        dialog.show();  
    }  

    @Override  
    protected Void doInBackground(Object... arg0) {
    	
    	if(checkSize()){
	    	if(NetUtils.upload())//上传图片
	    		resulted=true;
	    	else
	    		resulted=false;			 
    	}
    	else
    		Toast.makeText(context.getApplicationContext(), "上传失败!!图片太大或不存在!!", Toast.LENGTH_SHORT).show();
    	return null;
    }  

    @Override  
    protected void onProgressUpdate(Integer... progress) {  
    	dialog.setProgress(progress[0]);  
    }  

    @Override  
    protected void onPostExecute(Void result) {  
        try {  
            dialog.dismiss(); 
            if(resulted)
            	Toast.makeText(context.getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
            else
            	Toast.makeText(context.getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {  
        }  
    }  
    
    private boolean checkSize(){
    	File file=new File(Common.PATH);
    	if(file.exists()&&file.length()<FILESIZE)
    		return true;
    	else return false;
    }
}