package com.yunkouan.wms.modules.send.vo;

/** 
* @Description: 获取快递公司运单号结果
* @author tphe06
* @date 2018年4月19日 上午10:21:17  
*/
public class Vip009Result {
	/**结果：成功or失败**/
	private Boolean success;
	/**错误码**/
	private String message;
	/**运单号**/
	private String data;

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}