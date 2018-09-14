package com.yunkouan.wms.modules.send.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;

public class SendStasticsVo extends BaseVO{
	
	private int index;
	
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderTime;
	
	private String srcNo;
	
	private String expressBillNo;
	
	private String locationNo;
	
	private String skuName;
	
	private String skuBarCode;
	
	private String skuNo;
	
	private String merchantName;
	
	private Double orderQty;
	
	private Double sendQty;
	
	private String operPerson;

	private String measureUnit;
	
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date outDate;

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getSrcNo() {
		return srcNo;
	}

	public void setSrcNo(String srcNo) {
		this.srcNo = srcNo;
	}

	public String getExpressBillNo() {
		return expressBillNo;
	}

	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}

	public String getLocationNo() {
		return locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuBarCode() {
		return skuBarCode;
	}

	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	public Double getSendQty() {
		return sendQty;
	}

	public void setSendQty(Double sendQty) {
		this.sendQty = sendQty;
	}

	public String getOperPerson() {
		return operPerson;
	}

	public void setOperPerson(String operPerson) {
		this.operPerson = operPerson;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
    
    
}
