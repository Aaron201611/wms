package com.yunkouan.wms.modules.message.vo.iss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="messageHead",namespace="")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"messageId","functionCode","messageType","senderId","receiverId","sendTime","version"})
public class ISSMessageHead {

	private String messageId;
	
	private String functionCode;
	
	private String messageType;
	
	private String senderId;
	
	private String receiverId;
	
	private String sendTime;
	
	private String version;

	@XmlElement(name="MessageID")
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@XmlElement(name="FunctionCode")
	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	@XmlElement(name="MessageType")
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	@XmlElement(name="SenderID")
	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	@XmlElement(name="ReceiverID")
	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	@XmlElement(name="SendTime")
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	@XmlElement(name="Version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
