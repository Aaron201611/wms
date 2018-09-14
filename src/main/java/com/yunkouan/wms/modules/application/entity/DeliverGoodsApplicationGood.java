package com.yunkouan.wms.modules.application.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the deliver_goods_application_goods database table.
 * 
 */
@Entity
@Table(name="deliver_goods_application_goods")
public class DeliverGoodsApplicationGood extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="application_id")
	private String applicationId;

	private String curr;

	@Column(name="dec_price")
	private BigDecimal decPrice;

	@Column(name="dec_qty")
	private BigDecimal decQty;

	@Column(name="dec_total")
	private BigDecimal decTotal;

	@Column(name="duty_mode")
	private String dutyMode;

	@Column(name="eci_goods_flag")
	private String eciGoodsFlag;

	@Column(name="eci_modify_mark")
	private String eciModifyMark;

	@Column(name="exg_version")
	private Integer exgVersion;

	@Column(name="g_model")
	private String gModel;

	@Column(name="g_no")
	private String gNo;

	@Column(name="goods_name")
	private String goodsName;

	@Column(name="hs_code")
	private String hsCode;

	@Column(name="org_id")
	private String orgId;

	@Column(name="origin_country")
	private String originCountry;

	@Column(name="qty_1")
	private BigDecimal qty1;

	@Column(name="qty_2")
	private BigDecimal qty2;

	private String remark;

	private Integer status;

	private String unit;

	@Column(name="unit_1")
	private String unit1;

	@Column(name="unit_2")
	private String unit2;

	@Column(name="use_to")
	private String useTo;

	@Column(name="warehouse_id")
	private String warehouseId;

	public DeliverGoodsApplicationGood() {
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

	public String getCurr() {
		return this.curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public BigDecimal getDecPrice() {
		return this.decPrice;
	}

	public void setDecPrice(BigDecimal decPrice) {
		this.decPrice = decPrice;
	}

	public BigDecimal getDecQty() {
		return this.decQty;
	}

	public void setDecQty(BigDecimal decQty) {
		this.decQty = decQty;
	}

	public BigDecimal getDecTotal() {
		return this.decTotal;
	}

	public void setDecTotal(BigDecimal decTotal) {
		this.decTotal = decTotal;
	}

	public String getDutyMode() {
		return this.dutyMode;
	}

	public void setDutyMode(String dutyMode) {
		this.dutyMode = dutyMode;
	}

	public String getEciGoodsFlag() {
		return this.eciGoodsFlag;
	}

	public void setEciGoodsFlag(String eciGoodsFlag) {
		this.eciGoodsFlag = eciGoodsFlag;
	}

	public String getEciModifyMark() {
		return this.eciModifyMark;
	}

	public void setEciModifyMark(String eciModifyMark) {
		this.eciModifyMark = eciModifyMark;
	}

	public Integer getExgVersion() {
		return this.exgVersion;
	}

	public void setExgVersion(Integer exgVersion) {
		this.exgVersion = exgVersion;
	}

	public String getGModel() {
		return this.gModel;
	}

	public void setGModel(String gModel) {
		this.gModel = gModel;
	}

	public String getGNo() {
		return this.gNo;
	}

	public void setGNo(String gNo) {
		this.gNo = gNo;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getHsCode() {
		return this.hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOriginCountry() {
		return this.originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public BigDecimal getQty1() {
		return this.qty1;
	}

	public void setQty1(BigDecimal qty1) {
		this.qty1 = qty1;
	}

	public BigDecimal getQty2() {
		return this.qty2;
	}

	public void setQty2(BigDecimal qty2) {
		this.qty2 = qty2;
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

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit1() {
		return this.unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public String getUnit2() {
		return this.unit2;
	}

	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

	public String getUseTo() {
		return this.useTo;
	}

	public void setUseTo(String useTo) {
		this.useTo = useTo;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

}