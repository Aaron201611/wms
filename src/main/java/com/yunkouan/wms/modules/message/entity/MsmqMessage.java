package com.yunkouan.wms.modules.message.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunkouan.base.BaseEntity;

@Entity
@Table(name="msmq_message")
public class MsmqMessage extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8069308016340303881L;

	@Id
	private String id;
	
	/**
	 * 发送方
	 */
	private String sender;
	
	/**
	 * 接收方
	 */
	private String receiver;
	
	/**
	 * 消息id
	 */
	private String messageId;
	
	private String messageType;
	
	private String orderNo;
	
	private String functionType;
	
	/**
	 * 报文内容
	 */
	private String content;
	
	/**
	 * 发送时间
	 */
	private Date sendTime;
	
	/**
	 * 接收时间
	 */
	private Date receiveTime;
	
	/**
	 * 状态
	 */
	private String status;
	
	private String orgId;
	
	private String warehouseId;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
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

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	
}
