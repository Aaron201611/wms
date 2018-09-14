package com.yunkouan.wms.modules.send.dao;

import org.apache.ibatis.annotations.Param;

import com.yunkouan.wms.modules.send.entity.SendPickLocation;

import tk.mybatis.mapper.common.Mapper;


/**
 * 计划拣货单数据层接口
 */
public interface IPickLocationDao extends Mapper<SendPickLocation>{
	
	/**
	 * 根据拣货明细id删除库位计划明细
	 * @param pickDetailId
	 * @return
	 * @version 2017年2月20日 下午4:13:31<br/>
	 * @author Aaron He<br/>
	 */
	public int delByPickDetailId(@Param("pickDetailId")String pickDetailId,@Param("pickType")int pickType);
   

}