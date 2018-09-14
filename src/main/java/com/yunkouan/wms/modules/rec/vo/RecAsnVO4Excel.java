/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:15:24<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.vo;

import java.util.Date;
import java.util.List;

/**
 * ASN单VO对象<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午4:15:24<br/>
 * @author andy wang<br/>
 */
public class RecAsnVO4Excel{

	/**
	 * 构造方法
	 * @param recAsnVO
	 * @version 2017年10月24日 下午1:38:57<br/>
	 * @author 王通<br/>
	 */
	public RecAsnVO4Excel(RecAsnVO recAsnVO) {
		this.ownerShortName = recAsnVO.getOwnerMerchant().getMerchantShortName();
		this.poNo = recAsnVO.getAsn().getPoNo();
		this.asnNo = recAsnVO.getAsn().getAsnNo();
		this.orderTypeName = recAsnVO.getDocTypeComment();
		this.dataFrom = recAsnVO.getDataFromComment();
		this.orderDate = recAsnVO.getAsn().getOrderDate();
		this.orderslgjlf = recAsnVO.getAsn().getOrderQty() + "/" + recAsnVO.getAsn().getOrderWeight() + "/" + recAsnVO.getAsn().getOrderVolume();
		this.recslgjlf = recAsnVO.getAsn().getReceiveQty() + "/" + recAsnVO.getAsn().getReceiveWeight() + "/" + recAsnVO.getAsn().getReceiveVolume();
		this.putslgjlf = recAsnVO.getSumRealPtwQty() + "/" + recAsnVO.getSumRealPtwWeight() + "/" + recAsnVO.getSumRealPtwVolume();
		this.statusName = recAsnVO.getAsnStatusComment();
		this.erpStatusName = recAsnVO.getSyncErpStatusComment();
		this.createPerson = recAsnVO.getCreatePersonComment();
		this.opPerson = recAsnVO.getOpPersonComment();
	}
	
	public RecAsnVO4Excel(RecAsnVO recAsnVO,RecAsnDetailVO detailVo,Integer index){
		if(recAsnVO != null){
			this.index = index +"";
			this.ownerShortName = recAsnVO.getOwnerMerchant().getMerchantShortName();
			this.poNo = recAsnVO.getAsn().getPoNo();
			this.asnNo = recAsnVO.getAsn().getAsnNo();
			this.orderTypeName = recAsnVO.getDocTypeComment();
			this.dataFrom = recAsnVO.getDataFromComment();
			this.orderDate = recAsnVO.getAsn().getOrderDate();
			this.orderslgjlf = recAsnVO.getAsn().getOrderQty() + "/" + recAsnVO.getAsn().getOrderWeight() + "/" + recAsnVO.getAsn().getOrderVolume();
			this.recslgjlf = recAsnVO.getAsn().getReceiveQty() + "/" + recAsnVO.getAsn().getReceiveWeight() + "/" + recAsnVO.getAsn().getReceiveVolume();
			this.putslgjlf = recAsnVO.getSumRealPtwQty() + "/" + recAsnVO.getSumRealPtwWeight() + "/" + recAsnVO.getSumRealPtwVolume();
			this.statusName = recAsnVO.getAsnStatusComment();
			this.erpStatusName = recAsnVO.getSyncErpStatusComment();
			this.createPerson = recAsnVO.getCreatePersonComment();
			this.opPerson = recAsnVO.getOpPersonComment();
			this.sender = recAsnVO.getSenderComment();
		}
		this.skuName = detailVo.getSkuComment();
		this.skuBarCode = detailVo.getSkuBarCode();
	}

	private String index;
	private String ownerShortName;
	private String poNo;
	private String asnNo;
	private String orderTypeName;
	private String dataFrom;
	private Date orderDate;
	private String orderslgjlf;
	private String recslgjlf;
	private String putslgjlf;
	private String statusName;
	private String erpStatusName;
	private String createPerson;
	private String opPerson;
	private String sender;
	private String skuName;
	private String skuBarCode;
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
	 * 属性 ownerShortName getter方法
	 * @return 属性ownerShortName
	 * @author 王通<br/>
	 */
	public String getOwnerShortName() {
		return ownerShortName;
	}
	/**
	 * 属性 ownerShortName setter方法
	 * @param ownerShortName 设置属性ownerShortName的值
	 * @author 王通<br/>
	 */
	public void setOwnerShortName(String ownerShortName) {
		this.ownerShortName = ownerShortName;
	}
	/**
	 * 属性 poNo getter方法
	 * @return 属性poNo
	 * @author 王通<br/>
	 */
	public String getPoNo() {
		return poNo;
	}
	/**
	 * 属性 poNo setter方法
	 * @param poNo 设置属性poNo的值
	 * @author 王通<br/>
	 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	/**
	 * 属性 asnNo getter方法
	 * @return 属性asnNo
	 * @author 王通<br/>
	 */
	public String getAsnNo() {
		return asnNo;
	}
	/**
	 * 属性 asnNo setter方法
	 * @param asnNo 设置属性asnNo的值
	 * @author 王通<br/>
	 */
	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}
	/**
	 * 属性 orderTypeName getter方法
	 * @return 属性orderTypeName
	 * @author 王通<br/>
	 */
	public String getOrderTypeName() {
		return orderTypeName;
	}
	/**
	 * 属性 orderTypeName setter方法
	 * @param orderTypeName 设置属性orderTypeName的值
	 * @author 王通<br/>
	 */
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	/**
	 * 属性 dataFrom getter方法
	 * @return 属性dataFrom
	 * @author 王通<br/>
	 */
	public String getDataFrom() {
		return dataFrom;
	}
	/**
	 * 属性 dataFrom setter方法
	 * @param dataFrom 设置属性dataFrom的值
	 * @author 王通<br/>
	 */
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	/**
	 * 属性 orderDate getter方法
	 * @return 属性orderDate
	 * @author 王通<br/>
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * 属性 orderDate setter方法
	 * @param orderDate 设置属性orderDate的值
	 * @author 王通<br/>
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * 属性 orderslgjlf getter方法
	 * @return 属性orderslgjlf
	 * @author 王通<br/>
	 */
	public String getOrderslgjlf() {
		return orderslgjlf;
	}
	/**
	 * 属性 orderslgjlf setter方法
	 * @param orderslgjlf 设置属性orderslgjlf的值
	 * @author 王通<br/>
	 */
	public void setOrderslgjlf(String orderslgjlf) {
		this.orderslgjlf = orderslgjlf;
	}
	/**
	 * 属性 recslgjlf getter方法
	 * @return 属性recslgjlf
	 * @author 王通<br/>
	 */
	public String getRecslgjlf() {
		return recslgjlf;
	}
	/**
	 * 属性 recslgjlf setter方法
	 * @param recslgjlf 设置属性recslgjlf的值
	 * @author 王通<br/>
	 */
	public void setRecslgjlf(String recslgjlf) {
		this.recslgjlf = recslgjlf;
	}
	/**
	 * 属性 putslgjlf getter方法
	 * @return 属性putslgjlf
	 * @author 王通<br/>
	 */
	public String getPutslgjlf() {
		return putslgjlf;
	}
	/**
	 * 属性 putslgjlf setter方法
	 * @param putslgjlf 设置属性putslgjlf的值
	 * @author 王通<br/>
	 */
	public void setPutslgjlf(String putslgjlf) {
		this.putslgjlf = putslgjlf;
	}
	/**
	 * 属性 statusName getter方法
	 * @return 属性statusName
	 * @author 王通<br/>
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * 属性 statusName setter方法
	 * @param statusName 设置属性statusName的值
	 * @author 王通<br/>
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	/**
	 * 属性 erpStatusName getter方法
	 * @return 属性erpStatusName
	 * @author 王通<br/>
	 */
	public String getErpStatusName() {
		return erpStatusName;
	}
	/**
	 * 属性 erpStatusName setter方法
	 * @param erpStatusName 设置属性erpStatusName的值
	 * @author 王通<br/>
	 */
	public void setErpStatusName(String erpStatusName) {
		this.erpStatusName = erpStatusName;
	}
	/**
	 * 属性 createPerson getter方法
	 * @return 属性createPerson
	 * @author 王通<br/>
	 */
	public String getCreatePerson() {
		return createPerson;
	}
	/**
	 * 属性 createPerson setter方法
	 * @param createPerson 设置属性createPerson的值
	 * @author 王通<br/>
	 */
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 属性 opPerson getter方法
	 * @return 属性opPerson
	 * @author 王通<br/>
	 */
	public String getOpPerson() {
		return opPerson;
	}
	/**
	 * 属性 opPerson setter方法
	 * @param opPerson 设置属性opPerson的值
	 * @author 王通<br/>
	 */
	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuBarCode() {
		return skuBarCode;
	}
	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}
	
	
}