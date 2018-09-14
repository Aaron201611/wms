package com.yunkouan.wms.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.yunkouan.util.HttpUtil;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.wms.modules.send.vo.Vip007Data;

public class TestHttpClient {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		testVip007();
		testVip009();
	}

	/** 
	* @Title: testVip007 
	* @Description: 上送运单信息
	* @auth tphe06
	* @time 2018 2018年4月17日 下午6:38:29
	* @throws ClientProtocolException
	* @throws IOException
	* void
	*/
	private static void testVip007() throws ClientProtocolException, IOException {
		List<Vip007Data> list = new ArrayList<Vip007Data>();
		Vip007Data data = new Vip007Data();
		data.setBillno("220000000881");
		data.setSenddate("2014-02-27");
		data.setSendsite("上海陈行公司");
		data.setSendcus("深圳敏思达");
		data.setSendperson("李名学");
		data.setSendtel("13662625320");
		data.setReceivecus("深圳天翼德");
		data.setReceiveperson("小桥");
		data.setReceivetel("10086");
		data.setGoodsname("苹果测试");
		data.setInputdate("");
		data.setInputperson("东商");
		data.setInputsite("上海陈行公司");
		data.setLasteditdate("");
		data.setLasteditperson("");
		data.setLasteditsite("上海陈行公司");
		data.setRemark("测试备注");
		data.setReceiveprovince("上海");
		data.setReceivecity("上海市");
		data.setReceivearea("青浦区");
		data.setReceiveaddress("收件地址");
		data.setSendprovince("江西省");
		data.setSendcity("上饶市");
		data.setSendarea("上饶县");
		data.setSendaddress("寄件地址");
		data.setWeight("12");
		data.setProductcode("");
		data.setOrderno("081120317");
		list.add(data);
		String json = JsonUtil.toJson(list);
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "vip0007");//请求的方法名
		map.put("data_digest", "ec30c4dd6d04325b72688384753c2952");//方法签名
		map.put("cuspwd", "limx_1234");//客户密码
		map.put("data", json);
		String url = "http://222.72.44.130:28080/sto_vipFacade/PreviewInterfaceAction.action";
		String ret = HttpUtil.post(url, map);
		System.out.println();
		System.out.println(ret);
	}
	/** 
	* @Title: testVip009 
	* @Description: 获取运单号
	* @auth tphe06
	* @time 2018 2018年4月17日 下午6:39:02
	* @throws ClientProtocolException
	* @throws IOException
	* void
	*/
	private static void testVip009() throws ClientProtocolException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "vip0009");//请求的方法名
		map.put("data_digest", "ec30c4dd6d04325b72688384753c2952");//方法签名
		map.put("cuspwd", "limx_1234");//客户密码
		map.put("cusname", "东商");//客户名称
		map.put("cusite", "上海陈行公司");//网点名称
		map.put("len", "1");//个数
		String url = "http://222.72.44.130:28080/sto_vipFacade/PreviewInterfaceAction.action";
		String ret = HttpUtil.post(url, map);
		System.out.println();
		System.out.println(ret);
	}
}