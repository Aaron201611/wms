package com.yunkouan.wms.modules.inv.vo;

public class StockLocationVo {
	/**批次货品**/
	private String bathNo;
	/**待分配库位id**/
	private String locationId;
	private String locationComment;
	/**分配数量**/
	private String qty;
	/**分配重量**/
	private String weight;
	/**分配体积**/
	private String volume;

	public String getBathNo() {
		return bathNo;
	}
	public void setBathNo(String bathNo) {
		this.bathNo = bathNo;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
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
	public String getLocationComment() {
		return locationComment;
	}
	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}
}