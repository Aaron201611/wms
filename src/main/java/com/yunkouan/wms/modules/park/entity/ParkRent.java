package com.yunkouan.wms.modules.park.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
/**
 * 仓库出租记录
 *@Description TODO
 *@author Aaron
 *@date 2017年3月8日 上午9:55:14
 *version v1.0
 */
public class ParkRent extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3678767885149270822L;

	@Id
	private String rentId;

	@NotNull(message="{valid_ParkRent_orgId_isnull}",groups={ValidSave.class})
    private String orgId;

	/**
	 * 出租仓库id
	 */
	@NotNull(message="{valid_ParkRent_warehouseId_isnull}",groups={ValidSave.class})
    private String warehouseId;

    /**
     * 状态
     */
    private Integer rentStatus;

    /**
     * 开始日期
     */
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    @NotNull(message="{valid_ParkRent_startTime_isnull}",groups={ValidSave.class})
    private Date startTime;

    /**
     * 结束日期
     */
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    @NotNull(message="{valid_ParkRent_endTime_isnull}",groups={ValidSave.class})
    private Date endTime;

    /**
     * 租金
     */
    @Max(value=Integer.MAX_VALUE,message="{valid_ParkRent_rentMoney_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_ParkRent_rentMoney_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double rentMoney;

    /**
     * 押金
     */
    @Max(value=Integer.MAX_VALUE,message="{valid_ParkRent_deposit_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_ParkRent_deposit_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double deposit;

    /**
     * 租金收取方式 现金、转账
     */
    private Integer rentStyle;

    /**
     * 计费方式 按流量、按天、按月
     */
    private Integer feeStyle;

    /**
     * 结算周期 按月、按季度、按年
     */
    private Integer settleCycle;

    /**
     * 提前预警天数
     */
    @Max(value=Integer.MAX_VALUE,message="{valid_ParkRent_warnDays_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_ParkRent_warnDays_min}",groups={ValidSave.class,ValidUpdate.class})
    private Integer warnDays;

    /**
     * 预警频率 每天、每周、每月
     */
    private Integer warnFrequency;

    /**
     * 预警方式 系统、短信、邮件
     */
    private Integer warnStyle;

    /**
     * 租赁客商id
     */
    @NotNull(message="{valid_ParkRent_merchantId_isnull}",groups={ValidSave.class})
    private String merchantId;
    
    private String remark;

    private Integer rentId2;

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId == null ? null : rentId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
    }

    public Integer getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Integer rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(Double rentMoney) {
        this.rentMoney = rentMoney;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Integer getRentStyle() {
        return rentStyle;
    }

    public void setRentStyle(Integer rentStyle) {
        this.rentStyle = rentStyle;
    }

    public Integer getFeeStyle() {
        return feeStyle;
    }

    public void setFeeStyle(Integer feeStyle) {
        this.feeStyle = feeStyle;
    }

    public Integer getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(Integer settleCycle) {
        this.settleCycle = settleCycle;
    }

    public Integer getWarnDays() {
        return warnDays;
    }

    public void setWarnDays(Integer warnDays) {
        this.warnDays = warnDays;
    }

    public Integer getWarnFrequency() {
        return warnFrequency;
    }

    public void setWarnFrequency(Integer warnFrequency) {
        this.warnFrequency = warnFrequency;
    }

    public Integer getWarnStyle() {
        return warnStyle;
    }

    public void setWarnStyle(Integer warnStyle) {
        this.warnStyle = warnStyle;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public Integer getRentId2() {
        return rentId2;
    }

    public void setRentId2(Integer rentId2) {
        this.rentId2 = rentId2;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}