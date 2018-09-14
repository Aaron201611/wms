package com.yunkouan.wms.modules.meta.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 货品bom实体类
 * @author tphe06 2017年2月14日
 */
public class MetaSkuBom extends BaseEntity {
	private static final long serialVersionUID = -3786589406112454534L;

	/**货品bomId*/
	@Id
	private String skuBomId;
	/**bomId*/
	private String bomId;
    
	/**货品ID*/
    private String skuId;

    /**货品数量*/
    private BigDecimal number;


	
	/**仓库id*/
	@Length(max=32,message="{valid_sku_warehouseId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String warehouseId;

    /**组织id*/
	@Length(max=32,message="{valid_sku_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;

    @Override
	public void clear() {
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.warehouseId = null;
		super.clear();
	}

	public String getBomId() {
		return bomId;
	}
	

	public void setBomId(String bomId) {
		this.bomId = bomId;
	}
	


	public String getWarehouseId() {
		return warehouseId;
	}
	

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	

	public String getOrgId() {
		return orgId;
	}
	

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public String getSkuBomId() {
		return skuBomId;
	}
	

	public void setSkuBomId(String skuBomId) {
		this.skuBomId = skuBomId;
	}
	
	
}