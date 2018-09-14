package com.yunkouan.wms.modules.send.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.util.MD5Util;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.ExtInterf;
import com.yunkouan.wms.modules.intf.controller.ExternalController;
import com.yunkouan.wms.modules.intf.vo.Message;
import com.yunkouan.wms.modules.meta.vo.SkuVo;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.send.vo.SendDelivery2ExternalVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryDetailVo2ExtenalVo;

public class ExternalInterfaceTest extends BaseJunitTest{
	
	@Autowired
	private ExternalController externalController;
	@Autowired
	private ISysParamService paramService;

	@Test
	@Ignore
	public void testSku() throws IOException {
		Message vo = new Message();
		SkuVo sku = new SkuVo();
		sku.setCurrentPage(1);
		sku.setPageSize(10);
		String data = JsonUtil.toJson(sku);
		System.out.println("data==="+data);
		vo.setData(data);
		vo.setNotify_id(String.valueOf(System.currentTimeMillis()));
		vo.setNotify_time(new Date().getTime()+"");
		vo.setNotify_type(ExtInterf.INTERFACE_QUERY_SKU.getValue());
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		String sign = md5(vo.getData(), key);
		vo.setSign(sign);
		BeanPropertyBindingResult br = super.validateEntity(vo, ValidSearch.class);
		externalController.interf(vo, br, request, response);
	}

	@Test
	@Ignore
	public void test_importDelivery(){
		try {
			Message vo = new Message();
			vo.setNotify_id("2017083012121");
			vo.setNotify_type(ExtInterf.INTERFACE_SEND_ORDER.getValue());
			vo.setNotify_time(new Date().getTime()+"");
			SendDelivery2ExternalVo deliveryVo = new SendDelivery2ExternalVo();
			deliveryVo.getSendDelivery().setSrcNo("10");
			deliveryVo.getSendDelivery().setDocType(200);
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-26 16:16:20");
			deliveryVo.getSendDelivery().setOrderTime(d);
			deliveryVo.getSendDelivery().setContactPerson("82BE12AE461947A8B1D265F156D68F69");
			deliveryVo.getSendDelivery().setContactAddress("广东省广州市天河区19号");
			deliveryVo.getSendDelivery().setContactPhone("13710821987");
			deliveryVo.getSendDelivery().setProvince("广东");
			deliveryVo.getSendDelivery().setCity("广州");
			deliveryVo.getSendDelivery().setCounty("天河区");
			deliveryVo.getSendDelivery().setFreightCharge(1d);
			deliveryVo.getSendDelivery().setGrossWeight(2d);
			deliveryVo.getSendDelivery().setInsuranceCharge(3d);
			deliveryVo.getSendDelivery().setDeliveryNo2("A00001");
			SendDeliveryDetailVo2ExtenalVo detailVo1 = new SendDeliveryDetailVo2ExtenalVo();
			detailVo1.setSkuNo("A01111111");	
			detailVo1.getDeliveryDetail().setOrderQty(10d);
			deliveryVo.getDeliveryDetailVoList().add(detailVo1);
			String data = JsonUtil.toJson(deliveryVo);
			System.out.println("data==="+data);
			vo.setData(data);
			String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
			String sign = md5(vo.getData(), key);
			vo.setSign(sign);
			BeanPropertyBindingResult br = super.validateEntity(vo, ValidSearch.class);
//			externalController.interf(vo, br,request);
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	@Ignore
	public void test_qryStatus(){
		Message vo = new Message();
		vo.setNotify_id("2017083012122");
		vo.setNotify_type(ExtInterf.INTERFACE_QUERY_ORDER_STATUS.getValue());
		vo.setNotify_time(new Date().getTime()+"");
		SendDelivery2ExternalVo deliveryVo = new SendDelivery2ExternalVo();
		deliveryVo.getSendDelivery().setSrcNo("PO2017083010001");
		String data = JsonUtil.toJson(deliveryVo);
		System.out.println("data==="+data);
		vo.setData(data);
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		String sign = md5(vo.getData(), key);
		vo.setSign(sign);			
		BeanPropertyBindingResult br = super.validateEntity(vo, ValidSearch.class);
//		ResultModel rm = externalController.interf(vo, br,request);
//		super.formatResult(rm);
	}
	
	@Test
	@Ignore
	public void test_cancelToInterface(){
		Message vo = new Message();
		vo.setNotify_id("2017083012123");
		vo.setNotify_type(ExtInterf.INTERFACE_CANCEL_ORDER.getValue());
		vo.setNotify_time(new Date().getTime()+"");
		SendDelivery2ExternalVo deliveryVo = new SendDelivery2ExternalVo();
		deliveryVo.getSendDelivery().setSrcNo("PO2017083010001");
		String data = JsonUtil.toJson(deliveryVo);
		vo.setData(data);
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		String sign = md5(vo.getData(), key);
		vo.setSign(sign);			
		BeanPropertyBindingResult br = super.validateEntity(vo, ValidSearch.class);
//		ResultModel rm = externalController.interf(vo, br,request);
//		super.formatResult(rm);
	}
	
	@Test
	public void test_cancelAsn(){
		Message vo = new Message();
		vo.setNotify_id("2017083012123");
		vo.setNotify_type(ExtInterf.INTERFACE_CANCEL_ASN.getValue());
		vo.setNotify_time(new Date().getTime()+"");
		RecAsnVO asnVo = new RecAsnVO(new RecAsn());
		asnVo.getAsn().setAsnNo("201808230001");
		String data = JsonUtil.toJson(asnVo);
		System.out.println(data);
		vo.setData(data);
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		String sign = md5(vo.getData(), key);
		vo.setSign(sign);	
		System.out.println(sign);
		try {
			BindingResult br = super.validateEntity(vo, ValidSearch.class); 
			externalController.interf(vo, br,request,response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成md5签名数据
	 * @param data 数据
	 * @param key 密钥
	 * @return
	 */
	private static String md5(String data, String key) {
	   String sign = new StringBuilder(data.trim()).append(key).toString();
	    return MD5Util.md5(sign);
	}

}
