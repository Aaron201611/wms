/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月14日 下午3:08:24<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.sys.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.entity.SysSystemParam;
import com.yunkouan.saas.modules.sys.service.ISysParamExtlService;
import com.yunkouan.saas.modules.sys.vo.SysParamVO;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.sys.controller.SysParamController;

/**
 * <br/><br/>
 * @version 2017年3月14日 下午3:08:24<br/>
 * @author andy wang<br/>
 */
public class SysParamTest extends BaseJunitTest {

	
	@Autowired
	private SysParamController controller;
	
	@Autowired
	private ISysParamExtlService sysParamExtlService;
	
	@Test
	public void test () {
		try {
//			this.testInsertSysParam();
//			this.testListSysParam();
//			this.testViewSysParam();
//			this.testUpdateSysParam();
//			this.testActiveSysParam();
//			this.testInActiveSysParam();
//			this.testInsert();
			this.testShowParam();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 测试 - 获取页面显示参数
	 * @throws Exception
	 * @version 2017年3月22日 下午2:43:43<br/>
	 * @author andy wang<br/>
	 */
	public void testShowParam () throws Exception {
		List<String> listCacheName = new ArrayList<String>();
//		listCacheName.add("ASNDOCTYPE");
//		listCacheName.add("ASNSTATUS");
//		listCacheName.add("ASNDATAFROM");
		listCacheName.add("COMMON_YES_NO");
		ResultModel rm = this.controller.showParam(listCacheName);
		super.toJson(rm,true);
	}
	
	
	/**
	 * 测试 - 失效EEE
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:36<br/>
	 * @author andy wang<br/>
	 */
	public void testInActiveSysParam () throws Exception {
		List<String> listSysParamId = new ArrayList<String>();
		listSysParamId.add("B0D64D51BF854FC0A64653EA555B53E6");
		ResultModel rm = this.controller.disableSysParam(listSysParamId);
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 生效EEE
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:27<br/>
	 * @author andy wang<br/>
	 */
	public void testActiveSysParam () throws Exception {
		List<String> listSysParamId = new ArrayList<String>();
		listSysParamId.add("B0D64D51BF854FC0A64653EA555B53E6");
		ResultModel rm = this.controller.enableSysParam(listSysParamId);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试 - 更新EEE信息
	 * @throws Exception
	 * @version 2017年3月11日 下午2:32:20<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateSysParam () throws Exception {
		SysSystemParam sysParam = this.sysParamExtlService.findSysParamById("");
		
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
		SysParamVO sysParamVO = new SysParamVO(sysParam);
		BeanPropertyBindingResult br = super.validateEntity(sysParamVO, ValidUpdate.class);
		ResultModel rm = this.controller.updateSysParam(sysParamVO, br);
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 查询单个EEE信息
	 * @throws Exception
	 * @version 2017年3月11日 下午2:03:13<br/>
	 * @author andy wang<br/>
	 */
	public void testViewSysParam () throws Exception {
		ResultModel rm = this.controller.viewSysParam("B0D64D51BF854FC0A64653EA555B53E6");
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 查询EEE列表
	 * @throws Exception
	 * @version 2017年3月11日 下午1:36:17<br/>
	 * @author andy wang<br/>
	 */
	private void testListSysParam () throws Exception {
		SysParamVO sysParamVO = new SysParamVO();
		ResultModel rm = this.controller.listSysParam(sysParamVO);
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 保存EEE
	 * @version 2017年3月11日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	private void testInsertSysParam () throws Exception {
		SysParamVO sysParamVO = new SysParamVO();
		SysSystemParam sysParam = sysParamVO.getSysParam();
//		sysParam.setKey("");
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
//		sysParam.set
		BeanPropertyBindingResult br = super.validateEntity(sysParamVO, ValidSave.class);
		ResultModel rm = this.controller.insertSysParam(sysParamVO, br);
		super.formatResult(rm);
	}
	
	private void testInsert () throws Exception {
		SysSystemParam sysParam = new SysSystemParam();
		sysParam.setParamKey("testKey");
		sysParam.setParamValue("testValue");
		sysParam.setParamName("testName");
		this.sysParamExtlService.insertSysParam(sysParam, null, null );
	}
	
	
}
