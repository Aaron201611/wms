package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

public interface IDeliverGoodsApplicationService {

	/**
	 * 分页查询
	 * @param formVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public ResultModel pageList(DeliverGoodsApplicationVo applicationVo) throws DaoException, ServiceException;
	/**
	 * 查询
	 * @param formVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationVo> qryList(DeliverGoodsApplicationVo applicationVo) throws DaoException, ServiceException;
	
	/**
	 * 查找所有未完成核放申报的申请单
	 * @param appId
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationVo> qryAllUnexamine(Principal p) throws Exception;
	
	/**
	 * 从发货单生成申请单
	 * @param formVo
	 * @return
	 */
	public void createApplicationFromSend(SendDeliveryVo deliveryVo,Principal p) throws Exception;
	
	/**
	 * 从收货单生成申请单
	 * @param formVo
	 * @return
	 */
	public void createApplicationFromAsn(RecAsnVO asnVo,Principal p) throws Exception;
	
	/**
	 * 新增
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationVo add(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception;
	
	/**
	 * 检查申报单编号是否存在
	 * @param formNo
	 */
	public void applicationNoIsExist(String applicationNo);
	
	/**
	 * 新增生效
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationVo saveAndEnable(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception;
	
	/**
	 * 新增或更新
	 * @param applicationVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public DeliverGoodsApplicationVo addOrUpdate(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception;
	/**
	 * 修改
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationVo update(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception;
	
	/**
	 * 查看
	 * @param formVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public DeliverGoodsApplicationVo view(String applicationId) throws DaoException, ServiceException;
	
	/**
	 * 生效
	 * @param formVo
	 * @return
	 */
	public void enable(List<String> idList,Principal p)throws Exception;
	
	/**
	 * 失效
	 * @param formVo
	 * @return
	 */
	public void disable(List<String> idList,Principal p)throws Exception;
	
	/**
	 * 取消
	 * @param formVo
	 * @return
	 */
	public void cancel(List<String> idList,Principal p)throws Exception;
	
	/**
	 * 发送申请单
	 * @param applicationId
	 * @param p
	 * @throws Exception
	 */
	public void sendApplication(String applicationId,Principal p)throws Exception;
	
	/**
	 * 发送分类监管申请单
	 * @param applicationId
	 * @param p
	 * @throws Exception
	 */
	public void sendClassManageApplication(String applicationId,Principal p)throws Exception;
	/**
	 * 发送分送集报申请单
	 * @param applicationVo
	 * @throws Exception
	 */
	public void sendDeliverGoodsApplication(String applicationId,Principal p)throws Exception;
	
	/**
	 * 修改申请单审批环节
	 * @param applicationId
	 * @param status
	 * @throws Exception
	 */
	public void updateAuditStepById(String applicationId,String step,Principal p)throws Exception;
	
	/**
	 * 检查是否所有货品都可以核放
	 * @param applicationId
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public void isAllPassExamine(String applicationId) throws Exception;
	public void changeStatus(String applicationId) throws Exception;
}
