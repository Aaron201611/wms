package com.yunkouan.wms.modules.monitor.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.monitor.vo.ExceptionLogVO;

/**
 * 异常处理控制类
 */
@Controller

@RequestMapping("${adminPath}/exceptionLog")
@RequiresPermissions(value = { "exceptionLog.view" })
public class ExceptionLogController extends BaseController {

	@Autowired
    private IExceptionLogService exceptionLogService;
  
	/**
	 * 异常管理列表数据查询<br/><br/>
	 * <b>创建时间:</b><br/>		2017年2月13日14:10:09<br/>
	 * @param exceptionLogVO 查询条件
	 * @return 异常管理单列表、分页数据
	 * @throws Exception
	 * @author 王通
	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list(@RequestBody ExceptionLogVO exceptionLogVO) {
    	Page<ExceptionLogVO> list = this.exceptionLogService.listByPage(exceptionLogVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }

    /**
     * 查看异常管理详情
     * @return 
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel view(@RequestBody String id) {
    	ExceptionLogVO vo = this.exceptionLogService.view(id);
    	ResultModel rm = new ResultModel();
        rm.setObj(vo);
        return rm;
    }

    /**
     * 添加异常管理和明细
     */
    @OpLog(model = OpLog.MODEL_MONITOR_EXCEPTION, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel add(@Validated(value = { ValidSave.class }) @RequestBody ExceptionLog log,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
		ExceptionLog exceptionLog = this.exceptionLogService.add(log);
		ResultModel rm = new ResultModel();
		rm.setObj(exceptionLog);
        return rm;
    }

    /**
     * 修改异常/处理异常
     */
    @OpLog(model = OpLog.MODEL_MONITOR_EXCEPTION, type = OpLog.OP_TYPE_UPDATE, pos = 0)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody ExceptionLog log,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	ExceptionLog exceptionLog = this.exceptionLogService.update(log);
    	ResultModel rm = new ResultModel();
        rm.setObj(exceptionLog);
        return rm;
    }

    /**
     * 取消异常
     */
    @OpLog(model = OpLog.MODEL_MONITOR_EXCEPTION, type = OpLog.OP_TYPE_CANCEL, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel cancel(@RequestBody List<String> exceptionLogIdList ) throws Exception  {
    	this.exceptionLogService.cancel(exceptionLogIdList);
    	ResultModel rm = new ResultModel();
        return rm;
    }

}