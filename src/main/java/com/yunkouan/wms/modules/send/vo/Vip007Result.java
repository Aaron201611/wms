package com.yunkouan.wms.modules.send.vo;

import java.util.List;

/** 
* @Description: 往快递公司推送运单结果
* @author tphe06
* @date 2018年4月19日 上午10:16:42  
*/
public class Vip007Result {
	/**结果：成功or失败**/
	private Boolean success;
	/**错误码**/
	private String message;
	/**订单号、运单号等其他扩展字段**/
	private List<Vip007Return> data;

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
	public List<Vip007Return> getData() {
		return data;
	}
	public void setData(List<Vip007Return> data) {
		this.data = data;
	}
}