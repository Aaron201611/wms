
/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日上午11:34:40<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IDockDao;
import com.yunkouan.wms.modules.meta.entity.MetaDock;
import com.yunkouan.wms.modules.meta.service.IDockExtlService;
import com.yunkouan.wms.modules.meta.vo.MetaDockVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * EEE外调业务类<br/><br/>
 * @version 2017年3月13日上午11:34:40<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class DockExtlServiceImpl extends BaseService implements IDockExtlService {

	/** 日志类 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/** EEEdao <br/> add by andy wang */
	@Autowired
	private IDockDao dockDao;

	/**
	 * 根据条件，分页查询EEE
	 * @param metaDockVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listDockByPage(MetaDockVO metaDockVO) throws Exception {
		if (metaDockVO == null || metaDockVO.getDock() == null) {
			log.error("insertDock --> metaDock or dock is null!");
			return null;
		}
		Example example = metaDockVO.getExample();
		example.setOrderByClause("dock_id2 DESC");
		this.defWhere(example);
		Page page = PageHelper.startPage(metaDockVO.getCurrentPage() + 1, metaDockVO.getPageSize());
		this.dockDao.selectByExample(example);
		if (!PubUtil.isEmpty(page)) {
			for (Object obj : page) {
				MetaDock metaDock = (MetaDock) obj;
				metaDock.clear();
			}
		}
		return page;
	}

	/**
	 * 根据EEE编号，查询EEE
	 * @param dockNo EEE编号
	 * @return EEE对象
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock findDockByNo(String dockNo , String dockId ) throws Exception {
		if (StringUtil.isTrimEmpty(dockNo)) {
			log.error("findDockByNo --> dockNo is null!");
			return null;
		}
		MetaDockVO metaDockVO = new MetaDockVO();
		metaDockVO.getDock().setDockNo(dockNo);
		metaDockVO.addNotId(dockId);
		List<MetaDock> listDock = this.listDockByExample(metaDockVO);
		return PubUtil.isEmpty(listDock) ? null : listDock.get(0);
	}

	/**
	 * 根据EEE名，查询EEE
	 * @param dockName EEE名
	 * @return EEE
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock findDockByName(String dockName) throws Exception {
		if (StringUtil.isTrimEmpty(dockName)) {
			log.error("findDockByName --> dockName is null!");
			return null;
		}
		MetaDockVO metaDockVO = new MetaDockVO();
		metaDockVO.getDock().setDockName(dockName);
		List<MetaDock> listDock = this.listDockByExample(metaDockVO);
		return PubUtil.isEmpty(listDock) ? null : listDock.get(0);
	}

	/**
	 * 保存EEE
	 * @param metaDock 要保存的EEE对象
	 * @return 保存后的EEE对象
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock insertDock(MetaDock metaDock) throws Exception {
		if (metaDock == null) {
			log.error("insertDock --> metaDock is null!");
			return null;
		}
		if (StringUtil.isTrimEmpty(metaDock.getDockId())) {
			metaDock.setDockId(IdUtil.getUUID());
		}
		Principal loginUser = LoginUtil.getLoginUser();
		metaDock.setOrgId(loginUser.getOrgId());
		metaDock.setWarehouseId(LoginUtil.getWareHouseId());
		metaDock.setCreatePerson(loginUser.getUserId());
		metaDock.setUpdatePerson(loginUser.getUserId());
		metaDock.setDockId2(null);
		metaDock.setCreateTime(null);
		metaDock.setUpdateTime(null);
		metaDock.setDockId2(context.getStrategy4Id().getDockSeq());
		this.dockDao.insertSelective(metaDock);
		return metaDock;
	}

	/**
	 * 更新EEE
	 * @param dockVO 更新的EEE内容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public int updateDock(MetaDockVO dockVO) throws Exception {
		if (dockVO == null || dockVO.getDock() == null) {
			log.error("updateDock --> dockVO or dock is null!");
			return 0;
		}
		MetaDock dock = dockVO.getDock();
		// 拼接Where条件
		MetaDockVO metaDockVO = new MetaDockVO();
		metaDockVO.getDock().setDockId(dock.getDockId());
		Example example = metaDockVO.getExample();
		this.defWhere(example);
		// 拼接更新内容
		Principal loginUser = LoginUtil.getLoginUser();
		dock.setDockId2(null);
		dock.setCreatePerson(null);
		dock.setCreateTime(null);
		dock.setUpdatePerson(loginUser.getUserId());
		dock.setUpdateTime(new Date());
		return this.dockDao.updateByExampleSelective(dock, example);
	}

	/**
	 * 根据EEEid，更新EEE状态
	 * @param dockId EEEid
	 * @param status EEE状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public int updateDockStatusById(String dockId, Integer status) throws Exception {
		if (StringUtil.isTrimEmpty(dockId)) {
			log.error("updateDockStatusById --> dockId is null!");
			return 0;
		}
		// 设置更新条件
		MetaDockVO metaDockVO = new MetaDockVO();
		metaDockVO.getDock().setDockId(dockId);
		Example example = metaDockVO.getExample();
		this.defWhere(example);
		// 设置更新内容
		MetaDock record = new MetaDock();
		record.setDockStatus(status);
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.dockDao.updateByExampleSelective(record, example);
	}

	/**
	 * 根据EEEid，查询EEE信息
	 * @param dockId EEEid
	 * @return EEE信息
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock findDockById(String dockId) throws Exception {
		if (StringUtil.isTrimEmpty(dockId)) {
			log.error("findDockById --> dockId is null!");
			return null;
		}
		MetaDockVO metaDockVO = new MetaDockVO();
		metaDockVO.getDock().setDockId(dockId);
		List<MetaDock> listDock = this.listDockByExample(metaDockVO);
		return PubUtil.isEmpty(listDock) ? null : listDock.get(0);
	}

	/**
	 * 根据条件，查询EEE
	 * @param metaDockVO 查询条件
	 * @return EEE对象
	 * @throws Exception
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaDock> listDockByExample(MetaDockVO metaDockVO) throws Exception {
		if (metaDockVO == null || metaDockVO.getDock() == null) {
			log.error("listDockByExample --> metaDockVO or dock is null!");
			return null;
		}
		Example example = metaDockVO.getExample();
		this.defWhere(example);
		return this.dockDao.selectByExample(example);
	}

	/**
	 * 设置默认查询条件
	 * @param example 
	 * @version 2017年3月13日上午11:34:40<br/>
	 * @author andy wang<br/>
	 */
	private void defWhere(Example example) {
		if (example == null || example.getOredCriteria() == null || example.getOredCriteria().isEmpty()) {
			return;
		}
		Criteria criteria = example.getOredCriteria().get(0);
		Principal loginUser = LoginUtil.getLoginUser();
		criteria.andEqualTo("orgId", loginUser.getOrgId());
		criteria.andEqualTo("warehouseId", LoginUtil.getWareHouseId());
	}

}
 