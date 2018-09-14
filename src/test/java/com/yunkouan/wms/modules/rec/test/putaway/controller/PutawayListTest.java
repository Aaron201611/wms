/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.putaway.controller;

import java.util.List;

import org.junit.Test;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.test.PutawayTest;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class PutawayListTest extends PutawayTest {
	
	
	
	@Test
	public void test() throws Exception {
		try {
//			this.testList();
//			this.testListOwner();
//			this.testListAsnNo();
//			this.testListPoNo();
//			this.testListStatus();
			
			this.testtest();
		} catch (Exception e) {
			e.printStackTrace();
			ResultModel rm = new ResultModel();
			rm.setError();
			rm.addMessage(e.getLocalizedMessage());
			super.toJson(rm, true);
		}
	}
	
	public void testtest() throws Exception {
		List<RecPutawayVO> listPtwByStock = super.ptwExtlService.listPtwByStock("1", "3", "21220170218003", Constant.PUTAWAY_STATUS_ACTIVE,Constant.PUTAWAY_STATUS_WORKING);
		for (RecPutawayVO recPutawayVO : listPtwByStock) {
			recPutawayVO.searchCache();
			System.out.println(recPutawayVO.getPutawayStatusComment());
		}
	}
	
	protected void testListStatus () throws Exception {
		RecPutawayVO ptwVO = null;
		ResultModel rm = null;
		System.out.println("-------------------- 打开状态 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.getPutaway().setPutawayStatus(Constant.PUTAWAY_STATUS_OPEN);
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("-------------------- 生效状态 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.getPutaway().setPutawayStatus(Constant.PUTAWAY_STATUS_ACTIVE);
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("-------------------- 作业中状态 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.getPutaway().setPutawayStatus(Constant.PUTAWAY_STATUS_WORKING);
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("-------------------- 已上架状态 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.getPutaway().setPutawayStatus(Constant.PUTAWAY_STATUS_COMPLETE);
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("-------------------- 取消状态 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.getPutaway().setPutawayStatus(Constant.PUTAWAY_STATUS_CANCEL);
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
	}
	
	/**
	 * 测试查询上架单
	 * @throws Exception
	 * @version 2017年3月23日 下午2:09:37<br/>
	 * @author andy wang<br/>
	 */
	protected void testList () throws Exception {
		RecPutawayVO ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ResultModel rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
	}
	
	/**
	 * 货主模糊查询
	 * @throws Exception
	 * @version 2017年3月23日 下午2:11:32<br/>
	 * @author andy wang<br/>
	 */
	protected void testListOwner () throws Exception {
		RecPutawayVO ptwVO = null;
		ResultModel rm = null;
		System.out.println("-------------------- 查询已有的货主 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.setOwnerComment("心");
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("-------------------- 查询没有的货主 -----------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.setOwnerComment("腾");
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
	}
	
	/**
	 * ASN单号查询
	 * @throws Exception
	 * @version 2017年3月23日 下午2:11:50<br/>
	 * @author andy wang<br/>
	 */
	protected void testListAsnNo  () throws Exception {
		RecPutawayVO ptwVO = null;
		ResultModel rm = null;
		System.out.println("----------------------- 有相关的ASN单号------------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.setLikeAsnNo("IN170315000005");
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("----------------------- 没有相关的ASN单号------------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.setLikeAsnNo("REC05020201702090001");
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
	}
	
	/**
	 * PO单号查询
	 * @throws Exception
	 * @version 2017年3月23日 下午2:14:03<br/>
	 * @author andy wang<br/>
	 */
	protected void testListPoNo  () throws Exception {
		RecPutawayVO ptwVO = null;
		ResultModel rm = null;
		System.out.println("----------------------- 有相关的PO单号------------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.setLikePoNo("6398520");
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
		System.out.println("----------------------- 没有相关的PO单号------------------------");
		ptwVO = new RecPutawayVO();
		ptwVO.setPageSize(3);
		ptwVO.setCurrentPage(0);
		ptwVO.setLikePoNo("1238796");
		rm = super.controller.list(ptwVO);
		super.formatResult(rm);
		super.toJson(rm);
	}
	
}
