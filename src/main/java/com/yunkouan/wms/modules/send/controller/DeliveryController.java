package com.yunkouan.wms.modules.send.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.message.service.IMessageHandleService;
import com.yunkouan.wms.modules.message.service.IMessageService;
import com.yunkouan.wms.modules.message.service.impl.IssMessageHandleServiceImpl;
import com.yunkouan.wms.modules.message.util.BusinessServiceFactory;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IExpressService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.service.ISendDeliveryLogService;
import com.yunkouan.wms.modules.send.util.ThreadPoolUtils;
import com.yunkouan.wms.modules.send.vo.SendDeliveryLogVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

/**
 * 发货单控制类
 */
@Controller

@RequestMapping("${adminPath}/delivery")
@RequiresPermissions(value = { "send.view" })
public class DeliveryController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDeliveryService deliveryService;
	@Autowired
	private IPickService pickService;
	@Autowired
	private IDeliverGoodsApplicationService deliverGoodsApplicationService;
	/**context:策略上下文**/
	@Autowired
	protected StrategyContext context;
	@Autowired
	protected IMessageService msmqMessageService;
	
	@Autowired
	protected IDeliverGoodsApplicationService applicationService;
	@Autowired
	private ISendDeliveryLogService logService;

	@Autowired
	private IExpressService expressService;
	
	/**
	 * 检查源单号是否重复
	 * @param sendDeliveryVo
	 * @param br
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkSrcNo", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel checkSrcNo(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		deliveryService.checkSrcNo(sendDeliveryVo.getSendDelivery().getSrcNo(), orgId,null);
		ResultModel rm = new ResultModel();
		return rm;
	}
	/**
	 * 新增修改发货单
	 * @param sdVo
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel addAndUpdate(@Validated(value = { ValidSave.class })@RequestBody SendDeliveryVo sendDeliveryVo, BindingResult br) throws Exception {
		logger.info("addAndUpdate_start");			
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}	
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String userId = loginUser.getUserId();
		SendDeliveryVo vo = new SendDeliveryVo();
		//检查发货明细的skuid与批次号是否重复
		if(sendDeliveryVo.skuIsDuplicate()){
			throw new BizException(BizStatus.SKU_AND_BATCHNO_IS_DUPLIPCATIVE.getReasonPhrase());
		}
		//判断是否存在发货id
		//存在则修改
		if(!StringUtils.isEmpty(sendDeliveryVo.getSendDelivery().getDeliveryId())){			
			vo = deliveryService.update(sendDeliveryVo,orgId,wareHouseId,userId);			
		}
		//不存在则新增
		else{			
			//新建发货单
			vo = deliveryService.createNoAndAdd(sendDeliveryVo,orgId,wareHouseId,userId);
		}
		ResultModel rm = new ResultModel();
		rm.setObj(vo);
		return rm;
	}
	
	/**
	 * 新增发货单
	 * @param sdVo
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add(@Validated(value = { ValidSave.class })@RequestBody SendDeliveryVo sendDeliveryVo, BindingResult br) throws Exception {
		logger.info("add_start");			
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}	
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String userId = loginUser.getUserId();
		SendDeliveryVo vo = new SendDeliveryVo();
		//新建发货单
		vo = deliveryService.createNoAndAdd(sendDeliveryVo,orgId,wareHouseId,userId);
		ResultModel rm = new ResultModel();
		rm.setObj(vo);
		return rm;
	}
	
	/**
	 * 修改发货单
	 * @param sdVo
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class })@RequestBody SendDeliveryVo sendDeliveryVo, BindingResult br) throws Exception {
		logger.info("update_start");			
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}	
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String userId = loginUser.getUserId();
		SendDeliveryVo vo = new SendDeliveryVo();
		//检查发货明细的skuid与批次号是否重复
		if(sendDeliveryVo.skuIsDuplicate()){
			throw new BizException(BizStatus.SKU_AND_BATCHNO_IS_DUPLIPCATIVE.getReasonPhrase());
		}
		vo = deliveryService.update(sendDeliveryVo,orgId,wareHouseId,userId);
		ResultModel rm = new ResultModel();
		rm.setObj(vo);
		return rm;
	}
	

	/**
	 * 保存并生效
	 * @param sendDeliveryVo
	 * @param br
	 * @return
	 * @throws Exception
	 * @version 2017年3月15日 上午10:35:18<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable(@Validated(value = { ValidSave.class })@RequestBody SendDeliveryVo sendDeliveryVo, BindingResult br) throws Exception {
		logger.info("saveAndEnable_start");			
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}	
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String userId = loginUser.getUserId();
		SendDeliveryVo vo = new SendDeliveryVo();
		//保存并生效发货单
		//存在则修改
		vo = deliveryService.saveAndEnable(sendDeliveryVo,orgId,wareHouseId,userId);
		ResultModel rm = new ResultModel();
		rm.setObj(vo);
		return rm;
		
	}
	
	/**
	 * 保存并生效
	 * @param sendDeliveryVo
	 * @param br
	 * @return
	 * @throws Exception
	 * @version 2017年3月15日 上午10:35:18<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnableAndCreatePick", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnableAndCreatePick(@Validated(value = { ValidSave.class })@RequestBody SendDeliveryVo sendDeliveryVo, BindingResult br) throws Exception {
		logger.info("saveAndEnable_start");			
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}	
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String userId = loginUser.getUserId();
		SendDeliveryVo vo = new SendDeliveryVo();
		//保存并生效发货单,自动生成拣货单
		vo = deliveryService.saveAndEnableAndCreatePick(sendDeliveryVo,orgId,wareHouseId,userId);
		ResultModel rm = new ResultModel();
		rm.setObj(vo);
		//推送物流公司
		send2WL(vo);
		
		return rm;
		
	}
	
	/**
	 * 自动创建拣货单
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "自动创建拣货单", pos = 0)
	@RequestMapping(value = "/createPick", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel createPick(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception{
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String userId = loginUser.getUserId();
		deliveryService.createPickAndAssign(sendDeliveryVo.getSendDelivery().getDeliveryId(), userId);
		ResultModel rm = new ResultModel();
		return rm;	
	}
	
	
	/**
	 * 取消发货单
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 上午10:24:25<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception{
		logger.info("DeliveryController.cancel:Start");
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();		
		
		deliveryService.cancelSendDelivery(sendDeliveryVo.getSendDelivery().getDeliveryId(), operator);
		ResultModel rm = new ResultModel();
		return rm;
	}
	
	/**
	 * 生效发货单
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 上午10:45:04<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception{
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();		
		
		deliveryService.enableCheckWave(sendDeliveryVo.getSendDelivery().getDeliveryId(), operator);
		ResultModel rm = new ResultModel();
		return rm;
	}
	
	/**
	 * 生效并自动生成拣货单
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enableAndCreatePick", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enableAndCreatePick(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception{
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();		
		synchronized (this) {
			deliveryService.enableAndCreatePick(sendDeliveryVo.getSendDelivery().getDeliveryId(), operator);
			//查询发货单
			SendDeliveryVo deliveryVo = deliveryService.getDeliveryById(sendDeliveryVo.getSendDelivery().getDeliveryId());
			//将绑定了运单号的发货单推送给快递公司
			send2WL(deliveryVo);
		}
		
		ResultModel rm = new ResultModel();
		return rm;
	}
	
	
	
	/**
	 * 检查拣货单数量
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hasPick", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel hasPick(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception{
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		
		SendPickVo sendPickVo = new SendPickVo();
		sendPickVo.getSendPick().setDeliveryId(sendDeliveryVo.getSendDelivery().getDeliveryId());
		sendPickVo.getSendPick().setPickStatus(Constant.PICK_STATUS_OPEN);
		List<SendPickVo> list = pickService.qryListByParam(sendPickVo);		
		
		ResultModel rm = new ResultModel();
		rm.setObj(list.size());
		return rm;
	} 
	
	/**
	 * 失效
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 下午5:51:17<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception{
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		
		deliveryService.disableSendDelivery(sendDeliveryVo.getSendDelivery().getDeliveryId(),orgId,wareHouseId, operator);
		ResultModel rm = new ResultModel();
		return rm;
	}
	
	
	/**
	 * 打印集配单
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 上午11:00:20<br/>
	 * @author 王通<br/>
	 */
	@RequestMapping(value = "/centralList4Print", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel centralList4Print(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//查询发货单列表
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(wareHouseId);
		ResultModel rm = new ResultModel();	
		rm = deliveryService.centralList4Print(sendDeliveryVo);
		
		return rm;
	}
	/**
	 * 装车确认
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 上午10:46:47<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "装车确认", pos = 0)
	@RequestMapping(value = "/loadConfirm", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel loadConfirm(@RequestBody SendDeliveryVo sendDeliveryVo) throws Exception{
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		
		sendDeliveryVo.getSendDelivery().setUpdatePerson(operator);
		sendDeliveryVo.getSendDelivery().setUpdateTime(new Date());
		deliveryService.loadConfirm(sendDeliveryVo);
		ResultModel rm = new ResultModel();
		return rm;
	}	
		
	/**
	 * 查询发货单列表
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 上午11:00:20<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryPageList", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryPageList(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
		
		ResultModel rm = new ResultModel();	
		rm = deliveryService.qryPageList(sendDeliveryVo);
		
		return rm;
	}
	
	/**
	 * 查询订单列表
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 上午11:00:20<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryOrderPageList", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryOrderPageList(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
		
		
		ResultModel rm = new ResultModel();	
		sendDeliveryVo.setOrderByStr(" order_time desc");
		rm = deliveryService.qryPageList(sendDeliveryVo);
		
		return rm;
	}
	
	/**
	 * tongji 订单列表
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 上午11:00:20<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/totalOrder", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel totalOrder(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
		
		
		ResultModel rm = new ResultModel();	
		rm = deliveryService.qryTotalOrder(sendDeliveryVo);
		
		return rm;
	}
	
	/**
	 * 查询没完成拣货，没关联波次单的发货单列表
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 * @version 2017年3月7日 上午9:39:32<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryListNotFinish", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryListNotFinish(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null) 
			throw new BizException("SendDelivery is null");
		
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		//查询没关联波次单，状态是部分拣货或者部分发运的发货单
		List<SendDeliveryVo> list = deliveryService.qryListNotFinish(orgId, warehouseId);
		ResultModel rm = new ResultModel();	
		rm.setList(list);
		return rm;
	}
	
	/**
	 * 波次单新建查询发货单
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 * @version 2017年3月10日 下午5:03:56<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryListInWave", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryListInWave(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null) 
			throw new BizException("SendDelivery is null");
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
//		String operator = loginUser.getUserId();
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(wareHouseId);
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_OPEN);
		ResultModel rm = new ResultModel();
		if(!StringUtil.isTrimEmpty(sendDeliveryVo.getCarrierName())) return rm;
		//按条件查询没有关联波次单的发货单
		List<SendDeliveryVo> list = deliveryService.qryListInWave(sendDeliveryVo);
		rm.setList(list);
		return rm;
	}
	
	
	
	/**
	 * 拆分发货单
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 下午5:44:55<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "拆分", pos = 0)
	@RequestMapping(value = "/split", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel split(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
				
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		//拆分发货单
		deliveryService.splitDelivery(sendDeliveryVo, orgId, wareHouseId, operator);
		ResultModel rm = new ResultModel();
		return rm;
	}
	
	/**
	 * 查看发货单详情
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 下午6:15:47<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		

		//查看详情
		SendDeliveryVo deliveryVo = deliveryService.view(sendDeliveryVo.getSendDelivery().getDeliveryId());
		
		ResultModel rm = new ResultModel();	
		rm.setObj(deliveryVo);
		
		return rm;
	}
	/**
	 * 根据运单号查看发货单详情
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2018年4月20日15:46:41<br/>
	 * @author 王通<br/>
	 */
	@RequestMapping(value = "/viewByEntity", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel viewByEntity(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
			
		//查看详情
		SendDeliveryVo deliveryVo = deliveryService.viewByEntity(sendDeliveryVo);
		
		ResultModel rm = new ResultModel();	
		rm.setObj(deliveryVo);
		
		return rm;
	}
	
	/**
	 * 根据运单号查看发货单详情，同时获取辅材BOM
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2018年4月20日15:46:41<br/>
	 * @author 王通<br/>
	 */
	@RequestMapping(value = "/viewAndGetBom", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel viewAndGetBom(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
		//查看详情
		SendDeliveryVo deliveryVo = deliveryService.viewByEntity(sendDeliveryVo);
		if (deliveryVo == null) {
			throw new BizException("express_bill_no_is_null");
		}
		//检查状态，必须是全部拣货
		if (deliveryVo.getSendDelivery().getDeliveryStatus().intValue() != Constant.SEND_STATUS_ALLPICK) {
			throw new BizException("cannot_packaged");
		}
		//检查如果是爆款，不允许操作
		if (deliveryVo.getSendDelivery().getDocType().intValue() == Constant.DELIVERY_TYPE_BURST_OUT) {
			throw new BizException("burst_cannot_packaged");
		}
		deliveryVo = deliveryService.getBom(deliveryVo);
		ResultModel rm = new ResultModel();	
		rm.setObj(deliveryVo);
		
		return rm;
	}
	/**
	 * 打印发货单
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 下午6:15:47<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/print", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel print(@RequestBody List<String> ids)throws Exception {
		if(ids == null || ids.size() == 0) 
			throw new BizException("SendDelivery is null");		
			
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		for(String id:ids){
			//查看详情
			SendDeliveryVo deliveryVo = deliveryService.view(id);
			list.add(deliveryVo);
		}
		ResultModel rm = new ResultModel();	
		rm.setList(list);
		return rm;
	}
	
	/**
	 * 更新快递单打印次数
	 * @param deliveryId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/printExpress", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel printExpress(@RequestBody String deliveryId)throws Exception {
		//更新快递单打印次数
		deliveryService.updatePrintExpressTimes(deliveryId);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 查询发货明细的拣货库位
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qryPickLocations", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryPickLocations(@RequestBody SendPickDetailVo sendPickDetailVo)throws Exception {
		if(sendPickDetailVo == null || sendPickDetailVo.getSendPickDetail() == null) 
			throw new BizException("SendDelivery is null");
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();	
		
		String detailId = sendPickDetailVo.getSendPickDetail().getDeliveryDetailId();
		List<SendPickLocationVo> list = deliveryService.qryRealLocationByDetailId(detailId,orgId,wareHouseId);
		
		ResultModel rm = new ResultModel();	
		rm.setList(list);
		return rm;
	}
	
	/**
	 * 检查发货单是否全部拣货
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkAndGet", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel checkAndGet(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");
		
		SendDeliveryVo deliveryVo = deliveryService.checkStatusAndgetDelivery(sendDeliveryVo,Constant.SEND_STATUS_ALLPICK);
		ResultModel rm = new ResultModel();	
		rm.setObj(deliveryVo);
		return rm;
	}
	
	/**
	 * 打包复核
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "打包复核", pos = 0)
	@RequestMapping(value = "/reviewAndPackage", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel reviewAndPackage(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(warehouseId);
		
		deliveryService.reviewAndPackage(sendDeliveryVo, userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	
	
	/**
	 * 打包
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "打包", pos = 0)
	@RequestMapping(value = "/packaged", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel packaged(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(warehouseId);
		
		deliveryService.packaged(sendDeliveryVo, userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	
	/**
	 * 称重复核
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "称重复核", pos = 0)
	@RequestMapping(value = "/checkPackageWeight", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel checkPackageWeight(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException(BizStatus.DATA_IS_NULL.getReasonPhrase());
		ResultModel rm = new ResultModel();	

		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(warehouseId);
		synchronized (this) {
			deliveryService.checkPackageWeight(sendDeliveryVo, loginUser);
		}
	
		return rm;
	}
	
	/**
	 * 发货确认
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "发货确认", pos = 0)
	@RequestMapping(value = "/sendConfirm", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel sendConfirm(@RequestBody String expressBillNo)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		ResultModel rm = new ResultModel();	

		synchronized (this) {
			SendDeliveryVo deliveryVo = deliveryService.confirmSend(expressBillNo,orgId,warehouseId, userId);
			rm.setObj(deliveryVo);
		}

		return rm;
	}
	
	/**
	 * 退货
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "退货", pos = 0)
	@RequestMapping(value = "/chargeBack", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel chargeBack(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		sendDeliveryVo.getSendDelivery().setUpdateTime(new Date());
		
		deliveryService.chargeBack(sendDeliveryVo,userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}

	/**
	 * 查验
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "查验", pos = 0)
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel check(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		sendDeliveryVo.getSendDelivery().setUpdateTime(new Date());
		
		deliveryService.check(sendDeliveryVo,userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}
	/**
	 * 销毁
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_DELIVERY, type = "销毁", pos = 0)
	@RequestMapping(value = "/destory", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel destory(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		sendDeliveryVo.getSendDelivery().setUpdateTime(new Date());
		
		deliveryService.destory(sendDeliveryVo,userId);
		ResultModel rm = new ResultModel();	
		return rm;
	}

	/**
	 * 生成运单号/发货单号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDeliveryNo", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel getDeliveryNo(@RequestBody SendDelivery entity)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String deliveryNo = context.getStrategy4No(loginUser.getOrgId(), LoginUtil.getWareHouseId()).getDeliveryNo(loginUser.getOrgId(), entity.getDocType());
		return new ResultModel().setObj(deliveryNo);
	}
	
	/**
	 * 批量获取运单号,同时批量生效
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchGetExpressNoAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel batchGetExpressNoAndEnable(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//批量获取运单号
		if(sendDeliveryVo.getLoadConfirmIds() == null || sendDeliveryVo.getLoadConfirmIds().isEmpty()){
			return new ResultModel();
		}
		StringBuffer message = new StringBuffer("");
		for(String id:sendDeliveryVo.getLoadConfirmIds()){
			SendDeliveryVo vo = deliveryService.getDeliveryById(id);
			try {
				if(StringUtil.isEmpty(vo.getSendDelivery().getExpressBillNo())){
					String billNo = null;
					//若是ems或申通，则获取运单号
					if(Constant.EXPRESS_SERVICE_EMS.equals(sendDeliveryVo.getSendDelivery().getExpressServiceCode())||
							Constant.EXPRESS_SERVICE_ST.equals(sendDeliveryVo.getSendDelivery().getExpressServiceCode())){
						
						billNo = expressService.getLogisticsNo(sendDeliveryVo.getSendDelivery().getExpressServiceCode());
						if(StringUtil.isEmpty(billNo)){
							message.append(vo.getSendDelivery().getDeliveryNo())
									.append(" 获取运单号失败！")
									.append("\n\r<br />");
							continue;
						}
					}
					//若非ems或申通，则用发货单号
					else{
						billNo = vo.getSendDelivery().getDeliveryNo();
					}
					vo.getSendDelivery().setExpressBillNo(billNo);
				}
				deliveryService.updateEntityAndEnable(vo.getSendDelivery(), loginUser);
			} catch (BizException e) {
				log.error(e.getMessage()+e.getParam()[0]);
				message.append(vo.getSendDelivery().getDeliveryNo())
						.append(e.getMessage()+e.getParam()[0])
						.append("\n\r<br />");
			}
			catch (Exception e) {
				log.error(e.getMessage());
				message.append(vo.getSendDelivery().getDeliveryNo())
						.append(e.getMessage())
						.append("\n\r<br />");
			}
		}
		if(!StringUtil.isEmpty(message)){
			throw new BizException("batchGetExpressNoAndEnable_error",message.toString());
		}
		return new ResultModel();
	}
	
	
	/**
	 * 批量获取运单号,同时批量保存
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/batchGetExpressNoAndSave", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel batchGetExpressNoAndSave(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
//		//获取登录用户
//		Principal loginUser = LoginUtil.getLoginUser();
//		//批量获取运单号
//		if(sendDeliveryVo.getLoadConfirmIds() == null || sendDeliveryVo.getLoadConfirmIds().isEmpty()){
//			return new ResultModel();
//		}
//		StringBuffer message = new StringBuffer("");
//		for(String id:sendDeliveryVo.getLoadConfirmIds()){
//			SendDeliveryVo vo = deliveryService.getDeliveryById(id);
//			try {
//				if(StringUtil.isEmpty(vo.getSendDelivery().getExpressBillNo())){
//					String billNo = null;
//					//若是ems或申通，则获取运单号
//					if(Constant.EXPRESS_SERVICE_EMS.equals(sendDeliveryVo.getSendDelivery().getExpressServiceCode())||
//							Constant.EXPRESS_SERVICE_ST.equals(sendDeliveryVo.getSendDelivery().getExpressServiceCode())){
//						
//						billNo = expressService.getLogisticsNo(sendDeliveryVo.getSendDelivery().getExpressServiceCode());
//						if(StringUtil.isEmpty(billNo)){
//							message.append(vo.getSendDelivery().getDeliveryNo())
//									.append(" 获取运单号失败！")
//									.append("\n\r<br />");
//							continue;
//						}
//					}
//					//若非ems或申通，则用发货单号
//					else{
//						billNo = vo.getSendDelivery().getDeliveryNo();
//					}
//					vo.getSendDelivery().setExpressBillNo(billNo);
//				}
//				deliveryService.updateEntity(vo.getSendDelivery(), loginUser);
//			} catch (Exception e) {
//				log.error(e.getMessage());
//				message.append(vo.getSendDelivery().getDeliveryNo())
//						.append(e.getMessage())
//						.append("\n\r<br />");
//			}
//		}
//		if(!StringUtil.isEmpty(message)){
//			throw new BizException("batchGetExpressNoAndEnable_error",message.toString());
//		}
//		return new ResultModel();
//	}
	
	

	/**
	 * 批量同步出库数据
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/batchSyncOutStockData", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel batchSyncOutStockData(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
//		if(sendDeliveryVo == null) 
//			throw new BizException("data_is_null");		
//		this.deliveryService.bachSyncOutStockData(sendDeliveryVo.getOperIdList());
//		ResultModel rm = new ResultModel();
//		return rm;
//	}
	
    /**
     * 推送出库数据(单条推送)
     * @param putawayId
     * @return
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月7日 下午5:28:38<br/>
     * @author hgx<br/>
     */
    @RequestMapping(value = "/transmitOutStock", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel transmitOutStock( @RequestBody String deliveryId ) throws Exception {
    	ResultModel rm = new ResultModel();
    	this.deliveryService.transmitOutStockXML(deliveryId);
    	return rm;
    }
    
	
	 /**
     * 下载excel文件
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadExcel(String ownerName, 
    		String srcNoLike, String deliveryNoLike, 
    		String deliveryStatus,String beginTime,String endTime) throws Exception {
    	SendDeliveryVo vo = new SendDeliveryVo();
    	SendDelivery sd = new SendDelivery();
    	vo.setSendDelivery(sd);
    	if (deliveryStatus != null) {
        	sd.setDeliveryStatus(Integer.parseInt(deliveryStatus));
    	}
    	vo.setOwnerName(ownerName);
    	vo.setSrcNoLike(srcNoLike);
    	vo.setDeliveryNoLike(deliveryNoLike);
    	vo.setBeginTime(beginTime);
    	vo.setEndTime(endTime);
    	vo.setPageSize(99);
    	return this.deliveryService.downloadExcel(vo);
	}
    
    /**
     * 导出出库统计excel
     * @param sendDeliveryVo
     * @return
     */
    @RequestMapping(value = "/exportSendStatisExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> exportSendStatisExcel(String srcNo,String ownerName,String skuNo,
    		String skuName,String expressBillNo,String skuBarCode,String loctionNo,String beginTime,
    		String endTime)throws Exception {
    	SendPickLocationVo 	pickLocationVo = new SendPickLocationVo();
    	pickLocationVo.setSrcNo(srcNo);
    	pickLocationVo.setOwnerName(ownerName);
    	pickLocationVo.setSkuName(skuName);
    	pickLocationVo.setExpressBillNo(expressBillNo);
    	pickLocationVo.setSkuBarCode(skuBarCode);
    	pickLocationVo.setSkuNo(skuNo);
    	pickLocationVo.setLocationNo(loctionNo);
    	pickLocationVo.setBeginTime(beginTime);
    	pickLocationVo.setEndTime(endTime);
		
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
    	return this.deliveryService.exportSendStaticsExcel(pickLocationVo, p);
    }
    

    /**
     * 导出订单统计
     * @param srcNo
     * @param expressBillNo
     * @param expressServiceCode
     * @param receiver
     * @param beginTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportOrderExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> exportOrderExcel(String srcNo,String expressBillNo,
    		String expressServiceCode,String receiver,String beginTime,String endTime,String interceptStatus,
    		String deliveryNo1,String docType,String deliveryStatus)throws Exception {
    	SendDeliveryVo 	sendDeliveryVo = new SendDeliveryVo();
    	sendDeliveryVo.getSendDelivery().setSrcNo(srcNo);
    	sendDeliveryVo.getSendDelivery().setExpressBillNo(expressBillNo);
    	sendDeliveryVo.getSendDelivery().setExpressServiceCode(expressServiceCode);
    	sendDeliveryVo.getSendDelivery().setReceiver(receiver);
    	if(StringUtils.isNotEmpty(interceptStatus)) sendDeliveryVo.getSendDelivery().setInterceptStatus(Integer.parseInt(interceptStatus));
    	sendDeliveryVo.getSendDelivery().setDeliveryNo1(deliveryNo1);
    	if(StringUtils.isNotEmpty(docType))sendDeliveryVo.getSendDelivery().setDocType(Integer.parseInt(docType));
    	if(StringUtils.isNotEmpty(deliveryStatus)) sendDeliveryVo.getSendDelivery().setDeliveryStatus(Integer.parseInt(deliveryStatus));
    	sendDeliveryVo.setBeginTime(beginTime);
    	sendDeliveryVo.setEndTime(endTime);

		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
		return deliveryService.exportSendOrderExcel(sendDeliveryVo, p);
    }
    
    /**
     * 查询出库统计
     * @param sendDeliveryVo
     * @return
     */
    @RequestMapping(value = "/qrySendStatisList", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel qrySendStatisList(@RequestBody SendPickLocationVo pickLocationVo)throws Exception {
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
//		if(sendDeliveryVo.getLessThanUpdateTime() == null && sendDeliveryVo.getGreaterThanUpdateTime() == null){
//			sendDeliveryVo.setLessThanUpdateTime(new Date());
//			sendDeliveryVo.setGreaterThanUpdateTime(DateUtil.addDays(new Date(), -3));
//		}
    	return this.deliveryService.qryStatiticsPageList_new(pickLocationVo, p, true);
    }
    
    /**
     * 查询出库统计
     * @param sendDeliveryVo
     * @return
     */
    @RequestMapping(value = "/totalSend", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel totalSend(@RequestBody SendPickLocationVo pickLocationVo)throws Exception {
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
    	return this.deliveryService.qryTotalSend(pickLocationVo, p, true);
    }
    
	/**
	 * 生成申请单
	 * @param sendDeliveryVo
	 * @throws Exception
	 * @version 2017年2月15日 下午6:15:47<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/createApplication", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel createApplication(@RequestBody SendDeliveryVo sendDeliveryVo)throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) 
			throw new BizException("SendDelivery is null");		
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();	
		//查询是否有关联的申请单
    	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
    	List<String> statusList = new ArrayList<String>();
    	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
    	applicationVo.setStatusNotIn(statusList);
    	applicationVo.getEntity().setDeliveryId(sendDeliveryVo.getSendDelivery().getDeliveryId());
    	List<DeliverGoodsApplicationVo> qryList = deliverGoodsApplicationService.qryList(applicationVo);
    	if (qryList != null && !qryList.isEmpty()) {
    		throw new BizException("err_application_not_null");
    	}
		//查看详情
		SendDeliveryVo deliveryVo = deliveryService.view(sendDeliveryVo.getSendDelivery().getDeliveryId());
		applicationService.createApplicationFromSend(deliveryVo, p);
		ResultModel rm = new ResultModel();	
		
		return rm;
	}

	/** 
	* @Title: queryLogList 
	* @Description: 查询订单明细列表
	* @auth tphe06
	* @time 2018 2018年4月20日 下午4:29:40
	* @param deliveryVo
	* @return
	* ResultModel
	*/
	@RequestMapping(value = "/queryLogList", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel queryLogList(@RequestBody SendDeliveryVo deliveryVo) throws Exception {
		SendDeliveryLogVo vo = new SendDeliveryLogVo();
		vo.getEntity().setDeliveryId(deliveryVo.getSendDelivery().getDeliveryId());
		List<SendDeliveryLogVo> list = logService.qryLogList(vo);
		return new ResultModel().setList(list);
	}
	
	/**
	 * 获取并保存查验结果
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAndSaveInspectResult", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel getAndSaveInspectResult() throws Exception{
		IMessageHandleService messageHandleService = SpringContextHolder.getBean(IssMessageHandleServiceImpl.class);
		messageHandleService.receiveAndHandle(BusinessServiceFactory.getBusinessService(Constant.ISS01010));
		
		return new ResultModel();
	}
	
	/**
	 * 发送物流公司
	 * @param deliveryVo
	 */
	public void send2WL(SendDeliveryVo deliveryVo){
		
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				//推送物流公司
				try {
					//查询发货单
					List<SendDeliveryVo> sdVoList = new ArrayList<SendDeliveryVo>();
					sdVoList.add(deliveryVo);
					expressService.sendDeliveryToLogistics(ExpressController.filterExpress(sdVoList));
				} catch (Exception e) {
					if(log.isErrorEnabled()) log.error(e.getMessage(), e);
				}
			}
		});
		ThreadPoolUtils.getInstance().addThreadItem(thread);
	}
	
	@RequestMapping(value = "/orderInterception", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel orderInterception(@RequestBody String srcNo) throws Exception {
		Map<String,String>map=deliveryService.cancelToInterface(srcNo, "CD");
		return new ResultModel().setObj(map.get("result"));
	}
}