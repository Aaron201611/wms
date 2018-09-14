package com.yunkouan.wms.modules.send.annotation;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunkouan.wms.common.aop.log.LogAspect;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.send.util.InterfaceLogUtil;
import com.yunkouan.wms.modules.sys.entity.InterfaceLog;
import com.yunkouan.wms.modules.sys.service.IInterfaceLogService;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

@Aspect
@Component
public class OperLogAspect {
	
	@Autowired
	private IInterfaceLogService interfaceLogService;
	/**
	 * 定义切点
	 */
	@Pointcut("@annotation(com.yunkouan.wms.modules.send.annotation.MyOper)")
	public void operAspect(){}
	
	@Before(value="operAspect()")
	public void addOperLog(JoinPoint jp) throws Exception{
		System.out.println("addOperLog=======start!");
		Class<?> targetClass = jp.getTarget().getClass();
		String methodName = jp.getSignature().getName();
		Class<?>[] paramterTypes = ((MethodSignature)(jp.getSignature())).getParameterTypes();
		Method method = targetClass.getMethod(methodName, paramterTypes);
		//得到方法上的注解 
		MyOper myOper = method.getAnnotation(MyOper.class);
		System.out.println(myOper.oper());
		//新增
		InterfaceLogVo logVo = new InterfaceLogVo(new InterfaceLog());
		logVo.getEntity().setLogId(IdUtil.getUUID());
		logVo.getEntity().setCreateTime(new Date());
		logVo.getEntity().setUpdateTime(new Date());
		interfaceLogService.add(logVo);
		//修改
		logVo.getEntity().setOperer("fdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfddfdf");
		InterfaceLogUtil.getInstance().updateResult(logVo);
		
	}

}
