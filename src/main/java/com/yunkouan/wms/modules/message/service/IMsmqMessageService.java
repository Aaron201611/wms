package com.yunkouan.wms.modules.message.service;

import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;

/**
 * msmq消息服务接口
 */
public interface IMsmqMessageService {

	public void add(MsmqMessageVo messageVo);
	
	public MsmqMessageVo view(MsmqMessageVo messageVo);
	
	public void receiveMessage(MsmqMessageVo messageVo);
	
	/**
	 * 获取分送集报消息回执
	 * @param messageType
	 */
	public void receiveDeliverGoodsMessage(String messageType,MsmqMessageVo messageVo) throws Exception;
	
	public void testReceiveMessage(String xmlStr) throws Exception;

}