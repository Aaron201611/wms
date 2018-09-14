package com.yunkouan.wms.common.dao;

import tk.mybatis.mapper.common.Mapper;
import com.yunkouan.wms.common.vo.CommonVo;

/**
 * 单据编号生成接口（面向关系型数据库）
 * <br/><br/>
 * @Description 
 * @version 2017年2月17日 下午2:28:49<br/>
 * @author 王通<br/>
 */
@Deprecated
public interface ICommonDao extends Mapper<CommonVo> {
	/**
	 * 获取当前序列（同时数据库序列自增）
	 * @param vo 字段及机构参数 
	 * @return
	 * @Description 
	 * @version 2017年2月17日 下午2:35:36<br/>
	 * @author 王通<br/>
	 */
	public Integer getSeq(CommonVo vo);
	/**
	 * 新增一条序列
	 * @param vo
	 * @Description 
	 * @version 2017年2月17日 下午3:59:04<br/>
	 * @author 王通<br/>
	 */
	public void addSeq(CommonVo vo);


	/**
	 * 获取当前序列
	 * @param vo 字段及机构参数 
	 * @return
	 * @Description 
	 * @version 2017年8月9日 下午2:35:36<br/>
	 * @author tphe06<br/>
	 */
	public Integer getSequence(CommonVo vo);
	/**
	 * 更新当前序列
	 * @param vo 字段及机构参数 
	 * @return
	 * @Description 
	 * @version 2017年8月9日 下午2:35:36<br/>
	 * @author tphe06<br/>
	 */
	public void updateSequence(CommonVo vo);
	/**
	 * 新增一条序列
	 * @param vo
	 * @Description 
	 * @version 2017年2月17日 下午3:59:04<br/>
	 * @author tphe06<br/>
	 */
	public void addSequence(CommonVo vo);
}