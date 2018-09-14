package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 货品实体类
 * @author tphe06 2017年2月14日
 */
public class MetaSku extends BaseEntity {
	private static final long serialVersionUID = -3786589406112454534L;

	/**货品id*/
	@Id
	private String skuId;

	/**货品代码*/
	@Length(max=32,message="{valid_sku_skuNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_sku_skuNo_notnull}",groups={ValidUpdate.class})
    private String skuNo;

    /**货品名称*/
	@Length(max=128,message="{valid_sku_skuName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_sku_skuName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String skuName;

    /**货主id*/
	@Length(max=32,message="{valid_sku_owner_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_sku_owner_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String owner;

    /**货品条码*/
	@Length(max=32,message="{valid_sku_skuBarCode_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String skuBarCode;

    /**货品状态*/
    private Integer skuStatus;

    /**组织id*/
	@Length(max=32,message="{valid_sku_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;
	
	/**仓库id*/
	@Length(max=32,message="{valid_sku_warehouseId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String warehouseId;

    /**计量单位名称*/
	@Length(max=32,message="{valid_sku_measureUnit_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_sku_measureUnit_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String measureUnit;
	
	/**第一（法定）计量单位*/
	@Column(name="unit_1")
	private String unit1;
	
	/**第二计量单位*/
	@Column(name="unit_2")
	private String unit2;

    /**规格型号*/
	@Length(max=32,message="{valid_sku_specModel_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_sku_specModel_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String specModel;

    /**货品类型2 id*/
	@Length(max=32,message="{valid_sku_skuTypeId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_sku_skuTypeId_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String skuTypeId;

    /**保质期（天）*/
    private Integer shelflife;
    /**预警期（天）*/
    private Integer overdueWarningDays;
    /**
	 * 货品宽度
	 * @version 2017年5月16日 下午6:22:09<br/>
	 * @author 王通<br/>
	 */
    private Double width;
    /**
	 * 货品高度
	 * @version 2017年5月16日 下午6:22:09<br/>
	 * @author 王通<br/>
	 */
    private Double height;
    /**
	 * 货品长度
	 * @version 2017年5月16日 下午6:22:09<br/>
	 * @author 王通<br/>
	 */
    private Double length;
    
    /**最小安全库存*/
    private Integer minSafetyStock;
    /**最大安全库存*/
    private Integer maxSafetyStock;

    /**minReplenishStock:补货警戒数量**/
    private Integer minReplenishStock;
    /**maxReplenishStock:补货上限数量**/
    private Integer maxReplenishStock;
    
    /**项号*/
    private String gNo;
    
    /**料件性质*/
    private Integer goodsNature;
    
    /**币制*/
    private String curr;
    
    /**原产国*/
    private String originCountry;

    /**单体*/
    private Double perVolume;

    /**单重*/
    private Double perWeight;

	@Length(max=512,message="{valid_sku_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String note;
	
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private Integer skuId2;

    // 芜湖综保区
    /**hgGoodsNo:海关货号**/
    private String hgGoodsNo;
    /**hsCode:海关归类税号**/
    private String hsCode;
    /**perPrice:单价（元）**/
    private Double perPrice;
    /**declarePrice:申报价（元）**/
    private Double declarePrice;
    /**brand:品牌**/
    private String brand;
    /**measureUnitCode:计量单位代码**/
    private String measureUnitCode;

    @Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.skuId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
//    	this.skuStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.warehouseId = null;
		super.clear();
	}

    public String getSkuId() {
        return skuId;
    }

    public MetaSku setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
        return this;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public MetaSku setSkuNo(String skuNo) {
        this.skuNo = skuNo == null ? null : skuNo.trim();
        return this;
    }

    public String getSkuName() {
        return skuName;
    }

    public MetaSku setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public MetaSku setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
        return this;
    }

    public String getSkuBarCode() {
        return skuBarCode;
    }

    public MetaSku setSkuBarCode(String skuBarCode) {
        this.skuBarCode = skuBarCode == null ? null : skuBarCode.trim();
        return this;
    }

    public Integer getSkuStatus() {
        return skuStatus;
    }

    public MetaSku setSkuStatus(Integer skuStatus) {
        this.skuStatus = skuStatus;
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public MetaSku setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }
    
    public String getWarehouseId() {
        return warehouseId;
    }

    public MetaSku setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
        return this;
    }
    
    public String getMeasureUnit() {
        return measureUnit;
    }

    public MetaSku setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
        return this;
    }

    public String getSpecModel() {
        return specModel;
    }

    public MetaSku setSpecModel(String specModel) {
        this.specModel = specModel == null ? null : specModel.trim();
        return this;
    }

    public String getSkuTypeId() {
        return skuTypeId;
    }

    public MetaSku setSkuTypeId(String skuTypeId) {
        this.skuTypeId = skuTypeId == null ? null : skuTypeId.trim();
        return this;
    }

    public Integer getShelflife() {
        return shelflife;
    }

    public MetaSku setShelflife(Integer shelflife) {
        this.shelflife = shelflife;
        return this;
    }

    public String getNote() {
        return note;
    }

    public MetaSku setNote(String note) {
        this.note = note == null ? null : note.trim();
        return this;
    }

    public Integer getMinSafetyStock() {
        return minSafetyStock;
    }

    public MetaSku setMinSafetyStock(Integer minSafetyStock) {
        this.minSafetyStock = minSafetyStock;
        return this;
    }

    public Integer getMaxSafetyStock() {
        return maxSafetyStock;
    }

    public MetaSku setMaxSafetyStock(Integer maxSafetyStock) {
        this.maxSafetyStock = maxSafetyStock;
        return this;
    }

    public Double getPerVolume() {
        return perVolume;
    }

    public MetaSku setPerVolume(Double perVolume) {
        this.perVolume = perVolume;
        return this;
    }

    public Double getPerWeight() {
        return perWeight;
    }

    public MetaSku setPerWeight(Double perWeight) {
        this.perWeight = perWeight;
        return this;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public MetaSku setAttribute1(String attribute1) {
        this.attribute1 = attribute1 == null ? null : attribute1.trim();
        return this;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public MetaSku setAttribute2(String attribute2) {
        this.attribute2 = attribute2 == null ? null : attribute2.trim();
        return this;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public MetaSku setAttribute3(String attribute3) {
        this.attribute3 = attribute3 == null ? null : attribute3.trim();
        return this;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public MetaSku setAttribute4(String attribute4) {
        this.attribute4 = attribute4 == null ? null : attribute4.trim();
        return this;
    }

    public Integer getSkuId2() {
        return skuId2;
    }

    public MetaSku setSkuId2(Integer skuId2) {
        this.skuId2 = skuId2;
        return this;
    }

	/**
	 * 属性 width getter方法
	 * @return 属性width
	 * @author 王通<br/>
	 */
	public Double getWidth() {
		return width;
	}

	/**
	 * 属性 width setter方法
	 * @param width 设置属性width的值
	 * @author 王通<br/>
	 */
	public void setWidth(Double width) {
		this.width = width;
	}

	/**
	 * 属性 height getter方法
	 * @return 属性height
	 * @author 王通<br/>
	 */
	public Double getHeight() {
		return height;
	}

	/**
	 * 属性 height setter方法
	 * @param height 设置属性height的值
	 * @author 王通<br/>
	 */
	public void setHeight(Double height) {
		this.height = height;
	}

	/**
	 * 属性 length getter方法
	 * @return 属性length
	 * @author 王通<br/>
	 */
	public Double getLength() {
		return length;
	}

	/**
	 * 属性 length setter方法
	 * @param length 设置属性length的值
	 * @author 王通<br/>
	 */
	public void setLength(Double length) {
		this.length = length;
	}

	public Integer getMinReplenishStock() {
		return minReplenishStock;
	}

	public Integer getMaxReplenishStock() {
		return maxReplenishStock;
	}

	public void setMinReplenishStock(Integer minReplenishStock) {
		this.minReplenishStock = minReplenishStock;
	}

	public void setMaxReplenishStock(Integer maxReplenishStock) {
		this.maxReplenishStock = maxReplenishStock;
	}

	public String getHsCode() {
		return hsCode;
	}

	public Double getPerPrice() {
		return perPrice;
	}

	public Double getDeclarePrice() {
		return declarePrice;
	}

	public String getBrand() {
		return brand;
	}

	public MetaSku setHsCode(String hsCode) {
        this.hsCode = hsCode == null ? null : hsCode.trim();
		return this;
	}

	public void setPerPrice(Double perPrice) {
		this.perPrice = perPrice;
	}

	public void setDeclarePrice(Double declarePrice) {
		this.declarePrice = declarePrice;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMeasureUnitCode() {
		return measureUnitCode;
	}

	public void setMeasureUnitCode(String measureUnitCode) {
		this.measureUnitCode = measureUnitCode;
	}

	/**
	 * 属性 overdueWarningDays getter方法
	 * @return 属性overdueWarningDays
	 * @author 王通<br/>
	 */
	public Integer getOverdueWarningDays() {
		return overdueWarningDays;
	}

	/**
	 * 属性 overdueWarningDays setter方法
	 * @param overdueWarningDays 设置属性overdueWarningDays的值
	 * @author 王通<br/>
	 */
	public void setOverdueWarningDays(Integer overdueWarningDays) {
		this.overdueWarningDays = overdueWarningDays;
	}

	public String getHgGoodsNo() {
		return hgGoodsNo;
	}

	public MetaSku setHgGoodsNo(String hgGoodsNo) {
        this.hgGoodsNo = hgGoodsNo == null ? null : hgGoodsNo.trim();
        return this;
	}

	public String getgNo() {
		return gNo;
	}

	public void setgNo(String gNo) {
		this.gNo = gNo;
	}

	public Integer getGoodsNature() {
		return goodsNature;
	}

	public void setGoodsNature(Integer goodsNature) {
		this.goodsNature = goodsNature;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getUnit1() {
		return unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public String getUnit2() {
		return unit2;
	}

	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}
	
	
}