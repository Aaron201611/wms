/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月12日 下午9:00:36<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.vo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunkouan.base.BaseVO;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Asn单明细VO <br/><br/>
 * @version 2017年2月12日 下午9:00:36<br/>
 * @author andy wang<br/>
 */
public class RecAsnDetailVO extends BaseVO {

	private static final long serialVersionUID = 2239128176535268345L;
	
	public RecAsnDetailVO(){
		this( new RecAsnDetail() );
	}
	public RecAsnDetailVO( RecAsnDetail asnDetail ) {
		this.asnDetail = asnDetail;
	}
	
	/**
	 * Asn单明细
	 * @version 2017年2月12日下午9:03:43<br/>
	 * @author andy wang<br/>
	 */
	private RecAsnDetail asnDetail;

	/**
	 * 货品名
	 * @version 2017年2月12日下午9:32:12<br/>
	 * @author andy wang<br/>
	 */
	private String skuComment;
	
	/**
	 * 包装单位
	 * @version 2017年2月13日上午9:59:37<br/>
	 * @author andy wang<br/>
	 */
	private String packComment;
	
	/**
	 * 规格型号
	 * @version 2017年3月8日下午8:43:38<br/>
	 * @author andy wang<br/>
	 */
	private String specModelComment;

	
	/**
	 * 库位名
	 * @version 2017年3月9日下午7:46:40<br/>
	 * @author andy wang<br/>
	 */
	private String locationComment;
	
	private String skuStatusName;
	
	
	/**
	 * 上架单操作明细
	 * @version 2017年3月9日下午8:40:23<br/>
	 * @author andy wang<br/>
	 */
	private List<RecPutawayLocationVO> listPtwLocVO;
	
	/**
	 * Asn单明细id集合
	 * @version 2017年3月20日下午7:46:11<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listAsnDetailId;
	
	/**
	 * Asn单id集合
	 * @version 2017年3月20日下午7:55:43<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listAsnId;
	
	/**
	 * ASN单号
	 * @version 2017年3月30日下午3:36:39<br/>
	 * @author andy wang<br/>
	 */
	private String asnNo;

	/**sku:货品信息**/
	private MetaSku sku;
	/**money:金额**/
	private String money;

	/**
	 * 货品代码
	 * @version 2017年3月30日下午3:38:37<br/>
	 * @author andy wang<br/>
	 */
	private String skuNo;
	
	/**
	 * 暂存区待上架数量
	 * @version 2017年3月30日下午3:49:14<br/>
	 * @author andy wang<br/>
	 */
	private Double stockQty;
	
	/**
	 * 暂存区待上架重量
	 * @version 2017年4月6日下午4:40:23<br/>
	 * @author andy wang<br/>
	 */
	private Double stockWeight;
	
	/**
	 * 暂存区待上架体积
	 * @version 2017年4月6日下午4:40:34<br/>
	 * @author andy wang<br/>
	 */
	private Double stockVolume;
	
	/**
	 * 拆分数量
	 * @version 2017年3月31日上午11:36:56<br/>
	 * @author andy wang<br/>
	 */
	private Double splitQty;
	
	
	/**
	 * 拆分重量
	 * @version 2017年3月31日上午11:37:00<br/>
	 * @author andy wang<br/>
	 */
	private Double splitWeight;
	
	/**
	 * 拆分体积
	 * @version 2017年3月31日上午11:37:04<br/>
	 * @author andy wang<br/>
	 */
	private Double splitVolume;
	
	/**
	 * 货品单体
	 * @version 2017年4月21日下午3:48:55<br/>
	 * @author andy wang<br/>
	 */
	private Double perVolume;
	
	/**
	 * 货品单重
	 * @version 2017年4月21日下午3:49:05<br/>
	 * @author andy wang<br/>
	 */
	private Double perWeight;
	
	/**
	 * ASN单货主id
	 * @version 2017年4月21日下午3:48:29<br/>
	 * @author andy wang<br/>
	 */
	private String owner;
	
	/* 新属性 20170717 *********************************************/
	/** 货品条形码 add by andy wang */
	private String skuBarCode;
	/** 计划库位名 add by andy wang */
	private String planLocationComment;
	/** 收货库位名 add by andy wang */
	private String receiveLocationComment;
	/** 库位号 <br/> add by andy wang */
	private String locationNo;

	/* getset *************************************/
	
	
	/**skuProductDate生产日期**/
	@JsonIgnore
	public String getSkuProductDate() {
		if(asnDetail == null || asnDetail.getBatchNo() == null) return "";
		String[] format = {"yyyyMMdd"};
		try {
			Date date = DateUtils.parseDate(asnDetail.getBatchNo(), format);
			return DateFormatUtils.format(date, "yyyy/MM/dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getLocationNo() {
		return locationNo;
	}
	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}
	public String getSkuBarCode() {
		return skuBarCode;
	}
	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}
	public String getReceiveLocationComment() {
		return receiveLocationComment;
	}
	public void setReceiveLocationComment(String receiveLocationComment) {
		this.receiveLocationComment = receiveLocationComment;
	}
	public String getPlanLocationComment() {
		return planLocationComment;
	}
	public void setPlanLocationComment(String planLocationComment) {
		this.planLocationComment = planLocationComment;
	}
	/**
	 * 属性 perVolume getter方法
	 * @return 属性perVolume
	 * @author andy wang<br/>
	 */
	public Double getPerVolume() {
		return perVolume;
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
	 * 属性 perVolume setter方法
	 * @param perVolume 设置属性perVolume的值
	 * @author andy wang<br/>
	 */
	public void setPerVolume(Double perVolume) {
		this.perVolume = perVolume;
	}
	/**
	 * 属性 perWeight getter方法
	 * @return 属性perWeight
	 * @author andy wang<br/>
	 */
	public Double getPerWeight() {
		return perWeight;
	}
	/**
	 * 属性 perWeight setter方法
	 * @param perWeight 设置属性perWeight的值
	 * @author andy wang<br/>
	 */
	public void setPerWeight(Double perWeight) {
		this.perWeight = perWeight;
	}
	/**
	 * 属性 stockWeight getter方法
	 * @return 属性stockWeight
	 * @author andy wang<br/>
	 */
	public Double getStockWeight() {
		return stockWeight;
	}
	/**
	 * 属性 stockWeight setter方法
	 * @param stockWeight 设置属性stockWeight的值
	 * @author andy wang<br/>
	 */
	public void setStockWeight(Double stockWeight) {
		this.stockWeight = stockWeight;
	}
	/**
	 * 属性 stockVolume getter方法
	 * @return 属性stockVolume
	 * @author andy wang<br/>
	 */
	public Double getStockVolume() {
		return stockVolume;
	}
	/**
	 * 属性 stockVolume setter方法
	 * @param stockVolume 设置属性stockVolume的值
	 * @author andy wang<br/>
	 */
	public void setStockVolume(Double stockVolume) {
		this.stockVolume = stockVolume;
	}
	/**
	 * 属性 splitWeight getter方法
	 * @return 属性splitWeight
	 * @author andy wang<br/>
	 */
	public Double getSplitWeight() {
		return splitWeight;
	}
	/**
	 * 属性 splitWeight setter方法
	 * @param splitWeight 设置属性splitWeight的值
	 * @author andy wang<br/>
	 */
	public void setSplitWeight(Double splitWeight) {
		this.splitWeight = splitWeight;
	}
	/**
	 * 属性 splitVolume getter方法
	 * @return 属性splitVolume
	 * @author andy wang<br/>
	 */
	public Double getSplitVolume() {
		return splitVolume;
	}
	/**
	 * 属性 splitVolume setter方法
	 * @param splitVolume 设置属性splitVolume的值
	 * @author andy wang<br/>
	 */
	public void setSplitVolume(Double splitVolume) {
		this.splitVolume = splitVolume;
	}
	/**
	 * 属性 asnDetail getter方法
	 * @return 属性asnDetail
	 * @author andy wang<br/>
	 */
	public RecAsnDetail getAsnDetail() {
		return asnDetail;
	}
	/**
	 * 属性 asnDetail setter方法
	 * @param asnDetail 设置属性asnDetail的值
	 * @author andy wang<br/>
	 */
	public void setAsnDetail(RecAsnDetail asnDetail) {
		this.asnDetail = asnDetail;
	}
	public Double getStockQty() {
		return stockQty;
	}
	
	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	
	public Double getSplitQty() {
		return splitQty;
	}
	
	public void setSplitQty(Double splitQty) {
		this.splitQty = splitQty;
	}
	
	/**
	 * 属性 skuNo getter方法
	 * @return 属性skuNo
	 * @author andy wang<br/>
	 */
	public String getSkuNo() {
		return skuNo;
	}
	/**
	 * 属性 skuNo setter方法
	 * @param skuNo 设置属性skuNo的值
	 * @author andy wang<br/>
	 */
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
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
	 * 属性 listAsnId getter方法
	 * @return 属性listAsnId
	 * @author andy wang<br/>
	 */
	public List<String> getListAsnId() {
		return listAsnId;
	}
	/**
	 * 属性 listAsnId setter方法
	 * @param listAsnId 设置属性listAsnId的值
	 * @author andy wang<br/>
	 */
	public void setListAsnId(List<String> listAsnId) {
		this.listAsnId = listAsnId;
	}
	/**
	 * 属性 listAsnDetailId getter方法
	 * @return 属性listAsnDetailId
	 * @author andy wang<br/>
	 */
	public List<String> getListAsnDetailId() {
		return listAsnDetailId;
	}
	/**
	 * 属性 listAsnDetailId setter方法
	 * @param listAsnDetailId 设置属性listAsnDetailId的值
	 * @author andy wang<br/>
	 */
	public void setListAsnDetailId(List<String> listAsnDetailId) {
		this.listAsnDetailId = listAsnDetailId;
	}
	/**
	 * 属性 listPtwLocVO getter方法
	 * @return 属性listPtwLocVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> getListPtwLocVO() {
		return listPtwLocVO;
	}
	/**
	 * 属性 listPtwLocVO setter方法
	 * @param listPtwLocVO 设置属性listPtwLocVO的值
	 * @author andy wang<br/>
	 */
	public void setListPtwLocVO(List<RecPutawayLocationVO> listPtwLocVO) {
		this.listPtwLocVO = listPtwLocVO;
	}
	/**
	 * 属性 locationComment getter方法
	 * @return 属性locationComment
	 * @author andy wang<br/>
	 */
	public String getLocationComment() {
		return locationComment;
	}
	/**
	 * 属性 locationComment setter方法
	 * @param locationComment 设置属性locationComment的值
	 * @author andy wang<br/>
	 */
	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}
	/**
	 * 属性 specModelComment getter方法
	 * @return 属性specModelComment
	 * @author andy wang<br/>
	 */
	public String getSpecModelComment() {
		return specModelComment;
	}
	/**
	 * 属性 specModelComment setter方法
	 * @param specModelComment 设置属性specModelComment的值
	 * @author andy wang<br/>
	 */
	public void setSpecModelComment(String specModelComment) {
		this.specModelComment = specModelComment;
	}
	/**
	 * 属性 skuComment getter方法
	 * @return 属性skuComment
	 * @author andy wang<br/>
	 */
	public String getSkuComment() {
		return skuComment;
	}
	/**
	 * 属性 skuComment setter方法
	 * @param skuComment 设置属性skuComment的值
	 * @author andy wang<br/>
	 */
	public void setSkuComment(String skuComment) {
		this.skuComment = skuComment;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public MetaSku getSku() {
		return sku;
	}
	public void setSku(MetaSku sku) {
		this.sku = sku;
	}
	/**
	 * 属性 packComment getter方法
	 * @return 属性packComment
	 * @author andy wang<br/>
	 */
	public String getPackComment() {
		return packComment;
	}
	/**
	 * 属性 packComment setter方法
	 * @param packComment 设置属性packComment的值
	 * @author andy wang<br/>
	 */
	public void setPackComment(String packComment) {
		this.packComment = packComment;
	}
	/* method *****************************************************/
	
	
	/**
	 * 添加上架单操作明细
	 * @param ptwLocVO 上架单操作明细
	 * @version 2017年3月9日 下午8:42:36<br/>
	 * @author andy wang<br/>
	 */
	public void addPtwLocVO ( RecPutawayLocationVO ptwLocVO ) {
		if ( PubUtil.isEmpty(this.listPtwLocVO) ) {
			this.listPtwLocVO = new ArrayList<RecPutawayLocationVO>();
		}
		this.listPtwLocVO.add(ptwLocVO);
	}
	
	/**
	 * 查询缓存
	 * @version 2017年2月12日 下午9:30:33<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnDetailVO searchCache() {
		return this;
	}
	
	
	/**
	 * 设置登录人信息
	 * @version 2017年3月20日 下午7:40:46<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo() {
		if ( this.asnDetail == null ) {
			this.asnDetail = new RecAsnDetail();
		}
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser != null){
			this.asnDetail.setOrgId(loginUser.getOrgId());
			this.asnDetail.setWarehouseId(LoginUtil.getWareHouseId());
		}
	}
	
	
	@Override
	public Example getExample() {
		if ( this.asnDetail == null ) {
			this.asnDetail = new RecAsnDetail();
			this.loginInfo();
		}
		Example example = new Example(RecAsnDetail.class);
		Criteria criteria = example.createCriteria();
		example.setOrderByClause("sku_id asc, sku_status asc");
		if ( !StringUtil.isTrimEmpty(this.asnDetail.getAsnDetailId()) ) {
			criteria.andEqualTo("asnDetailId", this.asnDetail.getAsnDetailId());
		}
		if ( !PubUtil.isEmpty(this.getListAsnDetailId()) ) {
			criteria.andIn("asnDetailId", this.getListAsnDetailId());
		}
		if ( !StringUtil.isTrimEmpty(this.asnDetail.getAsnId()) ) {
			criteria.andEqualTo("asnId", this.asnDetail.getAsnId());
		}
		if ( !PubUtil.isEmpty(this.getListAsnId()) ) {
			criteria.andIn("asnId", this.getListAsnId());
		}
		if ( !StringUtil.isTrimEmpty(this.asnDetail.getOrgId()) ) {
			criteria.andEqualTo("orgId", this.asnDetail.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(this.asnDetail.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId", this.asnDetail.getWarehouseId());
		}
		if ( !StringUtil.isTrimEmpty(this.asnDetail.getSkuId()) ) {
			criteria.andEqualTo("skuId", this.asnDetail.getSkuId());
		}
		return example;
	}
	public String getSkuStatusName() {
		return skuStatusName;
	}
	public void setSkuStatusName(String skuStatusName) {
		this.skuStatusName = skuStatusName;
	}
	
}
