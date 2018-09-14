package com.yunkouan.wms.modules.inv.entity;

import java.util.Date;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;

public class InvCount  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:59:00<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 2770426226238863854L;

	@Id
	private String countId;
	
	private String countNo;

    private Integer countType;

    private Integer isBlockLocation;

    private Integer countStatus;

    private String opPerson;
    
    private String locationId;
    
    private String skuId;
    
    private Date opTime;
    
    private Integer result;
    
    private String transStatus;

    private String note;

    private String orgId;

    private String warehouseId;
    
    private String owner;

    private Integer countId2;
    

	/**
	 * 申请人
	 */
	private String applyPerson;
	/**
	 * 申请时间
	 */
	private String applyTime;
	/**
	 * 监管仓库名称
	 */
	private String applyWarehouse;
	/**
	 * 海关代码
	 */
	private String hgNo;
	/**
	 * 动碰次数
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private Integer shiftTimes;

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId == null ? null : countId.trim();
    }

    public Integer getCountType() {
        return countType;
    }

    public void setCountType(Integer countType) {
        this.countType = countType;
    }

    public Integer getIsBlockLocation() {
        return isBlockLocation;
    }

    public void setIsBlockLocation(Integer isBlockLocation) {
        this.isBlockLocation = isBlockLocation;
    }

    public Integer getCountStatus() {
        return countStatus;
    }

    public void setCountStatus(Integer countStatus) {
        this.countStatus = countStatus;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
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

    public Integer getCountId2() {
        return countId2;
    }

    public void setCountId2(Integer countId2) {
        this.countId2 = countId2;
    }

	/**
	 * 属性 countNo getter方法
	 * @return 属性countNo
	 * @author 王通<br/>
	 */
	public String getCountNo() {
		return countNo;
	}

	/**
	 * 属性 countNo setter方法
	 * @param countNo 设置属性countNo的值
	 * @author 王通<br/>
	 */
	public void setCountNo(String countNo) {
		this.countNo = countNo;
	}

	/**
	 * 属性 owner getter方法
	 * @return 属性owner
	 * @author 王通<br/>
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 属性 owner setter方法
	 * @param owner 设置属性owner的值
	 * @author 王通<br/>
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 属性 opPerson getter方法
	 * @return 属性opPerson
	 * @author 王通<br/>
	 */
	public String getOpPerson() {
		return opPerson;
	}

	/**
	 * 属性 opPerson setter方法
	 * @param opPerson 设置属性opPerson的值
	 * @author 王通<br/>
	 */
	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson;
	}

	/**
	 * 属性 shiftTimes getter方法
	 * @return 属性shiftTimes
	 * @author 王通<br/>
	 */
	public Integer getShiftTimes() {
		return shiftTimes;
	}

	/**
	 * 属性 shiftTimes setter方法
	 * @param shiftTimes 设置属性shiftTimes的值
	 * @author 王通<br/>
	 */
	public void setShiftTimes(Integer shiftTimes) {
		this.shiftTimes = shiftTimes;
	}

	/**
	 * 属性 result getter方法
	 * @return 属性result
	 * @author 王通<br/>
	 */
	public Integer getResult() {
		return result;
	}

	/**
	 * 属性 result setter方法
	 * @param result 设置属性result的值
	 * @author 王通<br/>
	 */
	public void setResult(Integer result) {
		this.result = result;
	}

	/**
	 * 属性 locationId getter方法
	 * @return 属性locationId
	 * @author 王通<br/>
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * 属性 locationId setter方法
	 * @param locationId 设置属性locationId的值
	 * @author 王通<br/>
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * 属性 skuId getter方法
	 * @return 属性skuId
	 * @author 王通<br/>
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * 属性 skuId setter方法
	 * @param skuId 设置属性skuId的值
	 * @author 王通<br/>
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getApplyPerson() {
		return applyPerson;
	}
	

	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}
	

	public String getApplyTime() {
		return applyTime;
	}
	

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	

	public String getApplyWarehouse() {
		return applyWarehouse;
	}
	

	public void setApplyWarehouse(String applyWarehouse) {
		this.applyWarehouse = applyWarehouse;
	}
	

	public String getHgNo() {
		return hgNo;
	}
	

	public void setHgNo(String hgNo) {
		this.hgNo = hgNo;
	}
	
	
	
}