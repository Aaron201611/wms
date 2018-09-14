package com.yunkouan.wms.modules.inv.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.yunkouan.wms.modules.inv.service.ICountService;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;

/**
 * 盘点控制类
 */
@Controller

@RequestMapping("${adminPath}/count")
@RequiresPermissions(value = { "count.view" })
public class CountController extends BaseController  {

    /**
     * 移位单业务服务类
     * @author 王通<br/>
     */
	@Autowired
    private ICountService countService;
    /**
     * Default constructor
     */
    public CountController() {
    }
    
	
    
 	/**
 	 * 盘点计划数据列表查询
 	 * @param countVO
 	 * @return
 	 * @throws Exception
 	 * @Description 
 	 * @version 2017年2月16日 下午1:43:11<br/>
 	 * @author 王通<br/>
 	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody InvCountVO countVO ) throws Exception {
    	Page<InvCountVO> listASN = this.countService.list(countVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }
    /**
 	 * 盘点计划数据列表查询
 	 * @param countVO
 	 * @return
 	 * @throws Exception
 	 * @Description 
 	 * @version 2017年2月16日 下午1:43:11<br/>
 	 * @author 王通<br/>
 	 */
    @RequestMapping(value = "/list4Print", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list4Print( @RequestBody InvCountVO countVO ) throws Exception {
    	Page<InvCountVO> listASN = this.countService.list4Print(countVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }
    
    /**
     * 查看盘点计划详情
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel view(@RequestBody String id) throws Exception {
    	InvCountVO countVo = this.countService.view(id);
    	ResultModel rm = new ResultModel();
        rm.setObj(countVo);
        return rm;
    }

    /**
     * 添加或更新盘点计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel add(@Validated(value = { ValidSave.class }) @RequestBody InvCountVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvCountVO countVo = this.countService.add(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(countVo);
        return rm;
    }
    /**
     * 添加并更新盘点计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/saveAndEnable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel saveAndEnable(@Validated(value = { ValidSave.class }) @RequestBody InvCountVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
    	InvCountVO countVo = this.countService.saveAndEnable(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(countVo);
        return rm;
    }
    
    /**
     * 添加或更新盘点计划
     * @param id
     * @return
     * @Description 
     * @version 2017年2月16日 下午4:40:05<br/>
     * @author 王通<br/>
     */  
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = OpLog.OP_TYPE_UPDATE, pos = 0)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel update(@Validated(value = { ValidUpdate.class }) @RequestBody InvCountVO vo,  BindingResult br) throws Exception  {
    	if ( br != null && br.hasErrors() ) {
			return super.handleValid(br);
		}
        InvCountVO countVo = this.countService.update(vo);
    	ResultModel rm = new ResultModel();
        rm.setObj(countVo);
        return rm;
    }
    /**
     * 生效盘点计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = OpLog.OP_TYPE_ENABLE, pos = 0)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
  	@ResponseBody
    public ResultModel enable(@RequestBody InvCountVO vo ) throws Exception  {
    	ResultModel rm = new ResultModel();
        this.countService.enable(vo);
        return rm;
    }

    /**
     * 失效盘点计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel disable(@RequestBody InvCountVO vo ) throws Exception  {
    	ResultModel rm = new ResultModel();
        this.countService.disable(vo);
        return rm;
    }

    /**
     * 取消盘点计划
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = OpLog.OP_TYPE_CANCEL, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel cancel(@RequestBody List<String> countIdList ) throws Exception  {
    	ResultModel rm = new ResultModel();
        this.countService.cancel(countIdList);
        return rm;
    }

    /**
     * 作业确认
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = "作业确认", pos = 0)
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel confirm(@RequestBody InvCountVO countVo ) throws Exception  {
    	ResultModel rm = new ResultModel();
    	this.countService.confirm(countVo);
        return rm;
    }
    /**
	 * 快速添加任务同时作业确认，用于手持终端
	 * @version 2017年7月20日 上午11:14:00<br/>
	 * @author 王通<br/>
	 */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_COUNT, type = "作业确认", pos = 0)
    @RequestMapping(value = "/quickConfirm", method = RequestMethod.POST)
   	@ResponseBody
    public ResultModel quickConfirm(@RequestBody InvCountVO countVo ) throws Exception  {
    	ResultModel rm = new ResultModel();
    	this.countService.quickConfirm(countVo);
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
    	ResultModel rm = new ResultModel();
    	this.countService.print(id);
    	return rm;
    }
    
    /**
     * 推送期末库存
     * @param putawayId
     * @return
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月7日 下午5:28:38<br/>
     * @author hgx<br/>
     */
    @RequestMapping(value = "/transmitStockAmount", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel transmitStockAmount( @RequestBody String id ) throws Exception {
    	ResultModel rm = new ResultModel();
    	this.countService.transmitStocksXML(id);
    	return rm;
    }
    /**
     * 推送期末库存
     */
    @RequestMapping(value = "/getApplyNo", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel getApplyNo() throws Exception {
    	ResultModel rm = new ResultModel();
    	String no = this.countService.getApplyNo();
    	rm.setObj(no);
    	return rm;
    }
    /**
     * 下载excel文件
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadExcel(String countId) throws Exception {
    	InvCountVO vo = new InvCountVO();
    	if (countId != null) {
    		vo.setCountIdList(Arrays.asList(countId));
    	}
    	vo.setPageSize(99999999);
    	return this.countService.downloadExcel(vo);
	}

}