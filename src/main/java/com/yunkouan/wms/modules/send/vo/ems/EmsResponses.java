package com.yunkouan.wms.modules.send.vo.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD) 
public class EmsResponses {

	@XmlElement
	private List<EmsResponse> Response;

	/**
	获取list  
	 * @return the list
	 */
	public List<EmsResponse> getList() {
		return Response;
	}
	

	/**
	 * @param list the list to set
	 */
	public void setList(List<EmsResponse> list) {
		this.Response = list;
	}
	
	
	
}
