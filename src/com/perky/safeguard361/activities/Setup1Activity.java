package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void goNext() {
		Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();
	}
}
