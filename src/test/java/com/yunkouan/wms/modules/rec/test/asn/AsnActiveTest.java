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
import org.springframework.stereotype.Service;

import com.yunkouan.wms.modules.rec.test.AsnTest;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

/**
 * 生效ASN单<br/><br/>
 * @version 2017年3月21日 上午12:06:35<br/>
 * @author andy wang<br/>
 */
@Service
public class AsnActiveTest extends AsnTest {

	
	/*
	 	-- 查验结果 - 查询ASN单
	  	select * from rec_asn where asn_id in ('1A5A04D7D5C84EC79DD97E24C36F40E0','3A955635BD72423EB4CAF3B8EA7244A5')
	  
	 */
	
	@Test
	public void test () throws Exception {
		try {
			this.testActive();
//			this.testActiveNotOpen();
//			this.testActiveWithoutDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生效无明细ASN单
	  	
	  	-- 测试数据 - 无明细打开状态的ASN单
	 	SELECT * FROM rec_asn a WHERE NOT EXISTS (SELECT 1 FROM rec_asn_detail b WHERE a.asn_id=b.asn_id )
		AND asn_status = 10
		
	 * @throws Exception
	 * @version 2017年3月21日 上午10:33:17<br/>
	 * @author andy wang<br/>
	 */
	protected void testActiveWithoutDetail () throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("1");
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.setTsTaskVo(new TsTaskVo());
		recAsnVO.getTsTaskVo().getTsTask().setOpPerson("abc");
		recAsnVO.setListAsnId(listAsnId);
		super.controller.enable(recAsnVO);
	}
	
	/**
	 * 生效非打开状态ASN单
	  	
	  	-- 测试数据 - 非打开状态的ASN单
		select * from rec_asn a where exists (select 1 from rec_asn_detail b where a.asn_id=b.asn_id )
		and asn_status != 10
		
	 * @throws Exception
	 * @version 2017年3月21日 上午10:25:49<br/>
	 * @author andy wang<br/>
	 */
	protected void testActiveNotOpen () throws Exception {
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add("F0FE2CFBA38B46A1B934A181224BFCB0");
		listAsnId.add("F22F7D0E90FB4AB2BD2EA963441D3BE5");
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.setTsTaskVo(new TsTaskVo());
		recAsnVO.getTsTaskVo().getTsTask().setOpPerson("abc");
		recAsnVO.setListAsnId(listAsnId);
		super.controller.enable(recAsnVO);
	}
	

	/**
	 * 生效打开状态ASN单
	  
	  	-- 测试数据 - 有明细项打开状态的ASN单
	  	select * from rec_asn a where exists (select 1 from rec_asn_detail b where a.asn_id=b.asn_id )
		and asn_status = 10
	  
	 * @throws Exception
	 * @version 2017年3月21日 上午10:11:31<br/>
	 * @author andy wang<br/>
	 */
	protected void testActive () throws Exception {
		this.exec("7C3BC4C86E384C95935DA8AB3BEC1AE9");
	}
	
	public void exec ( String... asnIds ) throws Exception {
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
	
}
