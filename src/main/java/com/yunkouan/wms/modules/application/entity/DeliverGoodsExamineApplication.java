package com.yunkouan.wms.modules.application.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the deliver_goods_examine_application database table.
 * 
 */
@Entity
@Table(name="deliver_goods_examine_application")
@NamedQuery(name="DeliverGoodsExamineApplication.findAll", query="SELECT d FROM DeliverGoodsExamineApplication d")
public class DeliverGoodsExamineApplication extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="application_id")
	private String applicationId;


//	@Column(name="detain_flag")
//	private String detainFlag;
	
	private String billNo;

	@Column(name="examine_id")
	private String examineId;

	@Column(name="last_car_flag")
	private String lastCarFlag;

	@Column(name="org_id")
	private String orgId;

	@Column(name="pack_no")
	private Integer packNo;

	private String remark;

	private Integer status;

	@Column(name="warehouse_id")
	private String warehouseId;

	private BigDecimal weight;

	public DeliverGoodsExamineApplication() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

//	public String getDetainFlag() {
//		return this.detainFlag;
//	}
//
//	public void setDetainFlag(String detainFlag) {
//		this.detainFlag = detainFlag;
//	}

	public String getExamineId() {
		return this.examineId;
	}

	public void setExamineId(String examineId) {
		this.examineId = examineId;
	}

	public String getLastCarFlag() {
		return this.lastCarFlag;
	}

	public void setLastCarFlag(String lastCarFlag) {
		this.lastCarFlag = lastCarFlag;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getPackNo() {
		return this.packNo;
	}

	public void setPackNo(Integer packNo) {
		this.packNo = packNo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

}