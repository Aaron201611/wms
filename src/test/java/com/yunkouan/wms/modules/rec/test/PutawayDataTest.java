package com.yunkouan.wms.modules.rec.test;

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
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

public class PutawayDataTest extends PutawayTest {

	@Test
	public void test () throws Exception {
		try {
			String asnId = "82C7EC12146A4794BD31B1E248845220";
			String ptwId = this.insert(asnId);
//			String ptwId = "EB11CE27AEF243409BE93AED1877DD68";
//			this.active(ptwId);
//			this.confirm(ptwId);
			System.out.println("asnId:" + asnId);
			System.out.println("ptwId:" + ptwId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void confirm ( String ptwId ) throws Exception {
		RecPutawayVO vo = new RecPutawayVO();
		vo.getPutaway().setPutawayId(ptwId);
		List<RecPutawayLocation> listLoc = super.ptwLocExtlService.listPutawayLocationByPtwId(ptwId, true);
		List<RecPutawayLocation> listRealLoc = new ArrayList<RecPutawayLocation>();
		vo.setListSavePutawayLocation(listRealLoc);
		for (RecPutawayLocation recPutawayLocation : listLoc) {
			recPutawayLocation.setPutawayLocationId(null);
			recPutawayLocation.setPutawayQty(recPutawayLocation.getPutawayQty()-1);
			listRealLoc.add(recPutawayLocation);
		}
		BeanPropertyBindingResult br = super.validateEntity(vo, RecPutawayVO.class);
		super.controller.complete(vo,br);
	}
	
	
	public void active ( String... ptwIds ) throws Exception {
		List<String> listPutawayId = new ArrayList<String>();
		for (String ptwId : ptwIds) {
			listPutawayId.add(ptwId);
		}
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		recPutawayVO.setListPutawayId(listPutawayId);
		this.controller.enable(recPutawayVO);
	}
	
	
	public String insert ( String asnId ) throws Exception {
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
				Double qty = invStock.getStockQty() ;
				detailVO = this.allSplitPtw(asn, recAsnDetail , new Double[]{qty}, new String[]{"1"},invStock);
				detailVO.getPutawayDetail().setLocationId(invStock.getLocationId());
				listSavePutawayDetailVO.add(detailVO);
			}
		}
		BeanPropertyBindingResult br = super.validateEntity(recPutawayVO, ValidSave.class);
		ResultModel rm = this.controller.add(recPutawayVO,br);
		super.formatResult(rm);
		System.out.println(((RecPutawayVO)rm.getObj()).getPutaway().getPutawayId());
		return ((RecPutawayVO)rm.getObj()).getPutaway().getPutawayId();
	}
	
	
}
