/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月3日 下午9:56:12<br/>
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
 * 增加库存策略<br/><br/>
 * @version 2017年3月3日 下午9:56:12<br/>
 * @author andy wang<br/>
 */
public class CapacityAddStrategy extends CapacityStrategy {
	
	
	/**
	 * 是否释放预分配库存
	 * —— true 释放预分配库存
	 * —— false 不操作释放
	 * @version 2017年3月3日下午10:58:12<br/>
	 * @author andy wang<br/>
	 */
	private boolean isRelease;
	
	/**
	 * 构造方法
	 * @param location 需要增加/减少库容度的库位对象
	 * @param sku 货品对象
	 * @param qty 货品基本包装的数量
	 * @param isRelease 是否释放预分配库存
	 * —— true 释放预分配库存
	 * —— false 不操作释放
	 * @version 2017年3月3日 下午10:11:29<br/>
	 * @author andy wang<br/>
	 */
	public CapacityAddStrategy(MetaLocation location, MetaSku sku, Double qty , boolean isRelease ) {
		super(location, sku, qty);
		this.isRelease = isRelease;
	}
	
	/**
	 * 构造方法
	 * @param location 需要增加/减少库容度的库位对象
	 * @param sku 货品对象
	 * @param capacity 库容
	 * @param isRelease 是否释放预分配库存
	 * —— true 释放预分配库存
	 * —— false 不操作释放
	 * @version 2017年3月4日 下午5:49:14<br/>
	 * @author andy wang<br/>
	 */
	public CapacityAddStrategy(MetaLocation location, MetaSku sku, BigDecimal capacity , boolean isRelease ) {
		super(location, sku, capacity);
		this.isRelease = isRelease;
	}
	
	
	
	@Override
	protected boolean validate() {
		if ( capacity.compareTo(BigDecimal.valueOf(0)) > 0 ) {
			if ( isRelease ) {
				this.validateAdd();
			} else {
				this.validateAddWithoutPreused();
			}
			if ( isRelease ) {
				record.getLocation().setPreusedCapacity(capacity.multiply(BigDecimal.valueOf(-1)));
//				record.getLocation().setPreusedCapacity(NumberUtil.mul(capacity,-1));
			}
		} else if ( capacity.compareTo(BigDecimal.valueOf(0)) < 0 ) {
			this.validateMinus();
		}
		return true;
	}
	
	@Override
	public MetaLocationVO createRecord () {
		if ( this.isRelease ) {
			this.record.getLocation().setPreusedCapacity(capacity.multiply(BigDecimal.valueOf(-1)));
		}
		this.record.getLocation().setUsedCapacity(capacity);
		this.record.getLocation().setLocationId(this.location.getLocationId());
		return this.record;
	}
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal(1).compareTo(BigDecimal.valueOf(0)));
	}
	
	/**
	 * 增加库容度,不扣除预分配库容度校验器
	 * @version 2017年3月3日 下午11:13:12<br/>
	 * @author andy wang<br/>
	 */
	public void validateAddWithoutPreused () {
		if ( location.getLocationType() == Constant.AREA_TYPE_TEMPSTORAGE ) {
			// 暂存区库位不需要进行库容校验
			return;
		}
		if ( capacity.compareTo(LocationUtil.surplusCapacity(location)) > 0 ) {
			throw new BizException("err_main_location_capacityNotEnough", location.getLocationName());
		}
	}
	
	/**
	 * 增加库容度校验
	 * @version 2017年3月3日 下午10:59:24<br/>
	 * @author andy wang<br/>
	 */
	public void validateAdd () {
		if ( capacity.compareTo(LocationUtil.surplusCapacity(location)) > 0 ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
		if ( capacity.compareTo(location.getPreusedCapacity()) > 0 ) {
			// 增加的库容度比预分配库容度高，会导致扣减预分配库容度时，预分配库容度数值为负数
			throw new BizException("err_main_location_preusedNotEnough",location.getLocationName());
		}
	}
	
	/**
	 * 减少库容度校验
	 * @version 2017年3月3日 下午10:59:41<br/>
	 * @author andy wang<br/>
	 */
	public void validateMinus () {
		// 减少的库容度不能大于已使用库容度
		if (capacity.abs().compareTo(location.getUsedCapacity()) > 0 ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
	}
	
}
