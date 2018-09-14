package com.yunkouan.wms.modules.sys.controller;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.wms.modules.sys.entity.SysRole;
import com.yunkouan.wms.modules.sys.service.IAccountService;
import com.yunkouan.wms.modules.sys.vo.AccountVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestAccount extends BaseJunitTest {
	@Autowired
	private AccountController c;
	@Autowired
	private IAccountService s;

	@Test
//	@Ignore
	public void testList() throws ServiceException, DaoException {
		SysAccount obj = new SysAccount();
		obj.setCreateTime(new Date());
		AccountVo vo = new AccountVo(obj);
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, AccountVo.class.getName()));
		System.out.println(r.getList().size());
	}

//	@Test
//	@Ignore
//	public void testView() throws ServiceException, DaoException {
//		ResultModel r = c.view("415B0FCD82EC415C94AD5404F94EDC02");
//		System.out.println(r.getObj());
//	}

	@Test
	@Ignore
	public void testAdd() throws ServiceException, DaoException {
		SysAccount obj = new SysAccount();
		obj.setAccountNo("admin");
		obj.setAccountName("admin");
		obj.setAccountPwd("admin");
		obj.setAccountStatus(Constant.STATUS_ACTIVE);
		obj.setUserId("384D873241D341B282253346CA481DCE");
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setCreateTime(new Date());
		obj.setOrgId("62E18D09DC2F4FC6A717F1E9503B40D3");
		AccountVo vo = new AccountVo(obj);
//		List<SysRole> list = new ArrayList<SysRole>();
//		for(int i=0; i<1; ++i) {
//			SysRole r = new SysRole();
//			r.setRoleId("5C939B3A13DF40A0AEA106A7006405FF");
//			list.add(r);
//		}
//		vo.setList(list);
		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, AccountVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws ServiceException, DaoException {
		SysAccount obj = new SysAccount();
		obj.setAccountId("14F46E18D940474DB9123FF9968024E1");
		obj.setAccountStatus(2);
		obj.setCreateTime(new Date());
		AccountVo vo = new AccountVo(obj);
//		List<SysRole> list = new ArrayList<SysRole>();
//		for(int i=0; i<10; ++i) {
//			SysRole r = new SysRole();
//			r.setRoleId("394B3CF49B5E43399A5BE52BEAF28A93");
//			list.add(r);
//		}
//		vo.setList(list);
		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, AccountVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testQuery() throws ServiceException, DaoException {
		List<SysAuth> list = s.query("14F46E18D940474DB9123FF9968024E1", "");
		System.out.println(list.size());
	}
}