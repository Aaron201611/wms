/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 上午11:07:23<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.saas.modules.sys.vo.MetaWarehouseVO;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.controller.WarehouseController;

/**
 * 仓库测试<br/><br/>
 * @version 2017年3月11日 上午11:07:23<br/>
 * @author andy wang<br/>
 */
public class WarehouseTest extends BaseJunitTest {

	/** 仓库控制类 <br/> add by andy wang */
	@Autowired
	private WarehouseController controller;
	
	/** 仓库外调业务类 <br/> add by andy wang */
	@Autowired
	private IWarehouseExtlService wrhExtlService;
	
	/**
	 * 测试方法
	 * @version 2017年3月11日 上午11:08:09<br/>
	 * @author andy wang<br/>
	 */
	@Test
//	public void test () {
//		try {
////			this.testInsertWrh();
////			this.testListWrh();
////			this.testViewWrh();
////			this.testUpdateWrh();
////			this.testActiveWrh();
//			this.testInActiveWrh();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	/**
	 * 测试 - 失效仓库
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:36<br/>
	 * @author andy wang<br/>
	 */
//	public void testInActiveWrh () throws Exception {
//		List<String> listWrhId = new ArrayList<String>();
//		listWrhId.add("B0D64D51BF854FC0A64653EA555B53E6");
//		ResultModel rm = this.controller.disableWrh(listWrhId);
//		super.formatResult(rm);
//	}
	
	/**
	 * 测试 - 生效仓库
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:27<br/>
	 * @author andy wang<br/>
	 */
//	public void testActiveWrh () throws Exception {
//		List<String> listWrhId = new ArrayList<String>();
//		listWrhId.add("B0D64D51BF854FC0A64653EA555B53E6");
//		ResultModel rm = this.controller.enableWrh(listWrhId);
//		super.formatResult(rm);
//	}
	
	
	/**
	 * 测试 - 更新仓库信息
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:20<br/>
	 * @author andy wang<br/>
	 */
//	public void testUpdateWrh () throws Exception {
//		MetaWarehouse warehouse = this.wrhExtlService.findWareHouseById("B0D64D51BF854FC0A64653EA555B53E6");
//		
//		warehouse.setContactAddress("更新后的地址");
//		warehouse.setContactPerson("更新后的联系人");
//		warehouse.setContactPhone("3333333333");
//		warehouse.setEmail("更新后的地址");
//		warehouse.setFax("更新后的fax");
//		warehouse.setWarehouseType(Constant.WAREHOUSE_TYPE_NOTCUSTOMS);
//		MetaWarehouseVO warehouseVO = new MetaWarehouseVO(warehouse);
//		BeanPropertyBindingResult br = super.validateEntity(warehouseVO, ValidUpdate.class);
//		ResultModel rm = this.controller.updateWrh(warehouseVO, br);
//		super.formatResult(rm);
//	}
	
	/**
	 * 测试 - 查询单个仓库信息
	 * @throws Exception
	 * @version 2017年3月11日 下午2:03:13<br/>
	 * @author andy wang<br/>
	 */
	public void testViewWrh () throws Exception {
		ResultModel rm = this.controller.viewWrh("B0D64D51BF854FC0A64653EA555B53E6");
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 查询仓库列表
	 * @throws Exception
	 * @version 2017年3月11日 下午1:36:17<br/>
	 * @author andy wang<br/>
	 */
//	private void testListWrh () throws Exception {
//		MetaWarehouseVO warehouseVO = new MetaWarehouseVO();
//		ResultModel rm = this.controller.listWrh(warehouseVO);
//		super.formatResult(rm);
//	}

	/**
	 * 测试 - 保存仓库
	 * @version 2017年3月11日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
//	private void testInsertWrh () throws Exception {
//		MetaWarehouseVO warehouseVO = new MetaWarehouseVO();
//		MetaWarehouse warehouse = warehouseVO.getWarehouse();
//		warehouse.setWarehouseNo("9123");
//		warehouse.setWarehouseName("311测试仓库1");
//		warehouse.setWarehouseType(Constant.WAREHOUSE_TYPE_CUSTOMS);
//		warehouse.setContactPerson("测试人1");
//		warehouse.setContactAddress("乱来测试地址");
//		warehouse.setContactPhone("38481934");
//		warehouse.setFax("35235235");
//		warehouse.setEmail("34tu2n23nr293.com");
//		warehouse.setLongitude(10d);
//		warehouse.setLatitude(12d);
//		warehouse.setNote("测试备注");
//		warehouse.setWarehouseStatus(32493283);
//		BeanPropertyBindingResult br = super.validateEntity(warehouseVO, ValidSave.class);
//		ResultModel rm = this.controller.insertWrh(warehouseVO, br);
//		super.formatResult(rm);
//	}
}
