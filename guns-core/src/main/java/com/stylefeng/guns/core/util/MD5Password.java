package com.stylefeng.guns.core.util;

import java.security.MessageDigest;

/**
 * @author kimi
 * @dateTime 2012-2-8 下午1:01:29 MD5加密以及加密之后的可逆加密与解密,用于密码加密
 */
public class MD5Password {

	/**
	 * @author kimi
	 * @dateTime 2012-2-8 下午1:01:07
	 * @param inStr
	 * @return 加密结果
	 */
	public static String md5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// 可逆的加密算法
	/**
	 * @author kimi
	 * @dateTime 2012-2-8 下午1:02:01
	 *
	 * @param inStr
	 * @return 可逆
	 */
	public static String KL(String inStr) {
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	// 加密后解密
	/**
	 * @author kimi
	 * @dateTime 2012-2-8 下午1:01:46
	 *
	 * @param inStr
	 * @return 解密
	 */
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	/**
	 * 加密串生成方式 1. 原密码直接MD5得到一个初始签名串； 2. 将初始签名串+"duoji"，再次盐值，得到新的加密原串；
	 * 3.最后将加密原串进行二次MD5，得到最终的密码串
	 *
	 * @author cym
	 * @date 2017年2月22日 下午6:10:44
	 */
	public static String encryptPassword(String password) {
		return md5(md5(password) + "douxi");
	}

	// 测试主函数
	public static void main(String args[]) {
		String s = new String("123456");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + md5(s));
		System.out.println("MD5后再加密：" + KL(md5(s)));
		System.out.println("解密为MD5后的：" + JM(md5(s)));
	}
}
