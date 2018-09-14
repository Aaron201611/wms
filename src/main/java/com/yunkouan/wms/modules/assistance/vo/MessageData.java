package com.yunkouan.wms.modules.assistance.vo;

public class MessageData {
	/**20位库位编号**/
	private String locationNo;
	/**库位信息**/
	private String locationComment;
	/**20位料号、海关备案货品编号**/
	private String skuNo;
	/**海关备案帐册号**/
	private String bookNo;
	/**数量；最大18位数字，保留5位小数**/
	private String qty;
	/**中文单位**/
	private String measureUnit;
	/**L料件；C成品**/
	private String materialType = "C";
	/**I：入库；E：出库**/
//	private String enterOrExit;
	/**001保税货物；002非保税货物；003口岸货物；004跨境货物**/
	private String goodType = "004";

	public MessageData(){}
	public MessageData(String locationNo, String locationComment, String skuNo, String qty, String measureUnit, String enterOrExit){
		this.locationNo = locationNo;
		this.locationComment = locationComment;
		this.skuNo = skuNo;
		this.qty = qty;
		this.measureUnit = measureUnit;
//		this.enterOrExit = enterOrExit;
	}

	public String getLocationNo() {
		return locationNo;
	}
	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}
	public String getLocationComment() {
		return locationComment;
	}
	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
//	public String getEnterOrExit() {
//		return enterOrExit;
//	}
//	public void setEnterOrExit(String enterOrExit) {
//		this.enterOrExit = enterOrExit;
//	}
	public String getGoodType() {
		return goodType;
	}
	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}
	public String getBookNo() {
		return bookNo;
	}
	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}
}