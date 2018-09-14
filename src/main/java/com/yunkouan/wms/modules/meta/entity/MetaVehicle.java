package com.yunkouan.wms.modules.meta.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;


/**
 * The persistent class for the meta_vehicle database table.
 * 
 */
@Entity
@Table(name="meta_vehicle")
public class MetaVehicle extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="a_conta_no")
	private String aContaNo;

	@Column(name="a_conta_type")
	private String aContaType;

	@Column(name="a_conta_wt")
	private String aContaWt;

	@Column(name="car_no")
    @Length(max=32,message="{valid_car_no_length}",groups={ValidSave.class,ValidUpdate.class})
    @NotNull(message="{valid_car_no_isnull}",groups={ValidSave.class,ValidUpdate.class})
	private String carNo;

	@Column(name="car_wt")
	private BigDecimal carWt;

	@Column(name="f_conta_no")
	private String fContaNo;

	@Column(name="f_conta_type")
	private String fContaType;

	@Column(name="f_conta_wt")
	private BigDecimal fContaWt;

	@Column(name="frame_no")
	private String frameNo;

	@Column(name="frame_type")
	private String frameType;

	@Column(name="frame_wt")
	private BigDecimal frameWt;

	@Column(name="org_id")
	private String orgId;

	private Integer status;

	@Column(name="warehouse_id")
	private String warehouseId;

	public MetaVehicle() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAContaNo() {
		return this.aContaNo;
	}

	public void setAContaNo(String aContaNo) {
		this.aContaNo = aContaNo;
	}

	public String getAContaType() {
		return this.aContaType;
	}

	public void setAContaType(String aContaType) {
		this.aContaType = aContaType;
	}

	public String getAContaWt() {
		return this.aContaWt;
	}

	public void setAContaWt(String aContaWt) {
		this.aContaWt = aContaWt;
	}

	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public BigDecimal getCarWt() {
		return this.carWt;
	}

	public void setCarWt(BigDecimal carWt) {
		this.carWt = carWt;
	}
	public String getFContaNo() {
		return this.fContaNo;
	}

	public void setFContaNo(String fContaNo) {
		this.fContaNo = fContaNo;
	}

	public String getFContaType() {
		return this.fContaType;
	}

	public void setFContaType(String fContaType) {
		this.fContaType = fContaType;
	}

	public BigDecimal getFContaWt() {
		return this.fContaWt;
	}

	public void setFContaWt(BigDecimal fContaWt) {
		this.fContaWt = fContaWt;
	}

	public String getFrameNo() {
		return this.frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getFrameType() {
		return this.frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}

	public BigDecimal getFrameWt() {
		return this.frameWt;
	}

	public void setFrameWt(BigDecimal frameWt) {
		this.frameWt = frameWt;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

}