package com.yunkouan.wms.modules.send.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.HttpUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.service.AbstractExpressService;
import com.yunkouan.wms.modules.send.util.Base64Utils;
import com.yunkouan.wms.modules.send.util.InterfaceLogUtil;
import com.yunkouan.wms.modules.send.util.JaxbUtil;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.ems.EmsLogisticsNo;
import com.yunkouan.wms.modules.send.vo.ems.EmsMsg;
import com.yunkouan.wms.modules.send.vo.ems.EmsResponse;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

/** 
* @Description: EMS快递接口
* @author tphe06
* @date 2018年4月20日 上午9:05:48  
*/
@Service
@Scope("prototype")//此注解不得删除!!
@Transactional(readOnly=false, rollbackFor=Exception.class)
public class EMSServiceImpl extends AbstractExpressService {
	private static Log log = LogFactory.getLog(EMSServiceImpl.class);

	/**
	 * 请求EMS
	 * @param sendXml
	 * @return
	 */
	private static String sendEMSPost(String xmlParams) {
		String response = "";
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String url = paramService.getKey(CacheName.EMS_INTERFACE_APPLY);
		try {
			String xml = new StringBuilder("content=").append(Base64Utils.getEncodeAndBase64(xmlParams)).toString();
			//记录接口日志
			InterfaceLogVo logVo = InterfaceLogUtil.getInstance().addLog(Constant.SYS_WMS, 
																			xml, 
																			new Date(), 
																			Constant.SYS_EMS, 
																			null, 
																			null, 
																			url, 
																			null, 
																			null);
			if(log.isDebugEnabled()) log.debug(new StringBuilder("sendEMS:").append(xml));
			response = HttpUtil.postXmlBody(url, xml);
			
			//更新接口日志
			logVo.getEntity().setReceiveTime(new Date());
			logVo.getEntity().setReceiveMessage(response);
			logVo.getEntity().setResult(response);
			InterfaceLogUtil.getInstance().updateResult(logVo);
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
		return response;
	}
	/**获取向EMS推送的结果*/
	private static EmsMsg parseEMSResult(String emsResult) {
		return JaxbUtil.converyToJavaBean(emsResult, EmsMsg.class);
	}

	/**
	 * 向EMS请求运单号并封装响应结果
	 * @param busType
	 * @param sysAccount
	 * @param passWord
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws Exception
	 */
	private static String getBigAccountData(String busType, String sysAccount, String passWord,Integer billNoAmount,String appKey,String tempurl) throws Exception {
		//String tempurl = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getBillNumBySys&xml=";
		StringBuffer msgXml = new StringBuffer(""); 
		msgXml.append("<?xml version='1.0' encoding='utf-8'?>");
		msgXml.append("<XMLInfo>");
		msgXml.append("<sysAccount>"+sysAccount+"</sysAccount>"); //大客户号
		msgXml.append("<passWord>"+passWord+"</passWord>"); //密码
		msgXml.append("<billNoAmount>"+billNoAmount+"</billNoAmount>"); //单号量 1-100之间数字
		msgXml.append("<appKey>"+appKey+"</appKey>"); //接授权码 uat:T5675AA9D19919D11 pro:T5675AA9D19919D11
		msgXml.append("<businessType>"+busType+"</businessType>"); //业务类型
		msgXml.append("</XMLInfo>");
		String doUrl = new StringBuffer(tempurl).append(Base64Utils.getEncodeAndBase64(msgXml.toString())).toString();

		//记录接口日志
		InterfaceLogVo logVo = InterfaceLogUtil.getInstance().addLog(Constant.SYS_WMS, 
																		msgXml.toString(), 
																		new Date(), 
																		Constant.SYS_EMS, 
																		null, 
																		null, 
																		doUrl, 
																		null, 
																		null);
		
		String resultSrt = Base64Utils.getDecodeAndBase64(HttpUtil.get(doUrl));
		
		//更新接口日志
		logVo.getEntity().setReceiveTime(new Date());
		logVo.getEntity().setReceiveMessage(resultSrt);
		logVo.getEntity().setResult(resultSrt);
		InterfaceLogUtil.getInstance().updateResult(logVo);
		
		StringBuffer srtBuffer = new StringBuffer("<?xml version='1.0' encoding='utf-8'?>");
		resultSrt = resultSrt.substring(resultSrt.indexOf("<response")).replaceAll("\\?", ">");
		srtBuffer.append(resultSrt);
		if(log.isDebugEnabled()) log.debug(resultSrt);
		return  srtBuffer.toString();
	}
	/**
	 * 获取解析出运单号
	 * @return
	 * @throws Exception
	 */
	private static String getLogisticsNo() throws Exception{
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String url=paramService.getKey(CacheName.EMS_GET_BILLNO_URL);
		String busType=paramService.getKey(CacheName.EMS_BUSINESSTYPE);
		String sysAccount=paramService.getKey(CacheName.EMS_SYSACCOUNT);
		String passWord=paramService.getKey(CacheName.EMS_PASSWORD);
		Integer billNoAmount=Integer.parseInt(paramService.getKey(CacheName.EMS_BILLNOAMOUNT));
		String appKey=paramService.getKey(CacheName.EMS_APPKEY);

		String strxml = getBigAccountData(busType, sysAccount, passWord, billNoAmount, appKey,url);
		EmsLogisticsNo emsLogisticsNo = JaxbUtil.converyToJavaBean(strxml, EmsLogisticsNo.class);
		if("E029".equals(emsLogisticsNo.getErrorCode())){
			throw new BizException("available_billno_not_enough");
		}else if(!"E000".equals(emsLogisticsNo.getErrorCode())){
			throw new BizException("getLogisticsNoException");
		}
		return emsLogisticsNo.getAssignIds().getAssignId().get(0).getBillno();
	}

	/** 
	* @Title: get 
	* @Description: 从物流公司获取N个运单号
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:24:42
	* @param sum
	* @return
	* @throws Exception
	*/
	@Override
	protected List<String> get(int sum) throws Exception {
		List<String> list = new ArrayList<String>();
		for(int i=0; i<sum; ++i) {
			list.add(getLogisticsNo());
		}
		return list;
	}

	/** 
	* @Title: isBathPush 
	* @Description: 是否支持批量推送订单
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:24:28
	* @return
	*/
	@Override
	protected boolean isBathPush() {
		return true;
	}

	/** 
	* @Title: send 
	* @Description: 单个推送订单接口，如果推送失败返回null，如果支持批量接口，本接口可以不用实现
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:24:23
	* @param vo
	* @return
	* @throws IOException
	*/
	@Override
	protected SendDeliveryVo send(SendDeliveryVo vo) throws IOException {
		return null;
	}

	/** 
	* @Title: send 
	* @Description: 批量推送订单接口，返回推送成功列表，如果不支持请在isBathPush方法返回false
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:23:42
	* @param vos
	* @return
	*/
	@Override
	protected List<SendDeliveryVo> send(List<SendDeliveryVo> vos) {
		// 1.过滤状态不符合条件的记录（已上提到所有物流公司通用）
		List<SendDeliveryVo> list = vos;
		// 2.封装EMS报文
		String xmlParam = packEms(1, list);
		if(log.isInfoEnabled()) log.info(new StringBuffer("EMS xmlParam:").append(xmlParam));
		// 3.发送给EMS并返回结果
		String xmlResult = sendEMSPost(xmlParam);
		if(log.isInfoEnabled()) log.info(new StringBuffer("EMS xmlResult:").append(xmlResult));
		if(StringUtil.isBlank(xmlResult)) throw new BizException("net_exception");
		// 4.解析EMS返回的结果
		EmsMsg emsMsg = parseEMSResult(xmlResult);
		List<EmsResponse> responses = emsMsg.getBody().getResponses().getList();
		if(responses==null || responses.size()==0) throw new BizException("ems_failure");
		// 5.过滤EMS入库失败的记录
		list = fillFaile(list, responses);
		// 6.更新发送状态（已上提到所有物流公司通用）
//		updateStatus(list);
		// 7.返回推送EMS成功记录
		return list;
	}

	/** 
	* @Title: fillFaile 
	* @Description: 过滤EMS入库失败的记录
	* @auth tphe06
	* @time 2018 2018年4月19日 下午5:28:09
	* @param deliveries
	* @param responses
	* @return
	* @throws ClientProtocolException
	* @throws IOException
	* List<SendDeliveryVo>
	*/
	private static List<SendDeliveryVo> fillFaile(List<SendDeliveryVo> deliveries,List<EmsResponse>responses) {
		List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
		for(SendDeliveryVo vo : deliveries){
			for(EmsResponse r : responses){
				//ems入库失败
				if(0 == r.getStatus()) continue;
				//ems返回的对应的运单号
				String billNo = r.getNo();
				//不是本条运单推送结果
				String expressBillNo = vo.getSendDelivery().getExpressBillNo();
				if(!expressBillNo.equals(billNo)) continue;
				list.add(vo);
			}
		}
		return list;
	}
	/** 
	* @Title: packEms 
	* @Description: 封装推送EMS的报文
	* @auth tphe06
	* @time 2018 2018年4月19日 下午5:29:29
	* @param appType 给ems推送的类型1.新增 2.修改 3.删除
	* @param vos
	* @return
	* String
	*/
	private static String packEms(int appType, List<SendDeliveryVo> vos) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("<Manifest>");

		//head
		stringBuffer.append("<Head>");
		String messageID=IdUtil.getUUID();
		stringBuffer.append("<MessageID>"+messageID+"</MessageID>");
		stringBuffer.append("<FunctionCode>0</FunctionCode>");
		stringBuffer.append("<MessageType>511</MessageType>");
		//发送者标识
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String EMS_INTERFACE_SENDER = paramService.getKey(CacheName.EMS_INTERFACE_SENDER);
		stringBuffer.append("<SenderID>"+EMS_INTERFACE_SENDER+"</SenderID>");
		//接收者标识
		String EMS_INTERFACE_RECEIVER = paramService.getKey(CacheName.EMS_INTERFACE_RECEIVER);
		stringBuffer.append("<ReceiverID>"+EMS_INTERFACE_RECEIVER+"</ReceiverID>");
		String date = DateUtil.formatDate(new Date(), "yyy-MM-dd HH:mm:ss");
		stringBuffer.append("<SendTime>"+date+"</SendTime>");
		stringBuffer.append("<Version>1.0</Version>");
		stringBuffer.append("</Head>");

		//body
		stringBuffer.append("<Declaration>");
		stringBuffer.append("<Freights>");
		for(SendDeliveryVo vo : vos){
			stringBuffer.append("<Freight>");
			//1-新增 2-变更 3-删除。默认为1。
			stringBuffer.append("<appType>"+appType+"</appType>");
			String date2=DateUtil.formatDate(new Date(), "yyyMMddHHmmss");
			stringBuffer.append("<appTime>"+date2+"</appTime>");
			//1：暂存；2：申报
			stringBuffer.append("<appStatus>2</appStatus>");
			//物流企业代码
			String EMS_LOGISTICS_CODE = paramService.getKey(CacheName.EMS_LOGISTICS_CODE);
			stringBuffer.append("<logisticsCode>"+EMS_LOGISTICS_CODE+"</logisticsCode>");
			//物流企业名称
			String EMS_LOGISTICS_NAME = paramService.getKey(CacheName.EMS_LOGISTICS_NAME);
			stringBuffer.append("<logisticsName>"+EMS_LOGISTICS_NAME+"</logisticsName>");
			//获取发货单
			SendDelivery sendDelivery = vo.getSendDelivery();
			//将符合条件的发货单加入集合供后边用
//			deliveries.add(sendDelivery);
			//获取运单号
			String expressBillNo=sendDelivery.getExpressBillNo();

			stringBuffer.append("<logisticsNo>"+expressBillNo+"</logisticsNo>");
			//获取提运单号,因为我们是保税备货模式，该处填  -
			stringBuffer.append("<billNo>-</billNo>");
			//获取运费
			Double freightCharge=sendDelivery.getFreightCharge();
			if(freightCharge==null){
				freightCharge=0.0;	
			}
			stringBuffer.append("<freight>"+freightCharge+"</freight>");
			//获取保价费
			Double insuranceCharge=sendDelivery.getInsuranceCharge();
			if(insuranceCharge==null){
				insuranceCharge=0.0;
			}
			stringBuffer.append("<insuredFee>"+insuranceCharge+"</insuredFee>");
			//币制
			stringBuffer.append("<currency>142</currency>");
			//毛重
			Double grossWeight=sendDelivery.getGrossWeight();
			if(grossWeight==null){
				grossWeight=0.0;
			}
			stringBuffer.append("<weight>"+grossWeight+"</weight>");
			//包裹数限定为1
			stringBuffer.append("<packNo>1</packNo>");
			//商品详情
			String goodsInfo="";
			//			List<DeliveryDetailVo> list=vo.getDeliveryDetailVoList();
			//			if(list!=null&&list.size()>1){
			//				goodsInfo=list.get(0).getSkuName()+"等";
			//			}else{
			//				goodsInfo=list.get(0).getSkuName();
			//			}
			stringBuffer.append("<goodsInfo>"+goodsInfo+"</goodsInfo>");
			//收货人
			String contactPerson=sendDelivery.getContactPerson();
			stringBuffer.append("<consignee>"+contactPerson+"</consignee>");
			//收货地址
			String contactAddress=sendDelivery.getContactAddress();
			stringBuffer.append("<consigneeAddress>"+contactAddress+"</consigneeAddress>");
			//联系方式
			String contactPhone=sendDelivery.getContactPhone();
			stringBuffer.append("<consigneeTelephone>"+contactPhone+"</consigneeTelephone>");
			//备注note
			String note=sendDelivery.getNote();
			stringBuffer.append("<note>"+note+"</note>");
			//订单号srcNo
			String srcNo=sendDelivery.getSrcNo();
			stringBuffer.append("<orderNo>"+srcNo+"</orderNo>");
			//电商平台代码
			String EMS_EBP_CODE = paramService.getKey(CacheName.EMS_EBP_CODE);
			stringBuffer.append("<ebpCode>"+EMS_EBP_CODE+"</ebpCode>");
			stringBuffer.append("</Freight>");
		}
		stringBuffer.append("</Freights>");
		stringBuffer.append("</Declaration>");
		stringBuffer.append("</Manifest>");
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		
		try {
			String response = HttpUtil.postXmlBody("http://211.156.193.152:8080/kjjktbApi_Server/call.api", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}