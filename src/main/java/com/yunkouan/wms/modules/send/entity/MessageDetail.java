package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunkouan.base.BaseEntity;


@Entity
@Table(name="message_detail")
public class MessageDetail extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -913599004000134800L;
	
	@Id
	@Column(name="id")
	private String id;

	@Column(name="MESSAGE_ID")
	private String MESSAGE_ID;
	
	@Column(name="MESSAGE_TYPE")
	private String MESSAGE_TYPE;
	
	@Column(name="EMS_NO")
	private String EMS_NO;
	
	@Column(name="COP_G_NO")
	private String COP_G_NO;
	
	@Column(name="ORDER_NO")
	private String ORDER_NO;
	
	@Column(name="RESULT_INFO")
	private String RESULT_INFO;
	
	@Column(name="RESULT_INFO1")
	private String RESULT_INFO1;
	
	@Column(name="RESULT_INFO2")
	private String RESULT_INFO2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getCOP_G_NO() {
		return COP_G_NO;
	}

	public void setCOP_G_NO(String cOP_G_NO) {
		COP_G_NO = cOP_G_NO;
	}

	public String getORDER_NO() {
		return ORDER_NO;
	}

	public void setORDER_NO(String oRDER_NO) {
		ORDER_NO = oRDER_NO;
	}

	public String getRESULT_INFO() {
		return RESULT_INFO;
	}

	public void setRESULT_INFO(String rESULT_INFO) {
		RESULT_INFO = rESULT_INFO;
	}

	public String getRESULT_INFO1() {
		return RESULT_INFO1;
	}

	public void setRESULT_INFO1(String rESULT_INFO1) {
		RESULT_INFO1 = rESULT_INFO1;
	}

	public String getRESULT_INFO2() {
		return RESULT_INFO2;
	}

	public void setRESULT_INFO2(String rESULT_INFO2) {
		RESULT_INFO2 = rESULT_INFO2;
	}

	
	
}
