package com.yunkouan.wms.modules.application.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the deliver_goods_application database table.
 * 
 */
@Entity
@Table(name="deliver_goods_application")
public class DeliverGoodsApplication extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="application_form_id")
	private String applicationFormId;

	@Column(name="application_no")
	private String applicationNo;
	
	private String declareType;
	
	private String bizType;
	
	private String bizMode;
	
	private String asnId;
	
	private String deliveryId;
	
	private String applyFrom;

	@Column(name="apply_org_code")
	private String applyOrgCode;

	@Column(name="apply_org_name")
	private String applyOrgName;

	@Column(name="apply_person")
	private String applyPerson;

	@Column(name="apply_time")
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date applyTime;

//	@Column(name="apply_type")
//	private String applyType;

//	@Column(name="biz_mode")
//	private String bizMode;

//	@Column(name="biz_type")
//	private String bizType;

//	@Column(name="direct_flag")
//	private String directFlag;

	@Column(name="gross_wt")
	private BigDecimal grossWt;

	@Column(name="i_e_flag")
	private String iEFlag;
	
	@Column(name="trade_code")
	private String tradeCode;

	@Column(name="trade_name")
	private String tradeName;
	
	@Column(name="out_entrprise_code")
	private String outEntrpriseCode;

	@Column(name="out_entrprise_name")
	private String outEntrpriseName;

	@Column(name="receive_deliver_org_code")
	private String receiveDeliverOrgCode;

	@Column(name="receive_deliver_org_name")
	private String receiveDeliverOrgName;
	
	@Column(name="guarantee_id")
	private String guaranteeId;

	@Column(name="guarantee_total")
	private BigDecimal guaranteeTotal;

	@Column(name="guarantee_type")
	private String guaranteeType;

	@Column(name="input_org_code")
	private String inputOrgCode;

	@Column(name="input_org_name")
	private String inputOrgName;

	@Column(name="link_man")
	private String linkMan;

	@Column(name="link_phone")
	private String linkPhone;

	@Column(name="net_wt")
	private BigDecimal netWt;

	@Column(name="org_id")
	private String orgId;

	@Column(name="pack_no")
	private BigInteger packNo;

	@Column(name="rel_application_no")
	private String relApplicationNo;

	@Column(name="rel_days")
	private int relDays;

	@Column(name="rel_type")
	private String relType;

	private String remark;

	@Column(name="require_guarantee_total")
	private BigDecimal requireGuaranteeTotal;

	private Integer status;
	
	private String auditStep;

//	@Column(name="step_id")
//	private String stepId;

//	@Column(name="trade_code")
//	private String tradeCode;
//
//	@Column(name="trade_name")
//	private String tradeName;

	@Column(name="warehouse_id")
	private String warehouseId;

	@Column(name="wrap_type")
	private String wrapType;

	public DeliverGoodsApplication() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationFormId() {
		return this.applicationFormId;
	}

	public void setApplicationFormId(String applicationFormId) {
		this.applicationFormId = applicationFormId;
	}

	public String getApplicationNo() {
		return this.applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getApplyOrgCode() {
		return this.applyOrgCode;
	}

	public void setApplyOrgCode(String applyOrgCode) {
		this.applyOrgCode = applyOrgCode;
	}

	public String getApplyOrgName() {
		return this.applyOrgName;
	}

	public void setApplyOrgName(String applyOrgName) {
		this.applyOrgName = applyOrgName;
	}

	public String getApplyPerson() {
		return this.applyPerson;
	}

	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}

	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

//	public String getApplyType() {
//		return this.applyType;
//	}

//	public void setApplyType(String applyType) {
//		this.applyType = applyType;
//	}

//	public String getBizMode() {
//		return this.bizMode;
//	}

//	public void setBizMode(String bizMode) {
//		this.bizMode = bizMode;
//	}

//	public String getBizType() {
//		return this.bizType;
//	}

//	public void setBizType(String bizType) {
//		this.bizType = bizType;
//	}

//	public String getDirectFlag() {
//		return this.directFlag;
//	}
//
//	public void setDirectFlag(String directFlag) {
//		this.directFlag = directFlag;
//	}

	public BigDecimal getGrossWt() {
		return this.grossWt;
	}

	public void setGrossWt(BigDecimal grossWt) {
		this.grossWt = grossWt;
	}

	public String getInputOrgCode() {
		return this.inputOrgCode;
	}

	public void setInputOrgCode(String inputOrgCode) {
		this.inputOrgCode = inputOrgCode;
	}

	public String getInputOrgName() {
		return this.inputOrgName;
	}

	public void setInputOrgName(String inputOrgName) {
		this.inputOrgName = inputOrgName;
	}

	public String getLinkMan() {
		return this.linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return this.linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public BigDecimal getNetWt() {
		return this.netWt;
	}

	public void setNetWt(BigDecimal netWt) {
		this.netWt = netWt;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public BigInteger getPackNo() {
		return this.packNo;
	}

	public void setPackNo(BigInteger packNo) {
		this.packNo = packNo;
	}

	public String getRelApplicationNo() {
		return this.relApplicationNo;
	}

	public void setRelApplicationNo(String relApplicationNo) {
		this.relApplicationNo = relApplicationNo;
	}

	public int getRelDays() {
		return this.relDays;
	}

	public void setRelDays(int relDays) {
		this.relDays = relDays;
	}

	public String getRelType() {
		return this.relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getRequireGuaranteeTotal() {
		return this.requireGuaranteeTotal;
	}

	public void setRequireGuaranteeTotal(BigDecimal requireGuaranteeTotal) {
		this.requireGuaranteeTotal = requireGuaranteeTotal;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAuditStep() {
		return auditStep;
	}

	public void setAuditStep(String auditStep) {
		this.auditStep = auditStep;
	}

//	public String getStepId() {
//		return this.stepId;
//	}

//	public void setStepId(String stepId) {
//		this.stepId = stepId;
//	}

//	public String getTradeCode() {
//		return this.tradeCode;
//	}
//
//	public void setTradeCode(String tradeCode) {
//		this.tradeCode = tradeCode;
//	}
//
//	public String getTradeName() {
//		return this.tradeName;
//	}
//
//	public void setTradeName(String tradeName) {
//		this.tradeName = tradeName;
//	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWrapType() {
		return this.wrapType;
	}

	public void setWrapType(String wrapType) {
		this.wrapType = wrapType;
	}

	public String getAsnId() {
		return asnId;
	}

	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getApplyFrom() {
		return applyFrom;
	}

	public void setApplyFrom(String applyFrom) {
		this.applyFrom = applyFrom;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getOutEntrpriseCode() {
		return outEntrpriseCode;
	}

	public void setOutEntrpriseCode(String outEntrpriseCode) {
		this.outEntrpriseCode = outEntrpriseCode;
	}

	public String getOutEntrpriseName() {
		return outEntrpriseName;
	}

	public void setOutEntrpriseName(String outEntrpriseName) {
		this.outEntrpriseName = outEntrpriseName;
	}

	public String getReceiveDeliverOrgCode() {
		return receiveDeliverOrgCode;
	}

	public void setReceiveDeliverOrgCode(String receiveDeliverOrgCode) {
		this.receiveDeliverOrgCode = receiveDeliverOrgCode;
	}

	public String getReceiveDeliverOrgName() {
		return receiveDeliverOrgName;
	}

	public void setReceiveDeliverOrgName(String receiveDeliverOrgName) {
		this.receiveDeliverOrgName = receiveDeliverOrgName;
	}

	public String getGuaranteeId() {
		return guaranteeId;
	}

	public void setGuaranteeId(String guaranteeId) {
		this.guaranteeId = guaranteeId;
	}

	public BigDecimal getGuaranteeTotal() {
		return guaranteeTotal;
	}

	public void setGuaranteeTotal(BigDecimal guaranteeTotal) {
		this.guaranteeTotal = guaranteeTotal;
	}

	public String getGuaranteeType() {
		return guaranteeType;
	}

	public void setGuaranteeType(String guaranteeType) {
		this.guaranteeType = guaranteeType;
	}

	public String getiEFlag() {
		return iEFlag;
	}
	
	public void setiEFlag(String iEFlag) {
		this.iEFlag = iEFlag;
	}

	public String getDeclareType() {
		return declareType;
	}

	public void setDeclareType(String declareType) {
		this.declareType = declareType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizMode() {
		return bizMode;
	}

	public void setBizMode(String bizMode) {
		this.bizMode = bizMode;
	}
	
	
}