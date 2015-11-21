package com.perky.safeguard361.receivers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class KillAllReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningAppProcessInfo process : am.getRunningAppProcesses()) {
			am.killBackgroundProcesses(process.processName);
		}
		Toast.makeText(context, "«Â¿ÌÕÍ±œ", Toast.LENGTH_SHORT).show();
	}

}
