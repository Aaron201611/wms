package com.yunkouan.wms.modules.ts.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.ts.service.ITaskService;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

/**
 * 拣货单控制类
 */
@Controller

@RequestMapping("${adminPath}/task")
@RequiresPermissions(value = { "task.view" })
public class TaskController extends BaseController{
	private static Log log = LogFactory.getLog(TaskController.class);
	
	@Autowired
	private ITaskService taskService;
	
	/**
	 * 批量任务分配
	 * @param tsTaskVo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchAllocate", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel batchAllocate(@RequestBody  TsTaskVo tsTaskVo, HttpServletRequest req)throws Exception{
		
		taskService.batchAllocate(Constant.TASK_TYPE_PICK, tsTaskVo.getOpIdList(), tsTaskVo.getOperer());
		
		ResultModel rm = new ResultModel();	
		
		return rm;
	}
	
	/**
	 * 查询任务列表
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午2:06:16<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel list(@RequestBody  TsTaskVo tsTaskVo, HttpServletRequest req)throws Exception {
		if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
		if(tsTaskVo == null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String userId = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//查询发货单列表
		tsTaskVo.getTsTask().setOpPerson(userId);
		tsTaskVo.getTsTask().setOrgId(orgId);
		tsTaskVo.getTsTask().setWarehouseId(wareHouseId);
		ResultModel rm = taskService.list(tsTaskVo);
//		if(log.isDebugEnabled()) log.debug("====任务列表返回数据："+JsonUtil.toJson(rm));
		return rm;
	}
	
	/**
	 * 查询任务数量
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午2:06:16<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel count(@RequestBody  TsTaskVo tsTaskVo, HttpServletRequest req)throws Exception {
		if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
		if(tsTaskVo == null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String user = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//查询发货单列表
		tsTaskVo.getTsTask().setOpPerson(user);
		tsTaskVo.getTsTask().setOrgId(orgId);
		tsTaskVo.getTsTask().setWarehouseId(wareHouseId);
		tsTaskVo.setStatusList(Arrays.asList(Constant.TASK_STATUS_OPEN,Constant.TASK_STATUS_EXEC));
		Map<Integer,Integer> obj = taskService.count(tsTaskVo);
		ResultModel rm = new ResultModel();	
		rm.setObj(obj);
		rm.put("date", DateUtil.now());
//		if(log.isDebugEnabled()) log.debug("====任务数量返回数据："+JsonUtil.toJson(rm));
		return rm;
	}

	/**
	 * 执行任务
	 * @param tsTaskVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exec", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel exec(@RequestBody  TsTaskVo tsTaskVo)throws Exception {
		if(tsTaskVo == null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String userId = loginUser.getUserId();
		
		taskService.exec(tsTaskVo.getTsTask().getTaskId(), userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 拒绝任务
	 * @param tsTaskVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody  TsTaskVo tsTaskVo)throws Exception {
		if(tsTaskVo == null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String userId = loginUser.getUserId();
		
		taskService.cancel(tsTaskVo.getTsTask().getTaskId(), userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	

	
}