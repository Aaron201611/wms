/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月17日 下午10:55:07<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.asn;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.DateUtil;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.test.AsnTest;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;


public class AsnUpdateTest extends AsnTest {
	
	/*
	 
 		-- 验证
	 	SELECT * FROM rec_asn WHERE asn_id IN ('8C1D713B603F4381829C439D41F119C3');
		SELECT asn_detail_id,order_qty,order_weight,order_volume,pack_id,batch_no,measure_unit,asn_id 
		FROM rec_asn_detail WHERE asn_id IN ('8C1D713B603F4381829C439D41F119C3') ORDER BY asn_id;
		-- 验证统计
		SELECT SUM(order_qty) AS order_qty ,SUM(order_weight) AS order_weight ,SUM(order_volume) AS order_volume FROM (
		SELECT order_qty,order_weight,order_volume FROM rec_asn a WHERE asn_id = 'D9F05DB290B2495A8551AC967E139979'	
		UNION ALL 
		SELECT SUM(order_qty)*-1 AS order_qty ,SUM(order_weight)*-1 AS order_weight ,SUM(order_volume)*-1 AS order_volume FROM rec_asn_detail b WHERE asn_id = 'D9F05DB290B2495A8551AC967E139979') c

	 	-- 数据
	    INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('38FEE77D071D460286CF7314AB168BB9','8C1D713B603F4381829C439D41F119C3','4',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'4','59','19','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','36',NULL);
		
		INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('69206D1576784583AC5EF8C8791A36A7','8C1D713B603F4381829C439D41F119C3','4',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'39','93','90','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','33',NULL);
		
		INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('7183309EEC25468A93E46F1B97160700','8C1D713B603F4381829C439D41F119C3','2',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'66','49','100','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','29',NULL);
		
		INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('83339549A7C74654B376FEA3E04ADC3A','8C1D713B603F4381829C439D41F119C3','4',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'67','97','23','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','31',NULL);
	    
	    INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('8F6F124F7B4B44ABA361B633ACB5D034','8C1D713B603F4381829C439D41F119C3','3',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'57','100','86','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','28',NULL);
		
		INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('9F873DFA93F2425A941B2226C768D7DA','8C1D713B603F4381829C439D41F119C3','4',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'26','4','88','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','30',NULL);
		
		INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('A1314E2B98904375A30CF367E8F2A153','8C1D713B603F4381829C439D41F119C3','4',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'47','77','4','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','32',NULL);

	    INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('EF12E244A3BC413B87CFD238A360B874','8C1D713B603F4381829C439D41F119C3','4',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'65','6','91','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','34',NULL);
		
		INSERT INTO `rec_asn_detail` (`asn_detail_id`, `asn_id`, `sku_id`, `location_id`, `warehouse_id`, `org_id`, `batch_no`, `measure_unit`, `receive_qty`, `receive_weight`, `receive_volume`, 
		`order_qty`, `order_weight`, `order_volume`, `pack_id`, `produce_date`, `expired_date`, `create_person`, `create_time`, `update_person`, `update_time`, 
		`asn_detail_id2`, `parent_asn_detail_id`) VALUES('FB389F86EF854E6DA3DD123058CDE77F','8C1D713B603F4381829C439D41F119C3','0',NULL,'1','1','21220170218001','包',NULL,NULL,NULL,
		'21','73','28','1','2017-02-14 23:14:00','3919-03-12 00:00:00','1','2017-02-14 23:14:28','1','2017-02-14 23:14:28','35',NULL);
 
 * */
	
	/*
	 	SELECT * FROM rec_asn WHERE asn_id= '5F57C482BB91497C907F6CC1F19FBE0F'

		SELECT * FROM rec_asn_detail WHERE asn_id= '5F57C482BB91497C907F6CC1F19FBE0F'
		
		UPDATE rec_asn_detail SET sku_id = asn_detail_id2 WHERE asn_id= '5F57C482BB91497C907F6CC1F19FBE0F'
		
	 */
	@Test
	public void test () {
		try {
//			this.testUpdate();
//			this.testUpdateSamePoNo();
//			this.testUpdateWithoutDetail();
//			this.testUpdateValidate();
//			this.testUpdateAddSameDetail();
//			this.testUpdateAddExistsDetail();
//			this.testUpdateModifySameDetail();
//			this.testUpdateModifyExistsDetail();
//			this.testUpdateModifyAndAddSameDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 新增与更新相同批次货品的ASN单明细
	 * @throws Exception
	 * @version 2017年3月20日 下午9:11:10<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateModifyAndAddSameDetail() throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setSender("1");
		
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		
		List<String> listUpdateDetailId = recAsnVO.getListUpdateDetailId();
		List<RecAsnDetail> listDetail = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		RecAsnDetail recAsnDetail = listDetail.get(0);
		recAsnDetail.setSkuId("0");
		recAsnDetail.setBatchNo("001");
		
		RecAsnDetail detail = new RecAsnDetail();
		detail.setSkuId("0");
		detail.setBatchNo("001");
		detail.setOrderQty(1d);
		listDetail.add(detail);
		
		listUpdateDetailId.add(recAsnDetail.getAsnDetailId());
		recAsnVO.setListSaveAsnDetail(listDetail);
		
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 更新与原明细单，相同批次货品的ASN单明细
	 * @throws Exception
	 * @version 2017年3月20日 下午8:33:45<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateModifyExistsDetail() throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setSender("1");
		
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		
		List<String> listUpdateDetailId = recAsnVO.getListUpdateDetailId();
		List<RecAsnDetail> listDetail = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		RecAsnDetail recAsnDetail = listDetail.get(0);
		RecAsnDetail recSameAsnDetail = listDetail.get(1);
		recAsnDetail.setSkuId(recSameAsnDetail.getSkuId());
		recAsnDetail.setBatchNo(recSameAsnDetail.getBatchNo());
		
		listUpdateDetailId.add(recAsnDetail.getAsnDetailId());
		recAsnVO.setListSaveAsnDetail(listDetail);
		
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试更新相同批次货品的ASN单明细
	 * @throws Exception
	 * @version 2017年3月20日 下午8:30:12<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateModifySameDetail() throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setSender("1");
		
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		List<String> listUpdateDetailId = recAsnVO.getListUpdateDetailId();
		
		List<RecAsnDetail> listDetail = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		String formatDate = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		
		RecAsnDetail detail1 = listDetail.get(0);
		detail1.setSkuId("F7D901BC084E4369835D1C658DE10305");
		detail1.setBatchNo(formatDate);
		listUpdateDetailId.add(detail1.getAsnDetailId());
		
		RecAsnDetail detail2 = listDetail.get(1);
		detail2.setSkuId("F7D901BC084E4369835D1C658DE10305");
		detail2.setBatchNo(formatDate);
		listUpdateDetailId.add(detail2.getAsnDetailId());
		
		recAsnVO.setListSaveAsnDetail(listDetail);
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试新增与原明细单，相同批次货品的ASN单明细
	 * @throws Exception
	 * @version 2017年3月20日 下午8:28:39<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateAddExistsDetail() throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setSender("1");
		
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		
		List<RecAsnDetail> listDetail = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		RecAsnDetail detail1 = (RecAsnDetail) BeanUtils.cloneBean(listDetail.get(0));
		detail1.setAsnDetailId(null);
		listDetail.add(detail1);
		recAsnVO.setListSaveAsnDetail(listDetail);
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试新增两次相同批次货品的ASN单明细
	 * @throws Exception
	 * @version 2017年3月20日 下午6:50:42<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateAddSameDetail() throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setSender("1");
		
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		
		List<RecAsnDetail> listDetail = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		RecAsnDetail detail1 = (RecAsnDetail) BeanUtils.cloneBean(listDetail.get(0));
		detail1.setAsnDetailId(null);
		detail1.setSkuId("F7D901BC084E4369835D1C658DE10305");
		listDetail.add(detail1);
		RecAsnDetail detail2 = (RecAsnDetail) BeanUtils.cloneBean(listDetail.get(0));
		detail2.setAsnDetailId(null);
		detail2.setSkuId("F7D901BC084E4369835D1C658DE10305");
		listDetail.add(detail2);
		recAsnVO.setListSaveAsnDetail(listDetail);
		
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	/**
	 * 测试字段校验
	 * @throws Exception
	 * @version 2017年3月20日 上午11:09:04<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateValidate() throws Exception {
		String asnId = "5F57C482BB91497C907F6CC1F19FBE0F";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setContactAddress(super.strLen("a", 516));
		recAsn.setContactPerson(super.strLen("a", 65));
		recAsn.setContactPhone(super.strLen("a", 17));
		recAsn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		recAsn.setPoNo(super.strLen("a", 65));
		recAsn.setNote(super.strLen("a", 2049));
		recAsn.setAsnNo1(super.strLen("a", 33));
		recAsn.setAsnNo2(super.strLen("a", 33));
		recAsn.setSender("1");
		
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		
		List<RecAsnDetail> list = recAsnVO.getListSaveAsnDetail();
		
		RecAsnDetail detail = null;
		detail = new RecAsnDetail();
		detail.setExpiredDate(new Date());
		detail.setProduceDate(new Date());
		list.add(detail);
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试只更新ASN单
	 * @throws Exception
	 * @version 2017年3月19日 下午10:02:58<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateWithoutDetail() throws Exception {
		String asnId = "D9F05DB290B2495A8551AC967E139979";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setPoNo("test"+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
	
	/**
	 * 测试相同PO单号
	 * @throws Exception
	 * @version 2017年3月19日 下午10:33:16<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdateSamePoNo() throws Exception {
		String asnId = "16E91DF74B844756BC4750EFA63155A2";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setPoNo("testPO1234123");
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}

	/**
	 * 测试 - 更新Asn单
	 * @throws Exception
	 * @version 2017年2月19日 下午7:40:40<br/>
	 * @author andy wang<br/>
	 */
	public void testUpdate() throws Exception {
		String asnId = "D9F05DB290B2495A8551AC967E139979";
		RecAsn recAsn = this.asnExtService.findAsnById(asnId);
		recAsn.setPoNo("test"+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		recAsn.setOwner("1");
		recAsn.setAsnNo1("test"+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		recAsn.setAsnNo2("test"+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		recAsn.setOrderDate(DateUtil.createDate(2018, 2, 28));
		recAsn.setSender("1");
		recAsn.setContactPerson("testUpdate"+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		recAsn.setContactPhone(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
		recAsn.setContactAddress("testUpdate"+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		recAsn.setDocType(Constant.ASN_DOCTYPE_OTHER);
		recAsn.setAsnStatus(1000);
		RecAsnVO recAsnVO = new RecAsnVO(recAsn);
		
		List<String> listUpdateDetailId = recAsnVO.getListUpdateDetailId();
		List<String> listDelDetailId = recAsnVO.getListDelDetailId();
		List<RecAsnDetail> list = this.asnDetailExtService.listAsnDetailByAsnId(asnId);
		
		
		recAsnVO.setListSaveAsnDetail(list);
		
		for (int i = 0; i < list.size(); i++) {
			RecAsnDetail detail = list.get(i);
			if ( i == 0 ) {
				// 信息更新 - 数量/重量/体积各+1
				detail.setOrderQty(detail.getOrderQty()+1);
				detail.setOrderWeight(detail.getOrderWeight()+1);
				detail.setOrderVolume(detail.getOrderVolume()+1);
				detail.setBatchNo(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")); 
				detail.setMeasureUnit("件");
				listUpdateDetailId.add(detail.getAsnDetailId());
			} else if ( i == 1 ) {
				// 信息删除
				listDelDetailId.add(detail.getAsnDetailId());
			}
		}
		
		// 信息新增 - 
		RecAsnDetail detail = new RecAsnDetail();
		detail.setSkuId("1");
		detail.setOrderQty(99d);
		detail.setOrderWeight(98.5d);
		detail.setOrderVolume(98d);
		detail.setBatchNo("add"+randomBatchNo()); 
		detail.setMeasureUnit("件");
		list.add(detail);
		
		BeanPropertyBindingResult br = super.validateEntity(recAsnVO, ValidUpdate.class);
		ResultModel rm = this.controller.update(recAsnVO, br);
		super.formatResult(rm);
	}
	
}
