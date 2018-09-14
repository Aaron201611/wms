package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineVo;

public interface IDeliverGoodsExamineService {

	/**
	 * 分页查询
	 * @param examineVo
	 * @return
	 */
	public ResultModel pageList(DeliverGoodsExamineVo examineVo) throws Exception;
	/**
	 * 查询
	 * @param examineVo
	 * @return
	 */
	public List<DeliverGoodsExamineVo> qryList(DeliverGoodsExamineVo examineVo)throws Exception;
	/**
	 * 新增
	 * @param examineVo
	 * @return
	 */
	public DeliverGoodsExamineVo add(DeliverGoodsExamineVo examineVo,Principal p)throws Exception;
	
	/**
	 * 检查核放单编号是否存在
	 * @param examineNo
	 */
	public void examineNoIsExist(String examineNo);
	
	/**
	 * 新增生效
	 * @param examineVo
	 * @return
	 */
	public DeliverGoodsExamineVo saveAndEnable(DeliverGoodsExamineVo examineVo,Principal p)throws Exception;
	
	/**
	 * 新增或更新
	 * @param examineVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public DeliverGoodsExamineVo addOrUpdate(DeliverGoodsExamineVo examineVo,Principal p)throws Exception;
	/**
	 * 修改
	 * @param examineVo
	 * @return
	 */
	public DeliverGoodsExamineVo update(DeliverGoodsExamineVo examineVo,Principal p)throws Exception;
	
	/**
	 * 查看
	 * @param examineVo
	 * @return
	 */
	public DeliverGoodsExamineVo view(String examineId) throws Exception;
	
	/**
	 * 生效
	 * @param examineVo
	 * @return
	 */
	public void enable(String examineId,Principal p)throws Exception;
	
	/**
	 * 失效
	 * @param examineVo
	 * @return
	 */
	public void disable(String examineId,Principal p)throws Exception;
	
	/**
	 * 取消
	 * @param examineVo
	 * @return
	 */
	public void cancel(String examineId,Principal p)throws Exception;
	
	/**
	 * 发送申请单
	 * @param examineVo
	 * @throws Exception
	 */
	public void sendExamine(String examineId,Principal p)throws Exception;
	
	/**
	 * 审批通过
	 * @param examineId
	 * @throws Exception
	 */
	public void passAudit(String examineId)throws Exception;
	
	/**
	 * 核放单退单
	 * @param examineId
	 * @throws Exception
	 */
	public void changeBack(String examineId)throws Exception;
	
	/**
	 * 修改申请单状态
	 * @param examineId
	 * @param status
	 * @throws Exception
	 */
	public void updateAuditStepById(String examineId,String step,Principal p)throws Exception;
}
