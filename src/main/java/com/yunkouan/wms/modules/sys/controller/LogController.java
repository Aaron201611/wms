package com.yunkouan.wms.modules.sys.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.service.ILogService;
import com.yunkouan.saas.modules.sys.vo.LogVo;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.constant.ErrorCode;

/**
* @Description: 日志控制类
* @author tphe06
* @date 2017年3月15日
*/
@Controller

@RequestMapping("${adminPath}/log")
public class LogController extends BaseController {
	protected static Log log = LogFactory.getLog(LogController.class);

	/**日志服务接口*/
	@Autowired
    private ILogService service;

    /**
     * 日志列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody LogVo vo, BindingResult br)  {
		ResultModel r = new ResultModel();
		try {
			if(br.hasErrors()) {
				return super.handleValid(br);
			}
			return service.list(vo);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
	}

    /**
     * 查看日志详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody LogVo vo)  {
		ResultModel r = new ResultModel();
        try {
        	return service.view(vo.getEntity().getLogId());
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return r.setError().addMessage(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return r.setError().addMessage(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return r.setError().addMessage(ErrorCode.UNKNOW_ERROR);
		}
    }
}