package com.perky.safeguard361.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class SmsUtils {
	public interface SmsProgressCallBack {
		void setMax(int size);

		void updateProgress(int progress);
	}

	public static void backUpSms(final Activity ctx,
			SmsProgressCallBack progressDlg) throws IllegalArgumentException,
			IllegalStateException, IOException {
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
			int progress = 0;
			while (crsr.moveToNext()) {
				serializer.startTag(null, "sms");
				serializer.startTag(null, "body");
				try {
					String txt = AES.encrypt(crsr.getString(1));
					serializer.text(txt);
				} catch (Exception e) {
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
				progressDlg.updateProgress(++progress);
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

	public static void restoreSms(final Activity ctx,
			SmsProgressCallBack progressDlg) throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(),
				"backup.xml");
		if (file.exists()) {
			FileInputStream is = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "utf-8");

			ContentResolver resolver = ctx.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			ContentValues sms = new ContentValues();
			int progress = 0;
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("smss")) {
						int size = Integer.parseInt(parser.getAttributeValue(
								null, "size"));
						progressDlg.setMax(size);
					} else if (parser.getName().equals("sms")) {
						Log.i("SmsUtils", " sms start");
					} else if (parser.getName().equals("body")) {
						parser.next();
						String txt = parser.getText();
						Log.i("SmsUtils", " body:" + txt);
						String body = AES.decrypt(txt);
						Log.i("SmsUtils", " body:" + body);
						sms.put("body", body);
					} else if (parser.getName().equals("address")) {
						parser.next();
						Log.i("SmsUtils", " address:" + parser.getText());
						sms.put("address", parser.getText());
					} else if (parser.getName().equals("type")) {
						parser.next();
						Log.i("SmsUtils", " type:" + parser.getText());
						sms.put("type", parser.getText());
					} else if (parser.getName().equals("date")) {
						parser.next();
						Log.i("SmsUtils", " date:" + parser.getText());
						sms.put("date", parser.getText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("sms")) {
						resolver.insert(uri, sms);
						progressDlg.updateProgress(++progress);
						sms.clear();
						Log.i("SmsUtils", " sms end");
					}
					break;
				}

				eventType = parser.next();
			}
		}
	}
}
