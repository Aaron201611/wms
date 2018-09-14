package com.yunkouan.wms.modules.meta.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.meta.entity.MetaVehicle;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class VehicleVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8791826765647819441L;
	
	private MetaVehicle entity;
	
	private String statusComment;
	
	private String orderBy = "update_time desc";
	
	private Integer statusLessThan;
	
	public VehicleVo(){
		
	}
	
	public  VehicleVo(MetaVehicle entity){
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(MetaVehicle.class);
		example.createCriteria(); 
		example.setOrderByClause(orderBy);
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getCarNo())){
			c.andEqualTo("carNo",entity.getCarNo());
		}
		if(entity.getStatus() != null){
			c.andEqualTo("status",entity.getStatus());
		}
		if(statusLessThan != null){
			c.andLessThan("status", statusLessThan);
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		
		
		return example;
	}

	public MetaVehicle getEntity() {
		return entity;
	}
	

	public String getStatusComment() {
		return statusComment;
	}

	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}
	

	public Integer getStatusLessThan() {
		return statusLessThan;
	}

	public void setStatusLessThan(Integer statusLessThan) {
		this.statusLessThan = statusLessThan;
	}

	public void setEntity(MetaVehicle entity) {
		this.entity = entity;
		if(entity.getStatus() != null){
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			this.statusComment = paramService.getValue(CacheName.VEHICLE_STATUS, entity.getStatus());
		}
	}
	
	
}
