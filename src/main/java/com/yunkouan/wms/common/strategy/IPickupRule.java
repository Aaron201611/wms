package com.yunkouan.wms.common.strategy;

import com.yunkouan.wms.modules.send.vo.SendPickVo;

/**
 * @function 拣货策略
 * @author tphe06
 */
public abstract class IPickupRule {
	/**
	 * 自动分配拣货库位（新）
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public abstract SendPickVo allocation_new(SendPickVo pickVo,String operator) throws Exception;
}