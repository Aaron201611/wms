package com.yunkouan.wms.modules.intf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.assistance.service.IAssisService;
import com.yunkouan.wms.modules.assistance.vo.MessageData;

public class TestAssis extends BaseJunitTest {
	private IAssisService service;

	@Test
	public void test() throws IOException {
		List<MessageData> list = new ArrayList<MessageData>();
		for(int i=0; i<10; ++i) {
			MessageData data = new MessageData();
			data.setLocationComment("");
			data.setLocationNo("");
			data.setMeasureUnit("千克");
			data.setQty("123456.78");
			data.setSkuNo("12345678");
			list.add(data);
		}
		service = SpringContextHolder.getBean("assisService");
		service.request("1", "0", "1", true, list);
		service.response();
	}
}