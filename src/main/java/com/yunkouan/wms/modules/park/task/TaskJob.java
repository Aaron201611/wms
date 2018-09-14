package com.yunkouan.wms.modules.park.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.modules.park.service.IParkRentService;

@Service
public class TaskJob {
	
	@Autowired
	private IParkRentService parkRentService;

	public void CheckRentJob() throws Exception{
		System.out.println("任务进行中。。。");
		parkRentService.warn();
	}
}
