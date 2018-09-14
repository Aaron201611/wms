/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:22:29<br/>
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
import com.yunkouan.saas.modules.sys.dao.ILocationSpecDao;
import com.yunkouan.saas.modules.sys.entity.MetaLocationSpec;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.ILocationSpecExtlService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationSpecVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 库位规格外调业务类<br/><br/>
 * @version 2017年3月13日下午2:22:29<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class LocationSpecExtlServiceImpl extends BaseService implements ILocationSpecExtlService {

	/** 日志类 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/** 库位规格dao <br/> add by andy wang */
	@Autowired
	private ILocationSpecDao locSpecDao;

	/**
	 * 根据条件，分页查询库位规格
	 * @param metaLocationSpecVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listLocSpecByPage(MetaLocationSpecVO metaLocationSpecVO) throws Exception {
		if (metaLocationSpecVO == null || metaLocationSpecVO.getLocSpec() == null) {
			log.error("insertLocationSpec --> metaLocationSpec or locSpec is null!");
			return null;
		}
		Example example = metaLocationSpecVO.getExample();
		example.setOrderByClause("spec_id2 DESC");
		this.defWhere(example);
		Page page = PageHelper.startPage(metaLocationSpecVO.getCurrentPage() + 1, metaLocationSpecVO.getPageSize());
		this.locSpecDao.selectByExample(example);
		if (!PubUtil.isEmpty(page)) {
			for (Object obj : page) {
				MetaLocationSpec metaLocationSpec = (MetaLocationSpec) obj;
				metaLocationSpec.clear();
			}
		}
		return page;
	}

	/**
	 * 根据库位规格编号，查询库位规格
	 * @param locSpecNo 库位规格编号
	 * @return 库位规格对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec findLocSpecByNo(String locSpecNo) throws Exception {
		if (StringUtil.isTrimEmpty(locSpecNo)) {
			log.error("findLocationSpecByNo --> locSpecNo is null!");
			return null;
		}
		MetaLocationSpecVO metaLocationSpecVO = new MetaLocationSpecVO();
		metaLocationSpecVO.getLocSpec().setSpecNo(locSpecNo);
		List<MetaLocationSpec> listLocationSpec = this.listLocSpecByExample(metaLocationSpecVO);
		return PubUtil.isEmpty(listLocationSpec) ? null : listLocationSpec.get(0);
	}

	/**
	 * 根据库位规格名，查询库位规格
	 * @param locSpecName 库位规格名
	 * @return 库位规格
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec findLocSpecByName(String locSpecName) throws Exception {
		if (StringUtil.isTrimEmpty(locSpecName)) {
			log.error("findLocationSpecByName --> locSpecName is null!");
			return null;
		}
		MetaLocationSpecVO metaLocationSpecVO = new MetaLocationSpecVO();
		metaLocationSpecVO.getLocSpec().setSpecName(locSpecName);
		List<MetaLocationSpec> listLocationSpec = this.listLocSpecByExample(metaLocationSpecVO);
		return PubUtil.isEmpty(listLocationSpec) ? null : listLocationSpec.get(0);
	}

	/**
	 * 保存库位规格
	 * @param metaLocationSpec 要保存的库位规格对象
	 * @return 保存后的库位规格对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec insertLocSpec(MetaLocationSpec metaLocationSpec) throws Exception {
		if (metaLocationSpec == null) {
			log.error("insertLocationSpec --> metaLocationSpec is null!");
			return null;
		}
		if (StringUtil.isTrimEmpty(metaLocationSpec.getSpecId())) {
			metaLocationSpec.setSpecId(IdUtil.getUUID());
		}
		Principal loginUser = LoginUtil.getLoginUser();
		metaLocationSpec.setOrgId(loginUser.getOrgId());
		metaLocationSpec.setWarehouseId(LoginUtil.getWareHouseId());
		metaLocationSpec.setCreatePerson(loginUser.getUserId());
		metaLocationSpec.setUpdatePerson(loginUser.getUserId());
		metaLocationSpec.setSpecId2(null);
		metaLocationSpec.setCreateTime(null);
		metaLocationSpec.setUpdateTime(null);
		metaLocationSpec.setSpecId2(context.getStrategy4Id().getLocationSpecSeq());
		this.locSpecDao.insertSelective(metaLocationSpec);
		return metaLocationSpec;
	}

	/**
	 * 更新库位规格
	 * @param locSpecVO 更新的库位规格内容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public int updateLocSpec(MetaLocationSpecVO locSpecVO) throws Exception {
		if (locSpecVO == null || locSpecVO.getLocSpec() == null) {
			log.error("updateLocationSpec --> locSpecVO or locSpec is null!");
			return 0;
		}
		MetaLocationSpec locSpec = locSpecVO.getLocSpec();
		// 拼接Where条件
		MetaLocationSpecVO metaLocationSpecVO = new MetaLocationSpecVO();
		metaLocationSpecVO.getLocSpec().setSpecId(locSpec.getSpecId());
		Example example = metaLocationSpecVO.getExample();
		this.defWhere(example);
		// 拼接更新内容
		Principal loginUser = LoginUtil.getLoginUser();
		locSpec.setSpecId2(null);
		locSpec.setCreatePerson(null);
		locSpec.setCreateTime(null);
		locSpec.setUpdatePerson(loginUser.getUserId());
		locSpec.setUpdateTime(new Date());
		return this.locSpecDao.updateByExampleSelective(locSpec, example);
	}

	/**
	 * 根据库位规格id，更新库位规格状态
	 * @param locSpecId 库位规格id
	 * @param status 库位规格状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public int updateLocSpecStatusById(String locSpecId, Integer status) throws Exception {
		if (StringUtil.isTrimEmpty(locSpecId)) {
			log.error("updateLocationSpecStatusById --> locSpecId is null!");
			return 0;
		}
		// 设置更新条件
		MetaLocationSpecVO metaLocationSpecVO = new MetaLocationSpecVO();
		metaLocationSpecVO.getLocSpec().setSpecId(locSpecId);
		Example example = metaLocationSpecVO.getExample();
		this.defWhere(example);
		// 设置更新内容
		MetaLocationSpec record = new MetaLocationSpec();
		record.setSpecStatus(status);
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.locSpecDao.updateByExampleSelective(record, example);
	}

	/**
	 * 根据库位规格id，查询库位规格信息
	 * @param locSpecId 库位规格id
	 * @return 库位规格信息
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec findLocSpecById(String locSpecId) throws Exception {
		if (StringUtil.isTrimEmpty(locSpecId)) {
			log.error("findLocationSpecById --> locSpecId is null!");
			return null;
		}
		MetaLocationSpecVO metaLocationSpecVO = new MetaLocationSpecVO();
		metaLocationSpecVO.getLocSpec().setSpecId(locSpecId);
		List<MetaLocationSpec> listLocationSpec = this.listLocSpecByExample(metaLocationSpecVO);
		return PubUtil.isEmpty(listLocationSpec) ? null : listLocationSpec.get(0);
	}

	/**
	 * 根据条件，查询库位规格
	 * @param metaLocationSpecVO 查询条件
	 * @return 库位规格对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:22:29<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocationSpec> listLocSpecByExample(MetaLocationSpecVO metaLocationSpecVO) throws Exception {
		if (metaLocationSpecVO == null || metaLocationSpecVO.getLocSpec() == null) {
			log.error("listLocSpecByExample --> metaLocationSpecVO or locSpec is null!");
			return null;
		}
		Example example = metaLocationSpecVO.getExample();
		this.defWhere(example);
		return this.locSpecDao.selectByExample(example);
	}

	/**
	 * 设置默认查询条件
	 * @param example 
	 * @version 2017年3月13日下午2:22:29<br/>
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
