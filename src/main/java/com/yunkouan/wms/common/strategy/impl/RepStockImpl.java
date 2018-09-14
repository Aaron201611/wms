package com.yunkouan.wms.common.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.strategy.IRepStockRule;
import com.yunkouan.wms.modules.inv.util.flow.autorep.AutoRepStockFlow;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;

/**
 * @function 补货规则的实现：只查看绑定库位
 * @author tphe06
 */
@Service(value="RepStockRule")
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class RepStockImpl extends IRepStockRule {
	private Logger log = LoggerFactory.getLogger(RepStockImpl.class);

	/**
	 * 自动分配补货库位
	 * @param param_recRepStockVO 上架信息
	 * @return 包含分配好上架库位的上架信息
	 * @throws Exception
	 * @version 2017年3月14日 下午6:12:56<br/>
	 * @author andy wang<br/>
	 */
	public InvShiftVO auto (InvShiftVO vo, MetaSku sku, double qty) throws Exception {
		AutoRepStockFlow flow = new AutoRepStockFlow(vo, sku, qty);
		flow.startFlow();
		return vo;
	}
}