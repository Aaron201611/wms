package com.yunkouan.wms.modules.sys.controller;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.vo.UserVo;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;
/**
 * @author tphe06 2017年2月10日
 */
public class TestUser extends BaseJunitTest {
	@Autowired
	private UserController c;
	@Autowired
	private IUserService s;

	@Test
	@Ignore
	public void testList1() throws DaoException, ServiceException {
		List<String> l = s.list("test");
		System.out.println(l.size());
	}

	@Test
//	@Ignore
	public void testList() throws Exception {
//		SysUser obj = new SysUser();
//		obj.setCreateTime(new Date());
		UserVo vo = new UserVo();
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, UserVo.class.getName()));
		super.toJson(r);
	}

//	@Test
//	@Ignore
//	public void testView() throws ServiceException, DaoException {
//		ResultModel r = c.view("05AE845721404BEE831FD43FF0ABC357");
//		System.out.println(r.getObj());
//	}

	@Test
//	@Ignore
	public void testAdd() throws ServiceException, DaoException {
		SysUser obj = new SysUser();
		obj.setUserNo(IdUtil.getUUID());
		obj.setUserName("test");
		obj.setUserStatus(Constant.STATUS_ACTIVE);
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setPhone("13642668655");
		obj.setEmail("tphe06@qq.com");
		obj.setCreateTime(new Date());
		UserVo vo = new UserVo(obj);
//		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, UserVo.class.getName()));
//		System.out.println(r.getObj());
	}

	@Test
//	@Ignore
	public void testUpdate() throws ServiceException, DaoException {
		SysUser obj = new SysUser();
		obj.setUserId("05AE845721404BEE831FD43FF0ABC357");
		obj.setUserStatus(2);
		obj.setCreateTime(new Date());
		UserVo vo = new UserVo(obj);
//		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, UserVo.class.getName()));
//		System.out.println(r.getObj());
	}
}