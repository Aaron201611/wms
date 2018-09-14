package com.yunkouan.wms.modules.send.util;

import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;

public class ExceptionLogUtil {
	
	public static ExceptionLog createInstance(int excType,String involveBill,int excStatus,int excLevel,String excDesc){
		ExceptionLog exceptionLog = new ExceptionLog();
		exceptionLog.setExType(excType);
		exceptionLog.setInvolveBill(involveBill);
		exceptionLog.setExStatus(excStatus);
		exceptionLog.setExDesc(excDesc);
		exceptionLog.setExLevel(excLevel);
		return exceptionLog;
	}

}
