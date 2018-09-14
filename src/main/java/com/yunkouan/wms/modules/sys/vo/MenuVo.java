package com.yunkouan.wms.modules.sys.vo;

import com.yunkouan.base.BaseObj;

/**
* @Description: TODO
* @author tphe06
* @date 2017年5月21日
*/
public class MenuVo extends BaseObj {
	private static final long serialVersionUID = -1117473094842505750L;

	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}