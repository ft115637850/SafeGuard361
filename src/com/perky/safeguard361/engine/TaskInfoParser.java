package com.perky.safeguard361.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Debug.MemoryInfo;

import com.perky.safeguard361.domain.TaskInfo;

public class TaskInfoParser {
	public static List<TaskInfo> getRunningTaskInfos(Context ctx) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = ctx.getPackageManager();
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo appInfo : runningApps) {
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.setPackName(appInfo.processName);
			MemoryInfo[] memInfo = am.getProcessMemoryInfo(new int[] { appInfo.pid });
			long memSize = memInfo[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemSize(memSize);
			try {
				PackageInfo packInfo = pm.getPackageInfo(appInfo.processName, 0);
				taskInfo.setAppIcon(packInfo.applicationInfo.loadIcon(pm));
				taskInfo.setAppName(packInfo.applicationInfo.loadLabel(pm).toString());
				if ((ApplicationInfo.FLAG_SYSTEM&packInfo.applicationInfo.flags)!=0){
					//系统进程
					taskInfo.setUsrTask(false);
				}else{
					//用户进程
					taskInfo.setUsrTask(true);
				}
				taskInfos.add(taskInfo);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return taskInfos;

	}
}
