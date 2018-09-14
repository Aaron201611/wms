package com.yunkouan.wms.modules.park.entity;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;

import tk.mybatis.mapper.entity.Example;

/**
 * 企业业务统计信息
 *@Description TODO
 *@author Aaron
 *@date 2017年3月9日 下午7:29:27
 *version v1.0
 */
public class ParkOrgBusiStas extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String busiStasId;

    private String orgId;

    private String merchantId;

    private String docNo;

    private Integer busiType;

    private Double stasQty;

    private Double stasWeight;

    private Double stasVolume;

    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private Date stasDate;

    private Integer busiStasId2;

    public String getBusiStasId() {
        return busiStasId;
    }

    public void setBusiStasId(String busiStasId) {
        this.busiStasId = busiStasId == null ? null : busiStasId.trim();
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

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo == null ? null : docNo.trim();
    }

    public Integer getBusiType() {
        return busiType;
    }

    public void setBusiType(Integer busiType) {
        this.busiType = busiType;
    }

    public Double getStasQty() {
		return stasQty;
	}
	
	public void setStasQty(Double stasQty) {
		this.stasQty = stasQty;
	}
	

	public Double getStasWeight() {
        return stasWeight;
    }

    public void setStasWeight(Double stasWeight) {
        this.stasWeight = stasWeight;
    }

    public Double getStasVolume() {
        return stasVolume;
    }

    public void setStasVolume(Double stasVolume) {
        this.stasVolume = stasVolume;
    }

    public Date getStasDate() {
        return stasDate;
    }

    public void setStasDate(Date stasDate) {
        this.stasDate = stasDate;
    }

    public Integer getBusiStasId2() {
        return busiStasId2;
    }

    public void setBusiStasId2(Integer busiStasId2) {
        this.busiStasId2 = busiStasId2;
    }
    
}