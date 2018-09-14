package com.yunkouan.wms.modules.message.util;

import java.util.HashMap;
import java.util.Map;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.message.service.IMessageService;
import com.yunkouan.wms.modules.message.service.impl.ISS01010ServiceImpl;


public class BusinessServiceFactory {
	
	private static Map<String,Class> serviceMap = new HashMap<String,Class>();
	
	
	static{
		serviceMap.put(Constant.ISS01010, ISS01010ServiceImpl.class);
	}
	
	public static IMessageService getBusinessService(String type){
		IMessageService service = null;
		
		Class bclass = serviceMap.get(type);
		if(bclass != null){
			service = (IMessageService)SpringContextHolder.getBean(bclass);
		}
		
		return service;
	}
	
	

}
