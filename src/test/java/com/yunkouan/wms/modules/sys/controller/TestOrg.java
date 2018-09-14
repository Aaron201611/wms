package com.yunkouan.wms.modules.sys.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.wms.common.BaseJunitTest;

/**
 * @author tphe06 2017年2月10日
 */
@Component
public class TestOrg extends BaseJunitTest {
	@Autowired
	private IOrgService s;

	@Test
	public void testList1() {
//		List<String> l = s.list("");
//		System.out.println(l.size());
	}
}