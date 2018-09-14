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
import com.yunkouan.wms.modules.meta.service.IMachineService;
import com.yunkouan.wms.modules.meta.vo.MachineVo;

/**
 * 设备控制类
 */
@Controller
@RequiresPermissions(value = {"machine.view"})
@RequestMapping("${adminPath}/machine")
public class MachineController extends BaseController {
	protected static Log log = LogFactory.getLog(MachineController.class);

	/**设备服务接口**/
	@Autowired
	private IMachineService service;

    /**
     * 设备列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody MachineVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
		try {
			if ( br.hasErrors() ) {
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
     * 查看设备详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody MachineVo vo)  {
//		ResultModel r = new ResultModel();
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(vo.getEntity().getMachineId(), p);
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
     * 添加设备
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody MachineVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if ( br.hasErrors() ) {
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
     * 修改设备
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody MachineVo vo, BindingResult br)  {
//		ResultModel r = new ResultModel();
        try {
    		if ( br.hasErrors() ) {
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
     * 生效设备
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody MachineVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.enable(vo.getEntity().getMachineId());
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
     * 失效设备
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody MachineVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.disable(vo.getEntity().getMachineId());
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
     * 取消设备
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody MachineVo vo)  {
//		ResultModel r = new ResultModel();
		try {
			return service.cancel(vo.getEntity().getMachineId());
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