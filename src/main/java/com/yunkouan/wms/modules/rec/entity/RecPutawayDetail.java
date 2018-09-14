/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:32:25<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.entity;


import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

/**
 * 上架单明细实体类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:32:25<br/>
 * @author andy wang<br/>
 */
public class RecPutawayDetail extends BaseEntity {


	/**
	 * 创建日期:<br/> 2017年2月8日 下午6:15:46<br/>
	 * @author andy wang<br/>
	 */
	private static final long serialVersionUID = -8584875632481482471L;

	/**
     * 上架单明细主键
	 * @author andy wang<br/>
     */
	@Id
    private String putawayDetailId;
    
    /**
     * 上架单主键
	 * @author andy wang<br/>
     */
    private String putawayId;

    /**
     * 货品代码
	 * @author andy wang<br/>
     */
    private String skuId;

    /**
     * 组织编号
	 * @author andy wang<br/>
     */
    private String orgId;

    /**
     * 仓库代码
	 * @author andy wang<br/>
     */
    private String warehouseId;

    /**
     * 包装id
	 * @author andy wang<br/>
     */
    private String packId;
    
    /**
     * 批次号
	 * @author andy wang<br/>
     */
    private String batchNo;

    /**标识货品是良品还是不良品，不是打开，生效，失效，取消这些状态！！！**/
    private Integer skuStatus;

    /**
     * 计量单位
	 * @author andy wang<br/>
     */
    private String measureUnit;

    /**
     * ASN主键
	 * @author andy wang<br/>
     */
    private String asnId;

    /**
     * ASN明细主键
	 * @author andy wang<br/>
     */
    @NotNull(message="{valid_rec_putawayDetail_asnDetailId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String asnDetailId;
    
    /**
     * 实际上架数量
	 * @author andy wang<br/>
     */
    private Double realPutawayQty;

    /**
     * 实际上架重量
	 * @author andy wang<br/>
     */
    private Double realPutawayWeight;

    /**
     * 实际上架体积
	 * @author andy wang<br/>
     */
    private Double realPutawayVolume;
    
    /**
     * 计划上架数量
	 * @author andy wang<br/>
     */
    private Double planPutawayQty;
    
    /**
     * 计划上架重量
	 * @author andy wang<br/>
     */
    private Double planPutawayWeight;

    /**
     * 计划上架体积
	 * @author andy wang<br/>
     */
    private Double planPutawayVolume;

    /**
     * 最后修改人
	 * @author andy wang<br/>
     */
    private Integer putawayDetailId2;
    
    /**
	 * 父上架单明细id
	 * @version 2017年3月2日上午10:17:59<br/>
	 * @author andy wang<br/>
	 */
    private String parentPutawayDetailId;
    
    /**
	 * 货主id
	 * @version 2017年3月14日下午4:22:24<br/>
	 * 林总组织讨论后决定添加
	 * 讨论地点：张代龙桌子
	 * 讨论结果：全体通过
	 * @author andy wang<br/>
	 */
    private String owner;
    
    
    /**
	 * 库位id
	 * @version 2017年3月15日下午7:32:59<br/>
	 * @author andy wang<br/>
	 */
    @NotNull(message="{valid_rec_putawayDetail_locationId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String locationId;
    
    /* getset ********************************************/
    
	/**
	 * 属性 putawayDetailId getter方法
	 * @return 属性putawayDetailId
	 * @author andy wang<br/>
	 */
	public String getPutawayDetailId() {
		return putawayDetailId;
	}

	/**
	 * 属性 putawayDetailId setter方法
	 * @param putawayDetailId 设置属性putawayDetailId的值
	 * @author andy wang<br/>
	 */
	public void setPutawayDetailId(String putawayDetailId) {
		this.putawayDetailId = putawayDetailId;
	}

	/**
	 * 属性 putawayId getter方法
	 * @return 属性putawayId
	 * @author andy wang<br/>
	 */
	public String getPutawayId() {
		return putawayId;
	}

	/**
	 * 属性 putawayId setter方法
	 * @param putawayId 设置属性putawayId的值
	 * @author andy wang<br/>
	 */
	public void setPutawayId(String putawayId) {
		this.putawayId = putawayId;
	}

	/**
	 * 属性 skuId getter方法
	 * @return 属性skuId
	 * @author andy wang<br/>
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * 属性 skuId setter方法
	 * @param skuId 设置属性skuId的值
	 * @author andy wang<br/>
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * 属性 orgId getter方法
	 * @return 属性orgId
	 * @author andy wang<br/>
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * 属性 orgId setter方法
	 * @param orgId 设置属性orgId的值
	 * @author andy wang<br/>
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 属性 warehouseId getter方法
	 * @return 属性warehouseId
	 * @author andy wang<br/>
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * 属性 warehouseId setter方法
	 * @param warehouseId 设置属性warehouseId的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * 属性 packId getter方法
	 * @return 属性packId
	 * @author andy wang<br/>
	 */
	public String getPackId() {
		return packId;
	}

	/**
	 * 属性 packId setter方法
	 * @param packId 设置属性packId的值
	 * @author andy wang<br/>
	 */
	public void setPackId(String packId) {
		this.packId = packId;
	}

	/**
	 * 属性 batchNo getter方法
	 * @return 属性batchNo
	 * @author andy wang<br/>
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * 属性 batchNo setter方法
	 * @param batchNo 设置属性batchNo的值
	 * @author andy wang<br/>
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 属性 measureUnit getter方法
	 * @return 属性measureUnit
	 * @author andy wang<br/>
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * 属性 measureUnit setter方法
	 * @param measureUnit 设置属性measureUnit的值
	 * @author andy wang<br/>
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	/**
	 * 属性 asnId getter方法
	 * @return 属性asnId
	 * @author andy wang<br/>
	 */
	public String getAsnId() {
		return asnId;
	}

	/**
	 * 属性 asnId setter方法
	 * @param asnId 设置属性asnId的值
	 * @author andy wang<br/>
	 */
	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}

	/**
	 * 属性 asnDetailId getter方法
	 * @return 属性asnDetailId
	 * @author andy wang<br/>
	 */
	public String getAsnDetailId() {
		return asnDetailId;
	}

	/**
	 * 属性 asnDetailId setter方法
	 * @param asnDetailId 设置属性asnDetailId的值
	 * @author andy wang<br/>
	 */
	public void setAsnDetailId(String asnDetailId) {
		this.asnDetailId = asnDetailId;
	}

	/**
	 * 属性 realPutawayWeight getter方法
	 * @return 属性realPutawayWeight
	 * @author andy wang<br/>
	 */
	public Double getRealPutawayWeight() {
		return realPutawayWeight;
	}

	/**
	 * 属性 realPutawayWeight setter方法
	 * @param realPutawayWeight 设置属性realPutawayWeight的值
	 * @author andy wang<br/>
	 */
	public void setRealPutawayWeight(Double realPutawayWeight) {
		this.realPutawayWeight = realPutawayWeight;
	}

	/**
	 * 属性 realPutawayVolume getter方法
	 * @return 属性realPutawayVolume
	 * @author andy wang<br/>
	 */
	public Double getRealPutawayVolume() {
		return realPutawayVolume;
	}

	/**
	 * 属性 realPutawayVolume setter方法
	 * @param realPutawayVolume 设置属性realPutawayVolume的值
	 * @author andy wang<br/>
	 */
	public void setRealPutawayVolume(Double realPutawayVolume) {
		this.realPutawayVolume = realPutawayVolume;
	}

	/**
	 * 属性 planPutawayWeight getter方法
	 * @return 属性planPutawayWeight
	 * @author andy wang<br/>
	 */
	public Double getPlanPutawayWeight() {
		return planPutawayWeight;
	}

	/**
	 * 属性 planPutawayWeight setter方法
	 * @param planPutawayWeight 设置属性planPutawayWeight的值
	 * @author andy wang<br/>
	 */
	public void setPlanPutawayWeight(Double planPutawayWeight) {
		this.planPutawayWeight = planPutawayWeight;
	}

	public Double getRealPutawayQty() {
		return realPutawayQty;
	}
	

	public void setRealPutawayQty(Double realPutawayQty) {
		this.realPutawayQty = realPutawayQty;
	}
	

	public Double getPlanPutawayQty() {
		return planPutawayQty;
	}
	

	public void setPlanPutawayQty(Double planPutawayQty) {
		this.planPutawayQty = planPutawayQty;
	}
	

	/**
	 * 属性 planPutawayVolume getter方法
	 * @return 属性planPutawayVolume
	 * @author andy wang<br/>
	 */
	public Double getPlanPutawayVolume() {
		return planPutawayVolume;
	}

	/**
	 * 属性 planPutawayVolume setter方法
	 * @param planPutawayVolume 设置属性planPutawayVolume的值
	 * @author andy wang<br/>
	 */
	public void setPlanPutawayVolume(Double planPutawayVolume) {
		this.planPutawayVolume = planPutawayVolume;
	}

	/**
	 * 属性 putawayDetailId2 getter方法
	 * @return 属性putawayDetailId2
	 * @author andy wang<br/>
	 */
	public Integer getPutawayDetailId2() {
		return putawayDetailId2;
	}

	/**
	 * 属性 putawayDetailId2 setter方法
	 * @param putawayDetailId2 设置属性putawayDetailId2的值
	 * @author andy wang<br/>
	 */
	public void setPutawayDetailId2(Integer putawayDetailId2) {
		this.putawayDetailId2 = putawayDetailId2;
	}

	/**
	 * 属性 parentPutawayDetailId getter方法
	 * @return 属性parentPutawayDetailId
	 * @author andy wang<br/>
	 */
	public String getParentPutawayDetailId() {
		return parentPutawayDetailId;
	}

	/**
	 * 属性 parentPutawayDetailId setter方法
	 * @param parentPutawayDetailId 设置属性parentPutawayDetailId的值
	 * @author andy wang<br/>
	 */
	public void setParentPutawayDetailId(String parentPutawayDetailId) {
		this.parentPutawayDetailId = parentPutawayDetailId;
	}

	/**
	 * 属性 owner getter方法
	 * @return 属性owner
	 * @author andy wang<br/>
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 属性 owner setter方法
	 * @param owner 设置属性owner的值
	 * @author andy wang<br/>
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 属性 locationId getter方法
	 * @return 属性locationId
	 * @author andy wang<br/>
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * 属性 locationId setter方法
	 * @param locationId 设置属性locationId的值
	 * @author andy wang<br/>
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Integer getSkuStatus() {
		return skuStatus;
	}

	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
	}
    
}