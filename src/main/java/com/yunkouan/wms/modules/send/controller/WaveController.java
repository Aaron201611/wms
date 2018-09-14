package com.yunkouan.wms.modules.send.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.github.pagehelper.StringUtil;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IExpressService;
import com.yunkouan.wms.modules.send.service.IWaveService;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;

/**
 * 波次单控制类
 *@Description TODO
 *@author Aaron
 *@date 2017年3月1日 上午11:11:19
 *version v1.0
 */

@Controller
@RequestMapping("${adminPath}/wave")
@RequiresPermissions(value = { "wave.view" })
public class WaveController extends BaseController{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IWaveService waveService;
	
	@Autowired
	IExpressService expressService;
	
	@Autowired
	IDeliveryService deliveryService;
	
	/**
	 * 分页查询
	 * 
	 * @version 2017年3月1日 上午11:12:22<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryPageList", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel qryPageList(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
        if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendWaveVo.getSendWave().setOrgId(orgId);
		sendWaveVo.getSendWave().setWarehouseId(warehouseId);
        ResultModel rm = new ResultModel();
        if(StringUtil.isNotEmpty(sendWaveVo.getOwnerName())) return rm;
        rm = waveService.qryPageList(sendWaveVo, orgId, warehouseId);
        return rm;
    }
	
	/**
	 * 查询波次单列表
	 * @param sendWaveVo
	 * @return
	 * @throws Exception
	 * @version 2017年3月7日 上午9:19:58<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryNotFinishList", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel qryNotFinishList(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
        if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendWaveVo.getSendWave().setOrgId(orgId);
		sendWaveVo.getSendWave().setWarehouseId(warehouseId);
        List<SendWaveVo>  list = waveService.qryNotFinishList(sendWaveVo);
        ResultModel rm = new ResultModel();	
        rm.setList(list);
        return rm;
    }
	
	/**
	 * 查询发货明细
	 * @param sendWaveVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qryDelDetails", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel qryDelDetails(@RequestBody  SendWaveVo sendWaveVo)throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		List<DeliveryDetailVo> list = waveService.qryDeliveryDetailsByWaveId(sendWaveVo.getSendWave().getWaveId());
		ResultModel rm = new ResultModel();
		rm.setList(list);
		return rm;
	}

	/**
	 * 查看
	 * @param sendWaveVo
	 * @throws Exception
	 * @version 2017年3月1日 上午11:22:53<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel view(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
    	if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendWaveVo.getSendWave().setOrgId(orgId);
		sendWaveVo.getSendWave().setWarehouseId(warehouseId);
        //查看
        SendWaveVo waveVo = waveService.getSendWaveVoById(sendWaveVo.getSendWave().getWaveId());
        ResultModel rm = new ResultModel();
        rm.setObj(waveVo);
        return rm;
    }
	
	/**
	 * 查询波次单的发货单明细
	 * @param sendWaveVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/print", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel print(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
    	if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	//查询发货单明细
    	sendWaveVo = waveService.printInfo(sendWaveVo);
        ResultModel rm = new ResultModel();
        rm.setObj(sendWaveVo);
        return rm;
	}
	

    /**
     * 新增与修改
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:23:47<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndUpdate", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel addAndUpdate(@Validated(value = { ValidSave.class })@RequestBody SendWaveVo sendWaveVo, BindingResult br) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		//判断是否存在waveid，不存在则新增，存在则更新
		SendWaveVo waveVo = new SendWaveVo();
		if(sendWaveVo == null || StringUtils.isEmpty(sendWaveVo.getSendWave().getWaveId())){
			waveVo = waveService.serchAndAdd(sendWaveVo, orgId, warehouseId, operator);
		}else{
			waveVo = waveService.searchAndUpdate(sendWaveVo, orgId, warehouseId, operator);
		}
		ResultModel rm = new ResultModel();
		rm.setObj(waveVo);
		return rm;
    }
	
    /**
     * 新增与修改
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:23:47<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel add(@Validated(value = { ValidSave.class })@RequestBody SendWaveVo sendWaveVo, BindingResult br) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		//新增
		SendWaveVo waveVo = waveService.serchAndAdd(sendWaveVo, orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		rm.setObj(waveVo);
		return rm;
    }
	
    /**
     * 新增与修改
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:23:47<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel update(@Validated(value = { ValidUpdate.class })@RequestBody SendWaveVo sendWaveVo, BindingResult br) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		//更新
		SendWaveVo waveVo = waveService.searchAndUpdate(sendWaveVo, orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		rm.setObj(waveVo);
		return rm;
    }
	
	/**
	 * 保存并生效
	 * @param sendWaveVo
	 * @throws Exception
	 * @version 2017年3月15日 上午11:06:08<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel saveAndEnable(@Validated(value = { ValidSave.class })@RequestBody SendWaveVo sendWaveVo, BindingResult br) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		//判断是否存在waveid，不存在则新增，存在则更新
		SendWaveVo waveVo = waveService.saveAndEnable(sendWaveVo, orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		rm.setObj(waveVo);
		return rm;
    }


    /**
     * 删除发货单
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:24:06<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_DELETE, pos = 0)
	@RequestMapping(value = "/delDelivery", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel delDelivery(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		waveService.delDeliveries(sendWaveVo, orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		return rm;
	}

    /**
     * 生效波次单
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:28:48<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel enable(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
//		waveService.active(sendWaveVo.getSendWave().getWaveId(), orgId, warehouseId, operator);
		waveService.enable(sendWaveVo.getSendWave().getWaveId(), orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		return rm;
    }

    /**
     * 失效波次单
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:29:03<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel disable(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		waveService.disable(sendWaveVo.getSendWave().getWaveId(), orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		return rm;
    }

    /**
     * 取消波次单
     * @param sendWaveVo
     * @throws Exception
     * @version 2017年3月1日 上午11:29:16<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_WAVE, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel cancel(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
		if(sendWaveVo == null)
        	throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		String operator = loginUser.getUserId();
		waveService.abolish(sendWaveVo.getSendWave().getWaveId(), orgId, warehouseId, operator);
		ResultModel rm = new ResultModel();
		return rm;
    }
	
	/**
	 * 统计数量
	 * @param sendWaveVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel count(@RequestBody  SendWaveVo sendWaveVo) throws Exception{
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		sendWaveVo.getSendWave().setOrgId(orgId);
		sendWaveVo.getSendWave().setWarehouseId(warehouseId);
		
		int num = waveService.count(sendWaveVo);
		ResultModel rm = new ResultModel();
		rm.setObj(num);
		return rm;
	}
	
	/**
	 * 批量获取运单号
	 * @param sendWaveVo
	 * @throws Exception
	 * @author ZWB<br/>
	 */
	@RequestMapping(value = "/batchGetBillNo", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel batchGetBillNo(@Validated(value = { ValidSave.class })@RequestBody SendWaveVo sendWaveVo, BindingResult br) throws Exception{
	
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		
		List<SendDeliveryVo> sendDeliberyVoList=sendWaveVo.getSendDeliberyVoList();
		if(sendDeliberyVoList==null||sendDeliberyVoList.size()<0){
			throw new Exception("无发货单");
		}
		//校验存在物流公司，且为同一个快递公司
		Set<String>set=new HashSet<>();
		for(SendDeliveryVo vo:sendDeliberyVoList){
			if(StringUtil.isNotEmpty(vo.getSendDelivery().getExpressServiceCode())){
				set.add(vo.getSendDelivery().getExpressServiceCode());
			}else{
				throw new BizException("Express_code_null");
			}
		}
		if(set.size()>1){
			throw new BizException("Express_code_not_same");
		}
		//循环获取 快递单号
		for(SendDeliveryVo vo:sendDeliberyVoList){
			String billNo = null;
			//若是ems或申通，则获取运单号
			if(Constant.EXPRESS_SERVICE_EMS.equals(vo.getSendDelivery().getExpressServiceCode())||
					Constant.EXPRESS_SERVICE_ST.equals(vo.getSendDelivery().getExpressServiceCode())){
				billNo = expressService.getLogisticsNo(vo.getSendDelivery().getExpressServiceCode());
			}
			//若非ems或申通，则用发货单号
			else{
				billNo = vo.getSendDelivery().getDeliveryNo();
			}
			if(StringUtils.isEmpty(billNo)) throw new BizException("expressbillno_is_null");
			//保存发货单
			SendDelivery entity = new SendDelivery();
			entity.setDeliveryId(vo.getSendDelivery().getDeliveryId());
			entity.setExpressBillNo(billNo);
			deliveryService.updateEntity(entity, loginUser);
			vo.getSendDelivery().setExpressBillNo(billNo);
		}
		ResultModel rm = new ResultModel();
		rm.setObj(sendWaveVo);
		return rm;
    }

}