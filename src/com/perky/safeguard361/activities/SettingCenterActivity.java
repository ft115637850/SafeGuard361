package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.services.CallAddressService;
import com.perky.safeguard361.services.CallSmsSafeService;
import com.perky.safeguard361.services.WatchDogService;
import com.perky.safeguard361.ui.SettingView;
import com.perky.safeguard361.utils.SystemInfoUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingCenterActivity extends Activity {
	private SharedPreferences sp;
	private SettingView sv_autoupdate;
	private TextView tv_style;

	/**
	 * 黑名单
	 */
	private SettingView sv_blacknumber;
	private Intent callSmsSafeIntent;

	/**
	 * 程序锁
	 */
	private SettingView sv_lockapp;
	private Intent watchDogIntent;
	/**
	 * 归属地显示
	 */
	private SettingView sv_showaddress;
	private Intent showAddressIntent;
	
	private static final String[] items ={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_style = (TextView) findViewById(R.id.tv_style);
		tv_style.setText(items[sp.getInt("which", 0)]);

		sv_autoupdate = (SettingView) findViewById(R.id.sv_autoupdate);
		sv_autoupdate.setChecked(sp.getBoolean("autoupdate", false));
		sv_autoupdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				// 判断勾选的状态。
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

		// 黑名单
		sv_blacknumber = (SettingView) findViewById(R.id.sv_blacknumber);
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		sv_blacknumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sv_blacknumber.isChecked()) {
					sv_blacknumber.setChecked(false);
					// 停止拦截服务
					stopService(callSmsSafeIntent);
				} else {
					sv_blacknumber.setChecked(true);
					// 开启拦截服务
					startService(callSmsSafeIntent);
				}
			}
		});
		
		//程序锁
		sv_lockapp = (SettingView) findViewById(R.id.sv_lockapp);
		watchDogIntent = new Intent(this, WatchDogService.class);
		sv_lockapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sv_lockapp.isChecked()) {
					sv_lockapp.setChecked(false);
					// 停止拦截服务
					stopService(watchDogIntent);
				} else {
					sv_lockapp.setChecked(true);
					// 开启拦截服务
					startService(watchDogIntent);
				}
			}
		});

		sv_showaddress = (SettingView) findViewById(R.id.sv_showaddress);
		showAddressIntent = new Intent(this, CallAddressService.class);
		sv_showaddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sv_showaddress.isChecked()) {
					sv_showaddress.setChecked(false);
					// 停止拦截服务
					stopService(showAddressIntent);
				} else {
					sv_showaddress.setChecked(true);
					// 开启拦截服务
					startService(showAddressIntent);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		boolean callSmsRunning = SystemInfoUtils.isServiceRunning(this,
				"com.perky.safeguard361.services.CallSmsSafeService");
		if (callSmsRunning) {
			sv_blacknumber.setChecked(true);
		} else {
			sv_blacknumber.setChecked(false);
		}
		
		boolean watchDogRunning = SystemInfoUtils.isServiceRunning(this,
				"com.perky.safeguard361.services.WatchDogService");
		if (watchDogRunning) {
			sv_lockapp.setChecked(true);
		} else {
			sv_lockapp.setChecked(false);
		}
		
		boolean showAddressRunning = SystemInfoUtils.isServiceRunning(this,
				"com.perky.safeguard361.services.CallAddressService");
		if (showAddressRunning) {
			sv_showaddress.setChecked(true);
		} else {
			sv_showaddress.setChecked(false);
		}
		super.onStart();
	}
	
	public void changeStyle(View view){
		AlertDialog.Builder dlgBld = new Builder(this);
		dlgBld.setIcon(R.drawable.ic_launcher);
		dlgBld.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Editor edt = sp.edit();
				edt.putInt("which", which);
				edt.commit();
				tv_style.setText(items[which]);
				dialog.dismiss();
			}
		});
		dlgBld.setTitle("归属地提示框风格");
		dlgBld.show();
	}
}
