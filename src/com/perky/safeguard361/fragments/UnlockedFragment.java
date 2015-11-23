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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UnlockedFragment extends Fragment {
	private TextView tv_status;
	private ListView lv_unlock;
	private List<AppInfo> unlockedApps;
	private AppLockDao dao;
	private UnlockAdapter unlockAdapter;

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
		unlockedApps = new ArrayList<AppInfo>();
		dao = new AppLockDao(getActivity());
		List<AppInfo> apps = AppInfoParser.getAppInfos(getActivity());
		for (AppInfo app : apps) {
			if (!dao.find(app.getApkName())) {
				unlockedApps.add(app);
			}
		}
		unlockAdapter = new UnlockAdapter();
		lv_unlock.setAdapter(unlockAdapter);
		super.onStart();
	}

	private class ViewHolder {
		public ImageView iv_app_icon;
		public TextView tv_app_name;
		public ImageView iv_app_lock;
	}

	private class UnlockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_status.setText("Î´¼ÓËø" + unlockedApps.size() + "¸ö");
			return unlockedApps.size();
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
				view = View.inflate(getActivity(), R.layout.item_unlock, null);
				holder = new ViewHolder();
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.iv_app_lock = (ImageView) view
						.findViewById(R.id.iv_app_lock);
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				view.setTag(holder);
			}

			AppInfo app = unlockedApps.get(position);
			holder.iv_app_icon.setImageDrawable(app.getIcon());
			holder.tv_app_name.setText(app.getName());
			holder.iv_app_lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dao.add(unlockedApps.get(position).getApkName());
					unlockedApps.remove(position);
					unlockAdapter.notifyDataSetChanged();
				}
			});
			return view;
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
