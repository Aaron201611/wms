package com.yunkouan.wms.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.sys.entity.SysRole;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 角色数据层接口
 */
public interface IRoleDao extends Mapper<SysRole> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<SysRole> selectByExample(Object example);

}