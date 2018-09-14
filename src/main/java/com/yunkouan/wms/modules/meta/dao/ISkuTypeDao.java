package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.vo.SkuTypeVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 货品类型数据层接口
 */
public interface ISkuTypeDao extends Mapper<MetaSkuType> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaSkuType> selectByExample(Object example);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月21日 上午11:44:11<br/>
	 * @author 王通<br/>
	 */
	List<MetaSkuType> selectByVo(SkuTypeVo vo);

	/**
	 * 查询最大编号
	 * @return
	 */
	public String getMaxNo();
}