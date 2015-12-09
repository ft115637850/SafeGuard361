package com.perky.safeguard361.services;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class LocationService extends Service {
	// private LocationManager lm;
	// private MyListener listener;
	private static final String TAG = "LocationService";
	private LocationClient mLocationClient;
	private MyLocationListenner myListener = new MyLocationListenner();

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
		Log.i(TAG, "registerLocationListener");
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		initLocation();
		mLocationClient.start();
		super.onCreate();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		mLocationClient.setLocOption(option);
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
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.i(TAG, "onReceiveLocation");
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
		// �˳�ʱ���ٶ�λ
		mLocationClient.unRegisterLocationListener(myListener);
		mLocationClient.stop();
		mLocationClient = null;
	}
}
