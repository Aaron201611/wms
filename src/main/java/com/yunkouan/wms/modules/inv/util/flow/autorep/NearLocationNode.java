/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月6日 上午10:06:10<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.util.flow.autorep;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
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
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.service.impl.PutawayServiceImpl;

/**
 * 分配临近库位<br/><br/>
 * @version 2017年3月6日 上午10:06:10<br/>
 * @author andy wang<br/>
 */
public class NearLocationNode implements FlowNode {
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/**service:上架服务接口**/
	private IPutawayService service;
	/** 库存业务类 <br/> add by andy */
	private IStockService stockService;
	/** 库位业务类 <br/> add by andy */
	private ILocationExtlService locExtlService;
	/** 自动分配规则 <br/> add by andy */
	private AutoRepStockAllot autoRepStockAllot;

	/** 上下文动碰库位 <br/> add by andy */
//	private Map<String,MetaLocation> mapCtxDTouchLoc;
	/** 上下文上架单明细集合 <br/> add by andy */
//	private List<RecPutawayDetailVO> listCtxPtwDetail;
	/** 当前登录用户 <br/> add by andy */
	private Principal loginUser;
	/** 空库位集合 <br/> add by andy */
	private List<MetaLocation> listNullLocation;
	/** 货品Map <br/> add by andy */
	private Map<String,MetaSku> mapSku;
	/** 用于记录某一货品的分配库位集合<br/> add by andy wang */
	private Map<String,List<MetaLocationVO>> mapUsedLoc;

	/** 货品条件 */
	private MetaSku sku;
	/** 移位数量 */
	private double qty;
	/** 上下文移位单 */
	private InvShiftVO shiftVo;
	private double remindQty;

	@Override
	public Boolean executeBefore(FlowContext fc) throws Exception {
		if(log.isWarnEnabled()) log.warn("进入规则相近空库位");
		this.service = (IPutawayService)fc.getBean(PutawayServiceImpl.class);
		this.stockService = (IStockService) fc.getBean(StockServiceImpl.class);
		this.locExtlService = (ILocationExtlService) fc.getBean(LocationExtlServiceImpl.class);
		this.shiftVo = (InvShiftVO) fc.get(FlowConstant.AUTOREP_MAP_SHIFT);
		this.sku = (MetaSku) fc.get(FlowConstant.AUTOREP_MAP_SKU);
		this.qty = (Double) fc.get(FlowConstant.AUTOREP_MAP_QTY);
		this.mapUsedLoc = new HashMap<String,List<MetaLocationVO>>();
		loginUser = LoginUtil.getLoginUser();
		this.autoRepStockAllot = (AutoRepStockAllot) fc.get(FlowConstant.AUTOREP_AUTOALLOT);
		return true;
	}

	// 执行库位分配算法 
	@Override
	public Boolean execute(FlowContext fc1) throws Exception {
		// 先查询所有的空库位（未冻结，生效），
		// 后续再过滤货主和货品类型，由于有多个货品此处无法过滤货主和货品类型
		MetaLocationVO metaLocationVO = new MetaLocationVO();
		metaLocationVO.getLocation().setUsedCapacity(BigDecimal.valueOf(0));
		metaLocationVO.getLocation().setPreusedCapacity(BigDecimal.valueOf(0));
		metaLocationVO.getLocation().setIsBlock(Constant.LOCATION_BLOCK_FALSE);
		metaLocationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		metaLocationVO.getLocation().setLocationType(Constant.LOCATION_TYPE_STORAGE);
		metaLocationVO.addListTypes(Constant.LOCATION_TYPE_PICKUP);
//		metaLocationVO.setOrderByLocationId(true);
		metaLocationVO.setOrderByLocationNoAsc(true);
		metaLocationVO.loginInfo();
		this.listNullLocation = this.locExtlService.listLocByExample(metaLocationVO);
		if ( PubUtil.isEmpty(this.listNullLocation) ) return true;
		// 把所有空库位实体类列表转化成VO类列表
		List<MetaLocationVO> listLocVO = new ArrayList<MetaLocationVO>();
		for (MetaLocation metaLocation : this.listNullLocation) {
			listLocVO.add(new MetaLocationVO(metaLocation));
		}
		
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
  		if (shiftVo.remindQty != 0 && shiftVo.remindQty != 0d) {
  			remindQty = shiftVo.remindQty;
  		}
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
			
      		// 查找原点库位（按照货主，货品类型过滤）
			if(log.isWarnEnabled()) log.warn("开始寻找原点库位");
			MetaLocation originLoc = this.findOrigin(stock.getBatchNo());
			// 没有适合的原点
			if ( originLoc == null ) continue;
			if(log.isWarnEnabled()) log.warn("原点库位 ： " + originLoc.getLocationName());
			// 含原点库位距离的库位列表
			List<MetaLocationVO> listMatchLocVO = new ArrayList<MetaLocationVO>();
			String key = this.autoRepStockAllot.getKey(sku.getSkuId(), stock.getBatchNo(), sku.getOwner());
			List<MetaLocationVO> listUsedLoc = this.mapUsedLoc.get(key);
			if ( listUsedLoc == null ) {
				listUsedLoc = new ArrayList<MetaLocationVO>();
				this.mapUsedLoc.put(key, listUsedLoc);
			} else {
				listMatchLocVO.addAll(listUsedLoc);
			}
			// 计算距离并添加到集合
			for (MetaLocationVO locVO : listLocVO) {
				// 货主不匹配，不予分配
				if ( !locVO.getLocation().getOwner().equals(sku.getOwner()) ) continue;
				// 货品类型不匹配，不予分配
				if(!service.isSameSkuType(sku.getSkuId(), locVO.getLocation().getLocationId())) continue;
				// 计算待分配库位跟原点库位的距离
				Double distance = this.calDistance(originLoc, locVO);
				if(log.isWarnEnabled()) log.warn("库位 " + locVO.getLocation().getLocationName() + " 距离原点 ： " + distance);
				locVO.setDistance(distance);
				listMatchLocVO.add(locVO);
			}
			MetaSku metaSku = this.mapSku.get(sku.getSkuId());
			if ( metaSku == null )  throw new BizException("err_meta_sku_null");
			if(log.isWarnEnabled()) log.warn("规则临近库位 -> 货品" + metaSku.getSkuName() + "，进行匹配");
			// 根据距离排序
			this.orderByDistance(listMatchLocVO);
			if(log.isWarnEnabled()) log.warn("排序结果");
			for (MetaLocationVO l : listMatchLocVO) {
				if(log.isWarnEnabled()) log.warn(l.getLocation().getLocationName() + ",剩余库容:" + LocationUtil.surplusCapacity(l.getLocation()) );
			}
//			BigDecimal capacity = LocationUtil.capacityConvert(stockQty, metaSku);
			for (MetaLocationVO locVO : listMatchLocVO) {
    			if (stockQty <= 0) {
    				break;
    			}
				MetaLocation location = locVO.getLocation();
				// 货主不匹配，不予分配
				if ( StringUtil.isTrimEmpty(location.getOwner())
					|| StringUtil.isTrimEmpty(sku.getOwner())
					|| !location.getOwner().equals(sku.getOwner()) ) continue;
				// 货品类型不匹配，不予分配
				if(!service.isSameSkuType(sku.getSkuId(), location.getLocationId())) continue;
				
				double shiftQty = this.autoRepStockAllot.execute(location, sku, stockQty);
				//根据库存，选择出库库位和数量
				InvShiftDetailVO shiftDetailVo = new InvShiftDetailVO();
	      		InvShiftDetail shiftDetail = new InvShiftDetail();
	      		shiftDetail.setShiftDetailId(UUID.randomUUID().toString());
	      		shiftDetail.setShiftId(shiftVo.getInvShift().getShiftId());
	      		shiftDetailVo.setInvShiftDetail(shiftDetail);
	      		shiftDetail.setOutLocation(locVO.getLocation().getLocationId());
	      		shiftDetail.setSkuId(skuId);
	      		shiftDetail.setBatchNo(batchNo);
				shiftDetail.setShiftQty(shiftQty);
				shiftDetail.setInLocation(location.getLocationId());
				
				shiftVo.getListInvShiftDetailVO().add(shiftDetailVo);
				
				stockQty -= shiftQty;
				// 移除空库位
				listLocVO.remove(locVO);
				// 记录相同批次货品已使用的库位
				if ( !listUsedLoc.contains(locVO) ) listUsedLoc.add(locVO);
				if(log.isWarnEnabled()) log.warn("规则临近空库位 -> 货品" + metaSku.getSkuName() + "，分配库位" + location.getLocationName());
			}
		}
		return true;
	}

	/**
	 * 根据距离排序
	 * @param listLocVO 库位集合
	 * @return 排序好的库位集合
	 * @throws Exception
	 * @version 2017年3月7日 下午8:30:29<br/>
	 * @author andy wang<br/>
	 */
	private List<MetaLocationVO> orderByDistance ( List<MetaLocationVO> listLocVO ) throws Exception {
		listLocVO.sort(new Comparator<MetaLocationVO>() {
			@Override
			public int compare(MetaLocationVO o1, MetaLocationVO o2) {
				int result = 0;
				if ( o1.getDistance() == o2.getDistance() ) {
					result = 0;
				} else if ( o1.getDistance() < o2.getDistance() ) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
		return listLocVO;
	}
	
	
	/**
	 * 计算原点库位与库位之间距离
	 * —— 计算公式：AB|=√[(x2-x1)^2+(y2-y1)^2+(z2-z1)^2]
	 * @param originLoc 原点库位
	 * @param Loc 库位
	 * @version 2017年3月7日 下午8:10:51<br/>
	 * @author andy wang<br/>
	 */
	private Double calDistance(MetaLocation originLoc , MetaLocationVO Loc) {
		MetaLocation location = Loc.getLocation();
		LocationUtil.defXYZ(originLoc);
		LocationUtil.defXYZ(location);
		return Math.sqrt( Math.pow(NumberUtil.sub(location.getX(), originLoc.getX()),2)
				+ Math.pow(NumberUtil.sub(location.getY(), originLoc.getY()),2)
				+ Math.pow(NumberUtil.sub(location.getZ(), originLoc.getZ()),2));
	}

	/**
	 * 查找原点
	 * 查找原点的三条规则
	 * —— 查询有存放skuId、批次一致的库位，没有的情况下一条规则
	 * —— 查询有存放skuId一致的库位，没有的情况下一条规则
	 * —— 查询跟货品有相同一级货品类型和货主的库位
	 * —— 不作分配
	 * @param ptwDetail 上架单明细
	 * @return 原点库位
	 * @throws Exception
	 * @version 2017年3月7日 下午4:09:30<br/>
	 * @author andy wang<br/>
	 */
	private MetaLocation findOrigin ( String batchNo) throws Exception {
		MetaLocation loc = null;
		// 存储有与待上架SKU、批次一致的库位
		loc = this.skuIdBatchNoSameLoc(batchNo);
		if ( loc != null ) {
			if(log.isWarnEnabled()) log.warn("进入规则2 -> 原点规则（SKU、批次一致的库位）");
			return loc;
		}
		// 存储有与待上架SKU一致的库位
		loc = this.skuIdSameLoc();
		if ( loc != null ) {
			if(log.isWarnEnabled()) log.warn("进入规则2 -> 原点规则（SKU一致的库位）");
			return loc;
		}
		// 查询跟货品有相同一级货品类型和货主的库位
		loc = this.randomLoc();
		if ( loc != null ) {
			if(log.isWarnEnabled()) log.warn("进入规则2 -> 原点规则（查询跟货品有相同一级货品类型和货主的库位）");
			return loc;
		}
		return loc;
	}

	/**
	 * 查询有存放skuId、批次一致货品的库位
	 * @param batchNo 上架单明细
	 * @return 有存放skuId、批次一致货品的库位
	 * @throws Exception
	 * @version 2017年3月7日 下午5:28:44<br/>
	 * @author andy wang<br/>
	 */
	private MetaLocation skuIdBatchNoSameLoc ( String batchNo ) throws Exception {
		// 查询有存放skuId、批次一致的库存
		InvStockVO p_stockVO = new InvStockVO(new InvStock());
		p_stockVO.getInvStock().setSkuId(sku.getSkuId());
		p_stockVO.getInvStock().setBatchNo(batchNo);
		p_stockVO.getInvStock().setIsBlock(Constant.STOCK_BLOCK_FALSE);
		p_stockVO.getInvStock().setOrgId(loginUser.getOrgId());
		p_stockVO.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
		p_stockVO.getInvStock().setOwner(sku.getOwner());
		List<InvStock> listStock = this.stockService.list(p_stockVO);
		if ( PubUtil.isEmpty(listStock) ) return null;
		List<String> listLocationId = new ArrayList<String>();
		for (InvStock invStock : listStock) {
			listLocationId.add(invStock.getLocationId());
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO(new MetaLocation());
//		metaLocationVO.setOrderByLocationId(true);
		metaLocationVO.setOrderByLocationNoAsc(true);
		metaLocationVO.setListLocationId(listLocationId );
		metaLocationVO.getLocation().setLocationType(Constant.LOCATION_TYPE_PICKUP);
		metaLocationVO.getLocation().setIsBlock(Constant.LOCATION_BLOCK_FALSE);
		metaLocationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		metaLocationVO.getLocation().setOwner(sku.getOwner());
		List<MetaLocation> listLocation = this.locExtlService.listLocByExample(metaLocationVO);
		if ( PubUtil.isEmpty(listLocation) ) return null;
		return PubUtil.isEmpty(listLocation)?null:listLocation.get(0);
	}

	/**
	 * 查询有存放skuId一致货品的库位
	 * @param ptwDetail 上架单明细
	 * @return 有存放skuId一致货品的库位
	 * @throws Exception
	 * @version 2017年3月7日 下午8:01:43<br/>
	 * @author andy wang<br/>
	 */
	private MetaLocation skuIdSameLoc ( ) throws Exception {
		InvStockVO p_stockVO = new InvStockVO(new InvStock());
		p_stockVO.getInvStock().setSkuId(sku.getSkuId());
		p_stockVO.getInvStock().setIsBlock(Constant.STOCK_BLOCK_FALSE);
		p_stockVO.getInvStock().setOrgId(loginUser.getOrgId());
		p_stockVO.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
		p_stockVO.getInvStock().setOwner(sku.getOwner());
		List<InvStock> listStock = this.stockService.list(p_stockVO);
		if ( PubUtil.isEmpty(listStock) ) return null;
		List<String> listLocationId = new ArrayList<String>();
		for (InvStock invStock : listStock) {
			listLocationId.add(invStock.getLocationId());
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO(new MetaLocation());
//		metaLocationVO.setOrderByLocationId(true);
		metaLocationVO.setOrderByLocationNoAsc(true);
		metaLocationVO.setListLocationId(listLocationId );
		metaLocationVO.getLocation().setLocationType(Constant.LOCATION_TYPE_PICKUP);
		metaLocationVO.getLocation().setIsBlock(Constant.LOCATION_BLOCK_FALSE);
		metaLocationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		metaLocationVO.getLocation().setOwner(sku.getOwner());
		List<MetaLocation> listLocation = this.locExtlService.listLocByExample(metaLocationVO);
		if ( PubUtil.isEmpty(listLocation) ) return null;
		return PubUtil.isEmpty(listLocation)?null:listLocation.get(0);
	}

	/**
	 * 随机一个相同货主，货品类型并且库容为空的库位 TODO
	 * @param ptwDetail 上架单明细
	 * @return 库容为空库位
	 * @throws Exception
	 * @version 2017年3月7日 下午8:06:18<br/>
	 * @author andy wang<br/>
	 */
	private MetaLocation randomLoc ( ) throws Exception {
		if ( PubUtil.isEmpty(this.listNullLocation) ) return null;
		MetaLocation origionLoc = null;
		for (MetaLocation location : this.listNullLocation) {
			// 库位类型不是拣货区，不予分配
			if (location.getLocationType() != Constant.LOCATION_TYPE_PICKUP) continue;
			// 货主不匹配，不予分配
			if (!location.getOwner().equals(sku.getOwner()) ) continue;
			// 货品类型不匹配，不予分配
			if(!service.isSameSkuType(sku.getSkuId(), location.getLocationId())) continue;
			// 找到一个适合库位即返回
			origionLoc = location;
			break;
		}
		return origionLoc;
	}

	@Override
	public Boolean executeAfter(FlowContext fc1) throws Exception {
		// 验证是否全部分配完毕
		if (remindQty > 0) {
			log.error("规则临近空库位，未能完全分配");
			shiftVo.remindQty = remindQty;
			return true;
		}
		log.error("规则临近空库位，分配完毕");
		return false;
	}
}