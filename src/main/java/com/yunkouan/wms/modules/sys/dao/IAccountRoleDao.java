package com.yunkouan.wms.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.sys.entity.SysAccountRole;
import com.yunkouan.wms.modules.sys.entity.SysRole;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

public interface IAccountRoleDao extends Mapper<SysAccountRole> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<SysAccountRole> selectByExample(Object example);

	/**
	 * 帐号授权角色列表数据查询
	 */
	public List<SysRole> list(SysAccountRole entity);

	/**
	 * 删除帐号授权角色
	 */
	public int delete(SysAccountRole entity);
}