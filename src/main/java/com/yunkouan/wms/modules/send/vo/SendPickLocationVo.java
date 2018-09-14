package com.yunkouan.wms.modules.send.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendPickLocationVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7153663325926331439L;

	private SendPickLocation sendPickLocation = new SendPickLocation();
	
	private String skuId;
	
//	private String batchNo;
	
	private String locationNo;
	
	private String asnId;
	
	private String locationComment;
	
	private Example example;
	
	private Boolean batchNoIsNull;
	
	private List<String> pickDetailIds;
	
	private String srcNo;
	private String orgId;
	private String warehouseId;
	private String expressBillNo;
	private String expressServiceCode;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private String beginTime;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private String endTime;
	private String ownerName;
	private String skuBarCode;
	private String skuNo;
	private String skuName;
	
	
	public SendPickLocationVo(){
		this.example = new Example(SendPickLocation.class);
		example.setOrderByClause("update_time desc,pick_location_id2 desc");
		example.createCriteria();
	}
	
	
	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}
	

	public SendPickLocation getSendPickLocation() {
		return sendPickLocation;
	}

	public void setSendPickLocation(SendPickLocation sendPickLocation) {
		this.sendPickLocation = sendPickLocation;
	}
	
	
	
	public String getSkuId() {
		return skuId;
	}


	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}


//	public String getBatchNo() {
//		return batchNo;
//	}


//	public void setBatchNo(String batchNo) {
//		this.batchNo = batchNo;
//	}
	

	public String getLocationNo() {
		return locationNo;
	}


	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}


	public Boolean getBatchNoIsNull() {
		return batchNoIsNull;
	}


	public String getAsnId() {
		return asnId;
	}


	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}


	public String getLocationComment() {
		return locationComment;
	}


	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}


	public void setBatchNoIsNull(Boolean batchNoIsNull) {
		this.batchNoIsNull = batchNoIsNull;
	}


	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(StringUtils.isNotEmpty(sendPickLocation.getLocationId())){
			c.andEqualTo("locationId",sendPickLocation.getLocationId());
		}
		if(sendPickLocation.getPickType() != null){
			c.andEqualTo("pickType",sendPickLocation.getPickType());
		}
		if(StringUtils.isNotEmpty(sendPickLocation.getPickDetailId())){
			c.andEqualTo("pickDetailId",sendPickLocation.getPickDetailId());
		}
		
		if(pickDetailIds != null && !pickDetailIds.isEmpty()){
			c.andIn("pickDetailId", pickDetailIds);
		}
		
		if(sendPickLocation.getBatchNo() != null){
			c.andEqualTo("batchNo",sendPickLocation.getBatchNo());
			
		}else if(batchNoIsNull != null && batchNoIsNull){
			c.andCondition("(batch_no is null or batch_no = '')");
		}

		return example;
	}


	public List<String> getPickDetailIds() {
		return pickDetailIds;
	}


	public void setPickDetailIds(List<String> pickDetailIds) {
		this.pickDetailIds = pickDetailIds;
	}


	public String getSrcNo() {
		return srcNo;
	}


	public void setSrcNo(String srcNo) {
		this.srcNo = srcNo;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getWarehouseId() {
		return warehouseId;
	}


	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}


	public String getExpressBillNo() {
		return expressBillNo;
	}


	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}


	public String getExpressServiceCode() {
		return expressServiceCode;
	}


	public void setExpressServiceCode(String expressServiceCode) {
		this.expressServiceCode = expressServiceCode;
	}


	public String getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getSkuBarCode() {
		return skuBarCode;
	}


	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}


	public String getSkuName() {
		return skuName;
	}


	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	
	
}
