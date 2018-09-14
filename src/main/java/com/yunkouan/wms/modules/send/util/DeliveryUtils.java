package com.yunkouan.wms.modules.send.util;

import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDelivery2ExternalVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryDetailVo2ExtenalVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

public class DeliveryUtils {

	public static SendDeliveryVo parse(SendDelivery2ExternalVo externalVo){
		SendDeliveryVo sendDeliveryVo = new SendDeliveryVo();
		sendDeliveryVo.setSendDelivery(externalVo.getSendDelivery());
		
		for(SendDeliveryDetailVo2ExtenalVo detail2Vo:externalVo.getDeliveryDetailVoList()){
			DeliveryDetailVo detailVo = new DeliveryDetailVo();
			detailVo.setDeliveryDetail(detail2Vo.getDeliveryDetail());
			detailVo.setSkuNo(detail2Vo.getSkuNo());
			sendDeliveryVo.getDeliveryDetailVoList().add(detailVo);
		}
		return sendDeliveryVo;
	}
}
