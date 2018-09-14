package com.yunkouan.wms.modules.sys.aop;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yunkouan.wms.modules.send.util.InterfaceLogUtil;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

@Aspect
@Component
public class InterfaceLogAspect {
	private Logger log = LoggerFactory.getLogger(InterfaceLogAspect.class);
	
//	@Pointcut("@annotation(com.yunkouan.wms.modules.sys.aop.InterfaceLogInfo)")
//	public void arroundPointcut(){}
	
//	@Around(value="arroundPointcut()")
	@Around("@annotation(interfaceLogInfo)")
	public String around(ProceedingJoinPoint jp,InterfaceLogInfo interfaceLogInfo){
		if(log.isInfoEnabled()) log.info("使用接口日志注解开始...");
//		Class<?> targetClass = jp.getTarget().getClass();
//		String methodName = jp.getSignature().getName();
//		Class<?>[] paramerTypes = ((MethodSignature)jp.getSignature()).getParameterTypes();
//		
//		String sendMessage = paramerTypes[1].toString();
//		String url = paramerTypes[0].toString();
		Object[] args = jp.getArgs();
		String url = args[0].toString();
		String sendMessage = args[1].toString();
		InterfaceLogVo logVo = InterfaceLogUtil.getInstance().addLog(interfaceLogInfo.send(), 
																	sendMessage, 
																	new Date(), 
																	interfaceLogInfo.receive(), 
																	null, 
																	null, 
																	url, 
																	null, 
																	null);
		String result = null;
		try {
			result = (String)jp.proceed();
			
		} catch (Throwable e) {
			result = e.getMessage();
		}
		logVo.getEntity().setResult(result);
		InterfaceLogUtil.getInstance().updateResult(logVo);
		if(log.isDebugEnabled()) log.debug("使用接口日志注解结束...");
		return result;
		
	}
	
	
	
}
