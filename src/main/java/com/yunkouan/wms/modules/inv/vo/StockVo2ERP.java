package com.yunkouan.wms.modules.inv.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StockVo2ERP {
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date inDate;
	private Integer skuStatus;
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date produceDate; //生产日期
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date expiredDate;//过期日期
	private String skuName;
	private String batchNo;
	private String measureUnit;
	private String specModel;
	private String warehouseNo;
	private String warehouseName;
	private String ownerName;
	private String merchantShortName;
	private String skuStatusName;
	private String isBlockName;
	private String areaName;
	private String locationName; //库位名称编码
	private String locationTypeName;
	private double stockQty;
	public Date getInDate() {
		return inDate;
	}
	
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	
	public Integer getSkuStatus() {
		return skuStatus;
	}
	
	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
	}
	
	public Date getProduceDate() {
		return produceDate;
	}
	
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}
	
	public Date getExpiredDate() {
		return expiredDate;
	}
	
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	public String getSkuName() {
		return skuName;
	}
	
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	public String getMeasureUnit() {
		return measureUnit;
	}
	
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	
	public String getSpecModel() {
		return specModel;
	}
	
	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}
	
	public String getWarehouseNo() {
		return warehouseNo;
	}
	
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	
	public String getWarehouseName() {
		return warehouseName;
	}
	
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getMerchantShortName() {
		return merchantShortName;
	}
	
	public void setMerchantShortName(String merchantShortName) {
		this.merchantShortName = merchantShortName;
	}
	
	public String getSkuStatusName() {
		return skuStatusName;
	}
	
	public void setSkuStatusName(String skuStatusName) {
		this.skuStatusName = skuStatusName;
	}
	
	public String getIsBlockName() {
		return isBlockName;
	}
	
	public void setIsBlockName(String isBlockName) {
		this.isBlockName = isBlockName;
	}
	
	public String getAreaName() {
		return areaName;
	}
	
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public String getLocationTypeName() {
		return locationTypeName;
	}
	
	public void setLocationTypeName(String locationTypeName) {
		this.locationTypeName = locationTypeName;
	}

	public double getStockQty() {
		return stockQty;
	}
	

	public void setStockQty(double stockQty) {
		this.stockQty = stockQty;
	}

	public String getBatchNo() {
		if(produceDate!=null){
			SimpleDateFormat sfd=new SimpleDateFormat("yyyyMMdd");
			this.batchNo=sfd.format(produceDate);
		}
		return batchNo;
	}
	

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
}
