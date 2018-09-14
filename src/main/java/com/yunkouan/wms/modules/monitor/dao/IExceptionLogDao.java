package com.yunkouan.wms.modules.monitor.dao;

import java.util.List;

import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;

import tk.mybatis.mapper.common.Mapper;

/**
 * 异常数据层接口
 * <br/><br/>
 * @Description 
 * @version 2017年2月17日 下午5:57:56<br/>
 * @author 王通<br/>
 */
public interface IExceptionLogDao  extends Mapper<ExceptionLog>{
	public List<String>getTask(String orgId);
}