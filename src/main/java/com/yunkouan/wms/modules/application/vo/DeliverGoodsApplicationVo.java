package com.yunkouan.wms.modules.application.vo;

import java.util.ArrayList;
import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsApplicationVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3679294884549000693L;

	private DeliverGoodsApplication entity;
	
	private DeliverGoodsApplicationFormVo formVo;
	
	private String statusName;
	private String auditStepName;
	private String declareTypeName;
	private String bizTypeName;
	private String bizModeName;
	private String wrapTypeName;
	private String iEFlagName;
	
	private String orderByStr = "update_time desc";
	
	private List<DeliverGoodsApplicationGoodVo> applicationGoodVoList = new ArrayList<DeliverGoodsApplicationGoodVo>();
	
	private List<String> appIdList;
	
	private List<DeliverGoodsApplicationGoodsSkuVo> goodsSkuVoList;
	
	private List<String> statusNotIn;
	
	public DeliverGoodsApplicationVo(){
		
	}
	
	public DeliverGoodsApplicationVo(DeliverGoodsApplication entity){
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsApplication.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getApplicationNo())){
			c.andEqualTo("applicationNo",entity.getApplicationNo());
		}
		if(StringUtil.isNotBlank(entity.getiEFlag())){
			c.andEqualTo("iEFlag",entity.getiEFlag());
		}
		if(entity.getStatus() != null){
			c.andEqualTo("status",entity.getStatus());
		}
		if(statusNotIn != null){
			c.andNotIn("status", statusNotIn);
		}
		if(StringUtil.isNoneBlank(entity.getAuditStep())){
			c.andEqualTo("auditStep",entity.getAuditStep());
		}
		if(StringUtil.isNoneBlank(entity.getAsnId())){
			c.andEqualTo("asnId",entity.getAsnId());
		}
		if(StringUtil.isNoneBlank(entity.getDeliveryId())){
			c.andEqualTo("deliveryId",entity.getDeliveryId());
		}
		if(StringUtil.isNoneBlank(entity.getApplyFrom())){
			c.andEqualTo("applyFrom",entity.getApplyFrom());
		}
		if(StringUtil.isNoneBlank(entity.getApplicationFormId())){
			c.andEqualTo("applicationFormId",entity.getApplicationFormId());
		}
//		if(statusLessThan != null){
//			c.andLessThan("status", statusLessThan);
//		}
		if(appIdList != null && !appIdList.isEmpty()){
			c.andIn("id", appIdList);
		}
		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		
		return example;
	}

	public DeliverGoodsApplication getEntity() {
		return entity;
	}

	public void setEntity(DeliverGoodsApplication entity) {
		this.entity = entity;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public List<DeliverGoodsApplicationGoodVo> getApplicationGoodVoList() {
		return applicationGoodVoList;
	}

	public void setApplicationGoodVoList(List<DeliverGoodsApplicationGoodVo> applicationGoodVoList) {
		this.applicationGoodVoList = applicationGoodVoList;
	}

	public void setAppIdList(List<String> appIdList) {
		this.appIdList = appIdList;
	}

	public List<DeliverGoodsApplicationGoodsSkuVo> getGoodsSkuVoList() {
		return goodsSkuVoList;
	}

	public void setGoodsSkuVoList(List<DeliverGoodsApplicationGoodsSkuVo> goodsSkuVoList) {
		this.goodsSkuVoList = goodsSkuVoList;
	}
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public DeliverGoodsApplicationFormVo getFormVo() {
		return formVo;
	}

	public void setFormVo(DeliverGoodsApplicationFormVo formVo) {
		this.formVo = formVo;
	}

	public List<String> getAppIdList() {
		return appIdList;
	}
	

	public List<String> getStatusNotIn() {
		return statusNotIn;
	}
	

	public void setStatusNotIn(List<String> statusNotIn) {
		this.statusNotIn = statusNotIn;
	}

	public String getiEFlagName() {
		return iEFlagName;
	}

	public void setiEFlagName(String iEFlagName) {
		this.iEFlagName = iEFlagName;
	}

	public String getAuditStepName() {
		return auditStepName;
	}

	public void setAuditStepName(String auditStepName) {
		this.auditStepName = auditStepName;
	}

	public String getDeclareTypeName() {
		return declareTypeName;
	}
	

	public void setDeclareTypeName(String declareTypeName) {
		this.declareTypeName = declareTypeName;
	}
	

	public String getBizTypeName() {
		return bizTypeName;
	}
	

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	

	public String getBizModeName() {
		return bizModeName;
	}
	

	public void setBizModeName(String bizModeName) {
		this.bizModeName = bizModeName;
	}

	public String getWrapTypeName() {
		return wrapTypeName;
	}

	public void setWrapTypeName(String wrapTypeName) {
		this.wrapTypeName = wrapTypeName;
	}
	
	
	
	
}
