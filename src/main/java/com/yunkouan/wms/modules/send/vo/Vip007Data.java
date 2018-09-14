package com.yunkouan.wms.modules.send.vo;

/** 
* @Description: 往快递公司推送运单详情
* @author tphe06
* @date 2018年4月19日 上午10:18:10  
*/
public class Vip007Data {
	/**运单编号/申通快递单号**/
	private String billno;
	/**寄件日期yyyy-mm-dd/当前系统时间**/
	private String senddate;
	/**中文网点名称/**/
	private String sendsite;
	/**客户名称/申通网点维护客户名称**/
	private String sendcus;
	/**寄件人/自定义或维护的客户名称**/
	private String sendperson;
	/**寄件人电话**/
	private String sendtel;
	/**收件客户**/
	private String receivecus;
	/**收件人**/
	private String receiveperson;
	/**收件人电话**/
	private String receivetel;
	/**内件品名**/
	private String goodsname;
	/**录入时间yyyy-mm-dd/取当前系统时间**/
	private String inputdate;
	/**录入人/申通网点维护客户名称**/
	private String inputperson;
	/**录入网点/申通网点中文名称**/
	private String inputsite;
	/**最后编辑时间**/
	private String lasteditdate;
	/**最后编辑人**/
	private String lasteditperson;
	/**最后编辑网点**/
	private String lasteditsite;
	/**备注**/
	private String remark;
	/**收件省份**/
	private String receiveprovince;
	/**收件城市**/
	private String receivecity;
	/**收件地区**/
	private String receivearea;
	/**收件详细地址**/
	private String receiveaddress;
	/**寄件省份**/
	private String sendprovince;
	/**寄件城市**/
	private String sendcity;
	/**寄件地区**/
	private String sendarea;
	/**寄件详细地址**/
	private String sendaddress;
	/**重量**/
	private String weight;
	/**产品代码**/
	private String productcode;
	/**订单号/商品的订单号，必填**/
	private String orderno;

	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getSendsite() {
		return sendsite;
	}
	public void setSendsite(String sendsite) {
		this.sendsite = sendsite;
	}
	public String getSendcus() {
		return sendcus;
	}
	public void setSendcus(String sendcus) {
		this.sendcus = sendcus;
	}
	public String getSendperson() {
		return sendperson;
	}
	public void setSendperson(String sendperson) {
		this.sendperson = sendperson;
	}
	public String getSendtel() {
		return sendtel;
	}
	public void setSendtel(String sendtel) {
		this.sendtel = sendtel;
	}
	public String getReceivecus() {
		return receivecus;
	}
	public void setReceivecus(String receivecus) {
		this.receivecus = receivecus;
	}
	public String getReceiveperson() {
		return receiveperson;
	}
	public void setReceiveperson(String receiveperson) {
		this.receiveperson = receiveperson;
	}
	public String getReceivetel() {
		return receivetel;
	}
	public void setReceivetel(String receivetel) {
		this.receivetel = receivetel;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getInputdate() {
		return inputdate;
	}
	public void setInputdate(String inputdate) {
		this.inputdate = inputdate;
	}
	public String getInputperson() {
		return inputperson;
	}
	public void setInputperson(String inputperson) {
		this.inputperson = inputperson;
	}
	public String getInputsite() {
		return inputsite;
	}
	public void setInputsite(String inputsite) {
		this.inputsite = inputsite;
	}
	public String getLasteditdate() {
		return lasteditdate;
	}
	public void setLasteditdate(String lasteditdate) {
		this.lasteditdate = lasteditdate;
	}
	public String getLasteditperson() {
		return lasteditperson;
	}
	public void setLasteditperson(String lasteditperson) {
		this.lasteditperson = lasteditperson;
	}
	public String getLasteditsite() {
		return lasteditsite;
	}
	public void setLasteditsite(String lasteditsite) {
		this.lasteditsite = lasteditsite;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReceiveprovince() {
		return receiveprovince;
	}
	public void setReceiveprovince(String receiveprovince) {
		this.receiveprovince = receiveprovince;
	}
	public String getReceivecity() {
		return receivecity;
	}
	public void setReceivecity(String receivecity) {
		this.receivecity = receivecity;
	}
	public String getReceivearea() {
		return receivearea;
	}
	public void setReceivearea(String receivearea) {
		this.receivearea = receivearea;
	}
	public String getReceiveaddress() {
		return receiveaddress;
	}
	public void setReceiveaddress(String receiveaddress) {
		this.receiveaddress = receiveaddress;
	}
	public String getSendprovince() {
		return sendprovince;
	}
	public void setSendprovince(String sendprovince) {
		this.sendprovince = sendprovince;
	}
	public String getSendcity() {
		return sendcity;
	}
	public void setSendcity(String sendcity) {
		this.sendcity = sendcity;
	}
	public String getSendarea() {
		return sendarea;
	}
	public void setSendarea(String sendarea) {
		this.sendarea = sendarea;
	}
	public String getSendaddress() {
		return sendaddress;
	}
	public void setSendaddress(String sendaddress) {
		this.sendaddress = sendaddress;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
}