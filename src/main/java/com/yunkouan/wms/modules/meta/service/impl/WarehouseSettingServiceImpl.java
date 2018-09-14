package com.yunkouan.wms.modules.meta.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.dao.IWarehouseSettingDao;
import com.yunkouan.wms.modules.meta.entity.MetaWarehouseSetting;
import com.yunkouan.wms.modules.meta.service.IWarehouseSettingService;
import com.yunkouan.wms.modules.meta.vo.MetaWarehouseSettingVo;

/**
 * 仓库设置服务实现
 * @author Aaron
 *
 */
@Service
public class WarehouseSettingServiceImpl extends BaseService implements IWarehouseSettingService{

	
	private Logger log = LoggerFactory.getLogger(WarehouseSettingServiceImpl.class);
	
	@Autowired
	private IWarehouseSettingDao warehouseSettingDao;
	
	/**
	 * 新增
	 * @param settingVo
	 * @param p
	 */
	public MetaWarehouseSettingVo add(MetaWarehouseSettingVo settingVo,Principal p) throws Exception{
		if(settingVo == null) return null;
		settingVo.defaultValue();
		settingVo.getEntity().setWhSetId(IdUtil.getUUID());
		settingVo.getEntity().setOrgId(p.getOrgId());
		settingVo.getEntity().setCreatePerson(p.getUserId());
		settingVo.getEntity().setUpdatePerson(p.getUserId());
		settingVo.getEntity().setUpdateTime(new Date());
		settingVo.getEntity().setCreateTime(new Date());
		warehouseSettingDao.insertSelective(settingVo.getEntity());
		return settingVo;
	}
	
	/**
	 * 修改
	 * @param settingVo
	 * @param p
	 */
	public MetaWarehouseSettingVo update(MetaWarehouseSettingVo settingVo,Principal p) throws Exception{
		if(settingVo == null) return null;
		
		settingVo.getEntity().setUpdatePerson(p.getUserId());
		settingVo.getEntity().setUpdateTime(new Date());
		warehouseSettingDao.updateByPrimaryKeySelective(settingVo.getEntity());
		
		return settingVo;
	}
	
	/**
	 * 查询
	 * @param settingVo
	 * @return
	 */
	public MetaWarehouseSettingVo findByWarehouseId(String warehouseId){
		MetaWarehouseSettingVo paramVo = new MetaWarehouseSettingVo(new MetaWarehouseSetting());
		paramVo.getEntity().setWarehouseId(warehouseId);
		List<MetaWarehouseSetting> list = warehouseSettingDao.selectByExample(paramVo.getExample());
		if(list == null || list.isEmpty()) {
			paramVo.getEntity().setLimitType(1);
			paramVo.getEntity().setLimitHigh(0d);
			paramVo.getEntity().setLimitLow(0d);
			
			return paramVo;
		}
		MetaWarehouseSettingVo settingVo = new MetaWarehouseSettingVo(list.get(0));
		
		return settingVo;
	}
	
	/**
	 * 详情
	 * @param settingId
	 * @return
	 */
	public MetaWarehouseSettingVo view(String settingId){
		MetaWarehouseSetting entity = warehouseSettingDao.selectByPrimaryKey(settingId);
		MetaWarehouseSettingVo settingVo = new MetaWarehouseSettingVo(entity);
		return settingVo;
	}
	
	/**
	 * 查询仓库配置列表
	 * @param settingVo
	 * @return
	 */
	public List<MetaWarehouseSetting> qryByParam(MetaWarehouseSettingVo settingVo){
		List<MetaWarehouseSetting> list = warehouseSettingDao.selectByExample(settingVo.getExample());
		return list;
	}
	
}
