package com.yunkouan.wms.common.strategy;

import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;

/**
 * @function 补货策略
 * @author 王通
 */
public abstract class IRepStockRule {
	/**
	 * 自动分配补货库位
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public abstract InvShiftVO auto(InvShiftVO vo, MetaSku sku, double qty) throws Exception;
}