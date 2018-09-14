/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月27日 上午11:48:11<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.util.strategy;

import java.math.BigDecimal;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位库容度策略<br/><br/>
 * @version 2017年2月27日 上午11:48:11<br/>
 * @author andy wang<br/>
 */
public abstract class CapacityStrategy {
	
	/**
	 * 需要增加/减少库容度的库位对象
	 * @version 2017年2月27日上午11:50:33<br/>
	 * @author andy wang<br/>
	 */
	protected MetaLocation location;
	
	/**
	 * 货品对象
	 * @version 2017年3月3日下午10:02:00<br/>
	 * @author andy wang<br/>
	 */
	protected MetaSku sku;
	
	/**
	 * 增加/减少的库容度/预分配库容度
	 * @version 2017年2月27日上午11:51:06<br/>
	 * @author andy wang<br/>
	 */
	protected BigDecimal capacity;
	
	/**
	 * 货品数量
	 * @version 2017年3月3日下午10:07:22<br/>
	 * @author andy wang<br/>
	 */
	protected Double qty;
	
	/**
	 * 要更新入库的库位对象
	 * @version 2017年3月3日下午11:10:01<br/>
	 * @author andy wang<br/>
	 */
	protected MetaLocationVO record;
	
	public CapacityStrategy (MetaLocation location , MetaSku sku) {
		if ( sku == null) {
			throw new BizException("err_meta_sku_null");
		}
		if ( location == null ) {
			throw new BizException("err_main_location_null");
		}
		this.sku = sku;
		this.record = new MetaLocationVO();
		this.location = location;
		LocationUtil.defCapacity(location);
	}
	
	
	/**
	 * 构造方法
	 * @param location 需要增加/减少库容度的库位对象
	 * @param sku 货品对象
	 * @param qty 货品基本包装的数量
	 * @version 2017年2月27日 下午1:44:57<br/>
	 * @author andy wang<br/>
	 */
	public CapacityStrategy( MetaLocation location , MetaSku sku , Double qty ) {
		this(location, sku);
		this.qty = qty;
	}
	
	
	/**
	 * 构造方法
	 * @param location 需要增加/减少库容度的库位对象
	 * @param sku 货品对象
	 * @param capacity 库容
	 * @version 2017年3月4日 下午5:47:19<br/>
	 * @author andy wang<br/>
	 */
	public CapacityStrategy ( MetaLocation location , MetaSku sku , BigDecimal capacity ) {
		this(location, sku);
		this.capacity = capacity;
	}
	
	/**
	 * 校验库位
	 * @return
	 * @version 2017年3月3日 下午5:09:03<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	protected boolean validateLocation () throws Exception {
		LocationUtil.checkLocation(location, true);
		return true;
	}
	
	
	/**
	 * 体积换算
	 * @version 2017年3月3日 下午5:17:32<br/>
	 * @author andy wang<br/>
	 */
	protected void volumeConvert () {
		// ANDY 数量 * 货品体积
		this.capacity = LocationUtil.capacityConvert(this.qty, this.sku);
	}
	
	/**
	 * 校验库容度<br/>
	 * —— 库容度不足是报异常
	 * @return
	 * @version 2017年2月27日 下午1:52:06<br/>
	 * @author andy wang<br/>
	 */
	protected abstract boolean validate();
	
	/**
	 * 创建要更新入库的库位对象
	 * @return 库位对象
	 * @version 2017年3月3日 下午11:04:44<br/>
	 * @author andy wang<br/>
	 */
	public abstract MetaLocationVO createRecord ();
	
	/**
	 * 执行库容策略
	 * —— *校验库位
	 * —— *体积换算
	 * —— *校验库容度
	 * —— *创建更新库位对象
	 * @return 要更新入库的库位对象
	 * @version 2017年3月3日 下午11:06:36<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	public MetaLocationVO execute() throws Exception {
		// 暂存区不需要计算库存
		if ( this.location.getLocationType() == Constant.AREA_TYPE_TEMPSTORAGE ) {
			return null;
		}
		this.validateLocation();
		if ( this.capacity == null ) {
			this.volumeConvert();
		}
		this.validate();
		return this.createRecord();
	}
}
