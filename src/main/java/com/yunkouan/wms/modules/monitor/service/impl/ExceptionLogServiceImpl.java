package com.yunkouan.wms.modules.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;

import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.monitor.dao.IExceptionLogDao;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.monitor.vo.ExceptionLogVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 异常业务实现类
 */
@Service
public class ExceptionLogServiceImpl extends BaseService implements IExceptionLogService {

	@Autowired
	private ISysParamService paramService;

	/**
	 * Default constructor
	 */
	public ExceptionLogServiceImpl() {
	}

	/**
	 * dao对象
	 */
	@Autowired
	private IExceptionLogDao dao;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;

	/**
	 * 登记异常
	 * @param exp
	 * @throws Exception
	 * @required exType,involveBill,exStatus,exDesc,exLevel
	 * @optional
	 * @Description
	 * @version 2017年2月28日 下午5:05:53<br/>
	 * @author 王通<br/>
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false)
	public ExceptionLog add(ExceptionLog exp) throws Exception {
		// 设置创建人/修改人/企业/仓库
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		// 字段校验
		if (exp == null) {
			throw new BizException("err_exp_param_null");
		}
		Date now = new Date();
		String orgId = loginUser.getOrgId();
		exp.setCreatePerson(loginUser.getUserId());
		exp.setCreateTime(now);
		exp.setUpdatePerson(loginUser.getUserId());
		exp.setUpdateTime(now);
		exp.setOrgId(orgId);
		exp.setWarehouseId(LoginUtil.getWareHouseId());
		Integer exType = exp.getExType();
		String involveBill = exp.getInvolveBill();
		exp.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
		if (exp.getExDataFrom() == null) {
			exp.setExDataFrom(Constant.EXCEPTION_DATE_FROM_AUTO);
		}
		if (exp.getOccurrenceTime() == null) {
			exp.setOccurrenceTime(now);
		}
		String exDesc = exp.getExDesc();
		Integer exLevel = exp.getExLevel();
		//防止内部调用时参数不全
		if (StringUtil.isTrimEmpty(involveBill) || StringUtil.isTrimEmpty(exDesc) || exType == null || exType < 0
				|| exLevel == null || exLevel < 0) {
			throw new BizException("err_exp_param_required_null");
		}
		exp.setExLogId(IdUtil.getUUID());
		exp.setExNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getExceptionNo(orgId));
		exp.setExLogId2(context.getStrategy4Id().getExpLogSeq());
		this.dao.insertSelective(exp);
		return exp;
	}


	/**
	 * @param exceptionLogVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:11:14<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Page<ExceptionLogVO> listByPage(ExceptionLogVO vo) {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null ) {
    		vo = new ExceptionLogVO(new ExceptionLog());
		}
		ExceptionLog exceptionLog = vo.getExceptionLog();
		if ( exceptionLog == null ) {
			exceptionLog = new ExceptionLog();
			vo.setExceptionLog(exceptionLog);
		}
		Page<ExceptionLogVO> page = null;
		//组装信息
		exceptionLog.setWarehouseId(LoginUtil.getWareHouseId());
		exceptionLog.setOrgId(loginUser.getOrgId());
		
		// 查询异常单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		Example example = vo.getExample();
		List<ExceptionLog> listExceptionLog = this.dao.selectByExample(example);
		if ( listExceptionLog == null || listExceptionLog.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<ExceptionLogVO> listVO = new ArrayList<ExceptionLogVO>();
		for (int i = 0; i < listExceptionLog.size(); i++) {
			ExceptionLog log = listExceptionLog.get(i);
			if ( log == null ) {
				continue;
			}
			ExceptionLogVO logVo = new ExceptionLogVO(log).searchCache();
			listVO.add(logVo);
			// 查询异常类型
			if (log.getExType() != null) {
				logVo.setExTypeName(paramService.getValue(CacheName.EXCEPTION_TYPE, log.getExType()));
			}
			// 查询异常等级
			if (log.getExLevel() != null) {
				logVo.setExLevelName(paramService.getValue(CacheName.EXCEPTION_LEVEL, log.getExLevel()));
			}
			// 查询异常导入方式
			if (log.getExDataFrom() != null) {
				logVo.setExDataFromName(paramService.getValue(CacheName.EXCEPTION_DATA_FROM, log.getExDataFrom()));
			}
			// 查询异常状态
			if (log.getExStatus() != null) {
				logVo.setExStatusName(paramService.getValue(CacheName.EXCEPTION_STATUS, log.getExStatus()));
			}
		}
		page.clear();
		page.addAll(listVO);
		return page;
	}


	/**
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:11:14<br/>
	 * @author 王通<br/>
	 */
	@Override
	public ExceptionLogVO view(String id) {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( id == null ) {
			throw new BizException("err_exceptionLog_null");
		}
		// 查询异常单详情 
		ExceptionLog exceptionLog = this.dao.selectByPrimaryKey(id);
		if (exceptionLog == null) {
			throw new BizException("err_exceptionLog_null");
		}
		//组装信息
		exceptionLog.setWarehouseId(LoginUtil.getWareHouseId());
		exceptionLog.setOrgId(loginUser.getOrgId());
		
		// 组装信息
		ExceptionLogVO exceptionLogVo = new ExceptionLogVO(exceptionLog).searchCache();

		// 查询异常类型
		if (exceptionLog.getExType() != null) {
			exceptionLogVo.setExTypeName(paramService.getValue(CacheName.EXCEPTION_TYPE, exceptionLog.getExType()));
		}
		// 查询异常等级
		if (exceptionLog.getExLevel() != null) {
			exceptionLogVo.setExLevelName(paramService.getValue(CacheName.EXCEPTION_LEVEL, exceptionLog.getExLevel()));
		}
		// 查询异常导入方式
		if (exceptionLog.getExDataFrom() != null) {
			exceptionLogVo.setExDataFromName(paramService.getValue(CacheName.EXCEPTION_DATA_FROM, exceptionLog.getExDataFrom()));
		}
		// 查询异常状态
		if (exceptionLog.getExStatus() != null) {
			exceptionLogVo.setExStatusName(paramService.getValue(CacheName.EXCEPTION_STATUS, exceptionLog.getExStatus()));
		}
		return exceptionLogVo;
	}


	/**
	 * 处理异常
	 * @param log
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:11:14<br/>
	 * @author 王通<br/>
	 */
	@Override
	public ExceptionLog update(ExceptionLog exp) {
		Principal loginUser = LoginUtil.getLoginUser();
		// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		// 字段校验
		if (exp == null) {
			throw new BizException("err_exp_param_null");
		}
		String exId = exp.getExLogId();
		// 检查异常单是否存在
		ExceptionLog expLog = this.dao.selectByPrimaryKey(exId);
		if (expLog == null || StringUtil.isEmpty(expLog.getExLogId())) {
			throw new BizException("err_exp_null");
		} else if (!expLog.getExStatus().equals(Constant.EXCEPTION_STATUS_TO_BE_HANDLED)) {
			throw new BizException("err_exp_status_wrong");
		}

		// 设置创建人/修改人/企业/仓库
		String orgId = loginUser.getOrgId();
		exp.setUpdatePerson(loginUser.getUserId());
		exp.setUpdateTime(new Date());
		exp.setOrgId(orgId);
		exp.setWarehouseId(LoginUtil.getWareHouseId());
		Integer exType = exp.getExType();
		String involveBill = exp.getInvolveBill();
		String exDesc = exp.getExDesc();
		Integer exLevel = exp.getExLevel();
		if (StringUtil.isTrimEmpty(exId) || StringUtil.isTrimEmpty(involveBill) 
				|| StringUtil.isTrimEmpty(exDesc) || exType == null || exType < 0
				|| exLevel == null || exLevel < 0) {
			throw new BizException("err_exp_param_required_null");
		}
		String msg = exp.getHandleMsg();
		if (!StringUtil.isTrimEmpty(msg)) {
			exp.setExStatus(Constant.EXCEPTION_STATUS_HANDLED);
		}
		this.dao.updateByPrimaryKeySelective(exp);
		return exp;
	}


	/**
	 * @param exceptionLogIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:11:14<br/>
	 * @author 王通<br/>
	 */
	@Override
	public void cancel(List<String> exceptionLogIdList) {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (exceptionLogIdList == null || exceptionLogIdList.isEmpty()) {
			throw new BizException("err_exp_null");
		}
    	//获取登录信息中的企业和仓库
		for (String expId : exceptionLogIdList) {
			ExceptionLogVO expVo = this.view(expId);
			if (expVo == null) {
				throw new BizException("err_exp_null");
			}
			if (expVo.getExceptionLog().getExStatus() != Constant.STATUS_ACTIVE) {
				throw new BizException("err_exp_status_wrong");
			}
			ExceptionLog expRecord = new ExceptionLog();
			expRecord.setExLogId(expId);
			expRecord.setExStatus(Constant.STATUS_CANCEL);
			this.dao.updateByPrimaryKeySelective(expRecord);
		}
	}

	/**
	 * 
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 上午10:34:25<br/>
	 * @author 王通<br/>
	 */
	@Override
	public int countByExample (ExceptionLogVO vo) {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null ) {
    		vo = new ExceptionLogVO(new ExceptionLog());
		}
		ExceptionLog exceptionLog = vo.getExceptionLog();
		if ( exceptionLog == null ) {
			exceptionLog = new ExceptionLog();
			vo.setExceptionLog(exceptionLog);
		}
		//组装信息
		exceptionLog.setWarehouseId(LoginUtil.getWareHouseId());
		exceptionLog.setOrgId(loginUser.getOrgId());
		
		// 查询异常单列表 
		Example example = vo.getExample();
		List<ExceptionLog> listExceptionLog = this.dao.selectByExample(example);
		if ( listExceptionLog == null || listExceptionLog.isEmpty() ) {
			return 0;
		} else {
			return listExceptionLog.size();
		}
	}
	@Override
	public List<ExceptionLog> countByExample2 (ExceptionLogVO vo) {
		// 查询异常单列表 
		Example example = vo.getExample();
		List<ExceptionLog> listExceptionLog = this.dao.selectByExample(example);
		return listExceptionLog;
	}
	public List<String>getTask(String orgId){
		return dao.getTask(orgId);
	}
}