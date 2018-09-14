package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaMerchant;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 客商数据层接口
 */
public interface IMerchantDao extends Mapper<MetaMerchant> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaMerchant> selectByExample(Object example);

	public List<String> list(MetaMerchant entity);
	/**
	 * 查询最大编号
	 * @return
	 */
	public String getMaxNo();
}