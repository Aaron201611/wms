package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import com.yunkouan.wms.modules.inv.entity.InvInventoryStatistics;
import com.yunkouan.wms.modules.inv.vo.InvInventoryStatisticsVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 库存数据层接口
 */
public interface IInventoryStatisticsDao  extends Mapper<InvInventoryStatistics> {

	List<InvInventoryStatistics> list(InvInventoryStatisticsVO statisticsVo);
	//根据页面条件统计当前月的记录
	List<InvInventoryStatistics> countThisMonth(InvInventoryStatisticsVO statisticsVo);
	//每月定时任务更新上个月的记录
	void updateLastMonth();
	void insertThisMonth();
	//根据实体条件统计当前月的记录
	InvInventoryStatistics countCurrentMonth(InvInventoryStatistics entity);

}