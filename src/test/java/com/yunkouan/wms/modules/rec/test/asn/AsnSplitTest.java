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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.test.AsnTest;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class AsnSplitTest extends AsnTest {

	
	/*
	  	-- 测试数据
	  	SELECT * FROM rec_asn a where exists (select 1 from rec_asn_detail b where a.asn_id=b.asn_id)
		and asn_status=10 order by asn_id2 desc 
	 
	 */
	
	
	@Test
	public void test() {
		try {
//			this.testSplit();
//			this.testSplitValidate();
//			this.testSplitNotOpen();
//			this.testSplitSameQty();
//			this.testSplitZeroQty();
//			this.testSplit1QtyAllWeight();
//			this.testSplitRateWeight();
//			this.testSplitNegativeQty();
//			this.testSplitNegativeWeightVolume();
//			this.testSplitZeroQty1Weight();
//			this.testSplitOverQty();
			this.testSplitOverWeightVolume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重量/体积超标
	 * @throws Exception
	 * @version 2017年3月21日 下午6:39:13<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitOverWeightVolume () throws Exception {
		String asnId = "42C00341543040E49B1905F0B6CEDA28";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(2d);
		detail.setOrderWeight(detail.getOrderWeight()+1);
		detail.setOrderVolume(detail.getOrderVolume()+1);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 数量超标
	 * @throws Exception
	 * @version 2017年3月21日 下午5:29:02<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitOverQty () throws Exception {
		String asnId = "42C00341543040E49B1905F0B6CEDA28";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(detail.getOrderQty()+1);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	/**
	 * 数量为0，但重量输入
	 * @throws Exception
	 * @version 2017年3月21日 下午5:24:45<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitZeroQty1Weight () throws Exception {
		String asnId = "42C00341543040E49B1905F0B6CEDA28";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(0d);
		detail.setOrderWeight(1d);
		detail.setOrderVolume(2d);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 数量正确输入，重量和体积输入负数
	 * @throws Exception
	 * @version 2017年3月21日 下午5:20:00<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitNegativeWeightVolume () throws Exception {
		String asnId = "42C00341543040E49B1905F0B6CEDA28";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(10d);
		detail.setOrderWeight(-10d);
		detail.setOrderVolume(-10d);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 数量输入负数
	 * @throws Exception
	 * @version 2017年3月21日 下午5:16:27<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitNegativeQty () throws Exception {
		String asnId = "42C00341543040E49B1905F0B6CEDA28";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(-10d);
		detail.setOrderWeight(10d);
		detail.setOrderVolume(10d);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 
	 * @throws Exception
	 * @version 2017年3月21日 下午5:02:32<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitRateWeight () throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(10d);
		detail.setOrderWeight(0d);
		detail.setOrderVolume(0d);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 数量为1，重量全拆分
	 * @throws Exception
	 * @version 2017年3月21日 下午5:00:08<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplit1QtyAllWeight () throws Exception {
		String asnId = "16E91DF74B844756BC4750EFA63155A2";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(1d);
		detail.setOrderWeight(detail.getOrderWeight());
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 拆分明细的数量为0
	 * @throws Exception
	 * @version 2017年3月21日 下午4:46:50<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitZeroQty () throws Exception {
		String asnId = "AF8C4D3AF0FF40D38433E311B04BD3BF";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderQty(0d);
		detail.setOrderWeight(null);
		detail.setOrderVolume(null);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	/**
	 * 拆分明细的数量相等
	 * @throws Exception
	 * @version 2017年3月21日 下午4:32:16<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitSameQty () throws Exception {
		String asnId = "392B06A6981D4170B4DD7FF895843C0F";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		
		List<RecAsnDetail> listSave = new ArrayList<RecAsnDetail>();
		
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		RecAsnDetail detail = list.get(0);
		System.out.println("-->"+detail.getAsnDetailId());
		detail.setOrderWeight(null);
		detail.setOrderVolume(null);
		
		listSave.add(detail);
		
		asnVO.setListSaveAsnDetail(listSave);
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 拆分非打开状态ASN单
	 * @throws Exception
	 * @version 2017年3月21日 下午4:06:16<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitNotOpen () throws Exception {
		String asnId = "3A955635BD72423EB4CAF3B8EA7244A5";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		asnVO.setListSaveAsnDetail(this.asnDetailExtService.listAsnDetailByAsnId(asnId));
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	

	/**
	 * 字段校验
	 * @throws Exception
	 * @version 2017年3月21日 下午4:21:19<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplitValidate () throws Exception {
		String asnId = "76FFFDCAEC0D4B5FBCF6E2D6C60A7042";
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		List<RecAsnDetail> list = super.asnDetailExtService.listAsnDetailByAsnId(asnId);
		asnVO.setListSaveAsnDetail(list);
		for (RecAsnDetail recAsnDetail : list) {
			recAsnDetail.setAsnDetailId(null);
			recAsnDetail.setOrderQty(null);
		}
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 拆分ASN单
	 	
	 	-- 数据验证SQL
	    SELECT asn_id , parent_asn_detail_id , order_qty , order_weight , order_volume , asn_detail_id FROM rec_asn_detail WHERE asn_id IN (SELECT asn_id FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD') ORDER BY parent_asn_detail_id;
		SELECT asn_detail_id, order_qty , order_weight , order_volume FROM rec_asn_detail WHERE asn_id = '71EC9CEE5F854A3E9B5FE491714354FD'
	    SELECT * FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD' OR asn_id='71EC9CEE5F854A3E9B5FE491714354FD'
	 	
	 	-- 恢复SQL
	 	DELETE FROM rec_asn_detail WHERE asn_id IN (SELECT asn_id FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD');
		DELETE FROM rec_asn WHERE parent_asn_id='71EC9CEE5F854A3E9B5FE491714354FD';
		UPDATE rec_asn SET asn_status='10' WHERE asn_id='71EC9CEE5F854A3E9B5FE491714354FD';
	 
	 * @throws Exception
	 * @version 2017年3月21日 上午11:33:52<br/>
	 * @author andy wang<br/>
	 */
	protected void testSplit () throws Exception {
		String asnId = "0171DDB4CF3547E3B128204236D3FE21";
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.getAsn().setAsnId(asnId);
		List<RecAsnDetail> listRecAsnDetail = new ArrayList<RecAsnDetail>();
		asnVO.setListSaveAsnDetail(listRecAsnDetail);
		for (int i = 0; i < list.size(); i++) {
			RecAsnDetail recAsnDetail = list.get(i);
			if ( i == 0 ) {
				recAsnDetail.setOrderQty(recAsnDetail.getOrderQty()/2);
				listRecAsnDetail.add(recAsnDetail);
				break;
			}
		}
		BeanPropertyBindingResult br = super.validateEntity(asnVO, ValidSplit.class);
		ResultModel rm = super.controller.split(asnVO,br);
		super.formatResult(rm);
	}
	
	
	
}
