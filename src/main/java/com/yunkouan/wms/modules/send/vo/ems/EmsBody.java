package com.yunkouan.wms.modules.send.vo.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)  
public class EmsBody {

	@XmlElement
	private EmsResponses Responses;

	/**
	获取responses  
	 * @return the responses
	 */
	public EmsResponses getResponses() {
		return Responses;
	}


	/**
	 * @param responses the responses to set
	 */
	public void setResponses(EmsResponses responses) {
		this.Responses = responses;
	}
}
