package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;

@Entity
@Table(name="transmit_message")
public class TransmitMessage extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2171728450459668596L;
	
	@Id
	@Column(name="MESSAGE_ID", unique=true, nullable=false, length=64)
	private String MESSAGE_ID;
	
	@Column(name="MESSAGE_TYPE")
	private String MESSAGE_TYPE;
	
	@Column(name="EMS_NO")
	private String EMS_NO;
	
	@Column(name="ORDER_NO")
	private String ORDER_NO;
	
	@Column(name="FUNCTION_CODE")
	private String FUNCTION_CODE ;
	
	@Column(name="CHK_RESULT")
	private String CHK_RESULT;
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	@Column(name="MESSAGE_DATE")
	private String MESSAGE_DATE;
	
	@Column(name="RECEIVER_ID")
	private String RECEIVER_ID;
	
	@Column(name="RECEIVER_ADDRESS")
	private String RECEIVER_ADDRESS;
	
	@Column(name="SENDER_ID")
	private String SENDER_ID;
	
	@Column(name="SEND_ADDRESS")
	private String SEND_ADDRESS;
	
	@Column(name="MESSAGE_SIGN")
	private String MESSAGE_SIGN;
	
	@Column(name="SEND_TYPE")
	private String SEND_TYPE;
	
	@Column(name="MESSAGE_ID2")
	private String MESSAGE_ID2;
	
	public String getMESSAGE_ID() {
		return MESSAGE_ID;
	}
	public void setMESSAGE_ID(String mESSAGE_ID) {
		MESSAGE_ID = mESSAGE_ID;
	}
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}
	public void setMESSAGE_TYPE(String mESSAGE_TYPE) {
		MESSAGE_TYPE = mESSAGE_TYPE;
	}
	public String getEMS_NO() {
		return EMS_NO;
	}
	public void setEMS_NO(String eMS_NO) {
		EMS_NO = eMS_NO;
	}
	public String getORDER_NO() {
		return ORDER_NO;
	}
	public void setORDER_NO(String oRDER_NO) {
		ORDER_NO = oRDER_NO;
	}
	public String getFUNCTION_CODE() {
		return FUNCTION_CODE;
	}
	public void setFUNCTION_CODE(String fUNCTION_CODE) {
		FUNCTION_CODE = fUNCTION_CODE;
	}
	public String getCHK_RESULT() {
		return CHK_RESULT;
	}
	public void setCHK_RESULT(String cHK_RESULT) {
		CHK_RESULT = cHK_RESULT;
	}
	
	public String getMESSAGE_DATE() {
		return MESSAGE_DATE;
	}
	public void setMESSAGE_DATE(String mESSAGE_DATE) {
		MESSAGE_DATE = mESSAGE_DATE;
	}
	public String getRECEIVER_ID() {
		return RECEIVER_ID;
	}
	public void setRECEIVER_ID(String rECEIVER_ID) {
		RECEIVER_ID = rECEIVER_ID;
	}
	public String getRECEIVER_ADDRESS() {
		return RECEIVER_ADDRESS;
	}
	public void setRECEIVER_ADDRESS(String rECEIVER_ADDRESS) {
		RECEIVER_ADDRESS = rECEIVER_ADDRESS;
	}
	public String getSENDER_ID() {
		return SENDER_ID;
	}
	public void setSENDER_ID(String sENDER_ID) {
		SENDER_ID = sENDER_ID;
	}
	public String getSEND_ADDRESS() {
		return SEND_ADDRESS;
	}
	public void setSEND_ADDRESS(String sEND_ADDRESS) {
		SEND_ADDRESS = sEND_ADDRESS;
	}
	public String getMESSAGE_SIGN() {
		return MESSAGE_SIGN;
	}
	public void setMESSAGE_SIGN(String mESSAGE_SIGN) {
		MESSAGE_SIGN = mESSAGE_SIGN;
	}
	public String getSEND_TYPE() {
		return SEND_TYPE;
	}
	public void setSEND_TYPE(String sEND_TYPE) {
		SEND_TYPE = sEND_TYPE;
	}
	
}
