package com.yunkouan.wms.modules.send.vo.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD) 
@XmlRootElement(name="response")
public class EmsLogisticsNo {

	@XmlElement
	private String Result;

	@XmlElement
	private String errorDesc;
	
	@XmlElement
	private String errorCode;
	
	@XmlElement
	private EmsAssignIds assignIds;

	/**
	获取result  
	 * @return the result
	 */
	public String getResult() {
		return Result;
	}
	

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		Result = result;
	}
	

	/**
	获取errorDesc  
	 * @return the errorDesc
	 */
	public String getErrorDesc() {
		return errorDesc;
	}
	

	/**
	 * @param errorDesc the errorDesc to set
	 */
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	

	/**
	获取errorCode  
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	

	/**
	获取assignIds  
	 * @return the assignIds
	 */
	public EmsAssignIds getAssignIds() {
		return assignIds;
	}
	

	/**
	 * @param assignIds the assignIds to set
	 */
	public void setAssignIds(EmsAssignIds assignIds) {
		this.assignIds = assignIds;
	}

}
