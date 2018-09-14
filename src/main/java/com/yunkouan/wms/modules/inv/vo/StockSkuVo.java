package com.yunkouan.wms.modules.inv.vo;

public class StockSkuVo {
	/**货品id**/
	private String skuId;
	/**规格型号**/
	private String skuSpec;
	/**货主id**/
	private String ownerId;
	/**入库库位id**/
	private String locationId;
	/**库位分配计划**/
	private StockLocationVo[] locations;

	private String id;
	private String skuNo;
	private String skuName;
	private String bathNo;
	private String unit;
	private String qty;
	private String weight;
	private String volume;
	
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuSpec() {
		return skuSpec;
	}
	public void setSkuSpec(String skuSpec) {
		this.skuSpec = skuSpec;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public StockLocationVo[] getLocations() {
		return locations;
	}
	public void setLocations(StockLocationVo[] locations) {
		this.locations = locations;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getBathNo() {
		return bathNo;
	}
	public void setBathNo(String bathNo) {
		this.bathNo = bathNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
}