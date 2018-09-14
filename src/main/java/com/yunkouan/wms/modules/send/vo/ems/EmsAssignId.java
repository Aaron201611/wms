package com.yunkouan.wms.modules.send.vo.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD) 
public class EmsAssignId {
	
	@XmlElement
	private String billno;

	/**
	获取billno  
	 * @return the billno
	 */
	public String getBillno() {
		return billno;
	}
	

	/**
	 * @param billno the billno to set
	 */
	public void setBillno(String billno) {
		this.billno = billno;
	}
	
	
}
