package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 货品类型实体类
 * @author tphe06 2017年2月14日
 */
public class MetaSkuType extends BaseEntity {
	private static final long serialVersionUID = -6339910864133681162L;

	/**货品类型id*/
	@Id
	private String skuTypeId;

	/**货品类型编码*/
	@Length(max=32,message="{valid_skuType_skuTypeNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_skuType_skuTypeNo_notnull}",groups={ValidUpdate.class})
    private String skuTypeNo;

    /**货品类型名称*/
	@Length(max=16,message="{valid_skuType_skuTypeName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_skuType_skuTypeName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String skuTypeName;

    /**货品类型状态*/
	@NotNull(message="{valid_skuType_skuTypeStatus_notnull}",groups={ValidUpdate.class})
    private Integer skuTypeStatus;

    /**上级货品类型id*/
	@Length(max=64,message="{valid_skuType_parentId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String parentId;

	/**levelCode:层级代码**/
	@Length(max=64,message="{valid_skuType_levelCode_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	private String levelCode;

	/**@Fields 所属客户id */
	private String owner;
	
	/**@Fields 所属企业id */
	private String orgId;

	/**@Fields 备注 */
	private String note;

	private Integer skuTypeId2;

    @Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.skuTypeId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.skuTypeStatus = null;
		super.clear();
	}

    public String getSkuTypeId() {
        return skuTypeId;
    }

    public MetaSkuType setSkuTypeId(String skuTypeId) {
        this.skuTypeId = skuTypeId == null ? null : skuTypeId.trim();
        return this;
    }

    public String getSkuTypeNo() {
        return skuTypeNo;
    }

    public MetaSkuType setSkuTypeNo(String skuTypeNo) {
        this.skuTypeNo = skuTypeNo == null ? null : skuTypeNo.trim();
        return this;
    }

    public String getSkuTypeName() {
        return skuTypeName;
    }

    public MetaSkuType setSkuTypeName(String skuTypeName) {
        this.skuTypeName = skuTypeName == null ? null : skuTypeName.trim();
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public MetaSkuType setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
        return this;
    }

    public Integer getSkuTypeId2() {
        return skuTypeId2;
    }

    public MetaSkuType setSkuTypeId2(Integer skuTypeId2) {
        this.skuTypeId2 = skuTypeId2;
        return this;
    }

	public Integer getSkuTypeStatus() {
		return skuTypeStatus;
	}

	public MetaSkuType setSkuTypeStatus(Integer skuTypeStatus) {
		this.skuTypeStatus = skuTypeStatus;
		return this;
	}


	public String getNote() {
		return note;
	}


	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public MetaSkuType setNote(String note) {
		this.note = note;
		return this;
	}

	/**
	 * 属性 orgId getter方法
	 * @return 属性orgId
	 * @author 王通<br/>
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * 属性 orgId setter方法
	 * @param orgId 设置属性orgId的值
	 * @author 王通<br/>
	 */
	public MetaSkuType setOrgId(String orgId) {
		this.orgId = orgId;
		return this;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public MetaSkuType setLevelCode(String levelCode) {
		this.levelCode = levelCode;
		return this;
	}
}