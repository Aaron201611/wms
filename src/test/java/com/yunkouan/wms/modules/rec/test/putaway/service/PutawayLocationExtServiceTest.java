/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年5月15日 下午5:11:08<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.putaway.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.rec.service.IPutawayLocationExtlService;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;

/**
 * 上架单操作明细业务测试
 * <br/><br/>
 * @version 2017年5月15日 下午5:11:08<br/>
 * @author andy wang<br/>
 */
public class PutawayLocationExtServiceTest extends BaseJunitTest {
	
	@Autowired
	private IPutawayLocationExtlService ptwLocExtlService;
	
	
	@Test
	public void test () {
		try {
			List<RecPutawayLocationVO> list = this.ptwLocExtlService.listPtwLocRecalByLocId("1");
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
