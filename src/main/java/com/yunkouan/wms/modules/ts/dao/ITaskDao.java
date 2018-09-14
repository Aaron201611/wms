package com.yunkouan.wms.modules.ts.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.ts.entity.TsTask;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 任务单数据层接口
 */
public interface ITaskDao extends Mapper<TsTask>{
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<TsTask> selectByExample(Object example);

	/**
	 * 查询任务类型任务数
	 * @param statusList
	 * @param opPerson
	 * @param orgId
	 * @param warehouseId
	 * @param lastItemTime
	 * @return
	 */
	public List<Map<String,Object>> countGroupByTaskType(@Param("statusList")List<Integer> statusList,
			@Param("opPerson")String opPerson,
			@Param("orgId")String orgId,
			@Param("warehouseId")String warehouseId,
			@Param("lastItemTime")Date lastItemTime);
	
	/**
	 * 查询所有人任务数
	 * @param statusList
	 * @param orgId
	 * @param warehouseId
	 * @param taskType
	 * @return
	 */
	public List<Map<String,String>> countGroupByOpperson(@Param("statusList")List<Integer> statusList,
			@Param("orgId")String orgId,
			@Param("warehouseId")String warehouseId,
			@Param("taskType")Integer taskType);
}