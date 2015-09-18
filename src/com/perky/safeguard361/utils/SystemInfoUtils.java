package com.perky.safeguard361.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class SystemInfoUtils {
	public static boolean isServiceRunning(Context ctx, String className){
		ActivityManager am= (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(200);
		for (RunningServiceInfo info : infos){
			String serviceClassName = info.service.getClassName();
			if (serviceClassName.equals(className))
				return true;
		}
		return false;
	}
}
