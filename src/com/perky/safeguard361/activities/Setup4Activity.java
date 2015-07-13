package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Setup4Activity extends BaseSetupActivity {
	private CheckBox cb_setup4_protect;
	private TextView tv_setup4_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_setup4_protect = (CheckBox) findViewById(R.id.cb_setup4_protect);
		tv_setup4_status = (TextView) findViewById(R.id.tv_setup4_status);

		cb_setup4_protect
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							tv_setup4_status.setText("防盗保护已经开启");
						} else {
							tv_setup4_status.setText("防盗保护没有开启");
						}
						Editor ed = sp.edit();
						ed.putBoolean("protecting", isChecked);
						ed.commit();
					}
				});

		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			tv_setup4_status.setText("防盗保护已经开启");
			cb_setup4_protect.setChecked(true);
		} else {
			tv_setup4_status.setText("防盗保护没有开启");
			cb_setup4_protect.setChecked(false);
		}
	}

	@Override
	public void goPre() {
		Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void goNext() {
		Editor ed = sp.edit();
		ed.putBoolean("setup", true);
		ed.commit();
		Intent intent = new Intent(Setup4Activity.this, LostFoundActivity.class);
		startActivity(intent);
		finish();
	}
}
