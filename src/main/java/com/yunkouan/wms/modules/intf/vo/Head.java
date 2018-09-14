package com.yunkouan.wms.modules.intf.vo;

public class Head {
	private String MESSAGE_ID;
	private String MESSAGE_TYPE = "2006";
	private String EMS_NO = "J33122017002";
	private String ORDER_NO = "";
	private String FUNCTION_CODE = "";
	private String CHK_RESULT = "";
	private String MESSAGE_DATE;//报文传输时间（YYYY-MM-DDTHH:MM:SS）
	private String RECEIVER_ID;
	private String RECEIVER_ADDRESS = "";
	private String SENDER_ID;
	private String SEND_ADDRESS = "";
	private String MESSAGE_SIGN = "";
	private String SEND_TYPE = "0";

	public String getMESSAGE_ID() {
		return MESSAGE_ID;
	}
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}
	public String getEMS_NO() {
		return EMS_NO;
	}
	public String getORDER_NO() {
		return ORDER_NO;
	}
	public String getFUNCTION_CODE() {
		return FUNCTION_CODE;
	}
	public String getCHK_RESULT() {
		return CHK_RESULT;
	}
	public String getMESSAGE_DATE() {
		return MESSAGE_DATE;
	}
	public String getRECEIVER_ID() {
		return RECEIVER_ID;
	}
	public String getRECEIVER_ADDRESS() {
		return RECEIVER_ADDRESS;
	}
	public String getSENDER_ID() {
		return SENDER_ID;
	}
	public String getSEND_ADDRESS() {
		return SEND_ADDRESS;
	}
	public String getMESSAGE_SIGN() {
		return MESSAGE_SIGN;
	}
	public String getSEND_TYPE() {
		return SEND_TYPE;
	}
	public void setMESSAGE_ID(String mESSAGE_ID) {
		MESSAGE_ID = mESSAGE_ID;
	}
	public void setMESSAGE_TYPE(String mESSAGE_TYPE) {
		MESSAGE_TYPE = mESSAGE_TYPE;
	}
	public void setEMS_NO(String eMS_NO) {
		EMS_NO = eMS_NO;
	}
	public void setORDER_NO(String oRDER_NO) {
		ORDER_NO = oRDER_NO;
	}
	public void setFUNCTION_CODE(String fUNCTION_CODE) {
		FUNCTION_CODE = fUNCTION_CODE;
	}
	public void setCHK_RESULT(String cHK_RESULT) {
		CHK_RESULT = cHK_RESULT;
	}
	public void setMESSAGE_DATE(String mESSAGE_DATE) {
		MESSAGE_DATE = mESSAGE_DATE;
	}
	public void setRECEIVER_ID(String rECEIVER_ID) {
		RECEIVER_ID = rECEIVER_ID;
	}
	public void setRECEIVER_ADDRESS(String rECEIVER_ADDRESS) {
		RECEIVER_ADDRESS = rECEIVER_ADDRESS;
	}
	public void setSENDER_ID(String sENDER_ID) {
		SENDER_ID = sENDER_ID;
	}
	public void setSEND_ADDRESS(String sEND_ADDRESS) {
		SEND_ADDRESS = sEND_ADDRESS;
	}
	public void setMESSAGE_SIGN(String mESSAGE_SIGN) {
		MESSAGE_SIGN = mESSAGE_SIGN;
	}
	public void setSEND_TYPE(String sEND_TYPE) {
		SEND_TYPE = sEND_TYPE;
	}
}