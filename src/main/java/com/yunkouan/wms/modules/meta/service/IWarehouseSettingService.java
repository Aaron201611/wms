package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaWarehouseSetting;
import com.yunkouan.wms.modules.meta.vo.MetaWarehouseSettingVo;

/**
 * 仓库设置接口
 * @author Aaron
 *
 */
public interface IWarehouseSettingService {

	/**
	 * 新增
	 * @param settingVo
	 * @param p
	 */
	public MetaWarehouseSettingVo add(MetaWarehouseSettingVo settingVo,Principal p) throws Exception;
	
	/**
	 * 修改
	 * @param settingVo
	 * @param p
	 */
	public MetaWarehouseSettingVo update(MetaWarehouseSettingVo settingVo,Principal p) throws Exception;
	
	/**
	 * 查询
	 * @param settingVo
	 * @return
	 */
	public MetaWarehouseSettingVo findByWarehouseId(String warehouseId);
	
	/**
	 * 详情
	 * @param settingId
	 * @return
	 */
	public MetaWarehouseSettingVo view(String settingId);
	
	/**
	 * 查询仓库配置列表
	 * @param settingVo
	 * @return
	 */
	public List<MetaWarehouseSetting> qryByParam(MetaWarehouseSettingVo settingVo);
}
