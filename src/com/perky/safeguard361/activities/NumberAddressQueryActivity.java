package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;
import com.perky.safeguard361.db.dao.NumberAddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	private EditText et_phone_number;
	private TextView tv_address_info;
	private Vibrator  vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address);
		tv_address_info = (TextView) findViewById(R.id.tv_address_info);
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		vibrator =  (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}
	
	public void query(View view){
		String phone = et_phone_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
	        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        et_phone_number.startAnimation(shake);
	        vibrator.vibrate(new long[]{200,100,300}, -1);
			Toast.makeText(this, "号码不能为空", 0).show();
			return;
		}
		String location = NumberAddressDao.getLocation(phone);
		tv_address_info.setText("归属地："+location);
	}
}
