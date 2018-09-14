package com.yunkouan.wms.modules.send.entity;

import com.yunkouan.base.BaseEntity;

public class SendDeliveryMaterial extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1880586473096368146L;
	/**
	 * 发货单id
	 */
	private String deliveryId;
	/**
	 * 辅材id
	 */
    private String materialId;
	/**
	 * 数量
	 */
    private Integer qty;
	/**
	 * 企业id
	 */
    private String orgId;
	/**
	 * 仓库id
	 */
    private String warehouseId;
	public String getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

    
}