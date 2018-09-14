package com.yunkouan.wms.modules.inv.test;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.inv.dao.IInvLogDao;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;

public class InvLogTest extends BaseJunitTest {
	@Autowired
	private IInvLogDao dao;

	@Test
	@Ignore
	public void testBackup() {
		InvLogVO vo = new InvLogVO();
		vo.setStartIndex(307);
		vo.setEndIndex(310);
		Integer sum = dao.backup(vo);
		System.out.println(sum);
	}

	@Test
	public void testList() {
		InvLogVO vo = new InvLogVO();
		vo.setOutDate("2017-09-10");
		InvLogVO list = dao.getOutList(vo);
		System.out.println(list.getStartIndex());
		System.out.println(list.getEndIndex());
		System.out.println(list.getSum());
	}
}