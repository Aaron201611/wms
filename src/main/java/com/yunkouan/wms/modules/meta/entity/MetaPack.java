package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 包装实体类
 * @author tphe06 2017年2月14日
 */
public class MetaPack extends BaseEntity {
	private static final long serialVersionUID = 3022179229395756292L;

	/**包装id*/
	@Id
	private String packId;

	/**包装编码*/
	@Length(max=32,message="{valid_pack_packNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_pack_packNo_notnull}",groups={ValidUpdate.class})
    private String packNo;

    /**包装单位*/
	@Length(max=32,message="{valid_pack_packUnit_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_pack_packUnit_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String packUnit;

    /**包装级别*/
	@Length(max=8,message="{valid_pack_packLevel_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
//	@NotNull(message="{valid_pack_packLevel_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String packLevel;

    /**包装状态*/
//	@NotNull(message="{valid_pack_packStatus_notnull}",groups={ValidUpdate.class})
    private Integer packStatus;

    /**长（M）*/
    private Double packLength;

    /**宽（M）*/
    private Double packWide;

    /**高（M）*/
    private Double packHeight;

    /**组织id*/
	@Length(max=32,message="{valid_pack_orgId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String orgId;

    /**仓库id*/
	@Length(max=32,message="{valid_pack_warehouseId_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String warehouseId;

    /**体积（CBM）*/
    private Double volume;
    private Double weight;

    /**托盘系数*/
    private Double trayPercent;

    /**包装系数**/
    @Transient
    private Double packPercent;
    private String note;

    private Integer packId2;

    @Override
	public void clear() {
    	/**不能由前端修改，添加修改时候都赋空值**/
    	this.packId2 = null;
    	/**添加时候由程序赋值，修改时候置空不更新数据库的值，生效/激活/取消时候才更新数据库的值**/
    	this.packStatus = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.orgId = null;
    	/**不能由前端修改，添加时候取当前登录人的，修改时候置空不更新数据库的值**/
    	this.warehouseId = null;
		super.clear();
	}

    public String getPackId() {
        return packId;
    }

    public MetaPack setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
        return this;
    }

    public String getPackNo() {
        return packNo;
    }

    public MetaPack setPackNo(String packNo) {
        this.packNo = packNo == null ? null : packNo.trim();
        return this;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public MetaPack setPackUnit(String packUnit) {
        this.packUnit = packUnit == null ? null : packUnit.trim();
        return this;
    }

    public String getPackLevel() {
        return packLevel;
    }

    public MetaPack setPackLevel(String packLevel) {
        this.packLevel = packLevel == null ? null : packLevel.trim();
        return this;
    }

    public Double getPackLength() {
        return packLength;
    }

    public MetaPack setPackLength(Double packLength) {
        this.packLength = packLength;
        return this;
    }

    public Double getPackWide() {
        return packWide;
    }

    public MetaPack setPackWide(Double packWide) {
        this.packWide = packWide;
        return this;
    }

    public Double getPackHeight() {
        return packHeight;
    }

    public MetaPack setPackHeight(Double packHeight) {
        this.packHeight = packHeight;
        return this;
    }

    public String getOrgId() {
        return orgId;
    }

    public MetaPack setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
        return this;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public MetaPack setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
        return this;
    }

    public Double getVolume() {
        return volume;
    }

    public MetaPack setVolume(Double volume) {
        this.volume = volume;
        return this;
    }

    public Double getTrayPercent() {
        return trayPercent;
    }

    public MetaPack setTrayPercent(Double trayPercent) {
        this.trayPercent = trayPercent;
        return this;
    }

    public Integer getPackId2() {
        return packId2;
    }

    public MetaPack setPackId2(Integer packId2) {
        this.packId2 = packId2;
        return this;
    }

	public Integer getPackStatus() {
		return packStatus;
	}

	public MetaPack setPackStatus(Integer packStatus) {
		this.packStatus = packStatus;
		return this;
	}

	public Double getPackPercent() {
		return packPercent;
	}

	public MetaPack setPackPercent(Double packPercent) {
		this.packPercent = packPercent;
		return this;
	}

	public Double getWeight() {
		return weight;
	}

	public MetaPack setWeight(Double weight) {
		this.weight = weight;
		return this;
	}

	public String getNote() {
		return note;
	}

	public MetaPack setNote(String note) {
		this.note = note;
		return this;
	}
}