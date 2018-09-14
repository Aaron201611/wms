package com.yunkouan.wms.modules.send.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 拣货单服务接口
 */
public interface IPickService {

	/**
	 * 查询拣货单列表分页
	 * @param param
	 * @param orgId
	 * @param wareHouseId
	 * @return
	 * @version 2017年2月16日 下午1:24:45<br/>
	 * @author Aaron He<br/>
	 */
	public ResultModel qryPageList(SendPickVo param)throws Exception;
	
	/**
	 * 查询打印分页
	 * @param sendPickVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public ResultModel qryPrintPageList(SendPickVo sendPickVo,Principal p) throws Exception;
	
	/**
	 * 查询拣货单列表
	 * @param param
	 * @return
	 */
	public List<SendPickVo> qryListByParam(SendPickVo param);
	/**
	 * 查询拣货单列表
	 * @param param
	 * @return
	 */
	public List<SendPickVo> qryListByParam2(SendPickVo param);
	
	/**
	 * 保存拣货单并且自动分配库位
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 * @version 2017年2月22日 上午11:40:51<br/>
	 * @author Aaron He<br/>
	 */
	public void SavePickAndAssgn(SendPickVo pickVo,String operator)throws Exception;
	
	/**
	 * 创建拣货单并保存
	 * @param pickVo
	 * @param oprator
	 * @throws Exception
	 * @version 2017年3月3日 下午2:45:21<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo createPickAndSave(SendPickVo pickVo,String pickNo,String oprator)throws Exception;

	
	/**
	 * 保存并生效
	 * @param pickVo
	 * @param pickNo
	 * @param oprator
	 * @return
	 * @throws Exception
	 */
	public SendPickVo saveAndEnable(SendPickVo pickVo,String oprator)throws Exception;
	
	/**
	 * 创建新拣货单
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月21日 下午5:52:34<br/>
	 * @author Aaron He<br/>
	 */
	public void saveNewPick(SendPickVo pickVo,String operator) throws Exception;
	
	/**
	 * 更新拣货单
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月22日 上午10:15:36<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo update(SendPickVo pickVo,String operator)throws Exception;
	
	/**
	 * 根据发货单id查询是否所有的拣货单状态都是打开
	 * @param deliveryId
	 * @return
	 * @version 2017年2月16日 下午2:15:36<br/>
	 * @author Aaron He<br/>
	 */
	public boolean isOpenByDeliveryId(String deliveryId)throws Exception;
	
	/**
	 * 根据条件取消所有拣货单
	 * @param deliveryId
	 * @return
	 * @version 2017年2月16日 下午2:41:48<br/>
	 * @author Aaron He<br/>
	 */
	public int delByParam(Example example) throws Exception;
	
	/**
	 * 批量生效拣货单
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	public void batchEnable(SendPickVo pickVo,String operator) throws Exception;
	/**
	 * 生效拣货单
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:41:55<br/>
	 * @author Aaron He<br/>
	 */
	public int active(String pickId,String operator) throws Exception;
		
	/**
	 * 失效拣货单
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:43:35<br/>
	 * @author Aaron He<br/>
	 */
	public int diable(String pickId,String operator)throws Exception;
	
	/**
	 * 取消拣货单
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:44:42<br/>
	 * @author Aaron He<br/>
	 */
	public int abolish(String pickId,String operator,boolean isAddExcetionLog) throws Exception;
	
	/**
	 * 获取拣货单详情
	 * @param pickId
	 * @return
	 * @version 2017年2月17日 下午3:35:17<br/>
	 * @author Aaron He<br/>
	 */
	public SendPickVo view(String pickId)throws Exception;
	
	
	/**
	 * 作业中
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午7:05:16<br/>
	 * @author Aaron He<br/>
	 */
	public int toWork(String pickId,String operator)throws Exception;
	
	/**
	 * 打印
	 * @param pickId
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public SendPickVo print(String pickId)throws Exception;
	
	/**
	 * 拆分拣货单
	 * @param pickVo
	 * @param orgId
	 * @param wareHouseId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月17日 下午4:29:11<br/>
	 * @author Aaron He<br/>
	 */
	public void splitPick(SendPickVo pickVo,String orgId,String wareHouseId,String operator)throws Exception;
	
	/**
	 * 取消拆分
	 * @param pickVo
	 * @param orgId
	 * @param wareHouseId
	 * @param operator
	 * @throws Exception
	 * @version 2017年2月20日 下午3:19:58<br/>
	 * @author Aaron He<br/>
	 */
	public void removeSplit(String pickId,String operator)throws Exception;
	
	/**
	 * 查询拣货单且自动分配库位
	 * @param pickId
	 * @param operator
	 * @throws Exception
	 * @version 2017年3月3日 下午2:53:01<br/>
	 * @author Aaron He<br/>
	 */
//	public void searchAndAutoAllocate(SendPickVo pickVo,String operator)throws Exception;
	
	/**
	 * 自动分配库位
	 * @param pickDetailVoList
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月20日 下午4:02:41<br/>
	 * @author Aaron He<br/>
	 */
//	public SendPickVo autoAllocateLocation(SendPickVo pickVo,String operator) throws Exception;
	
	/**
	 * 自动分配拣货库位（新）
	 * @param pickVo
	 * @param operator
	 * @return
	 * @throws Exception
	 */
//	public SendPickVo allocation_new(SendPickVo pickVo,String operator) throws Exception;
	
	/**
	 * 手持终端拣货确认
	 * 1、根据库位代码查找库位id
	 * 2、拣货确认
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	public void confirmPick_pda(SendPickVo pickVo,String operator)throws Exception;
	/**
	 * 拣货确认
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 * @version 2017年2月23日 上午11:23:13<br/>
	 * @author Aaron He<br/>
	 */
	public void confirmPick(SendPickVo pickVo,String operator)throws Exception;
	
	/**
	 * 完成拣货（芜湖）
	 * @param pickVo
	 * @param operator
	 * @throws Exception
	 */
	public void completePick(SendPickVo pickVo,String operator)throws Exception;
	
	/**
	 * 根据发货单明细查询拣货数量
	 * @param deliberyDetailIds
	 * @return
	 * @throws Exception
	 * @version 2017年2月23日 下午1:42:11<br/>
	 * @author Aaron He<br/>
	 */
	public  List<SendPickDetail> qryPickQtyByDeliveryDetails(List<String> deliberyDetailIds,String orgId,String warehouseId) throws Exception;
	
	/**
	 * 根据发货明细查询拣货明细包括实际拣货库位列表
	 * @param deliveryDetailIds
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @throws Exception
	 */
	public ResultModel qryPickDetailVosAndRealLoc(List<String> deliveryDetailIds,String orgId,String warehouseId,Boolean hasPage) throws Exception;

	
	/**
	 * 更新拣货单状态
	 * @param pickId
	 * @param operator
	 * @param status
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:49:21<br/>
	 * @author Aaron He<br/>
	 */
	public int updateStatus(String pickId,String operator,int status)throws Exception;
	 
	/**
	 * 根据波次单id查询拣货单列表
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年3月2日 下午4:17:35<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickVo> qryPicksByWaveId(String waveId,String orgId,String warehouseId);
	
	/**
	 * 查询计划拣货货品的拣货单列表
	 * @param detailVo
	 * @return
	 */
	public List<SendPickVo> qryPicksByPlanLocation(SendPickDetailVo param) throws Exception;
	
	/**
	 * 获取拣货单状态
	 * @param pickId
	 * @return
	 * @version 2017年2月16日 下午6:48:19<br/>
	 * @author Aaron He<br/>
	 */
	public int getPickStatus(String pickId);
	
	/**
	 * 更新拣货单为取消状态
	 * @param pick
	 * @param operator
	 * @return
	 */
	public int updateToCancelStatus(SendPick pick,String operator);
	
	/**
	 * 根据条件更新拣货单
	 * @param pick
	 * @param example
	 */
	public void updatePickByExample(SendPick pick,Example example) throws Exception;
	
	/**
	 * 打印拣货单与快递单
	 * 1、检查拣货单状态是否生效
	 * 2、更新拣货单打印次数
	 * 3、更新快递单打印次数
	 * @param sendPickVo
	 * @param p
	 */
	public void printPickAndExpress(List<SendPickVo> pickVoList,Principal p)throws Exception;

	public static final String ASSIS_STATUS_INIT = "0";
	public static final String ASSIS_STATUS_SENDED = "1";
	public static final String ASSIS_STATUS_SUCCESS = "2";
	public static final String ASSIS_STATUS_FAILED = "3";

	/** 
	* @Title: updateAssisStatus 
	* @Description: 更新发送辅助系统状态
	* @auth tphe06
	* @time 2018 2018年7月30日 下午2:14:51
	* @param pickId
	* @param status 发送辅助系统状态，null未发送，1已经发送，2辅助系统返回成功，3辅助系统返回失败
	* @return
	* @throws Exception
	* int
	*/
	public int updateAssisStatus(String pickId, String status) ;
	
	public List<String> getTask(String orgId);

	/** 
	* @Title: queryByDeliveryId 
	* @Description: 根据发货单id查询所有拣货单以及拣货货品和库位详情
	* @auth tphe06
	* @time 2018 2018年8月28日 上午10:57:49
	* @param deliveryId 发货单id
	* @return
	* List<SendPickVo>
	 * @throws Exception 
	*/
	public List<SendPickVo> queryByDeliveryId(String deliveryId) throws Exception;
	public List<SendPickVo> queryByWaveId(String waveId) throws Exception;
}