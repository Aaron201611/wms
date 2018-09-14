package com.yunkouan.wms.modules.sys.controller;

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
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.service.IAccountService;
import com.yunkouan.wms.modules.sys.vo.AccountVo;

/**
 * 企业帐号控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"account.view"})
@RequestMapping("${adminPath}/account")
public class AccountController extends BaseController {
    /**企业帐号服务接口*/
	@Autowired
    private IAccountService service;

    /**
     * 企业帐号列表数据查询
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody AccountVo vo, BindingResult br) {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
			return service.list(vo, p);
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
	}

    /**
     * 查看企业帐号详情
     * @throws DaoException 
     * @throws ServiceException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody AccountVo vo)  {
       try {
    	   Principal p = LoginUtil.getLoginUser();
		   return service.view(vo.getEntity().getAccountId(), p);
	   } catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
	   }
    }

    /**
     * 添加企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ORG_ACCOUNT, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody AccountVo vo, BindingResult br) {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
	        return service.add(vo, p);
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
    }
	
	 /**
     * 添加企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ORG_ACCOUNT, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody AccountVo vo, BindingResult br) {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
	        return service.saveAndEnable(vo);
		} catch(BizException e) {
			throw e;
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
    }
    /**
     * 修改企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ORG_ACCOUNT, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody AccountVo vo, BindingResult br) {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
	        return service.update(vo, p);
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
    }

    /**
     * 生效企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ORG_ACCOUNT, type = OpLog.OP_TYPE_ENABLE)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody AccountVo vo) {
		try {
			return service.enable(vo.getEntity().getAccountId());
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
    }

    /**
     * 失效企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ORG_ACCOUNT, type = OpLog.OP_TYPE_DISABLE)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody AccountVo vo) {
		try {
			return service.disable(vo.getEntity().getAccountId());
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
    }

    /**
     * 取消企业帐号
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_SYSTEM_ORG_ACCOUNT, type = OpLog.OP_TYPE_CANCEL)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody AccountVo vo) {
		try {
			return service.cancel(vo.getEntity().getAccountId());
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
    }

}