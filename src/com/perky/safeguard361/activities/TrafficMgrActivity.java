package com.perky.safeguard361.activities;

import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.domain.TrafficInfo;
import com.perky.safeguard361.engine.TrafficInfoParser;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pb_apps.setVisibility(View.INVISIBLE);
			lv_apps.setVisibility(View.VISIBLE);
			lv_apps.setAdapter(new AppManagerAdapter());
		};
	};

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
		tv_card.setText("手机卡："
				+ Formatter.formatFileSize(this, mobileRx + mobileTx));
		// 全部的网络信息 wifi + 手机卡
		long totalRx = TrafficStats.getTotalRxBytes();
		long totalTx = TrafficStats.getTotalTxBytes();
		tv_wifi.setText("Wifi："
				+ Formatter.formatFileSize(this, totalRx + totalTx - mobileRx
						- mobileTx));
		fillData();
	}

	private void fillData() {
		new Thread() {
			public void run() {
				trafficInfos = TrafficInfoParser
						.getTrafficInfos(TrafficMgrActivity.this);
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class ViewHolder {
		public ImageView iv_app_icon;
		public TextView tv_app_name;
		public TextView tv_snd;
		public TextView tv_rcv;
		public TextView tv_total;
	}

	private class AppManagerAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return trafficInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_trafficmgr, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				viewHolder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				viewHolder.tv_snd = (TextView) view.findViewById(R.id.tv_snd);
				viewHolder.tv_rcv = (TextView) view.findViewById(R.id.tv_rcv);
				viewHolder.tv_total = (TextView) view
						.findViewById(R.id.tv_total);
				view.setTag(viewHolder);
			}

			TrafficInfo trafficInfo = trafficInfos.get(position);
			viewHolder.iv_app_icon.setImageDrawable(trafficInfo.getIcon());
			viewHolder.tv_app_name.setText(trafficInfo.getName());
			viewHolder.tv_rcv.setText("下载："+Formatter.formatFileSize(
					getApplicationContext(), trafficInfo.getTcp_rcv()));
			viewHolder.tv_snd.setText("上传："+Formatter.formatFileSize(
					getApplicationContext(), trafficInfo.getTcp_snd()));
			viewHolder.tv_total.setText(Formatter.formatFileSize(
					getApplicationContext(), trafficInfo.getTcp_rcv()
							+ trafficInfo.getTcp_snd()));
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
