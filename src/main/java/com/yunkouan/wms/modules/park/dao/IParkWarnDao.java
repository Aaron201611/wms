package com.yunkouan.wms.modules.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunkouan.wms.modules.park.entity.ParkWarn;

import tk.mybatis.mapper.common.Mapper;

/**
 * 仓库出租告警数据层接口
 */
public interface IParkWarnDao extends Mapper<ParkWarn>{

	public List<ParkWarn> qryList(@Param("rentList")List<String> rentList);
}