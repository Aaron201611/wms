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
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.saas.modules.sys.service.IAdminService;
import com.yunkouan.saas.modules.sys.vo.AdminVo;
import com.yunkouan.valid.ValidSearch;

/**
 * 管理员帐号控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"people.view"})
@RequestMapping("${adminPath}/admin")
public class AdminController extends BaseController {
	protected static Log log = LogFactory.getLog(AdminController.class);

	/**管理员帐号服务接口*/
	@Autowired
    private IAdminService service;

	public AdminController() {
		if(log.isDebugEnabled()) log.debug("构建【管理员帐号控制器】类实例");
	}

	/**
     * 管理员帐号列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody AdminVo vo, BindingResult br)  {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
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
     * 查看管理员帐号详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody AdminVo vo)  {
        try {
        	return service.view(vo.getEntity().getAdminId());
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
     * 添加管理员帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody AdminVo vo, BindingResult br)  {
//        try {
//    		if ( br.hasErrors() ) {
//    			return super.handleValid(br);
//    		}
//    		//当前登录用户信息
//    		Principal p = LoginUtil.getLoginUser();
//    		com.yunkouan.saas.common.shiro.SystemAuthorizingRealm.Principal p1 = new com.yunkouan.saas.common.shiro.SystemAuthorizingRealm.Principal();
//    		BeanUtils.copyProperties(p, p1);
//			return service.add(vo, p1);
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
     * 修改管理员帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody AdminVo vo, BindingResult br)  {
//        try {
//    		if ( br.hasErrors() ) {
//    			return super.handleValid(br);
//    		}
//    		//当前登录用户信息
//    		Principal p = LoginUtil.getLoginUser();
//    		com.yunkouan.saas.common.shiro.SystemAuthorizingRealm.Principal p1 = new com.yunkouan.saas.common.shiro.SystemAuthorizingRealm.Principal();
//    		BeanUtils.copyProperties(p, p1);
//			return service.update(vo, p1);
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
     * 生效管理员帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
//	@RequestMapping(value = "/enable", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel enable(@RequestBody AdminVo vo)  {
//		try {
//			return service.enable(vo.getEntity().getAdminId());
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
     * 失效管理员帐号
     * @throws ServiceException 
     * @throws DaoException 
     */
//	@RequestMapping(value = "/disable", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel disable(@RequestBody AdminVo vo)  {
//		try {
//			return service.disable(vo.getEntity().getAdminId());
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
     * 取消管理员帐号
     * @throws ServiceException 
     * @throws DaoException 
     */
//	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel cancel(@RequestBody AdminVo vo)  {
//		try {
//			return service.cancel(vo.getEntity().getAdminId());
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