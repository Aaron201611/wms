package com.yunkouan.wms.modules.message.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.message.entity.MsmqMessage;
import com.yunkouan.wms.modules.send.entity.SendDelivery;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class MsmqMessageVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2525035283175109600L;
	
	private MsmqMessage entity = new MsmqMessage();
	
	private String messageType;
	

	public MsmqMessage getEntity() {
		return entity;
	}

	public void setEntity(MsmqMessage entity) {
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(MsmqMessage.class);
		
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getMessageId())){
			c.andEqualTo("messageId", entity.getMessageId());
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId", entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId", entity.getWarehouseId());
		}
		if(StringUtil.isNotBlank(entity.getSender())){
			c.andEqualTo("sender", entity.getSender());
		}
		return example;
	}
	

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	
	
}
