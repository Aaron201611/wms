package com.yunkouan.wms.modules.send.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolUtils {

	private static final int max_num = 30;
	
	private ExecutorService pool = Executors.newFixedThreadPool(max_num);
	
	private int activeTreadCount = 0;
	
	private static ThreadPoolUtils instance;
	
	public static final ThreadPoolUtils getInstance(){
		if(instance == null){
			synchronized (ThreadPoolUtils.class) {
				if(instance == null){
					instance = new ThreadPoolUtils();
				}
			}
		}
		return instance;
	}
	
	public void addThreadItem(Thread thread){
		if(pool == null || pool.isShutdown()){
			pool = Executors.newFixedThreadPool(max_num);
		}
		pool.execute(thread);
		this.activeTreadCount = ((ThreadPoolExecutor)pool).getActiveCount();
	}
	
	public void shutDown(){
		pool.shutdown();
	}
	
	public int getCupNum(){
		return Runtime.getRuntime().availableProcessors();
	}
	
	public int getActiveTreadCount(){
		this.activeTreadCount = ((ThreadPoolExecutor)pool).getActiveCount();
		return this.activeTreadCount;
				
	}
}
