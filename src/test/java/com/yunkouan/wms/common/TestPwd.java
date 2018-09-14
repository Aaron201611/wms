package com.yunkouan.wms.common;

import com.yunkouan.util.UserUtil;

/**
 * @author tphe06 2017年3月7日
 */
public class TestPwd {
	public static void main(String[] args) {
		String pwd  = UserUtil.entryptPassword("7218EDDF25CC44F698E2B8AC3C87B65B"+"admin");
		System.out.println(pwd);
	}
}