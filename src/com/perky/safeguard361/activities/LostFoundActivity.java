package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LostFoundActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_lostfound_safenumber;
	private TextView tv_lostfound_protecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = this.getSharedPreferences("config", MODE_PRIVATE);
		if (hasSetUp()) {
			setContentView(R.layout.activity_lost_found);
			tv_lostfound_safenumber = (TextView) findViewById(R.id.tv_lostfound_safenumber);
			tv_lostfound_safenumber.setText(sp.getString("safenumber", ""));

			tv_lostfound_protecting = (TextView) findViewById(R.id.tv_lostfound_protecting);
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				Drawable drawable = getResources().getDrawable(R.drawable.lock);
				drawable.setBounds(0, 0, 30, 30);
				tv_lostfound_protecting.setCompoundDrawables(null, null, drawable, null);
			} else {
				Drawable drawable = getResources().getDrawable(R.drawable.unlock);
				drawable.setBounds(0, 0, 30, 30);
				tv_lostfound_protecting.setCompoundDrawables(null, null, drawable, null);
			}
		} else {
			Intent intent = new Intent(LostFoundActivity.this,
					Setup1Activity.class);
			startActivity(intent);
			finish();
		}

	}

	private boolean hasSetUp() {
		return sp.getBoolean("setup", false);
	}

	public void onClickWizard(View view) {
		Intent intent = new Intent(LostFoundActivity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}
