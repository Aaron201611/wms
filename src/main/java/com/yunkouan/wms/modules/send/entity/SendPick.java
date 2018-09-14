package com.yunkouan.wms.modules.send.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class SendPick extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1785838871168324218L;

	@Id
	private String pickId;

	/**
	 * 货主
	 */
    private String owner;
	/**
	 * 拣货状态
	 */
    private Integer pickStatus;
	/**
	 * 企业id
	 */
    private String orgId;
	/**
	 * 父id
	 */
    private String parentId;
	/**
	 * 仓库id
	 */
    private String warehouseId;
	/**
	 * 源单号
	 */
    private String receiptNo;
	/**
	 * 单据类型
	 */
    private Integer docType;
	/**
	 * 发货单id
	 */
    @Length(max=64,message="{valid_send_pick_deliveryId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String deliveryId;
	/**
	 * 波次单id
	 */
    @Length(max=64,message="{valid_send_pick_waveId_length}",groups={ValidSave.class,ValidUpdate.class})
    private String waveId;
	/**
	 * 拣货单号
	 */
    private String pickNo;
	/**
	 * 作业人员
	 */
    @Length(max=64,message="{valid_send_pick_opPerson_length}",groups={ValidSave.class,ValidUpdate.class})
    private String opPerson;
	/**
	 * 作业时间
	 */
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private Date opTime;
	/**
	 * 计划拣货数量
	 */
    @Max(value=Integer.MAX_VALUE,message="{valid_send_pick_planPickQty_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_send_pick_planPickQty_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double planPickQty;
	/**
	 * 计划拣货重量
	 */
    @DecimalMax(value="999999999",message="{valid_send_pick_planPickWeight_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_pick_planPickWeight_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double planPickWeight;
	/**
	 * 计划拣货体积
	 */
    @DecimalMax(value="999999999",message="{valid_send_pick_planPickVolume_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_pick_planPickVolume_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double planPickVolume;
	/**
	 * 实际拣货梳理
	 */
    private Double realPickQty;
	/**
	 * 实际拣货重量
	 */
    private Double realPickWeight;
	/**
	 * 实际拣货体积
	 */
    private Double realPickVolume;
    
    private Integer pickBillPrintTimes;

    private Integer pickId2;
	/**
	 * 备注
	 */
    @Length(max=512,message="{valid_send_pick_remark_length}",groups={ValidSave.class,ValidUpdate.class})
    private String remark;
    
    private String expressServiceCode;
    private String expressBillNo;

    /**发送辅助系统状态，1已经发送，2辅助系统返回成功，3辅助系统返回失败**/
    private String assisStatus;

    public String getPickId() {
        return pickId;
    }

    public void setPickId(String pickId) {
        this.pickId = pickId == null ? null : pickId.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public Integer getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(Integer pickStatus) {
        this.pickStatus = pickStatus;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo == null ? null : receiptNo.trim();
    }

    public Integer getDocType() {
        return docType;
    }

    public void setDocType(Integer docType) {
        this.docType = docType;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId == null ? null : deliveryId.trim();
    }

    public String getWaveId() {
        return waveId;
    }

    public void setWaveId(String waveId) {
        this.waveId = waveId == null ? null : waveId.trim();
    }

    public String getPickNo() {
        return pickNo;
    }

    public void setPickNo(String pickNo) {
        this.pickNo = pickNo == null ? null : pickNo.trim();
    }

    public String getOpPerson() {
        return opPerson;
    }

    public void setOpPerson(String opPerson) {
        this.opPerson = opPerson == null ? null : opPerson.trim();
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public Integer getPickId2() {
        return pickId2;
    }

    public void setPickId2(Integer pickId2) {
        this.pickId2 = pickId2;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getPlanPickWeight() {
		return planPickWeight;
	}

	public void setPlanPickWeight(Double planPickWeight) throws Exception {
		this.planPickWeight = NumberUtil.round(planPickWeight,2);
	}

	public Double getPlanPickVolume() {
		return planPickVolume;
	}

	public void setPlanPickVolume(Double planPickVolume) throws Exception {
		this.planPickVolume = NumberUtil.round(planPickVolume,6);
	}

	public Double getRealPickWeight() {
		return realPickWeight;
	}

	public void setRealPickWeight(Double realPickWeight) throws Exception {
		this.realPickWeight = NumberUtil.round(realPickWeight,2);
	}

	public Double getRealPickVolume() {
		return realPickVolume;
	}

	public void setRealPickVolume(Double realPickVolume) throws Exception {
		this.realPickVolume = NumberUtil.round(realPickVolume,6);
	}
	
	public void defaultValue() throws Exception{
    	this.planPickQty = this.planPickQty == null?0:this.planPickQty;
    	setPlanPickWeight(this.planPickWeight == null?0.0:this.planPickWeight);
    	setPlanPickVolume(this.planPickVolume == null?0.0:this.planPickVolume);
    	this.realPickQty = this.realPickQty == null?0:this.realPickQty;
    	setRealPickWeight(this.realPickWeight == null?0.0:this.realPickWeight);
    	setRealPickVolume(this.realPickVolume == null?0.0:this.realPickVolume);
    }

	public Double getPlanPickQty() {
		return planPickQty;
	}
	

	public void setPlanPickQty(Double planPickQty) {
		this.planPickQty = planPickQty;
	}
	

	public Double getRealPickQty() {
		return realPickQty;
	}
	

	public void setRealPickQty(Double realPickQty) {
		this.realPickQty = realPickQty;
	}

	public String getExpressServiceCode() {
		return expressServiceCode;
	}

	public void setExpressServiceCode(String expressServiceCode) {
		this.expressServiceCode = expressServiceCode;
	}

	public String getExpressBillNo() {
		return expressBillNo;
	}

	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}

	public Integer getPickBillPrintTimes() {
		return pickBillPrintTimes;
	}

	public void setPickBillPrintTimes(Integer pickBillPrintTimes) {
		this.pickBillPrintTimes = pickBillPrintTimes;
	}

	public String getAssisStatus() {
		return assisStatus;
	}

	public void setAssisStatus(String assisStatus) {
		this.assisStatus = assisStatus;
	}

    
}