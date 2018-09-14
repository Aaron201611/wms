package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;

import com.yunkouan.base.BaseEntity;

/**
 * 货品包装实体类
 * @author tphe06 2017年2月16日
 */
public class MetaSkuPack extends BaseEntity {
	private static final long serialVersionUID = 2318336992489288350L;

	/**货品包装id**/
	@Id
	private String skuPackId;
	/**货品id**/
    private String skuId;
    /**包装id**/
    private String packId;
    /**包装系数**/
    private Double packPercent;
    /**组织id**/
    private String orgId;
    /**仓库id**/
    private String warehouseId;
    
    private Integer skuPackId2;

    public String getSkuPackId() {
        return skuPackId;
    }

    public MetaSkuPack setSkuPackId(String skuPackId) {
        this.skuPackId = skuPackId == null ? null : skuPackId.trim();
        return this;
    }

    public String getSkuId() {
        return skuId;
    }

    public MetaSkuPack setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
        return this;
    }

    public String getPackId() {
        return packId;
    }

    public MetaSkuPack setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
        return this;
    }

    public Double getPackPercent() {
        return packPercent;
    }

    public MetaSkuPack setPackPercent(Double packPercent) {
        this.packPercent = packPercent;
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public MetaSkuPack setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public MetaSkuPack setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
        return this;
    }

    public Integer getSkuPackId2() {
        return skuPackId2;
    }

    public MetaSkuPack setSkuPackId2(Integer skuPackId2) {
        this.skuPackId2 = skuPackId2;
        return this;
    }
}