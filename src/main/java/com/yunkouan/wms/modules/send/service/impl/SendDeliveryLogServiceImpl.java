package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.send.dao.IDeliveryLogDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendDeliveryLog;
import com.yunkouan.wms.modules.send.service.ISendDeliveryLogService;
import com.yunkouan.wms.modules.send.vo.SendDeliveryLogVo;

/**
 * 发货单操作明细服务
 */
@Service
public class SendDeliveryLogServiceImpl extends BaseService implements ISendDeliveryLogService{
	
	private Logger log = LoggerFactory.getLogger(SendDeliveryLogServiceImpl.class);
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Autowired
	protected IDeliveryLogDao logDao; 
	
	@Autowired
	protected IUserService userService;
	
	/**
	 * 新增
	 * @param logVo
	 * @return
	 */
	public SendDeliveryLogVo add(SendDeliveryLogVo logVo){
		
		Set<ConstraintViolation<SendDeliveryLog>> vr = validator.validate(logVo.getEntity(), ValidSave.class);
		if(vr.size() > 0) {
			String err = vr.iterator().next().getMessage();
			throw new BizException(err.replaceFirst("\\{", "").replaceFirst("\\}", ""));
		}
		logVo.getEntity().setId(IdUtil.getUUID());
		logDao.insertSelective(logVo.getEntity());
		return logVo;
	}
	
	/**
	 * 查询操作明细列表
	 * @param paramVo
	 * @return
	 */
	public List<SendDeliveryLogVo> qryLogList(SendDeliveryLogVo paramVo) throws Exception{
		
		List<SendDeliveryLog> logList = logDao.selectByExample(paramVo.getExample());
		
		List<SendDeliveryLogVo> logVoList = new ArrayList<SendDeliveryLogVo>();
		FqDataUtils fdu = new FqDataUtils();
		
		for(SendDeliveryLog log:logList){
			SendDeliveryLogVo logVo = new SendDeliveryLogVo();
			logVo.setEntity(log);
			logVo.setOperName(fdu.getUserNameById(userService, log.getOpPerson()));
			logVoList.add(logVo);
		}
		return logVoList;
	}
	
	
	/**
	 * 新增发货单操作明细
	 * @param deliveryId
	 * @param operer
	 * @param logType
	 * @param orgId
	 * @param warehouseId
	 */
	public void addNewDeliveryLog(String deliveryId,String operer,String logType,String orgId,String warehouseId){
		SendDeliveryLogVo logVo = new SendDeliveryLogVo(new SendDeliveryLog());
		logVo.getEntity().setCreatePerson(operer);
		logVo.getEntity().setDeliveryId(deliveryId);
		logVo.getEntity().setLogType(logType);
		logVo.getEntity().setOpPerson(operer);
		logVo.getEntity().setOrgId(orgId);
		logVo.getEntity().setWarehouseId(warehouseId);
		logVo.getEntity().setUpdatePerson(operer);
		
		add(logVo);
	}

}
