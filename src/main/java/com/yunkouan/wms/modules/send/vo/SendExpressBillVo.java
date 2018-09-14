package com.yunkouan.wms.modules.send.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.send.entity.SendExpressBill;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendExpressBillVo extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4482882461596806391L;
	
	private SendExpressBill sendExpressBill = new SendExpressBill();
	
	private String orderByClause = "create_time asc";
	
	private Example example;
	
	public SendExpressBillVo(){
		this.example = new Example(SendExpressBill.class);
	}

	public SendExpressBill getSendExpressBill() {
		return sendExpressBill;
	}

	public void setSendExpressBill(SendExpressBill sendExpressBill) {
		this.sendExpressBill = sendExpressBill;
	}
	

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}
	
	public Example getConditionExample(){
		this.example.setOrderByClause(this.orderByClause);
		Criteria c = this.example.createCriteria();
		
		if(sendExpressBill.getExpressBillNo() != null){
			c.andEqualTo("expressBillNo",sendExpressBill.getExpressBillNo());
		}
		if(sendExpressBill.getExpressBillStatus() != null){
			c.andEqualTo("expressBillStatus",sendExpressBill.getExpressBillStatus());
		}
		if(!StringUtil.isTrimEmpty(sendExpressBill.getOrgId())){
			c.andEqualTo("orgId",sendExpressBill.getOrgId());
		}
		if(!StringUtil.isTrimEmpty(sendExpressBill.getWarehouseId())){
			c.andEqualTo("warehouseId",sendExpressBill.getWarehouseId());
		}
		return this.example;
	}
	
	
}
