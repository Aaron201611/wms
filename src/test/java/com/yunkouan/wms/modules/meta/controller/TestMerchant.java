package com.yunkouan.wms.modules.meta.controller;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.vo.MerchantVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestMerchant extends BaseJunitTest {
	@Autowired
	private MerchantController c;
	@Autowired
	private IMerchantService s;

	@Test
	@Ignore
	public void testList1() {
		List<String> l = s.list("巴巴");
		System.out.println(l.size());
	}

	@Test
	@Ignore
	public void testList() throws Exception {
		MetaMerchant obj = new MetaMerchant();
//		obj.setCreateTime(new Date());
		MerchantVo vo = new MerchantVo(obj);
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, MerchantVo.class.getName()));
		super.toJson(r);
	}

//	@Test
//	@Ignore
//	public void testView() throws DaoException, ServiceException {
//		ResultModel r = c.view("0F833531BC834684BEF4B8C00F4C3997");
//		System.out.println(r.getObj());
//	}

	@Test
//	@Ignore
	public void testAdd() throws Exception {
		MetaMerchant obj = new MetaMerchant();
		obj.setMerchantNo(IdUtil.getUUID());
		obj.setMerchantName(super.getChineseName());
		obj.setMerchantStatus(1);
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setEmail("tphe06@qq.com");
		obj.setCreateTime(new Date());
		obj.setIsConsignor(false);
		MerchantVo vo = new MerchantVo(obj);
		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, MerchantVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws DaoException, ServiceException {
		MetaMerchant obj = new MetaMerchant();
		obj.setMerchantId("0F833531BC834684BEF4B8C00F4C3997");
		obj.setMerchantStatus(2);
		obj.setCreateTime(new Date());
		MerchantVo vo = new MerchantVo(obj);
		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, MerchantVo.class.getName()));
		System.out.println(r.getObj());
	}
}