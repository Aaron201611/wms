package com.yunkouan.wms.modules.send.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;

public class SendDeliveryDetailVo2ExtenalVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 620722099443506804L;

	private SendDeliveryDetail deliveryDetail = new SendDeliveryDetail();
	
	private String skuNo;//货品代码
	
	public SendDeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}

	public void setDeliveryDetail(SendDeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	
	
	
}
