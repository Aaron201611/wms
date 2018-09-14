package com.yunkouan.wms.modules.send.vo.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD) 
@XmlRootElement(name="Msg")
public class EmsMsg {

	@XmlElement
	private EmsHead Head;

	@XmlElement
	private EmsBody Body;

	/**
	获取head  
	 * @return the head
	 */
	public EmsHead getHead() {
		return Head;
	}


	/**
	 * @param head the head to set
	 */
	public void setHead(EmsHead head) {
		this.Head = head;
	}


	/**
	获取body  
	 * @return the body
	 */
	public EmsBody getBody() {
		return Body;
	}


	/**
	 * @param body the body to set
	 */
	public void setBody(EmsBody body) {
		this.Body = body;
	}


}
