package com.yunkouan.wms.modules.message.service;

import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;

public interface IMessageService {
	
	public void handleData(String xmlStr);
	
	public void save(MsmqMessageVo messageVo);
	
	public MsmqMessageVo view(MsmqMessageVo messageVo);
}
