package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import com.yunkouan.wms.modules.inv.entity.InvCount;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;

/**
 * 盘点数据层接口
 */
public interface ICountDao  extends Mapper<InvCount> {

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月31日 上午9:05:38<br/>
	 * @author 王通<br/>
	 */
	List<InvCount> list(InvCountVO vo);

	public List<String>getTask(String orgId);
}