package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.UIUtils;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class Setup2Activity extends BaseSetupActivity {
	private TextView tv_setup2_bind;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		tv_setup2_bind = (TextView) findViewById(R.id.tv_setup2_bind);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String sim = sp.getString("sim", null);
		boolean isLocked = !TextUtils.isEmpty(sim);
		refreshDisplatTxt(isLocked);
	}

	public void onClickBind(View view) {
		String sim = sp.getString("sim", null);
		boolean isLocked = !TextUtils.isEmpty(sim);
		if (isLocked) {
			Editor edt = sp.edit();
			edt.putString("sim", null);
			edt.commit();
			UIUtils.showToast(this, "解除绑定成功!");
			refreshDisplatTxt(false);
		} else {
			sim = tm.getSimSerialNumber();
			Editor edt = sp.edit();
			edt.putString("sim", sim);
			edt.commit();
			UIUtils.showToast(this, "绑定SIM卡成功!");
			refreshDisplatTxt(true);
		}
	}

	private void refreshDisplatTxt(boolean isLocked) {
		if (isLocked) {
			Drawable drawable = getResources().getDrawable(R.drawable.lock);
			drawable.setBounds(0, 0, 30, 30);
			tv_setup2_bind.setCompoundDrawables(null, null, drawable, null);
			tv_setup2_bind.setText("点击解绑SIM卡");
		} else {
			Drawable drawable = getResources().getDrawable(R.drawable.unlock);
			drawable.setBounds(0, 0, 30, 30);
			tv_setup2_bind.setCompoundDrawables(null, null, drawable, null);
			tv_setup2_bind.setText("点击绑定SIM卡");
		}
	}

	@Override
	public void goNext() {
		String sim = sp.getString("sim", null);
		if (TextUtils.isEmpty(sim)){
			UIUtils.showToast(this, "请先绑定SIM卡");
			return;
		}
		Intent intent = new Intent(Setup2Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void goPre() {
		Intent intent = new Intent(Setup2Activity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}
