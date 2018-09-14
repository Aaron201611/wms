package com.yunkouan.wms.modules.sys.controller;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.wms.modules.sys.vo.AccountVo;

/**
 * @author tphe06 2017年2月28日
 */
public class TestIndex extends BaseJunitTest {
	@Autowired
	private IndexController c;

//	@Test
//	public void testMenus() {
//		ResultModel r = c.loadMenu();
//		System.out.println(r);
//	}

//	@Test
//	@Ignore
//	public void testUsers() {
//		ResultModel r = c.loadUser();
//		System.out.println(r);
//	}
	//monitoringCenter

	@Test
	@Ignore
	public void testUpdateUser() throws DaoException, ServiceException {
		AccountVo vo = new AccountVo(new SysAccount().setAccountPwd("admin").setNote(""));
		vo.setOldPwd("admin");
//		vo.setUser(new SysUser().setEmail("tphe06@qq.com"));
		ResultModel r = c.updateUser(vo, new BeanPropertyBindingResult(vo, vo.getClass().getName()));
		System.out.println(r);
	}
}