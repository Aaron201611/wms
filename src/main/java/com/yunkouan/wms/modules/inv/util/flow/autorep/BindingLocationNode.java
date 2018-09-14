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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
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
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 只查询绑定货品的库位
 * @version 2018年5月28日11:02:05<br/>
 * @author 王通<br/>
 */
public class BindingLocationNode implements FlowNode {
	
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
		log.error("进入规则0");
		this.locExtlService = (ILocationExtlService) fc.getBean(LocationExtlServiceImpl.class);
		this.stockService = (IStockService) fc.getBean(StockServiceImpl.class);
		this.shiftVo = (InvShiftVO) fc.get(FlowConstant.AUTOREP_MAP_SHIFT);
		this.sku = (MetaSku) fc.get(FlowConstant.AUTOREP_MAP_SKU);
		this.qty = (Double) fc.get(FlowConstant.AUTOREP_MAP_QTY);
		this.autoRepAllot = (AutoRepStockAllot) fc.get(FlowConstant.AUTOREP_AUTOALLOT);
		return true;
	}

	@Override
	public Boolean execute(FlowContext fc) throws Exception {
//		Principal loginUser = LoginUtil.getLoginUser();
		String skuId = sku.getSkuId();
		// 查询当货品在存储区的库存
    	InvStockVO stockVo = new InvStockVO();
		InvStock invStock = new InvStock();
		stockVo.setInvStock(invStock);
		invStock.setSkuId(skuId);
		List<Integer> locationTypeList = new ArrayList<Integer>();
      	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
		stockVo.setLocationTypeList(locationTypeList);
      	stockVo.setResultOrder("t1.batch_no asc, t1.in_date asc");
      	List<InvStock> stockList = stockService.list(stockVo);
  		if (stockList == null || stockList.size() == 0) return false;
  		remindQty = qty;
		for (InvStock stock : stockList) {
			if (NumberUtil.compairTo(remindQty,0,6) <= 0) {
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
      		
      		// 查询对应skuId的绑定库位集合
			MetaLocationVO p_location = new MetaLocationVO(new MetaLocation());
			p_location.getLocation().setLocationType(Constant.LOCATION_TYPE_PICKUP);
			p_location.getLocation().setBatchNo(batchNo);
			p_location.getLocation().setSkuId(skuId);
			p_location.getLocation().setOrgId(sku.getOrgId());
			p_location.getLocation().setWarehouseId(sku.getWarehouseId());
			p_location.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
			List<MetaLocation> listLocation = this.locExtlService.listLocByExample(p_location);
			p_location.getLocation().setBatchNo(null);
			p_location.setBatchNoIsNull(true);
			List<MetaLocation> listLocationNoBatch = this.locExtlService.listLocByExample(p_location);
      		
			// 查询对应skuId的库位集合
			if ( PubUtil.isEmpty(listLocation) && PubUtil.isEmpty(listLocationNoBatch)) {
				return true;
			}
			// 遍历库位，选择库容足够的库位，进行匹配
			// 获取货品
			log.error("规则0 -> 货品" + sku.getSkuName() + " , 批次：" + batchNo + "，进行匹配");
			// 分配上架货位
			for (MetaLocation metaLocation : listLocation) {
				if ( stockQty <= 0 ) {
					// 当前库存已经分配完毕
					break;
				}
				double shiftQty = this.autoRepAllot.execute(metaLocation, sku, stockQty);

				//根据库存，选择出库库位和数量
				InvShiftDetailVO shiftDetailVo = new InvShiftDetailVO();
	      		InvShiftDetail shiftDetail = new InvShiftDetail();
	      		shiftDetail.setShiftDetailId(UUID.randomUUID().toString());
	      		shiftDetail.setShiftId(shiftVo.getInvShift().getShiftId());
	      		shiftDetailVo.setInvShiftDetail(shiftDetail);
	      		shiftDetail.setOutLocation(stock.getLocationId());
	      		shiftDetail.setSkuId(skuId);
	      		shiftDetail.setBatchNo(batchNo);
				shiftDetail.setShiftQty(shiftQty);
				shiftDetail.setInLocation(metaLocation.getLocationId());
				
				shiftVo.getListInvShiftDetailVO().add(shiftDetailVo);
				
				stockQty -= shiftQty;
				log.error("规则0 -> 货品" + sku.getSkuName() + " , 批次：" + batchNo + "，分配库位" + metaLocation.getLocationName());
			}
			for (MetaLocation metaLocation : listLocationNoBatch) {
				if ( stockQty <= 0 ) {
					// 当前库存已经分配完毕
					break;
				}
				double shiftQty = this.autoRepAllot.execute(metaLocation, sku, stockQty);
				//根据库存，选择出库库位和数量
				InvShiftDetailVO shiftDetailVo = new InvShiftDetailVO();
	      		InvShiftDetail shiftDetail = new InvShiftDetail();
	      		shiftDetail.setShiftDetailId(UUID.randomUUID().toString());
	      		shiftDetail.setShiftId(shiftVo.getInvShift().getShiftId());
	      		shiftDetailVo.setInvShiftDetail(shiftDetail);
	      		shiftDetail.setOutLocation(stock.getLocationId());
	      		shiftDetail.setSkuId(skuId);
	      		shiftDetail.setBatchNo(batchNo);
				shiftDetail.setShiftQty(shiftQty);
				shiftDetail.setInLocation(metaLocation.getLocationId());
				
				shiftVo.getListInvShiftDetailVO().add(shiftDetailVo);
				
				stockQty -= shiftQty;
				log.error("规则0 -> 货品" + sku.getSkuName() + " , 批次：" + batchNo + "，分配库位" + metaLocation.getLocationName());
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
			log.error("规则0，未能完全分配");
			return true;
		}
		log.error("规则0，分配完毕");
		return false;
	}
}