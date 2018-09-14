package com.yunkouan.wms.modules.assistance.entity;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;

//对应msmq_message表
public class InvAssisLog extends BaseEntity {
	private static final long serialVersionUID = -6801686232046728661L;

	@Id
	private String id;
	private String billType;
	private String billId;
	private String messageId;
	private Boolean enterOrExit;
	private String result;
	private String message;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Boolean getEnterOrExit() {
		return enterOrExit;
	}
	public void setEnterOrExit(Boolean enterOrExit) {
		this.enterOrExit = enterOrExit;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
}