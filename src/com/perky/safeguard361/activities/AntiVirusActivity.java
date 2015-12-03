package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {
	private ProgressBar pb_apps;
	private LinearLayout ll_container;
	private TextView tv_scan_status;
	private ImageView iv_scan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivirus);
		pb_apps = (ProgressBar) findViewById(R.id.pb_apps);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);

		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1500);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
	}

}
