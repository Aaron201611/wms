/**
 * Created on 2017年3月13日
 * ExceptionLogVO.java V1.0
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
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.rec.entity.RecAsn;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 异常管理
 * <br/><br/>
 * @Description 
 * @version 2017年3月13日 下午1:51:54<br/>
 * @author 王通<br/>
 */
public class ExceptionLogVO extends BaseVO {

	private static final long serialVersionUID = 7539617122998544603L;
	
	private ExceptionLog exceptionLog;
	
	private String exLevelName;
	
	private String exTypeName;

	private String exStatusName;
	
	private String exDataFromName;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date exOccurrenceTimeStart;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date exOccurrenceTimeEnd;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date exCreateTimeStart;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date exCreateTimeEnd;
	/**
	 * 构造方法
	 * @param exceptionLog2
	 * @version 2017年3月14日 下午1:16:23<br/>
	 * @author 王通<br/>
	 */
	public ExceptionLogVO(ExceptionLog exceptionLog) {
		this.exceptionLog = exceptionLog;
	}

	/**
	 * 构造方法
	 * @version 2017年3月14日 下午1:48:28<br/>
	 * @author 王通<br/>
	 */
	public ExceptionLogVO() {
	}

	/**
	 * 属性 exOccurrenceTimeStart getter方法
	 * @return 属性exOccurrenceTimeStart
	 * @author 王通<br/>
	 */
	public Date getExOccurrenceTimeStart() {
		return exOccurrenceTimeStart;
	}

	/**
	 * 属性 exOccurrenceTimeStart setter方法
	 * @param exOccurrenceTimeStart 设置属性exOccurrenceTimeStart的值
	 * @author 王通<br/>
	 */
	public void setExOccurrenceTimeStart(Date exOccurrenceTimeStart) {
		this.exOccurrenceTimeStart = exOccurrenceTimeStart;
	}

	/**
	 * 属性 exOccurrenceTimeEnd getter方法
	 * @return 属性exOccurrenceTimeEnd
	 * @author 王通<br/>
	 */
	public Date getExOccurrenceTimeEnd() {
		return exOccurrenceTimeEnd;
	}

	/**
	 * 属性 exOccurrenceTimeEnd setter方法
	 * @param exOccurrenceTimeEnd 设置属性exOccurrenceTimeEnd的值
	 * @author 王通<br/>
	 */
	public void setExOccurrenceTimeEnd(Date exOccurrenceTimeEnd) {
		this.exOccurrenceTimeEnd = exOccurrenceTimeEnd;
	}

	/**
	 * 属性 exCreateTimeStart getter方法
	 * @return 属性exCreateTimeStart
	 * @author 王通<br/>
	 */
	public Date getExCreateTimeStart() {
		return exCreateTimeStart;
	}

	/**
	 * 属性 exCreateTimeStart setter方法
	 * @param exCreateTimeStart 设置属性exCreateTimeStart的值
	 * @author 王通<br/>
	 */
	public void setExCreateTimeStart(Date exCreateTimeStart) {
		this.exCreateTimeStart = exCreateTimeStart;
	}

	/**
	 * 属性 exCreateTimeEnd getter方法
	 * @return 属性exCreateTimeEnd
	 * @author 王通<br/>
	 */
	public Date getExCreateTimeEnd() {
		return exCreateTimeEnd;
	}

	/**
	 * 属性 exCreateTimeEnd setter方法
	 * @param exCreateTimeEnd 设置属性exCreateTimeEnd的值
	 * @author 王通<br/>
	 */
	public void setExCreateTimeEnd(Date exCreateTimeEnd) {
		this.exCreateTimeEnd = exCreateTimeEnd;
	}

	/**
	 * 属性 exceptionLog getter方法
	 * @return 属性exceptionLog
	 * @author 王通<br/>
	 */
	public ExceptionLog getExceptionLog() {
		return exceptionLog;
	}

	/**
	 * 属性 exceptionLog setter方法
	 * @param exceptionLog 设置属性exceptionLog的值
	 * @author 王通<br/>
	 */
	public void setExceptionLog(ExceptionLog exceptionLog) {
		this.exceptionLog = exceptionLog;
	}

	/**
	 * 属性 exLevelName getter方法
	 * @return 属性exLevelName
	 * @author 王通<br/>
	 */
	public String getExLevelName() {
		return exLevelName;
	}

	/**
	 * 属性 exLevelName setter方法
	 * @param exLevelName 设置属性exLevelName的值
	 * @author 王通<br/>
	 */
	public void setExLevelName(String exLevelName) {
		this.exLevelName = exLevelName;
	}

	/**
	 * 属性 exTypeName getter方法
	 * @return 属性exTypeName
	 * @author 王通<br/>
	 */
	public String getExTypeName() {
		return exTypeName;
	}

	/**
	 * 属性 exTypeName setter方法
	 * @param exTypeName 设置属性exTypeName的值
	 * @author 王通<br/>
	 */
	public void setExTypeName(String exTypeName) {
		this.exTypeName = exTypeName;
	}

	/**
	 * 属性 exStatusName getter方法
	 * @return 属性exStatusName
	 * @author 王通<br/>
	 */
	public String getExStatusName() {
		return exStatusName;
	}

	/**
	 * 属性 exStatusName setter方法
	 * @param exStatusName 设置属性exStatusName的值
	 * @author 王通<br/>
	 */
	public void setExStatusName(String exStatusName) {
		this.exStatusName = exStatusName;
	}

	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:16:48<br/>
	 * @author 王通<br/>
	 */
	public ExceptionLogVO searchCache() {
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
		if ( this.exceptionLog == null ) {
			return null;
		}
		Example example = new Example(ExceptionLog.class);
		Criteria criteria = example.createCriteria();
		if ( this.exceptionLog.getExType() != null ) {
			criteria.andEqualTo("exType", this.exceptionLog.getExType());
		}
		if ( this.exceptionLog.getExLevel() != null ) {
			criteria.andEqualTo("exLevel", this.exceptionLog.getExLevel());
		}
		if ( !StringUtil.isTrimEmpty(this.exceptionLog.getInvolveBill()) ) {
			criteria.andLike("involveBill", StringUtil.likeEscapeH(this.exceptionLog.getInvolveBill()));
		}
		if ( !StringUtil.isTrimEmpty(this.exceptionLog.getExDesc()) ) {
			criteria.andLike("exDesc", StringUtil.likeEscapeH(this.exceptionLog.getExDesc()));
		}
		if ( this.getExOccurrenceTimeStart() != null ) {
			criteria.andGreaterThan("occurrenceTime",  this.getExOccurrenceTimeStart());
		}
		if ( this.getExOccurrenceTimeEnd() != null ) {
			criteria.andLessThan("occurrenceTime", DateUtil.setEndTime(this.getExOccurrenceTimeEnd()));
		}
		if ( this.getExCreateTimeStart() != null ) {
			criteria.andGreaterThan("createTime",  this.getExCreateTimeStart());
		}
		if ( this.getExCreateTimeEnd() != null ) {
			criteria.andLessThan("createTime", DateUtil.setEndTime(this.getExCreateTimeEnd()));
		}
		if(!StringUtil.isTrimEmpty(this.exceptionLog.getOrgId())){
			criteria.andEqualTo("orgId",this.exceptionLog.getOrgId());
		}
		if(!StringUtil.isTrimEmpty(this.exceptionLog.getWarehouseId())){
			criteria.andEqualTo("warehouseId",this.exceptionLog.getWarehouseId());
		}
		if ( this.exceptionLog.getExStatus() == null ) {
//			criteria.andNotEqualTo("exStatus", Constant.STATUS_CANCEL);
		} else {
			criteria.andEqualTo("exStatus", this.exceptionLog.getExStatus());
		}
		example.setOrderByClause("create_time desc");
		return example;
	}

	/**
	 * 属性 exDataFromName getter方法
	 * @return 属性exDataFromName
	 * @author 王通<br/>
	 */
	public String getExDataFromName() {
		return exDataFromName;
	}

	/**
	 * 属性 exDataFromName setter方法
	 * @param exDataFromName 设置属性exDataFromName的值
	 * @author 王通<br/>
	 */
	public void setExDataFromName(String exDataFromName) {
		this.exDataFromName = exDataFromName;
	}

}
