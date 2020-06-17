package com.im.phone.server.encryption;

import java.security.MessageDigest;

/**
 * Created by shiql on 2018/1/16.
 */
public class MD5Util {
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	
	// -------------------------微信调用---------------------------------------------------------------
	private static final char hexDigits1[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
	'f' };
	
	/*
	* 先转为utf-8
	*/
	public static String MD5Encoding(String s) {
		byte[] btInput = null;
		try {
			btInput = s.getBytes("UTF-8");
		} catch (Exception e) {
		}
		return MD5(btInput, 32);
	}
	
	public static String MD5(String s) {
		byte[] btInput = s.getBytes();
		return MD5(btInput, 32);
	}
	
	public static String MD5_16(String str) {
		byte[] btInput = str.getBytes();
		return MD5(btInput, 16);
	}
	
	private static String MD5(byte[] btInput, int length) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// MessageDigest mdInst = MessageDigest.getInstance("SHA-1");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (byte byte0 : md) {
				str[k++] = hexDigits1[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits1[byte0 & 0xf];
			}
			String result = new String(str);
			return length == 16 ? result.substring(8, 24) : result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static void main(String[] args) {
		System.out.println(MD5Encode("admin", "UTF-8"));
	}

}
