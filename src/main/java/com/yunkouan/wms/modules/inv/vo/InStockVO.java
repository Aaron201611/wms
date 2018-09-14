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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;

/**
 * <br/><br/>
 * @version 2017年2月20日 下午3:56:07<br/>
 * @author andy wang<br/>
 */
public class InStockVO extends BaseVO {

	private static final long serialVersionUID = -2993982175292734948L;
	
	/**
	 * 构造方法
	 * @version 2017年2月20日 下午3:58:24<br/>
	 * @author andy wang<br/>
	 */
	public InStockVO() {
	}
	
	private String locationNo;
	/**
	 * 货品名称
	 * @version 2017年5月15日下午4:56:39<br/>
	 * @author andy wang<br/>
	 */
	private String skuName;
	/**
	 * 货品条码
	 * @version 2017年5月15日下午4:56:39<br/>
	 * @author andy wang<br/>
	 */
	private String skuBarCode;
	
	private String skuNo;
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

    private Double orderQty;
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

	/**
	 * 生产日期
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date produceDate;
	/* 默认查询条件 */
	private String orgId;
	private String warehouseId;
	/* 页面查询条件 */
	private String locationNoLike;
	private String skuNameLike;
	private String skuBarCodeLike;
	private String skuNoLike;
	private String poNoLike;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderDateStart;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderDateEnd;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date inDateStart;
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date inDateEnd;
	private String ownerLike;
	/* 转换实例条件 */
	private List<String> locationIdList;
	private List<String> skuIdList;
	private List<String> putawayIdList;
	
	
	/* getset **************************************************/
	/* method *************************************************/


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
	

	public String getLocationNoLike() {
		return locationNoLike;
	}
	

	public void setLocationNoLike(String locationNoLike) {
		this.locationNoLike = locationNoLike;
	}
	

	public String getSkuNameLike() {
		return skuNameLike;
	}
	

	public void setSkuNameLike(String skuNameLike) {
		this.skuNameLike = skuNameLike;
	}
	

	public String getSkuBarCodeLike() {
		return skuBarCodeLike;
	}
	

	public void setSkuBarCodeLike(String skuBarCodeLike) {
		this.skuBarCodeLike = skuBarCodeLike;
	}
	

	public String getPoNoLike() {
		return poNoLike;
	}
	

	public void setPoNoLike(String poNoLike) {
		this.poNoLike = poNoLike;
	}
	

	public List<String> getLocationIdList() {
		return locationIdList;
	}
	

	public void setLocationIdList(List<String> locationIdList) {
		this.locationIdList = locationIdList;
	}
	

	public List<String> getSkuIdList() {
		return skuIdList;
	}
	

	public void setSkuIdList(List<String> skuIdList) {
		this.skuIdList = skuIdList;
	}
	

	public List<String> getPutawayIdList() {
		return putawayIdList;
	}
	

	public void setPutawayIdList(List<String> putawayIdList) {
		this.putawayIdList = putawayIdList;
	}
	

	public Date getInStockDate() {
		return inStockDate;
	}
	

	public void setInStockDate(Date inStockDate) {
		this.inStockDate = inStockDate;
	}
	

	public String getOwnerLike() {
		return ownerLike;
	}

	public void setOwnerLike(String ownerLike) {
		this.ownerLike = ownerLike;
	}

	public Date getOrderDateStart() {
		return orderDateStart;
	}
	

	public void setOrderDateStart(Date orderDateStart) {
		this.orderDateStart = orderDateStart;
	}
	

	public Date getOrderDateEnd() {
		return orderDateEnd;
	}
	

	public void setOrderDateEnd(Date orderDateEnd) {
		this.orderDateEnd = orderDateEnd;
	}
	

	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月9日 下午2:53:13<br/>
	 * @author andy wang<br/>
	 */
	public InStockVO seachCache () {
		return this;
	}
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @version 2017年3月25日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo() {
		Principal loginUser = LoginUtil.getLoginUser();
		this.loginInfo(loginUser);
	}
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @param loginUser 当前登录人对象
	 * @version 2017年3月25日 上午11:08:27<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo(Principal loginUser) {
		this.setOrgId(loginUser.getOrgId());
		this.setWarehouseId(LoginUtil.getWareHouseId());
	}
	
//	public Example getExample () {
//		if ( this.getPutawayDetail() == null ) {
//			return null;
//		}
//		RecPutawayDetail putawayDetail = this.getPutawayDetail();
//		Example example = new Example(RecPutawayDetail.class);
//		Criteria criteria = example.createCriteria();
//		if ( locationIdList != null && !locationIdList.isEmpty() ) {
//			criteria.andIn("locationId", locationIdList);
//		}
//		if ( skuIdList != null && !skuIdList.isEmpty() ) {
//			criteria.andIn("skuId", skuIdList);
//		}
//		if ( putawayIdList != null && !putawayIdList.isEmpty() ) {
//			criteria.andIn("putawayId", putawayIdList);
//		}
//		if ( !StringUtil.isTrimEmpty(putawayDetail.getPutawayDetailId()) ) {
//			criteria.andEqualTo("putawayDetailId", putawayDetail.getPutawayDetailId());
//		}
//		if ( !StringUtil.isTrimEmpty(putawayDetail.getPutawayDetailId()) ) {
//			criteria.andEqualTo("detailId", putawayDetail.getPutawayDetailId());
//		}
//		if ( !StringUtil.isTrimEmpty(putawayDetail.getPutawayDetailId()) ) {
//			criteria.andEqualTo("putawayDetailId", putawayDetail.getPutawayDetailId());
//		}
//		if ( !StringUtil.isTrimEmpty(putawayDetail.getOrgId()) ) {
//			criteria.andEqualTo("orgId", putawayDetail.getOrgId());
//		}
//		if ( !StringUtil.isTrimEmpty(putawayDetail.getWarehouseId()) ) {
//			criteria.andEqualTo("warehouseId", putawayDetail.getWarehouseId());
//		}
//		return example;
//	}

	public String getLocationNo() {
		return locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}
	

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getWarehouseId() {
		return warehouseId;
	}


	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}


	public String getSkuNoLike() {
		return skuNoLike;
	}


	public void setSkuNoLike(String skuNoLike) {
		this.skuNoLike = skuNoLike;
	}


	public String getSkuNo() {
		return skuNo;
	}


	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}


	public Date getProduceDate() {
		return produceDate;
	}
	


	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}


	public Date getInDateStart() {
		return inDateStart;
	}
	


	public void setInDateStart(Date inDateStart) {
		this.inDateStart = inDateStart;
	}
	


	public Date getInDateEnd() {
		return inDateEnd;
	}
	


	public void setInDateEnd(Date inDateEnd) {
		this.inDateEnd = inDateEnd;
	}
	
}
