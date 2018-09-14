package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;

/**
 * 盈亏调整数据层接口
 */
public interface IAdjustDao  extends Mapper<InvAdjust>  {

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午2:23:03<br/>
	 * @author 王通<br/>
	 */
	List<InvAdjust> listByPage(InvAdjustVO vo);
	
	public List<String>getTask(String orgId);

}