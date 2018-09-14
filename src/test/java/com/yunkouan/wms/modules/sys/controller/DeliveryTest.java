package com.yunkouan.wms.modules.sys.controller;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.send.controller.DeliveryController;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

public class DeliveryTest extends BaseJunitTest{

	@Autowired
	private DeliveryController deliveryController;
	
	private String org_id = "10131";
	private String ware_id = "10231";
	
	@Test
	public void test_add(){
		SendDeliveryVo req = new SendDeliveryVo();
		SendDelivery delivery = new SendDelivery();
		delivery.setDeliveryNo("20170113");
		delivery.setOwner("12345");
		delivery.setSender("12345");
		delivery.setDeliveryStatus(Constant.STATUS_OPEN);
		delivery.setOrgId(org_id);
		delivery.setWaveId(ware_id);
		delivery.setCreatePerson("w01");
		delivery.setCreateTime(new Date());
		delivery.setUpdateTime(new Date());
		req.setSendDelivery(delivery);
		
		try {
//			deliveryController.addAndUpdate(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
