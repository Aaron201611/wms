package com.yunkouan.wms.modules.send.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.yunkouan.wms.modules.send.service.SplitStrategy;

public class SplitContext{
	
	private SplitStrategy splitStrategy;
	
	public static final String STRATEGE_RATE = "rate";
	
	public static final String STRATEGE_QUAN = "quan";
	
	private DecimalFormat df = new DecimalFormat("#0.00");
	
	public SplitContext(SplitStrategy splitStrategy){
		this.splitStrategy = splitStrategy;
	}
	
	public static SplitContext createInstance(String type){
		SplitStrategy strategy = null;
		if(STRATEGE_RATE.equals(type)){
			strategy = new RateSplitStrategyImpl();
		}
		else if(STRATEGE_QUAN.equals(type)){
			strategy = new QuanSplitStrategyImpl();
		}
		SplitContext instance = new SplitContext(strategy);
		
		return instance;
	}
	
	public double quoteQty(double oriQty,double splitQty){
		return splitStrategy.calcQty(oriQty, splitQty);
	}
	
	public double quoteWeigh(double oriWeigh, double splitWeigh, double rate) throws Exception{
		return splitStrategy.calcWeigh(oriWeigh, splitWeigh, rate);
	}
	
	public double quoteVolume(double oriVolume, double splitVolume, double rate) throws Exception{
		return splitStrategy.calcVolume(oriVolume, splitVolume, rate);
	}
	
	public double round(double b){
		return Double.parseDouble(df.format(b));
	}
	
}
