package com.yunkouan.wms.modules.sys.service;

import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

public interface IInterfaceLogService {

	/**
	 * 新增
	 * @param logVo
	 */
	public void add(InterfaceLogVo logVo);
	
	/**
	 * 修改
	 * @param logVo
	 */
	public void update(InterfaceLogVo logVo);
	
}
