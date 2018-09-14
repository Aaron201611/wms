package com.yunkouan.wms.modules.inv.entity;

import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.wms.modules.inv.util.valid.ValidStockShift;

public class InvLog  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:58:53<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = -6917377874961493216L;

	private String logId;

	@NotNull(message="{valid_stock_shift_person_is_null}",groups={ValidStockShift.class})
    private String opPerson;

    private Integer opType;

    private String note;

    private String invoiceBill;

    private Integer logType;

    private String orgId;

    private String warehouseId;

    private String locationId;
    
    private String skuId;
    
    private String batchNo;
    
    private String owner;
    
    private Double qty;
    
    private Double volume;
    
    private Double weight;
    
    private Integer skuStatus;

	public Double getQty() {
		return qty;
	}
	

	public void setQty(Double qty) {
		this.qty = qty;
	}
	

	/**
	 * 属性 volume getter方法
	 * @return 属性volume
	 * @author 王通<br/>
	 */
	public Double getVolume() {
		return volume;
	}

	/**
	 * 属性 volume setter方法
	 * @param volumn 设置属性volume的值
	 * @author 王通<br/>
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/**
	 * 属性 weight getter方法
	 * @return 属性weight
	 * @author 王通<br/>
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * 属性 weight setter方法
	 * @param weight 设置属性weight的值
	 * @author 王通<br/>
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	private Integer logId2;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public String getOpPerson() {
        return opPerson;
    }

    public void setOpPerson(String opPerson) {
        this.opPerson = opPerson == null ? null : opPerson.trim();
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getInvoiceBill() {
        return invoiceBill;
    }

    public void setInvoiceBill(String invoiceBill) {
        this.invoiceBill = invoiceBill == null ? null : invoiceBill.trim();
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
    }

    public Integer getLogId2() {
        return logId2;
    }

    public void setLogId2(Integer logId2) {
        this.logId2 = logId2;
    }

	/**
	 * 属性 skuId getter方法
	 * @return 属性skuId
	 * @author 王通<br/>
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * 属性 skuId setter方法
	 * @param skuId 设置属性skuId的值
	 * @author 王通<br/>
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * 属性 ownerMerchantId getter方法
	 * @return 属性ownerMerchantId
	 * @author 王通<br/>
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 属性 ownerMerchantId setter方法
	 * @param ownerMerchantId 设置属性ownerMerchantId的值
	 * @author 王通<br/>
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 属性 batchNo getter方法
	 * @return 属性batchNo
	 * @author 王通<br/>
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * 属性 batchNo setter方法
	 * @param batchNo 设置属性batchNo的值
	 * @author 王通<br/>
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}


	public Integer getSkuStatus() {
		return skuStatus;
	}


	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
	}

}