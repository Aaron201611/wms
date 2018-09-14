/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午5:15:10<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow.autoptw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.impl.SysParamServiceImpl;
import com.yunkouan.saas.modules.sys.vo.SysParamVO;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.impl.SkuServiceImpl;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.util.RecUtil;
import com.yunkouan.wms.modules.rec.util.flow.AbstractFlow;
import com.yunkouan.wms.modules.rec.util.flow.FlowConstant;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;

/**
 * 自动分配上架库位流程<br/><br/>
 * @version 2017年3月5日 下午5:15:10<br/>
 * @author andy wang<br/>
 */
public class AutoPutawayFlow extends AbstractFlow<List<RecPutawayDetailVO>> {

	private List<RecPutawayDetailVO> listPtwDetailVO;
	private RecPutaway recPutaway;

	/** 上下文动碰库位 <br/> add by andy */
	private Map<String,MetaLocation> mapCtxDTouchLoc;
	private ISysParamService sysParamService;

	
	/**
	 * 构造方法
	 * @param fc 流程上下文
	 * @version 2017年3月5日 下午5:58:27<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	public AutoPutawayFlow( List<RecPutawayDetailVO> listPtwDetailVO ,RecPutaway recPutaway) throws Exception {
		super();
		this.listPtwDetailVO = listPtwDetailVO;
		this.recPutaway = recPutaway;
		AutoPutawayFlow.this.mapCtxDTouchLoc = new HashMap<String,MetaLocation>();
		this.sysParamService = (ISysParamService) fc.getBean(SysParamServiceImpl.class);
		
	}

	@Override
	protected void initFlow() throws Exception {
		// 初始化流程节点
		if (this.recPutaway.getDocType() == null) {
			this.recPutaway.setDocType(Constant.PUTAWAY_DOCTYPE_RECEIVE);
		}
		if (this.recPutaway.getDocType() == Constant.PUTAWAY_DOCTYPE_BAD ) {
			if(this.listPtwDetailVO != null) for(RecPutawayDetailVO vo : this.listPtwDetailVO) {
				vo.getPutawayDetail().setSkuStatus(20);
			}
		}
		//不良品，采用不良品库位
		if (this.listPtwDetailVO.get(0) != null 
				&& this.listPtwDetailVO.get(0).getPutawayDetail() != null 
				&& this.listPtwDetailVO.get(0).getPutawayDetail().getSkuStatus() != null 
				&& this.listPtwDetailVO.get(0).getPutawayDetail().getSkuStatus() == 20) {
			super.addNode(new BadLocationNode());
		} else {
			SysParamVO vo = sysParamService.viewSysParam("1196");
			if (vo.getSysParam().getParamKey().equals("1")) {
				super.addNode(new BindingLocationNode());
			} else if (vo.getSysParam().getParamKey().equals("0")) {
				super.addNode(new SameLocationNode());
				super.addNode(new NearLocationNode());
			}
		}
	}

	@Override
	public void beforeFlow() throws Exception {
		if ( PubUtil.isEmpty( this.listPtwDetailVO ) ) {
			throw new BizException("err_rec_putawayDetail_null");
		}
		super.fc.put(FlowConstant.AUTOPTW_LIST_PTWDETAILVO, this.listPtwDetailVO);
		super.fc.put(FlowConstant.AUTOPTW_MAP_DTOUCH_LOC, new HashMap<String,MetaLocation>());
		super.fc.put(FlowConstant.AUTOPTW_AUTOALLOT, new AutoPtwAllot());
		ISkuService skuService = (ISkuService) fc.getBean(SkuServiceImpl.class);
		HashMap<String, MetaSku> mapSku = new HashMap<String, MetaSku>();
		super.fc.put(FlowConstant.AUTPTW_MAP_SKU, mapSku);
		for (int i = 0; i < listPtwDetailVO.size(); i++) {
			RecPutawayDetailVO recPutawayDetailVO = listPtwDetailVO.get(i);
			// 设置计划上架数量/重量/体积默认值
			if ( recPutawayDetailVO == null || recPutawayDetailVO.getPutawayDetail() == null ) {
				RecUtil.defPutawayDetailPlanQWV(recPutawayDetailVO.getPutawayDetail());
			}
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			// 查询货品
			MetaSku metaSku = mapSku.get(putawayDetail.getSkuId());
			if ( metaSku == null ) {
				metaSku = skuService.get(putawayDetail.getSkuId());
				if ( metaSku == null ) {
					throw new BizException("err_meta_sku_null");
				}
				mapSku.put(putawayDetail.getSkuId(), metaSku);
			}
		}
	}

	@Override
	public void afterFlow() throws Exception {
		// 20170424 增加用户体验
		// 验证是否全部分配完毕，没有分配完毕的情况
		for (RecPutawayDetailVO recPutawayDetailVO : listPtwDetailVO) {
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			if ( putawayDetail.getPlanPutawayQty() == 0 ) {
				continue;
			}
			RecPutawayLocationVO ptwLocationVO = new RecPutawayLocationVO();
			ptwLocationVO.getPutawayLocation().setMeasureUnit(putawayDetail.getMeasureUnit());
			ptwLocationVO.getPutawayLocation().setPackId(putawayDetail.getPackId());
			ptwLocationVO.getPutawayLocation().setPutawayQty(putawayDetail.getPlanPutawayQty());
			ptwLocationVO.getPutawayLocation().setPutawayWeight(putawayDetail.getPlanPutawayWeight());
			ptwLocationVO.getPutawayLocation().setPutawayVolume(putawayDetail.getPlanPutawayVolume());
			ptwLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
			recPutawayDetailVO.addPtwLocation(ptwLocationVO, true);
		}
	}

	@Override
	public List<RecPutawayDetailVO> getResult() throws Exception {
		return this.listPtwDetailVO;
	}
	
	
	/**
	 * 上架库位自动分配<br/><br/>
	 * @version 2017年3月7日 下午8:59:56<br/>
	 * @author andy wang<br/>
	 */
	public class AutoPtwAllot {

		/**
		 * 构造方法
		 * @version 2017年3月7日 下午9:00:56<br/>
		 * @author andy wang<br/>
		 */
		private AutoPtwAllot(){}
		

		/**
		 * 获取货品库位Map的Key
		 * @param skuId 货品id
		 * @param batchNo 货品批次
		 * @param owner 货主id
		 * @return 货品库位Map的复核Key
		 * @version 2017年3月6日 下午4:44:54<br/>
		 * @author andy wang<br/>
		 */
		public String getKey( String skuId , String batchNo , String owner ) {
			return skuId +"_"+ batchNo +"_"+ owner;
		}
		
		/**
		 * 自动计算,分配后库位剩余库容，上架货品剩余数量
		 * @param metaLocation 库位
		 * @param metaSku 上架的货品
		 * @param putawayDetail 上架单明细信息
		 * @param capacity 上架单明细的货品所占总库容
		 * @return
		 * @version 2017年3月7日 下午9:00:58<br/>
		 * @author andy wang<br/>
		 * @throws Exception 
		 */
		public boolean execute (MetaLocation metaLocation, MetaSku metaSku, RecPutawayDetailVO putawayDetailVO) throws Exception {
			RecPutawayDetail putawayDetail = putawayDetailVO.getPutawayDetail();
			BigDecimal surplusCapacity = LocationUtil.surplusCapacity(metaLocation);
			if ( surplusCapacity.compareTo(BigDecimal.valueOf(metaSku.getPerVolume())) < 0 ) {
				// 库位库容小于单体库容时，不作任何处理
				return true;
			}
			BigDecimal capacity = putawayDetailVO.getCapacity();
			Double depositQty = putawayDetail.getPlanPutawayQty();
			BigDecimal depositCapacity = capacity;
			Double depositWeight = putawayDetail.getPlanPutawayWeight().doubleValue();
			Double depositVolume = putawayDetail.getPlanPutawayVolume().doubleValue();
			Double rate = 1.0 ;
			if ( capacity.compareTo(surplusCapacity) >= 0 ) {
				 // 上架单明细总库容大于库位剩余库容
				 // 计算存放数量/库容
				 depositQty = Math.floor(NumberUtil.div(surplusCapacity, BigDecimal.valueOf(metaSku.getPerVolume()),BigDecimal.ROUND_HALF_EVEN).doubleValue());
				 rate = NumberUtil.div(depositQty, putawayDetail.getPlanPutawayQty(), 10).doubleValue();
//				 depositQty = new Double(surplusCapacity / metaSku.getPerVolume()).intValue();
				 depositCapacity = LocationUtil.capacityConvert(depositQty, metaSku);
//				 depositWeight = NumberUtil.mul(depositQty, metaSku.getPerWeight(), 2);
//				 depositVolume = NumberUtil.mul(depositQty , metaSku.getPerVolume(), 6);
				 depositWeight = NumberUtil.mul(putawayDetail.getPlanPutawayWeight(), rate, 2);
				 depositVolume = NumberUtil.mul(putawayDetail.getPlanPutawayVolume() , rate, 6);
			}
			// 扣减上架数量
			putawayDetail.setPlanPutawayQty(putawayDetail.getPlanPutawayQty() - depositQty);
			putawayDetail.setPlanPutawayWeight(NumberUtil.subScale(putawayDetail.getPlanPutawayWeight(), depositWeight,2) );
			putawayDetail.setPlanPutawayVolume(NumberUtil.subScale(putawayDetail.getPlanPutawayVolume(), depositVolume,6) );
//			putawayDetail.setPlanPutawayVolume(NumberUtil.mul(putawayDetail.getPlanPutawayVolume(), rate,6) );
//			putawayDetail.setPlanPutawayWeight(NumberUtil.mul(putawayDetail.getPlanPutawayWeight(), rate,2) );
			putawayDetailVO.setCapacity(capacity.subtract(depositCapacity));
//			putawayDetailVO.setCapacity(NumberUtil.sub(capacity, depositCapacity));
			// 扣减库位库容
			// DECI
			metaLocation.setUsedCapacity(metaLocation.getUsedCapacity().add(depositCapacity));
//			metaLocation.setUsedCapacity(NumberUtil.add(metaLocation.getUsedCapacity() , depositCapacity));
			// 记录动碰库位
			AutoPutawayFlow.this.mapCtxDTouchLoc.put(metaLocation.getLocationId(), metaLocation);
			
			// 创建上架单操作明细
			RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO();
			recPutawayLocationVO.getPutawayLocation().setLocationId(metaLocation.getLocationId());
			recPutawayLocationVO.getPutawayLocation().setMeasureUnit(putawayDetail.getMeasureUnit());
			recPutawayLocationVO.getPutawayLocation().setPackId(putawayDetail.getPackId());
			recPutawayLocationVO.getPutawayLocation().setPutawayQty(depositQty);
			recPutawayLocationVO.getPutawayLocation().setPutawayWeight(depositWeight);
			recPutawayLocationVO.getPutawayLocation().setPutawayVolume(depositVolume);
			recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
			recPutawayLocationVO.setLocationComment(metaLocation.getLocationName());
			recPutawayLocationVO.setPerVolume(putawayDetailVO.getPerVolume());
			recPutawayLocationVO.setPerWeight(putawayDetailVO.getPerWeight());
			putawayDetailVO.addPtwLocation(recPutawayLocationVO, true);
			return true;
		}
	}
}
