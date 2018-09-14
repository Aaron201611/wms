package com.yunkouan.wms.modules.send.entity;

import java.util.Date;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

/**
 * 发货单
 * @author Aaron
 *
 */
public class SendDelivery extends BaseEntity{
	private static final long serialVersionUID = -1370352588186670598L;

	@Id
	private String deliveryId;

	/**
	 * 发货单号
	 */
    private String deliveryNo;

    /**
     * 源单号
     */
    @Length(max=32,message="{valid_send_delivery_src_no_length}",groups={ValidSave.class,ValidUpdate.class})
    @NotNull(message="{valid_send_delivery_src_no_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private String srcNo;

    /**
     * 货主
     */
    @Length(max=32,message="{valid_delivery_owner_length}",groups={ValidSave.class,ValidUpdate.class})
    private String owner;

    /**
     * 发货方
     */
    @Length(max=64,message="{valid_send_delivery_sender_length}",groups={ValidSave.class,ValidUpdate.class})
    private String sender;

    /**
     * 收货方
     */
    private String receiver;

    /**
     * 发货状态
     */
    private Integer deliveryStatus;
    
    /**
     * 拦截状态
     */
    private Integer interceptStatus;

    /**
     * 企业id
     */
    private String orgId;

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 单据号1
     */
    @Length(max=32,message="{valid_delivery_no1_length}",groups={ValidSave.class,ValidUpdate.class})
    private String deliveryNo1;

    /**
     * 单据号2/提运单号
     */
//    @NotNull(message="{valid_delivery_no2_isnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=32,message="{valid_delivery_no2_length}",groups={ValidSave.class,ValidUpdate.class})
    private String deliveryNo2;

    /**
     * 单据类型
     */
    @NotNull(message="{valid_send_delivery_docType_isnull}",groups={ValidSave.class})
    private Integer docType;

    /**
     * 订单时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    @NotNull(message="{valid_send_delivery_orderTime_isnull}",groups={ValidSave.class})
    private Date orderTime;

    /**
     * 订单数量
     */
    @Max(value=Integer.MAX_VALUE,message="{valid_send_delivery_orderQty_max}",groups={ValidSave.class,ValidUpdate.class})
    @Min(value=0,message="{valid_send_delivery_orderQty_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderQty;

    /**
     * 订单重量
     */
    @DecimalMax(value="999999999",message="{valid_send_delivery_orderWeight_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_delivery_orderWeight_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderWeight;

    /**
     * 订单体积
     */
    @DecimalMax(value="999999999",message="{valid_send_delivery_orderVolume_max}",groups={ValidSave.class,ValidUpdate.class})
    @DecimalMin(value="0",message="{valid_send_delivery_orderVolume_min}",groups={ValidSave.class,ValidUpdate.class})
    private Double orderVolume;
    
    /**
     * 拣货数量
     */
    private Double pickQty;

    /**
     * 拣货重量
     */
    private Double pickWeight;

    /**
     * 拣货体积
     */
    private Double pickVolume;
    
    /**
     * 货品种数
     */
    private Integer orderSkuQty;

    /**
     * 数据来源
     */
    private Integer dataFrom;
    
    private String gatejobNo;
    
    private String transStatus;

    /**
     * 联系人
     */
    @NotNull(message="{valid_contactPerson_isnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=64,message="{valid_send_delivery_contactPerson_length}",groups={ValidSave.class,ValidUpdate.class})
    private String contactPerson;

    /**
     * 联系地址
     */
    @NotNull(message="{valid_contactAddress_isnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=512,message="{valid_send_delivery_contactAddress_length}",groups={ValidSave.class,ValidUpdate.class})
    private String contactAddress;

    /**
     * 联系电话
     */
    @NotNull(message="{valid_contactPhone_isnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=16,message="{valid_send_delivery_contactPhone_length}",groups={ValidSave.class,ValidUpdate.class})
    private String contactPhone;

    /**
     * 省
     */
    @Length(max=32,message="{valid_send_delivery_province_length}",groups={ValidSave.class,ValidUpdate.class})
    private String province;

    /**
     * 市
     */
    @Length(max=32,message="{valid_send_delivery_city_length}",groups={ValidSave.class,ValidUpdate.class})
    private String city;

    /**
     * 区
     */
    @Length(max=32,message="{valid_send_delivery_county_length}",groups={ValidSave.class,ValidUpdate.class})
    private String county;

    /**
     * 备注
     */
    @Length(max=2048,message="{valid_send_delivery_note_length}",groups={ValidSave.class,ValidUpdate.class})
    private String note;

    /**
     * 波次id
     */
    private String waveId;

    /**
     * 预约发货时间
     */
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private Date appointmentTime;

    /**
     * 运输时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
    private Date shipmentTime;

    private Integer sendType;
    
    /**
     * 复核人
     */
    private String reviewPerson;
    
    /**
     * 复核时间
     */
    @JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private Date reviewTime;
    
    /**
     * 复核数量
     */
    private Double reviewQty;

    /**
     * 快递公司代码
     */
    private String expressServiceCode;
    /**
     * 面单号/运单号
     */
    @Length(max=32,message="{valid_delivery_express_bill_no_length}",groups={ValidSave.class,ValidUpdate.class})
    private String expressBillNo;
    
    /**
     * 快递大字号
     */
    private String expressBigchar;
    /**
     * 快递集包地
     */
    private String expressGatheringPlace;

    /**InsuredFee:保价费**/
//    @NotNull(message="{valid_insuranceCharge_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private Double insuranceCharge;
    /**FreightFee:运费**/
//    @NotNull(message="{valid_freightCharge_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private Double freightCharge;
    /**NetWeight:毛重**/
//    @NotNull(message="{valid_grossWeight_isnull}",groups={ValidSave.class,ValidUpdate.class})
    private Double grossWeight;
    
    /**称重重量**/
    private Double scalageWeight;

    private Integer deliveryId2;
    /**当前运单的推送状态，快递公司，erp**/
    private Integer sendStatus;

    /** 部门*/
    @NotNull(message="{valid_delivery_department_isnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=64,message="{valid_delivery_department_length}",groups={ValidSave.class,ValidUpdate.class})
    private String department;

    /**业务人员*/
    @NotNull(message="{valid_delivery_clerk_isnull}",groups={ValidSave.class,ValidUpdate.class})
    @Length(max=64,message="{valid_delivery_clerk_length}",groups={ValidSave.class,ValidUpdate.class})
    private String clerk;

    /**寄件人（客商）id**/
    private String consignorId;
    /**consignor:仓库寄件人（客商）**/
    private String consignor;
    /**consignorAddress:寄件人联系地址**/
    @Length(max=512,message="{valid_send_delivery_consignorAddress_length}",groups={ValidSave.class,ValidUpdate.class})
    private String consignorAddress;
    /**consignorPhone:寄件人联系电话**/
    @Length(max=16,message="{valid_send_delivery_consignorPhone_length}",groups={ValidSave.class,ValidUpdate.class})
    private String consignorPhone;
    
    private String sendProvince;
    
    private String sendCity;
    
    private String sendCounty;
    
    /**客户名称*/
    private String customer;
    
    private Integer expressBillPrintTimes;
    
    private Integer isPackage;
    
    private Integer isCheckWeight;
    
    private Integer isSendScan;
    
    private String inspectResult;


    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId == null ? null : deliveryId.trim();
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo == null ? null : deliveryNo.trim();
    }

    public String getSrcNo() {
        return srcNo;
    }

    public void setSrcNo(String srcNo) {
        this.srcNo = srcNo == null ? null : srcNo.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getDeliveryNo1() {
        return deliveryNo1;
    }

    public void setDeliveryNo1(String deliveryNo1) {
        this.deliveryNo1 = deliveryNo1 == null ? null : deliveryNo1.trim();
    }

    public String getDeliveryNo2() {
        return deliveryNo2;
    }

    public void setDeliveryNo2(String deliveryNo2) {
        this.deliveryNo2 = deliveryNo2 == null ? null : deliveryNo2.trim();
    }

    public Integer getDocType() {
        return docType;
    }

    public void setDocType(Integer docType) {
        this.docType = docType;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Double getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(Double orderWeight) throws Exception {
        this.orderWeight = NumberUtil.round(orderWeight,2);
    }

    public Double getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(Double orderVolume) throws Exception {
        this.orderVolume = NumberUtil.round(orderVolume,6);
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson == null ? null : contactPerson.trim();
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getWaveId() {
        return waveId;
    }

    public void setWaveId(String waveId) {
        this.waveId = waveId == null ? null : waveId.trim();
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Date getShipmentTime() {
        return shipmentTime;
    }

    public void setShipmentTime(Date shipmentTime) {
        this.shipmentTime = shipmentTime;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getDeliveryId2() {
        return deliveryId2;
    }

    public void setDeliveryId2(Integer deliveryId2) {
        this.deliveryId2 = deliveryId2;
    }

	public Double getPickWeight() {
		return pickWeight;
	}

	public void setPickWeight(Double pickWeight) throws Exception {
		this.pickWeight = NumberUtil.round(pickWeight, 2);
	}

	public Double getPickVolume() {
		return pickVolume;
	}

	public void setPickVolume(Double pickVolume) throws Exception {
		this.pickVolume = NumberUtil.round(pickVolume, 6);
	}

	public Integer getOrderSkuQty() {
		return orderSkuQty;
	}

	public void setOrderSkuQty(Integer orderSkuQty) {
		this.orderSkuQty = orderSkuQty;
	}
	
    
    public String getReviewPerson() {
		return reviewPerson;
	}

	public void setReviewPerson(String reviewPerson) {
		this.reviewPerson = reviewPerson;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public String getExpressServiceCode() {
		return expressServiceCode;
	}

	public void setExpressServiceCode(String expressServiceCode) {
		this.expressServiceCode = expressServiceCode;
	}

	public Double getOrderQty() {
		return orderQty;
	}
	

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	

	public Double getPickQty() {
		return pickQty;
	}
	

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}
	

	public Double getReviewQty() {
		return reviewQty;
	}
	

	public void setReviewQty(Double reviewQty) {
		this.reviewQty = reviewQty;
	}
	

	public Double getInsuranceCharge() {
		return insuranceCharge;
	}

	public Double getFreightCharge() {
		return freightCharge;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setInsuranceCharge(Double insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}

	public void setFreightCharge(Double freightCharge) {
		this.freightCharge = freightCharge;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getExpressBillNo() {
		return expressBillNo;
	}

	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}

	public void defaultValue() throws Exception{
    	setOrderQty(this.orderQty == null?0:this.orderQty);
    	setOrderWeight(this.orderWeight == null?0.0:this.orderWeight);
    	setOrderVolume(this.orderVolume == null?0.0:this.orderVolume);
    	setPickQty(this.pickQty == null?0:this.pickQty);
    	setPickWeight(this.pickWeight == null?0.0:this.pickWeight);
    	setPickVolume(this.pickVolume == null?0.0:this.pickVolume);
    }
    
    public void calPickQty(double qty){
    	double pickQty = this.pickQty == null?qty:this.pickQty + qty;
    	setPickQty(pickQty);
    }
    public void calPickdWeight(Double weight) throws Exception{
    	double pickWeight = this.pickWeight == null?weight:NumberUtil.add(this.pickWeight,weight);
    	setPickWeight(pickWeight);
    }
    public void calPickVolume(Double volume) throws Exception{
    	double pickVolume = this.pickVolume == null?volume:NumberUtil.add(this.pickVolume,volume);
    	setPickVolume(pickVolume);
    }

	/**
	获取当前运单的推送状态，快递公司，erp  
	 * @return the sendStatus
	 */
	public Integer getSendStatus() {
		return sendStatus;
	}
	

	/**
	 * @param 当前运单的推送状态，快递公司，erp the sendStatus to set
	 */
	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
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

	public String getConsignor() {
		return consignor;
	}

	public String getConsignorAddress() {
		return consignorAddress;
	}

	public String getConsignorPhone() {
		return consignorPhone;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}

	public void setConsignorAddress(String consignorAddress) {
		this.consignorAddress = consignorAddress;
	}

	public void setConsignorPhone(String consignorPhone) {
		this.consignorPhone = consignorPhone;
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

	public String getConsignorId() {
		return consignorId;
	}

	public void setConsignorId(String consignorId) {
		this.consignorId = consignorId;
	}

	public Double getScalageWeight() {
		return scalageWeight;
	}

	public void setScalageWeight(Double scalageWeight) {
		this.scalageWeight = scalageWeight;
	}

	public Integer getExpressBillPrintTimes() {
		return expressBillPrintTimes;
	}

	public void setExpressBillPrintTimes(Integer expressBillPrintTimes) {
		this.expressBillPrintTimes = expressBillPrintTimes;
	}

	public Integer getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(Integer isPackage) {
		this.isPackage = isPackage;
	}

	public Integer getIsCheckWeight() {
		return isCheckWeight;
	}

	public void setIsCheckWeight(Integer isCheckWeight) {
		this.isCheckWeight = isCheckWeight;
	}

	public Integer getIsSendScan() {
		return isSendScan;
	}

	public void setIsSendScan(Integer isSendScan) {
		this.isSendScan = isSendScan;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getExpressBigchar() {
		return expressBigchar;
	}

	public void setExpressBigchar(String expressBigchar) {
		this.expressBigchar = expressBigchar;
	}

	public String getExpressGatheringPlace() {
		return expressGatheringPlace;
	}

	public void setExpressGatheringPlace(String expressGatheringPlace) {
		this.expressGatheringPlace = expressGatheringPlace;
	}

	public String getSendProvince() {
		return sendProvince;
	}

	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}

	public String getSendCity() {
		return sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	public String getSendCounty() {
		return sendCounty;
	}

	public void setSendCounty(String sendCounty) {
		this.sendCounty = sendCounty;
	}

	public Integer getInterceptStatus() {
		return interceptStatus;
	}

	public void setInterceptStatus(Integer interceptStatus) {
		this.interceptStatus = interceptStatus;
	}

	public String getInspectResult() {
		return inspectResult;
	}

	public void setInspectResult(String inspectResult) {
		this.inspectResult = inspectResult;
	}

	
	
}