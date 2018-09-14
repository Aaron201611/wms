package com.yunkouan.wms.modules.inv.entity;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;

public class InvStock  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:58:40<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 340323446058075499L;

	@Id
	private String stockId;

    private String locationId;

    private String owner;

    private String skuId;

    private String packId;

    private String batchNo;
    
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date inDate;

    private Integer skuStatus;

    private String orgId;

    private String warehouseId;

    private String asnId;

    private String asnDetailId;

    private Integer isBlock;

    private Double pickQty;

    private Double stockQty;

    private Double stockWeight;

    private Double stockVolume;

    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date produceDate;

    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date expiredDate;

    private String att1;

    private String att2;

    private String att3;

    private String att4;

    private String att5;

    private Integer stockId2;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId == null ? null : stockId.trim();
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Integer getSkuStatus() {
        return skuStatus;
    }

    public void setSkuStatus(Integer skuStatus) {
        this.skuStatus = skuStatus;
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

    public String getAsnId() {
        return asnId;
    }

    public void setAsnId(String asnId) {
        this.asnId = asnId == null ? null : asnId.trim();
    }

    public String getAsnDetailId() {
        return asnDetailId;
    }

    public void setAsnDetailId(String asnDetailId) {
        this.asnDetailId = asnDetailId == null ? null : asnDetailId.trim();
    }

    public Integer getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Integer isBlock) {
        this.isBlock = isBlock;
    }

    public Double getStockWeight() {
        return stockWeight;
    }

    public void setStockWeight(Double stockWeight) {
        this.stockWeight = stockWeight;
    }

    public Double getStockVolume() {
        return stockVolume;
    }

    public void setStockVolume(Double stockVolume) {
        this.stockVolume = stockVolume;
    }

    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getAtt1() {
        return att1;
    }

    public void setAtt1(String att1) {
        this.att1 = att1 == null ? null : att1.trim();
    }

    public String getAtt2() {
        return att2;
    }

    public void setAtt2(String att2) {
        this.att2 = att2 == null ? null : att2.trim();
    }

    public String getAtt3() {
        return att3;
    }

    public void setAtt3(String att3) {
        this.att3 = att3 == null ? null : att3.trim();
    }

    public String getAtt4() {
        return att4;
    }

    public void setAtt4(String att4) {
        this.att4 = att4 == null ? null : att4.trim();
    }

    public String getAtt5() {
        return att5;
    }

    public void setAtt5(String att5) {
        this.att5 = att5 == null ? null : att5.trim();
    }

    public Integer getStockId2() {
        return stockId2;
    }

    public void setStockId2(Integer stockId2) {
        this.stockId2 = stockId2;
    }

	public Double getPickQty() {
		return pickQty;
	}
	

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}
	

	public Double getStockQty() {
		return stockQty;
	}
	

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	
}