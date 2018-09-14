package com.yunkouan.wms.modules.ts.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.service.ICountService;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.rec.service.IASNService;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.dao.IPickDao;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.wms.modules.ts.dao.ITaskDao;
import com.yunkouan.wms.modules.ts.entity.TsTask;
import com.yunkouan.wms.modules.ts.service.ITaskService;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import tk.mybatis.mapper.util.StringUtil;

/**
 * 任务服务
 * @author Aaron
 */
@Service
public class TaskServiceImpl extends BaseService implements ITaskService {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	public ITaskDao taskDao;

	@Autowired
	public IPickService pickService;

	@Autowired
	public IASNService asnService;

	@Autowired
	public IPutawayService putawayService;

	@Autowired
	public ICountService countService;

	@Autowired
	public IShiftService shiftService;

	@Autowired
	private IUserService userService;

	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	public IPickDao pickDao;

	
	/**
	 * 批量分配任务
	 * @param type
	 * @param opId
	 * @param opPerson
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void batchAllocate(Integer type,List<String> opIdList,String opPerson)throws Exception{
		if(opIdList == null || opIdList.isEmpty()) return;
		
		for(String opId:opIdList){
			allocate(type,opId,opPerson);
		}
	}
	/**
	 * 指派作业人员
	 * @param type
	 * @param opId
	 * @param opPerson
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void allocate(Integer type,String opId,String opPerson)throws Exception{
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		
		TsTaskVo tsTaskVo = new TsTaskVo();
		tsTaskVo.setOperer(opPerson);
		tsTaskVo.getTsTask().setOpId(opId);
		tsTaskVo.getTsTask().setTaskType(type);
		//若不存在则新增，若存在则更新
		addOrUpdate(tsTaskVo);
		
		//更新单据
		if(type.intValue() == Constant.TASK_TYPE_PICK){
			SendPick pick = new SendPick();
			pick.setPickId(opId);
			pick.setOpPerson(opPerson);
			pick.setOpTime(new Date());
			pick.setUpdatePerson(loginUser.getUserId());
			pickDao.updateByPrimaryKeySelective(pick);
		}
	}
	
	public void addOrUpdate(TsTaskVo tsTaskVo) throws Exception{
		//查询是否存在
		tsTaskVo.setStatusList(Arrays.asList(Constant.TASK_STATUS_OPEN));
		List<TsTask> list = taskDao.selectByExample(tsTaskVo.getConditionExample());
		tsTaskVo.getTsTask().setOpPerson(tsTaskVo.getOperer());
		if(list == null || list.isEmpty()) {
			add(tsTaskVo);
		}else{
			tsTaskVo.getTsTask().setTaskId(list.get(0).getTaskId());
			update(tsTaskVo);
			cancelJob(list.get(0).getOpPerson());
		}
		addJob(tsTaskVo.getOperer());
	}
	
	public void update(TsTaskVo tsTaskVo){
		taskDao.updateByPrimaryKeySelective(tsTaskVo.getTsTask());
	}
	/**
	 * 创建任务
	 * @param operator
	 * @param type
	 * @param opId
	 * @return
	 */
	public TsTaskVo create(Integer type, String opId) throws Exception {
		// 分配作业人员
		Principal user = assign();
		// 保存作业任务
		TsTaskVo tsTaskVo = new TsTaskVo();
		if(user != null) tsTaskVo.getTsTask().setOpPerson(user.getUserId());
		tsTaskVo.getTsTask().setOpPerson(null);
		tsTaskVo.getTsTask().setTaskType(type);
		tsTaskVo.getTsTask().setOpId(opId);
		tsTaskVo = add(tsTaskVo);
		// 单量+1
		if(user != null) addJob(user.getUserId());
		//自动分配作业人员
		autoAssignAll();
		return tsTaskVo;
	}

	/**
	 * 自动分配作业人员
	 * 1.1 自动分配任务规则：
	 * 按手持终端在线人员单量自动分配，当天单量少的优先分配
	 * 1.2 触发自动分配时机：
	 * 新创建任务时刻；手持终端登录时刻；任务完成时刻；
	 * 1.3 收货，上架，拣货，移位，盘点单生效时，生成任务单（作业人员可以为空，有在线人员则分配，无则暂时不分配）
	 * 1.4 PC端作业确认时自动把任务完成，如果任务已经完成手持终端不能再确认。
	 * @return 如果未找到合适人员，则返回null
	 */
	public Principal assign() {
		//最小任务量人员
		Principal p1 = null;
		SysUser user = null;
		//查找最小任务量的手持终端登录人员
		for(Principal p : filter()) {
			//如果非手持终端登录人员，不得分配任务
			if(!Constant.LOGIN_PHONE.equals(p.getFrom())) continue;
			//查询任务分配数量，分配最小任务量的人员
			SysUser u = userService.get(p.getUserId());
			if(u.getJobQuantity() == null) u.setJobQuantity(0);
			//如果已经分配任务，则不再分配
			TsTaskVo vo = new TsTaskVo();
			vo.getTsTask().setOrgId(p.getOrgId());
			vo.getTsTask().setWarehouseId(LoginUtil.getWareHouseId());
			vo.getTsTask().setOpPerson(p.getUserId());
			vo.addStatus(Constant.TASK_STATUS_OPEN);
			vo.addStatus(Constant.TASK_STATUS_EXEC);
			List<TsTask> list = taskDao.selectByExample(vo.getConditionExample());
			if(list != null && list.size() > 0) continue;
			//第一个作业人员
			if(user == null) {
				user = u;
				p1 = p;
			}
			//任务量小的作业人员
			if(u.getJobQuantity() < user.getJobQuantity()) {
				user = u;
				p1 = p;
			}
		}
		if(p1 != null && user != null && log.isDebugEnabled()) log.debug("自动分配作业人员编号："+user.getUserNo());
		if(p1 != null && user != null && log.isDebugEnabled()) log.debug("自动分配作业人员姓名："+user.getUserName());
		if(p1 == null && log.isDebugEnabled()) log.debug("未能自动分配作业人员");
//		if(p1 == null) throw new BizException("task_no_user_assign");
		return p1;
	}
	/**
	 * 从登录用户Session里面过滤唯一登陆人员（目前系统相同用户从不同地方登录生成不同Session）
	 * 同时过滤非本企业本仓库用户
	 * @return
	 */
	private List<Principal> filter() {
		List<Principal> list = new ArrayList<Principal>();
		//当前登录用户，分配的人员必须与当前登录用户属于相同机构相同仓库作业的人员
		Principal loginUser = LoginUtil.getLoginUser();
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for(Session session : sessions) {
			SimplePrincipalCollection c = (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if(c == null) continue;
			Principal p = (Principal)c.getPrimaryPrincipal();
			if(p == null) continue;
			if(loginUser.getOrgId() == null || !loginUser.getOrgId().equals(p.getOrgId())) continue;
			if(LoginUtil.getWareHouseId() == null || !LoginUtil.getWareHouseId().equals(LoginUtil.getWareHouseId())) continue;
			if(!contains(list, p)) list.add(p);
		}
		return list;
	}
	/**
	 * 判断登录用户列表是否已经包括该登录用户
	 * @param list
	 * @param p
	 * @return
	 */
	private boolean contains(List<Principal> list, Principal p) {
		for(Principal p1 : list) {
			if(p1.getUserId().equals(p.getUserId())) return true;
		}
		return false;
	}

	/**
	 * 单量+1
	 * @param userId
	 * @throws DaoException
	 * @throws ServiceException
	 */
	private void addJob(String userId) throws DaoException, ServiceException {
		if(StringUtils.isBlank(userId)) return;
		SysUser user = userService.get(userId);
		SysUser onlineUser = new SysUser();
		onlineUser.setUserId(userId);
		onlineUser.setJobQuantity(user.getJobQuantity()==null?1:user.getJobQuantity()+ 1);
		userService.update(onlineUser);
	}
	/**
	 * 单量-1
	 * @param userId
	 * @throws DaoException
	 * @throws ServiceException
	 */
	private void cancelJob(String userId) throws DaoException, ServiceException {
		if(StringUtils.isBlank(userId)) return;
		SysUser user = userService.get(userId);
		SysUser onlineUser = new SysUser();
		onlineUser.setUserId(userId);
		int sum = user.getJobQuantity() - 1;
		onlineUser.setJobQuantity((sum<0)?0:sum);
		userService.update(onlineUser);
	}

	/**
	 * 新增
	 */
	private TsTaskVo add(TsTaskVo tsTaskVo) throws Exception{
		if(tsTaskVo == null) return null;
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		if(loginUser != null){
			String orgId = loginUser.getOrgId();
			String wareHouseId = LoginUtil.getWareHouseId();
			tsTaskVo.getTsTask().setOrgId(orgId);
			tsTaskVo.getTsTask().setWarehouseId(wareHouseId);
			String userId = loginUser.getUserId();
			tsTaskVo.getTsTask().setCreatePerson(userId);
			tsTaskVo.getTsTask().setUpdatePerson(userId);
		}
		
		//新增id
		tsTaskVo.getTsTask().setTaskId(IdUtil.getUUID());
		tsTaskVo.getTsTask().setTaskStatus(Constant.TASK_STATUS_OPEN);
		tsTaskVo.getTsTask().setNoticeTime(new Date());
		taskDao.insertSelective(tsTaskVo.getTsTask());
		return tsTaskVo;
	}

	/**
	 * 查询列表
	 */
	public ResultModel list(TsTaskVo tsTaskVo) throws Exception {
		if(tsTaskVo == null) return null;
		Page<TsTask> page = PageHelper.startPage(tsTaskVo.getCurrentPage()+1, tsTaskVo.getPageSize());
		List<TsTask> taskList = taskDao.selectByExample(tsTaskVo.getConditionExample());
		ResultModel result = new ResultModel();
		if(taskList == null || taskList.isEmpty() ) return result;
		result.setPage(page);
		List<Object> list = new ArrayList<Object>();
		for(TsTask task:taskList){
			TsTaskVo vo = new TsTaskVo();
			vo.setTsTask(task);
			Object ob = getTaskInfo(task.getTaskType(), task.getOpId(), vo);	
			if(ob != null) list.add(ob);
		}
		result.setList(list);
		return result;
	}

	/**
	 * 查询任务数量
	 * @param tsTaskVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public Map<Integer,Integer> count(TsTaskVo tsTaskVo) throws DaoException, ServiceException {
		if(tsTaskVo == null) return null;
		//自动分配作业人员
		autoAssignAll();
		//{taskType=4, num=1}
		List<Map<String,Object>> list = taskDao.countGroupByTaskType(tsTaskVo.getStatusList(),
				tsTaskVo.getTsTask().getOpPerson(),
				tsTaskVo.getTsTask().getOrgId(),
				tsTaskVo.getTsTask().getWarehouseId(),
				tsTaskVo.getLastItemTime());
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		//默认所有任务类型的数量为0
		List<Integer> all = Arrays.asList(Constant.TASK_TYPE_ASN,Constant.TASK_TYPE_PUTAWAY,Constant.TASK_TYPE_PICK,
				Constant.TASK_TYPE_COUNT,Constant.TASK_TYPE_SHIFT);
		for(Integer type : all) {
			map.put(type, 0);
		}
		//填充有数据的值
		if(list != null) for(Map<String,Object> type : list) {
			map.put((Integer)type.get("taskType"), ((Long)type.get("num")).intValue());
		}
		return map;
	}

	/**
	 * 获取任务单据信息
	 * @param type
	 * @param id
	 * @param tsTaskVo
	 * @return
	 * @throws Exception
	 */
	public Object getTaskInfo(int type,String id,TsTaskVo tsTaskVo) throws Exception{
		Object ob = null;
		if(type == Constant.TASK_TYPE_PICK.intValue()){
			SendPickVo pickVo = pickService.view(id);
			//拣货单不是生效或者作业中，则不能查出
			if(pickVo.getSendPick().getPickStatus().intValue() == Constant.PICK_STATUS_ACTIVE 
					|| pickVo.getSendPick().getPickStatus().intValue() != Constant.PICK_STATUS_WORKING){
				pickVo.setTsTaskVo(tsTaskVo);
				ob = pickVo;
			}
		}
		else if(type == Constant.TASK_TYPE_ASN.intValue()){
			RecAsnVO asnVo = asnService.view(id);
			if(asnVo.getAsn().getAsnStatus().intValue() == Constant.ASN_STATUS_ACTIVE){
				asnVo.setTsTaskVo(tsTaskVo);
				ob = asnVo;
			}
		}
		else if(type == Constant.TASK_TYPE_PUTAWAY.intValue()){
			RecPutawayVO putawayVo = putawayService.view(id);
			if(putawayVo.getPutaway().getPutawayStatus().intValue() == Constant.PUTAWAY_STATUS_ACTIVE
					|| putawayVo.getPutaway().getPutawayStatus().intValue() == Constant.PUTAWAY_STATUS_WORKING){
				putawayVo.setTsTaskVo(tsTaskVo);
				ob = putawayVo;
			}
		}
		else if(type == Constant.TASK_TYPE_COUNT.intValue()){
			InvCountVO countVo = countService.view(id);
			if(countVo.getInvCount().getCountStatus().intValue() == Constant.STATUS_ACTIVE
				|| countVo.getInvCount().getCountStatus().intValue() == Constant.STATUS_WORKING){
				countVo.setTsTaskVo(tsTaskVo);
				ob = countVo;
			}
		}
		else if(type == Constant.TASK_TYPE_SHIFT.intValue()){
			InvShiftVO shiftVo = shiftService.view(id);
			if(shiftVo.getInvShift().getShiftStatus().intValue() == Constant.STATUS_ACTIVE
					|| shiftVo.getInvShift().getShiftStatus().intValue() == Constant.STATUS_WORKING){
				shiftVo.setTsTaskVo(tsTaskVo);
				ob = shiftVo;
			}
		}
		return ob;
	}

	/**
	 * 执行任务
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void exec(String taskId,String userId)throws Exception{
		//检查任务是否打开状态
		TsTask tsTask = taskDao.selectByPrimaryKey(taskId);
		if(tsTask.getTaskStatus().intValue() != Constant.TASK_STATUS_EXEC 
				&& tsTask.getTaskStatus().intValue() != Constant.TASK_STATUS_OPEN){
			throw new BizException(BizStatus.TASK_STATUS_IS_NOT_EXEC_OR_OPEN.getReasonPhrase());
		}
		//执行任务
		if(tsTask.getTaskStatus().intValue() != Constant.TASK_STATUS_EXEC){
			updateStatus(taskId,Constant.TASK_STATUS_EXEC,userId);
		}
	}
	
	/**
	 * 拒绝任务
	 * 检查任务是否执行状态
	 * 更新单据为打开状态
	 * 取消任务
	 * @param taskId
	 */
	@Transactional(rollbackFor=Exception.class)
	public void cancel(String taskId,String userId) throws Exception{
		//检查任务是否执行状态
		TsTask tsTask = taskDao.selectByPrimaryKey(taskId);
		if(tsTask.getTaskStatus().intValue() != Constant.TASK_STATUS_EXEC 
				&& tsTask.getTaskStatus().intValue() != Constant.TASK_STATUS_OPEN){
			throw new BizException(BizStatus.TASK_STATUS_IS_NOT_EXEC_OR_OPEN.getReasonPhrase());
		}
		//取消任务
		updateStatus(taskId,Constant.TASK_STATUS_CANCEL,userId);
		//更新 单据为打开状态
		disable(tsTask,userId);
		// 单量-1
		cancelJob(tsTask.getOpPerson());
	}

	/**
	 * 任务完成
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void finish(String taskId,String userId) throws Exception{
		//检查任务是否执行状态
		if(!checkStatus(taskId,Constant.TASK_STATUS_EXEC)){
			throw new BizException(BizStatus.TASK_STATUS_IS_NOT_EXEC.getReasonPhrase());
		}
		//取消任务
		updateStatus(taskId,Constant.TASK_STATUS_FINISH,userId);
		//自动分配作业人员
		autoAssignAll();
	}

	/**
	 * 根据单据id结束任务
	 * @param opId
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void finishByOpId(String opId,String userId)throws Exception{
		if(StringUtil.isEmpty(opId)) return;
		//查找task
		TsTaskVo tsTaskVo = new TsTaskVo();
		tsTaskVo.getTsTask().setOpId(opId);
		tsTaskVo.setStatusList(Arrays.asList(Constant.TASK_STATUS_EXEC,Constant.TASK_STATUS_OPEN));
		List<TsTask> list = taskDao.selectByExample(tsTaskVo.getConditionExample());
		if(list == null || list.size() == 0) return;
		//检查状态是否执行中
		for(TsTask task:list){
			updateStatus(task.getTaskId(), Constant.TASK_STATUS_FINISH, userId);
		}
		//自动分配作业人员
		autoAssignAll();
	}
	
	
	
	/**
	 * 根据单号id取消任务
	 * 1、检查任务是否执行状态
	 * 2、取消任务
	 * @param id
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void cancelByOpId(String opId,String userId)throws Exception{
		if(StringUtil.isEmpty(opId)) return;
		//查找task
		TsTaskVo tsTaskVo = new TsTaskVo();
		tsTaskVo.getTsTask().setOpId(opId);
		List<TsTask> list = taskDao.selectByExample(tsTaskVo.getConditionExample());
		if(list == null || list.size() == 0) return;
		//检查状态是否执行中
		for(TsTask task:list){
			//执行中的状态不允许拒绝
			if(task.getTaskStatus().intValue() == Constant.TASK_STATUS_EXEC){
				throw new BizException(BizStatus.TASK_IS_EXEC_NOCANCEL.getReasonPhrase());
			}
			//不是取消的任务可以取消
			else if(task.getTaskStatus().intValue() != Constant.TASK_STATUS_CANCEL){
				//取消任务
				updateStatus(task.getTaskId(), Constant.TASK_STATUS_CANCEL, userId);
				//单量-1
				cancelJob(task.getOpPerson());
			}
		}
		
	}
	
	
	/**
	 * 根据任务类型更新单据状态为打开
	 * @param tsTask
	 * @param userId
	 * @throws Exception
	 */
	public void disable(TsTask tsTask,String userId) throws Exception{
		if(tsTask.getTaskType() == Constant.TASK_TYPE_PICK.intValue()){
			pickService.diable(tsTask.getOpId(), userId);
		}
		else if(tsTask.getTaskType() == Constant.TASK_TYPE_ASN.intValue()){
			asnService.disable(Arrays.asList(tsTask.getOpId()));
		}
		else if(tsTask.getTaskType() == Constant.TASK_TYPE_PUTAWAY.intValue()){
			putawayService.disable(Arrays.asList(tsTask.getOpId()));
		}
		else if(tsTask.getTaskType() == Constant.TASK_TYPE_COUNT.intValue()){
			List<String> countIdList = Arrays.asList(tsTask.getOpId());
			InvCountVO vo = new InvCountVO();
			vo.setCountIdList(countIdList);
			countService.disable(vo);
		}
		else if(tsTask.getTaskType() == Constant.TASK_TYPE_SHIFT.intValue()){
			List<String> shiftIdList = Arrays.asList(tsTask.getOpId());
			InvShiftVO vo = new InvShiftVO();
			vo.setShiftIdList(shiftIdList);
			shiftService.disable(vo);
		}
	}
	
	/**
	 * 更新状态
	 */
	public int updateStatus(String taskId, Integer newStatus,String operator) {
		TsTask tsTask = new TsTask();
		tsTask.setTaskId(taskId);
		tsTask.setTaskStatus(newStatus);
		tsTask.setUpdatePerson(operator);
		int num = taskDao.updateByPrimaryKeySelective(tsTask);
		return num;
	}
	
	/**
	 * 根据单据id检查任务是否执行状态
	 * @param taskId
	 */
	public void checkTaskExecStatusByOpId(String opId){
		if(StringUtil.isEmpty(opId)) return;
		//查找task
		TsTaskVo tsTaskVo = new TsTaskVo();
		tsTaskVo.getTsTask().setOpId(opId);
		List<TsTask> list = taskDao.selectByExample(tsTaskVo.getConditionExample());
		if(list == null || list.size() == 0) return;
		//检查状态是否执行中
		for(TsTask task:list){
			if(task.getTaskStatus().intValue() != Constant.TASK_STATUS_EXEC){
				throw new BizException(BizStatus.TASK_STATUS_IS_NOT_EXEC.getReasonPhrase());
			}
		}
	}
	
	/**
	 * 检查任务状态
	 * @param taskId
	 * @param status
	 * @return
	 */
	public boolean checkStatus(String taskId,int isStatus){
		TsTask tsTask = taskDao.selectByPrimaryKey(taskId);
		if(tsTask.getTaskStatus().intValue() != isStatus){
			return false;
		}
		return true;
	}
	
	/**
	 * 查询任务
	 * @param taskId
	 * @return
	 */
	public TsTaskVo view(String taskId){
		TsTaskVo taskVo = new TsTaskVo();
		TsTask task = taskDao.selectByPrimaryKey(taskId);
		taskVo.setTsTask(task);
		return taskVo;
	}

	/**
	 * 自动给所有未分配作业人员的任务分配作业人员
	 * 1.1 自动分配任务规则：
	 * 按手持终端在线人员单量自动分配，当天单量少的优先分配
	 * 1.2 触发自动分配时机：
	 * 新创建任务时刻；手持终端登录时刻；任务完成时刻；
	 * 1.3 收货，上架，拣货，移位，盘点单生效时，生成任务单（作业人员可以为空，有在线人员则分配，无则暂时不分配）
	 * 1.4 PC端作业确认时自动把任务完成，如果任务已经完成手持终端不能再确认。
	 * @return 如果未找到合适人员，则返回null
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@Override
	public void autoAssignAll() throws DaoException, ServiceException {
		//查找所有未分配的任务
		TsTaskVo vo = new TsTaskVo();
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser != null){
			vo.getTsTask().setOrgId(loginUser.getOrgId());
			vo.getTsTask().setWarehouseId(LoginUtil.getWareHouseId());
		}
		vo.getTsTask().setTaskStatus(Constant.TASK_STATUS_OPEN);
		List<TsTask> list = taskDao.selectByExample(vo.getConditionExample());
		for(TsTask ts : list) {
			if(StringUtils.isNoneBlank(ts.getOpPerson())) continue;
			//自动分配作业人员
			Principal p = assign();
			if(p == null) break;//无作业人员可以分配
			TsTask t = new TsTask();
			t.setTaskId(ts.getTaskId());
			t.setOpPerson(p.getUserId());
			taskDao.updateByPrimaryKeySelective(t);
			//单量+1
//			addJob(p.getUserId());
		}
	}
	
	/**
	 * 查询用户单量
	 * @param userId
	 * @return
	 */
	public int countUserTaskNum(String userId){
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		TsTaskVo taskVo = new TsTaskVo();
		taskVo.getTsTask().setOpPerson(userId);
		taskVo.getTsTask().setOrgId(loginUser.getOrgId());
		taskVo.getTsTask().setWarehouseId(LoginUtil.getWareHouseId());
		taskVo.setStatusList(Arrays.asList(Constant.TASK_STATUS_OPEN));
		
		int num = taskDao.selectCountByExample(taskVo.getConditionExample());
		return num;
	}
	
	public static void main(String[] args) {
//		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
//		Map<String,String> map1 = new HashMap<String,String>();
//		Map<String,String> map2 = new HashMap<String,String>();
//		Map<String,String> map3 = new HashMap<String,String>();
//		
//		map3.put("num", "4");
//		map2.put("num", "2");
//		map1.put("num", "6");
//		list.add(map1);
//		list.add(map2);
//		list.add(map3);
//		list.sort((p1,p2)->p1.get("num").compareTo(p2.get("num")));
//		list.stream().forEach(t->System.out.println(t.get("num")));
		List<String> list = new ArrayList<String>();
		list.add("0");
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.remove(0);
		System.out.println(list.get(0));
		
		
	}
}