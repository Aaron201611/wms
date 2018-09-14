package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class SendPickLocation extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3960920515041826994L;

	@Id
	private String pickLocationId;
	/**
	 * 拣货明细id
	 */
    private String pickDetailId;
	/**
	 * 库位id
	 */
    @Length(max=64,message="{valid_send_pick_location_locationId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String locationId;
	/**
	 * 拣货数量
	 */
    @Max(value=Integer.MAX_VALUE,message="{valid_send_pick_location_pickQty_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_send_pick_location_pickQty_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double pickQty;
	/**
	 * 拣货重量
	 */
    @DecimalMax(value="999999999",message="{valid_send_pick_location_pickWeight_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_pick_location_pickWeight_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double pickWeight;
	/**
	 * 拣货体积
	 */
    @DecimalMax(value="999999999",message="{valid_send_pick_location_pickVolume_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_pick_location_pickVolume_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double pickVolume;
	/**
	 * 拣货类型
	 */
    private Integer pickType;
	/**
	 * 包装id
	 */
    private String packId;
	/**
	 * 基本单位
	 */
    private String measureUnit;
	/**
	 * asn单明细id
	 */
    private String asnDetailId;
    
    /**
     * 批次号
     */
    private String batchNo;

    private Integer pickLocationId2;

    public String getPickLocationId() {
        return pickLocationId;
    }

    public void setPickLocationId(String pickLocationId) {
        this.pickLocationId = pickLocationId == null ? null : pickLocationId.trim();
    }

    public String getPickDetailId() {
        return pickDetailId;
    }

    public void setPickDetailId(String pickDetailId) {
        this.pickDetailId = pickDetailId == null ? null : pickDetailId.trim();
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
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

    public Integer getPickType() {
        return pickType;
    }

    public void setPickType(Integer pickType) {
        this.pickType = pickType;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
    }

    public String getAsnDetailId() {
        return asnDetailId;
    }

    public void setAsnDetailId(String asnDetailId) {
        this.asnDetailId = asnDetailId == null ? null : asnDetailId.trim();
    }

    public Integer getPickLocationId2() {
        return pickLocationId2;
    }

    public void setPickLocationId2(Integer pickLocationId2) {
        this.pickLocationId2 = pickLocationId2;
    }
    
    
    public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public void defaultValue(){
    	this.pickQty = this.pickQty == null?0:this.pickQty;
    	this.pickWeight = this.pickWeight== null?0.0:this.pickWeight;
    	this.pickVolume = this.pickVolume== null?0.0:this.pickVolume;
    }
}