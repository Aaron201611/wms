/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 上午9:11:03<br/>
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
import com.yunkouan.saas.modules.sys.service.IWarehouseService;
import com.yunkouan.saas.modules.sys.vo.MetaWarehouseVO;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;

/**
 * 仓库控制类<br/><br/>
 * @version 2017年3月11日 上午9:11:03<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/wrh")
@RequiresPermissions(value = { "warehouse.view" })
public class WarehouseController extends BaseController {

	/** 仓库业务类 <br/> add by andy wang */
	@Autowired
	private IWarehouseService wrhService;

	
	/**
	 * 保存并生效
	 * @param metaWarehouseVO 仓库对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
//	@OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE, type = OpLog.OP_TYPE_ADD, pos = 0)
//	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MetaWarehouseVO metaWarehouseVO 
//    		, BindingResult br ) throws Exception {
//    	if ( br.hasErrors() ) {
//			return super.handleValid(br);
//		}
//    	Principal p = LoginUtil.getLoginUser();
//    	this.wrhService.addAndEnable(metaWarehouseVO, p.getUserId(), p.getOrgId());
//    	ResultModel rm = new ResultModel();
//    	return rm;
//	}
	

	/**
	 * 更新并生效
	 * @param metaWarehouseVO 仓库对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:14:02<br/>
	 * @author andy wang<br/>
	 */
//	@OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE, type = OpLog.OP_TYPE_UPDATE, pos = 0)
//	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody MetaWarehouseVO metaWarehouseVO 
//    		, BindingResult br ) throws Exception {
//    	if ( br.hasErrors() ) {
//			return super.handleValid(br);
//		}
//    	Principal p = LoginUtil.getLoginUser();
//    	this.wrhService.updateAndEnable(metaWarehouseVO, p.getUserId(), p.getOrgId());
//    	ResultModel rm = new ResultModel();
//    	return rm;
//	}
	
	
	
    /**
     * 失效仓库
     * @param listWrhId 仓库id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月11日 下午2:08:53<br/>
     * @author andy wang<br/>
     */
//	@OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE, type = OpLog.OP_TYPE_DISABLE, pos = 0)
//	@RequestMapping(value = "/disable", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel disableWrh(@RequestBody List<String> listWrhId) throws Exception {
//		Principal p = LoginUtil.getLoginUser();
//    	this.wrhService.inactiveWrh(listWrhId, p.getUserId(), p.getOrgId());
//    	ResultModel rm = new ResultModel();
//    	return rm;
//    }

	
	/**
	 * 生效仓库
	 * @param listWrhId 仓库id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:07:54<br/>
	 * @author andy wang<br/>
	 */
//	@OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE, type = OpLog.OP_TYPE_ENABLE, pos = 0)
//	@RequestMapping(value = "/enable", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel enableWrh( @RequestBody List<String> listWrhId ) throws Exception {
//		Principal p = LoginUtil.getLoginUser();
//    	this.wrhService.activeWrh(listWrhId, p.getUserId(), p.getOrgId());
//    	ResultModel rm = new ResultModel();
//    	return rm;
//    }
    
	/**
	 * 更新仓库信息
	 * @param metaWarehouseVO 仓库信息
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:05:56<br/>
	 * @author andy wang<br/>
	 */
//	@OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE, type = OpLog.OP_TYPE_UPDATE, pos = 0)
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel updateWrh( @Validated(value = { ValidUpdate.class }) @RequestBody MetaWarehouseVO metaWarehouseVO 
//    		, BindingResult br ) throws Exception {
//    	if ( br.hasErrors() ) {
//			return super.handleValid(br);
//		}
//    	Principal p = LoginUtil.getLoginUser();
//    	this.wrhService.updateWrh(metaWarehouseVO, p.getUserId(), p.getOrgId());
//    	ResultModel rm = new ResultModel();
//    	return rm;
//	}
	
	/**
	 * 查询单个仓库
	 * @param wrhId 仓库id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午1:56:10<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel viewWrh( @RequestBody String wrhId ) throws Exception {
        MetaWarehouseVO metaWarehouseVO = this.wrhService.viewWrh(wrhId);
        ResultModel rm = new ResultModel();
        rm.setObj(metaWarehouseVO);
        return rm;
    }
	@RequestMapping(value = "/viewFirstWrh", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel viewFirstWrh() throws Exception {
		Principal p = LoginUtil.getLoginUser();
        MetaWarehouseVO metaWarehouseVO = this.wrhService.viewFirstWrh(p.getOrgId());
        ResultModel rm = new ResultModel();
        rm.setObj(metaWarehouseVO);
        return rm;
    }
	
	
	/**
	 * 查询仓库列表信息
	 * @param metaWarehouseVO 查询条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 下午2:06:29<br/>
	 * @author andy wang<br/>
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listWrh( @RequestBody MetaWarehouseVO metaWarehouseVO ) throws Exception {
    	Principal p = LoginUtil.getLoginUser();
    	Page<MetaWarehouseVO> listWrh = this.wrhService.listWrh(metaWarehouseVO, p.getOrgId());
    	ResultModel rm = new ResultModel();
    	rm.setPage(listWrh);
        return rm;
    }
	
	
	/**
	 * 保存仓库
	 * @param asnVO 仓库信息
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月11日 上午9:11:56<br/>
	 * @author andy wang<br/>
	 */
//    @OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE, type = OpLog.OP_TYPE_ADD, pos = 0)
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel insertWrh( @Validated(value = { ValidSave.class }) @RequestBody MetaWarehouseVO metaWarehouseVO 
//    		, BindingResult br ) throws Exception {
//    	if ( br.hasErrors() ) {
//			return super.handleValid(br);
//		}
//    	Principal p = LoginUtil.getLoginUser();
//    	MetaWarehouseVO warehouseVO = this.wrhService.insertWrh(metaWarehouseVO, p.getUserId(), p.getOrgId());
//    	ResultModel rm = new ResultModel();
//    	rm.setObj(warehouseVO);
//    	return rm;
//    }
    

	/**
	 * 查询页面仓库列表信息
	 * @param metaWarehouseVO 长训条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年4月4日 上午9:53:43<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/show", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel showWrh( @RequestBody MetaWarehouseVO metaWarehouseVO ) throws Exception {
		Principal p = LoginUtil.getLoginUser();
    	List<MetaWarehouseVO> listWrh = this.wrhService.showWrh(metaWarehouseVO, p.getOrgId());
    	ResultModel rm = new ResultModel();
    	rm.setList(listWrh);
        return rm;
    }
	

	/**
	 * 查询当前登录仓库
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年4月4日 上午9:53:43<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/loginWrh", method = RequestMethod.POST)
 	@ResponseBody
	public ResultModel getLoginWrh() throws Exception {
    	ResultModel rm = new ResultModel();
    	MetaWarehouseVO warehouse = this.wrhService.viewWrh(LoginUtil.getWareHouseId());
    	rm.setObj(warehouse);
        return rm;
    }
	
}