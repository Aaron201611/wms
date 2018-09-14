package com.yunkouan.wms.modules.inv.entity;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;

public class InvCountDetail  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:58:57<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = -5732067052491197023L;

	@Id
	private String countDetailId;

    private String countId;

    private String locationId;

    private String orgId;

    private String warehouseId;

    private String skuId;

    private String batchNo;
    
    private String asnId;
    
    private String asnDetailId;

    private Integer result;

    private Double stockQty;

    private Double realCountQty;

    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date inStockDate;

    private Integer countDetailId2;

    public String getCountDetailId() {
        return countDetailId;
    }

    public void setCountDetailId(String countDetailId) {
        this.countDetailId = countDetailId == null ? null : countDetailId.trim();
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId == null ? null : countId.trim();
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
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

    public Integer getCountDetailId2() {
        return countDetailId2;
    }

    public void setCountDetailId2(Integer countDetailId2) {
        this.countDetailId2 = countDetailId2;
    }

	/**
	 * 属性 result getter方法
	 * @return 属性result
	 * @author 王通<br/>
	 */
	public Integer getResult() {
		return result;
	}

	/**
	 * 属性 result setter方法
	 * @param result 设置属性result的值
	 * @author 王通<br/>
	 */
	public void setResult(Integer result) {
		this.result = result;
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
	

	public Double getRealCountQty() {
		return realCountQty;
	}
	

	public void setRealCountQty(Double realCountQty) {
		this.realCountQty = realCountQty;
	}

	public Date getInStockDate() {
		return inStockDate;
	}

	public void setInStockDate(Date inStockDate) {
		this.inStockDate = inStockDate;
	}
	

}