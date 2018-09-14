package com.yunkouan.wms.modules.application.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
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
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationDao;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationGoodDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGood;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGoodsSku;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationFormService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodsSkuService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.util.ApplicationStringUtil;
import com.yunkouan.wms.modules.application.util.DeliverGoodsXMLMessageUtil;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationFormVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodsSkuVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.message.service.IMessageService;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.rec.dao.IASNDao;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.send.dao.IDeliveryDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;

/**
 * 申请单服务
 * @author Aaron
 *
 */
@Service
public class DeliverGoodsApplicationServiceImpl extends BaseService implements IDeliverGoodsApplicationService{

	private Logger log = LoggerFactory.getLogger(DeliverGoodsApplicationServiceImpl.class);
	@Autowired
	private IDeliverGoodsApplicationDao applicationDao;
	
	@Autowired
	private IDeliverGoodsApplicationGoodDao applicationGoodDao;

	@Autowired
	private IDeliverGoodsApplicationGoodService goodService;
	
	@Autowired
	private IDeliverGoodsApplicationFormService applicationFormService;
	
	@Autowired
	private IMsmqMessageService msmqMessageService;
	
	@Autowired
	private IDeliverGoodsApplicationGoodsSkuService goodsSkuService;
	
	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IASNDao asnDao;
	
	@Autowired
	private IDeliveryDao deliveryDao;
	
	@Autowired
	private ISysParamService paramService;

	/**
	 * 分页查询
	 * @param formVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public ResultModel pageList(DeliverGoodsApplicationVo applicationVo) throws DaoException, ServiceException{
		ResultModel result = new ResultModel();
		Page<DeliverGoodsApplicationVo> page = null;
		if ( applicationVo.getCurrentPage() == null ) {
			applicationVo.setCurrentPage(0);
		}
		if ( applicationVo.getPageSize() == null ) {
			applicationVo.setPageSize(20);
		}
		page = PageHelper.startPage(applicationVo.getCurrentPage()+1, applicationVo.getPageSize());
		
		//不查询取消状态的记录
//		if(formVo.getEntity().getStatus() == null){
//			formVo.setStatusLessThan(Constant.);
//		}
		List<DeliverGoodsApplication> recordList = applicationDao.selectByExample(applicationVo.getExample());
		if(recordList == null || recordList.isEmpty()) return result;
		result.setPage(page);
		
		List<DeliverGoodsApplicationVo> voList = new ArrayList<DeliverGoodsApplicationVo>();
		for(DeliverGoodsApplication entity:recordList){
			DeliverGoodsApplicationVo vo = chg(entity);
			voList.add(vo);
		}
		result.setList(voList);
		
		return result;
	}
	private DeliverGoodsApplicationVo chg(DeliverGoodsApplication entity) throws DaoException, ServiceException {
		DeliverGoodsApplicationVo vo = new DeliverGoodsApplicationVo(entity);
		//查询商品
		DeliverGoodsApplicationGoodVo goodsVo = new DeliverGoodsApplicationGoodVo(new DeliverGoodsApplicationGood());
		goodsVo.getEntity().setApplicationId(vo.getEntity().getId());
		List<DeliverGoodsApplicationGoodVo> goodsVoList = goodService.qryList(goodsVo);
		vo.setApplicationGoodVoList(goodsVoList);
		vo.setStatusName(paramService.getValue("APPLICATION_STATUS", entity.getStatus()));
		vo.setAuditStepName(paramService.getValue("APPLICATION_STEP", entity.getAuditStep()));
		vo.setiEFlagName(paramService.getValue("I_E_FLAG", entity.getiEFlag()));
		vo.setDeclareTypeName(paramService.getValue(CacheName.DECLARE_TYPE, entity.getDeclareType()));
		vo.setBizModeName(paramService.getValue(CacheName.DECLARE_BIZ_MODE, entity.getBizMode()));
		vo.setBizTypeName(paramService.getValue(CacheName.DECLARE_BIZ_TYPE, entity.getBizType()));
		vo.setWrapTypeName(paramService.getValue(CacheName.WRAP_TYPE, entity.getWrapType()));
		if (StringUtil.isNoneBlank(entity.getApplicationFormId())) {
			vo.setFormVo(applicationFormService.view(entity.getApplicationFormId()));
		}
		return vo;
	}
	/**
	 * 查询
	 * @param formVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationVo> qryList(DeliverGoodsApplicationVo applicationVo) throws DaoException, ServiceException{
		List<DeliverGoodsApplication> recordList = applicationDao.selectByExample(applicationVo.getExample());
		
		List<DeliverGoodsApplicationVo> voList = new ArrayList<DeliverGoodsApplicationVo>();
		for(DeliverGoodsApplication entity:recordList){
			DeliverGoodsApplicationVo vo = chg(entity);
			voList.add(vo);
		}
		return voList;
	}
	

	/**
	 * 查找所有未完成核放申报的申请单
	 * @param appId
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	/* (non-Javadoc)
	 * @see com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService#qryAllUnexamine(com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal)
	 */
	public List<DeliverGoodsApplicationVo> qryAllUnexamine(Principal p) throws Exception{

		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		//查询没有完成核放审批的货品列表
		DeliverGoodsApplicationGoodsSkuVo _skuParamVo = new DeliverGoodsApplicationGoodsSkuVo(new DeliverGoodsApplicationGoodsSku());
		_skuParamVo.setNotCompleteExamine(true);
		_skuParamVo.getEntity().setOrgId(p.getOrgId());
		_skuParamVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		List<DeliverGoodsApplicationGoodsSkuVo> skuVoList = goodsSkuService.qryList(_skuParamVo);
		//map<goodsId,DeliverGoodsApplicationGoodsSkuVo>
		Map<String,List<DeliverGoodsApplicationGoodsSkuVo>> skuListMap = new HashMap<String,List<DeliverGoodsApplicationGoodsSkuVo>>();
		if (skuVoList != null) for(DeliverGoodsApplicationGoodsSkuVo gsVo:skuVoList){
			List<DeliverGoodsApplicationGoodsSkuVo> gsVoList = null;
			if(skuListMap.containsKey(gsVo.getEntity().getApplyGoodsId())){
				gsVoList = skuListMap.get(gsVo.getEntity().getApplyGoodsId());
			}else{
				gsVoList = new ArrayList<DeliverGoodsApplicationGoodsSkuVo>();
			}
			gsVoList.add(gsVo);
			skuListMap.put(gsVo.getEntity().getApplyGoodsId(), gsVoList);
		}
		
		//根据货品查询没完成审批的商品
		List<String> goodsIds = null;
		if (skuVoList != null) {
			goodsIds = skuVoList.stream().map(t->t.getEntity().getApplyGoodsId()).collect(Collectors.toList());
		}
		DeliverGoodsApplicationGoodVo _goodsVo = new DeliverGoodsApplicationGoodVo(new DeliverGoodsApplicationGood());
		_goodsVo.setGoodsIds(goodsIds);
		_goodsVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		List<DeliverGoodsApplicationGood> goods_entitys = applicationGoodDao.selectByExample(_goodsVo.getExample());
		//map<applicationId,DeliverGoodsApplicationGoodVo>
		Map<String,List<String>> goodsListMap = new HashMap<String,List<String>>();
		if (goods_entitys != null) for(DeliverGoodsApplicationGood good:goods_entitys){
			List<String> goodList = null;
			if(goodsListMap.containsKey(good.getApplicationId())){
				goodList = goodsListMap.get(good.getApplicationId());
			}else{ 
				goodList = new ArrayList<String>();
			}
			goodList.add(good.getId());
			goodsListMap.put(good.getApplicationId(),goodList);
		}
		
		//查询未完成核放申报的申请单列表
		List<String> appIds = goods_entitys.stream().map(t->t.getApplicationId()).collect(Collectors.toList());
		DeliverGoodsApplicationVo _paramVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
		_paramVo.setAppIdList(appIds);
		_paramVo.getEntity().setAuditStep(Constant.APPLICATION_STATUS_HAS_AUDIT);
		_paramVo.getEntity().setOrgId(p.getOrgId());
		_paramVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		//若是保税仓，就是分送集报的申请单
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
			_paramVo.getEntity().setApplyFrom(Constant.APPLY_FROM_DELIVER_GOODS);
		}
		//若是普通仓，则是分类监管的申请单
		else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			_paramVo.getEntity().setApplyFrom(Constant.APPLY_FROM_CLASS_MASSAGE);
		}
		List<DeliverGoodsApplication> entityList = applicationDao.selectByExample(_paramVo.getExample());
		
		List<DeliverGoodsApplicationVo> applicationVoList = new ArrayList<DeliverGoodsApplicationVo>();
		if (entityList != null) for(DeliverGoodsApplication entity:entityList){
			DeliverGoodsApplicationVo recordVo = new DeliverGoodsApplicationVo(entity);
			recordVo.setGoodsSkuVoList(new ArrayList<DeliverGoodsApplicationGoodsSkuVo>());
			List<String> goodList = goodsListMap.get(entity.getId());
			for(String goodId:goodList){
				List<DeliverGoodsApplicationGoodsSkuVo> gsVoList = skuListMap.get(goodId);
				if (gsVoList != null) {
					recordVo.getGoodsSkuVoList().addAll(gsVoList);
				}
			}
			applicationVoList.add(recordVo);
		}
		
		return applicationVoList;
	}
	
	/**
	 * 从发货单生成申请单
	 * @param formVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void createApplicationFromSend(SendDeliveryVo deliveryVo,Principal p) throws Exception{
		DeliverGoodsApplicationVo applicationVo = parseDeliveryVo(deliveryVo,p);
		applicationVo = add(applicationVo,p);
		deliveryVo.getSendDelivery().setGatejobNo(applicationVo.getEntity().getApplicationNo());
		deliveryDao.updateByPrimaryKeySelective(deliveryVo.getSendDelivery());
		
	}
	
	/**
	 * 从收货单生成申请单
	 * @param formVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void createApplicationFromAsn(RecAsnVO asnVo,Principal p) throws Exception{
		DeliverGoodsApplicationVo applicationVo = parseAsnVo(asnVo,p);
		applicationVo = add(applicationVo,p);
		asnVo.getAsn().setGatejobNo(applicationVo.getEntity().getApplicationNo());
		asnDao.updateByPrimaryKeySelective(asnVo.getAsn());
	}
	
	/**
	 * 新增
	 * @param formVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationVo add(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception{
		if(applicationVo == null) throw new BizException("data_is_null");
		String tradeCode = paramService.getKey(CacheName.TRAED_CODE);
		
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
//		MetaMerchant merchant = merchantService.get(p.getOrgId());
//		String hgMerchantNo = merchant.getHgMerchantNo();
		
		Date today = new Date();
		String year = DateFormatUtils.format(today, "yyyy");
		String month = DateFormatUtils.format(today, "MM");
		String i_flag = Constant.IN_FLAG.equals(applicationVo.getEntity().getiEFlag())?"1":"2";
		String no = context.getStrategy4No(p.getOrgId(), LoginUtil.getWareHouseId()).getApplicationNo(p.getOrgId());
		String id = IdUtil.getUUID();
		
		if (tradeCode != null) {
			//若是保税仓，就是分送集报的申请单
			if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
				String applyType = paramService.getKey(CacheName.APPLICATION_TYPE_01);
				String bizType = paramService.getKey(CacheName.APPLICATION_BIZ_TYPE);
				String bizMode = paramService.getKey(CacheName.APPLICATION_BIZ_MODE);
				//编号规则：U+业务类型代码（临时进出区 TMP）+企业10位海关编码+4位年份+2位月份+进出标志（1:进／2:出）+7位流水号
				String applicationNo = "U"+Constant.BIZTYPE_CODE_APPLICATION + tradeCode + year + month + i_flag + no;
				applicationVo.getEntity().setApplicationNo(applicationNo);
				applicationVo.getEntity().setApplyFrom(Constant.APPLY_FROM_DELIVER_GOODS);
				applicationVo.getEntity().setDeclareType(applyType);
				applicationVo.getEntity().setBizType(bizType);
				applicationVo.getEntity().setBizMode(bizMode);
			}
			//若是普通仓，则是分类监管的申请单
			else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
				String applyType = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_TYPE);
				String bizType = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_BIZ_TYPE);
				String bizMode = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_BIZ_MODE);
				//编号规则：U+PW（临时进出区 TMP）+企业10位海关编码+4位年份+2位月份+进出标志（1:进／2:出）+7位流水号
				String applicationNo = "U"+"PW"+Constant.BIZTYPE_CODE_APPLICATION + tradeCode + year + month + i_flag + no;
				applicationVo.getEntity().setApplicationNo(applicationNo);
				applicationVo.getEntity().setApplyFrom(Constant.APPLY_FROM_CLASS_MASSAGE);
				applicationVo.getEntity().setDeclareType(applyType);
				applicationVo.getEntity().setBizType(bizType);
				applicationVo.getEntity().setBizMode(bizMode);
			}
			//检查申请表编号是否已存在
			applicationNoIsExist(applicationVo.getEntity().getApplicationNo());
		}
		applicationVo.getEntity().setId(id); 
		
		//新增申报商品
		goodService.addApplicationGoods(applicationVo,p);
		
		applicationVo.getEntity().setApplyPerson(p.getUserName());
		applicationVo.getEntity().setApplyTime(today);
		applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_OPEN);
		applicationVo.getEntity().setAuditStep(Constant.APPLICATION_STATUS_NO_SEND);
		applicationVo.getEntity().setOrgId(p.getOrgId());
		applicationVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		applicationVo.getEntity().setCreatePerson(p.getUserName());
		applicationVo.getEntity().setUpdatePerson(p.getUserName());
		applicationDao.insertSelective(applicationVo.getEntity());
		
		return applicationVo;
	}

	
	/**
	 * 检查申报单编号是否存在
	 * @param formNo
	 */
	public void applicationNoIsExist(String applicationNo){
		DeliverGoodsApplicationVo applicatonVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
		applicatonVo.getEntity().setApplicationNo(applicationNo);
		List<DeliverGoodsApplication> list = applicationDao.selectByExample(applicatonVo.getExample());
		if(list != null && list.size() > 0)
			throw new BizException("application_no_is_exist");
	}
	
	/**
	 * 新增生效
	 * @param formVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationVo saveAndEnable(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception{
		applicationVo = addOrUpdate(applicationVo,p);
		List<String> idList = new ArrayList<String>();
		idList.add(applicationVo.getEntity().getId());
		enable(idList,p);
		
		applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_ACTIVE);
		return applicationVo;
	}
	
	/**
	 * 修改
	 * @param formVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationVo update(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception{
		if(applicationVo == null) throw new BizException("data_is_null");
		Date today = new Date();
		
		applicationVo.getEntity().setUpdatePerson(p.getUserId());
		applicationVo.getEntity().setUpdateTime(today);
		applicationDao.updateByPrimaryKeySelective(applicationVo.getEntity());
		//更新商品
		goodService.update(applicationVo, p);
		
		return applicationVo;
	}
	
	/**
	 * 新增或更新
	 * @param applicationVo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationVo addOrUpdate(DeliverGoodsApplicationVo applicationVo,Principal p)throws Exception{
		if(applicationVo == null) throw new BizException("data_is_null");
		
		DeliverGoodsApplicationVo vo = null;
		if(StringUtil.isNotBlank(applicationVo.getEntity().getId())){
			vo = update(applicationVo,p);
		}else{
			vo = add(applicationVo,p);
		}
		return vo;
	}
	
	/**
	 * 查看
	 * @param formVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public DeliverGoodsApplicationVo view(String applicationId) throws DaoException, ServiceException{
		//查询申请单
		DeliverGoodsApplication application = applicationDao.selectByPrimaryKey(applicationId);
		if(application == null) return null;
		DeliverGoodsApplicationVo applicationVo = chg(application);
		
		return applicationVo;
	}
	
	/**
	 * 生效
	 * @param formVo
	 * @return
	 */
	public void enable(List<String> listId,Principal p)throws Exception{
		for (String applicationId : listId) {
			//检查状态是否打开
			queryAndCheckStatus(applicationId,Constant.APPLICATION_STATUS_OPEN,"application_stauts_is_not_open");
			
			DeliverGoodsApplication entity = new DeliverGoodsApplication();
			entity.setId(applicationId);
			entity.setStatus(Constant.APPLICATION_STATUS_ACTIVE);
			entity.setUpdatePerson(p.getUserId());
			applicationDao.updateByPrimaryKeySelective(entity);
		}
	}
	
	/**
	 * 失效
	 * @param formVo
	 * @return
	 */
	public void disable(List<String> listId,Principal p)throws Exception{
		for (String applicationId : listId) {
			//检查状态是否生效
			queryAndCheckStatus(applicationId,Constant.APPLICATION_STATUS_ACTIVE,"application_stauts_is_not_active");
			//检查是否放行
			checkAuditStep(applicationId,Constant.APPLICATION_STATUS_PASS,"application_is_auditing");
			//检查是否转查验
			checkAuditStep(applicationId,Constant.APPLICATION_STATUS_TO_CHECK,"application_is_auditing");
			//检查是否审批通过
			checkAuditStep(applicationId,Constant.APPLICATION_STATUS_HAS_AUDIT,"application_has_pass_audit");
			
			DeliverGoodsApplication entity = new DeliverGoodsApplication();
			entity.setId(applicationId);
			entity.setStatus(Constant.APPLICATION_STATUS_OPEN);
			entity.setUpdatePerson(p.getUserId());
			applicationDao.updateByPrimaryKeySelective(entity);
		}
	}
	
	
	/**
	 * 取消
	 * @param formVo
	 * @return
	 */
	public void cancel(List<String> listId,Principal p)throws Exception{
		for (String applicationId : listId) {
			//检查状态是否打开
			queryAndCheckStatus(applicationId,Constant.APPLICATION_STATUS_OPEN,"application_stauts_is_not_open");
			
			DeliverGoodsApplication entity = applicationDao.selectByPrimaryKey(applicationId);
			entity.setId(applicationId);
			entity.setStatus(Constant.APPLICATION_STATUS_CANCAL);
			entity.setUpdatePerson(p.getUserId());
			applicationDao.updateByPrimaryKeySelective(entity);
			
			//取消时，删除原收货单的申请单号
			if(StringUtil.isNotBlank(entity.getAsnId())){
				RecAsn asnEntity = asnDao.selectByPrimaryKey(entity.getAsnId());
				asnEntity.setGatejobNo("");
				asnDao.updateByPrimaryKey(asnEntity);
			}
			//取消时，删除原发货单的申请单号
			if(StringUtil.isNotBlank(entity.getDeliveryId())){
				SendDelivery sendEntity = deliveryDao.selectByPrimaryKey(entity.getDeliveryId());
				sendEntity.setGatejobNo("");
				deliveryDao.updateByPrimaryKey(sendEntity);
			}
		}
		
	}
	
	/**
	 * 发送申请单
	 * @param applicationId
	 * @param p
	 * @throws Exception
	 */
	public void sendApplication(String applicationId,Principal p)throws Exception{
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		//若是保税仓，就是分送集报的申请单
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
			sendDeliverGoodsApplication(applicationId, p);
		}
		//若是普通仓，则是分类监管的申请单
		else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			sendClassManageApplication(applicationId, p);
		}
	}
	
	/**
	 * 发送分类监管申请单
	 * @param applicationId
	 * @param p
	 * @throws Exception
	 */
	public void sendClassManageApplication(String applicationId,Principal p)throws Exception{
		
		String AREA_CODE = paramService.getKey(CacheName.AREA_CODE);
		String CUSTOM_CODE = paramService.getKey(CacheName.CUSTOM_CODE);
		String messageType = paramService.getKey(CacheName.MESSAGE_TYPE_DECLARE);
		String receiver_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		String applyType = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_TYPE);
		String bizType = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_BIZ_TYPE);
		String bizMode = paramService.getKey(CacheName.CLASSMESSAGE_APPLY_BIZ_MODE);
		String direct_flag = paramService.getKey(CacheName.DIRECT_FLAG);
		String eciEmsNo = paramService.getKey(CacheName.EMS_NO);
		String eciEmsProperty = paramService.getKey(CacheName.CLASSMESSAGE_ECI_EMS_PROPERTY);
		String eciEmsLevel = paramService.getKey(CacheName.CLASSMESSAGE_ECI_EMS_LEVEL);
		String CHCL_TYPE = paramService.getKey(CacheName.CLASSMESSAGE_CHCL_TYPE);
		String COLLECT_TYPE = paramService.getKey(CacheName.CLASSMESSAGE_COLLECT_TYPE);
		String current_step_id = paramService.getKey(CacheName.CLASSMESSAGE_APP_STEP_ID);
		String eciModifyMark = paramService.getKey(CacheName.ECI_MODIFY_MARK);
		
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		//查询申请单
		DeliverGoodsApplicationVo applicationVo = view(applicationId);
		String messageId = IdUtil.getUUID();
		//设置messageHead
		DeliverGoodsXMLMessageUtil messageUtil = new DeliverGoodsXMLMessageUtil();
		messageUtil.getMessageHeadMap().put("MESSAGE_ID", messageId);
		messageUtil.getMessageHeadMap().put("MESSAGE_TYPE", messageType);
		messageUtil.getMessageHeadMap().put("ORDER_NO", applicationVo.getEntity().getApplicationNo());
		messageUtil.getMessageHeadMap().put("FUNCTION_CODE", "3");
		messageUtil.getMessageHeadMap().put("MESSAGE_DATE", dateTime);
		messageUtil.getMessageHeadMap().put("SENDER_ID", Constant.SYSTEM_TYPE_WMS);
		messageUtil.getMessageHeadMap().put("RECEIVER_ID", receiver_id);
		
		//设置gateJobHeadMap
		messageUtil.getGateJobHeadMap().put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
		messageUtil.getGateJobHeadMap().put("GATEJOB_TYPE", applyType);
		messageUtil.getGateJobHeadMap().put("BIZ_TYPE", bizType);
		messageUtil.getGateJobHeadMap().put("BIZ_MODE", bizMode);
		messageUtil.getGateJobHeadMap().put("I_E_FLAG", applicationVo.getEntity().getiEFlag());
		messageUtil.getGateJobHeadMap().put("DIRECT_FLAG", direct_flag);
		messageUtil.getGateJobHeadMap().put("ECI_EMS_NO", eciEmsNo);
		messageUtil.getGateJobHeadMap().put("ECI_EMS_PROPERTY", eciEmsProperty);
		messageUtil.getGateJobHeadMap().put("ECI_EMS_LEVEL", eciEmsLevel);
		messageUtil.getGateJobHeadMap().put("TRADE_CODE", applicationVo.getEntity().getTradeCode());
		messageUtil.getGateJobHeadMap().put("TRADE_NAME", applicationVo.getEntity().getTradeName());
		messageUtil.getGateJobHeadMap().put("OUT_CODE", applicationVo.getEntity().getReceiveDeliverOrgCode());
		messageUtil.getGateJobHeadMap().put("OUT_NAME", applicationVo.getEntity().getReceiveDeliverOrgName());
		messageUtil.getGateJobHeadMap().put("REL_GATEJOB_NO", applicationVo.getEntity().getRelApplicationNo());
		messageUtil.getGateJobHeadMap().put("OWNER_CODE", applicationVo.getEntity().getOutEntrpriseCode());
		messageUtil.getGateJobHeadMap().put("OWNER_NAME", applicationVo.getEntity().getOutEntrpriseName());
		messageUtil.getGateJobHeadMap().put("GUARANTEE_TYPE", applicationVo.getEntity().getGuaranteeType());
		messageUtil.getGateJobHeadMap().put("GUARANTEE_ID", applicationVo.getEntity().getGuaranteeId());
		messageUtil.getGateJobHeadMap().put("GUARANTEE_TOTAL", ApplicationStringUtil.parseNum(applicationVo.getEntity().getGuaranteeTotal()));
		messageUtil.getGateJobHeadMap().put("USED_TOTAL", ApplicationStringUtil.parseNum(applicationVo.getEntity().getRequireGuaranteeTotal()));
		messageUtil.getGateJobHeadMap().put("LINK_MAN", applicationVo.getEntity().getLinkMan());
		messageUtil.getGateJobHeadMap().put("LINK_PHONE", applicationVo.getEntity().getLinkPhone());
		messageUtil.getGateJobHeadMap().put("PACK_NO", ApplicationStringUtil.parseNum(applicationVo.getEntity().getPackNo()));
		messageUtil.getGateJobHeadMap().put("GROSS_WT", ApplicationStringUtil.parseNum(applicationVo.getEntity().getGrossWt()));
		messageUtil.getGateJobHeadMap().put("NET_WT", ApplicationStringUtil.parseNum(applicationVo.getEntity().getNetWt()));
		messageUtil.getGateJobHeadMap().put("WRAP_TYPE", applicationVo.getEntity().getWrapType());
		messageUtil.getGateJobHeadMap().put("REL_TYPE", applicationVo.getEntity().getRelType());
		messageUtil.getGateJobHeadMap().put("REL_DAYS", ApplicationStringUtil.parseNum(applicationVo.getEntity().getRelDays()));
		messageUtil.getGateJobHeadMap().put("CHCL_TYPE", CHCL_TYPE);
		messageUtil.getGateJobHeadMap().put("COLLECT_TYPE", COLLECT_TYPE);
		messageUtil.getGateJobHeadMap().put("REMARK", applicationVo.getEntity().getRemark());
		
		//设置gateJobBodyList
		for(int i=0;i<applicationVo.getApplicationGoodVoList().size();i++){
			DeliverGoodsApplicationGoodVo goodVo = applicationVo.getApplicationGoodVoList().get(i);
			
			Map<String,String> gateJobBodyMap = new HashMap<String,String>();
			gateJobBodyMap.put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
			gateJobBodyMap.put("TRADE_CODE", applicationVo.getEntity().getTradeCode());
			gateJobBodyMap.put("TRADE_NAME", applicationVo.getEntity().getTradeName());
			gateJobBodyMap.put("ECI_EMS_NO", eciEmsNo);
			gateJobBodyMap.put("ECI_GOODS_FLAG", "3");
			gateJobBodyMap.put("SEQ_NO", (i+1)+"");
			gateJobBodyMap.put("G_NO", goodVo.getEntity().getGNo());
			gateJobBodyMap.put("EXG_VERSION", "0");
			gateJobBodyMap.put("CODE_T_S", goodVo.getEntity().getHsCode());
			gateJobBodyMap.put("G_NAME", goodVo.getEntity().getGoodsName());
			gateJobBodyMap.put("G_MODEL", goodVo.getEntity().getGModel());
			gateJobBodyMap.put("UNIT", goodVo.getEntity().getUnit());
			gateJobBodyMap.put("DEC_QTY", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecQty()));
			gateJobBodyMap.put("DEC_PRICE", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecPrice()));
			gateJobBodyMap.put("DEC_TOTAL", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecTotal()));
			gateJobBodyMap.put("CURR", goodVo.getEntity().getCurr());
			gateJobBodyMap.put("QTY_1", ApplicationStringUtil.parseNum(goodVo.getEntity().getQty1()));
			gateJobBodyMap.put("UNIT_1",goodVo.getEntity().getUnit1());
			gateJobBodyMap.put("QTY_2", ApplicationStringUtil.parseNum(goodVo.getEntity().getQty2()));
			gateJobBodyMap.put("UNIT_2", goodVo.getEntity().getUnit2());
			gateJobBodyMap.put("ECI_MODIFY_MARK", eciModifyMark);
			gateJobBodyMap.put("ORIGIN_COUNTRY", goodVo.getEntity().getOriginCountry());
			gateJobBodyMap.put("DUTY_MODE", goodVo.getEntity().getDutyMode());
			gateJobBodyMap.put("USE_TO", goodVo.getEntity().getUseTo());
			gateJobBodyMap.put("REMARK", goodVo.getEntity().getRemark());
			messageUtil.getGateJobBodyList().add(gateJobBodyMap);
			
		}
		
		  	//查询用户
//		SysUser creater = userService.get(applicationVo.getEntity().getCreatePerson());
//		SysUser declarer = userService.get(applicationVo.getEntity().getApplyPerson());
			//stateGateJobMap
			messageUtil.getStateGateJobMap().put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
			messageUtil.getStateGateJobMap().put("AREA_CODE", AREA_CODE);
			messageUtil.getStateGateJobMap().put("CUSTOMS_CODE", CUSTOM_CODE);
			messageUtil.getStateGateJobMap().put("TRADE_CODE", applicationVo.getEntity().getTradeCode());
			messageUtil.getStateGateJobMap().put("TRADE_NAME", applicationVo.getEntity().getTradeName());
			messageUtil.getStateGateJobMap().put("INPUT_CODE", applicationVo.getEntity().getInputOrgCode());
			messageUtil.getStateGateJobMap().put("INPUT_NAME", applicationVo.getEntity().getInputOrgName());
			messageUtil.getStateGateJobMap().put("DECLARE_CODE", applicationVo.getEntity().getApplyOrgCode());
			messageUtil.getStateGateJobMap().put("DECLARE_NAME", applicationVo.getEntity().getApplyOrgName());
			messageUtil.getStateGateJobMap().put("STEP_ID", current_step_id);
			messageUtil.getStateGateJobMap().put("CREATE_PERSON", applicationVo.getEntity().getCreatePerson());
			messageUtil.getStateGateJobMap().put("CREATE_DATE", ApplicationStringUtil.parseDate(applicationVo.getEntity().getCreateTime()));
			messageUtil.getStateGateJobMap().put("DECLARE_PERSON", applicationVo.getEntity().getApplyPerson());
			messageUtil.getStateGateJobMap().put("DECLARE_DATE", ApplicationStringUtil.parseDate(applicationVo.getEntity().getApplyTime()));
			
			String xmlStr = messageUtil.createApplicationXMLMessage(applicationVo.getEntity().getApplyFrom());
			if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messageId+"_申报单数据报文：["+xmlStr+"]");	
			
//			String sendChnalName = paramService.getKey(CacheName.MSMQ_DECLARE_CHNALNAME);
//			String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);

			sendXmlMessage(xmlStr,applicationVo.getEntity().getId(),messageId,p);
	}
	
	/**
	 * 发送分送集报申请单
	 * @param applicationVo
	 * @throws Exception
	 */
	public void sendDeliverGoodsApplication(String applicationId,Principal p)throws Exception{
		
		String AREA_CODE = paramService.getKey(CacheName.AREA_CODE);
		String CUSTOM_CODE = paramService.getKey(CacheName.CUSTOM_CODE);
		String messageType = paramService.getKey(CacheName.MESSAGE_TYPE_DECLARE);
		String applyType = paramService.getKey(CacheName.APPLICATION_TYPE_01);
		String bizType = paramService.getKey(CacheName.APPLICATION_BIZ_TYPE);
		String bizMode = paramService.getKey(CacheName.APPLICATION_BIZ_MODE);
		String current_step_id = paramService.getKey(CacheName.DECLARE_CURRENT_STEP_ID);
		String receiver_id = paramService.getKey(CacheName.DECLARE_RECEIVER_ID);
		String direct_flag = paramService.getKey(CacheName.DIRECT_FLAG);
		String eciModifyMark = paramService.getKey(CacheName.ECI_MODIFY_MARK);
		
		//查询申请单
		DeliverGoodsApplicationVo applicationVo = view(applicationId);
	
		DeliverGoodsApplicationFormVo formVo = applicationFormService.view(applicationVo.getEntity().getApplicationFormId());
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		//设置messageHead
		String messageId = IdUtil.getUUID();
		
		DeliverGoodsXMLMessageUtil messageUtil = new DeliverGoodsXMLMessageUtil();
		messageUtil.getMessageHeadMap().put("MESSAGE_ID", messageId);
		messageUtil.getMessageHeadMap().put("MESSAGE_TYPE", messageType);
		messageUtil.getMessageHeadMap().put("ORDER_NO", applicationVo.getEntity().getApplicationNo());
		messageUtil.getMessageHeadMap().put("FUNCTION_CODE", "3");
		messageUtil.getMessageHeadMap().put("MESSAGE_DATE", dateTime);
		messageUtil.getMessageHeadMap().put("SENDER_ID", Constant.SYSTEM_TYPE_WMS);
		messageUtil.getMessageHeadMap().put("RECEIVER_ID", receiver_id);
		
		//设置gateJobHeadMap
		messageUtil.getGateJobHeadMap().put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
		messageUtil.getGateJobHeadMap().put("GATEJOB_TYPE", applyType);
		messageUtil.getGateJobHeadMap().put("GATEPASS_NO", formVo.getEntity().getApplicationFormNo());
		messageUtil.getGateJobHeadMap().put("GP_I_E_FLAG", formVo.getEntity().getGpIEFlag());
		messageUtil.getGateJobHeadMap().put("BIZ_TYPE", bizType);
		messageUtil.getGateJobHeadMap().put("BIZ_MODE", bizMode);
		messageUtil.getGateJobHeadMap().put("I_E_FLAG", applicationVo.getEntity().getiEFlag());
		messageUtil.getGateJobHeadMap().put("DIRECT_FLAG", direct_flag);
		messageUtil.getGateJobHeadMap().put("ECI_EMS_NO", formVo.getEntity().getEciEmsNo());
		messageUtil.getGateJobHeadMap().put("ECI_EMS_PROPERTY", formVo.getEntity().getEciEmsProperty());
		messageUtil.getGateJobHeadMap().put("ECI_EMS_LEVEL", formVo.getEntity().getEciEmsLevel());
		messageUtil.getGateJobHeadMap().put("TRADE_CODE", formVo.getEntity().getTradeCode());
		messageUtil.getGateJobHeadMap().put("TRADE_NAME", formVo.getEntity().getTradeName());
		messageUtil.getGateJobHeadMap().put("OUT_CODE", formVo.getEntity().getReceiveDeliverOrgCode());
		messageUtil.getGateJobHeadMap().put("OUT_NAME", formVo.getEntity().getReceiveDeliverOrgName());
		messageUtil.getGateJobHeadMap().put("REL_GATEJOB_NO", applicationVo.getEntity().getRelApplicationNo());
		messageUtil.getGateJobHeadMap().put("OWNER_CODE", formVo.getEntity().getOutEntrpriseCode());
		messageUtil.getGateJobHeadMap().put("OWNER_NAME", formVo.getEntity().getOutEntrpriseName());
		messageUtil.getGateJobHeadMap().put("GUARANTEE_TYPE", formVo.getEntity().getGuaranteeType());
		messageUtil.getGateJobHeadMap().put("GUARANTEE_ID", formVo.getEntity().getGuaranteeId());
		messageUtil.getGateJobHeadMap().put("GUARANTEE_TOTAL", ApplicationStringUtil.parseNum(formVo.getEntity().getGuaranteeTotal()));
		messageUtil.getGateJobHeadMap().put("USED_TOTAL", ApplicationStringUtil.parseNum(applicationVo.getEntity().getRequireGuaranteeTotal()));
		messageUtil.getGateJobHeadMap().put("LINK_MAN", applicationVo.getEntity().getLinkMan());
		messageUtil.getGateJobHeadMap().put("LINK_PHONE", applicationVo.getEntity().getLinkPhone());
		messageUtil.getGateJobHeadMap().put("PACK_NO", ApplicationStringUtil.parseNum(applicationVo.getEntity().getPackNo()));
		messageUtil.getGateJobHeadMap().put("GROSS_WT", ApplicationStringUtil.parseNum(applicationVo.getEntity().getGrossWt()));
		messageUtil.getGateJobHeadMap().put("NET_WT", ApplicationStringUtil.parseNum(applicationVo.getEntity().getNetWt()));
		messageUtil.getGateJobHeadMap().put("WRAP_TYPE", applicationVo.getEntity().getWrapType());
		messageUtil.getGateJobHeadMap().put("REL_TYPE", applicationVo.getEntity().getRelType());
		messageUtil.getGateJobHeadMap().put("REL_DAYS", ApplicationStringUtil.parseNum(applicationVo.getEntity().getRelDays()));
		messageUtil.getGateJobHeadMap().put("REMARK", applicationVo.getEntity().getRemark());
		
		int j = 0;
		//设置gateJobBodyList
		for(int i=0;i<applicationVo.getApplicationGoodVoList().size();i++){
			DeliverGoodsApplicationGoodVo goodVo = applicationVo.getApplicationGoodVoList().get(i);
			Map<String,String> gateJobBodyMap = new HashMap<String,String>();
			gateJobBodyMap.put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
			gateJobBodyMap.put("TRADE_CODE", formVo.getEntity().getTradeCode());
			gateJobBodyMap.put("TRADE_NAME", formVo.getEntity().getTradeName());
			gateJobBodyMap.put("ECI_EMS_NO", formVo.getEntity().getEciEmsNo());
			gateJobBodyMap.put("ECI_GOODS_FLAG", "3");
			gateJobBodyMap.put("SEQ_NO", (i+1)+"");
			gateJobBodyMap.put("G_NO", goodVo.getEntity().getGNo());
			gateJobBodyMap.put("EXG_VERSION", "0");
			gateJobBodyMap.put("CODE_T_S", goodVo.getEntity().getHsCode());
			gateJobBodyMap.put("G_NAME", goodVo.getEntity().getGoodsName());
			gateJobBodyMap.put("G_MODEL", goodVo.getEntity().getGModel());
			gateJobBodyMap.put("UNIT", goodVo.getEntity().getUnit());
			gateJobBodyMap.put("DEC_QTY", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecQty()));
			gateJobBodyMap.put("DEC_PRICE", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecPrice()));
			gateJobBodyMap.put("DEC_TOTAL", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecTotal()));
			gateJobBodyMap.put("CURR", goodVo.getEntity().getCurr());
			gateJobBodyMap.put("QTY_1", ApplicationStringUtil.parseNum(goodVo.getEntity().getQty1()));
			gateJobBodyMap.put("UNIT_1", goodVo.getEntity().getUnit1());
			gateJobBodyMap.put("QTY_2", ApplicationStringUtil.parseNum(goodVo.getEntity().getQty2()));
			gateJobBodyMap.put("UNIT_2", goodVo.getEntity().getUnit2());
			gateJobBodyMap.put("ECI_MODIFY_MARK", eciModifyMark);
			gateJobBodyMap.put("ORIGIN_COUNTRY", goodVo.getEntity().getOriginCountry());
			gateJobBodyMap.put("DUTY_MODE", goodVo.getEntity().getDutyMode());
			gateJobBodyMap.put("USE_TO", goodVo.getEntity().getUseTo());
			gateJobBodyMap.put("REMARK", goodVo.getEntity().getRemark());
			messageUtil.getGateJobBodyList().add(gateJobBodyMap);
			//设置gateJobItemList
			for(DeliverGoodsApplicationGoodsSkuVo goodSkuVo:goodVo.getApplicationGoodSkuVoList()){
				j++;
				Map<String,String> itemMap = new HashMap<String,String>();
				itemMap.put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
				itemMap.put("TRADE_CODE", formVo.getEntity().getTradeCode());
				itemMap.put("TRADE_NAME", formVo.getEntity().getTradeName());
				itemMap.put("ECI_EMS_NO", formVo.getEntity().getEciEmsNo());
				itemMap.put("ECI_GOODS_FLAG", "3");
				itemMap.put("SEQ_NO", j+"");
				itemMap.put("COP_G_NO", goodSkuVo.getEntity().getHgGoodsNo());
				itemMap.put("EXG_VERSION", goodVo.getEntity().getExgVersion()+"");
				itemMap.put("G_NO", goodVo.getEntity().getGNo());
				itemMap.put("CODE_T_S", goodVo.getEntity().getHsCode());
				itemMap.put("G_NAME", goodVo.getEntity().getGoodsName());
				itemMap.put("G_MODEL", goodVo.getEntity().getGModel());
				itemMap.put("UNIT", goodVo.getEntity().getUnit());
				itemMap.put("DEC_QTY", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecQty()));
				itemMap.put("DEC_PRICE", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecPrice()));
				itemMap.put("DEC_TOTAL", ApplicationStringUtil.parseNum(goodVo.getEntity().getDecTotal()));
				itemMap.put("CURR", goodVo.getEntity().getCurr());
				itemMap.put("UNIT_1",  goodVo.getEntity().getUnit1());
				itemMap.put("QTY_2",  ApplicationStringUtil.parseNum(goodVo.getEntity().getDecQty()));
				itemMap.put("UNIT_2", goodVo.getEntity().getUnit2());
				itemMap.put("ECI_MODIFY_MARK", goodVo.getEntity().getEciModifyMark());
				itemMap.put("ORIGIN_COUNTRY", goodVo.getEntity().getOriginCountry());
				itemMap.put("DUTY_MODE", goodVo.getEntity().getDutyMode());
				itemMap.put("USE_TO", goodVo.getEntity().getUseTo());
				messageUtil.getGateJobItemList().add(itemMap);
			}
		}
	  	//查询用户
//		SysUser creater = userService.get(applicationVo.getEntity().getCreatePerson());
//		SysUser declarer = userService.get(applicationVo.getEntity().getApplyPerson());
		//stateGateJobMap
		messageUtil.getStateGateJobMap().put("GATEJOB_NO", applicationVo.getEntity().getApplicationNo());
		messageUtil.getStateGateJobMap().put("AREA_CODE", AREA_CODE);
		messageUtil.getStateGateJobMap().put("CUSTOMS_CODE", CUSTOM_CODE);
		messageUtil.getStateGateJobMap().put("TRADE_CODE", formVo.getEntity().getTradeCode());
		messageUtil.getStateGateJobMap().put("TRADE_NAME", formVo.getEntity().getTradeName());
		messageUtil.getStateGateJobMap().put("INPUT_CODE", applicationVo.getEntity().getInputOrgCode());
		messageUtil.getStateGateJobMap().put("INPUT_NAME", applicationVo.getEntity().getInputOrgName());
		messageUtil.getStateGateJobMap().put("DECLARE_CODE", applicationVo.getEntity().getApplyOrgCode());
		messageUtil.getStateGateJobMap().put("DECLARE_NAME", applicationVo.getEntity().getApplyOrgName());
		messageUtil.getStateGateJobMap().put("STEP_ID", current_step_id);
		messageUtil.getStateGateJobMap().put("CREATE_PERSON", applicationVo.getEntity().getCreatePerson());
		messageUtil.getStateGateJobMap().put("CREATE_DATE", ApplicationStringUtil.parseDate(applicationVo.getEntity().getCreateTime()));
		messageUtil.getStateGateJobMap().put("DECLARE_PERSON", applicationVo.getEntity().getApplyPerson());
		messageUtil.getStateGateJobMap().put("DECLARE_DATE", ApplicationStringUtil.parseDate(applicationVo.getEntity().getApplyTime()));
		
		String xmlStr = messageUtil.createApplicationXMLMessage(applicationVo.getEntity().getApplyFrom());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messageId+"_申报单数据报文：["+xmlStr+"]");	
		
//		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
//		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);

		sendXmlMessage(xmlStr,applicationVo.getEntity().getId(),messageId,p);
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
	public void sendXmlMessage(String xmlStr,String applicationId,String messageId,Principal p)throws Exception{
		
		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);

		//传送入库数据到仓储企业联网监管系统
		if(log.isInfoEnabled()) log.info("Queue名称：["+sendChnalName+"]");
		boolean result = MSMQUtil.send(sendChnalName, label, xmlStr, messageId.getBytes());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messageId+"_申报单数据发送接口运行结果：["+result+"]");	
		
		//保存报文
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setMessageId(messageId);
		messageVo.getEntity().setFunctionType(Constant.FUNCTION_TYPE_APPLY);
		messageVo.getEntity().setOrderNo(applicationId);
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
			updateAuditStepById(applicationId,Constant.APPLICATION_STATUS_SEND_SUCCESS,p);
		}else{
			updateAuditStepById(applicationId,Constant.APPLICATION_STATUS_SEND_FAIL,p);
		}
	}
	
	/**
	 * 修改申请单状态
	 * @param applicationId
	 * @param status
	 * @throws Exception
	 */
	public void updateAuditStepById(String applicationId,String step,Principal p)throws Exception{
		DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
		applicationVo.getEntity().setId(applicationId);
		applicationVo.getEntity().setAuditStep(step);
		applicationVo.getEntity().setUpdatePerson(p.getUserId());
		applicationDao.updateByPrimaryKeySelective(applicationVo.getEntity());
	}
	
	/**
	 * 发货单转申报单
	 * @param deliveryVo
	 * @return
	 */
	public DeliverGoodsApplicationVo parseDeliveryVo(SendDeliveryVo deliveryVo,Principal p) throws Exception{
		//获取仓库
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		
		DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
		applicationVo.getEntity().setDeliveryId(deliveryVo.getSendDelivery().getDeliveryId());
		if(deliveryVo.getDeliveryDetailVoList() != null && !deliveryVo.getDeliveryDetailVoList().isEmpty()){
			
			List<DeliverGoodsApplicationGoodVo> goodsVoList = new ArrayList<DeliverGoodsApplicationGoodVo>();
			
			Map<String,List<DeliveryDetailVo>> hscodeMap = new HashMap<String,List<DeliveryDetailVo>>();
			
			//按商品编号存放货品列表
			for(DeliveryDetailVo detailVo:deliveryVo.getDeliveryDetailVoList()){
				List<DeliveryDetailVo> list = null;
				//保税仓按货品归类税号分类
				if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
					if(hscodeMap.containsKey(detailVo.getSku().getHsCode())){
						list = hscodeMap.get(detailVo.getSku().getHsCode());
					}else{
						list = new ArrayList<DeliveryDetailVo>();
					}
					list.add(detailVo);
					//如果货品有归类税号，按归类税号分类
					if (StringUtil.isNoneBlank(detailVo.getSku().getHsCode())) {
						hscodeMap.put(detailVo.getSku().getHsCode(), list);
					}
					//如果货品没有归类税号，按货品代码分类
					else{
						hscodeMap.put(detailVo.getSku().getSkuNo(),list);
					}
				}//if
				//普通仓按货品编号分类
				else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
					if(hscodeMap.containsKey(detailVo.getSku().getSkuNo())){
						list = hscodeMap.get(detailVo.getSku().getSkuNo());
					}else{
						list = new ArrayList<DeliveryDetailVo>();
					}
					list.add(detailVo);
					hscodeMap.put(detailVo.getSku().getSkuNo(),list);
				}
				
			}
			
			//转换成申请商品列表
			for(List<DeliveryDetailVo> list:hscodeMap.values()){
				DeliverGoodsApplicationGoodVo goodVo = new DeliverGoodsApplicationGoodVo(new DeliverGoodsApplicationGood());
				goodVo.setApplicationGoodSkuVoList(new ArrayList<DeliverGoodsApplicationGoodsSkuVo>());
				List<DeliverGoodsApplicationGoodsSkuVo> goodSkuVoList = new ArrayList<DeliverGoodsApplicationGoodsSkuVo>();
				double qty = 0d;
				for(int i=0;i<list.size();i++){
					DeliveryDetailVo detailVo = list.get(i);
					if(i == 0){
						goodVo.getEntity().setEciGoodsFlag(detailVo.getSku().getGoodsNature()+"");
						goodVo.getEntity().setGNo(detailVo.getSku().getgNo());
						goodVo.getEntity().setExgVersion(0);
						goodVo.getEntity().setHsCode(detailVo.getSku().getHsCode());
						goodVo.getEntity().setGoodsName(detailVo.getSku().getSkuName());
						goodVo.getEntity().setGModel(detailVo.getSku().getSpecModel());
						goodVo.getEntity().setUnit(detailVo.getSku().getMeasureUnit());
						goodVo.getEntity().setCurr(detailVo.getSku().getCurr());
						goodVo.getEntity().setDecPrice(new BigDecimal("0"));
						goodVo.getEntity().setDecTotal(new BigDecimal("0"));
						goodVo.getEntity().setEciModifyMark("3");
						goodVo.getEntity().setOriginCountry(detailVo.getSku().getOriginCountry());
						goodVo.getEntity().setUnit1(detailVo.getSku().getUnit1());
						goodVo.getEntity().setUnit2(detailVo.getSku().getUnit2());
						goodVo.getEntity().setQty1(new BigDecimal(0));
						goodVo.getEntity().setQty2(new BigDecimal(0));
					}
					DeliverGoodsApplicationGoodsSkuVo goodSkuVo = new DeliverGoodsApplicationGoodsSkuVo(new DeliverGoodsApplicationGoodsSku());
					goodSkuVo.getEntity().setSkuId(detailVo.getSku().getSkuId());
					goodSkuVo.getEntity().setHgGoodsNo(detailVo.getSku().getHgGoodsNo());
					goodSkuVo.getEntity().setDecQty(new BigDecimal(detailVo.getDeliveryDetail().getOrderQty()));
					goodSkuVo.getEntity().setDecPrice(new BigDecimal("0"));
					goodSkuVo.getEntity().setDecTotal(new BigDecimal("0"));
					goodSkuVoList.add(goodSkuVo);
					//统计数量
					qty = qty + detailVo.getDeliveryDetail().getOrderQty();
					goodVo.getEntity().setDecQty(new BigDecimal(qty));
				}
				goodVo.setApplicationGoodSkuVoList(goodSkuVoList);
				goodsVoList.add(goodVo);
			}
			applicationVo.setApplicationGoodVoList(goodsVoList);
		}//if
		
		return applicationVo;
	}
	
	/**
	 * asn单转申报单
	 * @param asnVo
	 * @return
	 */
	public DeliverGoodsApplicationVo parseAsnVo(RecAsnVO asnVo,Principal p) throws Exception{
		//获取仓库
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		
		DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
		applicationVo.getEntity().setAsnId(asnVo.getAsn().getAsnId());
		if(asnVo.getListAsnDetailVO() != null && !asnVo.getListAsnDetailVO().isEmpty()){
			
			List<DeliverGoodsApplicationGoodVo> goodsVoList = new ArrayList<DeliverGoodsApplicationGoodVo>();
			
			Map<String,List<RecAsnDetailVO>> hscodeMap = new HashMap<String,List<RecAsnDetailVO>>();
			
			//按商品编号存放货品列表
			for(RecAsnDetailVO detailVo:asnVo.getListAsnDetailVO()){
				List<RecAsnDetailVO> list = null;
				//保税仓按货品归类税号分类
				if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_BS){
					if(hscodeMap.containsKey(detailVo.getSku().getHsCode())){
						list = hscodeMap.get(detailVo.getSku().getHsCode());
					}else{
						list = new ArrayList<RecAsnDetailVO>();
					}
					list.add(detailVo);
					//如果货品有归类税号，按归类税号分类
					if (StringUtil.isNoneBlank(detailVo.getSku().getHsCode())) {
						hscodeMap.put(detailVo.getSku().getHsCode(), list);
					}
					//如果货品没有归类税号，按货品代码分类
					else{
						hscodeMap.put(detailVo.getSku().getSkuNo(),list);
					}
				}
				//普通仓按货品编号分类
				else if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
					if(hscodeMap.containsKey(detailVo.getSku().getSkuNo())){
						list = hscodeMap.get(detailVo.getSku().getSkuNo());
					}else{
						list = new ArrayList<RecAsnDetailVO>();
					}
					list.add(detailVo);
					hscodeMap.put(detailVo.getSku().getSkuNo(),list);
				}

			}
			
			//转换成申请商品列表
			for(List<RecAsnDetailVO> list:hscodeMap.values()){
				DeliverGoodsApplicationGoodVo goodVo = new DeliverGoodsApplicationGoodVo(new DeliverGoodsApplicationGood());
				goodVo.setApplicationGoodSkuVoList(new ArrayList<DeliverGoodsApplicationGoodsSkuVo>());
				List<DeliverGoodsApplicationGoodsSkuVo> goodSkuVoList = new ArrayList<DeliverGoodsApplicationGoodsSkuVo>();
				double qty = 0d;
				for(int i=0;i<list.size();i++){
					RecAsnDetailVO detailVo = list.get(i);
					if(i == 0){
						goodVo.getEntity().setEciGoodsFlag(detailVo.getSku().getGoodsNature()+"");
						goodVo.getEntity().setGNo(detailVo.getSku().getgNo());
						goodVo.getEntity().setExgVersion(0);
						goodVo.getEntity().setHsCode(detailVo.getSku().getHsCode());
						goodVo.getEntity().setGoodsName(detailVo.getSku().getSkuName());
						goodVo.getEntity().setGModel(detailVo.getSku().getSpecModel());
						goodVo.getEntity().setUnit(detailVo.getSku().getMeasureUnit());
						goodVo.getEntity().setDecPrice(new BigDecimal("0"));
						goodVo.getEntity().setDecTotal(new BigDecimal("0"));
						goodVo.getEntity().setCurr(detailVo.getSku().getCurr());
						goodVo.getEntity().setEciModifyMark("3");
						goodVo.getEntity().setOriginCountry(detailVo.getSku().getOriginCountry());
						goodVo.getEntity().setUnit1(detailVo.getSku().getUnit1());
						goodVo.getEntity().setUnit2(detailVo.getSku().getUnit2());
						goodVo.getEntity().setQty1(new BigDecimal(0));
						goodVo.getEntity().setQty2(new BigDecimal(0));
					}
					DeliverGoodsApplicationGoodsSkuVo goodSkuVo = new DeliverGoodsApplicationGoodsSkuVo(new DeliverGoodsApplicationGoodsSku());
					goodSkuVo.getEntity().setSkuId(detailVo.getSku().getSkuId());
					goodSkuVo.getEntity().setHgGoodsNo(detailVo.getSku().getHgGoodsNo());
					goodSkuVo.getEntity().setDecQty(new BigDecimal(detailVo.getAsnDetail().getOrderQty()));
					goodSkuVo.getEntity().setDecPrice(new BigDecimal("0"));
					goodSkuVo.getEntity().setDecTotal(new BigDecimal("0"));
					goodSkuVoList.add(goodSkuVo);
					//统计数量
					qty = qty + detailVo.getAsnDetail().getOrderQty();
					goodVo.getEntity().setDecQty(new BigDecimal(qty));
				}
				goodVo.setApplicationGoodSkuVoList(goodSkuVoList);
				goodsVoList.add(goodVo);
			}
			applicationVo.setApplicationGoodVoList(goodsVoList);
		}//if
		return applicationVo;
	}
	
	/**
	 * 查询并检查状态
	 * @param formIdR
	 * @param isStatus
	 * @param msg
	 */
	public void queryAndCheckStatus(String applicationId,Integer isStatus,String msg){
		DeliverGoodsApplication record = applicationDao.selectByPrimaryKey(applicationId);
		if(isStatus.intValue() != record.getStatus().intValue()) {
			throw new BizException(msg);
		}
	}
	
	/**
	 * 查询并检查环节
	 * @param formIdR
	 * @param isStatus
	 * @param msg
	 */
	public void checkAuditStep(String applicationId,String isStep,String msg){
		DeliverGoodsApplication record = applicationDao.selectByPrimaryKey(applicationId);
		if(isStep.equals(record.getAuditStep())) {
			throw new BizException(msg);
		}
	}
	
	
	/**
	 * 检查是否所有货品都可以核放
	 * @param applicationId
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void isAllPassExamine(String applicationId) throws Exception{
		DeliverGoodsApplicationVo applicationVo = view(applicationId);
		
		boolean pass = true;
		for(DeliverGoodsApplicationGoodVo goodVo:applicationVo.getApplicationGoodVoList()){
			if(!pass) break;
			for(DeliverGoodsApplicationGoodsSkuVo gsVo:goodVo.getApplicationGoodSkuVoList()){
				if(!gsVo.getEntity().getDecQty().equals(gsVo.getEntity().getPassAuditQty())){
					pass = false;
					break;
				}
			}
		}
		//若申请单通过核放审批，则原收发货单作生效操作
		if(pass){
			applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_ALL_EXMINE);
			applicationDao.updateByPrimaryKeySelective(applicationVo.getEntity());
			//若申请单全部核放，则原单需生效
//			if(StringUtil.isNotBlank(applicationVo.getEntity().getAsnId())){
//				RecAsnVO param_asnVO = new RecAsnVO();
//				List<String> ids = new ArrayList<String>();
//				ids.add(applicationVo.getEntity().getAsnId());
//				param_asnVO.setListAsnId(ids);
//				asnService.enable(param_asnVO);
//			}else if(StringUtil.isNotBlank(applicationVo.getEntity().getDeliveryId())){
//				
//				deliveryServece.enable(applicationVo.getEntity().getDeliveryId(), Constant.SYSTEM_CUST_ID);
//			}

		}
		
	}
	
	public static void main(String[] args) {
		try {
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
			Date today = sdf.parse("2017-01-01");
			String year = DateFormatUtils.format(today, "yyyy");
			String month = DateFormatUtils.format(today, "MM");
			System.out.println(year+"..."+month);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 检查是否所有货品都可以核放
	 * @param applicationId
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void changeStatus(String applicationId) throws Exception{
		DeliverGoodsApplicationVo applicationVo = view(applicationId);

		boolean partPass = true;
		boolean partPassing = false;
		boolean allPass = true;
		boolean allPassing = true;
		for(DeliverGoodsApplicationGoodVo goodVo:applicationVo.getApplicationGoodVoList()){
			for(DeliverGoodsApplicationGoodsSkuVo gsVo:goodVo.getApplicationGoodSkuVoList()){
				if (!gsVo.getEntity().getDecQty().equals(gsVo.getEntity().getPassAuditQty())){
					allPass = false;
				}
				if (gsVo.getEntity().getRemainQty().doubleValue() != 0.0) {
					allPassing = false;
				}
				if (!gsVo.getEntity().getAuditQty().equals(gsVo.getEntity().getPassAuditQty())){
					partPass = false;
				}
				if (gsVo.getEntity().getAuditQty().doubleValue() != 0d) {
					partPassing = true;
				}
			}
		}
		//若申请单通过核放审批，则原收发货单作生效操作
		if(allPass){
			applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_ALL_EXMINE);
			//若申请单全部核放，则原单需生效
		} else if (allPassing) {
			applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_ALL_EXMINEING);
		} else if (partPass) {
			applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_PART_EXMINE);
		} else if (partPassing) {
			applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_PART_EXMINEING);
		} else {
			applicationVo.getEntity().setStatus(Constant.APPLICATION_STATUS_ACTIVE);
		}
		
		applicationDao.updateByPrimaryKeySelective(applicationVo.getEntity());
	}
}
