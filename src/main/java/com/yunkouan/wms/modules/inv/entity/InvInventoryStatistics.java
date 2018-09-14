package com.yunkouan.wms.modules.inv.entity;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;

public class InvInventoryStatistics  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:58:40<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 340323446058075499L;

	@Id
	private String id;

	private String orgId;
	private String warehouseId;
	private String skuId;

    @JsonFormat(pattern="yyyy/MM",locale = "zh" , timezone="GMT+8")
    private Date statisticsDate;
    private Double periodBeginNum;
    private Double inStockNum;
    private Double outStockNum;
    private Double adjustNum;
    private Double periodEndNum;
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getWarehouseId() {
		return warehouseId;
	}
	
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	public String getSkuId() {
		return skuId;
	}
	
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
	public Date getStatisticsDate() {
		return statisticsDate;
	}
	
	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}
	
	public Double getPeriodBeginNum() {
		return periodBeginNum;
	}
	
	public void setPeriodBeginNum(Double periodBeginNum) {
		this.periodBeginNum = periodBeginNum;
	}
	
	public Double getInStockNum() {
		return inStockNum;
	}
	
	public void setInStockNum(Double inStockNum) {
		this.inStockNum = inStockNum;
	}
	
	public Double getOutStockNum() {
		return outStockNum;
	}
	
	public void setOutStockNum(Double outStockNum) {
		this.outStockNum = outStockNum;
	}
	
	public Double getAdjustNum() {
		return adjustNum;
	}
	
	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}
	
	public Double getPeriodEndNum() {
		return periodEndNum;
	}
	
	public void setPeriodEndNum(Double periodEndNum) {
		this.periodEndNum = periodEndNum;
	}
	
}