package com.yunkouan.wms.modules.meta.controller;

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
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.IWorkGroupService;
import com.yunkouan.wms.modules.meta.vo.WorkGroupVo;

/**
 * 【班组】控制类
 */
@Controller
@RequiresPermissions(value = {"workGroup.view"})
@RequestMapping("${adminPath}/workgroup")
public class WorkGroupController extends BaseController {
	protected static Log log = LogFactory.getLog(WorkGroupController.class);

	/**班组服务接口**/
	@Autowired
	private IWorkGroupService service;

    /**
     * 班组列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody WorkGroupVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
		try {
			if(br.hasErrors()) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
			return service.list(vo, p);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
	}

    /**
     * 查看班组详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody WorkGroupVo vo)  {
//		ResultModel r = new ResultModel();
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(vo.getEntity().getWorkGroupId(), p);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 添加班组
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody WorkGroupVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if(br.hasErrors()) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.add(vo, p);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 修改班组
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody WorkGroupVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if(br.hasErrors()) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.update(vo, p);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 生效班组
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody WorkGroupVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.enable(vo.getEntity().getWorkGroupId());
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 失效班组
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody WorkGroupVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.disable(vo.getEntity().getWorkGroupId());
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 取消班组
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody WorkGroupVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.cancel(vo.getEntity().getWorkGroupId());
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
//			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
//			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }
}