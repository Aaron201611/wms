package com.yunkouan.wms.modules.meta.vo;

import java.util.List;

import javax.validation.Valid;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.meta.entity.MetaWarehouseSetting;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class MetaWarehouseSettingVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3209947697340112856L;
	@Valid
	private MetaWarehouseSetting entity;
	
	private List<String> warehouseIdList;
	
	private String orderStr = " update_time desc";
	
	public MetaWarehouseSettingVo(){
		
	}
	
	public MetaWarehouseSettingVo(MetaWarehouseSetting entity){
		this.entity = entity;
	}

	public MetaWarehouseSetting getEntity() {
		return entity;
	}

	public void setEntity(MetaWarehouseSetting entity) {
		this.entity = entity;
	}
	
	public List<String> getWarehouseIdList() {
		return warehouseIdList;
	}

	public void setWarehouseIdList(List<String> warehouseIdList) {
		this.warehouseIdList = warehouseIdList;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public Example getExample(){
		Example example = new Example(MetaWarehouseSetting.class);
		example.setOrderByClause(orderStr);
		Criteria c = example.createCriteria();
		if(entity == null) return example;
		if(StringUtils.isNotEmpty(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtils.isNotEmpty(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		if(warehouseIdList != null && !warehouseIdList.isEmpty()){
			c.andIn("warehouseId", warehouseIdList);
		}
		if(entity.getIsSyncStock() != null){
			c.andEqualTo("isSyncStock",entity.getIsSyncStock());
		}
		return example;
	}
	
	public void defaultValue(){
		if(this.entity != null){
			if(this.entity.getIsSyncStock() == null){
				this.entity.setIsSyncStock(false);
			}
			if(this.entity.getOutOfLimitRemind() == null){
				this.entity.setOutOfLimitRemind(false);
			}
			if(this.entity.getManual() == null){
				this.entity.setManual(false);
			}
			
		}
	}
}
