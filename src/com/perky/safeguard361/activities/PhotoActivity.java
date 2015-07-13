package com.perky.safeguard361.activities;

import java.io.File;
import java.io.FileOutputStream;
import com.perky.safeguard361.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.widget.FrameLayout;

public class PhotoActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		if (!checkCameraHardware(this)) {
			// �����Ӳ�������ڣ��ر������
			finish();
		}
		mCamera = getCameraInstance();
		if (mCamera == null) {
			finish();
		}
		// ��Ĭ�ϵ����������
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mCamera.takePicture(null, null, new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						try {
							FileOutputStream fos = new FileOutputStream(
									new File(Environment
											.getExternalStorageDirectory()+"/DCIM/Camera/",
											"info.jpg"));
							fos.write(data);
							fos.close();
							// mCamera.startPreview();
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			};
		}.start();
	}

	/** ���������豸�Ƿ���� */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/** ��ȫ�ķ��� ȥ���������. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();// ��ȡ�����ʵ��
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onDestroy() {
		mCamera.release();
		mCamera = null;
		super.onDestroy();
	}
}
