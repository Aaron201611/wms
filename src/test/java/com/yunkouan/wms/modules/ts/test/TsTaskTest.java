package com.yunkouan.wms.modules.ts.test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.ts.service.ITaskService;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

/**
 * 园区出租管理测试
 *@Description TODO
 *@author Aaron
 *@date 2017年3月11日 下午2:32:52
 *version v1.0
 */
public class TsTaskTest extends BaseJunitTest{
	
	
	@Autowired
	private ITaskService taskService;
	
	/**
	 * 测试查询列表
	 * 
	 * @version 2017年3月11日 下午2:32:44<br/>
	 * @author Aaron He<br/>
	 */
	@Test
	public void test_count(){
		try {
			TsTaskVo tsTaskVo = new TsTaskVo();
//			tsTaskVo.getTsTask().setOrgId("6745B3E9F401462F9BBC17EFE305BC94");
//			tsTaskVo.getTsTask().setWarehouseId("5A316A0D6F07440FB469DD319364527A");
			tsTaskVo.setStatusList(Arrays.asList(Constant.TASK_STATUS_OPEN,Constant.TASK_STATUS_EXEC,Constant.TASK_STATUS_CANCEL));
			Map<Integer,Integer> list = taskService.count(tsTaskVo);
			super.formatResult(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
