/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 下午2:50:29<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.rec.controller.PutawayController;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayLocationExtlService;
import com.yunkouan.wms.modules.rec.util.RecUtil;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 上架单测试<br/><br/>
 * @version 2017年2月22日 下午2:50:29<br/>
 * @author andy wang<br/>
 */
public class PutawayTest extends BaseJunitTest {

	/**
	 * 测试的控制类
	 */
	@Autowired
	protected PutawayController controller;
	
	@Autowired
	protected IPutawayDetailExtlService ptwDetailExtlService;
	
	@Autowired
	protected IPutawayLocationExtlService ptwLocExtlService;
	
	@Autowired
	protected IPutawayExtlService ptwExtlService;
	
	@Autowired
	protected IASNDetailExtlService asnDetailExtlService;
	
	@Autowired
	protected IASNExtlService asnExtlService;
	@Autowired
	protected IStockService stockService;
	
	
	
	private void testConfirmPutaway () throws Exception {
		
	}
	
	private void testUntearPutaway () throws Exception {
		List<String> listPtwId = new ArrayList<String>();
		listPtwId.add("AD880FCCF1FC4FC2AFDF9DDCAF0F24BC");
		ResultModel rm = this.controller.unsplit(listPtwId);
		super.formatResult(rm);
	}
	
	private void testTearPutaway () throws Exception {
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		RecPutaway putaway = recPutawayVO.getPutaway();
		putaway.setPutawayId("23D4780E75554BC49EAB39A436824B52");
		List<RecPutawayDetail> listDetail = ptwDetailExtlService.listPutawayDetailByPtwId(putaway.getPutawayId());		
		
		List<RecPutawayDetailVO> list = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(list);
		
		for (int i = 0; i < listDetail.size(); i++) {
			RecPutawayDetail detail = listDetail.get(i);
			RecPutawayDetailVO detailVO = new RecPutawayDetailVO(detail);
			if ( i == 0 ) {
				detail.setPlanPutawayQty(20d);
			} else {
				detail.setPlanPutawayQty(detail.getPlanPutawayQty()-i);
			}
			list.add(detailVO);
		}
		this.controller.split(recPutawayVO);
	}
	
	private void testCancelPutaway () throws Exception {
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add("E80EF617CB6149F494124A49A50BC312");
		this.controller.cancel(listPutawayId );
	}
	
	
	private void testInactivePutaway () throws Exception {
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add("07E6992CBD374059B6D181CD22B100D0");
		this.controller.disable(listPutawayId );
	}
	
	
	/**
	 * 测试 - 显示单个上架单
	 * @throws Exception
	 * @version 2017年3月1日 下午7:54:25<br/>
	 * @author andy wang<br/>
	 */
	private void testViewPutaway () throws Exception {
		ResultModel rm = this.controller.view("23D4780E75554BC49EAB39A436824B52");
		super.formatResult(rm);
	}
	
	/**
	 * 测试 - 查询上架单列表
	 * @throws Exception
	 * @version 2017年2月28日 下午3:50:31<br/>
	 * @author andy wang<br/>
	 */
	private void testListPutaway () throws Exception {
		RecPutawayVO recPutawayVO = new RecPutawayVO(new RecPutaway());
//		recPutawayVO.setAsnNo("12341123");
//		recPutawayVO.setPoNo("123");
		recPutawayVO.setCurrentPage(0);
		ResultModel rm = this.controller.list(recPutawayVO);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试 - 生效上架单
	 * @throws Exception
	 * @version 2017年2月28日 下午3:48:24<br/>
	 * @author andy wang<br/>
	 */
	private void testActivePutaway () throws Exception {
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add("D2A49561A8864450BC2B7A0990724591");
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		recPutawayVO.setListPutawayId(listPutawayId);
		this.controller.enable(recPutawayVO);
	}
	
	/**
	 * 测试 - 保存上架单
	 * @throws Exception
	 * @version 2017年2月22日 下午2:56:06<br/>
	 * @author andy wang<br/>
	 * 
	   -- 造数据
		SELECT a.asn_detail_id,a.stock_qty,a.stock_volume,a.stock_weight FROM inv_stock a WHERE a.asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5' ;
		SELECT * 
		FROM rec_asn_detail WHERE asn_id='F22F7D0E90FB4AB2BD2EA963441D3BE5' ;
		
		
		-- 查看结果
		SELECT * FROM rec_putaway ORDER BY putaway_id2 DESC ;
		SELECT * FROM rec_putaway_detail ORDER BY putaway_detail_id2 DESC ;
		SELECT * FROM rec_putaway_location ORDER BY putaway_location_id2 DESC ;
	   
	 */
	private void testInsertPutaway () throws Exception {
		
		RecPutawayVO recPutawayVO = new RecPutawayVO(new RecPutaway());
		List<RecPutawayDetailVO> listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO );
		
		// 分两个库位进行上架,全部上架，全部保存
		RecPutawayDetailVO detailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		RecPutawayDetail putawayDetail = detailVO.getPutawayDetail();
		List<RecPutawayLocationVO> listLocation = new ArrayList<RecPutawayLocationVO>();
		RecPutawayLocationVO locationVO1 = new RecPutawayLocationVO(new RecPutawayLocation());
		RecPutawayLocationVO locationVO2 = new RecPutawayLocationVO(new RecPutawayLocation());
		RecPutawayLocation l1 = locationVO1.getPutawayLocation();
		RecPutawayLocation l2 = locationVO2.getPutawayLocation();
		listSavePutawayDetailVO.add(detailVO);
		detailVO.setListSavePutawayLocationVO(listLocation);
		listLocation.add(locationVO1);
		listLocation.add(locationVO2);
		putawayDetail.setAsnDetailId("0BD38D6648034E828EC7743C71BA5D62");
		putawayDetail.setOwner("1");
		l1.setPutawayQty(24d);
		l1.setLocationId("1");
		l2.setPutawayQty(12d);
		l2.setLocationId("2");
		
		// 分两个库位进行上架，一个库位上架数量为0，只保存一条上架单操作明细
		detailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		putawayDetail = detailVO.getPutawayDetail();
		listLocation = new ArrayList<RecPutawayLocationVO>();
		locationVO1 = new RecPutawayLocationVO(new RecPutawayLocation());
		locationVO2 = new RecPutawayLocationVO(new RecPutawayLocation());
		l1 = locationVO1.getPutawayLocation();
		l2 = locationVO2.getPutawayLocation();
		listSavePutawayDetailVO.add(detailVO);
		detailVO.setListSavePutawayLocationVO(listLocation);
		listLocation.add(locationVO1);
		listLocation.add(locationVO2);
		putawayDetail.setAsnDetailId("13832D839A6E4B25BF04B53B93A5B29E");
		putawayDetail.setOwner("1");
		l1.setPutawayQty(46d);
		l1.setLocationId("1");
		l2.setPutawayQty(0d);
		l2.setLocationId("2");
		
		// 部分上架
		detailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		putawayDetail = detailVO.getPutawayDetail();
		listLocation = new ArrayList<RecPutawayLocationVO>();
		locationVO1 = new RecPutawayLocationVO(new RecPutawayLocation());
//		locationVO2 = new RecPutawayLocationVO(new RecPutawayLocation());
		l1 = locationVO1.getPutawayLocation();
//		l2 = locationVO2.getPutawayLocation();
		listSavePutawayDetailVO.add(detailVO);
		detailVO.setListSavePutawayLocationVO(listLocation);
		listLocation.add(locationVO1);
//		listLocation.add(locationVO2);
		putawayDetail.setAsnDetailId("59DB6CD420A243DABEF5B40EAD851471");
		putawayDetail.setOwner("1");
		l1.setPutawayQty(20d);
		l1.setLocationId("6");
		
		
		// ANDY 分两个计划入库，但是上架数量都为0
		
		// ANDY 有上架明细，没有操作明细
		
		
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO, br);
		super.formatResult(rm);
	}
	
	
	/** 分多个库位进行上架,全部上架，全部保存 <br/> add by andy wang */
	protected RecPutawayDetailVO allSplitPtw (RecAsn asn , RecAsnDetail asnDetail , Double[] listQty , String[] listLocationId , InvStock stock ) throws Exception {
		RecPutawayDetailVO detailVO = new RecPutawayDetailVO();
		RecPutawayDetail putawayDetail = detailVO.getPutawayDetail();
		List<RecPutawayLocationVO> listLocation = new ArrayList<RecPutawayLocationVO>();
		detailVO.setListSavePutawayLocationVO(listLocation);

		putawayDetail.setAsnDetailId(asnDetail.getAsnDetailId());
		putawayDetail.setOwner(asn.getOwner());
		
		Double totalPtwQty = 0d;
		
		Double totalPtwWeight = 0d;
		Double totalPtwVolume = 0d;
		for (int i = 0; i < listLocationId.length; i++) {
			String locationId = listLocationId[i];
			Double qty = listQty[i];
			RecPutawayLocationVO locationVO = new RecPutawayLocationVO();
			RecPutawayLocation ptwLoc = locationVO.getPutawayLocation();
			listLocation.add(locationVO);
			ptwLoc.setPutawayQty(qty);
			ptwLoc.setLocationId(locationId);
			Double rate = qty/Double.valueOf(asnDetail.getReceiveQty());
			totalPtwQty += qty;
			if ( asnDetail.getReceiveWeight() != null && asnDetail.getReceiveWeight() > 0 ) {
				Double weight = asnDetail.getReceiveWeight() * rate;
				totalPtwWeight += weight;
				ptwLoc.setPutawayWeight(weight);
			}
			if ( asnDetail.getReceiveVolume() != null && asnDetail.getReceiveVolume() > 0 ) {
				Double volume = asnDetail.getReceiveVolume() * rate;
				totalPtwVolume += volume;
				ptwLoc.setPutawayVolume(volume);
			}
		}
		RecPutawayLocationVO lastPtwLocVO = listLocation.get(listLocation.size() - 1 );
		if ( stock.getStockWeight() == null ) {
			stock.setStockWeight(0d);
		}
		Double surplusWeight = stock.getStockWeight() - totalPtwWeight;
		if ( stock.getStockWeight() <= totalPtwWeight ) {
			surplusWeight = 0d;
		}
		RecUtil.defLocationQWV(lastPtwLocVO.getPutawayLocation());
		lastPtwLocVO.getPutawayLocation().setPutawayWeight(lastPtwLocVO.getPutawayLocation().getPutawayWeight()+surplusWeight);
		if ( stock.getStockVolume() == null ) {
			stock.setStockVolume(0d);
		}
		Double surplusVolume = stock.getStockVolume() - totalPtwVolume;
		if ( stock.getStockVolume() <= totalPtwVolume ) {
			surplusVolume = 0d;
		}
		lastPtwLocVO.getPutawayLocation().setPutawayVolume(lastPtwLocVO.getPutawayLocation().getPutawayVolume()+surplusVolume);
		Double surplusQty = stock.getStockQty()  - totalPtwQty;
		if ( stock.getStockQty() <= totalPtwQty ) {
			surplusQty = 0d;
		}
		lastPtwLocVO.getPutawayLocation().setPutawayQty(lastPtwLocVO.getPutawayLocation().getPutawayQty()+surplusQty);
		return detailVO;
	}
}
