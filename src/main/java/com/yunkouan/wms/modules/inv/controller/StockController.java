package com.yunkouan.wms.modules.inv.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.util.valid.ValidStockShift;
import com.yunkouan.wms.modules.inv.vo.InStockVO;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;
import com.yunkouan.wms.modules.inv.vo.InvSkuStockVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.inv.vo.InvWarnVO;
import com.yunkouan.wms.modules.send.vo.TotalVo;

/**
 * 在库管理控制类
 */
@Controller

@RequestMapping("${adminPath}/stock")
@RequiresPermissions(value = { "stock.view" , "stockWarning.view"},logical=Logical.OR)
public class StockController  extends BaseController {

    /**
     * 库存业务服务类
     * @author 王通<br/>
     */
	@Autowired
    private IStockService stockService;
    /**
     * Default constructor
     */
    public StockController() {
    }

    /**
     * 库存数据列表查询
     * @return 
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody InvStockVO vo ) throws Exception {
    	Page<InvStockVO> list = this.stockService.listByPage(vo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }

    /**
     * 退货库存数据列表查询
     * @return 
     */
	@RequestMapping(value = "/listByT", method = RequestMethod.POST)
	@ResponseBody
    public ResultModel listByT(@Validated(value = { ValidSearch.class }) @RequestBody InvStockVO vo, BindingResult br) {
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
			Principal p = LoginUtil.getLoginUser();
			List<InvStockVO> list = stockService.listByT(vo, p);
			return new ResultModel().setList(list).setPageCount(vo.getPageCount()).setTotalCount(vo.getTotalCount());
		} catch(BizException e) {
			throw e;
		} catch(Throwable e) {
			throw new BizException(ErrorCode.UNKNOW_ERROR);
		}
	}


    /**
     * 库存数据列表查询
     * @return 
     */
    @RequestMapping(value = "/listPageNoLogin", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listPageNoLogin( @RequestBody InvStockVO vo ) throws Exception {
    	Page<InvStockVO> list = this.stockService.listByPageNoLogin(vo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }
    
    /**
     * 库存数据列表查询
     * @return 
     */
    @RequestMapping(value = "/listByCount", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listByCount( @RequestBody InvCountVO vo ) throws Exception {
    	List<InvStockVO> list = this.stockService.listByCount(vo);
    	ResultModel rm = new ResultModel();
    	rm.setList(list);
        return rm;
    }
    
    /**
     * 库存货品批次数据列表查询
     * @return 
     */
    @RequestMapping(value = "/listStockSku", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listStockSku( @RequestBody InvStockVO vo ) throws Exception {
    	Page<InvStockVO> list = this.stockService.listStockSku(vo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }
    /**
     * 批量导入库存数据
     * @throws Exception 
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_STOCK, type = "导入", pos = -2)
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = { "application/json" })
 	@ResponseBody
    public ResultModel importStock(@RequestParam(value = "file", required = true) MultipartFile file ) throws Exception {
		this.stockService.importStock(file);
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
    	return this.stockService.downloadStockDemo();
	}
    /**
     * 快速库存移位
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_STOCK, type = "快速移位", pos = 0)
    @RequestMapping(value = "/shift", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel shift(@Validated(value = { ValidStockShift.class }) @RequestBody InvStockVO outStockVo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.stockService.shift(outStockVo);
    	ResultModel rm = new ResultModel();
    	rm.setObj(outStockVo);
        return rm;
    }

//    /**
//     * 冻结
//     */
//    @RequestMapping(value = "/freezeList", method = RequestMethod.POST)
// 	@ResponseBody
//    public void freezeList(List<String> idList)  throws Exception {
//        this.stockService.freezeList(idList);
//    }
    
    /**
     * 冻结列表
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_STOCK, type = "冻结", pos = 0)
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel freeze(@RequestBody InvStockVO vo)  throws Exception {
        this.stockService.freeze(vo);
        ResultModel rm = new ResultModel();
    	rm.setStatus(1);
        return rm;
    }
    /**
     * 解除冻结
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_STOCK, type = "解冻", pos = 0)
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel unfreeze(@RequestBody InvStockVO vo)  throws Exception {
        this.stockService.unfreeze(vo);
        ResultModel rm = new ResultModel();
    	rm.setStatus(1);
        return rm;
    }
//    /**
//     * 解冻列表
//     */
//    @RequestMapping(value = "/unfreezeList", method = RequestMethod.POST)
// 	@ResponseBody
//    public void unfreezeList(List<String> idList) throws Exception  {
//        this.stockService.unfreezeList(idList);
//    }

    /**
     * 库存预警列表数据查询
     * @return 
     */
    @RequestMapping(value = "/warnList", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel warnList(@RequestBody InvWarnVO vo) throws Exception  {
    	Page<InvWarnVO> list = this.stockService.warnList(vo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }

    /**
     * 库存日志列表数据查询
     */
    @RequestMapping(value = "/logList", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel logList(@RequestBody InvLogVO logVo) throws Exception  {
    	Page<InvLogVO> listASN = this.stockService.logList(logVo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }

    /**
     * 库存调整日志数据查询
     */
    @RequestMapping(value = "/logListAdjustment", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel logListAdjustment(@RequestBody InvLogVO logVo) throws Exception  {
    	//设置查询只查询调整相关日志
    	logVo.setFindAdjustment(1);
    	Page<InvLogVO> listASN = this.stockService.logList(logVo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }
    /**
     * 快速调整库存，添加、更新或删除库存
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_STOCK, type = "快速调整库存", pos = 0)
    @RequestMapping(value = "/changeStock", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel changeStock(@Validated(value = { ValidSave.class }) @RequestBody InvStockVO stockVo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.stockService.changeStock(stockVo);
    	ResultModel rm = new ResultModel();
    	rm.setObj(stockVo.getInvStock());
        return rm;
    }
    
    /**
     * 预分配详情
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/outLockDetail", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel outLockDetail(@Validated(value = { ValidSave.class }) @RequestBody String id) throws Exception  {
    	InvStockVO stockVo = this.stockService.outLockDetail(id);
    	ResultModel rm = new ResultModel();
    	rm.setObj(stockVo);
        return rm;
    }
    
    /**
     * 刷新预分配详情
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/refreshOutLock", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel refreshOutLock(@Validated(value = { ValidSave.class }) @RequestBody String id) throws Exception  {
    	double ret = this.stockService.refreshOutLock(id);
    	ResultModel rm = new ResultModel();
    	rm.setObj(ret);
        return rm;
    }

    /**
     * 库存数量列表查询
     * @return 
     */
    @RequestMapping(value = "/stockSkuCount", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel stockSkuCount( @RequestBody List<String> skuList ) throws Exception {
    	List<InvSkuStockVO> list = this.stockService.stockSkuCount(skuList);
    	ResultModel rm = new ResultModel();
    	rm.setList(list);
        return rm;
    }
    
    /**
     * 下载excel文件
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadExcel(String isBlock, String skuStatus, String ownerName, 
    		String locationName, String skuNo, String skuStatusName, String isBlockName, 
    		String containTemp, String onlyTemp) throws Exception {
    	InvStockVO stockVo = new InvStockVO();
    	InvStock stock = new InvStock();
    	stockVo.setInvStock(stock);
    	if (isBlock != null) {
        	stock.setIsBlock(Integer.parseInt(isBlock));
    	}
    	if (skuStatus != null) {
        	stock.setSkuStatus(Integer.parseInt(skuStatus));
    	}
    	stockVo.setOwnerName(ownerName);
    	stockVo.setLocationName(locationName);
    	stockVo.setSkuNo(skuNo);
    	stockVo.setContainBatch(Boolean.getBoolean(containTemp));
    	stockVo.setOnlyTemp(Boolean.getBoolean(onlyTemp));
    	stockVo.setPageSize(99999999);
    	return this.stockService.downloadExcel(stockVo);
	}
    

    /**
	 * 查询入库单列表
	 * @param recPutawayVO 上架单
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月1日 下午8:40:06<br/>
	 * @author andy wang<br/>
	 */
    @RequestMapping(value = "/listInStock", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listInStock( @RequestBody InStockVO inStockVO ) throws Exception {
    	Page<InStockVO> page = this.stockService.listInStock(inStockVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(page);
    	return rm;
    }
    /**
   	 * 查询入库单列表
   	 * @param recPutawayVO 上架单
   	 * @return 结果对象
   	 * @throws Exception
   	 * @version 2017年3月1日 下午8:40:06<br/>
   	 * @author andy wang<br/>
   	 */
       @RequestMapping(value = "/totalInStock", method = RequestMethod.POST)
    	@ResponseBody
       public ResultModel totalInStock( @RequestBody InStockVO inStockVO ) throws Exception {
       	TotalVo vo = this.stockService.totalInStock(inStockVO);
       	ResultModel rm = new ResultModel();
       	rm.setObj(vo);
       	return rm;
       }
    /**
     * 下载excel文件
     */
    @RequestMapping(value = "/downloadInStockExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadExcel(String locationNoLike, String skuNameLike, String skuNoLike,
    		String skuBarCodeLike,String poNoLike, Date orderDateStart, Date orderDateEnd, String ownerLike) throws Exception {
    	InStockVO vo = new InStockVO();
    	vo.setLocationNoLike(locationNoLike);
    	vo.setSkuNameLike(skuNameLike);
    	vo.setSkuBarCodeLike(skuBarCodeLike);
    	vo.setSkuNoLike(skuNoLike);
    	vo.setPoNoLike(poNoLike);
    	vo.setOrderDateStart(orderDateStart);
    	vo.setOrderDateEnd(orderDateEnd);
    	vo.setOwnerLike(ownerLike);
    	vo.setPageSize(999999999);
    	return this.stockService.downloadInStockExcel(vo);
	}
    /**
     * 下载库存调整日志文件
     */
    @RequestMapping(value = "/downloadAdjustmentLogExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadAdjustmentLogExcel(String reqParam) throws Exception {
    	JSONObject jsonObject= JSONObject.parseObject(reqParam);
    	InvLogVO logVo= JSONObject.toJavaObject(jsonObject, InvLogVO.class);
    	
    	logVo.setFindAdjustment(1);
    	logVo.setPageSize(99999);
    	Page<InvLogVO> listASN = this.stockService.logList(logVo);
    	
    	return this.stockService.adjustmentLogExcel(listASN);
	}
}