/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.putaway.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.test.PutawayTest;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 保存上架单<br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class PutawayInsertTest extends PutawayTest {

	@Test
	public void test() throws Exception {
		try {
			System.out.println("*#*#*#*#*#*#*#*#*#*#*#*# 保存上架单 *#*#*#*#*#*#*#*#*#*#*#*#");
			this.testInsert();
//			this.testInsertNoDetail();
//			this.testInsertSameDetail();
//			this.testInsertValidate();
//			this.testInsertOverQty();
//			this.testInsertOneAsnDetailMultipleLocation();
//			this.testInsertMultipleASN();
		} catch (Exception e) {
			e.printStackTrace();
			ResultModel rm = new ResultModel();
			rm.setError();
			rm.addMessage(e.getLocalizedMessage());
			super.toJson(rm, true);
		}
	}
	
	/**
	 * 多个ASN单上架
	 * @throws Exception
	 * @version 2017年3月24日 下午5:37:13<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertMultipleASN () throws Exception {
		String asnId = null;
		RecAsn asn = null;
		List<RecAsnDetail> listDetail = null;
		RecAsnDetail recAsnDetail = null;
		Double qty = null;
		RecPutawayDetailVO detailVO = null;
		
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		
		
		asnId = "06D108B992C043888301A79BB0F600DD";
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		asn = super.asnExtlService.findAsnById(asnId);
		listDetail = super.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		recAsnDetail = listDetail.get(0);
		
		InvStockVO stockVo = new InvStockVO( new InvStock() );
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setAsnDetailId(recAsnDetail.getAsnDetailId());
		List<InvStock> list = super.stockService.list(stockVo );
		InvStock invStock = list.get(0);
		qty = invStock.getStockQty();
		detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty}, new String[]{"1"},invStock);
		
		
		listSavePutawayDetailVO.add(detailVO);
		
		
		asnId = "68C54882B49C4D18AE2314C4226726C6";
		asn = super.asnExtlService.findAsnById(asnId);
		listDetail = super.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		recAsnDetail = listDetail.get(0);
		
		
		stockVo = new InvStockVO( new InvStock() );
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setAsnDetailId(recAsnDetail.getAsnDetailId());
		list = super.stockService.list(stockVo );
		invStock = list.get(0);
		qty = invStock.getStockQty();
		detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty}, new String[]{"1"},invStock);
		
		listSavePutawayDetailVO.add(detailVO);
		
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
		
	}
	
	/**
	 * 一个ASN单明细，分多个库位上架，但总数量超过收货数量
	 * @throws Exception
	 * @version 2017年3月24日 下午5:17:50<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertOneAsnDetailMultipleLocation () throws Exception {
		String asnId = "06D108B992C043888301A79BB0F600DD";
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		RecAsn asn = super.asnExtlService.findAsnById(asnId);
		List<RecAsnDetail> listDetail = super.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		RecAsnDetail recAsnDetail = listDetail.get(0);
		
		
		InvStockVO stockVo = new InvStockVO( new InvStock() );
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setAsnDetailId(recAsnDetail.getAsnDetailId());
		List<InvStock> list = super.stockService.list(stockVo );
		InvStock invStock = list.get(0);
		Double qty = invStock.getStockQty();
		RecPutawayDetailVO detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty,qty}, new String[]{"1","2"},invStock);
		
		listSavePutawayDetailVO.add(detailVO);
		
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
	}
	
	
	
	
	/**
	 * 上架数量大于ASN单明细收货数量
	 * @throws Exception
	 * @version 2017年3月24日 下午4:37:15<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertOverQty () throws Exception {
		String asnId = "06D108B992C043888301A79BB0F600DD";
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		RecAsn asn = super.asnExtlService.findAsnById(asnId);
		List<RecAsnDetail> listDetail = super.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		RecAsnDetail recAsnDetail = listDetail.get(0);
		
		
		InvStockVO stockVo = new InvStockVO( new InvStock() );
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setAsnDetailId(recAsnDetail.getAsnDetailId());
		List<InvStock> list = super.stockService.list(stockVo );
		InvStock invStock = list.get(0);
		Double qty = invStock.getStockQty()+2;
		RecPutawayDetailVO detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty}, new String[]{"1"},invStock);
		
		listSavePutawayDetailVO.add(detailVO);
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
	}
	
	/**
	 * 字段校验
	 * @throws Exception
	 * @version 2017年3月24日 下午4:16:49<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertValidate () throws Exception {
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		
		RecPutawayDetailVO detailVO = new RecPutawayDetailVO();
		listSavePutawayDetailVO.add(detailVO);
		RecPutawayLocationVO locVO = new RecPutawayLocationVO();
		List<RecPutawayLocationVO> listSavePutawayLocationVO = new ArrayList<RecPutawayLocationVO>();
		listSavePutawayLocationVO.add(locVO);
		detailVO.setListSavePutawayLocationVO(listSavePutawayLocationVO );
		
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 上架相同的ASN单明细
	 * @throws Exception
	 * @version 2017年3月24日 下午4:10:59<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertSameDetail () throws Exception {
		String asnId = "06D108B992C043888301A79BB0F600DD";
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		RecAsn asn = super.asnExtlService.findAsnById(asnId);
		List<RecAsnDetail> listDetail = super.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		RecAsnDetail recAsnDetail = listDetail.get(0);
		
		
		InvStockVO stockVo = new InvStockVO( new InvStock() );
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setAsnDetailId(recAsnDetail.getAsnDetailId());
		List<InvStock> list = super.stockService.list(stockVo );
		InvStock invStock = list.get(0);
		Double qty = invStock.getStockQty() / 3;
		RecPutawayDetailVO detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty,qty,qty}, new String[]{"1","2","3"},invStock);
		
		listSavePutawayDetailVO.add(detailVO);
		listSavePutawayDetailVO.add(detailVO);
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 没有上架明细
	 * @throws Exception
	 * @version 2017年3月24日 下午3:11:29<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertNoDetail () throws Exception {
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试保存上架单
	 	
	 	-- 清空环境
		DELETE FROM rec_putaway_location;
		DELETE FROM rec_putaway_detail;
		DELETE FROM rec_putaway;
	 	
	 	-- 查询结果
		SELECT * FROM rec_putaway ORDER BY putaway_id2 DESC 
		SELECT * FROM rec_putaway_detail ORDER BY putaway_detail_id2 DESC 
		SELECT * FROM rec_putaway_location ORDER BY putaway_location_id2 DESC 
	 * @throws Exception
	 * @version 2017年3月24日 上午10:41:37<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsert () throws Exception {
		String asnId = "0A18F3FCC9B5441AAFF454B3455089B4";
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		RecAsn asn = super.asnExtlService.findAsnById(asnId);
		List<RecAsnDetail> listDetail = super.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		
		for (int i = 0; i < listDetail.size(); i++) {
			RecAsnDetail recAsnDetail = listDetail.get(i);
			RecPutawayDetailVO detailVO = null;
			
			InvStockVO stockVo = new InvStockVO( new InvStock() );
			stockVo.setContainTemp(true);
			stockVo.getInvStock().setAsnDetailId(recAsnDetail.getAsnDetailId());
			List<InvStock> list = super.stockService.list(stockVo );
			for (int j = 0; j < list.size(); j++) {
				InvStock invStock = list.get(j);
				Double qty = invStock.getStockQty() / 3;
				detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty,qty,qty}, new String[]{"1","2","3"},invStock);
				detailVO.getPutawayDetail().setLocationId(invStock.getLocationId());
				listSavePutawayDetailVO.add(detailVO);
			}
		}
		

//		// 分两个库位进行上架，一个库位上架数量为0，只保存一条上架单操作明细
//		detailVO = new RecPutawayDetailVO(new RecPutawayDetail());
//		putawayDetail = detailVO.getPutawayDetail();
//		listLocation = new ArrayList<RecPutawayLocationVO>();
//		locationVO1 = new RecPutawayLocationVO(new RecPutawayLocation());
//		locationVO2 = new RecPutawayLocationVO(new RecPutawayLocation());
//		l1 = locationVO1.getPutawayLocation();
//		l2 = locationVO2.getPutawayLocation();
//		listSavePutawayDetailVO.add(detailVO);
//		detailVO.setListSavePutawayLocationVO(listLocation);
//		listLocation.add(locationVO1);
//		listLocation.add(locationVO2);
//		putawayDetail.setAsnDetailId("13832D839A6E4B25BF04B53B93A5B29E");
//		putawayDetail.setOwner("1");
//		l1.setPutawayQty(46);
//		l1.setLocationId("1");
//		l2.setPutawayQty(0);
//		l2.setLocationId("2");
//		
//		// 部分上架
//		detailVO = new RecPutawayDetailVO(new RecPutawayDetail());
//		putawayDetail = detailVO.getPutawayDetail();
//		listLocation = new ArrayList<RecPutawayLocationVO>();
//		locationVO1 = new RecPutawayLocationVO(new RecPutawayLocation());
////		locationVO2 = new RecPutawayLocationVO(new RecPutawayLocation());
//		l1 = locationVO1.getPutawayLocation();
////		l2 = locationVO2.getPutawayLocation();
//		listSavePutawayDetailVO.add(detailVO);
//		detailVO.setListSavePutawayLocationVO(listLocation);
//		listLocation.add(locationVO1);
////		listLocation.add(locationVO2);
//		putawayDetail.setAsnDetailId("59DB6CD420A243DABEF5B40EAD851471");
//		putawayDetail.setOwner("1");
//		l1.setPutawayQty(20);
//		l1.setLocationId("6");
		
		
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
		System.out.println(((RecPutawayVO)rm.getObj()).getPutaway().getPutawayId());
	}
	
	
}
