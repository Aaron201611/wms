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
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.saas.modules.sys.vo.OrgVo;
import com.yunkouan.valid.ValidSearch;

/**
 * 企业控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"org.view"})
@RequestMapping("${adminPath}/org")
public class OrgController extends BaseController {
	protected static Log log = LogFactory.getLog(OrgController.class);

	/**企业服务接口*/
	@Autowired
    private IOrgService service;

	public OrgController() {
		if(log.isDebugEnabled()) log.debug("构建【企业控制器】类实例");
	}

	/**
     * 企业列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody OrgVo vo, BindingResult br)  {
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
     * 查看企业详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody OrgVo vo)  {
//		ResultModel r = new ResultModel();
        try {
        	return service.view(vo.getEntity().getOrgId());
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