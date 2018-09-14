package com.yunkouan.wms.modules.rec.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.DateUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;


public class AsnDataTest extends AsnTest  {

	
	
	@Test
	public void testData () throws Exception {
		String asnId = this.insert();
		this.active(asnId);
		this.confirm(asnId);
		System.out.println(asnId);
	}
	
	public void confirm ( String asnId ) throws Exception {
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		recAsnVO.getAsn().setOpPerson("3");
		
		List<RecAsnDetail> listAsnDetailByAsnId = super.asnDetailExtService.listAsnDetailByAsnId(asnId);
		recAsnVO.setListSaveAsnDetail(listAsnDetailByAsnId);
		for (int i = 0; i < listAsnDetailByAsnId.size(); i++) {
			RecAsnDetail recAsnDetail = listAsnDetailByAsnId.get(i);
			recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty());
			recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight());
			recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume());
			recAsnDetail.setLocationId("101");
		}
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidConfirm.class);
		ResultModel rm = this.controller.complete(recAsnVO , br);
		super.formatResult(rm);
	}
	
	
	public void active ( String... asnIds ) throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		for (String asnId : asnIds) {
			listAsnId.add(asnId);
		}
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.setTsTaskVo(new TsTaskVo());
		recAsnVO.getTsTaskVo().getTsTask().setOpPerson("abc");
		recAsnVO.setListAsnId(listAsnId);
		super.controller.enable(recAsnVO);
	}
	
	public String insert () throws Exception {
		RecAsnVO asnVO = new RecAsnVO();
		RecAsn recAsn = asnVO.getAsn();
		List<RecAsnDetail> list = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(list);
		Random r = new Random();
		
		recAsn.setContactAddress("广东省广州市天河区华明路" + r.nextInt(1000) + "号");
		recAsn.setContactPerson(super.getChineseName());
		recAsn.setContactPhone(String.format("131%08d", r.nextInt(10000000)));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setDocType(Constant.ASN_DOCTYPE_RETURNED);
		recAsn.setOrderDate(new Date());
		recAsn.setOwner("1");
		recAsn.setPoNo(r.nextInt(10000000)+"");
		recAsn.setSender("1");

		for (int i = 0; i < 3; i++) {
			RecAsnDetail detail = new RecAsnDetail();
			detail.setBatchNo(DateUtil.formatDate(new Date(), "yyyyMMddHHmmssSSSS")+i);
			detail.setExpiredDate(new Date(2019, 3, 12));
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
		System.out.println(((RecAsnVO)rm.getObj()).getAsn().getAsnId());
		return ((RecAsnVO)rm.getObj()).getAsn().getAsnId();
	}
}
