package com.yunkouan.wms.modules.send.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.util.MD5Util;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IERPService;
import com.yunkouan.wms.modules.send.util.HttpClientUtils;
import com.yunkouan.wms.modules.send.vo.SendDeliveryMaterialVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

@Service(value="ERPServiceImpl")
public class ERPServiceImpl extends BaseService implements IERPService {
	private static Log log = LogFactory.getLog(ERPServiceImpl.class);

	@Autowired
	private IDeliveryService deliveryService;
	@Autowired
	private ISysParamService paramService;

	private String url = "";

	private void setUrl(String type){
		if(type.equals(Constant.ERP_UPDATELOGISTICS)){
			url = paramService.getKey(CacheName.ERP_INTERFACE_UPDATE_BILLNO);
		}
		else if(type.equals(Constant.ERP_UPDATEGOODS)){
			url = paramService.getKey(CacheName.ERP_INTERFACE_UPDATE_GOODS);
		}
		else if(type.equals(Constant.ERP_UPDATEINVENTORY)){
			url = paramService.getKey(CacheName.ERP_INTERFACE_UPDATE_INVENTORY);
		}
	}
	/**
	 * 推送运单号到ERP
	 * @param paramMap
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String updateLogistics(Map<String,String> paramMap) throws ClientProtocolException, IOException{
		Map<String,String> map = new TreeMap<String,String>(new Comparator<String>(){
			public int compare(String key1, String key2) {
				return key1.toString().compareTo(key2.toString());
			}
		});
		//将请求的参数（不包含sign）按键名顺序排序
		map.putAll(paramMap);
		StringBuffer str = new StringBuffer();
		//按此顺序将键值所对应的value拼接
		for(String key:map.keySet()){
			str.append(map.get(key));
		}
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		log.info("key:"+key);
		//所得到的字符串再拼接KEY
		str.append(key);
		//MD5加密，加密串转大写
		String sign = MD5Util.md5(str.toString()).toUpperCase();
		map.put("sign", sign);
		String url = paramService.getKey(CacheName.ERP_INTERFACE_UPDATE_BILLNO);
		String res = HttpClientUtils.getInstance().doPost(url, map, "UTF-8");
		return res;
	}
	
	/**
	 * erp接口调用
	 * @param erpInterfaceName
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public ErpResult doInvoke(String erpInterfaceName, Map<String,String> paramMap){
		//配置地址
		setUrl(erpInterfaceName);
		Map<String,String> map = new TreeMap<String,String>(new Comparator<String>(){
			public int compare(String key1, String key2) {
				return key1.toString().compareTo(key2.toString());
			}
		});
		//将请求的参数（不包含sign）按键名顺序排序
		map.putAll(paramMap);
		StringBuffer str = new StringBuffer();
		//按此顺序将键值所对应的value拼接
		for(String key:map.keySet()){
			str.append(map.get(key));
		}
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		log.info("key:"+key);
		//所得到的字符串再拼接KEY
		str.append(key);
		//MD5加密，加密串转大写
		log.info("Http_str:"+str);
		String sign = MD5Util.md5(str.toString().trim());
		log.info("Http_sign:"+sign);
		map.put("sign", sign);
		ErpResult erpResult= null;
		String res = null;
		try {
			res = HttpClientUtils.getInstance().doPost(this.url, map, "UTF-8");
			erpResult = JsonUtil.fromJson(res, ErpResult.class);
			if(erpResult == null){
				erpResult = new ErpResult();
				erpResult.setCode(-1);
				erpResult.setMessage(res);
			}
		} catch (Exception e) {
			erpResult = new ErpResult();
			erpResult.setCode(-1);
			erpResult.setMessage(e.getMessage());
		}
		return erpResult;
	}

	/** 
	* @Title: sendToERP 
	* @Description: 推送运单号到ERP，同时更新运单状态
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:15:47
	* @param vo
	* @param status
	* @return
	*/
	@Override
	public ErpResult sendToERP(SendDeliveryVo vo, String remarks,int intercept) throws Exception{
		String srcNo = vo.getSendDelivery().getSrcNo();
		String expressBillNo = vo.getSendDelivery().getExpressBillNo();
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("order_no", srcNo);
		paramMap.put("intercept", intercept+"");
		paramMap.put("remarks", remarks);
		paramMap.put("order_no", srcNo);
		paramMap.put("out_stype", vo.getSendDelivery().getExpressServiceCode());
		paramMap.put("out_sid", expressBillNo);
		paramMap.put("notify_time", timeStamp());
		//包裹重量
		paramMap.put("order_weight", vo.getSendDelivery().getScalageWeight()+"");
		String outDateStr = (vo.getSendDelivery().getShipmentTime().getTime()+"").substring(0,10);
		paramMap.put("out_date", outDateStr);
		//辅材
		List<Map<String,String>> materialList = new ArrayList<Map<String,String>>();
		for(SendDeliveryMaterialVo dmVo:vo.getDeliveryMaterialVoList()){
			Map<String,String> dmMap = new HashMap<String,String>();
			dmMap.put("bh", dmVo.getMaterialVo().getEntity().getMaterialNo());
			dmMap.put("number", dmVo.getSendDeliveryMaterial().getQty()+"");
			materialList.add(dmMap);
		}
		String jsonStr = JsonUtil.toJson(materialList);
		paramMap.put("materials", jsonStr);
//		String result = this.updateLogistics(paramMap);
//		if(log.isInfoEnabled()) log.info(result);
//		if(StringUtils.isBlank(result)) return 0;
//		ErpResult erpResult = JsonUtil.fromJson(result, ErpResult.class);
//		if(erpResult!=null && erpResult.getCode()==1){//成功
//			return deliveryService.updateSendStatus(vo.getSendDelivery().getDeliveryId(), status);
//		}else{
//			if(log.isErrorEnabled()) log.error(erpResult.getMessage());
//		}
		ErpResult erpResult = new ErpResult();
		try {
			String result = this.updateLogistics(paramMap);
			if(log.isInfoEnabled()) log.info(result);
			if(StringUtils.isBlank(result)) return null;
			erpResult = JsonUtil.fromJson(result, ErpResult.class);
			
			return erpResult;
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
			erpResult.setCode(-1);
			erpResult.setMessage("无法连接");
		}
		return erpResult;
	}
	/**时间戳给ERP推送用*/
	private static String timeStamp(){
		String str=System.currentTimeMillis()+"";
		return str.substring(0, 10);
	}
}