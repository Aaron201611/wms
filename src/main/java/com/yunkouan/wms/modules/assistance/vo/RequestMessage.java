package com.yunkouan.wms.modules.assistance.vo;

public class RequestMessage {
	private MessageHead MessageHead = new MessageHead();
	private MessageBody MessageBody = new MessageBody();

	public MessageHead getMessageHead() {
		return MessageHead;
	}
	public void setMessageHead(MessageHead messageHead) {
		MessageHead = messageHead;
	}
	public MessageBody getMessageBody() {
		return MessageBody;
	}
	public void setMessageBody(MessageBody messageBody) {
		MessageBody = messageBody;
	}
}