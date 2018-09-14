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
import com.yunkouan.wms.modules.meta.service.IDockService;
import com.yunkouan.wms.modules.meta.vo.MetaDockVO;

/**
 * 月台控制类
 */
@Controller

@RequestMapping("${adminPath}/dock")
@RequiresPermissions(value = { "dock.view" })
public class DockController extends BaseController {

	/** 库区业务类 <br/> add by andy wang */
	@Autowired
	private IDockService dockService;
	
	
	

	
	/**
	 * 保存并生效
	 * @param metaDockVO 月台对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_DOCK, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MetaDockVO metaDockVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.dockService.addAndEnable(metaDockVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	

	/**
	 * 更新并生效
	 * @param metaWarehouseVO 月台对象
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:14:02<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_META_DOCK, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody MetaDockVO metaDockVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.dockService.updateAndEnable(metaDockVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	
	
	@OpLog(model = OpLog.MODEL_META_DOCK, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel disableDock(@RequestBody List<String> listDockId) throws Exception {
    	this.dockService.inactiveDock(listDockId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

	@OpLog(model = OpLog.MODEL_META_DOCK, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel enableDock( @RequestBody List<String> listDockId ) throws Exception {
    	this.dockService.activeDock(listDockId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
	@OpLog(model = OpLog.MODEL_META_DOCK, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateDock( @Validated(value = { ValidUpdate.class }) @RequestBody MetaDockVO metaDockVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.dockService.updateDock(metaDockVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	
	@RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel viewDock( @RequestBody String dockId ) throws Exception {
        MetaDockVO MetaDockVO = this.dockService.viewDock(dockId);
        ResultModel rm = new ResultModel();
        rm.setObj(MetaDockVO);
        return rm;
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listDock( @RequestBody MetaDockVO metaDockVO ) throws Exception {
    	Page<MetaDockVO> listDock = this.dockService.listDock(metaDockVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listDock);
        return rm;
    }
	
    @OpLog(model = OpLog.MODEL_META_DOCK, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel insertDock( @Validated(value = { ValidSave.class }) @RequestBody MetaDockVO metaDockVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	MetaDockVO dockVO = this.dockService.insertDock(metaDockVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(dockVO);
    	return rm;
    }

}