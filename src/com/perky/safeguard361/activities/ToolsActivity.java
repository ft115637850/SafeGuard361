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
		pd.setMessage("������...");
		pd.setTitle("����");
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
					UIUtils.showToast(ToolsActivity.this, "����ʧ��");
				} catch (IllegalStateException e) {
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "����ʧ��");
				} catch (IOException e) {
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "����ʧ��");
				} finally {
					pd.dismiss();
				}
			}

		}).start();
	}

	public void restoreSms(View view) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("��ԭ��...");
		pd.setTitle("����");
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
