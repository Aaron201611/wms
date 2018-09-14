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

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvCountDetail;

/**
 * 盘点单VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvCountDetailVO extends BaseVO {

	/**
	 * 
	 * @version 2017年2月16日 上午10:06:30<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7066852346579800875L;

	/**
	 * 
	 * 构造方法
	 * @version 2017年2月16日 上午10:06:17<br/>
	 * @author 王通<br/>
	 */
	public InvCountDetailVO(){}
	
	/**
	 * 
	 * 构造方法
	 * @param count
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvCountDetailVO(InvCountDetail invCountDetail){
		this.setInvCountDetail(invCountDetail);
	}

	/**
	 * 盘点单明细对象
	 * @version 2017年2月16日 上午10:07:48<br/>
	 * @author 王通<br/>
	 */
	private InvCountDetail invCountDetail;
	
	/**
	 * 货品名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	
	/**
	 * 货品代码
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String skuNo;
	/**
	 * 货品条码
	 * @version 2017年7月11日 下午1:17:27<br/>
	 * @author 王通<br/>
	 */
	private String skuBarCode;
	/**
	 * 货品条码
	 * @version 2017年7月11日 下午1:17:27<br/>
	 * @author 王通<br/>
	 */
	private Double changeQty;
	/**
	 * 货品条码
	 * @version 2017年7月11日 下午1:17:27<br/>
	 * @author 王通<br/>
	 */
	private Double changeWeight;
	/**
	 * 货品条码
	 * @version 2017年7月11日 下午1:17:27<br/>
	 * @author 王通<br/>
	 */
	private Double changeValue;
	/**
	 * 入库日期
	 * @version 2017年7月11日 下午1:17:27<br/>
	 * @author 王通<br/>
	 */
	private Date inStockDate;
	/**
	 * 收货单号
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String asnNo;
	/**
	 * 库位名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String locationName;
	/**
	 * 库位名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String locationNo;

	/**
	 * 库区名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String areaName;
	/**
	 * 计量单位
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 规格型号
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private String specModel;
	
	private String ownerName;
	
	
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
	public InvCountDetailVO searchCache () {
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
	 * 属性 locationNo getter方法
	 * @return 属性locationNo
	 * @author 王通<br/>
	 */
	public String getLocationNo() {
		return locationNo;
	}

	/**
	 * 属性 locationNo setter方法
	 * @param locationNo 设置属性locationNo的值
	 * @author 王通<br/>
	 */
	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
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
	 * 属性 invCountDetail getter方法
	 * @return 属性invCountDetail
	 * @author 王通<br/>
	 */
	public InvCountDetail getInvCountDetail() {
		return invCountDetail;
	}

	/**
	 * 属性 invCountDetail setter方法
	 * @param invCountDetail 设置属性invCountDetail的值
	 * @author 王通<br/>
	 */
	public void setInvCountDetail(InvCountDetail invCountDetail) {
		this.invCountDetail = invCountDetail;
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

	public Double getChangeQty() {
		return changeQty;
	}

	public void setChangeQty(Double changeQty) {
		this.changeQty = changeQty;
	}

	public Double getChangeWeight() {
		return changeWeight;
	}

	public void setChangeWeight(Double changeWeight) {
		this.changeWeight = changeWeight;
	}

	public Double getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(Double changeValue) {
		this.changeValue = changeValue;
	}

}
