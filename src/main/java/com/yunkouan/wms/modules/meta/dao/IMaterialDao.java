package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.meta.entity.MetaMaterial;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 辅材数据层接口
 */
public interface IMaterialDao extends Mapper<MetaMaterial> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaMaterial> selectByExample(Object example);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月15日 下午6:45:06<br/>
	 * @author 王通<br/>
	 */
	public List<MetaMaterial> list(MaterialVo vo);
}