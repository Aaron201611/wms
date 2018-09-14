package com.yunkouan.wms.modules.send.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.send.controller.PickController;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

public class PickTest extends BaseJunitTest{

	@Autowired
	private PickController pickController;
	
	@Autowired
	private IPickService pickService;
	
	/**
	 * 新增修改测试
	 * 
	 * @version 2017年2月28日 下午4:37:26<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void testAddAndUpdate(){
		SendPickVo pickVo = new SendPickVo();
		SendPick pick = new SendPick();		
		pick.setDeliveryId("3F85CFC04CD740F78EBE7957B055FF78");
		
		SendPickDetailVo detailVo = new SendPickDetailVo();
		detailVo.getSendPickDetail().setDeliveryDetailId("B0275BD5C1624CBCA0A11DA9779C8F44");
		SendPickLocationVo locationVo = new SendPickLocationVo();
		locationVo.getSendPickLocation().setLocationId("011B5A9B79AD4B2AA4C3DFCD99EAF7F8");
		detailVo.getPlanPickLocations().add(locationVo);
		
		SendPickDetailVo detailVo2 = new SendPickDetailVo();
		detailVo2.getSendPickDetail().setDeliveryDetailId("DE8F51792BC54FB5B3FF21CF9E2FE2A7");
		SendPickLocationVo locationVo2 = new SendPickLocationVo();
		locationVo2.getSendPickLocation().setLocationId("08A4CE11070B4A51BE1CC087CD68EE57");
		detailVo2.getPlanPickLocations().add(locationVo2);
		
		SendPickDetailVo detailVo3 = new SendPickDetailVo();
		detailVo3.getSendPickDetail().setDeliveryDetailId("DF4D3D47FD264F6C830E6E5892DBD06C");
		SendPickLocationVo locationVo3 = new SendPickLocationVo();
		locationVo3.getSendPickLocation().setLocationId("09CB1B16BA1E41548ECE633B7EC44374");
		detailVo3.getPlanPickLocations().add(locationVo3);	
		
		pickVo.setSendPick(pick);
		pickVo.getSendPickDetailVoList().add(detailVo);
		pickVo.getSendPickDetailVoList().add(detailVo2);
		pickVo.getSendPickDetailVoList().add(detailVo3);
		try {
			ResultModel rm = pickController.addAndUpdate(pickVo, null);
			super.formatResult(rm);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 查询列表测试
	 * 
	 * @version 2017年2月28日 下午4:38:17<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_qryList(){
		SendPickVo pickVo = new SendPickVo();
		SendPick pick = new SendPick();
		pick.setOwner("阿里");
		pickVo.setDeliveryNo("ON170315000010");
//		pick.setWaveNo();
//		pick.setPickStatus();
		pickVo.setSendPick(pick);
		try {
			ResultModel rm = pickController.qryList(pickVo);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 拣货单查看测试
	 * 
	 * @version 2017年2月28日 下午5:28:24<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_view(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
			ResultModel rm = pickController.view(pickVo);
			super.formatResult(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 自动分配拣货库位测试
	 * 
	 * @version 2017年2月28日 下午5:30:03<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_autoAllocate(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
			pickController.autoAllocate(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生效测试
	 * 
	 * @version 2017年2月28日 下午5:30:23<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_active(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
			pickController.enable(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 失效测试
	 * 
	 * @version 2017年2月28日 下午5:30:56<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_disable(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
			pickController.disable(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取消测试
	 * 
	 * @version 2017年2月28日 下午5:31:31<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_abolish(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
			pickController.cancel(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拣货单打印
	 * 
	 * @version 2017年2月28日 下午5:32:01<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_print(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
//			pickController.print(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拣货单拆分测试
	 * 
	 * @version 2017年2月28日 下午5:32:39<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_split(){
		try {
			SendPickVo sdVo = new SendPickVo();
			SendPick entity = new SendPick();
			
			entity.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			
			SendPickDetailVo vo = new SendPickDetailVo();
			SendPickDetail detail1 = new SendPickDetail();
			detail1.setPickDetailId("444BB174F27145A2B482EF50EF380B8F");
			detail1.setOrderQty(12d);//35 30 20	
			detail1.setOrderWeight(10.0);
			detail1.setOrderVolume(0.0);
			vo.setSendPickDetail(detail1);
			sdVo.getSendPickDetailVoList().add(vo);
			
//			vo = new SendPickDetailVo();
//			SendPickDetail detail2 = new SendPickDetail();
//			detail2.setPickDetailId("A47416BB49BA409FA2A48AFA07C10DA8");
//			detail2.setOrderQty(21);//29 null null
//			detail2.setOrderWeight(0.0);
//			detail2.setOrderVolume(0.0);
//			vo.setSendPickDetail(detail2);
//			sdVo.getSendPickDetailVoList().add(vo);
				
			vo = new SendPickDetailVo();
			SendPickDetail detail3 = new SendPickDetail();
			detail3.setPickDetailId("A6FAA4CFAE1D41B6B51BF0E31AEA8165");
			detail3.setOrderQty(10d);//18 18 10
			detail3.setOrderWeight(0.0);
			detail3.setOrderVolume(0.0);
			vo.setSendPickDetail(detail3);
			sdVo.getSendPickDetailVoList().add(vo);
			
			sdVo.setSendPick(entity);
			pickController.split(sdVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取消拆分测试
	 * 
	 * @version 2017年2月28日 下午5:49:32<br/>
	 * @author Aaron He<br/>
	 */
	public void test_removeSplit(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("556213C7E01F4CB9A807831B516F6E7E");
			pickVo.setSendPick(pick);
			pickController.removeSplit(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拣货确认测试
	 * 
	 * @version 2017年2月28日 下午5:49:46<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_confirm(){
		try {
			SendPickVo pickVo = new SendPickVo();
			SendPick pick = new SendPick();
			pick.setPickId("468F1A5270624495B56F92FEFF69FDD0");
			pickVo.setSendPick(pick);
			
			//detailVo1
			SendPickDetailVo detailVo1 = new SendPickDetailVo();
			detailVo1.getSendPickDetail().setPickDetailId("444BB174F27145A2B482EF50EF380B8F");
			//location1
			SendPickLocationVo locationVo1 = new SendPickLocationVo();
			locationVo1.getSendPickLocation().setLocationId("08E56F2C3DDA46C28A9E2B13ECBF4003");
			locationVo1.getSendPickLocation().setPickQty(20d);					
			//location4
//			SendPickLocationVo locationVo4 = new SendPickLocationVo();
//			locationVo4.getSendPickLocation().setLocationId("9E96204C93A44DCBA75795867CA21517");
//			locationVo4.getSendPickLocation().setPickQty(8);
			
			detailVo1.getRealPickLocations().add(locationVo1);
//			detailVo1.getRealPickLocations().add(locationVo4);
			
			
			//detailVo2
			SendPickDetailVo detailVo2 = new SendPickDetailVo();
			detailVo2.getSendPickDetail().setPickDetailId("A6FAA4CFAE1D41B6B51BF0E31AEA8165");
			//location2			
			SendPickLocationVo locationVo2 = new SendPickLocationVo();
			locationVo2.getSendPickLocation().setLocationId("26078A3C0DC74A6CB53B4892E4C6C66F");
			locationVo2.getSendPickLocation().setPickQty(10d);
			
			detailVo2.getRealPickLocations().add(locationVo2);
			
			
			//detailVo3
			SendPickDetailVo detailVo3 = new SendPickDetailVo();
			detailVo3.getSendPickDetail().setPickDetailId("A47416BB49BA409FA2A48AFA07C10DA8");
			//location3
			SendPickLocationVo locationVo3 = new SendPickLocationVo();
			locationVo3.getSendPickLocation().setLocationId("9586EA2ACEFC483E9709A38990CB4C2C");
			locationVo3.getSendPickLocation().setPickQty(20d);
		
			detailVo3.getRealPickLocations().add(locationVo3);
			
			pickVo.getSendPickDetailVoList().add(detailVo1);
			pickVo.getSendPickDetailVoList().add(detailVo2);
			pickVo.getSendPickDetailVoList().add(detailVo3);
		
			
			pickController.confirm(pickVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询计划拣货货品的拣货单列表测试
	 */
	@Test
	public void test_qryPicksByPlanLocation(){
		try {
			SendPickDetailVo vo = new SendPickDetailVo();
			vo.getSendPickDetail().setSkuId("DCC7888941114A1D91EDE09E8ADE47F4");
			vo.getSendPickDetail().setBatchNo("1");
			SendPickLocationVo loVo = new SendPickLocationVo();
			loVo.getSendPickLocation().setLocationId("762D92667AD44635BF4B4C094CFCCDEE");
			vo.getPlanPickLocations().add(loVo);
			
			List<SendPickVo> list = pickService.qryPicksByPlanLocation(vo);
			super.formatResult(list);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	

}
