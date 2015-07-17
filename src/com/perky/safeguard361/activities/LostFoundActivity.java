package com.perky.safeguard361.activities;

import com.perky.safeguard361.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lost_found_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.item_change_name==item.getItemId()){
			AlertDialog.Builder blder = new Builder(this);
			blder.setTitle("请输入功能名称");
			final EditText et = new EditText(this);
			blder.setView(et);
			blder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String newName = et.getText().toString().trim();
					Editor editor = sp.edit();
					editor.putString("newname", newName);
					editor.commit();
				}
			});
			blder.show();
		}
		return super.onOptionsItemSelected(item);
	}
}
