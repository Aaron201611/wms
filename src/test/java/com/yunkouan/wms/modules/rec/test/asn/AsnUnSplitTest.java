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

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.test.AsnTest;

/**
 * 取消拆分<br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class AsnUnSplitTest extends AsnTest {
	
	
	
	
	
	@Test
	public void test() {
		try {
//			this.testUnSplit();
//			this.testUnSplitNotOpen();
//			this.testUnSplitNoParent();
//			this.testUnSplitParentNotExists();
			this.testUnSplitChildNotOpen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 取消拆分另一子单状态为非打开状态
	
		-- 测试数据
		select * from rec_asn a WHERE parent_asn_id IS NOT NULL 
		and exists (select 1 from rec_asn b where a.parent_asn_id=b.parent_asn_id and a.asn_id!=b.asn_id and b.asn_status!=10)

	 * @throws Exception
	 * @version 2017年3月21日 下午7:21:20<br/>
	 * @author andy wang<br/>
	 */
	protected void testUnSplitChildNotOpen() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("A0224AE647114AE68422D2F54B856A00");
		ResultModel rm = this.controller.unsplit(listAsnId);
		super.formatResult(rm);
	}
	
	
	
	
	
	/**
	 * 取消拆分父单不存在
		
		-- 测试数据
		SELECT * FROM rec_asn a WHERE parent_asn_id IS NOT NULL AND asn_status=10
		AND NOT EXISTS (SELECT 1 FROM rec_asn b WHERE a.parent_asn_id=b.asn_id)
		
	 * @throws Exception
	 * @version 2017年3月21日 下午7:17:25<br/>
	 * @author andy wang<br/>
	 */
	protected void testUnSplitParentNotExists() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("10002");
		ResultModel rm = this.controller.unsplit(listAsnId);
		super.formatResult(rm);
	}
	
	
	
	/**
	 * 取消拆分非拆分的ASN单
	
		-- 测试数据
		SELECT * FROM rec_asn WHERE parent_asn_id IS NULL AND asn_status=10
		
		-- 查看结果
		SELECT * FROM rec_asn WHERE asn_id = '10001' 
		
	 * @throws Exception
	 * @version 2017年3月21日 下午7:12:53<br/>
	 * @author andy wang<br/>
	 */
	protected void testUnSplitNoParent() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("10001");
		ResultModel rm = this.controller.unsplit(listAsnId);
		super.formatResult(rm);
	}
	
	/**
	 * 取消拆分非打开的ASN单
	
		-- 测试数据
		SELECT * FROM rec_asn WHERE parent_asn_id IS NOT NULL AND asn_status != 10
		
		-- 查看结果
		SELECT * FROM rec_asn WHERE asn_no LIKE '%IN170309000003%'
	
	 * @throws Exception
	 * @version 2017年3月21日 下午7:07:16<br/>
	 * @author andy wang<br/>
	 */
	protected void testUnSplitNotOpen() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("01F918739CCB4ABEBEAB7BC5F3FA4035");
		ResultModel rm = this.controller.unsplit(listAsnId);
		super.formatResult(rm);
	}
	
	/**
	 * 取消拆分ASN单
	
		-- 测试数据
		SELECT * FROM rec_asn WHERE parent_asn_id IS NOT NULL 
		
		-- 查看结果
		SELECT * FROM rec_asn WHERE asn_no LIKE '%IN170309000003%'
	
	 * @throws Exception
	 * @version 2017年3月21日 下午7:05:14<br/>
	 * @author andy wang<br/>
	 */
	protected void testUnSplit() throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("5ABE644788754F1E8F6B022DADAC5CAD");
		ResultModel rm = this.controller.unsplit(listAsnId);
		super.formatResult(rm);
	}
	
}
