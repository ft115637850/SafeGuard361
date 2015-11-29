package com.perky.safeguard361.services;

import java.util.List;

import com.perky.safeguard361.activities.EnterPwdActivity;
import com.perky.safeguard361.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class WatchDogService extends Service {
	protected static final String TAG = "WatchDogService";
	private boolean flag = false;
	private ActivityManager am;
	private RunningTaskInfo runningTask;
	private List<String> lockedApps;
	private String tempStopProtectPackname;
	private WatchDogReceiver receiver;
	private AppLockDao dao;
	private AppLockObserver observer;
	private Intent intent;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		dao = new AppLockDao(this);
		lockedApps = dao.findAll();

		observer = new AppLockObserver(new Handler());
		getContentResolver().registerContentObserver(
				Uri.parse("content://com.perky.safeguard361.applock.update"),
				true, observer);

		receiver = new WatchDogReceiver();
		IntentFilter filter = new IntentFilter(
				"com.perky.safeguard361.stopapplock");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		intent = new Intent(WatchDogService.this,
				EnterPwdActivity.class);
		startWatchDog();

		super.onCreate();
	}

	private void startWatchDog() {
		new Thread() {
			public void run() {
				flag = true;
				while (flag) {
					runningTask = am.getRunningTasks(1).get(0);
					String pkg = runningTask.topActivity.getPackageName();
					Log.i(TAG, "WatchDog is running");
					if (!pkg.equals(tempStopProtectPackname)
							&& lockedApps.contains(pkg)) {
						Log.i(TAG, "start EnterPwdActivity");
						
						intent.putExtra("pkgName", pkg);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(receiver);
		receiver = null;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
		super.onDestroy();
	}

	private class WatchDogReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.perky.safeguard361.stopapplock".equals(intent.getAction())) {
				tempStopProtectPackname = intent.getStringExtra("pkgName");
			} else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				tempStopProtectPackname = null;
				flag = false;
			} else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
				if (flag == false) {
					startWatchDog();
				}
			}
		}
	}

	private class AppLockObserver extends ContentObserver {

		public AppLockObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.i(TAG, "数据库数据变化了");
			lockedApps = dao.findAll();
		}
	}
}
