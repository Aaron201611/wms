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
import com.yunkouan.wms.modules.rec.test.PutawayTest;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 生效上架单<br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class PutawayActiveTest extends PutawayTest {

	@Test
	public void test() throws Exception {
		try {
			this.testActive();
		} catch (Exception e) {
			e.printStackTrace();
			ResultModel rm = new ResultModel();
			rm.setError();
			rm.addMessage(e.getLocalizedMessage());
			super.toJson(rm, true);
		}
	}

	
	public void testActive() throws Exception {
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add("2CC594FC935C444E87464162DA48B37D");
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		recPutawayVO.setListPutawayId(listPutawayId);
		this.controller.enable(recPutawayVO);
	}
	
}
