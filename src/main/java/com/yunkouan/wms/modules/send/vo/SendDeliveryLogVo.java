package com.yunkouan.wms.modules.send.vo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.send.entity.SendDeliveryLog;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendDeliveryLogVo extends BaseVO {
	private static final long serialVersionUID = 2273119468171015980L;

	private SendDeliveryLog entity;
	
	private String operName;

	private String orderByStr = " create_time desc,delivery_id desc";
	
	public SendDeliveryLogVo(SendDeliveryLog entity){
		this.entity = entity;
	}
	
	public SendDeliveryLogVo(){
		this.entity = new SendDeliveryLog();
	}

	public SendDeliveryLog getEntity() {
		return entity;
	}

	public void setEntity(SendDeliveryLog entity) {
		this.entity = entity;
	}
	
	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public Example getExample(){
		Example example = new Example(SendDeliveryLog.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		if(entity == null) return example;
		if(StringUtils.isNotEmpty(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtils.isNotEmpty(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		if(StringUtils.isNotEmpty(entity.getDeliveryId())){
			c.andEqualTo("deliveryId",entity.getDeliveryId());
		}
		if(StringUtils.isNotEmpty(entity.getLogType())){
			c.andLike("logType",StringUtil.likeEscapeH(entity.getLogType()));
		}
		return example;
	}
}