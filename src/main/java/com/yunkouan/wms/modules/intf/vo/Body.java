package com.yunkouan.wms.modules.intf.vo;

public class Body {
	private String MESSAGE_ID;
	private String EMS_NO = "J33122017002";
	private String COP_G_NO;//货品代码
	private String G_NO = "";
	private String QTY;//数量
	private String UNIT;//计量单位
	private String STOCK_DATE;//实盘日期（YYYY-MM-DD）
	private String GOODS_NATURE = "1";
	private String BELONG = "";
	private String GOODS_FORM = "";
	private String BOM_VERSION = "";
	private String DATA_TYPE = "B";
	private String WHS_CODE;//库区
	private String LOCATION_CODE;//库位
	private String NOTE = "";

	public String getMESSAGE_ID() {
		return MESSAGE_ID;
	}
	public String getEMS_NO() {
		return EMS_NO;
	}
	public String getCOP_G_NO() {
		return COP_G_NO;
	}
	public String getG_NO() {
		return G_NO;
	}
	public String getQTY() {
		return QTY;
	}
	public String getUNIT() {
		return UNIT;
	}
	public String getSTOCK_DATE() {
		return STOCK_DATE;
	}
	public String getGOODS_NATURE() {
		return GOODS_NATURE;
	}
	public String getBELONG() {
		return BELONG;
	}
	public String getGOODS_FORM() {
		return GOODS_FORM;
	}
	public String getBOM_VERSION() {
		return BOM_VERSION;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public String getWHS_CODE() {
		return WHS_CODE;
	}
	public String getLOCATION_CODE() {
		return LOCATION_CODE;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setMESSAGE_ID(String mESSAGE_ID) {
		MESSAGE_ID = mESSAGE_ID;
	}
	public void setEMS_NO(String eMS_NO) {
		EMS_NO = eMS_NO;
	}
	public void setCOP_G_NO(String cOP_G_NO) {
		COP_G_NO = cOP_G_NO;
	}
	public void setG_NO(String g_NO) {
		G_NO = g_NO;
	}
	public void setQTY(String qTY) {
		QTY = qTY;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	public void setSTOCK_DATE(String sTOCK_DATE) {
		STOCK_DATE = sTOCK_DATE;
	}
	public void setGOODS_NATURE(String gOODS_NATURE) {
		GOODS_NATURE = gOODS_NATURE;
	}
	public void setBELONG(String bELONG) {
		BELONG = bELONG;
	}
	public void setGOODS_FORM(String gOODS_FORM) {
		GOODS_FORM = gOODS_FORM;
	}
	public void setBOM_VERSION(String bOM_VERSION) {
		BOM_VERSION = bOM_VERSION;
	}
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	public void setWHS_CODE(String wHS_CODE) {
		WHS_CODE = wHS_CODE;
	}
	public void setLOCATION_CODE(String lOCATION_CODE) {
		LOCATION_CODE = lOCATION_CODE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
}