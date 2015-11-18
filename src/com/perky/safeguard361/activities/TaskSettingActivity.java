package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskSettingActivity extends Activity {
	private CheckBox cb_show_sysprocess;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_show_sysprocess = (CheckBox) findViewById(R.id.cb_show_sysprocess);
		cb_show_sysprocess.setChecked(sp.getBoolean("showsysproc", true));
		cb_show_sysprocess.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor ed = sp.edit();
				ed.putBoolean("showsysproc", isChecked);
				ed.commit();
			}
		});
	}
}
