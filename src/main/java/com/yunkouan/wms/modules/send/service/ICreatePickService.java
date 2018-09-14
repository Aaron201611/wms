package com.yunkouan.wms.modules.send.service;

import com.yunkouan.wms.modules.send.vo.SendPickVo;

public interface ICreatePickService {

	/**
	 * 创建拣货单
	 * @param id
	 * @return
	 * @version 2017年3月3日 下午4:03:50<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo createPick(String id,String operator)throws Exception;
	
	
	
}
