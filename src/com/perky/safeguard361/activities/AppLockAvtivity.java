package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.fragments.LockedFragment;
import com.perky.safeguard361.fragments.UnlockedFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AppLockAvtivity extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = "AppLockAvtivity";
	private TextView tv_unlocked;
	private TextView tv_locked;
	private FragmentManager fm;
	private LockedFragment lockedFragment;
	private UnlockedFragment unlockedFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		tv_unlocked = (TextView) findViewById(R.id.tv_unlocked);
		tv_locked = (TextView) findViewById(R.id.tv_locked);
		fm = getSupportFragmentManager();

		tv_unlocked.setOnClickListener(this);
		tv_locked.setOnClickListener(this);

		lockedFragment = new LockedFragment();
		unlockedFragment = new UnlockedFragment();

		FragmentTransaction trx = fm.beginTransaction();
		trx.replace(R.id.fl_container, unlockedFragment);
		trx.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction trx = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_unlocked:
			tv_unlocked.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_locked.setBackgroundResource(R.drawable.tab_right_default);
			Log.i(TAG,"替换unlockedFragment的界面");
			trx.replace(R.id.fl_container, unlockedFragment);
			break;
		case R.id.tv_locked:
			tv_unlocked.setBackgroundResource(R.drawable.tab_left_default);
			tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
			Log.i(TAG,"替换lockedFragment的界面");
			trx.replace(R.id.fl_container, lockedFragment);
			break;
		}
		trx.commit();
	}
}
