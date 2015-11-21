package com.perky.safeguard361.services;

import java.util.Timer;
import java.util.TimerTask;

import com.perky.safeguard361.R;
import com.perky.safeguard361.receivers.MyWidget;
import com.perky.safeguard361.utils.SystemInfoUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	protected static final String TAG = "UpdateWidgetService";
	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				Log.i(TAG, "更新widget");
				ComponentName provider = new ComponentName(
						getApplicationContext(), MyWidget.class);
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				views.setTextViewText(
						R.id.process_count,
						"正在运行软件:"
								+ SystemInfoUtils
										.getProcessCount(getApplicationContext()));
				String availsize = Formatter.formatFileSize(
						getApplicationContext(),
						SystemInfoUtils.getAvailMem(getApplicationContext()));
				views.setTextViewText(R.id.process_memory, "可用内存:" + availsize);
				Intent intent = new Intent("com.perky.killall");
				PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0, 5000);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null && task != null) {
			task.cancel();
			timer.cancel();
			task = null;
			timer = null;
		}
	}

}
