package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 工作区实体类
 * @author tphe06 2017年2月21日
 */
public class MetaWorkArea extends BaseEntity {
	private static final long serialVersionUID = -4908462974490129370L;

	/**工作区id**/
	@Id
	@NotNull(message="{valid_workarea_workAreaId_notnull}",groups={ValidUpdate.class})
	private String workAreaId;

	/**工作区编号**/
	private String workAreaNo;

	/**工作区名称**/
	@Length(max=32,message="{valid_workarea_workAreaName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_workarea_workAreaName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String workAreaName;

    /**工作区状态**/
    private Integer workAreaStatus;

    /**组织id**/
    private String orgId;

    /**仓库id**/
    private String warehouseId;

    /**库区id**/
    private String areaId;

	@Length(max=512,message="{valid_workarea_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String note;

    private Integer workAreaId2;

    @Override
	public void clear() {
    	/**不能由前端修改，添加时候由程序赋值，修改时候置空不更新数据库的值**/
    	this.workAreaNo = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.workAreaStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.warehouseId = null;
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.workAreaId2 = null;
		super.clear();
	}

	public String getWorkAreaId() {
        return workAreaId;
    }

    public MetaWorkArea setWorkAreaId(String workAreaId) {
        this.workAreaId = workAreaId == null ? null : workAreaId.trim();
        return this;
    }

    public String getWorkAreaName() {
        return workAreaName;
    }

    public MetaWorkArea setWorkAreaName(String workAreaName) {
        this.workAreaName = workAreaName == null ? null : workAreaName.trim();
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public MetaWorkArea setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public MetaWorkArea setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
        return this;
    }

    public String getAreaId() {
        return areaId;
    }

    public MetaWorkArea setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
        return this;
    }

    public String getNote() {
        return note;
    }

    public MetaWorkArea setNote(String note) {
        this.note = note == null ? null : note.trim();
        return this;
    }

    public Integer getWorkAreaId2() {
        return workAreaId2;
    }

    public MetaWorkArea setWorkAreaId2(Integer workAreaId2) {
        this.workAreaId2 = workAreaId2;
        return this;
    }

	public String getWorkAreaNo() {
		return workAreaNo;
	}

	public Integer getWorkAreaStatus() {
		return workAreaStatus;
	}

	public MetaWorkArea setWorkAreaNo(String workAreaNo) {
		this.workAreaNo = workAreaNo;
		return this;
	}

	public MetaWorkArea setWorkAreaStatus(Integer workAreaStatus) {
		this.workAreaStatus = workAreaStatus;
		return this;
	}
}