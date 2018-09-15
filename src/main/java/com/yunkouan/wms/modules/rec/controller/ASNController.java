/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:37:17<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.excel.ExcelUtil;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.FileUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IASNService;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.util.valid.ValidBatchConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.util.ThreadPoolUtils;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

/**
 * ASN单控制类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:37:17<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/asn")
@RequiresPermissions(value = { "asn.view" })
public class ASNController extends BaseController {

	/** ASN单业务服务类 <br/> add by andy wang */ 
	@Autowired
    private IASNService asnService;
	
	/** 上架单业务服务类 <br/> add by andy wang */
	@Autowired
	private IPutawayService putawayService;
	
	@Autowired
	protected IDeliverGoodsApplicationService applicationService;
	
    @Autowired
    private IASNExtlService asnExtlService;
    
    @Autowired
	private IASNDetailExtlService asnDetailExtlService;
	

	@OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_ADD, pos = 0)
	@RequestMapping(value = "/addAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addAndEnable( @Validated(value = { ValidSave.class }) @RequestBody RecAsnVO recAsnVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.asnService.addAndEnable(recAsnVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	

	@OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateAndEnable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel updateAndEnable( @Validated(value = { ValidUpdate.class }) @RequestBody RecAsnVO recAsnVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.asnService.updateAndEnable(recAsnVO);
    	ResultModel rm = new ResultModel();
    	return rm;
	}
	
	
	/**
	 * ASN单列表查询
	 * —— 页面：仓储管理 -> 收货管理
	 * @param asnVO 查询条件
	 * @return 结果对象（包括：ASN单列表、分页数据）
	 * @throws Exception
	 * 创建日期:<br/> 2017年2月10日 上午10:49:53<br/>
	 * @author andy wang<br/>
	 */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody RecAsnVO asnVO ) throws Exception {
    	// 查询ASN列表
    	Page<RecAsnVO> listASN = this.asnService.list(asnVO);
    	ResultModel rm = new ResultModel();
    	rm.setPage(listASN);
        return rm;
    }

    /**
     * 打印ASN单
     * @param asnVO listAsnId 待打印ASN单id列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/print", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel print( @RequestBody RecAsnVO asnVO ) throws Exception {
    	// 查询ASN列表
    	List<RecAsnVO> listASN = this.asnService.list4print(asnVO);
    	return new ResultModel().setList(listASN);
    }

    /**
     * 查看ASN单详情
     * @param asnId asn单id
     * @return 结果对象
     * 创建日期:<br/> 2017年2月10日 下午2:32:18<br/>
     * @author andy wang<br/>
     * @throws Exception 
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel view( @RequestBody String asnId ) throws Exception {
        RecAsnVO recAsnVO = this.asnService.view(asnId);
        ResultModel rm = new ResultModel();
        rm.setObj(recAsnVO);
        return rm;
    }
    
    /**
     * 收货单拦截
     * @param poNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderInterception", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel orderInterception( @RequestBody String poNo ) throws Exception {
        RecAsnVO recAsnVO = new RecAsnVO();
        if(recAsnVO.getAsn()==null){
        	RecAsn asn=new RecAsn();
        	recAsnVO.setAsn(asn);
        }
        recAsnVO.getAsn().setPoNo(poNo);
        asnService.cancelByNo(recAsnVO);
        ResultModel rm = new ResultModel();
        rm.setObj(recAsnVO);
        return rm;
    }
    /**
     * 保存Asn单
     * @param asnVO Asn单VO
     * @return 结果对象
     * @version 2017年2月14日 上午11:02:17<br/>
     * @author andy wang<br/>
     * @throws Exception 
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_ADD, pos = 0)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel add( @Validated(value = { ValidSave.class }) @RequestBody RecAsnVO asnVO , BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	RecAsnVO recAsnVO = this.asnService.add(asnVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(recAsnVO);
    	return rm;
    }

    /**
     * 生效Asn单
     * @param asnVO Asn单VO
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月15日 上午9:57:39<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_ENABLE, pos = 0)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel enable( @RequestBody RecAsnVO asnVO ) throws Exception {
    	this.asnService.enable(asnVO);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
    /**
     * 失效ASN单
     * @param listAsnId Asn单id集合 
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月15日 下午1:32:00<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_DISABLE, pos = 0)
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel disable(@RequestBody List<String> listAsnId) throws Exception {
    	this.asnService.disable(listAsnId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

    /**
     * 取消ASN单
     * @param listAsnId Asn单id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月15日 下午1:57:18<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_CANCEL, pos = 0)
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel cancel ( @RequestBody List<String> listAsnId ) throws Exception {
    	this.asnService.cancel(listAsnId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
    /**
     * 拆分ASN单
     * @param asnVO 需要拆分的Asn单
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月15日 下午2:28:11<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "拆分", pos = 0)
    @RequestMapping(value = "/split", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel split ( @Validated(value = { ValidSplit.class }) @RequestBody RecAsnVO asnVO , BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
    		return super.handleValid(br);
    	}
    	this.asnService.split(asnVO);
    	ResultModel rm = new ResultModel();
    	return rm;
    }

    /**
     * 取消拆分ASN单
     * @param listAsnId Asn单id集合
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月16日 下午1:37:23<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "取消拆分", pos = 0)
    @RequestMapping(value = "/unsplit", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel unsplit( @RequestBody List<String> listAsnId ) throws Exception {
    	this.asnService.unsplit(listAsnId);
    	ResultModel rm = new ResultModel();
    	return rm;
    }
    
    /**
     * ASN单整单收货确认
     * @param recAsnVO Asn单对象
     * @param br 验证结果
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月19日 下午1:42:55<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "收货确认", pos = 0)
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel complete( @Validated(value = { ValidConfirm.class }) @RequestBody RecAsnVO recAsnVO , BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.asnService.complete(recAsnVO);
//    	asnService.updateERPInventory(recAsnVO);
    	ResultModel rm = new ResultModel();
//    	ErpResult result = asnService.updateERPInventory(recAsnVO);
//    	if(result.getCode() != 1){
//    		rm.setError();
//    		rm.addMessage("收货成功，发送ERP异常："+result.getMessage()+",请手工重发！");
//    	}
    	return rm;
    }
    
    /**
     * 更新erp入库清单
     * @param recAsnVO
     * @return
     * @throws Exception
     */
//    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "收货确认同步erp", pos = 0)
//    @RequestMapping(value = "/updateERPInventory", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel updateERPInventory(@RequestBody String id) throws Exception{
//    	RecAsnVO _recAsnVO = asnService.view(id);
//    	//若已同步成功，则不再提交
//    	if(Constant.ASN_HASSYNC_STATUS == _recAsnVO.getAsn().getSyncErpStatus()){
//    		ResultModel rm = new ResultModel();
//        	return rm;
//    	}
//    	ErpResult result = asnService.updateERPInventory(_recAsnVO);
//    	if(result.getCode() != 1){
//    		throw new BizException("SYC_ERP_ERR",result.getMessage());
//    	}
//    	ResultModel rm = new ResultModel();
//    	return rm;
//    }
    /**
     * ASN单整单收货确认并自动生成上架单
     * @param recAsnVO
     * @param br
     * @return
     * @throws Exception
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "收货确认", pos = 0)
    @RequestMapping(value = "/completeAndPutaway", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel completeAndPutaway( @Validated(value = { ValidConfirm.class }) @RequestBody RecAsnVO recAsnVO , BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.asnService.completeAndPutaway(recAsnVO);
    	ResultModel rm = new ResultModel();
    	
    	//异步发送ERP
//    	Thread thread = new Thread(new Runnable() {
//			public void run() {
//				try {
//					ErpResult result = asnService.updateERPInventory(recAsnVO);
//				} catch (Exception e) {
//					log.error("发送ERP异常："+e.getMessage());
//				}
//			}
//		});
//    	ThreadPoolUtils.getInstance().addThreadItem(thread);
//    	
//    	try {
//			ErpResult result = asnService.updateERPInventory(recAsnVO);
//			if(result.getCode() != 1){
//				rm.setError();
//				rm.addMessage("收货成功，发送ERP异常："+result.getMessage()+",请手工重发！");
//			}
//		} catch (Exception e) {
//			rm.setError();
//			rm.addMessage("收货成功，发送ERP网络异常,请手工重发！");
//		}
    	return rm;
    }

    /**
     * ASN单批量收货确认并自动生成上架单
     * @param recAsnVO Asn单对象
     * @param br 验证对象
     * @return 结果对象
     * @throws Exception
     * @version 2017年2月19日 下午1:45:22<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "批量收货确认", pos = 0)
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel batch( @Validated(value = { ValidBatchConfirm.class }) @RequestBody RecAsnVO recAsnVO , BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	this.asnService.batchAndAddPtw(recAsnVO);
    	
    	ResultModel rm = new ResultModel();
    	List<String> listAsnId = recAsnVO.getListAsnId();
    	for(String asnId:listAsnId){
        	RecAsn recAsn = this.asnExtlService.viewById(asnId);
    		RecAsnVO asnVO = new RecAsnVO(recAsn);
    		List<RecAsnDetail> listAsnDetail = this.asnDetailExtlService.listSkuByAsnId(asnId);
    		List<RecAsnDetailVO> listAsnDetailVO = new ArrayList<RecAsnDetailVO>(); 
    		if ( listAsnDetail != null && !listAsnDetail.isEmpty() ) {
    			for (int i = 0; i < listAsnDetail.size(); i++) {
    				RecAsnDetail detail = listAsnDetail.get(i);
    				RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO(detail);
    				listAsnDetailVO.add(recAsnDetailVO);
    			}
    			asnVO.setListAsnDetailVO(listAsnDetailVO);
    		}
        	//异步发送ERP
//        	Thread thread = new Thread(new Runnable() {
//    			public void run() {
//    				try {
//    					ErpResult result = asnService.updateERPInventory(recAsnVO);
//    				} catch (Exception e) {
//    					log.error("发送ERP异常："+e.getMessage());
//    				}
//    			}
//    		});
//        	ThreadPoolUtils.getInstance().addThreadItem(thread);
//    		ErpResult result = asnService.updateERPInventory(recAsnVO);
//        	if(result.getCode() != 1){
//        		rm.setError();
//        		rm.addMessage("收货成功，发送ERP异常："+result.getMessage()+",请手工重发！");
//        		return rm;
//        	}
    		
    	}
    	return rm;
    }

	/**
	 * 修改ASN单和货品明细
	 * @param asnVO Asn单对象
	 * @param br 验证对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年2月19日 下午1:46:08<br/>
	 * @author andy wang<br/>
	 */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = OpLog.OP_TYPE_UPDATE, pos = 0)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel update( @Validated(value = { ValidUpdate.class }) @RequestBody RecAsnVO asnVO 
    		, BindingResult br ) throws Exception {
    	if ( br.hasErrors() ) {
			return super.handleValid(br);
		}
    	RecAsnVO recAsnVO = this.asnService.update(asnVO);
    	ResultModel rm = new ResultModel();
    	rm.setObj(recAsnVO);
    	return rm;
    }

    /**
     * ASN单的Excel导入
     * @param file Excel文件
     * @return
     * @throws Exception
     * @version 2017年5月2日 下午5:50:19<br/>
     * @author andy wang<br/>
     */
//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel imptASN( @RequestParam(value="fileLicense",required=false) MultipartFile fileLicense ) throws Exception {
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "导入", pos = -2)
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = { "application/json" })
 	@ResponseBody
    public ResultModel upload(@RequestParam(value = "file", required = true) MultipartFile file ) throws Exception {
    	ResultModel rm = new ResultModel();
    	File newFile = FileUtil.createNewFile(file);
    	if ( newFile != null ) {
    		try {
    			List<RecAsnVO> listImportAsn = new ArrayList<RecAsnVO>();
        		List<Object> listImport = ExcelUtil.parseExcel("asn", newFile.getAbsolutePath());
        		for (int i = 0; i < listImport.size(); i++) {
        			listImportAsn.add((RecAsnVO) listImport.get(i));
    			}
        		this.asnService.upload(listImportAsn);
			} catch ( Exception e ) {
				throw e;
			} finally {
        		// 删除文件
        		newFile.delete();
			}
    		
    	}
		return rm;
    }
    
    
    /**
     * 下载ASN单Excel导入模板
     * @return Excel模板的流
     * @throws Exception
     * @version 2017年5月2日 下午5:50:34<br/>
     * @author andy wang<br/>
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download () throws Exception {
    	String path = FileUtil.getResource()+"excel/template/asn_template.xls";
    	String downloadName = "asn_template.xls";
    	return FileUtil.excel2Stream(path, downloadName);
    }
    
    /**
     * 查询暂存区的ASN单列表
     * @return 结果对象
     * @throws Exception
     * @version 2017年3月30日 下午2:17:16<br/>
     * @author andy wang<br/>
     */
    @RequestMapping(value = "/listStock", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel listStock (@RequestBody Integer docType) throws Exception {
    	ResultModel rm = new ResultModel();
    	List<RecAsnVO> listStock = this.asnService.listStock(docType);
    	rm.setList(listStock);
    	return rm;
    }
    
    /**
     * 收货确认后，根据ASN单id，生成上架单
     * @return
     * @throws Exception
     * @version 2017年5月4日 下午3:13:48<br/>
     * @author andy wang<br/>
     */
    @OpLog(model = OpLog.MODEL_WAREHOUSE_ASN, type = "生成上架单", pos = 0)
    @RequestMapping(value = "/addPutaway", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel addPutaway( @RequestBody List<String> asnId ) throws Exception {
    	List<RecPutawayVO> listPtwVO = this.putawayService.createPutaway(asnId);
    	ResultModel rm = new ResultModel();
    	rm.setList(listPtwVO);
    	return rm;
    }
    
    /**
     * 下载excel文件
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadExcel(String likeOwner, String likeCreatePerson, String likePoNo, 
    		String likeAsnNo, String orderDate, String dataFrom, String asnStatus,String orderDateStart,String orderDateEnd) throws Exception {
    	RecAsnVO vo = new RecAsnVO();
    	RecAsn asn = new RecAsn();
    	vo.setAsn(asn);
    	if (dataFrom != null) {
        	asn.setDataFrom(Integer.parseInt(dataFrom));
    	}
    	if (asnStatus != null) {
        	asn.setAsnStatus(Integer.parseInt(asnStatus));
    	}
    	vo.setLikeOwner(likeOwner);
    	vo.setLikeCreatePerson(likeCreatePerson);
    	vo.setLikePoNo(likePoNo);
    	vo.setLikeAsnNo(likeAsnNo);
    	if (orderDate != null) {
    		asn.setOrderDate(new SimpleDateFormat("yyyy/MM/dd").parse(orderDate));
    	}
    	vo.setPageSize(999999999);
    	return this.asnService.downloadExcel(vo);
	}
    
	/**
	 * 批量同步出库数据
	 * @param sendDeliveryVo
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/batchSyncInstockData", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultModel batchSyncInstockData(@RequestBody RecAsnVO asnVO)throws Exception {
//		if(asnVO == null) 
//			throw new BizException("data_is_null");		
//		this.asnService.batchSyncInstockData(asnVO.getListAsnId());
//		ResultModel rm = new ResultModel();
//		return rm;
//	}
	
    /**
     * 推送入库数据
     * @param putawayId
     * @return
     * @throws Exception
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月7日 下午5:28:38<br/>
     * @author hgx<br/>
     */
//    @RequestMapping(value = "/transmitInstock", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel transmitInstock( @RequestBody String id ) throws Exception {
//    	ResultModel rm = new ResultModel();
//    	this.asnService.transmitInstockXML(id);
//    	return rm;
//    }
    
    /**
     * 生成申请单
     * @param asnId asn单id
     * @return 结果对象
     * 创建日期:<br/> 2017年2月10日 下午2:32:18<br/>
     * @author hgx<br/>
     * @throws Exception 
     */
//    @RequestMapping(value = "/createApplication", method = RequestMethod.POST)
// 	@ResponseBody
//    public ResultModel createApplication( @RequestBody String asnId ) throws Exception {
//    	
//		//获取登录用户
//		Principal p = LoginUtil.getLoginUser();	
//		//查询是否有关联的申请单
//    	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
//    	List<String> statusList = new ArrayList<String>();
//    	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
//    	applicationVo.setStatusNotIn(statusList);
//    	applicationVo.getEntity().setAsnId(asnId);
//    	List<DeliverGoodsApplicationVo> qryList = applicationService.qryList(applicationVo);
//    	if (qryList != null && !qryList.isEmpty()) {
//    		throw new BizException("err_application_not_null");
//    	}
//        RecAsnVO recAsnVO = this.asnService.view(asnId);
//        if ( recAsnVO.getAsn().getAsnStatus() == null || Constant.ASN_STATUS_OPEN != recAsnVO.getAsn().getAsnStatus() ) {
//    		throw new BizException("err_rec_asn_update_statusNotOpen");
//    	}
//        applicationService.createApplicationFromAsn(recAsnVO, p);
//        ResultModel rm = new ResultModel();
//        return rm;
//    }
    
    public static void main(String[] args) {
//    	String dStr = "2017/02/03";
//    	String rex = "^\\d{4}/\\d{2}/\\d{2}$";
    	
//    	String dStr = "2017/02/03 01:23:12";
//    	String rex = "^\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}$";
//        Pattern pat = Pattern.compile(rex);
//        Matcher mat = pat.matcher(dStr);
//        boolean dateType = mat.matches();
//        System.out.println(dateType);
	}
    
}