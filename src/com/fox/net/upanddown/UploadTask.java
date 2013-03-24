/**
 * ͨ��AsyncTaskʹ�ϴ��ں�̨����..��ֹUI����
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
        dialog.setMessage("�����ϴ�...");  
        dialog.setIndeterminate(false);  
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        dialog.setProgress(0);  
        dialog.show();  
    }  

    @Override  
    protected Void doInBackground(Object... arg0) {
    	
    	if(checkSize()){
	    	if(NetUtils.upload())//�ϴ�ͼƬ
	    		resulted=true;
	    	else
	    		resulted=false;			 
    	}
    	else
    		Toast.makeText(context.getApplicationContext(), "�ϴ�ʧ��!!ͼƬ̫��򲻴���!!", Toast.LENGTH_SHORT).show();
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
            	Toast.makeText(context.getApplicationContext(), "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
            else
            	Toast.makeText(context.getApplicationContext(), "�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
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