package com.yunkouan.wms.modules.meta.controller;

import java.util.List;

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
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.vo.MerchantVo;

/**
 * 客商控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"merchant.view"})
@RequestMapping("${adminPath}/merchant")
public class MerchantController extends BaseController {
	protected static Log log = LogFactory.getLog(MerchantController.class);

	/**客商服务接口*/
	@Autowired
    private IMerchantService service;

    /**
     * 客商列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody MerchantVo vo, BindingResult br)  {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
			return service.list(vo, p);
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
     * 查看客商详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody MerchantVo vo)  {
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(vo.getEntity().getMerchantId(), p);
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
     * 添加客商
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody MerchantVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.add(vo, p);
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
     * 添加并激活客商
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MerchantVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.addAndEnable(vo, p);
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
     * 添加并激活客商
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MerchantVo vo, BindingResult br)  {
        try {
        	if (StringUtil.isTrimEmpty(vo.getEntity().getMerchantId())) {
    			return this.addAndEnable(vo, br);
        	} else {
    			return this.updateAndEnable(vo, br);
        	}
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
    /**
     * 修改客商
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody MerchantVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.update(vo, p);
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
     * 修改并激活客商
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel updateAndEnable(@Validated(value = { ValidUpdate.class }) @RequestBody MerchantVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.updateAndEnable(vo, p);
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
     * 生效客商
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody List<String> idList)  {
		try {
			return service.enable(idList);
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
     * 失效客商
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_DISABLE, pos = 0)
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disable(@RequestBody List<String> idList)  {
		try {
			return service.disable(idList);
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
     * 取消客商
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_MERCHANTS, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody List<String> idList)  {
		try {
			return service.cancel(idList);
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
}