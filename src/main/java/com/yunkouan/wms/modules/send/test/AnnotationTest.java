package com.yunkouan.wms.modules.send.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.modules.send.annotation.MyOper;
import com.yunkouan.wms.modules.send.util.HttpClientUtils;
import com.yunkouan.wms.modules.sys.service.IInterfaceLogService;

@Component("test")
public class AnnotationTest {
	
	
	@MyOper(oper="测试")
	public void say(){
		System.out.println("saying.........");
	}
	
	public static void main(String[] args) throws Exception{
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring/spr*.xml,");
		AnnotationTest test = (AnnotationTest)ac.getBean("test");
		test.say();
		
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("test", "123");
//		ApplicationContext ac = new ClassPathXmlApplicationContext("spring/spr*.xml");
//		HttpClientUtils.getInstance().doPost("www.123.com", new HashMap<String,String>(), "UTF-8");
	}
}
