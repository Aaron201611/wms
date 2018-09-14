package com.yunkouan.wms.modules.application.vo;

import java.util.ArrayList;
import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamineApplication;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliverGoodsExamineApplicationVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5475959242960773322L;
	
	private DeliverGoodsExamineApplication entity;
	
	private String applicationNo;
	
	private String orderByStr = "update_time desc";
	
	private List<DeliverGoodsExamineApplicationDetailVo> detailVoList = new ArrayList<DeliverGoodsExamineApplicationDetailVo>();
	
	private List<String> delDetailIdList;
	
	public DeliverGoodsExamineApplicationVo(){
		
	}
	
	public DeliverGoodsExamineApplicationVo(DeliverGoodsExamineApplication entity){
		this.entity = entity;
	}
	
	public Example getExample(){
		Example example = new Example(DeliverGoodsExamineApplication.class);
		example.setOrderByClause(orderByStr);
		Criteria c = example.createCriteria();
		
		if(StringUtil.isNotBlank(entity.getApplicationId())){
			c.andEqualTo("applicationId",entity.getApplicationId());
		}
		if(StringUtil.isNotBlank(entity.getExamineId())){
			c.andEqualTo("examineId",entity.getExamineId());
		}

		if(StringUtil.isNotBlank(entity.getOrgId())){
			c.andEqualTo("orgId",entity.getOrgId());
		}
		if(StringUtil.isNotBlank(entity.getWarehouseId())){
			c.andEqualTo("warehouseId",entity.getWarehouseId());
		}
		
		return example;
	}

	public DeliverGoodsExamineApplication getEntity() {
		return entity;
	}

	public void setEntity(DeliverGoodsExamineApplication entity) {
		this.entity = entity;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public List<String> getDelDetailIdList() {
		return delDetailIdList;
	}

	public void setDelDetailIdList(List<String> delDetailIdList) {
		this.delDetailIdList = delDetailIdList;
	}

	public List<DeliverGoodsExamineApplicationDetailVo> getDetailVoList() {
		return detailVoList;
	}

	public void setDetailVoList(List<DeliverGoodsExamineApplicationDetailVo> detailVoList) {
		this.detailVoList = detailVoList;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	
	

}
