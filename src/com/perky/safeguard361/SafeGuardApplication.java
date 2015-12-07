package com.perky.safeguard361;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import android.app.Application;
import android.os.Build;
import android.os.Environment;

public class SafeGuardApplication extends Application {
	/**
	 * Ӧ�ó�������ڵ�һ�α�������ʱ����õķ��������κ��������󴴽�֮ǰִ�е��߼�
	 */
	@Override
	public void onCreate() {
		// ����δ�����쳣�Ĵ�����
		Thread.currentThread().setUncaughtExceptionHandler(
				new MyExecptionHandler());
		super.onCreate();
	}

	private class MyExecptionHandler implements UncaughtExceptionHandler {
		// ���̳߳�����δ������쳣ִ�еķ�����
		// ������ֹjava������˳���ֻ����jvm�˳�֮ǰ�� ����һ��ʱ�䣬 ��һ������
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				System.out.println("�������쳣");
				Field[] fileds = Build.class.getDeclaredFields();
				for (Field filed : fileds) {
					System.out
							.println(filed.getName() + "--" + filed.get(null));
					sw.write(filed.getName() + "--" + filed.get(null) + "\n");
				}
				ex.printStackTrace(pw);
				File file = new File(Environment.getExternalStorageDirectory(),
						"log.txt");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sw.toString().getBytes());
				fos.close();
				pw.close();
				sw.close();
				// �����糬��
				android.os.Process.killProcess(android.os.Process.myPid());
				// ԭ�ظ���
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
