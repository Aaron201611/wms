package com.yunkouan.wms.modules.send.service;

import java.util.Map;

import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

public interface IERPService {
	/** 
	* @Title: updateLogistics 
	* @Description: 推送运单号到ERP同时更新订单状态
	* @auth tphe06
	* @time 2018 2018年4月19日 下午6:29:12
	* @param vo
	* @param status
	* @return
	* int
	*/
	public ErpResult sendToERP(SendDeliveryVo vo, String remarks,int intercept)  throws Exception;

	/**
	 * 推送运单号到ERP
	 * @param paramMap
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
//	public String updateLogistics(Map<String,String> paramMap) throws Exception;
	
	/**
	 * erp接口调用
	 * @param erpInterfaceName
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ErpResult doInvoke(String erpInterfaceName,Map<String,String> paramMap) throws Exception;
}