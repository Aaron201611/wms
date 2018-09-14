package com.yunkouan.wms.modules.intf.timer;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.timer.SingerTimer;

/**
 * 系统定时任务启动入口，所有定时任务都由这里启动
 * @author tphe06
 */
public class SystemTimer {
	private static Log log = LogFactory.getLog(SystemTimer.class);
	private SingerTimer stockTimer = new SingerTimer();
	private SingerTimer invLogTimer = new SingerTimer();

	public SystemTimer() throws DaoException, ServiceException, ParseException {
		if(log.isDebugEnabled()) log.debug("正在构建【定时器】实例...");
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String invLogRunTime = paramService.getKey(CacheName.PARAM_TIMER_LOG_RUNTIME);
		String stockPushTime = paramService.getKey(CacheName.PARAM_MSMQ_STOCK_PUSHTIME);
		// 推送给华东信息库存定时任务
//		stockTimer.start(new QueryStockTask(), getTimeHour(stockPushTime, 24*60*60*1000, "03:00:00"), 24*60*60*1000);
//		new QueryStockTask().run();
		// 库存日志备份定时任务
//		invLogTimer.start(new InvLogBackupTask(), getTimeHour(invLogRunTime, 24*60*60*1000, "22:00:00"), 24*60*60*1000);
//		new InvLogBackupTask().run();
		if(log.isDebugEnabled()) log.debug("构建【定时器】实例完成.");
	}

	/**
	 * 得到大于当前时间的最近执行时间
	 * @param time 下次执行时间（格式：03:00:00）
	 * @param period 执行时间间隔，以毫秒为单位，并且必须大于1小时
	 * @return
	 * @throws ParseException 
	 */
	public static Date getTimeHour(String time, long period, String def) throws ParseException {
		Date dt_today = new Date();
		String str_today  = DateFormatUtils.format(dt_today, "yyyy-MM-dd");//yyyyMMddHHmmss
		try {
			Time.valueOf(time);
		}catch(Exception e){
			log.error(e.getMessage());
			time = def;
		}
		String collate_time = str_today+" "+time;
		String[] format = new String[]{"yyyy-MM-dd HH:mm:ss"};
		Date firstTime = DateUtils.parseDate(collate_time, format);
		for(;sub(new Date(), firstTime) > 0;) {
			firstTime = DateUtils.addHours(firstTime, (int)(period/3600000));
		}
		if(log.isInfoEnabled()) log.info("请检查下次操作时间是否正确！"+DateFormatUtils.format(firstTime, "yyyy-MM-dd HH:mm:ss"));
		return firstTime;
	}

	public static long sub(Date d1, Date d2) {
		return d1.getTime()-d2.getTime();
	}
}