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

import javax.validation.Valid;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;

import tk.mybatis.mapper.entity.Example;

/**
 * 移位单VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvShiftDetailVO extends BaseVO {

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
	public InvShiftDetailVO(){
		this.invShiftDetail = new InvShiftDetail();
	}
	
	/**
	 * 
	 * 构造方法
	 * @param shift
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvShiftDetailVO(InvShiftDetail invShiftDetail){
		this.invShiftDetail = invShiftDetail;
	}

	/**
	 * 移位单明细对象
	 * @version 2017年2月16日 上午10:07:48<br/>
	 * @author 王通<br/>
	 */
	@Valid
	private InvShiftDetail invShiftDetail;
	/**
	 * 货品代码
	 */
	private String skuNo;
	/**
	 * 货品条码
	 */
	private String skuBarCode;
	/**
	 * 收货单号
	 */
	private String asnNo;
	/**
	 * 货品名称
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	/**
	 * 移出库位
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String outLocation;
	/**
	 * 移入
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String inLocation;

	/**
	 * 移出库位
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String outLocationNo;
	/**
	 * 移入
	 * @version 2017年2月16日 下午2:30:45<br/>
	 * @author 王通<br/>
	 */
	private String inLocationNo;
	
	/**
	 * 属性 outLocationNo getter方法
	 * @return 属性outLocationNo
	 * @author 王通<br/>
	 */
	public String getOutLocationNo() {
		return outLocationNo;
	}

	/**
	 * 属性 outLocationNo setter方法
	 * @param outLocationNo 设置属性outLocationNo的值
	 * @author 王通<br/>
	 */
	public void setOutLocationNo(String outLocationNo) {
		this.outLocationNo = outLocationNo;
	}

	/**
	 * 属性 inLocationNo getter方法
	 * @return 属性inLocationNo
	 * @author 王通<br/>
	 */
	public String getInLocationNo() {
		return inLocationNo;
	}

	/**
	 * 属性 inLocationNo setter方法
	 * @param inLocationNo 设置属性inLocationNo的值
	 * @author 王通<br/>
	 */
	public void setInLocationNo(String inLocationNo) {
		this.inLocationNo = inLocationNo;
	}

	/**
	 * 包装单位
	 * @version 2017年2月16日14:31:04<br/>
	 * @author 王通<br/>
	 */
	private String packUnit;
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

	
	/* getset *************************************************/

	/**
	 * 属性 outLocation getter方法
	 * @return 属性outLocation
	 * @author 王通<br/>
	 */
	public String getOutLocation() {
		return outLocation;
	}

	/**
	 * 属性 outLocation setter方法
	 * @param outLocation 设置属性outLocation的值
	 * @author 王通<br/>
	 */
	public void setOutLocation(String outLocation) {
		this.outLocation = outLocation;
	}

	/**
	 * 属性 inLocation getter方法
	 * @return 属性inLocation
	 * @author 王通<br/>
	 */
	public String getInLocation() {
		return inLocation;
	}

	/**
	 * 属性 inLocation setter方法
	 * @param inLocation 设置属性inLocation的值
	 * @author 王通<br/>
	 */
	public void setInLocation(String inLocation) {
		this.inLocation = inLocation;
	}

	/**
	 * 属性 invShiftDetail getter方法
	 * @return 属性invShiftDetail
	 * @author 王通<br/>
	 */
	public InvShiftDetail getInvShiftDetail() {
		return invShiftDetail;
	}

	/**
	 * 属性 invShiftDetail setter方法
	 * @param invShiftDetail 设置属性invShiftDetail的值
	 * @author 王通<br/>
	 */
	public void setInvShiftDetail(InvShiftDetail invShiftDetail) {
		this.invShiftDetail = invShiftDetail;
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
	 * 属性 packUnit getter方法
	 * @return 属性packUnit
	 * @author 王通<br/>
	 */
	public String getPackUnit() {
		return packUnit;
	}

	/**
	 * 属性 packUnit setter方法
	 * @param packUnit 设置属性packUnit的值
	 * @author 王通<br/>
	 */
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:23:04<br/>
	 * @author 王通<br/>
	 */
	public InvShiftDetailVO searchCache () {
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


}
