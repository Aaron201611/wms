package com.yunkouan.wms.modules.send.service;

import java.util.List;

import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;

/**
 * 库存策略
 *@Description TODO
 *@author Aaron
 *@date 2017年2月21日 上午10:59:17
 *version v1.0
 */
public interface AllocateStrategy {
	
	/**
	 * 拣货分配库位
	 * @param skuId
	 * @param batchNo
	 * @param num
	 * @return
	 * @version 2017年2月21日 上午11:31:13<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickLocation> allocate(List<InvStock> stockList,SendPickDetail pickDetail);
	/**
	 * 按fifo分配库位
	 * @param skuId
	 * @param batchNo
	 * @param num
	 * @return
	 * @version 2017年2月21日 上午11:31:13<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickLocation> allocate_new(List<InvStock> hasBatchNoStockList,List<InvStock> noBatchNoStockList,SendPickDetail pickDetail,Boolean hasBatchNo)throws Exception;
	/**
	 * 按fifo分配库位
	 * @param skuId
	 * @param batchNo
	 * @param num
	 * @return
	 * @version 2018年9月5日 上午11:31:13<br/>
	 * @author ZWB<br/>
	 */
	public List<SendPickLocation> allocate_new_wave(List<InvStock> hasBatchNoStockList,List<InvStock> noBatchNoStockList,SendPickDetail pickDetail,Boolean hasBatchNo)throws Exception;
}
