package com.yunkouan.wms.modules.application.entity;

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
 * The persistent class for the deliver_goods_application_form database table.
 * 
 */
@Entity
@Table(name="deliver_goods_application_form")
public class DeliverGoodsApplicationForm extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="application_form_no")
    @Length(max=32,message="{valid_application_form_no_length}",groups={ValidSave.class,ValidUpdate.class})
    @NotNull(message="{valid_application_form_no_isnull}",groups={ValidSave.class,ValidUpdate.class})
	private String applicationFormNo;

	@Column(name="eci_ems_level")
	private String eciEmsLevel;

	@Column(name="eci_ems_no")
	private String eciEmsNo;

	@Column(name="eci_ems_property")
	private String eciEmsProperty;

	@Column(name="gp_i_e_flag")
	private String gpIEFlag;

	@Column(name="guarantee_id")
	private String guaranteeId;

	@Column(name="guarantee_total")
	private BigDecimal guaranteeTotal;

	@Column(name="guarantee_type")
	private String guaranteeType;

	@Column(name="org_id")
	private String orgId;

	@Column(name="out_entrprise_code")
	private String outEntrpriseCode;

	@Column(name="out_entrprise_name")
	private String outEntrpriseName;

	@Column(name="receive_deliver_org_code")
	private String receiveDeliverOrgCode;

	@Column(name="receive_deliver_org_name")
	private String receiveDeliverOrgName;

	private Integer status;

	@Column(name="trade_code")
	private String tradeCode;

	@Column(name="trade_name")
	private String tradeName;

	@Column(name="warehouse_id")
	private String warehouseId;

	public DeliverGoodsApplicationForm() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationFormNo() {
		return this.applicationFormNo;
	}

	public void setApplicationFormNo(String applicationFormNo) {
		this.applicationFormNo = applicationFormNo;
	}

	public String getEciEmsLevel() {
		return this.eciEmsLevel;
	}

	public void setEciEmsLevel(String eciEmsLevel) {
		this.eciEmsLevel = eciEmsLevel;
	}

	public String getEciEmsNo() {
		return this.eciEmsNo;
	}

	public void setEciEmsNo(String eciEmsNo) {
		this.eciEmsNo = eciEmsNo;
	}

	public String getEciEmsProperty() {
		return this.eciEmsProperty;
	}

	public void setEciEmsProperty(String eciEmsProperty) {
		this.eciEmsProperty = eciEmsProperty;
	}

	public String getGpIEFlag() {
		return this.gpIEFlag;
	}

	public void setGpIEFlag(String gpIEFlag) {
		this.gpIEFlag = gpIEFlag;
	}

	public String getGuaranteeId() {
		return this.guaranteeId;
	}

	public void setGuaranteeId(String guaranteeId) {
		this.guaranteeId = guaranteeId;
	}

	public BigDecimal getGuaranteeTotal() {
		return this.guaranteeTotal;
	}

	public void setGuaranteeTotal(BigDecimal guaranteeTotal) {
		this.guaranteeTotal = guaranteeTotal;
	}

	public String getGuaranteeType() {
		return this.guaranteeType;
	}

	public void setGuaranteeType(String guaranteeType) {
		this.guaranteeType = guaranteeType;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOutEntrpriseCode() {
		return this.outEntrpriseCode;
	}

	public void setOutEntrpriseCode(String outEntrpriseCode) {
		this.outEntrpriseCode = outEntrpriseCode;
	}

	public String getOutEntrpriseName() {
		return this.outEntrpriseName;
	}

	public void setOutEntrpriseName(String outEntrpriseName) {
		this.outEntrpriseName = outEntrpriseName;
	}

	public String getReceiveDeliverOrgCode() {
		return this.receiveDeliverOrgCode;
	}

	public void setReceiveDeliverOrgCode(String receiveDeliverOrgCode) {
		this.receiveDeliverOrgCode = receiveDeliverOrgCode;
	}

	public String getReceiveDeliverOrgName() {
		return this.receiveDeliverOrgName;
	}

	public void setReceiveDeliverOrgName(String receiveDeliverOrgName) {
		this.receiveDeliverOrgName = receiveDeliverOrgName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTradeCode() {
		return this.tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

}