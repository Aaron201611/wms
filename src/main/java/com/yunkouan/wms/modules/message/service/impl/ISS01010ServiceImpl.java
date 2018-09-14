package com.yunkouan.wms.modules.message.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.message.vo.iss.ISS01010Message;
import com.yunkouan.wms.modules.send.dao.IDeliveryDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.util.JaxbUtil;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

@Service
public class ISS01010ServiceImpl extends AbstractMessageServiceImpl{

	@Autowired
	private IDeliveryService deliveryService;
	
	@Autowired
	private IDeliveryDao deliveryDao;
	
	public void handleData(String xmlStr) {
		ISS01010Message issMessage = JaxbUtil.converyToJavaBean(xmlStr, ISS01010Message.class);
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setMessageId(issMessage.getMessageHead().getMessageId());
		messageVo.getEntity().setMessageType(issMessage.getMessageHead().getMessageType());
		messageVo.getEntity().setFunctionType(issMessage.getMessageHead().getFunctionCode());
		messageVo.getEntity().setOrderNo(issMessage.getMessageBody().getDeclaration().getLogistisId());
		messageVo.getEntity().setSender(Constant.SYS_ISS);
		messageVo.getEntity().setReceiver(Constant.SYS_WMS);
		messageVo.getEntity().setContent(xmlStr);
		messageVo.getEntity().setReceiveTime(new Date());
		messageVo.getEntity().setStatus(issMessage.getMessageBody().getDeclaration().getSortingLineStatus());
		messageVo.getEntity().setUpdateTime(new Date());
		messageVo.getEntity().setCreateTime(new Date());
		
		save(messageVo);
		//保存发货单查验结果
		if(StringUtils.isNotEmpty(issMessage.getMessageBody().getDeclaration().getLogistisId())){
			SendDeliveryVo param = new SendDeliveryVo();
			param.getSendDelivery().setExpressBillNo(issMessage.getMessageBody().getDeclaration().getLogistisId());
			List<SendDeliveryVo> list = deliveryService.qryListByParam(param);
			if(list == null || list.isEmpty()) return;
			SendDelivery entity = list.get(0).getSendDelivery();
			entity.setInspectResult(issMessage.getMessageBody().getDeclaration().getSortingLineStatus());
			deliveryDao.updateByPrimaryKeySelective(entity);
		}
		
	}



	

}
