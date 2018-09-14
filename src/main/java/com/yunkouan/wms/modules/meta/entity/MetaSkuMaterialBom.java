package com.yunkouan.wms.modules.meta.entity;

import java.util.Date;

import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 货品辅材BOM实体类
 * @author tphe06 2017年2月14日
 */
public class MetaSkuMaterialBom extends BaseEntity {
	private static final long serialVersionUID = -3786589406112454534L;

	/**货品辅材BOMid*/
	@Id
	private String bomId;

	/**识别号*/
    private String identificationCode;
    
	/**货品代码摘要*/
    private String skuNoComment;

    /**货品信息摘要*/
    private String skuInfoComment;

    /**货品辅材BOM状态*/
    private Integer status;

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
	

	public String getSkuNoComment() {
		return skuNoComment;
	}
	

	public void setSkuNoComment(String skuNoComment) {
		this.skuNoComment = skuNoComment;
	}
	

	public String getSkuInfoComment() {
		return skuInfoComment;
	}
	

	public MetaSkuMaterialBom setSkuInfoComment(String skuInfoComment) {
		this.skuInfoComment = skuInfoComment;
		return this;
	}
	

	public Integer getStatus() {
		return status;
	}
	

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getIdentificationCode() {
		return identificationCode;
	}

	public MetaSkuMaterialBom setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
		return this;
	}
	
}