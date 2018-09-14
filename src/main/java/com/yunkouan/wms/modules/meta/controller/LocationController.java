/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 下午5:41:27<br/>
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
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.ILocationService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位控制类<br/><br/>
 * @version 2017年3月11日 下午5:41:27<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/location")
@RequiresPermissions(value = { "location.view" })
public class LocationController extends BaseController {
	/** 库位业务类 <br/> add by andy wang */
	@Autowired
	private ILocationService locService;

	/**
	 * 保存并生效
	 * @param metaLocationVO 库位对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MetaLocationVO metaLocationVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.locService.addAndEnable(metaLocationVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 更新并生效
	 * @param metaLocationVO 仓库对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:14:02<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody MetaLocationVO metaLocationVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.locService.updateAndEnable(metaLocationVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 库位库容度重算
	 * @param 库位id集合
	 * @return
	 * @throws Exception
	 * @version 2017年5月15日 下午6:44:27<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = "库容度重算", pos = 0)
	@RequestMapping(value = "/recal", method = RequestMethod.POST)
 	@ResponseBody
	public ResultModel recalCapacity (@RequestBody List<String> listLocId) throws Exception {
		this.locService.recalCapacity(listLocId);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
    /**
     * 失效库位
     * @param listLocId 库位id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月11日 下午2:08:53<br/>
     * @author andy wang<br/>
     */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel disableLoc(@RequestBody List<String> listLocId) throws Exception {
    	this.locService.inactiveLoc(listLocId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

	/**
	 * 生效库位
	 * @param listLocId 库位id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:07:54<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel enableLoc( @RequestBody List<String> listLocId ) throws Exception {
    	this.locService.activeLoc(listLocId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
	/**
	 * 更新库位信息
	 * @param MetaLocationVO 库位信息
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:05:56<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateLoc( @Validated(value = { ValidUpdate.class }) @RequestBody MetaLocationVO metaLocationVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.locService.updateLoc(metaLocationVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 更新库位信息
	 * @param MetaLocationVO 库位信息
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:05:56<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/change", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel changeLoc( @Validated(value = { ValidUpdate.class }) @RequestBody MetaLocationVO metaLocationVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	// 更新仓库
    	this.locService.changeLoc(metaLocationVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}

	/**
	 * 更新库位信息
	 * @param MetaLocationVO 库位信息
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:05:56<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/autoBind", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel autoBind(@RequestBody MetaLocationVO metaLocationVO, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	// 更新仓库
    	this.locService.autoBind();
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 查询单个库位
	 * @param LocId 库位id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午1:56:10<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel viewLoc( @RequestBody String LocId ) throws Exception {
        MetaLocationVO metaLocationVO = this.locService.viewLoc(LocId);
        ResultModel rm = new ResultModel();
        rm.setObj(metaLocationVO);
        return rm;
    }

	/**
	 * 查询库位列表信息
	 * @param MetaLocationVO 查询条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:06:29<br/>
	 * @author andy wang<br/>
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listLoc( @RequestBody MetaLocationVO metaLocationVO ) throws Exception {
    	Page<MetaLocationVO> listLoc = this.locService.listLoc(metaLocationVO, true);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listLoc);
        return rm;
    }
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/listNoTouch", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listNoTouch( @RequestBody MetaLocationVO metaLocationVO ) throws Exception {
    	Page<MetaLocationVO> listLoc = this.locService.listLoc(metaLocationVO, false);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listLoc);
        return rm;
    }

	/**
	 * 保存库位
	 * @param asnVO 库位信息
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 上午9:11:56<br/>
	 * @author andy wang<br/>
	 */
    @OpLog(model = OpLog.MODEL_META_LOCATION, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel insertLoc( @Validated(value = { ValidSave.class }) @RequestBody MetaLocationVO metaLocationVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	MetaLocationVO locationVO = this.locService.insertLoc(metaLocationVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(locationVO);
    	return rm;
    }

	/** 
	* @Title: query 
	* @Description: 查询单条库位信息
	* @auth tphe06
	* @time 2018 2018年8月29日 下午3:02:51
	* @param vo
	* @return
	* @throws Exception
	* ResultModel
	*/
	@RequestMapping(value = "/query", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel query(@RequestBody MetaLocationVO vo) throws Exception {
		Principal p = LoginUtil.getLoginUser();
		vo.getLocation().setOrgId(p.getOrgId());
		vo.getLocation().setWarehouseId(LoginUtil.getWareHouseId());
    	vo = locService.findLoc(vo);
        return new ResultModel().setObj(vo);
    }

}