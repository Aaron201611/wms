package com.yunkouan.wms.modules.sys.entity;

import java.util.Date;

import com.yunkouan.base.BaseEntity;

/**
* @Description: 【系统接口】实体类
* @author tphe06
* @date 2017年3月15日
*/
public class SysInterfaces extends BaseEntity {
	private static final long serialVersionUID = 3864145079998979270L;

	private String interfaceId;

    private Integer interfaceType;

    private String srcSystem;

    private String destSystem;

    private Date reqTime;

    private Date rspTime;

    private Integer dealTime;

    private String uniqueId;

    private Integer interfaceStatus;

    private String note;

    private Integer interfaceStyle;

    private String interaceParam;

    private String outParam;

    private String dealResult;

    private String orgId;

    private String createPerson;

    private Date createTime;

    private String updatePerson;

    private Date updateTime;

    private Integer interfaceId2;

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId == null ? null : interfaceId.trim();
    }

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getSrcSystem() {
        return srcSystem;
    }

    public void setSrcSystem(String srcSystem) {
        this.srcSystem = srcSystem == null ? null : srcSystem.trim();
    }

    public String getDestSystem() {
        return destSystem;
    }

    public void setDestSystem(String destSystem) {
        this.destSystem = destSystem == null ? null : destSystem.trim();
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public Date getRspTime() {
        return rspTime;
    }

    public void setRspTime(Date rspTime) {
        this.rspTime = rspTime;
    }

    public Integer getDealTime() {
        return dealTime;
    }

    public void setDealTime(Integer dealTime) {
        this.dealTime = dealTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId == null ? null : uniqueId.trim();
    }

    public Integer getInterfaceStatus() {
        return interfaceStatus;
    }

    public void setInterfaceStatus(Integer interfaceStatus) {
        this.interfaceStatus = interfaceStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Integer getInterfaceStyle() {
        return interfaceStyle;
    }

    public void setInterfaceStyle(Integer interfaceStyle) {
        this.interfaceStyle = interfaceStyle;
    }

    public String getInteraceParam() {
        return interaceParam;
    }

    public void setInteraceParam(String interaceParam) {
        this.interaceParam = interaceParam == null ? null : interaceParam.trim();
    }

    public String getOutParam() {
        return outParam;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam == null ? null : outParam.trim();
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult == null ? null : dealResult.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getInterfaceId2() {
        return interfaceId2;
    }

    public void setInterfaceId2(Integer interfaceId2) {
        this.interfaceId2 = interfaceId2;
    }
}