/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:31:18<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.entity;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;

/**
 * 上架单货品库位明细<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:31:18<br/>
 * @author andy wang<br/>
 */
public class RecPutawayLocation extends BaseEntity {


	/**
	 * 创建日期:<br/> 2017年2月8日 下午6:29:29<br/>
	 * @author andy wang<br/>
	 */
	private static final long serialVersionUID = -3142732691194818841L;

	/**
     * 上架单货品库位明细主键
     * @author andy<br/>
     */
	@Id
    private String putawayLocationId;

    /**
     * 库位编号
     * @author andy<br/>
     */
    @NotNull(message="{valid_rec_putawayLocation_locationNull}",groups={ValidConfirm.class})
    private String locationId;
    /**
     * 上架明细编号
     * @author andy<br/>
     */
    @NotNull(message="{valid_rec_putawayLocation_ptwDetailId}",groups={ValidConfirm.class,})
    private String putawayDetailId;

    /**
     * 上架数量
     * @author andy<br/>
     */
    @NotNull(message="{valid_rec_putawayLocation_putawayQtyNull}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    @Max(value=Integer.MAX_VALUE,message="{valid_rec_putawayLocation_qty_max}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_rec_putawayLocation_qty_min}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    private Double putawayQty;

    /**
     * 上架重量
     * @author andy<br/>
     */
    @DecimalMax(value=Double.MAX_VALUE+"",message="{valid_rec_putawayLocation_weight_max}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_rec_putawayLocation_weight_min}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    private Double putawayWeight;
    
    /**
     * 上架体积
     * @author andy<br/>
     */
    @DecimalMax(value=Double.MAX_VALUE+"",message="{valid_rec_putawayLocation_volume_max}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_rec_putawayLocation_volume_min}",groups={ValidConfirm.class,ValidSave.class,ValidUpdate.class})
    private Double putawayVolume;

    /**
     * 上架类型
     * @author andy<br/>
     */
    private Integer putawayType;

    /**
     * 包装id
     * @author andy<br/>
     */
    private String packId;

    /**
     * 计量单位
     * @author andy<br/>
     */
    private String measureUnit;

    /**
     * 备用id
     * @author andy<br/>
     */
    private Integer putawayLocationId2;

    /**
	 * 仓库id
	 * @version 2017年2月27日下午8:43:22<br/>
	 * @author andy wang<br/>
	 */
    private String warehouseId;
    
    /**
	 * 企业id
	 * @version 2017年2月27日下午8:43:26<br/>
	 * @author andy wang<br/>
	 */
    private String orgId;
    
    /* getset **************************************/
    
	/**
	 * 属性 putawayLocationId getter方法
	 * @return 属性putawayLocationId
	 * @author andy wang<br/>
	 */
	public String getPutawayLocationId() {
		return putawayLocationId;
	}

	/**
	 * 属性 putawayLocationId setter方法
	 * @param putawayLocationId 设置属性putawayLocationId的值
	 * @author andy wang<br/>
	 */
	public void setPutawayLocationId(String putawayLocationId) {
		this.putawayLocationId = putawayLocationId;
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

	public Double getPutawayQty() {
		return putawayQty;
	}
	

	public void setPutawayQty(Double putawayQty) {
		this.putawayQty = putawayQty;
	}
	

	/**
	 * 属性 putawayWeight getter方法
	 * @return 属性putawayWeight
	 * @author andy wang<br/>
	 */
	public Double getPutawayWeight() {
		return putawayWeight;
	}

	/**
	 * 属性 putawayWeight setter方法
	 * @param putawayWeight 设置属性putawayWeight的值
	 * @author andy wang<br/>
	 */
	public void setPutawayWeight(Double putawayWeight) {
		this.putawayWeight = putawayWeight;
	}

	/**
	 * 属性 putawayVolume getter方法
	 * @return 属性putawayVolume
	 * @author andy wang<br/>
	 */
	public Double getPutawayVolume() {
		return putawayVolume;
	}

	/**
	 * 属性 putawayVolume setter方法
	 * @param putawayVolume 设置属性putawayVolume的值
	 * @author andy wang<br/>
	 */
	public void setPutawayVolume(Double putawayVolume) {
		this.putawayVolume = putawayVolume;
	}

	/**
	 * 属性 putawayType getter方法
	 * @return 属性putawayType
	 * @author andy wang<br/>
	 */
	public Integer getPutawayType() {
		return putawayType;
	}

	/**
	 * 属性 putawayType setter方法
	 * @param putawayType 设置属性putawayType的值
	 * @author andy wang<br/>
	 */
	public void setPutawayType(Integer putawayType) {
		this.putawayType = putawayType;
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
	 * 属性 putawayLocationId2 getter方法
	 * @return 属性putawayLocationId2
	 * @author andy wang<br/>
	 */
	public Integer getPutawayLocationId2() {
		return putawayLocationId2;
	}

	/**
	 * 属性 putawayLocationId2 setter方法
	 * @param putawayLocationId2 设置属性putawayLocationId2的值
	 * @author andy wang<br/>
	 */
	public void setPutawayLocationId2(Integer putawayLocationId2) {
		this.putawayLocationId2 = putawayLocationId2;
	}
    
}