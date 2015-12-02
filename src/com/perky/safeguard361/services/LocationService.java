package com.perky.safeguard361.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import android.location.LocationListener;

public class LocationService extends Service {
	private LocationManager lm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		Criteria crt = new Criteria();
		crt.setAccuracy(Criteria.ACCURACY_FINE);
		crt.setCostAllowed(true);
		String provider = lm.getBestProvider(crt, true);
		Log.i("provider", "?" + provider);
		listener = new MyListener();
		if (!TextUtils.isEmpty(provider)) {
			lm.requestLocationUpdates(provider, 0, 0, listener);
		}
		else{
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
		}
		super.onCreate();
	}

	private class MyListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			StringBuilder sb = new StringBuilder();
			sb.append("getAccuracy:" + location.getAccuracy() + "\n");
			sb.append("Altitude:" + location.getAltitude() + "\n");
			sb.append("Latitude:" + location.getLatitude() + "\n");
			sb.append("Longitude:" + location.getLongitude() + "\n");
			SharedPreferences sp = getSharedPreferences("config",
					Context.MODE_PRIVATE);
			String safenumber = sp.getString("safenumber", "");
			SmsManager.getDefault().sendTextMessage(safenumber, null,
					sb.toString(), null, null);
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}
}
