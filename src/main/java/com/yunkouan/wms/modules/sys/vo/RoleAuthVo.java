package com.yunkouan.wms.modules.sys.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.sys.entity.SysRoleAuth;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 角色授权数据传输类
 * @author tphe06 2017年2月15日
 */
public final class RoleAuthVo extends BaseVO {
	private static final long serialVersionUID = 4304420362367209447L;

	/**@Fields 角色授权实体类 */
	private SysRoleAuth entity;
	/**@Fields 排序字段 */
	private String orderBy = "role_auth_id2 desc";

	/**@Fields 配合前端显示，用来标识是否勾选 */
	private Boolean selectStatus;
	/**@Fields 子权限列表 */
	private List<RoleAuthVo> list;

	public RoleAuthVo(){}
	public RoleAuthVo(SysRoleAuth entity){
		this.entity = entity;
	}

	@Override
	public Example getExample() {
		Example example = new Example(SysRoleAuth.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		c.andEqualTo("roleId", entity.getRoleId());
		return example;
	}

	public SysRoleAuth getEntity() {
		return entity;
	}
	public RoleAuthVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public Boolean getSelectStatus() {
		return selectStatus;
	}
	public List<RoleAuthVo> getList() {
		return list;
	}
	public RoleAuthVo setSelectStatus(Boolean selectStatus) {
		this.selectStatus = selectStatus;
		return this;
	}
	public RoleAuthVo setList(List<RoleAuthVo> list) {
		this.list = list;
		return this;
	}
}