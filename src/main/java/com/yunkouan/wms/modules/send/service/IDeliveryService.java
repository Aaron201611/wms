package com.yunkouan.wms.modules.send.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDelivery2ExternalVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

/**
 * 发货单服务接口
 */
public interface IDeliveryService {
	/** 
	* @Title: updateSendStatus 
	* @Description: 更新发货单状态、大字、集包地
	* @auth tphe06
	* @time 2018 2018年6月5日 上午10:23:29
	* @param deliveryId 发货单id
	* @param sendStatus 发送状态
	* @param bigchar 大字
	* @param gather_place 集包地
	* @return
	* int
	*/
	public int updateSendStatus(String deliveryId, int sendStatus, String bigchar, String gather_place);
	/**
	 * 更新运单推送状态
	 */
	public int updateSendStatus(String deliveryId,int sendStatus);
	/**
	 * 检查源单号是否重复
	 * @param srcNo
	 * @param orgId
	 */
	public void checkSrcNo(String srcNo,String orgId,List<String> deliveryIds);

	/**
	 * 创建发货单号，新增发货单
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public SendDeliveryVo createNoAndAdd(SendDeliveryVo sdVo,String orgId,String wareHouseId,String userId)throws Exception;
	/**
	 * 新增发货单
	 * @param entity
	 * @return
	 */
	public SendDeliveryVo add(SendDeliveryVo sendDeliveryVo,String orgId,String wareHouseId,String userId)throws Exception;
	
	/**
	 * 保存并生效发货单
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public SendDeliveryVo saveAndEnable(SendDeliveryVo sdVo,String orgId,String wareHouseId,String userId)throws Exception;
	
	/**
	 * 添加发货明细
	 * @param details
	 * @param deliveryId
	 * @param orgId
	 * @param wareHouseId
	 */	
	public int addDeliveryDetails(List<DeliveryDetailVo> details,String deliveryId,String orgId,String wareHouseId,String operator)throws Exception;
	
	/**
	 * 更新发货明细
	 * @param details
	 * @param deliveryId
	 * @version 2017年2月14日 下午1:30:51<br/>
	 * @author Aaron He<br/>
	 */
	public void updateDetails(List<DeliveryDetailVo> details,String deliveryId,String orgId,String wareHouseId,String operator)throws Exception;
	
	/**
	 * 更新发货单
	 * @param sdVo
	 * @return
	 */
	public SendDeliveryVo update(SendDeliveryVo sdVo,String orgId,String wareHouseId,String operator)throws Exception;
	
	/**
	 * 更新发货单
	 * @param entity
	 * @return
	 */
	public int updateEntity(SendDelivery entity,Principal p)throws Exception;
	/**
	 * 更新实体并生效
	 * @param entity
	 * @param p
	 * @throws Exception
	 */
	public void updateEntityAndEnable(SendDelivery entity,Principal p)throws Exception;
	/**
	 * 查询发货单分页
	 * @param sdVo
	 * @return
	 */
	public ResultModel qryPageList(SendDeliveryVo sdVo) throws Exception;
	
	/**
	 * 查询发货单列表
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @return
	 */
	public List<SendDeliveryVo> qryListByParam(SendDeliveryVo sendDeliveryVo);
	
	/**
	 * 取消发货单
	 * @param deliveryId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年2月14日 下午3:07:31<br/>
	 * @author Aaron He<br/>
	 */
	public int cancelSendDelivery(String deliveryId,String operator)throws Exception;
	
	/**
	 * 生效发货单,检查是否关联波次单
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月14日 下午5:14:29<br/>
	 * @author Aaron He<br/>
	 */
	public int enableCheckWave(String deliveryId,String operator)throws Exception;
	
	/**
	 * 生效发货单，仅供波次单生效
	 * @param deliveryId
	 * @param operator
	 * @version 2017年3月1日 下午8:20:24<br/>
	 * @author Aaron He<br/>
	 */
	public int enable(String deliveryId,String operator)throws Exception;
	
	/**
	 * 生效并自动生成拣货单
	 * @param deliveryId
	 * @param operator
	 * @throws Exception
	 */
	public void enableAndCreatePick(String deliveryId,String operator)throws Exception;
	
	/**
	 * 保存并生效发货单，自动生成拣货单
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @param userId
	 * @throws Exception
	 */
	public SendDeliveryVo saveAndEnableAndCreatePick(SendDeliveryVo sdVo,String orgId,String wareHouseId,String userId)throws Exception;
	
	/**
	 * 从发货单创建拣货单且保存分配库位
	 * @param deliveryId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月3日 上午11:21:18<br/>
	 * @author Aaron He<br/>
	 */
	public void createPickAndAssign(String deliveryId,String operator)throws Exception;
	
	/**
	 * 失效发货单
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 下午5:53:28<br/>
	 * @author Aaron He<br/>
	 */
	public int disableSendDelivery(String deliveryId,String orgId,String warehouseId,String operator)throws Exception;
	
	/**
	 * 根据发货id查询发货明细列表
	 * @param deliveryId
	 * @return
	 * @version 2017年2月15日 下午1:17:52<br/>
	 * @author Aaron He<br/>
	 */
	public List<DeliveryDetailVo> getDetailsByDeliveryId(String deliveryId);
	
	/**
	 * 批量获取所有发货单id的发货明细列表
	 * @param deliveryIds
	 * @return
	 */
	public List<DeliveryDetailVo> getDetailsByDeliveryIds(List<String> deliveryIds);
	
	/**
	 * 查看发货单
	 * @param deliveryId
	 * @return
	 * @version 2017年2月14日 下午5:23:19<br/>
	 * @author Aaron He<br/>
	 */
	public SendDeliveryVo view(String deliveryId)throws Exception;
	
	/**
	 * 根据id获取发货单
	 * @param deliveryId
	 * @return
	 * @version 2017年3月11日 下午1:17:48<br/>
	 * @author Aaron He<br/>
	 */
	public SendDeliveryVo getDeliveryById(String deliveryId);
	
	/**
	 * 根据运单号查询发货单
	 * @param expressBillNo
	 * @return
	 */
	public SendDelivery getByExpressBillNo(String expressBillNo);
	
	/**
	 * 拆分发货单
	 * @param sdVo
	 * @param operator
	 * @return
	 * @version 2017年2月14日 下午5:40:59<br/>
	 * @author Aaron He<br/>
	 */
	public void splitDelivery(SendDeliveryVo sdVo,String orgId,String wareHouseId,String operator) throws Exception;
	
	/**
	 * 更新发货单状态
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月15日 下午6:05:32<br/>
	 * @author Aaron He<br/>
	 */
	public int updateStatus(String deliveryId,String operator,int status);
	
	/**
	 * 根据波次单号更新状态
	 * @param param
	 * @version 2017年3月2日 下午5:46:45<br/>
	 * @author Aaron He<br/>
	 */
	public void updateStatusByParam(int status,String operator,SendDeliveryVo param) throws Exception;
	
	/**
	 * 装车确认
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月14日 下午6:33:16<br/>
	 * @author Aaron He<br/>
	 */
	public void loadConfirm(SendDeliveryVo sdVo) throws Exception;
	
	/**
	 * 更新发货单拣货结果
	 * @param deliberyDetailId
	 * @param pickQty
	 * @throws Exception
	 * @version 2017年2月23日 下午1:31:20<br/>
	 * @author Aaron He<br/>
	 */
	public SendDeliveryVo updateAfterConfirmPick(String deliberyDetailId,String operator) throws Exception;
	
	/**
	 * 拣货确认更新发货单
	 * @param sendPickVo
	 * @param userId
	 * @throws Exception
	 */
	public void updateAfterCompletePick(SendPickVo sendPickVo,String userId) throws Exception;
	/**
	 * 登记到企业业务统计
	 * @param deliveryVo
	 * @throws Exception
	 * @version 2017年3月11日 上午11:11:28<br/>
	 * @author Aaron He<br/>
	 */
	public void addOrgBusiStas(SendDeliveryVo deliveryVo,String operator) throws Exception;
	
	/**
	 * 根据id列表批量查询发货单
	 * @param idList
	 * @return
	 * @version 2017年3月10日 下午5:48:05<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryByIds(List<String> idList)  throws Exception;
	
	/**
	 * 查询没有完成拣货，且没有关联波次单的发货单列表
	 * @param orgId
	 * @param warehouseId
	 * @version 2017年3月7日 上午10:39:02<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryListNotFinish(String orgId,String warehouseId);
	
	
	/**
	 * 按条件查询关联了该波次单与没有关联波次单的发货单列表
	 * @param sdVo
	 * @return
	 * @version 2017年3月1日 下午1:43:48<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryListInWave(SendDeliveryVo param);
	
	/**
	 * 更新波次单id
	 * @param deliveryId
	 * @param waveId
	 * @version 2017年3月1日 下午4:32:40<br/>
	 * @author Aaron He<br/>
	 */
	public void updateWaveId(SendDelivery entity);
	
	/**
	 * 批量更新波次单号
	 * @param waveId
	 * @param deliveryIds
	 * @param status
	 * @version 2017年3月1日 下午5:33:39<br/>
	 * @author Aaron He<br/>
	 */
	public void batchUpdateWaveId(String waveId,List<String> deliveryIds,int deliveryStatus,String operator)throws Exception;
	
	/**
	 * 查询发货单列表
	 * @param param
	 * @return
	 * @version 2017年3月1日 下午7:38:30<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryList(SendDelivery param);
	
	/**
	 * 查询发货单列表
	 * @param param
	 * @return
	 * @version 2017年3月1日 下午7:38:30<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryList2(SendDelivery param);
	
	public List<String>getTask(String orgId);
	/**
	 * 删除波次单号
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月2日 下午1:31:01<br/>
	 * @author Aaron He<br/>
	 */
	public void delWaveId(String waveId,String orgId,String warehouseId,String operator)throws Exception;
	
	/**
	 * 根据单号查询
	 * @param deliveryNo
	 * @version 2017年3月7日 下午6:34:25<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDelivery> qryByDeliveryNo(String deliveryNo,String orgId,String warehouseId);
	
	/**
	 * 根据发货明细id查询实际拣货库位
	 * @param detailId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public List<SendPickLocationVo> qryRealLocationByDetailId(String detailId,String orgId,String warehouseId) throws Exception;
	
	/**
	 * 根据发货明细id查询计划拣货库位
	 * @param detailId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public List<SendPickLocationVo> qryPlanLocationByDetailId(String detailId,String orgId,String warehouseId) throws Exception;
	
	/**
	 * 查询发货单状态
	 * @param deliveryId
	 * @param status
	 * @return
	 * @version 2017年2月14日 下午3:17:26<br/>
	 * @author Aaron He<br/>
	 */
	public int qryDeliveryStatus(String deliveryId);
	
	/**
	 * 检查发货单状态并获取发货单
	 * @param id
	 * @param isStatus
	 * @return
	 */
	public SendDeliveryVo checkStatusAndgetDelivery(SendDeliveryVo sendDeliveryVo,Integer isStatus) throws Exception;
	
	/**
	 * 打包复核
	 * @param deliveryVo
	 * @param userId
	 * @throws Exception
	 */
	public void reviewAndPackage(SendDeliveryVo deliveryVo,String userId) throws Exception;
	
	/**
	 * 打包
	 * @param deliveryVo
	 * @param userId
	 * @throws Exception
	 */
	public void packaged(SendDeliveryVo deliveryVo,String userId) throws Exception;
	
	/**
	 * 称重复核
	 * @param sendDeliveryVo
	 * @param p
	 */
	public void checkPackageWeight(SendDeliveryVo sendDeliveryVo,Principal p) throws Exception;
	
	/**
	 * 导入发货单
	 * @param entity
	 * @return
	 */
	public void importDeliveryToInterface(SendDelivery2ExternalVo sendDeliveryVo2)throws Exception;
	
	/**
	 * 外部接口取消订单
	 * @param deliveryId
	 * @return
	 */
	public Map<String,String> cancelToInterface(String deliveryId,String cancelFlag) throws Exception;
	
	/**
	 * 取消拦截
	 * @param deliveryId
	 * @param operator
	 */
	public void cancelIntecept(SendDelivery record,String operator);
	/**
	 * 查询发货单状态
	 * @param deliveryId
	 * @param status
	 * @return
	 * @version 2017年2月14日 下午3:17:26<br/>
	 * @author Aaron He<br/>
	 */
	public Map<String,String> qryStatus(String deliveryId);
	
	/**
	 * 发货确认
	 * @param deliveryIdList
	 * @throws Exception
	 */
	public SendDeliveryVo confirmSend(String expressBillNo,String orgId,String wareHouseId,String userId) throws Exception;
	
	/**
	 * 退货
	 * @param sendDeliveryVo
	 * @throws Exception
	 */
	public void chargeBack(SendDeliveryVo sendDeliveryVo,String userId) throws Exception;

	/**
	 * @param sendDeliveryVo
	 * @param userId
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月8日 上午9:15:30<br/>
	 * @author 王通<br/>
	 */
	public void check(SendDeliveryVo sendDeliveryVo, String userId) throws Exception;

	/**
	 * @param sendDeliveryVo
	 * @param userId
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月8日 上午9:15:45<br/>
	 * @author 王通<br/>
	 */
	public void destory(SendDeliveryVo sendDeliveryVo, String userId) throws Exception;
	/**
	 * @param sendDeliveryVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月11日 下午5:56:15<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	ResultModel centralList4Print(SendDeliveryVo sendDeliveryVo) throws Exception;
	
	/**
	 * 下载excel
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月24日 下午2:46:13<br/>
	 * @author 王通<br/>
	 */
	public ResponseEntity<byte[]> downloadExcel(SendDeliveryVo vo) throws Exception;
	
	/**
	 * 出库统计导出
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<byte[]> exportSendStaticsExcel(SendPickLocationVo vo, Principal p) throws Exception;
	
	public ResultModel qryStatiticsPageList_new(SendPickLocationVo vo, Principal p,Boolean hasPage) throws Exception;
	
	/**
	 * 导出订单统计excel
	 * @param vo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<byte[]> exportSendOrderExcel(SendDeliveryVo vo, Principal p) throws Exception;
	
	/**
	 * 查询出库统计列表
	 * @param vo
	 * @param p
	 * @param hasPage
	 * @return
	 * @throws Exception
	 */
//	public ResultModel qryStatiticsPageList(SendDeliveryVo vo, Principal p,Boolean hasPage) throws Exception;
	
	/**
	 * 将所有仓库的出库数据发送到海关
	 * @param warehouseIdList
	 * @throws Exception
	 */
	public void findAndSendOutStockData(List<String> warehouseIdList)throws Exception;
	/**
	 * 批量同步出库数据到海关辅助系统
	 * @param dataIdList
	 * @throws Exception
	 */
	public void bachSyncOutStockData(List<String> dataIdList) throws Exception;
	
	/**
	 * 更新快递单打印次数
	 * @param deliveryId
	 * @throws Exception
	 */
	public void updatePrintExpressTimes(String deliveryId) throws Exception;
	
	/**
	 * 传送出库数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
	public void transmitOutStockXML(String deliveryId) throws Exception;
	/** 
	* @Title: viewByEntity 
	* @Description: 称重复核-根据运单号查询发货单详情
	* @auth tphe06
	* @time 2018 2018年8月28日 下午1:52:27
	* @param sendDeliveryVo
	* @return
	* @throws Exception
	* SendDeliveryVo
	*/
	public SendDeliveryVo viewByEntity(SendDeliveryVo sendDeliveryVo) throws Exception;
	public SendDeliveryVo getBom(SendDeliveryVo deliveryVo) throws Exception;
	public ResultModel qryTotalOrder(SendDeliveryVo sdVo) throws Exception;

	public ResultModel qryTotalSend(SendPickLocationVo vo, Principal p,Boolean hasPage) throws Exception;

	/** 
	* @Title: intercept 
	* @Description: 订单拦截接口（在称重复核阶段）
	* @auth tphe06
	* @time 2018 2018年8月28日 上午10:20:31
	* @param expressBilNo 运单号
	* @return 是否拦截成功
	* boolean
	 * @throws Exception 
	*/
	public boolean intercept(String expressBilNo) throws Exception;
}