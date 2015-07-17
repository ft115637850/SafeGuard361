package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.ui.SettingView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingCenterActivity extends Activity {
	private SharedPreferences sp;
	private SettingView sv_autoupdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		sv_autoupdate = (SettingView) findViewById(R.id.sv_autoupdate);
		sv_autoupdate.setChecked(sp.getBoolean("autoupdate", false));
		sv_autoupdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				// ÅÐ¶Ï¹´Ñ¡µÄ×´Ì¬¡£
				if (sv_autoupdate.isChecked()) {
					sv_autoupdate.setChecked(false);
					editor.putBoolean("autoupdate", false);
				} else {
					sv_autoupdate.setChecked(true);
					editor.putBoolean("autoupdate", true);
				}
				editor.commit();
			}
		});

	}
}
