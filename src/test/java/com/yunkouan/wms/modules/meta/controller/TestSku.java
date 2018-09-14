package com.yunkouan.wms.modules.meta.controller;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.alibaba.fastjson.JSON;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuPack;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestSku extends BaseJunitTest {
	@Autowired
	private SkuController c;
	@Autowired
	private ISkuService s;

	@Test
//	@Ignore
	public void testList() throws Exception {
		SkuVo vo = new SkuVo();
		MetaSku obj = new MetaSku();
		obj.setCreateTime(new Date());
		vo.setEntity(obj);
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, SkuVo.class.getName()));
		super.toJson(r);
	}

//	@Test
//	@Ignore
//	public void testView() throws DaoException, ServiceException {
//		ResultModel r = c.view("CCC32A84E09847A797708B4BD6915C7A");
//		System.out.println(r.getObj());
//	}

	@Test
	@Ignore
	public void testAdd() throws DaoException, ServiceException {
		SkuVo vo = new SkuVo();
		MetaSku obj = new MetaSku();
		obj.setSkuNo(IdUtil.getUUID());
		obj.setSkuName("test");
		obj.setSkuStatus(1);
		obj.setCreatePerson("tphe06");
		obj.setUpdatePerson("tphe06");
		obj.setCreateTime(new Date());
		vo.setEntity(obj);
		List<MetaPack> list = new ArrayList<MetaPack>();
		for(int i=0; i<10; ++i) {
			MetaPack p = new MetaPack();
			p.setPackId("AC961851C25E4A7D9E57F7BA1D666DE0");
			p.setPackPercent(12.34);
			list.add(p);
		}
		vo.setList(list);
		ResultModel r = c.add(vo, new BeanPropertyBindingResult(vo, SkuVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws DaoException, ServiceException {
		SkuVo vo = new SkuVo();
		MetaSku obj = new MetaSku();
		obj.setSkuId("CCC32A84E09847A797708B4BD6915C7A");
		obj.setSkuStatus(2);
		obj.setCreateTime(new Date());
		vo.setEntity(obj);
		List<MetaPack> list = new ArrayList<MetaPack>();
		for(int i=0; i<5; ++i) {
			MetaPack p = new MetaPack();
			p.setPackId("20F0E470D6204A8793F51F0D534F2C2A");
			p.setPackPercent(12.34);
			list.add(p);
		}
		vo.setList(list);
		ResultModel r = c.update(vo, new BeanPropertyBindingResult(vo, SkuVo.class.getName()));
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testSelectSkuPack() {
		MetaSkuPack o = s.querySkuPack(new MetaSkuPack().setSkuId("CCEFC75647BA42988D3F96E8FB99BE5D").setPackId("AC961851C25E4A7D9E57F7BA1D666DE0"));
		System.out.println(o);
	}

	@Test
	@Ignore
	public void testList4stock() throws DaoException, ServiceException {
		SkuVo vo = new SkuVo(new MetaSku().setSkuNo("3D473F216727433AB46FD60BE7F7739_"));
//		vo.setMerchant(new MetaMerchant().setMerchantName(""));
		Principal p = LoginUtil.getLoginUser();
		List<MetaSku> list = s.list4stock(vo, p);
		System.out.println(list.size());
	}
}