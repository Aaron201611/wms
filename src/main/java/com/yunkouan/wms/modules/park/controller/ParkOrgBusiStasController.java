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
import com.yunkouan.exception.BizException;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.modules.park.service.IParkOrgBusiStasService;
import com.yunkouan.wms.modules.park.vo.ParkOrgBusiStasVo;

/**
 * 企业业务统计控制类
 *@Description TODO
 *@author Aaron
 *@date 2017年3月9日 下午7:37:50
 *version v1.0
 */
@Controller

@RequestMapping("${adminPath}/busiStas")
@RequiresPermissions(value = { "busiStas.view" })
public class ParkOrgBusiStasController extends BaseController{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IParkOrgBusiStasService parkOrgBusiStasService;
	
	/**
	 * 查询统计分页信息
	 * @param parkOrgBusiStasVo
	 * @return
	 * @throws Exception
	 * @version 2017年3月9日 下午7:52:04<br/>
	 * @author Aaron He<br/>
	 */
	@RequestMapping(value = "/qryList", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel qryList(@RequestBody ParkOrgBusiStasVo parkOrgBusiStasVo) throws Exception {
		if(parkOrgBusiStasVo == null)
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		ResultModel rm = new ResultModel();
		rm = parkOrgBusiStasService.qryPageList(parkOrgBusiStasVo);
		
		return rm;
		
	}

}
