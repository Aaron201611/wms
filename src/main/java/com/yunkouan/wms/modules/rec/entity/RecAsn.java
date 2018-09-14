/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * Date: 2017年2月8日 下午2:58:52<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.util.valid.ValidBatchConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;


/**
 * ASN单实体类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午2:58:52<br/>
 * @author andy wang<br/>
 */
public class RecAsn extends BaseEntity {

	/**
	 * Date:2017年2月8日 下午12:34:02<br/>
	 * @author andy wang<br/>
	 */
	private static final long serialVersionUID = -2314635507900136229L;

	/**
     * ASN单主键
     * @author andy wang<br/>
     */
	@Id
	@NotNull(message="{valid_rec_asn_asnId_notnull}",groups={ValidSplit.class, ValidUpdate.class})
    private String asnId;
    
    /**
     * ASN单号
     * @author andy wang<br/>
     */
    private String asnNo;

    /**
     * ASN单状态
     * @author andy wang<br/>
     */
    private Integer asnStatus;
    
    /**
     * 拦截状态
     */
    private Integer interceptStatus;

    /**
     * PO单编号
     * @author andy wang<br/>
     */
    @Length(max=64,message="{valid_rec_asn_poNo_length}",groups={ValidSave.class,ValidUpdate.class})
    private String poNo;

    /**
     * 货主
     * @author andy wang<br/>
     */
    @NotNull(message="{valid_rec_asn_owner_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private String owner;

    /**
     * 组织编号
     * @author andy wang<br/>
     */
    private String orgId;

    /**
     * 父ASN主键
     * @author andy wang<br/>
     */
    private String parentAsnId;

    /**
     * 仓库代码
     * @author andy wang<br/>
     */
    private String warehouseId;

    /**
     * 相关单号1
     * @author andy wang<br/>
     */
    @Length(max=32,message="{valid_rec_asn_asnNo1_length}",groups={ValidSave.class,ValidUpdate.class})
    private String asnNo1;

    /**
     * 相关单号2
     * @author andy wang<br/>
     */
    @Length(max=32,message="{valid_rec_asn_asnNo2_length}",groups={ValidSave.class,ValidUpdate.class})
    private String asnNo2;

    /**
     * 单据类型
     * @author andy wang<br/>
     */
    @NotNull(message="{valid_rec_asn_docType_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private Integer docType;

    /**
     * 订单日期
     * @author andy wang<br/>
     */
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    @NotNull(message="{valid_rec_asn_orderDate_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private Date orderDate;

    /**
     * 发货方
     * @author andy wang<br/>
     */
    private String sender;

    /**
     * 收货数量
     * @author andy wang<br/>
     */
    private Double receiveQty;

    /**
     * 收货重量
     * @author andy wang<br/>
     */
    private Double receiveWeight;

    /**
     * 收货体积
     * @author andy wang<br/>
     */
    private Double receiveVolume;
    

    /**
     * 联系人
     * @author andy wang<br/>
     */
    @Length(max=64,message="{valid_rec_asn_contactPerson_length}",groups={ValidSave.class,ValidUpdate.class})
    private String contactPerson;

    /**
     * 联系电话
     * @author andy wang<br/>
     */
    @Length(max=16,message="{valid_rec_asn_contactPhone_length}",groups={ValidSave.class,ValidUpdate.class})
    private String contactPhone;

    /**
     * 联系地址
     * @author andy wang<br/>
     */
    @Length(max=512,message="{valid_rec_asn_contactAddress_length}",groups={ValidSave.class,ValidUpdate.class})
    private String contactAddress;
    
    /**
     * 备注
     * @author andy wang<br/>
     */
    @Length(max=2048,message="{valid_rec_asn_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidConfirm.class})
    private String note;

    /**
     * 单据来源
     * @author andy wang<br/>
     */
    private Integer dataFrom;

    /**
     * 订单数量
     * @author andy wang<br/>
     */
    private Double orderQty;

    /**
     * 订单重量
     * @author andy wang<br/>
     */
    private Double orderWeight;
    
    /**
     * 订单体积
     * @author andy wang<br/>
     */
    private Double orderVolume;

    /**
     * 预约到货时间
     * @author andy wang<br/>
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date appointmentTime;
    
    /**
     * 作业人员
     * @author andy wang<br/>
     */
    @Length(max=64,message="{valid_rec_asn_opPerson_length}",groups={ValidConfirm.class,ValidBatchConfirm.class})
    private String opPerson;
    
    /**
     * 作业时间
     * @author andy wang<br/>
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date opTime;
    
    /**
     * 同步erp状态
     */
    private Integer syncErpStatus;
    
    /**
     * 申请单号
     */
    private String gatejobNo;
    
    /**
     * 发送状态
     */
    private String transStatus;
    
    /**
     * 备用id
     * @author andy wang<br/>
     */
    private Integer asnId2;
    
    /**
     * 供应商
     */
    private String supplier;
    
    /**
     * 部门
     */
    private String department;
    /**
     * 业务员
     */
    private String clerk;

    /* getset *************************************************/
    
    
	/**
	 * 属性 asnId getter方法
	 * @return 属性asnId
	 * @author andy wang<br/>
	 */
	public String getAsnId() {
		return asnId;
	}

	/**
	 * 属性 asnId setter方法
	 * @param asnId 设置属性asnId的值
	 * @author andy wang<br/>
	 */
	public void setAsnId(String asnId) {
		this.asnId = asnId;
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
	 * 属性 asnStatus getter方法
	 * @return 属性asnStatus
	 * @author andy wang<br/>
	 */
	public Integer getAsnStatus() {
		return asnStatus;
	}

	/**
	 * 属性 asnStatus setter方法
	 * @param asnStatus 设置属性asnStatus的值
	 * @author andy wang<br/>
	 */
	public void setAsnStatus(Integer asnStatus) {
		this.asnStatus = asnStatus;
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
	 * 属性 owner getter方法
	 * @return 属性owner
	 * @author andy wang<br/>
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 属性 owner setter方法
	 * @param owner 设置属性owner的值
	 * @author andy wang<br/>
	 */
	public void setOwner(String owner) {
		this.owner = owner;
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
	 * 属性 parentAsnId getter方法
	 * @return 属性parentAsnId
	 * @author andy wang<br/>
	 */
	public String getParentAsnId() {
		return parentAsnId;
	}

	/**
	 * 属性 parentAsnId setter方法
	 * @param parentAsnId 设置属性parentAsnId的值
	 * @author andy wang<br/>
	 */
	public void setParentAsnId(String parentAsnId) {
		this.parentAsnId = parentAsnId;
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
	 * 属性 asnNo1 getter方法
	 * @return 属性asnNo1
	 * @author andy wang<br/>
	 */
	public String getAsnNo1() {
		return asnNo1;
	}

	/**
	 * 属性 asnNo1 setter方法
	 * @param asnNo1 设置属性asnNo1的值
	 * @author andy wang<br/>
	 */
	public void setAsnNo1(String asnNo1) {
		this.asnNo1 = asnNo1;
	}

	/**
	 * 属性 asnNo2 getter方法
	 * @return 属性asnNo2
	 * @author andy wang<br/>
	 */
	public String getAsnNo2() {
		return asnNo2;
	}

	/**
	 * 属性 asnNo2 setter方法
	 * @param asnNo2 设置属性asnNo2的值
	 * @author andy wang<br/>
	 */
	public void setAsnNo2(String asnNo2) {
		this.asnNo2 = asnNo2;
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

	/**
	 * 属性 orderDate getter方法
	 * @return 属性orderDate
	 * @author andy wang<br/>
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * 属性 orderDate setter方法
	 * @param orderDate 设置属性orderDate的值
	 * @author andy wang<br/>
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * 属性 sender getter方法
	 * @return 属性sender
	 * @author andy wang<br/>
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * 属性 sender setter方法
	 * @param sender 设置属性sender的值
	 * @author andy wang<br/>
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	

	/**
	 * 属性 receiveWeight getter方法
	 * @return 属性receiveWeight
	 * @author andy wang<br/>
	 */
	public Double getReceiveWeight() {
		return receiveWeight;
	}

	/**
	 * 属性 receiveWeight setter方法
	 * @param receiveWeight 设置属性receiveWeight的值
	 * @author andy wang<br/>
	 */
	public void setReceiveWeight(Double receiveWeight) {
		this.receiveWeight = receiveWeight;
	}

	/**
	 * 属性 receiveVolume getter方法
	 * @return 属性receiveVolume
	 * @author andy wang<br/>
	 */
	public Double getReceiveVolume() {
		return receiveVolume;
	}

	/**
	 * 属性 receiveVolume setter方法
	 * @param receiveVolume 设置属性receiveVolume的值
	 * @author andy wang<br/>
	 */
	public void setReceiveVolume(Double receiveVolume) {
		this.receiveVolume = receiveVolume;
	}

	/**
	 * 属性 contactPerson getter方法
	 * @return 属性contactPerson
	 * @author andy wang<br/>
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * 属性 contactPerson setter方法
	 * @param contactPerson 设置属性contactPerson的值
	 * @author andy wang<br/>
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * 属性 contactPhone getter方法
	 * @return 属性contactPhone
	 * @author andy wang<br/>
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * 属性 contactPhone setter方法
	 * @param contactPhone 设置属性contactPhone的值
	 * @author andy wang<br/>
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	/**
	 * 属性 contactAddress getter方法
	 * @return 属性contactAddress
	 * @author andy wang<br/>
	 */
	public String getContactAddress() {
		return contactAddress;
	}

	/**
	 * 属性 contactAddress setter方法
	 * @param contactAddress 设置属性contactAddress的值
	 * @author andy wang<br/>
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
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
	 * 属性 dataFrom getter方法
	 * @return 属性dataFrom
	 * @author andy wang<br/>
	 */
	public Integer getDataFrom() {
		return dataFrom;
	}

	/**
	 * 属性 dataFrom setter方法
	 * @param dataFrom 设置属性dataFrom的值
	 * @author andy wang<br/>
	 */
	public void setDataFrom(Integer dataFrom) {
		this.dataFrom = dataFrom;
	}


	public Double getOrderQty() {
		return orderQty;
	}
	

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	

	/**
	 * 属性 orderWeight getter方法
	 * @return 属性orderWeight
	 * @author andy wang<br/>
	 */
	public Double getOrderWeight() {
		return orderWeight;
	}

	/**
	 * 属性 orderWeight setter方法
	 * @param orderWeight 设置属性orderWeight的值
	 * @author andy wang<br/>
	 */
	public void setOrderWeight(Double orderWeight) {
		this.orderWeight = orderWeight;
	}

	/**
	 * 属性 orderVolume getter方法
	 * @return 属性orderVolume
	 * @author andy wang<br/>
	 */
	public Double getOrderVolume() {
		return orderVolume;
	}

	/**
	 * 属性 orderVolume setter方法
	 * @param orderVolume 设置属性orderVolume的值
	 * @author andy wang<br/>
	 */
	public void setOrderVolume(Double orderVolume) {
		this.orderVolume = orderVolume;
	}

	/**
	 * 属性 appointmentTime getter方法
	 * @return 属性appointmentTime
	 * @author andy wang<br/>
	 */
	public Date getAppointmentTime() {
		return appointmentTime;
	}

	/**
	 * 属性 appointmentTime setter方法
	 * @param appointmentTime 设置属性appointmentTime的值
	 * @author andy wang<br/>
	 */
	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
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
	

	public Integer getSyncErpStatus() {
		return syncErpStatus;
	}

	public void setSyncErpStatus(Integer syncErpStatus) {
		this.syncErpStatus = syncErpStatus;
	}

	/**
	 * 属性 asnId2 getter方法
	 * @return 属性asnId2
	 * @author andy wang<br/>
	 */
	public Integer getAsnId2() {
		return asnId2;
	}

	/**
	 * 属性 asnId2 setter方法
	 * @param asnId2 设置属性asnId2的值
	 * @author andy wang<br/>
	 */
	public void setAsnId2(Integer asnId2) {
		this.asnId2 = asnId2;
	}
	
	public String getGatejobNo() {
		return gatejobNo;
	}

	public void setGatejobNo(String gatejobNo) {
		this.gatejobNo = gatejobNo;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getClerk() {
		return clerk;
	}

	public void setClerk(String clerk) {
		this.clerk = clerk;
	}

	public Integer getInterceptStatus() {
		return interceptStatus;
	}

	public void setInterceptStatus(Integer interceptStatus) {
		this.interceptStatus = interceptStatus;
	}

}