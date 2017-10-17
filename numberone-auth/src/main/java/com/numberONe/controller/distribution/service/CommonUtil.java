package com.numberONe.controller.distribution.service;

public class CommonUtil {
	
	public final static int STATUS_200 = 200; //正确码
	public final static int STATUS_400 = 400; //错误码

	/**
	 * 判断变量是否为空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
			return true;
		} else {
			return false;
		}
	}
}
