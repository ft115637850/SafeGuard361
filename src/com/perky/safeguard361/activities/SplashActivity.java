package com.perky.safeguard361.activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.StreamUtils;
import com.perky.safeguard361.utils.UIUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private TextView tv_splash_version;
	private TextView tv_splash_progress;
	private PackageManager packageManager;
	private int version_code;
	private String downloadURL;
	private String desc;
	private DevicePolicyManager dpm;
	private ComponentName deviceAdm;
	private static final int SHOW_UPDATE_DIALOG = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				AlertDialog.Builder bld = new Builder(SplashActivity.this);
				bld.setTitle(getResources().getString(R.string.update_note));
				bld.setMessage(desc);

				bld.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						loadHomeActivity();
					}
				});

				bld.setNegativeButton(
						getResources().getString(R.string.update_later),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								loadHomeActivity();
							}
						});
				bld.setPositiveButton(
						getResources().getString(R.string.update_now),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								downloadNewVersion(downloadURL);
							}
						});
				bld.show();
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		openDeviceAdmin();
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_progress = (TextView) findViewById(R.id.tv_splash_progress);
		packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					"com.perky.safeguard361", 0);
			String version = packageInfo.versionName;
			tv_splash_version.setText(this.getString(R.string.version_number)
					+ version);
			version_code = packageInfo.versionCode;
			checkAndUpgrade();

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openDeviceAdmin() {
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		deviceAdm = new ComponentName(this,
				"com.perky.safeguard361.receivers.MyDeviceAdmin");
		if (!dpm.isAdminActive(deviceAdm)) {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			// 声明超级管理员的组件名称 其实就是一个广播接收者

			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdm);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"请开启管理员，开启后才可以远程锁屏,远程清除数据");
			startActivity(intent);
		}
	}

	private void downloadNewVersion(String url) {
		HttpUtils http = new HttpUtils();
		final String targetFile = Environment.getExternalStorageDirectory()
				+ "/SafeGuard361.apk";
		http.download(url, targetFile, new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// <action android:name="android.intent.action.VIEW" />
				// <category android:name="android.intent.category.DEFAULT" />
				// <data android:scheme="content" />
				// <data android:scheme="file" />
				// <data
				// android:mimeType="application/vnd.android.package-archive" />
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "SafeGuard361.apk")),
						"application/vnd.android.package-archive");
				startActivityForResult(intent, 0);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				UIUtils.showToast(SplashActivity.this, getResources()
						.getString(R.string.download_err));
				loadHomeActivity();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				tv_splash_progress.setText(current + "/" + total);
				super.onLoading(total, current, isUploading);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadHomeActivity();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void checkAndUpgrade() {
		new Thread() {
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					Boolean update = getSharedPreferences("config",
							MODE_PRIVATE).getBoolean("autoupdate", false);
					if (update) {
						update = checkVersion();
					}
					long endTime = System.currentTimeMillis();
					long duration = (endTime - startTime);
					if (duration <= 2000) {
						Thread.sleep(2000 - duration);
					}
					if (update) {
						Message msg = Message.obtain();
						msg.what = SHOW_UPDATE_DIALOG;
						handler.sendMessage(msg);
					} else {
						loadHomeActivity();
					}
				} catch (Exception e) {
					e.printStackTrace();
					// UIUtils.showToast(SplashActivity.this,
					// getResources().getString(R.string.update_err));
					loadHomeActivity();
				}
			}
		}.start();
	}

	/**
	 * Whether it needs to upgrade
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 * @throws JSONException
	 */
	private boolean checkVersion() throws NotFoundException, IOException,
			JSONException {
		URL url = new URL(getResources().getString(R.string.version_url));
		HttpURLConnection cnn = (HttpURLConnection) url.openConnection();
		cnn.setRequestMethod("GET");
		cnn.setConnectTimeout(5000);
		int responseCode = cnn.getResponseCode();
		if (responseCode == 200) {
			InputStream is = cnn.getInputStream();
			String versionInfo = StreamUtils.readStream(is);
			JSONObject jsonRes = new JSONObject(versionInfo);
			downloadURL = jsonRes.getString("downloadURL");
			desc = jsonRes.getString("desc");
			int newVerCode = jsonRes.getInt("verCode");
			if (newVerCode != version_code) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	private void loadHomeActivity() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
