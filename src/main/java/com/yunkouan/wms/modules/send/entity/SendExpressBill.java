package com.yunkouan.wms.modules.send.entity;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;
/**
 * 面单
 * @author Aaron
 *
 */
public class SendExpressBill extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2366509892415830049L;

	@Id
	private String expressBillId;

	/**
	 * 面单号
	 */
    private String expressBillNo;
    /**
     * 面单类型
     */
    private Integer expressBillType;
    /**
     * 面单状态
     */
    private Integer expressBillStatus;

    /**
     * 企业id
     */
    private String orgId;

    /**
     * 仓库id
     */
    private String warehouseId;
    /**
     * 面单id2
     */
    private String expressBillId2;
	public String getExpressBillId() {
		return expressBillId;
	}
	public void setExpressBillId(String expressBillId) {
		this.expressBillId = expressBillId;
	}
	public String getExpressBillNo() {
		return expressBillNo;
	}
	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}
	public Integer getExpressBillType() {
		return expressBillType;
	}
	public void setExpressBillType(Integer expressBillType) {
		this.expressBillType = expressBillType;
	}
	public Integer getExpressBillStatus() {
		return expressBillStatus;
	}
	public void setExpressBillStatus(Integer expressBillStatus) {
		this.expressBillStatus = expressBillStatus;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getExpressBillId2() {
		return expressBillId2;
	}
	public void setExpressBillId2(String expressBillId2) {
		this.expressBillId2 = expressBillId2;
	}
    
    
    
    
}