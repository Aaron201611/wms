package com.yunkouan.wms.common.aop.log;

import java.lang.reflect.Method;
import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.entity.SysLog;
import com.yunkouan.saas.modules.sys.service.ILogService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;

/**
 * 自定义日志切面
 * @author tphe06
 */
@Aspect
@Component
public class LogAspect {
	private Logger log = LoggerFactory.getLogger(LogAspect.class);

	@Autowired
	private ILogService service;

    /**
     * 定义切点，在下方注解中用到
     */
    @Pointcut("@annotation(com.yunkouan.wms.common.aop.log.OpLog)")
    public void aspect() {}

	@Around("aspect()")
	public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {
		if(log.isInfoEnabled()) log.info("使用自定义的日志注解开始...");
		// 得到方法上的自定义日志注解
		Class<?> classTarget = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class<?>[] paramTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
		Method objMethod = classTarget.getMethod(methodName, paramTypes);
		OpLog annotation = objMethod.getAnnotation(OpLog.class);
		Principal p = LoginUtil.getLoginUser();
		Object result = point.proceed();
		try {
			Principal p1 = LoginUtil.getLoginUser();
			if(p == null && p1 != null) p = p1;
			if(result instanceof ResultModel) {
				Boolean success = ((ResultModel)result).isSuccess();
				if(success) {
					saveLog(p, annotation.type(), annotation.model(), getParam(point, annotation.pos()), 0);
				} else {
					saveLog(p, annotation.type(), annotation.model(), getParam(point, annotation.pos()), 1);
				}
			} else {
				saveLog(p, annotation.type(), annotation.model(), getParam(point, annotation.pos()), 0);
			}
		} catch(Throwable e) {
			e.printStackTrace();
			if(log.isErrorEnabled()) log.error(e.getMessage());
			saveLog(p, annotation.type(), annotation.model(), getParam(point, annotation.pos()), 1);
		}
		if(log.isInfoEnabled()) log.info("使用自定义的日志注解结束.");
		return result;
	}

	/**
	 * 保存日志信息
	 * @param button 操作类型
	 * @param model 模块标识
	 * @param content 日志内容
	 * @param error 是否失败
	 */
	private void saveLog(Principal p, String button, String model, String content, Integer error) {
		SysLog log = new SysLog();
		log.setCreateTime(new Date());
		log.setIsError(error);
		log.setLogContent(content);
		log.setOpButton(button);
		log.setOpModel(model);
		log.setOpTime(new Date());
		if(p != null) {
			log.setCreatePerson(p.getUserId());
			log.setOpPerson(p.getUserId());
			log.setOrgId(p.getOrgId());
			log.setWarehouseId(LoginUtil.getWareHouseId());
		}
//      	service.add(log);
	}

	/**
	 * 把参数封装成json格式
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private static String getParam(JoinPoint joinPoint, int pos) throws Exception {
		Object[] arguments = joinPoint.getArgs();
		if(arguments == null || arguments.length == 0) return "";
		if(pos == -2) return "";
		if(pos >= 0) return toJson(arguments[pos]);
		return toJson(arguments);
   }

	/**
	 * 把数据封装成JSON格式字符串
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	private static String toJson(Object obj) throws JsonProcessingException {
		// 把数据封装成JSON格式字符串
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String result = objectMapper.writeValueAsString(obj);
		return result;
	}
}