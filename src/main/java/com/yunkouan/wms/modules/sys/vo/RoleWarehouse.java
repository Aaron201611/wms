package com.yunkouan.wms.modules.sys.vo;

import com.yunkouan.wms.modules.sys.entity.SysRole;

/**
* @Description: 角色仓库授权数据传输类
* @author tphe06
* @date 2017年6月1日
*/
public class RoleWarehouse extends SysRole {
	private static final long serialVersionUID = 5986854929878368090L;

	private String warehouseId;
	private String warehouseNo;
	private String warehouseName;

	public String getWarehouseId() {
		return warehouseId;
	}
	public String getWarehouseNo() {
		return warehouseNo;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
}