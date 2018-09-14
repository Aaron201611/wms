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
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.test.PutawayTest;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class PutawayConfirmTest extends PutawayTest {

	
	/**
		SELECT * FROM rec_putaway WHERE putaway_id='9A44F13E740749BFBE7EFEA3D41C8A39'
		SELECT * FROM rec_putaway ORDER BY putaway_id2 DESC 
		SELECT * FROM rec_putaway_location WHERE putaway_detail_id IN (SELECT putaway_detail_id FROM rec_putaway_detail WHERE putaway_id='9A44F13E740749BFBE7EFEA3D41C8A39')
	 * 
	 */
	
	
	@Test
	public void test() throws Exception {
		try {
			this.testConfirm();
		} catch (Exception e) {
			e.printStackTrace();
			ResultModel rm = new ResultModel();
			rm.setError();
			rm.addMessage(e.getLocalizedMessage());
			super.toJson(rm, true);
		}
	}

	
	public void testConfirm () throws Exception {
		RecPutawayVO vo = new RecPutawayVO();
		String ptwId = "2CC594FC935C444E87464162DA48B37D";
		vo.getPutaway().setPutawayId(ptwId);
		List<RecPutawayLocation> listLoc = super.ptwLocExtlService.listPutawayLocationByPtwId(ptwId, true);
		List<RecPutawayLocation> listRealLoc = new ArrayList<RecPutawayLocation>();
		vo.setListSavePutawayLocation(listRealLoc);
		for (RecPutawayLocation recPutawayLocation : listLoc) {
			recPutawayLocation.setPutawayLocationId(null);
			listRealLoc.add(recPutawayLocation);
		}
		BeanPropertyBindingResult br = super.validateEntity(vo, RecPutawayVO.class);
		super.controller.complete(vo,br);
	}
	
	
}
