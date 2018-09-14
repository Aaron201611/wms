package com.yunkouan.wms.modules.application.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yunkouan.util.StringUtil;
import com.yunkouan.util.XmlUtil;
import com.yunkouan.wms.common.constant.Constant;

public class DeliverGoodsXMLMessageUtil {

	private Map<String,String> messageHeadMap;
	
	private Map<String,String> gateJobHeadMap;
	
	private List<Map<String,String>> gateJobBodyList;
	
	private List<Map<String,String>> gateJobItemList;
	
	private Map<String,String> stateGateJobMap;
	
	private Map<String,String> kernelHeadMap;
	
	private List<Map<String,String>> kernelBodyList;
	
	private List<Map<String,String>> kernelListList;
	
	private Map<String,String> statKernetMap;
	
	private Document document;
	
	private Element root;
	
	private Element MESSAGE_HEAD;
	
	private Element MESSAGE_BODY;
	

	
	
	public DeliverGoodsXMLMessageUtil(){
		document = XmlUtil.createDocument();
		root = XmlUtil.createElement(document, "ECI2013_MESSAGE");
		MESSAGE_HEAD = XmlUtil.createElement(root,"MESSAGE_HEAD");
		MESSAGE_BODY = XmlUtil.createElement(root,"MESSAGE_BODY");
		gateJobBodyList = new ArrayList<Map<String,String>>();
		gateJobItemList = new ArrayList<Map<String,String>>();
		messageHeadMap = new HashMap<String,String>();
		gateJobHeadMap = new HashMap<String,String>();
		stateGateJobMap = new HashMap<String,String>();
		kernelHeadMap = new HashMap<String,String>();
		kernelBodyList = new ArrayList<Map<String,String>>();
		kernelListList = new ArrayList<Map<String,String>>();
		statKernetMap = new HashMap<String,String>();
	}
	/**
	 * 创建申请单报文
	 * @return
	 */
	public String createApplicationXMLMessage(String applyFrom){
		createMessageHead();
		createApplicationMessageBody(applyFrom);
		String xml = XmlUtil.toXML(document);
		return xml;
	}
	
	/**
	 * 创建核放单报文
	 * @return
	 */
	public String createExamineXMLMessage(String applyFrom){
		createMessageHead();
		createExamineMessageBody(applyFrom);
		String xml = XmlUtil.toXML(document);
		return xml;
	}
	
	public String[] messageHeadName = {"MESSAGE_ID",						
										"MESSAGE_TYPE",        
										"ORDER_NO",               
										"FUNCTION_CODE",       
										"MESSAGE_DATE",        
										"SENDER_ID",             
										"SEND_ADDRESS",          
										"RECEIVER_ID",          
										"RECEIVER_ADDRESS",   
										"MESSAGE_SIGN",         
										"END_FLAG"};
	/**
	 * 创建报文头
	 */
	public void createMessageHead(){
		for(String s:messageHeadName){
			XmlUtil.createElement(MESSAGE_HEAD, s, messageHeadMap.get(s));
		}
	}
	
	/**
	 * 创建申请单报文体
	 */
	public void createApplicationMessageBody(String applyFrom){
		Element TMP_PRE_GATEJOB = XmlUtil.createElement(MESSAGE_BODY,"TMP_PRE_GATEJOB");
		Element TMP_PRE_GATEJOB_HEAD = XmlUtil.createElement(TMP_PRE_GATEJOB,"TMP_PRE_GATEJOB_HEAD");
		createGateJobHead(TMP_PRE_GATEJOB_HEAD);
		Element TMP_PRE_GATEJOB_BODY_LIST = XmlUtil.createElement(TMP_PRE_GATEJOB,"TMP_PRE_GATEJOB_BODY_LIST");
		createGateJobBodyList(TMP_PRE_GATEJOB_BODY_LIST);
		//分送集报报文增加TMP_PRE_GATEJOB_LIST，料号级别报文
		if(Constant.APPLY_FROM_DELIVER_GOODS.equals(applyFrom)){
			Element TMP_PRE_GATEJOB_LIST = XmlUtil.createElement(TMP_PRE_GATEJOB,"TMP_PRE_GATEJOB_LIST");
			createGateJobList(TMP_PRE_GATEJOB_LIST);
		}
		Element TMP_STAT_GATEJOB = XmlUtil.createElement(TMP_PRE_GATEJOB,"TMP_STAT_GATEJOB");
		createStatGateJob(TMP_STAT_GATEJOB);
	}
	
	public void createGateJobHead(Element c){
		for(String s:TMP_PRE_GATEJOB_HEAD){
			XmlUtil.createElement(c, s, gateJobHeadMap.get(s));
		}
//		XmlUtil.createElement(c, "GATEJOB_NO", gateJobHeadMap.get("GATEJOB_NO"));             
//		XmlUtil.createElement(c, "GATEJOB_TYPE", gateJobHeadMap.get("GATEJOB_TYPE"));         
//		XmlUtil.createElement(c, "GATEPASS_NO", gateJobHeadMap.get("GATEPASS_NO"));           
//		XmlUtil.createElement(c, "GP_I_E_FLAG", gateJobHeadMap.get("GP_I_E_FLAG"));           
//		XmlUtil.createElement(c, "BIZ_TYPE", gateJobHeadMap.get("BIZ_TYPE"));                 
//		XmlUtil.createElement(c, "BIZ_MODE", gateJobHeadMap.get("BIZ_MODE"));                 
//		XmlUtil.createElement(c, "I_E_FLAG", gateJobHeadMap.get("I_E_FLAG"));                 
//		XmlUtil.createElement(c, "DIRECT_FLAG", gateJobHeadMap.get("DIRECT_FLAG"));           
//		XmlUtil.createElement(c, "ECI_GOODS_FLAG", gateJobHeadMap.get("ECI_GOODS_FLAG"));     
//		XmlUtil.createElement(c, "ECI_EMS_NO", gateJobHeadMap.get("ECI_EMS_NO"));             
//		XmlUtil.createElement(c, "ECI_EMS_PROPERTY", gateJobHeadMap.get("ECI_EMS_PROPERTY")); 
//		XmlUtil.createElement(c, "ECI_EMS_LEVEL", gateJobHeadMap.get("ECI_EMS_LEVEL"));       
//		XmlUtil.createElement(c, "TRADE_CODE", gateJobHeadMap.get("TRADE_CODE"));             
//		XmlUtil.createElement(c, "TRADE_NAME", gateJobHeadMap.get("TRADE_NAME"));             
//		XmlUtil.createElement(c, "OUT_CODE", gateJobHeadMap.get("OUT_CODE"));                 
//		XmlUtil.createElement(c, "OUT_NAME", gateJobHeadMap.get("OUT_NAME"));                 
//		XmlUtil.createElement(c, "REL_GATEJOB_NO", gateJobHeadMap.get("REL_GATEJOB_NO"));     
//		XmlUtil.createElement(c, "OWNER_CODE", gateJobHeadMap.get("OWNER_CODE"));             
//		XmlUtil.createElement(c, "OWNER_NAME", gateJobHeadMap.get("OWNER_NAME"));             
//		XmlUtil.createElement(c, "GUARANTEE_TYPE", gateJobHeadMap.get("GUARANTEE_TYPE"));     
//		XmlUtil.createElement(c, "GUARANTEE_ID", gateJobHeadMap.get("GUARANTEE_ID"));         
//		XmlUtil.createElement(c, "GUARANTEE_TOTAL", gateJobHeadMap.get("GUARANTEE_TOTAL"));   
//		XmlUtil.createElement(c, "USED_TOTAL", gateJobHeadMap.get("USED_TOTAL"));             
//		XmlUtil.createElement(c, "LINK_MAN", gateJobHeadMap.get("LINK_MAN"));                 
//		XmlUtil.createElement(c, "LINK_PHONE", gateJobHeadMap.get("LINK_PHONE"));             
//		XmlUtil.createElement(c, "PACK_NO", gateJobHeadMap.get("PACK_NO"));                   
//		XmlUtil.createElement(c, "GROSS_WT", gateJobHeadMap.get("GROSS_WT"));                 
//		XmlUtil.createElement(c, "NET_WT", gateJobHeadMap.get("NET_WT"));                     
//		XmlUtil.createElement(c, "WRAP_TYPE", gateJobHeadMap.get("WRAP_TYPE"));               
//		XmlUtil.createElement(c, "REL_TYPE", gateJobHeadMap.get("REL_TYPE"));                 
//		XmlUtil.createElement(c, "REL_DAYS", gateJobHeadMap.get("REL_DAYS"));
//		XmlUtil.createElement(c, "CHCL_TYPE", gateJobHeadMap.get("CHCL_TYPE"));
//		XmlUtil.createElement(c, "COLLECT_TYPE", gateJobHeadMap.get("COLLECT_TYPE"));
//		XmlUtil.createElement(c, "REMARK", gateJobHeadMap.get("REMARK"));                     
	}
	
	public void createGateJobBodyList(Element c){
		for(Map<String,String> map:gateJobBodyList){
			Element TMP_PRE_GATEJOB_BODYNode = XmlUtil.createElement(c,"TMP_PRE_GATEJOB_BODY");
			for(String s:TMP_PRE_GATEJOB_BODY){
				XmlUtil.createElement(TMP_PRE_GATEJOB_BODYNode, s, map.get(s));
			}
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "GATEJOB_NO", map.get("GATEJOB_NO"));     
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "TRADE_CODE", map.get("TRADE_CODE"));
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "TRADE_NAME", map.get("TRADE_NAME"));    
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "ECI_EMS_NO", map.get("ECI_EMS_NO"));     
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "ECI_GOODS_FLAG", map.get("ECI_GOODS_FLAG")); 
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "SEQ_NO", map.get("SEQ_NO"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "G_NO", map.get("G_NO"));           
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "EXG_VERSION", map.get("EXG_VERSION"));    
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "CODE_T_S", map.get("CODE_T_S"));       
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "G_NAME", map.get("G_NAME"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "G_MODEL", map.get("G_MODEL"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "UNIT", map.get("UNIT"));           
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "DEC_QTY", map.get("DEC_QTY"));        
////			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "GROUP_QTY", map.get("GROUP_QTY"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "DEC_PRICE", map.get("DEC_PRICE"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "DEC_TOTAL", map.get("DEC_TOTAL"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "CURR", map.get("CURR"));          
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "QTY_1", map.get("QTY_1"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "UNIT_1", map.get("UNIT_1"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "QTY_2", map.get("QTY_2"));          
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "UNIT_2", map.get("UNIT_2"));        
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "ECI_MODIFY_MARK", map.get("ECI_MODIFY_MARK"));
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "ORIGIN_COUNTRY", map.get("ORIGIN_COUNTRY"));
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "DUTY_MODE", map.get("DUTY_MODE"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "USE_TO", map.get("USE_TO"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_BODY, "REMARK", map.get("REMARK"));
		}
	}
	
	public void createGateJobList(Element c){
		for(Map<String,String> map:gateJobItemList){
			Element TMP_PRE_GATEJOB_LIST_ITEM = XmlUtil.createElement(c,"TMP_PRE_GATEJOB_LIST_ITEM");
			for(String s:TMP_PRE_GATEJOB_LIST){
				XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, s, map.get(s));
			}
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "GATEJOB_NO", map.get("GATEJOB_NO"));     
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "TRADE_CODE", map.get("TRADE_CODE"));
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "TRADE_NAME", map.get("TRADE_NAME"));    
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "ECI_EMS_NO", map.get("ECI_EMS_NO"));     
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "ECI_GOODS_FLAG", map.get("ECI_GOODS_FLAG")); 
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "SEQ_NO", map.get("SEQ_NO"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "COP_G_NO", map.get("COP_G_NO"));           
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "EXG_VERSION", map.get("EXG_VERSION"));    
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "G_NO", map.get("G_NO"));    
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "CODE_T_S", map.get("CODE_T_S"));       
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "G_NAME", map.get("G_NAME"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "G_MODEL", map.get("G_MODEL"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "UNIT", map.get("UNIT"));           
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "DEC_QTY", map.get("DEC_QTY"));        
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "DEC_PRICE", map.get("DEC_PRICE"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "DEC_TOTAL", map.get("DEC_TOTAL"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "CURR", map.get("CURR"));          
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "QTY_1", map.get("QTY_1"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "UNIT_1", map.get("UNIT_1"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "QTY_2", map.get("QTY_2"));          
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "UNIT_2", map.get("UNIT_2"));        
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "ECI_MODIFY_MARK", map.get("ECI_MODIFY_MARK"));
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "ORIGIN_COUNTRY", map.get("ORIGIN_COUNTRY"));
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "DUTY_MODE", map.get("DUTY_MODE"));      
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "USE_TO", map.get("USE_TO"));         
//			XmlUtil.createElement(TMP_PRE_GATEJOB_LIST_ITEM, "REMARK", map.get("REMARK"));
		}
	}
	
	public void createStatGateJob(Element c){
		for(String s:TMP_STAT_GATEJOB){
			XmlUtil.createElement(c, s, stateGateJobMap.get(s));
		}
//		XmlUtil.createElement(c, "GATEJOB_NO", stateGateJobMap.get("GATEJOB_NO"));
//		XmlUtil.createElement(c, "AREA_CODE", stateGateJobMap.get("AREA_CODE"));
//		XmlUtil.createElement(c, "CUSTOMS_CODE", stateGateJobMap.get("CUSTOMS_CODE"));
//		XmlUtil.createElement(c, "TRADE_CODE", stateGateJobMap.get("TRADE_CODE"));
//		XmlUtil.createElement(c, "TRADE_NAME", stateGateJobMap.get("TRADE_NAME"));
//		XmlUtil.createElement(c, "INPUT_CODE", stateGateJobMap.get("INPUT_CODE"));
//		XmlUtil.createElement(c, "INPUT_NAME", stateGateJobMap.get("INPUT_NAME"));
//		XmlUtil.createElement(c, "DECLARE_CODE", stateGateJobMap.get("DECLARE_CODE"));
//		XmlUtil.createElement(c, "DECLARE_NAME", stateGateJobMap.get("DECLARE_NAME"));
//		XmlUtil.createElement(c, "STEP_ID", stateGateJobMap.get("STEP_ID"));
//		XmlUtil.createElement(c, "CREATE_PERSON", stateGateJobMap.get("CREATE_PERSON"));
//		XmlUtil.createElement(c, "CREATE_DATE", stateGateJobMap.get("CREATE_DATE"));
//		XmlUtil.createElement(c, "DECLARE_PERSON", stateGateJobMap.get("DECLARE_PERSON"));
//		XmlUtil.createElement(c, "DECLARE_DATE", stateGateJobMap.get("DECLARE_DATE"));
	}
	
	/**
	 * 创建核放单报文体
	 */
	public void createExamineMessageBody(String applyFrom){
		Element GAT_PRE_KERNEL = XmlUtil.createElement(MESSAGE_BODY,"GAT_PRE_KERNEL");
		Element GAT_PRE_KERNEL_HEAD = XmlUtil.createElement(GAT_PRE_KERNEL,"GAT_PRE_KERNEL_HEAD");
		if(Constant.APPLY_FROM_DELIVER_GOODS.equals(applyFrom)){
			createKernelHead(GAT_PRE_KERNEL_HEAD);
		}else{
			createClassManageKernelHead(GAT_PRE_KERNEL_HEAD);
		}
		Element GAT_PRE_KERNEL_BODY_LIST = XmlUtil.createElement(GAT_PRE_KERNEL,"GAT_PRE_KERNEL_BODY_LIST");
		createKernelBodyList(GAT_PRE_KERNEL_BODY_LIST);
		//分送集报要生成报文
		if(Constant.APPLY_FROM_DELIVER_GOODS.equals(applyFrom)){
			Element GAT_PRE_KERNEL_LIST_LIST = XmlUtil.createElement(GAT_PRE_KERNEL,"GAT_PRE_KERNEL_LIST_LIST");
			createKernelListList(GAT_PRE_KERNEL_LIST_LIST);
		}
		Element GAT_STAT_KERNEL = XmlUtil.createElement(GAT_PRE_KERNEL,"GAT_STAT_KERNEL");
		createStatKernel(GAT_STAT_KERNEL);
	}
	
	public void createKernelHead(Element c){
		for(String s:kernelHeadNames){
			XmlUtil.createElement(c, s, kernelHeadMap.get(s));
		}
	}
	
	public void createClassManageKernelHead(Element c){
		for(String s:classManagekernelHeadNames){
			XmlUtil.createElement(c, s, kernelHeadMap.get(s));
		}
	}
	
	public void createKernelBodyList(Element c){
		for(Map<String,String> map:kernelBodyList){
			Element GAT_PRE_KERNEL_BODY = XmlUtil.createElement(c,"GAT_PRE_KERNEL_BODY");
			for(String s:kernelBodyNames){
				XmlUtil.createElement(GAT_PRE_KERNEL_BODY, s, map.get(s));
			}
		}
	}
	
	public void createKernelListList(Element c){
		for(Map<String,String> map:kernelListList){
			Element GAT_PRE_KERNEL_LIST = XmlUtil.createElement(c,"GAT_PRE_KERNEL_LIST");
			for(String s:kernelListNames){
				XmlUtil.createElement(GAT_PRE_KERNEL_LIST, s, map.get(s));
			}
		}
	}
	
	public void createStatKernel(Element c){
		for(String s:statKernelNames){
			XmlUtil.createElement(c, s,statKernetMap.get(s));
		}
	}
	
	/**
	 * 解析回执报文
	 * @param messageType
	 * @param xmlStr
	 * @throws Exception
	 */
	public void parseMessage(String messageType,String xmlStr)throws Exception{
		//HBT1001回执报文
		if(messageType.equals(Constant.MESSAGE_TYPE_HBT1001)){
			parseHBT1001Message(xmlStr);
		}
		//申请单审批回执
		else if(messageType.equals(Constant.MESSAGE_TYPE_TMP1001)){
			parseTMP1001Message(xmlStr);
		}
		//核放单审批回执
		else if(messageType.equals(Constant.MESSAGE_TYPE_GAT1001)){
			parseGAT1001Message(xmlStr);
		}
	}
	
	/**
	 * 解析HBT1001回执报文
	 * @param xmlStr
	 * @throws Exception
	 */
	public void parseHBT1001Message(String xmlStr) throws Exception{
		if(StringUtil.isBlank(xmlStr)) return;
		
		//获取document
		this.document = DocumentHelper.parseText(xmlStr);
		//获取根节点元素对象
		Element root = this.document.getRootElement();
		
		//读取HBT_SUF_HERTBEAT_HEAD节点
		MESSAGE_HEAD = (Element)root.selectSingleNode("HBT_SUF_HERTBEAT_HEAD");
		this.messageHeadMap = parseToMap(MESSAGE_HEAD);
		
		//读取HBT_SUF_HERTBEAT_LIST节点
		MESSAGE_BODY = (Element)root.selectSingleNode("HBT_SUF_HERTBEAT_LIST");
		List<Element> nodeList = MESSAGE_BODY.selectNodes("HBT_SUF_HERTBEAT_DETAIL");
		for(Element node:nodeList){
			Map<String,String> detailMap = parseToMap(node);
			gateJobBodyList.add(detailMap);
		}
	}
	
	/**
	 * 解析TMP1001临时进出区申请单
	 * @param xmlStr
	 * @throws Exception
	 */
	public void parseTMP1001Message(String xmlStr) throws Exception{
		if(StringUtil.isBlank(xmlStr)) return;
		//获取document
		this.document = DocumentHelper.parseText(xmlStr);
		//获取根节点元素对象
		Element root = this.document.getRootElement();
		//读取MESSAGE_HEAD节点
		MESSAGE_HEAD = (Element)root.selectSingleNode("MESSAGE_HEAD");
		this.messageHeadMap = parseToMap(MESSAGE_HEAD);
		
		//读取MESSAGE_BODY节点
		MESSAGE_BODY = (Element)root.selectSingleNode("MESSAGE_BODY");
		//读取TMP_PRE_GATEJOB节点
		Element gateJobNode = (Element)MESSAGE_BODY.selectSingleNode("TMP_PRE_GATEJOB");
		//读取TMP_PRE_GATEJOB_HEAD节点
		Element gateJobHeadNode = (Element)gateJobNode.selectSingleNode("TMP_PRE_GATEJOB_HEAD");
		this.gateJobHeadMap = parseToMap(gateJobHeadNode);
		//读取TMP_STAT_GATEJOB节点
		Element statGateJobNode = (Element)gateJobNode.selectSingleNode("TMP_STAT_GATEJOB");
		this.stateGateJobMap = parseToMap(statGateJobNode);
	}
	
	/**
	 * 解析GAT1001核放单报文
	 * @param xmlStr
	 * @throws Exception
	 */
	public void parseGAT1001Message(String xmlStr) throws Exception{
		if(StringUtil.isBlank(xmlStr)) return;
		//获取document
		this.document = DocumentHelper.parseText(xmlStr);
		//获取根节点元素对象
		Element root = this.document.getRootElement();
		//读取MESSAGE_HEAD节点
		MESSAGE_HEAD = (Element)root.selectSingleNode("MESSAGE_HEAD");
		this.messageHeadMap = parseToMap(MESSAGE_HEAD);
		
		//读取MESSAGE_BODY节点
		MESSAGE_BODY = (Element)root.selectSingleNode("MESSAGE_BODY");
		//读取TMP_PRE_GATEJOB节点
		Element gateJobNode = (Element)MESSAGE_BODY.selectSingleNode("GAT_PRE_KERNEL");
		//读取TMP_PRE_GATEJOB_HEAD节点
		Element gateJobHeadNode = (Element)gateJobNode.selectSingleNode("GAT_PRE_KERNEL_HEAD");
		this.kernelHeadMap = parseToMap(gateJobHeadNode);
		//读取TMP_STAT_GATEJOB节点
		Element statGateJobNode = (Element)gateJobNode.selectSingleNode("GAT_STAT_KERNEL");
		this.statKernetMap = parseToMap(statGateJobNode);
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

	public Map<String, String> getMessageHeadMap() {
		return messageHeadMap;
	}

	public void setMessageHeadMap(Map<String, String> messageHeadMap) {
		this.messageHeadMap = messageHeadMap;
	}

	public Map<String, String> getGateJobHeadMap() {
		return gateJobHeadMap;
	}

	public void setGateJobHeadMap(Map<String, String> gateJobHeadMap) {
		this.gateJobHeadMap = gateJobHeadMap;
	}

	public List<Map<String, String>> getGateJobBodyList() {
		return gateJobBodyList;
	}

	public void setGateJobBodyList(List<Map<String, String>> gateJobBodyList) {
		this.gateJobBodyList = gateJobBodyList;
	}

	public List<Map<String, String>> getGateJobItemList() {
		return gateJobItemList;
	}

	public void setGateJobItemList(List<Map<String, String>> gateJobItemList) {
		this.gateJobItemList = gateJobItemList;
	}


	public Element getMESSAGE_HEAD() {
		return MESSAGE_HEAD;
	}


	public void setMESSAGE_HEAD(Element mESSAGE_HEAD) {
		MESSAGE_HEAD = mESSAGE_HEAD;
	}


	public Element getMESSAGE_BODY() {
		return MESSAGE_BODY;
	}


	public void setMESSAGE_BODY(Element mESSAGE_BODY) {
		MESSAGE_BODY = mESSAGE_BODY;
	}


	public Map<String, String> getStateGateJobMap() {
		return stateGateJobMap;
	}


	public void setStateGateJobMap(Map<String, String> stateGateJobMap) {
		this.stateGateJobMap = stateGateJobMap;
	}
	
	

	public Map<String, String> getKernelHeadMap() {
		return kernelHeadMap;
	}
	public void setKernelHeadMap(Map<String, String> kernelHeadMap) {
		this.kernelHeadMap = kernelHeadMap;
	}
	public List<Map<String, String>> getKernelBodyList() {
		return kernelBodyList;
	}
	public void setKernelBodyList(List<Map<String, String>> kernelBodyList) {
		this.kernelBodyList = kernelBodyList;
	}
	public List<Map<String, String>> getKernelListList() {
		return kernelListList;
	}
	public void setKernelListList(List<Map<String, String>> kernelListList) {
		this.kernelListList = kernelListList;
	}
	public Map<String, String> getStatKernetMap() {
		return statKernetMap;
	}
	public void setStatKernetMap(Map<String, String> statKernetMap) {
		this.statKernetMap = statKernetMap;
	}
	public static void main(String[] args) {
		String s = "{\"code\":3006,\"message\":\"\u540c\u6b65\u7528\u53cb\u5931\u8d25\"}";
		System.out.println(s);
	}
	
	public String[] TMP_PRE_GATEJOB_HEAD = {"GATEJOB_NO",      
											"GATEJOB_TYPE",    
											"GATEPASS_NO",     
											"GP_I_E_FLAG",     
											"BIZ_TYPE",        
											"BIZ_MODE",        
											"I_E_FLAG",        
											"DIRECT_FLAG",     
											"ECI_GOODS_FLAG",  
											"ECI_EMS_NO",      
											"ECI_EMS_PROPERTY",
											"ECI_EMS_LEVEL",   
											"TRADE_CODE",      
											"TRADE_NAME",      
											"OUT_CODE",        
											"OUT_NAME",        
											"REL_GATEJOB_NO",  
											"OWNER_CODE",      
											"OWNER_NAME",      
											"GUARANTEE_TYPE",  
											"GUARANTEE_ID",    
											"GUARANTEE_TOTAL", 
											"USED_TOTAL",      
											"LINK_MAN",        
											"LINK_PHONE",      
											"PACK_NO",         
											"GROSS_WT",        
											"NET_WT",          
											"WRAP_TYPE",       
											"REL_TYPE",        
											"REL_DAYS",        
											"CHCL_TYPE",       
											"COLLECT_TYPE",    
//											"RSK_ID",          
//											"RSK_TYPE",        
//											"GATE_STATUS",     
//											"GATE_DATE",       
											"REMARK",          
//											"EXTEND_FIELD_1",  
//											"EXTEND_FIELD_2",  
//											"EXTEND_FIELD_3",  
//											"EXTEND_FIELD_4",  
//											"EXTEND_FIELD_5" 
											};
	
	public String[] TMP_PRE_GATEJOB_BODY = {"GATEJOB_NO",     
											"TRADE_CODE",     
											"TRADE_NAME",     
											"ECI_EMS_NO",     
											"ECI_GOODS_FLAG", 
											"SEQ_NO",         
											"G_NO",           
											"EXG_VERSION",    
											"CODE_T_S",       
											"G_NAME",         
											"G_MODEL",        
											"UNIT",           
											"DEC_QTY",        
//											"GROUP_QTY",      
											"DEC_PRICE",      
											"DEC_TOTAL",      
//											"DEC_TOTAL_RMB",  
//											"DEC_TOTAL_USD",  
//											"EXCHANGE_RATE",  
											"CURR",           
											"QTY_1",          
											"UNIT_1",         
											"QTY_2",          
											"UNIT_2",         
											"ECI_MODIFY_MARK",
											"ORIGIN_COUNTRY", 
											"DUTY_MODE",      
											"USE_TO",         
											"REMARK",         
//											"EXTEND_FIELD_1", 
//											"EXTEND_FIELD_2", 
//											"EXTEND_FIELD_3", 
//											"EXTEND_FIELD_4", 
//											"EXTEND_FIELD_5" 
											};
	
	
	public String[] TMP_PRE_GATEJOB_LIST = {"GATEJOB_NO",       
											"TRADE_CODE",       
											"TRADE_NAME",       
											"ECI_EMS_NO",       
											"ECI_GOODS_FLAG",   
											"SEQ_NO",           
											"COP_G_NO",         
											"EXG_VERSION",      
											"G_NO",             
											"CODE_T_S",         
											"G_NAME",           
											"G_MODEL",          
											"UNIT",             
											"DEC_QTY",          
											"DEC_PRICE",        
											"DEC_TOTAL",        
//											"DEC_TOTAL_RMB",    
//											"DEC_TOTAL_USD",    
//											"EXCHANGE_RATE",    
											"CURR",             
											"QTY_1",            
											"UNIT_1",           
											"QTY_2",            
											"UNIT_2",           
											"ECI_MODIFY_MARK",  
											"ORIGIN_COUNTRY",   
											"DUTY_MODE",        
											"USE_TO",           
											"REMARK",           
//											"EXTEND_FIELD_1",   
//											"EXTEND_FIELD_2",   
//											"EXTEND_FIELD_3",   
//											"EXTEND_FIELD_4",   
//											"EXTEND_FIELD_5"  
												};
	
	public String[] TMP_STAT_GATEJOB = {"GATEJOB_NO",    
										"AREA_CODE",     
										"CUSTOMS_CODE",  
										"TRADE_CODE",    
										"TRADE_NAME",    
										"INPUT_CODE",    
										"INPUT_NAME",    
										"DECLARE_CODE",  
										"DECLARE_NAME",  
										"STEP_ID",       
//										"PRE_STEP_ID",   
//										"OPER_TYPE",     
//										"OPER_RESULT",   
										"CREATE_PERSON", 
										"CREATE_DATE",   
										"DECLARE_PERSON",
										"DECLARE_DATE"  
										};
	
	public String[] kernelHeadNames = {"KERNEL_NO",      	
										"KERNEL_TYPE",      
										"KERNEL_BIZ_TYPE",  
										"KERNEL_BIZ_MODE",  
										"BEG_AREA",         
										"END_AREA",         
										"I_E_FLAG",         
										"IC_CARD",          
										"CAR_NO",           
										"CAR_NUM",          
										"LAST_CAR_FLAG",    
										"CAR_WT",           
										"GOODS_WT",         
										"FRAME_NO",         
										"FRAME_TYPE",       
										"FRAME_WT",         
										"F_CONTA_NO",       
										"F_CONTA_TYPE",     
										"F_CONTA_WT",       
										"A_CONTA_NO",       
										"A_CONTA_TYPE",     
										"A_CONTA_WT",       
										"TOTAL_WT",         
										"TOTAL_PACK_NO",    
										"BILL_NO",          
										"FCB_NO",           
										"DYNAMIC_NO",       
										"MAIN_ITEMS",       
										//"GATE_I_STATUS",  
										//"GATE_I_DATE",    
										//"GATE_E_STATUS",  
										//"GATE_E_DATE",    
										//"RSK_TYPE",       
										//"B_STATUS",       
										//"M_STATUS",       
										//"H_STATUS",       
										//"DETAIN_TYPE",    
										"IS_CIRCLE",        
										"REMARK"           
										//"EXTEND_FIELD_1", 
										//"EXTEND_FIELD_2", 
										//"EXTEND_FIELD_3", 
										//"EXTEND_FIELD_4", 
										//"EXTEND_FIELD_5", 
										};
	
	public String[] kernelBodyNames = {"AUTO_ID",
										"KERNEL_NO",
										"GATEJOB_NO",
										"I_E_FLAG",
										"BIZ_TYPE",
										"REL_GATEJOB_NO",
										"BILL_NO",
										//"B_STATUS",
										"PACK_NO",
										"WEIGHT",
										"LAST_CAR_FLAG",
										"DETAIN_FLAG",
										"REMARK"
										//"EXTEND_FIELD_1", 
										//"EXTEND_FIELD_2", 
										//"EXTEND_FIELD_3", 
										//"EXTEND_FIELD_4", 
										//"EXTEND_FIELD_5", 
										};
	
	public String[] kernelListNames = {"AUTO_ID",
										"KERNEL_NO_REL",
										"KERNEL_NO",
										"KERNEL_TYPE",
										"KERNEL_BIZ_TYPE",
										"KERNEL_BIZ_MODE",
//										"GATEJOB_NO",
										"TRADE_CODE",
										"TRADE_NAME",
//										"ECI_EMS_NO",
//										"ECI_GOODS_FLAG",
//										"SEQ_NO",
//										"COP_G_NO",
//										"EXG_VERSION",
//										"G_NO",
//										"CODE_T_S",
										"G_NAME",
										"G_MODEL",
										"UNIT",
										"DEC_QTY",
										"GROSS_WT",
//										"DEC_PRICE",
//										"DEC_TOTAL",
//										"DEC_TOTAL_RMB",
//										"DEC_TOTAL_USD",
//										"EXCHANGE_RATE",
//										"CURR",
//										"QTY_1",
//										"UNIT_1",
//										"QTY_2",
//										"UNIT_2",
//										"ECI_MODIFY_MARK",
//										"ORIGIN_COUNTRY",
//										"DUTY_MODE",
//										"USE_TO",
										"REMARK",
//										"EXTEND_FIELD_1",  
//										"EXTEND_FIELD_2",  
//										"EXTEND_FIELD_3",  
//										"EXTEND_FIELD_4",  
//										"EXTEND_FIELD_5",  
										"PACK_NO"};
	
	public String[] statKernelNames = {"KERNEL_NO",
										"AREA_CODE",
										"CUSTOMS_CODE",
										"INPUT_CODE",
										"INPUT_NAME",
										"DECLARE_CODE",
										"DECLARE_NAME",
										"STEP_ID",
//										"PRE_STEP_ID", 
//										"OPER_TYPE",   
//										"OPER_RESULT", 
										"CREATE_PERSON",
										"CREATE_DATE",
										"DECLARE_PERSON",
										"DECLARE_DATE",};
	
	public String[] classManagekernelHeadNames = {"KERNEL_NO",
													"KERNEL_TYPE",
													"KERNEL_BIZ_TYPE",
													"KERNEL_BIZ_MODE",
													"BEG_AREA",
													"END_AREA",
													"I_E_FLAG",
													"IC_CARD",
													"CAR_NO",
													"CAR_NUM",
													"LAST_CAR_FLAG",
													"CAR_WT",
													"GOODS_WT",
													"FRAME_NO",
													"FRAME_TYPE",
													"FRAME_WT",
													"F_CONTA_NO",
													"F_CONTA_TYPE",
													"F_CONTA_WT",
													"A_CONTA_NO",
													"A_CONTA_TYPE",
													"A_CONTA_WT",
													"TOTAL_WT",
													"TOTAL_PACK_NO",
													"BILL_NO",
													"FCB_NO",
													"DYNAMIC_NO",
													"MAIN_ITEMS",
													"REMARK",
										//			"EXTEND_FIELD_1",
										//			"EXTEND_FIELD_2",
										//			"EXTEND_FIELD_3",
										//			"EXTEND_FIELD_4",
										//			"EXTEND_FIELD_5",
													};
}
