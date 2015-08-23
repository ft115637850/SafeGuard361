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
	 * �޸ĺ��������������ģʽ
	 * 
	 * @param number
	 *            ����
	 * @param newmode
	 *            �µ�����ģʽ
	 * @return �Ƿ��޸ĳɹ�
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
	 * ����һ����������������ģʽ
	 * 
	 * @param number
	 *            Ҫ��ѯ�ĺ���������
	 * @return 0���Ǻ��������벻���� 1ȫ������ 2�������� 3�绰����
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
	 * ��ҳ��ѯ���ݿ�ļ�¼
	 * @param pagenumber �ڼ�ҳ��ҳ�� �ӵ�0ҳ��ʼ
	 * @param pagesize   ÿһ��ҳ��Ĵ�С
	 */
	public List<BlackNumberInfo> findPart(int pagenumber,int pagesize){
		// �õ��ɶ������ݿ�
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
	 * ������������
	 * @param startIndex ���ĸ�λ�ÿ�ʼ��������
	 * @param maxCount   �����ؼ�������
	 */
	public List<BlackNumberInfo> findPart2(int startIndex,int maxCount){
		// �õ��ɶ������ݿ�
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
	 * ��ȡ���ݿ������Ŀ����
	 * @param pagenumber �ڼ�ҳ��ҳ�� �ӵ�0ҳ��ʼ
	 * @param pagesize   ÿһ��ҳ��Ĵ�С
	 */
	public int getTotalNumber(){
		// �õ��ɶ������ݿ�
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blackinfo",null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
	}
}
