package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 辅材bom实体类
 * @author tphe06 2017年2月14日
 */
public class MetaMaterialBom extends BaseEntity {
	private static final long serialVersionUID = -3786589406112454534L;

	/**辅材bomId*/
	@Id
	private String materialBomId;

	/**bomId*/
	private String bomId;
    
	/**辅材ID*/
    private String materialId;

    /**辅材数量*/
    private Integer number;


	
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

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}


	public String getMaterialBomId() {
		return materialBomId;
	}
	

	public void setMaterialBomId(String materialBomId) {
		this.materialBomId = materialBomId;
	}
}