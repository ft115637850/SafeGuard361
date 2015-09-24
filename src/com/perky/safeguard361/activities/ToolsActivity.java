package com.perky.safeguard361.activities;

import java.io.IOException;

import com.perky.safeguard361.R;
import com.perky.safeguard361.utils.SmsUtils;
import com.perky.safeguard361.utils.UIUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ToolsActivity extends Activity {
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);
	}

	public void numberAddressQuery(View view) {
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}

	public void backUpSms(View view) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("备份中...");
		pd.setTitle("提醒");
		pd.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SmsUtils.backUpSms(ToolsActivity.this,
							new SmsUtils.SmsProgressCallBack() {

								@Override
								public void updateProgress(int progress) {
									pd.setProgress(progress);
								}

								@Override
								public void setMax(int size) {
									pd.setMax(size);
								}
							});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "备份失败");
				} catch (IllegalStateException e) {
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "备份失败");
				} catch (IOException e) {
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "备份失败");
				} finally {
					pd.dismiss();
				}
			}

		}).start();
	}

	public void restoreSms(View view) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("还原中...");
		pd.setTitle("提醒");
		pd.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SmsUtils.restoreSms(ToolsActivity.this,
							new SmsUtils.SmsProgressCallBack() {

								@Override
								public void updateProgress(int progress) {
									pd.setProgress(progress);
								}

								@Override
								public void setMax(int size) {
									pd.setMax(size);
								}
							});
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pd.dismiss();
				}
			}
		}).start();
	}
}
