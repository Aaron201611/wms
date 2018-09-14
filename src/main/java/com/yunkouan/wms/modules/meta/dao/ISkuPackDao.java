package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaPack;
import com.yunkouan.wms.modules.meta.entity.MetaSkuPack;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 货品包装数据层接口
 */
public interface ISkuPackDao extends Mapper<MetaSkuPack> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaSkuPack> selectByExample(Object example);

	/**
	 * 查询货品包装列表数据
	 */
	public List<MetaPack> list(MetaSkuPack entity);
	/**
	 * 批量删除货品包装信息
	 */
	public int delete(MetaSkuPack entity);
}