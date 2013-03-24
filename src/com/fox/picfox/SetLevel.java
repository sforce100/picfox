package com.fox.picfox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SetLevel extends Activity {

	private Spinner spinner;
	Button save_bt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_level);
		save_bt = (Button) findViewById(R.id.save_bt);
		save_bt.setOnClickListener(buttonListener);
		spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.skin, android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		SharedPreferences sp = getSharedPreferences("PICFORSKIN",
				MODE_WORLD_READABLE);
		String skin_old = sp.getString("SKIN", null);
		int position = Integer.valueOf(skin_old);
		spinner.setSelection(position);
		
		if (skin_old.equals("0")) {
			getWindow().setBackgroundDrawableResource(R.drawable.skyblue);
		} else if (skin_old.equals("1")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_g);
		} else if (skin_old.equals("2")) {
			getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_r);
		}
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String skin_new = String.valueOf(position);
				SharedPreferences sp = getSharedPreferences("PICFORSKIN",
						MODE_WORLD_READABLE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("SKIN", skin_new);
				editor.commit();
				if (skin_new.equals("0")) {
					getWindow().setBackgroundDrawableResource(R.drawable.skyblue);
				} else if (skin_new.equals("1")) {
					getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_g);
				} else if (skin_new.equals("2")) {
					getWindow().setBackgroundDrawableResource(R.drawable.picfoxbg_r);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// button按扭响应事件
	private OnClickListener buttonListener = new OnClickListener() {
		public void onClick(View v) {
			Button button = (Button) v;
			switch (button.getId()) {
			case R.id.save_bt:
				Intent intent = new Intent(SetLevel.this, PicFoxActivity.class);
				setResult(0, intent);
				
				Toast.makeText(SetLevel.this, "设置成功 ！", Toast.LENGTH_LONG)
						.show();
				finish();
				break;
			}
		}
	};

}
