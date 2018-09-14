package com.yunkouan.wms.modules.application.vo;

import java.util.ArrayList;
import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGood;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsApplicationGoodVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4077026885545003327L;

	private DeliverGoodsApplicationGood entity;
	
	private String statusName;
	
	private String originCountryName;
	private String dutyModeName;
	private String useToName;
	
	private String orderByStr = "update_time desc";
	
	private List<DeliverGoodsApplicationGoodsSkuVo> applicationGoodSkuVoList = new ArrayList<DeliverGoodsApplicationGoodsSkuVo>();
	
	private List<String> goodsIds;
	
	public DeliverGoodsApplicationGoodVo(){
		
	}
	
	public DeliverGoodsApplicationGoodVo(DeliverGoodsApplicationGood entity){
		this.entity = entity;
	}

	public DeliverGoodsApplicationGood getEntity() {
		return entity;
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsApplicationGood.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
//		if(StringUtil.isNotBlank(entity.getApplicationNo())){
//			c.andEqualTo("applicationNo",entity.getApplicationNo());
//		}
		if(StringUtil.isNotBlank(entity.getApplicationId())){
			c.andEqualTo("applicationId",entity.getApplicationId());
		}
//		if(entity.getStatus() != null){
//			c.andEqualTo("status",entity.getStatus());
//		}
//		if(statusLessThan != null){
//			c.andLessThan("status", statusLessThan);
//		}
		if(goodsIds != null && !goodsIds.isEmpty()){
			c.andIn("id", goodsIds);
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		
		return example;
	}

	public void setEntity(DeliverGoodsApplicationGood entity) {
		this.entity = entity;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public List<DeliverGoodsApplicationGoodsSkuVo> getApplicationGoodSkuVoList() {
		return applicationGoodSkuVoList;
	}

	public void setApplicationGoodSkuVoList(List<DeliverGoodsApplicationGoodsSkuVo> applicationGoodSkuVoList) {
		this.applicationGoodSkuVoList = applicationGoodSkuVoList;
	}

	public List<String> getGoodsIds() {
		return goodsIds;
	}

	public void setGoodsIds(List<String> goodsIds) {
		this.goodsIds = goodsIds;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getOriginCountryName() {
		return originCountryName;
	}

	public void setOriginCountryName(String originCountryName) {
		this.originCountryName = originCountryName;
	}

	public String getUseToName() {
		return useToName;
	}

	public void setUseToName(String useToName) {
		this.useToName = useToName;
	}

	public String getDutyModeName() {
		return dutyModeName;
	}

	public void setDutyModeName(String dutyModeName) {
		this.dutyModeName = dutyModeName;
	}
	
	
}
