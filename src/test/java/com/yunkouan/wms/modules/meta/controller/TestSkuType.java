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
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.vo.SkuTypeVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestSkuType extends BaseJunitTest {
	@Autowired
	private SkuTypeController c;

	@Test
	@Ignore
	public void testList() throws ServiceException, DaoException {
		MetaSkuType obj = new MetaSkuType();
		obj.setCreateTime(new Date());
		SkuTypeVo vo = new SkuTypeVo(obj);
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, SkuTypeVo.class.getName()));
		System.out.println(r.getList().size());
	}

//	@Test
//	@Ignore
//	public void testView() throws ServiceException, DaoException {
//		ResultModel r = c.view("55856C8D37B24AEAB2583159F08DDCA1");
//		System.out.println(r.getObj());
//	}

	@Test
//	@Ignore
	public void testAdd() throws ServiceException, DaoException {
		MetaSkuType obj = new MetaSkuType();
		obj.setSkuTypeNo(IdUtil.getUUID());
		obj.setSkuTypeName("test");
		obj.setSkuTypeStatus(1);
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setCreateTime(new Date());
		SkuTypeVo vo = new SkuTypeVo(obj);
		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, SkuTypeVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws ServiceException, DaoException {
		MetaSkuType obj = new MetaSkuType();
		obj.setSkuTypeId("55856C8D37B24AEAB2583159F08DDCA1");
		obj.setSkuTypeStatus(2);
		obj.setCreateTime(new Date());
		SkuTypeVo vo = new SkuTypeVo(obj);
		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, SkuTypeVo.class.getName()));
		System.out.println(r.getObj());
	}
}