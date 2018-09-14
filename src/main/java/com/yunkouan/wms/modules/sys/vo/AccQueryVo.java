package com.yunkouan.wms.modules.sys.vo;

import com.yunkouan.base.BaseObj;

/**
* @Description: 权限查询对象
* @author tphe06
* @date 2017年5月18日
*/
public class AccQueryVo extends BaseObj {
	private static final long serialVersionUID = -1445167300909325158L;

	private String accountId;
	private String warehouseId;

	public String getAccountId() {
		return accountId;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public AccQueryVo setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}
	public AccQueryVo setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
		return this;
	}
}