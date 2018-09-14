package com.yunkouan.wms.modules.sys.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.dao.IInterfaceLogDao;
import com.yunkouan.wms.modules.sys.entity.InterfaceLog;
import com.yunkouan.wms.modules.sys.service.IInterfaceLogService;
import com.yunkouan.wms.modules.sys.vo.InterfaceLogVo;

/**
 * 接口日志服务类
 * @author Aaron
 *
 */
@Service
public class InterfaceLogServiceImpl extends BaseService implements IInterfaceLogService{

	private Logger log = LoggerFactory.getLogger(InterfaceLogServiceImpl.class);
	
	@Autowired
	private IInterfaceLogDao logDao;

	/**
	 * 新增
	 * @param logVo
	 */
	public void add(InterfaceLogVo logVo) {
		logDao.insertSelective(logVo.getEntity());
	}

	/**
	 * 修改
	 * @param logVo
	 */
	public void update(InterfaceLogVo logVo){
		logVo.getEntity().setUpdatePerson(LoginUtil.getLoginUser().getUserId());
		logVo.getEntity().setUpdateTime(new Date());
		logDao.updateByPrimaryKeySelective(logVo.getEntity());
	}
	

	
}
