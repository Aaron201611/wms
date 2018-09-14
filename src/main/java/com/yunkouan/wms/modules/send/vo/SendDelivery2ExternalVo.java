package com.yunkouan.wms.modules.send.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;

import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;



public class SendDelivery2ExternalVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1215592945913407054L;

	@Valid
	private SendDelivery sendDelivery = new SendDelivery();
	
	private String oper;
	
	
	private List<SendDeliveryDetailVo2ExtenalVo> deliveryDetailVoList = new ArrayList<SendDeliveryDetailVo2ExtenalVo>();

	private DeliverGoodsApplicationVo applicationVo;

	public SendDelivery getSendDelivery() {
		return sendDelivery;
	}


	public void setSendDelivery(SendDelivery sendDelivery) {
		this.sendDelivery = sendDelivery;
	}


	public List<SendDeliveryDetailVo2ExtenalVo> getDeliveryDetailVoList() {
		return deliveryDetailVoList;
	}


	public void setDeliveryDetailVoList(List<SendDeliveryDetailVo2ExtenalVo> deliveryDetailVoList) {
		this.deliveryDetailVoList = deliveryDetailVoList;
	}


	public DeliverGoodsApplicationVo getApplicationVo() {
		return applicationVo;
	}


	public void setApplicationVo(DeliverGoodsApplicationVo applicationVo) {
		this.applicationVo = applicationVo;
	}


	public String getOper() {
		return oper;
	}


	public void setOper(String oper) {
		this.oper = oper;
	}
	
	
}
