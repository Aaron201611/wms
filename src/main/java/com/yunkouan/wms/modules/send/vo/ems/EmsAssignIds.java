package com.yunkouan.wms.modules.send.vo.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD) 
public class EmsAssignIds {
	@XmlElement
	private List<EmsAssignId> assignId;

	/**
	获取assignId  
	 * @return the assignId
	 */
	public List<EmsAssignId> getAssignId() {
		return assignId;
	}
	

	/**
	 * @param assignId the assignId to set
	 */
	public void setAssignId(List<EmsAssignId> assignId) {
		this.assignId = assignId;
	}
	
}