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
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.vo.UserVo;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.constant.ErrorCode;

/**
* @Description: 用户控制类【限企业级用户】
* @author tphe06
* @date 2017年3月15日
*/
@Controller

@RequiresPermissions(value = {"account.view"})
@RequestMapping("${adminPath}/user")
public class UserController extends BaseController {
	protected static Log log = LogFactory.getLog(UserController.class);

	/**用户服务接口*/
	@Autowired
    private IUserService service;

    /**
     * 用户列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody UserVo vo, BindingResult br)  {
		try {
			if(br.hasErrors()) {
				return super.handleValid(br);
			}
			if(vo.getEntity() == null) {
				vo.setEntity(new SysUser());
			}
			vo.getEntity().setIsEmployee(3);
			vo.getEntity().setOrgId(LoginUtil.getLoginUser().getOrgId());
			return service.list(vo);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
	}

    /**
     * 查看用户详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody UserVo vo)  {
        try {
        	return service.view(vo.getEntity().getUserId());
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 添加用户
     * @throws DaoException 
     * @throws ServiceException 
     */
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody UserVo vo, BindingResult br)  {
//        try {
//    		if ( br.hasErrors() ) {
//    			return super.handleValid(br);
//    		}
//			if (vo.getEntity() == null) {
//				vo.setEntity(new SysUser());
//			}
//    		vo.getEntity().setIsEmployee(3);
//    		//当前登录用户信息
//    		Principal p = LoginUtil.getLoginUser();
//			return service.add(vo, p);
//		} catch (DaoException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (ServiceException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (Throwable e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(ErrorCode.UNKNOW_ERROR);
//		}
//    }

    /**
     * 修改用户
     * @throws DaoException 
     * @throws ServiceException 
     */
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody UserVo vo, BindingResult br)  {
//        try {
//    		if ( br.hasErrors() ) {
//    			return super.handleValid(br);
//    		}
//    		//当前登录用户信息
//    		Principal p = LoginUtil.getLoginUser();
//			return service.update(vo, p);
//		} catch (DaoException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (ServiceException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (Throwable e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(ErrorCode.UNKNOW_ERROR);
//		}
//    }

    /**
     * 生效用户
     * @throws DaoException 
     * @throws ServiceException 
     */
//	@RequestMapping(value = "/enable", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel enable(@RequestBody UserVo vo)  {
//		try {
//			return service.enable(vo.getEntity().getUserId());
//		} catch (DaoException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (ServiceException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (Throwable e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(ErrorCode.UNKNOW_ERROR);
//		}
//    }

    /**
     * 失效用户
     * @throws ServiceException 
     * @throws DaoException 
     */
//	@RequestMapping(value = "/disable", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel disable(@RequestBody UserVo vo)  {
//		try {
//			return service.disable(vo.getEntity().getUserId());
//		} catch (DaoException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (ServiceException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (Throwable e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(ErrorCode.UNKNOW_ERROR);
//		}
//    }

    /**
     * 取消用户
     * @throws ServiceException 
     * @throws DaoException 
     */
//	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel cancel(@RequestBody UserVo vo)  {
//		try {
//			return service.cancel(vo.getEntity().getUserId());
//		} catch (DaoException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (ServiceException e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(e.getMessage());
//		} catch (Throwable e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//			throw new BizException(ErrorCode.UNKNOW_ERROR);
//		}
//    }
}