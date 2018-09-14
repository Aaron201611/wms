package com.yunkouan.wms.modules.send.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.sys.aop.InterfaceLogInfo;

@Component("httpClientUtils")
public class HttpClientUtils {
	private static Log log = LogFactory.getLog(HttpClientUtils.class);

	public static HttpClientUtils getInstance(){
		HttpClientUtils httpClientUtils = SpringContextHolder.getBean("httpClientUtils");
		return httpClientUtils;
	}
	
	@InterfaceLogInfo(send=Constant.SYS_WMS,receive=Constant.SYS_ERP)
//    @OpLog(model = OpLog.MODEL_SYSTEM_WAREHOUSE_LOG, type = OpLog.OP_TYPE_ADD, pos = 0)
	public String doPost(String url,Map<String,String> map,String charset) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		
		httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();//设置请求和传输超时时间
		httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//请求头
		//设置参数
		List<NameValuePair>  list = new ArrayList<NameValuePair>();
		Iterator<Entry<String,String>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,String> entry = iterator.next();
			list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
		}
		if(list.size() > 0){
			UrlEncodedFormEntity urlEncodeFormEntity = new UrlEncodedFormEntity(list,charset);
			httpPost.setEntity(urlEncodeFormEntity);
		}
		log.info("HTTP_URL:"+httpPost.getURI());
		log.info("HTTP_Param:"+map);
		HttpResponse response = httpClient.execute(httpPost);
		if(response != null){
			HttpEntity resEntity = response.getEntity();  
            if(resEntity != null){  
                result = EntityUtils.toString(resEntity,charset);  
                log.info("HTTP_Result:"+result);
            } 
		}
		return result;
	} 
	
	public static String convert(String utfString){  
		   StringBuilder sb = new StringBuilder();  
		   int i = -1;  
		   int pos = 0;  
		     
		   while((i=utfString.indexOf("\\u", pos)) != -1){  
		       sb.append(utfString.substring(pos, i));  
		       if(i+5 < utfString.length()){  
		           pos = i+6;  
		           sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));  
		       }  
		   }  
		   return sb.toString();  
	}
	
	public static void main(String[] args) throws Exception{
//		Map<String,String> map = new TreeMap<String,String>(new Comparator<String>(){
//			public int compare(String key1, String key2) {
//				return key1.toString().compareTo(key2.toString());
//			}
//			
//		});
//		map.put("order_no", "123456");
//		map.put("out_stype", "SF");
//		map.put("out_sid", "987654321");
//		map.put("notify_time", new Date().getTime()+"");
//
//		System.out.println(map.entrySet());
		
		
//		StringBuffer str = new StringBuffer();
//		for(String key:map.keySet()){
//			str.append(map.get(key));
//		}
//		str.append("12345678");
//		System.out.println(str.toString());
//		String sign = MD5Util.md5(str.toString()).toUpperCase();
//		map.put("sign", sign);
//		String url = "http://60.167.71.23/index.php?g=Api&m=Wms&a=updateLogistics";
//		try {
//			String res = HttpClientUtils.doPost(url, map, "UTF-8");
////			String s = new String(res.getBytes("utf-8"), "GBK");
//            Map map1 = JSON.parseObject(res, Map.class);
//			System.out.println(res);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
	

}
