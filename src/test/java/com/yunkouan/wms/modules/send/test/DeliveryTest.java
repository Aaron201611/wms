package com.yunkouan.wms.modules.send.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.util.MD5Util;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ExtInterf;
import com.yunkouan.wms.modules.intf.controller.ExternalController;
import com.yunkouan.wms.modules.intf.vo.Message;
import com.yunkouan.wms.modules.send.controller.DeliveryController;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;
import com.yunkouan.wms.modules.send.service.IExpressBillService;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendExpressBillVo;

public class DeliveryTest extends BaseJunitTest{

	@Autowired
	private DeliveryController deliveryController;
	
	@Autowired
	private ExternalController externalController;
	
	@Autowired
	private IExpressBillService expressBillService;
	
	/**
	 * 新增修改测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:11:16<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testAddAndUpdate(){
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			entity.setDocType(Constant.DELIVERY_TYPE_NORMAL_OUT);
			entity.setOwner("8D8CC8F7B51B41739F4A9F6378CBABB1");
			entity.setOrderTime(new Date());
			
			List<DeliveryDetailVo> list = new ArrayList<DeliveryDetailVo>();
			DeliveryDetailVo dt1 = new DeliveryDetailVo();
			dt1.getDeliveryDetail().setSkuId("0CA94432A8224F3699E90D2E22387C0A");
			dt1.getDeliveryDetail().setBatchNo("test20170310472");
			dt1.getDeliveryDetail().setOrderQty(70d);
			
			DeliveryDetailVo dt2 = new DeliveryDetailVo();
			dt2.getDeliveryDetail().setSkuId("19C6A52D8758490BA3E69492A5E1BE10");
			dt2.getDeliveryDetail().setBatchNo("test20170310472");
			dt2.getDeliveryDetail().setOrderQty(58d);
			
			DeliveryDetailVo dt3 = new DeliveryDetailVo();
			dt3.getDeliveryDetail().setSkuId("2E991FCFFD2942518DE2FAE415099DF1");
			dt3.getDeliveryDetail().setBatchNo("test20170310472");
			dt3.getDeliveryDetail().setOrderQty(40d);
			
			list.add(dt1);
			list.add(dt2);
			list.add(dt3);
			
			sdVo.setSendDelivery(entity);
			sdVo.setDeliveryDetailVoList(list);
			BeanPropertyBindingResult br = super.validateEntity(sdVo, ValidUpdate.class);
			
//			ObjectMapper objectMapper = new ObjectMapper();
//			System.out.println(objectMapper.writeValueAsString(sdVo));
			ResultModel rm = deliveryController.addAndUpdate(sdVo, br);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询列表测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testQryList(){
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			sdVo.setOwnerName("阿里");
//			entity.setSrcNo("123");
//			entity.setDeliveryNo("234");
			entity.setDeliveryStatus(Constant.SEND_STATUS_OPEN);
			sdVo.setSendDelivery(entity);
			Page<SendDeliveryVo> page = new Page<SendDeliveryVo>();
			page.setPageNum(1);
			page.setPageSize(10);
			sdVo.setPage(page);
			ObjectMapper objectMapper = new ObjectMapper();
//			System.out.println(objectMapper.writeValueAsString(sdVo));
			ResultModel rm = deliveryController.qryPageList(sdVo);
			System.out.println(objectMapper.writeValueAsString(rm));
			super.formatResult(rm);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 生效测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testActive() throws Exception{
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			entity.setDeliveryId("3F85CFC04CD740F78EBE7957B055FF78");
			sdVo.setSendDelivery(entity);
			
			deliveryController.enable(sdVo);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 失效测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testDisable(){
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			entity.setDeliveryId("3F85CFC04CD740F78EBE7957B055FF78");
			sdVo.setSendDelivery(entity);
			deliveryController.disable(sdVo);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 取消测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testAbolish() throws Exception{
		SendDeliveryVo sdVo = new SendDeliveryVo();
		SendDelivery entity = new SendDelivery();
		
		entity.setDeliveryId("513692B6AEAC47D2BB9A916AF93C3225");
		sdVo.setSendDelivery(entity);
		deliveryController.cancel(sdVo);
	}
	
	/**
	 * 拆分测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testSplit(){
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			
			entity.setDeliveryId("3F85CFC04CD740F78EBE7957B055FF78");
			
			DeliveryDetailVo vo = new DeliveryDetailVo();
			SendDeliveryDetail detail1 = new SendDeliveryDetail();
			detail1.setDeliveryDetailId("B0275BD5C1624CBCA0A11DA9779C8F44");
			detail1.setOrderQty(10d);//18  18  10	
			detail1.setOrderWeight(0.0);
			detail1.setOrderVolume(0.0);
			vo.setDeliveryDetail(detail1);
			sdVo.getDeliveryDetailVoList().add(vo);
			
			vo = new DeliveryDetailVo();
			SendDeliveryDetail detail2 = new SendDeliveryDetail();
			detail2.setDeliveryDetailId("DE8F51792BC54FB5B3FF21CF9E2FE2A7");
			detail2.setOrderQty(20d);//35 20 20
			detail2.setOrderWeight(10.0);
			detail2.setOrderVolume(5.0);
			vo.setDeliveryDetail(detail2);
			sdVo.getDeliveryDetailVoList().add(vo);
				
//			vo = new DeliveryDetailVo();
//			SendDeliveryDetail detail3 = new SendDeliveryDetail();
//			detail3.setDeliveryDetailId("F1A082E80919406CBC7F8F647F4D70B5");
//			detail3.setOrderQty(25);//74 30 null
//			detail3.setOrderWeight(20.0);
//			detail3.setOrderVolume(0.0);
//			vo.setDeliveryDetail(detail3);
//			sdVo.getDeliveryDetailVoList().add(vo);
			
			sdVo.setSendDelivery(entity);			
			ObjectMapper objectMapper = new ObjectMapper();
			System.out.println(objectMapper.writeValueAsString(sdVo));
//			deliveryController.split(sdVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 查看测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testView(){
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			entity.setDeliveryId("3F85CFC04CD740F78EBE7957B055FF78");
			sdVo.setSendDelivery(entity);
			ResultModel rm = deliveryController.view(sdVo);
			super.toJson(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 装车确认测试
	 * @throws Exception
	 * @version 2017年2月24日 下午5:12:05<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	@Ignore
	public void testLoadConfirm(){
		try {
			SendDeliveryVo sdVo = new SendDeliveryVo();
			SendDelivery entity = new SendDelivery();
			entity.setDeliveryId("513692B6AEAC47D2BB9A916AF93C3225");
			sdVo.setSendDelivery(entity);
			deliveryController.loadConfirm(sdVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Ignore
	public void test_qryListInWave(){
		SendDeliveryVo sdVo = new SendDeliveryVo();
		sdVo.getSendDelivery().setDocType(200);
		try {
			ResultModel rm = deliveryController.qryListInWave(sdVo);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Ignore
	public void test_addExpressBill(){
		try {
			SendExpressBillVo expressBillVo = new SendExpressBillVo();
			expressBillVo.getSendExpressBill().setExpressBillNo("B2017072601");
			expressBillService.add(expressBillVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_importDelivery(){
		try {
			Message vo = new Message();
			vo.setNotify_id("2017083012121");
			vo.setNotify_type(ExtInterf.INTERFACE_SEND_ORDER.getValue());
			vo.setNotify_time(new Date().getTime()+"");
			SendDeliveryVo deliveryVo = new SendDeliveryVo();
			deliveryVo.getSendDelivery().setSrcNo("PO2017083010001");
			deliveryVo.getSendDelivery().setDocType(200);
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-26 16:16:20");
			deliveryVo.getSendDelivery().setOrderTime(d);
			deliveryVo.getSendDelivery().setContactPerson("82BE12AE461947A8B1D265F156D68F69");
			deliveryVo.getSendDelivery().setContactAddress("广东省广州市天河区19号");
			deliveryVo.getSendDelivery().setContactPhone("13710821987");
			deliveryVo.getSendDelivery().setProvince("广东");
			deliveryVo.getSendDelivery().setCity("广州");
			deliveryVo.getSendDelivery().setCounty("天河区");
			DeliveryDetailVo detailVo1 = new DeliveryDetailVo();
			detailVo1.setSkuNo("HW0001P0120170830");	
			detailVo1.getDeliveryDetail().setOrderQty(10d);
			deliveryVo.getDeliveryDetailVoList().add(detailVo1);
			String data = JsonUtil.toJson(deliveryVo);
			vo.setData(data);
			System.out.println(data);
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
			String sign = md5(vo.getData(), key);
			vo.setSign(sign);			
			BeanPropertyBindingResult br = super.validateEntity(vo, ValidSearch.class);
//			externalController.interf(vo, br,request);
		} catch (ParseException e) {
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
	
	public static void main(String[] args) {
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
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
//			ObjectMapper objectMapper=new ObjectMapper();
//			String data = objectMapper.writeValueAsString(sdVo);
//			System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
