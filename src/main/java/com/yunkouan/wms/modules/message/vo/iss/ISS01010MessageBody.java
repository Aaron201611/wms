package com.yunkouan.wms.modules.message.vo.iss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="messageBody",namespace="")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ISS01010MessageBody {
	
	private ISS01010Declaration declaration;

	@XmlElement(name="Declaration")
	public ISS01010Declaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(ISS01010Declaration declaration) {
		this.declaration = declaration;
	}
	
	

}
