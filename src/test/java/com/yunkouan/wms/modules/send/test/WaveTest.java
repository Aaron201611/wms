package com.yunkouan.wms.modules.send.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.send.controller.WaveController;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;

public class WaveTest extends BaseJunitTest{

	@Autowired
	private WaveController waveController;
	
	/**
	 * 查询波次单分页
	 * 
	 * @version 2017年3月6日 下午4:53:38<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_qryPageList(){
		try {
			SendWaveVo reqParam = new SendWaveVo();
			ResultModel rm = waveController.qryPageList(reqParam);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看波次单
	 * 
	 * @version 2017年3月6日 下午5:15:09<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_view(){
		try {
			SendWaveVo reqParam = new SendWaveVo();
			reqParam.getSendWave().setWaveId("3FBC938C11AB468481B2A92E676404BD");
			ResultModel rm = waveController.view(reqParam);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 新增修改测试
	 * 
	 * @version 2017年3月6日 下午3:12:45<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_addAndUpdate(){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 删除发货单
	 * 
	 * @version 2017年3月6日 下午5:18:36<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_delDelivery(){
		try {
			SendWaveVo reqParam = new SendWaveVo();
			reqParam.getSendWave().setWaveId("3FBC938C11AB468481B2A92E676404BD");
			List<SendDeliveryVo> list = new ArrayList<SendDeliveryVo>();
			SendDeliveryVo vo = new SendDeliveryVo();
			vo.getSendDelivery().setDeliveryId("129354318BB14D168DC8993484F259A3");
			list.add(vo);	
			SendDeliveryVo vo2 = new SendDeliveryVo();
			vo2.getSendDelivery().setDeliveryId("B43924C318CF4143BA7B3537F7C603B6");
			list.add(vo2);
			reqParam.setSendDeliberyVoList(list);
			waveController.delDelivery(reqParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_qryNotFinishList(){
		try {
			SendWaveVo reqParam = new SendWaveVo();
			ResultModel rm = waveController.qryNotFinishList(reqParam);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test_active(){
		
	}
	
	public void test_disable(){
		
	}
	
	public void test_abolish(){
		
	}
}
