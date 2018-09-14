/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 下午3:02:59<br/>
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
import com.yunkouan.exception.BizException;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.modules.meta.service.IAreaService;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;

/**
 * 库区控制类<br/><br/>
 * @version 2017年3月11日 下午3:02:59<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/area")
@RequiresPermissions(value = { "area.view" })
public class AreaController extends BaseController {
	
	/** 库区业务类 <br/> add by andy wang */
	@Autowired
	private IAreaService areaService;
	
	/**
	 * 保存并生效仓库
	 * @param metaWarehouseVO 仓库对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MetaAreaVO metaAreaVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.areaService.addAndEnable(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	

	/**
	 * 更新并返回
	 * @param metaWarehouseVO 仓库对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:14:02<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody MetaAreaVO metaAreaVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.areaService.updateAndEnable(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 调整
	 * @param metaWarehouseVO 仓库对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:14:02<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/change", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel change( @Validated(value = { ValidUpdate.class }) @RequestBody MetaAreaVO metaAreaVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.areaService.change(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}

	/**
	 * 查询库区列表
	 * @param metaAreaVO 查询条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:27:19<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/show", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel showArea( @RequestBody MetaAreaVO metaAreaVO ) throws Exception {
    	List<MetaAreaVO> listArea = this.areaService.showArea(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	rm.setList(listArea);
        return rm;
    }
	
	
    /**
     * 失效库区
     * @param listAreaId 库区id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月11日 下午3:28:18<br/>
     * @author andy wang<br/>
     */
	@OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel disableArea(@RequestBody List<String> listAreaId) throws Exception {
    	this.areaService.inactiveArea(listAreaId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

	
	/**
	 * 生效库区
	 * @param listAreaId 库区id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:28:01<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel enableArea( @RequestBody List<String> listAreaId ) throws Exception {
    	this.areaService.activeArea(listAreaId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
	/**
	 * 更新库区信息
	 * @param metaAreaVO 库区对象
	 * @param br 校验信息
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:27:46<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateArea( @Validated(value = { ValidUpdate.class }) @RequestBody MetaAreaVO metaAreaVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.checkSave(metaAreaVO);
    	this.areaService.updateArea(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 查询单个库区
	 * @param areaId 库区id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:27:33<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel viewArea( @RequestBody String areaId ) throws Exception {
        MetaAreaVO MetaAreaVO = this.areaService.viewArea(areaId);
        ResultModel rm = new ResultModel();
        rm.setObj(MetaAreaVO);
        return rm;
    }

	/**
	 * 查询库区列表
	 * @param metaAreaVO 查询条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:27:19<br/>
	 * @author andy wang<br/>
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listArea( @RequestBody MetaAreaVO metaAreaVO ) throws Exception {
    	Page<MetaAreaVO> listArea = this.areaService.listArea(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listArea);
        return rm;
    }
	
	
	/**
	 * 保存库区
	 * @param metaAreaVO 库区对象 
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:26:43<br/>
	 * @author andy wang<br/>
	 */
    @OpLog(model = OpLog.MODEL_META_AREA, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel insertArea( @Validated(value = { ValidSave.class }) @RequestBody MetaAreaVO metaAreaVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.checkSave(metaAreaVO);
    	MetaAreaVO areaVO = this.areaService.insertArea(metaAreaVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(areaVO);
    	return rm;
    }
    
    
    /**
     * 入库时，进行校验库区字段
     * @param metaAreaVO 库区对象
     * @throws Exception
     * @version 2017年5月12日 下午5:50:49<br/>
     * @author andy wang<br/>
     */
    private void checkSave ( MetaAreaVO metaAreaVO ) throws Exception {
    	Double min = 0d;
    	Double max = 0d;
    	if ( metaAreaVO.getArea().getMinTemperature() != null ) {
    		min = metaAreaVO.getArea().getMinTemperature();
    	}
    	if ( metaAreaVO.getArea().getMaxTemperature() != null ) {
    		max = metaAreaVO.getArea().getMaxTemperature();
    	}
    	if ( min > max ) {
    		throw new BizException("err_main_area_save_overtemperature");
    	}
    }
	
}