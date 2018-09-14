package com.yunkouan.wms.modules.park.entity;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;

/**
 * 出租预警记录
 *@Description TODO
 *@author Aaron
 *@date 2017年3月8日 上午9:55:41
 *version v1.0
 */
public class ParkWarn extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3055086099948882806L;

	@Id
    private String warnId;

    private String rentId;

    private Integer warnStatus;

    private String warnMsg;

    private String orgId;

    private String merchantId;

    private Integer warnId2;

    public String getWarnId() {
        return warnId;
    }

    public void setWarnId(String warnId) {
        this.warnId = warnId == null ? null : warnId.trim();
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId == null ? null : rentId.trim();
    }

    public Integer getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(Integer warnStatus) {
        this.warnStatus = warnStatus;
    }

    public String getWarnMsg() {
        return warnMsg;
    }

    public void setWarnMsg(String warnMsg) {
        this.warnMsg = warnMsg == null ? null : warnMsg.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public Integer getWarnId2() {
        return warnId2;
    }

    public void setWarnId2(Integer warnId2) {
        this.warnId2 = warnId2;
    }
}