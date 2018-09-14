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
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.sys.entity.SysRole;
import com.yunkouan.wms.modules.sys.entity.SysRoleAuth;
import com.yunkouan.wms.modules.sys.vo.RoleVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestRole extends BaseJunitTest {
	@Autowired
	private RoleController c;

//	@Test
//	@Ignore
//	public void testList() throws DaoException, ServiceException {
//		
//		SysRole obj = new SysRole();
//		obj.setCreateTime(new Date());
//		RoleVo vo = new RoleVo(obj);
//		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, RoleVo.class.getName()));
//		System.out.println(r.getList().size());
//	}

//	@Test
//	@Ignore
//	public void testView() throws ServiceException, DaoException {
//		ResultModel r = c.view("394B3CF49B5E43399A5BE52BEAF28A93");
//		System.out.println(r.getObj());
//	}

	@Test
	@Ignore
	public void testAdd() throws DaoException, ServiceException {
//		RoleVo vo = new RoleVo();
		SysRole obj = new SysRole();
		obj.setRoleNo(IdUtil.getUUID());
		obj.setRoleName("test");
		obj.setRoleStatus(Constant.STATUS_ACTIVE);
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setCreateTime(new Date());
		obj.setOrgId("62E18D09DC2F4FC6A717F1E9503B40D3");
		RoleVo vo = new RoleVo(obj);
		List<SysRoleAuth> list = new ArrayList<SysRoleAuth>();
		for(int i=0; i<1; ++i) {
			SysRoleAuth auth = new SysRoleAuth();
			auth.setOrgAuthId("8D0F9E4EB300479EB2482479082EFE41");
			list.add(auth);
//			list.add(new RoleAuthVo().setEntity(auth));
		}
//		vo.setList(list);
		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, RoleVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws DaoException, ServiceException {
//		RoleVo vo = new RoleVo();
		SysRole obj = new SysRole();
		obj.setRoleId("5C939B3A13DF40A0AEA106A7006405FF");
		obj.setRoleStatus(2);
		obj.setCreateTime(new Date());
		RoleVo vo = new RoleVo(obj);
		List<SysRoleAuth> list = new ArrayList<SysRoleAuth>();
		for(int i=0; i<1; ++i) {
			SysRoleAuth auth = new SysRoleAuth();
			auth.setOrgAuthId("3514BB390F4942349154DAB814FD88AF");
			list.add(auth);
			SysRoleAuth auth2 = new SysRoleAuth();
			auth2.setOrgAuthId("93230F37DA714D49A554453263FBE9EB");
			list.add(auth2);
			SysRoleAuth auth3 = new SysRoleAuth();
			auth3.setOrgAuthId("EF8715040AFC4AD99055AFD0BB13FCAD");
			list.add(auth3);
			SysRoleAuth auth4 = new SysRoleAuth();
			auth4.setOrgAuthId("FB29CCB89C2342F49CED7B40FECE4708");
			list.add(auth4);
		}
//		vo.setList(list);
		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, RoleVo.class.getName()));
		System.out.println(r.getObj());
	}
}