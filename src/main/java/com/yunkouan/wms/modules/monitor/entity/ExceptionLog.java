package com.yunkouan.wms.modules.monitor.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;

public class ExceptionLog extends BaseEntity {
	/**
	 * 
	 * @version 2017年3月13日 下午1:55:59<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7609792741588444684L;

	@Id
    @NotNull(message="{valid_exp_ex_log_id_is_null}",groups={ValidUpdate.class})
    private String exLogId;
	
    private String exNo;
    
    /**
     * 异常类型
     */
    @NotNull(message="{valid_exp_ex_type_is_null}",groups={ValidSave.class})
    private Integer exType;
    /**
     * 异常来源
     */
    @NotNull(message="{valid_exp_ex_data_from_is_null}",groups={ValidSave.class})
    private Integer exDataFrom;

    /**
     * 相关单号
     */
    @NotNull(message="{valid_exp_involve_bill_is_null}",groups={ValidSave.class})
    @Length(max=32,message="{valid_exp_involve_bill_length}",groups={ValidSave.class})
    private String involveBill;

    /**
     * 异常状态
     */
    private Integer exStatus;
    /**
     * 异常详情
     */
    @NotNull(message="{valid_exp_ex_desc_is_null}",groups={ValidSave.class})
    @Length(max=512,message="{valid_exp_ex_desc_length}",groups={ValidSave.class})
    private String exDesc;
    
    /**
     * 处理情况
     */
    @NotNull(message="{valid_exp_handle_msg_is_null}",groups={ValidUpdate.class})
    @Length(max=512,message="{valid_exp_handle_msg_length}",groups={ValidUpdate.class})
    private String handleMsg;

    private String orgId;

    private String warehouseId;
    
    /**
     * 异常级别
     */
    @NotNull(message="{valid_exp_ex_level_is_null}",groups={ValidSave.class})
    private Integer exLevel;

    private Integer exLogId2;
    
    /**
     * 发生时间
     */
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date occurrenceTime;

    public String getExLogId() {
        return exLogId;
    }

    public void setExLogId(String exLogId) {
        this.exLogId = exLogId == null ? null : exLogId.trim();
    }

    public Integer getExType() {
        return exType;
    }

    public void setExType(Integer exType) {
        this.exType = exType;
    }

    public String getInvolveBill() {
        return involveBill;
    }

    public void setInvolveBill(String involveBill) {
        this.involveBill = involveBill == null ? null : involveBill.trim();
    }

    public Integer getExStatus() {
        return exStatus;
    }

    public void setExStatus(Integer exStatus) {
        this.exStatus = exStatus;
    }

    public String getExDesc() {
        return exDesc;
    }

    public void setExDesc(String exDesc) {
        this.exDesc = exDesc == null ? null : exDesc.trim();
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

    public Integer getExLevel() {
        return exLevel;
    }

    public void setExLevel(Integer exLevel) {
        this.exLevel = exLevel;
    }

    public Integer getExLogId2() {
        return exLogId2;
    }

    public void setExLogId2(Integer exLogId2) {
        this.exLogId2 = exLogId2;
    }

	/**
	 * 属性 exNo getter方法
	 * @return 属性exNo
	 * @author 王通<br/>
	 */
	public String getExNo() {
		return exNo;
	}

	/**
	 * 属性 exNo setter方法
	 * @param exNo 设置属性exNo的值
	 * @author 王通<br/>
	 */
	public void setExNo(String exNo) {
		this.exNo = exNo;
	}

	/**
	 * 属性 occurrenceTime getter方法
	 * @return 属性occurrenceTime
	 * @author 王通<br/>
	 */
	public Date getOccurrenceTime() {
		return occurrenceTime;
	}

	/**
	 * 属性 occurrenceTime setter方法
	 * @param occurrenceTime 设置属性occurrenceTime的值
	 * @author 王通<br/>
	 */
	public void setOccurrenceTime(Date occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}

	/**
	 * 属性 handleMsg getter方法
	 * @return 属性handleMsg
	 * @author 王通<br/>
	 */
	public String getHandleMsg() {
		return handleMsg;
	}

	/**
	 * 属性 handleMsg setter方法
	 * @param handleMsg 设置属性handleMsg的值
	 * @author 王通<br/>
	 */
	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}

	/**
	 * 属性 exDataFrom getter方法
	 * @return 属性exDataFrom
	 * @author 王通<br/>
	 */
	public Integer getExDataFrom() {
		return exDataFrom;
	}

	/**
	 * 属性 exDataFrom setter方法
	 * @param exDataFrom 设置属性exDataFrom的值
	 * @author 王通<br/>
	 */
	public void setExDataFrom(Integer exDataFrom) {
		this.exDataFrom = exDataFrom;
	}
}