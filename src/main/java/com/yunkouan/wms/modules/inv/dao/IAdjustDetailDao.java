package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import com.yunkouan.wms.modules.inv.entity.InvAdjustDetail;

/**
 * 盈亏调整明细数据层接口
 */
public interface IAdjustDetailDao  extends Mapper<InvAdjustDetail> {

	/**
	 * 批量插入方法
	 * @param listAdjustDetail
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午5:27:40<br/>
	 * @author 王通<br/>
	 */
	void add(List<InvAdjustDetail> listAdjustDetail);


}