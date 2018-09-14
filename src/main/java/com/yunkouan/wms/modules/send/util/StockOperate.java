package com.yunkouan.wms.modules.send.util;

import java.util.List;

import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;



public class StockOperate{
	
	private InvStockVO invStockVo;
	
	
	/**
	 * 库存对象
	 * @param skuId
	 * @param batchNo
	 * @param owner
	 * @param locationId
	 * @param num
	 * @param inTemp
	 * @return
	 */
	public static InvStockVO getInvStockVO(String skuId,String batchNo,String owner,String locationId,String asnDetailId,Double num,Boolean inTemp,Boolean containBatch,List<Integer> locationTypes){
		InvStockVO skuVo = new InvStockVO();
		InvStock stock = new InvStock();
		stock.setIsBlock(Constant.STOCK_BLOCK_FALSE);//非冻结库存
		stock.setSkuId(skuId);
		if(!StringUtil.isTrimEmpty(asnDetailId)){
			stock.setAsnDetailId(asnDetailId);
			skuVo.setContainTemp(true);
			skuVo.setOnlyTemp(false);
		}else{
			skuVo.setContainTemp(inTemp);
		}
		//若批次号为空，则查询该货品所有库存列表
		//库存查询增加“是否有批次”查询条件，对应字段为“containBatch”，
		//使用举例：若需查询所有该货品库存，批次号填空，containBatch=false；若需查询无批次库存，批次号为空，containBatch=true
		skuVo.setContainBatch(containBatch);
		stock.setBatchNo(batchNo);
		stock.setOwner(owner);
		stock.setLocationId(locationId);
		skuVo.setFindNum(num);			
		skuVo.setInvStock(stock);
		if(locationTypes !=null && locationTypes.size()>0){
			skuVo.setLocationTypeList(locationTypes);
		}
		
		return skuVo; 
		
	}
	
	/**
	 * 库存日志
	 * @param skuId
	 * @param batchNo
	 * @param locationId
	 * @param pickQty
	 * @param userId
	 * @param id
	 * @return
	 */
	public static InvLog getInvLogVo(String skuId,String batchNo,String locationId,Double pickQty,String userId,String id){
		//库存日志
		InvLog log = new InvLog();
		log.setBatchNo(batchNo);
		log.setOpPerson(userId);
		log.setSkuId(skuId);
		log.setInvoiceBill(id);
		log.setLocationId(locationId);
		log.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
		log.setLogType(Constant.STOCK_LOG_TYPE_PICKING);
		log.setQty(pickQty);
		
		return log;
	}
	
}
