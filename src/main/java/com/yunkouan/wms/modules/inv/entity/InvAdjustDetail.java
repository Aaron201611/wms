package com.yunkouan.wms.modules.inv.entity;

import com.yunkouan.base.BaseEntity;

public class InvAdjustDetail  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:59:03<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = -1695660569833965495L;

	private String adjustDetailId;

    private String adjustId;

    private String skuId;

    private String batchNo;

    private String locationId;

    private Double stockQty;

    private Double differenceQty;

    private Double realQty;

    private Integer adjustType;

    private String orgId;

    private String warehouseId;

    private String asnId;
    
    private String asnDetailId;

    private Integer adjustDetailId2;

    public String getAdjustDetailId() {
        return adjustDetailId;
    }

    public void setAdjustDetailId(String adjustDetailId) {
        this.adjustDetailId = adjustDetailId == null ? null : adjustDetailId.trim();
    }

    public String getAdjustId() {
        return adjustId;
    }

    public void setAdjustId(String adjustId) {
        this.adjustId = adjustId == null ? null : adjustId.trim();
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
    }


    public Integer getAdjustType() {
        return adjustType;
    }

    public void setAdjustType(Integer adjustType) {
        this.adjustType = adjustType;
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

    public Integer getAdjustDetailId2() {
        return adjustDetailId2;
    }

    public void setAdjustDetailId2(Integer adjustDetailId2) {
        this.adjustDetailId2 = adjustDetailId2;
    }

	/**
	 * 属性 asnId getter方法
	 * @return 属性asnId
	 * @author 王通<br/>
	 */
	public String getAsnId() {
		return asnId;
	}

	/**
	 * 属性 asnId setter方法
	 * @param asnId 设置属性asnId的值
	 * @author 王通<br/>
	 */
	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}

	/**
	 * 属性 asnDetailId getter方法
	 * @return 属性asnDetailId
	 * @author 王通<br/>
	 */
	public String getAsnDetailId() {
		return asnDetailId;
	}

	/**
	 * 属性 asnDetailId setter方法
	 * @param asnDetailId 设置属性asnDetailId的值
	 * @author 王通<br/>
	 */
	public void setAsnDetailId(String asnDetailId) {
		this.asnDetailId = asnDetailId;
	}

	public Double getStockQty() {
		return stockQty;
	}
	

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	

	public Double getDifferenceQty() {
		return differenceQty;
	}
	

	public void setDifferenceQty(Double differenceQty) {
		this.differenceQty = differenceQty;
	}
	

	public Double getRealQty() {
		return realQty;
	}
	

	public void setRealQty(Double realQty) {
		this.realQty = realQty;
	}
	
}