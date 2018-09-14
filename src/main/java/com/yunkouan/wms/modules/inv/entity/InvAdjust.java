package com.yunkouan.wms.modules.inv.entity;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;

public class InvAdjust  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:59:07<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 8326054933532182882L;

	@Id
	private String adjustId;
	
	private String adjustNo;

    private String countId;

    private Integer adjustStatus;

    private String owner;

    private String orgId;

    private String warehouseId;

    private Integer adjustId2;

    private Integer dataFrom;
    
    private String note;

    public String getAdjustId() {
        return adjustId;
    }

    public void setAdjustId(String adjustId) {
        this.adjustId = adjustId == null ? null : adjustId.trim();
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId == null ? null : countId.trim();
    }

    public Integer getAdjustStatus() {
        return adjustStatus;
    }

    public void setAdjustStatus(Integer adjustStatus) {
        this.adjustStatus = adjustStatus;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
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

    public Integer getAdjustId2() {
        return adjustId2;
    }

    public void setAdjustId2(Integer adjustId2) {
        this.adjustId2 = adjustId2;
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
    }

	/**
	 * 属性 adjustNo getter方法
	 * @return 属性adjustNo
	 * @author 王通<br/>
	 */
	public String getAdjustNo() {
		return adjustNo;
	}

	/**
	 * 属性 adjustNo setter方法
	 * @param adjustNo 设置属性adjustNo的值
	 * @author 王通<br/>
	 */
	public void setAdjustNo(String adjustNo) {
		this.adjustNo = adjustNo;
	}

	/**
	 * 属性 note getter方法
	 * @return 属性note
	 * @author 王通<br/>
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 属性 note setter方法
	 * @param note 设置属性note的值
	 * @author 王通<br/>
	 */
	public void setNote(String note) {
		this.note = note;
	}
}