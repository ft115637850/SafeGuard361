package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.UIUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPwdActivity extends Activity {

	private String pkg;
	private ImageView iv_lock_appicon;
	private TextView tv_lock_appname;
	private EditText et_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_enterpwd);
		pkg = getIntent().getStringExtra("pkgName");
		iv_lock_appicon = (ImageView) findViewById(R.id.iv_lock_appicon);
		tv_lock_appname = (TextView) findViewById(R.id.tv_lock_appname);
		et_password = (EditText) findViewById(R.id.et_password);

		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(pkg, 0);
			iv_lock_appicon.setImageDrawable(appInfo.loadIcon(pm));
			tv_lock_appname.setText(appInfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.onCreate(savedInstanceState);
	}

	public void onClick(View view) {
		String pwd = et_password.getText().toString().trim();
		if ("123456".equals(pwd)) {
			Intent intent = new Intent("com.perky.safeguard361.stopapplock");
			intent.putExtra("pkgName", pkg);
			sendBroadcast(intent);
			finish();
		}else{
			UIUtils.showToast(this, "√‹¬Î¥ÌŒÛ");
			Animation aa= AnimationUtils.loadAnimation(this, R.anim.shake);
			et_password.startAnimation(aa);
		}
	}
}
