package com.yunkouan.wms.modules.application.util;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class ApplicationStringUtil {

	public static String parseNum(Object num){
		String s = num == null ? "0":num+"";
		return s;
	}
	
	public static String parseDate(Date date){
		String s = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		return s;
	}
}
