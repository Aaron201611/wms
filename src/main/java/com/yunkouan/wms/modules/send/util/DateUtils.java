package com.yunkouan.wms.modules.send.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Date parse(String dateStr,SimpleDateFormat sdf) throws ParseException{
		Date date = sdf.parse(dateStr);
		return date;
		
	}
}
