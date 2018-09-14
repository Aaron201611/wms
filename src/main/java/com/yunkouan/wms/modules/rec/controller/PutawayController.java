/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:38:38<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.controller;

import java.util.ArrayList;
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
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

/**
 * 上架单控制类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:38:38<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/ptw")
@RequiresPermissions(value = { "putaway.view" })
public class PutawayController extends BaseController {
	/**
	 * 上架单业务类
     * @author andy wang<br/>
	 */
	@Autowired
	private IPutawayService putawayService;
	@Autowired
	private StrategyContext context;

	/**
	 * 更新并生效上架单
	 * @param recPutawayVO 需要更新的上架单
	 * @param br 验证对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年5月2日 下午4:20:08<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_UPDATE, pos = 0)
    @RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
 	@ResponseBody
	public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody RecPutawayVO recPutawayVO 
    		, BindingResult br ) throws Exception { 
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.putawayService.updateAndEnable(recPutawayVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(recPutawayVO);
    	return rm;
    }
	
	/**
	 * 保存并生效上架单
	 * @param recPutawayVO 需要保存的上架单
	 * @param br 验证对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年5月2日 下午4:21:15<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
	public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody RecPutawayVO recPutawayVO 
    		, BindingResult br ) throws Exception { 
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.putawayService.addAndEnable(recPutawayVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(recPutawayVO);
    	return rm;
    }

    /**
     * 自动分配上架单
     * @param recPutawayVO 上架单信息
     * @throws Exception
     * @version 2017年3月14日 下午4:54:06<br/>
     * @author andy wang<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = "自动分配库位", pos = 0)
    @RequestMapping(value = "/auto", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel auto( @RequestBody RecPutawayVO recPutawayVO ) throws Exception{
        // ANDY 字段校验
    	Principal p = LoginUtil.getLoginUser();
//    	String json1 = JsonUtil.toJson(recPutawayVO);
//    	if(log.isInfoEnabled()) log.info(json1);
    	RecPutawayVO autoPutaway = this.context.getStrategy4Putaway(p.getOrgId(), LoginUtil.getWareHouseId()).auto(recPutawayVO);
//    	String json2 = JsonUtil.toJson(autoPutaway);
//    	if(log.isInfoEnabled()) log.info(json2);
    	ResultModel rm = new ResultModel();
    	rm.setObj(autoPutaway);
    	return rm;
    }
	
	
	/**
	 * ANDY 上架单批量作业确认
	 * @param listPutawayId
	 * @throws Exception
	 * @version 2017年3月4日 下午7:02:13<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = "批量作业确认", pos = 0)
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
 	@ResponseBody
 	@Deprecated
	public ResultModel batchConfirmPutaway ( @RequestBody RecPutawayVO recPutawayVO ) throws Exception {
    	this.putawayService.batchConfirmPutaway(recPutawayVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}

	/**
	 * ANDY 上架单作业确认
	 * @param recPutaway 
	 * @return
	 * @throws Exception
	 * @version 2017年3月2日 下午8:42:27<br/>
	 * @author andy wang<br/>
	 */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = "作业确认", pos = 0)
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel complete( @Validated(value = { ValidConfirm.class }) @RequestBody RecPutawayVO recPutawayVO, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.putawayService.complete(recPutawayVO);
    	return new ResultModel();
    }
    
    /**
     * 取消上架单
     * @param listPutawayId 上架单id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月2日 下午8:15:07<br/>
     * @author andy wang<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_CANCEL, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel cancel( @RequestBody List<String> listPutawayId ) throws Exception {
    	this.putawayService.cancel(listPutawayId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

    /**
     * 取消拆分上架单
     * @param listPutawayId 上架单id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月2日 下午8:13:55<br/>
     * @author andy wang<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = "取消拆分", pos = 0)
    @RequestMapping(value = "/unsplit", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel unsplit( @RequestBody List<String> listPutawayId ) throws Exception {
    	this.putawayService.unsplit(listPutawayId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
	
	
    /**
     * ANDY 校验
     * 拆分上架单
     * @param recPutawayVO 上架单对象
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月2日 下午8:13:33<br/>
     * @author andy wang<br/>
     */
	@OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = "拆分", pos = 0)
    @RequestMapping(value = "/split", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel split( @RequestBody RecPutawayVO recPutawayVO ) throws Exception {
    	this.putawayService.split(recPutawayVO);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
	
	/**
	 * 打印上架单
	 * @param putawayId 上架单id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月1日 下午8:39:24<br/>
	 * @author andy wang<br/>
	 */
    @RequestMapping(value = "/print", method = RequestMethod.POST)
 	@ResponseBody
 	@Deprecated
    public ResultModel printPutaway( @RequestBody String putawayId ) throws Exception {
    	this.putawayService.printPutaway(putawayId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
	
	/**
	 * 查看上架单详情
	 * @param putawayId 上架单id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月1日 下午8:39:49<br/>
	 * @author andy wang<br/>
	 */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel view( @RequestBody String putawayId ) throws Exception {
    	RecPutawayVO recPutawayVO = this.putawayService.view(putawayId);
    	return new ResultModel().setObj(recPutawayVO);
    }
	
	
	/**
	 * ANDY 校验
	 * 查询上架单列表
	 * @param recPutawayVO 上架单
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月1日 下午8:40:06<br/>
	 * @author andy wang<br/>
	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody RecPutawayVO recPutawayVO ) throws Exception {
    	Page<RecPutawayVO> page = this.putawayService.list(recPutawayVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(page);
    	return rm;
    }
    @RequestMapping(value = "/bathPrint", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel bathPrint( @RequestBody RecPutawayVO recPutawayVO ) throws Exception {
    	List<RecPutawayVO> list = this.putawayService.list4print(recPutawayVO);
    	return new ResultModel().setList(list);
    }

    /**
     * 修改上架单和货品明细
     * @param recPutawayVO 上架单
     * @param br 校验对象
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月1日 下午8:40:26<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_UPDATE, pos = 0)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel update( @Validated(value = { ValidUpdate.class }) @RequestBody RecPutawayVO recPutawayVO 
    		, BindingResult br ) throws Exception { 
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.putawayService.update(recPutawayVO);
    	recPutawayVO.setListRemovePutawayDetail(new ArrayList<String>());
    	recPutawayVO.setListRemovePutawayLocation(new ArrayList<String>());
    	recPutawayVO.setListUpdatePutawayLocation(new ArrayList<String>());
    	ResultModel rm = new ResultModel();
    	rm.setObj(recPutawayVO);
    	return rm;
    }
	
    /**
     * 失效上架单
     * @param 上架单id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月27日 下午4:52:55<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel disable( @RequestBody List<String> listPutawayId ) throws Exception {
    	this.putawayService.disable(listPutawayId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
    /**
     * 生效上架单
     * @param listPutawayId 上架单id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月22日 下午5:43:42<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_ENABLE, pos = 0)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel enable ( @RequestBody RecPutawayVO putawayVO ) throws Exception {
    	this.putawayService.enable(putawayVO);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
	
    /**
     * 新增上架单
     * @param putawayVO 上架单VO
     * @param br 验证对象
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月22日 下午5:40:08<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_PUTAWAY, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody RecPutawayVO putawayVO , BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	RecPutawayVO recPutawayVO = this.putawayService.add(putawayVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(recPutawayVO);
    	return rm;
    }
    


}