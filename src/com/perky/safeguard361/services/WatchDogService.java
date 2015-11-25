package com.perky.safeguard361.services;

import java.util.List;

import com.perky.safeguard361.activities.EnterPwdActivity;
import com.perky.safeguard361.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WatchDogService extends Service {
	private boolean flag = false;
	private ActivityManager am;
	private RunningTaskInfo runningTask;
	private List<String> lockedApps;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		AppLockDao dao = new AppLockDao(this);
		lockedApps = dao.findAll();
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		new Thread() {
			public void run() {
				flag = true;
				while (flag) {
					runningTask = am.getRunningTasks(1).get(0);
					String pkg = runningTask.topActivity.getPackageName();
					if (lockedApps.contains(pkg)) {
						Intent intent = new Intent(WatchDogService.this,
								EnterPwdActivity.class);
						startActivity(intent);
					}
				}
			};
		}.start();
		super.onCreate();
	}
}
