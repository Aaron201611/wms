package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.yunkouan.base.BaseEntity;


/**
 * The persistent class for the meta_warehouse_setting database table.
 * 
 */
@Entity
@Table(name="meta_warehouse_setting")
@NamedQuery(name="MetaWarehouseSetting.findAll", query="SELECT m FROM MetaWarehouseSetting m")
public class MetaWarehouseSetting extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="wh_set_id")
	private String whSetId;

	@Column(name="is_sync_stock")
	private Boolean isSyncStock;

	@Column(name="limit_high")
	private double limitHigh;

	@Column(name="limit_low")
	private double limitLow;

	@Column(name="limit_type")
	private int limitType;

	@Column(name="org_id")
	private String orgId;

	@Column(name="out_of_limit_remind")
	private Boolean outOfLimitRemind;

	@Column(name="warehouse_id")
	private String warehouseId;
	
    /**寄件人（客商）id**/
    private String consignorId;
    /**consignor:仓库寄件人（客商）**/
    private String consignor;
    /**consignorAddress:寄件人联系地址**/
    private String consignorAddress;
    /**consignorPhone:寄件人联系电话**/
    private String consignorPhone;
    
    /**单据抬头**/
    private String receiptTitle;
    
    private Integer inputSkuNo;
    
    private Boolean manual;

	public MetaWarehouseSetting() {
	}

	public String getWhSetId() {
		return this.whSetId;
	}

	public void setWhSetId(String whSetId) {
		this.whSetId = whSetId;
	}
	

	public double getLimitHigh() {
		return this.limitHigh;
	}

	public void setLimitHigh(double limitHigh) {
		this.limitHigh = limitHigh;
	}

	public double getLimitLow() {
		return this.limitLow;
	}

	public void setLimitLow(double limitLow) {
		this.limitLow = limitLow;
	}

	public int getLimitType() {
		return this.limitType;
	}

	public void setLimitType(int limitType) {
		this.limitType = limitType;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Boolean getIsSyncStock() {
		return isSyncStock;
	}

	public void setIsSyncStock(Boolean isSyncStock) {
		this.isSyncStock = isSyncStock;
	}

	public Boolean getOutOfLimitRemind() {
		return outOfLimitRemind;
	}

	public void setOutOfLimitRemind(Boolean outOfLimitRemind) {
		this.outOfLimitRemind = outOfLimitRemind;
	}

	public String getConsignorId() {
		return consignorId;
	}

	public void setConsignorId(String consignorId) {
		this.consignorId = consignorId;
	}

	public String getConsignor() {
		return consignor;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}

	public String getConsignorAddress() {
		return consignorAddress;
	}

	public void setConsignorAddress(String consignorAddress) {
		this.consignorAddress = consignorAddress;
	}

	public String getConsignorPhone() {
		return consignorPhone;
	}

	public void setConsignorPhone(String consignorPhone) {
		this.consignorPhone = consignorPhone;
	}

	public Boolean getManual() {
		return manual;
	}

	public void setManual(Boolean manual) {
		this.manual = manual;
	}

	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	public Integer getInputSkuNo() {
		return inputSkuNo;
	}

	public void setInputSkuNo(Integer inputSkuNo) {
		this.inputSkuNo = inputSkuNo;
	}

	
}