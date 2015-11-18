package com.perky.safeguard361.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AutoKillService extends Service {

	public static final String TAG = "AutoKillService";
	private ScreenLockReceiver receiver;

	private class ScreenLockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "À¯∆¡¡À");
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for (RunningAppProcessInfo process : am.getRunningAppProcesses()) {
				am.killBackgroundProcesses(process.processName);
			}
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new ScreenLockReceiver();
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
