/**
 * Created on 2017年2月16日
 * InvShiftVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.meta.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialLog;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 辅材日志VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class MaterialLogVo extends BaseVO {

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
	public MaterialLogVo(){}
	
	/**
	 * 
	 * 构造方法
	 * @param shift
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public MaterialLogVo(MetaMaterialLog materialLog){
		this.materialLog = materialLog;
	}

	/**
	 * 
	 * 日志对象
	 * @version 2017年3月2日 上午11:45:30<br/>
	 * @author 王通<br/>
	 */
	private MetaMaterialLog materialLog;

	/**
	 * 日志类型中文
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String materialLogTypeName;
	/**
	 * 变动开始时间
	 * @version 2017年8月24日09:32:35<br/>
	 * @author 王通<br/>
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date startDate;
	/**
	 * 变动结束时间
	 * @version 2017年8月24日09:32:31<br/>
	 * @author 王通<br/>
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date endDate;
	/**
	 * 辅材名称模糊查询
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String materialNameLike;
	/**
	 * 辅材代码模糊查询
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String materialNoLike;
	/**
	 * 相关单号模糊查询
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String involveBillLike;

	/**
	 * 属性 materialNameLike getter方法
	 * @return 属性materialNameLike
	 * @author 王通<br/>
	 */
	public String getMaterialNameLike() {
		return materialNameLike;
	}

	/**
	 * 属性 materialNameLike setter方法
	 * @param materialNameLike 设置属性materialNameLike的值
	 * @author 王通<br/>
	 */
	public void setMaterialNameLike(String materialNameLike) {
		this.materialNameLike = materialNameLike;
	}

	/**
	 * 属性 materialNoLike getter方法
	 * @return 属性materialNoLike
	 * @author 王通<br/>
	 */
	public String getMaterialNoLike() {
		return materialNoLike;
	}

	/**
	 * 属性 materialNoLike setter方法
	 * @param materialNoLike 设置属性materialNoLike的值
	 * @author 王通<br/>
	 */
	public void setMaterialNoLike(String materialNoLike) {
		this.materialNoLike = materialNoLike;
	}

	/**
	 * 属性 involveBillLike getter方法
	 * @return 属性involveBillLike
	 * @author 王通<br/>
	 */
	public String getInvolveBillLike() {
		return involveBillLike;
	}

	/**
	 * 属性 involveBillLike setter方法
	 * @param involveBillLike 设置属性involveBillLike的值
	 * @author 王通<br/>
	 */
	public void setInvolveBillLike(String involveBillLike) {
		this.involveBillLike = involveBillLike;
	}

	/**
	 * 属性 materialLogTypeName getter方法
	 * @return 属性materialLogTypeName
	 * @author 王通<br/>
	 */
	public String getMaterialLogTypeName() {
		return materialLogTypeName;
	}

	/**
	 * 属性 materialLogTypeName setter方法
	 * @param materialLogTypeName 设置属性materialLogTypeName的值
	 * @author 王通<br/>
	 */
	public void setMaterialLogTypeName(String materialLogTypeName) {
		this.materialLogTypeName = materialLogTypeName;
	}

	/**
	 * 属性 materialName getter方法
	 * @return 属性materialName
	 * @author 王通<br/>
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * 属性 materialName setter方法
	 * @param materialName 设置属性materialName的值
	 * @author 王通<br/>
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	/**
	 * 属性 materialNo getter方法
	 * @return 属性materialNo
	 * @author 王通<br/>
	 */
	public String getMaterialNo() {
		return materialNo;
	}

	/**
	 * 属性 materialNo setter方法
	 * @param materialNo 设置属性materialNo的值
	 * @author 王通<br/>
	 */
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	/**
	 * 属性 materialBarCode getter方法
	 * @return 属性materialBarCode
	 * @author 王通<br/>
	 */
	public String getMaterialBarCode() {
		return materialBarCode;
	}

	/**
	 * 属性 materialBarCode setter方法
	 * @param materialBarCode 设置属性materialBarCode的值
	 * @author 王通<br/>
	 */
	public void setMaterialBarCode(String materialBarCode) {
		this.materialBarCode = materialBarCode;
	}

	/**
	 * 辅材名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String materialName;
	/**
	 * 辅材代码
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String materialNo;
	/**
	 * 辅材单位
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 辅材条码
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String materialBarCode;

	/**
	 * 创建人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String createUserName;
	
	/* getset *************************************************/


	/**
	 * 属性 materialLog getter方法
	 * @return 属性MaterialLog
	 * @author 王通<br/>
	 */
	public MetaMaterialLog getMaterialLog() {
		return materialLog;
	}

	/**
	 * 属性 materialLog setter方法
	 * @param materialLog 设置属性MaterialLog的值
	 * @author 王通<br/>
	 */
	public void setMaterialLog(MetaMaterialLog materialLog) {
		this.materialLog = materialLog;
	}



	/**
	 * 属性 createUserName getter方法
	 * @return 属性createUserName
	 * @author 王通<br/>
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * 属性 createUserName setter方法
	 * @param createUserName 设置属性createUserName的值
	 * @author 王通<br/>
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public MaterialLogVo searchCache () {
		return this;
	}
	
	@Override
	public Example getExample() {
		Example example = new Example(MetaMaterialLog.class);
		example.setOrderByClause("create_time desc");
		Criteria c = example.createCriteria();
		c.andEqualTo("warehouseId", this.materialLog.getWarehouseId());
		c.andEqualTo("orgId", this.materialLog.getOrgId());
//		if(!StringUtil.isTrimEmpty(materialNo)) {
//			c.andEqualTo("orgName", this.getMaterialNo());
//		}
		return example;
	}

	/**
	 * 属性 startDate getter方法
	 * @return 属性startDate
	 * @author 王通<br/>
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 属性 startDate setter方法
	 * @param startDate 设置属性startDate的值
	 * @author 王通<br/>
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * 属性 endDate getter方法
	 * @return 属性endDate
	 * @author 王通<br/>
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 属性 endDate setter方法
	 * @param endDate 设置属性endDate的值
	 * @author 王通<br/>
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
