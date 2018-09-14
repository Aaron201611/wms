package com.yunkouan.wms.modules.sys.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 【企业角色】实体类
 * @author tphe06 2017年2月14日
 */
public class SysRole extends BaseEntity {
	private static final long serialVersionUID = -5614862796740568262L;

	/**角色id*/
	@Id
	private String roleId;

	/**角色编号*/
	@Length(max=32,message="{valid_role_roleNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_role_roleNo_notnull}",groups={ValidUpdate.class})
	private String roleNo;

	/**角色名称*/
	@Length(max=32,message="{valid_role_roleName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_role_roleName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String roleName;

	/**角色状态*/
//	@NotNull(message="{valid_role_roleStatus_notnull}",groups={ValidUpdate.class})
    private Integer roleStatus;

	/**组织id*/
	@Length(max=64,message="{valid_role_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_role_orgId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String orgId;

	/**角色描述*/
	@Length(max=1024,message="{valid_role_roleDesc_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String roleDesc;

	private Integer roleId2;

    @Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.roleId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.roleStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
		super.clear();
	}

    public String getRoleId() {
        return roleId;
    }

    public SysRole setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public SysRole setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
        return this;
    }

    public Integer getRoleStatus() {
        return roleStatus;
    }

    public SysRole setRoleStatus(Integer roleStatus) {
        this.roleStatus = roleStatus;
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public SysRole setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public SysRole setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
        return this;
    }

    public Integer getRoleId2() {
        return roleId2;
    }

    public SysRole setRoleId2(Integer roleId2) {
        this.roleId2 = roleId2;
        return this;
    }

	public String getRoleNo() {
		return roleNo;
	}

	public SysRole setRoleNo(String roleNo) {
		this.roleNo = roleNo;
		return this;
	}
}