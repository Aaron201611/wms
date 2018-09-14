package com.yunkouan.wms.modules.send.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.yunkouan.exception.BizException;
import com.yunkouan.wms.common.constant.Constant;


public class HttpUtils {
	private static Log log = LogFactory.getLog(HttpUtils.class);

	private static String  httpPostEms(String url,String message) throws ClientProtocolException, IOException{
		try {
			
			String testStr="content="+Base64Utils.getEncodeAndBase64(message);
			log.debug("sendEMS"+testStr);

			String ret = Request.Post(url)
					.socketTimeout(10000)
					.useExpectContinue()
					.version(HttpVersion.HTTP_1_1)
					.bodyString(testStr, ContentType.create("application/x-www-form-urlencoded",Consts.UTF_8))
					.execute().returnContent().asString();
			System.out.println(ret);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	private static String  httpGet(String url) throws ClientProtocolException, IOException{
		try {
			if(log.isDebugEnabled()) log.debug("url=================="+url);
			String ret = Request.Get(url)
					.socketTimeout(10000)
					.useExpectContinue()
					.version(HttpVersion.HTTP_1_1)
					.execute().returnContent().asString();
			System.out.println(ret);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(e.getMessage());
		}

	}
	private static String httpPost(String url,String message){
		HttpURLConnection httpurlconnection = null;
		try {
			URL u = new URL(url);
			httpurlconnection = (HttpURLConnection) u.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setReadTimeout(3000);
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			OutputStream os = httpurlconnection.getOutputStream();
			String message2=Base64Utils.encode(message.getBytes());
			os.write(("content="+message2).getBytes("utf-8"));
			os.flush();
			os.close();
			String rspString = InputStreamTOString(httpurlconnection.getInputStream());
			return rspString;
		}catch (Exception e) {
			return null;
		}finally {
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
	}
	private static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int count = -1;
		while ((count = in.read(data, 0, 1024)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "utf-8");
	}
	public static void main(String[] args) throws Exception {
		String str="<Manifest><Head><MessageID>3B3612E9EA0D499BA577D440E7AE7DC9</MessageID><FunctionCode>0</FunctionCode><MessageType>511</MessageType><SenderID>1701</SenderID><ReceiverID>EMS</ReceiverID><SendTime>2017-10-31 17:20:17</SendTime><Version>1.0</Version></Head><Declaration><Freights><Freight><appType>1</appType><appTime>20171031172017</appTime><appStatus>2</appStatus><logisticsCode>340226T001</logisticsCode><logisticsName>中国邮政速递物流股份有限公司芜湖市分公司</logisticsName><logisticsNo>KDBG102373764</logisticsNo><billNo>-</billNo><freight>5.0</freight><insuredFee>0.0</insuredFee><currency>142</currency><weight>0.6</weight><packNo>1</packNo><goodsInfo></goodsInfo><consignee>相翰林</consignee><consigneeAddress>长虹路凤凰和美4-2803</consigneeAddress><consigneeTelephone>13770799949</consigneeTelephone><note>null</note><orderNo>20171031130845586230</orderNo><ebpCode>340226T001</ebpCode></Freight></Freights></Declaration></Manifest>";
		String data = Base64Utils.encode(str.getBytes());
		String str2="content=" + URLEncoder.encode(data, "UTF-8");
		System.out.println(str2);
	}
}