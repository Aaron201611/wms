/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午5:15:10<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.util.flow.autorep;

import java.math.BigDecimal;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.impl.SysParamServiceImpl;
import com.yunkouan.saas.modules.sys.vo.SysParamVO;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.inv.util.flow.FlowConstant;
import com.yunkouan.wms.modules.inv.util.flow.AbstractFlow;

/**
 * 自动分配补货库位流程<br/><br/>
 * @version 2017年3月5日 下午5:15:10<br/>
 * @author andy wang<br/>
 */
public class AutoRepStockFlow extends AbstractFlow<InvShiftVO> {

	//需要创建的移位单主单，每分配一个库位，创建一个移位单子单
	private InvShiftVO shiftVo;
	//补货货品
	private MetaSku sku;
	//补货数量
	private double qty;
	//校验是否全部移位
	
	

	private ISysParamService sysParamService;
	/**
	 * 构造方法
	 * @param fc 流程上下文
	 * @version 2017年3月5日 下午5:58:27<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	public AutoRepStockFlow(InvShiftVO shiftVo, MetaSku sku, double qty) throws Exception {
		super();
		this.shiftVo = shiftVo;
		this.sku = sku;
		this.qty = qty;
		this.sysParamService = (ISysParamService) fc.getBean(SysParamServiceImpl.class);
		
	}

	@Override
	protected void initFlow() throws Exception {
		// 初始化流程节点
		SysParamVO vo = sysParamService.viewSysParam("1196");
		if (vo.getSysParam().getParamKey().equals("1")) {
			super.addNode(new BindingLocationNode());
		} else if (vo.getSysParam().getParamKey().equals("0")) {
			super.addNode(new SameLocationNode());
			super.addNode(new NearLocationNode());
		}
//		super.addNode(new BindingLocationNode());
//		super.addNode(new SameLocationNode());
//		super.addNode(new NearLocationNode());
	}

	@Override
	public void beforeFlow() throws Exception {
		if ( sku == null ) {
			throw new BizException("err_rec_asn_sku_is_not_exist");
		}
		super.fc.put(FlowConstant.AUTOREP_MAP_SHIFT, this.shiftVo);
		super.fc.put(FlowConstant.AUTOREP_MAP_SKU, this.sku);
		super.fc.put(FlowConstant.AUTOREP_MAP_QTY, this.qty);
		super.fc.put(FlowConstant.AUTOREP_AUTOALLOT, new AutoRepStockAllot());
	}

	@Override
	public void afterFlow() throws Exception {
		// 验证是否全部分配完毕，没有分配完毕的情况（不考虑）
	}

	@Override
	public InvShiftVO getResult() throws Exception {
		return this.shiftVo;
	}
	
	
	/**
	 * 补货库位自动分配<br/><br/>
	 * @version 2017年3月7日 下午8:59:56<br/>
	 * @author andy wang<br/>
	 */
	public class AutoRepStockAllot {

		/**
		 * 构造方法
		 * @version 2017年3月7日 下午9:00:56<br/>
		 * @author andy wang<br/>
		 */
		private AutoRepStockAllot(){}
		
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
		public double execute (MetaLocation metaLocation, MetaSku metaSku, Double remindQty) throws Exception {
			BigDecimal surplusCapacity = LocationUtil.surplusCapacity(metaLocation);
			if ( surplusCapacity.compareTo(BigDecimal.valueOf(metaSku.getPerVolume())) < 0 ) {
				// 库位库容小于单体库容时，不作任何处理
				return 0d;
			}
			// 计算所占库容
			BigDecimal capacity = LocationUtil.capacityConvert(remindQty, sku);
			Double depositQty = remindQty;
			BigDecimal depositCapacity = capacity;
//			Double depositWeight = sku.getPerWeight() * remindQty;
//			Double depositVolume = sku.getPerVolume() * remindQty;
//			Double rate = 1.0 ;
			if ( capacity.compareTo(surplusCapacity) >= 0 ) {
				 // 计算存放数量/库容
				 depositQty = Math.floor(NumberUtil.div(surplusCapacity, BigDecimal.valueOf(metaSku.getPerVolume()),BigDecimal.ROUND_HALF_EVEN).doubleValue());
//				 rate = NumberUtil.div(depositQty, remindQty, 10) ;
				 depositCapacity = LocationUtil.capacityConvert(depositQty, metaSku);
//				 depositWeight = NumberUtil.mul(depositWeight, rate, 2);
//				 depositVolume = NumberUtil.mul(depositVolume , rate, 6);
			}
			// 扣减库位库容
			metaLocation.setUsedCapacity(metaLocation.getUsedCapacity().add(depositCapacity));
			// 记录动碰库位
//			AutoRepStockFlow.this.mapCtxDTouchLoc.put(metaLocation.getLocationId(), metaLocation);
			
			return depositQty;
		}
	}
}
