/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月20日 下午2:57:32<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 上架单明细VO2<br/><br/>
 * @version 2017年2月20日 下午2:57:32<br/>
 * @author andy wang<br/>
 */
public class RecPutawayDetailVO extends BaseVO {
	
	private static final long serialVersionUID = -4129429304258045003L;

	/**
	 * 构造方法
	 * @version 2017年2月20日 下午3:04:26<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetailVO(){
		this(new RecPutawayDetail());
	}
	
	/**
	 * 构造方法
	 * @param putawayDetail 上架单明细对象
	 * @version 2017年2月20日 下午3:04:30<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetailVO( RecPutawayDetail putawayDetail ){
		this.putawayDetail = putawayDetail;
	}
	
	/**
	 * 上架单对象
	 * @version 2017年2月20日下午2:55:10<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	@NotNull(message="{valid_rec_putaway_asnId_notnull}",groups={ValidSave.class,ValidUpdate.class})
	private RecPutawayDetail putawayDetail;
	
	/**
	 * 需要保存的上架单操作明细
	 * @version 2017年2月20日下午3:53:48<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private List<RecPutawayLocationVO> listSavePutawayLocationVO;
	
	/**
	 * 上架单明细id集合
	 * @version 2017年2月27日下午8:35:40<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listDetailId;
	
	
	/**
	 * ASN单号
	 * @version 2017年3月1日下午3:03:04<br/>
	 * @author andy wang<br/>
	 */
	private String asnNo;
	
	
	/**
	 * PO单号
	 * @version 2017年3月1日下午3:03:07<br/>
	 * @author andy wang<br/>
	 */
	private String poNo;

	/**
	 * 货主名称
	 * @version 2017年2月28日下午2:11:49<br/>
	 * @author andy wang<br/>
	 */
	private String ownerComment;
	
	private String hgMerchantNo;

	/**
	 * 货品名称
	 * @version 2017年3月1日下午6:38:30<br/>
	 * @author andy wang<br/>
	 */
	private String skuComment;

	/**
	 * 货品状态名称
	 * @version 2017年2月28日下午2:11:49<br/>
	 * @author andy wang<br/>
	 */
	private String skuStatusName;
	
	/**
	 * 规格型号描述
	 * @version 2017年3月1日下午6:38:44<br/>
	 * @author andy wang<br/>
	 */
	private String specModelComment;
	
	/**
	 * 包装描述
	 * @version 2017年3月1日下午6:50:19<br/>
	 * @author andy wang<br/>
	 */
	private String packComment;
	
	/**
	 * ASN单明细收货数量
	 * @version 2017年4月6日下午3:56:33<br/>
	 * @author andy wang<br/>
	 */
	private Double receiveQty;
	
	
	/**
	 * ASN单明细收货重量
	 * @version 2017年4月6日下午3:56:46<br/>
	 * @author andy wang<br/>
	 */
	private Double receiveWeight;
	
	
	/**
	 * ASN单明细收货体积
	 * @version 2017年4月6日下午3:56:56<br/>
	 * @author andy wang<br/>
	 */
	private Double receiveVolume;
	
	/**
	 * 上架单计划操作明细集合
	 * @version 2017年3月1日下午7:43:38<br/>
	 * @author andy wang<br/>
	 */
	private List<RecPutawayLocationVO> listPlanLocationVO;
	
	/**
	 * 上架单实际操作明细集合
	 * @version 2017年3月1日下午7:43:57<br/>
	 * @author andy wang<br/>
	 */
	private List<RecPutawayLocationVO> listRealLocationVO;
	
	/**
	 * 上架单id集合
	 * @version 2017年3月2日下午7:54:12<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listPutawayId;
	
	/**
	 * 上架单明细分配的库位
	 * @version 2017年3月6日下午6:02:24<br/>
	 * @author andy wang<br/>
	 */
	private List<MetaLocation> listLocation;
	
	/**
	 * 库容
	 * @version 2017年3月7日下午9:09:02<br/>
	 * @author andy wang<br/>
	 */
	private BigDecimal capacity;
	
	/**
	 * ASN单id集合
	 * @version 2017年3月23日下午2:58:20<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listAsnId;
	
	/**
	 * 客商id集合
	 * @version 2017年3月23日下午7:02:25<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listOwnerId;
	
	
	/**
	 * 总实际上架数量
	 * @version 2017年3月27日下午3:24:18<br/>
	 * @author andy wang<br/>
	 */
	private Double sumRealQty;
	
	/**
	 * 总实际上架重量
	 * @version 2017年3月27日下午3:24:22<br/>
	 * @author andy wang<br/>
	 */
	private Double sumRealWeight;
	
	/**
	 * 总实际上架体积
	 * @version 2017年3月27日下午3:24:25<br/>
	 * @author andy wang<br/>
	 */
	private Double sumRealVolume;
	
	/**
	 * 货品代码
	 * @version 2017年3月29日下午2:17:02<br/>
	 * @author andy wang<br/>
	 */
	private String skuNo;
	
	/**
	 * 暂存区待上架数量
	 * @version 2017年4月6日下午5:51:15<br/>
	 * @author andy wang<br/>
	 */
	private Double stockQty;
	
	/**
	 * 暂存区待上架重量
	 * @version 2017年4月6日下午5:56:04<br/>
	 * @author andy wang<br/>
	 */
	private Double stockWeight;
	
	/**
	 * 暂存区待上架体积
	 * @version 2017年4月6日下午5:56:13<br/>
	 * @author andy wang<br/>
	 */
	private Double stockVolume;
	
	/**
	 * 订单数量
	 * @version 2017年4月7日上午9:07:50<br/>
	 * @author andy wang<br/>
	 */
	private Double orderQty;
	
	/**
	 * 订单重量
	 * @version 2017年4月7日上午9:07:53<br/>
	 * @author andy wang<br/>
	 */
	private Double orderWeight;
	
	/**
	 * 订单体积
	 * @version 2017年4月7日上午9:08:01<br/>
	 * @author andy wang<br/>
	 */
	private Double orderVolume;
	
	/**
	 * 待上架数量
	 * @version 2017年4月18日上午10:28:31<br/>
	 * @author andy wang<br/>
	 */
	private Double handlingQty;
	
	/**
	 * 待上架重量
	 * @version 2017年4月18日上午10:28:35<br/>
	 * @author andy wang<br/>
	 */
	private Double handlingWeight;
	
	
	/**
	 * 待上架体积
	 * @version 2017年4月18日上午10:28:40<br/>
	 * @author andy wang<br/>
	 */
	private Double handlingVolume;
	
	/**
	 * 单个货品体积
	 * @version 2017年5月11日下午2:08:35<br/>
	 * @author andy wang<br/>
	 */
	private Double perVolume;
	
	/**
	 * 单个货品的重量
	 * @version 2017年5月11日下午2:08:45<br/>
	 * @author andy wang<br/>
	 */
	private Double perWeight;
	
	/* 新属性 20170717 *********************************************/

	/** 货品条码 add by andy wang */
	private String skuBarCode;
	
	private MetaSku sku;
	
	/* getset *********************************************/
	
	/**
	 * 属性 perVolume getter方法
	 * @return 属性perVolume
	 * @author andy wang<br/>
	 */
	public Double getPerVolume() {
		return perVolume;
	}

	/**
	 * 属性 perVolume setter方法
	 * @param perVolume 设置属性perVolume的值
	 * @author andy wang<br/>
	 */
	public void setPerVolume(Double perVolume) {
		this.perVolume = perVolume;
	}

	public String getSkuBarCode() {
		return skuBarCode;
	}

	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
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
	 * 属性 putawayDetail getter方法
	 * @return 属性putawayDetail
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail getPutawayDetail() {
		return putawayDetail;
	}
	/**
	 * 属性 putawayDetail setter方法
	 * @param putawayDetail 设置属性putawayDetail的值
	 * @author andy wang<br/>
	 */
	public void setPutawayDetail(RecPutawayDetail putawayDetail) {
		this.putawayDetail = putawayDetail;
	}
	
	/**
	 * 属性 handlingWeight getter方法
	 * @return 属性handlingWeight
	 * @author andy wang<br/>
	 */
	public Double getHandlingWeight() {
		return handlingWeight;
	}

	/**
	 * 属性 handlingWeight setter方法
	 * @param handlingWeight 设置属性handlingWeight的值
	 * @author andy wang<br/>
	 */
	public void setHandlingWeight(Double handlingWeight) {
		this.handlingWeight = handlingWeight;
	}

	/**
	 * 属性 handlingVolume getter方法
	 * @return 属性handlingVolume
	 * @author andy wang<br/>
	 */
	public Double getHandlingVolume() {
		return handlingVolume;
	}

	/**
	 * 属性 handlingVolume setter方法
	 * @param handlingVolume 设置属性handlingVolume的值
	 * @author andy wang<br/>
	 */
	public void setHandlingVolume(Double handlingVolume) {
		this.handlingVolume = handlingVolume;
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


	public Double getReceiveQty() {
		return receiveQty;
	}
	

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	

	public Double getSumRealQty() {
		return sumRealQty;
	}
	

	public void setSumRealQty(Double sumRealQty) {
		this.sumRealQty = sumRealQty;
	}
	

	public Double getStockQty() {
		return stockQty;
	}
	

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	

	public Double getOrderQty() {
		return orderQty;
	}
	

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	

	public Double getHandlingQty() {
		return handlingQty;
	}
	

	public void setHandlingQty(Double handlingQty) {
		this.handlingQty = handlingQty;
	}
	

	/**
	 * 属性 sumRealWeight getter方法
	 * @return 属性sumRealWeight
	 * @author andy wang<br/>
	 */
	public Double getSumRealWeight() {
		return sumRealWeight;
	}

	/**
	 * 属性 sumRealWeight setter方法
	 * @param sumRealWeight 设置属性sumRealWeight的值
	 * @author andy wang<br/>
	 */
	public void setSumRealWeight(Double sumRealWeight) {
		this.sumRealWeight = sumRealWeight;
	}

	/**
	 * 属性 sumRealVolume getter方法
	 * @return 属性sumRealVolume
	 * @author andy wang<br/>
	 */
	public Double getSumRealVolume() {
		return sumRealVolume;
	}

	/**
	 * 属性 sumRealVolume setter方法
	 * @param sumRealVolume 设置属性sumRealVolume的值
	 * @author andy wang<br/>
	 */
	public void setSumRealVolume(Double sumRealVolume) {
		this.sumRealVolume = sumRealVolume;
	}

	/**
	 * 属性 listOwnerId getter方法
	 * @return 属性listOwnerId
	 * @author andy wang<br/>
	 */
	public List<String> getListOwnerId() {
		return listOwnerId;
	}

	/**
	 * 属性 listOwnerId setter方法
	 * @param listOwnerId 设置属性listOwnerId的值
	 * @author andy wang<br/>
	 */
	public void setListOwnerId(List<String> listOwnerId) {
		this.listOwnerId = listOwnerId;
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
	 * 属性 capacity getter方法
	 * @return 属性capacity
	 * @author andy wang<br/>
	 */
	public BigDecimal getCapacity() {
		return capacity;
	}

	/**
	 * 属性 capacity setter方法
	 * @param capacity 设置属性capacity的值
	 * @author andy wang<br/>
	 */
	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	/**
	 * 属性 listLocation getter方法
	 * @return 属性listLocation
	 * @author andy wang<br/>
	 */
	public List<MetaLocation> getListLocation() {
		return listLocation;
	}

	/**
	 * 属性 listLocation setter方法
	 * @param listLocation 设置属性listLocation的值
	 * @author andy wang<br/>
	 */
	public void setListLocation(List<MetaLocation> listLocation) {
		this.listLocation = listLocation;
	}

	/**
	 * 属性 listPutawayId getter方法
	 * @return 属性listPutawayId
	 * @author andy wang<br/>
	 */
	public List<String> getListPutawayId() {
		return listPutawayId;
	}

	/**
	 * 属性 listPutawayId setter方法
	 * @param listPutawayId 设置属性listPutawayId的值
	 * @author andy wang<br/>
	 */
	public void setListPutawayId(List<String> listPutawayId) {
		this.listPutawayId = listPutawayId;
	}

	/**
	 * 属性 listPlanLocationVO getter方法
	 * @return 属性listPlanLocationVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> getListPlanLocationVO() {
		return listPlanLocationVO;
	}

	/**
	 * 属性 listPlanLocationVO setter方法
	 * @param listPlanLocationVO 设置属性listPlanLocationVO的值
	 * @author andy wang<br/>
	 */
	public void setListPlanLocationVO(List<RecPutawayLocationVO> listPlanLocationVO) {
		this.listPlanLocationVO = listPlanLocationVO;
	}

	/**
	 * 属性 listRealLocationVO getter方法
	 * @return 属性listRealLocationVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> getListRealLocationVO() {
		return listRealLocationVO;
	}

	/**
	 * 属性 listRealLocationVO setter方法
	 * @param listRealLocationVO 设置属性listRealLocationVO的值
	 * @author andy wang<br/>
	 */
	public void setListRealLocationVO(List<RecPutawayLocationVO> listRealLocationVO) {
		this.listRealLocationVO = listRealLocationVO;
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
	 * 属性 listDetailId getter方法
	 * @return 属性listDetailId
	 * @author andy wang<br/>
	 */
	public List<String> getListDetailId() {
		return listDetailId;
	}

	/**
	 * 属性 listDetailId setter方法
	 * @param listDetailId 设置属性listDetailId的值
	 * @author andy wang<br/>
	 */
	public void setListDetailId(List<String> listDetailId) {
		this.listDetailId = listDetailId;
	}

	public String getOwnerComment() {
		return ownerComment;
	}

	public void setOwnerComment(String ownerComment) {
		this.ownerComment = ownerComment;
	}
	
	

	public MetaSku getSku() {
		return sku;
	}

	public void setSku(MetaSku sku) {
		this.sku = sku;
	}
	
	
	public String getHgMerchantNo() {
		return hgMerchantNo;
	}

	public void setHgMerchantNo(String hgMerchantNo) {
		this.hgMerchantNo = hgMerchantNo;
	}

	/**
	 * 属性 listSavePutawayDetailLocationVO getter方法
	 * @return 属性listSavePutawayDetailLocationVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> getListSavePutawayLocationVO() {
		if ( this.listSavePutawayLocationVO == null ) {
			this.listSavePutawayLocationVO = new ArrayList<RecPutawayLocationVO>();
		}
		return listSavePutawayLocationVO;
	}

	/**
	 * 属性 listSavePutawayDetailLocationVO setter方法
	 * @param listSavePutawayDetailLocationVO 设置属性listSavePutawayDetailLocationVO的值
	 * @author andy wang<br/>
	 */
	public void setListSavePutawayLocationVO(List<RecPutawayLocationVO> listSavePutawayLocationVO) {
		this.listSavePutawayLocationVO = listSavePutawayLocationVO;
	}
	
	/* method **********************************************/
	
	/**
	 * 添加库位
	 * @param location 库位
	 * @version 2017年3月6日 下午6:03:50<br/>
	 * @author andy wang<br/>
	 */
	public void addLocation ( MetaLocation location ) {
		if ( this.listLocation == null ) {
			this.listLocation = new ArrayList<MetaLocation>();
		}
		this.listLocation.add(location);
	}
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月9日 下午2:53:13<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetailVO searchCache () {
		return this;
	}
	
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @param loginUser 当前登录人对象
	 * @version 2017年2月11日 下午11:34:46<br/>
	 * @author andy wang<br/>
	 */
	public void setLoginUser( Principal loginUser ) {
		if ( loginUser == null || this.putawayDetail == null ) {
			return;
		}
		this.putawayDetail.setOrgId(loginUser.getOrgId());
		this.putawayDetail.setWarehouseId(LoginUtil.getWareHouseId());
	}
	
	/**
	 * 添加上架单操作明细
	 * @param ptwLocation 上架单操作明细
	 * @param isPlan 是否计划操作
	 * @version 2017年3月7日 下午10:26:33<br/>
	 * @author andy wang<br/>
	 */
	public void addPtwLocation ( RecPutawayLocationVO ptwLocation , boolean isPlan ) {
		if ( isPlan ) {
			if ( this.listPlanLocationVO == null ) {
				this.listPlanLocationVO = new ArrayList<RecPutawayLocationVO>();
			}
			this.listPlanLocationVO.add(ptwLocation);
		} else {
			if ( this.listRealLocationVO == null ) {
				this.listRealLocationVO = new ArrayList<RecPutawayLocationVO>();
			}
			this.listRealLocationVO.add(ptwLocation);
		}
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
		if ( this.putawayDetail == null ) {
			this.putawayDetail = new RecPutawayDetail();
		}
		this.putawayDetail.setOrgId(loginUser.getOrgId());
		this.putawayDetail.setWarehouseId(LoginUtil.getWareHouseId());
	}
	
	@Override
	public Example getExample () {
		if ( putawayDetail == null ) {
			loginInfo();
		}
		RecPutawayDetail putawayDetail = this.getPutawayDetail();
		Example example = new Example(RecPutawayDetail.class);
		Criteria criteria = example.createCriteria();
		if ( !PubUtil.isEmpty(this.getListPutawayId()) ) {
			criteria.andIn("putawayId", this.getListPutawayId());
		}
		if ( !PubUtil.isEmpty(this.getListDetailId()) ) {
			criteria.andIn("asnDetailId", this.getListDetailId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getLocationId()) ) {
			criteria.andEqualTo("locationId",putawayDetail.getLocationId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getAsnDetailId()) ) {
			criteria.andEqualTo("asnDetailId",putawayDetail.getAsnDetailId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getAsnId()) ) {
			criteria.andEqualTo("asnId",putawayDetail.getAsnId());
		}
//		if ( !StringUtil.isTrimEmpty(putawayDetail.getBatchNo()) ) {
		//此处改动原因：当批次为空时，应匹配批次为空的记录，不匹配有批次的记录 by 王通
		criteria.andEqualTo("batchNo",putawayDetail.getBatchNo());
		if ( !StringUtil.isTrimEmpty(putawayDetail.getMeasureUnit()) ) {
			criteria.andEqualTo("measureUnit",putawayDetail.getMeasureUnit());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getPackId()) ) {
			criteria.andEqualTo("packId",putawayDetail.getPackId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getPutawayDetailId()) ) {
			criteria.andEqualTo("putawayDetailId",putawayDetail.getPutawayDetailId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getPutawayId()) ) {
			criteria.andEqualTo("putawayId",putawayDetail.getPutawayId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getSkuId()) ) {
			criteria.andEqualTo("skuId",putawayDetail.getSkuId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getOrgId()) ) {
			criteria.andEqualTo("orgId",putawayDetail.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId",putawayDetail.getWarehouseId());
		}
		if ( !PubUtil.isEmpty(this.getListAsnId()) ) {
			criteria.andIn("asnId",this.getListAsnId());
		}
		if ( !StringUtil.isTrimEmpty(putawayDetail.getOwner()) ) {
			criteria.andEqualTo("owner",putawayDetail.getOwner());
		}
		if ( !PubUtil.isEmpty(this.getListOwnerId()) ) {
			criteria.andIn("owner",this.getListOwnerId());
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
