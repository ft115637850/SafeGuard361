package com.perky.safeguard361.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;

@SuppressWarnings("deprecation")
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			String boundSim = sp.getString("sim", "");
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String realSim = tm.getSimSerialNumber();
			if (boundSim.equals(realSim)) {
				Log.i("I", "SIM卡未变化");
			} else {
				Log.i("I", "SIM卡变化了");
				String safeNumber = sp.getString("safenumber", "");
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(safeNumber, null, "手机被盗,新sim:" + realSim,
						null, null);
			}
		} else {
			Log.i("I", "防盗保护没有开启");
		}
	}
}
