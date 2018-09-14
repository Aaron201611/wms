package com.yunkouan.wms.modules.message.service;

public interface IMessageHandleService {
	
	public void receiveAndHandle(IMessageService service) throws Exception;

}
