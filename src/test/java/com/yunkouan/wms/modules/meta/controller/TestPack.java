package com.yunkouan.wms.modules.meta.controller;

import org.junit.Test;

import java.util.Date;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.vo.PackVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestPack extends BaseJunitTest {
	@Autowired
	private PackController c;

	@Test
	@Ignore
	public void testList() throws DaoException, ServiceException {
		MetaPack obj = new MetaPack();
		obj.setCreateTime(new Date());
		PackVo vo = new PackVo(obj);
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, PackVo.class.getName()));
		System.out.println(r.getList().size());
	}

//	@Test
//	@Ignore
//	public void testView() throws DaoException, ServiceException {
//		ResultModel r = c.view("AEE6BDA95818467780C7506CFE0857A1");
//		System.out.println(r.getObj());
//	}

	@Test
//	@Ignore
	public void testAdd() throws DaoException, ServiceException {
		for(int i=0; i<5; ++i){
		MetaPack obj = new MetaPack();
		obj.setPackNo(IdUtil.getUUID());
		obj.setPackUnit(IdUtil.getUUID());
		obj.setPackHeight(1.01d);
		obj.setPackWide(0.002);
		obj.setPackStatus(1);
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setCreateTime(new Date());
		PackVo vo = new PackVo(obj);
		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, PackVo.class.getName()));
		System.out.println(r.getObj());
		}
	}

	@Test
	@Ignore
	public void testUpdate() throws DaoException, ServiceException {
		MetaPack obj = new MetaPack();
		obj.setPackId("AEE6BDA95818467780C7506CFE0857A1");
		obj.setPackStatus(2);
		obj.setCreateTime(new Date());
		PackVo vo = new PackVo(obj);
		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, PackVo.class.getName()));
		System.out.println(r.getObj());
	}
}