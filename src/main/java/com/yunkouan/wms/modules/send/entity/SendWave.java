package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class SendWave extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2184694037397278471L;

	@Id
	private String waveId;
	/**
	 * 波次单号
	 */
    private String waveNo;
	/**
	 * 波次单状态
	 */
    private Integer waveStatus;
	/**
	 * 企业id
	 */
    private String orgId;
	/**
	 * 仓库id
	 */
    private String warehouseId;
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
	 * 订单货品种数
	 */
    private Integer orderSkuQty;
	/**
	 * 发货单数量
	 */
    private Integer deliveryAmount;
	/**
	 * 备注
	 */
    @Length(max=2048,message="{valid_send_wave_remark_length}",groups={ValidSave.class,ValidUpdate.class})
    private String remark;
	/**
	 * 
	 */
    private Integer waveId2;
    /**
     * 单据类型，发货单类型
     */
    private Integer deliveryType;

    public String getWaveId() {
        return waveId;
    }

    public void setWaveId(String waveId) {
        this.waveId = waveId == null ? null : waveId.trim();
    }

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo == null ? null : waveNo.trim();
    }

    public Integer getWaveStatus() {
        return waveStatus;
    }

    public void setWaveStatus(Integer waveStatus) {
        this.waveStatus = waveStatus;
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

    public Integer getWaveId2() {
        return waveId2;
    }

    public void setWaveId2(Integer waveId2) {
        this.waveId2 = waveId2;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getDeliveryAmount() {
		return deliveryAmount;
	}
	

	public void setDeliveryAmount(Integer deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
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

	public Integer getOrderSkuQty() {
		return orderSkuQty;
	}

	public void setOrderSkuQty(Integer orderSkuQty) {
		this.orderSkuQty = orderSkuQty;
	}
    
	public void defaultValue() throws Exception{
    	this.orderQty = this.orderQty == null?0:this.orderQty;
    	setOrderWeight(this.orderWeight == null?0.0:this.orderWeight);
    	setOrderVolume(this.orderVolume == null?0.0:this.orderVolume);
    	this.pickQty = this.pickQty == null?0:this.pickQty;
    	setPickWeight(this.pickWeight == null?0.0:this.pickWeight);
    	setPickVolume(this.pickVolume == null?0.0:this.pickVolume);
    }
	
	public void calPickQty(double pickQty){
		this.pickQty = this.pickQty == null?pickQty:this.pickQty+pickQty;
	}
    public void calPickdWeight(Double weight) throws Exception{
    	double pickWeight = this.pickWeight == null?weight:NumberUtil.add(this.pickWeight,weight);
    	setPickWeight(pickWeight);
    }
    public void calPickVolume(Double volume) throws Exception{
    	double pickVolume = this.pickVolume == null?volume:NumberUtil.add(this.pickVolume,volume);
    	setPickVolume(pickVolume);
    }

	public Integer getDeliveryType() {
		return deliveryType;
	}
	

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}
	
}