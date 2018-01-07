package com.boylegu.springboot_vue.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("all")
public class MCDDateUtil {
	public static final String YEAR_MONTH_DAY_H_M_S = "yyyy-MM-dd HH:mm:ss";
	public static final String BUNDLE_VERSION_FORMAT = "yyyy.MM.dd.HHmmss";
	public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
	public static final String MONTH_DAY = "MM-dd";
	public static final String MONTH_DAY_H_M_S = "MM-dd HH:mm:ss";
	public static final String MONTH_DAY_H_M = "MM-dd HH:mm";
	public static final String H_M_S = "HH:mm:ss";

	private static final SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_H_M_S);

	/** yyyy-MM-dd HH:mm:ss 匹配 */
	private static final Pattern patten = Pattern
			.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

	public static String formatDate(Date date) {
		return sdf.format(date);
	}

	public static Date parse(String strDate) {

		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String formatDate(Date date, String formatter) {
		if(null==date){
			return null;
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat(formatter);
		return sdf2.format(date);
	}

	public static Date parse(String strDate, String formatter) {

		try {
			SimpleDateFormat sdf2 = new SimpleDateFormat(formatter);
			return sdf2.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断输入的字符串是否满足时间格式 ： yyyy-MM-dd HH:mm:ss
	 * 
	 * @param patternString
	 *            需要验证的字符串
	 * @return 合法返回 true ; 不合法返回false
	 */
	public static boolean isTimeLegal(String patternString) {
		Matcher matcher = patten.matcher(patternString);
		return matcher.matches();
	}
}
