package com.yunkouan.wms.modules.meta.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.IWarehouseSettingService;
import com.yunkouan.wms.modules.meta.vo.MetaWarehouseSettingVo;

import tk.mybatis.mapper.util.StringUtil;

/**
 * 仓库设置控制
 * @author Aaron
 *
 */
@Controller

@RequestMapping("${adminPath}/warehouseSetting")
@RequiresPermissions(value = { "warehouse.view" })
public class WarehouseSettingController extends BaseController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public IWarehouseSettingService warehouseSettingService;
	
	
	/**
	 * 保存或者新增
	 * @param warehouseSettingVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel addOrUpdate(@Validated(value = { ValidSave.class })@RequestBody MetaWarehouseSettingVo warehouseSettingVo, BindingResult br)throws Exception{
		
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
		MetaWarehouseSettingVo recordVo = null;
		if(StringUtil.isEmpty(warehouseSettingVo.getEntity().getWhSetId())){
			recordVo = warehouseSettingService.add(warehouseSettingVo, p);
		}else{
			recordVo = warehouseSettingService.update(warehouseSettingVo, p);
		}
		ResultModel rm = new ResultModel();
		rm.setObj(recordVo);
		return rm;
	}
	
	/**
	 * 新增仓库配置
	 * @param warehouseSettingVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add(@Validated(value = { ValidSave.class })@RequestBody MetaWarehouseSettingVo warehouseSettingVo)throws Exception{
		
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
		MetaWarehouseSettingVo recordVo = warehouseSettingService.add(warehouseSettingVo, p);
		ResultModel rm = new ResultModel();
		rm.setObj(recordVo);
		return rm;
	}
	
	/**
	 * 更新
	 * @param warehouseSettingVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@RequestBody MetaWarehouseSettingVo warehouseSettingVo)throws Exception{
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
		MetaWarehouseSettingVo recordVo = warehouseSettingService.update(warehouseSettingVo, p);
		ResultModel rm = new ResultModel();
		rm.setObj(recordVo);
		return rm;
	}
	
	/**
	 * 查看仓库设置
	 * @param warehouseId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findWarehouseSetting", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel findWarehouseSetting()throws Exception{
		//获取登录用户
		Principal p = LoginUtil.getLoginUser();
		MetaWarehouseSettingVo recordVo = warehouseSettingService.findByWarehouseId(LoginUtil.getWareHouseId());
		ResultModel rm = new ResultModel();
		rm.setObj(recordVo);
		return rm;
	}
	
	
}
