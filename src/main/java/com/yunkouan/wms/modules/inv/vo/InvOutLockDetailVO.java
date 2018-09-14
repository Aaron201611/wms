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
 * 库存预分配明细VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvOutLockDetailVO extends BaseVO {

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
	public InvOutLockDetailVO(){}

	/**
	 * 单号
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String bill;

	/**
	 * 单据类型
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String billType;

	/**
	 * 单据状态
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String billStatus;
	/**
	 * 数量
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private Double outLockQty;
	/**
	 * 计量单位
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	
	/* getset *************************************************/

	
	/**
	 * 属性 billType getter方法
	 * @return 属性billType
	 * @author 王通<br/>
	 */
	public String getBillType() {
		return billType;
	}

	/**
	 * 属性 billType setter方法
	 * @param billType 设置属性billType的值
	 * @author 王通<br/>
	 */
	public void setBillType(String billType) {
		this.billType = billType;
	}

	/**
	 * 属性 billStatus getter方法
	 * @return 属性billStatus
	 * @author 王通<br/>
	 */
	public String getBillStatus() {
		return billStatus;
	}

	/**
	 * 属性 billStatus setter方法
	 * @param billStatus 设置属性billStatus的值
	 * @author 王通<br/>
	 */
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	/**
	 * 属性 bill getter方法
	 * @return 属性bill
	 * @author 王通<br/>
	 */
	public String getBill() {
		return bill;
	}

	/**
	 * 属性 bill setter方法
	 * @param bill 设置属性bill的值
	 * @author 王通<br/>
	 */
	public void setBill(String bill) {
		this.bill = bill;
	}

	/**
	 * 属性 mesureUnit getter方法
	 * @return 属性mesureUnit
	 * @author 王通<br/>
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * 属性 mesureUnit setter方法
	 * @param mesureUnit 设置属性mesureUnit的值
	 * @author 王通<br/>
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}


	public Double getOutLockQty() {
		return outLockQty;
	}
	

	public void setOutLockQty(Double outLockQty) {
		this.outLockQty = outLockQty;
	}
	

	/* method ********************************************/
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public InvOutLockDetailVO searchCache () {
		return this;
	}
}
