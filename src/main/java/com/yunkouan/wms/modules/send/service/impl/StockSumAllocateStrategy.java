package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.service.AllocateStrategy;

/**
 * @function 实际为库位分配功能，未直接含【按数量由少到多分配】功能【备份，未使用】
 * @author tphe06
 */
public class StockSumAllocateStrategy implements AllocateStrategy {
	private static Log log = LogFactory.getLog(StockSumAllocateStrategy.class);

	/**amount:剩余分配数量**/
	private double amount = 0;
	/**weight:剩余分配重量**/
	private double weight = 0;
	/**volume:剩余分配体积**/
	private double volume = 0;

	@Override
	public List<SendPickLocation> allocate(List<InvStock> stockList, SendPickDetail pickDetail) {
		throw new BizException("the_strategy_is_no_used");
	}

	/** 
	 * 分配库位
	 * @param hasBatchNoStockList 有批次的库存列表
	 * @param noBatchNoStockList 没有批次的库存列表
	 * @param pickDetail 拣货货品明细
	 * @param hasBatchNo 是否有批次
	 */
	@Override
	public List<SendPickLocation> allocate_new(List<InvStock> hasBatchNoStockList, List<InvStock> noBatchNoStockList,
			SendPickDetail pickDetail, Boolean hasBatchNo) throws Exception {
		List<SendPickLocation> locations = new ArrayList<SendPickLocation>();
		this.amount = pickDetail.getOrderQty();
		this.weight = pickDetail.getOrderWeight();
		this.volume = pickDetail.getOrderVolume();
		//没有批次的，先查找没有批次的库存列表。若没批次的库存不足，则再找有批次的库存列表
		if(!hasBatchNo && noBatchNoStockList != null) {
			for (InvStock invStock : noBatchNoStockList) {
				if(amount <= 0) break;
				//可用库存数量
				double inQty = invStock.getStockQty().doubleValue() - invStock.getPickQty().doubleValue();
				if(inQty == 0) continue;
				SendPickLocation pl = findPickLocation(inQty, invStock, pickDetail);
				if(log.isDebugEnabled()) log.debug("分配库位："+pl.getLocationId()+"，分配数量："+pl.getPickQty());
				locations.add(pl);
				//剩余数量 = 拣货数量- 可用库存数量
				amount -= inQty;			
			}
		}
		//有批次的，或者没有批次的，都在有批次的库存列表中分配
		for (InvStock invStock : hasBatchNoStockList) {
			if(amount <= 0) break;
			//可用库存数量
			double inQty = invStock.getStockQty().doubleValue() - invStock.getPickQty().doubleValue();
			if(inQty == 0) continue;
			//若有批次号，则skuid和批次号必须与invStock的skuid和批次号相同
			if(hasBatchNo && (!invStock.getSkuId().equals(pickDetail.getSkuId()) || !invStock.getBatchNo().equals(pickDetail.getBatchNo()))) continue;
			SendPickLocation pl = findPickLocation(inQty, invStock, pickDetail);
			if(log.isDebugEnabled()) log.debug("分配库位："+pl.getLocationId()+"，分配数量："+pl.getPickQty());
			locations.add(pl);
			//剩余数量 = 拣货数量- 可用库存数量
			amount -= inQty;
		}
		//若还有拣货数量没有分配，则库存不足
		if(amount > 0){
			throw new BizException(BizStatus.PICK_INVSTOCK_NOT_ENOUGH.getReasonPhrase());
		}
		return locations;
	}

	/**
	 * 把库位VO转化成分配库位VO
	 * @param inQty 待分配数量
	 * @param invStock 货品库存
	 * @param pickDetail 待拣货货品明细
	 * @return
	 * @throws Exception
	 */
	public SendPickLocation findPickLocation(double inQty, InvStock invStock, SendPickDetail pickDetail) throws Exception{
		SendPickLocation pl = new SendPickLocation();					
		if(amount <= inQty){
			pl.setPickQty(amount);
			pl.setPickWeight(weight);
			pl.setPickVolume(volume);
			invStock.setPickQty(invStock.getPickQty()+amount);
		}else{
			pl.setPickQty(inQty);
			double rate = (double)inQty/amount;
			double weight_r = NumberUtil.mul(pickDetail.getOrderWeight(), rate, 2);
			double volume_r = NumberUtil.mul(pickDetail.getOrderVolume(),rate,6);
			pl.setPickWeight(weight_r);
			pl.setPickVolume(volume_r);	
			weight = NumberUtil.sub(weight, weight_r);
			volume = NumberUtil.sub(volume,volume_r);
			invStock.setPickQty(invStock.getStockQty());
		}
		pl.setLocationId(invStock.getLocationId());
		pl.setBatchNo(invStock.getBatchNo());
		return pl;
	}

	@Override
	public List<SendPickLocation> allocate_new_wave(List<InvStock> hasBatchNoStockList,
			List<InvStock> noBatchNoStockList, SendPickDetail pickDetail, Boolean hasBatchNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}