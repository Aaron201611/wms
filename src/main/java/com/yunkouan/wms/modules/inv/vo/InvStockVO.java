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

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.util.valid.ValidStockShift;
import com.yunkouan.wms.modules.meta.entity.MetaSku;

import tk.mybatis.mapper.entity.Example;

/**
 * 
  * 库存明细VO
  * @author 王通
  * @date 2017年2月14日 上午11:47:36
  *
 */
public class InvStockVO extends BaseVO {
	
	private static final long serialVersionUID = 2239128176535268345L;
	
	public InvStockVO(){}
	public InvStockVO( InvStock invStock ) {
		this.setInvStock(invStock);
	}
	
	/**
	 * 库存定义
	 */
	private InvStock invStock;
	
	/**
	 * 机构名称
	 */
	private String orgName;
	/**
	 * 货品名称
	 */
	private String skuName;
	/**
	 * 规格型号
	 */
	private String specModel;
	/**
	 * 收货单号
	 */
	private String asnNo;
	/**
	 * 库位编码
	 */
	private String locationNo;
	/**
	 * 库位类型
	 */
	private List<Integer> locationTypeList;
	/**
	 * 货品条码
	 */
	private String skuBarCode;

	/**
	 * 冻结类型
	 */
	private Integer freezeType;
	/**
	 * 计量单位（名称）
	 */
	private String measureUnit;
	/**measureUnitCode:计量单位（编号）**/
	private String measureUnitCode;
	/**hsCode:海关归类税号**/
	private String hsCode;
	/**
	 * 货品编号
	 */
	private String skuNo;

	private MetaSku sku;
	/**
	 * 仓库代码
	 */
	private String warehouseNo;
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	/**
	 * 货主名称
	 */
	private String ownerName;
	private String merchantShortName;
	/**
	 * 剩余有效期（天）
	 */
	private String limitDays;
	/**
	 * 海关客商代码
	 */
	private String hgMerchantNo;
	/**
	 * 作业人员姓名
	 */
	private String opPersonName;
	/**
	 * 货品状态名称
	 */
	private String skuStatusName;
	/**
	 * 是否冻结名称
	 */
	private String isBlockName;
	/**
	 * 库区名称
	 */
	private String areaName;
	/**
	 * 库区编号
	 */
	private String areaNo;
	/**
	 * 库位名称
	 */
	private String locationName;
	/**
	 * 库位类型名称
	 */
	private String locationTypeName;
	/**
	 * 入库库位名称
	 */
	private String inLocationName;
	/**
	 * 入库库位ID
	 */
	@NotNull(message="{valid_stock_shift_location_is_null}",groups={ValidStockShift.class})
	private String inLocationId;
	/**
	 * 计划数量
	 */
	private Double findNum = 0.0;
	/**
	 * 是否包含暂存区
	 */
	private Boolean containTemp = true;
	/**
	 * 是否只查暂存区
	 */
	private Boolean onlyTemp = false;
	/**
	 * 是否直接调整库存
	 */
	private Boolean changeStock = true;
	/**
	 * 是否包含批次
	 */
	private Boolean containBatch = false;
	/**
	 * areaType 库位类型
	 */
	private String locationType;
//	/**
//	 * 收货单详情编号为空
//	 */
//	private Boolean asnDetailIdIsNull = false;
	/**
	 * 排序方式
	 */
	private String resultOrder = "t1.update_time DESC,t1.in_date DESC,t1.location_id,t1.sku_id,t1.batch_no";
	/**
	 * 货品ID列表
	 */
	private List<String> skuIdList;
	/**
	 * 收货单号列表
	 */
	private List<String> asnIdList;
	/**
	 * 批次号列表
	 */
	private List<String> batchNoList;
	/**
	 * 货主列表
	 */
	private List<String> ownerList;
	/**
	 * 拣货明细列表
	 */
	private List<InvOutLockDetailVO> outLockDetailList;
	
	/**
	 * 库存日志
	 */
	@NotNull(message="{valid_stock_shift_log_is_null}",groups={ValidStockShift.class})
	private InvLog invLog;
	
	private List<String> notLocationId;

	/* method *****************************************************/
	
	/**
	 * 查询缓存
	 * @version 2017年2月12日 下午9:30:33<br/>
	 * @author andy wang<br/>
	 */
	public InvStockVO searchCache() {
		return this;
	}
	/*
	  * <p>Title: getExample</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.yunkouan.base.BaseVO#getExample()
	  */
	
	
	@Override
	public Example getExample() {
		// TODO Auto-generated method stub
		return null;
	}
	

	/* getset *************************************/
	
	/**
	 * 属性 owner getter方法
	 * @return 属性owner
	 * @author 王通<br/>
	 */
	public String getOwnerName() {
		return ownerName;
	}
	/**
	 * 属性 owner setter方法
	 * @param owner 设置属性owner的值
	 * @author 王通<br/>
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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
	 * 属性 warehouseName getter方法
	 * @return 属性warehouseName
	 * @author 王通<br/>
	 */
	public String getWarehouseName() {
		return warehouseName;
	}
	/**
	 * 属性 warehouseName setter方法
	 * @param warehouseName 设置属性warehouseName的值
	 * @author 王通<br/>
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	/**
	 * 属性 skuStatusName getter方法
	 * @return 属性skuStatusName
	 * @author 王通<br/>
	 */
	public String getSkuStatusName() {
		return skuStatusName;
	}
	/**
	 * 属性 skuStatusName setter方法
	 * @param skuStatusName 设置属性skuStatusName的值
	 * @author 王通<br/>
	 */
	public void setSkuStatusName(String skuStatusName) {
		this.skuStatusName = skuStatusName;
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
	 * 属性 skuIdList getter方法
	 * @return 属性skuIdList
	 * @author 王通<br/>
	 */
	public List<String> getSkuIdList() {
		return skuIdList;
	}
	/**
	 * 属性 skuIdList setter方法
	 * @param skuIdList 设置属性skuIdList的值
	 * @author 王通<br/>
	 */
	public void setSkuIdList(List<String> skuIdList) {
		this.skuIdList = skuIdList;
	}
	/**
	 * 属性 batchNoList getter方法
	 * @return 属性batchNoList
	 * @author 王通<br/>
	 */
	public List<String> getBatchNoList() {
		return batchNoList;
	}
	/**
	 * 属性 batchNoList setter方法
	 * @param batchNoList 设置属性batchNoList的值
	 * @author 王通<br/>
	 */
	public void setBatchNoList(List<String> batchNoList) {
		this.batchNoList = batchNoList;
	}
	/**
	 * 属性 invStock getter方法
	 * @return 属性invStock
	 * @author 王通<br/>
	 */
	public InvStock getInvStock() {
		return invStock;
	}
	/**
	 * 属性 invStock setter方法
	 * @param invStock 设置属性invStock的值
	 * @author 王通<br/>
	 */
	public void setInvStock(InvStock invStock) {
		this.invStock = invStock;
	}
	/**
	 * 属性 orgName getter方法
	 * @return 属性orgName
	 * @author 王通<br/>
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * 属性 orgName setter方法
	 * @param orgName 设置属性orgName的值
	 * @author 王通<br/>
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Double getFindNum() {
		return findNum;
	}
	
	public void setFindNum(Double findNum) {
		this.findNum = findNum;
	}
	
	/**
	 * 属性 containTemp getter方法
	 * @return 属性containTemp
	 * @author 王通<br/>
	 */
	public Boolean getContainTemp() {
		return containTemp;
	}
	/**
	 * 属性 containTemp setter方法
	 * @param containTemp 设置属性containTemp的值
	 * @author 王通<br/>
	 */
	public void setContainTemp(Boolean containTemp) {
		this.containTemp = containTemp;
	}
	/**
	 * 属性 invLog getter方法
	 * @return 属性invLog
	 * @author 王通<br/>
	 */
	public InvLog getInvLog() {
		return invLog;
	}
	/**
	 * 属性 invLog setter方法
	 * @param invLog 设置属性invLog的值
	 * @author 王通<br/>
	 */
	public void setInvLog(InvLog invLog) {
		this.invLog = invLog;
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
//	/**
//	 * 属性 packUnit getter方法
//	 * @return 属性packUnit
//	 * @author 王通<br/>
//	 */
//	public String getPackUnit() {
//		return packUnit;
//	}
//	/**
//	 * 属性 packUnit setter方法
//	 * @param packUnit 设置属性packUnit的值
//	 * @author 王通<br/>
//	 */
//	public void setPackUnit(String packUnit) {
//		this.packUnit = packUnit;
//	}
	/**
	 * 属性 changeStock getter方法
	 * @return 属性changeStock
	 * @author 王通<br/>
	 */
	public Boolean getChangeStock() {
		return changeStock;
	}
	/**
	 * 属性 changeStock setter方法
	 * @param changeStock 设置属性changeStock的值
	 * @author 王通<br/>
	 */
	public void setChangeStock(Boolean changeStock) {
		this.changeStock = changeStock;
	}
	/**
	 * 属性 ownerList getter方法
	 * @return 属性ownerList
	 * @author 王通<br/>
	 */
	public List<String> getOwnerList() {
		return ownerList;
	}
	/**
	 * 属性 ownerList setter方法
	 * @param ownerList 设置属性ownerList的值
	 * @author 王通<br/>
	 */
	public void setOwnerList(List<String> ownerList) {
		this.ownerList = ownerList;
	}
	/**
	 * 属性 outLockDetailList getter方法
	 * @return 属性outLockDetailList
	 * @author 王通<br/>
	 */
	public List<InvOutLockDetailVO> getOutLockDetailList() {
		return outLockDetailList;
	}
	/**
	 * 属性 outLockDetailList setter方法
	 * @param outLockDetailList 设置属性outLockDetailList的值
	 * @author 王通<br/>
	 */
	public void setOutLockDetailList(List<InvOutLockDetailVO> outLockDetailList) {
		this.outLockDetailList = outLockDetailList;
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
	 * 属性 isBlockName getter方法
	 * @return 属性isBlockName
	 * @author 王通<br/>
	 */
	public String getIsBlockName() {
		return isBlockName;
	}
	/**
	 * 属性 isBlockName setter方法
	 * @param isBlockName 设置属性isBlockName的值
	 * @author 王通<br/>
	 */
	public void setIsBlockName(String isBlockName) {
		this.isBlockName = isBlockName;
	}
	/**
	 * 属性 freezeType getter方法
	 * @return 属性freezeType
	 * @author 王通<br/>
	 */
	public Integer getFreezeType() {
		return freezeType;
	}
	/**
	 * 属性 freezeType setter方法
	 * @param freezeType 设置属性freezeType的值
	 * @author 王通<br/>
	 */
	public void setFreezeType(Integer freezeType) {
		this.freezeType = freezeType;
	}
	/**
	 * 属性 inLocationName getter方法
	 * @return 属性inLocationName
	 * @author 王通<br/>
	 */
	public String getInLocationName() {
		return inLocationName;
	}
	/**
	 * 属性 inLocationName setter方法
	 * @param inLocationName 设置属性inLocationName的值
	 * @author 王通<br/>
	 */
	public void setInLocationName(String inLocationName) {
		this.inLocationName = inLocationName;
	}
	/**
	 * 属性 onlyTemp getter方法
	 * @return 属性onlyTemp
	 * @author 王通<br/>
	 */
	public Boolean getOnlyTemp() {
		return onlyTemp;
	}
	/**
	 * 属性 onlyTemp setter方法
	 * @param onlyTemp 设置属性onlyTemp的值
	 * @author 王通<br/>
	 */
	public void setOnlyTemp(Boolean onlyTemp) {
		this.onlyTemp = onlyTemp;
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
	 * 属性 opPersonName getter方法
	 * @return 属性opPersonName
	 * @author 王通<br/>
	 */
	public String getOpPersonName() {
		return opPersonName;
	}
	/**
	 * 属性 opPersonName setter方法
	 * @param opPersonName 设置属性opPersonName的值
	 * @author 王通<br/>
	 */
	public void setOpPersonName(String opPersonName) {
		this.opPersonName = opPersonName;
	}
	/**
	 * 属性 inLocationId getter方法
	 * @return 属性inLocationId
	 * @author 王通<br/>
	 */
	public String getInLocationId() {
		return inLocationId;
	}
	/**
	 * 属性 inLocationId setter方法
	 * @param inLocationId 设置属性inLocationId的值
	 * @author 王通<br/>
	 */
	public void setInLocationId(String inLocationId) {
		this.inLocationId = inLocationId;
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
	 * 属性 asnIdList getter方法
	 * @return 属性asnIdList
	 * @author 王通<br/>
	 */
	public List<String> getAsnIdList() {
		return asnIdList;
	}
	/**
	 * 属性 asnIdList setter方法
	 * @param asnIdList 设置属性asnIdList的值
	 * @author 王通<br/>
	 */
	public void setAsnIdList(List<String> asnIdList) {
		this.asnIdList = asnIdList;
	}
	/**
	 * 属性 containBatch getter方法
	 * @return 属性containBatch
	 * @author 王通<br/>
	 */
	public Boolean getContainBatch() {
		return containBatch;
	}
	/**
	 * 属性 containBatch setter方法
	 * @param containBatch 设置属性containBatch的值
	 * @author 王通<br/>
	 */
	public void setContainBatch(Boolean containBatch) {
		this.containBatch = containBatch;
	}
	/**
	 * 属性 resultOrder getter方法
	 * @return 属性resultOrder
	 * @author 王通<br/>
	 */
	public String getResultOrder() {
		return resultOrder;
	}
	/**
	 * 属性 resultOrder setter方法
	 * @param resultOrder 设置属性resultOrder的值
	 * @author 王通<br/>
	 */
	public void setResultOrder(String resultOrder) {
		this.resultOrder = resultOrder;
	}
	/**
	 * 属性 locationTypeList getter方法
	 * @return 属性locationTypeList
	 * @author 王通<br/>
	 */
	public List<Integer> getLocationTypeList() {
		return locationTypeList;
	}
	/**
	 * 属性 locationTypeList setter方法
	 * @param locationTypeList 设置属性locationTypeList的值
	 * @author 王通<br/>
	 */
	public void setLocationTypeList(List<Integer> locationTypeList) {
		this.locationTypeList = locationTypeList;
	}
	public void addLocationTypeList(Integer locationType) {
		if(this.locationTypeList == null) this.locationTypeList = new ArrayList<Integer>();
		this.locationTypeList.add(locationType);
	}
	/**
	 * 属性 areaNo getter方法
	 * @return 属性areaNo
	 * @author 王通<br/>
	 */
	public String getAreaNo() {
		return areaNo;
	}
	/**
	 * 属性 areaNo setter方法
	 * @param areaNo 设置属性areaNo的值
	 * @author 王通<br/>
	 */
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	public String getMeasureUnitCode() {
		return measureUnitCode;
	}
	public void setMeasureUnitCode(String measureUnitCode) {
		this.measureUnitCode = measureUnitCode;
	}
	/**
	 * 属性 locationTypeName getter方法
	 * @return 属性locationTypeName
	 * @author 王通<br/>
	 */
	public String getLocationTypeName() {
		return locationTypeName;
	}
	/**
	 * 属性 locationTypeName setter方法
	 * @param locationTypeName 设置属性locationTypeName的值
	 * @author 王通<br/>
	 */
	public void setLocationTypeName(String locationTypeName) {
		this.locationTypeName = locationTypeName;
	}
	/**
	 * 属性 merchantShortName getter方法
	 * @return 属性merchantShortName
	 * @author 王通<br/>
	 */
	public String getMerchantShortName() {
		return merchantShortName;
	}
	/**
	 * 属性 merchantShortName setter方法
	 * @param merchantShortName 设置属性merchantShortName的值
	 * @author 王通<br/>
	 */
	public void setMerchantShortName(String merchantShortName) {
		this.merchantShortName = merchantShortName;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	public MetaSku getSku() {
		return sku;
	}
	public void setSku(MetaSku sku) {
		this.sku = sku;
	}
	public String getWarehouseNo() {
		return warehouseNo;
	}
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	public String getHgMerchantNo() {
		return hgMerchantNo;
	}
	public void setHgMerchantNo(String hgMerchantNo) {
		this.hgMerchantNo = hgMerchantNo;
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
	public String getLocationType() {
		return locationType;
	}
	
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getLimitDays() {
		return limitDays;
	}
	public void setLimitDays(String limitDays) {
		this.limitDays = limitDays;
	}
	public List<String> getNotLocationId() {
		return notLocationId;
	}
	public void setNotLocationId(List<String> notLocationId) {
		this.notLocationId = notLocationId;
	}
	public void addNotLocationId(String notLocationId) {
		if(this.notLocationId == null) this.notLocationId = new ArrayList<String>();
		this.notLocationId.add(notLocationId);
	}
}