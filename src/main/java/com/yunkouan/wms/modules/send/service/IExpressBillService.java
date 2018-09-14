package com.yunkouan.wms.modules.send.service;

import com.yunkouan.wms.modules.send.vo.SendExpressBillVo;

/**
 * 发货单服务接口
 */
public interface IExpressBillService {
	
	public void add(SendExpressBillVo expressBillVo) throws Exception;
	
	/**
	 * 生效
	 * @param billId
	 * @throws Exception
	 */
	public void enable(String billId) throws Exception;
	
	/**
	 * 取消
	 * @param billId
	 * @throws Exception
	 */
	public void cancel(String billId) throws Exception;
}