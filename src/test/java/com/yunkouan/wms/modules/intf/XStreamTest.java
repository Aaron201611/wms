package com.yunkouan.wms.modules.intf;

import java.io.IOException;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.yunkouan.util.FileUtil;
import com.yunkouan.wms.modules.assistance.vo.MessageBody;
import com.yunkouan.wms.modules.assistance.vo.MessageHead;
import com.yunkouan.wms.modules.assistance.vo.RequestMessage;
import com.yunkouan.wms.modules.assistance.vo.WarehouseHead;
import com.yunkouan.wms.modules.assistance.vo.WarehouseRtnDocument;
import com.yunkouan.wms.modules.assistance.vo.WarehouseRtnHead;

public class XStreamTest {
	public static void main(String[] args) throws IOException {
		RequestMessage msg = new RequestMessage();
		msg.getMessageHead().setMESSAGE_ID("111111");
		for(int i=0; i<10000; ++i) {
			WarehouseHead body = new WarehouseHead();
			body.setWAREHOUSE_NO("222222");
			List<WarehouseHead> list = msg.getMessageBody().getWarehouseDocument();
			list.add(body);
		}
//		msg.getMessageBody().getWarehouseRtnDocument().getWarehouseRtnHead().setRESULT_CODE("000000");
		msg.getMessageBody().setWarehouseRtnDocument(null);
		XStream x = new XStream(new XppDriver(new XmlFriendlyReplacer("-_", "_")));
		x.alias("RequestMessage", RequestMessage.class);
		x.alias("MessageHead", MessageHead.class);
		x.alias("MessageBody", MessageBody.class);
		x.alias("WarehouseHead", WarehouseHead.class);
		x.alias("WarehouseRtnDocument", WarehouseRtnDocument.class);
		x.alias("WarehouseRtnHead", WarehouseRtnHead.class);
		String xml = x.toXML(msg);
		long start = System.currentTimeMillis();
		for(int i=0; i<10000; ++i) {
			FileUtil.write("e:/1_1.xml", xml);
		}
		long end = System.currentTimeMillis();
		System.out.println("time:"+(end-start));
		RequestMessage r = (RequestMessage)x.fromXML(xml);
		System.out.println(r.getMessageBody().getWarehouseDocument().get(1).getWAREHOUSE_NO());
	}
}