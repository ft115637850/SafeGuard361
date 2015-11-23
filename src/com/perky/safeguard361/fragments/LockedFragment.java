package com.perky.safeguard361.fragments;

import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LockedFragment extends Fragment {
	private TextView tv_status;
	private ListView lv_locked;
	private List<AppInfo> lockedApps;
	private AppLockDao dao;
	private LockAdapter lockAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locked, null);
		tv_status = (TextView) view.findViewById(R.id.tv_status);
		lv_locked = (ListView) view.findViewById(R.id.lv_locked);
		return view;
	}

	@Override
	public void onStart() {
		lockedApps = new ArrayList<AppInfo>();
		dao = new AppLockDao(getActivity());
		List<AppInfo> apps = AppInfoParser.getAppInfos(getActivity());
		for (AppInfo app : apps) {
			if (dao.find(app.getApkName())) {
				lockedApps.add(app);
			}
		}

		lockAdapter = new LockAdapter();
		lv_locked.setAdapter(lockAdapter);
		super.onStart();
	}

	private class ViewHolder {
		public ImageView iv_app_icon;
		public TextView tv_app_name;
		public ImageView iv_app_lock;
	}

	private class LockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_status.setText("Î´¼ÓËø" + lockedApps.size() + "¸ö");
			return lockedApps.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(getActivity(), R.layout.item_locked, null);
				holder = new ViewHolder();
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.iv_app_lock = (ImageView) view
						.findViewById(R.id.iv_app_lock);
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				view.setTag(holder);
			}

			AppInfo app = lockedApps.get(position);
			holder.iv_app_icon.setImageDrawable(app.getIcon());
			holder.tv_app_name.setText(app.getName());
			holder.iv_app_lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dao.delete(lockedApps.get(position).getApkName());
					lockedApps.remove(position);
					lockAdapter.notifyDataSetChanged();
				}
			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

}
