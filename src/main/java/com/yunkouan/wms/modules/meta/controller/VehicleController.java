package com.yunkouan.wms.modules.meta.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.IVehicleService;
import com.yunkouan.wms.modules.meta.vo.VehicleVo;

/**
 * 车辆管理控制类
 * @author Aaron
 *
 */
@Controller

@RequestMapping("${adminPath}/vehicle")
@RequiresPermissions(value = { "vehicle.view" })
public class VehicleController extends BaseController {

	@Autowired
	private IVehicleService vehicleService;
	
	/**
	 * 保存并生效
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody VehicleVo vehicleVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	VehicleVo vo = this.vehicleService.saveAndEnable(vehicleVo, p);
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	

	/**
	 * 新增或修改
	 * @param vehicleVo
	 * @param br
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addOrUpdate( @Validated(value = { ValidSave.class }) @RequestBody VehicleVo vehicleVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	VehicleVo vo = null;
    	if(StringUtil.isNotBlank(vehicleVo.getEntity().getId())){
    		vo = vehicleService.update(vehicleVo,p);
    	}else{
    		vo = vehicleService.add(vehicleVo,p);
    	}
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	

	/**
	 * 查询
	 * @param vehicleVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel pageList(@RequestBody VehicleVo vehicleVo) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	vehicleVo.getEntity().setOrgId(p.getOrgId());
    	vehicleVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
    	ResultModel rm = this.vehicleService.pageList(vehicleVo);
    	return rm;
	}
	
	/**
	 * 生效
	 * @param vehicleVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel enable(@RequestBody String id) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.vehicleService.enable(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 失效
	 * @param vehicleVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel disable(@RequestBody String id) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.vehicleService.disable(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 取消
	 * @param vehicleVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel cancel(@RequestBody String id) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.vehicleService.cancel(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	
	
	
}
