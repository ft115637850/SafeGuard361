package com.perky.safeguard361.activities;

import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.domain.TrafficInfo;
import com.perky.safeguard361.engine.TrafficInfoParser;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrafficMgrActivity extends Activity {
	private TextView tv_card;
	private TextView tv_wifi;
	private ProgressBar pb_apps;
	private ListView lv_apps;
	private List<TrafficInfo> trafficInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmgr);
		tv_card = (TextView) findViewById(R.id.tv_card);
		tv_wifi = (TextView) findViewById(R.id.tv_wifi);
		pb_apps = (ProgressBar) findViewById(R.id.pb_apps);
		lv_apps = (ListView) findViewById(R.id.lv_apps);

		// rx receive 接收 下载
		// 手机的2g/3g/4g 产生流量
		long mobileRx = TrafficStats.getMobileRxBytes();
		// transfer 发送 上传
		long mobileTx = TrafficStats.getMobileTxBytes();
		tv_card.setText(Formatter.formatFileSize(this, mobileRx + mobileTx));
		// 全部的网络信息 wifi + 手机卡
		long totalRx = TrafficStats.getTotalRxBytes();
		long totalTx = TrafficStats.getTotalTxBytes();
		tv_wifi.setText(Formatter.formatFileSize(this, totalRx + totalTx
				- mobileRx - mobileTx));
		fillData();
	}

	private void fillData() {
		trafficInfos = TrafficInfoParser
				.getTrafficInfos(TrafficMgrActivity.this);
		lv_apps.setAdapter(new AppManagerAdapter());
	}

	private class AppManagerAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return trafficInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_trafficmgr, null);
			}
			return view;
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

}
