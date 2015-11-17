package com.perky.safeguard361.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private Drawable appIcon;
	private String appName;
	private String packName;
	private boolean usrTask;
	private long memSize;
	private boolean checked;

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public boolean isUsrTask() {
		return usrTask;
	}

	public void setUsrTask(boolean usrTask) {
		this.usrTask = usrTask;
	}

	public long getMemSize() {
		return memSize;
	}

	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}

	@Override
	public String toString() {
		return "TaskInfo [appName=" + appName + ", packName=" + packName
				+ ", usrTask=" + usrTask + ", memSize=" + memSize + "]";
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
