package com.perky.safeguard361.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.perky.safeguard361.db.BlackNumberDbHelper;
import com.perky.safeguard361.domain.BlackNumberInfo;

public class BlackNumberDao {
	private BlackNumberDbHelper helper;

	public BlackNumberDao(Context ctx) {
		helper = new BlackNumberDbHelper(ctx);
	}

	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		long rowid = db.insert("blackinfo", null, values);
		if (rowid == -1) {
			return false;
		} else {
			return true;
		}
	}

	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowNumber = db.delete("blackinfo", "number=?",
				new String[] { number });
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 修改黑名单号码的拦截模式
	 * 
	 * @param number
	 *            号码
	 * @param newmode
	 *            新的拦截模式
	 * @return 是否修改成功
	 */
	public boolean changeBlockMode(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		int rowNumber = db.update("blackinfo", values, "number=?",
				new String[] { number });
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 返回一个黑名单号码拦截模式
	 * 
	 * @param number
	 *            要查询的黑名单号码
	 * @return 0不是黑名单号码不拦截 1全部拦截 2短信拦截 3电话拦截
	 */
	public String findBlockMode(String number) {
		String mode = "0";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor crsr = db.query("blackinfo", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		if (crsr.moveToNext())
			mode=crsr.getString(0);
		crsr.close();
		db.close();
		return mode;
	}
	
	public List<BlackNumberInfo> findAll(){
		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor crsr = db.query("blackinfo", new String[] { "number", "mode" },
				null, null, null, null, null);
		while (crsr.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			info.setNumber(crsr.getString(0));
			info.setMode(crsr.getString(1));
			result.add(info);
		}
		crsr.close();
		db.close();
		return result;
	}
	
	/**
	 * 分页查询数据库的记录
	 * @param pagenumber 第几页，页码 从第0页开始
	 * @param pagesize   每一个页面的大小
	 */
	public List<BlackNumberInfo> findPart(int pagenumber,int pagesize){
		// 得到可读的数据库
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blackinfo limit ? offset ?", new String[]{String.valueOf(pagesize),
				String.valueOf(pagesize*pagenumber)
		});
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setMode(mode);
			info.setNumber(number);
			blackNumberInfos.add(info);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(30);
		return blackNumberInfos;
	}
	
	/**
	 * 分批加载数据
	 * @param startIndex 从哪个位置开始加载数据
	 * @param maxCount   最多加载几条数据
	 */
	public List<BlackNumberInfo> findPart2(int startIndex,int maxCount){
		// 得到可读的数据库
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blackinfo order by _id desc limit ? offset ?", new String[]{String.valueOf(maxCount),
				String.valueOf(startIndex)
		});
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setMode(mode);
			info.setNumber(number);
			blackNumberInfos.add(info);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(30);
		return blackNumberInfos;
	}
	
	/**
	 * 获取数据库的总条目个数
	 * @param pagenumber 第几页，页码 从第0页开始
	 * @param pagesize   每一个页面的大小
	 */
	public int getTotalNumber(){
		// 得到可读的数据库
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blackinfo",null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
	}
}
