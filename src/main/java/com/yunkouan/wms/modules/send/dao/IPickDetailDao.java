package com.yunkouan.wms.modules.send.dao;

import java.util.List;

import com.yunkouan.wms.modules.send.entity.SendPickDetail;

import tk.mybatis.mapper.common.Mapper;


/**
 * 拣货单明细数据层接口
 */
public interface IPickDetailDao extends Mapper<SendPickDetail>{


	/**
	 * 查询拣货明细列表
	 * @param detail
	 * @return
	 * @version 2017年2月17日 上午10:59:22<br/>
	 * @author Aaron He<br/>
	 */
    public List<SendPickDetail> qryList(SendPickDetail detail);

}