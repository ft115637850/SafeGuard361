package com.perky.safeguard361.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {
	public static String checkVirus(String md5) {
		String desc = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.perky.safeguard361/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}
}
