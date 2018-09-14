/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日上午11:36:49<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.meta.entity.MetaDock;
import com.yunkouan.wms.modules.meta.vo.MetaDockVO;

/**
 * 月台外调接口<br/><br/>
 * @version 2017年3月13日上午11:36:49<br/>
 * @author andy wang<br/>
 */
public interface IDockExtlService {

	/**
	 * 根据条件，分页查询月台
	 * @param metaDockVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listDockByPage(MetaDockVO metaDockVO) throws Exception;

	/**
	 * 根据月台编号，查询月台
	 * @param areaNo 月台编号
	 * @return 月台对象
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock findDockByNo(String dockNo, String dockId) throws Exception;

	/**
	 * 根据月台名，查询月台
	 * @param areaName 月台名
	 * @return 月台
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock findDockByName(String areaName) throws Exception;

	/**
	 * 保存月台
	 * @param metaDock 要保存的月台对象
	 * @return 保存后的月台对象
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock insertDock(MetaDock metaDock) throws Exception;

	/**
	 * 更新月台
	 * @param areaVO 更新的月台内容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public int updateDock(MetaDockVO areaVO) throws Exception;

	/**
	 * 根据月台id，更新月台状态
	 * @param areaId 月台id
	 * @param status 月台状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public int updateDockStatusById(String areaId, Integer status) throws Exception;

	/**
	 * 根据月台id，查询月台信息
	 * @param areaId 月台id
	 * @return 月台信息
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public MetaDock findDockById(String areaId) throws Exception;

	/**
	 * 根据条件，查询月台
	 * @param metaDockVO 查询条件
	 * @return 月台对象
	 * @throws Exception
	 * @version 2017年3月13日上午11:36:49<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaDock> listDockByExample(MetaDockVO metaDockVO) throws Exception;

}
 