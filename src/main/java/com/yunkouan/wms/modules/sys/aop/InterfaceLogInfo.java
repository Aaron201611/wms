package com.yunkouan.wms.modules.sys.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InterfaceLogInfo {

	/**
	 * 发送方
	 * @return
	 */
	public String send();
	
	/**
	 * 接收方
	 * @return
	 */
	public String receive();
	
}
