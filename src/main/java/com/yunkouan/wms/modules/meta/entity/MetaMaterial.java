package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 辅材实体类
 * @author 王通 2017年8月22日10:47:48
 */
public class MetaMaterial extends BaseEntity {
	private static final long serialVersionUID = -3786589406112454534L;

	/**辅材id*/
	@Id
	private String materialId;

	/**辅材代码*/
	@Length(max=32,message="{valid_material_materialNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_material_materialNo_notnull}",groups={ValidUpdate.class})
    private String materialNo;

    /**辅材名称*/
	@Length(max=32,message="{valid_material_materialName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_material_materialName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String materialName;
	/**辅材类型id*/
	@NotNull(message="{valid_material_materialTypeId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String materialTypeId;

    /**辅材条码*/
	@Length(max=32,message="{valid_material_materialBarCode_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String materialBarCode;

    /**辅材状态*/
    private Integer materialStatus;

    /**组织id*/
	@Length(max=32,message="{valid_material_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;

//    /**仓库id*/
//	@Length(max=32,message="{valid_material_warehouseId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//    private String warehouseId;

    /**计量单位*/
	@Length(max=32,message="{valid_material_measureUnit_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_material_measureUnit_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String measureUnit;

    /**规格型号*/
	@Length(max=32,message="{valid_material_specModel_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_material_specModel_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String specModel;
	/**库存数量*/
	private Integer qty;
	
    /**
	 * 辅材宽度
	 * @version 2017年5月16日 下午6:22:09<br/>
	 * @author 王通<br/>
	 */
    private Double packWidth;
    /**
	 * 辅材高度
	 * @version 2017年5月16日 下午6:22:09<br/>
	 * @author 王通<br/>
	 */
    private Double packHeight;
    /**
	 * 辅材长度
	 * @version 2017年5月16日 下午6:22:09<br/>
	 * @author 王通<br/>
	 */
    private Double packLength;
    
    private Double packVolume;
    
    /**警戒库存**/
    private Integer warningQty;

    /**单体*/
    private Double perVolume;

    /**单重*/
    private Double perWeight;

    /**
	 * 属性 packWidth getter方法
	 * @return 属性packWidth
	 * @author 王通<br/>
	 */
	public Double getPackWidth() {
		return packWidth;
	}

	/**
	 * 属性 packWidth setter方法
	 * @param packWidth 设置属性packWidth的值
	 * @author 王通<br/>
	 */
	public void setPackWidth(Double packWidth) {
		this.packWidth = packWidth;
	}

	/**
	 * 属性 packHeight getter方法
	 * @return 属性packHeight
	 * @author 王通<br/>
	 */
	public Double getPackHeight() {
		return packHeight;
	}

	/**
	 * 属性 packHeight setter方法
	 * @param packHeight 设置属性packHeight的值
	 * @author 王通<br/>
	 */
	public void setPackHeight(Double packHeight) {
		this.packHeight = packHeight;
	}

	/**
	 * 属性 packLength getter方法
	 * @return 属性packLength
	 * @author 王通<br/>
	 */
	public Double getPackLength() {
		return packLength;
	}

	/**
	 * 属性 packLength setter方法
	 * @param packLength 设置属性packLength的值
	 * @author 王通<br/>
	 */
	public void setPackLength(Double packLength) {
		this.packLength = packLength;
	}

	/**
	 * 属性 warningQty getter方法
	 * @return 属性warningQty
	 * @author 王通<br/>
	 */
	public Integer getWarningQty() {
		return warningQty;
	}

	/**
	 * 属性 warningQty setter方法
	 * @param warningQty 设置属性warningQty的值
	 * @author 王通<br/>
	 */
	public void setWarningQty(Integer warningQty) {
		this.warningQty = warningQty;
	}

	public String getMaterialId() {
        return materialId;
    }

    public MetaMaterial setMaterialId(String materialId) {
        this.materialId = materialId == null ? null : materialId.trim();
        return this;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public MetaMaterial setMaterialNo(String materialNo) {
        this.materialNo = materialNo == null ? null : materialNo.trim();
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public MetaMaterial setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
        return this;
    }

    public String getMaterialBarCode() {
        return materialBarCode;
    }

    public MetaMaterial setMaterialBarCode(String materialBarCode) {
        this.materialBarCode = materialBarCode == null ? null : materialBarCode.trim();
        return this;
    }

    public Integer getMaterialStatus() {
        return materialStatus;
    }

    public MetaMaterial setMaterialStatus(Integer materialStatus) {
        this.materialStatus = materialStatus;
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public MetaMaterial setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public MetaMaterial setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
        return this;
    }

    public String getSpecModel() {
        return specModel;
    }

    public MetaMaterial setSpecModel(String specModel) {
        this.specModel = specModel == null ? null : specModel.trim();
        return this;
    }

    public Double getPerVolume() {
        return perVolume;
    }

    public MetaMaterial setPerVolume(Double perVolume) {
        this.perVolume = perVolume;
        return this;
    }

    public Double getPerWeight() {
        return perWeight;
    }

    public MetaMaterial setPerWeight(Double perWeight) {
        this.perWeight = perWeight;
        return this;
    }

	/**
	 * 属性 qty getter方法
	 * @return 属性qty
	 * @author 王通<br/>
	 */
	public Integer getQty() {
		return qty;
	}

	/**
	 * 属性 qty setter方法
	 * @param qty 设置属性qty的值
	 * @author 王通<br/>
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}

	/**
	 * 属性 packVolume getter方法
	 * @return 属性packVolume
	 * @author 王通<br/>
	 */
	public Double getPackVolume() {
		return packVolume;
	}

	/**
	 * 属性 packVolume setter方法
	 * @param packVolume 设置属性packVolume的值
	 * @author 王通<br/>
	 */
	public void setPackVolume(Double packVolume) {
		this.packVolume = packVolume;
	}

	/**
	 * 属性 materialTypeId getter方法
	 * @return 属性materialTypeId
	 * @author 王通<br/>
	 */
	public String getMaterialTypeId() {
		return materialTypeId;
	}

	/**
	 * 属性 materialTypeId setter方法
	 * @param materialTypeId 设置属性materialTypeId的值
	 * @author 王通<br/>
	 */
	public void setMaterialTypeId(String materialTypeId) {
		this.materialTypeId = materialTypeId;
	}



}