package com.yunkouan.wms.modules.intf.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.inv.service.IInvLogService;

/**
 * 库存日志备份任务的执行
 * @author tphe06
 */
@DisallowConcurrentExecution
public class InvLogBackupTask extends QuartzJobBean {
	private static Log log = LogFactory.getLog(InvLogBackupTask.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(log.isDebugEnabled()) log.debug("运行【库存日志备份】定时任务...");
		try {
			IInvLogService service = SpringContextHolder.getBean(IInvLogService.class);
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			String outDays = paramService.getKey(CacheName.PARAM_TIMER_LOG_OUTTIME);
			service.backup(Integer.parseInt(outDays));
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
		if(log.isDebugEnabled()) log.debug("【库存日志备份】定时任务完成.");
	}
}