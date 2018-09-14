/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月23日 上午10:30:12<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.test.asn;

import java.io.FileInputStream;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.rec.test.AsnTest;

/**
 * ASN单导入<br/><br/>
 * @version 2017年3月23日 上午10:30:12<br/>
 * @author andy wang<br/>
 */
public class AsnImportTest extends AsnTest {
	
	@Test
	public void test() throws Exception {
		try {
//			this.importAsn();
//			this.importAsnSkuNotExists();
//			this.importAsnSameSku();
			this.importAsnNotSameOwner();
		} catch (Exception e) {
			e.printStackTrace();
			ResultModel rm = new ResultModel();
			rm.setError();
			rm.addMessage(e.getLocalizedMessage());
			super.toJson(rm, true);
		}
	}
	
	
	/**
	 * 同一个源单号，货主不一致
	 * @throws Exception
	 * @version 2017年3月23日 上午11:29:22<br/>
	 * @author andy wang<br/>
	 */
	protected void importAsnNotSameOwner () throws Exception {
		this.impt("Asn_temple_notSameOwner");
	}
	
	/**
	 * 输入相同货主的批次货品代码
	 * @throws Exception
	 * @version 2017年3月23日 上午11:27:20<br/>
	 * @author andy wang<br/>
	 */
	protected void importAsnSameSku () throws Exception {
		this.impt("Asn_temple_sameSku");
	}
	
	/**
	 * 货主的货品代码不存在
	 * @throws Exception
	 * @version 2017年3月23日 上午10:50:09<br/>
	 * @author andy wang<br/>
	 */
	protected void importAsnSkuNotExists () throws Exception {
		this.impt("Asn_temple_skuNotExists");
	}
	
	/**
	 * 导入ASN单
	 * @throws Exception
	 * @version 2017年3月23日 上午10:50:09<br/>
	 * @author andy wang<br/>
	 */
	protected void importAsn () throws Exception {
		this.impt("Asn_temple");
	}
	
	/**
	 * 导入
	 * @param fileName 文件名
	 * @throws Exception
	 * @version 2017年3月23日 上午10:58:37<br/>
	 * @author andy wang<br/>
	 */
	private void impt ( String fileName ) throws Exception {
		final FileInputStream fis = new FileInputStream("E:/" + fileName + ".xls");
		MockMultipartFile multipartFile = new MockMultipartFile(fileName,fileName+".xls","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",fis);
		ResultModel rm = super.controller.upload(multipartFile);
		formatResult(rm);
	}
}
