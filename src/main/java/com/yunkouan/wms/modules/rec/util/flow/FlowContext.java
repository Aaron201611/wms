/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午2:21:51<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow;

import java.util.HashMap;

import com.yunkouan.util.SpringContextHolder;

/**
 * 调用上下文对象，用于存放业务服务类对象和流程节点之间的共享数据  <br/><br/>
 * @version 2017年3月5日 下午2:21:51<br/>
 * @author andy wang<br/>
 */
public class FlowContext extends HashMap<String,Object> {
	private static final long serialVersionUID = -51418877800438129L;
	
//	/**
//	 * 获取Spring上下文Bean
//	 * @version 2017年3月5日下午5:44:47<br/>
//	 * @author andy wang<br/>
//	 */
//	public Object getBean ( Class<?> clazz ) throws Exception {
//		return this.getBean(clazz.getName());
//	}
	
	/**
	 * 获取Spring上下文Bean（并把Bean缓存）
	 * —— 如果填入key已经被缓存，直接获取缓存中的对象
	 * @param key 缓存key
	 * @param clazz Bean对象的Class
	 * @return 对应clazz的Bean
	 * @throws Exception
	 * @version 2017年3月5日 下午5:51:44<br/>
	 * @author andy wang<br/>
	 */
	public Object getBean ( String key ) throws Exception {
		Object bean = null;
		if ( this.containsKey(key) ) {
			bean = this.get(key);
		} else {
			if ( key.startsWith("com.") ) {
				bean = SpringContextHolder.getBean(key);
			}
			this.put(key, bean);
		}
		return bean;
	}
	
	public Object getBean ( Class<?> clazz ) throws Exception {
		Object bean = null;
		String key = clazz.getName();
		if ( this.containsKey(key) ) {
			bean = this.get(key);
		} else {
			if ( key.startsWith("com.") ) {
				bean = SpringContextHolder.getBean(clazz);
			}
			this.put(key, bean);
		}
		return bean;
	}
}