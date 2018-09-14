/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月10日 上午10:26:47<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.test;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.yunkouan.base.GlobalExceptionController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.MD5Util;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ExtInterf;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.intf.controller.ExternalController;
import com.yunkouan.wms.modules.intf.vo.Message;
import com.yunkouan.wms.modules.inv.controller.AdjustController;
import com.yunkouan.wms.modules.inv.controller.CountController;
import com.yunkouan.wms.modules.inv.controller.ShiftController;
import com.yunkouan.wms.modules.inv.controller.StockController;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.inv.entity.InvAdjustDetail;
import com.yunkouan.wms.modules.inv.entity.InvCount;
import com.yunkouan.wms.modules.inv.entity.InvCountDetail;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvShift;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvAdjustDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;
import com.yunkouan.wms.modules.inv.vo.InvCountDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.inv.vo.InvWarnVO;
import com.yunkouan.wms.modules.monitor.controller.ExceptionLogController;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.monitor.vo.ExceptionLogVO;

/**
 * 库存相关测试<br/><br/>
 * <b>创建日期</b>:<br/>
 * 2017年2月17日18:28:48<br/>
 * @author 王通<br/>
 */
public class InvTest extends BaseJunitTest {


	@Autowired
	public CountController countController;
	@Autowired
	public ExceptionLogController expController;
	@Autowired
	public IExceptionLogService expService;
	@Autowired
	public ShiftController shiftController;
	@Autowired
	public StockController stockController;
	@Autowired
	public ExternalController externalController;
	@Autowired
	public IStockService stockService;
	@Autowired
	public IShiftService shiftService;
	@Autowired
	public AdjustController adjustController;
	
	
    public MockMvc mockMvc; 
    
    @Autowired
    public GlobalExceptionController gec;

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
	 * @version 2017年2月17日18:29:42<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Test(expected = Exception.class)
	public void test() throws Exception {
		try {
			this.testExternalInterf();
//			this.testShiftList();
//			this.testShiftView();
//			this.testShiftSaveOrUpdate();
//			this.testShiftEnable();
//			this.testShiftDisable();
//			this.testShiftPrint();
//			this.testShiftConfirm();
//			this.testShiftQuickConfirm();
//			this.testShiftgetInLocationOccupy();
//			this.testStockLockOutStock();
//			this.testStockUnLockOutStock();
//			this.testStockInStock();
//			this.testStockOutStock();
//			this.testStockChangeStock();
//			this.testStockList();
//			this.testStockLogList();
//			this.testStockFreeze();
//			this.testStockUnfreeze();
//			this.testStockShift();
//			this.testStockImport();
//			this.testStockOutLockDetail();
//			this.testStockStockSkuCount();
//			this.testRefreshOutLock();
//			this.testAdjustList();
//			this.testAdjustView();
//			this.testAdjustAdd();
//			this.testAdjustUpdate();
//			this.testAdjustEnable();
//			this.testAdjustCancel();
//			this.testExceptionAdd();
//			this.testExceptionUpdate();
//			this.testExceptionList();
//			this.testExceptionCancel();
//			this.testExceptionView();
//			this.testWarnList();
//			this.testCountAdd();
//			this.testCountUpdate();
//			this.testCountView();
//			this.testCountList();
//			this.testCountEnable();
//			this.testCountDisable(); 
//			this.testCountConfirm();
//			this.testCountQuickConfirm();
//			this.testCountCancel();
//			this.testCountPrint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月31日 下午8:13:26<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	private void testExternalInterf() throws Exception {
		Message countVo = new Message();
		List<String> list = new ArrayList<String>();
		list.add("111111BARCODE001");
		list.add("1111110101010101");
		countVo.setNotify_type(ExtInterf.INTERFACE_QUERY_STOCK.getValue());
		countVo.setData(JSONArray.toJSONString(list));
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String key = paramService.getKey(CacheName.PARAM_EXT_KEY);
		String md5 = md5(countVo.getData(), key);
		countVo.setSign(md5);
		formatResult(countVo);
		System.out.println("--------我是分割线--------");
		BeanPropertyBindingResult br = super.validateEntity(countVo, ValidSave.class);
//		ResultModel rm = this.externalController.interf(countVo, br, super.request);
//		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月15日 下午4:35:15<br/>
	 * @author 王通<br/>
	 */
	private void testShiftgetInLocationOccupy() throws Exception{
		List<InvShiftDetail> retlist = this.shiftService.getInLocationOccupy("2", 20, 30);
		formatResult(retlist);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月16日 下午2:15:21<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountPrint() throws Exception {
//		List<String> shiftIdList = new ArrayList<String>();
//		shiftIdList.add("2818AE08E8DA4BA8ABED8E7AFF40A4F8");
		String shiftId = "CAE5BADC69D344B2912F820D0A8E42B5";
		ResultModel rm = this.countController.print(shiftId);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月16日 上午11:53:59<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountCancel() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("2818AE08E8DA4BA8ABED8E7AFF40A4F8");
		ResultModel rm = this.countController.cancel(shiftIdList);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 下午4:38:41<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountConfirm() throws Exception {
//		Random r = new Random();
		InvCountVO countVo = new InvCountVO();
		List<InvCountDetailVO> list = new ArrayList<InvCountDetailVO>();
		InvCount count = new InvCount();
		countVo.setInvCount(count);
		countVo.setListInvCountDetailVO(list);
		count.setCountId("CAE5BADC69D344B2912F820D0A8E42B5");
		InvCountDetailVO InvCountDetailVO1 = new InvCountDetailVO();
		InvCountDetail countDetail1 = new InvCountDetail();
		InvCountDetailVO1.setInvCountDetail(countDetail1);
		countDetail1.setCountDetailId("450C309EA9614D118B34DC9429EBC8AE");
		countDetail1.setStockQty(1000d);
		countDetail1.setRealCountQty(1111d);
		list.add(InvCountDetailVO1);
		int nowRow = 4;
		for (int i =1; i < nowRow; i++) {
			InvCountDetailVO InvCountDetailVO = new InvCountDetailVO();
			InvCountDetail countDetail = new InvCountDetail();
			InvCountDetailVO.setInvCountDetail(countDetail);
			countDetail.setLocationId("1");
			countDetail.setSkuId(i + "");
			countDetail.setStockQty(1000d);
			countDetail.setRealCountQty(1111d);
			list.add(InvCountDetailVO);
		}
		ResultModel rm = this.countController.confirm(countVo);
		formatResult(rm);
	}
	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 下午4:38:41<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountQuickConfirm() throws Exception {
//		Random r = new Random();
		InvCountVO countVo = new InvCountVO();
		List<InvCountDetailVO> list = new ArrayList<InvCountDetailVO>();
//		InvCount count = new InvCount();
//		countVo.setInvCount(count);
		countVo.setListInvCountDetailVO(list);
//		count.setCountId("CAE5BADC69D344B2912F820D0A8E42B5");
//		InvCountDetailVO InvCountDetailVO1 = new InvCountDetailVO();
//		InvCountDetail countDetail1 = new InvCountDetail();
//		InvCountDetailVO1.setInvCountDetail(countDetail1);
//		countDetail1.setCountDetailId("450C309EA9614D118B34DC9429EBC8AE");
//		countDetail1.setStockQty(1000);
//		countDetail1.setRealCountQty(1111);
//		list.add(InvCountDetailVO1);
		System.out.println("1111111");
		int nowRow = 3;
		for (int i =0; i < nowRow; i++) {
			System.out.println("2222222");
			InvCountDetailVO InvCountDetailVO = new InvCountDetailVO();
			InvCountDetail countDetail = new InvCountDetail();
			InvCountDetailVO.setInvCountDetail(countDetail);
//			countDetail.setLocationId("1");
//			countDetail.setSkuId(i + "");
//			countDetail.setStockQty(1000);
			InvCountDetailVO.setLocationNo("C101");
			InvCountDetailVO.setSkuBarCode("binggo");
			countDetail.setBatchNo("2016101" + (i));
			countDetail.setRealCountQty(100d);
			list.add(InvCountDetailVO);
		}
		ResultModel rm = this.countController.quickConfirm(countVo);
		formatResult(rm);
	}
	/**
	 * 失效
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 下午4:01:08<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountDisable() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("2818AE08E8DA4BA8ABED8E7AFF40A4F8");
		InvCountVO vo = new InvCountVO();
		vo.setCountIdList(shiftIdList);
		ResultModel rm = this.countController.disable(vo);
		formatResult(rm);
	}

	/**
	 * 生效
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 下午2:12:48<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountEnable() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("CAE5BADC69D344B2912F820D0A8E42B5");
		InvCountVO vo = new InvCountVO();
		vo.setCountIdList(shiftIdList);
		ResultModel rm = this.countController.enable(vo);
		formatResult(rm);
	}

	/**
	 * 测试盘点单列表
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 下午1:11:52<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountList() throws Exception {
		InvCountVO countVo = null;
		countVo = new InvCountVO();
		ResultModel rm =  this.countController.list(countVo);
		formatResult(rm);
	}

	/**
	 * 测试查看盘点单
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 下午1:11:50<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountView() throws Exception {
		String shiftId = "CAE5BADC69D344B2912F820D0A8E42B5";
		ResultModel rm = this.countController.view(shiftId);
		formatResult(rm);
	}

	/**
	 * 测试修改盘点单
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月15日 上午11:40:16<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountUpdate() throws Exception {
		Random r = new Random();
		InvCountVO countVo = new InvCountVO();
		List<InvCountDetailVO> list = new ArrayList<InvCountDetailVO>();
		InvCount count = new InvCount();
		countVo.setInvCount(count);
		countVo.setListInvCountDetailVO(list);
		count.setCountId("CAE5BADC69D344B2912F820D0A8E42B5");
		count.setCountType(Constant.COUNT_TYPE_SKU);
		count.setIsBlockLocation(Constant.COUNT_NO_FREEZE);
		int nowRow = 2 + r.nextInt(8);
		for (int i =0; i < nowRow; i++) {
			InvCountDetailVO InvCountDetailVO = new InvCountDetailVO();
			InvCountDetail countDetail = new InvCountDetail();
			InvCountDetailVO.setInvCountDetail(countDetail);
			countDetail.setLocationId("1");
			countDetail.setSkuId("0");
			countDetail.setStockQty(1000d);
			list.add(InvCountDetailVO);
		}
		BeanPropertyBindingResult br = super.validateEntity(countVo, ValidSave.class);
		ResultModel rm = this.countController.update(countVo, br);
		formatResult(rm);
	}

	/**
	 * 测试新增盘点
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午7:22:57<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testCountAdd() throws Exception {
		Random r = new Random();
		InvCountVO countVo = new InvCountVO();
		List<InvCountDetailVO> list = new ArrayList<InvCountDetailVO>();
		InvCount count = new InvCount();
		countVo.setInvCount(count);
		countVo.setListInvCountDetailVO(list);
		count.setCountType(Constant.COUNT_TYPE_SKU);
		count.setIsBlockLocation(Constant.COUNT_NO_FREEZE);
		int nowRow = 2 + r.nextInt(8);
		for (int i =0; i < nowRow; i++) {
			InvCountDetailVO InvCountDetailVO = new InvCountDetailVO();
			InvCountDetail countDetail = new InvCountDetail();
			InvCountDetailVO.setInvCountDetail(countDetail);
			countDetail.setLocationId("1");
			countDetail.setSkuId("0");
			countDetail.setStockQty(1000d);
			list.add(InvCountDetailVO);
		}
		BeanPropertyBindingResult br = super.validateEntity(countVo, ValidSave.class);
		ResultModel rm = this.countController.add(countVo, br);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午2:00:58<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testExceptionView() throws Exception {
		String shiftId = "4815885DA3B247E09E17AF645EF0F7F8";
		ResultModel rm = this.expController.view(shiftId);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:47:42<br/>
	 * @author 王通<br/>
	 */
	public void testExceptionList() {
		ExceptionLogVO adjVo = new ExceptionLogVO();
//		InvAdjust adj = new InvAdjust();
//		adjVo.setInvAdjust(adj);
//		adj.setAdjustNo("");
//		adjVo.setCountNo("");
		ResultModel rm =  this.expController.list(adjVo);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:08:15<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testExceptionCancel() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("4815885DA3B247E09E17AF645EF0F7F8");
		ResultModel rm = this.expController.cancel(shiftIdList);
		formatResult(rm);
	}

	/**
	 * 测试库存告警列表
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午6:39:45<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testWarnList() throws Exception {
		InvWarnVO vo = new InvWarnVO();
//		vo.setSkuNo("3D473F216727433AB46FD60BE7");
		vo.setOwner("中云");
		ResultModel rm = this.stockController.warnList(vo);
		formatResult(rm);
	}

	/**
	 * 测试异常修改
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:56:04<br/>
	 * @author 王通<br/>
	 */
	public void testExceptionUpdate() {
		Random r = new Random();
		int num = r.nextInt(10) + 1;
		ExceptionLog exp = new ExceptionLog();
		exp.setExLogId("4815885DA3B247E09E17AF645EF0F7F8");
		exp.setExDesc("庫存数量异常，增加了" + num + "个！");
		exp.setExLevel(Constant.EXCEPTION_LEVEL_NORMAL);
		exp.setExType(Constant.EXCEPTION_TYPE_COUNT_ABNORMAL);
		exp.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
		exp.setInvolveBill(num + "000" + num);
		exp.setHandleMsg("面壁思过→   ( ￣ ￣)σ…( ＿ ＿)ノ｜壁");
//		BeanPropertyBindingResult br = super.validateEntity(exp, ValidSave.class);
		try {
			this.expService.update(exp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试增加异常
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月28日 下午5:30:38<br/>
	 * @author 王通<br/>
	 */
	public void testExceptionAdd() {
		Random r = new Random();
		int num = r.nextInt(10) + 1;
		ExceptionLog exp = new ExceptionLog();
		exp.setExDesc("庫存数量异常，增加了" + num + "个！");
		exp.setExLevel(Constant.EXCEPTION_LEVEL_NORMAL);
		exp.setExType(Constant.EXCEPTION_TYPE_COUNT_ABNORMAL);
		exp.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
		exp.setInvolveBill(num + "000" + num);
//		BeanPropertyBindingResult br = super.validateEntity(exp, ValidSave.class);
		try {
			this.expService.add(exp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 上午11:06:06<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testAdjustCancel() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("A421554506034FAA9F98D39F008B8002");
		ResultModel rm = this.adjustController.cancel(shiftIdList);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 上午10:41:11<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testAdjustEnable() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("90C98D753A88492FAC70F18EFC73BD28");
		ResultModel rm = this.adjustController.enable(shiftIdList);
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 上午9:48:21<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testAdjustView() throws Exception {
		ResultModel rm =  this.adjustController.view("A421554506034FAA9F98D39F008B8002");
		formatResult(rm);
	}

	/**
	 * 
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月12日 下午5:38:50<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testAdjustUpdate() throws Exception {
		Random r = new Random();
		InvAdjustVO adjustVo = new InvAdjustVO();
		List<InvAdjustDetailVO> list = new ArrayList<InvAdjustDetailVO>();
		InvAdjust adj = new InvAdjust();
		adj.setAdjustId("A421554506034FAA9F98D39F008B8002");
		adj.setCountId("countIdmod");
		adj.setAdjustStatus(Constant.STATUS_OPEN);
		adj.setDataFrom(Constant.ADJUST_DATE_FROM_MANUAL);
		adjustVo.setInvAdjust(adj);
		
		int nowRow =  2+r.nextInt(8);
		for (int i =0; i < nowRow; i++) {
			InvAdjustDetailVO InvAdjustDetailVO = new InvAdjustDetailVO();
			InvAdjustDetail adjDetail = new InvAdjustDetail();
			adjDetail.setSkuId("6910CB7D9B7E4D22AC5FD677F79405C4");
			adjDetail.setLocationId("9E96204C93A44DCBA75795867CA21517");
			adjDetail.setStockQty(new Double(1000));
			adjDetail.setRealQty(997d);
			adjDetail.setDifferenceQty(-3d);
			adjDetail.setAdjustType(Constant.ADJUST_TYPE_OUT);
			InvAdjustDetailVO.setInvAdjustDetail(adjDetail);
			list.add(InvAdjustDetailVO);
		}
		adjustVo.setAdjDetailVoList(list);
		BeanPropertyBindingResult br = super.validateEntity(adjustVo, ValidSave.class);
		ResultModel rm = this.adjustController.update(adjustVo, br);
		formatResult(rm);
	}

	/**
	 * 新增调账单
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午6:03:11<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testAdjustAdd() throws Exception {
		Random r = new Random();
		InvAdjustVO adjustVo = new InvAdjustVO();
		List<InvAdjustDetailVO> list = new ArrayList<InvAdjustDetailVO>();
		InvAdjust adj = new InvAdjust();
		adj.setCountId("countId");
		adj.setAdjustStatus(Constant.STATUS_OPEN);
		adj.setDataFrom(Constant.ADJUST_DATE_FROM_MANUAL);
		adjustVo.setInvAdjust(adj);
		
		int nowRow =  2+r.nextInt(8);
		for (int i =0; i < nowRow; i++) {
			InvAdjustDetailVO InvAdjustDetailVO = new InvAdjustDetailVO();
			InvAdjustDetail adjDetail = new InvAdjustDetail();
			adjDetail.setSkuId("7FF5633D88AE4AAD928ED1EC15A85BEC");
			adjDetail.setBatchNo("002");
			adjDetail.setLocationId("90");
			adjDetail.setStockQty(0d);
			adjDetail.setRealQty(1000d);
			adjDetail.setDifferenceQty(1000d);
			adjDetail.setAdjustType(Constant.ADJUST_TYPE_OUT);
			InvAdjustDetailVO.setInvAdjustDetail(adjDetail);
			list.add(InvAdjustDetailVO);
		}
		adjustVo.setAdjDetailVoList(list);
		BeanPropertyBindingResult br = super.validateEntity(adjustVo, ValidSave.class);
		ResultModel rm = this.adjustController.add(adjustVo, br);
		formatResult(rm);
	}

	/**
	 * 测试盈亏调整单查询列表
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午5:59:21<br/>
	 * @author 王通<br/>
	 */
	public void testAdjustList() {
		InvAdjustVO adjVo = new InvAdjustVO();
		InvAdjust adj = new InvAdjust();
		adjVo.setInvAdjust(adj);
//		adj.setAdjustNo("");
//		adjVo.setCountNo("");
		ResultModel rm =  this.adjustController.list(adjVo);
		formatResult(rm);
	}


	/**
	 * 测试库存导入
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月6日 下午6:01:43<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testStockImport() throws Exception {
		final FileInputStream fis = new FileInputStream("D:\\workspace\\yunkouan\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\wms\\WEB-INF\\classes\\ef768485-43c0-4f49-8e94-3eb9c83b3bdb.xlsx");
		MockMultipartFile multipartFile = new MockMultipartFile("demo","demo.xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",fis);
		ResultModel rm = this.stockController.importStock(multipartFile);
		formatResult(rm);
//		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest() ;
////		String strTEAMPHOTOCLASSID = String.valueOf( TEAMPHOTOCLASSID ) ;
////		String strSPECIFICATIONID = String.valueOf( SPECIFICATIONID ) ;
//		final FileInputStream fis = new FileInputStream("C:/Users/Think/Desktop/demo.xlsx");
//		MockMultipartFile multipartFile = new MockMultipartFile("demo","demo.xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",fis);
//		request.addFile(multipartFile);
//		request.setMethod("POST");
//		request.setContentType("multipart/form-data");
//		request.addHeader("Content-type", "multipart/form-data");
//		request.setRequestURI("/wms/stock/import");
////		request.addParameter( "leader", "lzj update" ) ;
////		request.addParameter( "STeamphotoclass", strTEAMPHOTOCLASSID ) ;
////		request.addParameter( "SSpecification", strSPECIFICATIONID ) ;
////		int countTeamphoto = teamPhotoDao.getTeamphotoCount() ;
////		int countTeamphotoclass = teamPhotoDao.getSTeamphotoclassCount() ;
////		int countSpecification = teamPhotoDao.getSSpecificationCount() ;
//		new AnnotationMethodHandlerAdapter()
//		.handle( request, new MockHttpServletResponse(), stockController ) ;
//		assertEquals( 
//		teamPhotoDao.getSTeamphotoById( TEAMPHOTOID ).getLeader(), "lzj update" 
//		) ;
//		assertEquals( teamPhotoDao.getTeamphotoCount(), countTeamphoto ) ;
//		assertEquals( teamPhotoDao.getSTeamphotoclassCount(), countTeamphotoclass ) ;
//		assertEquals( teamPhotoDao.getSSpecificationCount(), countSpecification ) ;
		
	}

	/**
	 * 测试库存列表查询
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月6日 下午1:09:26<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testStockList() throws Exception {
//		int num = r.nextInt(10) + 1;
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		stockVo.setInvStock(stock);
		stock.setOrgId("1");
		stock.setWarehouseId("1");
		stockVo.setContainTemp(true);
		stockVo.setOnlyTemp(false);
		stockVo.setResultOrder("t1.stock_id2 desc");
//		stock.setAsnDetailId("13B58FE06188427A90AC1FEE6395856B");
//		stock.setAsnId(null);
		//测试单条
//		stock.setBatchNo(IdUtil.getUUID().substring(32));
//		stock.setSkuId(IdUtil.getUUID());
//		stock.setLocationId("1");
		//测试范围查询
		List<String> batchNoList = new ArrayList<String>();
		batchNoList.add("21220170218201");
		batchNoList.add("21220170218301");
		batchNoList.add("21220170218001");
		List<String> skuIdList = new ArrayList<String>();
		skuIdList.add("F5A76172E35749188EEC5F5C853D4EF9");
		skuIdList.add("F0B37AC04E834E1CAE45398434740DBF");
		skuIdList.add("2");
		skuIdList.add("1");
		skuIdList.add("0");
		stockVo.setBatchNoList(batchNoList);
		stockVo.setSkuIdList(skuIdList);
		super.toJson(stockVo);
		ResultModel rm = this.stockController.list(stockVo);
		super.toJson(rm);
//		formatResult(rm);
//		stock.setPackId(null);
	}

	/**
	 * 测试快速移位
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月3日 上午11:40:26<br/>
	 * @author 王通<br/>
	 */
	public void testStockShift() {
//		Random r = new Random();
//		int num = r.nextInt(10) + 1;
		double num = 2d;
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		InvLog stockLog = new InvLog();
		stockVo.setInvStock(stock);
		stockVo.setInvLog(stockLog);
		stockLog.setOpPerson("张三");
		stock.setStockId("C091182F3E7B4875A4983FFE757765BF");
		stockVo.setFindNum(num);
		stockVo.setInLocationName("库位100");
		InvStockVO stockVo2 = new InvStockVO();
		InvStock stock2 = new InvStock();
		stockVo2.setInvStock(stock2);
		stock2.setLocationId("12");
//		BeanPropertyBindingResult br = super.validateEntity(stockVo, ValidSave.class);
		try {
			this.stockService.shift(stockVo);
//			System.out.println(rm.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		formatResult(rm);
	}

	/**
	 * 测试库存日志列表
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月3日 上午10:16:23<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testStockLogList() throws Exception {
		InvLogVO logVo = new InvLogVO();
		InvLog log = new InvLog();
//		log.setWarehouseId("1");
//		log.setOrgId("1");
		log.setSkuId("a");
		log.setBatchNo("1001");
		logVo.setOwnerName("中云");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date startDate = sdf.parse("1900-10-01");
//		logVo.setStartDate(startDate);
		Date endDate = sdf.parse("2017-3-2");
		logVo.setEndDate(endDate);
		logVo.setInvLog(log);
		logVo.setCurrentPage(1);
		logVo.setPageSize(10);
		ResultModel rm = this.stockController.logList(logVo);
		formatResult(rm);
	}

	/**
	 * 测试库存解冻
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月2日 上午10:38:21<br/>
	 * @author 王通<br/>
	 */
	public void testStockUnfreeze() {
		List<String> idList = new ArrayList<String>();
		idList.add("4186C0495E4D4FD5BC2ACCA820ACBD4E");
		idList.add("5CE8E1D6508F452D9744233DF07A47B0");
		try {
			this.stockService.unfreezeList(idList);
//			System.out.println(rm.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试库存冻结
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月2日 上午10:38:18<br/>
	 * @author 王通<br/>
	 */
	public void testStockFreeze() {
		List<String> idList = new ArrayList<String>();
		idList.add("4186C0495E4D4FD5BC2ACCA820ACBD4E");
		idList.add("5CE8E1D6508F452D9744233DF07A47B0");
		try {
			this.stockService.freezeList(idList);
//			System.out.println(rm.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 测试快速调整库存
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月28日 下午3:30:49<br/>
	 * @author 王通<br/>
	 */
	public void testStockChangeStock() {
		Random r = new Random();
		double num = r.nextInt(10) + 1d;
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		InvLog stockLog = new InvLog();
		stockVo.setInvStock(stock);
		stockVo.setInvLog(stockLog);
		stockVo.setChangeStock(true);
		stock.setAsnDetailId(null);
		stock.setAsnId(null);
		String batchNo = IdUtil.getUUID();
		stock.setBatchNo(batchNo);
		stock.setSkuId("1");
		stock.setLocationId("100");
		stock.setPackId(null);
		//设置log属性
		//UUID，作业人员，操作类型，
		//日志类型，相关单号，库位ID
		 stockLog.setOpPerson(IdUtil.getUUID());
		 stockLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
		 stockLog.setLogType(Constant.STOCK_LOG_TYPE_CHANGE);
		 stockLog.setInvoiceBill(IdUtil.getUUID());
		 stockLog.setLocationId("100");
		 stockLog.setBatchNo(batchNo);
		 stockLog.setSkuId("1");
		 stockLog.setQty(num);
		//测试1：删除时
//		stock.setStockQty(0);
//		 stock.setStockId("225AD30E24E546D0AAA2459224446EE6");
//			stockVo.setFindNum(-10);
		 //测试2：新增时
			stock.setStockQty(num);
			stockVo.setFindNum(num);
		 //测试3：修改时
//			stock.setStockQty(10);
//		 stock.setStockId("985E6ACB16B14094B8D5AE52E8D8A99F");
//		stockVo.setFindNum(-num);
//			stockVo.setFindNum(-6);
			
//		BeanPropertyBindingResult br = super.validateEntity(stockVo, ValidSave.class);
		try {
			this.stockService.changeStock(stockVo);
//			System.out.println(rm.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		formatResult(rm);
	}

	/**
	 * 测试解锁出库库存
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月28日 下午2:38:18<br/>
	 * @author 王通<br/>
	 */
	public void testStockUnLockOutStock() {
		Random r = new Random();
		double num = r.nextInt(10) + 1d;
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		InvLog stockLog = new InvLog();
		stockVo.setInvStock(stock);
		stockVo.setInvLog(stockLog);
		stock.setAsnDetailId(null);
		stock.setAsnId(null);
		stock.setBatchNo("21220170218001");
		stock.setSkuId("1");
		stock.setLocationId("1");
		stock.setPackId(null);
		stockVo.setFindNum(num);
//		BeanPropertyBindingResult br = super.validateEntity(stockVo, ValidSave.class);
		try {
			this.stockService.unlockOutStock(stockVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试锁定出库库存
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月28日 下午1:30:45<br/>
	 * @author 王通<br/>
	 */
	public void testStockLockOutStock() {
		Random r = new Random();
		double num = r.nextInt(10) + 1d;
		num = 83;
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		InvLog stockLog = new InvLog();
		stockVo.setInvStock(stock);
		stockVo.setInvLog(stockLog);
//		stock.setAsnDetailId(null);
//		stock.setAsnId(null);
//		stock.setBatchNo("test20170310471");
		stock.setSkuId("1");
		stock.setLocationId("1");
//		stock.setPackId("2");
		stockVo.setFindNum(num);
//		BeanPropertyBindingResult br = super.validateEntity(stockVo, ValidSave.class);
		try {
			this.stockService.lockOutStock(stockVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试入库
	 * @Description 
	 * @version 2017年2月23日 下午1:22:48<br/>
	 * @author 王通<br/>
	 */
	public void testStockInStock() {
		Random r = new Random();
		double num = r.nextInt(10) + 1d;
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		InvLog stockLog = new InvLog();
		stockVo.setInvStock(stock);
		stockVo.setInvLog(stockLog);
		stock.setAsnDetailId(null);
		stock.setAsnId(null);
		stock.setBatchNo(IdUtil.getUUID().substring(32));
		stock.setSkuId(IdUtil.getUUID());
		stock.setLocationId("1");
		stock.setPackId("2");
		//设置log属性
		//UUID，作业人员，操作类型，
		//日志类型，相关单号，库位ID
		 stockLog.setLogId(IdUtil.getUUID());
		 stockLog.setOpPerson(IdUtil.getUUID());
		 stockLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
		 stockLog.setLogType(Constant.STOCK_LOG_TYPE_CHANGE);
		 stockLog.setInvoiceBill(null);
		 stockLog.setLocationId(null);
		stockVo.setFindNum(num);
//		BeanPropertyBindingResult br = super.validateEntity(stockVo, ValidSave.class);
		try {
			InvStock rm = this.stockService.inStock(stockVo);
			System.out.println(rm.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		formatResult(rm);
	}
	
	/**
	 * 测试出库
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月3日 上午11:39:38<br/>
	 * @author 王通<br/>
	 */
	public void testStockOutStock() {
//		Random r = new Random();
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		InvLog stockLog = new InvLog();
		stockVo.setInvStock(stock);
		stockVo.setInvLog(stockLog);
		stock.setAsnDetailId(null);
		stock.setAsnId(null);
		stock.setBatchNo(IdUtil.getUUID().substring(32));
		stock.setSkuId("92CE7CEF935E4BCA8661C4A41CC0DAEE");
		stock.setLocationId("1");
		stock.setPackId("2");
		//设置log属性
		//UUID，作业人员，操作类型，
		//日志类型，相关单号，库位ID
		 stockLog.setLogId(IdUtil.getUUID());
		 stockLog.setOpPerson(IdUtil.getUUID());
		 stockLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
		 stockLog.setLogType(Constant.STOCK_LOG_TYPE_CHANGE);
		 stockLog.setInvoiceBill(null);
		 stockLog.setLocationId(null);
		 stockVo.setFindNum(1d);
//		BeanPropertyBindingResult br = super.validateEntity(stockVo, ValidSave.class);
		try {
			this.stockService.outStock(stockVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		formatResult(rm);
	}

	/**
	 * 测试获取预分配详情
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月20日 下午5:14:39<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testStockOutLockDetail() throws Exception {
		ResultModel rm = this.stockController.outLockDetail("7B845DFA63E94CC58288DF93972B1379");
		formatResult(rm);
	}
	
	/**
	 * 测试获取预分配详情
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月20日 下午5:14:39<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testStockStockSkuCount() throws Exception {
		List<String> skuList = new ArrayList<String>();
		skuList.add("F7EE229163B04B1DB859A5FD372582F1");
		ResultModel rm = this.stockController.stockSkuCount(skuList);
		formatResult(rm);
	}
	/**
	 * 测试预分配重算
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月21日 下午4:25:54<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testRefreshOutLock() throws Exception {
		ResultModel rm = this.stockController.refreshOutLock("7B845DFA63E94CC58288DF93972B1379");
		formatResult(rm);
	}
	/**
	 * 测试移位生效
	 * @Description 
	 * @version 2017年2月23日 下午1:20:21<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testShiftEnable() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("5F0874A7F1E84069B173C392E1D20312");
//		shiftIdList.add("DCFC1D4BEEC04247B79EE0728B739D7E");
		InvShiftVO vo = new InvShiftVO();
		vo.setShiftIdList(shiftIdList);
		ResultModel rm = this.shiftController.enable(vo);
		super.toJson(rm);
//		formatResult(rm);
	}

	/**
	 * 测试移位生效
	 * @Description 
	 * @version 2017年2月23日 下午1:20:21<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testShiftDisable() throws Exception {
		List<String> shiftIdList = new ArrayList<String>();
		shiftIdList.add("0A79AA343A6B4E6CB1C50E178AE77828");
//		shiftIdList.add("DCFC1D4BEEC04247B79EE0728B739D7E");
		InvShiftVO vo = new InvShiftVO();
		vo.setShiftIdList(shiftIdList);
		ResultModel rm = this.shiftController.disable(vo);
		super.toJson(rm);
//		formatResult(rm);
	}
	/**
	 * 测试 - 查询移位单
	 * @throws Exception
	 * @version 2017年2月17日18:35:51<br/>
	 * @author 王通<br/>
	 */
	public void testShiftList() throws Exception {
		InvShiftVO shiftVo = new InvShiftVO();
		InvShift shift = new InvShift();
		shiftVo.setInvShift(shift);
		shiftVo.setCurrentPage(0);
		shiftVo.setPageSize(2);
		shiftVo.setInLocationName("1");
		shiftVo.getInvShift().setShiftStatus(Constant.STATUS_ACTIVE);
		super.toJson(shiftVo);
		ResultModel rm = this.shiftController.list(shiftVo);
		super.toJson(rm);
//		formatResult(rm);
	}
	
	/**
	 * 测试 - 查询移位单详情
	 * @throws Exception
	 * @version 2017年2月17日18:35:51<br/>
	 * @author 王通<br/>
	 */
	public void testShiftView() throws Exception {
		String shiftVo = "D93D857CF70E11E684B4DCFE0719DC07D93D858BF70E11E684B4DCFE0719DC07";
		ResultModel rm = this.shiftController.view(shiftVo);

		super.toJson(rm);
		formatResult(rm);
	}

	/**
	 * 测试移位单作业完成
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午6:13:58<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testShiftConfirm() throws Exception {
		Random r = new Random();
		InvShiftVO shiftVo = new InvShiftVO();
		List<InvShiftDetailVO> list = new ArrayList<InvShiftDetailVO>();
		InvShift shift = new InvShift();
		shift.setShiftType(Constant.SHIFT_TYPE_INWAREHOUSE);
		shift.setNote("我是备注~");
		shiftVo.setInvShift(shift);
		shift.setShiftId("8C59861C1DF14D37A0591EA3CA4750C8");
		List<String> shiftDetailIdList = new ArrayList<String>();
		shiftDetailIdList.add("02812241D48449AAB3AC03EF3FDFBF42");
		shiftDetailIdList.add("10F5D499171C4CEA8D48973D7C43371D");
		shiftDetailIdList.add("2C45C576B3DD40C88BCF90165610A199");
		shiftDetailIdList.add("CAFDF3D6E60846CEAAE5D30655B593D4");
		shiftDetailIdList.add("D80FC1CF4FC4433BA34D3302BDB534FC");
		
		int nowRow = shiftDetailIdList.size();
		for (int i =0; i < nowRow; i++) {
			InvShiftDetailVO InvShiftDetailVO = new InvShiftDetailVO();
			InvShiftDetail shiftDetail = new InvShiftDetail();
			shiftDetail.setShiftDetailId(shiftDetailIdList.get(i));
			shiftDetail.setInLocation("105");
			shiftDetail.setShiftQty(1d + r.nextInt(1000));
			InvShiftDetailVO.setInvShiftDetail(shiftDetail);
			list.add(InvShiftDetailVO);
		}
		shiftVo.setListInvShiftDetailVO(list);
		ResultModel rm = this.shiftController.confirm(shiftVo);
		formatResult(rm);
	}
	/**
	 * 测试快速移位单作业完成，手持终端用
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午6:13:58<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testShiftQuickConfirm() throws Exception {
		Random r = new Random();
		InvShiftVO shiftVo = new InvShiftVO();
		InvShift shift = new InvShift();
		shiftVo.setInvShift(shift);
		List<InvShiftDetailVO> shiftDetailVoList = new ArrayList<InvShiftDetailVO>();
		InvShiftDetailVO shiftDetailVo1 = new InvShiftDetailVO();
		shiftDetailVo1.setOutLocationNo("C101");
		shiftDetailVo1.setInLocationNo("C102");
		shiftDetailVo1.setSkuBarCode("binggo");
		shiftDetailVo1.setInvShiftDetail(new InvShiftDetail());
		shiftDetailVo1.getInvShiftDetail().setBatchNo("20161010");
		shiftDetailVo1.getInvShiftDetail().setShiftQty(50d);
		shiftDetailVoList.add(shiftDetailVo1);
		InvShiftDetailVO shiftDetailVo2 = new InvShiftDetailVO();
		shiftDetailVo2.setOutLocationNo("C101");
		shiftDetailVo2.setInLocationNo("C102");
		shiftDetailVo2.setSkuBarCode("binggo");
		shiftDetailVo2.setInvShiftDetail(new InvShiftDetail());
		shiftDetailVo2.getInvShiftDetail().setBatchNo("20161014");
		shiftDetailVo2.getInvShiftDetail().setShiftQty(50d);
		shiftDetailVoList.add(shiftDetailVo2);
		shiftVo.setListInvShiftDetailVO(shiftDetailVoList);
		ResultModel rm = this.shiftController.quickConfirm(shiftVo);
		formatResult(rm);
	}
	/**
	 * 测试移位单打印
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 下午5:43:35<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public void testShiftPrint() throws Exception {
		ResultModel rm = this.shiftController.print("DCFC1D4BEEC04247B79EE0728B739D7E");
		formatResult(rm);
	}
	/**
	 * 测试 - 查询移位单详情
	 * @throws Exception
	 * @version 2017年2月17日18:35:51<br/>
	 * @author 王通<br/>
	 */
	public void testShiftSaveOrUpdate() throws Exception {
		Random r = new Random();
		InvShiftVO shiftVo = new InvShiftVO();
		List<InvShiftDetailVO> list = new ArrayList<InvShiftDetailVO>();
		InvShift shift = new InvShift();
		shift.setShiftType(Constant.SHIFT_TYPE_INWAREHOUSE);
		shift.setNote("我是备注~");
		shiftVo.setInvShift(shift);
		//此行为测试编辑的情况
		shift.setShiftId("5F0874A7F1E84069B173C392E1D20312");
		
		int nowRow =  1;
		for (int i =0; i < nowRow; i++) {
			InvShiftDetailVO InvShiftDetailVO = new InvShiftDetailVO();
			InvShiftDetail shiftDetail = new InvShiftDetail();
			shiftDetail.setSkuId("1");
			shiftDetail.setInLocation("2");
			shiftDetail.setOutLocation("1");
			shiftDetail.setShiftQty(1d + r.nextInt(11));
			InvShiftDetailVO.setInvShiftDetail(shiftDetail);
			list.add(InvShiftDetailVO);
		}
		shiftVo.setListInvShiftDetailVO(list);
		BeanPropertyBindingResult br = super.validateEntity(shiftVo, ValidSave.class);
		super.toJson(shiftVo);
		ResultModel rm = this.shiftController.saveOrUpdate(shiftVo, br);
		super.toJson(rm);
//		formatResult(rm);
	}

	private static String md5(String data, String key) {
	   String sign = new StringBuilder(data.trim()).append(key).toString();
	    return MD5Util.md5(sign);
	}
}
