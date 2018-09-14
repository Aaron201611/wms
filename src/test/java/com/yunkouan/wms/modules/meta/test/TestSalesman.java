package com.yunkouan.wms.modules.meta.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.meta.entity.MetaSalesman;
import com.yunkouan.wms.modules.meta.service.impl.SalesmanServiceImpl;
import com.yunkouan.wms.modules.meta.vo.MetaSalesmanVo;

@Service
public class TestSalesman extends BaseJunitTest{
	@Autowired
	SalesmanServiceImpl service;
	@Test
	public void testadd() throws DaoException, ServiceException{
		MetaSalesmanVo vo= new MetaSalesmanVo();
		MetaSalesman entity=new MetaSalesman();
		entity.setMobile("13232132");
		entity.setSex(1);
		entity.setSalesmanNo("001");
		entity.setDepartmentId("12321");
		entity.setDepartmentName("部门1");
		entity.setSalesmanName("张三");
		vo.setEntity(entity);
		service.add(vo);
	}
	@Test
	public void testList() throws ServiceException, DaoException{
		MetaSalesmanVo vo= new MetaSalesmanVo();
		service.pageList(vo);
	}
	@Test
	public void testUpdate() throws DaoException, ServiceException{
		MetaSalesmanVo vo= new MetaSalesmanVo();
		MetaSalesman entity=new MetaSalesman();
		entity.setMobile("13232132");
		entity.setSex(1);
		entity.setSalesmanNo("001");
		entity.setDepartmentId("12321");
		entity.setDepartmentName("部门1");
		entity.setSalesmanName("张三");
		vo.setEntity(entity);
		service.add(vo);
	}
}
