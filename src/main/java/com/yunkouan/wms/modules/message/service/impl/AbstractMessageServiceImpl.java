package com.yunkouan.wms.modules.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.message.dao.IMsmqMessageDao;
import com.yunkouan.wms.modules.message.entity.MsmqMessage;
import com.yunkouan.wms.modules.message.service.IMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;

@Service
public abstract class AbstractMessageServiceImpl implements IMessageService{
	
	@Autowired
	private IMsmqMessageDao msmqMessageDao;

	
	public void save(MsmqMessageVo messageVo){
		//
		MsmqMessage message =  getEntityByMessageId(messageVo.getEntity().getMessageId());
		if(message == null){
			add(messageVo);
		}else{
			update(messageVo);
		}
		
	}
	
	public void add(MsmqMessageVo messageVo){
		String id = IdUtil.getUUID();
		messageVo.getEntity().setId(id);
		msmqMessageDao.insertSelective(messageVo.getEntity());
	}
	
	public void update(MsmqMessageVo messageVo){
		msmqMessageDao.updateByPrimaryKeySelective(messageVo.getEntity());
	}
	
	public MsmqMessageVo view(MsmqMessageVo messageVo){
		List<MsmqMessage> list = msmqMessageDao.selectByExample(messageVo.getExample());
		if(list == null || list.isEmpty()) return null;
		
		MsmqMessageVo vo = new MsmqMessageVo();
		vo.setEntity(list.get(0));
		return vo;
	}
	
	public MsmqMessage getEntityByMessageId(String messageId){
		MsmqMessageVo vo = new MsmqMessageVo();
		vo.getEntity().setMessageId(messageId);
		List<MsmqMessage> list = msmqMessageDao.selectByExample(vo.getExample());
		if(list == null || list.isEmpty()) return null;
		return list.get(0);
	}
	
}
