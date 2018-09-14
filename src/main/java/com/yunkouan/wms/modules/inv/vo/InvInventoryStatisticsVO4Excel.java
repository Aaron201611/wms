/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月12日 下午9:00:36<br/>
 * @author 王通<br/>
 */
package com.yunkouan.wms.modules.inv.vo;

import java.text.SimpleDateFormat;

/**
 * 
  * 进销存明细VO供导出excel使用，每个字段对应显示的一列
  * @author 王通
  * @date 2017年2月14日 上午11:47:36
  *
 */
public class InvInventoryStatisticsVO4Excel{

	public InvInventoryStatisticsVO4Excel(InvInventoryStatisticsVO vo) {
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
	
		this.startDate = monthFormat.format(vo.getInventoryStatistics().getStatisticsDate());
		this.circle = vo.getCircle();
		this.ownerShortName = vo.getSkuVo().getMerchant().getMerchantShortName();
		this.skuName = vo.getSkuVo().getEntity().getSkuName();
		this.skuNo = vo.getSkuVo().getEntity().getSkuNo();
		this.measureUnit = vo.getSkuVo().getEntity().getMeasureUnit();
		this.periodBeginNum = vo.getInventoryStatistics().getPeriodBeginNum();
		this.inStockNum = vo.getInventoryStatistics().getInStockNum();
		this.outStockNum = vo.getInventoryStatistics().getOutStockNum();
		this.adjustNum = vo.getInventoryStatistics().getAdjustNum();
		this.periodEndNum = vo.getInventoryStatistics().getPeriodEndNum();
	}

	private String index;
	private String startDate;
	private Integer circle;
	private String ownerShortName;
	private String skuName;
	private String skuNo;
	private String measureUnit;
    private Double periodBeginNum;
    private Double inStockNum;
    private Double outStockNum;
    private Double adjustNum;
    private Double periodEndNum;
	public String getIndex() {
		return index;
	}
	
	public void setIndex(String index) {
		this.index = index;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public Integer getCircle() {
		return circle;
	}
	
	public void setCircle(Integer circle) {
		this.circle = circle;
	}
	
	public String getOwnerShortName() {
		return ownerShortName;
	}
	
	public void setOwnerShortName(String ownerShortName) {
		this.ownerShortName = ownerShortName;
	}
	
	public String getSkuName() {
		return skuName;
	}
	
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	public String getSkuNo() {
		return skuNo;
	}
	
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	
	public String getMeasureUnit() {
		return measureUnit;
	}
	
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
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
