/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月24日 下午1:22:41<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.meta.controller.LocationController;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 测试库位类<br/><br/>
 * @version 2017年2月24日 下午1:22:41<br/>
 * @author andy wang<br/>
 */
public class LocationExtlTest extends BaseJunitTest {
	
	
	@Autowired
	private ILocationExtlService locationExtlService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private LocationController controller;
	
	/**
	 * 测试方法
	 * @throws Exception
	 * @version 2017年2月24日 下午1:24:12<br/>
	 * @author andy wang<br/>
	 */
	@Test
	public void test () throws Exception {
		try {
//			testRecalCapacity();
			testAddCapacity();
//			testBlockLoc();
//			this.testList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试 - 冻结库位
	 * @version 2017年3月14日 下午3:39:05<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	private void testBlockLoc() throws Exception {
		
		locationExtlService.blockLoc("110", 0);
	}

	/**
	 * 测试增加库存接口
	 * @throws Exception
	 * @version 2017年2月24日 下午1:24:56<br/>
	 * @author andy wang<br/>
	 */
	public void testAddCapacity () throws Exception {
//		MetaLocationVO metaLocationVO = new MetaLocationVO(new MetaLocation());
//		metaLocationVO.getLocation().setLocationName("库位1");
//		MetaLocation findLocation = locationExtlService.findLocation(metaLocationVO);
//		System.out.println(findLocation.getLocationId());
//		locationExtlService.addCapacity("485CECF184CE4C129D4EA5BAC428B836", "F69799F97C5C4437ABC48B7BF8237191", null, -100,false);

		locationExtlService.addCapacity("13305A83D9CA4A2D8FB6BA4F93D4C940"
				, "17B95E3754EC448CAA9BDCFAD04FBC01", null, 1d,false);
		
		
//		locationExtlService.minusCapacity("485CECF184CE4C129D4EA5BAC428B836", "F69799F97C5C4437ABC48B7BF8237191", null, 100);
//		this.locationExtlService.addPreusedCapacity("485CECF184CE4C129D4EA5BAC428B836", "F69799F97C5C4437ABC48B7BF8237191", null, 100);
	}
	
	
	public void testRecalCapacity () throws Exception {
		locationExtlService.recalCapacity("C2CD535B344E4096A7D7652F852E6B78");
	}
	
	public void testList () throws Exception {
		MetaLocationVO vo = new MetaLocationVO();
		vo.setLikeAreaName("小的");
		ResultModel rm = controller.listLoc(vo);
		super.toJson(rm, true);
	}
	
	/**
	 * 
	 	添加测试数据
	 	
	 	INSERT INTO meta_location(location_id,location_no,location_name,area_id)
		VALUES ('1','1','1','1');

	 	UPDATE meta_location a SET location_name = CONCAT('库位',location_id2),location_id=location_id2,
	 	location_no=location_id2,area_id='1',warehouse_id='1',org_id='1',pack_id='1',is_block=1,
	 	location_status=20;
		
		UPDATE meta_location a SET max_capacity=1000,used_capacity=0,preused_capacity=0 WHERE max_capacity IS NULL;
		
		UPDATE meta_location SET X=FLOOR(1 + (RAND() * 50)),Y=FLOOR(1 + (RAND() * 50)),z=FLOOR(1 + (RAND() * 50)) WHERE X IS NULL ;
		
	 * 
	 */
	
}
