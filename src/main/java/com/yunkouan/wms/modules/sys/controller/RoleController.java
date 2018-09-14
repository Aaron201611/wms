package com.yunkouan.wms.modules.sys.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.service.IRoleService;
import com.yunkouan.wms.modules.sys.vo.RoleVo;

/**
 * 角色控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"role.view"})
@RequestMapping("${adminPath}/role")
public class RoleController extends BaseController {
	protected static Log log = LogFactory.getLog(RoleController.class);

	/**角色服务接口*/
	@Autowired
    private IRoleService service;

    /**
     * 角色列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody RoleVo vo, BindingResult br, HttpServletRequest req)  {
//		ResultModel r = new ResultModel();
		try {
			if(req != null) {
				if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			}
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
			return service.list(vo, p);
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
     * 查看角色详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody RoleVo vo)  {
//		ResultModel r = new ResultModel();
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(vo.getEntity().getRoleId(), p);
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
     * 添加角色
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ROLE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody RoleVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.add(vo, p);
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
     * 添加并生效角色
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ROLE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody RoleVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
			return service.saveAndEnable(vo);
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
     * 修改角色
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ROLE, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody RoleVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.update(vo, p);
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
     * 生效角色
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ROLE, type = OpLog.OP_TYPE_ENABLE)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody RoleVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.enable(vo.getEntity().getRoleId());
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
     * 失效角色
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ROLE, type = OpLog.OP_TYPE_DISABLE)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody RoleVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.disable(vo.getEntity().getRoleId());
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
     * 取消角色
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ROLE, type = OpLog.OP_TYPE_CANCEL)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody RoleVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.cancel(vo.getEntity().getRoleId());
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
     * 权限列表【限当前登录用户所属组织所拥有的权限】
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/authList", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel authList(@RequestBody RoleVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			Principal p = LoginUtil.getLoginUser();
			return service.tree(p);
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