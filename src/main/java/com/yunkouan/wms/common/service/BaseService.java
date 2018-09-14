package com.yunkouan.wms.common.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.wms.common.strategy.StrategyContext;

public abstract class BaseService {
	/**context:策略上下文**/
	@Autowired
	protected StrategyContext context;

}