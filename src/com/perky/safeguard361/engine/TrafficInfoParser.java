package com.perky.safeguard361.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.perky.safeguard361.domain.TrafficInfo;

public class TrafficInfoParser {
	public static List<TrafficInfo> getTrafficInfos(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		List<PackageInfo> pkgs = pm.getInstalledPackages(0);
		List<TrafficInfo> infos = new ArrayList<TrafficInfo>();
		for (PackageInfo pkg : pkgs) {
			int uid = pkg.applicationInfo.uid;
			long rcv = TrafficStats.getUidRxBytes(uid);
			long snd = TrafficStats.getUidTxBytes(uid);
			if ((rcv + snd) <= 0)
				continue;

			int flags = pkg.applicationInfo.flags;
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				continue;
			}
			TrafficInfo info = new TrafficInfo();
			info.setTcp_rcv(rcv);
			info.setTcp_snd(snd);
			info.setIcon(pkg.applicationInfo.loadIcon(pm));
			info.setName(pkg.applicationInfo.loadLabel(pm).toString());
			infos.add(info);
			info = null;
		}
		return infos;
	}
}
