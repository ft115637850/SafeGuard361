package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.UIUtils;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
	EditText et_setup3_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);
		et_setup3_phone.setText(sp.getString("safenumber", ""));
	}

	public void onClickSelectContact(View view) {

		Intent intent = new Intent(Setup3Activity.this,
				SelectContactsActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data != null) {
			String phone = data.getStringExtra("phone");
			et_setup3_phone.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void goNext() {
		String safeNumber = et_setup3_phone.getText().toString().trim();
		if (TextUtils.isEmpty(safeNumber)) {
			UIUtils.showToast(this, "«Î…Ë÷√∞≤»´∫≈¬Î");
			return;
		} else {
			Editor ed = sp.edit();
			ed.putString("safenumber", safeNumber);
			ed.commit();
		}
		Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void goPre() {
		Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();
	}
}
