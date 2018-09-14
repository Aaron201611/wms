package com.yunkouan.wms.modules.inv.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

public class InvShift  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:57:52<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = -4691880272923603353L;

	/**
	 * 移位单id
	 */
	@Id
	private String shiftId;

	private String shiftNo;
	
	private String involveBill;
	/**
	 * 移位单类型
	 */
	@Max(value=9999,message="{valid_shift_shift_type_max}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_shift_shift_type_is_null}",groups={ValidUpdate.class})
    private Integer shiftType;
	
	/**
	 * 移位单状态
	 */
	@Max(value=9999,message="{valid_shift_shift_status_max}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private Integer shiftStatus;
    /**
	 * 作业人员
	 */
    private String opPerson;
    
    /**
     * 作业时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date opTime;
    /**
	 * 备注
	 */
	@Length(max=256,message="{valid_shift_note_out_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String note;
	/**
	 * 组织ID
	 */
    private String orgId;
    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 自增字段
     */
    private Integer shiftId2;

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId == null ? null : shiftId.trim();
    }

    public Integer getShiftType() {
        return shiftType;
    }

    public void setShiftType(Integer shiftType) {
        this.shiftType = shiftType;
    }

    public Integer getShiftStatus() {
        return shiftStatus;
    }

    public void setShiftStatus(Integer shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public String getOpPerson() {
        return opPerson;
    }

    public void setOpPerson(String opPerson) {
        this.opPerson = opPerson == null ? null : opPerson.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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

    public Integer getShiftId2() {
        return shiftId2;
    }

    public void setShiftId2(Integer shiftId2) {
        this.shiftId2 = shiftId2;
    }

	/**
	 * 属性 shiftNo getter方法
	 * @return 属性shiftNo
	 * @author 王通<br/>
	 */
	public String getShiftNo() {
		return shiftNo;
	}

	/**
	 * 属性 shiftNo setter方法
	 * @param shiftNo 设置属性shiftNo的值
	 * @author 王通<br/>
	 */
	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
	}

	/**
	 * 属性 opTime getter方法
	 * @return 属性opTime
	 * @author 王通<br/>
	 */
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * 属性 opTime setter方法
	 * @param opTime 设置属性opTime的值
	 * @author 王通<br/>
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	/**
	 * 属性 involveBill getter方法
	 * @return 属性involveBill
	 * @author 王通<br/>
	 */
	public String getInvolveBill() {
		return involveBill;
	}

	/**
	 * 属性 involveBill setter方法
	 * @param involveBill 设置属性involveBill的值
	 * @author 王通<br/>
	 */
	public void setInvolveBill(String involveBill) {
		this.involveBill = involveBill;
	}
}