/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年4月19日 下午4:55:53<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;


/**
 * 月台实体类<br/><br/>
 * @version 2017年4月19日 下午4:55:53<br/>
 * @author andy wang<br/>
 */
public class MetaDock extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 月台id
	 * @version 2017年4月19日下午4:52:30<br/>
	 * @author andy wang<br/>
	 */
	@Id
	private String dockId;

	/**
	 * 月台代码
	 * @version 2017年4月19日下午4:52:41<br/>
	 * @author andy wang<br/>
	 */
	@NotNull(message = "{valid_main_dock_dockNo_notnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=32, message="{valid_main_dock_dockNo_length}",groups={ValidSave.class,ValidUpdate.class})
    private String dockNo;

    /**
	 * 月台名称
	 * @version 2017年4月19日下午4:52:48<br/>
	 * @author andy wang<br/>
	 */
	@NotNull(message = "{valid_main_dock_dockName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=16, message="{valid_main_dock_dockName_length}",groups={ValidSave.class,ValidUpdate.class})
    private String dockName;

    /**
	 * 库区id
	 * @version 2017年4月19日下午4:52:57<br/>
	 * @author andy wang<br/>
	 */
    private String areaId;

    /**
	 * 企业id
	 * @version 2017年4月19日下午4:53:05<br/>
	 * @author andy wang<br/>
	 */
    private String orgId;

    
    /**
	 * 仓库id
	 * @version 2017年4月19日下午4:53:13<br/>
	 * @author andy wang<br/>
	 */
    private String warehouseId;

    /**
	 * 是否收货月台
	 * 1 - 收货月台
	 * 0 - 不是收货月台
	 * @version 2017年4月19日下午4:53:21<br/>
	 * @author andy wang<br/>
	 */
    private Integer isIn;

    /**
	 * 是否发货月台
	 * 1 - 发货月台
	 * @version 2017年4月19日下午4:53:41<br/>
	 * @author andy wang<br/>
	 */
    private Integer isOut;

    /**
	 * 月台排序id
	 * @version 2017年4月19日下午4:54:19<br/>
	 * @author andy wang<br/>
	 */
    private Integer dockId2;
    
    /**
	 * 月台状态
	 * @version 2017年4月19日下午4:54:27<br/>
	 * @author andy wang<br/>
	 */
    private Integer dockStatus;
    
    /**
	 * 备注
	 * @version 2017年4月19日下午4:54:35<br/>
	 * @author andy wang<br/>
	 */
    @Length(max=512, message="{valid_main_dock_note_length}",groups={ValidSave.class,ValidUpdate.class})
    private String note;

	/**
	 * 属性 dockId getter方法
	 * @return 属性dockId
	 * @author andy wang<br/>
	 */
	public String getDockId() {
		return dockId;
	}

	/**
	 * 属性 dockId setter方法
	 * @param dockId 设置属性dockId的值
	 * @author andy wang<br/>
	 */
	public void setDockId(String dockId) {
		this.dockId = dockId;
	}

	/**
	 * 属性 dockNo getter方法
	 * @return 属性dockNo
	 * @author andy wang<br/>
	 */
	public String getDockNo() {
		return dockNo;
	}

	/**
	 * 属性 dockNo setter方法
	 * @param dockNo 设置属性dockNo的值
	 * @author andy wang<br/>
	 */
	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}

	/**
	 * 属性 dockName getter方法
	 * @return 属性dockName
	 * @author andy wang<br/>
	 */
	public String getDockName() {
		return dockName;
	}

	/**
	 * 属性 dockName setter方法
	 * @param dockName 设置属性dockName的值
	 * @author andy wang<br/>
	 */
	public void setDockName(String dockName) {
		this.dockName = dockName;
	}

	/**
	 * 属性 areaId getter方法
	 * @return 属性areaId
	 * @author andy wang<br/>
	 */
	public String getAreaId() {
		return areaId;
	}

	/**
	 * 属性 areaId setter方法
	 * @param areaId 设置属性areaId的值
	 * @author andy wang<br/>
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	/**
	 * 属性 orgId getter方法
	 * @return 属性orgId
	 * @author andy wang<br/>
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * 属性 orgId setter方法
	 * @param orgId 设置属性orgId的值
	 * @author andy wang<br/>
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 属性 warehouseId getter方法
	 * @return 属性warehouseId
	 * @author andy wang<br/>
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * 属性 warehouseId setter方法
	 * @param warehouseId 设置属性warehouseId的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * 属性 isIn getter方法
	 * @return 属性isIn
	 * @author andy wang<br/>
	 */
	public Integer getIsIn() {
		return isIn;
	}

	/**
	 * 属性 isIn setter方法
	 * @param isIn 设置属性isIn的值
	 * @author andy wang<br/>
	 */
	public void setIsIn(Integer isIn) {
		this.isIn = isIn;
	}

	/**
	 * 属性 isOut getter方法
	 * @return 属性isOut
	 * @author andy wang<br/>
	 */
	public Integer getIsOut() {
		return isOut;
	}

	/**
	 * 属性 isOut setter方法
	 * @param isOut 设置属性isOut的值
	 * @author andy wang<br/>
	 */
	public void setIsOut(Integer isOut) {
		this.isOut = isOut;
	}

	/**
	 * 属性 dockId2 getter方法
	 * @return 属性dockId2
	 * @author andy wang<br/>
	 */
	public Integer getDockId2() {
		return dockId2;
	}

	/**
	 * 属性 dockId2 setter方法
	 * @param dockId2 设置属性dockId2的值
	 * @author andy wang<br/>
	 */
	public void setDockId2(Integer dockId2) {
		this.dockId2 = dockId2;
	}

	/**
	 * 属性 dockStatus getter方法
	 * @return 属性dockStatus
	 * @author andy wang<br/>
	 */
	public Integer getDockStatus() {
		return dockStatus;
	}

	/**
	 * 属性 dockStatus setter方法
	 * @param dockStatus 设置属性dockStatus的值
	 * @author andy wang<br/>
	 */
	public void setDockStatus(Integer dockStatus) {
		this.dockStatus = dockStatus;
	}

	/**
	 * 属性 note getter方法
	 * @return 属性note
	 * @author andy wang<br/>
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 属性 note setter方法
	 * @param note 设置属性note的值
	 * @author andy wang<br/>
	 */
	public void setNote(String note) {
		this.note = note;
	}
    
    
}