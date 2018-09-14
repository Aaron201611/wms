package com.yunkouan.wms.modules.ts.service;

import java.util.List;
import java.util.Map;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

/**
 * 任务服务接口
 */
public interface ITaskService {
	
	/**
	 * 查询用户单量
	 * @param userId
	 * @return
	 */
	public int countUserTaskNum(String userId);
	/**
	 * 批量分配任务
	 * @param type
	 * @param opId
	 * @param opPerson
	 * @throws Exception
	 */
	public void batchAllocate(Integer type,List<String> opIdList,String opPerson)throws Exception;
	/**
	 * 指派作业人员
	 * @param type
	 * @param opId
	 * @param opPerson
	 * @return
	 * @throws Exception
	 */
	public void allocate(Integer type,String opId,String opPerson)throws Exception;
	/**
	 * 创建任务
	 * @param operator
	 * @param type
	 * @param opId
	 * @return
	 */
	public TsTaskVo create(Integer type,String opId) throws Exception;

	/**
	 * 新增
	 * @param tsTaskVo
	 */
//	public TsTaskVo add(TsTaskVo tsTaskVo) throws Exception;
	
	/**
	 * 查询任务列表
	 * @param tsTaskVo
	 * @return
	 */
	public ResultModel list(TsTaskVo tsTaskVo) throws Exception;
	
	/**
	 * 查询任务数量
	 * @param tsTaskVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public Map<Integer,Integer> count(TsTaskVo tsTaskVo) throws DaoException, ServiceException;
	
	/**
	 * 查询任务
	 * @param taskId
	 * @return
	 */
	public TsTaskVo view(String taskId);
	
	/**
	 * 执行任务
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void exec(String taskId,String userId)throws Exception;
	
	/**
	 * 拒绝任务
	 * 检查任务是否执行状态
	 * 更新单据为打开状态
	 * 取消任务
	 * @param taskId
	 */
	public void cancel(String taskId,String userId) throws Exception;
	
	/**
	 * 任务完成
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void finish(String taskId,String userId) throws Exception;
	
	/**
	 * 根据单据id结束任务
	 * @param opId
	 * @param userId
	 * @throws Exception
	 */
	public void finishByOpId(String opId,String userId)throws Exception;
	
	/**
	 * 根据单号id取消任务
	 * 1、检查任务是否执行状态
	 * 2、取消任务
	 * @param id
	 * @param userId
	 * @throws Exception
	 */
	public void cancelByOpId(String opId,String userId)throws Exception;
	
	/**
	 * 更新状态
	 * @param tsTaskVo
	 * @param newStatus
	 * @return
	 */
	public int updateStatus(String taskId, Integer newStatus,String operator);
	
	/**
	 * 检查是否执行状态
	 * @param taskId
	 */
	public void checkTaskExecStatusByOpId(String taskId);
	/**
	 * 检查任务状态
	 * @param taskId
	 * @param status
	 * @return
	 */
	public boolean checkStatus(String taskId,int isStatus);

	/**
	 * 指派一个作业人员
	 * 1.1 自动分配任务规则：
	 * 按手持终端在线人员单量自动分配，当天单量少的优先分配
	 * 1.2 触发自动分配时机：
	 * 新创建任务时刻；手持终端登录时刻；任务完成时刻；
	 * 1.3 收货，上架，拣货，移位，盘点单生效时，生成任务单（作业人员可以为空，有在线人员则分配，无则暂时不分配）
	 * 1.4 PC端作业确认时自动把任务完成，如果任务已经完成手持终端不能再确认。
	 * @return 如果未找到合适人员，则返回null
	 */
	public Principal assign();

	/**
	 * 自动给所有未分配作业人员的任务分配作业人员
	 * 1.1 自动分配任务规则：
	 * 按手持终端在线人员单量自动分配，当天单量少的优先分配
	 * 1.2 触发自动分配时机：
	 * 新创建任务时刻；手持终端登录时刻；任务完成时刻；
	 * 1.3 收货，上架，拣货，移位，盘点单生效时，生成任务单（作业人员可以为空，有在线人员则分配，无则暂时不分配）
	 * 1.4 PC端作业确认时自动把任务完成，如果任务已经完成手持终端不能再确认。
	 * @return 如果未找到合适人员，则返回null
	 */
	public void autoAssignAll() throws DaoException, ServiceException;
}