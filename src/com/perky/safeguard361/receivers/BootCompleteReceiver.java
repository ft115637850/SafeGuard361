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
				Log.i("I", "SIM��δ�仯");
			} else {
				Log.i("I", "SIM���仯��");
				String safeNumber = sp.getString("safenumber", "");
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(safeNumber, null, "�ֻ�����,��sim:" + realSim,
						null, null);
			}
		} else {
			Log.i("I", "��������û�п���");
		}
	}
}
