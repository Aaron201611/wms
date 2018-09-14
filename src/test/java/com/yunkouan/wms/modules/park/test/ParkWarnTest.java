package com.yunkouan.wms.modules.park.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.park.controller.ParkWarnController;
import com.yunkouan.wms.modules.park.vo.ParkWarnVo;
/**
 * 出租预警测试
 *@Description TODO
 *@author Aaron
 *@date 2017年3月11日 下午2:50:58
 *version v1.0
 */
public class ParkWarnTest extends BaseJunitTest{

	@Autowired
	private ParkWarnController parkWarnController;
	
	/**
	 * 查询分页测试
	 * 
	 * @version 2017年3月11日 下午2:55:04<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_qryList(){
		try {
			ParkWarnVo reqParam = new ParkWarnVo();
//			reqParam.setOrgName("");
			reqParam.setMerchantName("巴巴");
			reqParam.setWarehouseName("311");
			ResultModel rm = parkWarnController.qryList(reqParam);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 查看详情测试
	 * 
	 * @version 2017年3月11日 下午3:00:33<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_view(){
		try {
			ParkWarnVo reqParam = new ParkWarnVo();
			reqParam.getParkWarn().setWarnId("65072AF4B6124B45AB7C7BFA879F7B40");
			ResultModel rm = parkWarnController.view(reqParam);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭测试
	 * 
	 * @version 2017年3月11日 下午3:02:29<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void close(){
		try {
			ParkWarnVo reqParam = new ParkWarnVo();
			reqParam.getParkWarn().setWarnId("65072AF4B6124B45AB7C7BFA879F7B40");
			parkWarnController.close(reqParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
