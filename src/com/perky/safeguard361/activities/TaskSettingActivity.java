package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.services.AutoKillService;
import com.perky.safeguard361.utils.SystemInfoUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskSettingActivity extends Activity {
	private CheckBox cb_show_sysprocess;
	private CheckBox cb_autokill;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_show_sysprocess = (CheckBox) findViewById(R.id.cb_show_sysprocess);
		cb_autokill = (CheckBox) findViewById(R.id.cb_autokill);

		cb_show_sysprocess.setChecked(sp.getBoolean("showsysproc", true));
		cb_show_sysprocess
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor ed = sp.edit();
						ed.putBoolean("showsysproc", isChecked);
						ed.commit();
					}
				});

		final Intent autoKillService = new Intent(this, AutoKillService.class);
		cb_autokill.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					startService(autoKillService);
				} else {
					stopService(autoKillService);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean isAutoKillRunning = SystemInfoUtils.isServiceRunning(this,
				"com.perky.safeguard361.services.AutoKillService");
		cb_autokill.setChecked(isAutoKillRunning);
	}
}
