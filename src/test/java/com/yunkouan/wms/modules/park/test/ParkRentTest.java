package com.yunkouan.wms.modules.park.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.park.controller.ParkRentController;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;

/**
 * 园区出租管理测试
 *@Description TODO
 *@author Aaron
 *@date 2017年3月11日 下午2:32:52
 *version v1.0
 */
public class ParkRentTest extends BaseJunitTest{
	
	@Autowired
	private ParkRentController parkRentController;
	
	@Autowired
	private IParkRentService parkRentService;
	
	/**
	 * 测试查询列表
	 * 
	 * @version 2017年3月11日 下午2:32:44<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_qryList(){
		try {
			ParkRentVo vo = new ParkRentVo();
//			vo.setOrgName("te");
			vo.setWarehouseName("311");
			vo.setMerchantName("阿里");
			vo.getParkRent().setRentStatus(Constant.STATUS_OPEN);
			ResultModel rm = parkRentController.qryList(vo);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看详情测试
	 * 
	 * @version 2017年3月11日 下午2:37:41<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_view(){
		try {
			ParkRentVo vo = new ParkRentVo();
			vo.getParkRent().setRentId("DE3196BE0EBE4336952163E3E9206EB3");
			ResultModel rm = parkRentController.view(vo);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增修改测试
	 * 
	 * @version 2017年3月11日 下午2:43:29<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_addAndUpdate(){
		try {
			ParkRentVo reqParam = new ParkRentVo();
			reqParam.getParkRent().setDeposit(500.0);
			reqParam.getParkRent().setEndTime(getDate("20170310"));
			reqParam.getParkRent().setFeeStyle(Constant.PARK_FEE_STYLE_DAY);
			reqParam.getParkRent().setMerchantId("8D8CC8F7B51B41739F4A9F6378CBABB1");
			reqParam.getParkRent().setOrgId("300E30CEE94C47F48B8901C60E1E622C");
			reqParam.getParkRent().setRentMoney(100.0);
			reqParam.getParkRent().setRentStyle(Constant.PARK_RENT_STYLE_CASH);
			reqParam.getParkRent().setSettleCycle(Constant.PARK_SETTLE_CYCLE_MONTH);
			reqParam.getParkRent().setStartTime(getDate("20170101"));
			reqParam.getParkRent().setWarehouseId("B0D64D51BF854FC0A64653EA555B53E6");
			reqParam.getParkRent().setWarnDays(21);
			reqParam.getParkRent().setWarnFrequency(Constant.WARN_FREQUENCY_EVERYWEEK);
			reqParam.getParkRent().setWarnStyle(Constant.WARN_STYLE_SYSTEM);
			
			BeanPropertyBindingResult br = super.validateEntity(reqParam, ValidUpdate.class);
			ResultModel rm = parkRentController.addAndUpdate(reqParam, br);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生效测试
	 * 
	 * @version 2017年3月11日 下午2:49:04<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_enable(){
		try {
			ParkRentVo reqParam = new ParkRentVo();
			reqParam.getParkRent().setRentId("4A7E4F0F68E6487DBEF759896684DE54");
			parkRentController.enable(reqParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 失效测试
	 * 
	 * @version 2017年3月11日 下午2:49:21<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_disable(){
		try {
			ParkRentVo reqParam = new ParkRentVo();
			reqParam.getParkRent().setRentId("DE3196BE0EBE4336952163E3E9206EB3");
			parkRentController.disable(reqParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void warn(){
		try {
			parkRentService.warn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Date getDate(String dateStr) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.parse(dateStr);
	}

}
