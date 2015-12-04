package com.perky.safeguard361.activities;

import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.db.dao.AntiVirusDao;
import com.perky.safeguard361.utils.MD5Utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {
	protected static final int SCAN_BEGIN = 0;
	protected static final int SCANNING = 1;
	protected static final int SCAN_COMPLETE = 2;
	protected static final String TAG = "AntiVirusActivity";
	private ProgressBar pb_apps;
	private LinearLayout ll_container;
	private TextView tv_scan_status;
	private ImageView iv_scan;
	private PackageManager pm;
	private boolean runFlag;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_BEGIN:
				tv_scan_status.setText("正在初始化双核杀毒引擎...");
				break;
			case SCANNING:
				tv_scan_status.setText("正在扫描...");
				ScanInfo info = (ScanInfo) msg.obj;
				TextView tv_child = new TextView(getApplicationContext());
				if (info.isVirus) {
					tv_child.setTextColor(Color.RED);
				} else {
					tv_child.setTextColor(Color.BLACK);
				}
				tv_child.setText(info.appName + ":" + info.description);
				ll_container.addView(tv_child, 0);
				break;
			case SCAN_COMPLETE:
				tv_scan_status.setText("扫描结束");
				iv_scan.clearAnimation();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivirus);
		pb_apps = (ProgressBar) findViewById(R.id.pb_apps);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);

		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1500);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
		pm = getPackageManager();
		scanVirus();
	}

	public void scanVirus() {
		runFlag = true;
		new Thread() {
			@Override
			public void run() {
				if (!runFlag)
					return;
				List<PackageInfo> pkgs = pm.getInstalledPackages(0);
				Message msg = Message.obtain();
				msg.what = SCAN_BEGIN;
				handler.sendMessage(msg);
				pb_apps.setMax(pkgs.size());
				int progress = 0;
				for (PackageInfo pkg : pkgs) {
					String md5 = MD5Utils
							.getFileMD5(pkg.applicationInfo.sourceDir);
					String desc = AntiVirusDao.checkVirus(md5);
					ScanInfo scanInfo = new ScanInfo();
					if (desc == null) {
						scanInfo.description = "扫描安全";
						scanInfo.isVirus = false;
					} else {
						scanInfo.description = desc;
						scanInfo.isVirus = true;
					}

					scanInfo.appName = pkg.applicationInfo.loadLabel(pm)
							.toString();
					Log.i(TAG, scanInfo.appName + ":" + md5 + ":"
							+ pkg.packageName);
					msg = Message.obtain();
					msg.what = SCANNING;
					msg.obj = scanInfo;
					handler.sendMessage(msg);
					pb_apps.setProgress(++progress);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				msg = Message.obtain();
				msg.what = SCAN_COMPLETE;
				handler.sendMessage(msg);
				super.run();
			}
		}.start();
	}

	private class ScanInfo {
		String appName;
		String description;
		boolean isVirus;
	}

	@Override
	protected void onDestroy() {
		runFlag = false;
		super.onDestroy();
	}

}
