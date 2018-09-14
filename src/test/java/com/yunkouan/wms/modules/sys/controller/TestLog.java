package com.yunkouan.wms.modules.sys.controller;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysLog;
import com.yunkouan.saas.modules.sys.service.ILogService;
import com.yunkouan.saas.modules.sys.vo.LogVo;
import com.yunkouan.wms.common.BaseJunitTest;

/**
 * @author tphe06 2017年2月10日
 */
public class TestLog extends BaseJunitTest {
	@Autowired
	private LogController c;
	@Autowired
	private ILogService s;

	@Test
//	@Ignore
	public void testList1() throws DaoException, ServiceException {
		LogVo vo = new LogVo();
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, LogVo.class.getName()));
		System.out.println(r.getList().size());
	}

	@Test
	public void testAdd() {
		SysLog entity = new SysLog();
		entity.setLogContent("11111111");
		System.out.println(s.add(entity));
	}
}