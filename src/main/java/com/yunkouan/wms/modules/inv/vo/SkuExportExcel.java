package com.yunkouan.wms.modules.inv.vo;

public class SkuExportExcel {

	private String merchantName;
	/**货品名称*/
	private String skuName;
	/**货品代码*/
	private String skuNo;
	/**计量单位名称*/
	private String measureUnit;
	/**规格型号*/
	private String specModel;
	/**单重*/
	private Double perWeight;
	/**
	 * 货品长度
	 */
	private Double length;
	/**
	 * 货品宽度
	 */
	private Double width;
	/**
	 * 货品高度
	 */
	private Double height;
	
	private String skuTypeName;
	
	/**最小安全库存*/
	private Integer minSafetyStock;
	/**最大安全库存*/
	private Integer maxSafetyStock;

	/**minReplenishStock:补货警戒数量**/
	private Integer minReplenishStock;
	/**maxReplenishStock:补货上限数量**/
	private Integer maxReplenishStock;
	/**hgGoodsNo:海关货号**/
	private String hgGoodsNo;
	
	/**项号*/
	private String gNo;
	/**料件性质*/
	private String goodsNatureComment;
	
	/**币制*/
	private String currComment;

	/**原产国*/
	private String originCountryName;
	/**hsCode:海关归类税号**/
	private String hsCode;
	
	private String unit1;

	private String unit2;


	/**perPrice:单价（元）**/
	private Double perPrice;
	/**declarePrice:申报价（元）**/
	private Double declarePrice;
	/**brand:品牌**/
	private String brand;
	
	private String attribute1;
	private String attribute2;
	private String attribute3;
	private String attribute4;
	
	/**保质期（天）*/
	private Integer shelflife;
	/**预警期（天）*/
	private Integer overdueWarningDays;
	private String note;
	private String skuStatus;
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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


	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getSkuTypeName() {
		return skuTypeName;
	}

	public void setSkuTypeName(String skuTypeName) {
		this.skuTypeName = skuTypeName;
	}

	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}


	public Integer getShelflife() {
		return shelflife;
	}

	public void setShelflife(Integer shelflife) {
		this.shelflife = shelflife;
	}

	public Integer getOverdueWarningDays() {
		return overdueWarningDays;
	}

	public void setOverdueWarningDays(Integer overdueWarningDays) {
		this.overdueWarningDays = overdueWarningDays;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Integer getMinSafetyStock() {
		return minSafetyStock;
	}

	public void setMinSafetyStock(Integer minSafetyStock) {
		this.minSafetyStock = minSafetyStock;
	}

	public Integer getMaxSafetyStock() {
		return maxSafetyStock;
	}

	public void setMaxSafetyStock(Integer maxSafetyStock) {
		this.maxSafetyStock = maxSafetyStock;
	}

	public Integer getMinReplenishStock() {
		return minReplenishStock;
	}

	public void setMinReplenishStock(Integer minReplenishStock) {
		this.minReplenishStock = minReplenishStock;
	}

	public Integer getMaxReplenishStock() {
		return maxReplenishStock;
	}

	public void setMaxReplenishStock(Integer maxReplenishStock) {
		this.maxReplenishStock = maxReplenishStock;
	}

	public String getGNo() {
		return gNo;
	}

	public void setGNo(String gNo) {
		this.gNo = gNo;
	}

	public String getGoodsNatureComment() {
		return goodsNatureComment;
	}

	public void setGoodsNatureComment(String goodsNatureComment) {
		this.goodsNatureComment = goodsNatureComment;
	}

	public String getCurrComment() {
		return currComment;
	}

	public void setCurrComment(String currComment) {
		this.currComment = currComment;
	}

	public String getOriginCountryName() {
		return originCountryName;
	}

	public void setOriginCountryName(String originCountryName) {
		this.originCountryName = originCountryName;
	}


	public Double getPerWeight() {
		return perWeight;
	}

	public void setPerWeight(Double perWeight) {
		this.perWeight = perWeight;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getHgGoodsNo() {
		return hgGoodsNo;
	}

	public void setHgGoodsNo(String hgGoodsNo) {
		this.hgGoodsNo = hgGoodsNo;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public Double getPerPrice() {
		return perPrice;
	}

	public void setPerPrice(Double perPrice) {
		this.perPrice = perPrice;
	}

	public Double getDeclarePrice() {
		return declarePrice;
	}

	public void setDeclarePrice(Double declarePrice) {
		this.declarePrice = declarePrice;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getSkuStatus() {
		return skuStatus;
	}

	public void setSkuStatus(String skuStatus) {
		this.skuStatus = skuStatus;
	}

	public String getUnit1() {
		return unit1;
	}


	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}


	public String getUnit2() {
		return unit2;
	}


	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

}
