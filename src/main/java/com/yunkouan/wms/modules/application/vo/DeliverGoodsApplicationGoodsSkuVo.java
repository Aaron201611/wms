package com.yunkouan.wms.modules.application.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGoodsSku;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsApplicationGoodsSkuVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 201088607720778759L;
	
	private DeliverGoodsApplicationGoodsSku entity;
	
	private String statusComment;
	
	private SkuVo skuVo;
	
	private String orderByStr = "update_time desc";
	
	private Boolean notCompleteExamine;
	
	public DeliverGoodsApplicationGoodsSkuVo(){
		
	}
	
	public DeliverGoodsApplicationGoodsSkuVo(DeliverGoodsApplicationGoodsSku entity){
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsApplicationGoodsSku.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
//		if(StringUtil.isNotBlank(entity.getApplicationNo())){
//			c.andEqualTo("applicationNo",entity.getApplicationNo());
//		}
		if(StringUtil.isNotBlank(entity.getApplyGoodsId())){
			c.andEqualTo("applyGoodsId",entity.getApplyGoodsId());
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		if(notCompleteExamine != null && notCompleteExamine){
			c.andCondition("dec_qty > pass_audit_qty");
		}
		
		return example;
	}

	public DeliverGoodsApplicationGoodsSku getEntity() {
		return entity;
	}

	public void setEntity(DeliverGoodsApplicationGoodsSku entity) {
		this.entity = entity;
	}

	public String getStatusComment() {
		return statusComment;
	}

	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public Boolean getNotCompleteExamine() {
		return notCompleteExamine;
	}

	public void setNotCompleteExamine(Boolean notCompleteExamine) {
		this.notCompleteExamine = notCompleteExamine;
	}

	public SkuVo getSkuVo() {
		return skuVo;
	}

	public void setSkuVo(SkuVo skuVo) {
		this.skuVo = skuVo;
	}
	
	

}
