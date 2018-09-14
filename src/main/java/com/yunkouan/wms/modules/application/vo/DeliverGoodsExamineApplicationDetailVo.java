package com.yunkouan.wms.modules.application.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamineApplicationDetail;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsExamineApplicationDetailVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4304432320377925188L;
	
	private DeliverGoodsExamineApplicationDetail entity;
	
	private String orderByStr = "update_time desc";
	
	private SkuVo skuVo;
	
	private DeliverGoodsApplicationGoodsSkuVo appGoodsSkuVo;
	
	public DeliverGoodsExamineApplicationDetailVo(){
		
	}
	public DeliverGoodsExamineApplicationDetailVo(DeliverGoodsExamineApplicationDetail entity){
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsExamineApplicationDetail.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getExamineApplicationId())){
			c.andEqualTo("examineApplicationId",entity.getExamineApplicationId());
		}

		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		
		return example;
	}
	public DeliverGoodsExamineApplicationDetail getEntity() {
		return entity;
	}
	public void setEntity(DeliverGoodsExamineApplicationDetail entity) {
		this.entity = entity;
	}

	public String getOrderByStr() {
		return orderByStr;
	}
	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}
	public SkuVo getSkuVo() {
		return skuVo;
	}
	public void setSkuVo(SkuVo skuVo) {
		this.skuVo = skuVo;
	}
	public DeliverGoodsApplicationGoodsSkuVo getAppGoodsSkuVo() {
		return appGoodsSkuVo;
	}
	public void setAppGoodsSkuVo(DeliverGoodsApplicationGoodsSkuVo appGoodsSkuVo) {
		this.appGoodsSkuVo = appGoodsSkuVo;
	}
	
	

}
