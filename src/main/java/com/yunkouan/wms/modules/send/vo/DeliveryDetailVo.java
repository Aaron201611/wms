package com.yunkouan.wms.modules.send.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeliveryDetailVo extends BaseVO implements Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7770262024054180782L;

	private SendDeliveryDetail deliveryDetail = new SendDeliveryDetail();
	
	private MetaSku sku;

    private String skuName;
	
	private String skuNo;//货品代码
	
	private String skuBarCode;
	
	private String skuTypeName;
	
	private String specModel;//规格型号
	
	private String measureUnit;//计量单位
    
    private String packUnit; 
    
	private Double perWeight;
	
	private Double perVolume;
	
	private String receiver;
	
	private String srcNo;
	
	private Double perPrice;
	
	
	@JsonIgnore
	private Example example;
	
	private String order = "update_time desc";
	
	private List<String> deliveryIdList = new ArrayList<String>();
	
	private List<String> skuIdList = new ArrayList<String>();
	
	public DeliveryDetailVo(){
		this.example = new Example(SendDeliveryDetail.class);
		this.example.createCriteria();
	}
	
	private List<SendPickLocationVo> planPickLocations = new ArrayList<SendPickLocationVo>();
	
	private List<SendPickLocationVo> realPickLocations = new ArrayList<SendPickLocationVo>();
	
	@JsonIgnore
	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(StringUtils.isNotEmpty(deliveryDetail.getOrgId())){
			c.andEqualTo("orgId",deliveryDetail.getOrgId());
		}
		if(StringUtils.isNotEmpty(deliveryDetail.getWarehouseId())){
			c.andEqualTo("warehouseId",deliveryDetail.getWarehouseId());
		}
		if(deliveryIdList !=null && !deliveryIdList.isEmpty()){
			c.andIn("deliveryId", deliveryIdList);
		}
		if(skuIdList !=null && !skuIdList.isEmpty()){
			c.andIn("skuId", skuIdList);
		}
		if(StringUtils.isNotEmpty(deliveryDetail.getDeliveryId())){
			c.andEqualTo("deliveryId",deliveryDetail.getDeliveryId());
		}
		return this.example;
	}

	public SendDeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}

	public void setDeliveryDetail(SendDeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	
    
	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	

	public Double getPerWeight() {
		return perWeight;
	}

	public void setPerWeight(Double perWeight) {
		this.perWeight = perWeight;
	}

	public Double getPerVolume() {
		return perVolume;
	}

	public void setPerVolume(Double perVolume) {
		this.perVolume = perVolume;
	}

	public List<SendPickLocationVo> getRealPickLocations() {
		return realPickLocations;
	}

	public void setRealPickLocations(List<SendPickLocationVo> realPickLocations) {
		this.realPickLocations = realPickLocations;
	}

	@JsonIgnore
	public Example getExample() {
		return example;
	}

	@JsonIgnore
	public void setExample(Example example) {
		this.example = example;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<String> getDeliveryIdList() {
		return deliveryIdList;
	}

	public void setDeliveryIdList(List<String> deliveryIdList) {
		this.deliveryIdList = deliveryIdList;
	}

	public String getSkuBarCode() {
		return skuBarCode;
	}

	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}

	/**
	 * 属性 skuIdList getter方法
	 * @return 属性skuIdList
	 * @author 王通<br/>
	 */
	public List<String> getSkuIdList() {
		return skuIdList;
	}

	/**
	 * 属性 skuIdList setter方法
	 * @param skuIdList 设置属性skuIdList的值
	 * @author 王通<br/>
	 */
	public void setSkuIdList(List<String> skuIdList) {
		this.skuIdList = skuIdList;
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
	 * 属性 receiver getter方法
	 * @return 属性receiver
	 * @author 王通<br/>
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * 属性 receiver setter方法
	 * @param receiver 设置属性receiver的值
	 * @author 王通<br/>
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * 属性 perPrice getter方法
	 * @return 属性perPrice
	 * @author 王通<br/>
	 */
	public Double getPerPrice() {
		return perPrice;
	}

	/**
	 * 属性 perPrice setter方法
	 * @param perPrice 设置属性perPrice的值
	 * @author 王通<br/>
	 */
	public void setPerPrice(Double perPrice) {
		this.perPrice = perPrice;
	}

	public MetaSku getSku() {
		return sku;
	}

	public void setSku(MetaSku sku) {
		this.sku = sku;
	}

	public String getSkuTypeName() {
		return skuTypeName;
	}

	public void setSkuTypeName(String skuTypeName) {
		this.skuTypeName = skuTypeName;
	}

	public List<SendPickLocationVo> getPlanPickLocations() {
		return planPickLocations;
	}

	public void setPlanPickLocations(List<SendPickLocationVo> planPickLocations) {
		this.planPickLocations = planPickLocations;
	}

	@Override
	public DeliveryDetailVo clone() {
		DeliveryDetailVo vo = null;
		try {
			vo = (DeliveryDetailVo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return vo;
	}
}
