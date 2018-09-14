package com.yunkouan.wms.modules.send.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.send.service.IPickDetailService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

/**
 * 拣货单控制类
 */
@Controller

@RequestMapping("${adminPath}/pick")
@RequiresPermissions(value = { "pick.view" })
public class PickController extends BaseController{
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IPickService pickService;

	@Autowired
	private IPickDetailService pickDetailService;

	@Autowired
	private StrategyContext context;

	public PickController() {
		System.out.println("===PickController===");
	}

	/**
	 * 查询拣货单列表
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午2:06:16<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryList", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryList(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if(logger.isDebugEnabled()) logger.debug("qryList---------start");

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//查询发货单列表
		sendPickVo.getSendPick().setOrgId(orgId);
		sendPickVo.getSendPick().setWarehouseId(wareHouseId);
		ResultModel rm = new ResultModel();	
		rm = pickService.qryPageList(sendPickVo);
		if(logger.isDebugEnabled()) logger.debug("qryList---------end");
		return rm;
	}
	
	/**
	 * 查询打印列表
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qryPrintList", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryPrintList(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		ResultModel rm = new ResultModel();	
		rm = pickService.qryPrintPageList(sendPickVo,loginUser);
		return rm;
	}
	
	/**
	 * 新增修改拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:39:04<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel addAndUpdate(@Validated(value = { ValidSave.class })@RequestBody  SendPickVo sendPickVo, BindingResult br)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		SendPickVo vo = new SendPickVo();
		//新增拣货单
		if(StringUtils.isEmpty(sendPickVo.getSendPick().getPickId())){
			vo = pickService.createPickAndSave(sendPickVo,null, operator);
		}else{
			vo = pickService.update(sendPickVo, operator);
		}
		SendPickVo record = pickService.view(vo.getSendPick().getPickId());
		ResultModel rm = new ResultModel();
		rm.setObj(record);
		
		return rm;
	}
	
	/**
	 * 新增拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:39:04<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add(@Validated(value = { ValidSave.class })@RequestBody  SendPickVo sendPickVo, BindingResult br)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		SendPickVo vo = new SendPickVo();
		//新增拣货单
		vo = pickService.createPickAndSave(sendPickVo,null, operator);
		SendPickVo record = pickService.view(vo.getSendPick().getPickId());
		ResultModel rm = new ResultModel();
		rm.setObj(record);
		
		return rm;
	}
	
	/**
	 * 修改拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:39:04<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class })@RequestBody  SendPickVo sendPickVo, BindingResult br)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		SendPickVo vo = new SendPickVo();
		//修改拣货单
		vo = pickService.update(sendPickVo, operator);
		SendPickVo record = pickService.view(vo.getSendPick().getPickId());
		ResultModel rm = new ResultModel();
		rm.setObj(record);		
		return rm;
	}
	
	/**
	 * 保存并生效
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年3月15日 上午10:42:07<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable(@Validated(value = { ValidSave.class })@RequestBody  SendPickVo sendPickVo, BindingResult br)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String userId = loginUser.getUserId();
		SendPickVo vo = pickService.saveAndEnable(sendPickVo, userId);
		ResultModel rm = new ResultModel();	
		rm.setObj(vo);
		return rm;
	}
	
	/**
	 * 拣货单查看
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:40:59<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
				
		SendPickVo pickVo = pickService.view(sendPickVo.getSendPick().getPickId());
		ResultModel rm = new ResultModel();	
		rm.setObj(pickVo);
		return rm;
	}
	
	/**
	 * 自动分配库位
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:42:07<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = "自动分配库位", pos = 0)
	@RequestMapping(value = "/autoAllocate", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel autoAllocate(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();
		//自动分配库位
		SendPickVo record = context.getStrategy4Pickup(loginUser.getOrgId(), LoginUtil.getWareHouseId()).allocation_new(sendPickVo, operator);		
		ResultModel rm = new ResultModel();	
		rm.setObj(record);
		return rm;
	}
	
	/**
	 * 生效拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:42:52<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();

		pickService.batchEnable(sendPickVo, operator);

		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 失效拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:44:00<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();
		pickService.diable(sendPickVo.getSendPick().getPickId(), operator);

		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 取消拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:44:34<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();
		pickService.abolish(sendPickVo.getSendPick().getPickId(), operator,true);
		ResultModel rm = new ResultModel();	
		return rm;

	}
	
	/**
	 * 拣货单打印
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:45:24<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/print", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel print(@RequestBody  List<String> pickIds)throws Exception {
		if(pickIds == null || pickIds.size()== 0) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		List<SendPickVo> pickVoList = new ArrayList<SendPickVo>();
		for(String id:pickIds){
			SendPickVo pickVo = pickService.print(id);
			pickVoList.add(pickVo);
		}
		ResultModel rm = new ResultModel();	
		rm.setList(pickVoList);
		return rm;
	}
	
	/**
	 * 拆分拣货单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:45:58<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = "拆分", pos = 0)
	@RequestMapping(value = "/split", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel split(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException("Pick is null");
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();

		pickService.splitPick(sendPickVo, orgId, wareHouseId, operator);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 取消拆分
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:47:34<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = "取消拆分", pos = 0)
	@RequestMapping(value = "/removeSplit", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel removeSplit(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException("Pick is null");

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();
		if(sendPickVo.getSendPick().getParentId() == null)
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		pickService.removeSplit(sendPickVo.getSendPick().getParentId(), operator);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 手持终端拣货单确认
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:48:21<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = "拣货确认", pos = 0)
	@RequestMapping(value = "/pdaConfirm", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel pdaConfirm(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException("Pick is null");
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();
		if(StringUtil.isTrimEmpty(sendPickVo.getSendPick().getOpPerson())){
			sendPickVo.getSendPick().setOpPerson(operator);
		}
		synchronized (this) {
			pickService.confirmPick_pda(sendPickVo, operator);
		}
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 打印拣货单与快递单
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = "打印拣货单", pos = 0)
	@RequestMapping(value = "/printPickAndExpress", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel printPickAndExpress(@RequestBody  List<SendPickVo> pickVoList)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		pickService.printPickAndExpress(pickVoList, loginUser);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 拣货单确认
	 * @param sendPickVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午6:48:21<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PICK, type = "作业确认", pos = 0)
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel confirm(@RequestBody  SendPickVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPick()== null) 
			throw new BizException("Pick is null");
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String operator = loginUser.getUserId();
		if(StringUtil.isTrimEmpty(sendPickVo.getSendPick().getOpPerson())){
			sendPickVo.getSendPick().setOpPerson(operator);
		}
//		pickService.confirmPick(sendPickVo, operator);
		synchronized (this) {
			pickService.completePick(sendPickVo, operator);
		}
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 查询拣货明细
	 * @param sendPickVo
	 * @throws Exception
	 */
	@RequestMapping(value = "/qryDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryDetail(@RequestBody  SendPickDetailVo sendPickVo)throws Exception {
		if(sendPickVo == null || sendPickVo.getSendPickDetail()== null) 
			throw new BizException("PickDetail is null");
		SendPickDetailVo detailVo = pickDetailService.qryDetailAndLocationById(
				sendPickVo.getSendPickDetail().getPickDetailId());
		ResultModel rm = new ResultModel();
		rm.setObj(detailVo);
		return rm;
	}
	

}