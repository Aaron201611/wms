package com.yunkouan.wms.modules.meta.dao;

import java.util.List;

import com.yunkouan.wms.modules.meta.entity.MetaMaterialLog;
import com.yunkouan.wms.modules.meta.vo.MaterialLogVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 辅材日志数据层接口
 */
public interface IMaterialLogDao extends Mapper<MetaMaterialLog> {

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午9:25:20<br/>
	 * @author 王通<br/>
	 */
	public List<MetaMaterialLog> list(MaterialLogVo vo);

}