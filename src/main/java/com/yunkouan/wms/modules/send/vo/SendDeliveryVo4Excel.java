package com.yunkouan.wms.modules.send.vo;

import java.util.Date;

public class SendDeliveryVo4Excel{

	
	/**
	 * 构造方法
	 * @version 2017年10月24日 下午3:07:57<br/>
	 * @author 王通<br/>
	 */
	public SendDeliveryVo4Excel(SendDeliveryVo vo) {
		this.deliveryNo = vo.getSendDelivery().getDeliveryNo();
		this.srcNo = vo.getSendDelivery().getSrcNo();
		this.expressBillNo = vo.getSendDelivery().getExpressBillNo();
		this.docTypeComment = vo.getDocTypeComment();
		this.dataFromComment = vo.getDataFromComment();
		this.orderTime = vo.getSendDelivery().getOrderTime();
		this.orderQty = vo.getSendDelivery().getOrderQty();
		this.orderWeight = vo.getSendDelivery().getOrderWeight();
		this.orderVolume = vo.getSendDelivery().getOrderVolume();
		this.pickQty = vo.getSendDelivery().getPickQty();
		this.pickWeight = vo.getSendDelivery().getPickWeight();
		this.pickVolume = vo.getSendDelivery().getPickVolume();
		this.contactPerson = vo.getSendDelivery().getContactPerson();
		this.contactPhone = vo.getSendDelivery().getContactPhone();
		this.statusComment = vo.getStatusComment();
		this.sendEmsStatus = vo.getSendDelivery().getSendStatus() != null && vo.getSendDelivery().getSendStatus() > 1 ? "已发送" : "未发送" ;
		this.sendErpStatus = vo.getSendDelivery().getSendStatus() != null && vo.getSendDelivery().getSendStatus() == 3 ? "已发送" : "未发送" ;
		this.createTime = vo.getSendDelivery().getCreateTime();
		this.note = vo.getSendDelivery().getNote();
	}
	
	private String index;
	private String deliveryNo;
	private String srcNo;
	private String expressBillNo;
	private String docTypeComment;
	private String dataFromComment;
	private Date orderTime;
	private Double orderQty;
	private Double orderWeight;
	private Double orderVolume;
	private Double pickQty;
	private Double pickWeight;
	private Double pickVolume;
	private String contactPerson;
	private String contactPhone;
	private String statusComment;
	private String sendEmsStatus;
	private String sendErpStatus;
	private Date createTime;
	private String note;
	
	
	/**
	 * 属性 index getter方法
	 * @return 属性index
	 * @author 王通<br/>
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * 属性 index setter方法
	 * @param index 设置属性index的值
	 * @author 王通<br/>
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * 属性 orderWeight getter方法
	 * @return 属性orderWeight
	 * @author 王通<br/>
	 */
	public Double getOrderWeight() {
		return orderWeight;
	}
	/**
	 * 属性 orderVolume getter方法
	 * @return 属性orderVolume
	 * @author 王通<br/>
	 */
	public Double getOrderVolume() {
		return orderVolume;
	}
	/**
	 * 属性 pickWeight getter方法
	 * @return 属性pickWeight
	 * @author 王通<br/>
	 */
	public Double getPickWeight() {
		return pickWeight;
	}
	/**
	 * 属性 pickVolume getter方法
	 * @return 属性pickVolume
	 * @author 王通<br/>
	 */
	public Double getPickVolume() {
		return pickVolume;
	}
	/**
	 * 属性 sendEmsStatus getter方法
	 * @return 属性sendEmsStatus
	 * @author 王通<br/>
	 */
	public String getSendEmsStatus() {
		return sendEmsStatus;
	}
	/**
	 * 属性 sendEmsStatus setter方法
	 * @param sendEmsStatus 设置属性sendEmsStatus的值
	 * @author 王通<br/>
	 */
	public void setSendEmsStatus(String sendEmsStatus) {
		this.sendEmsStatus = sendEmsStatus;
	}
	/**
	 * 属性 sendErpStatus getter方法
	 * @return 属性sendErpStatus
	 * @author 王通<br/>
	 */
	public String getSendErpStatus() {
		return sendErpStatus;
	}
	/**
	 * 属性 sendErpStatus setter方法
	 * @param sendErpStatus 设置属性sendErpStatus的值
	 * @author 王通<br/>
	 */
	public void setSendErpStatus(String sendErpStatus) {
		this.sendErpStatus = sendErpStatus;
	}
	/**
	 * 属性 createTime getter方法
	 * @return 属性createTime
	 * @author 王通<br/>
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 属性 createTime setter方法
	 * @param createTime 设置属性createTime的值
	 * @author 王通<br/>
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 属性 orderWeight setter方法
	 * @param orderWeight 设置属性orderWeight的值
	 * @author 王通<br/>
	 */
	public void setOrderWeight(Double orderWeight) {
		this.orderWeight = orderWeight;
	}
	/**
	 * 属性 orderVolume setter方法
	 * @param orderVolume 设置属性orderVolume的值
	 * @author 王通<br/>
	 */
	public void setOrderVolume(Double orderVolume) {
		this.orderVolume = orderVolume;
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
	
	/**
	 * 属性 pickWeight setter方法
	 * @param pickWeight 设置属性pickWeight的值
	 * @author 王通<br/>
	 */
	public void setPickWeight(Double pickWeight) {
		this.pickWeight = pickWeight;
	}
	/**
	 * 属性 pickVolume setter方法
	 * @param pickVolume 设置属性pickVolume的值
	 * @author 王通<br/>
	 */
	public void setPickVolume(Double pickVolume) {
		this.pickVolume = pickVolume;
	}
	/**
	 * 属性 deliveryNo getter方法
	 * @return 属性deliveryNo
	 * @author 王通<br/>
	 */
	public String getDeliveryNo() {
		return deliveryNo;
	}
	/**
	 * 属性 deliveryNo setter方法
	 * @param deliveryNo 设置属性deliveryNo的值
	 * @author 王通<br/>
	 */
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	/**
	 * 属性 srcNo getter方法
	 * @return 属性srcNo
	 * @author 王通<br/>
	 */
	public String getSrcNo() {
		return srcNo;
	}
	/**
	 * 属性 srcNo setter方法
	 * @param srcNo 设置属性srcNo的值
	 * @author 王通<br/>
	 */
	public void setSrcNo(String srcNo) {
		this.srcNo = srcNo;
	}
	/**
	 * 属性 expressBillNo getter方法
	 * @return 属性expressBillNo
	 * @author 王通<br/>
	 */
	public String getExpressBillNo() {
		return expressBillNo;
	}
	/**
	 * 属性 expressBillNo setter方法
	 * @param expressBillNo 设置属性waveNo的值
	 * @author 王通<br/>
	 */
	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}
	/**
	 * 属性 docTypeComment getter方法
	 * @return 属性docTypeComment
	 * @author 王通<br/>
	 */
	public String getDocTypeComment() {
		return docTypeComment;
	}
	/**
	 * 属性 docTypeComment setter方法
	 * @param docTypeComment 设置属性docTypeComment的值
	 * @author 王通<br/>
	 */
	public void setDocTypeComment(String docTypeComment) {
		this.docTypeComment = docTypeComment;
	}
	/**
	 * 属性 dataFromComment getter方法
	 * @return 属性dataFromComment
	 * @author 王通<br/>
	 */
	public String getDataFromComment() {
		return dataFromComment;
	}
	/**
	 * 属性 dataFromComment setter方法
	 * @param dataFromComment 设置属性dataFromComment的值
	 * @author 王通<br/>
	 */
	public void setDataFromComment(String dataFromComment) {
		this.dataFromComment = dataFromComment;
	}
	/**
	 * 属性 orderTime getter方法
	 * @return 属性orderTime
	 * @author 王通<br/>
	 */
	public Date getOrderTime() {
		return orderTime;
	}
	/**
	 * 属性 orderTime setter方法
	 * @param orderTime 设置属性orderTime的值
	 * @author 王通<br/>
	 */
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	/**
	 * 属性 contactPerson getter方法
	 * @return 属性contactPerson
	 * @author 王通<br/>
	 */
	public String getContactPerson() {
		return contactPerson;
	}
	/**
	 * 属性 contactPerson setter方法
	 * @param contactPerson 设置属性contactPerson的值
	 * @author 王通<br/>
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	/**
	 * 属性 contactPhone getter方法
	 * @return 属性contactPhone
	 * @author 王通<br/>
	 */
	public String getContactPhone() {
		return contactPhone;
	}
	/**
	 * 属性 contactPhone setter方法
	 * @param contactPhone 设置属性contactPhone的值
	 * @author 王通<br/>
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	/**
	 * 属性 statusComment getter方法
	 * @return 属性statusComment
	 * @author 王通<br/>
	 */
	public String getStatusComment() {
		return statusComment;
	}
	/**
	 * 属性 statusComment setter方法
	 * @param statusComment 设置属性statusComment的值
	 * @author 王通<br/>
	 */
	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}