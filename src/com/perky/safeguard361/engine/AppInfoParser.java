package com.perky.safeguard361.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.perky.safeguard361.domain.AppInfo;

public class AppInfoParser {
	public static List<AppInfo> getAppInfos(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		List<PackageInfo> pkgs = pm.getInstalledPackages(0);
		List<AppInfo> apps = new ArrayList<AppInfo>();
		for (PackageInfo pkg : pkgs) {
			AppInfo app = new AppInfo();
			app.setApkName(pkg.packageName);
			app.setIcon(pkg.applicationInfo.loadIcon(pm));
			String apkPath = pkg.applicationInfo.sourceDir;
			app.setApkPath(apkPath);
			File file = new File(apkPath);
			app.setAppSize(file.length());
			app.setName(pkg.applicationInfo.loadLabel(pm).toString());

			int flags = pkg.applicationInfo.flags;
			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
				app.setInMemory(false);
			} else {
				app.setInMemory(true);
			}
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				app.setUsrApp(false);
			} else {
				app.setUsrApp(true);
			}
			apps.add(app);
			app = null;
		}
		return apps;
	}
}
