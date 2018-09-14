package com.yunkouan.wms.modules.message.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.quartz.ReceiveChkResultJob;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationDao;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsExamineDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamine;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineService;
import com.yunkouan.wms.modules.application.util.DeliverGoodsXMLMessageUtil;
import com.yunkouan.wms.modules.inv.dao.ICountDao;
import com.yunkouan.wms.modules.inv.entity.InvCount;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.message.dao.IMsmqMessageDao;
import com.yunkouan.wms.modules.message.entity.MsmqMessage;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.rec.dao.IASNDao;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.send.dao.IDeliveryDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.util.TransmitXmlUtil;

@Service
public class MsmqmessageServiceImpl extends BaseService implements IMsmqMessageService{
	private Logger log = LoggerFactory.getLogger(MsmqmessageServiceImpl.class);

	@Autowired
	private ISysParamService paramService;

	@Autowired
	private IMsmqMessageDao msmqMessageDao;
	
	@Autowired
	private IDeliverGoodsApplicationDao applicationDao;
	
	@Autowired
    private ICountDao countDao;
	
	@Autowired
	private IASNDao asnDao;
	
	@Autowired
	private IDeliveryDao deliveryDao;
	
	@Autowired
	private IDeliverGoodsExamineDao examineDao;
	
	@Autowired
	private IDeliverGoodsExamineService examineService;
	
	public static String UTF_FORMAT = "UTF-8";
	
	
	/**
	 * 新增
	 */
	public void add(MsmqMessageVo messageVo){
		String id = IdUtil.getUUID();
		messageVo.getEntity().setId(id);
		msmqMessageDao.insertSelective(messageVo.getEntity());
	}
	
	/**
	 * 查看
	 */
	public MsmqMessageVo view(MsmqMessageVo messageVo){
		
		List<MsmqMessage> list = msmqMessageDao.selectByExample(messageVo.getExample());
		if(list == null || list.isEmpty()) return null;
		
		MsmqMessageVo vo = new MsmqMessageVo();
		vo.setEntity(list.get(0));
		return vo;
	}
	
	/**
	 * 接收回执
	 */
	public void receiveMessage(MsmqMessageVo messageVo){
//		String RECEIVEQUENAME = paramService.getKey(CacheName.MSMQ_RECEIVE_QUE_NAME);
//		//接收报文
//		if(log.isDebugEnabled()) log.debug("接收Queue名称:["+RECEIVEQUENAME+"]");
//		String xmlStr = MSMQUtil.rec(RECEIVEQUENAME,UTF_FORMAT);
//		if(log.isDebugEnabled()) log.debug("WMS_receive_XMLstr:["+xmlStr+"]");
//		if(StringUtil.isBlank(xmlStr)) return;
//		
//		//保存报文
//		MsmqMessageVo messageVo = new MsmqMessageVo();
//		messageVo.getEntity().setContent(xmlStr);
//		messageVo.getEntity().setReceiver(Constant.SYSTEM_TYPE_WMS);
//		messageVo.getEntity().setReceiveTime(new Date());
//		add(messageVo);
		
		//解析报文
		TransmitXmlUtil transmitUtil = new TransmitXmlUtil();
		try {
			transmitUtil.parseResultToObj(messageVo.getEntity().getContent());
			if(transmitUtil.getTransmitMessage() == null) return;
			
			//根据messageid,查询服务类型，单号id
			MsmqMessageVo paramVo = new MsmqMessageVo();
			paramVo.getEntity().setMessageId(transmitUtil.getTransmitMessage().getMESSAGE_ID());
			paramVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
			MsmqMessageVo mVo = view(paramVo);
			
			//更新报文
			messageVo.getEntity().setMessageId(transmitUtil.getTransmitMessage().getMESSAGE_ID());
			messageVo.getEntity().setFunctionType(mVo.getEntity().getFunctionType());
			messageVo.getEntity().setOrderNo(mVo.getEntity().getOrderNo());
			messageVo.getEntity().setOrgId(mVo.getEntity().getOrgId());
			messageVo.getEntity().setWarehouseId(mVo.getEntity().getWarehouseId());
			msmqMessageDao.updateByPrimaryKeySelective(messageVo.getEntity());
			
			
			//根据报文结果，更新状态
			if(Constant.FUNCTION_TYPE_COUNT.equals(mVo.getEntity().getFunctionType())){
				InvCountVO countVo = new InvCountVO(new InvCount());
				countVo.getInvCount().setCountId(mVo.getEntity().getOrderNo());
				countVo.getInvCount().setTransStatus(transmitUtil.getTransmitMessage().getCHK_RESULT());
				countDao.updateByPrimaryKeySelective(countVo.getInvCount());
			}
			else if(Constant.FUNCTION_TYPE_ASN.equals(mVo.getEntity().getFunctionType())){
				RecAsn asn = new RecAsn();
				asn.setAsnId(mVo.getEntity().getOrderNo());
				asn.setTransStatus(transmitUtil.getTransmitMessage().getCHK_RESULT());
				asnDao.updateByPrimaryKeySelective(asn);
			}
			else if(Constant.FUNCTION_TYPE_SEND.equals(mVo.getEntity().getFunctionType())){
				SendDelivery delivery = new SendDelivery();
				delivery.setDeliveryId(mVo.getEntity().getOrderNo());	
				delivery.setTransStatus(transmitUtil.getTransmitMessage().getCHK_RESULT());
				deliveryDao.updateByPrimaryKeySelective(delivery);
			}
			
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
	}
	
	/**
	 * 获取分送集报消息回执
	 * @param messageType
	 * @throws Exception 
	 */
	public void receiveDeliverGoodsMessage(String messageType,MsmqMessageVo messageVo) throws Exception{
		
		//String send_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		//接收对列
//		String RECEIVEQUENAME = null;
//		DeliverGoodsXMLMessageUtil messageUtil = new DeliverGoodsXMLMessageUtil();
		
		//HBT1001回执报文对列
//		if(messageType.equals(Constant.MESSAGE_TYPE_HBT1001)){
//			RECEIVEQUENAME = paramService.getKey(CacheName.MSMQ_OP_RESULT_CHNALNAME);
//		}
//		//申请单审批回执对列
//		else if(messageType.equals(Constant.MESSAGE_TYPE_TMP1001)){
//			RECEIVEQUENAME = paramService.getKey(CacheName.MSMQ_DECLARE_AUDIT_CHNALNAME);
//		}
//		//核放单审批回执对列
//		else if(messageType.equals(Constant.MESSAGE_TYPE_GAT1001)){
//			RECEIVEQUENAME = paramService.getKey(CacheName.MSMQ_KENNEL_AUDIT_CHNALNAME);
//		}
		//接收报文
//		if(log.isDebugEnabled()) log.debug("接收Queue名称:["+RECEIVEQUENAME+"]");
//		String xmlStr = MSMQUtil.rec(RECEIVEQUENAME,UTF_FORMAT);
//		if(log.isDebugEnabled()) log.debug("WMS_receive_XMLstr:["+xmlStr+"]");
//		if(StringUtil.isBlank(xmlStr)) return;
		
		//保存报文
//		MsmqMessageVo messageVo = new MsmqMessageVo();
//		messageVo.getEntity().setContent(xmlStr);
//		messageVo.getEntity().setSender(send_id);
//		messageVo.getEntity().setReceiver(Constant.SYSTEM_TYPE_WMS);
//		messageVo.getEntity().setReceiveTime(new Date());
//		add(messageVo);	
		
		DeliverGoodsXMLMessageUtil messageUtil = new DeliverGoodsXMLMessageUtil();
		//解析报文
		messageUtil.parseMessage(messageType, messageVo.getEntity().getContent());
		
		//查询发送报文信息
		MsmqMessageVo paramVo = new MsmqMessageVo();
		paramVo.getEntity().setMessageId(messageUtil.getMessageHeadMap().get("MESSAGE_ID"));
		paramVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
		MsmqMessageVo mVo = view(paramVo);
		
		//更新回执报文
		messageVo.getEntity().setMessageId(messageUtil.getMessageHeadMap().get("MESSAGE_ID"));
		messageVo.getEntity().setFunctionType(mVo.getEntity().getFunctionType());
		messageVo.getEntity().setOrderNo(mVo.getEntity().getOrderNo());
		messageVo.getEntity().setOrgId(mVo.getEntity().getOrgId());
		messageVo.getEntity().setWarehouseId(mVo.getEntity().getWarehouseId());
		msmqMessageDao.updateByPrimaryKeySelective(messageVo.getEntity());
		
		//根据报文结果，更新状态
		if(messageType.equals(Constant.MESSAGE_TYPE_HBT1001)){
			String result = messageUtil.getMessageHeadMap().get("OP_FLAG");
			if(Constant.FUNCTION_TYPE_APPLY.equals(mVo.getEntity().getFunctionType())){
				DeliverGoodsApplication application = new DeliverGoodsApplication();
				application.setId(mVo.getEntity().getOrderNo());
				application.setAuditStep(result.equals("S")?Constant.APPLICATION_STATUS_CHECK_SUCCESS:Constant.APPLICATION_STATUS_CHECK_FAIL);
				applicationDao.updateByPrimaryKeySelective(application);
			}
			else if(Constant.FUNCTION_TYPE_EXAMINE.equals(mVo.getEntity().getFunctionType())){
				DeliverGoodsExamine examine = new DeliverGoodsExamine();
				examine.setId(mVo.getEntity().getOrderNo());
				examine.setAuditStep(result.equals("S")?Constant.EXAMINE_STATUS_CHECK_SUCCESS:Constant.EXAMINE_STATUS_CHECK_FAIL);
				examineDao.updateByPrimaryKeySelective(examine);
			}
		}
		//申请单审批回执
		else if(messageType.equals(Constant.MESSAGE_TYPE_TMP1001)){
			DeliverGoodsApplication application = new DeliverGoodsApplication();
			application.setId(mVo.getEntity().getOrderNo());
			application.setAuditStep(messageUtil.getStateGateJobMap().get("STEP_ID"));
			applicationDao.updateByPrimaryKeySelective(application);
		}
		//核放单审批回执
		else if(messageType.equals(Constant.MESSAGE_TYPE_GAT1001)){
			String kernel_pass = paramService.getKey(CacheName.KERNEL_AUDIT_STEP_PASS);
			String kernel_back = paramService.getKey(CacheName.KERNEL_AUDIT_STEP_BACK);
			//审批通过则减少审批数量，增加已审批数量
			if(kernel_pass.equals(messageUtil.getStatKernetMap().get("STEP_ID"))){
				examineService.passAudit(mVo.getEntity().getOrderNo());
			}
			//审批不通更新状态，可再次提交
			else if(kernel_back.equals(messageUtil.getStatKernetMap().get("STEP_ID"))){
				examineService.changeBack(mVo.getEntity().getOrderNo());
			}
		}

	}
	
	/**
	 * 测试接收消息
	 */
	public void testReceiveMessage(String xmlStr) throws Exception{
		String send_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		//保存回执报文
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setContent(xmlStr);
		messageVo.getEntity().setSender(send_id);
		messageVo.getEntity().setReceiver(Constant.SYSTEM_TYPE_WMS);
		messageVo.getEntity().setReceiveTime(new Date());
		add(messageVo);
		
		//判断是哪种消息的回执报文
		String resultType = ReceiveChkResultJob.getMessageTypeToXML(xmlStr);
		if(resultType == null) return;
		
		//分类监管出入库，实盘报文回执处理
		if(Constant.MESSAGE_TYPE_HCHX_DATA.equals(resultType)){
			receiveMessage(messageVo);
		}
		else {
			receiveDeliverGoodsMessage(resultType,messageVo);
		}
	}
}
