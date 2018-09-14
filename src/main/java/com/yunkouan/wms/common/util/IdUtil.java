/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午5:28:04<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.common.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.util.StringUtil;

/**
 * 用于创建业务UUID<br/>
 * 创建日期:<br/> 2017年2月8日 下午5:28:04<br/>
 * @author andy wang<br/>
 */
public class IdUtil {
	private static final String S = getChars();
	public static final String INIT = "A01";
	public static final String INIT4 = "0000";
	public static final String INIT6 = "000000";

	public static String getUUID () {
		return StringUtils.upperCase(UUID.randomUUID().toString().replaceAll("-", ""));
	}

	/**
	 * 初始化字符串
	 * @return
	 */
	private static String getChars() {
		StringBuilder buf = new StringBuilder();
		for(char c = '0'; c <= '9'; c++) {
			buf.append(c);
		}
		for(char c = 'A'; c <= 'Z'; c++) {
			buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * 取下一个字符
	 * @param c
	 * @return
	 */
	private static char next(char c) {
		int index = S.indexOf(c);
		return S.charAt(index + 1);
	}

	/**
	 * “加一”运算
	 * @param in 入参，待加一参数
	 * @param def 默认值，起始值
	 * @return
	 */
	public static String addOne(String in, String def) {
		if(StringUtil.isBlank(in)) return def;
		char[] _in = in.toCharArray();
		add(_in, _in.length - 1);
		String result = new String(_in);
		if(result.length() > def.length()) return result.substring(0, def.length());
		return result;
	}

	/**
	 * “加一”运算
	 * @param in
	 * @param index
	 */
	private static void add(char[] in, int index) {
		// 运算完毕
		if(index < 0) return ;
		if(in[index] == 'Z') {
			// 进位
			in[index] = '0';
			add(in, index-1);
		} else {
			// “加一”，结束
			in[index] = next(in[index]);
		}
	}

	public static void main(String[] args) {
		System.out.println(addOne("A04", "A01"));
		System.out.println(addOne("A09", "A01"));
		System.out.println(addOne("A0Z", "A01"));
		System.out.println(addOne("A9Z", "A01"));
		System.out.println(addOne("AZZ", "A01"));
		System.out.println(addOne("ZZZ", "A01"));
		System.out.println(addOne("ABZ", "A01"));
		System.out.println(addOne("MZZ", "A01"));
		System.out.println(addOne("BZZZ", "A01"));
	}
}