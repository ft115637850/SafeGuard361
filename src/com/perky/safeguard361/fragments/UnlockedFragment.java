package com.perky.safeguard361.fragments;

import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.db.dao.AppLockDao;
import com.perky.safeguard361.domain.AppInfo;
import com.perky.safeguard361.engine.AppInfoParser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UnlockedFragment extends Fragment {
	private TextView tv_status;
	private ListView lv_unlock;
	private List<AppInfo> unlockedApps;
	private AppLockDao dao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_unlock, null);
		tv_status = (TextView) view.findViewById(R.id.tv_status);
		lv_unlock = (ListView) view.findViewById(R.id.lv_unlock);
		return view;
	}

	@Override
	public void onStart() {
		dao = new AppLockDao(getActivity());
		List<AppInfo> apps = AppInfoParser.getAppInfos(getActivity());
		for (AppInfo app : apps) {
			if (!dao.find(app.getApkName())) {
				unlockedApps.add(app);
			}
		}
		lv_unlock.setAdapter(new UnlockAdapter());
		super.onStart();
	}

	private class UnlockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_status.setText("Î´¼ÓËø(" + unlockedApps.size() + ")¸ö");
			return unlockedApps.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
