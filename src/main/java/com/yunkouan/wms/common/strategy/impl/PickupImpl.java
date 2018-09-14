package com.yunkouan.wms.common.strategy.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.strategy.IPickupRule;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.service.AllocateStrategy;
import com.yunkouan.wms.modules.send.service.impl.FifoAallocateStrategy;
import com.yunkouan.wms.modules.send.util.StockOperate;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

/**
 * @function 拣货规则的实现
 * @author tphe06
 */
@Service(value="pickupRule")
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class PickupImpl extends IPickupRule {
	@Autowired
	private ILocationExtlService locExtlService;

	@Autowired
	public IStockService stockService;//库存服务

	@Override
	public SendPickVo allocation_new(SendPickVo pickVo, String operator) throws Exception {
		//sendPickVo 专门捕获用于异常，如果 excptions 不为空则返回该对象
		SendPickVo sendPickVo=new SendPickVo();
		List<BizException>exceptions=new ArrayList<>();
		sendPickVo.setExceptions(exceptions);
		
		if(pickVo.getSendPickDetailVoList().isEmpty()) return null;
		List<SendPickDetailVo> pickDetailVoList = pickVo.getSendPickDetailVoList();
		//保存有批次号的sku拣货明细列表
		Map<String,List<SendPickDetailVo>> hasBatchNoMap = new HashMap<String,List<SendPickDetailVo>>();
		//若sku没有批次号，则存放在此map中
		Map<String,List<SendPickDetailVo>> noBatchNoMap = new HashMap<String,List<SendPickDetailVo>>();

		for(SendPickDetailVo pickDetailVo:pickDetailVoList){
			pickDetailVo.setPlanPickLocations(new ArrayList<SendPickLocationVo>());
			List<SendPickDetailVo> pdVoList = new ArrayList<SendPickDetailVo>();
			//保存在缺少批次号的Map中
			if(noBatchNoMap.containsKey(pickDetailVo.getSendPickDetail().getSkuId())){
				pdVoList = noBatchNoMap.get(pickDetailVo.getSendPickDetail().getSkuId());
				if(StringUtil.isNotEmpty(pickDetailVo.getSendPickDetail().getBatchNo())){
					pdVoList.add(0,pickDetailVo);
				}else{
					pdVoList.add(pickDetailVo);
				}
				continue;
			}
			//保存在有批次号的map中
			if(hasBatchNoMap.containsKey(pickDetailVo.getSendPickDetail().getSkuId())){
				pdVoList = hasBatchNoMap.get(pickDetailVo.getSendPickDetail().getSkuId());					
			}
			pdVoList.add(pickDetailVo);
			if(StringUtil.isNotEmpty(pickDetailVo.getSendPickDetail().getBatchNo())){			
				hasBatchNoMap.put(pickDetailVo.getSendPickDetail().getSkuId(), pdVoList);
			}else{
				noBatchNoMap.put(pickDetailVo.getSendPickDetail().getSkuId(), pdVoList);
				hasBatchNoMap.remove(pickDetailVo.getSendPickDetail().getSkuId());
			}
		}
		/**
		 * 按库存数量先少后多分配
		 */
		String order = "t1.batch_no ASC ,(t1.stock_qty-t1.pick_qty) ASC";
		//hasBatchNoMap转换成Map<skuId,Map<batchNo,List<SendPickDetailVo>>>
		Map<String,Map<String,List<SendPickDetailVo>>> skuMap = new HashMap<String,Map<String,List<SendPickDetailVo>>>();
		if(!hasBatchNoMap.isEmpty()){
			//转换成Map<skuId,Map<batchNo,List<SendPickDetailVo>>>
			for(String skuId:hasBatchNoMap.keySet()){
				List<SendPickDetailVo> pickDetailList = hasBatchNoMap.get(skuId);
				Map<String,List<SendPickDetailVo>> batchNoMap = pickDetailList.stream().collect(Collectors.groupingBy(t->t.getSendPickDetail().getBatchNo()));
				skuMap.put(skuId, batchNoMap);
			}
			//根据skuid，batchNo查询库存列表
			for(String skuId:skuMap.keySet()){
				Map<String,List<SendPickDetailVo>> batchNoMap = skuMap.get(skuId);
				for(String batchNo:batchNoMap.keySet()){
					List<Integer> locationTypes = new ArrayList<Integer>();
					//					locationTypes.add(Constant.LOCATION_TYPE_STORAGE);
					locationTypes.add(Constant.LOCATION_TYPE_PICKUP);
					InvStockVO stockVo = StockOperate.getInvStockVO(skuId,batchNo,null,null,null,null,false,true,locationTypes);
					stockVo.setResultOrder(order);
					//获取货品的库存列表
					List<InvStock> stockList = stockService.list(stockVo);
					//设定fifo规则
					AllocateStrategy allocateStrategy = new FifoAallocateStrategy();
					//按fifo规则分配库位，获取库位与批次号，生成拣货计划库位明细列表
					List<SendPickDetailVo> list = batchNoMap.get(batchNo);
					for(SendPickDetailVo pickDevailVo:list){
						try {
							List<SendPickLocation> locations = allocateStrategy.allocate_new_wave(stockList,null,pickDevailVo.getSendPickDetail(),true);
							for(SendPickLocation t:locations){
								SendPickLocationVo pickLocationVo = new SendPickLocationVo();
								pickLocationVo.setSendPickLocation(t);
								FqDataUtils fdu = new FqDataUtils();
								//获取库位名称
								pickLocationVo.setLocationComment(fdu.getLocNameById(locExtlService, t.getLocationId()));
								pickDevailVo.getPlanPickLocations().add(pickLocationVo);
							}
						} catch (BizException e) {
							sendPickVo.getExceptions().add(e);
						}
						
					}										
				}
			}
		}		
		//根据skuid查询所有库存
		if(!noBatchNoMap.isEmpty()){
			for(String skuId:noBatchNoMap.keySet()){
				//modify by 王通 2017年9月8日15:06:00
				//				InvStockVO stockVo = StockOperate.getInvStockVO(skuId,null,null,null,null,null,false,false,Arrays.asList(Constant.LOCATION_TYPE_STORAGE));
				//				InvStockVO stockVo = StockOperate.getInvStockVO(skuId,null,null,null,null,null,false,false,Arrays.asList(Constant.LOCATION_TYPE_STORAGE, Constant.LOCATION_TYPE_PICKUP));
				InvStockVO stockVo = StockOperate.getInvStockVO(skuId,null,null,null,null,null,false,false,Arrays.asList(Constant.LOCATION_TYPE_PICKUP));
				stockVo.setResultOrder(order);
				//获取货品的库存列表
				List<InvStock> stockList = stockService.list(stockVo);
				//拆分成有批次的库存列表与没批次的库存列表
				List<InvStock> hasBatchNoStockList = new ArrayList<InvStock>();
				List<InvStock> noBatchNoStockList = new ArrayList<InvStock>();
				for(InvStock stock:stockList){
					if(StringUtil.isTrimEmpty(stock.getBatchNo())){
						noBatchNoStockList.add(stock);
					}else{
						hasBatchNoStockList.add(stock);
					}
				}
				//设定fifo规则
				AllocateStrategy allocateStrategy = new FifoAallocateStrategy();
				//按fifo规则分配库位，获取库位与批次号，生成拣货计划库位明细列表
				List<SendPickDetailVo> list = noBatchNoMap.get(skuId);
				for(SendPickDetailVo pickDevailVo:list){
					Boolean hasBatchNo = StringUtils.isNotEmpty(pickDevailVo.getSendPickDetail().getBatchNo());
					try {
						List<SendPickLocation> locations = allocateStrategy.allocate_new_wave(hasBatchNoStockList,noBatchNoStockList,pickDevailVo.getSendPickDetail(),hasBatchNo);


						for(SendPickLocation t:locations){
							SendPickLocationVo pickLocationVo = new SendPickLocationVo();
							pickLocationVo.setSendPickLocation(t);
							FqDataUtils fdu = new FqDataUtils();
							//获取库位名称
							pickLocationVo.setLocationComment(fdu.getLocNameById(locExtlService, t.getLocationId()));
							pickDevailVo.getPlanPickLocations().add(pickLocationVo);
						};
					} catch (BizException e) {
						sendPickVo.getExceptions().add(e);
					}
				}
			}
		}
		if(sendPickVo.getExceptions()!=null&&sendPickVo.getExceptions().size()>0){
			return sendPickVo;
		}
		return pickVo;
	}

}