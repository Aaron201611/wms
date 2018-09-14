package com.yunkouan.wms.modules.application.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationForm;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsApplicationFormVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7283757224244328177L;
	
	private DeliverGoodsApplicationForm entity;
	
	private String statusName;
	
	private String orderByStr = "update_time desc";
	
	private Integer statusLessThan;
	
	private String gpIEFlagName;
	
	private List<String> statusNotIn;
	
	public DeliverGoodsApplicationFormVo(){
		
	}
	
	public DeliverGoodsApplicationFormVo(DeliverGoodsApplicationForm entity){
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsApplicationForm.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getApplicationFormNo())){
			c.andEqualTo("applicationFormNo",entity.getApplicationFormNo());
		}
		if(StringUtil.isNotBlank(entity.getGpIEFlag())){
			c.andEqualTo("gpIEFlag",entity.getGpIEFlag());
		}
		if(entity.getStatus() != null){
			c.andEqualTo("status",entity.getStatus());
		}
		if(statusLessThan != null){
			c.andLessThan("status", statusLessThan);
		}
		if(statusNotIn != null){
			c.andNotIn("status", statusNotIn);
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		
		return example;
	}
	

	public DeliverGoodsApplicationForm getEntity() {
		return entity;
	}

	public void setEntity(DeliverGoodsApplicationForm entity) {
		this.entity = entity;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public Integer getStatusLessThan() {
		return statusLessThan;
	}

	public void setStatusLessThan(Integer statusLessThan) {
		this.statusLessThan = statusLessThan;
	}

	public String getGpIEFlagName() {
		return gpIEFlagName;
	}

	public void setGpIEFlagName(String gpIEFlagName) {
		this.gpIEFlagName = gpIEFlagName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<String> getStatusNotIn() {
		return statusNotIn;
	}

	public void setStatusNotIn(List<String> statusNotIn) {
		this.statusNotIn = statusNotIn;
	}

	
}
