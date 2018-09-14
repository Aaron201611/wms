/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月17日 下午10:55:07<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.asn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.MessageUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.test.AsnTest;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * 测试保存ASN单<br/><br/>
 * @version 2017年3月17日 下午10:55:07<br/>
 * @author andy wang<br/>
 */
@SuppressWarnings("deprecation")
public class AsnInsertTest extends AsnTest {
	
	/*
	 
	 	SELECT * FROM rec_asn ORDER BY asn_id2 DESC 
	
		SELECT * FROM rec_asn_detail WHERE asn_id = '16E91DF74B844756BC4750EFA63155A2'
		
		SELECT SUM(order_qty),SUM(order_weight),SUM(order_volume) FROM rec_asn_detail WHERE asn_id = '16E91DF74B844756BC4750EFA63155A2'
	 
	 * */
	@Test
	public void test () throws Exception {
		try {
			this.testInsert();
//			this.testInsertSameSku();
//			this.testInsertWithoutMain();
//			this.testInsertValidate();
//			this.testInsertSamePoNo();
		} catch (Exception e) {
			e.printStackTrace();
			ResultModel rm = new ResultModel();
			rm.setError();
			rm.addMessage(e.getLocalizedMessage());
			super.toJson(rm,true);
		}
	}
	
	
	/**
	 * 测试相同PO单号
	 * @throws Exception
	 * @version 2017年3月19日 下午7:22:22<br/>
	 * @author andy wang<br/>
	 */
	public void testInsertSamePoNo () throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn recAsn = asnVO.getAsn();
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(list);
		Random r = new Random();
		
		recAsn.setContactAddress("广东省广州市天河区华明路" + r.nextInt(1000) + "号");
		recAsn.setContactPerson(super.getChineseName());
		recAsn.setContactPhone(String.format("131%08d", r.nextInt(10000000)));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setDocType(Constant.ASN_DOCTYPE_RETURNED);
		recAsn.setOrderDate(new Date());
		recAsn.setOwner("2");
		recAsn.setPoNo("7907141");
		recAsn.setSender("1");
		
		for (int i = 0; i < 2; i++) {
			RecAsnDetail detail = new RecAsnDetail();
			detail.setBatchNo("2122017021800"+i);
			detail.setExpiredDate(new Date(2019, 3, 12));
			detail.setMeasureUnit("包");
			detail.setOrderQty(new Double(Math.ceil(Math.random() * 100)));
			detail.setOrderVolume(Math.ceil(Math.random() * 100));
			detail.setOrderWeight(Math.ceil(Math.random() * 100));
			detail.setPackId("1");
			detail.setProduceDate(new Date());
			detail.setSkuId(r.nextInt(5) + "");
			list.add(detail);
		}
		
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSave.class);
		ResultModel rm = this.controller.add(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 测试字段校验
	 * @throws Exception
	 * @version 2017年3月19日 下午12:53:25<br/>
	 * @author andy wang<br/>
	 */
	public void testInsertValidate () throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn recAsn = asnVO.getAsn();
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(list);
		
		recAsn.setContactAddress(super.strLen("a", 516));
		recAsn.setContactPerson(super.strLen("a", 65));
		recAsn.setContactPhone(super.strLen("a", 17));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setPoNo(super.strLen("a", 65));
		recAsn.setNote(super.strLen("a", 2049));
		recAsn.setAsnNo1(super.strLen("a", 33));
		recAsn.setAsnNo2(super.strLen("a", 33));
		recAsn.setSender("1");
		
		RecAsnDetail detail = null;
		detail = new RecAsnDetail();
		detail.setExpiredDate(new Date());
		detail.setProduceDate(new Date());
		list.add(detail);
		
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSave.class);
		ResultModel rm = this.controller.add(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试保存没有货品明细
	 * @throws Exception
	 * @version 2017年3月19日 下午12:51:30<br/>
	 * @author andy wang<br/>
	 */
	public void testInsertWithoutMain () throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn recAsn = asnVO.getAsn();
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(list);
		Random r = new Random();
		
		recAsn.setContactAddress("广东省广州市天河区华明路" + r.nextInt(1000) + "号");
		recAsn.setContactPerson(super.getChineseName());
		recAsn.setContactPhone(String.format("131%08d", r.nextInt(10000000)));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setDocType(Constant.ASN_DOCTYPE_RETURNED);
		recAsn.setOrderDate(new Date());
		recAsn.setOwner("2");
		recAsn.setPoNo(r.nextInt(10000000)+"");
		recAsn.setSender("1");
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSave.class);
		ResultModel rm = this.controller.add(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 测试保存相同批次货品
	 * @throws Exception
	 * @version 2017年3月19日 下午12:50:17<br/>
	 * @author andy wang<br/>
	 */
	public void testInsertSameSku () throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn recAsn = asnVO.getAsn();
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(list);
		Random r = new Random();
		
		recAsn.setContactAddress("广东省广州市天河区华明路" + r.nextInt(1000) + "号");
		recAsn.setContactPerson(super.getChineseName());
		recAsn.setContactPhone(String.format("131%08d", r.nextInt(10000000)));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setDocType(Constant.ASN_DOCTYPE_RETURNED);
		recAsn.setOrderDate(new Date());
		recAsn.setOwner("2");
		recAsn.setPoNo(r.nextInt(10000000)+"");
		recAsn.setSender("1");

		for (int i = 0; i < 2; i++) {
			RecAsnDetail detail = new RecAsnDetail();
			detail.setBatchNo("aabbcc");
			detail.setExpiredDate(new Date(2019, 3, 12));
			detail.setMeasureUnit("包");
			detail.setOrderQty(new Double(Math.ceil(Math.random() * 100)));
			detail.setOrderVolume(Math.ceil(Math.random() * 100));
			detail.setOrderWeight(Math.ceil(Math.random() * 100));
			detail.setPackId("1");
			detail.setProduceDate(new Date());
			detail.setSkuId("1");
			list.add(detail);
		}
		
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSave.class);
		ResultModel rm = this.controller.add(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
	
	/**
	 * 测试 - 保存ASN单
	 * @throws Exception
	 * @version 2017年3月19日 下午12:03:07<br/>
	 * @author andy wang<br/>
	 */
	public String testInsert () throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn recAsn = asnVO.getAsn();
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(list);
		Random r = new Random();
		
		recAsn.setContactAddress("广东省广州市天河区华明路" + r.nextInt(1000) + "号");
		recAsn.setContactPerson(super.getChineseName());
		recAsn.setContactPhone(String.format("131%08d", r.nextInt(10000000)));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setDocType(Constant.ASN_DOCTYPE_RETURNED);
		recAsn.setOrderDate(new Date());
		recAsn.setOwner("1");
		recAsn.setPoNo(r.nextInt(10000000)+"");
		recAsn.setSender("1");

		for (int i = 0; i < 3; i++) {
			RecAsnDetail detail = new RecAsnDetail();
			detail.setBatchNo("2122017021800"+i);
			detail.setExpiredDate(new Date(2019, 3, 12));
			detail.setMeasureUnit("包");
			detail.setOrderQty(new Double(Math.ceil(Math.random() * 100)));
			detail.setOrderVolume(Math.ceil(Math.random() * 100));
			detail.setOrderWeight(Math.ceil(Math.random() * 100));
			detail.setPackId("1");
			detail.setProduceDate(new Date());
			detail.setSkuId(r.nextInt(5) + "");
			list.add(detail);
		}
		
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSave.class);
		ResultModel rm = this.controller.add(asnVO,br);
		super.formatResult(rm);
		System.out.println(((RecAsnVO)rm.getObj()).getAsn().getAsnId());
		return ((RecAsnVO)rm.getObj()).getAsn().getAsnId();
	}
	
}
