/**
 * Created on 2017年2月16日
 * InvCountVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.Date;

import com.yunkouan.util.DateUtil;

/**
 * 盘点单详情VO导出对象<br/><br/>
 * @Description 
 * @version 2018年5月5日10:42:51<br/>
 * @author 王通<br/>
 */
public class InvCountDetailVO4Excel {

	public InvCountDetailVO4Excel(int index, InvCountDetailVO vo) {
		this.setIndex(index);
		this.ownerName = vo.getOwnerName();
		this.areaName = vo.getAreaName();
		this.locationName = vo.getLocationName();
		this.skuName = vo.getSkuName();
		this.skuBarCode = vo.getSkuBarCode();
		this.batchNo = vo.getInvCountDetail().getBatchNo();
		this.specModel = vo.getSpecModel();
		this.measureUnit = vo.getMeasureUnit();
		this.stockQty = vo.getInvCountDetail().getStockQty();
		this.realCountQty = vo.getInvCountDetail().getRealCountQty();
		this.inStockDate = vo.getInvCountDetail().getInStockDate();
		this.inStockDays = DateUtil.getIntervalDays(inStockDate, new Date());
	}

	private int index;
	private String ownerName;
	/**
	 * 库区名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String areaName;
	/**
	 * 库位名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String locationName;
	/**
	 * 货品名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	/**
	 * 货品条码
	 * @version 2017年7月11日 下午1:17:27<br/>
	 * @author 王通<br/>
	 */
	private String skuBarCode;
	/**
	 * 批次
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String batchNo;

	/**
	 * 规格型号
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private String specModel;
	/**
	 * 计量单位
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 账存
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private Double stockQty;
	/**
	 * 实存
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private Double realCountQty;
	/**
	 * 在库天数
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private Integer inStockDays;
	/**
	 * 入库日期
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private Date inStockDate;
	
	
	
	/* getset *************************************************/

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


	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:23:04<br/>
	 * @author 王通<br/>
	 */
	public InvCountDetailVO4Excel searchCache () {
		return this;
	}
	/*
	  * <p>Title: getExample</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.yunkouan.base.BaseVO#getExample()
	  */
	
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
	 * 属性 skuBarCode getter方法
	 * @return 属性skuBarCode
	 * @author 王通<br/>
	 */
	public String getSkuBarCode() {
		return skuBarCode;
	}

	/**
	 * 属性 skuBarCode setter方法
	 * @param skuBarCode 设置属性skuBarCode的值
	 * @author 王通<br/>
	 */
	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Date getInStockDate() {
		return inStockDate;
	}

	public void setInStockDate(Date inStockDate) {
		this.inStockDate = inStockDate;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBatchNo() {
		return batchNo;
	}
	

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	

	public Integer getInStockDays() {
		return inStockDays;
	}
	

	public void setInStockDays(Integer inStockDays) {
		this.inStockDays = inStockDays;
	}
	

}
