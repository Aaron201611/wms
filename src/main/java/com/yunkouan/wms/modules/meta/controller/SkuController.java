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
import com.yunkouan.wms.common.strategy.INoRule;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

/**
 * 货品控制类
 * @author tphe06 2017年2月14日
 */
@Controller

@RequiresPermissions(value = {"sku.view"})
@RequestMapping("${adminPath}/sku")
public class SkuController extends BaseController {
	protected static Log log = LogFactory.getLog(SkuController.class);

	/**货品服务接口*/
	@Autowired
    private ISkuService service;
	@Autowired
	private StrategyContext context;

    /**
     * 货品列表数据查询
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel list(@Validated(value = { ValidSearch.class }) @RequestBody SkuVo vo, BindingResult br)  {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
			return service.list(vo, p);
		} catch (BizException e) {
			throw e;
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
     * 查看货品详情
     * @throws ServiceException 
     * @throws DaoException 
     */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel view(@RequestBody SkuVo vo)  {
        try {
        	Principal p = LoginUtil.getLoginUser();
        	return service.view(vo.getEntity().getSkuId(), p);
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
     * 保存并生效货品
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel saveAndEnable( @Validated(value = { ValidSave.class }) @RequestBody SkuVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
			return service.saveAndEnable(vo);
		} catch (BizException e) {
			throw e;
		} catch (DaoException | ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
	
    /**
     * 添加货品
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody SkuVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.add(vo, p);
        } catch (BizException e) {
			throw e;
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
     * 修改货品
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody SkuVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.update(vo, p);
        } catch (BizException e) {
			throw e;
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
     * 修改货品
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel change(@Validated(value = { ValidUpdate.class }) @RequestBody SkuVo vo, BindingResult br)  {
        try {
    		if ( br.hasErrors() ) {
    			return super.handleValid(br);
    		}
    		Principal p = LoginUtil.getLoginUser();
			return service.update(vo, p);
        } catch (BizException e) {
			throw e;
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
     * 生效货品
     * @throws DaoException 
     * @throws ServiceException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_ENABLE, pos = 0)
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enable(@RequestBody List<String> idList)  {
		try {
			ResultModel rm = service.enable(idList);
			//同步erp
//			service.updateErpSku(1, idList);
			return rm;
			
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
     * 失效货品
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
     * 取消货品
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel cancel(@RequestBody List<String> idList)  {
		try {
			ResultModel rm = service.cancel(idList);
			//同步erp
//			service.updateErpSku(0, idList);
			return rm;
		} catch (BizException e) {
			throw e;
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
     * 恢复货品
     * @throws ServiceException 
     * @throws DaoException 
     */
	@OpLog(model = OpLog.MODEL_META_SKU, type = OpLog.OP_TYPE_CANCEL, pos = 0)
	@RequestMapping(value = "/recovery", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel recovery(@RequestBody List<String> idList)  {
		try {
			return service.recovery(idList);
		} catch (BizException e) {
			throw e;
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
     * 批量导入数据
     * @throws Exception 
     */
    @OpLog(model = OpLog.MODEL_META_SKU, type = "导入", pos = -2)
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = { "application/json" })
 	@ResponseBody
    public ResultModel importSku(@RequestParam(value = "file", required = true) MultipartFile file ) throws Exception {
		this.service.importSku(file);
		ResultModel rm = new ResultModel();
		rm.setStatus(0);
		return rm;
    }
    /**
     * 导入数据批量调整货品
     * @throws Exception 
     */
    @OpLog(model = OpLog.MODEL_META_SKU, type = "导入", pos = -2)
    @RequestMapping(value = "/batchAdjustmentSku", method = RequestMethod.POST, produces = { "application/json" })
 	@ResponseBody
    public ResultModel batchAdjustmentSku(@RequestParam(value = "file", required = true) MultipartFile file ) throws Exception {
		this.service.batchAdjustmentSku(file);
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
    	return this.service.downloadSkuDemo();
	}
    /**
     * 下载批量调整模板文件
     */
    @RequestMapping(value = "/downloadBatchAjdustmentDemo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadBatchAjdustmentDemo() throws Exception {
    	return this.service.downloadBatchAjdustmentDemo();
	}
    /**
     * 批量生成货品条码
     * @throws DaoException
     * @throws ServiceException
     */
	@RequestMapping(value = "/getNo", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel getNo(@RequestBody Integer sum)  {
		try {
			Principal p = LoginUtil.getLoginUser();
			INoRule rule = context.getStrategy4No(p.getOrgId(), null);
			List<String> list = rule.getSkuSuffixNo(p.getOrgId(), sum);
			return new ResultModel().setList(list);
		} catch (BizException e) {
			throw e;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
    }
}