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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
  * 库存明细VO供导出excel使用，每个字段对应显示的一列
  * @author 王通
  * @date 2017年2月14日 上午11:47:36
  *
 */
public class InvStockVO4Excel{
	
//	/**
//	 * 每个字段对应的名称，按顺序
//			//TODO
//	 */
//	public static final String[] headers = { 
//			"序号", "货主简称", "库区", "库位", 
//			"收货单号", "货品代码", "货品名称", "批次", 
//			"规格", "计量单位", "数量/公斤/立方", "预分配数量", 
//			"状态", "是否冻结", "收货日期"};  

	
	
	/**
	 * 构造方法
	 * @version 2017年10月23日 下午2:29:27<br/>
	 * @author 王通<br/>
	 */
	public InvStockVO4Excel(InvStockVO vo) {
		this.ownerShortName = vo.getMerchantShortName();
		this.areaName = vo.getAreaName();
		this.locationName = vo.getLocationName();
		this.asnNo = vo.getAsnNo();
		this.skuNo = vo.getSkuNo();
		this.skuName = vo.getSkuName();
		this.skuBarCode = vo.getSkuBarCode();
		this.batchNo = vo.getInvStock().getBatchNo();
		this.specModel = vo.getSpecModel();
		this.measureUnit = vo.getMeasureUnit();
//		this.slgjlf = vo.getInvStock().getStockQty() + "/" + 
//		vo.getInvStock().getStockWeight() + "/" + vo.getInvStock().getStockVolume();
		this.setLimitDays(vo.getLimitDays());
		this.stockQty = new BigDecimal(vo.getInvStock().getStockQty().toString()).toString();
		this.setCountQty(stockQty);
		this.stockWeight = new BigDecimal(vo.getInvStock().getStockWeight().toString()).toString();
		this.stockVolume = new BigDecimal(vo.getInvStock().getStockVolume().toString()).toString();
		this.pickQty = new BigDecimal(vo.getInvStock().getPickQty().toString()).toString();
		this.statusName = vo.getSkuStatusName();
		this.blockStatusName = vo.getIsBlockName();
		this.recDate = vo.getInvStock().getInDate();
	}
	/**
	 * 构造方法
	 * @param index
	 * @param ownerShortName
	 * @param areaName
	 * @param locationName
	 * @param asnNo
	 * @param skuNo
	 * @param skuName
	 * @param batchNo
	 * @param specModel
	 * @param measureUnit
	 * @param slgjlf
	 * @param pickQty
	 * @param statusName
	 * @param blockStatusName
	 * @param recDate
	 * @version 2017年10月23日 下午2:28:49<br/>
	 * @author 王通<br/>
	 */
//	public InvStockVO4Excel(String index, String ownerShortName, String areaName, String locationName, String asnNo,
//			String skuNo, String skuName, String batchNo, String specModel, String measureUnit, String stockQty,
//			String stockWeight,String stockVolume,String pickQty, String statusName, 
//			String blockStatusName, Date recDate) {
//		this.index = index;
//		this.ownerShortName = ownerShortName;
//		this.areaName = areaName;
//		this.locationName = locationName;
//		this.asnNo = asnNo;
//		this.skuNo = skuNo;
//		this.skuName = skuName;
//		this.batchNo = batchNo;
//		this.specModel = specModel;
//		this.measureUnit = measureUnit;
////		this.slgjlf = slgjlf;
//		this.stockQty = stockQty;
//		this.stockWeight = stockWeight;
//		this.stockVolume = stockVolume;
//		this.pickQty = pickQty;
//		this.statusName = statusName;
//		this.blockStatusName = blockStatusName;
//		this.recDate = recDate;
//	}
	
	private String index;
	private String ownerShortName;
	private String areaName;
	private String locationName;
	private String asnNo;
	private String skuNo;
	private String skuName;
	private String skuBarCode;
	private String batchNo;
	private String specModel;
	private String measureUnit;
//	private String slgjlf;
	private String limitDays;
	private String stockQty;
	private String countQty;
	private String stockWeight;
	private String stockVolume;
	private String pickQty;
	private String statusName;
	private String blockStatusName;
	private Date recDate;
	/* getset *************************************/



	/**
	 * 属性 index getter方法
	 * @return 属性index
	 * @author 王通<br/>
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * 属性 index setter方法
	 * @param index 设置属性index的值
	 * @author 王通<br/>
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * 属性 ownerShortName getter方法
	 * @return 属性ownerShortName
	 * @author 王通<br/>
	 */
	public String getOwnerShortName() {
		return ownerShortName;
	}
	/**
	 * 属性 ownerShortName setter方法
	 * @param ownerShortName 设置属性ownerShortName的值
	 * @author 王通<br/>
	 */
	public void setOwnerShortName(String ownerShortName) {
		this.ownerShortName = ownerShortName;
	}
	/**
	 * 属性 areaName getter方法
	 * @return 属性areaName
	 * @author 王通<br/>
	 */
	public String getAreaName() {
		return areaName;
	}
	/**
	 * 属性 areaName setter方法
	 * @param areaName 设置属性areaName的值
	 * @author 王通<br/>
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	/**
	 * 属性 locationName getter方法
	 * @return 属性locationName
	 * @author 王通<br/>
	 */
	public String getLocationName() {
		return locationName;
	}
	/**
	 * 属性 locationName setter方法
	 * @param locationName 设置属性locationName的值
	 * @author 王通<br/>
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * 属性 asnNo getter方法
	 * @return 属性asnNo
	 * @author 王通<br/>
	 */
	public String getAsnNo() {
		return asnNo;
	}
	/**
	 * 属性 asnNo setter方法
	 * @param asnNo 设置属性asnNo的值
	 * @author 王通<br/>
	 */
	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}
	/**
	 * 属性 skuNo getter方法
	 * @return 属性skuNo
	 * @author 王通<br/>
	 */
	public String getSkuNo() {
		return skuNo;
	}
	/**
	 * 属性 skuNo setter方法
	 * @param skuNo 设置属性skuNo的值
	 * @author 王通<br/>
	 */
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	/**
	 * 属性 skuName getter方法
	 * @return 属性skuName
	 * @author 王通<br/>
	 */
	public String getSkuName() {
		return skuName;
	}
	/**
	 * 属性 skuName setter方法
	 * @param skuName 设置属性skuName的值
	 * @author 王通<br/>
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
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
	/**
	 * 属性 specModel getter方法
	 * @return 属性specModel
	 * @author 王通<br/>
	 */
	public String getSpecModel() {
		return specModel;
	}
	/**
	 * 属性 specModel setter方法
	 * @param specModel 设置属性specModel的值
	 * @author 王通<br/>
	 */
	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}
	/**
	 * 属性 measureUnit getter方法
	 * @return 属性measureUnit
	 * @author 王通<br/>
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}
	/**
	 * 属性 measureUnit setter方法
	 * @param measureUnit 设置属性measureUnit的值
	 * @author 王通<br/>
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	/**
	 * 属性 slgjlf getter方法
	 * @return 属性slgjlf
	 * @author 王通<br/>
	 */
//	public String getSlgjlf() {
//		return slgjlf;
//	}
	/**
	 * 属性 slgjlf setter方法
	 * @param slgjlf 设置属性slgjlf的值
	 * @author 王通<br/>
	 */
//	public void setSlgjlf(String slgjlf) {
//		this.slgjlf = slgjlf;
//	}
	/**
	 * 属性 pickQty getter方法
	 * @return 属性pickQty
	 * @author 王通<br/>
	 */
	public String getPickQty() {
		return pickQty;
	}
	/**
	 * 属性 pickQty setter方法
	 * @param pickQty 设置属性pickQty的值
	 * @author 王通<br/>
	 */
	public void setPickQty(String pickQty) {
		this.pickQty = pickQty;
	}
	/**
	 * 属性 statusName getter方法
	 * @return 属性statusName
	 * @author 王通<br/>
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * 属性 statusName setter方法
	 * @param statusName 设置属性statusName的值
	 * @author 王通<br/>
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	/**
	 * 属性 blockStatusName getter方法
	 * @return 属性blockStatusName
	 * @author 王通<br/>
	 */
	public String getBlockStatusName() {
		return blockStatusName;
	}
	/**
	 * 属性 blockStatusName setter方法
	 * @param blockStatusName 设置属性blockStatusName的值
	 * @author 王通<br/>
	 */
	public void setBlockStatusName(String blockStatusName) {
		this.blockStatusName = blockStatusName;
	}
	/**
	 * 属性 recDate getter方法
	 * @return 属性recDate
	 * @author 王通<br/>
	 */
	public Date getRecDate() {
		return recDate;
	}
	/**
	 * 属性 recDate setter方法
	 * @param recDate 设置属性recDate的值
	 * @author 王通<br/>
	 */
	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}
	public String getStockQty() {
		return stockQty;
	}
	public void setStockQty(String stockQty) {
		this.stockQty = stockQty;
	}
	public String getStockWeight() {
		return stockWeight;
	}
	public void setStockWeight(String stockWeight) {
		this.stockWeight = stockWeight;
	}
	public String getStockVolume() {
		return stockVolume;
	}
	public void setStockVolume(String stockVolume) {
		this.stockVolume = stockVolume;
	}
	public String getSkuBarCode() {
		return skuBarCode;
	}
	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}
	public String getCountQty() {
		return countQty;
	}
	public void setCountQty(String countQty) {
		this.countQty = countQty;
	}
	public String getLimitDays() {
		return limitDays;
	}
	public void setLimitDays(String limitDays) {
		this.limitDays = limitDays;
	}
	
	
}
