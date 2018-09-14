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
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.vo.SkuTypeVo;

/**
 * 货品类型控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"skuType.view"})
@RequestMapping("${adminPath}/skuType")
public class SkuTypeController extends BaseController {
	protected static Log log = LogFactory.getLog(SkuTypeController.class);

	/**货品类型服务接口*/
	@Autowired
    private ISkuTypeService service;

    /**
     * 货品类型列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody SkuTypeVo vo, BindingResult br)  {
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
     * 顶级货品类型列表数据查询（用于货品类型下来选择框）
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/toplist", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel toplist()  {
		try {
			Principal p = LoginUtil.getLoginUser();
			return service.toplist(p);
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
     * 货品类型列表数据查询（用于货品类型下拉选择框）
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/selectList", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel selectList(@RequestBody SkuTypeVo vo)  {
		try {
			Principal p = LoginUtil.getLoginUser();
			return service.selectList(vo, p);
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
     * 查看货品类型详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody SkuTypeVo vo)  {
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(vo.getEntity().getSkuTypeId(), p);
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
     * 添加货品类型
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU_TYPE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody SkuTypeVo vo, BindingResult br)  {
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
     * 修改货品类型
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU_TYPE, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody SkuTypeVo vo, BindingResult br)  {
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
     * 保存并生效货品类型
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU_TYPE, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable(@Validated(value = { ValidSave.class }) @RequestBody SkuTypeVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
			return service.saveAndEnable(vo);
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
     * 生效货品类型
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU_TYPE, type = OpLog.OP_TYPE_ENABLE, pos = 0)
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
     * 失效货品类型
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU_TYPE, type = OpLog.OP_TYPE_DISABLE, pos = 0)
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
     * 取消货品类型 
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU_TYPE, type = OpLog.OP_TYPE_CANCEL, pos = 0)
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