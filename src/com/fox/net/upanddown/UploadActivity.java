/**
 * 
 */
package com.fox.net.upanddown;

import com.fox.utils.*;
import com.fox.picfox.R;
import android.app.Activity;  
import android.app.AlertDialog;    
import android.content.DialogInterface;   
import android.os.Bundle;  
import android.view.View;  
import android.widget.Button;  
import android.widget.TextView;  
  
public class UploadActivity extends Activity {  

	int serverResponseCode;//���ͺ󷵻ص�code
    private TextView mtv1 = null;  
    private TextView mtv2 = null;  
    private Button bupload = null;  
 
  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.upload); 
  
        mtv1 = (TextView) findViewById(R.id.local_path);  
        mtv1.setText("�ļ�·����\n" + Common.PATH);  
        mtv2 = (TextView) findViewById(R.id.upload_path);  
        mtv2.setText("�ϴ���ַ��\n" + Common.upload+"/"+Common.DIR);  
        bupload = (Button) findViewById(R.id.upload_button);  
        bupload.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub   
            	//ִ���ϴ�����
                UploadTask fileuploadtask = new UploadTask(UploadActivity.this);  
                fileuploadtask.execute();
                
            }  
        });  
    }  
  
    // show Dialog method   
    @SuppressWarnings("unused")
	private void showDialog(String mess) {  
        new AlertDialog.Builder(UploadActivity.this).setTitle("Message")  
        .setMessage(mess)  
                .setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    }  
                }).show();  
    }  

   
}  
