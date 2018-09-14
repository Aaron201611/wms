package com.yunkouan.wms.common.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.message.service.IMessageHandleService;
import com.yunkouan.wms.modules.message.service.impl.IssMessageHandleServiceImpl;
import com.yunkouan.wms.modules.message.util.BusinessServiceFactory;

//@DisallowConcurrentExecution
public class ReceiveIssMessageJob extends QuartzJobBean{
	
	private static Log log = LogFactory.getLog(ReceiveIssMessageJob.class);
	
	

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(log.isInfoEnabled()) log.info("运行【查验线消息读取】定时任务...开始");
		try {
			IMessageHandleService messageHandleService = SpringContextHolder.getBean(IssMessageHandleServiceImpl.class);
			messageHandleService.receiveAndHandle(BusinessServiceFactory.getBusinessService(Constant.ISS01010));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		if(log.isInfoEnabled()) log.info("运行【查验线消息读取】定时任务...结束");
	}

}
