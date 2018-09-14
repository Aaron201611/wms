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
 * 取消ASN单<br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class AsnCancelTest extends AsnTest {

	
	/*
	 	-- 查验结果 - 查询ASN单
	 	select * from rec_asn where asn_id in ('10001','10002')
		
		-- 查验结果 - 查询这次操作新增的异常数据
		select * from exception_log where ex_type='440' and involve_bill in 
		(select asn_no from rec_asn where asn_id in ('10001','10002')) order by ex_log_id2 desc 


	 */
	
	
	
	@Test
	public void test () throws Exception {
		try {
//			this.testCancel();
			this.testCancelNotOpen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 取消打开状态ASN单
	 * @throws Exception
	 * @version 2017年3月21日 上午12:30:59<br/>
	 * @author andy wang<br/>
	 */
	protected void testCancel () throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("10001");
		listAsnId.add("10002");
		ResultModel rm = super.controller.cancel(listAsnId);
		super.formatResult(rm);
	}
	
	/**
	 * 取消非打开状态ASN单
	 * @throws Exception
	 * @version 2017年3月21日 上午12:33:36<br/>
	 * @author andy wang<br/>
	 */
	protected void testCancelNotOpen () throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("10004");
		listAsnId.add("71EC9CEE5F854A3E9B5FE491714354FD");
		ResultModel rm = super.controller.cancel(listAsnId);
		super.formatResult(rm);
	}
	
}
