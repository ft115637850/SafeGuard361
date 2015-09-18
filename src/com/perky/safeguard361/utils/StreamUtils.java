package com.perky.safeguard361.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	public static String readStream(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while (is.read(buffer)!=-1){
			bos.write(buffer);
		}
		is.close();
		return new String(bos.toByteArray());
	}
}
