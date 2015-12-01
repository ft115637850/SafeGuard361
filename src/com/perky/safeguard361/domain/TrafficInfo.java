package com.perky.safeguard361.domain;

import android.graphics.drawable.Drawable;

public class TrafficInfo {
	private Drawable icon;
	private String name;
	private long tcp_rcv;

	@Override
	public String toString() {
		return "TrafficInfo [name=" + name + ", tcp_rcv=" + tcp_rcv
				+ ", tcp_snd=" + tcp_snd + "]";
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

	public long getTcp_rcv() {
		return tcp_rcv;
	}

	public void setTcp_rcv(long tcp_rcv) {
		this.tcp_rcv = tcp_rcv;
	}

	public long getTcp_snd() {
		return tcp_snd;
	}

	public void setTcp_snd(long tcp_snd) {
		this.tcp_snd = tcp_snd;
	}

	private long tcp_snd;
}
