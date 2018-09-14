package com.yunkouan.wms.modules.send.vo.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD) 
public class EmsResponse {
	
	@XmlElement
	private int status;
	
	@XmlElement
	private String no;
	
	@XmlElement
	private String remark;

	/**
	获取status  
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	

	/**
	获取no  
	 * @return the no
	 */
	public String getNo() {
		return no;
	}
	

	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}
	

	/**
	获取remark  
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
