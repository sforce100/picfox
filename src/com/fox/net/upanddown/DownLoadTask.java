package com.fox.net.upanddown;
/**
 * 通过AsyncTask使上传在后台运行..防止UI阻塞
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fox.utils.NetUtils;

public class DownLoadTask extends AsyncTask<Object, Integer, Void> {  
	  
	private NetUtils net;
	private String path;
    private ProgressDialog dialog = null;  
    private boolean resulted=false; 
    private Context context;
    public DownLoadTask(Context context,String path){
    	this.context=context;
    	this.path=path;
    }
    
    @Override  
    protected void onPreExecute() {  
        dialog = new ProgressDialog(context);  
        dialog.setMessage("正在下载...");  
        dialog.setIndeterminate(false);  
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        dialog.setProgress(0);  
        dialog.show();  
    }  

    @Override  
    protected Void doInBackground(Object... arg0) {
    	//下载
		net=new NetUtils();
		Bitmap down=net.download(path);
		if(down!=null){
			NetUtils.download(context, down);
			resulted=true;
		}
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
            if(resulted){
            	Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
            	//不能这里finish引用窗口..会导致leaked window错误
            	//((Activity) context).finish();
            }
            else
            	Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {  
        }  
    }        
}