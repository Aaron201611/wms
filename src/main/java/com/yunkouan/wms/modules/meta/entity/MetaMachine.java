package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 设备实体类
 * @author tphe06 2017年2月21日
 */
public class MetaMachine extends BaseEntity {
	private static final long serialVersionUID = 5219552294331449875L;

	/**设备id**/
	@Id
	@NotNull(message="{valid_machine_machineId_notnull}",groups={ValidUpdate.class})
	private String machineId;

	/**设备编号**/
    private String machineNo;

    /**设备名称**/
	@Length(max=16,message="{valid_machine_machineName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_machine_machineName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String machineName;

    /**组织id**/
    private String orgId;

    /**仓库id**/
    private String warehouseId;

    /**设备状态**/
    private Integer machineStatus;

    /**设备工作状态**/
    private Integer workStatus;

    private Integer machineId2;

    @Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.machineId2 = null;
    	/**不能由前端修改，添加时候由程序赋值，修改时候置空不更新数据库的值**/
    	this.machineNo = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.machineStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.warehouseId = null;
		super.clear();
	}

	public String getMachineId() {
        return machineId;
    }

    public MetaMachine setMachineId(String machineId) {
        this.machineId = machineId == null ? null : machineId.trim();
        return this;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public MetaMachine setMachineNo(String machineNo) {
        this.machineNo = machineNo == null ? null : machineNo.trim();
        return this;
    }

    public String getMachineName() {
        return machineName;
    }

    public MetaMachine setMachineName(String machineName) {
        this.machineName = machineName == null ? null : machineName.trim();
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public MetaMachine setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public MetaMachine setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
        return this;
    }

    public Integer getMachineStatus() {
        return machineStatus;
    }

    public MetaMachine setMachineStatus(Integer machineStatus) {
        this.machineStatus = machineStatus;
        return this;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public MetaMachine setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
        return this;
    }

    public Integer getMachineId2() {
        return machineId2;
    }

    public MetaMachine setMachineId2(Integer machineId2) {
        this.machineId2 = machineId2;
        return this;
    }
}