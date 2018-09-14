package com.yunkouan.wms.modules.sys.entity;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;

/**
 * 接口日志实体类
 * @author Aaron
 *
 */
public class InterfaceLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6429789296932617746L;
	
	@Id
	private String logId;
	
	private String sender;
	
	private String receiver;
	
	private String url;
	
	private String sendMessage;
	
	private String receiveMessage;
	
	private String result;
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date sendTime;
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date receiveTime;
	
	private String operer;
	
	private String note;
	
	private String orgId;
	
	private String warehouseId;
	
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getReceiveMessage() {
		return receiveMessage;
	}
	public void setReceiveMessage(String receiveMessage) {
		this.receiveMessage = receiveMessage;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getOperer() {
		return operer;
	}
	public void setOperer(String operer) {
		this.operer = operer;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
