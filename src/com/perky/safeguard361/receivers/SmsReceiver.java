package com.perky.safeguard361.receivers;

import com.perky.safeguard361.R;
import com.perky.safeguard361.activities.PhotoActivity;
import com.perky.safeguard361.services.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "message arrived");
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);

		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String safeNumber = sp.getString("safenumber", "");

		for (Object obj : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
			String sender = sms.getOriginatingAddress();
			if (!sender.contains(safeNumber))
				continue;
			String body = sms.getMessageBody();
			if ("#*location*#".equals(body)) {
				Log.i(TAG, "location send");
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
				abortBroadcast();
			} else if ("#*alarm*#".equals(body)) {
				Log.i(TAG, "alarm play");
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			} else if ("#*wipedata*#".equals(body)) {
				Log.i(TAG, "wipedata");
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				abortBroadcast();
			} else if (body.startsWith("#*lockscreen*#")) {
				Log.i(TAG, "lockscreen");
				String[] s = body.split("#");
				if (s.length > 2) {
					dpm.resetPassword(s[2], 0);
				}else
				{
					dpm.resetPassword("123456", 0);
				}
				dpm.lockNow();
				abortBroadcast();
			} else if ("#*takephoto*#".equals(body)) {
				Intent intnt = new Intent(context, PhotoActivity.class);
				intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intnt);
				abortBroadcast();
			}
		}
	}

}
