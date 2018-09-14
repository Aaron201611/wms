/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月16日 上午9:27:22<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.asn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.DurationUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.vo.MetaLocationSpecVO;
import com.yunkouan.wms.modules.rec.test.AsnTest;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * 测试ASN列表查询<br/><br/>
 * @version 2017年3月16日 上午9:27:22<br/>
 * @author andy wang<br/>
 */
public class AsnListTest extends AsnTest {
	
	public void test () {
		try {
//			this.testPage();
//			this.testOwner();
//			this.testPoNo();
			this.testAsnNo();
//			this.testStatus();
//			this.testOrderDate();
//			this.testDataFrom();
//			this.testCreatePerson();
//			this.testListStock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试 - 查询库存列表
	 * @throws Exception
	 * @version 2017年3月30日 下午4:00:13<br/>
	 * @author andy wang<br/>
	 */
	public void testListStock () throws Exception {
		DurationUtil d = new DurationUtil();
//		d.start();
		ResultModel rm = this.controller.listStock(150);
//		System.out.println(rm.getList().size());
//		d.end(true);
		List<RecAsnVO> list = rm.getList();
		List<String> l = new ArrayList<String>();
		for (RecAsnVO vo : list) {
			if ( l.contains(vo.getAsn().getAsnId()) ) {
				System.out.println("重复");
			}
			l.add(vo.getAsn().getAsnId());
		}
//		super.toJson(rm);
	}
	
	/**
	 * 分页查询
	 * @throws Exception
	 * @version 2017年3月11日 下午1:36:17<br/>
	 * @author andy wang<br/>
	 */
	public void testPage () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("----------------------首次查询---------------------");
		asnVO = new RecAsnVO();
		asnVO.setCurrentPage(0);
		asnVO.setPageSize(3);
		asnVO.getAsn().setCreateTime(new Date(1902,2,1));
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
		super.toJson(rm,true);
//		System.out.println("----------------------第二页---------------------");
//		asnVO = new RecAsnVO();
//		asnVO.setCurrentPage(1);
//		asnVO.setPageSize(3);
//		rm = this.controller.listASN(asnVO);
//		super.formatResult(rm);
//		super.toJson(rm);
//		System.out.println("----------------------第三页---------------------");
//		asnVO = new RecAsnVO();
//		asnVO.setCurrentPage(2);
//		asnVO.setPageSize(3);
//		rm = this.controller.listASN(asnVO);
//		super.formatResult(rm);
//		super.toJson(rm);


		
	}
	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
//		String json = "{\"locationName\":\"库位100\","
//				+ "\"skuName\":\"\",\"specModel\":\"\",\"measureUnit\":\"\","
//				+ "\"findNum\":null,\"freezeType\":0,\"invStock\":{\"stockId\":\"\","
//				+ "\"batchNo\":\"\",\"skuStatus\":null,\"stockQty\":null,\"skuId\":\"\","
//				+ "\"locationId\":\"\"},\"invLog\":{\"note\":\"\",\"opPerson\":\"\"}}";
//		
//		InvStockVO readValue = objectMapper.readValue(json, InvStockVO.class);
//		System.out.println(objectMapper.writeValueAsString(readValue));
		
		
		MetaLocationSpecVO vo = new MetaLocationSpecVO();
		String json;
		try {
			json = objectMapper.writeValueAsString(vo);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 测试货主查询条件
	 * @throws Exception
	 * @version 2017年3月16日 下午9:52:46<br/>
	 * @author andy wang<br/>
	 */
	public void testOwner () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("----------------------货主查询条件---------------------");
		asnVO = new RecAsnVO();
		asnVO.setOwnerComment("里巴");
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}

	/**
	 * 测试PO单号查询条件
	 * @throws Exception
	 * @version 2017年3月16日 下午9:54:32<br/>
	 * @author andy wang<br/>
	 */
	public void testPoNo () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("----------------------PO单号查询条件---------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setPoNo("testPO1234123");
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试ASN编号查询条件
	 * @throws Exception
	 * @version 2017年3月17日 上午8:48:01<br/>
	 * @author andy wang<br/>
	 */
	public void testAsnNo () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("----------------------ASN编号查询条件---------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnNo("IN170409000003");
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}

	/**
	 * 测试状态查询条件
	 * @throws Exception
	 * @version 2017年3月16日 下午9:56:56<br/>
	 * @author andy wang<br/>
	 */
	public void testStatus () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("----------------------打开状态---------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_OPEN);
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
//		System.out.println("----------------------生效状态---------------------");
//		asnVO = new RecAsnVO();
//		asnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_ACTIVE);
//		rm = this.controller.listASN(asnVO);
//		super.formatResult(rm);
//		System.out.println("----------------------已收货状态---------------------");
//		asnVO = new RecAsnVO();
//		asnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_RECEIVED);
//		rm = this.controller.listASN(asnVO);
//		super.formatResult(rm);
		System.out.println("----------------------取消状态---------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_CANCEL);
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}
	

	/**
	 * 测试订单日期查询条件
	 * @throws Exception
	 * @version 2017年3月16日 下午9:58:23<br/>
	 * @author andy wang<br/>
	 */
	public void testOrderDate () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("----------------------订单日期查询条件---------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setOrderDate(DateUtil.createDate(2017, 3, 16));
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}


	/**
	 * 测试单据来源查询条件
	 * @throws Exception
	 * @version 2017年3月16日 下午10:01:30<br/>
	 * @author andy wang<br/>
	 */
	public void testDataFrom () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("-------------------普通录入------------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
		System.out.println("--------------------Excel导入-----------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setDataFrom(Constant.ASN_DATAFROM_IMPORT);
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
		System.out.println("--------------------其他录入-----------------------");
		asnVO = new RecAsnVO();
		asnVO.getAsn().setDataFrom(Constant.ASN_DATAFROM_OTHER);
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试创建人
	 * @throws Exception
	 * @version 2017年3月17日 下午10:36:30<br/>
	 * @author andy wang<br/>
	 */
	public void testCreatePerson () throws Exception {
		RecAsnVO asnVO = null;
		ResultModel rm = null;
		System.out.println("-------------------测试创建人查询条件------------------------");
		asnVO = new RecAsnVO();
		asnVO.setCreatePersonComment("test");
		rm = this.controller.list(asnVO);
		super.formatResult(rm);
	}
	
}
