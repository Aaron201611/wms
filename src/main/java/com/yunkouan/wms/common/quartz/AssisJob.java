package com.yunkouan.wms.common.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.modules.assistance.service.IAssisService;

@DisallowConcurrentExecution
public class AssisJob extends QuartzJobBean {
	private static Log log = LogFactory.getLog(AssisJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			// 不得自动注入，否则要求IAssisService必须实现序列化
			StrategyContext assisService = SpringContextHolder.getBean(StrategyContext.class);
			IAssisService service = assisService.getStrategy4Assis();
			service.response();
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
	}
}