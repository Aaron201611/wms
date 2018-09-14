package com.yunkouan.wms.modules.sys.entity;

import java.util.Date;

import com.yunkouan.base.BaseEntity;

public class SysUserParam extends BaseEntity {
	private static final long serialVersionUID = -6446563235143369872L;

	private String paramId;

    private String paramGroup;

    private String paramType;

    private String paramName;

    private String paramValue;

    private String note;

    private Integer paramStatus;

    private String orgId;

    private String createPerson;

    private Date createTime;

    private String updatePerson;

    private Date updateTime;

    private Integer paramId2;

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId == null ? null : paramId.trim();
    }

    public String getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(String paramGroup) {
        this.paramGroup = paramGroup == null ? null : paramGroup.trim();
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType == null ? null : paramType.trim();
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue == null ? null : paramValue.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Integer getParamStatus() {
        return paramStatus;
    }

    public void setParamStatus(Integer paramStatus) {
        this.paramStatus = paramStatus;
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

    public Integer getParamId2() {
        return paramId2;
    }

    public void setParamId2(Integer paramId2) {
        this.paramId2 = paramId2;
    }
}