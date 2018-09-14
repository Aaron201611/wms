/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日 上午10:27:08<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.meta.controller.LocationController;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位测试<br/><br/>
 * @version 2017年3月13日 上午10:27:08<br/>
 * @author andy wang<br/>
 */
public class LocationTest extends BaseJunitTest {
	
	@Autowired
	private LocationController controller;
	@Autowired
	private ILocationExtlService locExtlService;
	
	@Test
	public void test () {
		try {
//			this.testInsertLoc();
			this.testListLoc();
//			this.testViewLoc();
//			this.testUpdateLoc();
//			this.testActiveLoc();
//			this.testInActiveLoc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 测试 - 失效EEE
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:36<br/>
	 * @author andy wang<br/>
	 */
	public void testInActiveLoc () throws Exception {
		List<String> listLocId = new ArrayList<String>();
		listLocId.add("B0D64D51BF854FC0A64653EA555B53E6");
		ResultModel rm = this.controller.disableLoc(listLocId);
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 生效EEE
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:27<br/>
	 * @author andy wang<br/>
	 */
	public void testActiveLoc () throws Exception {
		List<String> listLocId = new ArrayList<String>();
		listLocId.add("B0D64D51BF854FC0A64653EA555B53E6");
		ResultModel rm = this.controller.enableLoc(listLocId);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试 - 更新EEE信息
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:20<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateLoc () throws Exception {
		MetaLocation location = this.locExtlService.findLocById("");
		
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
		MetaLocationVO locationVO = new MetaLocationVO(location);
		BeanPropertyBindingResult br = super.validateEntity(locationVO, ValidUpdate.class);
		ResultModel rm = this.controller.updateLoc(locationVO, br);
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 查询单个EEE信息
	 * @throws Exception
	 * @version 2017年3月11日 下午2:03:13<br/>
	 * @author andy wang<br/>
	 */
	public void testViewLoc () throws Exception {
		ResultModel rm = this.controller.viewLoc("101");
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 查询EEE列表
	 * @throws Exception
	 * @version 2017年3月11日 下午1:36:17<br/>
	 * @author andy wang<br/>
	 */
	private void testListLoc () throws Exception {
		MetaLocationVO locationVO = new MetaLocationVO();
		ResultModel rm = this.controller.listLoc(locationVO);
		super.formatResult(rm);
	}

	/**
	 * 测试 - 保存EEE
	 * @version 2017年3月11日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	private void testInsertLoc () throws Exception {
		MetaLocationVO locationVO = new MetaLocationVO();
		MetaLocation location = locationVO.getLocation();
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
//		location.set
		BeanPropertyBindingResult br = super.validateEntity(locationVO, ValidSave.class);
		ResultModel rm = this.controller.insertLoc(locationVO, br);
		super.formatResult(rm);
	}
	
	
}
