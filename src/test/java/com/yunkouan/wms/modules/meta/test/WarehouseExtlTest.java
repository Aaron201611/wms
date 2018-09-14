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

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.wms.common.BaseJunitTest;

/**
 * 测试库位类<br/><br/>
 * @version 2017年2月24日 下午1:22:41<br/>
 * @author andy wang<br/>
 */
public class WarehouseExtlTest extends BaseJunitTest {
	
	
	@Autowired
	private IWarehouseExtlService warehouseExtlService;
	
	/**
	 * 测试方法
	 * @throws Exception
	 * @version 2017年3月1日 下午2:43:07<br/>
	 * @author andy wang<br/>
	 */
	@Test
	public void test () throws Exception {
		try {
//			testFindWarehouseById();
//			testFindWarehouseByName();
			testListWarehouseLikeName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 测试 - 根据仓库名，查询库位
	 * @throws Exception
	 * @version 2017年3月6日 下午4:16:04<br/>
	 * @author andy wang<br/>
	 */
	public void testListWarehouseLikeName () throws Exception {
		List<MetaWarehouse> listWareHouse = this.warehouseExtlService.listWareHouseByName("温", true);
		super.formatResult(listWareHouse);
	}

	/**
	 * 测试 - 根据仓库名，查询库位
	 * @throws Exception
	 * @version 2017年3月6日 下午4:16:04<br/>
	 * @author andy wang<br/>
	 */
	public void testFindWarehouseByName () throws Exception {
		MetaWarehouse wareHouse = this.warehouseExtlService.findWareHouseByName("3");
		super.formatResult(wareHouse);
	}
	
	/**
	 * 测试 - 根据仓库id，查询库位
	 * @throws Exception
	 * @version 2017年3月1日 下午2:46:06<br/>
	 * @author andy wang<br/>
	 */
	public void testFindWarehouseById () throws Exception {
		MetaWarehouse wareHouse = this.warehouseExtlService.findWareHouseById("1");
		super.formatResult(wareHouse);
	}
	
}
