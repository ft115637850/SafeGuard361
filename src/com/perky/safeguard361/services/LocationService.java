package com.perky.safeguard361.services;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {
	// private LocationManager lm;
	// private MyListener listener;
	private LocationClient mLocationClient;
	private MyLocationListenner myListener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		/*
		 * lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		 * Criteria crt = new Criteria();
		 * crt.setAccuracy(Criteria.ACCURACY_FINE); crt.setCostAllowed(true);
		 * String provider = lm.getBestProvider(crt, true); Log.i("provider",
		 * "?" + provider); listener = new MyListener(); if
		 * (!TextUtils.isEmpty(provider)) { lm.requestLocationUpdates(provider,
		 * 0, 0, listener); } else{
		 * lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
		 * listener); }
		 */
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		super.onCreate();
	}

	/*
	 * private class MyListener implements LocationListener {
	 * 
	 * @Override public void onLocationChanged(Location location) {
	 * StringBuilder sb = new StringBuilder(); sb.append("getAccuracy:" +
	 * location.getAccuracy() + "\n"); sb.append("Altitude:" +
	 * location.getAltitude() + "\n"); sb.append("Latitude:" +
	 * location.getLatitude() + "\n"); sb.append("Longitude:" +
	 * location.getLongitude() + "\n"); SharedPreferences sp =
	 * getSharedPreferences("config", Context.MODE_PRIVATE); String safenumber =
	 * sp.getString("safenumber", "");
	 * SmsManager.getDefault().sendTextMessage(safenumber, null, sb.toString(),
	 * null, null); stopSelf(); }
	 * 
	 * @Override public void onStatusChanged(String provider, int status, Bundle
	 * extras) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onProviderEnabled(String provider) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onProviderDisabled(String provider) { // TODO
	 * Auto-generated method stub
	 * 
	 * } }
	 */

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			StringBuilder sb = new StringBuilder();
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

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// lm.removeUpdates(listener);
		// listener = null;
		// 退出时销毁定位
		mLocationClient.unRegisterLocationListener(myListener);
		mLocationClient.stop();
		mLocationClient = null;
	}
}
