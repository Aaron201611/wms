package com.yunkouan.wms.modules.sys.vo;

import java.util.List;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.sys.entity.SysRole;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 角色数据传输对象
 * @author tphe06 2017年2月13日
 */
public final class RoleVo extends BaseVO {
	private static final long serialVersionUID = 4176086380884057204L;

	/**角色实体类*/
	@Valid
	private SysRole entity;
	/**@Fields 排序字段 */
	private String orderBy = "role_id2 desc";
	/**@Fields 角色编号模糊查询字段 */
	private String roleNoLike;
	/**@Fields 角色名称模糊查询字段 */
	private String roleNameLike;
	/**@Fields 角色描述模糊查询字段 */
	private String roleDescLike;
	/**@Fields 不查询的角色状态 */
	private Integer roleStatusNo;
	/**@Fields 所属企业 */
	private SysOrg org;

	private List<RoleAuthVo> list;
	private String statusName;

	public RoleVo(){}
	public RoleVo(SysRole entity){
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.statusName = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getRoleStatus());
//		this.statusName = Constant.getStatus(this.entity.getRoleStatus());
	}

	@Override
	public Example getExample() {
		Example example = new Example(SysRole.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(roleNoLike)) {
			c.andLike("roleNo", "%"+roleNoLike+"%");
		}
		if(StringUtil.isNoneBlank(roleNameLike)) {
			c.andLike("roleName", "%"+roleNameLike+"%");
		}
		if(StringUtil.isNoneBlank(roleDescLike)) {
			c.andLike("roleDesc", "%"+roleDescLike+"%");
		}
		if(StringUtils.isNoneBlank(entity.getRoleNo())) {
			c.andEqualTo("roleNo", entity.getRoleNo());
		}
		if(StringUtil.isNoneBlank(entity.getRoleName())) {
			c.andEqualTo("roleName", entity.getRoleName());
		}
		if(StringUtil.isNoneBlank(entity.getRoleDesc())) {
			c.andEqualTo("roleDesc", entity.getRoleDesc());
		}
		if(entity.getRoleStatus() != null) {
			c.andEqualTo("roleStatus", entity.getRoleStatus());
		}
		if(roleStatusNo != null) {
			c.andNotEqualTo("roleStatus", roleStatusNo);
		}
		c.andEqualTo("orgId", entity.getOrgId());
		return example;
	}

	public SysRole getEntity() {
		return entity;
	}
	public RoleVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}

	public List<RoleAuthVo> getList() {
		return list;
	}

	public RoleVo setList(List<RoleAuthVo> list) {
		this.list = list;
		return this;
	}
	public String getStatusName() {
		return statusName;
	}
	public RoleVo setStatusName(String statusName) {
		this.statusName = statusName;
		return this;
	}
	public RoleVo setRoleNoLike(String roleNoLike) {
		this.roleNoLike = roleNoLike;
		return this;
	}
	public RoleVo setRoleNameLike(String roleNameLike) {
		this.roleNameLike = roleNameLike;
		return this;
	}
	public RoleVo setRoleDescLike(String roleDescLike) {
		this.roleDescLike = roleDescLike;
		return this;
	}
	public Integer getRoleStatusNo() {
		return roleStatusNo;
	}
	public RoleVo setRoleStatusNo(Integer roleStatusNo) {
		this.roleStatusNo = roleStatusNo;
		return this;
	}
	public SysOrg getOrg() {
		return org;
	}
	public RoleVo setOrg(SysOrg org) {
		this.org = org;
		return this;
	}
}