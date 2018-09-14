package com.yunkouan.wms.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.sys.entity.SysRoleAuth;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 角色权限数据层接口
 */
public interface IRoleAuthDao extends Mapper<SysRoleAuth> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<SysRoleAuth> selectByExample(Object example);

	/**
	 * 角色授权列表数据查询【针对某个企业】
	 */
	public List<SysRoleAuth> query(SysRoleAuth entity);
	/**
	 * 角色授权列表数据查询【针对某个角色】
	 */
	public List<SysRoleAuth> list(SysRoleAuth entity);
}