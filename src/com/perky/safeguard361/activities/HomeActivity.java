package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.MD5Utils;
import com.perky.safeguard361.utils.UIUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private GridView gv_home;
	private String[] funcLStrings;
	private int[] icons = { R.drawable.phone_selector, R.drawable.cmm_selector,
			R.drawable.software_selector, R.drawable.process_selector,
			R.drawable.traffic_selector, R.drawable.virus_selector,
			R.drawable.cache_selector, R.drawable.adv_selector,
			R.drawable.setting_selector };
	private SharedPreferences sp;
	private EditText pwdEt;
	private EditText cfmEt;
	private Button cfmBtn;
	private Button cancelBtn;
	private AlertDialog dlg;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		funcLStrings = new String[] { this.getString(R.string.phone_guard),
				this.getString(R.string.communication_guard),
				this.getString(R.string.software_mgr),
				this.getString(R.string.process_mgr),
				this.getString(R.string.traffic_sum),
				this.getString(R.string.anti_virus),
				this.getString(R.string.cache_clear),
				this.getString(R.string.advanced_tool),
				this.getString(R.string.setting_center) };
		sp = getSharedPreferences("config", MODE_PRIVATE);

		gv_home = (GridView) findViewById(R.id.gv_home);

		gv_home.setOnItemClickListener(new GvClickListener());
	}

	@Override
	protected void onStart() {
		gv_home.setAdapter(new GvAdapter());
		super.onStart();
	}

	private class GvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return funcLStrings.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.item_home_gv, null);
			} else {
				view = convertView;
			}
			ImageView iv_homeitem_icon = (ImageView) view
					.findViewById(R.id.iv_homeitem_icon);
			iv_homeitem_icon.setImageResource(icons[position]);
			TextView tv_homeitem_name = (TextView) view
					.findViewById(R.id.tv_homeitem_name);
			tv_homeitem_name.setText(funcLStrings[position]);
			if (position == 0) {
				String newName = getSharedPreferences("config", MODE_PRIVATE)
						.getString("newname", "");
				if (!TextUtils.isEmpty(newName)) {
					tv_homeitem_name.setText(newName);
				}
			}
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

	private class GvClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent;
			switch (position) {
			case 0:
				if (isPwdSet()) {
					showEnterPwdDlg();
				} else {
					showSetPwdDlg();
				}
				break;
			case 1:
				intent = new Intent(HomeActivity.this,
						CallSmsSafeActivity.class);
				startActivity(intent);
				break;
			case 8:
				intent = new Intent(HomeActivity.this,
						SettingCenterActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}

		}

		private void showSetPwdDlg() {
			AlertDialog.Builder dlgBlder = new Builder(HomeActivity.this);
			View view = View.inflate(HomeActivity.this,
					R.layout.dialog_setup_pwd, null);

			pwdEt = (EditText) view.findViewById(R.id.et_pwd);
			cfmEt = (EditText) view.findViewById(R.id.et_confirm);
			cfmBtn = (Button) view.findViewById(R.id.btn_confirm);
			cancelBtn = (Button) view.findViewById(R.id.btn_cancel);

			cfmBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String pwd = pwdEt.getText().toString().trim();
					String pwdCfm = cfmEt.getText().toString().trim();
					if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdCfm)) {
						UIUtils.showToast(HomeActivity.this, getResources()
								.getString(R.string.err_no_pwd));
						return;
					}

					if (!pwd.equals(pwdCfm)) {
						UIUtils.showToast(HomeActivity.this, getResources()
								.getString(R.string.err_pwd));
						return;
					}

					Editor ed = sp.edit();
					ed.putString("pwd", MD5Utils.md5Encode(pwd));
					ed.commit();
					dlg.dismiss();
				}
			});
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
			});
			dlgBlder.setView(view);
			dlg = dlgBlder.show();
		}

		private void showEnterPwdDlg() {
			AlertDialog.Builder dlgBlder = new Builder(HomeActivity.this);
			View view = View.inflate(HomeActivity.this,
					R.layout.dialog_enter_pwd, null);

			pwdEt = (EditText) view.findViewById(R.id.et_pwd);
			cfmBtn = (Button) view.findViewById(R.id.btn_confirm);
			cancelBtn = (Button) view.findViewById(R.id.btn_cancel);

			cfmBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String pwd = pwdEt.getText().toString().trim();
					if (TextUtils.isEmpty(pwd)) {
						UIUtils.showToast(HomeActivity.this, getResources()
								.getString(R.string.err_no_pwd));
						return;
					}

					String savedPwd = sp.getString("pwd", "");
					if (savedPwd.equals(MD5Utils.md5Encode(pwd))) {
						Intent intent = new Intent(HomeActivity.this,
								LostFoundActivity.class);
						startActivity(intent);
						dlg.dismiss();
					} else {
						UIUtils.showToast(HomeActivity.this, getResources()
								.getString(R.string.err_pwd));
						return;
					}
				}
			});
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
			});
			dlgBlder.setView(view);
			dlg = dlgBlder.show();
		}

		private boolean isPwdSet() {
			String pwd = sp.getString("pwd", null);
			if (TextUtils.isEmpty(pwd)) {
				return false;
			} else {
				return true;
			}

		}
	}

}
