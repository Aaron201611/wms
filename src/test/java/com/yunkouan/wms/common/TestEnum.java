package com.yunkouan.wms.common;

import com.yunkouan.wms.common.constant.ExtInterf;

public class TestEnum {
	public static void main(String[] args) {
		System.out.println(ExtInterf.INTERFACE_CANCEL_ORDER.getValue());
		ExtInterf[] v = ExtInterf.values();
		System.out.println(v[0].getValue());
	}
}