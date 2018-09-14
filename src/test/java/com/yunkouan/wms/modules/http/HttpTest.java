package com.yunkouan.wms.modules.http;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.shiro.UsernamePasswordToken;

public class HttpTest  {
	private static Log log = LogFactory.getLog(HttpTest.class);

	public static void main(String[] args) throws Exception {
		for(int i=0; i<1; ++i) {
	        Thread t = new Thread() {
	            public void run() {
	            	try {
						go();
					} catch (UnsupportedOperationException | IOException | InterruptedException e) {
						e.printStackTrace();
					}
	            };
	        };
	        t.start();
		}
		Thread.sleep(5*1000);
	}

	private static void go() throws ClientProtocolException, IOException, UnsupportedOperationException, InterruptedException {
		//先登录
//		String json = login();
		String json = "{}";
		StringEntity entity = new StringEntity(json, ContentType.create("application/json", Consts.UTF_8));
//		String url = "http://localhost:6060/wms/console/adminlogin/doLogin";
		String url = "http://localhost:6060/wms/console/area/list";
		Request req = Request.Post(url).body(entity).useExpectContinue().version(HttpVersion.HTTP_1_1);
		CookieStore cookieStore = new BasicCookieStore();
		Executor executor = Executor.newInstance();
		Response rsp = executor.use(cookieStore).execute(req);
		HttpResponse http = rsp.returnResponse();
		Header[] headers = http.getAllHeaders();
		for(int i=0; i<headers.length; ++i) {
			Header h1 = headers[i];
			//Set-Cookie:JSESSIONID=b5882d5b-e2c3-4d43-9e26-6ee5fc3bcad0; Path=/telec; HttpOnly
//			if(log.isInfoEnabled()) log.info(h1.getName()+":"+h1.getValue());
		}
//		String json_rsp = rsp.returnContent().asString(Consts.UTF_8);
		long length = http.getEntity().getContentLength();
		byte[] data = new byte[(int)length];
		http.getEntity().getContent().read(data);
		if(log.isErrorEnabled()) log.error("json_rsp1:"+new String(data, Consts.UTF_8));
//		list(executor, headers);
	}
	private void list(Executor executor, Header[] headers) throws ClientProtocolException, IOException, UnsupportedOperationException, InterruptedException {
		if(headers == null) return;
		//先登录
		String json = "{}";
		StringEntity entity = new StringEntity(json, ContentType.create("application/json", Consts.UTF_8));
		String url = "http://localhost:6060/wms/console/area/list";
		Request req = Request.Post(url).body(entity).useExpectContinue().version(HttpVersion.HTTP_1_1);
		CookieStore cookieStore = new BasicCookieStore();
		for(int i=0; i<headers.length; ++i) {
			Header header = headers[i];
			if(header.getValue().startsWith("JSESSIONID")) {
				String cookieid = header.getValue().split(";")[0].replaceFirst("JSESSIONID\\=", "");
				if(cookieid.indexOf("-") == -1) continue;
				if(log.isInfoEnabled()) log.info("cookieid:"+cookieid);
				BasicClientCookie2 c = new BasicClientCookie2("JSESSIONID", cookieid);
				c.setVersion(0);
				c.setDomain("localhost");
				c.setPath("/");///telec
				c.setPorts(new int[] {6060});
				c.setSecure(true);
				cookieStore.addCookie(c);
			}
		}
		Response rsp = executor.use(cookieStore).execute(req);
		String json_rsp = rsp.returnContent().asString(Consts.UTF_8);
		if(log.isInfoEnabled()) log.info("json_rsp2:"+json_rsp);
	}

	private String login() {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setFrom("1");
		token.setLoginType(false);
		token.setOrgId("000001");
		token.setPassword("111111".toCharArray());
		token.setUsername("000001");
		String json = JSON.toJSONString(token);
		return json;
	}
}