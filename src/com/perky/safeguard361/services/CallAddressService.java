package com.perky.safeguard361.services;

import com.perky.safeguard361.R;
import com.perky.safeguard361.db.dao.NumberAddressDao;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

public class CallAddressService extends Service {

	private OutCallReceiver outCallReceiver;
	private MyPhoneListener phoneListener;
	private TelephonyManager tm;
	private View view;
	private SharedPreferences sp;
	private static final int[] bgs = { R.drawable.call_locate_white,
			R.drawable.call_locate_orange, R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green };
	/**
	 * 窗体管理的服务。
	 */
	private WindowManager windowManager;
	/**
	 * view对象在窗体上的参数。
	 */
	private WindowManager.LayoutParams mParams;

	protected static final String TAG = "ShowLocationService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		outCallReceiver = new OutCallReceiver();
		registerReceiver(outCallReceiver, new IntentFilter(
				Intent.ACTION_NEW_OUTGOING_CALL));
		phoneListener = new MyPhoneListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy sssss");
		unregisterReceiver(outCallReceiver);
		outCallReceiver = null;
		tm.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
		phoneListener = null;
		super.onDestroy();
	}

	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "OutCallReceiver");
			String number = getResultData();
			String address = NumberAddressDao.getLocation(number);
			showMyToast(address);
		}

	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					windowManager.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				String address = NumberAddressDao.getLocation(incomingNumber);
				showMyToast(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			}
		}
	}

	private void showMyToast(String address) {
		Log.i(TAG, "showMyToast 1");
		view = View.inflate(this, R.layout.toast_number_address, null);
		int which = sp.getInt("which", 0);
		// "半透明","活力橙","卫士蓝","金属灰","苹果绿"
		view.setBackgroundResource(bgs[which]);
		TextView tv_address = (TextView) view
				.findViewById(R.id.tv_toast_address);
		tv_address.setText(address);

		Log.i(TAG, "showMyToast 2");
		mParams = new WindowManager.LayoutParams();
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

		// 左上角对齐
		mParams.gravity = Gravity.LEFT + Gravity.TOP;
		mParams.x = sp.getInt("lastx", 0);
		mParams.y = sp.getInt("lasty", 0);

		mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;自定义的土司需要用户触摸
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mParams.format = PixelFormat.TRANSLUCENT;
		// mParams.type = WindowManager.LayoutParams.TYPE_TOAST;土司窗体天生不响应触摸事件
		mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		Log.i(TAG, "showMyToast 3");
		// 把view添加到手机窗体上。
		windowManager.addView(view, mParams);
		Log.i(TAG, "showMyToast 4");
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					mParams.x += dx;
					mParams.y += dy;
					if (mParams.x < 0) {
						mParams.x = 0;
					}
					if (mParams.y < 0) {
						mParams.y = 0;
					}
					if (mParams.x > (windowManager.getDefaultDisplay()
							.getWidth() - view.getWidth())) {
						mParams.x = windowManager.getDefaultDisplay()
								.getWidth() - view.getWidth();
					}
					if (mParams.y > (windowManager.getDefaultDisplay()
							.getHeight() - view.getHeight())) {
						mParams.y = windowManager.getDefaultDisplay()
								.getHeight() - view.getHeight();
					}
					windowManager.updateViewLayout(v, mParams);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					Editor editor = sp.edit();
					editor.putInt("lastx", mParams.x);
					editor.putInt("lasty", mParams.y);
					editor.commit();
					break;
				}
				return true;
			}
		});
	}

}
