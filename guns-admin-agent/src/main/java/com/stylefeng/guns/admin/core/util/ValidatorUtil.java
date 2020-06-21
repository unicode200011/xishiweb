package com.stylefeng.guns.admin.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : lx
 * @Description : 校验器：利用正则表达式校验邮箱、手机号等
 * @Params:
 * @Date : 2018/11/22
 */
public class ValidatorUtil {

	/**
	 * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
	 */
	private static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

	/**
	 * 正则表达式:验证密码(不包含特殊字符)
	 */
	private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

	/**
	 * 说明：移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186
	 * 电信：133、153、180、189
	 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	 * 验证号码 手机号 固话均可
	 * 正则表达式:验证手机号
	 */
	private static final String REGEX_MOBILE = "((^(13|15|18|17)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
	private static final String REGEX_PHONE = "^(1[3|4|5|6|7|8][0-9]{9})$|^(400\\d{7})$|^(0\\d{2}\\d{9})$";

	/**
	 * 正则表达式:验证邮箱
	 */
	private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
	 */
	private static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";

	/**
	 * 正则表达式:验证身份证
	 */
	private static final String REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

	/**
	 * 正则表达式:验证URL
	 */
	private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * 正则表达式:验证IP地址
	 */
	private static final String REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

	/**
	 * 验证手机验证码
	 */
	private static final String REGEX_PHONE_CODE = "^[0-9]{6}$";
	/**
	 * 带区号的手机号正则
	 */
	private static final String REGEX_PHONE_CITY = "^0\\d{2,3}-?\\d{7,8}$";
	/**
	 * 没有带区号的手机号正则
	 */
//	private static final String REGEX_PHONE = "^[1-9]{1}[0-9]{5,8}$";
//	private static final String REGEX_PHONE = "^[1-9]\\d{6,7}$";

	private static final String REG_PHONE_400 = "^400[0-9]{7}$";
	/**
	 * 正浮点数正则
	 */
	private static final String FLOAT_NUMBER_REGEX = "([0-9]\\d*\\.?\\d*)|((-)?[0-9]\\d*\\.?\\d*)";
	/**
	 * 香港身份证正则
	 */
	public static final String HONGKON_ID_CARD_REGEX = "^((\\s?[A-Za-z])|([A-Za-z]{2}))\\d{6}(\\([0−9aA]\\)|[0-9aA])$";
	/**
	 * 台湾身份证正则
	 */
	public static final String TAIWAN_ID_CARD_REGEX = "^[a-zA-Z][0-9]{9}$";
	/**
	 * 澳门身份证正则
	 */
	public static final String AOMEN_ID_CARD_REGEX = "^[1|5|7][0-9]{6}\\([0-9Aa]\\)";

	/**
	 * 两位小数正数字
	 */
	public static final String TWO_POINT_NUMBER = "^[0-9]+([.]{1}[0-9]{1,2})?$";
	/**
	 * 两位小数尺寸
	 */
	public static final String TWO_POINT_NUMBER_SIZE = "^([0-9]+([.]{1}[0-9]{1,2})?)\\*([0-9]+([.]{1}[0-9]{1,2})?)$";
	/**
	 * 数字
	 */
	public static final String NUMBER_REGEX = "^[0-9]*$";
	/**
	 * 校验两位小数
	 */
	public static boolean isTwoPointNumber(String number) {
		return Pattern.matches(TWO_POINT_NUMBER, number);
	}
	/**
	 * 校验两位小数尺寸
	 */
	public static boolean isTwoPointNumberSize(String number) {
		return Pattern.matches(TWO_POINT_NUMBER_SIZE, number);
	}

	/**
	 * 校验数字
	 */
	public static boolean isNumber(String number) {
		return Pattern.matches(NUMBER_REGEX, number);
	}
	/**
	 * 校验用户名
	 *
	 * @param username
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUserName(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	/**
	 * 校验密码
	 *
	 * @param password
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isPassword(String password) {
		return Pattern.matches(REGEX_PASSWORD, password);
	}

	/**
	 * 校验手机号
	 *
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验邮箱
	 *
	 * @param email
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	/**
	 * 校验汉字
	 *
	 * @param chinese
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isChinese(String chinese) {
		return Pattern.matches(REGEX_CHINESE, chinese);
	}

	/**
	 * 校验身份证
	 *
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	/**
	 * 校验URL
	 *
	 * @param url
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUrl(String url) {
		return Pattern.matches(REGEX_URL, url);
	}

	/**
	 * 校验IP地址
	 *
	 * @param ipAddress
	 * @return
	 */
	public static boolean isIPAddress(String ipAddress) {
		return Pattern.matches(REGEX_IP_ADDR, ipAddress);
	}

	/**
	 * 校验手机验证码
	 */
	public static boolean isPhoneCode(String phoneCode) {
		return Pattern.matches(REGEX_PHONE_CODE, phoneCode);
	}

	/**
	 * 验证手机号
	 *
	 * @param str 手机号
	 * @return 验证成功返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1, p2;
		Matcher m;
		boolean b;
		p1 = Pattern.compile(REGEX_PHONE_CITY);  // 验证带区号的
		p2 = Pattern.compile(REGEX_PHONE);         // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	public static boolean isPhone400(String phoneCode) {
		return Pattern.matches(REG_PHONE_400, phoneCode);
	}
	public static boolean isTkPhone(String phone) {
		return Pattern.matches(REGEX_PHONE, phone);
	}

	/**
	 * 验证是否是手机号或者是座机，一个匹配即返回true
	 * @param phone 传入的手机号
	 * @return 匹配成功返回true
	 */
	public static boolean validatorMobileOrPhone(String phone) {
		boolean isMobile = isMobile(phone);
		return isMobile || isPhone(phone) || isPhone400(phone);
	}

	/**
	 * 验证是否是正浮点数
	 * @param str 验证的手机号
	 * @return
	 */
	public static boolean isFloatNumber(String str){
		return Pattern.matches(FLOAT_NUMBER_REGEX, str);
	}

	/**
	 * @Author : lx
	 * @Description : 是否是港澳台身份证
	 * @Params:
	 * @Date : 2019/3/12
	 */
	public static boolean isOtherIdCard(String str){
		boolean isHongkong = Pattern.matches(HONGKON_ID_CARD_REGEX, str);
		boolean isAoMen = Pattern.matches(AOMEN_ID_CARD_REGEX, str);
		boolean isTaiWang = Pattern.matches(TAIWAN_ID_CARD_REGEX, str);
		return isHongkong || isAoMen || isTaiWang;
	}

	/**
	 * @Author : lx
	 * @Description : 所有地区身份证
	 * @Params:
	 * @Date : 2019/3/12
	 */
	public static boolean isAllIdCard(String str){
		boolean idCard = isIDCard(str);
		return idCard || isOtherIdCard(str);
	}
}
