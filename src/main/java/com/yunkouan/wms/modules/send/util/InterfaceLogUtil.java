package com.yunkouan.wms.modules.send.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.entity.InterfaceLog;
import com.yunkouan.wms.modules.sys.service.IInterfaceLogService;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

@Component("interfaceLogUtil")
public class InterfaceLogUtil {
	
	@Autowired
	private IInterfaceLogService interfaceLogService;
	
	public static InterfaceLogUtil getInstance(){
		InterfaceLogUtil logUtil = SpringContextHolder.getBean("interfaceLogUtil");
		return logUtil;
	}

	
	/**
	 * 增加接口日志记录
	 * @param sender
	 * @param sendMessage
	 * @param sendTime
	 * @param receiver
	 * @param receiveMessage
	 * @param receiveTime
	 * @param url
	 * @param result
	 * @param note
	 */
	public InterfaceLogVo addLog(String sender,String sendMessage,Date sendTime,String receiver,String receiveMessage,
			Date receiveTime,String url,String result,String note){
		InterfaceLogVo logVo = new InterfaceLogVo(new InterfaceLog());
			logVo.getEntity().setLogId(IdUtil.getUUID());
			logVo.getEntity().setSender(sender);
			logVo.getEntity().setReceiver(receiver);
			logVo.getEntity().setUrl(url);
			logVo.getEntity().setSendMessage(sendMessage);
			logVo.getEntity().setReceiveMessage(receiveMessage);
			logVo.getEntity().setResult(result);
			logVo.getEntity().setSendTime(sendTime);
			logVo.getEntity().setReceiveTime(receiveTime);
			logVo.getEntity().setOperer(LoginUtil.getLoginUser().getUserId());
			logVo.getEntity().setNote(note);
			logVo.getEntity().setCreatePerson(LoginUtil.getLoginUser().getUserId());
			logVo.getEntity().setUpdatePerson(LoginUtil.getLoginUser().getUserId());
			logVo.getEntity().setOrgId(LoginUtil.getLoginUser().getOrgId());
			logVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
			logVo.getEntity().setCreateTime(new Date());
			logVo.getEntity().setUpdateTime(new Date());
			interfaceLogService.add(logVo);
			return logVo;
	}
	
	public void updateResult(InterfaceLogVo logVo){
		interfaceLogService.update(logVo);
	}
}
