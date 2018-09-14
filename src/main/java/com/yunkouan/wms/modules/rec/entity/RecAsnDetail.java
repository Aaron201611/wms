/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午2:59:32<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.rec.util.valid.ValidBatchConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;

/**
 * ASN单货品明细实体类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午2:59:32<br/>
 * @author andy wang<br/>
 */
public class RecAsnDetail extends BaseEntity {


	/**
	 * 创建日期:<br/> 2017年2月8日 下午5:59:09<br/>
	 * @author andy wang<br/>
	 */
	private static final long serialVersionUID = -5053677472824373360L;

	/**
     * ASN单货品明细主键
     * @author andy<br/>
     */
	@Id
	@NotNull(message="{valid_rec_asnDetail_asnDetailId_notnull}",groups={ValidSplit.class,ValidConfirm.class})
    private String asnDetailId;

    /**
     * ASN单主键
     * @author andy<br/>
     */
    private String asnId;
    
    /**
     * 货品代码
     * @author andy<br/>
     */
    @NotNull(message="{valid_rec_asnDetail_skuId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String skuId;

    /**标识货品是良品还是不良品，不是打开，生效，失效，取消这些状态！！！**/
    private Integer skuStatus;

    /**
     * 收货库位
     * @author andy<br/>
     */
    private String locationId;

    /**
     * 仓库代码
     * @author andy<br/>
     */
    private String warehouseId;
    
    /**
     * 组织编号
     * @author andy<br/>
     */
    private String orgId;
    
    /**
     * 批次号
     * @author andy<br/>
     */
    @NotNull(message="{valid_rec_asnDetail_batchNo_notnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=32,message="valid_rec_asnDetail_batchNo_length",groups={ValidSave.class,ValidUpdate.class})
    private String batchNo;

    /**
     * 计量单位
     * @author andy<br/>
     */
    private String measureUnit;

    /**
     * 收货数量
     * @author andy<br/>
     */
    @Max(value=999999,message="{valid_rec_asnDetail_receiveQty_max}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    @Min(value=0,message="{valid_rec_asnDetail_receiveQty_min}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    private Double receiveQty;
    
    /**
     * 收货重量
     * @author andy<br/>
     */
    @DecimalMax(value="999999999",message="{valid_rec_asnDetail_receiveWeight_max}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    @DecimalMin(value="0",message="{valid_rec_asnDetail_receiveWeight_min}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    private Double receiveWeight;

    /**
     * 收货体积
     * @author andy<br/>
     */
    @DecimalMax(value="999999999",message="{valid_rec_asnDetail_receiveVolume_max}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    @DecimalMin(value="0",message="{valid_rec_asnDetail_receiveVolume_min}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    private Double receiveVolume;
    
    /**
     * 订单数量
     * @author andy<br/>
     */
    @Max(value=Integer.MAX_VALUE,message="{valid_rec_asnDetail_orderQty_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_rec_asnDetail_orderQty_min}",groups={ValidSave.class,ValidUpdate.class})
    @NotNull(message="{valid_rec_asnDetail_orderQty_notnull}",groups={ValidSplit.class,ValidSave.class,ValidUpdate.class})
    private Double orderQty;

    /**
     * 订单重量
     * @author andy<br/>
     */
    @DecimalMax(value="999999999",message="{valid_rec_asnDetail_orderWeight_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_rec_asnDetail_orderWeight_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderWeight;

    /**
     * 订单体积
     * @author andy<br/>
     */
    @DecimalMax(value="999999999",message="{valid_rec_asnDetail_orderVolume_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_rec_asnDetail_orderVolume_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderVolume;
    
    /**
     * 包装id
     * @author andy<br/>
     */
    private String packId;
    
    /**
     * 生产日期
     * @author andy<br/>
     */
    private Date produceDate;

    /**
     * 过期日期
     * @author andy<br/>
     */
    private Date expiredDate;

    /**
     * 创建人
     * @author andy<br/>
     */
    private String createPerson;

    /**
     * 最后修改人
     * @author andy<br/>
     */
    private String updatePerson;
    
    /**
     * 备用id
     * @author andy<br/>
     */
    private Integer asnDetailId2;
    
    /**
	 * ASN单明细父id
	 * @version 2017年2月15日下午2:46:27<br/>
	 * @author andy wang<br/>
	 */
    private String parentAsnDetailId;

    /* getset *****************************************/
    
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
	 * 属性 receiveWeight getter方法
	 * @return 属性receiveWeight
	 * @author andy wang<br/>
	 */
	public Double getReceiveWeight() {
		return receiveWeight;
	}

	/**
	 * 属性 receiveWeight setter方法
	 * @param receiveWeight 设置属性receiveWeight的值
	 * @author andy wang<br/>
	 */
	public void setReceiveWeight(Double receiveWeight) {
		this.receiveWeight = receiveWeight;
	}

	/**
	 * 属性 receiveVolume getter方法
	 * @return 属性receiveVolume
	 * @author andy wang<br/>
	 */
	public Double getReceiveVolume() {
		return receiveVolume;
	}

	/**
	 * 属性 receiveVolume setter方法
	 * @param receiveVolume 设置属性receiveVolume的值
	 * @author andy wang<br/>
	 */
	public void setReceiveVolume(Double receiveVolume) {
		this.receiveVolume = receiveVolume;
	}

	/**
	 * 属性 orderWeight getter方法
	 * @return 属性orderWeight
	 * @author andy wang<br/>
	 */
	public Double getOrderWeight() {
		return orderWeight;
	}

	/**
	 * 属性 orderWeight setter方法
	 * @param orderWeight 设置属性orderWeight的值
	 * @author andy wang<br/>
	 */
	public void setOrderWeight(Double orderWeight) {
		this.orderWeight = orderWeight;
	}

	/**
	 * 属性 orderVolume getter方法
	 * @return 属性orderVolume
	 * @author andy wang<br/>
	 */
	public Double getOrderVolume() {
		return orderVolume;
	}

	/**
	 * 属性 orderVolume setter方法
	 * @param orderVolume 设置属性orderVolume的值
	 * @author andy wang<br/>
	 */
	public void setOrderVolume(Double orderVolume) {
		this.orderVolume = orderVolume;
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
	 * 属性 produceDate getter方法
	 * @return 属性produceDate
	 * @author andy wang<br/>
	 */
	public Date getProduceDate() {
		return produceDate;
	}

	/**
	 * 属性 produceDate setter方法
	 * @param produceDate 设置属性produceDate的值
	 * @author andy wang<br/>
	 */
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	/**
	 * 属性 expiredDate getter方法
	 * @return 属性expiredDate
	 * @author andy wang<br/>
	 */
	public Date getExpiredDate() {
		return expiredDate;
	}

	/**
	 * 属性 expiredDate setter方法
	 * @param expiredDate 设置属性expiredDate的值
	 * @author andy wang<br/>
	 */
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	/**
	 * 属性 createPerson getter方法
	 * @return 属性createPerson
	 * @author andy wang<br/>
	 */
	public String getCreatePerson() {
		return createPerson;
	}

	/**
	 * 属性 createPerson setter方法
	 * @param createPerson 设置属性createPerson的值
	 * @author andy wang<br/>
	 */
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	/**
	 * 属性 updatePerson getter方法
	 * @return 属性updatePerson
	 * @author andy wang<br/>
	 */
	public String getUpdatePerson() {
		return updatePerson;
	}

	/**
	 * 属性 updatePerson setter方法
	 * @param updatePerson 设置属性updatePerson的值
	 * @author andy wang<br/>
	 */
	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	/**
	 * 属性 asnDetailId2 getter方法
	 * @return 属性asnDetailId2
	 * @author andy wang<br/>
	 */
	public Integer getAsnDetailId2() {
		return asnDetailId2;
	}

	/**
	 * 属性 asnDetailId2 setter方法
	 * @param asnDetailId2 设置属性asnDetailId2的值
	 * @author andy wang<br/>
	 */
	public void setAsnDetailId2(Integer asnDetailId2) {
		this.asnDetailId2 = asnDetailId2;
	}

	/**
	 * 属性 parentAsnDetailId getter方法
	 * @return 属性parentAsnDetailId
	 * @author andy wang<br/>
	 */
	public String getParentAsnDetailId() {
		return parentAsnDetailId;
	}

	/**
	 * 属性 parentAsnDetailId setter方法
	 * @param parentAsnDetailId 设置属性parentAsnDetailId的值
	 * @author andy wang<br/>
	 */
	public void setParentAsnDetailId(String parentAsnDetailId) {
		this.parentAsnDetailId = parentAsnDetailId;
	}

	public Double getReceiveQty() {
		return receiveQty;
	}
	

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	public Double getOrderQty() {
		return orderQty;
	}

	public Integer getSkuStatus() {
		return skuStatus;
	}

	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
	}
	
	

}