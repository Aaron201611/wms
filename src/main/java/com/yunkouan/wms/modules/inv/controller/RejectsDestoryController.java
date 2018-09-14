package com.yunkouan.wms.modules.inv.controller;

import java.util.ArrayList;
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
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.modules.inv.service.IRejectsDestoryService;
import com.yunkouan.wms.modules.inv.vo.InvRejectsDestoryVO;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

/**
 * 不良品控制类
 */
@Controller

@RequestMapping("${adminPath}/rejectsDestory")
@RequiresPermissions(value = { "rejectsDestory.view" })
public class RejectsDestoryController  extends BaseController {

    /**
     * 不良品单业务服务类
     * @author 王通<br/>
     */
	@Autowired
    private IRejectsDestoryService rejectsDestoryService;
    
 	/**
 	 * 不良品计划数据列表查询
 	 * @param rejectsDestoryVO
 	 * @return
 	 * @throws Exception
 	 * @Description 
 	 * @version 2017年2月16日 下午1:43:11<br/>
 	 * @author 王通<br/>
 	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody InvRejectsDestoryVO rejectsDestoryVO ) throws Exception {
    	Page<InvRejectsDestoryVO> list = this.rejectsDestoryService.list(rejectsDestoryVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }
    
    /**
     * 查看不良品计划详情
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel view(@RequestBody String id) throws Exception {
    	InvRejectsDestoryVO rejectsDestoryVo = this.rejectsDestoryService.view(id);
    	ResultModel rm = new ResultModel();
        rm.setObj(rejectsDestoryVo);
        return rm;
    }

    /**
     * 添加或更新不良品计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_REJECTS, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel saveOrUpdate(@Validated(value = { ValidSave.class }) @RequestBody InvRejectsDestoryVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvRejectsDestoryVO rejectsDestoryVo = this.rejectsDestoryService.saveOrUpdate(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(rejectsDestoryVo);
        return rm;
    }

    /**
     * 添加并更新不良品计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_REJECTS, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel saveAndEnable(@Validated(value = { ValidSave.class }) @RequestBody InvRejectsDestoryVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvRejectsDestoryVO rejectsDestoryVo = this.rejectsDestoryService.saveAndEnable(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(rejectsDestoryVo);
        return rm;
    }
    
    /**
     * 生效不良品计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_REJECTS, type = OpLog.OP_TYPE_ENABLE, pos = 0)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel enable(@RequestBody InvRejectsDestoryVO vo ) throws Exception  {
    	this.rejectsDestoryService.enable(vo);
    	ResultModel rm = new ResultModel();
        return rm;
    }

    /**
     * 失效不良品计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_REJECTS, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel disable(@RequestBody InvRejectsDestoryVO vo) throws Exception  {
    	 this.rejectsDestoryService.disable(vo);
    	 ResultModel rm = new ResultModel();
         return rm;
    }

    /**
     * 取消不良品计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_REJECTS, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel cancel(@RequestBody InvRejectsDestoryVO vo) throws Exception  {
    	 this.rejectsDestoryService.cancel(vo);
    	 ResultModel rm = new ResultModel();
         return rm;
    }
    /**
     * 申报确认
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_REJECTS, type = "申报确认", pos = 0)
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel confirm(@RequestBody InvRejectsDestoryVO rejectsDestoryVo ) throws Exception  {
    	this.rejectsDestoryService.confirm(rejectsDestoryVo);
    	ResultModel rm = new ResultModel();
        return rm;
    }

    /**
     * 打印不良品单
     * @param putawayId
     * @return
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月7日 下午5:28:38<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/print", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel print(@RequestBody  List<String> ids)throws Exception {
		if(ids == null || ids.size()== 0) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		List<InvRejectsDestoryVO> voList = new ArrayList<InvRejectsDestoryVO>();
		for(String id:ids){
			InvRejectsDestoryVO vo = rejectsDestoryService.print(id);
			voList.add(vo);
		}
		ResultModel rm = new ResultModel();	
		rm.setList(voList);
		return rm;
	}

}