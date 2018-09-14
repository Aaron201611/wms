package com.yunkouan.wms.modules.sys.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import com.yunkouan.base.BaseEntity;

/**
 * 【企业角色授权】实体类
 * @author tphe06 2017年2月15日
 */
public class SysRoleAuth extends BaseEntity {
	private static final long serialVersionUID = -6702116378046945685L;

	/**企业角色授权id**/
	@Id
	private String roleAuthId;
	/**角色id**/
    private String roleId;
    /**企业权限id**/
    private String orgAuthId;
    /**企业id**/
    private String orgId;

    private Integer roleAuthId2;

	/**权限id*/
    @Transient
	private String authId;
    /**权限名称*/
    @Transient
    private String authName;
    /**权限URL地址*/
    @Transient
    private String authUrl;
    @Transient
    private String parentId;
    @Transient
    private Integer authStatus;
    @Transient
    private Integer authLevelMin;
    @Transient
    private Integer authLevel;

    public String getRoleAuthId() {
        return roleAuthId;
    }

    public SysRoleAuth setRoleAuthId(String roleAuthId) {
        this.roleAuthId = roleAuthId == null ? null : roleAuthId.trim();
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public SysRoleAuth setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public SysRoleAuth setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getOrgAuthId() {
        return orgAuthId;
    }

    public SysRoleAuth setOrgAuthId(String orgAuthId) {
        this.orgAuthId = orgAuthId == null ? null : orgAuthId.trim();
        return this;
    }

    public Integer getRoleAuthId2() {
        return roleAuthId2;
    }

    public SysRoleAuth setRoleAuthId2(Integer roleAuthId2) {
        this.roleAuthId2 = roleAuthId2;
        return this;
    }

	public String getAuthId() {
		return authId;
	}

	public String getAuthName() {
		return authName;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public SysRoleAuth setAuthId(String authId) {
		this.authId = authId;
		return this;
	}

	public SysRoleAuth setAuthName(String authName) {
		this.authName = authName;
		return this;
	}

	public SysRoleAuth setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
		return this;
	}

	public String getParentId() {
		return parentId;
	}

	public Integer getAuthStatus() {
		return authStatus;
	}

	public SysRoleAuth setParentId(String parentId) {
		this.parentId = parentId;
		return this;
	}

	public SysRoleAuth setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
		return this;
	}

	public Integer getAuthLevelMin() {
		return authLevelMin;
	}

	public SysRoleAuth setAuthLevelMin(Integer authLevelMin) {
		this.authLevelMin = authLevelMin;
		return this;
	}

	public Integer getAuthLevel() {
		return authLevel;
	}

	public SysRoleAuth setAuthLevel(Integer authLevel) {
		this.authLevel = authLevel;
		return this;
	}
}