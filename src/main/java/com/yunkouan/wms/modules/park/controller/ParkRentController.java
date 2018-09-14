package com.yunkouan.wms.modules.park.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.StringUtil;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;

/**
 * 仓库出租控制类
 *@Description 
 *@author Aaron
 *@date 2017年3月8日 上午10:15:00
 *version v1.0
 */
@Controller

@RequestMapping("${adminPath}/parkRent")
@RequiresPermissions(value = { "rent.view" })
public class ParkRentController extends BaseController{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IParkRentService parkRentService;


	/**
	 * 仓库出租列表数据查询
	 * @param parkRentVo
	 * @version 2017年3月8日 上午10:05:31<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryList", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel qryList(@RequestBody ParkRentVo parkRentVo) throws Exception {
        
		ResultModel rm = new ResultModel();
		
		rm = parkRentService.qryPageList(parkRentVo);
		return rm;
    }

	/**
	 * 查看仓库出租详情
	 * @param parkRentVo
	 * @version 2017年3月8日 上午10:06:17<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel view(@RequestBody ParkRentVo parkRentVo) throws Exception{
        
		ResultModel rm = new ResultModel();

		ParkRentVo vo = parkRentService.view(parkRentVo.getParkRent().getRentId());
		
		rm.setObj(vo);
		return rm;
    }
	
	/**
	 * 添加仓库出租
	 * @param parkRentVo
	 * @param br
	 * @return
	 * @throws Exception
	 * @version 2017年3月8日 上午10:12:22<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_PARK_RENT, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndUpdate", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel addAndUpdate(@Validated(value ={ ValidSave.class })@RequestBody ParkRentVo parkRentVo, BindingResult br) throws Exception {
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		ParkRentVo rentVo = new ParkRentVo();
		if(StringUtil.isEmpty(parkRentVo.getParkRent().getRentId())){
			rentVo = parkRentService.add(parkRentVo, operator);
		}else{
			rentVo = parkRentService.update(parkRentVo, operator);
		}
		ResultModel rm = new ResultModel();
		rm.setObj(rentVo);
		return rm;
    }
	
	/**
	 * 添加仓库出租
	 * @param parkRentVo
	 * @param br
	 * @return
	 * @throws Exception
	 * @version 2017年3月8日 上午10:12:22<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_PARK_RENT, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel add(@Validated(value ={ ValidSave.class })@RequestBody ParkRentVo parkRentVo, BindingResult br) throws Exception {
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		ParkRentVo rentVo = new ParkRentVo();
		rentVo = parkRentService.add(parkRentVo, operator);
		ResultModel rm = new ResultModel();
		rm.setObj(rentVo);
		return rm;
    }
	
	/**
	 * 保存并生效
	 * @param parkRentVo
	 * @param br
	 * @return
	 * @throws Exception
	 */
	@OpLog(model = OpLog.MODEL_PARK_RENT, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel addAndEnable(@Validated(value ={ ValidSave.class })@RequestBody ParkRentVo parkRentVo, BindingResult br) throws Exception {
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		ParkRentVo rentVo = parkRentService.addAndEnable(parkRentVo, operator);
		ResultModel rm = new ResultModel();
		rm.setObj(rentVo);
		return rm;
    }
	
	/**
	 * 添加仓库出租
	 * @param parkRentVo
	 * @param br
	 * @return
	 * @throws Exception
	 * @version 2017年3月8日 上午10:12:22<br/>
	 * @author Aaron He<br/>
	 */
	@OpLog(model = OpLog.MODEL_PARK_RENT, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel update(@Validated(value ={ ValidSave.class })@RequestBody ParkRentVo parkRentVo, BindingResult br) throws Exception {
		if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		ParkRentVo rentVo = new ParkRentVo();
		if(!StringUtil.isEmpty(parkRentVo.getParkRent().getRentId())){
			rentVo = parkRentService.update(parkRentVo, operator);
		} 
		ResultModel rm = new ResultModel();
		rm.setObj(rentVo);
		return rm;
    }

    /**
     * 生效仓库出租
     * @param parkRentVo
     * @version 2017年3月8日 上午10:12:52<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_PARK_RENT, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel enable(@RequestBody ParkRentVo parkRentVo) throws Exception{
        
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		
		parkRentService.enable(parkRentVo.getParkRent().getRentId(), operator);
		ResultModel rm = new ResultModel();
		return rm;
    }

    /**
     * 失效仓库出租
     * @param parkRentVo
     * @version 2017年3月8日 上午10:13:20<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_PARK_RENT, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel disable(@RequestBody ParkRentVo parkRentVo) throws Exception{
    	
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		parkRentService.disable(parkRentVo.getParkRent().getRentId(), operator);
		ResultModel rm = new ResultModel();
		return rm;
    }

}