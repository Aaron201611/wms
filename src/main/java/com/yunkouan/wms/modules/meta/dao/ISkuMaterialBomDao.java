package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaSkuMaterialBom;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 货品数据层接口
 */
public interface ISkuMaterialBomDao extends Mapper<MetaSkuMaterialBom> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaSkuMaterialBom> selectByExample(Object example);
}