package com.yunkouan.wms.modules.send.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.service.AllocateStrategy;
import com.yunkouan.wms.modules.send.util.DataUtils;

/**
 * @function 实际为库位分配功能，未直接含【按数量由少到多分配】功能，更不是FIFO
 * @author tphe06
 */
public class FifoAallocateStrategy implements AllocateStrategy {
	
	private double amount = 0d;
	
	private double weight = 0d;
	
	private double volume = 0d;
	
	

	/**
	 * 按fifo分配库位
	 * @param skuId
	 * @param batchNo
	 * @param num
	 * @return
	 * @version 2017年2月21日 上午11:31:13<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickLocation> allocate(List<InvStock> stockList,SendPickDetail pickDetail) {
		
		//排除收货区暂存库位库存，对正常库位库存按先进先出FIFO处理。
		//1、对库存列表进行排序，先按库存日期先后排序；
		//2、库存日期相同，按库存数量大小排序；
		stockList = sortByFifo(stockList);
		List<SendPickLocation> locations = new ArrayList<SendPickLocation>();
		double amount = pickDetail.getOrderQty();
		double weight = pickDetail.getOrderWeight();
		double volume = pickDetail.getOrderVolume();
		//排序以后，按数量分配库位
		for (InvStock invStock : stockList) {
			if(amount <= 0) break;
			SendPickLocation pl = new SendPickLocation();
			//可用库存数量
			double inQty = invStock.getStockQty().doubleValue() - invStock.getPickQty().doubleValue();
			if(amount <= inQty){
				pl.setPickQty(amount);
				pl.setPickWeight(weight);
				pl.setPickVolume(volume);				
			}else{
				pl.setPickQty(inQty);
				double rate = inQty/amount;
				double weight_r = DataUtils.round(pickDetail.getOrderWeight()*rate);
				double volume_r = DataUtils.round(pickDetail.getOrderVolume()*rate);
				pl.setPickWeight(weight_r);
				pl.setPickVolume(volume_r);	
				weight -= weight_r;
				volume -= volume_r;
			}
			pl.setLocationId(invStock.getLocationId());
			pl.setBatchNo(invStock.getBatchNo());
			locations.add(pl);
			//剩余数量 = 拣货数量- 可用库存数量
			amount -= inQty;
		}	
		return locations;
	}
	
	
	/**
	 * 分配库位
	 * @param skuId
	 * @param batchNo
	 * @param num
	 * @return
	 * @version 2017年2月21日 上午11:31:13<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickLocation> allocate_new(List<InvStock> hasBatchNoStockList,List<InvStock> noBatchNoStockList,SendPickDetail pickDetail,Boolean hasBatchNo)throws Exception{
		List<SendPickLocation> locations = new ArrayList<SendPickLocation>();
		this.amount = pickDetail.getOrderQty();
		this.weight = pickDetail.getOrderWeight();
		this.volume = pickDetail.getOrderVolume();
		//没有批次的，先查找没有批次的库存列表。若没批次的库存不足，则再找有批次的库存列表
		if(!hasBatchNo && noBatchNoStockList != null && !noBatchNoStockList.isEmpty()){
			for (InvStock invStock : noBatchNoStockList) {
				if(amount <= 0) break;
				//可用库存数量
				Double inQty = invStock.getStockQty().doubleValue() - invStock.getPickQty().doubleValue();
				if(inQty == 0) continue;
				
				SendPickLocation pl = findPickLocation(inQty,invStock,pickDetail);					
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
			if(hasBatchNo){
				if(!invStock.getSkuId().equals(pickDetail.getSkuId()) || !invStock.getBatchNo().equals(pickDetail.getBatchNo())){
					continue;
				}
			}
			SendPickLocation pl = findPickLocation(inQty,invStock,pickDetail);					
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
	 * @param inQty
	 * @param invStock
	 * @param pickDetail
	 * @return
	 * @throws Exception
	 */
	public SendPickLocation findPickLocation(double inQty,InvStock invStock,SendPickDetail pickDetail) throws Exception{
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
	
	/**
	 * 按fifo排序,相同日期按可用数量大小排序
	 * @param stockList
	 * @return
	 * @version 2017年2月21日 下午2:35:39<br/>
	 * @author Aaron He<br/>
	 */
	private List<InvStock> sortByFifo(List<InvStock> stockList){
		stockList.sort((o1,o2)->{
			if(o1.getInDate().equals(o2.getInDate())){
				Double qty_1 = o1.getStockQty() - o1.getPickQty();
				Double qty_2 = o2.getStockQty() - o2.getPickQty();
				return (qty_2.compareTo(qty_1));
			}else{
				return o1.getInDate().compareTo(o2.getInDate());
			}
		});
			
		return stockList;
	}
	
	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}


	public static void main(String[] args) throws ParseException {
//		Map<Integer,String> map = new TreeMap<Integer,String>();
//		map.put(12, "12");
//		map.put(15, "15");
//		map.put(11, "11");
//		map.put(16, "16");
//		map.put(18, "18");
//		Date dd1 = DateUtils.parseDate("2017-02-01", "yyyy-MM-dd");
//		Date dd2 = DateUtils.parseDate("2017-02-04", "yyyy-MM-dd");
//		Date dd3 = DateUtils.parseDate("2017-02-02", "yyyy-MM-dd");
//		Date dd4 = DateUtils.parseDate("2017-02-05", "yyyy-MM-dd");
//		
//		List<Date> list2 = new ArrayList<Date>();
//		list2.add(dd1);
//		list2.add(dd2);
//		list2.add(dd3);
//		list2.add(dd4);
//		list2.sort((o1,o2)->{
//			return o2.compareTo(o1);
//		});
//		System.out.println(list2);
		
//		int a = 11;
//		int b = 99;
//		double r = (double)a/b;
//		System.out.println(r);
		String s = null;
		System.out.println(s.equals("3"));
	
	}


	public double getAmount() {
		return amount;
	}
	


	public void setAmount(double amount) {
		this.amount = amount;
	}


	@Override
	public List<SendPickLocation> allocate_new_wave(List<InvStock> hasBatchNoStockList,
			List<InvStock> noBatchNoStockList, SendPickDetail pickDetail, Boolean hasBatchNo) throws Exception {
		List<SendPickLocation> locations = new ArrayList<SendPickLocation>();
		Double ableStock=0.0d;//（针对不带批次号的可用库存）
		Double batchableStock=0.0d;//（针对带批次号的可用库存）
		Double needStock=pickDetail.getOrderQty();//（需要拣货库存）
		this.amount = pickDetail.getOrderQty();
		this.weight = pickDetail.getOrderWeight();
		this.volume = pickDetail.getOrderVolume();
		//没有批次的，先查找没有批次的库存列表。若没批次的库存不足，则再找有批次的库存列表
		if(!hasBatchNo && noBatchNoStockList != null && !noBatchNoStockList.isEmpty()){
			for (InvStock invStock : noBatchNoStockList) {
				if(amount <= 0) break;
				//可用库存数量
				Double inQty = invStock.getStockQty().doubleValue() - invStock.getPickQty().doubleValue();
				batchableStock+=inQty;
				if(inQty == 0) continue;
				
				SendPickLocation pl = findPickLocation(inQty,invStock,pickDetail);					
				locations.add(pl);
				//剩余数量 = 拣货数量- 可用库存数量
				amount -= inQty;			
			}
		}
		//有批次的，或者没有批次的，都在有批次的库存列表中分配
		for (InvStock invStock : hasBatchNoStockList) {
			if(amount <= 0) break;
			//可用库存数量
			Double inQty = invStock.getStockQty().doubleValue() - invStock.getPickQty().doubleValue();
			ableStock+=inQty;
			if(inQty == 0) continue;
			//若有批次号，则skuid和批次号必须与invStock的skuid和批次号相同
			if(hasBatchNo){
				if(!invStock.getSkuId().equals(pickDetail.getSkuId()) || !invStock.getBatchNo().equals(pickDetail.getBatchNo())){
					continue;
				}
			}
			SendPickLocation pl = findPickLocation(inQty,invStock,pickDetail);					
			locations.add(pl);
			//剩余数量 = 拣货数量- 可用库存数量
			amount -= inQty;			
		}
		//若还有拣货数量没有分配，则库存不足
		if(amount > 0){
			//BizException params("sku id","批次号"，"实际库存"，"需要库存")
			throw new BizException(BizStatus.PICK_INVSTOCK_NOT_ENOUGH.getReasonPhrase(),pickDetail.getSkuId(),pickDetail.getBatchNo(),ableStock,batchableStock,needStock);
		}
		return locations;
	}
	

	
}
