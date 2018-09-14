package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.rec.util.valid.ValidBatchConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;

public class SendDeliveryDetail extends BaseEntity{

	private static final long serialVersionUID = 7020027876029496183L;

	@Id
	private String deliveryDetailId;

	/**
     * 发货id
     */
    private String deliveryId;
   
    /**
     * skuid
     */
    @NotNull(message="{valid_send_delivery_detail_skuId_notnull}",groups={ValidSave.class})
    @Length(max=64,message="{valid_send_delivery_detail_skuId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String skuId;

    /**
     * 批次号
     */
    @Length(max=32,message="{valid_send_delivery_detail_batch_no_length}",groups={ValidSave.class,ValidUpdate.class})
    private String batchNo;

    /**
     * 包装id
     */
    private String packId;

    /**
     * 订单数量
     */
    @NotNull(message="{valid_send_delivery_detail_qty_notnull}",groups={ValidSave.class})
    @Max(value=Integer.MAX_VALUE,message="{valid_send_delivery_detail_orderQty_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_send_delivery_detail_orderQty_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderQty;

    /**
     * 订单重量
     */
    @DecimalMax(value="999999999",message="{valid_send_delivery_detail_orderWeight_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_delivery_detail_orderWeight_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderWeight;

    /**
     * 订单体积
     */
    @DecimalMax(value="999999999",message="{valid_send_delivery_detail_orderVolume_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_delivery_detail_orderVolume_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderVolume;
    
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
    private Double pickVolume ;

    /**
     * 企业id
     */
    @Length(max=64,message="{valid_send_delivery_detail_orgId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String orgId;

    /**
     * 仓库id
     */
    @Length(max=64,message="{valid_send_delivery_detail_warehouseId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String warehouseId;

    /**
     * 基本单位
     */
    private String measureUnit;
    
    /**
     * 复核数量
     */
    private Double reviewQty;


    private Integer deliveryDetailId2;
      

    public String getDeliveryDetailId() {
        return deliveryDetailId;
    }

    public void setDeliveryDetailId(String deliveryDetailId) {
        this.deliveryDetailId = deliveryDetailId == null ? null : deliveryDetailId.trim();
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId == null ? null : deliveryId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

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

    public void setOrderWeight(Double orderWeight) throws Exception {
        this.orderWeight = NumberUtil.round(orderWeight,2);
    }

    public Double getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(Double orderVolume) throws Exception {
        this.orderVolume = NumberUtil.round(orderVolume,6);
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

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
    }
    
    public Integer getDeliveryDetailId2() {
        return deliveryDetailId2;
    }

    public void setDeliveryDetailId2(Integer deliveryDetailId2) {
        this.deliveryDetailId2 = deliveryDetailId2;
    }

	public Double getPickWeight() {
		return pickWeight;
	}

	public void setPickWeight(Double pickWeight) throws Exception {
		this.pickWeight = NumberUtil.round(pickWeight,2);
	}

	public Double getPickVolume() {
		return pickVolume;
	}

	public void setPickVolume(Double pickVolume) throws Exception {
		this.pickVolume = NumberUtil.round(pickVolume,6);
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
	

	public Double getReviewQty() {
		return reviewQty;
	}
	

	public void setReviewQty(Double reviewQty) {
		this.reviewQty = reviewQty;
	}
	

	public void calPickQty(double qty){
		this.pickQty = this.pickQty == null ? qty:this.pickQty+qty;
	}
    
	public void calPickWeight(double weight) throws Exception{
		double pickWeight= this.pickWeight == null ? weight:NumberUtil.add(this.pickWeight,weight);
		setPickWeight(pickWeight);
	}
	
	public void calPickVolume(double volume) throws Exception{
		double pickVolume= this.pickVolume == null ? volume:NumberUtil.add(this.pickVolume,volume);
		setPickVolume(pickVolume);
	}
	
    public void defaultValue() throws Exception{
    	this.orderQty = this.orderQty == null?0:this.orderQty;
    	setOrderWeight(this.orderWeight == null?0.0:this.orderWeight);
    	setOrderVolume(this.orderVolume == null?0.0:this.orderVolume);
    	this.pickQty = this.pickQty == null?0:this.pickQty;
    	setPickWeight(this.pickWeight == null?0.0:this.pickWeight);
    	setPickVolume(this.pickVolume == null?0.0:this.pickVolume);
    }
    
}