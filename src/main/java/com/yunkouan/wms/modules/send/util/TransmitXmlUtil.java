package com.yunkouan.wms.modules.send.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.util.XmlUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.intf.vo.MSMQ;
import com.yunkouan.wms.modules.send.entity.MessageDetail;
import com.yunkouan.wms.modules.send.entity.TransmitMessage;


public class TransmitXmlUtil {
	
	private MSMQ data;
	
	private Document document;
	
	private Element root;
	
	private TransmitMessage transmitMessage;
	
	private List<MessageDetail> messageList;
	
	
	public TransmitXmlUtil(){
		
	}
	
	public TransmitXmlUtil(MSMQ data){
		this.data = data;
	}

	/**
	 * 期末库存报文
	 * @param stockVoList
	 * @return
	 */
	public String formXml(){
		createRoot();
		createTransmitNote();
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String messageType_count = paramService.getKey(CacheName.MSMQ_MESSAGE_TYPE_COUNT);
		String messageType_instock= paramService.getKey(CacheName.MSMQ_MESSAGE_TYPE_INOUTSTOCK);
		//期末库存报文
		if(messageType_count.equals(data.getHead().getMESSAGE_TYPE())){
			createStoreAmountNodes();
		}
		//出入库报文
		else if(messageType_instock.equals(data.getHead().getMESSAGE_TYPE())){
			createStoreTransNodes();
		}
		String xml = XmlUtil.toXML(document);
		return xml;
	}
	
	/**
	 * 创建报文根节点
	 */
	public void createRoot(){
		document = XmlUtil.createDocument();
		root = XmlUtil.createElement(document, "HCHX_DATA");
	}
	
	/**
	 * 创建报文头
	 */
	public void createTransmitNote(){
		Element c = XmlUtil.createElement(root,"TRANSMIT");
		XmlUtil.createElement(c, "MESSAGE_ID", data.getHead().getMESSAGE_ID());
		XmlUtil.createElement(c, "MESSAGE_TYPE", data.getHead().getMESSAGE_TYPE());
		XmlUtil.createElement(c, "EMS_NO", data.getHead().getEMS_NO());
		XmlUtil.createElement(c, "ORDER_NO", data.getHead().getORDER_NO());
		XmlUtil.createElement(c, "FUNCTION_CODE", data.getHead().getFUNCTION_CODE());
		XmlUtil.createElement(c, "CHK_RESULT", data.getHead().getCHK_RESULT());
		XmlUtil.createElement(c, "MESSAGE_DATE", data.getHead().getMESSAGE_DATE());
		XmlUtil.createElement(c, "SENDER_ID", data.getHead().getSENDER_ID());
		XmlUtil.createElement(c, "SEND_ADDRESS", data.getHead().getSEND_ADDRESS());
		XmlUtil.createElement(c, "RECEIVER_ID", data.getHead().getRECEIVER_ID());
		XmlUtil.createElement(c, "RECEIVER_ADDRESS", data.getHead().getRECEIVER_ADDRESS());
		XmlUtil.createElement(c, "MESSAGE_SIGN", data.getHead().getMESSAGE_SIGN());
		XmlUtil.createElement(c, "SEND_TYPE", data.getHead().getSEND_TYPE());
	}
	
	/**
	 * 创建期末库存报文体
	 */
	public void createStoreAmountNodes(){
		for(Map<String, String> map:data.getBodyMaps()){
			Element c = XmlUtil.createElement(root,"STORE_AMOUNT");
			XmlUtil.createElement(c, "MESSAGE_ID", map.get("MESSAGE_ID"));
			XmlUtil.createElement(c, "EMS_NO", map.get("EMS_NO"));
			XmlUtil.createElement(c, "COP_G_NO", map.get("COP_G_NO"));
			XmlUtil.createElement(c, "G_NO", map.get("G_NO"));
			XmlUtil.createElement(c, "QTY", map.get("QTY"));
			XmlUtil.createElement(c, "UNIT", map.get("UNIT"));
			XmlUtil.createElement(c, "STOCK_DATE", map.get("STOCK_DATE"));
			XmlUtil.createElement(c, "GOODS_NATURE", map.get("GOODS_NATURE"));
			XmlUtil.createElement(c, "GOODS_BELONG", map.get("GOODS_BELONG"));
			XmlUtil.createElement(c, "GOODS_FORM", map.get("GOODS_FORM"));
			XmlUtil.createElement(c, "BOM_VERSION", map.get("BOM_VERSION"));
			XmlUtil.createElement(c, "DATA_TYPE", map.get("DATA_TYPE"));
			XmlUtil.createElement(c, "WHS_CODE", map.get("WHS_CODE"));
			XmlUtil.createElement(c, "LOCATION_CODE", map.get("LOCATION_CODE"));
			XmlUtil.createElement(c, "OWNER_CODE", map.get("OWNER_CODE"));
			XmlUtil.createElement(c, "OWNER_NAME", map.get("OWNER_NAME"));
			XmlUtil.createElement(c, "NOTE", map.get("NOTE"));
		}
	}

	
	/**
	 * 创建出入库报文体
	 */
	public void createStoreTransNodes(){
		for(Map<String, String> map:data.getBodyMaps()){
			Element c = XmlUtil.createElement(root,"STORE_TRANS");
			XmlUtil.createElement(c, "MESSAGE_ID", map.get("MESSAGE_ID"));
			XmlUtil.createElement(c, "EMS_NO", map.get("EMS_NO"));
			XmlUtil.createElement(c, "IO_NO", map.get("IO_NO"));
			XmlUtil.createElement(c, "GOODS_NATURE", map.get("GOODS_NATURE"));
			XmlUtil.createElement(c, "IO_DATE", map.get("IO_DATE"));
			XmlUtil.createElement(c, "COP_G_NO", map.get("COP_G_NO"));
			XmlUtil.createElement(c, "G_NO", map.get("G_NO"));
			XmlUtil.createElement(c, "HSCODE", map.get("HSCODE"));
			XmlUtil.createElement(c, "GNAME", map.get("GNAME"));
			XmlUtil.createElement(c, "GMODEL", map.get("GMODEL"));
			XmlUtil.createElement(c, "CURR", map.get("CURR"));
			XmlUtil.createElement(c, "COUNTRY", map.get("COUNTRY"));
			XmlUtil.createElement(c, "QTY", map.get("QTY"));
			XmlUtil.createElement(c, "UNIT", map.get("UNIT"));
			XmlUtil.createElement(c, "TYPE", map.get("TYPE"));
			XmlUtil.createElement(c, "CHK_CODE", map.get("CHK_CODE"));
			XmlUtil.createElement(c, "ENTRY_ID", map.get("ENTRY_ID"));
			XmlUtil.createElement(c, "GATEJOB_NO", map.get("GATEJOB_NO"));
			XmlUtil.createElement(c, "GOODS_BELONG", map.get("GOODS_BELONG"));
			XmlUtil.createElement(c, "GOODS_FORM", map.get("GOODS_FORM"));
			XmlUtil.createElement(c, "WHS_CODE", map.get("WHS_CODE"));
			XmlUtil.createElement(c, "LOCATION_CODE", map.get("LOCATION_CODE"));
			XmlUtil.createElement(c, "OWNER_CODE", map.get("OWNER_CODE"));
			XmlUtil.createElement(c, "OWNER_NAME", map.get("OWNER_NAME"));
			XmlUtil.createElement(c, "NOTE", map.get("NOTE"));
		}
	}
	
	/**
	 * 解析xml报文
	 * @param xmlStr
	 * @throws Exception
	 */
	public void parseResultToObj(String xmlStr) throws Exception{
		
		if(StringUtil.isBlank(xmlStr)) return;
		//获取document
		this.document = DocumentHelper.parseText(xmlStr);
		//获取根节点元素对象
		Element root = this.document.getRootElement();
		
		//读取TRANSMIT节点
		Element transmitElement = (Element)root.selectSingleNode("TRANSMIT");
		this.transmitMessage = parseMapToTransmitMessage(parseToMap(transmitElement));
		
		//如果入库失败，则读取RESULT_LIST节点
		if("A000".equals(transmitMessage.getCHK_RESULT())){
			messageList = new ArrayList<MessageDetail>();
			List<Element> nodeList = root.selectNodes("RESULT_LIST");
			for(Element node:nodeList){
				Map<String,String> detailMap = parseToMap(node);
				MessageDetail resultMessage = parseMapToMessageDetail(detailMap);
				messageList.add(resultMessage);
			}
		}
	}
	
	public Map<String,String> parseToMap(Element node) throws Exception{
		Iterator<Element> ite = node.elementIterator();
		Map<String,String> map = new HashMap<String,String>();
		while(ite.hasNext()){
			Element e = (Element)ite.next();
			map.put(e.getName(), e.getStringValue());
		}
		return map;
	}
	
	public TransmitMessage parseMapToTransmitMessage(Map<String,String> map) throws Exception{
		TransmitMessage transmitMessage = (TransmitMessage)mapToObject(map,TransmitMessage.class);
		return transmitMessage;
	}
	
	public MessageDetail parseMapToMessageDetail(Map<String,String> map) throws Exception{
		MessageDetail detail = (MessageDetail)mapToObject(map,MessageDetail.class);
		return detail;
	}
	
	
	public static Object mapToObject(Map<String,String> map,Class<?> beanClass) throws Exception{
		Object obj = beanClass.newInstance();
		BeanUtils.populate(obj, map);
		return obj;
	}
	

	public MSMQ getData() {
		return data;
	}

	public void setData(MSMQ data) {
		this.data = data;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}
	
	
	public TransmitMessage getTransmitMessage() {
		return transmitMessage;
	}

	public void setTransmitMessage(TransmitMessage transmitMessage) {
		this.transmitMessage = transmitMessage;
	}

	public List<MessageDetail> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageDetail> messageList) {
		this.messageList = messageList;
	}

	public static void main(String[] args) {
		try {
//			String xmlStr = "<HCHX_DATA><TRANSMIT><MESSAGE_ID>35DB6B07DCFC5FADE055000000000001</MESSAGE_ID><MESSAGE_TYPE>4001</MESSAGE_TYPE><EMS_NO>H80121603120</EMS_NO><ORDER_NO /><FUNCTION_CODE>R</FUNCTION_CODE><CHK_RESULT>A001</CHK_RESULT><MESSAGE_DATE>2016-06-22T17:18:18</MESSAGE_DATE><SENDER_ID>2335000001</SENDER_ID><SEND_ADDRESS /><RECEIVER_ID>8000</RECEIVER_ID><RECEIVER_ADDRESS /><MESSAGE_SIGN /></TRANSMIT></HCHX_DATA>";
//			Document doc = DocumentHelper.parseText(xmlStr);
//			Element node = doc.getRootElement();
//			Element transmitElement= (Element)node.selectSingleNode("TRANSMIT");
//			Iterator ite = transmitElement.elementIterator();
//			while(ite.hasNext()){
//				Element e = (Element)ite.next();
//				System.out.println(e.getName());
//				System.out.println(e.getStringValue());
//			}
//			Document doc = XmlUtil.parseDoc(xmlStr);
			
//			System.out.println(XmlUtil.getText(doc.getRootElement(), "MESSAGE_ID"));
			TransmitXmlUtil util = new TransmitXmlUtil();
			String xmlStr = "<HCHX_DATA><TRANSMIT><MESSAGE_ID>35DB6B07DCFC5FADE055000000000001</MESSAGE_ID><MESSAGE_TYPE>4001</MESSAGE_TYPE><EMS_NO>H80121603120</EMS_NO><ORDER_NO /><FUNCTION_CODE>R</FUNCTION_CODE><CHK_RESULT>A001</CHK_RESULT><MESSAGE_DATE>2016-06-22T17:18:18</MESSAGE_DATE><SENDER_ID>2335000001</SENDER_ID><SEND_ADDRESS /><RECEIVER_ID>8000</RECEIVER_ID><RECEIVER_ADDRESS /><MESSAGE_SIGN /></TRANSMIT></HCHX_DATA>";
			util.parseResultToObj(xmlStr);
			
			System.out.println(util.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
