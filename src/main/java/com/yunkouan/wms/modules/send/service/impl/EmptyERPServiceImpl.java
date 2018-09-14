package com.yunkouan.wms.modules.send.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IERPService;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

@Service(value="emptyERPServiceImpl")
public class EmptyERPServiceImpl implements IERPService {
	@Autowired
	private IDeliveryService deliveryService;

	@Override
	public ErpResult sendToERP(SendDeliveryVo vo, String remarks,int intercept) throws Exception {
		//成功
		deliveryService.updateSendStatus(vo.getSendDelivery().getDeliveryId(),1);
		ErpResult erpResult = new ErpResult();
		erpResult.setCode(1);
		return erpResult;
	}

//	@Override
//	public String updateLogistics(Map<String, String> paramMap) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public ErpResult doInvoke(String erpInterfaceName, Map<String, String> paramMap) throws Exception {
		ErpResult erpResult = new ErpResult();
		erpResult.setCode(1);
		return erpResult;
	}
}