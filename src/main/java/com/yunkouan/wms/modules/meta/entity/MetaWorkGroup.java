package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
* @Description: 【班组】实体类
* @author tphe06
* @date 2017年3月14日
*/
public class MetaWorkGroup extends BaseEntity {
	private static final long serialVersionUID = -9083325909304286705L;

	/**@Fields 班组ID */
	@Id
	@NotNull(message="{valid_workgroup_workGroupId_notnull}",groups={ValidUpdate.class})
	private String workGroupId;
	/**@Fields 班组编码 */
	private String workGroupNo;
	/**@Fields 班组名称 */
	@Length(max=32,message="{valid_workgroup_workGroupName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_workgroup_workGroupName_notnull}",groups={ValidSave.class,ValidUpdate.class})
	private String workGroupName;
	/**@Fields 班组状态 */
	private Integer workGroupStatus;
	/**@Fields 组织id */
	@Length(max=32,message="{valid_workgroup_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;
	/**@Fields 仓库id */
	@Length(max=32,message="{valid_workgroup_warehouseId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String warehouseId;
	/**@Fields 备注 */
	@Length(max=512,message="{valid_workgroup_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	private String note;
	private Integer workGroupId2;

	@Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.workGroupId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.workGroupStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.warehouseId = null;
		super.clear();
	}

	public String getWorkGroupId() {
		return workGroupId;
	}

	public String getWorkGroupNo() {
		return workGroupNo;
	}

	public String getWorkGroupName() {
		return workGroupName;
	}

	public Integer getWorkGroupStatus() {
		return workGroupStatus;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public String getNote() {
		return note;
	}

	public Integer getWorkGroupId2() {
		return workGroupId2;
	}

	public MetaWorkGroup setWorkGroupId(String workGroupId) {
		this.workGroupId = workGroupId;
		return this;
	}

	public MetaWorkGroup setWorkGroupNo(String workGroupNo) {
		this.workGroupNo = workGroupNo;
		return this;
	}

	public MetaWorkGroup setWorkGroupName(String workGroupName) {
		this.workGroupName = workGroupName;
		return this;
	}

	public MetaWorkGroup setWorkGroupStatus(Integer workGroupStatus) {
		this.workGroupStatus = workGroupStatus;
		return this;
	}

	public MetaWorkGroup setOrgId(String orgId) {
		this.orgId = orgId;
		return this;
	}

	public MetaWorkGroup setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
		return this;
	}

	public MetaWorkGroup setNote(String note) {
		this.note = note;
		return this;
	}

	public MetaWorkGroup setWorkGroupId2(Integer workGroupId2) {
		this.workGroupId2 = workGroupId2;
		return this;
	}
}