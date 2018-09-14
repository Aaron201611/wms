package com.yunkouan.wms.modules.send.service;

import java.util.List;

import com.yunkouan.wms.modules.send.vo.SendDeliveryLogVo;

public interface ISendDeliveryLogService {

	/**
	 * 新增
	 * @param logVo
	 * @return
	 */
	public SendDeliveryLogVo add(SendDeliveryLogVo logVo);
	
	/**
	 * 查询操作明细列表
	 * @param paramVo
	 * @return
	 */
	public List<SendDeliveryLogVo> qryLogList(SendDeliveryLogVo paramVo) throws Exception;
	
	/**
	 * 新增发货单操作明细
	 * @param deliveryId
	 * @param operer
	 * @param logType
	 * @param orgId
	 * @param warehouseId
	 */
	public void addNewDeliveryLog(String deliveryId,String operer,String logType,String orgId,String warehouseId);
}
