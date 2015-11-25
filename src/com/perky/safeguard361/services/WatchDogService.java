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

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		AppLockDao dao = new AppLockDao(this);
		lockedApps = dao.findAll();
		receiver = new WatchDogReceiver();
		IntentFilter filter = new IntentFilter(
				"com.perky.safeguard361.stopapplock");
		registerReceiver(receiver, filter);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		new Thread() {
			public void run() {
				flag = true;
				while (flag) {
					Log.i(TAG, "Watchdog running");
					runningTask = am.getRunningTasks(1).get(0);
					String pkg = runningTask.topActivity.getPackageName();
					if ((tempStopProtectPackname == null || !pkg.equals(tempStopProtectPackname))
							&& lockedApps.contains(pkg)) {
						Intent intent = new Intent(WatchDogService.this,
								EnterPwdActivity.class);
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
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private class WatchDogReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.perky.safeguard361.stopapplock".equals(intent.getAction())) {
				tempStopProtectPackname = intent.getStringExtra("pkgName");
			}
		}

	}
}
