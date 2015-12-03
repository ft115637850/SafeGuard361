package com.perky.safeguard361.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String md5Encode(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] result = md.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b & 0xff;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					sb.append("0" + hex);
				} else {
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String getFileMD5(String path) {
		String md5Result;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);

			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				md.update(buffer, 0, len);
			}
			byte[] result = md.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b & 0xff;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					sb.append("0" + hex);
				} else {
					sb.append(hex);
				}
			}
			md5Result = sb.toString();
		} catch (Exception ex) {
			md5Result = null;
		}
		return md5Result;

	}
}
