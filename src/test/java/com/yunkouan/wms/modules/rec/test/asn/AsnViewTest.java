/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月18日 下午8:16:42<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.asn;

import org.junit.Test;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.test.AsnTest;

/**
 * 测试查看ASN<br/><br/>
 * @version 2017年3月18日 下午8:16:42<br/>
 * @author andy wang<br/>
 */
public class AsnViewTest extends AsnTest {
	
	
	@Test
	public void test () {
		try {
			this.testViewAsn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 测试查看ASN单
	 * @throws Exception
	 * @version 2017年3月18日 下午8:19:00<br/>
	 * @author andy wang<br/>
	 */
	public void testViewAsn () throws Exception {
		String asnId = "F22F7D0E90FB4AB2BD2EA963441D3BE5";
		super.line("测试查看ASN单");
		ResultModel rm = super.controller.view(asnId);
		super.formatResult(rm);
	}
	
}
