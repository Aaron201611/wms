package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaPack;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 包装数据层接口
 */
public interface IPackDao extends Mapper<MetaPack> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaPack> selectByExample(Object example);

	/**
	* @Description: 查询最小货品包装编号
	* @param entity
	* @return
	*/
	public String selectMaxNo(MetaPack entity);
}