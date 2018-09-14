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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.test.PutawayTest;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class PutawayAutoTest extends PutawayTest {

	@Test
	public void test () throws Exception {
		try {
			testAuto();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testAuto () throws Exception {
		String ptwId = "AA2D6FE0BC904F2F8F7594FA871AF592";
		
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		recPutawayVO.getPutaway().setPutawayId(ptwId);
		List<RecPutawayDetailVO> listPutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListPutawayDetailVO(listPutawayDetailVO);
		// 查询上架单明细
		List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(ptwId);
		for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
			listPutawayDetailVO.add(new RecPutawayDetailVO(recPutawayDetail));
		}
		ResultModel rm = super.controller.auto(recPutawayVO);
		super.formatResult(rm);
	}
	
	
}
