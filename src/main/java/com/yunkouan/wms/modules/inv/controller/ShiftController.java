package com.yunkouan.wms.modules.inv.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.StockLocationVo;
import com.yunkouan.wms.modules.inv.vo.StockSkuVo;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 移位控制类
 */
@Controller

@RequestMapping("${adminPath}/shift")
@RequiresPermissions(value = { "shift.view" })
public class ShiftController  extends BaseController {
	@Autowired
	private StrategyContext context;

    /**
     * 移位单业务服务类
     * @author 王通<br/>
     */
	@Autowired
    private IShiftService shiftService;
//	@Autowired
//    private ISkuService skuService;

    /**
 	 * 移位计划数据列表查询
 	 * @param shiftVO
 	 * @return
 	 * @throws Exception
 	 * @Description 
 	 * @version 2017年2月16日 下午1:43:11<br/>
 	 * @author 王通<br/>
 	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody InvShiftVO shiftVO ) throws Exception {
    	Page<InvShiftVO> listASN = this.shiftService.list(shiftVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }
    
    /**
 	 * 移位计划数据列表查询
 	 * @param shiftVO
 	 * @return
 	 * @throws Exception
 	 * @Description 
 	 * @version 2017年2月16日 下午1:43:11<br/>
 	 * @author 王通<br/>
 	 */
    @RequestMapping(value = "/list4Print", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list4Print( @RequestBody InvShiftVO shiftVO ) throws Exception {
    	Page<InvShiftVO> listASN = this.shiftService.list4Print(shiftVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }
    
    /**
     * 查看移位计划详情
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel view(@RequestBody String id) throws Exception {
    	InvShiftVO shiftVo = this.shiftService.view(id);
    	ResultModel rm = new ResultModel();
        rm.setObj(shiftVo);
        return rm;
    }

    /**
     * 添加或更新移位计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel saveOrUpdate(@Validated(value = { ValidSave.class }) @RequestBody InvShiftVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvShiftVO shiftVo = this.shiftService.saveOrUpdate(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(shiftVo);
        return rm;
    }

    /**
     * 添加并更新移位计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel saveAndEnable(@Validated(value = { ValidSave.class }) @RequestBody InvShiftVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvShiftVO shiftVo = this.shiftService.saveAndEnable(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(shiftVo);
        return rm;
    }
    
    /**
     * 生效移位计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = OpLog.OP_TYPE_ENABLE, pos = 0)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel enable(@RequestBody InvShiftVO vo ) throws Exception  {
    	this.shiftService.enable(vo);
    	ResultModel rm = new ResultModel();
        return rm;
    }

    /**
     * 失效移位计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel disable(@RequestBody InvShiftVO vo) throws Exception  {
    	 this.shiftService.disable(vo);
    	 ResultModel rm = new ResultModel();
         return rm;
    }

    /**
     * 取消移位计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel cancel(@RequestBody InvShiftVO vo) throws Exception  {
    	 this.shiftService.cancel(vo);
    	 ResultModel rm = new ResultModel();
         return rm;
    }
    /**
     * 作业确认
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = "作业确认", pos = 0)
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel confirm(@RequestBody InvShiftVO shiftVo ) throws Exception  {
    	this.shiftService.confirm(shiftVo);
    	ResultModel rm = new ResultModel();
        return rm;
    }
    /**
     * 作业确认
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = "批量作业确认", pos = 0)
    @RequestMapping(value = "/confirmBatch", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel confirmBatch(@RequestBody InvShiftVO shiftVo ) throws Exception  {
    	this.shiftService.confirmBatch(shiftVo);
    	ResultModel rm = new ResultModel();
        return rm;
    }
    /**
     * 快速作业确认，用于手持终端
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_SHIFT, type = "作业确认", pos = 0)
    @RequestMapping(value = "/quickConfirm", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel quickConfirm(@RequestBody InvShiftVO shiftVo ) throws Exception  {
    	this.shiftService.quickConfirm(shiftVo);
    	ResultModel rm = new ResultModel();
        return rm;
    }
    /**
     * 打印移位单
     * @param putawayId
     * @return
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月7日 下午5:28:38<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/print", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel print( @RequestBody String id ) throws Exception {
    	this.shiftService.print(id);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

    /** 
    * @Title: auto 
    * @Description: 自动分配库位
    * @auth tphe06
    * @time 2018 2018年8月31日 下午1:53:24
    * @param list
    * @return
    * ResultModel
     * @throws Exception 
    */
    @RequestMapping(value = "/auto", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel auto(@RequestBody List<StockSkuVo> list) throws Exception {
    	Principal p = LoginUtil.getLoginUser();
    	RecPutawayVO vo = shiftToPutaway(list);
    	RecPutawayVO result = this.context.getStrategy4Putaway(p.getOrgId(), LoginUtil.getWareHouseId()).auto(vo);
    	if(!result.isSuccess()) throw new BizException("valid_putaway_auto_faile");
    	return new ResultModel().setList(putawayToShift(list, result));
    }
    /** 
    * @Title: shiftToPutaway 
    * @Description: TODO
    * @auth tphe06
    * @time 2018 2018年8月31日 下午2:31:31
    * @param list
    * @return
    * RecPutawayVO
    */
    private RecPutawayVO shiftToPutaway(List<StockSkuVo> list) {
    	RecPutawayVO vo = new RecPutawayVO();
    	for(StockSkuVo shift : list) {
	    	RecPutawayDetailVO detail = new RecPutawayDetailVO();
	    	detail.getPutawayDetail().setSkuId(shift.getSkuId());
	    	detail.getPutawayDetail().setPackId("");
	    	detail.getPutawayDetail().setBatchNo(shift.getBathNo());
//	    	detail.getPutawayDetail().setSkuStatus(skuService.get(shift.getSkuId()).getSkuStatus());
	    	detail.getPutawayDetail().setMeasureUnit(shift.getUnit());
	    	detail.getPutawayDetail().setPlanPutawayQty(NumberUtil.parse(shift.getQty()));
	    	detail.getPutawayDetail().setPlanPutawayWeight(NumberUtil.parse(shift.getWeight()));
	    	detail.getPutawayDetail().setPlanPutawayVolume(NumberUtil.parse(shift.getVolume()));
	    	detail.getPutawayDetail().setOwner(shift.getOwnerId());
	    	vo.getListSavePutawayDetailVO().add(detail);
    	}
    	return vo;
    }
    /** 
    * @Title: putawayToShift 
    * @Description: TODO
    * @auth tphe06
    * @time 2018 2018年8月31日 下午2:31:34
    * @param vo
    * @return
    * List<ShiftVo>
    */
    private static List<StockSkuVo> putawayToShift(List<StockSkuVo> list, RecPutawayVO vo) {
    	for(int i=0; i<vo.getListPutawayDetailVO().size(); ++i) {
    		RecPutawayDetailVO detail = vo.getListPutawayDetailVO().get(i);
    		for(int j=0; j<detail.getListPlanLocationVO().size(); ++j) {
    			 RecPutawayLocationVO location = detail.getListPlanLocationVO().get(j);
    			 StockLocationVo shift = list.get(i).getLocations()[j];
    			 shift.setLocationId(location.getPutawayLocation().getLocationId());
    			 shift.setLocationComment(location.getLocationComment());
//    			detail.getPutawayDetail().getSkuId();
//    			location.getLocationComment();
//    			location.getPutawayLocation().getLocationId();
//    			location.getPutawayLocation().getPutawayQty();
//    			vo.getMessage();
    		}
    	}
    	return list;
    }
}