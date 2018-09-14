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

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.test.PutawayTest;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class PutawayUpdateTest extends PutawayTest {
	
	
	@Test
	public void test() throws Exception {
		try {
//			this.testUpdate();
//			this.testUpdateNoDetail();
			this.testUpdateSameAsnDetail();
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
	 * @version 2017年3月24日 下午8:41:54<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertMultipleASN () throws Exception {
		
	}
	
	/**
	 * 同一个ASN单明细，分多个库位上架，但总数量超过收货数量
	 * @throws Exception
	 * @version 2017年3月24日 下午8:41:47<br/>
	 * @author andy wang<br/>
	 */
	protected void testInsertOneAsnDetailMultipleLocation () throws Exception {
		
	}
	
	/**
	 * 上架数量大于ASN单明细收货数量
	 * @throws Exception
	 * @version 2017年3月24日 下午8:41:40<br/>
	 * @author andy wang<br/>
	 */
	protected void testUpdateOverQty () throws Exception {
		
	}
	
	/**
	 * 字段校验
	 * @throws Exception
	 * @version 2017年3月24日 下午8:41:35<br/>
	 * @author andy wang<br/>
	 */
	protected void testUpdateValidate () throws Exception {
		
	}
	
	/**
	 * 上架相同的ASN单明细
	 * @throws Exception
	 * @version 2017年3月24日 下午8:41:29<br/>
	 * @author andy wang<br/>
	 */
	protected void testUpdateSameAsnDetail () throws Exception {
		String ptwId = "63E424771ECB4457848EF3FBECD075D3";
		RecPutaway ptw = super.ptwExtlService.findPutawayById(ptwId);
		RecPutawayVO ptwVO = new RecPutawayVO(ptw);
		List<RecPutawayDetail> listDetail = super.ptwDetailExtlService.listPutawayDetailByPtwId(ptwId);
		List<RecPutawayDetailVO> listSavePutawayDetailVO = ptwVO.getListSavePutawayDetailVO();
		for (int i = 0; i < listDetail.size(); i++) {
			if ( i > 0 ) {
				continue;
			}
			RecPutawayDetail recPutawayDetail = listDetail.get(i);
			String asnDetailId = recPutawayDetail.getAsnDetailId();
			RecAsnDetail asnDetail = super.asnDetailExtlService.findByDetailId(asnDetailId);
			RecAsn asn = super.asnExtlService.findAsnById(asnDetail.getAsnId());
			
			InvStockVO stockVo = new InvStockVO( new InvStock() );
			stockVo.setContainTemp(true);
			stockVo.getInvStock().setAsnDetailId(asnDetail.getAsnDetailId());
			List<InvStock> list = super.stockService.list(stockVo );
			for (int j = 0; j < list.size(); j++) {
				if ( j > 0 ) {
					continue;
				}
				InvStock invStock = list.get(j);
				Double qty = invStock.getStockQty() / 3;
				RecPutawayDetailVO detailVO = this.allSplitPtw(asn, asnDetail , new Double[]{qty,qty,qty}, new String[]{"1","2","3"},invStock);
				detailVO.getPutawayDetail().setLocationId(invStock.getLocationId());
				listSavePutawayDetailVO.add(detailVO);
			}
		}
		BeanPropertyBindingResult br = super.validateEntity(ptwVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(ptwVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 没有上架明细
	 * @throws Exception
	 * @version 2017年3月24日 下午8:41:22<br/>
	 * @author andy wang<br/>
	 */
	protected void testUpdateNoDetail () throws Exception {
		String ptwId = "6504A541E39949B890134BA5F39D5206";
		RecPutaway ptw = super.ptwExtlService.findPutawayById(ptwId);
		RecPutawayVO ptwVO = new RecPutawayVO(ptw);
		List<RecPutawayDetail> listDetail = super.ptwDetailExtlService.listPutawayDetailByPtwId(ptwId);
		List<String> listRemovePutawayDetail = ptwVO.getListRemovePutawayDetail();
		for (RecPutawayDetail ptwDetail : listDetail) {
			listRemovePutawayDetail.add(ptwDetail.getPutawayDetailId());
		}
		BeanPropertyBindingResult br = super.validateEntity(ptwVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(ptwVO,br);
		super.formatResult(rm);
	}
	
	/**
	 * 更新上架单
	 	
	 	-- 结果查询
	 	
		SELECT * FROM rec_putaway ORDER BY putaway_id2 DESC ;
		SELECT * FROM rec_putaway_detail ORDER BY putaway_detail_id2 DESC ;
		SELECT * FROM rec_putaway_location ORDER BY putaway_location_id2 DESC 
	 	
	 	SELECT SUM(plan_putaway_qty) ,SUM(plan_putaway_weight) ,SUM(plan_putaway_volume) FROM rec_putaway_detail UNION ALL 
		SELECT plan_qty,plan_weight,plan_volume FROM rec_putaway ;
		
		SELECT 1 AS flag , putaway_detail_id, SUM(putaway_qty) , SUM(putaway_weight),SUM(putaway_volume) FROM rec_putaway_location GROUP BY putaway_detail_id UNION ALL
		SELECT 2 AS flag , putaway_detail_id , plan_putaway_qty, plan_putaway_weight, plan_putaway_volume FROM rec_putaway_detail 
		 

	 	
	 	-- 数据清空
		DELETE FROM rec_putaway_location ;
		DELETE FROM rec_putaway_detail ;
		DELETE FROM rec_putaway ;

	 * 
	 * @throws Exception
	 * @version 2017年3月24日 下午8:36:09<br/>
	 * @author andy wang<br/>
	 */
	protected void testUpdate () throws Exception {
		String ptwId = "6504A541E39949B890134BA5F39D5206";
		RecPutaway ptw = super.ptwExtlService.findPutawayById(ptwId);
		RecPutawayVO ptwVO = new RecPutawayVO(ptw);
		List<RecPutawayDetail> listDetail = super.ptwDetailExtlService.listPutawayDetailByPtwId(ptwId);
		List<RecPutawayDetailVO> listSavePutawayDetailVO = ptwVO.getListSavePutawayDetailVO();
		List<String> listRemovePutawayLocation = ptwVO.getListRemovePutawayLocation();
		List<String> mapUpdatePutawayLocation = ptwVO.getListUpdatePutawayLocation();
		for (int j = 0; j < listDetail.size(); j++) {
			RecPutawayDetail recPutawayDetail = listDetail.get(j);
			List<RecPutawayLocation> listPtwLoc = super.ptwLocExtlService.listPtwLocByPtwDetailId(recPutawayDetail.getPutawayDetailId(), true);
			if ( listPtwLoc == null ) {
				continue;
			}
			RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO(recPutawayDetail);
			listSavePutawayDetailVO.add(recPutawayDetailVO);
			List<RecPutawayLocationVO> listPtwLocVO = recPutawayDetailVO.getListSavePutawayLocationVO();
			
			for (int i = 0; i < listPtwLoc.size(); i++) {
				RecPutawayLocation ptwLoc = listPtwLoc.get(i);
				if ( i == 0 ) {
					// 修改
					ptwLoc.setPutawayQty(ptwLoc.getPutawayQty()+1);
					ptwLoc.setPutawayWeight(ptwLoc.getPutawayWeight()+1);
					ptwLoc.setPutawayVolume(ptwLoc.getPutawayVolume()+1);
					mapUpdatePutawayLocation.add(ptwLoc.getPutawayLocationId());
				} else if ( j == 1 && i == 1 ) {
					// 删除
					listRemovePutawayLocation.add(ptwLoc.getPutawayLocationId());
					continue;
				}
				listPtwLocVO.add(new RecPutawayLocationVO(ptwLoc));
			}
			
		}
		String asnDetailId = "419D4AAAC5F84DFB9FCB2D79DED01443";
		RecAsnDetail asnDetail = super.asnDetailExtlService.findByDetailId(asnDetailId);
		RecAsn asn = super.asnExtlService.findAsnById(asnDetail.getAsnId());
		
		InvStockVO stockVo = new InvStockVO( new InvStock() );
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setAsnDetailId(asnDetail.getAsnDetailId());
		List<InvStock> list = super.stockService.list(stockVo );
		for (int j = 0; j < list.size(); j++) {
			if ( j > 0 ) {
				continue;
			}
			InvStock invStock = list.get(j);
			Double qty = invStock.getStockQty() / 3;
			RecPutawayDetailVO detailVO = this.allSplitPtw(asn, asnDetail , new Double[]{qty,qty,qty}, new String[]{"1","2","3"},invStock);
			detailVO.getPutawayDetail().setLocationId(invStock.getLocationId());
			listSavePutawayDetailVO.add(detailVO);
		}
		
		
		BeanPropertyBindingResult br = super.validateEntity(ptwVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(ptwVO,br);
		super.formatResult(rm);
	}
	
}
