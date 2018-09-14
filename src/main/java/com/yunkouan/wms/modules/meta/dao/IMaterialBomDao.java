package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaMaterialBom;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 辅材BOM数据层接口
 */
public interface IMaterialBomDao extends Mapper<MetaMaterialBom> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaMaterialBom> selectByExample(Object example);
}