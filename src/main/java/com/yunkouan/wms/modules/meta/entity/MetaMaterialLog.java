package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class MetaMaterialLog extends BaseEntity {
	/**
	 * 
	 * @version 2017年3月13日 下午1:55:59<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7609792741588444684L;

	@Id
    @NotNull(message="{valid_materialLog_materialLogId_isNull}",groups={ValidUpdate.class})
    private String materialLogId;
	
	@NotNull(message="{valid_materialLog_materialId_isNull}",groups={ValidUpdate.class})
	private String materialId;
	
    /**
     * 辅材日志类型
     */
    @NotNull(message="{valid_materialLog_type_is_null}",groups={ValidSave.class})
    private Integer materialLogType;

    /**
     * 相关单号
     */
    @Length(max=32,message="{valid_materialLog_involve_bill_length}",groups={ValidSave.class})
    private String involveBill;

    /**
     * 辅材日志备注
     */
    private String note;
    
    private String orgId;

    private String warehouseId;
    
    private int changeQty;

    private int resultQty;
    
    
    public String getMaterialLogId() {
        return materialLogId;
    }

    public void setMaterialLogId(String materialLogId) {
        this.materialLogId = materialLogId == null ? null : materialLogId.trim();
    }

    public Integer getMaterialLogType() {
        return materialLogType;
    }

    public void setMaterialLogType(Integer materialLogType) {
        this.materialLogType = materialLogType;
    }

    public String getInvolveBill() {
        return involveBill;
    }

    public void setInvolveBill(String involveBill) {
        this.involveBill = involveBill == null ? null : involveBill.trim();
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

	/**
	 * 属性 materialId getter方法
	 * @return 属性materialId
	 * @author 王通<br/>
	 */
	public String getMaterialId() {
		return materialId;
	}

	/**
	 * 属性 materialId setter方法
	 * @param materialId 设置属性materialId的值
	 * @author 王通<br/>
	 */
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	/**
	 * 属性 changeQty getter方法
	 * @return 属性changeQty
	 * @author 王通<br/>
	 */
	public int getChangeQty() {
		return changeQty;
	}

	/**
	 * 属性 changeQty setter方法
	 * @param changeQty 设置属性changeQty的值
	 * @author 王通<br/>
	 */
	public void setChangeQty(int changeQty) {
		this.changeQty = changeQty;
	}

	/**
	 * 属性 resultQty getter方法
	 * @return 属性resultQty
	 * @author 王通<br/>
	 */
	public int getResultQty() {
		return resultQty;
	}

	/**
	 * 属性 resultQty setter方法
	 * @param resultQty 设置属性resultQty的值
	 * @author 王通<br/>
	 */
	public void setResultQty(int resultQty) {
		this.resultQty = resultQty;
	}

	/**
	 * 属性 note getter方法
	 * @return 属性note
	 * @author 王通<br/>
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 属性 note setter方法
	 * @param note 设置属性note的值
	 * @author 王通<br/>
	 */
	public void setNote(String note) {
		this.note = note;
	}

}