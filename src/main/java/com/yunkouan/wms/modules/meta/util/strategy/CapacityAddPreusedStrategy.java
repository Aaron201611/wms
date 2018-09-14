/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月3日 下午11:16:22<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.util.strategy;

import java.math.BigDecimal;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 增加预分配库存策略<br/><br/>
 * @version 2017年3月3日 下午11:16:22<br/>
 * @author andy wang<br/>
 */
public class CapacityAddPreusedStrategy extends CapacityStrategy {
	
	
	/**
	 * 构造方法
	 * @param location 需要增加/减少库容度的库位对象
	 * @param sku 货品对象
	 * @param qty 货品基本包装的数量
	 * @version 2017年3月3日 下午11:16:45<br/>
	 * @author andy wang<br/>
	 */
	public CapacityAddPreusedStrategy(MetaLocation location, MetaSku sku, Double qty ) {
		super(location, sku, qty);
	}
	
	/**
	 * 构造方法
	 * @param location 需要增加/减少库容度的库位对象
	 * @param sku 货品对象
	 * @param capacity 库容
	 * @version 2017年3月4日 下午5:50:55<br/>
	 * @author andy wang<br/>
	 */
	public CapacityAddPreusedStrategy(MetaLocation location, MetaSku sku, BigDecimal capacity ) {
		super(location, sku, capacity);
	}
	
	@Override
	protected boolean validate() {
		if ( capacity.compareTo(BigDecimal.ZERO) < 0 ) {
			this.validateMinus();
		} else {
			this.validateAdd();
		}
		return true;
	}
	
	@Override
	public MetaLocationVO createRecord () {
		this.record.getLocation().setPreusedCapacity(capacity);
		this.record.getLocation().setLocationId(this.location.getLocationId());
		return this.record;
	}
	
	/**
	 * 增加预分配库容度校验
	 * @version 2017年3月3日 下午11:26:01<br/>
	 * @author andy wang<br/>
	 */
	public void validateAdd () {
		// 预分配库容度不能大于剩余库容度
		if ( formatNum(capacity) - (location.getMaxCapacity().subtract(location.getUsedCapacity().subtract(location.getPreusedCapacity()))).doubleValue() > 0d ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
	}
	
	/**
	 * 减少预分配库容度校验
	 * @version 2017年3月3日 下午11:26:14<br/>
	 * @author andy wang<br/>
	 */
	public void validateMinus () {
		// 减少的预分配库容度不能大于预分配库容度
		if ( formatNum(capacity.abs()) + location.getPreusedCapacity().doubleValue() < 0d) {
			throw new BizException("err_main_location_preusedNotEnough",location.getLocationName());
		}
	}
	
	public double formatNum(BigDecimal number) {
		return Math.round(number.doubleValue()*100000)/1000000d;
	}
}
