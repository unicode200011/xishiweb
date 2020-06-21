package com.stylefeng.guns.core.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * 哈希加密算法
 * 
 *
 */
public class HmacSHA {
	
	private static final String ENCODING = "UTF-8";
	
	/**
	 * 签名算法，HmacSHA1或HmacSHA256
	 */
	public static String  generateSign(String apiSecretKey,String value,String macName) {
		try {
			byte[] data = apiSecretKey.getBytes(ENCODING);
			//根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
			SecretKey secretKey = new SecretKeySpec(data, macName);
			//生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(macName);
			//用给定密钥初始化 Mac 对象
			mac.init(secretKey);

			byte[] text = value.getBytes(ENCODING);
			//完成 Mac 操作
//			return URLEncoder.encode(new String(mac.doFinal(text)),ENCODING);
			return new String(Base64.encodeBase64(mac.doFinal(text)));
		} catch (Exception e) {

		}
		return null;
	}


	/**
	 * @description: 生成短视频签名
	 *
	 * @author: LEIYU
	 */
	public static String generateSignTx(String apiSecretKey,String value,String macName){
		String strSign = "";
		try {
			Mac mac = Mac.getInstance(macName);
			SecretKeySpec secretKey = new SecretKeySpec(apiSecretKey.getBytes(ENCODING), mac.getAlgorithm());
			mac.init(secretKey);

			byte[] hash = mac.doFinal(value.getBytes(ENCODING));
			byte[] sigBuf = byteMerger(hash, value.getBytes(ENCODING));
			strSign = new String(new BASE64Encoder().encode(sigBuf).getBytes());
			strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
		} catch (Exception e) {

		}
		return strSign;
	}

	public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
		byte[] byte3 = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, byte3, 0, byte1.length);
		System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
		return byte3;
	}
}
