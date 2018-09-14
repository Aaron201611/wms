/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 下午3:41:25<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.saas.modules.sys.dao.IAreaDao;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 库区外调业务类<br/><br/>
 * @version 2017年3月11日 下午3:41:25<br/>
 * @author andy wang<br/>
 */
@Service
public class AreaExtlServiceImpl extends BaseService implements IAreaExtlService {

	
	/** 日志类 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	
	/** 库区dao <br/> add by andy wang */
	@Autowired
	private IAreaDao areaDao;

	/**
	 * 根据库区名称，查询库区
	 * @param areaName 库区名称
	 * @throws Exception
	 * @version 2017年3月30日 上午9:58:45<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaArea> listAreaByName ( String areaName , boolean isLike ) throws Exception {
		if ( StringUtil.isTrimEmpty(areaName) ) {
			log.error("listAreaByName --> areaName is null!");
			return null;
		}
		MetaAreaVO areaVO = new MetaAreaVO();
		if ( isLike ) {
			areaVO.setLikeAreaName(areaName);
		} else {
			areaVO.getArea().setAreaName(areaName);
		}
		return this.listAreaByExample(areaVO);
	}
	
	
	/**
	 * 根据条件，分页查询库区
	 * @param metaAreaVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月11日 下午5:01:16<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listAreaByPage ( MetaAreaVO metaAreaVO ) throws Exception {
		if ( metaAreaVO == null || metaAreaVO.getArea() == null ) {
			log.error("insertArea --> metaArea or area is null!");
			return null;
		}
		metaAreaVO.setLoginUser();
		Example example = metaAreaVO.getExample();
		example.setOrderByClause("area_id2 DESC");
		Page page = PageHelper.startPage(metaAreaVO.getCurrentPage()+1, metaAreaVO.getPageSize());
		this.areaDao.selectByExample(example);
		if ( !PubUtil.isEmpty(page) ) {
			for (Object obj : page) {
				MetaArea metaArea = (MetaArea) obj;
				metaArea.clear();
			}
		}
		return page;
	}
	
	
	/**
	 * 根据库区编号，查询库区
	 * @param areaNo 库区编号
	 * @return 库区对象
	 * @throws Exception
	 * @version 2017年3月11日 下午4:28:08<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea findAreaByNo ( String areaNo , String areaId ) throws Exception {
		if ( StringUtil.isTrimEmpty(areaNo) ) {
			log.error("findAreaByNo --> areaNo is null!");
			return null;
		}
		MetaAreaVO metaAreaVO = new MetaAreaVO();
		metaAreaVO.addNotId(areaId);
		metaAreaVO.getArea().setAreaNo(areaNo);
		List<MetaArea> listArea = this.listAreaByExample(metaAreaVO);
		return PubUtil.isEmpty(listArea)?null:listArea.get(0);
	}
	
	
	/**
	 * 根据库区名，查询库区
	 * @param areaName 库区名
	 * @return 库区
	 * @throws Exception
	 * @version 2017年3月11日 下午4:26:23<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea findAreaByName ( String areaName ) throws Exception {
		if ( StringUtil.isTrimEmpty(areaName) ) {
			log.error("findAreaByName --> areaName is null!");
			return null;
		}
		MetaAreaVO metaAreaVO = new MetaAreaVO();
		metaAreaVO.getArea().setAreaName(areaName);
		List<MetaArea> listArea = this.listAreaByExample(metaAreaVO);
		return PubUtil.isEmpty(listArea)?null:listArea.get(0);
	}
	
	
	/**
	 * 保存库区
	 * @param metaArea 要保存的库区对象
	 * @return 保存后的库区对象
	 * @throws Exception
	 * @version 2017年3月11日 下午4:22:07<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea insertArea ( MetaArea metaArea ) throws Exception {
		if ( metaArea == null ) {
			log.error("insertArea --> metaArea is null!");
			return null;
		}
		if ( StringUtil.isTrimEmpty(metaArea.getAreaId()) ) {
			metaArea.setAreaId(IdUtil.getUUID());
		}
		Principal loginUser = LoginUtil.getLoginUser();
		metaArea.setOrgId(loginUser.getOrgId());
		metaArea.setWarehouseId(metaArea.getWarehouseId());
		metaArea.setCreatePerson(loginUser.getUserId());
		metaArea.setUpdatePerson(loginUser.getUserId());
		metaArea.setAreaId2(null);
		metaArea.setCreateTime(null);
		metaArea.setUpdateTime(null);
		metaArea.setAreaId2(context.getStrategy4Id().getAreaSeq());
		this.areaDao.insertSelective(metaArea);
		return metaArea;
	}
	
	
	/**
	 * 更新库区
	 * @param areaVO 更新的库区内容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月11日 下午4:20:11<br/>
	 * @author andy wang<br/>
	 */
	public int updateArea ( MetaAreaVO areaVO ) throws Exception {
		if ( areaVO == null || areaVO.getArea() == null ) {
			log.error("updateArea --> areaVO or area is null!");
			return 0;
		}
		MetaArea area = areaVO.getArea();
		// 拼接Where条件
		MetaAreaVO metaAreaVO = new MetaAreaVO();
		metaAreaVO.getArea().setAreaId(area.getAreaId());
		metaAreaVO.setLoginUser();
		Example example = metaAreaVO.getExample();
		// 拼接更新内容
		Principal loginUser = LoginUtil.getLoginUser();
		area.setAreaId2(null);
		area.setCreatePerson(null);
		area.setCreateTime(null);
		area.setUpdatePerson(loginUser.getUserId());
		area.setUpdateTime(new Date());
		return this.areaDao.updateByExampleSelective(area, example);
	}
	
	
	/**
	 * 根据库区id，更新库区状态
	 * @param areaId 库区id
	 * @param status 库区状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月11日 下午4:16:32<br/>
	 * @author andy wang<br/>
	 */
	public int updateAreaStatusById ( String areaId , Integer status ) throws Exception {
		if ( StringUtil.isTrimEmpty(areaId) ) {
			log.error("updateAreaStatusById --> areaId is null!");
			return 0;
		}
		// 设置更新条件
		MetaAreaVO metaAreaVO = new MetaAreaVO();
		metaAreaVO.getArea().setAreaId(areaId);
		metaAreaVO.setLoginUser();
		Example example = metaAreaVO.getExample();
		// 设置更新内容
		MetaArea record = new MetaArea();
		record.setAreaStatus(status);
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.areaDao.updateByExampleSelective(record, example);
	}
	
	
	/**
	 * 根据库区id，查询库区信息
	 * @param areaId 库区id
	 * @return 库区信息
	 * @throws Exception
	 * @version 2017年3月11日 下午4:04:47<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea findAreaById ( String areaId ) throws Exception {
		if ( StringUtil.isTrimEmpty(areaId) ) {
			log.error("findAreaById --> areaId is null!");
			return null;
		}
		return this.areaDao.selectByPrimaryKey(areaId);
//		MetaAreaVO metaAreaVO = new MetaAreaVO();
//		metaAreaVO.getArea().setAreaId(areaId);
//		List<MetaArea> listArea = this.listAreaByExample(metaAreaVO);
//		return PubUtil.isEmpty(listArea)?null:listArea.get(0);
	}
	
	
	/**
	 * 根据条件，查询库区
	 * @param metaAreaVO 查询条件
	 * @return 库区对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:55:50<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaArea> listAreaByExample ( MetaAreaVO metaAreaVO ) throws Exception {
		if ( metaAreaVO == null || metaAreaVO.getArea() == null ) {
			log.error("listAreaByExample --> metaAreaVO or area is null!");
			return null;
		}
		metaAreaVO.setLoginUser();
		Example example = metaAreaVO.getExample();
		return this.areaDao.selectByExample(example);
	}
	
}
