/**
 * Created on 2017年2月16日
 * InvAdjustVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvAdjustDetail;

import tk.mybatis.mapper.entity.Example;

/**
 * 调整单详情VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvAdjustDetailVO extends BaseVO {

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
	public InvAdjustDetailVO(){}
	
	/**
	 * 
	 * 构造方法
	 * @param adjust
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvAdjustDetailVO(InvAdjustDetail invAdjustDetail){
		this.setInvAdjustDetail(invAdjustDetail);
	}

	/**
	 * 移位单明细对象
	 * @version 2017年3月11日15:20:08<br/>
	 * @author 王通<br/>
	 */
	private InvAdjustDetail invAdjustDetail;
	
	/**
	 * 货品名称
	 * @version 2017年3月11日15:20:04<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	/**
	 * 库区名称
	 * @version 2017年3月11日15:19:59<br/>
	 * @author 王通<br/>
	 */
	private String areaName;
	/**
	 * 库位名称
	 * @version 2017年3月11日15:19:56<br/>
	 * @author 王通<br/>
	 */
	private String locationName;
	/**
	 * 货品编码
	 * @version 2017年3月11日15:19:52<br/>
	 * @author 王通<br/>
	 */
	private String skuNo;
	/**
	 * 收货单号
	 * @version 2017年3月11日15:19:56<br/>
	 * @author 王通<br/>
	 */
	private String asnNo;

	/**
	 * 计量单位
	 * @version 2017年3月11日15:19:46<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 规格型号
	 * @version 2017年3月11日15:19:46<br/>
	 * @author 王通<br/>
	 */
	private String specModel;
	
	/**
	 * 调账类型
	 * @version 2017年3月11日 下午3:19:36<br/>
	 * @author 王通<br/>
	 */
	private String adjustTypeName;

	
	/* getset *************************************************/

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
	 * 属性 adjustTypeName getter方法
	 * @return 属性adjustTypeName
	 * @author 王通<br/>
	 */
	public String getAdjustTypeName() {
		return adjustTypeName;
	}

	/**
	 * 属性 adjustTypeName setter方法
	 * @param adjustTypeName 设置属性adjustTypeName的值
	 * @author 王通<br/>
	 */
	public void setAdjustTypeName(String adjustTypeName) {
		this.adjustTypeName = adjustTypeName;
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

	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:23:04<br/>
	 * @author 王通<br/>
	 */
	public InvAdjustDetailVO searchCache () {
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

	/**
	 * 属性 invAdjustDetail getter方法
	 * @return 属性invAdjustDetail
	 * @author 王通<br/>
	 */
	public InvAdjustDetail getInvAdjustDetail() {
		return invAdjustDetail;
	}

	/**
	 * 属性 invAdjustDetail setter方法
	 * @param invAdjustDetail 设置属性invAdjustDetail的值
	 * @author 王通<br/>
	 */
	public void setInvAdjustDetail(InvAdjustDetail invAdjustDetail) {
		this.invAdjustDetail = invAdjustDetail;
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

}
