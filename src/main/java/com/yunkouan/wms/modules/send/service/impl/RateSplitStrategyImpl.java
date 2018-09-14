package com.yunkouan.wms.modules.send.service.impl;

import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.modules.send.service.SplitStrategy;

public class RateSplitStrategyImpl implements SplitStrategy{

	/**
	 * 按比例拆分数量
	 */
	public double calcQty(double oriQty, double splitQty) {
		
		return oriQty - splitQty;
	}

    /**
     * 按比例拆分重量
     */
	public double calcWeigh(double oriWeigh, double splitWeigh, double rate) throws Exception{
		
		return NumberUtil.mul(oriWeigh,rate,2);
	}

	/**
	 * 按比例拆分体积
	 */
	public double calcVolume(double oriVolume, double splitVolume, double rate) throws Exception{
		
		return NumberUtil.mul(oriVolume,rate,6);
	}
	
	

}
