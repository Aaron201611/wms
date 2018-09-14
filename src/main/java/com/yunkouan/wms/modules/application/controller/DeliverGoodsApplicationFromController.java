package com.yunkouan.wms.modules.application.controller;

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
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationFormService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationFormVo;


/**
 * 申报表管理控制类
 * @author Aaron
 *
 */
@Controller

@RequestMapping("${adminPath}/applicationForm")
@RequiresPermissions(value = { "applicationForm.view" })
public class DeliverGoodsApplicationFromController extends BaseController{

	@Autowired
	private IDeliverGoodsApplicationFormService applicationFormService;
	
	/**
	 * 保存并生效
	 * @param br 校验对象
	 * @return 返回对象
	 * @throws Exception
	 * @version 2017年6月19日 下午3:13:34<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody DeliverGoodsApplicationFormVo fromVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	DeliverGoodsApplicationFormVo vo = this.applicationFormService.saveAndEnable(fromVo, p);
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
    public ResultModel addOrUpdate( @Validated(value = { ValidSave.class }) @RequestBody DeliverGoodsApplicationFormVo fromVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	DeliverGoodsApplicationFormVo vo = null;
    	if(StringUtil.isNotBlank(fromVo.getEntity().getId())){
    		vo = applicationFormService.update(fromVo,p);
    	}else{
    		vo = applicationFormService.add(fromVo,p);
    	}
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	
	/**
	 * 查询
	 * @param formVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel pageList(@RequestBody DeliverGoodsApplicationFormVo formVo) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	formVo.getEntity().setOrgId(p.getOrgId());
    	formVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
    	ResultModel rm = this.applicationFormService.pageList(formVo);
    	return rm;
	}
	
	/**
	 * 查看
	 * @param vehicleVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel view(@RequestBody String id) throws Exception {

		DeliverGoodsApplicationFormVo formVo = this.applicationFormService.view(id);
    	ResultModel rm = new ResultModel();
    	rm.setObj(formVo);
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
    	this.applicationFormService.enable(id, p);
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
    	this.applicationFormService.disable(id, p);
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
    	this.applicationFormService.cancel(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
}
