package com.yunkouan.wms.common.quartz;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.MSMQUtil;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;

@Component("receiveChkResultjob")
public class ReceiveChkResultJob{
	private static Log log = LogFactory.getLog(ReceiveChkResultJob.class);

	@Autowired
	private IMsmqMessageService msmqMessageService;
	@Autowired
	private ISysParamService paramService;

	public static String UTF_FORMAT = "UTF-8";

	public void execute() throws Exception{
		if(log.isDebugEnabled()) log.debug("运行【接收报文回执】定时任务...");
		
		//接收报文
		String RECEIVEQUENAME = paramService.getKey(CacheName.MSMQ_RECEIVE_QUE_NAME);
//		String RECEIVEQUENAME = paramService.getKey(CacheName.MSMQ_OP_RESULT_CHNALNAME);
		if(log.isDebugEnabled()) log.debug("接收Queue名称:["+RECEIVEQUENAME+"]");
		String xmlStr = MSMQUtil.rec(RECEIVEQUENAME,UTF_FORMAT);
		if(log.isDebugEnabled()) log.debug("WMS_receive_XMLstr:["+xmlStr+"]");
		if(StringUtil.isBlank(xmlStr)) return;
		
		String send_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		//保存回执报文
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setContent(xmlStr);
		messageVo.getEntity().setSender(send_id);
		messageVo.getEntity().setReceiver(Constant.SYSTEM_TYPE_WMS);
		messageVo.getEntity().setReceiveTime(new Date());
		msmqMessageService.add(messageVo);
		
		//判断是哪种消息的回执报文
		String resultType = getMessageTypeToXML(xmlStr);
		if(resultType == null) return;
		
		//分类监管出入库，实盘报文回执处理
		if(Constant.MESSAGE_TYPE_HCHX_DATA.equals(resultType)){
			msmqMessageService.receiveMessage(messageVo);
		}
		else {
			msmqMessageService.receiveDeliverGoodsMessage(resultType,messageVo);
		}
		
		
		if(log.isDebugEnabled()) log.debug("【接收报文回执】定时任务完成.");
	}
	
	/**
	 * 根据报文判断消息类型
	 * @param xmlStr
	 * @return
	 */
	public static String getMessageTypeToXML(String xmlStr){
		//分类监管出入库，盘点报文回执
		if(xmlStr.contains("HCHX_DATA")){
			return Constant.MESSAGE_TYPE_HCHX_DATA;
		}
		//校验HBT1001回执报文
		else if(xmlStr.contains("HERTBEAT_MESSAGE")){
			return Constant.MESSAGE_TYPE_HBT1001;
		}
		//申请单回执报文
		else if(xmlStr.contains("TMP_PRE_GATEJOB")){
			return Constant.MESSAGE_TYPE_TMP1001;
		}
		//核放单回执报文
		else if(xmlStr.contains("GAT_PRE_KERNEL")){
			return Constant.MESSAGE_TYPE_GAT1001;
		}
		return null;
	}
	


}
