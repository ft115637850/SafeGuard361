package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TrafficMgrActivity extends Activity {
	private TextView tv_card;
	private TextView tv_wifi;
	private ProgressBar pb_apps;
	private ListView lv_apps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmgr);
		tv_card = (TextView) findViewById(R.id.tv_card);
		tv_wifi = (TextView) findViewById(R.id.tv_wifi);
		pb_apps = (ProgressBar) findViewById(R.id.pb_apps);
		lv_apps = (ListView) findViewById(R.id.lv_apps);

		fillData();
	}

	private void fillData() {
		lv_apps.setAdapter(new AppManagerAdapter());
	}

	private class AppManagerAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return null;
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
