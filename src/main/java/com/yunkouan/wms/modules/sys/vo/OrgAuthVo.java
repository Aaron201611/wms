package com.yunkouan.wms.modules.sys.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.SysOrgAuth;
import com.yunkouan.wms.modules.sys.entity.SysRoleAuth;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 企业授权数据传输类
 * @author tphe06 2017年2月15日
 */
public final class OrgAuthVo extends BaseVO {
	private static final long serialVersionUID = 4730406834988416012L;

	private SysOrgAuth entity;
	/**@Fields 排序字段 */
	private String orderBy = "auth_id2 desc";

	@Override
	public Example getExample() {
		Example example = new Example(SysRoleAuth.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		c.andEqualTo("orgId", entity.getOrgId());
		return example;
	}

	public SysOrgAuth getEntity() {
		return entity;
	}
	public OrgAuthVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}

	public OrgAuthVo setEntity(SysOrgAuth entity) {
		this.entity = entity;
		return this;
	}
}