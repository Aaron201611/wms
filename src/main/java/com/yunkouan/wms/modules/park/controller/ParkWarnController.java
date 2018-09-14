package com.yunkouan.wms.modules.park.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.park.service.IParkWarnService;
import com.yunkouan.wms.modules.park.vo.ParkWarnVo;

/**
 * 仓库出租告警控制类
 *@Description
 *@author Aaron
 *@date 2017年3月8日 下午3:21:50
 *version v1.0
 */
@Controller

@RequestMapping("${adminPath}/parkWarn")
@RequiresPermissions(value = { "warn.view" })
public class ParkWarnController extends BaseController{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private IParkWarnService parkWarnService;

    /**
     * 仓库出租告警记录列表数据查询
     * @param parkWarnVo
     * @version 2017年3月8日 下午3:26:29<br/>
     * @author Aaron He<br/>
     */
	@RequestMapping(value = "/qryList", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel qryList(@RequestBody ParkWarnVo parkWarnVo) throws Exception{
		ResultModel rm = new ResultModel();
		
		rm = parkWarnService.qryPageList(parkWarnVo);
		return rm;
    }

    /**
     * 查看仓库出租告警详情
     * @param warnId
     * @return
     * @throws Exception
     * @version 2017年3月8日 下午5:53:12<br/>
     * @author Aaron He<br/>
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel view(@RequestBody ParkWarnVo parkWarnVo) throws Exception{
		
		ParkWarnVo vo = parkWarnService.view(parkWarnVo.getParkWarn().getWarnId());
		ResultModel rm = new ResultModel();
		rm.setObj(vo);
		return rm;
    }

    /**
     * 关闭仓库出租告警提醒
     * @param warnId
     * @throws Exception
     * @version 2017年3月8日 下午5:53:21<br/>
     * @author Aaron He<br/>
     */
	@OpLog(model = OpLog.MODEL_PARK_RENTWARN, type = "关闭", pos = 0)
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel close(@RequestBody ParkWarnVo parkWarnVo) throws Exception{
		
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String operator = loginUser.getUserId();
		
		parkWarnService.close(parkWarnVo.getParkWarn().getWarnId(), operator);
		ResultModel rm = new ResultModel();
		return rm;
    }

}