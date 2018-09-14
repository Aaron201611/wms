package com.yunkouan.wms.modules.sys.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.service.IAuthService;
import com.yunkouan.saas.modules.sys.vo.AuthVo;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.constant.ErrorCode;

/**
* @Description: 权限控制类【限企业权限/3级权限】
* @author tphe06
* @date 2017年3月15日
*/
@Controller

@RequiresPermissions(value = {"rights.view"})
@RequestMapping("${adminPath}/auth")
public class AuthController extends BaseController {
	protected static Log log = LogFactory.getLog(AuthController.class);

	/**权限服务接口*/
    @Autowired
    private IAuthService service;

    /**
     * 权限列表数据查询【限企业权限/3级权限】
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody AuthVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			return service.list(vo);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
	}

    /**
     * 查看权限详情【限企业权限/3级权限】
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody AuthVo vo)  {
//		ResultModel r = new ResultModel();
        try {
        	return service.view(vo.getEntity().getAuthId());
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
}