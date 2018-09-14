package com.yunkouan.wms.modules.send.dao;

import java.util.List;

import com.yunkouan.wms.modules.send.entity.SendWave;

import tk.mybatis.mapper.common.Mapper;

/**
 *波次单dao
 *@Description
 *@author Aaron
 *@date 2017年3月1日 下午1:23:47
 *version v1.0
 */
public interface IWaveDao extends Mapper<SendWave>{

	/**
	 * 查询波次单
	 * @param sendWave
	 * @return
	 * @version 2017年3月1日 下午6:57:39<br/>
	 * @author Aaron He<br/>
	 */
	List<SendWave> qryList(SendWave sendWave);
	
	public List<String>getTask(String orgId);
}
