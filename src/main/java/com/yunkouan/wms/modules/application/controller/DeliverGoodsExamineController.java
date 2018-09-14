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
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineVo;

/**
 * 核放单管理控制类
 * @author Aaron
 *
 */
@Controller

@RequestMapping("${adminPath}/examine")
@RequiresPermissions(value = { "examine.view" })
public class DeliverGoodsExamineController extends BaseController{

	@Autowired
	private IDeliverGoodsExamineService examineService;
	
	
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
    public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody DeliverGoodsExamineVo examineVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	DeliverGoodsExamineVo vo = this.examineService.saveAndEnable(examineVo, p);
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	
	/**
	 * 新增或修改
	 * @param examineVo
	 * @param br
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addOrUpdate( @Validated(value = { ValidSave.class }) @RequestBody DeliverGoodsExamineVo examineVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	DeliverGoodsExamineVo vo = examineService.addOrUpdate(examineVo, p);
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	
	/**
	 * 查询
	 * @param examineVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel pageList(@RequestBody DeliverGoodsExamineVo examineVo) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	examineVo.getEntity().setOrgId(p.getOrgId());
    	examineVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
    	ResultModel rm = this.examineService.pageList(examineVo);
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

		DeliverGoodsExamineVo examineVo = this.examineService.view(id);
    	ResultModel rm = new ResultModel();
    	rm.setObj(examineVo);
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
    	this.examineService.enable(id, p);
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
    	this.examineService.disable(id, p);
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
    	this.examineService.cancel(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	/**
	 * 发送到公共服务平台
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendToCust", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel sendToCust(@RequestBody String id) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.examineService.sendExamine(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
}
