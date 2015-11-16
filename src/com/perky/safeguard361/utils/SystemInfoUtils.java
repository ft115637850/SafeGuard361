package com.perky.safeguard361.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
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
	
	public static int getProcessCount(Context ctx){
		ActivityManager am = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
		return processes.size();
	}
	
	public static long getAvailMem(Context ctx){
		ActivityManager am = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMem = outInfo.availMem;
		return availMem;
	}
	
	public static long getTotalMem() {
		try {
			FileInputStream fs = new FileInputStream(new File("/proc/meminfo"));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(fs));
			String totalInfo = reader.readLine();
			StringBuilder sb = new StringBuilder();
			for (char c : totalInfo.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
