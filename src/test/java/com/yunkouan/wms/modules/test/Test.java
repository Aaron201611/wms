package com.yunkouan.wms.modules.test;

import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PatternUtil;

public class Test {
	public static void main(String[] args) {
		String PREFIX = "shiro_";
		String id = "shiro_776d44f8-2418-4302-9330-0e9a7bb13adf";
		id = id.replaceFirst(PREFIX, "");
		System.out.println(id);
//		//数字截取
//		String r = NumberUtil.rounded(123.444d, 0);
//		System.out.println(r);
//		//正则表达式
//		String xml = "WMSR_1532571888362_wms_1438166480137_0001.XML";
//		boolean math = PatternUtil.valid(xml, "^WMSR_[\\d]+_WMS_[\\d]+_[\\d]{4}.xml$");
//		System.out.println(math);
	}
}