package com.yunkouan.wms.modules.send.service;

import java.util.List;

import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;

public interface IPickLocationService {
	
	/**
	 * 查询拣货库位列表
	 * @param pickDetailId
	 * @param pickType
	 * @return
	 * @version 2017年2月17日 下午3:46:36<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickLocationVo> qryPickLocations(SendPickLocationVo param) throws Exception;
	/**
	 * 删除明细
	 * @param locationVo
	 * @throws Exception
	 */
	public void del(SendPickLocationVo locationVo)throws Exception;

}
