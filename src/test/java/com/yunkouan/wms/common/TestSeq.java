package com.yunkouan.wms.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.yunkouan.wms.common.dao.ISeqDao;

public class TestSeq extends BaseJunitTest {
	@Autowired
	private ISeqDao dao;

	@Test
	public void testList() {
		Long seq = dao.getSkuSeq();
		System.out.println(seq);
	}
}