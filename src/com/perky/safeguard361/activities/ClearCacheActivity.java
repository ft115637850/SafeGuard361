package com.perky.safeguard361.activities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.UIUtils;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClearCacheActivity extends Activity {
	protected static final int SCANNING = 0;
	protected static final int FINISH = 1;
	private PackageManager pm;
	private List<CacheInfo> cacheInfos;
	private TextView tv_scan_status;
	private LinearLayout ll_loading;
	private LinearLayout ll_container;
	private Button btn_clear;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANNING:
				PackageInfo info = (PackageInfo) msg.obj;
				tv_scan_status.setText("正在扫描："
						+ info.applicationInfo.loadLabel(pm));
				break;
			case FINISH:
				ll_loading.setVisibility(View.INVISIBLE);
				ll_container.setVisibility(View.VISIBLE);
				if (cacheInfos.size() == 0) {
					Toast.makeText(getApplicationContext(), "恭喜您，您的手机干净无比...",
							1).show();
					return;
				}

				for (CacheInfo cacheInfo : cacheInfos) {
					View view = View.inflate(getApplicationContext(),
							R.layout.item_cache_info, null);
					ImageView iv_app_icon = (ImageView) view
							.findViewById(R.id.iv_app_icon);
					TextView tv_app_name = (TextView) view
							.findViewById(R.id.tv_app_name);
					TextView tv_cache_size = (TextView) view
							.findViewById(R.id.tv_cache_size);

					iv_app_icon.setImageDrawable(cacheInfo.icon);
					tv_app_name.setText(cacheInfo.appname);
					tv_cache_size.setText(Formatter.formatFileSize(
							getApplicationContext(), cacheInfo.cachesize));
					ll_container.addView(view);
				}
				btn_clear.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clearcache);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		pm = getPackageManager();
		getCacheSizeForAll();
	}

	public void getCacheSizeForAll() {
		new Thread() {
			@Override
			public void run() {
				cacheInfos = new ArrayList<ClearCacheActivity.CacheInfo>();
				List<PackageInfo> pkgs = pm.getInstalledPackages(0);
				for (PackageInfo pkg : pkgs) {
					getCacheSize(pkg);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.what = SCANNING;
					msg.obj = pkg;
					handler.sendMessage(msg);
				}
				Message msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
				super.run();
			}
		}.start();
	}

	public void clearAll(View view) {
		// 清除全部 缓存 利用Android系统的一个漏洞。 freeStorageAndNotify
		
		Method[] methods = PackageManager.class.getMethods();
		for (Method method : methods) {
			if ("freeStorageAndNotify".equals(method.getName())) {
				try {
					method.invoke(pm, Integer.MAX_VALUE,
							new ClearCacheObserver());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		ll_container.removeAllViews();
		UIUtils.showToast(this, "清理完毕");
	}

	private class ClearCacheObserver extends IPackageDataObserver.Stub {
		public void onRemoveCompleted(final String packageName,
				final boolean succeeded) {
			System.out.println(succeeded);
		}
	}

	/**
	 * 获取某个包名对应的应用程序的缓存大小
	 * 
	 * @param info
	 *            应用程序的包信息
	 */
	public void getCacheSize(PackageInfo info) {
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			method.invoke(pm, info.packageName, new MyPackObserver(info));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyPackObserver extends
			android.content.pm.IPackageStatsObserver.Stub {
		private PackageInfo info;

		public MyPackObserver(PackageInfo info) {
			this.info = info;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;
			if (cachesize > 0) {
				System.out.println("应用程序有缓存："
						+ info.applicationInfo.loadLabel(pm)
						+ "--"
						+ Formatter.formatFileSize(getApplicationContext(),
								cachesize));
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.cachesize = cachesize;
				cacheInfo.packname = info.packageName;
				cacheInfo.appname = info.applicationInfo.loadLabel(pm)
						.toString();
				cacheInfo.icon = info.applicationInfo.loadIcon(pm);
				cacheInfos.add(cacheInfo);
			}
		}

	}

	class CacheInfo {
		String packname;
		String appname;
		long cachesize;
		Drawable icon;
	}
}
