package com.yunkouan.wms.modules.send.service.impl;

import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.modules.send.service.SplitStrategy;

public class QuanSplitStrategyImpl implements SplitStrategy{

	/**
	 * 按实际数量计算
	 */
	public double calcQty(double oriQty, double splitQty) {
		return oriQty - splitQty;
	}

	/**
	 * 按实际重量计算
	 */
	public double calcWeigh(double oriWeigh, double splitWeigh, double rate) {
		return NumberUtil.sub(oriWeigh,splitWeigh);
	}

	/**
	 * 按实际体积计算
	 */
	public double calcVolume(double oriVolume, double splitVolume, double rate) {
		return NumberUtil.sub(oriVolume,splitVolume);
	}

}
