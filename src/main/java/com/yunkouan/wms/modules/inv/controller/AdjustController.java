package com.yunkouan.wms.modules.inv.controller;

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
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.modules.inv.service.IAdjustService;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;

/**
  * @Description 盈亏调整控制类
  * @author 王通
  * @date 2017年2月13日 下午2:46:04
  *
 */
@Controller

@RequestMapping("${adminPath}/adjust")
@RequiresPermissions(value = { "adjust.view" })
public class AdjustController  extends BaseController {

	@Autowired
    private IAdjustService adjustService;
  
	/**
	 * 盈亏调整列表数据查询<br/><br/>
	 * <b>创建时间:</b><br/>		2017年2月13日14:10:09<br/>
	 * @param adjVO 查询条件
	 * @return 盈亏调整单列表、分页数据
	 * @throws Exception
	 * @author 王通
	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list(@RequestBody InvAdjustVO adjVO) {
    	Page<InvAdjustVO> list = this.adjustService.listByPage(adjVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }

    /**
     * 查看盈亏调整详情
     * @return 
     * @throws Exception 
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel view(@RequestBody String id) throws Exception {
        InvAdjustVO vo = this.adjustService.view(id);
    	ResultModel rm = new ResultModel();
        rm.setObj(vo);
        return rm;
    }

    /**
     * 添加盈亏调整和明细
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ADJUST, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel add(@Validated(value = { ValidSave.class }) @RequestBody InvAdjustVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
        InvAdjustVO adjustVo = this.adjustService.add(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(adjustVo);
        return rm;
    }

    /**
     * 修改盈亏调整和明细
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ADJUST, type = OpLog.OP_TYPE_UPDATE, pos = 0)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel update(@Validated(value = { ValidSave.class }) @RequestBody InvAdjustVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
        InvAdjustVO adjustVo = this.adjustService.update(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(adjustVo);
        return rm;
    }
    /**
     * 生效盈亏调整
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ADJUST, type = OpLog.OP_TYPE_ENABLE, pos = 0)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel enable(@RequestBody List<String> adjustIdList ) throws Exception  {
        this.adjustService.enable(adjustIdList);
        ResultModel rm = new ResultModel();
        return rm;
    }
    /**
     * 添加并更新盘点计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ADJUST, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel saveAndEnable(@Validated(value = { ValidSave.class }) @RequestBody InvAdjustVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvAdjustVO adjustVo = this.adjustService.saveAndEnable(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(adjustVo);
        return rm;
    }

    /**
     * 取消盈亏调整
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ADJUST, type = OpLog.OP_TYPE_CANCEL, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel cancel(@RequestBody List<String> adjustIdList ) throws Exception  {
    	this.adjustService.cancel(adjustIdList);
    	ResultModel rm = new ResultModel();
        return rm;
    }

}