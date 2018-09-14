/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月10日 上午10:26:47<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.controller.ASNController;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.util.valid.ValidBatchConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import junit.framework.Assert;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * ASN单测试<br/><br/>
 * <b>创建日期</b>:<br/>
 * 2017年2月10日 上午10:26:47<br/>
 * @author andy wan<br/>
 */
@SuppressWarnings("deprecation")
public class AsnTest extends BaseJunitTest {

	@Autowired
	protected ASNController controller;
	
    private MockMvc mockMvc; 
    

    @Autowired
    protected IASNExtlService asnExtService;
    @Autowired
    protected IASNDetailExtlService asnDetailExtService;
    
	@Before
	public void init () {
//		this.mockMvc = MockMvcBuilders
//	     .standaloneSetup(asnController)
//	     .setControllerAdvice(this.gec)
//	     .build();
		
//		final StaticApplicationContext applicationContext = new StaticApplicationContext();
//	    applicationContext.registerSingleton("exceptionHandler", GlobalExceptionController.class);
//
//	    final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();
//	    webMvcConfigurationSupport.setApplicationContext(applicationContext);
//
//	    mockMvc = MockMvcBuilders.standaloneSetup(asnController).
//	        setHandlerExceptionResolvers(webMvcConfigurationSupport.handlerExceptionResolver()).
//	        build();
	}
	
	/**
	 * 测试类
	 * @version 2017年2月14日 下午5:18:14<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	@Test(expected = Exception.class)
	public void test() throws Exception {
		try {
//			this.testListASN();
//			this.testViewASN();
//			this.testInsertASN();
//			this.testUpdateASN();
//			this.testActiveASN();
//			this.testInactiveASN();
//			this.testCancelASN();
//			this.testTearASN();
//			this.testUntearASN();
//			this.testConfirmASN();
//			this.testBatchConfirmASN();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 测试 - 批量收货确认
	 * @throws Exception
	 * @version 2017年2月18日 下午2:16:29<br/>
	 * @author andy wang<br/>
	 * 
	 	--验证数据
	 	SELECT * FROM rec_asn WHERE asn_id IN ('EF3B5C58E5444438B0E02B61FC108FAA','C108397CC9E14F57A2C6EFA3BED72656','F3913535F5F6400D92BC4F931CB2FD96');
		SELECT asn_detail_id,receive_qty,receive_weight,receive_volume,order_qty,order_weight,order_volume,asn_id 
		FROM rec_asn_detail WHERE asn_id IN ('EF3B5C58E5444438B0E02B61FC108FAA','C108397CC9E14F57A2C6EFA3BED72656','F3913535F5F6400D92BC4F931CB2FD96') ORDER BY asn_id;
		
		--恢复数据
		UPDATE rec_asn SET asn_status = 20,receive_qty = NULL ,receive_weight = NULL ,receive_volume = NULL 
		WHERE asn_id IN ('EF3B5C58E5444438B0E02B61FC108FAA','C108397CC9E14F57A2C6EFA3BED72656','F3913535F5F6400D92BC4F931CB2FD96');
		UPDATE rec_asn_detail SET receive_qty = NULL ,receive_weight = NULL ,receive_volume = NULL  
		WHERE asn_id IN ('EF3B5C58E5444438B0E02B61FC108FAA','C108397CC9E14F57A2C6EFA3BED72656','F3913535F5F6400D92BC4F931CB2FD96');
	 */
	private void testBatchConfirmASN () throws Exception {
		RecAsnVO recAsnVO = new RecAsnVO();
		List<String> list = new ArrayList<String>();
		list.add("EF3B5C58E5444438B0E02B61FC108FAA");
		list.add("C108397CC9E14F57A2C6EFA3BED72656");
		list.add("F3913535F5F6400D92BC4F931CB2FD96");
		recAsnVO.setListAsnId(list);
		recAsnVO.setLocationId("100");
		recAsnVO.getAsn().setOpPerson("3");
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidBatchConfirm.class);
		ResultModel rm = this.controller.batch(recAsnVO , br);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 测试 - 收货确认
	 * @throws Exception
	 * @version 2017年2月18日 下午1:13:48<br/>
	 * @author andy wang<br/>
	 * 
	   	--验证数据
		SELECT * FROM rec_asn WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5';
		SELECT asn_detail_id,receive_qty,receive_weight,receive_volume,order_qty,order_weight,order_volume,asn_id 
		FROM rec_asn_detail WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5' OR asn_detail_id = '3F217DC9A30E46168A905CC5A1B97EB4' ;
		
		SELECT a.stock_qty=b.receive_qty '数量' ,  a.stock_weight=b.receive_weight '重量' , a.stock_volume=b.receive_volume '体积' 
		, a.measure_unit=b.measure_unit '包装单位' , a.location_id=b.location_id '库位' , a.sku_id=b.sku_id '货品' 
		FROM inv_stock a INNER JOIN rec_asn_detail b ON a.asn_detail_id=b.asn_detail_id WHERE a.asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5' ;
		
		SELECT * FROM inv_stock WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5' ;
		
		--恢复数据
		UPDATE rec_asn SET asn_status = 20,receive_qty = NULL ,receive_weight = NULL ,receive_volume = NULL WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5';
		UPDATE rec_asn_detail SET receive_qty = NULL ,receive_weight = NULL ,receive_volume = NULL  
		WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5' OR asn_detail_id = '3F217DC9A30E46168A905CC5A1B97EB4' ;
		DELETE FROM inv_stock WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5'  
	 */
	public void testConfirmASN () throws Exception {
		RecAsnVO recAsnVO = this.getConfirmVO2();
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidConfirm.class);
		ResultModel rm = this.controller.complete(recAsnVO , br);
		super.formatResult(rm);
	}
	

	private RecAsnVO getConfirmVO2() throws Exception {
		String asnId = "6D13E152453642EA915443842446DF51";
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		recAsnVO.getAsn().setOpPerson("3");
		
		List<RecAsnDetail> listAsnDetailByAsnId = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		recAsnVO.setListSaveAsnDetail(listAsnDetailByAsnId);
		for (int i = 0; i < listAsnDetailByAsnId.size(); i++) {
			RecAsnDetail recAsnDetail = listAsnDetailByAsnId.get(i);
			recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty());
			recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight());
			recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume());
			recAsnDetail.setLocationId("1");
		}
		return recAsnVO;
	}
	
	private RecAsnVO getConfirmVO1() {
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId("EF3B5C58E5444438B0E02B61FC108FAA");
		recAsnVO.getAsn().setOpPerson("3");
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		recAsnVO.setListSaveAsnDetail(list);
		
		// 收货数量相同
		RecAsnDetail detail = new RecAsnDetail();
		list.add(detail);
		detail.setAsnDetailId("06FB3CA86FBA45DE9752F01E54D0F6C2");
		detail.setReceiveQty(52d);
		detail.setReceiveWeight(77d);
		detail.setReceiveVolume(86d);
		detail.setLocationId("1");
		
		// 大于收货数量
		detail = new RecAsnDetail();
		list.add(detail);
		detail.setAsnDetailId("07735F81F120498785ACA7C0C8FA4C10");
		detail.setReceiveQty(39d);
		detail.setReceiveWeight(50d);
		detail.setReceiveVolume(38d);
		detail.setLocationId("2");

		// 小于收货数量，部分收货
		detail = new RecAsnDetail();
		list.add(detail);
		detail.setAsnDetailId("4E2902D046764A81B247DCC7908C0BA3");
		detail.setReceiveQty(75d);
		detail.setReceiveWeight(90d);
		detail.setReceiveVolume(36d);
		detail.setLocationId("3");

		// 收货数量一致，重量/体积缺失
		detail = new RecAsnDetail();
		list.add(detail);
		detail.setAsnDetailId("5F8731F47E774789890E97DAE0FBDF7C");
		detail.setReceiveQty(1d);
		detail.setLocationId("4");

		// 收货数量相同
		detail = new RecAsnDetail();
		list.add(detail);
		detail.setAsnDetailId("7B9E65D3C05543868103759E0627C77F");
		detail.setReceiveQty(16d);
		detail.setReceiveWeight(65d);
		detail.setReceiveVolume(72d);
		detail.setLocationId("5");

		// 异常 - 收货数量/重量/体积不能为负数
//		detail = new RecAsnDetail();
//		list.add(detail);
//		detail.setAsnDetailId("9630874340044FBAB7171EDD9C83F8E0");
//		detail.setReceiveQty(-42);
//		detail.setReceiveWeight(-31d);
//		detail.setReceiveWeight(-55d);
		
		// 不操作 - 其他Asn单明细，与提交的asn单不是同一张单，系统不操作
		detail = new RecAsnDetail();
		list.add(detail);
		detail.setAsnDetailId("3F217DC9A30E46168A905CC5A1B97EB4");
		detail.setReceiveQty(52d);
		detail.setReceiveWeight(77d);
		detail.setReceiveVolume(86d);
		detail.setLocationId("1");

		// 异常 - 数量超越最大/明细Id为null
//		detail = new RecAsnDetail();
//		list.add(detail);
//		detail.setAsnDetailId(null);
//		detail.setReceiveQty(201);
//		detail.setReceiveWeight(11d);
//		detail.setReceiveWeight(20d);
		return recAsnVO;
	}
	
	/**
	 * 测试 - 取消拆分Asn单
	 * @throws Exception
	 * @version 2017年2月16日 下午3:43:36<br/>
	 * @author andy wang<br/>
	 */
	private void testUntearASN() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("49A2BE6ABFDD4784A0669E281CFB4329");
		ResultModel rm = this.controller.unsplit(listAsnId);
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 拆分Asn单
	 * @throws Exception
	 * @version 2017年2月16日 下午3:42:50<br/>
	 * @author andy wang<br/>
	 * 
	 *	数据验证SQL
	    SELECT asn_id , parent_asn_detail_id , order_qty , order_weight , order_volume , asn_detail_id FROM rec_asn_detail WHERE asn_id IN (SELECT asn_id FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD') ORDER BY parent_asn_detail_id;
		SELECT asn_detail_id, order_qty , order_weight , order_volume FROM rec_asn_detail WHERE asn_id = '71EC9CEE5F854A3E9B5FE491714354FD'
	    SELECT * FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD' OR asn_id='71EC9CEE5F854A3E9B5FE491714354FD'
	    
	  	恢复SQL
	 	DELETE FROM rec_asn_detail WHERE asn_id IN (SELECT asn_id FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD');
		DELETE FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD';
		UPDATE rec_asn SET asn_status='10' WHERE asn_id='71EC9CEE5F854A3E9B5FE491714354FD';
	 */
	private void testTearASN() throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn asn = asnVO.getAsn();
		asn.setAsnId("2F8374EA4EEC4C99A6FDFA154A3B4B53");
		List<RecAsnDetail> listRecAsnDetail = new ArrayList<RecAsnDetail>();
		

		// 数量1,重量12，体积45，扣减方式计算重量/体积
		RecAsnDetail recAsnDetail = new RecAsnDetail();
		recAsnDetail.setAsnDetailId("30B7940242A24CAB863040B13D78619D");
		recAsnDetail.setOrderQty(1d);
		recAsnDetail.setOrderWeight(12d);
		recAsnDetail.setOrderVolume(8d);
		listRecAsnDetail.add(recAsnDetail);
		
		// 数量相同
		recAsnDetail = new RecAsnDetail();
		recAsnDetail.setAsnDetailId("49994B7A92D44BBD93FE65681670CF15");
		recAsnDetail.setOrderQty(80d);
		listRecAsnDetail.add(recAsnDetail);
		
		// 少于数量
		recAsnDetail = new RecAsnDetail();
		recAsnDetail.setAsnDetailId("56A5184F2F934F2B83B303023171F451");
		recAsnDetail.setOrderQty(2d);
		listRecAsnDetail.add(recAsnDetail);
		
		// 数量为0
		recAsnDetail = new RecAsnDetail();
		recAsnDetail.setAsnDetailId("A69E02DF9D3543BBB60B089251A5407B");
		recAsnDetail.setOrderQty(0d);
		listRecAsnDetail.add(recAsnDetail);
		
		// 数量32
		recAsnDetail = new RecAsnDetail();
		recAsnDetail.setAsnDetailId("C5EF28FA958D4DFEBCF8E9C2EA446ED6");
		recAsnDetail.setOrderQty(1d);
		listRecAsnDetail.add(recAsnDetail);
		
//		// 数量一半,体积占1，扣减方式计算重量/体积
//		recAsnDetail = new RecAsnDetail();
//		recAsnDetail.setAsnDetailId("B3D36FB4205D4D5B8E81841889C31E8E");
//		recAsnDetail.setOrderQty(30);
//		recAsnDetail.setOrderVolume(1d);
//		listRecAsnDetail.add(recAsnDetail);
//		
//		// 数量1,重量全占，扣减方式计算重量/体积
//		recAsnDetail = new RecAsnDetail();
//		recAsnDetail.setAsnDetailId("B84CD2307F554F2E9D887A68906128B7");
//		recAsnDetail.setOrderQty(1);
//		recAsnDetail.setOrderWeight(70d);
//		listRecAsnDetail.add(recAsnDetail);
		
		// 异常 - 负数
//		recAsnDetail = new RecAsnDetail();
//		recAsnDetail.setAsnDetailId("CEF99C1CEB594B68B66AA38E59B12097");
//		recAsnDetail.setOrderQty(-1);
//		listRecAsnDetail.add(recAsnDetail);
		
		// 异常 - 数量为0 ， 但有重量
//		recAsnDetail = new RecAsnDetail();
//		recAsnDetail.setAsnDetailId("E322B4191EC34C6C8B0D28E724AEEF8F");
//		recAsnDetail.setOrderQty(0);
//		recAsnDetail.setOrderWeight(2d);
//		listRecAsnDetail.add(recAsnDetail);
		
		// 异常 - 数量/重量/体积超标
//		recAsnDetail = new RecAsnDetail();
//		recAsnDetail.setAsnDetailId("EFEB0053D7FE49EC80E62D623D328C1A");
//		recAsnDetail.setOrderQty(51);
//		recAsnDetail.setOrderWeight(1000d);
//		recAsnDetail.setOrderVolume(1000d);
//		listRecAsnDetail.add(recAsnDetail);
		
		asnVO.setListSaveAsnDetail(listRecAsnDetail);

		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = this.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 测试 - ASN单取消
	 * @throws Exception
	 * @version 2017年2月15日 下午2:06:44<br/>
	 * @author andy wang<br/>
	 */
	private void testCancelASN() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("9CE43F14B0F9439A9D0F9AD25A814D08");
//		listAsnId.add("1");
		listAsnId.add("E732472511124F9C92B131B388AE7657");
		listAsnId.add("F0FE2CFBA38B46A1B934A181224BFCB0");
//		listAsnId.add("10018");
//		listAsnId.add("10017");
		ResultModel rm = this.controller.cancel(listAsnId);
		super.formatResult(rm);
	}

	/**
	 * 测试 - ASN单失效
	 * @throws Exception
	 * @version 2017年2月15日 下午2:06:23<br/>
	 * @author andy wang<br/>
	 */
	private void testInactiveASN() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("9CE43F14B0F9439A9D0F9AD25A814D08");
//		listAsnId.add("1");
		listAsnId.add("71EC9CEE5F854A3E9B5FE491714354FD");
		listAsnId.add("F0FE2CFBA38B46A1B934A181224BFCB0");
//		listAsnId.add("10018");
//		listAsnId.add("10017");
		ResultModel rm = this.controller.disable(listAsnId);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试 - ASN单生效
	 * @throws Exception
	 * @version 2017年2月15日 下午1:41:10<br/>
	 * @author andy wang<br/>
	 */
	private void testActiveASN() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("6D13E152453642EA915443842446DF51");
//		listAsnId.add("1");
//		listAsnId.add("71EC9CEE5F854A3E9B5FE491714354FD");
//		listAsnId.add("F0FE2CFBA38B46A1B934A181224BFCB0");
//		listAsnId.add("10018");
//		listAsnId.add("10017");
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.setTsTaskVo(new TsTaskVo());
		recAsnVO.getTsTaskVo().getTsTask().setOpPerson("abc");
		recAsnVO.setListAsnId(listAsnId);
		ResultModel rm = this.controller.enable(recAsnVO);
		super.formatResult(rm);
//			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/asn/enable"))  
//			.andExpect(MockMvcResultMatchers.view().name("user/view"))  
//			.andExpect(MockMvcResultMatchers.model().attributeExists("user"))  
//			.andDo(MockMvcResultHandlers.print())  
//			.andReturn();  
//			System.out.println(result);
	}
	
	/**
	 * 测试 - ASN单列表查询
	 * @throws Exception
	 * @version 2017年2月11日 下午10:20:07<br/>
	 * @author andy wang<br/>
	 */
	private void testListASN() throws Exception {
		try {
			// 分页查询
			
			
			
			
			RecAsnVO recAsnVO = new RecAsnVO();
			recAsnVO.setOwnerComment("巴巴");
			recAsnVO.getAsn().setPoNo("testPO1234123");
			recAsnVO.getAsn().setAsnNo("REC03810201702090002");
			recAsnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_OPEN);
			recAsnVO.getAsn().setOrderDate(DateUtil.createDate(2017, 2, 9));
			recAsnVO.getAsn().setDataFrom(Constant.ASN_DATAFROM_IMPORT);
			recAsnVO.setCurrentPage(0);
			ResultModel rm = this.controller.list(recAsnVO);
			String col = "货主\t仓库\tASN单号\t父编码\t订单类型\t\t单据来源\t\tPO单号\t订单日期\t"
					+ "\t订单数量/公斤/立方\t收货数量/公斤/立方\t上架数量/公斤/立方\t状态\t创建人\t最后修改人\n";
			col += "\t\t\t\t\t\t相关单号1\t\t\t\t\t\t\t\t\t\t\t\t\t\t创建时间\t修改时间";
			System.out.println(col);
			super.formatResult(rm);
			List<RecAsnVO> list = rm.getList();
			if ( !PubUtil.isEmpty(list) ) {
				RecAsnVO vo = list.get(0);
				System.out.println("阿里巴巴".equals(vo.getOwnerComment()));
				System.out.println("testPO1234123".equals(vo.getAsn().getPoNo()));
				System.out.println("REC03810201702090002".equals(vo.getAsn().getAsnNo()));
				System.out.println(vo.getAsn().getAsnStatus() == Constant.ASN_STATUS_OPEN);
				System.out.println("2017-02-09".equals(DateUtil.formatDate(vo.getAsn().getOrderDate(),null)) );
				System.out.println(Constant.ASN_DATAFROM_IMPORT == vo.getAsn().getDataFrom().intValue() );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试 - 查看ASN单详情
	 * 
	 	-- 查询没有ASN单明细的ASN单
	 	SELECT * FROM rec_asn a WHERE NOT EXISTS (SELECT 1 FROM rec_asn_detail b WHERE a.asn_id=b.asn_id )
		
		-- 查询有ASN单明细的ASN单
	 	SELECT * FROM rec_asn a WHERE EXISTS (SELECT 1 FROM rec_asn_detail b WHERE a.asn_id=b.asn_id )
	   	
	   	-- 查询有ASN单明细并且有上架单操作明细的ASN单
		SELECT * FROM rec_asn a WHERE EXISTS (SELECT 1 FROM rec_putaway_detail b WHERE a.asn_id = b.asn_id 
		AND EXISTS ( SELECT 1 FROM rec_putaway_location c WHERE b.putaway_detail_id = c.putaway_detail_id));

	   	
	 * @throws Exception
	 * @version 2017年2月11日 下午10:20:22<br/>
	 * @author andy wang<br/>
	 */
	private void testViewASN() throws Exception {
		try {
			ResultModel rm = null;
//			System.out.println("--- 开始 测试 ASN单不存在 -----------------------------------------------------------------");
//			
//			rm = this.controller.viewASN("9394348");
//			super.formatResult(rm);
//			System.out.println("结果：" + (rm.getObj() == null) );
//			System.out.println("--- 结束 测试 ASN单不存在 -----------------------------------------------------------------");
//
//			System.out.println("\n\n\n");
//			
//			System.out.println("--- 开始 测试 没有ASN单明细 -----------------------------------------------------------------");
//			rm = this.controller.viewASN("10002");
//			super.formatResult(rm);
//			System.out.println("--- 结束 测试 没有ASN单明细 -----------------------------------------------------------------");
			
			System.out.println("\n\n\n");

			System.out.println("--- 开始 测试 正常数据 -----------------------------------------------------------------");
			rm = this.controller.view("F22F7D0E90FB4AB2BD2EA963441D3BE5");
			super.formatResult(rm);
			System.out.println("--- 结束 测试 正常数据 -----------------------------------------------------------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试 - 保存ASN单
	 * 
	 	SELECT * FROM rec_asn ORDER BY asn_id2 DESC 

		SELECT * FROM rec_asn_detail WHERE asn_id = '16E91DF74B844756BC4750EFA63155A2'
		
		SELECT SUM(order_qty),SUM(order_weight),SUM(order_volume) FROM rec_asn_detail WHERE asn_id = '16E91DF74B844756BC4750EFA63155A2'
	 	
	 * @throws Exception
	 * @version 2017年2月14日 下午2:55:49<br/>
	 * @author andy wang<br/>
	 */
	private void testInsertASN() throws Exception {
		try {
			RecAsnVO asnVO = new RecAsnVO();
			RecAsn recAsn = new RecAsn();
			asnVO.setAsn(recAsn);
			List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
			asnVO.setListSaveAsnDetail(list);
			Random r = new Random();
			recAsn.setContactAddress("广东省广州市天河区华明路" + r.nextInt(1000) + "号");
			recAsn.setContactPerson(super.getChineseName());
			recAsn.setContactPhone(String.format("131%08d", r.nextInt(10000000)));
			recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
			recAsn.setDocType(Constant.ASN_DOCTYPE_RETURNED);
			recAsn.setOrderDate(new Date());
			recAsn.setOwner("2");
			recAsn.setPoNo(r.nextInt(10000000)+"");
			recAsn.setSender("1");

			for (int i = 0; i < 2; i++) {
				RecAsnDetail detail = new RecAsnDetail();
				detail.setBatchNo("2122017021800"+i);
				detail.setExpiredDate(new Date(2019, 2, 12));
				detail.setMeasureUnit("包");
				detail.setOrderQty(new Double(Math.ceil(Math.random() * 100)));
				detail.setOrderVolume(Math.ceil(Math.random() * 100));
				detail.setOrderWeight(Math.ceil(Math.random() * 100));
				detail.setPackId("1");
				detail.setProduceDate(new Date());
				detail.setSkuId(r.nextInt(5) + "");
				list.add(detail);
			}
			BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSave.class);
			ResultModel rm = this.controller.add(asnVO,br);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取随机批次号
	 * @return
	 * @version 2017年2月19日 下午8:35:59<br/>
	 * @author andy wang<br/>
	 */
	protected String randomBatchNo () {
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String batchNo = String.format("test%s%03d",sdf.format(new Date()),random.nextInt(999));
		return batchNo;
	}
	
	/**
	 * 显示分割线
	 * @param msg 分割描述
	 * @version 2017年3月18日 下午8:20:13<br/>
	 * @author andy wang<br/>
	 */
	protected void line ( String msg ) {
		System.out.println("-------------------" + msg + "------------------------");
	}
	
	/**
	 * 填充对应长度的字符串
	 * @param s 填充字符串
	 * @param len 填充的长度
	 * @return 填充后的字符串
	 * @version 2017年3月19日 下午12:54:55<br/>
	 * @author andy wang<br/>
	 */
	protected String strLen ( String s , int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(s);
		}
		return sb.toString();
		
	}
}
