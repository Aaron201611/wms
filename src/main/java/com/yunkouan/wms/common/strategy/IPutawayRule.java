package com.yunkouan.wms.common.strategy;

import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * @function 上架策略
 * @author tphe06
 */
public abstract class IPutawayRule {
	/**
	 * 自动分配上架库位
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public abstract RecPutawayVO auto(RecPutawayVO vo) throws Exception;
}