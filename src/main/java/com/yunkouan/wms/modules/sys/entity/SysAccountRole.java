package com.yunkouan.wms.modules.sys.entity;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;

/**
 * 【企业帐号-角色授权】实体类
 * @author tphe06 2017年2月16日
 */
public class SysAccountRole extends BaseEntity {
	private static final long serialVersionUID = -5525269224569044653L;

	/**主键**/
	@Id
	private String accountRoleId;
	/**帐号id**/
    private String accountId;
    /**角色id**/
    private String roleId;
    /**仓库id**/
    private String warehouseId;
    /**企业id**/
    private String orgId;

    private Integer userRoleId2;

    public String getAccountRoleId() {
        return accountRoleId;
    }

    public SysAccountRole setAccountRoleId(String accountRoleId) {
        this.accountRoleId = accountRoleId == null ? null : accountRoleId.trim();
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public SysAccountRole setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public SysAccountRole setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
        return this;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public SysAccountRole setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public SysAccountRole setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public Integer getUserRoleId2() {
        return userRoleId2;
    }

    public SysAccountRole setUserRoleId2(Integer userRoleId2) {
        this.userRoleId2 = userRoleId2;
        return this;
    }
}