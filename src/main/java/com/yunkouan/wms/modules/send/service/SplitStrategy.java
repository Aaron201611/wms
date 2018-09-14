package com.yunkouan.wms.modules.send.service;

public interface SplitStrategy {

	public double calcQty(double oriQty,double splitQty);
	
	public double calcWeigh(double oriWeigh,double splitWeigh,double rate) throws Exception;
	
	public double calcVolume(double oriVolume,double splitVolume,double rate) throws Exception;
}
