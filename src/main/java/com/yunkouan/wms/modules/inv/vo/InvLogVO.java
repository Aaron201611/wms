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

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 库存日志VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvLogVO extends BaseVO {

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
	public InvLogVO(){}
	
	/**
	 * 
	 * 构造方法
	 * @param shift
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvLogVO(InvLog invLog){
		this.invLog = invLog;
	}

	/**
	 * 
	 * 日志对象
	 * @version 2017年3月2日 上午11:45:30<br/>
	 * @author 王通<br/>
	 */
	private InvLog invLog;

	/**
	 * 日志类型中文
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String logTypeName;

	/**
	 * 操作类型中文
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String opTypeName;

	/**
	 * 货主组织名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String ownerName;
	/**
	 * 货品名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String skuName;
	/**
	 * 货品状态名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String skuStatusName;
	/**
	 * 货品代码
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String skuNo;
	/**
	 * 货品规格
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String specModel;
	/**
	 * 货品单位
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String measureUnit;
	/**
	 * 库位名称
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String locationName;

	/**
	 * 仓库名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String warehouseName;

	/**
	 * 企业名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String orgName;
	/**
	 * 创建人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String createUserName;
	/**
	 * 创建人用户名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String createUserNo;
	/**
	 * 修改人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String updateUserName;
	/**
	 * 作业人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String opPersonName;
	/**
	 * 创建起始日期
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date startDate;
	/**
	 * 创建截止日期
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date endDate;

	/**outDate:过期时间**/
	private String outDate;
	/**startIndex:过期开始记录index**/
	private Integer startIndex;
	/**endIndex:过期结束记录index**/
	private Integer endIndex;
	/**sum:过期记录数量**/
	private Integer sum;
	/**findAdjustment:是否只查询调整类型日志  null or 0 不是 1 是*/
	private Integer findAdjustment;
	@Override
	public Example getExample() {
		Example example = new Example(InvLog.class);
		example.setOrderByClause("log_id2 desc");
		Criteria c = example.createCriteria();
		if(startIndex != null) {
			c.andGreaterThanOrEqualTo("logId2", startIndex);
		}
		if(endIndex != null) {
			c.andLessThanOrEqualTo("logId2", endIndex);
		}
		return example;
	}

	/* getset *************************************************/
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
	 * 属性 logTypeName getter方法
	 * @return 属性logTypeName
	 * @author 王通<br/>
	 */
	public String getLogTypeName() {
		return logTypeName;
	}

	/**
	 * 属性 logTypeName setter方法
	 * @param logTypeName 设置属性logTypeName的值
	 * @author 王通<br/>
	 */
	public void setLogTypeName(String logTypeName) {
		this.logTypeName = logTypeName;
	}

	/**
	 * 属性 opTypeName getter方法
	 * @return 属性opTypeName
	 * @author 王通<br/>
	 */
	public String getOpTypeName() {
		return opTypeName;
	}

	/**
	 * 属性 opTypeName setter方法
	 * @param opTypeName 设置属性opTypeName的值
	 * @author 王通<br/>
	 */
	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
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

	/**
	 * 属性 updateUserName getter方法
	 * @return 属性updateUserName
	 * @author 王通<br/>
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * 属性 updateUserName setter方法
	 * @param updateUserName 设置属性updateUserName的值
	 * @author 王通<br/>
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
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

	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public InvLogVO searchCache () {
		return this;
	}
	
	/**
	 * 属性 ownerName getter方法
	 * @return 属性ownerName
	 * @author 王通<br/>
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * 属性 ownerName setter方法
	 * @param ownerName 设置属性ownerName的值
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

	public Integer getStartIndex() {
		return startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	/**
	 * 属性 createUserNo getter方法
	 * @return 属性createUserNo
	 * @author 王通<br/>
	 */
	public String getCreateUserNo() {
		return createUserNo;
	}

	/**
	 * 属性 createUserNo setter方法
	 * @param createUserNo 设置属性createUserNo的值
	 * @author 王通<br/>
	 */
	public void setCreateUserNo(String createUserNo) {
		this.createUserNo = createUserNo;
	}

	public Integer getFindAdjustment() {
		return findAdjustment==null?0:1;
	}
	

	public void setFindAdjustment(Integer findAdjustment) {
		this.findAdjustment = findAdjustment;
	}

	public String getSkuStatusName() {
		return skuStatusName;
	}

	public void setSkuStatusName(String skuStatusName) {
		this.skuStatusName = skuStatusName;
	}
	
	
}