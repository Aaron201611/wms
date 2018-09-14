package com.yunkouan.wms.modules.application.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the deliver_goods_application_goods_sku database table.
 * 
 */
@Entity
@Table(name="deliver_goods_application_goods_sku")
@NamedQuery(name="DeliverGoodsApplicationGoodsSku.findAll", query="SELECT d FROM DeliverGoodsApplicationGoodsSku d")
public class DeliverGoodsApplicationGoodsSku extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="apply_goods_id")
	private String applyGoodsId;

	@Column(name="dec_price")
	private BigDecimal decPrice;

	@Column(name="dec_qty")
	private BigDecimal decQty;

	@Column(name="dec_total")
	private BigDecimal decTotal;
	
	private BigDecimal auditQty;
	
	private BigDecimal passAuditQty;
	
	private BigDecimal remainQty;

	@Column(name="org_id")
	private String orgId;

	@Column(name="qty_1")
	private BigDecimal qty1;

	@Column(name="qty_2")
	private BigDecimal qty2;

	private String remark;

	@Column(name="sku_id")
	private String skuId;
	
	private String hgGoodsNo;

	private Integer status;

	@Column(name="warehouse_id")
	private String warehouseId;

	public DeliverGoodsApplicationGoodsSku() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyGoodsId() {
		return this.applyGoodsId;
	}

	public void setApplyGoodsId(String applyGoodsId) {
		this.applyGoodsId = applyGoodsId;
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

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public BigDecimal getAuditQty() {
		return auditQty;
	}

	public void setAuditQty(BigDecimal auditQty) {
		this.auditQty = auditQty;
	}

	public BigDecimal getPassAuditQty() {
		return passAuditQty;
	}

	public void setPassAuditQty(BigDecimal passAuditQty) {
		this.passAuditQty = passAuditQty;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getHgGoodsNo() {
		return hgGoodsNo;
	}

	public void setHgGoodsNo(String hgGoodsNo) {
		this.hgGoodsNo = hgGoodsNo;
	}

	public BigDecimal getRemainQty() {
		return remainQty;
	}

	public void setRemainQty(BigDecimal remainQty) {
		this.remainQty = remainQty;
	}
	
}