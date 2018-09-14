package com.yunkouan.wms.modules.send.service;

import java.io.IOException;
import java.util.List;

import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

public interface IExpressService {
	/**快递公司-申通**/
	public static final String EXPRESS_CODE_ST = "ST";
	/**快递公司-顺丰**/
	public static final String EXPRESS_CODE_SF = "SF";
	/**快递公司-韵达**/
	public static final String EXPRESS_CODE_YD = "YD";
	/**快递公司-华亿乐购**/
	public static final String EXPRESS_CODE_HYLG = "HYLG";
	/**快递公司-EMS**/
	public static final String EXPRESS_CODE_EMS = "EMS";

	/** 
	* @Title: getLogisticsNo 
	* @Description: 获取物流公司运单号
	* @auth tphe06
	* @time 2018 2018年4月19日 下午2:25:07
	* @param expressCode 物流公司编号
	* @return
	* String
	 * @throws Exception 
	 * @throws IOException 
	*/
	public String getLogisticsNo(String expressCode) throws IOException, Exception;
	/** 
	* @Title: sendDeliveryToLogistics 
	* @Description: 批量将发货单发送给物流公司，返回推送成功的记录
	* @auth tphe06
	* @time 2018 2018年4月19日 下午2:28:04
	* @param deliveryIds
	* @return
	* @throws Exception
	* String
	*/
	public List<SendDeliveryVo> sendDeliveryToLogistics(List<SendDeliveryVo> list) throws Exception;
}