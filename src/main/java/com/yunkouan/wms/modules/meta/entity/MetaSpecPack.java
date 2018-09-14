package com.yunkouan.wms.modules.meta.entity;

import java.util.Date;

public class MetaSpecPack {
    private String specPackId;

    private String packId;

    private Integer maxQty;

    private String specId;

    private String orgId;

    private String warehouseId;

    private String createPerson;

    private Date createTime;

    private String updatePerson;

    private Date updateTime;

    private Integer specPackId2;

    public String getSpecPackId() {
        return specPackId;
    }

    public void setSpecPackId(String specPackId) {
        this.specPackId = specPackId == null ? null : specPackId.trim();
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
    }

    public Integer getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Integer maxQty) {
        this.maxQty = maxQty;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId == null ? null : specId.trim();
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

    public Integer getSpecPackId2() {
        return specPackId2;
    }

    public void setSpecPackId2(Integer specPackId2) {
        this.specPackId2 = specPackId2;
    }
}