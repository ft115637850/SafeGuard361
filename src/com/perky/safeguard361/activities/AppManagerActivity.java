package com.perky.safeguard361.activities;

import java.util.ArrayList;
import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.domain.AppInfo;
import com.perky.safeguard361.engine.AppInfoParser;
import com.perky.safeguard361.utils.UIUtils;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppManagerActivity extends Activity implements OnClickListener {
	private ListView lv_apps;
	private TextView tv_memory;
	private TextView tv_sd;
	private ProgressBar pb_apps;
	private TextView tv_appCount;
	private AppInfo clickedApp;
	private PopupWindow popupWin;
	private LinearLayout ll_uninstall;
	private LinearLayout ll_start;
	private LinearLayout ll_share;
	private LinearLayout ll_setting;
	private UninstallReceiver receiver;

	private List<AppInfo> apps;
	private List<AppInfo> systemApps;
	private List<AppInfo> usrApps;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pb_apps.setVisibility(View.INVISIBLE);
			lv_apps.setVisibility(View.VISIBLE);
			lv_apps.setAdapter(new AppListAdapter());
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmgr);
		lv_apps = (ListView) findViewById(R.id.lv_apps);
		pb_apps = (ProgressBar) findViewById(R.id.pb_apps);
		tv_memory = (TextView) findViewById(R.id.tv_memory);
		tv_sd = (TextView) findViewById(R.id.tv_sd);
		tv_appCount = (TextView) findViewById(R.id.tv_appCount);

		long sd = Environment.getExternalStorageDirectory().getFreeSpace();
		long memory = Environment.getDataDirectory().getFreeSpace();
		String str_sd = Formatter.formatFileSize(this, sd);
		String str_memory = Formatter.formatFileSize(this, memory);
		tv_sd.setText("sd卡可用：" + str_sd);
		tv_memory.setText("内存可用：" + str_memory);
		fillData();
		lv_apps.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopup();
				if (systemApps != null && usrApps != null) {
					if (firstVisibleItem > usrApps.size()) {
						tv_appCount.setText("系统程序: " + systemApps.size() + "个");
					} else {
						tv_appCount.setText("用户程序: " + usrApps.size() + "个");
					}
				}
			}
		});

		lv_apps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = lv_apps.getItemAtPosition(position);
				if (item != null && item instanceof AppInfo) {
					clickedApp = (AppInfo) item;
					Log.i("APP", clickedApp.getName());
					View popup = View.inflate(getApplicationContext(),
							R.layout.popup_appmgr, null);
					dismissPopup();
					popupWin = new PopupWindow(popup,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					// 播放动画必须有背景
					popupWin.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));
					int[] location = new int[2];
					view.getLocationInWindow(location);
					popupWin.showAtLocation(parent, Gravity.TOP + Gravity.LEFT,
							60, location[1]);
					ScaleAnimation sa = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0.5f);
					sa.setDuration(200);
					AlphaAnimation aa = new AlphaAnimation(0.5f, 1f);
					aa.setDuration(200);
					AnimationSet set = new AnimationSet(false);
					set.addAnimation(sa);
					set.addAnimation(aa);
					popup.startAnimation(set);

					ll_uninstall = (LinearLayout) popup
							.findViewById(R.id.ll_uninstall);
					ll_start = (LinearLayout) popup.findViewById(R.id.ll_start);
					ll_share = (LinearLayout) popup.findViewById(R.id.ll_share);
					ll_setting = (LinearLayout) popup
							.findViewById(R.id.ll_setting);
					ll_uninstall.setOnClickListener(AppManagerActivity.this);
					ll_start.setOnClickListener(AppManagerActivity.this);
					ll_share.setOnClickListener(AppManagerActivity.this);
					ll_setting.setOnClickListener(AppManagerActivity.this);
				}
			}
		});
		receiver = new UninstallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);
	}

	private void fillData() {
		new Thread() {
			public void run() {
				apps = AppInfoParser.getAppInfos(getApplicationContext());
				usrApps = new ArrayList<AppInfo>();
				systemApps = new ArrayList<AppInfo>();
				for (AppInfo app : apps) {
					if (app.isUsrApp()) {
						usrApps.add(app);
					} else {
						systemApps.add(app);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	private void dismissPopup() {
		if (popupWin != null && popupWin.isShowing()) {
			popupWin.dismiss();
			popupWin = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_uninstall:
			Log.i("APP", "删除App");
			uninstallApp();
			break;
		case R.id.ll_start:
			Log.i("APP", "开始App");
			startApp();
			break;
		case R.id.ll_share:
			Log.i("APP", "共享App");
			shareApp();
			break;
		case R.id.ll_setting:
			Log.i("APP", "设置App");
			viewAppDetail();
			break;
		}
		dismissPopup();
	}

	private void viewAppDetail() {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + clickedApp.getApkName()));
		startActivity(intent);
	}

	private void startApp() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(clickedApp.getApkName());
		if (intent != null) {
			startActivity(intent);
		} else {
			UIUtils.showToast(this, "该应用没有启动界面");
		}
	}

	private void uninstallApp() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + clickedApp.getApkName()));
		startActivity(intent);
	}

	private void shareApp() {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"推荐您使用一款软件，名称叫：" + clickedApp.getName()
						+ "下载路径：https://play.google.com/store/apps/details?id="
						+ clickedApp.getApkName());
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		dismissPopup();
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private class ViewHolder {
		ImageView iv_appicon;
		TextView tv_appsize;
		TextView tv_appname;
		TextView tv_applocation;
	}

	private class AppListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return apps.size();
		}

		@Override
		public Object getItem(int position) {
			int usrAppsCount = usrApps.size();
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户程序: " + usrAppsCount + "个");
				return null;
			} else if (position == (usrAppsCount + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序: " + systemApps.size() + "个");
				return null;
			}
			AppInfo app;
			if (position < usrAppsCount + 1) {
				app = usrApps.get(position - 1);
			} else {
				int location = position - usrAppsCount - 2;
				app = systemApps.get(location);
			}
			return app;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int usrAppsCount = usrApps.size();
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户程序: " + usrAppsCount + "个");
				return tv;
			} else if (position == (usrAppsCount + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序: " + systemApps.size() + "个");
				return tv;
			}
			AppInfo app;
			if (position < usrAppsCount + 1) {
				app = usrApps.get(position - 1);
			} else {
				int location = position - usrAppsCount - 2;
				app = systemApps.get(location);
			}

			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_appmgr, null);
				holder = new ViewHolder();
				holder.iv_appicon = (ImageView) view
						.findViewById(R.id.iv_appicon);
				holder.tv_appsize = (TextView) view
						.findViewById(R.id.tv_appsize);
				holder.tv_appname = (TextView) view
						.findViewById(R.id.tv_appname);
				holder.tv_applocation = (TextView) view
						.findViewById(R.id.tv_applocation);
				view.setTag(holder);
			}

			holder.iv_appicon.setImageDrawable(app.getIcon());
			String appSize = Formatter.formatFileSize(getApplicationContext(),
					app.getAppSize());
			holder.tv_appsize.setText(appSize);
			holder.tv_appname.setText(app.getName());
			if (app.isInMemory()) {
				holder.tv_applocation.setText("内存");
			} else {
				holder.tv_applocation.setText("SD卡");
			}
			return view;
		}
	}

	private class UninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			fillData();
		}

	}
}
