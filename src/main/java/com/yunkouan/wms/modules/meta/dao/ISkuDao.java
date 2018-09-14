package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 货品数据层接口
 */
public interface ISkuDao extends Mapper<MetaSku> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<MetaSku> selectByExample(Object example);

	/**
	 * 查询最大编号
	 * @return
	 */
	public String getMaxNo();

	/**
    * @Description: 分页查询安全库存信息（限设置了最大安全库存或者最小安全库存记录）
    * @param vo：查询参数，可选：
    * 货主名称：merchant.merchantName
    * 货品代码：entity.skuNo
    * 当前页：currentPage
    * 每页显示数量：pageSize
    * @throws DaoException
    * @throws ServiceException
    * @return SkuVo
    * @throws
    */
    public List<MetaSku> list4stock(SkuVo vo) throws DaoException, ServiceException;

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月15日 下午6:45:06<br/>
	 * @author 王通<br/>
	 */
	public List<MetaSku> list(SkuVo vo);
	
	/**
	 * add by zwb
	 * @param vo
	 * @return
	 */
	public List<MetaSku> findByOwnerSkuno(SkuVo vo);
}