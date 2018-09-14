package com.yunkouan.wms.common;

import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.intf.vo.Message;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.vo.SendDelivery2ExternalVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

/**
 * @author tphe06 2017年2月22日
 */
public class TestJson extends BaseJunitTest {
	@Test
	public void test() {
		SendDelivery2ExternalVo vo1 = new SendDelivery2ExternalVo();
		DeliverGoodsApplicationVo vo2 = new DeliverGoodsApplicationVo();
		vo1.setApplicationVo(vo2);
		String json1 = JsonUtil.toJson(vo1);
		
		Message vo = new Message();
		vo.setData(json1);
		vo.setNotify_type("10");
		vo.setSign("AABBCC");
		
		String json = JsonUtil.toJson(vo);
		System.out.println(json);
	}
	
	@Test
	@Ignore
	public void testToJson() {
		SendDeliveryVo sdVo = new SendDeliveryVo();
		SendDelivery entity = new SendDelivery();
		sdVo.setOwnerName("阿里");
		entity.setSrcNo("123");
		entity.setDeliveryNo("234");
		entity.setDeliveryStatus(Constant.SEND_STATUS_OPEN);
		sdVo.setSendDelivery(entity);
		Page<SendDeliveryVo> page = new Page<SendDeliveryVo>();
		page.setPageNum(1);
		page.setPageSize(10);
		sdVo.setPage(page);
		String data = JsonUtil.toJson(sdVo);
		System.out.println(data);
	}

	@Test
	@Ignore
	public void testJson() {
		SendDeliveryVo vo = JsonUtil.fromJson("{'ownerName':'之后', 'sendDelivery':{'deliveryNo':'1001'}}", SendDeliveryVo.class);
		System.out.println(vo.getOwnerName());
		System.out.println(vo.getSendDelivery().getDeliveryNo());
	}

	@Test
	@Ignore
	public void testFastjson() throws JsonProcessingException {
//		long start = System.currentTimeMillis();
//		for(int i=0;i<100000;++i) {
//			Token t = new Token();
//			t.setStatus("1");
//			List<MetaWarehouse> list = new ArrayList<MetaWarehouse>();
//			MetaWarehouse h1 = new MetaWarehouse();
//			h1.setWarehouseId("1");
//			h1.setWarehouseName("w1");
//			list.add(h1);
//			MetaWarehouse h2 = new MetaWarehouse();
//			h2.setWarehouseId("2");
//			h2.setWarehouseName("w2");
//			list.add(h2);
//			t.setList(list);
//			String json = JSON.toJSONString(t);
//			Token b = JSON.parseObject(json, Token.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("fastjson:"+(end-start));
	}

	@Test
	@Ignore
	public void testObjectMapper() throws IOException {
//		long start = System.currentTimeMillis();
//		for(int i=0;i<100000;++i) {
//			Token t = new Token();
//			t.setStatus("1");
//			List<MetaWarehouse> list = new ArrayList<MetaWarehouse>();
//			MetaWarehouse h1 = new MetaWarehouse();
//			h1.setWarehouseId("1");
//			h1.setWarehouseName("w1");
//			list.add(h1);
//			MetaWarehouse h2 = new MetaWarehouse();
//			h2.setWarehouseId("2");
//			h2.setWarehouseName("w2");
//			list.add(h2);
//			t.setList(list);
//			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.setSerializationInclusion(Include.NON_NULL);
//			String json = objectMapper.writeValueAsString(t);
//			Token b = objectMapper.readValue(json, Token.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("objectmapper:"+(end-start));
	}
}