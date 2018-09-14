package com.yunkouan.wms.modules.send.vo;

/** 
* @Description: 往快递公司推送运单结果扩展信息
* @author tphe06
* @date 2018年4月19日 上午10:17:24  
*/
public class Vip007Return {
	/**订单号**/
	private String orderno;
	/**大字**/
	private String bigchar;
	/**集包地**/
	private String packageName;
	/**运单编号**/
	private String expno;

	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getBigchar() {
		return bigchar;
	}
	public void setBigchar(String bigchar) {
		this.bigchar = bigchar;
	}
	public String getExpno() {
		return expno;
	}
	public void setExpno(String expno) {
		this.expno = expno;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}