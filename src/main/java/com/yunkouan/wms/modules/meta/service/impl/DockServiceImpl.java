/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日上午10:57:48<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.entity.MetaDock;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.IDockExtlService;
import com.yunkouan.wms.modules.meta.service.IDockService;
import com.yunkouan.wms.modules.meta.vo.MetaDockVO;

/**
 * 月台业务类<br/><br/>
 * @version 2017年3月13日上午10:57:48<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class DockServiceImpl extends BaseService implements IDockService {

	/** 日志对象 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/** 月台外调业务类 <br/> add by andy */
	@Autowired
	private IDockExtlService dockExtlService;
	
	/**  <br/> add by andy wang */
	@Autowired
	private IAreaExtlService areaExtlService;

	

	/**
	 * 更新并生效仓库
	 * @param param_warehouseVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO updateAndEnable ( MetaDockVO param_DockVO ) throws Exception {
		if ( param_DockVO == null || param_DockVO.getDock() == null ) {
    		log.error("updateAndEnable --> param_DockVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		this.updateDock(param_DockVO);
		this.activeDock(Arrays.asList(param_DockVO.getDock().getDockId()));
		param_DockVO.getDock().setDockStatus(Constant.DOCK_STATUS_ACTIVE);
		return param_DockVO;
	}
	
	/**
	 * 保存并生效仓库
	 * @param param_warehouseVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO addAndEnable ( MetaDockVO param_DockVO ) throws Exception {
		if ( param_DockVO == null || param_DockVO.getDock() == null ) {
    		log.error("updateAndEnable --> param_DockVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		MetaDockVO insertDockVO = this.insertDock(param_DockVO);
		if ( insertDockVO == null 
				|| StringUtil.isTrimEmpty(insertDockVO.getDock().getDockId()) ) {
			throw new NullPointerException("addAndEnable return is null!");
		}
		this.activeDock(Arrays.asList(param_DockVO.getDock().getDockId()));
		param_DockVO.getDock().setDockStatus(Constant.DOCK_STATUS_ACTIVE);
		return insertDockVO;
	}
	
	
	
	/**
	 * 更新月台
	 * @param param_dockVO 月台信息
	 * @throws Exception
	 * @version 2017年3月13日上午10:57:48<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public void updateDock(MetaDockVO param_dockVO) throws Exception {
		if (param_dockVO == null || param_dockVO.getDock() == null) {
			log.error("updatedock --> param_dockVO or dock is null!");
			throw new NullPointerException("parameter is null!");
		}
		// 查询月台编号是否有重复
		MetaDock existsdock = this.dockExtlService.findDockByNo(param_dockVO.getDock().getDockNo(),param_dockVO.getDock().getDockId());
		if (existsdock != null) {
			throw new BizException("err_main_dock_no_exists");
		}
		MetaDock param_dock = param_dockVO.getDock();
		MetaDock dock = this.dockExtlService.findDockById(param_dock.getDockId());
		if (dock.getDockStatus() == null || Constant.DOCK_STATUS_OPEN != dock.getDockStatus()) {
			throw new BizException("err_main_dock_update_statusNotOpen");
		}
		// 更新仓库
		this.dockExtlService.updateDock(param_dockVO);
	}

	/**
	 * 失效月台
	 * @param param_listdockId 月台id集合
	 * @throws Exception
	 * @version 2017年3月13日上午10:57:48<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public void inactiveDock(List<String> param_listdockId) throws Exception {
		if (param_listdockId == null || param_listdockId.isEmpty()) {
			log.error("inactivedock --> param_listdockId is null!");
			throw new NullPointerException("parameter is null!");
		}

		for (int i = 0; i < param_listdockId.size(); i++) {
			String dockId = param_listdockId.get(i);
			if (StringUtil.isTrimEmpty(dockId)) {
				throw new NullPointerException("parameter id is null");
			}
			// 查询仓库
			MetaDock dock = this.dockExtlService.findDockById(dockId);
			if (dock == null) {
				throw new BizException("err_main_dock_null");
			}

			if (Constant.DOCK_STATUS_ACTIVE != dock.getDockStatus()) {
				throw new BizException("err_main_dock_inactive_statusNotActive");
			}

			// 更新状态
			this.dockExtlService.updateDockStatusById(dockId, Constant.DOCK_STATUS_OPEN);
		}
	}

	/**
	 * 生效月台
	 * @param param_listdockId 月台id集合
	 * @throws Exception
	 * @version 2017年3月13日上午10:57:48<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public void activeDock(List<String> param_listdockId) throws Exception {
		if (param_listdockId == null || param_listdockId.isEmpty()) {
			log.error("activedock --> param_listdockId is null!");
			throw new NullPointerException("parameter is null!");
		}

		for (int i = 0; i < param_listdockId.size(); i++) {
			String dockId = param_listdockId.get(i);
			if (StringUtil.isTrimEmpty(dockId)) {
				throw new NullPointerException("parameter id is null");
			}
			// 查询仓库
			MetaDock dock = this.dockExtlService.findDockById(dockId);
			if (dock == null) {
				throw new BizException("err_main_dock_null");
			}

			if (Constant.DOCK_STATUS_OPEN != dock.getDockStatus()) {
				throw new BizException("err_main_dock_active_statusNotOpen");
			}
			

        	// 201700620 增加逻辑判断库位所属库区状态，如果为非生效，不能进行生效操作
        	MetaArea area = this.areaExtlService.findAreaById(dock.getAreaId());
        	if ( area == null ) {
        		throw new BizException("err_main_area_null");
        	}
        	if ( Constant.AREA_STATUS_ACTIVE != area.getAreaStatus() ) {
        		throw new BizException("err_main_location_active_areaNotActive");
        	}

			// 更新状态
			this.dockExtlService.updateDockStatusById(dockId, Constant.DOCK_STATUS_ACTIVE);
		}
	}

	/**
	 * 根据月台id，查询月台信息
	 * @param param_dockId 月台id
	 * @return 月台信息
	 * @throws Exception
	 * @version 2017年3月13日上午10:57:48<br/>
	 * @author andy wang<br/>
	 */
	@Override
	@Transactional(readOnly = true)
	public MetaDockVO viewDock(String param_dockId) throws Exception {
		if (StringUtil.isTrimEmpty(param_dockId)) {
			log.error("viewdock --> param_dockId is null!");
			throw new NullPointerException("parameter is null!");
		}
		// 根据id，查询月台
		MetaDock dock = this.dockExtlService.findDockById(param_dockId);
		if (dock == null) {
			// 月台不存在
			throw new BizException("err_main_dock_null");
		}
		return new MetaDockVO(dock);
	}

	/**
	 * 保存月台
	 * @param param_dockVO 要保存的月台
	 * @return 保存后的月台（包含id）
	 * @throws Exception
	 * @version 2017年3月13日上午10:57:48<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public MetaDockVO insertDock(MetaDockVO param_dockVO) throws Exception {
		if (param_dockVO == null || param_dockVO.getDock() == null) {
			log.error("insertdock --> param_dockVO is null!");
			throw new NullPointerException("parameter is null!");
		}
		MetaDock dock = param_dockVO.getDock();
		// 查询月台编号是否有重复
		MetaDock existsdock = this.dockExtlService.findDockByNo(dock.getDockNo(),null);
		if (existsdock != null) {
			throw new BizException("err_main_dock_no_exists");
		}
		// 设置id
		String dockId = IdUtil.getUUID();
		dock.setDockId(dockId);
		// 设置默认状态
		dock.setDockStatus(Constant.DOCK_STATUS_OPEN);
		// 保存月台
		MetaDock metaDock = this.dockExtlService.insertDock(dock);
		if (metaDock == null) {
			return null;
		}
		return new MetaDockVO(metaDock);
	}

	/**
	 * 根据条件，分页查询月台
	 * @param param_metaDockVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日上午10:57:48<br/>
	 * @author andy wang<br/>
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public Page listDock(MetaDockVO param_metaDockVO) throws Exception {
		if (param_metaDockVO == null) {
			param_metaDockVO = new MetaDockVO();
		}
		if (param_metaDockVO.getDock() == null) {
			param_metaDockVO.setDock(new MetaDock());
		}
		if ( !StringUtil.isTrimEmpty(param_metaDockVO.getLikeAreaName()) ) {
			List<MetaArea> listArea = this.areaExtlService.listAreaByName(param_metaDockVO.getLikeAreaName(), true);
			if ( PubUtil.isEmpty(listArea) ) {
				return null;
			}
			List<String> listAreaId = new ArrayList<String>();
			for (MetaArea metaArea : listArea) {
				listAreaId.add(metaArea.getAreaId());
			}
			param_metaDockVO.setListAreaId(listAreaId);
		}
		Page page = this.dockExtlService.listDockByPage(param_metaDockVO);
		if (PubUtil.isEmpty(page)) {
			return null;
		}
		List<MetaDockVO> listdockVO = new ArrayList<MetaDockVO>();
		FqDataUtils fq = new FqDataUtils();
		for (Object obj : page) {
			MetaDock metaDock = (MetaDock) obj;
			MetaDockVO metaDockVO = new MetaDockVO(metaDock).searchCache();
			listdockVO.add(metaDockVO);
			// 获取月台信息
			metaDockVO.setAreaComment(fq.getAreaNameById(this.areaExtlService, metaDock.getAreaId()));
		}
		page.clear();
		page.addAll(listdockVO);
		return page;
	}

}