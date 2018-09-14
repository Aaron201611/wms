package com.yunkouan.wms.modules.sys.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.sys.entity.InterfaceLog;

public class InterfaceLogVo  extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1933804030815900481L;
	
	private InterfaceLog entity;
	
	public InterfaceLogVo(){
		
	}
	
	public InterfaceLogVo(InterfaceLog log){
		this.entity = log;
	}
	

	public InterfaceLog getEntity() {
		return entity;
	}

	public void setEntity(InterfaceLog entity) {
		this.entity = entity;
	}
	
	

	
}
