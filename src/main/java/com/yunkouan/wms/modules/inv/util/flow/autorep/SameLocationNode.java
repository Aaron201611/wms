/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午5:31:09<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.util.flow.autorep;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.service.impl.StockServiceImpl;
import com.yunkouan.wms.modules.inv.util.flow.FlowConstant;
import com.yunkouan.wms.modules.inv.util.flow.FlowContext;
import com.yunkouan.wms.modules.inv.util.flow.FlowNode;
import com.yunkouan.wms.modules.inv.util.flow.autorep.AutoRepStockFlow.AutoRepStockAllot;
import com.yunkouan.wms.modules.inv.vo.InvShiftDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.impl.LocationExtlServiceImpl;
import com.yunkouan.wms.modules.meta.util.LocationUtil;

/**
 * 相同SKU、相同批次货品，按库位空余量从大到小排序自动分配，
 * 若有剩余货品无法分配，则进行下一节点的分配<br/><br/>
 * @version 2017年3月5日 下午5:31:09<br/>
 * @author andy wang<br/>
 */
public class SameLocationNode implements FlowNode {
	
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/** 库位业务类 <br/> add by andy */
	private ILocationExtlService locExtlService;
	/** 库存业务类 <br/> add by andy */
	private IStockService stockService;
	
	/** 货品条件 */
	private MetaSku sku;
	
	/** 移位数量 */
	private double qty;
	
	/** 上下文移位单 */
	private InvShiftVO shiftVo;

	/** 自动分配规则 <br/> add by 王通 */
	private AutoRepStockAllot autoRepAllot;

	private double remindQty;
	
	
	@Override
	public Boolean executeBefore(FlowContext fc) throws Exception {
		log.error("进入规则1");
		this.stockService = (IStockService) fc.getBean(StockServiceImpl.class);
		this.locExtlService = (ILocationExtlService) fc.getBean(LocationExtlServiceImpl.class);
		this.shiftVo = (InvShiftVO) fc.get(FlowConstant.AUTOREP_MAP_SHIFT);
		this.sku = (MetaSku) fc.get(FlowConstant.AUTOREP_MAP_SKU);
		this.qty = (Double) fc.get(FlowConstant.AUTOREP_MAP_QTY);
		this.autoRepAllot = (AutoRepStockAllot) fc.get(FlowConstant.AUTOREP_AUTOALLOT);
		return true;
	}

	@Override
	public Boolean execute(FlowContext fc) throws Exception {
		String skuId = sku.getSkuId();
		// 查询当货品在存储区的库存
    	InvStockVO outStockVo = new InvStockVO();
		InvStock outStock = new InvStock();
		outStockVo.setInvStock(outStock);
		outStock.setSkuId(skuId);
		List<Integer> locationTypeList = new ArrayList<Integer>();
      	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
      	outStockVo.setLocationTypeList(locationTypeList);
      	outStockVo.setResultOrder("t1.batch_no asc, t1.in_date asc");
      	List<InvStock> stockList = stockService.list(outStockVo);
  		if (stockList == null || stockList.size() == 0) return false;
  		remindQty = qty;
		for (InvStock stock : stockList) {
			if (remindQty <= 0) {
				// 所需移动库存已经分配完毕
				break;
			}
      		double stockQty = stock.getStockQty();
      		String batchNo = stock.getBatchNo();
      		//如果库存大于所需数量，则取所需数量，如果小于则取全部
      		if (stockQty < remindQty) {
    			remindQty -= stockQty;
      		} else {
      			stockQty = remindQty;
      			remindQty = 0d;
      		}
      		
      		InvStockVO inStockVo = new InvStockVO();
			InvStock pInStock = new InvStock();
			inStockVo.setInvStock(pInStock);
			pInStock.setSkuId(sku.getSkuId());
			List<Integer> inlocationTypeList = new ArrayList<Integer>();
			inlocationTypeList.add(Constant.LOCATION_TYPE_PICKUP);
			inStockVo.setLocationTypeList(inlocationTypeList);
			inStockVo.setResultOrder("t1.batch_no asc, t1.in_date asc");
          	List<InvStock> inStockList = stockService.list(inStockVo);
      		if (inStockList == null || inStockList.size() == 0) return true;
    		for (InvStock inStock : inStockList) {
    			if (stockQty <= 0) {
    				break;
    			}
    			MetaLocation location = this.locExtlService.findLocById(inStock.getLocationId());

				double shiftQty = this.autoRepAllot.execute(location, sku, stockQty);
	
				//根据库存，选择出库库位和数量
				InvShiftDetailVO shiftDetailVo = new InvShiftDetailVO();
	      		InvShiftDetail shiftDetail = new InvShiftDetail();
	      		shiftDetail.setShiftDetailId(UUID.randomUUID().toString());
	      		shiftDetail.setShiftId(shiftVo.getInvShift().getShiftId());
	      		shiftDetailVo.setInvShiftDetail(shiftDetail);
	      		shiftDetail.setOutLocation(inStock.getLocationId());
	      		shiftDetail.setSkuId(skuId);
	      		shiftDetail.setBatchNo(batchNo);
				shiftDetail.setShiftQty(shiftQty);
				shiftDetail.setInLocation(location.getLocationId());
				
				shiftVo.getListInvShiftDetailVO().add(shiftDetailVo);
				
				stockQty -= shiftQty;
				log.error("规则0 -> 货品" + sku.getSkuName() + " , 批次：" + batchNo + "，分配库位" + location.getLocationName());
    		}
			//如果分配完成还有剩余数量，则加上剩余数量
			remindQty += stockQty;
			//继续下一个库存的分配
		}
		return true;
	}
	
	@Override
	public Boolean executeAfter(FlowContext fc1) throws Exception {
		// 验证是否全部分配完毕
		if (remindQty > 0) {
			log.error("规则同库存库位，未能完全分配");
			shiftVo.remindQty = remindQty;
			return true;
		}
		log.error("规则同库存库位，分配完毕");
		return false;
	}
	
	/**
	 * 根据库容进行由大到小排序，并且去掉重复项<br/>
	 * —— 使用归并排序，进行排序
	 * @param listLoc 库位集合
	 * @return 排序后的库位集合
	 * @throws Exception
	 * @version 2017年3月6日 下午5:44:13<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("unused")
	private List<MetaLocation> orderAndPutOff ( List<MetaLocation> listLoc ) throws Exception {
		listLoc.sort(new Comparator<MetaLocation>() {
			@Override
			public int compare(MetaLocation o1, MetaLocation o2) {
				int result = 0;
				BigDecimal sc1 = LocationUtil.surplusCapacity(o1);
				BigDecimal sc2 = LocationUtil.surplusCapacity(o2);
				if ( sc1.compareTo(sc2) == 0 ) {
					result = 0;
				} else if ( sc1.compareTo(sc2) < 0 ) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
		return listLoc;
	}
}