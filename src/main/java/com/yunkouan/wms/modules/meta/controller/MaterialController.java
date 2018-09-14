package com.yunkouan.wms.modules.meta.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
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
import com.yunkouan.wms.modules.inv.vo.InvLogVO;
import com.yunkouan.wms.modules.meta.service.IMaterialService;
import com.yunkouan.wms.modules.meta.vo.MaterialLogVo;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;

/**
 * 辅材控制类
 * @author 
 */
@Controller

@RequiresPermissions(value = {"material.view"})
@RequestMapping("${adminPath}/material")
public class MaterialController extends BaseController {
	protected static Log log = LogFactory.getLog(MaterialController.class);

	/**辅材服务接口*/
	@Autowired
    private IMaterialService service;

    /**
     * 辅材列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody MaterialVo vo, BindingResult br)  {
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
	}

    /**
     * 查看辅材详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody String id)  {
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(id, p);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 保存并生效辅材
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody MaterialVo vo, BindingResult br)  {
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
	
    /**
     * 添加辅材
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody MaterialVo vo, BindingResult br)  {
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 修改辅材
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody MaterialVo vo, BindingResult br)  {
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 生效辅材
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_ENABLE, pos = 0)
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 失效辅材
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_DISABLE, pos = 0)
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }

    /**
     * 取消辅材
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_CANCEL, pos = 0)
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
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
	
	/**
     * 修改辅材数量
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/changeQty", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel changeQty(@RequestBody MaterialVo vo)  {
		try {
			return service.changeQty(vo);
		} catch (DaoException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
	
    /**
     * 库存日志列表数据查询
     */
    @RequestMapping(value = "/logList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel logList(@RequestBody MaterialLogVo logVo) throws Exception  {
    	Page<MaterialLogVo> list = this.service.logList(logVo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }
    
    /**
     * 批量导入数据
     * @throws Exception 
     */
    @OpLog(model = OpLog.MODEL_META_SKU, type = "导入", pos = -2)
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = { "application/json" })
 	@ResponseBody
    public ResultModel importMaterial(@RequestParam(value = "file", required = true) MultipartFile file ) throws Exception {
		this.service.importMaterial(file);
		ResultModel rm = new ResultModel();
		rm.setStatus(0);
		return rm;
    }
    
    /**
     * 下载文件
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> download() throws Exception {
    	return this.service.downloadMaterialDemo();
	}
}