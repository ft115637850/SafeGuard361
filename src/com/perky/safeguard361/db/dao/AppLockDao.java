package com.perky.safeguard361.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.perky.safeguard361.db.AppLockDbOpenHelper;

public class AppLockDao {
	private AppLockDbOpenHelper dbHelper;
	private Context ctx;

	public AppLockDao(Context context) {
		ctx = context;
		dbHelper = new AppLockDbOpenHelper(ctx);
	}

	public boolean find(String packName) {
		boolean result = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor csr = db.query("info", null, "packname=?",
				new String[] { packName }, null, null, null);
		if (csr.moveToNext()) {
			result = true;
		}
		csr.close();
		db.close();
		return result;
	}

	public List<String> findAll() {
		List<String> result = new ArrayList<String>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor csr = db.query("info", new String[] { "packname" }, null, null,
				null, null, null);
		if (csr.moveToNext()) {
			result.add(csr.getString(0));
		}
		csr.close();
		db.close();
		return result;
	}

	public void delete(String packName) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("info", "packname=?", new String[] { packName });
		db.close();
		ctx.getContentResolver().notifyChange(
				Uri.parse("content://com.perky.safeguard361.applock.update"),
				null);
	}

	public void add(String packName) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packName);
		db.insert("info", null, values);
		db.close();
		ctx.getContentResolver().notifyChange(
				Uri.parse("content://com.perky.safeguard361.applock.update"),
				null);
	}
}
