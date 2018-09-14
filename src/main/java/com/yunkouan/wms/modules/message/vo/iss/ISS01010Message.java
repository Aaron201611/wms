package com.yunkouan.wms.modules.message.vo.iss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Message",namespace="")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"messageHead","messageBody"})
public class ISS01010Message {

	private ISSMessageHead messageHead;
	
	private ISS01010MessageBody messageBody;

	@XmlElement(name="messageHead")
	public ISSMessageHead getMessageHead() {
		return messageHead;
	}

	public void setMessageHead(ISSMessageHead messageHead) {
		this.messageHead = messageHead;
	}

	@XmlElement(name="messageBody")
	public ISS01010MessageBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(ISS01010MessageBody messageBody) {
		this.messageBody = messageBody;
	}
	
	
}
