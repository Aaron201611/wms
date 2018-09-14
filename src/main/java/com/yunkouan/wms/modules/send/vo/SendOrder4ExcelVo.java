package com.yunkouan.wms.modules.send.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SendOrder4ExcelVo {

	private int index;
	
	 @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderTime;
	
	private String srcNo;
	
	private String expressServiceName;
	
	private String expressBillNo;
	
	private String receiver;
	
	private String contactPhone;
	
	private String contactAddress;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public String getExpressServiceName() {
		return expressServiceName;
	}

	public void setExpressServiceName(String expressServiceName) {
		this.expressServiceName = expressServiceName;
	}

	public String getExpressBillNo() {
		return expressBillNo;
	}

	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	
	
	
}
