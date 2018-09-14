/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午5:31:09<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow.autoptw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.service.impl.StockServiceImpl;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.impl.LocationExtlServiceImpl;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.util.flow.FlowConstant;
import com.yunkouan.wms.modules.rec.util.flow.FlowContext;
import com.yunkouan.wms.modules.rec.util.flow.FlowNode;
import com.yunkouan.wms.modules.rec.util.flow.autoptw.AutoPutawayFlow.AutoPtwAllot;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;

/**
 * 相同SKU、相同批次货品，按库位空余量从大到小排序自动分配，
 * 若有剩余货品无法分配，则进行下一节点的分配<br/><br/>
 * @version 2017年3月5日 下午5:31:09<br/>
 * @author andy wang<br/>
 */
public class SameLocationNode implements FlowNode {
	
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	/** 库存业务类 <br/> add by andy */
	private IStockService stockService;
	
	/** 库位业务类 <br/> add by andy */
	private ILocationExtlService locExtlService;
	
	/** 货品Map <br/> add by andy */
	private Map<String,MetaSku> mapSku;
	
	/** 上下文动碰库位 <br/> add by andy */
	private Map<String,MetaLocation> mapCtxDTouchLoc;
	
	/** 货品库位Map <br/> add by andy */
	private Map<String,List<MetaLocation>> mapSkuLocation;
	
	/** 上下文上架单明细集合 <br/> add by andy */
	private List<RecPutawayDetailVO> listCtxPtwDetail;

	/** 自动分配规则 <br/> add by andy */
	private AutoPtwAllot autoPtwAllot;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean executeBefore(FlowContext fc) throws Exception {
		log.error("进入规则1");
		this.stockService = (IStockService) fc.getBean(StockServiceImpl.class);
		this.locExtlService = (ILocationExtlService) fc.getBean(LocationExtlServiceImpl.class);
		this.mapCtxDTouchLoc = (Map<String, MetaLocation>) fc.get(FlowConstant.AUTOPTW_MAP_DTOUCH_LOC);
		this.mapSku = (Map<String, MetaSku>) fc.get(FlowConstant.AUTPTW_MAP_SKU);
		this.mapSkuLocation = new HashMap<String,List<MetaLocation>>();
		this.listCtxPtwDetail = (List<RecPutawayDetailVO>) fc.get(FlowConstant.AUTOPTW_LIST_PTWDETAILVO);
		this.autoPtwAllot = (AutoPtwAllot) fc.get(FlowConstant.AUTOPTW_AUTOALLOT);
		return true;
	}

	@Override
	public Boolean execute(FlowContext fc) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
		
		// 查询有相同skuid，相同批次，相同货主库存的未冻结库位
		/* 	VERSION
		 *  条件添加 【相同货主】
			林总组织讨论后决定添加
		    @version 2017年3月14日下午4:10:47
			add by andy
		*/
		List<String> listSku = new ArrayList<String>();
		List<String> listBatchNo = new ArrayList<String>();
		List<String> listOwner = new ArrayList<String>();
		for (RecPutawayDetailVO recPutawayDetailVO : this.listCtxPtwDetail) {
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			if ( putawayDetail == null ) {
				throw new BizException("err_rec_putawayDetail_null");
			}
			if ( !listSku.contains(putawayDetail.getSkuId()) ) {
				listSku.add(putawayDetail.getSkuId());
			}
			if ( !listBatchNo.contains(putawayDetail.getBatchNo()) ) {
				listBatchNo.add(putawayDetail.getBatchNo());
			}
			if ( !listOwner.contains(putawayDetail.getOwner()) ) {
				listOwner.add(putawayDetail.getOwner());
			}
		}
		// 查询对应skuId、批次的库存集合
		InvStockVO p_stockVO = new InvStockVO(new InvStock());
		p_stockVO.setSkuIdList(listSku);
		p_stockVO.setBatchNoList(listBatchNo);
		p_stockVO.setOwnerList(listOwner);
		p_stockVO.setContainTemp(false);
		p_stockVO.getInvStock().setIsBlock(Constant.STOCK_BLOCK_FALSE);
		p_stockVO.getInvStock().setOrgId(loginUser.getOrgId());
		p_stockVO.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
		List<InvStock> listStock = this.stockService.list(p_stockVO);
		if ( PubUtil.isEmpty(listStock) ) {
			return true;
		}
		// 根据库存集合中的库位id，查询库位
		List<String> listLocId = new ArrayList<String>();
		for (InvStock invStock : listStock) {
			listLocId.add(invStock.getLocationId());
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO();
		metaLocationVO.setListLocationId(listLocId);
		metaLocationVO.setListOwnerId(listOwner);
		metaLocationVO.setOrderByCapacityDesc(true);
		metaLocationVO.getLocation().setIsBlock(Constant.LOCATION_BLOCK_FALSE);
		metaLocationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		metaLocationVO.getLocation().setLocationType(Constant.LOCATION_TYPE_STORAGE);
		List<MetaLocation> listLocation = this.locExtlService.listLocByExample(metaLocationVO);
		if ( PubUtil.isEmpty(listLocation) ) {
			return true;
		}
		//遍历库存和库位，得到完整货品存放库位列表
		for (InvStock invStock : listStock) {
			String key = this.autoPtwAllot.getKey(invStock.getSkuId(),invStock.getBatchNo(),invStock.getOwner());
			List<MetaLocation> list = this.mapSkuLocation.get(key);
			if ( list == null ) {
				list = new ArrayList<MetaLocation>();
				this.mapSkuLocation.put(key,list);
			}
			for (MetaLocation metaLocation : listLocation) {
				if ( !metaLocation.getLocationId().equals(invStock.getLocationId()) ) {
					continue;
				}
				MetaLocation touchLocation = this.mapCtxDTouchLoc.get(metaLocation.getLocationId());
				if ( touchLocation != null ) {
					list.add(touchLocation);
					break;
				} else {
					list.add(metaLocation);
					break;
				}
			}
		}
		// 排序/去重
		for (List<MetaLocation> listLoc : this.mapSkuLocation.values()) {
			this.orderAndPutOff(listLoc);
		}
		// 为上架单明细分配库位
		for (RecPutawayDetailVO recPutawayDetailVO : this.listCtxPtwDetail) {
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			String skuId = putawayDetail.getSkuId();
			if ( putawayDetail.getPlanPutawayQty() <= 0 ) {
				// 已经分配完毕
				continue;
			}
			// 获取对应的库位
			List<MetaLocation> listLoc = this.mapSkuLocation.get(this.autoPtwAllot.getKey(skuId,putawayDetail.getBatchNo(),putawayDetail.getOwner()));
			if ( PubUtil.isEmpty(listLoc) ) {
				continue;
			}
			// 获取货品
			MetaSku metaSku = this.mapSku.get(skuId);
			if ( metaSku == null ) {
				throw new BizException("err_meta_sku_null");
			}
			log.error("规则1 -> 货品" + metaSku.getSkuName() + " , 批次：" + putawayDetail.getBatchNo() + "，进行匹配");
			// 分配上架货位
			// 计算所占库容
			BigDecimal capacity = LocationUtil.capacityConvert(putawayDetail.getPlanPutawayQty(), metaSku);
			recPutawayDetailVO.setCapacity(capacity);
			for (MetaLocation metaLocation : listLoc) {
				if ( putawayDetail.getPlanPutawayQty() <= 0 ) {
					// 已经分配完毕
					break;
				}
				if ( StringUtil.isTrimEmpty(metaLocation.getOwner()) 
						|| StringUtil.isTrimEmpty(putawayDetail.getOwner())
						|| !metaLocation.getOwner().equals(putawayDetail.getOwner()) ) {
					// 库位设置的货主与货品的货主一致，才能分配
					continue;
				}
				this.autoPtwAllot.execute(metaLocation, metaSku, recPutawayDetailVO);
				// 设置库位对象到上架单明细对象
				recPutawayDetailVO.addLocation(metaLocation);
				log.error("规则1 -> 货品" + metaSku.getSkuName() + " , 批次：" + putawayDetail.getBatchNo() + "，分配库位" + metaLocation.getLocationName());
			}
		}
		return true;
	}
	
	@Override
	public Boolean executeAfter(FlowContext fc1) throws Exception {
		// 验证是否全部分配完毕
		for (RecPutawayDetailVO recPutawayDetailVO : this.listCtxPtwDetail) {
			if ( recPutawayDetailVO.getPutawayDetail().getPlanPutawayQty() > 0 ) {
				// 还有未分配上架明细,流程继续
				log.error("规则1，未能完全分配");
				return true;
			}
		}
		log.error("规则1，分配完毕");
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