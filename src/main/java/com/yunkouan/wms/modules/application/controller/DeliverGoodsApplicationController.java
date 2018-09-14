package com.yunkouan.wms.modules.application.controller;

import java.util.List;

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
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;

/**
 * 申请单管理控制类
 * @author Aaron
 *
 */
@Controller

@RequestMapping("${adminPath}/application")
@RequiresPermissions(value = { "application.view" })
public class DeliverGoodsApplicationController extends BaseController{
	
	@Autowired
	private IDeliverGoodsApplicationService DeliverGoodsApplicationService;

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
    public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody DeliverGoodsApplicationVo applicationVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	DeliverGoodsApplicationVo vo = this.DeliverGoodsApplicationService.saveAndEnable(applicationVo, p);
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	
	/**
	 * 新增或修改
	 * @param applicationVo
	 * @param br
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addOrUpdate( @Validated(value = { ValidSave.class }) @RequestBody DeliverGoodsApplicationVo applicationVo 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	DeliverGoodsApplicationVo vo = DeliverGoodsApplicationService.addOrUpdate(applicationVo, p);
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
    	return rm;
	}
	
	/**
	 * 查询
	 * @param applicationVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel pageList(@RequestBody DeliverGoodsApplicationVo applicationVo) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	applicationVo.getEntity().setOrgId(p.getOrgId());
    	applicationVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
    	ResultModel rm = this.DeliverGoodsApplicationService.pageList(applicationVo);
    	return rm;
	}
	
	/**
	 * 查询没有核放审批的列表
	 * @param applicationVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qryUnexamineList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel qryUnexamineList(@RequestBody DeliverGoodsApplicationVo applicationVo) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	List<DeliverGoodsApplicationVo> list = DeliverGoodsApplicationService.qryAllUnexamine(p);
    	ResultModel rm = new ResultModel();
    	rm.setList(list);
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

		DeliverGoodsApplicationVo applicationVo = this.DeliverGoodsApplicationService.view(id);
    	ResultModel rm = new ResultModel();
    	rm.setObj(applicationVo);
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
    public ResultModel enable(@RequestBody List<String> listId) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.DeliverGoodsApplicationService.enable(listId, p);
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
    public ResultModel disable(@RequestBody List<String> listId) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.DeliverGoodsApplicationService.disable(listId, p);
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
    public ResultModel cancel(@RequestBody List<String> listId) throws Exception {
    	//获取登录用户
    	Principal p = LoginUtil.getLoginUser();
    	this.DeliverGoodsApplicationService.cancel(listId, p);
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
    	this.DeliverGoodsApplicationService.sendApplication(id, p);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
}
