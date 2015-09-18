package com.perky.safeguard361.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsUtils {
	public interface BackupCallBack{
		void setMax(int size);
		void updateProgress(int progress);
	}
	
	public static void backUpSms(final Activity ctx, BackupCallBack progressDlg)
			throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer serializer = Xml.newSerializer();
		File sdDir = Environment.getExternalStorageDirectory();
		long freeSize = sdDir.getFreeSpace();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& freeSize > 1024l * 1024l) {
			File file = new File(Environment.getExternalStorageDirectory(),
					"backup.xml");
			FileOutputStream os = new FileOutputStream(file);
			serializer.setOutput(os, "utf-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "smss");
			ContentResolver resolver = ctx.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			Cursor crsr = resolver.query(uri, new String[] { "address", "body",
					"type", "date" }, null, null, null);
			int size = crsr.getCount();
			serializer.attribute(null, "size", String.valueOf(size));
			progressDlg.setMax(size);
			int progress=0;
			while (crsr.moveToNext()) {
				serializer.startTag(null, "sms");
				serializer.startTag(null, "body");
				String txt;
				try {
					txt = Crypto.encrypt("123", crsr.getString(1));
					serializer.text(txt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					serializer.text("短信读取失败");
				}
				serializer.endTag(null, "body");
				serializer.startTag(null, "address");
				serializer.text(crsr.getString(0));
				serializer.endTag(null, "address");
				serializer.startTag(null, "type");
				serializer.text(crsr.getString(2));
				serializer.endTag(null, "type");
				serializer.startTag(null, "date");
				serializer.text(crsr.getString(3));
				serializer.endTag(null, "date");
				serializer.endTag(null, "sms");
				progressDlg.updateProgress(progress);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			crsr.close();
			serializer.endTag(null, "smss");
			serializer.endDocument();
			os.flush();
			os.close();
			return;
		} else {
			throw new IllegalStateException("sd卡不存在或者空间不足");
		}

	}
}
