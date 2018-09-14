package com.yunkouan.wms.common.dao;

import com.yunkouan.wms.common.entity.Sequence;
import tk.mybatis.mapper.common.Mapper;

/**
 * @function 单据编号生成接口（面向对象）
 * @author tphe06
 */
public interface INoDao extends Mapper<Sequence> {
	/** 
	* @Title: increaseSequence 
	* @Description: 数据库值加1，大于0表示成功，等于0表示失败（利用数据库行锁，支持并发）
	* @auth tphe06
	* @time 2018 2018年7月25日 下午4:47:50
	* @param id
	* @param currentValue
	* @return
	* int
	*/
	public int increaseSequence(Sequence seq);
}