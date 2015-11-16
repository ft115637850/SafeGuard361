package com.perky.safeguard361.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private String apkPath;
	private Drawable icon;

	public String getApkPath() {
		return apkPath;
	}

	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInMemory() {
		return isInMemory;
	}

	public void setInMemory(boolean isInMemory) {
		this.isInMemory = isInMemory;
	}

	public boolean isUsrApp() {
		return isUsrApp;
	}

	public void setUsrApp(boolean isUsrApp) {
		this.isUsrApp = isUsrApp;
	}

	public long getAppSize() {
		return appSize;
	}

	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	private String name;
	private boolean isInMemory;
	private boolean isUsrApp;
	private long appSize;
	private String apkName;

	@Override
	public String toString() {
		return "AppInfo [apkPath=" + apkPath + ", name=" + name
				+ ", isInMemory=" + isInMemory + ", isUsrApp=" + isUsrApp
				+ ", appSize=" + appSize + ", apkName=" + apkName + "]";
	}
}
