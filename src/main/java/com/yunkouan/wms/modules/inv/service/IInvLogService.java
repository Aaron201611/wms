package com.yunkouan.wms.modules.inv.service;

public interface IInvLogService {
	/**
	 * 备份过期日志
	 * @param days 过期天数
	 * @return
	 */
	public Integer backup(Integer days);
}