/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:28:28<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;

/**
 * 上架单实体类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:28:28<br/>
 * @author andy wang<br/>
 */
public class RecPutaway extends BaseEntity {

    /**
	 * Date:2017年2月8日 下午1:50:26<br/>
	 * @author andy wang<br/>
	 */
	private static final long serialVersionUID = -8258762149113744527L;
	
	/**
     * 上架单主键
	 * @author andy wang<br/>
     */
	@Id
	@NotNull(message="{valid_rec_putaway_id_notnull}",groups={ValidSplit.class, ValidUpdate.class})
    private String putawayId;
    
    /**
     * 上架单号
	 * @author andy wang<br/>
     */
    private String putawayNo;

    /**
     * 上架状态
	 * @author andy wang<br/>
     */
    private Integer putawayStatus;

    /**
     * 企业id
	 * @author andy wang<br/>
     */
    private String orgId;
    
    /**
     * 自动执行状态
	 * @author andy wang<br/>
     */
    private Integer autoStatus;
    
    /**
     * 父上架单
	 * @author andy wang<br/>
     */
    private String parentPutawayId;
    
    /**
     * 仓库代码
	 * @author andy wang<br/>
     */
    private String warehouseId;

    /**
     * 作业人员
	 * @author andy wang<br/>
     */
    private String opPerson;

    /**
     * 作业时间
	 * @author andy wang<br/>
     */
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date opTime;
    
    /**
     * 备用ID
	 * @author andy wang<br/>
     */
    private Integer putawayId2;
    
    /**
	 * 单据类型
	 * @version 2017年2月28日下午2:43:26<br/>
	 * @author andy wang<br/>
	 */
    private Integer docType;
    
    /**
	 * 计划上架数量
	 * @version 2017年3月3日上午10:38:07<br/>
	 * @author andy wang<br/>
	 */
    private Double planQty;
    
    /**
	 * 计划上架重量
	 * @version 2017年3月3日上午10:38:19<br/>
	 * @author andy wang<br/>
	 */
    private Double planWeight;
    
    /**
	 * 计划上架体积
	 * @version 2017年3月3日上午10:38:32<br/>
	 * @author andy wang<br/>
	 */
    private Double planVolume;

    /**
	 * 实际上架数量
	 * @version 2017年3月3日上午10:38:40<br/>
	 * @author andy wang<br/>
	 */
    private Double realQty;
    
    /**
	 * 实际上架重量
	 * @version 2017年3月3日上午10:38:43<br/>
	 * @author andy wang<br/>
	 */
    private Double realWeight;
    
    /**
	 * 实际上架体积
	 * @version 2017年3月3日上午10:38:48<br/>
	 * @author andy wang<br/>
	 */
    private Double realVolume;

    /**
	 * PO单号
	 * @version 2017年3月23日下午2:26:39<br/>
	 * @author andy wang<br/>
	 */
    private String poNo;
    
    /**
	 * ASN单号
	 * @version 2017年3月23日下午2:26:42<br/>
	 * @author andy wang<br/>
	 */
    private String asnNo;
//    
//    /**
//     * 申请单号
//     */
//    private String gatejobNo;
    
//    /**
//     * 发送状态
//     */
//    private String transStatus;
    
    
    /**
	 * 备注
	 * @version 2017年4月6日下午12:38:52<br/>
	 * @author andy wang<br/>
	 */
    private String note;

    /**发送辅助系统状态，1已经发送，2辅助系统返回成功，3辅助系统返回失败**/
    private String assisStatus;

    /* getset ***********************************/
    
	/**
	 * 属性 putawayId getter方法
	 * @return 属性putawayId
	 * @author andy wang<br/>
	 */
	public String getPutawayId() {
		return putawayId;
	}

	/**
	 * 属性 putawayId setter方法
	 * @param putawayId 设置属性putawayId的值
	 * @author andy wang<br/>
	 */
	public void setPutawayId(String putawayId) {
		this.putawayId = putawayId;
	}

	/**
	 * 属性 note getter方法
	 * @return 属性note
	 * @author andy wang<br/>
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 属性 note setter方法
	 * @param note 设置属性note的值
	 * @author andy wang<br/>
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * 属性 poNo getter方法
	 * @return 属性poNo
	 * @author andy wang<br/>
	 */
	public String getPoNo() {
		return poNo;
	}

	/**
	 * 属性 poNo setter方法
	 * @param poNo 设置属性poNo的值
	 * @author andy wang<br/>
	 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	/**
	 * 属性 asnNo getter方法
	 * @return 属性asnNo
	 * @author andy wang<br/>
	 */
	public String getAsnNo() {
		return asnNo;
	}

	/**
	 * 属性 asnNo setter方法
	 * @param asnNo 设置属性asnNo的值
	 * @author andy wang<br/>
	 */
	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}


	/**
	 * 属性 planWeight getter方法
	 * @return 属性planWeight
	 * @author andy wang<br/>
	 */
	public Double getPlanWeight() {
		return planWeight;
	}

	/**
	 * 属性 planWeight setter方法
	 * @param planWeight 设置属性planWeight的值
	 * @author andy wang<br/>
	 */
	public void setPlanWeight(Double planWeight) {
		this.planWeight = planWeight;
	}

	/**
	 * 属性 planVolume getter方法
	 * @return 属性planVolume
	 * @author andy wang<br/>
	 */
	public Double getPlanVolume() {
		return planVolume;
	}

	/**
	 * 属性 planVolume setter方法
	 * @param planVolume 设置属性planVolume的值
	 * @author andy wang<br/>
	 */
	public void setPlanVolume(Double planVolume) {
		this.planVolume = planVolume;
	}

	/**
	 * 属性 realWeight getter方法
	 * @return 属性realWeight
	 * @author andy wang<br/>
	 */
	public Double getRealWeight() {
		return realWeight;
	}

	/**
	 * 属性 realWeight setter方法
	 * @param realWeight 设置属性realWeight的值
	 * @author andy wang<br/>
	 */
	public void setRealWeight(Double realWeight) {
		this.realWeight = realWeight;
	}

	/**
	 * 属性 realVolume getter方法
	 * @return 属性realVolume
	 * @author andy wang<br/>
	 */
	public Double getRealVolume() {
		return realVolume;
	}

	/**
	 * 属性 realVolume setter方法
	 * @param realVolume 设置属性realVolume的值
	 * @author andy wang<br/>
	 */
	public void setRealVolume(Double realVolume) {
		this.realVolume = realVolume;
	}

	/**
	 * 属性 putawayNo getter方法
	 * @return 属性putawayNo
	 * @author andy wang<br/>
	 */
	public String getPutawayNo() {
		return putawayNo;
	}

	/**
	 * 属性 putawayNo setter方法
	 * @param putawayNo 设置属性putawayNo的值
	 * @author andy wang<br/>
	 */
	public void setPutawayNo(String putawayNo) {
		this.putawayNo = putawayNo;
	}

	/**
	 * 属性 putawayStatus getter方法
	 * @return 属性putawayStatus
	 * @author andy wang<br/>
	 */
	public Integer getPutawayStatus() {
		return putawayStatus;
	}

	/**
	 * 属性 putawayStatus setter方法
	 * @param putawayStatus 设置属性putawayStatus的值
	 * @author andy wang<br/>
	 */
	public void setPutawayStatus(Integer putawayStatus) {
		this.putawayStatus = putawayStatus;
	}

	/**
	 * 属性 orgId getter方法
	 * @return 属性orgId
	 * @author andy wang<br/>
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * 属性 orgId setter方法
	 * @param orgId 设置属性orgId的值
	 * @author andy wang<br/>
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 属性 warehouseId getter方法
	 * @return 属性warehouseId
	 * @author andy wang<br/>
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * 属性 warehouseId setter方法
	 * @param warehouseId 设置属性warehouseId的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * 属性 opPerson getter方法
	 * @return 属性opPerson
	 * @author andy wang<br/>
	 */
	public String getOpPerson() {
		return opPerson;
	}

	/**
	 * 属性 opPerson setter方法
	 * @param opPerson 设置属性opPerson的值
	 * @author andy wang<br/>
	 */
	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson;
	}

	/**
	 * 属性 opTime getter方法
	 * @return 属性opTime
	 * @author andy wang<br/>
	 */
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * 属性 opTime setter方法
	 * @param opTime 设置属性opTime的值
	 * @author andy wang<br/>
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}


	/**
	 * 属性 putawayId2 getter方法
	 * @return 属性putawayId2
	 * @author andy wang<br/>
	 */
	public Integer getPutawayId2() {
		return putawayId2;
	}

	/**
	 * 属性 putawayId2 setter方法
	 * @param putawayId2 设置属性putawayId2的值
	 * @author andy wang<br/>
	 */
	public void setPutawayId2(Integer putawayId2) {
		this.putawayId2 = putawayId2;
	}

	/**
	 * 属性 parentPutawayId getter方法
	 * @return 属性parentPutawayId
	 * @author andy wang<br/>
	 */
	public String getParentPutawayId() {
		return parentPutawayId;
	}

	/**
	 * 属性 parentPutawayId setter方法
	 * @param parentPutawayId 设置属性parentPutawayId的值
	 * @author andy wang<br/>
	 */
	public void setParentPutawayId(String parentPutawayId) {
		this.parentPutawayId = parentPutawayId;
	}

	/**
	 * 属性 docType getter方法
	 * @return 属性docType
	 * @author andy wang<br/>
	 */
	public Integer getDocType() {
		return docType;
	}

	/**
	 * 属性 docType setter方法
	 * @param docType 设置属性docType的值
	 * @author andy wang<br/>
	 */
	public void setDocType(Integer docType) {
		this.docType = docType;
	}

	public Double getPlanQty() {
		return planQty;
	}
	

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}
	

	public Double getRealQty() {
		return realQty;
	}
	

	public void setRealQty(Double realQty) {
		this.realQty = realQty;
	}

	public String getAssisStatus() {
		return assisStatus;
	}

	public void setAssisStatus(String assisStatus) {
		this.assisStatus = assisStatus;
	}

	public Integer getAutoStatus() {
		return autoStatus;
	}

	public void setAutoStatus(Integer autoStatus) {
		this.autoStatus = autoStatus;
	}
	


	
	

}