package com.yunkouan.wms.modules.send.vo.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)  
public class EmsHead {

	@XmlElement
	private int Result;

	@XmlElement
	private String MsgType;

	@XmlElement
	private String Desc;


	/**
获取result  
	 * @return the result
	 */
	public int getResult() {
		return Result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		this.Result = result;
	}

	/**
获取msgType  
	 * @return the msgType
	 */
	public String getMsgType() {
		return MsgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.MsgType = msgType;
	}

	/**
获取desc  
	 * @return the desc
	 */
	public String getDesc() {
		return Desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.Desc = desc;
	}


}
