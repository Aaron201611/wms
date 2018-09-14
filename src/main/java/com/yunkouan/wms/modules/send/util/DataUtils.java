package com.yunkouan.wms.modules.send.util;

import java.text.DecimalFormat;

public class DataUtils {

	private static DecimalFormat DF = new DecimalFormat("#0.00");
	
	public static double round(double b){
		return Double.parseDouble(DF.format(b));
	}
}
