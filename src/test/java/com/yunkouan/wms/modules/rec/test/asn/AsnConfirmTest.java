/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.asn;

import java.util.List;

import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.test.AsnTest;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
@Service
public class AsnConfirmTest extends AsnTest {

	/*
	 
		-- 检验结果
		SELECT * FROM rec_asn WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C'
		SELECT * FROM rec_asn_detail WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C'
		SELECT * FROM inv_stock WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C' ;
		SELECT * FROM inv_log WHERE invoice_bill IN (SELECT asn_no FROM rec_asn WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C')
		SELECT * FROM exception_log WHERE involve_bill IN (SELECT asn_no FROM rec_asn WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C')
		-- 检验结果 - 部分收货才要校验
		SELECT * FROM park_org_busi_stas WHERE doc_no IN (SELECT asn_no FROM rec_asn WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C')
		-- 回滚
		UPDATE rec_asn SET asn_status = 20,receive_qty = NULL ,receive_weight = NULL ,receive_volume = NULL WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C';
		UPDATE rec_asn_detail SET receive_qty = NULL ,receive_weight = NULL ,receive_volume = NULL  
		WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C';
		DELETE FROM inv_stock WHERE asn_id='CF268C68F7DB4422BC6DCDFE4A301D1C' 
	
	
	 */
	
	
	
	
	@Test
	public void test() {
		try {
			this.testConfirm();
//			this.testConfirmPart();
//			this.testConfirmNoOpPerson();
//			this.testConfirmNoLocation();
//			this.testConfirmNotActive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void testConfirmNotActive () throws Exception {
		String asnId = "F8158A247B5D4EBD90B30D0401356524";
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listAsnDetailByAsnId = super.asnDetailExtService.listAsnDetailByAsnId(asnId);
		recAsnVO.setListSaveAsnDetail(listAsnDetailByAsnId);
		for (int i = 0; i < listAsnDetailByAsnId.size(); i++) {
			RecAsnDetail recAsnDetail = listAsnDetailByAsnId.get(i);
			recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty()-1);
			recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight()-2);
			recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume()-3);
		}
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidConfirm.class);
		ResultModel rm = this.controller.complete(recAsnVO , br);
		super.formatResult(rm);
	}
	
	/**
	 * 没有选择收货库位
	 * @throws Exception
	 * @version 2017年3月22日 下午3:30:09<br/>
	 * @author andy wang<br/>
	 */
	protected void testConfirmNoLocation () throws Exception {
		String asnId = "BD452E3B17BC411F81C7439875EE874A";
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listAsnDetailByAsnId = super.asnDetailExtService.listAsnDetailByAsnId(asnId);
		recAsnVO.setListSaveAsnDetail(listAsnDetailByAsnId);
		for (int i = 0; i < listAsnDetailByAsnId.size(); i++) {
			RecAsnDetail recAsnDetail = listAsnDetailByAsnId.get(i);
			recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty()-1);
			recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight()-2);
			recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume()-3);
		}
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidConfirm.class);
		ResultModel rm = this.controller.complete(recAsnVO , br);
		super.formatResult(rm);
	}
	
	/**
	 * 没有选择作业人员
	 * @throws Exception
	 * @version 2017年3月22日 下午3:18:35<br/>
	 * @author andy wang<br/>
	 */
	protected void testConfirmNoOpPerson () throws Exception {
		String asnId = "69BAE2C5353D4206915CA06949D3B5C6";
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listAsnDetailByAsnId = super.asnDetailExtService.listAsnDetailByAsnId(asnId);
		recAsnVO.setListSaveAsnDetail(listAsnDetailByAsnId);
		for (int i = 0; i < listAsnDetailByAsnId.size(); i++) {
			RecAsnDetail recAsnDetail = listAsnDetailByAsnId.get(i);
			recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty()-1);
			recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight()-2);
			recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume()-3);
			recAsnDetail.setLocationId("1");
		}
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidConfirm.class);
		ResultModel rm = this.controller.complete(recAsnVO , br);
		super.formatResult(rm);
	}
	
	
	/**
	 * ASN单部分收货确认
	 * @throws Exception
	 * @version 2017年3月22日 上午11:32:42<br/>
	 * @author andy wang<br/>
	 */
	protected void testConfirmPart () throws Exception {
		String asnId = "E95601FE76C64EF39AB9F1954E2A3BE3";
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		recAsnVO.getAsn().setOpPerson("3");
		
		List<RecAsnDetail> listAsnDetailByAsnId = super.asnDetailExtService.listAsnDetailByAsnId(asnId);
		recAsnVO.setListSaveAsnDetail(listAsnDetailByAsnId);
		for (int i = 0; i < listAsnDetailByAsnId.size(); i++) {
			RecAsnDetail recAsnDetail = listAsnDetailByAsnId.get(i);
			recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty()-1);
			recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight()-2);
			recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume()-3);
			recAsnDetail.setLocationId("1");
		}
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidConfirm.class);
		ResultModel rm = this.controller.complete(recAsnVO , br);
		super.formatResult(rm);
	}
	
	
	/**
	 * ASN单收货确认
		-- 测试数据
		select * from rec_asn where asn_status=20
		
	 * @throws Exception
	 * @version 2017年3月22日 上午8:53:29<br/>
	 * @author andy wang<br/>
	 */
	protected void testConfirm () throws Exception {
		String asnId = "7C3BC4C86E384C95935DA8AB3BEC1AE9";
		this.exec(asnId);
	}
	
	
	public void exec ( String asnId ) throws Exception {
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
	
	
}
