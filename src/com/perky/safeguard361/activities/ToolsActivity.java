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
import android.widget.Toast;

public class ToolsActivity extends Activity {
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);
		pd = new ProgressDialog(this);
	}

	public void numberAddressQuery(View view) {
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}

	public void backUpSms(View view) {
		pd.setMessage("备份中...");
		pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SmsUtils.backUpSms(ToolsActivity.this,
							new SmsUtils.BackupCallBack() {

								@Override
								public void updateProgress(int progress) {
									pd.setProgress(progress);
								}

								@Override
								public void setMax(int size) {
									pd.setMax(size);
								}
							});
					pd.dismiss();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "备份失败");
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "备份失败");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					UIUtils.showToast(ToolsActivity.this, "备份失败");
				}
			}

		}).start();
	}

	public void restoreSms(View view) {

	}
}
