package com.yunkouan.wms.modules.meta.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaDepartment;
import com.yunkouan.wms.modules.meta.service.impl.DepartmentServiceImpl;
import com.yunkouan.wms.modules.meta.vo.MetaDepartmentVo;
@Service
public class TestDepartment extends BaseJunitTest{
	@Autowired
	DepartmentServiceImpl service;
	@Test
	public void testadd() throws DaoException{
		MetaDepartmentVo vo=new MetaDepartmentVo();
		MetaDepartment entity=new MetaDepartment();
		entity.setDepartmentName("部门2");
		entity.setDepartmentNo("001");
		entity.setDepartmentStatus(20);
		entity.setPrincipal("小黄");
		entity.setMobile("11111111111");
		vo.setEntity(entity);
		service.add(vo);
	}
	@Test
	public void testEnable() throws DaoException, ServiceException{
		MetaDepartmentVo vo=new MetaDepartmentVo();
		MetaDepartment entity=new MetaDepartment();
		entity.setDepartmentName("部门1");
		entity.setDepartmentNo("001");
		//entity.setDepartmentStatus(20);
		entity.setPrincipal("小黄");
		entity.setMobile("11111111111");
		vo.setEntity(entity);
		service.enable(vo);
	}
	@Test
	public void testupdate() throws DaoException, ServiceException{
		MetaDepartmentVo vo=new MetaDepartmentVo();
		MetaDepartment entity=new MetaDepartment();
		entity.setDepartmentName("部门");
		entity.setDepartmentNo("002");
		//entity.setDepartmentStatus(20);
		entity.setPrincipal("小黄");
		entity.setMobile("11111222111");
		vo.setEntity(entity);
		service.update(vo);
	}
	@Test
	public void testList() throws DaoException, ServiceException{
		MetaDepartmentVo vo=new MetaDepartmentVo();
		service.pageList(vo);
	}
	@Test
	public void testcancle() throws DaoException, ServiceException{
		MetaDepartmentVo vo=new MetaDepartmentVo();
		MetaDepartment entity=new MetaDepartment();
		//entity.setDepartmentName("部门");
		entity.setDepartmentNo("001");
		vo.setEntity(entity);
		service.cancel(vo);
	}
}
