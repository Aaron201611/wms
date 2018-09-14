package com.yunkouan.wms.modules.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IWarehouseService;
import com.yunkouan.saas.modules.sys.vo.MetaWarehouseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.common.util.MSMQUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationGoodsSkuDao;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsExamineDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGoodsSku;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamine;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamineApplication;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationFormService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodsSkuService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineApplicationService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineService;
import com.yunkouan.wms.modules.application.util.ApplicationStringUtil;
import com.yunkouan.wms.modules.application.util.DeliverGoodsXMLMessageUtil;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationFormVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodsSkuVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationDetailVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineVo;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ISkuService;

/**
 * 核放单服务
 * @author Aaron
 *
 */
@Service
public class DeliverGoodsExamineServiceImpl extends BaseService implements IDeliverGoodsExamineService{

	private Logger log = LoggerFactory.getLogger(DeliverGoodsExamineServiceImpl.class);
	
	@Autowired
	private IDeliverGoodsExamineDao examineDao;
	
	@Autowired
	private IDeliverGoodsExamineApplicationService exAppService;
	
	@Autowired
	private IDeliverGoodsApplicationGoodsSkuDao goodsSkuDao;
	
	@Autowired
	private IMsmqMessageService msmqMessageService;
	
	@Autowired
	private IDeliverGoodsApplicationService applicationService;
	
	@Autowired
	private IDeliverGoodsApplicationFormService applicationFormService;
	
	@Autowired
	private ISkuService skuService;
	
	@Autowired
	private IDeliverGoodsApplicationGoodsSkuService goodsSkuService;
	
	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private ISysParamService paramService;

	/**
	 * 分页查询
	 * @param examineVo
	 * @return
	 */
	public ResultModel pageList(DeliverGoodsExamineVo examineVo) throws Exception{
		ResultModel result = new ResultModel();
		Page<DeliverGoodsExamineVo> page = null;
		if ( examineVo.getCurrentPage() == null ) {
			examineVo.setCurrentPage(0);
		}
		if ( examineVo.getPageSize() == null ) {
			examineVo.setPageSize(20);
		}
		page = PageHelper.startPage(examineVo.getCurrentPage()+1, examineVo.getPageSize());
		
		//不查询取消状态的记录
		if(examineVo.getStatusNotIn() == null 
				&& examineVo.getEntity().getStatus() == null 
				&& examineVo.getStatusLessThan() == null){
			examineVo.setStatusLessThan(99);
		}
		
		List<DeliverGoodsExamine> recordList = examineDao.selectByExample(examineVo.getExample());
		if(recordList == null || recordList.isEmpty()) return result;
		result.setPage(page);
		
		List<DeliverGoodsExamineVo> voList = new ArrayList<DeliverGoodsExamineVo>();
		for(DeliverGoodsExamine entity:recordList){
			DeliverGoodsExamineVo vo = chg(entity);
			voList.add(vo);
		}
		result.setList(voList);
		
		return result;
	}
	private DeliverGoodsExamineVo chg(DeliverGoodsExamine entity) throws Exception{
		DeliverGoodsExamineVo vo = new DeliverGoodsExamineVo(new DeliverGoodsExamine());
		vo.setEntity(entity);
		vo.setiEFlagName(paramService.getValue("I_E_FLAG", entity.getiEFlag()));
		vo.setStatusName(paramService.getValue("STATUS", entity.getStatus()));
		vo.setAuditStepName(paramService.getValue("AUDIT_STEP", entity.getAuditStep()));
		vo.setKernelTypeName(paramService.getValue(CacheName.KERNEL_TYPE, entity.getKernelType()));;
		vo.setKernelBizModeName(paramService.getValue(CacheName.KERNEL_BIZ_MODE, entity.getKernelBizMode()));
		vo.setKernelBizTypeName(paramService.getValue(CacheName.KERNEL_BIZ_TYPE, entity.getKernelBizType()));
		
		DeliverGoodsExamineApplicationVo applicationVo = new DeliverGoodsExamineApplicationVo(new DeliverGoodsExamineApplication());
		applicationVo.getEntity().setExamineId(entity.getId());
		List<DeliverGoodsExamineApplicationVo> applicationVoList = exAppService.qryList(applicationVo);
		vo.setExamineApplicationVoList(applicationVoList);
		return vo;
	}
	/**
	 * 查询
	 * @param examineVo
	 * @return
	 */
	public List<DeliverGoodsExamineVo> qryList(DeliverGoodsExamineVo examineVo) throws Exception{
		List<DeliverGoodsExamine> recordList = examineDao.selectByExample(examineVo.getExample());
		
		List<DeliverGoodsExamineVo> voList = new ArrayList<DeliverGoodsExamineVo>();
		for(DeliverGoodsExamine entity:recordList){
			DeliverGoodsExamineVo vo = chg(entity);
			voList.add(vo);
		}
		return voList;
	}
	/**
	 * 新增
	 * @param examineVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsExamineVo add(DeliverGoodsExamineVo examineVo,Principal p)throws Exception{
		if(examineVo == null) throw new BizException("data_is_null") ;
		
		Date today = new Date();
		String year = DateFormatUtils.format(today, "yyyy");
		String i_flag = Constant.IN_FLAG.equals(examineVo.getEntity().getIEFlag())?"1":"2";
		
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		//新增核放单
		String no = context.getStrategy4No(p.getOrgId(), LoginUtil.getWareHouseId()).getExamineNo(p.getOrgId());
		String examineNo = "";
		//若是保税仓，就是分送集报的核放单
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
			String KERNEL_TYPE = paramService.getKey(CacheName.KERNEL_TYPE);
			String KERNEL_BIZ_TYPE = paramService.getKey(CacheName.KERNEL_BIZ_TYPE);
			String KERNEL_BIZ_MODE = paramService.getKey(CacheName.KERNEL_BIZ_MODE);
			examineVo.getEntity().setExamineFrom(Constant.APPLY_FROM_DELIVER_GOODS);
			examineVo.getEntity().setKernelType(KERNEL_TYPE);
			examineVo.getEntity().setKernelBizType(KERNEL_BIZ_TYPE);
			examineVo.getEntity().setKernelBizMode(KERNEL_BIZ_MODE);
			//核放单编号：核放单类型代码（核放单GAT）+4位年份+进出标志（1:进／2:出）+7位流水号
			examineNo = Constant.BIZTYPE_CODE_EXAMINE +year+ i_flag + no;
		}
		//若是普通仓，则是分类监管的核放单
		else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			String KERNEL_TYPE = paramService.getKey(CacheName.KERNEL_TYPE);
			String KERNEL_BIZ_TYPE = paramService.getKey(CacheName.KERNEL_BIZ_TYPE);
			String CLASSMESSAGE_KERNEL_BIZ_MODE = paramService.getKey(CacheName.CLASSMESSAGE_KERNEL_BIZ_MODE);
			examineVo.getEntity().setKernelType(KERNEL_TYPE);
			examineVo.getEntity().setKernelBizType(KERNEL_BIZ_TYPE);
			examineVo.getEntity().setKernelBizMode(CLASSMESSAGE_KERNEL_BIZ_MODE);
			examineVo.getEntity().setExamineFrom(Constant.APPLY_FROM_CLASS_MASSAGE);
			//编号规则：QW+4位年份+进出标志（1:进／2:出）+7位流水号
			examineNo = "QW" +year+ i_flag + no;
		}
		//检查申请表编号是否已存在
		examineNoIsExist(examineNo);
		
		String id = IdUtil.getUUID();
		
		//新增核放申请   统计总件数，总重量，货物重量
		if(examineVo.getExamineApplicationVoList() == null || examineVo.getExamineApplicationVoList().isEmpty()) 
			throw new BizException("examine_application_is_null");
		//查询车辆信息
//		VehicleVo vehicleVo = vehicleService.view(examineVo.getEntity().getCarId());
//		BigDecimal totalwt = examineVo.getEntity().getCarWt().add(examineVo.getEntity().getGoodsWt());
	
		examineVo.getEntity().setId(id); 
		examineVo.getEntity().setExamineNo(examineNo);
//		examineVo.getEntity().setTotalWt(totalwt);
		examineVo.getEntity().setStatus(Constant.EXAMINE_STATUS_OPEN);
		examineVo.getEntity().setAuditStep(Constant.EXAMINE_STATUS_NO_SEND);
		examineVo.getEntity().setDeclarePerson(p.getUserName());
		examineVo.getEntity().setDeclareTime(today);
		examineVo.getEntity().setOrgId(p.getOrgId());
		examineVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		examineVo.getEntity().setCreatePerson(p.getUserName());
		
		examineVo = exAppService.addExamineApplications(examineVo, p);
		
		examineVo.getEntity().setUpdatePerson(p.getUserId());
		//若是保税仓，就是分送集报的核放单
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
			examineVo.getEntity().setExamineFrom(Constant.APPLY_FROM_DELIVER_GOODS);
		}
		//若是普通仓，则是分类监管的核放单
		else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			examineVo.getEntity().setExamineFrom(Constant.APPLY_FROM_CLASS_MASSAGE);
		}
		examineDao.insertSelective(examineVo.getEntity());

		return examineVo;
	}
	
	/**
	 * 检查核放单编号是否存在
	 * @param examineNo
	 */
	public void examineNoIsExist(String examineNo){
		DeliverGoodsExamineVo examineVo = new DeliverGoodsExamineVo(new DeliverGoodsExamine());
		examineVo.getEntity().setExamineNo(examineNo);
		List<DeliverGoodsExamine> list = examineDao.selectByExample(examineVo.getExample());
		if(list != null && list.size() > 0)
			throw new BizException("examine_no_is_exist");
	}
	
	/**
	 * 新增生效
	 * @param examineVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsExamineVo saveAndEnable(DeliverGoodsExamineVo examineVo,Principal p)throws Exception{
		examineVo = addOrUpdate(examineVo,p);
		
		enable(examineVo.getEntity().getId(),p);
		
		examineVo.getEntity().setStatus(Constant.EXAMINE_STATUS_ACTIVE);
		return examineVo;
	}
	
	/**
	 * 新增或更新
	 * @param examineVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsExamineVo addOrUpdate(DeliverGoodsExamineVo examineVo,Principal p)throws Exception{
		if(examineVo == null) throw new BizException("data_is_null");
		
		DeliverGoodsExamineVo vo = null;
		if(StringUtil.isNotBlank(examineVo.getEntity().getId())){
			vo = update(examineVo,p);
		}else{
			vo = add(examineVo,p);
		}
		return vo;
	}
	/**
	 * 修改
	 * @param examineVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsExamineVo update(DeliverGoodsExamineVo examineVo,Principal p)throws Exception{
		if(examineVo == null) throw new BizException("data_is_null") ;
		
		//更新核放申请
		examineVo = exAppService.update(examineVo, p);
		
		examineVo.getEntity().setUpdatePerson(p.getUserId());
		examineDao.updateByPrimaryKeySelective(examineVo.getEntity());

		
		return examineVo;
	}
	
	/**
	 * 查看
	 * @param examineVo
	 * @return
	 */
	public DeliverGoodsExamineVo view(String examineId) throws Exception{
		
		DeliverGoodsExamine examine = examineDao.selectByPrimaryKey(examineId);
		DeliverGoodsExamineVo examineVo = chg(examine);
		
		return examineVo;
	}
	
	/**
	 * 生效
	 * @param examineVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void enable(String examineId,Principal p)throws Exception{
		//检查状态是否打开
		queryAndCheckStatus(examineId,Constant.EXAMINE_STATUS_OPEN,"examine_stauts_is_not_open");
		
		DeliverGoodsExamine entity = new DeliverGoodsExamine();
		entity.setId(examineId);
		entity.setStatus(Constant.EXAMINE_STATUS_ACTIVE);
		entity.setUpdatePerson(p.getUserId());
		examineDao.updateByPrimaryKeySelective(entity);
		
		//查看核放单
		DeliverGoodsExamineVo examineVo = view(examineId);
		
		//增加申请单审批核放数量
		for(DeliverGoodsExamineApplicationVo applicationVo:examineVo.getExamineApplicationVoList()){
			for(DeliverGoodsExamineApplicationDetailVo detailVo:applicationVo.getDetailVoList()){
				DeliverGoodsApplicationGoodsSku goodsSku = goodsSkuDao.selectByPrimaryKey(detailVo.getEntity().getGoodsSkuId());
				if(detailVo.getEntity().getDecQty().compareTo(goodsSku.getDecQty()) > 0) 
					throw new BizException("examine_decqty_not_more_than_application_decqty");
				BigDecimal auditQty = goodsSku.getAuditQty().add(detailVo.getEntity().getDecQty());
				BigDecimal remainQty = goodsSku.getRemainQty().subtract(detailVo.getEntity().getDecQty());
				if(remainQty.compareTo(new BigDecimal("0")) < 0) 
					throw new BizException("examine_total_has_more_than_application_decqty");
				goodsSku.setAuditQty(auditQty);
				goodsSku.setRemainQty(remainQty);
				goodsSku.setUpdatePerson(p.getUserId());
				goodsSkuDao.updateByPrimaryKeySelective(goodsSku);
			}
			applicationService.changeStatus(applicationVo.getEntity().getApplicationId());
		}
		
	}
	
	/**
	 * 失效
	 * @param examineVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void disable(String examineId,Principal p)throws Exception{
		//检查状态是否生效
		queryAndCheckStatus(examineId,Constant.EXAMINE_STATUS_ACTIVE,"examine_stauts_is_not_active");
		//检查是否已审批通过
		checkAuditStep(examineId,Constant.EXAMINE_STATUS_AUDIT_SUCCESS,"examine_has_audit_successs");
		
		DeliverGoodsExamine entity = new DeliverGoodsExamine();
		entity.setId(examineId);
		entity.setStatus(Constant.EXAMINE_STATUS_OPEN);
		entity.setUpdatePerson(p.getUserId());
		examineDao.updateByPrimaryKeySelective(entity);
		
		//查看核放单
		DeliverGoodsExamineVo examineVo = view(examineId);
		
		//减少申请单审批核放数量
		for(DeliverGoodsExamineApplicationVo applicationVo:examineVo.getExamineApplicationVoList()){
			if(applicationVo.getDetailVoList() == null || applicationVo.getDetailVoList().isEmpty()) continue;
			
			for(DeliverGoodsExamineApplicationDetailVo detailVo:applicationVo.getDetailVoList()){
				DeliverGoodsApplicationGoodsSku goodsSku = goodsSkuDao.selectByPrimaryKey(detailVo.getEntity().getGoodsSkuId());
				BigDecimal auditQty = goodsSku.getAuditQty().subtract(detailVo.getEntity().getDecQty());
				BigDecimal remainQty = goodsSku.getRemainQty().add(detailVo.getEntity().getDecQty());
				goodsSku.setAuditQty(auditQty);
				goodsSku.setRemainQty(remainQty);
				goodsSku.setUpdatePerson(p.getUserId());
				goodsSkuDao.updateByPrimaryKeySelective(goodsSku);
			}
		}
		
		
	}
	
	/**
	 * 取消
	 * @param examineVo
	 * @return
	 */
	public void cancel(String examineId,Principal p)throws Exception{
		//检查状态是否打开
		queryAndCheckStatus(examineId,Constant.EXAMINE_STATUS_OPEN,"examine_stauts_is_not_open");
		
		DeliverGoodsExamine entity = new DeliverGoodsExamine();
		entity.setId(examineId);
		entity.setStatus(Constant.EXAMINE_STATUS_CANCAL);
		entity.setUpdatePerson(p.getUserId());
		examineDao.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 发送申请单
	 * @param examineVo
	 * @throws Exception
	 */
	public void sendExamine(String examineId,Principal p)throws Exception{
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		//若是保税仓，就是分送集报的核放单
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
			sendDeliverGoodsExamine(examineId, p);
		}
		//若是普通仓，则是分类监管的核放单
		else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			sendClassManageExamine(examineId, p);
		}
	}
	
	/**
	 * 发送分类监管核放单报文
	 * @param examineId
	 * @param p
	 * @throws Exception
	 */
	public void sendClassManageExamine(String examineId,Principal p)throws Exception{
		
		String messageType = paramService.getKey(CacheName.MESSAGE_TYPE_KERNEL);
		String AREA_CODE = paramService.getKey(CacheName.AREA_CODE);
		String CUSTOM_CODE = paramService.getKey(CacheName.CUSTOM_CODE);
		String examineType= paramService.getKey(CacheName.KERNEL_TYPE);
		String bizType = paramService.getKey(CacheName.KERNEL_BIZ_TYPE);
		String bizMode = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_BIZ_MODE);
		String begArea = paramService.getKey(CacheName.KERNEL_BEG_AREA);
		String endArea = paramService.getKey(CacheName.KERNEL_END_AREA);
		String currentStepId = paramService.getKey(CacheName.CLASSMESSAGE_KERNEL_STEP_ID);
		String receiver_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		String declare_biz_type = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_BIZ_TYPE);
		String DETAIN_FLAG = paramService.getKey(CacheName.DETAIN_FLAG); 
		
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		//查询申请单
		DeliverGoodsExamineVo examineVo = view(examineId);
		
		String messageId = IdUtil.getUUID();
		
		DeliverGoodsXMLMessageUtil messageUtil = new DeliverGoodsXMLMessageUtil();
		//设置messageHead
		messageUtil.getMessageHeadMap().put("MESSAGE_ID", messageId);
		messageUtil.getMessageHeadMap().put("MESSAGE_TYPE", messageType);
		messageUtil.getMessageHeadMap().put("ORDER_NO", examineVo.getEntity().getExamineNo());
		messageUtil.getMessageHeadMap().put("FUNCTION_CODE", "3");
		messageUtil.getMessageHeadMap().put("MESSAGE_DATE", dateTime);
		messageUtil.getMessageHeadMap().put("SENDER_ID", Constant.SYSTEM_TYPE_WMS);
		messageUtil.getMessageHeadMap().put("RECEIVER_ID", receiver_id);
		
		//设置kernelHeadMap
		messageUtil.getKernelHeadMap().put("KERNEL_NO", examineVo.getEntity().getExamineNo());
		messageUtil.getKernelHeadMap().put("KERNEL_TYPE", examineType);
		messageUtil.getKernelHeadMap().put("KERNEL_BIZ_TYPE", bizType);
		messageUtil.getKernelHeadMap().put("KERNEL_BIZ_MODE", bizMode);
		messageUtil.getKernelHeadMap().put("BEG_AREA", begArea);
		messageUtil.getKernelHeadMap().put("END_AREA", endArea);
		messageUtil.getKernelHeadMap().put("I_E_FLAG", examineVo.getEntity().getIEFlag());
		messageUtil.getKernelHeadMap().put("IC_CARD", examineVo.getEntity().getIcCard());
		messageUtil.getKernelHeadMap().put("CAR_NO", examineVo.getEntity().getCarNo());
		messageUtil.getKernelHeadMap().put("CAR_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getCarWt()));
		messageUtil.getKernelHeadMap().put("GOODS_WT",  ApplicationStringUtil.parseNum(examineVo.getEntity().getGoodsWt()));
		messageUtil.getKernelHeadMap().put("FRAME_NO", examineVo.getEntity().getFrameNo());
		messageUtil.getKernelHeadMap().put("FRAME_TYPE", examineVo.getEntity().getFrameType());
		messageUtil.getKernelHeadMap().put("FRAME_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getFrameWt()));
		messageUtil.getKernelHeadMap().put("F_CONTA_NO", examineVo.getEntity().getfContaNo());
		messageUtil.getKernelHeadMap().put("F_CONTA_TYPE", examineVo.getEntity().getfContaType());
		messageUtil.getKernelHeadMap().put("F_CONTA_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getfContaWt()));
		messageUtil.getKernelHeadMap().put("A_CONTA_NO", examineVo.getEntity().getaContaNo());
		messageUtil.getKernelHeadMap().put("A_CONTA_TYPE", examineVo.getEntity().getaContaType());
		messageUtil.getKernelHeadMap().put("A_CONTA_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getaContaType()));
		messageUtil.getKernelHeadMap().put("TOTAL_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getTotalWt()));
		messageUtil.getKernelHeadMap().put("TOTAL_PACK_NO", ApplicationStringUtil.parseNum(examineVo.getEntity().getTotalPackNo()));
		messageUtil.getKernelHeadMap().put("BILL_NO", examineVo.getEntity().getBillNo());
		messageUtil.getKernelHeadMap().put("MAIN_ITEMS", examineVo.getEntity().getMainItems());
		messageUtil.getKernelHeadMap().put("REMARK", examineVo.getEntity().getRemark());
		
		//kernelBodyList
		for(int i=0;i<examineVo.getExamineApplicationVoList().size();i++){
			DeliverGoodsExamineApplicationVo exaAppVo = examineVo.getExamineApplicationVoList().get(i);
			
			DeliverGoodsApplicationVo apllicationVo = applicationService.view(exaAppVo.getEntity().getApplicationId());
			
			Map<String,String> kernelBodyMap = new HashMap<String,String>();
			kernelBodyMap.put("AUTO_ID", exaAppVo.getEntity().getId());
			kernelBodyMap.put("KERNEL_NO", examineVo.getEntity().getExamineNo());
			kernelBodyMap.put("GATEJOB_NO", apllicationVo.getEntity().getApplicationNo());
			kernelBodyMap.put("I_E_FLAG", apllicationVo.getEntity().getiEFlag());
			kernelBodyMap.put("BIZ_TYPE", declare_biz_type);
			kernelBodyMap.put("REL_GATEJOB_NO", apllicationVo.getEntity().getRelApplicationNo());
			kernelBodyMap.put("BILL_NO", exaAppVo.getEntity().getBillNo());
			kernelBodyMap.put("PACK_NO", ApplicationStringUtil.parseNum(exaAppVo.getEntity().getPackNo()));
			kernelBodyMap.put("WEIGHT", ApplicationStringUtil.parseNum(exaAppVo.getEntity().getWeight()));
			kernelBodyMap.put("LAST_CAR_FLAG", exaAppVo.getEntity().getLastCarFlag());
			kernelBodyMap.put("DETAIN_FLAG", DETAIN_FLAG);
			kernelBodyMap.put("REMARK", exaAppVo.getEntity().getRemark());
			messageUtil.getKernelBodyList().add(kernelBodyMap);
		}
	  	//查询用户
//		SysUser creater = userService.get(examineVo.getEntity().getCreatePerson());
//		SysUser declarer = userService.get(examineVo.getEntity().getDeclarePerson());
		
		//statKernetMap
		messageUtil.getStatKernetMap().put("KERNEL_NO", examineVo.getEntity().getExamineNo());
		messageUtil.getStatKernetMap().put("AREA_CODE", AREA_CODE);
		messageUtil.getStatKernetMap().put("CUSTOMS_CODE", CUSTOM_CODE);
		messageUtil.getStatKernetMap().put("INPUT_CODE", examineVo.getEntity().getInputOrgCode());
		messageUtil.getStatKernetMap().put("INPUT_NAME", examineVo.getEntity().getInputOrgName());
		messageUtil.getStatKernetMap().put("DECLARE_CODE", examineVo.getEntity().getDeclareOrgCode());
		messageUtil.getStatKernetMap().put("DECLARE_NAME", examineVo.getEntity().getDeclareOrgName());
		messageUtil.getStatKernetMap().put("STEP_ID", currentStepId);
		messageUtil.getStatKernetMap().put("CREATE_PERSON", examineVo.getEntity().getCreatePerson());
		messageUtil.getStatKernetMap().put("CREATE_DATE", ApplicationStringUtil.parseDate(examineVo.getEntity().getCreateTime()));
		messageUtil.getStatKernetMap().put("DECLARE_PERSON", examineVo.getEntity().getDeclarePerson());
		messageUtil.getStatKernetMap().put("DECLARE_DATE", ApplicationStringUtil.parseDate(examineVo.getEntity().getDeclareTime()));
		
		//生成报文
		String xmlStr = messageUtil.createExamineXMLMessage(examineVo.getEntity().getExamineFrom());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messageId+"_核放单数据报文：["+xmlStr+"]");	

//		String sendChnalName = paramService.getKey(CacheName.MSMQ_DECLARE_CHNALNAME);
//		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
		//发送报文
		sendXmlMessage(xmlStr,examineVo.getEntity().getId(),messageId,p);
	}
	
	/**
	 * 发送分送集报核放单报文
	 * @param examineVo
	 * @throws Exception
	 */
	public void sendDeliverGoodsExamine(String examineId,Principal p)throws Exception{
		
		String messageType = paramService.getKey(CacheName.MESSAGE_TYPE_KERNEL);
		String AREA_CODE = paramService.getKey(CacheName.AREA_CODE);
		String CUSTOM_CODE = paramService.getKey(CacheName.CUSTOM_CODE);
		String examineType= paramService.getKey(CacheName.KERNEL_TYPE);
		String bizType = paramService.getKey(CacheName.KERNEL_BIZ_TYPE);
		String bizMode = paramService.getKey(CacheName.KERNEL_BIZ_MODE);
		String begArea = paramService.getKey(CacheName.KERNEL_BEG_AREA);
		String endArea = paramService.getKey(CacheName.KERNEL_END_AREA);
		String currentStepId = paramService.getKey(CacheName.KERNEL_CURRENT_STEP_ID);
		String iscricle = paramService.getKey(CacheName.CHECK_IS_CIRCLE);
		String receiver_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		String declare_biz_type = paramService.getKey(CacheName.APPLICATION_BIZ_TYPE);
		String DETAIN_FLAG = paramService.getKey(CacheName.DETAIN_FLAG); 
		
		//查询申请单
		DeliverGoodsExamineVo examineVo = view(examineId);
		
		//查询车辆
//		VehicleVo vehicleVo = vehicleService.view(examineVo.getEntity().getCarId());
	
		DeliverGoodsXMLMessageUtil messageUtil = new DeliverGoodsXMLMessageUtil();
		
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		
		//设置messageHead
		String messageId = IdUtil.getUUID();
		messageUtil.getMessageHeadMap().put("MESSAGE_ID", messageId);
		messageUtil.getMessageHeadMap().put("MESSAGE_TYPE", messageType);
		messageUtil.getMessageHeadMap().put("ORDER_NO", examineVo.getEntity().getExamineNo());
		messageUtil.getMessageHeadMap().put("FUNCTION_CODE", "3");
		messageUtil.getMessageHeadMap().put("MESSAGE_DATE", dateTime);
		messageUtil.getMessageHeadMap().put("SENDER_ID", Constant.SYSTEM_TYPE_WMS);
		messageUtil.getMessageHeadMap().put("RECEIVER_ID", receiver_id);
		
		//设置kernelHeadMap
		messageUtil.getKernelHeadMap().put("KERNEL_NO", examineVo.getEntity().getExamineNo());
		messageUtil.getKernelHeadMap().put("KERNEL_TYPE", examineType);
		messageUtil.getKernelHeadMap().put("KERNEL_BIZ_TYPE", bizType);
		messageUtil.getKernelHeadMap().put("KERNEL_BIZ_MODE", bizMode);
		messageUtil.getKernelHeadMap().put("BEG_AREA", begArea);
		messageUtil.getKernelHeadMap().put("END_AREA", endArea);
		messageUtil.getKernelHeadMap().put("I_E_FLAG", examineVo.getEntity().getIEFlag());
		messageUtil.getKernelHeadMap().put("IC_CARD", examineVo.getEntity().getIcCard());
		messageUtil.getKernelHeadMap().put("CAR_NO", examineVo.getEntity().getCarNo());
		messageUtil.getKernelHeadMap().put("CAR_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getCarWt()));
		messageUtil.getKernelHeadMap().put("GOODS_WT",  ApplicationStringUtil.parseNum(examineVo.getEntity().getGoodsWt()));
		messageUtil.getKernelHeadMap().put("FRAME_NO", examineVo.getEntity().getFrameNo());
		messageUtil.getKernelHeadMap().put("FRAME_TYPE", examineVo.getEntity().getFrameType());
		messageUtil.getKernelHeadMap().put("FRAME_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getFrameWt()));
		messageUtil.getKernelHeadMap().put("F_CONTA_NO", examineVo.getEntity().getfContaNo());
		messageUtil.getKernelHeadMap().put("F_CONTA_TYPE", examineVo.getEntity().getfContaType());
		messageUtil.getKernelHeadMap().put("F_CONTA_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getfContaWt()));
		messageUtil.getKernelHeadMap().put("A_CONTA_NO", examineVo.getEntity().getaContaNo());
		messageUtil.getKernelHeadMap().put("A_CONTA_TYPE", examineVo.getEntity().getaContaType());
		messageUtil.getKernelHeadMap().put("A_CONTA_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getaContaType()));
		messageUtil.getKernelHeadMap().put("TOTAL_WT", ApplicationStringUtil.parseNum(examineVo.getEntity().getTotalWt()));
		messageUtil.getKernelHeadMap().put("TOTAL_PACK_NO", ApplicationStringUtil.parseNum(examineVo.getEntity().getTotalPackNo()));
		messageUtil.getKernelHeadMap().put("BILL_NO", examineVo.getEntity().getBillNo());
		messageUtil.getKernelHeadMap().put("MAIN_ITEMS", examineVo.getEntity().getMainItems());
		messageUtil.getKernelHeadMap().put("IS_CIRCLE", iscricle);
		messageUtil.getKernelHeadMap().put("REMARK", examineVo.getEntity().getRemark());
		
		//kernelBodyList
		for(int i=0;i<examineVo.getExamineApplicationVoList().size();i++){
			DeliverGoodsExamineApplicationVo exaAppVo = examineVo.getExamineApplicationVoList().get(i);
			
			DeliverGoodsApplicationVo apllicationVo = applicationService.view(exaAppVo.getEntity().getApplicationId());
			
			DeliverGoodsApplicationFormVo formVo = applicationFormService.view(apllicationVo.getEntity().getApplicationFormId());
			
			Map<String,String> kernelBodyMap = new HashMap<String,String>();
			kernelBodyMap.put("AUTO_ID", exaAppVo.getEntity().getId());
			kernelBodyMap.put("KERNEL_NO", examineVo.getEntity().getExamineNo());
			kernelBodyMap.put("GATEJOB_NO", apllicationVo.getEntity().getApplicationNo());
			kernelBodyMap.put("I_E_FLAG", apllicationVo.getEntity().getiEFlag());
			kernelBodyMap.put("BIZ_TYPE", declare_biz_type);
			kernelBodyMap.put("REL_GATEJOB_NO", apllicationVo.getEntity().getRelApplicationNo());
			kernelBodyMap.put("BILL_NO", exaAppVo.getEntity().getBillNo());
			kernelBodyMap.put("PACK_NO", ApplicationStringUtil.parseNum(exaAppVo.getEntity().getPackNo()));
			kernelBodyMap.put("WEIGHT", ApplicationStringUtil.parseNum(exaAppVo.getEntity().getWeight()));
			kernelBodyMap.put("LAST_CAR_FLAG", exaAppVo.getEntity().getLastCarFlag());
			kernelBodyMap.put("DETAIN_FLAG", DETAIN_FLAG);
			kernelBodyMap.put("REMARK", exaAppVo.getEntity().getRemark());
			messageUtil.getKernelBodyList().add(kernelBodyMap);
			
			//kernelListList
			for(DeliverGoodsExamineApplicationDetailVo detailVo:exaAppVo.getDetailVoList()){
				Map<String,String> kernelListMap = new HashMap<String,String>();
				
				MetaSku sku = skuService.get(detailVo.getEntity().getSkuId());
				
				kernelListMap.put("AUTO_ID", detailVo.getEntity().getId());
				kernelListMap.put("KERNEL_NO_REL", detailVo.getEntity().getKernelNoRel());
				kernelListMap.put("KERNEL_NO", examineVo.getEntity().getExamineNo());
				kernelListMap.put("KERNEL_TYPE", examineType);
				kernelListMap.put("KERNEL_BIZ_TYPE", bizType);
				kernelListMap.put("KERNEL_BIZ_MODE", bizMode);
				kernelListMap.put("TRADE_CODE", formVo.getEntity().getTradeCode());
				kernelListMap.put("TRADE_NAME", formVo.getEntity().getTradeName());
				kernelListMap.put("G_NAME", sku.getSkuName());
				kernelListMap.put("G_MODEL", sku.getSpecModel());
				kernelListMap.put("UNIT", sku.getMeasureUnit());
				kernelListMap.put("DEC_QTY", ApplicationStringUtil.parseNum(detailVo.getEntity().getDecQty()));
				kernelListMap.put("GROSS_WT", ApplicationStringUtil.parseNum(detailVo.getEntity().getGrossWt()));
				kernelListMap.put("PACK_NO", ApplicationStringUtil.parseNum(detailVo.getEntity().getPackNo()));
				messageUtil.getKernelListList().add(kernelListMap);
			}
		}
		//查询用户
//		SysUser creater = userService.get(examineVo.getEntity().getCreatePerson());
//		SysUser declarer = userService.get(examineVo.getEntity().getDeclarePerson());
		//statKernetMap
		messageUtil.getStatKernetMap().put("KERNEL_NO", examineVo.getEntity().getExamineNo());
		messageUtil.getStatKernetMap().put("AREA_CODE", AREA_CODE);
		messageUtil.getStatKernetMap().put("CUSTOMS_CODE", CUSTOM_CODE);
		messageUtil.getStatKernetMap().put("INPUT_CODE", examineVo.getEntity().getInputOrgCode());
		messageUtil.getStatKernetMap().put("INPUT_NAME", examineVo.getEntity().getInputOrgName());
		messageUtil.getStatKernetMap().put("DECLARE_CODE", examineVo.getEntity().getDeclareOrgCode());
		messageUtil.getStatKernetMap().put("DECLARE_NAME", examineVo.getEntity().getDeclareOrgName());
		messageUtil.getStatKernetMap().put("STEP_ID", currentStepId);
		messageUtil.getStatKernetMap().put("CREATE_PERSON", examineVo.getEntity().getCreatePerson());
		messageUtil.getStatKernetMap().put("CREATE_DATE", ApplicationStringUtil.parseDate(examineVo.getEntity().getCreateTime()));
		messageUtil.getStatKernetMap().put("DECLARE_PERSON", examineVo.getEntity().getDeclarePerson());
		messageUtil.getStatKernetMap().put("DECLARE_DATE", ApplicationStringUtil.parseDate(examineVo.getEntity().getDeclareTime()));
		
		//生成报文
		String xmlStr = messageUtil.createExamineXMLMessage(examineVo.getEntity().getExamineFrom());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messageId+"_核放单数据报文：["+xmlStr+"]");	

//		String sendChnalName = paramService.getKey(CacheName.MSMQ_DECLARE_CHNALNAME);
//		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);

		//发送报文
		sendXmlMessage(xmlStr,examineVo.getEntity().getId(),messageId,p);
	}
	
	/**
	 * 发送xml报文
	 * @param xmlStr
	 * @param sendChnalName
	 * @param label
	 * @param applicationId
	 * @param messageId
	 * @param p
	 */
	public void sendXmlMessage(String xmlStr,String examineId,String messageId,Principal p)throws Exception{
		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
		//传送入库数据到仓储企业联网监管系统
		if(log.isInfoEnabled()) log.info("Queue名称：["+sendChnalName+"]");
		boolean result = MSMQUtil.send(sendChnalName, label, xmlStr, messageId.getBytes());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messageId+"_申报单数据发送接口运行结果：["+result+"]");	
		
		//保存报文
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setMessageId(messageId);
		messageVo.getEntity().setFunctionType(Constant.FUNCTION_TYPE_EXAMINE);
		messageVo.getEntity().setOrderNo(examineId);
		messageVo.getEntity().setContent(xmlStr);
		messageVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
		messageVo.getEntity().setReceiver(Constant.SYSTEM_CUST_ID);
		messageVo.getEntity().setSendTime(new Date());
		messageVo.getEntity().setCreatePerson(p.getUserId());
		messageVo.getEntity().setOrgId(p.getOrgId());
		messageVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		msmqMessageService.add(messageVo);
		
		//修改审批环节
		if(result){
			updateAuditStepById(examineId,Constant.EXAMINE_STATUS_SEND_SUCCESS,p);
		}else{
			updateAuditStepById(examineId,Constant.EXAMINE_STATUS_SEND_FAIL,p);
		}
	}
	
	/**
	 * 审批通过
	 * @param examineId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void passAudit(String examineId)throws Exception{
		DeliverGoodsExamineVo examineVo = view(examineId);
		
		//检查状态，若核放单已审批通过则不再处理
		if(Constant.EXAMINE_STATUS_AUDIT_SUCCESS.equals(examineVo.getEntity().getAuditStep()))
			return;
		//更新核放单发送结果
		DeliverGoodsExamine examine = new DeliverGoodsExamine();
		examine.setId(examineId);
		examine.setAuditStep(Constant.EXAMINE_STATUS_AUDIT_SUCCESS);
		examineDao.updateByPrimaryKeySelective(examine);
		
		
		for(DeliverGoodsExamineApplicationVo examAppVo:examineVo.getExamineApplicationVoList()){
			for(DeliverGoodsExamineApplicationDetailVo detailVo:examAppVo.getDetailVoList()){
				DeliverGoodsApplicationGoodsSkuVo gsVo = goodsSkuService.view(detailVo.getEntity().getGoodsSkuId());
				//减少申请单审批数量
				gsVo.getEntity().setAuditQty(gsVo.getEntity().getAuditQty().subtract(detailVo.getEntity().getDecQty()));
				//
				//增加申请单已审批数量
				gsVo.getEntity().setPassAuditQty(gsVo.getEntity().getPassAuditQty().add(detailVo.getEntity().getDecQty()));
				goodsSkuService.updateEntity(gsVo.getEntity());
			}
			//判断申请单是否都核放
			applicationService.isAllPassExamine(examAppVo.getEntity().getApplicationId());
			
		}
		
		
	}
	
	/**
	 * 核放单退单
	 * @param examineId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void changeBack(String examineId)throws Exception{
		
		DeliverGoodsExamineVo examineVo = view(examineId);
		//检查状态，若核放单已审批通过则不再处理
		if(Constant.EXAMINE_STATUS_AUDIT_SUCCESS.equals(examineVo.getEntity().getAuditStep()))
			return;
		//更新核放单发送结果
		DeliverGoodsExamine examine = new DeliverGoodsExamine();
		examine.setId(examineId);
		examine.setAuditStep(Constant.EXAMINE_STATUS_CHARGEBACK);
		examineDao.updateByPrimaryKeySelective(examine);
		
//		for(DeliverGoodsExamineApplicationVo examAppVo:examineVo.getExamineApplicationVoList()){
//			for(DeliverGoodsExamineApplicationDetailVo detailVo:examAppVo.getDetialVoList()){
//				DeliverGoodsApplicationGoodsSkuVo gsVo = goodsSkuService.view(detailVo.getEntity().getGoodsSkuId());
//				//减少申请单审批数量
//				gsVo.getEntity().setAuditQty(gsVo.getEntity().getAuditQty().subtract(detailVo.getEntity().getDecQty()));
//				//增加申请单剩余数量
//				gsVo.getEntity().setRemainQty(gsVo.getEntity().getRemainQty().add(detailVo.getEntity().getDecQty()));
//				goodsSkuService.updateEntity(gsVo.getEntity());
//			}
//		}
		
	}
	
	/**
	 * 修改申请单状态
	 * @param examineId
	 * @param status
	 * @throws Exception
	 */
	public void updateAuditStepById(String examineId,String step,Principal p)throws Exception{
		DeliverGoodsExamineVo examineVo = new DeliverGoodsExamineVo(new DeliverGoodsExamine());
		examineVo.getEntity().setId(examineId);
		examineVo.getEntity().setAuditStep(step);
		examineVo.getEntity().setUpdatePerson(p.getUserId());
		examineDao.updateByPrimaryKeySelective(examineVo.getEntity());
	}
	
	/**
	 * 查询并检查状态
	 * @param formIdR
	 * @param isStatus
	 * @param msg
	 */
	public void queryAndCheckStatus(String examineId,Integer isStatus,String msg){
		DeliverGoodsExamine record = examineDao.selectByPrimaryKey(examineId);
		if(isStatus.intValue() != record.getStatus().intValue())
			throw new BizException(msg);
	}
	
	/**
	 * 检查审批环节
	 * @param examineId
	 * @param step
	 * @param msg
	 */
	public void checkAuditStep(String examineId,String isStep,String msg){
		DeliverGoodsExamine record = examineDao.selectByPrimaryKey(examineId);
		if(isStep.equals(record.getAuditStep())){
			throw new BizException(msg);
		}
	}
	
	
}
