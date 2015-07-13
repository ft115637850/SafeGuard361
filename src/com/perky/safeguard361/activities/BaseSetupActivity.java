package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;

public class BaseSetupActivity extends Activity {
	protected SharedPreferences sp;
	protected GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = this.getSharedPreferences("config", MODE_PRIVATE);
		gestureDetector = new GestureDetector(this,
				new SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (Math.abs(velocityX) < 200) {
							return true;
						}

						if ((e1.getRawX() - e2.getRawX()) > 100) {
							goNext();
							overridePendingTransition(R.anim.next_in, R.anim.next_out);
							return true;
						}
						
						if ((e2.getRawX() - e1.getRawX()) > 100) {
							goPre();
							overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	public void onClickNext(View view) {
		goNext();
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}

	public void onClickPre(View view) {
		goPre();
		overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
	}

	public void goNext() {

	}

	public void goPre() {

	}
}
