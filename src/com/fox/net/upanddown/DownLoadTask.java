package com.fox.net.upanddown;
/**
 * ͨ��AsyncTaskʹ�ϴ��ں�̨����..��ֹUI����
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
        dialog.setMessage("��������...");  
        dialog.setIndeterminate(false);  
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        dialog.setProgress(0);  
        dialog.show();  
    }  

    @Override  
    protected Void doInBackground(Object... arg0) {
    	//����
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
            	Toast.makeText(context, "���سɹ�", Toast.LENGTH_SHORT).show();
            	//��������finish���ô���..�ᵼ��leaked window����
            	//((Activity) context).finish();
            }
            else
            	Toast.makeText(context, "����ʧ��", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {  
        }  
    }        
}