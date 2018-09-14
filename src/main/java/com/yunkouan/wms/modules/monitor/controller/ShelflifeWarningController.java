package com.yunkouan.wms.modules.monitor.controller;

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
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.modules.monitor.entity.WarningHandler;
import com.yunkouan.wms.modules.monitor.service.IShelflifeWarningService;
import com.yunkouan.wms.modules.monitor.vo.ShelflifeWarningVO;

/**
 * 保质期预警控制类
 */
@Controller

@RequestMapping("${adminPath}/shelflifeWarning")
@RequiresPermissions(value = { "shelflifeWarning.view" })
public class ShelflifeWarningController extends BaseController {

	@Autowired
    private IShelflifeWarningService shelflifeWarningService;
  
	/**
	 * 保质期管理列表数据查询<br/><br/>
	 * <b>创建时间:</b><br/>		2017年2月13日14:10:09<br/>
	 * @param shelflifeWarningVO 查询条件
	 * @return 保质期管理单列表、分页数据
	 * @throws Exception
	 * @author 王通
	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list(@RequestBody ShelflifeWarningVO shelflifeWarningVO) throws Exception {
    	Page<ShelflifeWarningVO> list = this.shelflifeWarningService.listByPage(shelflifeWarningVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }

    /**
     * 提交保质期预警处理意见
     */
    @OpLog(model = OpLog.MODEL_MONITOR_EXCEPTION, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/handler", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel handler(@Validated(value = { ValidSave.class }) @RequestBody WarningHandler handler,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
		this.shelflifeWarningService.handler(handler);
		ResultModel rm = new ResultModel();
//		rm.setObj(warningHandler);
        return rm;
    }
}