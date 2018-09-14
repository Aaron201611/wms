package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class SendPickDetail extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5894630930735940823L;

	@Id
	private String pickDetailId;

    private String pickId;

    /**
     * skuid
     */
    @NotNull(message="{valid_send_pick_detail_skuId_notnull}",groups={ValidSave.class})
    @Length(max=64,message="{valid_send_pick_detail_skuId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String skuId;
    /**
     * 企业id
     */
    private String orgId;
    /**
     * 仓库id
     */
    private String warehouseId;
    /**
     * 批次号（空，不可用）
     */
    @Deprecated
    private String batchNo;
    /**
     * 货主
     */
    private String owner;
    /**
     * 包装id
     */
    private String packId;
    /**
     * 订单数量
     */
    private Double orderQty;
    /**
     * 订单重量
     */
    private Double orderWeight;
    /**
     * 订单体积
     */
    private Double orderVolume;
    /**
     * 发货单id
     */
    private String deliveryId;
    /**
     * 发货单明细id
     */
    private String deliveryDetailId;
    /**
     * 拣货数量
     */
    private Double pickQty;
    /**
     * 拣货重量
     */
    private Double pickWeight;
    /**
     * 拣货体积
     */
    private Double pickVolume;
    /**
     * 基本单位
     */
    private String measureUnit;
    /**
     * 
     */
    private Integer pickDetailId2;

    public String getPickDetailId() {
        return pickDetailId;
    }

    public void setPickDetailId(String pickDetailId) {
        this.pickDetailId = pickDetailId == null ? null : pickDetailId.trim();
    }

    public String getPickId() {
        return pickId;
    }

    public void setPickId(String pickId) {
        this.pickId = pickId == null ? null : pickId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
    }

    /** 
    * @Title: getBatchNo 
    * @Description: （空，不可用）
    * @auth tphe06
    * @time 2018 2018年8月28日 下午3:01:24
    * @return
    * String
    */
    @Deprecated
    public String getBatchNo() {
        return batchNo;
    }

    /** 
    * @Title: setBatchNo 
    * @Description: （空，不可用）
    * @auth tphe06
    * @time 2018 2018年8月28日 下午3:01:28
    * @param batchNo
    * void
    */
    @Deprecated
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
    }

    public Double getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(Double orderWeight) {
        this.orderWeight = orderWeight;
    }

    public Double getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(Double orderVolume) {
        this.orderVolume = orderVolume;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId == null ? null : deliveryId.trim();
    }

    public String getDeliveryDetailId() {
        return deliveryDetailId;
    }

    public void setDeliveryDetailId(String deliveryDetailId) {
        this.deliveryDetailId = deliveryDetailId == null ? null : deliveryDetailId.trim();
    }

    public Double getOrderQty() {
		return orderQty;
	}
	

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	

	public Double getPickQty() {
		return pickQty;
	}
	

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}
	

	public Double getPickWeight() {
        return pickWeight;
    }

    public void setPickWeight(Double pickWeight) {
        this.pickWeight = pickWeight;
    }

    public Double getPickVolume() {
        return pickVolume;
    }

    public void setPickVolume(Double pickVolume) {
        this.pickVolume = pickVolume;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
    }

    public Integer getPickDetailId2() {
        return pickDetailId2;
    }

    public void setPickDetailId2(Integer pickDetailId2) {
        this.pickDetailId2 = pickDetailId2;
    }

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
    
    
}