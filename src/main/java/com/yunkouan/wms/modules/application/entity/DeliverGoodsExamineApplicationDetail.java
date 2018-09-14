package com.yunkouan.wms.modules.application.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the deliver_goods_examine_application_detail database table.
 * 
 */
@Entity
@Table(name="deliver_goods_examine_application_detail")
@NamedQuery(name="DeliverGoodsExamineApplicationDetail.findAll", query="SELECT d FROM DeliverGoodsExamineApplicationDetail d")
public class DeliverGoodsExamineApplicationDetail extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="dec_qty")
	private BigDecimal decQty;

	@Column(name="examine_application_id")
	private String examineApplicationId;

	@Column(name="goods_sku_id")
	private String goodsSkuId;

	@Column(name="gross_wt")
	private BigDecimal grossWt;

	@Column(name="org_id")
	private String orgId;

	@Column(name="pack_no")
	private Integer packNo;

	@Column(name="sku_id")
	private String skuId;
	
	private String kernelNoRel;

	private Integer status;

	private String unit;

	@Column(name="warehouse_id")
	private String warehouseId;

	public DeliverGoodsExamineApplicationDetail() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getDecQty() {
		return this.decQty;
	}

	public void setDecQty(BigDecimal decQty) {
		this.decQty = decQty;
	}

	public String getExamineApplicationId() {
		return this.examineApplicationId;
	}

	public void setExamineApplicationId(String examineApplicationId) {
		this.examineApplicationId = examineApplicationId;
	}

	public String getGoodsSkuId() {
		return this.goodsSkuId;
	}

	public void setGoodsSkuId(String goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public BigDecimal getGrossWt() {
		return this.grossWt;
	}

	public void setGrossWt(BigDecimal grossWt) {
		this.grossWt = grossWt;
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

	public String getSkuId() {
		return this.skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
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

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getKernelNoRel() {
		return kernelNoRel;
	}

	public void setKernelNoRel(String kernelNoRel) {
		this.kernelNoRel = kernelNoRel;
	}

}