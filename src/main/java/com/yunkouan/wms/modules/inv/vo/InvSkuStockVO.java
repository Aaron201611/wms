/**
 * Created on 2017年2月16日
 * InvShiftVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import com.yunkouan.base.BaseVO;

/**
 * 货品库存VO对象，对外提供接口使用<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvSkuStockVO extends BaseVO {

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
	public InvSkuStockVO(){}
	
	/* 查询条件 start*/
	/**
	 * 仓库id
	 * @version 2017年3月14日 上午9:19:16<br/>
	 * @author 王通<br/>
	 */
	private String warehouseId;
	/**
	 * 机构id
	 * @version 2017年3月14日 上午9:18:57<br/>
	 * @author 王通<br/>
	 */
	private String orgId;
	/**
	 * 货品代码
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String skuNo;
	/**
	 * 库存状态
	 * @version 2017年3月13日 下午5:36:02<br/>
	 * @author 王通<br/>
	 */
	private Integer skuStatus;
	/* 查询条件 end*/
	
	/**
	 * 货品id
	 * @version 2017年3月13日 下午4:47:50<br/>
	 * @author 王通<br/>
	 */
	private String skuId;
	/**
	 * 货品名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	/**
	 * 规格型号
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String specModel;
	
	/**
	 * 计量单位
	 * @version 2017年3月13日 下午6:07:07<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 当前库存
	 * @version 2017年3月13日 下午6:07:07<br/>
	 * @author 王通<br/>
	 */
	private Double stockQty;
	
	/* getset *************************************************/

	
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
	 * 属性 packUnit getter方法
	 * @return 属性packUnit
	 * @author 王通<br/>
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * 属性 packUnit setter方法
	 * @param packUnit 设置属性packUnit的值
	 * @author 王通<br/>
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Double getStockQty() {
		return stockQty;
	}
	

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	

	/**
	 * 属性 skuStatus getter方法
	 * @return 属性skuStatus
	 * @author 王通<br/>
	 */
	public Integer getSkuStatus() {
		return skuStatus;
	}

	/**
	 * 属性 skuStatus setter方法
	 * @param skuStatus 设置属性skuStatus的值
	 * @author 王通<br/>
	 */
	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
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
	
	/* method ********************************************/

	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public InvSkuStockVO searchCache () {
		return this;
	}

}
