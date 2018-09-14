/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月20日 下午3:56:07<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <br/><br/>
 * @version 2017年2月20日 下午3:56:07<br/>
 * @author andy wang<br/>
 */
public class InStockVO4Excel {

	private String index;
	private String locationName;
	/**
	 * 货品名称
	 * @version 2017年5月15日下午4:56:39<br/>
	 * @author andy wang<br/>
	 */
	private String skuName;
	/**
	 * 货品名称
	 * @version 2017年5月15日下午4:56:39<br/>
	 * @author andy wang<br/>
	 */
	private String skuBarCode;
	/**
	 * 源单号
	 * @version 2017年5月15日下午4:56:39<br/>
	 * @author andy wang<br/>
	 */
	private String poNo;
	/**
	 * 订单日期，取发货单创建日期
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderDate;
	/**
	 * 收货数量
	 */
	private Double orderQty;
	/**
	 * 实际收货数量
	 */
	private Double receiveQty;
	/**
	 * 货主
	 */
	private String owner;
	/**
	 * 送货方
	 */
	private String sender;
	/**
	 * 联系方式
	 */
	private String contactWay;
	/**
	 * 入库日期
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date inStockDate;
	
	/* getset **************************************************/
	/* method *************************************************/


	public InStockVO4Excel(InStockVO vo,Integer index) {
		if(vo != null){
			this.index = index + 1 +"";
			this.locationName = vo.getLocationNo();
			this.skuName = vo.getSkuName();
			this.skuBarCode = vo.getSkuBarCode();
			this.poNo = vo.getPoNo();
			this.orderDate = vo.getOrderDate();
			this.setOrderQty(vo.getOrderQty());
			this.setReceiveQty(vo.getReceiveQty());
			this.owner = vo.getOwner();
			this.sender = vo.getSender();
			this.contactWay = vo.getContactWay();
			this.inStockDate = vo.getInStockDate();
		}
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
	

	public String getPoNo() {
		return poNo;
	}
	

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getLocationName() {
		return locationName;
	}
	

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	

	

	public String getOwner() {
		return owner;
	}
	

	public void setOwner(String owner) {
		this.owner = owner;
	}
	

	public String getSender() {
		return sender;
	}
	

	public void setSender(String sender) {
		this.sender = sender;
	}
	

	public String getContactWay() {
		return contactWay;
	}
	
	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public Date getInStockDate() {
		return inStockDate;
	}
	

	public void setInStockDate(Date inStockDate) {
		this.inStockDate = inStockDate;
	}




	public String getIndex() {
		return index;
	}
	


	public void setIndex(String index) {
		this.index = index;
	}



	public Date getOrderDate() {
		return orderDate;
	}
	


	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}


	public Double getOrderQty() {
		return orderQty;
	}


	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}


	public Double getReceiveQty() {
		return receiveQty;
	}


	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	
	
	
	
}
