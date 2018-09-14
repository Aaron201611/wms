package com.yunkouan.wms.modules.ts.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class TsTask extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1785838871168324218L;

	@Id
	private String taskId;

	/**
	 * 作业人员
	 */
    private String opPerson;
	/**
	 * 单号Id
	 */
    private String opId;
	/**
	 * 通知时间
	 */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date noticeTime;
	/**
	 * 任务类型
	 */
    private Integer taskType;
    /**
     * 任务状态 0 打开，1 执行 ，99 取消
     */
    private Integer taskStatus;
	/**
	 * 企业id
	 */
    private String orgId;
	/**
	 * 仓库id
	 */
    private String warehouseId;

    private Integer taskId2;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

	public String getOpPerson() {
		return opPerson;
	}

	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson== null ? null : opPerson.trim();
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public Date getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId == null ? null : warehouseId.trim();
	}

	public Integer getTaskId2() {
		return taskId2;
	}

	public void setTaskId2(Integer taskId2) {
		this.taskId2 = taskId2;
	}
    
}