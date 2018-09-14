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

import com.yunkouan.wms.modules.rec.test.AsnTest;

/**
 * <br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
public class AsnInActiveTest extends AsnTest {
	
	
	public void test() {
		try {
//			this.testInActive();
			this.testInActiveNotActive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	/**
	 * 失效生效状态ASN单
	  
	  	-- 测试数据 - 生效的ASN单
	  	select * from rec_asn where asn_status = 20
	  	-- 校验结果
	  	select * from rec_asn where asn_id in ('10004','1A5A04D7D5C84EC79DD97E24C36F40E0')
	  	
	 * @throws Exception
	 * @version 2017年3月21日 上午10:53:02<br/>
	 * @author andy wang<br/>
	 */
	protected void testInActive () throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("10004");
		listAsnId.add("1A5A04D7D5C84EC79DD97E24C36F40E0");
		super.controller.disable(listAsnId );
	}
	
	/**
	 * 失效非生效状态ASN单
	  
	  	-- 测试数据 - 非生效ASN单
		SELECT * FROM rec_asn WHERE asn_status != 20
	  
	 * @throws Exception
	 * @version 2017年3月21日 上午10:54:01<br/>
	 * @author andy wang<br/>
	 */
	protected void testInActiveNotActive () throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("10001");
		listAsnId.add("10002");
		super.controller.disable(listAsnId );
	}
	
}
