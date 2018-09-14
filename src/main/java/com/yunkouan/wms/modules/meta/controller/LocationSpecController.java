/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:02:47<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.modules.meta.service.ILocationSpecService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationSpecVO;

/**
 * 库位规格控制类<br/><br/>
 * @version 2017年3月13日下午2:02:47<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/locSpec")
@RequiresPermissions(value = { "locationSpec.view" })
public class LocationSpecController extends BaseController {

	/** 库位规格业务类 <br/> add by andy wang */
	@Autowired
	private ILocationSpecService locSpecService;

	
	

	/**
	 * 保存并生效
	 * @param metaSpecVO 库位规格对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION_SPEC, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MetaLocationSpecVO metaSpecVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.locSpecService.addAndEnable(metaSpecVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	

	/**
	 * 更新并生效
	 * @param metaWarehouseVO 库位规格对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:14:02<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION_SPEC, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody MetaLocationSpecVO metaSpecVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.locSpecService.updateAndEnable(metaSpecVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	

	/**
	 * 页面显示库位规格
	 * @param metaLocSpecVO 查询条件
	 * @return
	 * @throws Exception
	 * @version 2017年4月4日 下午2:26:17<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/show", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel showLocSpec(@RequestBody MetaLocationSpecVO metaLocSpecVO) throws Exception {
		List<MetaLocationSpecVO> listLocationSpec = this.locSpecService.showLocSpec(metaLocSpecVO);
		ResultModel rm = new ResultModel();
		rm.setList(listLocationSpec);
		return rm;
	}
	
	
	/**
	 * 失效库位规格
	 * @param listLocSpecId 库位规格id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION_SPEC, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disableLocSpec(@RequestBody List<String> listLocSpecId) throws Exception {
		this.locSpecService.inactiveLocSpec(listLocSpecId);
		ResultModel rm = new ResultModel();
		return rm;
	}

	/**
	 * 生效库位规格
	 * @param listLocSpecId 库位规格id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION_SPEC, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enableLocSpec(@RequestBody List<String> listLocSpecId) throws Exception {
		this.locSpecService.activeLocSpec(listLocSpecId);
		ResultModel rm = new ResultModel();
		return rm;
	}

	/**
	 * 更新库位规格信息
	 * @param metaLocSpecVO 库位规格对象
	 * @param br 校验信息
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION_SPEC, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel updateLocSpec(@Validated(value = { ValidUpdate.class }) @RequestBody MetaLocationSpecVO metaLocSpecVO,
			BindingResult br) throws Exception {
		if (br.hasErrors()) {
			return super.handleValid(br);
		}
		this.locSpecService.updateLocSpec(metaLocSpecVO);
		ResultModel rm = new ResultModel();
		return rm;
	}

	/**
	 * 查询单个库位规格
	 * @param locSpecId 库位规格id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel viewLocSpec(@RequestBody String locSpecId) throws Exception {
		MetaLocationSpecVO MetaLocationSpecVO = this.locSpecService.viewLocSpec(locSpecId);
		ResultModel rm = new ResultModel();
		rm.setObj(MetaLocationSpecVO);
		return rm;
	}

	/**
	 * 查询库位规格列表
	 * @param metaLocSpecVO 查询条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel listLocSpec(@RequestBody MetaLocationSpecVO metaLocSpecVO) throws Exception {
		Page<MetaLocationSpecVO> listLocationSpec = this.locSpecService.listLocSpec(metaLocSpecVO);
		ResultModel rm = new ResultModel();
		rm.setPage(listLocationSpec);
		return rm;
	}

	/**
	 * 保存库位规格
	 * @param metaLocSpecVO 库位规格对象 
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION_SPEC, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel insertLocSpec(@Validated(value = { ValidSave.class }) @RequestBody MetaLocationSpecVO metaLocSpecVO,
			BindingResult br) throws Exception {
		if (br.hasErrors()) {
			return super.handleValid(br);
		}
		MetaLocationSpecVO locSpecVO = this.locSpecService.insertLocSpec(metaLocSpecVO);
		ResultModel rm = new ResultModel();
		rm.setObj(locSpecVO);
		return rm;
	}

}