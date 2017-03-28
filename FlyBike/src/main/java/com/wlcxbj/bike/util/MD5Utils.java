package com.wlcxbj.bike.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	
	/**
	 * md5加密  sh-1
	 * @param src
	 * @return
	 */
	public static String encode(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			// 加密
			byte[] digest = md.digest(src.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int a = b & 0xff;// 取低八位 取正
				String hexString = Integer.toHexString(a);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
