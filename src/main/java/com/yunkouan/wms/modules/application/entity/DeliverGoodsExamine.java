package com.yunkouan.wms.modules.application.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the deliver_goods_examine database table.
 * 
 */
@Entity
@Table(name="deliver_goods_examine")
public class DeliverGoodsExamine extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="beg_area")
	private String begArea;

	private String kernelType;
	
	private String kernelBizMode;

	private String kernelBizType;
	
	private String icCard;

//	@Column(name="car_id")
//	private String carId;
	private String carNo;
	private BigDecimal carWt;
	private String frameNo;
	private String frameType;
	private BigDecimal frameWt;
	private String fContaNo;
	private BigDecimal fContaWt;
	private String fContaType;
	private String aContaNo;
	private BigDecimal aContaWt;
	private String aContaType;
	

	private String billNo;

	@Column(name="end_area")
	private String endArea;

	@Column(name="examine_no")
	private String examineNo;
	
	private String examineFrom;

//	@Column(name="examine_type")
//	private String examineType;

	@Column(name="goods_wt")
	private BigDecimal goodsWt;

	@Column(name="i_e_flag")
	private String iEFlag;

//	@Column(name="is_circle")
//	private String isCircle;

	@Column(name="org_id")
	private String orgId;
	
	private String mainItems;
	
	@Column(name="input_org_code")
	private String inputOrgCode;

	@Column(name="input_org_name")
	private String inputOrgName;
	
	@Column(name="declare_org_code")
	private String declareOrgCode;

	@Column(name="declare_org_name")
	private String declareOrgName;

	@Column(name="declare_person")
	private String declarePerson;

	@Column(name="declare_time")
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date declareTime;
	
//	@Column(name="step_id")
//	private String stepId;

	private String remark;

	private Integer status;
	
	private String auditStep;

	@Column(name="total_pack_no")
	private Integer totalPackNo;

	@Column(name="total_wt")
	private BigDecimal totalWt;

	@Column(name="warehouse_id")
	private String warehouseId;

	public DeliverGoodsExamine() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBegArea() {
		return this.begArea;
	}

	public void setBegArea(String begArea) {
		this.begArea = begArea;
	}

//	public String getBizMode() {
//		return this.bizMode;
//	}

//	public void setBizMode(String bizMode) {
//		this.bizMode = bizMode;
//	}
//
//	public String getBizType() {
//		return this.bizType;
//	}
//
//	public void setBizType(String bizType) {
//		this.bizType = bizType;
//	}

	public String getEndArea() {
		return this.endArea;
	}

	public void setEndArea(String endArea) {
		this.endArea = endArea;
	}

	public String getExamineNo() {
		return this.examineNo;
	}

	public void setExamineNo(String examineNo) {
		this.examineNo = examineNo;
	}

//	public String getExamineType() {
//		return this.examineType;
//	}
//
//	public void setExamineType(String examineType) {
//		this.examineType = examineType;
//	}

	public BigDecimal getGoodsWt() {
		return this.goodsWt;
	}

	public void setGoodsWt(BigDecimal goodsWt) {
		this.goodsWt = goodsWt;
	}

	public String getIEFlag() {
		return this.iEFlag;
	}

	public void setIEFlag(String iEFlag) {
		this.iEFlag = iEFlag;
	}

//	public String getIsCircle() {
//		return this.isCircle;
//	}
//
//	public void setIsCircle(String isCircle) {
//		this.isCircle = isCircle;
//	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getiEFlag() {
		return iEFlag;
	}

	public void setiEFlag(String iEFlag) {
		this.iEFlag = iEFlag;
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

	public Integer getTotalPackNo() {
		return this.totalPackNo;
	}

	public void setTotalPackNo(Integer totalPackNo) {
		this.totalPackNo = totalPackNo;
	}

	public BigDecimal getTotalWt() {
		return this.totalWt;
	}

	public void setTotalWt(BigDecimal totalWt) {
		this.totalWt = totalWt;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getMainItems() {
		return mainItems;
	}

	public void setMainItems(String mainItems) {
		this.mainItems = mainItems;
	}

	public String getIcCard() {
		return icCard;
	}

	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getInputOrgCode() {
		return inputOrgCode;
	}

	public void setInputOrgCode(String inputOrgCode) {
		this.inputOrgCode = inputOrgCode;
	}

	public String getInputOrgName() {
		return inputOrgName;
	}

	public void setInputOrgName(String inputOrgName) {
		this.inputOrgName = inputOrgName;
	}

	public String getDeclareOrgCode() {
		return declareOrgCode;
	}

	public void setDeclareOrgCode(String declareOrgCode) {
		this.declareOrgCode = declareOrgCode;
	}

	public String getDeclareOrgName() {
		return declareOrgName;
	}

	public void setDeclareOrgName(String declareOrgName) {
		this.declareOrgName = declareOrgName;
	}

	public String getDeclarePerson() {
		return declarePerson;
	}

	public void setDeclarePerson(String declarePerson) {
		this.declarePerson = declarePerson;
	}

	public Date getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}

//	public String getStepId() {
//		return stepId;
//	}
//
//	public void setStepId(String stepId) {
//		this.stepId = stepId;
//	}
	

	public String getCarNo() {
		return carNo;
	}
	

	public String getFrameNo() {
		return frameNo;
	}
	

	public String getFrameType() {
		return frameType;
	}
	


	public String getfContaNo() {
		return fContaNo;
	}
	


	public String getfContaType() {
		return fContaType;
	}
	

	public String getaContaNo() {
		return aContaNo;
	}
	


	public String getaContaType() {
		return aContaType;
	}
	

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	


	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}
	


	public void setfContaNo(String fContaNo) {
		this.fContaNo = fContaNo;
	}
	

	public void setfContaType(String fContaType) {
		this.fContaType = fContaType;
	}
	

	public void setaContaNo(String aContaNo) {
		this.aContaNo = aContaNo;
	}
	
	public void setaContaType(String aContaType) {
		this.aContaType = aContaType;
	}

	public BigDecimal getCarWt() {
		return carWt;
	}
	

	public BigDecimal getFrameWt() {
		return frameWt;
	}
	

	public BigDecimal getfContaWt() {
		return fContaWt;
	}
	

	public BigDecimal getaContaWt() {
		return aContaWt;
	}
	

	public void setCarWt(BigDecimal carWt) {
		this.carWt = carWt;
	}
	

	public void setFrameWt(BigDecimal frameWt) {
		this.frameWt = frameWt;
	}
	

	public void setfContaWt(BigDecimal fContaWt) {
		this.fContaWt = fContaWt;
	}
	

	public void setaContaWt(BigDecimal aContaWt) {
		this.aContaWt = aContaWt;
	}

	public String getExamineFrom() {
		return examineFrom;
	}

	public void setExamineFrom(String examineFrom) {
		this.examineFrom = examineFrom;
	}

	public String getKernelType() {
		return kernelType;
	}

	public void setKernelType(String kernelType) {
		this.kernelType = kernelType;
	}

	public String getKernelBizMode() {
		return kernelBizMode;
	}

	public void setKernelBizMode(String kernelBizMode) {
		this.kernelBizMode = kernelBizMode;
	}

	public String getKernelBizType() {
		return kernelBizType;
	}

	public void setKernelBizType(String kernelBizType) {
		this.kernelBizType = kernelBizType;
	}
	
}