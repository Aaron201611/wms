package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.github.pagehelper.Page;

import com.yunkouan.wms.modules.meta.vo.MetaDockVO;

/**
 * 月台服务接口
 */
public interface IDockService {

	Page listDock(MetaDockVO param_metaDockVO) throws Exception;

	MetaDockVO insertDock(MetaDockVO param_dockVO) throws Exception;

	MetaDockVO viewDock(String param_dockId) throws Exception;

	void activeDock(List<String> param_listdockId) throws Exception;

	void inactiveDock(List<String> param_listdockId) throws Exception;

	void updateDock(MetaDockVO param_dockVO) throws Exception;

	/**
	 * 更新并生效仓库
	 * @param param_warehouseVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO updateAndEnable ( MetaDockVO param_DockVO ) throws Exception;
	
	/**
	 * 保存并生效仓库
	 * @param param_warehouseVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaDockVO addAndEnable ( MetaDockVO param_DockVO ) throws Exception;
}