/**
 * Created on 2017年3月13日
 * ShelflifeWarningVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.monitor.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 保质期预警视图
 * <br/><br/>
 * @Description 
 * @version 2017年3月13日 下午1:51:54<br/>
 * @author 王通<br/>
 */
public class ShelflifeWarningVO extends BaseVO {

	private static final long serialVersionUID = 7539617122998544603L;
	
	//提交字段start
	/**
	 * 货主
	 */
	private String ownerLike;
	/**
	 * 货品代码
	 */
	private String skuNoLike;
	/**
	 * 批次
	 */
	private String batchNoLike;
	/**
	 * 处理状态
	 */
	private int handleStatus;
	private String orgId;
	private String warehouseId;
	//提交字段end
	
	// 列表显示字段start
	private String owner;
	private String skuNo;
	private String skuName;
	private String batchNo;
	private String specModel;
	private String measureUnit;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date expireDate;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date warningDate;
	/**
	 * 库存总数量
	 */
	private int stockCount;
	// 列表显示字段end
	
	// 隐含字段
	private String warningHandlerId;
	
	private String skuId;
	/**
	 * 处理信息
	 */
	private WarningHandlerVO warningHandlerVo; 
	
	/**
	 * 构造方法
	 * @version 2017年3月14日 下午1:48:28<br/>
	 * @author 王通<br/>
	 */
	public ShelflifeWarningVO() {
	}

	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:16:48<br/>
	 * @author 王通<br/>
	 */
	public ShelflifeWarningVO searchCache() {
		return this;
	}
	
	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:31:23<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Example getExample() {
		return null;
	}

	/**
	 * 属性 warningHandlerVo getter方法
	 * @return 属性warningHandlerVo
	 * @author 王通<br/>
	 */
	public WarningHandlerVO getWarningHandlerVo() {
		return warningHandlerVo;
	}

	/**
	 * 属性 warningHandlerVo setter方法
	 * @param warningHandlerVo 设置属性warningHandlerVo的值
	 * @author 王通<br/>
	 */
	public void setWarningHandlerVo(WarningHandlerVO warningHandlerVo) {
		this.warningHandlerVo = warningHandlerVo;
	}

	/**
	 * 属性 stockCount getter方法
	 * @return 属性stockCount
	 * @author 王通<br/>
	 */
	public int getStockCount() {
		return stockCount;
	}

	/**
	 * 属性 stockCount setter方法
	 * @param stockCount 设置属性stockCount的值
	 * @author 王通<br/>
	 */
	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
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
	 * 属性 owner getter方法
	 * @return 属性owner
	 * @author 王通<br/>
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 属性 owner setter方法
	 * @param owner 设置属性owner的值
	 * @author 王通<br/>
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 属性 handleStatus getter方法
	 * @return 属性handleStatus
	 * @author 王通<br/>
	 */
	public int getHandleStatus() {
		return handleStatus;
	}

	/**
	 * 属性 handleStatus setter方法
	 * @param handleStatus 设置属性handleStatus的值
	 * @author 王通<br/>
	 */
	public void setHandleStatus(int handleStatus) {
		this.handleStatus = handleStatus;
	}

	/**
	 * 属性 batchNoLike getter方法
	 * @return 属性batchNoLike
	 * @author 王通<br/>
	 */
	public String getBatchNoLike() {
		return batchNoLike;
	}

	/**
	 * 属性 batchNoLike setter方法
	 * @param batchNoLike 设置属性batchNoLike的值
	 * @author 王通<br/>
	 */
	public void setBatchNoLike(String batchNoLike) {
		this.batchNoLike = batchNoLike;
	}

	/**
	 * 属性 skuNoLike getter方法
	 * @return 属性skuNoLike
	 * @author 王通<br/>
	 */
	public String getSkuNoLike() {
		return skuNoLike;
	}

	/**
	 * 属性 skuNoLike setter方法
	 * @param skuNoLike 设置属性skuNoLike的值
	 * @author 王通<br/>
	 */
	public void setSkuNoLike(String skuNoLike) {
		this.skuNoLike = skuNoLike;
	}

	/**
	 * 属性 ownerLike getter方法
	 * @return 属性ownerLike
	 * @author 王通<br/>
	 */
	public String getOwnerLike() {
		return ownerLike;
	}

	/**
	 * 属性 ownerLike setter方法
	 * @param ownerLike 设置属性ownerLike的值
	 * @author 王通<br/>
	 */
	public void setOwnerLike(String ownerLike) {
		this.ownerLike = ownerLike;
	}

	/**
	 * 属性 orgId getter方法
	 * @return 属性orgId
	 * @author 王通<br/>
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * 属性 orgId setter方法
	 * @param orgId 设置属性orgId的值
	 * @author 王通<br/>
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 属性 warehouseId getter方法
	 * @return 属性warehouseId
	 * @author 王通<br/>
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * 属性 warehouseId setter方法
	 * @param warehouseId 设置属性warehouseId的值
	 * @author 王通<br/>
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
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
	 * 属性 expireDate getter方法
	 * @return 属性expireDate
	 * @author 王通<br/>
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * 属性 expireDate setter方法
	 * @param expireDate 设置属性expireDate的值
	 * @author 王通<br/>
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * 属性 warningDate getter方法
	 * @return 属性warningDate
	 * @author 王通<br/>
	 */
	public Date getWarningDate() {
		return warningDate;
	}

	/**
	 * 属性 warningDate setter方法
	 * @param warningDate 设置属性warningDate的值
	 * @author 王通<br/>
	 */
	public void setWarningDate(Date warningDate) {
		this.warningDate = warningDate;
	}

	/**
	 * 属性 warningHandlerId getter方法
	 * @return 属性warningHandlerId
	 * @author 王通<br/>
	 */
	public String getWarningHandlerId() {
		return warningHandlerId;
	}

	/**
	 * 属性 warningHandlerId setter方法
	 * @param warningHandlerId 设置属性warningHandlerId的值
	 * @author 王通<br/>
	 */
	public void setWarningHandlerId(String warningHandlerId) {
		this.warningHandlerId = warningHandlerId;
	}
}
