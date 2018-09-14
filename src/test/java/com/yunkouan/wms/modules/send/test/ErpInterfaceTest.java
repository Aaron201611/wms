package com.yunkouan.wms.modules.send.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yunkouan.util.MD5Util;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IERPService;
import com.yunkouan.wms.modules.send.util.HttpClientUtils;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;


public class ErpInterfaceTest extends BaseJunitTest{

	@Autowired
	private IDeliveryService deliveryService;
	
	@Autowired
	private IERPService erpService;
	
	@Test
	public void  testSendErp(){
		
		//查找发货单 7C8148D77371468E8672B4F64FADE949
		List<String> idList = new ArrayList<String>();
		idList.add("7C8148D77371468E8672B4F64FADE949");
		try {
			List<SendDeliveryVo> list = deliveryService.qryByIds(idList);
			for(SendDeliveryVo deliver : list) {
				erpService.sendToERP(deliver,"",Constant.CONFIRM_RETURN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String,String> createMap(Map<String,String> paramMap){
		Map<String,String> map = new TreeMap<String,String>(new Comparator<String>(){
			public int compare(String key1, String key2) {
				return key1.toString().compareTo(key2.toString());
			}
		});
		map.putAll(paramMap);
		StringBuffer str = new StringBuffer();
		for(String key:map.keySet()){
			str.append(map.get(key));
		}
		str.append("12345678");
		System.out.println(str.toString());
		String sign = MD5Util.md5(str.toString()).toUpperCase();
		map.put("sign", sign);
		return map;
	}
	
	public void updateLogisticsTest(){
		Map<String,String> map = new TreeMap<String,String>(new Comparator<String>(){
			public int compare(String key1, String key2) {
				return key1.toString().compareTo(key2.toString());
			}
			
		});
		map.put("order_no", "123456");
		map.put("out_stype", "SF");
		map.put("out_sid", "987654321");
		map.put("notify_time", new Date().getTime()+"");

		StringBuffer str = new StringBuffer();
		for(String key:map.keySet()){
			str.append(map.get(key));
		}
		str.append("12345678");
		System.out.println(str.toString());
		String sign = MD5Util.md5(str.toString()).toUpperCase();
		map.put("sign", sign);
		String url = "http://60.167.71.23/index.php?g=Api&m=Wms&a=updateLogistics";
		try {
			String res = HttpClientUtils.getInstance().doPost(url, map, "UTF-8");
//			String s = new String(res.getBytes("utf-8"), "GBK");
            Map map1 = JSON.parseObject(res, Map.class);
			System.out.println(res);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateGoodsTest(int status) throws ClientProtocolException, IOException{
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("goods_no", "12345678000");
		paramMap.put("hg_goods_no", "A0197192345");
		paramMap.put("goods_name", "王者农药");
		paramMap.put("bar_code", "283102318018");
		paramMap.put("warehouse_code", "WHKJ01");
		paramMap.put("unit", "单");
		paramMap.put("goods_weight", "1.01");
		paramMap.put("notify_time", new Date().getTime()+"");
		paramMap.put("status", status+"");
		Map<String,String> map = createMap(paramMap);
		String url = "http://60.167.71.23/Api/Wms/updateGoods";
		String res = HttpClientUtils.getInstance().doPost(url, map, "UTF-8");
		Map map1 = JSON.parseObject(res, Map.class);
		System.out.println(map1);
	}
	
	public static void main(String[] args) {
		try {
			ErpInterfaceTest.updateGoodsTest(0);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
