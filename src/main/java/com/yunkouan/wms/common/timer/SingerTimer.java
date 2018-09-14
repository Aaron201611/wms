package com.yunkouan.wms.common.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 封装了JDK自带的Timer类，增加功能：同时只有一个定时任务运行，可以重复启动和停止
 * @author tphe06
 */
public class SingerTimer {
	private static Log log = LogFactory.getLog(SingerTimer.class);

	private Timer timer;
	private Boolean running = false;

	public boolean start(TimerTask task, Date delay, long period) {
		synchronized(running) {
			if(running) return false;
			if(log.isDebugEnabled()) log.debug("启动定时任务...");
			timer = new Timer();
			timer.scheduleAtFixedRate(task, delay, period);
			running = true;
			return true;
		}
	}
	public boolean start(TimerTask task, long delay, long period) {
		synchronized(running) {
			if(running) return false;
			if(log.isDebugEnabled()) log.debug("启动定时任务...");
			timer = new Timer();
			timer.scheduleAtFixedRate(task, delay, period);
			running = true;
			return true;
		}
	}

	public void stop() {
		synchronized(running) {
			if(timer != null) timer.cancel();
			if(!running) return;
			if(log.isDebugEnabled()) log.debug("取消定时任务...");
			running = false;
		}
	}
}