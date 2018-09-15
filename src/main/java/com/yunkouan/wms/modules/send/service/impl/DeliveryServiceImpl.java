package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.excel.impt.ExcelExport;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.saas.modules.sys.service.IWarehouseService;
import com.yunkouan.saas.modules.sys.vo.MetaWarehouseVO;
import com.yunkouan.util.MD5Util;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.common.util.MSMQUtil;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodsSkuVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.intf.vo.Head;
import com.yunkouan.wms.modules.intf.vo.MSMQ;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvShiftDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.meta.entity.MetaMaterial;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialLog;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMaterialService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuMaterialBomService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.service.IWarehouseSettingService;
import com.yunkouan.wms.modules.meta.vo.MaterialBomVo;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;
import com.yunkouan.wms.modules.meta.vo.MetaWarehouseSettingVo;
import com.yunkouan.wms.modules.meta.vo.SkuMaterialBomVo;
import com.yunkouan.wms.modules.meta.vo.SkuVo;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.park.entity.ParkOrgBusiStas;
import com.yunkouan.wms.modules.park.service.IParkOrgBusiStasService;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.vo.ParkOrgBusiStasVo;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;
import com.yunkouan.wms.modules.send.dao.IDeliveryDao;
import com.yunkouan.wms.modules.send.dao.IDeliveryDetailDao;
import com.yunkouan.wms.modules.send.dao.IDeliveryMaterialDao;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;
import com.yunkouan.wms.modules.send.entity.SendDeliveryMaterial;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.service.ICreatePickService;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IPickDetailService;
import com.yunkouan.wms.modules.send.service.IPickLocationService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.service.ISendDeliveryLogService;
import com.yunkouan.wms.modules.send.service.IWaveService;
import com.yunkouan.wms.modules.send.util.DeliverySplitUtil;
import com.yunkouan.wms.modules.send.util.DeliveryUtils;
import com.yunkouan.wms.modules.send.util.ExceptionLogUtil;
import com.yunkouan.wms.modules.send.util.ServiceConstant;
import com.yunkouan.wms.modules.send.util.StockOperate;
import com.yunkouan.wms.modules.send.util.ThreadPoolUtils;
import com.yunkouan.wms.modules.send.util.TransmitXmlUtil;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDelivery2ExternalVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryLogVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryMaterialVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo4Excel;
import com.yunkouan.wms.modules.send.vo.SendOrder4ExcelVo;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.wms.modules.send.vo.SendStastics4ExcelVo;
import com.yunkouan.wms.modules.send.vo.SendStasticsVo;
import com.yunkouan.wms.modules.send.vo.TotalVo;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;

/**
 * 发货单业务实现类
 */
@Service
public class DeliveryServiceImpl extends BaseService implements IDeliveryService,ICreatePickService{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private Logger log = LoggerFactory.getLogger(DeliveryServiceImpl.class);

	@Autowired
	private IDeliveryDao deliveryDao;
	
	@Autowired
	private IDeliveryDetailDao deliveryDetailDao;
	
//	@Autowired
//	private IExpressBillDao expressBillDao;
	
	@Autowired 
	private IMerchantService merchantService;//客商接口
	
//	@Autowired
//	private IPickDao pickDao;
	
	@Autowired
	private IDeliveryMaterialDao deliveryMaterialDao;
	
	@Autowired 
	private ISkuService skuService;//货品接口
	@Autowired 
	private ISkuMaterialBomService bomService;//货品辅材BOM接口
	@Autowired 
	private ISkuTypeService skuTypeService;//货品类型接口
	
	@Autowired 
	private IPickService pickService;//拣货单接口
	
	@Autowired 
	private IWaveService waveService;//波次单接口
	
	@Autowired
	private IPickDetailService pickDetailService;//拣货明细接口
	
	@Autowired
	private IStockService stockService;//库存服务

	@Autowired
	private IExceptionLogService exceptionLogService;//异常服务
	
	@Autowired
	private IWarehouseExtlService wrehouseExtlService;//仓库服务
	
	@Autowired
	private IParkOrgBusiStasService parkOrgBusiStasService;//园区企业业务统计
	
	@Autowired
	private IParkRentService parkRentService;//出租服务
	
	@Autowired
	private IMaterialService materialService;//辅材服务
	
	@Autowired
	private IMsmqMessageService msmqMessageService;
	
	@Autowired
	private TransactionTemplate transactionTemplate;  
	@Autowired
	private IDeliverGoodsApplicationService deliverGoodsApplicationService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private ILocationExtlService locExtlService;
	
	@Autowired
	private IUserService userService;

//	@Autowired
//	private ISysExpressNoPoolService expressPoolService;
	
	@Autowired
	private ISendDeliveryLogService deliveryLogService;
	
	@Autowired
	public IPickLocationService pickLocationService;
	
	@Autowired
	private IWarehouseSettingService warehouseSettingService;

	@Autowired
	private ISysParamService paramService;

	@Autowired
	private IShiftService shiftService;

	public static final int DEFAULT_PAGE_SIZE = 20;
	/**
	 * 跟新运单号的推送状态
	 */
	/**
	 * 更新发货单状态
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月15日 下午6:05:32<br/>
	 * @author Aaron He<br/>
	 */
	public int updateSendStatus(String deliveryId,int sendStatus){
		SendDelivery entity = new SendDelivery();
		entity.setDeliveryId(deliveryId);
		entity.setSendStatus(sendStatus);
		entity.setUpdateTime(new Date());	
		int num = deliveryDao.updateByPrimaryKeySelective(entity);
		return num;
	}
	
	/**
	 * 检查源单号是否重复
	 * @param srcNo
	 * @param orgId
	 */
	public void checkSrcNo(String srcNo,String orgId,List<String> deliveryIds){
		//检查发货单是否已存在源单号
		SendDeliveryVo sendDeliveryVo = new SendDeliveryVo();
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setSrcNo(srcNo);
//		sendDeliveryVo.setStatusLessThan(Constant.SEND_STATUS_CANCAL);//非取消状态	
		sendDeliveryVo.setNotInDeliveryIdList(deliveryIds);
		
		int num = deliveryDao.selectCountByExample(sendDeliveryVo.getCondition(0).getExample());
		if(num > 0)
		  throw new BizException(BizStatus.DELIVERY_SRCNO_IS_REDUPLICATED.getReasonPhrase());
	}
	
	/**
	 * 创建发货单号，新增发货单
	 * 1、创建发货单号
	 * 2、新增发货单
	 * @param sendDeliveryVo
	 * @param orgId
	 * @param wareHouseId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo createNoAndAdd(SendDeliveryVo sendDeliveryVo,String orgId,String wareHouseId,String userId)throws Exception{
		//检查发货明细的skuid与批次号是否重复
		if(sendDeliveryVo.skuIsDuplicate()){
			throw new BizException(BizStatus.SKU_AND_BATCHNO_IS_DUPLIPCATIVE.getReasonPhrase());
		}
		//检查源单号是否重复
		checkSrcNo(sendDeliveryVo.getSendDelivery().getSrcNo(), orgId,null);
		//检查运单号是否重复
		checkExpressNoIsDuplicated(sendDeliveryVo.getSendDelivery().getSrcNo(),sendDeliveryVo.getSendDelivery().getExpressBillNo(),orgId,wareHouseId);
		//1、创建发货单号
//		sendDeliveryVo.toFieldName();
//		if(StringUtils.isEmpty(orgId) || sendDeliveryVo.getFieldName() == null)
//			throw new BizException(BizStatus.DELIVERY_NO_CREATE_ERROR.getReasonPhrase());
//		CommonVo common = new CommonVo(orgId, sendDeliveryVo.getFieldName()); 
		if(StringUtils.isBlank(sendDeliveryVo.getSendDelivery().getDeliveryNo())) {
			String deliveryNo = context.getStrategy4No(orgId, wareHouseId).getDeliveryNo(orgId, sendDeliveryVo.getSendDelivery().getDocType());
			sendDeliveryVo.getSendDelivery().setDeliveryNo(deliveryNo);
		}
		/*if(StringUtils.isBlank(sendDeliveryVo.getSendDelivery().getExpressBillNo())) {
			sendDeliveryVo.getSendDelivery().setExpressBillNo(sendDeliveryVo.getSendDelivery().getDeliveryNo());
		}*/
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_OPEN);//发货单为打开
		//2、新增发货单
		add(sendDeliveryVo,orgId,wareHouseId,userId);
		return sendDeliveryVo;
	}
	/**
	 * 新增发货单
	 * 1、新增发货单明细
	 * 2、统计 发货单总数量
	 * 3、统计发货单货品数
	 * 4、保存发货单
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo add(SendDeliveryVo sendDeliveryVo,String orgId,String wareHouseId,String userId) throws Exception{
		if(sendDeliveryVo == null || StringUtils.isEmpty(orgId) || StringUtils.isEmpty(wareHouseId)) return null;
		if(sendDeliveryVo.getSendDelivery() == null) return sendDeliveryVo;
		// 由于数据可能通过导入而非手工添加进入系统，故再次校验一遍
		SendDelivery entity = sendDeliveryVo.getSendDelivery();
		Set<ConstraintViolation<SendDelivery>> vr = validator.validate(entity, ValidSave.class);
		if(vr.size() > 0) {
			String err = vr.iterator().next().getMessage();
			throw new BizException(err.replaceFirst("\\{", "").replaceFirst("\\}", ""));
		}
		//添加uuid
		sendDeliveryVo.getSendDelivery().setDeliveryId(IdUtil.getUUID());
		sendDeliveryVo.getSendDelivery().setDataFrom(Constant.ASN_DATAFROM_NORMAL);
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(wareHouseId);
		sendDeliveryVo.getSendDelivery().setCreatePerson(userId);
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		sendDeliveryVo.getSendDelivery().defaultValue();
		//1、新增发货单明细
		addDeliveryDetails(sendDeliveryVo.getDeliveryDetailVoList(),
									sendDeliveryVo.getSendDelivery().getDeliveryId(),
									orgId,
									wareHouseId,
									userId);
		//2、统计 发货单总数量
		sendDeliveryVo.calTotalOrder();
		//3、统计发货单货品数
		sendDeliveryVo.calSkuQty();
		sendDeliveryVo.getSendDelivery().setDeliveryId2(context.getStrategy4Id().getDeliverySeq());
		//4、保存发货单
		deliveryDao.insertSelective(sendDeliveryVo.getSendDelivery());
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(sendDeliveryVo.getSendDelivery().getDeliveryId(),
				userId,Constant.DELIVERY_LOG_TYPE_ADD,orgId,wareHouseId);
		//返回寄件人信息 
		sendDeliveryVo.setConsignorName(FqDataUtils.getMerchantNameById(merchantService, sendDeliveryVo.getSendDelivery().getConsignor()));
		return sendDeliveryVo;
	}

	/**
	 * 添加发货单明细
	 * @param details
	 * @param deliveryId
	 */
	@Transactional(rollbackFor=Exception.class)
	public int addDeliveryDetails(List<DeliveryDetailVo> details,String deliveryId,
			String orgId,String wareHouseId,String operator) throws Exception{
		if(details == null || details.isEmpty() 
				|| StringUtils.isEmpty(orgId)
				||StringUtils.isEmpty(wareHouseId)
				||StringUtils.isEmpty(deliveryId)) return 0;
		
		int total = 0;
		//保存发货单明细
		for (DeliveryDetailVo detailsVo : details) {
			SendDeliveryDetail record = detailsVo.getDeliveryDetail();
			record.setDeliveryDetailId(IdUtil.getUUID());
			record.setDeliveryId(deliveryId);
			record.setOrgId(orgId);
			record.setWarehouseId(wareHouseId);
			record.setCreatePerson(operator);
			record.setUpdatePerson(operator);
			record.defaultValue();
			total += record.getOrderQty();
			record.setDeliveryDetailId2(context.getStrategy4Id().getDeliveryDetailSeq());
			deliveryDetailDao.insertSelective(record);
		}
		
		return total;
	}

	/**
	 * 更新发货单
	 * @param sdVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo update(SendDeliveryVo sdVo,String orgId,String wareHouseId,String operator) throws Exception{
		if(sdVo == null ||sdVo.getSendDelivery() == null) return null;

		//发货单是否打开状态
		if(Constant.SEND_STATUS_OPEN != qryDeliveryStatus(sdVo.getSendDelivery().getDeliveryId()))
			throw new BizException(BizStatus.SEND_DELIVERY_STATUS_IS_NOT_OPEN.getReasonPhrase());
		
		//如果不是拆分的子单，则检查源单号是否重复
		if(StringUtil.isTrimEmpty(sdVo.getSendDelivery().getParentId())){
			checkSrcNo(sdVo.getSendDelivery().getSrcNo(),orgId,Arrays.asList(sdVo.getSendDelivery().getDeliveryId()));
		}
		
		//检查运单号是否重复
		checkExpressNoIsDuplicated(sdVo.getSendDelivery().getSrcNo(),sdVo.getSendDelivery().getExpressBillNo(),orgId,wareHouseId);
		
		//删除原发货单明细
		if(sdVo.getDelDetailIds() != null &&  !sdVo.getDelDetailIds().isEmpty()){
			for(String detailId:sdVo.getDelDetailIds()){
				deliveryDetailDao.deleteByPrimaryKey(detailId);
			}
		}
		if(sdVo.getDeliveryDetailVoList() != null && !sdVo.getDeliveryDetailVoList().isEmpty()){
			//更新发货明细
			updateDetails(sdVo.getDeliveryDetailVoList(),sdVo.getSendDelivery().getDeliveryId(),orgId,wareHouseId,operator);
			//更新 发货单总数量
			sdVo.calTotalOrder();
			//统计发货单货品数
			sdVo.calSkuQty();
		}
		sdVo.getSendDelivery().defaultValue();
		//更新发货单
		sdVo.getSendDelivery().setUpdatePerson(operator);
		sdVo.getSendDelivery().setUpdateTime(new Date());
		deliveryDao.updateByPrimaryKeySelective(sdVo.getSendDelivery());
		//若发货单关联了波次单，则重新计算波次单的货品数
		if(StringUtil.isNotEmpty(sdVo.getSendDelivery().getWaveId())){
			waveService.updateQty(sdVo.getSendDelivery().getWaveId(), operator);
		}
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(sdVo.getSendDelivery().getDeliveryId(),
				operator,Constant.DELIVERY_LOG_TYPE_UPDATE,orgId,wareHouseId);
		//返回寄件人信息 
		sdVo.setConsignorName(FqDataUtils.getMerchantNameById(merchantService, sdVo.getSendDelivery().getConsignor()));
		return sdVo;
	}
	
	
	/**
	 * 更新发货明细
	 * @param details
	 * @param deliveryId
	 * @version 2017年2月14日 下午1:30:51<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateDetails(List<DeliveryDetailVo> detailVos,String deliveryId,
			String orgId,String wareHouseId,String operator) throws Exception{		
		 for(DeliveryDetailVo deliveryDetailVo:detailVos){
			 SendDeliveryDetail record = deliveryDetailVo.getDeliveryDetail();
			 //若id不存在则新增
			 if(StringUtil.isTrimEmpty(deliveryDetailVo.getDeliveryDetail().getDeliveryDetailId())){
					record.setDeliveryDetailId(IdUtil.getUUID());
					record.setDeliveryId(deliveryId);
					record.setOrgId(orgId);
					record.setWarehouseId(wareHouseId);
					record.setCreatePerson(operator);
					record.setUpdatePerson(operator);
					record.defaultValue();
					record.setDeliveryDetailId2(context.getStrategy4Id().getDeliveryDetailSeq());
					deliveryDetailDao.insertSelective(record);
			 }
			 //否则更新
			 else{
				 record.setUpdatePerson(operator);
				 record.setUpdateTime(new Date());
				 deliveryDetailDao.updateByPrimaryKeySelective(record);
			 }
		 }
		 
	}

	/**
	 * 保存并生效发货单
	 * 1、新增或更新发货单
	 * 2、生效发货单
	 * 3、向ems发送订单
	 * @param sendDeliveryVo
	 * @param orgId
	 * @param wareHouseId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo saveAndEnable(SendDeliveryVo sendDeliveryVo,String orgId,String wareHouseId,String userId)throws Exception{
		SendDeliveryVo vo = new SendDeliveryVo();
		//1、新增或更新发货单
		//判断是否存在发货id,存在则修改
		if(!StringUtils.isEmpty(sendDeliveryVo.getSendDelivery().getDeliveryId())){
			vo = update(sendDeliveryVo,orgId,wareHouseId,userId);
		}
		//不存在则新增
		else{
			vo = createNoAndAdd(sendDeliveryVo,orgId,wareHouseId,userId);
		}
		//2、生效发货单
		enableCheckWave(vo.getSendDelivery().getDeliveryId(), userId);
		vo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_ACTIVE);
		return vo;
	}

	/**
	 * 查询发货单列表
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @return
	 */
	public ResultModel qryPageList(SendDeliveryVo sendDeliveryVo) throws Exception{
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) return null;
		ResultModel result = new ResultModel();
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//查询发货单列表
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(wareHouseId);
		
		List<String> deliveryIdList = new ArrayList<String>();
		//根据拣货单号查询拣货单列表
//		if(sendDeliveryVo.getSendPickVo() != null && 
//				(sendDeliveryVo.getSendPickVo().getSendPick().getPickBillPrintTimes() != null || 
//				   StringUtils.isNotEmpty(sendDeliveryVo.getSendPickVo().getPickNo()))){
//			List<SendPickVo> pVoList = pickService.qryListByParam(sendDeliveryVo.getSendPickVo());
//			if(pVoList == null || pVoList.isEmpty()) return result;
//			deliveryIdList.addAll(pVoList.stream().map(t->t.getSendPick().getDeliveryId()).collect(Collectors.toList()));
//		}

		//根据货主查询发货单id列表
		if(StringUtils.isNotEmpty(sendDeliveryVo.getOwnerName())){
			//根据客商名称查询id列表
//			List<String> merchantList = merchantService.list(sendDeliveryVo.getOwnerName());
//			if(merchantList == null || merchantList.isEmpty()) return result;
			//根据客商名称列表查询货品列表
			SkuVo skuVo = new SkuVo().setMerchant(new MetaMerchant().setMerchantName(sendDeliveryVo.getOwnerName()));
			List<MetaSku> skuList = skuService.qryAllList(skuVo,loginUser);
			if(skuList == null || skuList.isEmpty()) return result;
			List<String> skuIdList = skuList.stream().map(t->t.getSkuId()).collect(Collectors.toList());
			//在发货明细表中查询包含此货品id的发货id列表
			DeliveryDetailVo detailVo = new DeliveryDetailVo();
			detailVo.setSkuIdList(skuIdList);
			List<SendDeliveryDetail> detailList = deliveryDetailDao.selectByExample(detailVo.getConditionExample());
			if(detailList == null || detailList.isEmpty()) return result;
			deliveryIdList.addAll(detailList.stream().map(t->t.getDeliveryId()).collect(Collectors.toList()));
		}
		
		if(!deliveryIdList.isEmpty()){
			sendDeliveryVo.setLoadConfirmIds(deliveryIdList);
		}
		//发货单查询没有选就默认查询除取消以外的所有状态
		if(sendDeliveryVo.getSendDelivery().getDeliveryStatus() == null){
			sendDeliveryVo.setLessThanStatus(Constant.SEND_STATUS_CANCAL);
		}
		
		//分页设置
		Page<SendDelivery> page = null;
		if ( sendDeliveryVo.getCurrentPage() == null ) {
			sendDeliveryVo.setCurrentPage(0);
		}
		if ( sendDeliveryVo.getPageSize() == null ) {
			sendDeliveryVo.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(sendDeliveryVo.getCurrentPage()+1, sendDeliveryVo.getPageSize());
		//查询发货单列表
		List<SendDelivery> list = deliveryDao.selectByExample(sendDeliveryVo.getCondition(0).getExample());
		if ( list == null || list.isEmpty() ) {
			return result;
		}
		result.setPage(page);	

		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		//货主map
		Map<String,MetaMerchant>  merchantMap = new HashMap<String,MetaMerchant>();
		for (SendDelivery sendDelivery : list) {
			SendDeliveryVo vo = new SendDeliveryVo();			
			vo.setSendDelivery(sendDelivery);
			if(StringUtil.isNotEmpty(sendDelivery.getOwner())){
				//查询货主
				if(merchantMap.containsKey(sendDelivery.getOwner())){
					vo.setOwnerName(merchantMap.get(sendDelivery.getOwner()).getMerchantName());
					vo.setMerchantShortName(merchantMap.get(sendDelivery.getOwner()).getMerchantShortName());
				}else{
					MetaMerchant metaMerchant = FqDataUtils.getMerchantById(merchantService,sendDelivery.getOwner());
					if (metaMerchant != null) {
						vo.setOwnerName(metaMerchant.getMerchantName());
						vo.setMerchantShortName(metaMerchant.getMerchantShortName());
						merchantMap.put(sendDelivery.getOwner(), metaMerchant);
					}
				}
			}
			if(StringUtils.isNotEmpty(sendDelivery.getWaveId())){
				//查询波次单号
				vo.setWaveNo(FqDataUtils.getWaveNoById(waveService, sendDelivery.getWaveId()));
			}
			//收货方名称
			vo.setReceiverName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getReceiver()));
			results.add(vo);
		}	
		
		result.setList(results);
		return result;
	}
	
	
	/**
	 * 查询发货单列表
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @return
	 */
	public List<SendDeliveryVo> qryListByParam(SendDeliveryVo sendDeliveryVo) {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) return null;
		
		//查询发货单列表
		List<SendDelivery> list = deliveryDao.selectByExample(sendDeliveryVo.getCondition(0).getExample());
		if ( list == null || list.isEmpty() ) {
			return null;
		}
		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		for (SendDelivery sendDelivery : list) {
			
			SendDeliveryVo vo = new SendDeliveryVo();			
			vo.setSendDelivery(sendDelivery);
			results.add(vo);
		}	
		return results;
	}
	 

	/**
	 * 查询列表
	 * @param param
	 * @return
	 * @version 2017年3月1日 下午7:38:30<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryList(SendDelivery param){
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		param.setOrgId(orgId);
		param.setWarehouseId(wareHouseId);
		//查询发货单列表
		List<SendDelivery> list = deliveryDao.qryList(param);		
		
		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		for (SendDelivery sendDelivery : list) {
			SendDeliveryVo vo = new SendDeliveryVo();
			vo.setSendDelivery(sendDelivery);
			results.add(vo);
		}
		return results;
	}
	public List<SendDeliveryVo> qryList2(SendDelivery param){
		//查询发货单列表
		List<SendDelivery> list = deliveryDao.qryList(param);		
		
		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		for (SendDelivery sendDelivery : list) {
			SendDeliveryVo vo = new SendDeliveryVo();
			vo.setSendDelivery(sendDelivery);
			results.add(vo);
		}
		return results;
	}
	public List<String>getTask(String orgId){
		return deliveryDao.getTask(orgId);
	}

	/**
	 * 取消发货单
	 * 1、检查发货单是否打开状态
	 * 2、检查是否关联波次单
	 * 3、发货单状态由“打开”自动变更为“取消”
	 * 4、新增异常记录
	 * @param deliveryId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年2月14日 下午3:07:31<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public int cancelSendDelivery(String deliveryId,String operator) throws Exception{
		if(deliveryId == null) return 0;
		//1、检查发货单是否打开状态
		checkStatus(deliveryId,Constant.SEND_STATUS_OPEN,BizStatus.SEND_DELIVERY_STATUS_IS_NOT_OPEN.getReasonPhrase());	
		//2、检查是否关联波次单
//		if(isAssociateWave(deliveryId)) 
//			throw new BizException(BizStatus.SEND_DELIVERY_ASSOCIATE_WAVE.getReasonPhrase());
		//根据id查询发货单
		SendDelivery sendDelivery = deliveryDao.selectByPrimaryKey(deliveryId);
		//查询是否有关联的申请单
    	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
    	List<String> statusList = new ArrayList<String>();
    	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
    	applicationVo.setStatusNotIn(statusList);
    	applicationVo.getEntity().setDeliveryId(deliveryId);
    	List<DeliverGoodsApplicationVo> qryList = deliverGoodsApplicationService.qryList(applicationVo);
    	if (qryList != null && !qryList.isEmpty()) {
    		throw new BizException("err_application_not_null");
    	}
		//3、发货单状态由“打开”自动变更为“取消”
		int num = updateStatus(deliveryId,operator,Constant.SEND_STATUS_CANCAL);
		
		
		
		//4、新增异常记录
		ExceptionLog exceptionLog =  ExceptionLogUtil.createInstance(Constant.EXCEPTION_TYPE_SEND_ABNORMAL,
																	sendDelivery.getDeliveryNo(), 
																	Constant.EXCEPTION_STATUS_TO_BE_HANDLED,
																	Constant.EXCEPTION_LEVEL_SLIGHT,
																	ServiceConstant.DELIVERY_CANCEL);
		exceptionLogService.add(exceptionLog);
		
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(deliveryId,operator,Constant.DELIVERY_LOG_TYPE_CANCEL,sendDelivery.getOrgId(),sendDelivery.getWarehouseId());
		
		
		return num;
	}
	
	/**
	 * 生效发货单，检查是否关联波次单
	 * 1、检查是否关联波次单
	 * 2、生效发货单
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月14日 下午5:14:29<br/>
	 * @author Aaron He<br/>
	 */
	public int enableCheckWave(String deliveryId,String operator) throws Exception{
		if(deliveryId == null) return 0;
		
		//1、检查是否关联波次单
		if(isAssociateWave(deliveryId)) 
			throw new BizException(BizStatus.SEND_DELIVERY_ASSOCIATE_WAVE.getReasonPhrase());
		
		//2、生效发货单
		int num = enable(deliveryId,operator);
		
		return num;
		
	}
	
	/**
	 * 生效发货单
	 * 1、检查发货单是否打开状态
	 * 2、检查仓库的可分配库存（[库存数量 – 拣货分配数量]）是否有相应数量
	 * 3、发货单状态变为生效
	 * @param deliveryId
	 * @param operator
	 * @version 2017年3月1日 下午8:20:24<br/>
	 * @author Aaron He<br/>
	 */
	public int enable(String deliveryId,String operator)throws Exception{
		if(deliveryId == null) return 0;

		//1、检查发货单是否打开状态
		checkStatus(deliveryId,Constant.SEND_STATUS_OPEN,BizStatus.SEND_DELIVERY_STATUS_IS_NOT_OPEN.getReasonPhrase());			
		//查询发货单
		SendDeliveryVo deliveryVo = getDeliveryById(deliveryId);
		//检查是否有物流公司
		if(StringUtil.isTrimEmpty(deliveryVo.getSendDelivery().getExpressServiceCode())){
			throw new BizException("expressServiceCode_is_null");
		}
		//检查发货单是否有运单号
		if(StringUtil.isTrimEmpty(deliveryVo.getSendDelivery().getExpressBillNo())){
			throw new BizException(BizStatus.EXPRESS_BILL_NO_IS_NULL.getReasonPhrase());
		}
		//检查发货单是否有发货明细
		if(deliveryVo.getDeliveryDetailVoList().isEmpty()){
			throw new BizException(BizStatus.SEND_DELIVERYDETAIL_IS_NULL.getReasonPhrase());
		}
//		FqDataUtils fdu = new FqDataUtils();
		//2、检查仓库的可分配库存（[库存数量 – 拣货分配数量]）是否有相应数量
		String returnMsg = "";
		for (DeliveryDetailVo deliveryDetailVo : deliveryVo.getDeliveryDetailVoList()) {
			SendDeliveryDetail sendDeliveryDetail = deliveryDetailVo.getDeliveryDetail();
			//查询货品名称
//			MetaSku sku = fdu.getSkuById(skuService, sendDeliveryDetail.getSkuId());
//			deliveryDetailVo.setSkuName(sku.getSkuName());
			//仓库的可分配库存（[库存数量 – 拣货分配数量]）是否有相应数量
			//若批次号为空，则检查此sku的所有库存
			
			//检查拣货区与存放区库存是否足够。如果库存不够，就提示库存不足
			InvStockVO stockVo = StockOperate.getInvStockVO(sendDeliveryDetail.getSkuId(), 
												sendDeliveryDetail.getBatchNo(), 
												null, 
												null, 
												null,
												sendDeliveryDetail.getOrderQty(), 
												null,
												false,
//												Arrays.asList(Constant.LOCATION_TYPE_STORAGE,Constant.LOCATION_TYPE_PICKUP));
												Arrays.asList(Constant.LOCATION_TYPE_PICKUP));
			try {
				stockService.checkStock(stockVo);
			} catch (BizException e) {
				Object[] param = e.getParam();
				String msg = (String) param[0];
				returnMsg += msg + "<br />";
			}
			//检查拣货区库存是否足够。如果不够，则生成补货单-不设拣货区
//			stockService.repSendSku(sendDeliveryDetail.getSkuId(), sendDeliveryDetail.getOrderQty());
		}		
		if (StringUtil.isNoneBlank(returnMsg)) {
			throw new BizException("err_stock_size",returnMsg);
		}
		//3、发货单状态变为生效
		int num = updateStatus(deliveryId,operator,Constant.SEND_STATUS_ACTIVE);
		
		//记录操作明细“获取运单号并生效”
		deliveryLogService.addNewDeliveryLog(deliveryId,
				operator, Constant.DELIVERY_LOG_TYPE_ENABLE, 
				deliveryVo.getSendDelivery().getOrgId(),deliveryVo.getSendDelivery().getWarehouseId());
		return num;		
	}
	
	/**
	 * 生效并自动生成拣货单
	 * @param deliveryId
	 * @param operator
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void enableAndCreatePick(String deliveryId,String operator)throws Exception{
		//生效发货单
		enable(deliveryId,operator);
		//自动生成拣货单
		createPickAndAssign(deliveryId, operator);
	}
	
	/**
	 * 保存并生效发货单，自动生成拣货单
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo saveAndEnableAndCreatePick(SendDeliveryVo sdVo,String orgId,String wareHouseId,String userId)throws Exception{
		//保存生效发货单
		SendDeliveryVo vo = saveAndEnable(sdVo, orgId, wareHouseId, userId);
		//自动生成拣货单
		createPickAndAssign(sdVo.getSendDelivery().getDeliveryId(), userId);
		
		return vo;
	}
	
	
	/**
	 * 从发货单创建拣货单且保存分配库位
	 * 1、创建拣货单
	 * 2、保存拣货单并且分配库位
	 * @param deliveryId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月3日 上午11:21:18<br/>
	 * @author Aaron He<br/>
	 */
	public void createPickAndAssign(String deliveryId,String operator) throws Exception{
		if(StringUtils.isEmpty(deliveryId)) return;
		//1、创建拣货单
		SendPickVo pickVo = createPick(deliveryId, operator);
		//2、保存拣货单并且分配库位
		pickService.SavePickAndAssgn(pickVo, operator);
		
		//生效拣货单2018-5-24
		pickService.active(pickVo.getSendPick().getPickId(), operator);
	}
	
	/**
	 * 失效发货单
	 * 1、检查发货单是否生效状态
	 * 2、检查拣货单是否都是打开状态
	 * 3、检查是否关联波次单
	 * 4、发货单状态变为打开
	 * 5、删除拣货单
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 下午5:53:28<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public int disableSendDelivery(String deliveryId,String orgId,String warehouseId,String operator)throws Exception{
		if(deliveryId == null) return 0;
		//1、检查发货单是否生效状态
		checkStatus(deliveryId,Constant.SEND_STATUS_ACTIVE,BizStatus.SEND_DELIVERY_STATUS_IS_NOT_ACTIVE.getReasonPhrase());
		
		//2、检查拣货单是否都是打开或者取消状态
		if(!pickService.isOpenByDeliveryId(deliveryId))
			throw new BizException(BizStatus.PICK_STATUS_IS_NOT_OPEN.getReasonPhrase());
		
		//3、检查是否关联波次单
		if(isAssociateWave(deliveryId)) 
			throw new BizException(BizStatus.SEND_DELIVERY_ASSOCIATE_WAVE.getReasonPhrase());
		
		//4、发货单状态变为打开
		int num = updateStatus(deliveryId,operator,Constant.SEND_STATUS_OPEN);
		
		//5、拣货单变为取消
		SendPick pick = new SendPick();
		pick.setPickStatus(Constant.PICK_STATUS_CANCAL);
		pick.setUpdatePerson(operator);
		pick.setUpdateTime(new Date());
		
		SendPickVo paramVo = new SendPickVo();
		paramVo.getSendPick().setDeliveryId(deliveryId);
		paramVo.getSendPick().setOrgId(orgId);
		paramVo.getSendPick().setWarehouseId(warehouseId);

		pickService.updatePickByExample(pick, paramVo.getConditionExample());
		
		//删除拣货明细
		List<SendPickVo> pickVoList = pickService.qryListByParam(paramVo);
		if (pickVoList != null) {
			for(SendPickVo pickVo:pickVoList){
				SendPickDetailVo detailVo = new SendPickDetailVo();
				detailVo.getSendPickDetail().setPickId(pickVo.getSendPick().getPickId());
				pickDetailService.delEntity(detailVo);
			}
		}
		//记录操作明细失效
		deliveryLogService.addNewDeliveryLog(deliveryId,
				operator, Constant.DELIVERY_LOG_TYPE_DISABLE, 
				LoginUtil.getLoginUser().getOrgId(),LoginUtil.getWareHouseId());
		return num;
	}
	
	
	/**
	 * 查看发货单详情
	 * @param deliveryId
	 * @return
	 * @version 2017年2月14日 下午5:23:19<br/>
	 * @author Aaron He<br/>
	 */
	public SendDeliveryVo view(String deliveryId) throws Exception{
		if(StringUtils.isEmpty(deliveryId)) return null;
		
		SendDeliveryVo deliveryVo = new SendDeliveryVo();
		//查询发货单，
		SendDelivery sendDelivery = deliveryDao.selectByPrimaryKey(deliveryId);	
		
		//发货方名称
		deliveryVo.setSenderName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getSender()));
		//收货方名称
		deliveryVo.setReceiverName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getReceiver()));
		//仓库寄件人（客商）名称 
		deliveryVo.setConsignor(FqDataUtils.getMerchantById(merchantService, sendDelivery.getConsignorId()));
		deliveryVo.setConsignorName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getConsignor()));

		//查询仓库
		MetaWarehouse warehouse = wrehouseExtlService.findWareHouseById(sendDelivery.getWarehouseId());
		deliveryVo.setWarehouseName(FqDataUtils.getWarehouseNameById(wrehouseExtlService, sendDelivery.getWarehouseId()));
		deliveryVo.setWarehouseNo(warehouse.getWarehouseNo());
		//查询货主
		deliveryVo.setOwnerName(FqDataUtils.getMerchantNameById(merchantService,sendDelivery.getOwner()));
		//查询发货明细		
		List<DeliveryDetailVo> detailVoList = getDetailsInfoBydeliveryId(deliveryId);		
		deliveryVo.setSendDelivery(sendDelivery);
		deliveryVo.setDeliveryDetailVoList(detailVoList);
		
		//查询辅材明细
		SendDeliveryMaterialVo deliveryMaterialVo = new SendDeliveryMaterialVo();
		deliveryMaterialVo.getSendDeliveryMaterial().setOrgId(sendDelivery.getOrgId());
		deliveryMaterialVo.getSendDeliveryMaterial().setWarehouseId(sendDelivery.getWarehouseId());
		deliveryMaterialVo.getSendDeliveryMaterial().setDeliveryId(deliveryId);
		//查询发货单辅材
		List<SendDeliveryMaterial> materialList = deliveryMaterialDao.selectByExample(deliveryMaterialVo.getConditionExample());
		for(SendDeliveryMaterial sdm:materialList){
			SendDeliveryMaterialVo sdmVo = new SendDeliveryMaterialVo();
			sdmVo.setSendDeliveryMaterial(sdm);
			//根据id查询辅材信息
			MaterialVo materialVo = materialService.get(sdm.getMaterialId());
			sdmVo.setMaterialVo(materialVo);
			deliveryVo.getDeliveryMaterialVoList().add(sdmVo);
		}

		SendDeliveryLogVo vo = new SendDeliveryLogVo();
		vo.getEntity().setDeliveryId(deliveryId);
		List<SendDeliveryLogVo> logList = deliveryLogService.qryLogList(vo);
		deliveryVo.setLogList(logList);

		return deliveryVo;
	}
	
	/**
	 * 获取发货明细详细信息
	 * @param deliveryId
	 * @return
	 */
	public List<DeliveryDetailVo> getDetailsInfoBydeliveryId(String deliveryId) throws Exception{
		List<DeliveryDetailVo> detailVoList = getDetailsByDeliveryId(deliveryId);
		//查询货品
		for (DeliveryDetailVo t : detailVoList) {
			SendDeliveryDetail detail = t.getDeliveryDetail();
			//查询拣货库位
			List<SendPickLocationVo> realLocations = qryRealLocationByDetailId(detail.getDeliveryDetailId(),detail.getOrgId(),detail.getWarehouseId());
			t.setRealPickLocations(realLocations);
			//查询货品
			MetaSku sku = FqDataUtils.getSkuById(skuService, detail.getSkuId());
			if(sku == null) continue;
			MetaSkuType skuType = FqDataUtils.getSkuTypeById(skuTypeService, sku.getSkuTypeId());
			t.setSku(sku);
			t.setSkuName(sku.getSkuName());
			t.setMeasureUnit(sku.getMeasureUnit());
			t.setSkuNo(sku.getSkuNo());
			t.setSkuTypeName(skuType.getSkuTypeName());
			t.setSpecModel(sku.getSpecModel());
			t.setPerWeight(sku.getPerWeight());
			t.setPerVolume(sku.getPerVolume());
			t.setSkuBarCode(sku.getSkuBarCode());
			t.setPerPrice(sku.getPerPrice());
		}
		return detailVoList;
	}
	
	/**
	 * 根据id获取发货单
	 * @param deliveryId
	 * @return
	 * @version 2017年3月11日 下午1:17:48<br/>
	 * @author Aaron He<br/>
	 */
	public SendDeliveryVo getDeliveryById(String deliveryId){
		if(StringUtils.isEmpty(deliveryId)) return null;
		
		SendDeliveryVo deliveryVo = new SendDeliveryVo();
		//查询发货单，
		SendDelivery sendDelivery = deliveryDao.selectByPrimaryKey(deliveryId);	
		//查询发货明细		
		List<DeliveryDetailVo> detailVoList = getDetailsByDeliveryId(deliveryId);	
		deliveryVo.setSendDelivery(sendDelivery);
		deliveryVo.setDeliveryDetailVoList(detailVoList);
		return deliveryVo;
	}
	
	/**
	 * 拆分发货单
	 * 1、检查发货单是否打开状态
	 * 2、检查是否关联波次单
	 * 3、按规则拆分发货单
	 * 4、保存子发货单
	 * 5、原发货单状态变为取消
	 * @param sdVo
	 * @param operator
	 * @return
	 * @version 2017年2月14日 下午5:40:59<br/>
	 * @author Aaron He<br/>
	 */
	public void splitDelivery(SendDeliveryVo sdVo,String orgId,String wareHouseId,String operator) throws Exception{
		if(sdVo == null || sdVo.getSendDelivery() == null) 
			throw new BizException(BizStatus.PARAMETER_ERROR.getReasonPhrase());
		//1、检查发货单是否打开状态
		checkStatus(sdVo.getSendDelivery().getDeliveryId(),
				Constant.SEND_STATUS_OPEN,
				BizStatus.SEND_DELIVERY_STATUS_IS_NOT_OPEN.getReasonPhrase());
		//2、检查是否关联波次单
		if(isAssociateWave(sdVo.getSendDelivery().getDeliveryId())) 
			throw new BizException(BizStatus.SEND_DELIVERY_ASSOCIATE_WAVE.getReasonPhrase());
		
		//拆分发货单明细是否存在
		if(sdVo.getDeliveryDetailVoList() == null || sdVo.getDeliveryDetailVoList().isEmpty())
			throw new BizException(BizStatus.SEND_DELIVERYDETAIL_IS_NULL.getReasonPhrase());
		
		//查询原发货单
		SendDelivery delivery = deliveryDao.selectByPrimaryKey(sdVo.getSendDelivery().getDeliveryId());
		//查询原发货明细
		List<DeliveryDetailVo> orgList = getDetailsByDeliveryId(sdVo.getSendDelivery().getDeliveryId());
		SendDeliveryVo parentVo = new SendDeliveryVo();
		parentVo.setSendDelivery(delivery);
		parentVo.setDeliveryDetailVoList(orgList);
		
		//3、按规则拆分发货单
		DeliverySplitUtil deliverySplitUtil = new DeliverySplitUtil();
		deliverySplitUtil.setParentVo(parentVo);
		deliverySplitUtil.split(sdVo, operator);
		//如果拆分以后子发货单没有明细，则不拆分
		if(deliverySplitUtil.getDeliveryVo_1().getDeliveryDetailVoList().isEmpty() ||
				deliverySplitUtil.getDeliveryVo_2().getDeliveryDetailVoList().isEmpty()){
			throw new BizException(BizStatus.DELIVERY_NUM_NO_SPLIT.getReasonPhrase());
		}
		//提交事务
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				try {
					//4、保存子发货单
					add(deliverySplitUtil.getDeliveryVo_1(),orgId,wareHouseId,operator);
					add(deliverySplitUtil.getDeliveryVo_2(),orgId,wareHouseId,operator);
					//5、原发货单状态变为取消
					updateStatus(sdVo.getSendDelivery().getDeliveryId(),operator,Constant.SEND_STATUS_CANCAL);

				} catch (Exception e) {					
					e.printStackTrace();
					throw new BizException(e.getMessage());
				}
			
			}
		
		});		
	}
	
	
	/**
	 * 装车确认
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月14日 下午6:33:16<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public void loadConfirm(SendDeliveryVo sdVo) throws Exception{
		if(sdVo == null || sdVo.getSendDelivery() == null) return;
		//是否“部分拣货”或“已拣货”
		for(String id:sdVo.getLoadConfirmIds()){
			int status = qryDeliveryStatus(id);
			if(Constant.SEND_STATUS_PARTPICK != status && Constant.SEND_STATUS_ALLPICK != status)
				throw new BizException(BizStatus.SEND_DELIVERY_STATUS_IS_NOT_PARTPICK_AND_ALLPICK.getReasonPhrase());			
		}
		//更新条件
		SendDeliveryVo param = new SendDeliveryVo();
		param.setLoadConfirmIds(sdVo.getLoadConfirmIds());
		//更新发运状态与备注
		deliveryDao.updateByExampleSelective(sdVo.getSendDelivery(), param.getCondition(0).getExample());

	}
	
	/**
	 * 根据发货id查询发货明细列表
	 * @param deliveryId
	 * @return
	 * @version 2017年2月15日 下午1:17:52<br/>
	 * @author Aaron He<br/>
	 */
	public List<DeliveryDetailVo> getDetailsByDeliveryId(String deliveryId){
		if(StringUtils.isEmpty(deliveryId)) return null;
		
		List<SendDeliveryDetail> details = deliveryDetailDao.selectByDeliveryId(deliveryId);
		List<DeliveryDetailVo> records = new ArrayList<DeliveryDetailVo>();
		details.stream().forEach(t->{
			DeliveryDetailVo vo = new DeliveryDetailVo();
			vo.setDeliveryDetail(t);
			records.add(vo);
		});
		
		return records;
	}
	
	/**
	 * 批量获取所有发货单id的发货明细列表
	 * @param deliveryIds
	 * @return
	 */
	public List<DeliveryDetailVo> getDetailsByDeliveryIds(List<String> deliveryIds){
		if(deliveryIds == null || deliveryIds.isEmpty()) return null;
		
		DeliveryDetailVo detailVo = new DeliveryDetailVo();
		detailVo.setDeliveryIdList(deliveryIds);
		List<SendDeliveryDetail> deliveryDetails = deliveryDetailDao.selectByExample(detailVo.getConditionExample());
		List<DeliveryDetailVo> records = new ArrayList<DeliveryDetailVo>();
		deliveryDetails.stream().forEach(t->{
			DeliveryDetailVo vo = new DeliveryDetailVo();
			vo.setDeliveryDetail(t);
			records.add(vo);
		});
		return records;
	}
	
	
	/**
	 * 拣货确认更新发货单
	 * 1、查询对应的发货单明细
	 * 2、查询发货单明细对应的拣货单明细
	 * 3、判断是否完成拣货
	 * 4、发货单状态由“生效”更改为“已拣货”，或者发货单状态由“生效”更改为“部分拣货”
	 * 
	 * @param deliveryId
	 * @param operator
	 * @throws Exception
	 * @version 2017年3月6日 下午2:18:17<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo updateAfterConfirmPick(String deliveryId,String operator) throws Exception{
		//1、根据发货单id查询对应的发货单及发货明细
		SendDeliveryVo deliveryVo = getDeliveryById(deliveryId);
		
		if(deliveryVo == null) return null;

		List<String> detailIds = deliveryVo.toDetailIdList();
		//2、查询发货单明细对应的拣货单明细 
		List<SendPickDetail> list = pickService.qryPickQtyByDeliveryDetails(
				 detailIds,deliveryVo.getSendDelivery().getOrgId(),deliveryVo.getSendDelivery().getWarehouseId());
		//map<发货明细id，拣货明细列表>
		Map<String,List<SendPickDetail>> map = new HashMap<String,List<SendPickDetail>>();
		//3、判断是否完成拣货
		for(SendPickDetail pd:list){
			//检查是否拣货单是否存在或者已取消
			int pickStatus = pickService.getPickStatus(pd.getPickId());
			if(pickStatus == 0 || pickStatus == Constant.PICK_STATUS_CANCAL) continue;
			//map<发货明细id，拣货数量>
			List<SendPickDetail> pdList = map.get(pd.getDeliveryDetailId());
			if(pdList == null){
				pdList = new ArrayList<SendPickDetail>();
			}
			pdList.add(pd);
			map.put(pd.getDeliveryDetailId(), pdList);			
		}		
		
		int status = Constant.SEND_STATUS_ALLPICK;
		Double qty = 0d;
		double weight = 0.0;
		double volume = 0.0;
		//更新发货明细订单数量 订单数量-拣货数量
		for (DeliveryDetailVo detailVo : deliveryVo.getDeliveryDetailVoList()) {
			List<SendPickDetail> pickList = map.get(detailVo.getDeliveryDetail().getDeliveryDetailId());
			if(pickList != null && !pickList.isEmpty()){
				//拣货总数量，总重量，总体积
				qty = pickList.stream().mapToDouble(p->p.getPickQty()).sum();
				weight = pickList.stream().mapToDouble(p->p.getPickWeight()).sum();
				volume = pickList.stream().mapToDouble(p->p.getPickVolume()).sum();
				//计算发货剩余数量，重量，体积与拣货数量
				detailVo.getDeliveryDetail().calPickQty(qty);
				detailVo.getDeliveryDetail().calPickWeight(weight);
				detailVo.getDeliveryDetail().calPickVolume(volume);	
				detailVo.getDeliveryDetail().setUpdatePerson(operator);
				detailVo.getDeliveryDetail().setUpdateTime(new Date());				
				//更新
				deliveryDetailDao.updateByPrimaryKeySelective(detailVo.getDeliveryDetail());
			}	
		}
		//计算发货单拣货数量
		deliveryVo.calTotalPick();
		//4、是否完成拣货,若是发货单状态由“生效”更改为“已拣货”,若否发货单状态由“生效”更改为“部分拣货”		
		if(deliveryVo.getSendDelivery().getOrderQty() != deliveryVo.getSendDelivery().getPickQty()){
			status = Constant.SEND_STATUS_PARTPICK;
		}
		deliveryVo.getSendDelivery().setDeliveryStatus(status);
		deliveryVo.getSendDelivery().setUpdatePerson(operator);
		deliveryVo.getSendDelivery().setUpdateTime(new Date());
		//更新
		deliveryDao.updateByPrimaryKeySelective(deliveryVo.getSendDelivery());
		
		return deliveryVo;		
	}
	
	/**
	 * 拣货确认更新发货单
	 * @param sendPickVo
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateAfterCompletePick(SendPickVo sendPickVo,String userId) throws Exception{
		//统计发货单 map<deliveryId,List<SendPickDetailVo>> 
		Map<String,List<SendPickDetailVo>> map = sendPickVo.getSendPickDetailVoList()
				.stream()
				.collect(Collectors.groupingBy((p)->p.getSendPickDetail().getDeliveryId()));

		
		//更新发货明细
		for(SendPickDetailVo pickDetialVo:sendPickVo.getSendPickDetailVoList()){
			SendPickDetail pickDetail = pickDetialVo.getSendPickDetail();	
			if(StringUtil.isNotEmpty(pickDetail.getDeliveryDetailId())){
				//查询发货单明细
				SendDeliveryDetail deliveryDetail = deliveryDetailDao.selectByPrimaryKey(pickDetail.getDeliveryDetailId());
				//更新发货明细的拣货数量，重量，体积
				deliveryDetail.calPickQty(pickDetail.getPickQty());
				deliveryDetail.calPickWeight(pickDetail.getPickWeight());
				deliveryDetail.calPickVolume(pickDetail.getPickVolume());
				deliveryDetail.setUpdatePerson(userId);
				deliveryDetail.setUpdateTime(new Date());
				deliveryDetailDao.updateByPrimaryKeySelective(deliveryDetail);	
			}			
		}
		//更新发货单
		for(String deliveryId:map.keySet()){
			if(map.get(deliveryId) == null || map.get(deliveryId).isEmpty()) continue;
			SendDelivery delivery = deliveryDao.selectByPrimaryKey(deliveryId);
			List<SendPickDetailVo> list = map.get(deliveryId);
			double qty = list.stream().mapToDouble(t->t.getSendPickDetail().getPickQty()).sum();
			Double weight = list.stream().mapToDouble(t->t.getSendPickDetail().getPickWeight()).sum();
			Double volume = list.stream().mapToDouble(t->t.getSendPickDetail().getPickVolume()).sum();
			//更新发货单拣货数量
			delivery.calPickQty(qty);
			delivery.calPickdWeight(weight);
			delivery.calPickVolume(volume);
			delivery.setUpdatePerson(userId);
			delivery.setUpdateTime(new Date());
			//是否完成拣货,若全部拣货，发货单状态由“生效”更改为“已拣货”,若没有拣完货，发货单状态由“生效”更改为“部分拣货”
			if(delivery.getOrderQty().equals(delivery.getPickQty())){
				delivery.setDeliveryStatus(Constant.SEND_STATUS_ALLPICK);
			}else{
				delivery.setDeliveryStatus(Constant.SEND_STATUS_PARTPICK);
			}
			deliveryDao.updateByPrimaryKeySelective(delivery);
		}
	}
	
	/**
	 * 登记到企业业务统计
	 * @param deliveryVo
	 * @throws Exception
	 * @version 2017年3月11日 上午11:11:28<br/>
	 * @author Aaron He<br/>
	 */
	public void addOrgBusiStas(SendDeliveryVo deliveryVo,String operator) throws Exception{
		
		//查询出租企业
		ParkRentVo parkRentVo = new ParkRentVo();
		parkRentVo.getParkRent().setMerchantId(deliveryVo.getSendDelivery().getOrgId());//承租方
		parkRentVo.getParkRent().setWarehouseId(deliveryVo.getSendDelivery().getWarehouseId());
		parkRentVo.setOrderTime(new Date());
		List<ParkRentVo> rentList = parkRentService.qryList(parkRentVo);
		ParkRentVo rentVo = new ParkRentVo();
		if(rentList != null && !rentList.isEmpty()){
			rentVo = rentList.get(0);
		}
		//登记企业业务统计
		ParkOrgBusiStasVo stasVo = new ParkOrgBusiStasVo();
		ParkOrgBusiStas entity = new ParkOrgBusiStas();
		entity.setBusiStasId(IdUtil.getUUID());
		entity.setBusiType(deliveryVo.getSendDelivery().getDocType());
		entity.setDocNo(deliveryVo.getSendDelivery().getDeliveryNo());
		entity.setMerchantId(deliveryVo.getSendDelivery().getOrgId());//承租方
		entity.setOrgId(rentVo.getParkRent().getOrgId());//出租方
		entity.setStasQty(deliveryVo.getSendDelivery().getPickQty());
		entity.setStasWeight(deliveryVo.getSendDelivery().getPickWeight());
		entity.setStasVolume(deliveryVo.getSendDelivery().getPickVolume());
		entity.setStasDate(new Date());
		entity.setCreatePerson(operator);
		entity.setUpdatePerson(operator);
		
		stasVo.setParkOrgBusiStas(entity);		
		parkOrgBusiStasService.add(stasVo);
	}
	
	/**
	 * 根据波次单id更新状态
	 * @param param
	 * @version 2017年3月2日 下午5:46:45<br/>
	 * @author Aaron He<br/>
	 */
	public void updateStatusByParam(int status,String operator,SendDeliveryVo param)throws Exception{
		SendDelivery record = new SendDelivery();
		record.setDeliveryStatus(status);
		record.setUpdatePerson(operator);
		record.setUpdateTime(new Date());
		deliveryDao.updateByExampleSelective(record,param.getCondition(0).getExample());
	}
	
	/**
	 * 更新发货单状态
	 * @param deliveryId
	 * @param operator
	 * @return
	 * @version 2017年2月15日 下午6:05:32<br/>
	 * @author Aaron He<br/>
	 */
	public int updateStatus(String deliveryId,String operator,int status){
		SendDelivery entity = new SendDelivery();
		entity.setDeliveryId(deliveryId);
		entity.setDeliveryStatus(status);
		entity.setUpdatePerson(operator);
		entity.setUpdateTime(new Date());	
		int num = deliveryDao.updateByPrimaryKeySelective(entity);
		return num;
	}
	
	
	/**
	 * 按条件查询关联了该波次单与没有关联波次单的发货单列表
	 * @param sdVo
	 * @return
	 * @version 2017年3月1日 下午1:43:48<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryListInWave(SendDeliveryVo param){
		//若波次单新增，则按条件查询没有关联波次单的发货单列表
		if(StringUtil.isTrimEmpty(param.getSendDelivery().getWaveId())){
			param.setWaveIsNull(true);
			param.getCondition(0);
		}
		//若波次单修改，则查询符合条件的没有关联波次单的发货单与符合条件的该波次单的发货单
		else{
			//查询符合条件的该波次单的发货单
			param.getCondition(0);
			//按条件查询没有关联波次单的发货单列表
			param.getSendDelivery().setWaveId("");
			param.setWaveIsNull(true);
			param.or().getCondition(1);
		}	
		List<SendDelivery> list = deliveryDao.selectByExample(param.getExample());
		
		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		list.stream().forEach(t->{
			SendDeliveryVo vo = new SendDeliveryVo();
			vo.setSendDelivery(t);
			//查询发货方，
			vo.setSenderName(FqDataUtils.getMerchantNameById(merchantService, t.getSender()));			
			//收货方
			vo.setReceiverName(FqDataUtils.getMerchantNameById(merchantService, t.getReceiver()));	
			//查询货主
			vo.setOwnerName(FqDataUtils.getMerchantNameById(merchantService,t.getOwner()));
			
			results.add(vo);
		});
		return results;
	}
	
	/**
	 * 查询没有完成拣货，且没有关联波次单的发货单列表
	 * 1、查询所有由发货单发起，打开，生效的,工作中的拣货单
	 * 2、查询状态是部分拣货，部分发运，生效，全部发运，并且没有关联波次单的发货单
	 * @param orgId
	 * @param warehouseId
	 * @version 2017年3月7日 上午10:39:02<br/>
	 * @author Aaron He<br/>
	 * 
	 */
	public List<SendDeliveryVo> qryListNotFinish(String orgId,String warehouseId){
		
		//1、查询所有由发货单发起，打开，生效的拣货单
		SendPickVo sendPickVo = new SendPickVo();
		sendPickVo.getSendPick().setOrgId(orgId);
		sendPickVo.getSendPick().setWarehouseId(warehouseId);
		sendPickVo.setDeliveryIsNotNull(true);
		sendPickVo.setPickStatusList(Arrays.asList(Constant.PICK_STATUS_OPEN,Constant.PICK_STATUS_ACTIVE));
		List<SendPickVo> pickList = pickService.qryListByParam(sendPickVo);
		List<String> dpIds = null;
		if(pickList != null && !pickList.isEmpty()){
			dpIds = pickList.stream().map(t->t.getSendPick().getDeliveryId()).collect(Collectors.toList());
		}		
		//2、查询状态是部分拣货，部分发运，生效，并且没有关联波次单的发货单		
		SendDeliveryVo param = new SendDeliveryVo();
		param.getSendDelivery().setOrgId(orgId);
		param.getSendDelivery().setWarehouseId(warehouseId);
//		param.setDeliveryStatusList(Arrays.asList(Constant.SEND_STATUS_PARTPICK,Constant.SEND_STATUS_PARTSHIP,Constant.SEND_STATUS_ACTIVE,Constant.SEND_STATUS_ALLSHIP));
		param.setDeliveryStatusList(Arrays.asList(Constant.SEND_STATUS_PARTPICK,Constant.SEND_STATUS_ACTIVE));
		param.setWaveIsNull(true);
		param.setHasAllPick(true);
		param.setNotInDeliveryIdList(dpIds);//过滤生效发货单中已生效拣货单的发货单		
		List<SendDelivery> list = deliveryDao.selectByExample(param.getCondition(0).getExample());
		
		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		list.stream().forEach(t->{
			SendDeliveryVo vo = new SendDeliveryVo();
			vo.setSendDelivery(t);
			//查询货主
			vo.setOwnerName(FqDataUtils.getMerchantNameById(merchantService,t.getOwner()));	
			results.add(vo);
		});
		return results;
	}
	
	/**
	 * 根据id列表批量查询发货单
	 * @param idList
	 * @return
	 * @version 2017年3月10日 下午5:48:05<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryVo> qryByIds(List<String> idList) throws Exception{
		if(idList == null || idList.isEmpty()) return null;
		SendDeliveryVo param = new SendDeliveryVo();
		param.getCriteria(0).andIn("deliveryId", idList);
		List<SendDelivery> list = deliveryDao.selectByExample(param.getExample());
		//补充扩展信息
		List<SendDeliveryVo> results = new ArrayList<SendDeliveryVo>();
		for (SendDelivery entity : list) {
			SendDeliveryVo vo = new SendDeliveryVo();
			vo.setSendDelivery(entity);
			//查询辅材
			vo = qryMaterialList(vo);
			//查询寄件人信息
			vo.setConsignor(FqDataUtils.getMerchantById(merchantService, entity.getConsignorId()));
			results.add(vo);
		}
		return results;
	}
	
	/**
	 * 查找发货单辅材明细
	 * @param deliveryVo
	 * @return
	 * @throws Exception
	 */
	public SendDeliveryVo qryMaterialList(SendDeliveryVo deliveryVo) throws Exception{
		//查询辅材明细
		SendDeliveryMaterialVo deliveryMaterialVo = new SendDeliveryMaterialVo();
		deliveryMaterialVo.getSendDeliveryMaterial().setOrgId(deliveryVo.getSendDelivery().getOrgId());
		deliveryMaterialVo.getSendDeliveryMaterial().setWarehouseId(deliveryVo.getSendDelivery().getWarehouseId());
		deliveryMaterialVo.getSendDeliveryMaterial().setDeliveryId(deliveryVo.getSendDelivery().getDeliveryId());
		//查询发货单辅材
		List<SendDeliveryMaterial> materialList = deliveryMaterialDao.selectByExample(deliveryMaterialVo.getConditionExample());
		if(materialList == null || materialList.isEmpty()) return deliveryVo;
		for(SendDeliveryMaterial sdm:materialList){
			SendDeliveryMaterialVo sdmVo = new SendDeliveryMaterialVo();
			sdmVo.setSendDeliveryMaterial(sdm);
			//根据id查询辅材信息
			MaterialVo materialVo = materialService.get(sdm.getMaterialId());
			sdmVo.setMaterialVo(materialVo);
			deliveryVo.getDeliveryMaterialVoList().add(sdmVo);
		}
		return deliveryVo;
	}
	
	
	/**
	 * 更新波次单id
	 * @param deliveryId
	 * @param waveId
	 * @version 2017年3月1日 下午4:32:40<br/>
	 * @author Aaron He<br/>
	 */
	public void updateWaveId(SendDelivery entity){
		deliveryDao.updateWaveId(entity);
	}
	
	/**
	 * 批量更新波次单号
	 * @param waveId
	 * @param deliveryIds
	 * @param status
	 * @version 2017年3月1日 下午5:33:39<br/>
	 * @author Aaron He<br/>
	 */
	public void batchUpdateWaveId(String waveId,List<String> deliveryIds,int deliveryStatus,String operator)throws Exception{
		deliveryDao.batchUpdateWaveId(waveId, deliveryIds, deliveryStatus,operator,new Date());
	}
	
	/**
	 * 删除波次单号
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月2日 下午1:31:01<br/>
	 * @author Aaron He<br/>
	 */
	public void delWaveId(String waveId,String orgId,String warehouseId,String operator) throws Exception{
		deliveryDao.delWaveId(waveId, orgId, warehouseId,Constant.SEND_STATUS_OPEN, operator);
	}
	
	/**
	 * 查询发货单状态
	 * @param deliveryId
	 * @param status
	 * @return
	 * @version 2017年2月14日 下午3:17:26<br/>
	 * @author Aaron He<br/>
	 */
	@Transactional(rollbackFor=Exception.class)
	public int qryDeliveryStatus(String deliveryId){
		if(StringUtils.isEmpty(deliveryId)) 
			throw new BizException(BizStatus.DELIVERY_ID_IS_NULL.getReasonPhrase());
		
		SendDelivery record = deliveryDao.selectByPrimaryKey(deliveryId);
		
		if(record == null) 
			throw new BizException(BizStatus.DELIVERY_IS_NOT_EXIT.getReasonPhrase());		
		return record.getDeliveryStatus().intValue();
	}
	
	/**
	 * 是否关联波次单
	 * @param deliveryId
	 * @return
	 * @version 2017年2月14日 下午3:52:42<br/>
	 * @author Aaron He<br/>
	 */
	public boolean isAssociateWave(String deliveryId){
		if(StringUtils.isEmpty(deliveryId)) return true;
		
		SendDelivery record = deliveryDao.selectByPrimaryKey(deliveryId);		
		if(record == null) return true;
		//没有关联波次单
		if(StringUtils.isEmpty(record.getWaveId())) return false;
		
		return true;
		
	}
	
	/**
	 * 根据单号查询
	 * @param deliveryNo
	 * @version 2017年3月7日 下午6:34:25<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDelivery> qryByDeliveryNo(String deliveryNo,String orgId,String warehouseId){
		SendDeliveryVo param = new SendDeliveryVo();
		param.setDeliveryNoLike(deliveryNo);
		param.getSendDelivery().setOrgId(orgId);
		param.getSendDelivery().setWarehouseId(warehouseId);
		
		List<SendDelivery> list = deliveryDao.selectByExample(param.getCondition(0).getExample());
		if(list == null || list.isEmpty()) return null;
		return list; 
	}
	
	/**
	 * 根据发货明细id查询实际拣货库位
	 * @param detailId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public List<SendPickLocationVo> qryRealLocationByDetailId(String detailId,String orgId,String warehouseId) throws Exception{
		//根据发货明细查询拣货明细
		SendPickDetailVo param = new SendPickDetailVo();
		param.getSendPickDetail().setDeliveryDetailId(detailId);
		param.getSendPickDetail().setOrgId(orgId);
		param.getSendPickDetail().setWarehouseId(warehouseId);
		List<SendPickDetailVo> list = pickDetailService.qryDetailsAndLocation(param,Constant.PICK_TYPE_REAL);
		List<SendPickLocationVo> allLocations = new ArrayList<SendPickLocationVo>();
		if(list != null && !list.isEmpty()){
			for(SendPickDetailVo pdVo:list){
				allLocations.addAll(pdVo.getRealPickLocations());
			}
		}
		return allLocations;
	}
	
	/**
	 * 根据发货明细id查询计划拣货库位
	 * @param detailId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public List<SendPickLocationVo> qryPlanLocationByDetailId(String detailId,String orgId,String warehouseId) throws Exception{
		//根据发货明细查询拣货明细
		SendPickDetailVo param = new SendPickDetailVo();
		param.getSendPickDetail().setDeliveryDetailId(detailId);
		param.getSendPickDetail().setOrgId(orgId);
		param.getSendPickDetail().setWarehouseId(warehouseId);
		List<SendPickDetailVo> list = pickDetailService.qryDetailsAndLocation(param,Constant.PICK_TYPE_PLAN);
		List<SendPickLocationVo> allLocations = new ArrayList<SendPickLocationVo>();
		if(list != null && !list.isEmpty()){
			for(SendPickDetailVo pdVo:list){
				allLocations.addAll(pdVo.getRealPickLocations());
			}
		}
		return allLocations;
	}
	
	/**
	 * 检查状态
	 * @param deliveryId
	 * @param status
	 * @param msg
	 * @version 2017年3月1日 下午7:55:42<br/>
	 * @author Aaron He<br/>
	 */
	public void checkStatus(String deliveryId,int status,String msg){
		int sta = qryDeliveryStatus(deliveryId);
		if(status != sta)
			throw new BizException(msg);
	}
	
	/**
	 * 新增异常
	 * @param exceptionType
	 * @param involveBill
	 * @param exceptionStatus
	 * @param exceptionDesc
	 * @throws Exception
	 * @version 2017年3月6日 上午10:46:23<br/>
	 * @author Aaron He<br/>
	 */
	public void newExceptionLog(int exceptionType,String involveBill,int exceptionStatus,int excLevel,String exceptionDesc)throws Exception{
		ExceptionLog exceptionLog = new ExceptionLog();
		exceptionLog.setExType(exceptionType);
		exceptionLog.setInvolveBill(involveBill);
		exceptionLog.setExStatus(exceptionStatus);
		exceptionLog.setExDesc(exceptionDesc);
		exceptionLog.setExLevel(excLevel);
		exceptionLogService.add(exceptionLog);		
	}

	/**
	 * 创建拣货单
	 * @param id
	 * @return
	 * @version 2017年3月3日 下午4:03:50<br/>
	 * @author Aaron He<br/>
	 * @throws Exception 
	 */
	public SendPickVo createPick(String id, String operator) throws Exception {
		if(StringUtil.isTrimEmpty(id)) return null;
		//查询发货单
		SendDeliveryVo deliveryVo =getDeliveryById(id);		
		//自动生成拣货单
		SendPickVo pickVo = new SendPickVo();
		pickVo.createPickFromDelivery(deliveryVo, operator);
		return pickVo;
	}
	
	/**
	 * 检查发货单状态并获取发货单
	 * @param id
	 * @param isStatus
	 * @return
	 */
	public SendDeliveryVo checkStatusAndgetDelivery(SendDeliveryVo sendDeliveryVo,Integer isStatus) throws Exception{
		List<SendDelivery> list = qryByDeliveryNo(sendDeliveryVo.getSendDelivery().getDeliveryNo(),
				sendDeliveryVo.getSendDelivery().getOrgId(),
				sendDeliveryVo.getSendDelivery().getWarehouseId());
		if(list == null || list.size() == 0) return null;
		SendDelivery delivery = list.get(0);
		if(delivery.getDeliveryStatus().intValue() != isStatus.intValue()){
			throw new BizException(BizStatus.DELIVERY_STATUS_IS_NOT_ALLPICK.getReasonPhrase());
		}
		List<DeliveryDetailVo> detailVoList = getDetailsInfoBydeliveryId(delivery.getDeliveryId());
		SendDeliveryVo deliveryVo = new SendDeliveryVo();
		deliveryVo.setSendDelivery(delivery);
		deliveryVo.setDeliveryDetailVoList(detailVoList);
		return deliveryVo;
	}
	
	/**
	 * 打包复核
	 * @param deliveryVo
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void reviewAndPackage(SendDeliveryVo deliveryVo,String userId) throws Exception{
		//检查复核数量与订单数量是否一致
		if(deliveryVo.getDeliveryDetailVoList() != null && deliveryVo.getDeliveryDetailVoList().size() != 0){
			for(DeliveryDetailVo detailVo:deliveryVo.getDeliveryDetailVoList()){
				if(detailVo.getDeliveryDetail().getReviewQty().doubleValue() != detailVo.getDeliveryDetail().getOrderQty().doubleValue()){
					throw new BizException(BizStatus.REVIEW_QTY_NOT_EQUAL_ORDER_QTY.getReasonPhrase());
				}
			}
		}
		//打包
		packaged(deliveryVo,userId);
	}
	
	/**
	 * 打包
	 * @param deliveryVo
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void packaged(SendDeliveryVo deliveryVo,String userId) throws Exception{
		//保存辅材列表
		if(deliveryVo.getDeliveryMaterialVoList() == null || deliveryVo.getDeliveryMaterialVoList().size() == 0){
			throw new BizException(BizStatus.MATERIAL_IS_NULL.getReasonPhrase());
		}
		double grossWeight = 0.0;
		for (DeliveryDetailVo ddv : deliveryVo.getDeliveryDetailVoList()) {
			//累加毛重
			grossWeight = NumberUtil.add(grossWeight, NumberUtil.mul((ddv.getSku().getPerWeight() == null ? 0 : ddv.getSku().getPerWeight()), ddv.getDeliveryDetail().getOrderQty(), 4));
		}
		for(SendDeliveryMaterialVo sdmVo:deliveryVo.getDeliveryMaterialVoList()){
			sdmVo.getSendDeliveryMaterial().setOrgId(deliveryVo.getSendDelivery().getOrgId());
			sdmVo.getSendDeliveryMaterial().setWarehouseId(deliveryVo.getSendDelivery().getWarehouseId());
			sdmVo.getSendDeliveryMaterial().setCreatePerson(userId);
			sdmVo.getSendDeliveryMaterial().setUpdatePerson(userId);
			if (sdmVo.getSendDeliveryMaterial().getMaterialId() == null) {
				String materialId = materialService.getIdByNo(sdmVo.getMaterialVo().getEntity().getMaterialNo());
				if (materialId != null) {
					sdmVo.getSendDeliveryMaterial().setMaterialId(materialId);
				} else {
					throw new BizException(BizStatus.MATERIAL_IS_NULL.getReasonPhrase());
				}
			}
			sdmVo.getSendDeliveryMaterial().setDeliveryId(deliveryVo.getSendDelivery().getDeliveryId());
			deliveryMaterialDao.insertSelective(sdmVo.getSendDeliveryMaterial());
			
			//扣减相应辅材
			MaterialVo materialVo = new MaterialVo();
			MetaMaterial entity = new MetaMaterial();
			entity.setMaterialId(sdmVo.getSendDeliveryMaterial().getMaterialId());
			materialVo.setChangeQty(-sdmVo.getSendDeliveryMaterial().getQty());
			MetaMaterialLog log = new MetaMaterialLog();
			log.setMaterialId(sdmVo.getSendDeliveryMaterial().getMaterialId());
			log.setInvolveBill(deliveryVo.getSendDelivery().getDeliveryNo());
			log.setMaterialLogType(Constant.MATERIAL_LOG_TYPE_PACKAGE);
			log.setNote(deliveryVo.getSendDelivery().getNote());
			materialVo.setEntity(entity);
			materialVo.setMaterialLog(log);
			materialService.changeQty(materialVo);
			//累加毛重
			grossWeight = NumberUtil.add(grossWeight, NumberUtil.mul((sdmVo.getMaterialVo().getEntity().getPerWeight() == null ? 0 : sdmVo.getMaterialVo().getEntity().getPerWeight()), sdmVo.getSendDeliveryMaterial().getQty(), 4));
		}
		//修改状态为打包
		SendDelivery param = new SendDelivery();
		param.setDeliveryId(deliveryVo.getSendDelivery().getDeliveryId());
		param.setGrossWeight(grossWeight);
		param.setIsPackage(Constant.PACKAGE_HAS);
		param.setCreatePerson(userId);
		param.setUpdatePerson(userId);
		param.setDeliveryStatus(Constant.SEND_STATUS_PACKAGE);
		param.setUpdateTime(new Date());
		deliveryDao.updateByPrimaryKeySelective(param);
		
		//增加操作日志
		deliveryLogService.addNewDeliveryLog(deliveryVo.getSendDelivery().getDeliveryId(),
				userId, Constant.DELIVERY_LOG_TYPE_CHECKPACKET, 
				deliveryVo.getSendDelivery().getOrgId(),
				deliveryVo.getSendDelivery().getWarehouseId());
		
	}
	
	/**
	 * 导入发货单
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void importDeliveryToInterface(SendDelivery2ExternalVo sendDeliveryVo2)throws Exception{
		SendDeliveryVo sendDeliveryVo = DeliveryUtils.parse(sendDeliveryVo2);
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//根据货品代码查询货品
		for(DeliveryDetailVo detailVo:sendDeliveryVo.getDeliveryDetailVoList()){
			if(StringUtil.isTrimEmpty(detailVo.getSkuNo())){
				throw new BizException(BizStatus.DELIVERY_DETAIL_SKUNO_IS_NULL.getReasonPhrase());
			}
			MetaSku skuParam = new MetaSku();
			skuParam.setSkuNo(detailVo.getSkuNo());
			skuParam.setOrgId(orgId);
			skuParam.setWarehouseId(wareHouseId);
			MetaSku sku = skuService.query(skuParam);
			if(sku == null) 
				throw new BizException(BizStatus.SKU_IS_NOT_EXISTS.getReasonPhrase());
			detailVo.getDeliveryDetail().setSkuId(sku.getSkuId());
		}
		//数据类型默认是普通出库
		if(sendDeliveryVo.getSendDelivery().getDocType() == null){
			sendDeliveryVo.getSendDelivery().setDocType(Constant.DELIVERY_TYPE_NORMAL_OUT);
		}
		//保价费默认为0
		sendDeliveryVo.getSendDelivery().setInsuranceCharge(0D);
		//若没有寄件人，寄件人默人是仓库的配置的
		MetaWarehouseSettingVo settingVo = warehouseSettingService.findByWarehouseId(wareHouseId);
		if(settingVo != null && StringUtil.isBlank(sendDeliveryVo.getSendDelivery().getConsignor())
				|| StringUtil.isBlank(sendDeliveryVo.getSendDelivery().getConsignorAddress())
				|| StringUtil.isBlank(sendDeliveryVo.getSendDelivery().getConsignorPhone())){
			sendDeliveryVo.getSendDelivery().setConsignor(settingVo.getEntity().getConsignor());
			sendDeliveryVo.getSendDelivery().setConsignorId(settingVo.getEntity().getConsignorId());
			sendDeliveryVo.getSendDelivery().setConsignorAddress(settingVo.getEntity().getConsignorAddress());
			sendDeliveryVo.getSendDelivery().setConsignorPhone(settingVo.getEntity().getConsignorPhone());
			MetaMerchant merchant = merchantService.get(settingVo.getEntity().getConsignorId());
//			if(merchant == null){
//				throw new BizException("Consignor_is_null");
//			}
			if(merchant != null) {
				sendDeliveryVo.getSendDelivery().setSendProvince(merchant.getProvince());
				sendDeliveryVo.getSendDelivery().setSendCity(merchant.getCity());
				sendDeliveryVo.getSendDelivery().setSendCounty(merchant.getCounty());
			}
		}
		//生成运单号
//		if(StringUtils.isNotBlank(sendDeliveryVo.getSendDelivery().getExpressServiceCode())) {
//			//若接口没送运单号，则从数据库查询运单号
//			if(StringUtils.isBlank(sendDeliveryVo.getSendDelivery().getExpressBillNo())){
//				SysExpressNoPool pool = new SysExpressNoPool();
//				pool.setExpressServiceCode(sendDeliveryVo.getSendDelivery().getExpressServiceCode());
//				pool.setIsUsed(false);
//				String billNo = expressPoolService.selectOne(pool);
//				sendDeliveryVo.getSendDelivery().setExpressBillNo(billNo);
//			}
//		}
		

		sendDeliveryVo = createNoAndAdd(sendDeliveryVo, orgId, wareHouseId, userId);
		
		//获取仓库
//		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(wareHouseId);
//		if(sendDeliveryVo2.getApplicationVo() != null &&
//				warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
//			//若是普通仓，则自动生成申请单
//			sendDeliveryVo2.getApplicationVo().getEntity().setDeliveryId(sendDeliveryVo.getSendDelivery().getDeliveryId());
//			DeliverGoodsApplicationVo applicationVo = addApplicationVo(sendDeliveryVo2.getApplicationVo());
//
//			//更新发货单的申请单号
//			SendDelivery delivery = new SendDelivery();
//			delivery.setDeliveryId(sendDeliveryVo.getSendDelivery().getDeliveryId());
//			delivery.setGatejobNo(applicationVo.getEntity().getApplicationNo());
//			deliveryDao.updateByPrimaryKeySelective(delivery);
//		}


	}
	
	/**
	 * 自动生成申请单
	 * @param applicationVo
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationVo addApplicationVo(DeliverGoodsApplicationVo applicationVo) throws Exception{
		Principal p = LoginUtil.getLoginUser();
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			String o_flag = "E";//paramService.getKey(CacheName.APPLY_E_FLAG);
			applicationVo.getEntity().setiEFlag(o_flag);
			//查询skuId
			if(applicationVo.getApplicationGoodVoList() != null && !applicationVo.getApplicationGoodVoList().isEmpty()){
				for(DeliverGoodsApplicationGoodVo goodVo:applicationVo.getApplicationGoodVoList()){
					if(goodVo.getApplicationGoodSkuVoList() != null && !goodVo.getApplicationGoodSkuVoList().isEmpty()){
						for(DeliverGoodsApplicationGoodsSkuVo goodSkuVo:goodVo.getApplicationGoodSkuVoList()){
							MetaSku p_sku = new MetaSku();
							p_sku.setHgGoodsNo(goodSkuVo.getEntity().getHgGoodsNo());
							p_sku.setOrgId(p.getOrgId());
							p_sku.setWarehouseId(LoginUtil.getWareHouseId());
							MetaSku sku = skuService.query(p_sku);
							goodSkuVo.getEntity().setSkuId(sku.getSkuId());
						}//for
					}//if
				}//for
			}//if
			applicationVo = deliverGoodsApplicationService.add(applicationVo, p);
		}
		return applicationVo;
	}
	
	/**
	 * 取消订单
	 * @param deliveryId
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String,String> cancelToInterface(String srcNo,String cancelFlag) throws Exception{
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		//获取发货单
		SendDelivery sendDelivery = getBySrcNo(srcNo,loginUser);
		Map<String,String> result = null;
		//订单拦截
		if(Constant.CANCEL_DELIVERY.equals(cancelFlag)){
			result = interceptHandle(srcNo);
		}
		//取消订单拦截
		else if(Constant.CANCEL_INTERCEPT.equals(cancelFlag)){
			cancelIntecept(sendDelivery,userId);
		}
		return result;
	}
	
	/**
	 * 根据订单号查询发货单
	 * @param srcNo
	 * @param loginUser
	 * @return
	 */
	public SendDelivery getBySrcNo(String srcNo,Principal loginUser){
		//获取发货单
		SendDeliveryVo paramVo = new SendDeliveryVo();
		paramVo.getSendDelivery().setSrcNo(srcNo);
		paramVo.getSendDelivery().setOrgId(loginUser.getOrgId());
		paramVo.getSendDelivery().setWarehouseId(LoginUtil.getWareHouseId());
		List<SendDelivery> list = deliveryDao.selectByExample(paramVo.getCondition(0).getExample());
		if(list == null || list.size() ==0)
			throw new BizException(BizStatus.DELIVERY_IS_NOT_EXIT.getReasonPhrase());
		SendDelivery sendDelivery = list.get(0);
		return sendDelivery;
	}
	
	/**
	 * 根据运单号查询发货单
	 * @param expressBillNo
	 * @return
	 */
	public SendDelivery getByExpressBillNo(String expressBillNo){
		SendDeliveryVo param = new SendDeliveryVo();
		param.getSendDelivery().setExpressBillNo(expressBillNo);
		List<SendDelivery> list = deliveryDao.selectByExample(param.getCondition(0).getExample());
		if(list == null || list.size() ==0){
			throw new BizException(BizStatus.DELIVERY_IS_NOT_EXIT.getReasonPhrase());
		}
		SendDelivery sendDelivery = list.get(0);
		return sendDelivery;
	}
	
	/**
	 * 取消拦截
	 * @param deliveryId
	 * @param operator
	 */
	public void cancelIntecept(SendDelivery record,String operator){
		//判断是否拦截成功状态或截留状态
		if(!Constant.INTERCEPT_HOLD_OFF.equals(record.getInterceptStatus()) &&
				!Constant.INTERCEPT_TRAP.equals(record.getInterceptStatus())){
			throw new BizException("INTERCEPT_CANCEL_ERR");
		}
		record.setInterceptStatus(null);
		record.setUpdatePerson(operator);
		record.setUpdateTime(new Date());
		deliveryDao.updateByPrimaryKey(record);
	}
	
	/**
	 * 查询发货单状态
	 * @param srcNo
	 * @param status
	 * @return
	 * @version 2017年2月14日 下午3:17:26<br/>
	 * @author Aaron He<br/>
	 */
	public Map<String,String> qryStatus(String srcNo){
		SendDeliveryVo paramVo = new SendDeliveryVo();
		paramVo.getSendDelivery().setSrcNo(srcNo);
		List<SendDelivery> list = deliveryDao.selectByExample(paramVo.getCondition(0).getExample());
		if(list == null || list.size() ==0)
			throw new BizException(BizStatus.DELIVERY_IS_NOT_EXIT.getReasonPhrase());
		SendDelivery sendDelivery = list.get(0);
		Map<String,String> statusMap = new HashMap<String,String>();
		statusMap.put("deliveryStatus", sendDelivery.getDeliveryStatus()+"");
		statusMap.put("deliveryStatusName", paramService.getValue(CacheName.SEND_STATUS, sendDelivery.getDeliveryStatus()));
		statusMap.put("InterceptComment", paramService.getValue(CacheName.SEND_INTERCEPT_STATUS, sendDelivery.getInterceptStatus()));
		
		return statusMap;
	}
	
	/**
	 * 发货确认
	 * @param deliveryIdList
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo confirmSend(String expressBillNo,String orgId,String wareHouseId,String userId) throws Exception{
		if(StringUtil.isTrimEmpty(expressBillNo)) 
			throw new BizException(BizStatus.DELIVERY_ID_IS_NULL.getReasonPhrase());
		//根据运单号查询发货单
		SendDelivery sendDelivery = getByExpressBillNo(expressBillNo);
		//检查发货单状态
		checkBeforeConfirm(sendDelivery);
		
		sendDelivery.setDeliveryStatus(Constant.SEND_STATUS_ALLSHIP);
		sendDelivery.setUpdatePerson(userId);
		sendDelivery.setUpdateTime(new Date());
		sendDelivery.setShipmentTime(new Date());
		sendDelivery.setIsSendScan(Constant.SCAN_HAS);
		deliveryDao.updateByPrimaryKeySelective(sendDelivery);
		SendDeliveryVo sendDeliveryVo = new SendDeliveryVo();
		sendDeliveryVo.setSendDelivery(sendDelivery);
		
		//从发货区移除货品数量
		stockService.outStock(sendDelivery.getDeliveryId());
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(sendDelivery.getDeliveryId(),
				userId,Constant.DELIVERY_LOG_TYPE_SHIP,orgId,wareHouseId);
		//查询辅材
		sendDeliveryVo = qryMaterialList(sendDeliveryVo);
		//运单号发送物流公司及erp
//		send2ErpAndSaveStatus(sendDeliveryVo);
		
		return sendDeliveryVo;
	}
	
	/**
	 * 确认发货前检查发货单状态
	 * @param sendDelivery
	 * @throws Exception
	 */
	public void checkBeforeConfirm(SendDelivery sendDelivery) throws Exception{
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		//若是普通仓，订单是爆款类型，则完成拣货就可出库
		if(Constant.WAREHOUSE_TYPE_NOTCUSTOMS == warehouseVo.getWarehouse().getWarehouseType()
				&& Constant.DELIVERY_TYPE_BURST_OUT == sendDelivery.getDocType()){
			if(Constant.SEND_STATUS_ALLPICK != sendDelivery.getDeliveryStatus()){
				throw new BizException("delivery_status_is_not_allpick");
			}
		}
		//其他类型出库，需检查是否已称重或已查验
		else{
			//已称重或已查验才能发货
			if(Constant.SEND_STATUS_CHECKWEIGHT != sendDelivery.getDeliveryStatus().intValue() 
					&& Constant.SEND_STATUS_CHECK != sendDelivery.getDeliveryStatus().intValue()){
				throw new BizException("DELIVERY_STATUS_IS_NOT_CHECKWEIGHT_OR_CHECK");
			}
		}
		
	}
	
	/**
	 * 退单
	 * @param sendDeliveryVo
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void chargeBack(SendDeliveryVo sendDeliveryVo,String userId) throws Exception{
		//获取发货单
		SendDeliveryVo deliveryVo = view(sendDeliveryVo.getSendDelivery().getDeliveryId());
		
		//若发货单的状态是已打包复核或全部拣货或查验中，则做移位处理
		if(deliveryVo.getSendDelivery().getDeliveryStatus().intValue() == Constant.SEND_STATUS_ALLPICK
				|| deliveryVo.getSendDelivery().getDeliveryStatus().intValue() == Constant.SEND_STATUS_CHECK
				|| deliveryVo.getSendDelivery().getDeliveryStatus().intValue() == Constant.SEND_STATUS_PACKAGE){
			stockService.shiftToPick(sendDeliveryVo.getSendDelivery().getDeliveryId());
		}
			
			//查找拣货单，按拣货库位生成退货移位单
//			SendPickVo pickParam = new SendPickVo();
//			pickParam.getSendPick().setDeliveryId(sendDeliveryVo.getSendDelivery().getDeliveryId());
//			pickParam.getSendPick().setOrgId(sendDeliveryVo.getSendDelivery().getOrgId());
//			pickParam.getSendPick().setWarehouseId(sendDeliveryVo.getSendDelivery().getWarehouseId());
//			List<SendPick> pickList = pickDao.selectByExample(pickParam.getConditionExample());
			
//			if(pickList != null && pickList.size() > 0){
//				SendPick pick = pickList.get(0);
//				SendPickVo pickVo = pickService.view(pick.getPickId());
//				for(SendPickDetailVo pickDetailVo:pickVo.getSendPickDetailVoList()){
//					
//					for(SendPickLocationVo locationVo:pickDetailVo.getRealPickLocations()){
						//调用移位服务生成移位单，并保存发货单号
//						InvStockVO stockVo_1 = StockOperate.getInvStockVO(
//								pickDetailVo.getSendPickDetail().getSkuId(),
//								locationVo.getSendPickLocation().getBatchNo(),
//								null, 
//								locationVo.getSendPickLocation().getLocationId(), 
//								null,
//								locationVo.getSendPickLocation().getPickQty(), 
//								null,
//								true,
//								null);
//						InvLog invLog = new InvLog();
//						invLog.setOpPerson(userId);
//						stockVo_1.setInvLog(invLog);
//						stockService.shiftToPick(sendDeliveryVo.getSendDelivery().getDeliveryId());
//					}
//				}
//			}
//		}
		
		//若发货单的状态是已打包，则还需对辅材做移位处理
//		if(deliveryVo.getSendDelivery().getDeliveryStatus().intValue() == Constant.SEND_STATUS_PACKAGE){
//			for(SendDeliveryMaterialVo deliveryMaterialVo:deliveryVo.getDeliveryMaterialVoList()){
//				MaterialVo materialVo = new MaterialVo();
//				MetaMaterial entity = new MetaMaterial();
//				entity.setMaterialId(deliveryMaterialVo.getSendDeliveryMaterial().getMaterialId());
//				materialVo.setResultQty(deliveryMaterialVo.getSendDeliveryMaterial().getQty());
//				MetaMaterialLog log = materialVo.getMaterialLog();
//				log.setMaterialId(deliveryMaterialVo.getSendDeliveryMaterial().getMaterialId());
//				log.setInvolveBill(deliveryVo.getSendDelivery().getDeliveryNo());
//				log.setMaterialLogType(Constant.MATERIAL_LOG_TYPE_PACKAGE);
//				log.setNote(sendDeliveryVo.getSendDelivery().getNote());
//				materialVo.setEntity(entity);
//				materialVo.setMaterialLog(log);
//				materialService.changeQty(materialVo);
//			}
//		}
		//发货单状态变为退单，保存备注原因
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_RETURN);
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		sendDeliveryVo.getSendDelivery().setUpdateTime(new Date());
		deliveryDao.updateByPrimaryKeySelective(sendDeliveryVo.getSendDelivery());
		
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(deliveryVo.getSendDelivery().getDeliveryId(),
				userId,Constant.DELIVERY_LOG_TYPE_RETURN,deliveryVo.getSendDelivery().getOrgId(),
				deliveryVo.getSendDelivery().getWarehouseId());
	}
	
	/**
	 * 查验
	 * @param sendDeliveryVo
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void check(SendDeliveryVo sendDeliveryVo,String userId) throws Exception{
		//获取发货单
		SendDeliveryVo deliveryVo = view(sendDeliveryVo.getSendDelivery().getDeliveryId());
		
		//若发货单的状态是已复核或全部拣货，则做移位处理
		if(deliveryVo.getSendDelivery().getDeliveryStatus().intValue() != Constant.SEND_STATUS_CHECKWEIGHT){
			throw new BizException("valid_delivery_no_checked");
		}
		//发货单状态变为退单，保存备注原因
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_CHECK);
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		sendDeliveryVo.getSendDelivery().setUpdateTime(new Date());
		deliveryDao.updateByPrimaryKeySelective(sendDeliveryVo.getSendDelivery());
		
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(deliveryVo.getSendDelivery().getDeliveryId(),
				userId,Constant.DELIVERY_LOG_TYPE_CHECHING,deliveryVo.getSendDelivery().getOrgId(),
				deliveryVo.getSendDelivery().getWarehouseId());
	}
	
	/**
	 * 销毁
	 * @param sendDeliveryVo
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void destory(SendDeliveryVo sendDeliveryVo,String userId) throws Exception{
		//获取发货单
		SendDeliveryVo deliveryVo = view(sendDeliveryVo.getSendDelivery().getDeliveryId());
		
		//若发货单的状态是已复核或全部拣货，则做移位处理
		if(deliveryVo.getSendDelivery().getDeliveryStatus().intValue() != Constant.SEND_STATUS_CHECK){
			throw new BizException("查验中的发货单才能销毁！");
		}
		//发货单状态变为已销毁，保存备注原因
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_DESTORY);
		sendDeliveryVo.getSendDelivery().setUpdatePerson(userId);
		deliveryDao.updateByPrimaryKeySelective(sendDeliveryVo.getSendDelivery());
		//调用销毁接口，后调用的原因是需要备注字段
		stockService.destory(sendDeliveryVo.getSendDelivery().getDeliveryId());
		
		//新增发货操作日志
		deliveryLogService.addNewDeliveryLog(sendDeliveryVo.getSendDelivery().getDeliveryId(),
				userId,Constant.DELIVERY_LOG_TYPE_DESTROY,deliveryVo.getSendDelivery().getOrgId(),
				deliveryVo.getSendDelivery().getWarehouseId());
	}
	
	/**
	 * 检查快递单号是否重复
	 * @param expressBillNo
	 */
	public void checkExpressNoIsDuplicated(String srcNo,String expressBillNo,String orgId,String wareHouseId) throws Exception{
		if(StringUtil.isTrimEmpty(expressBillNo)) return;
		
		SendDeliveryVo paramVo = new SendDeliveryVo();
		paramVo.getSendDelivery().setExpressBillNo(expressBillNo);
		paramVo.getSendDelivery().setOrgId(orgId);
		paramVo.getSendDelivery().setWarehouseId(wareHouseId);
		
		List<SendDelivery> list = deliveryDao.selectByExample(paramVo.getCondition(0).getExample());
		if(list.size() > 1){
			throw new BizException(BizStatus.EXPRESS_BILLNO_IS_DUPLICATED.getReasonPhrase());
		}else if(list.size() == 1){
			SendDelivery delivery = list.get(0);
			if(!delivery.getSrcNo().equals(srcNo)){
				throw new BizException(BizStatus.EXPRESS_BILLNO_IS_DUPLICATED.getReasonPhrase());
			}
		}
	}
	
	/**
	 * 查询发货单列表
	 * @param sdVo
	 * @param orgId
	 * @param wareHouseId
	 * @return
	 * @throws Exception 
	 */
	@Override
	public ResultModel centralList4Print(SendDeliveryVo sendDeliveryVo) throws Exception {
		
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) return null;
		ResultModel result = new ResultModel();
    	//根据客商名称查询id列表

		//获取货品列表
		List<String> skuIdList = skuService.getSkuIdList(sendDeliveryVo.getMerchantIdList());
		sendDeliveryVo.setMerchantShortName(merchantService.concatMerchantShortName(sendDeliveryVo.getMerchantIdList()));
		//根据修改时间和状态获取发货单列表
		sendDeliveryVo.setMerchantIdList(null);
		List<SendDelivery> deliveryList = deliveryDao.selectByExample(sendDeliveryVo.getCondition(0).getExample());
		List<DeliveryDetailVo> results = new ArrayList<DeliveryDetailVo>();
		for (SendDelivery sd : deliveryList) {
			DeliveryDetailVo tempVo = new DeliveryDetailVo();
			tempVo.setDeliveryDetail(new SendDeliveryDetail());
			tempVo.getDeliveryDetail().setDeliveryId(sd.getDeliveryId());
			tempVo.setSkuIdList(skuIdList);
			List<SendDeliveryDetail> list = deliveryDetailDao.selectByExample(tempVo.getConditionExample());
			for (SendDeliveryDetail detail : list) {
				DeliveryDetailVo t = new DeliveryDetailVo();			
				t.setDeliveryDetail(detail);
				//查询货品
				MetaSku sku = FqDataUtils.getSkuById(skuService, detail.getSkuId());
				if(sku == null) continue;
				t.setSkuName(sku.getSkuName());
				t.setMeasureUnit(sku.getMeasureUnit());
				t.setSkuNo(sku.getSkuNo());
				t.setSpecModel(sku.getSpecModel());
				t.setReceiver(sd.getContactPerson());
				t.setSrcNo(sd.getSrcNo());
				results.add(t);
			}
		}
		sendDeliveryVo.setDeliveryDetailVoList(results);
		result.setObj(sendDeliveryVo);
		return result;
	}
	

	/**
	 * @param string
	 * @param file
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:14:23<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<byte[]> downloadExcel(SendDeliveryVo vo) throws Exception {
		// 导出出库清单  
		ExcelExport<SendDeliveryVo4Excel> ex = new ExcelExport<SendDeliveryVo4Excel>();  
		String[] headers = { 
				"序号", "发货单号", "订单号", 
				"运单号", "订单类型", "单据来源", 
				"订单日期", "订单数量", "订单公斤", 
				"订单立方", "拣选数量", "拣选公斤", 
				"拣选立方", "收货人", "联系方式", 
				"状态", "EMS", "ERP","创建日期","备注"};  
      
		List<SendDeliveryVo4Excel> dataset = vo2excelVo(qryPageList(vo).getList());
		if(dataset == null ||dataset.isEmpty()) throw new BizException("no_data");
		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("出库清单", headers, dataset, "yyyy-MM-dd", "出库清单.xls");
		return bytes;
	}
	
	/**
	 * 出库统计导出
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<byte[]> exportSendStaticsExcel(SendPickLocationVo vo, Principal p) throws Exception{
		//导出出库清单  
		ExcelExport<SendStastics4ExcelVo> ex = new ExcelExport<SendStastics4ExcelVo>(); 
		String[] headers = {"序号","订单日期","订单号","快递运单号","库位号","商品名称","商品条码","货主简称","货品数量","发货数量","作业人员"}; 
		
		//组装统计导出列表
		List<SendStasticsVo> list = qryStatiticsPageList_new(vo,p,false).getList();
		List<SendStastics4ExcelVo> excelVoList = new ArrayList<SendStastics4ExcelVo>();
		int index = 1;
		for(SendStasticsVo staticsVo:list){
			SendStastics4ExcelVo excelVo = new SendStastics4ExcelVo();
			BeanUtils.copyProperties(staticsVo, excelVo);
			excelVo.setIndex(index);
			excelVoList.add(excelVo);
			index++;
		}
		
		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("出库统计", headers, excelVoList, "yyyy-MM-dd", "出库统计.xls");
		return bytes;
	}
	
	/**
	 * 导出订单统计excel
	 * @param vo
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<byte[]> exportSendOrderExcel(SendDeliveryVo vo, Principal p) throws Exception{
		//导出订单统计  
		ExcelExport<SendOrder4ExcelVo> ex = new ExcelExport<SendOrder4ExcelVo>(); 
		String[] headers = {"序号","订单日期","订单号","快递公司","运单号","收件人","联系方式","联系地址"};
		
		vo.getSendDelivery().setOrgId(p.getOrgId());
		vo.getSendDelivery().setWarehouseId(LoginUtil.getWareHouseId());
		//发货单查询没有选就默认查询除取消以外的所有状态
		if(vo.getSendDelivery().getDeliveryStatus() == null){
			vo.setLessThanStatus(Constant.SEND_STATUS_CANCAL);
		}
		//查询订单
		List<SendDeliveryVo> deliveryVoList = qryListByParam(vo); 
		//转换成订单列表
		List<SendOrder4ExcelVo> excelVoList = toOrderExcelVoList(deliveryVoList);
		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("订单统计", headers, excelVoList, "yyyy-MM-dd", "订单统计.xls");
		return bytes;
	}
	
	
	/**
	 * 从发货单列表转换成订单列表
	 * @param list
	 * @return
	 */
	public List<SendOrder4ExcelVo> toOrderExcelVoList(List<SendDeliveryVo> list){
		List<SendOrder4ExcelVo> excelVoList = new ArrayList<SendOrder4ExcelVo>();
		if(list == null || list.isEmpty()) return excelVoList;
		for(int i=0;i<list.size();i++){
			SendDeliveryVo delVo = list.get(i);
			SendOrder4ExcelVo excelVo = new SendOrder4ExcelVo();
			excelVo.setIndex(i+1);
			excelVo.setContactAddress(delVo.getSendDelivery().getContactAddress());
			excelVo.setExpressBillNo(delVo.getSendDelivery().getExpressBillNo());
			excelVo.setExpressServiceName(delVo.getExpressName());
			excelVo.setOrderTime(delVo.getSendDelivery().getOrderTime());
			excelVo.setReceiver(delVo.getSendDelivery().getContactPerson());
			excelVo.setContactPhone(delVo.getSendDelivery().getConsignorPhone());
			excelVo.setSrcNo(delVo.getSendDelivery().getSrcNo());
			excelVoList.add(excelVo);
		}
		return excelVoList;
	}
	
	
	/**
	 * 查询出库统计列表
	 * @param vo
	 * @param p
	 * @param hasPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ResultModel qryStatiticsPageList(SendDeliveryVo vo, Principal p,Boolean hasPage) throws Exception{
		ResultModel result = new ResultModel();
		//1.查询已出库的发货单列表
		vo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_ALLSHIP);
		vo.getSendDelivery().setOrgId(p.getOrgId());
		vo.getSendDelivery().setWarehouseId(LoginUtil.getWareHouseId());
		List<SendDeliveryVo> sendVoList = qryListByParam(vo);
		
		if(sendVoList == null || sendVoList.isEmpty()) return result;
		List<String> deliveryIdList = sendVoList.stream().map(t->t.getSendDelivery().getDeliveryId()).collect(Collectors.toList());
		//Map<发货单id，发货单Vo>
		Map<String,SendDeliveryVo> deliveryMap = new HashMap<String,SendDeliveryVo>();
		for(SendDeliveryVo deVo:sendVoList){
			deliveryMap.put(deVo.getSendDelivery().getDeliveryId(), deVo);
		}
	    
    	//2.根据货主,货品名称，货品代码查找货列表
		List<String> skuIdList = new ArrayList<String>();
		if(StringUtils.isNotEmpty(vo.getOwnerName()) || StringUtils.isNotEmpty(vo.getSkuNo()) || StringUtils.isNotEmpty(vo.getSkuName())){
			SkuVo skuVo = new SkuVo(new MetaSku());
			if(StringUtils.isNotEmpty(vo.getOwnerName())){
				skuVo.setMerchant(new MetaMerchant().setMerchantName(vo.getOwnerName()));
			}
			if(StringUtils.isNotEmpty(vo.getSkuNo())){
				skuVo.setSkuNoLike(vo.getSkuNo());
			}
			if(StringUtils.isNotEmpty(vo.getSkuName())){
				skuVo.setSkuNameLike(vo.getSkuName());
			}
			List<SkuVo> skuVoList = skuService.list(skuVo, p).getList();
			if(skuVoList == null || skuVoList.isEmpty()) return result;
			skuIdList = skuVoList.stream().parallel().map(t->t.getEntity().getSkuId()).collect(Collectors.toList());
		}
		
		//查询发货明细列表
		DeliveryDetailVo detailVo = new DeliveryDetailVo();
		detailVo.setDeliveryIdList(deliveryIdList);
		detailVo.setSkuIdList(skuIdList);
		detailVo.getDeliveryDetail().setOrgId(p.getOrgId());
		detailVo.getDeliveryDetail().setWarehouseId(LoginUtil.getWareHouseId());
		List<SendDeliveryDetail> detailList = qryDetails(detailVo);

		List<String> ddetailList = detailList.stream().map(t->t.getDeliveryDetailId()).collect(Collectors.toList());
 
		//map<发货明细id，发货单Vo>
		Map<String,SendDeliveryVo> sendDetailMap = new HashMap<String,SendDeliveryVo>();
		for(SendDeliveryDetail dedetail:detailList){
			SendDeliveryVo sdVo = deliveryMap.get(dedetail.getDeliveryId());
			sendDetailMap.put(dedetail.getDeliveryDetailId(), sdVo);
		}
		
		//查询拣货明细
		List<SendPickDetail> pickDetailList = pickService.qryPickQtyByDeliveryDetails(ddetailList,p.getOrgId(),LoginUtil.getWareHouseId());
		if(pickDetailList == null || pickDetailList.isEmpty()) return null;
		List<String> pickDetailIds = pickDetailList.stream().parallel().map(t->t.getPickDetailId()).collect(Collectors.toList());
		//转换map<拣货明细id，拣货明细单>
		Map<String,SendPickDetail> detailId2PickVoMap = new HashMap<String,SendPickDetail>();
		//转换map<拣货明细id，发货单vo>
		Map<String,SendDeliveryVo> pickDetail2deliveryVoMap = new HashMap<String,SendDeliveryVo>();
		for(SendPickDetail pickDetail:pickDetailList){
			detailId2PickVoMap.put(pickDetail.getPickDetailId(), pickDetail);
			if(sendDetailMap.containsKey(pickDetail.getDeliveryDetailId())){
				pickDetail2deliveryVoMap.put(pickDetail.getPickDetailId(), sendDetailMap.get(pickDetail.getDeliveryDetailId()));
			}
		}
		
		//查询实际拣货库位列表
		SendPickLocationVo realParam = new SendPickLocationVo();
		//根据库位代码查库位id
		if(StringUtil.isNotEmpty(vo.getLoctionNo())){
			MetaLocation record = locExtlService.findLocByNo(vo.getLoctionNo(), null);
			realParam.getSendPickLocation().setLocationId(record.getLocationId());
		}
		realParam.setPickDetailIds(pickDetailIds);
		realParam.getSendPickLocation().setPickType(Constant.PICK_TYPE_REAL);
		
		//是否用分页查询
		Page<SendPickLocation> page = null;
		if(hasPage){
			
			if ( vo.getCurrentPage() == null ) {
				vo.setCurrentPage(0);
				realParam.setCurrentPage(vo.getCurrentPage());
			}
			if ( vo.getPageSize() == null ) {
				vo.setPageSize(DEFAULT_PAGE_SIZE);
			}
			realParam.setCurrentPage(vo.getCurrentPage());
			realParam.setPageSize(vo.getPageSize());
			page = PageHelper.startPage(realParam.getCurrentPage()+1, realParam.getPageSize());

		}else{
			realParam.retset();
		}
		//查询实际拣货库位列表
		List<SendPickLocationVo> realLocVos = pickLocationService.qryPickLocations(realParam);
		result.setPage(page);
		//组装统计导出列表
		List<SendStasticsVo> staticsList = fromRealLoc2ExportVoList(realLocVos,detailId2PickVoMap,pickDetail2deliveryVoMap);
		result.setList(staticsList);
		return result;
	}
	
	/**
	 * 查询出库统计列表
	 * @param vo
	 * @param p
	 * @param hasPage
	 * @return
	 * @throws Exception
	 */
	public ResultModel qryStatiticsPageList_new(SendPickLocationVo vo, Principal p,Boolean hasPage) throws Exception{
		ResultModel result = new ResultModel();
		vo.setOrgId(p.getOrgId());
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		//是否用分页查询
		Page<SendStasticsVo> page = null;
//		SendPickLocationVo realParam = new SendPickLocationVo();
		if(hasPage){
			if ( vo.getCurrentPage() == null ) {
				vo.setCurrentPage(0);
			}
			if ( vo.getPageSize() == null ) {
				vo.setPageSize(DEFAULT_PAGE_SIZE);
			}
			page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());

		}else{
			vo.retset();
		}
//		else{
//			realParam.retset();
//		}
		List<SendStasticsVo> staticsList = deliveryDao.qryOutStaticsList(vo);
		result.setPage(page);
		result.setList(staticsList);
		return result;
	}
	
	/**
	 * 组装统计导出列表
	 * @param list
	 * @param pd2SendMap
	 * @return
	 * @throws Exception
	 */
	public List<SendStasticsVo> fromRealLoc2ExportVoList(List<SendPickLocationVo> realLocVos,Map<String,SendPickDetail> detailId2PickVoMap,Map<String,SendDeliveryVo> pickDetail2deliveryVoMap)throws Exception{
		List<SendStasticsVo> staticsList = new ArrayList<SendStasticsVo>();
		int i=1;
		for(SendPickLocationVo locVo:realLocVos){
			SendStasticsVo staticsVo = new SendStasticsVo();
			//获取订单
			SendDeliveryVo deliVo = pickDetail2deliveryVoMap.get(locVo.getSendPickLocation().getPickDetailId());
			staticsVo.setIndex(i);
			staticsVo.setExpressBillNo(deliVo.getSendDelivery().getExpressBillNo());
			staticsVo.setLocationNo(FqDataUtils.getLocById(locExtlService,locVo.getSendPickLocation().getLocationId()).getLocationNo());
			staticsVo.setOrderTime(deliVo.getSendDelivery().getOrderTime());
			staticsVo.setSrcNo(deliVo.getSendDelivery().getSrcNo());
			staticsVo.setOrderQty(deliVo.getSendDelivery().getOrderQty());
			staticsVo.setOutDate(deliVo.getSendDelivery().getUpdateTime());
			//作业人员
			staticsVo.setOperPerson(FqDataUtils.getUserNameById(userService, deliVo.getSendDelivery().getUpdatePerson()));
			//获取货品信息
			SendPickDetail pickDetail = detailId2PickVoMap.get(locVo.getSendPickLocation().getPickDetailId());
			MetaSku sku = FqDataUtils.getSkuById(skuService, pickDetail.getSkuId());
			
			staticsVo.setSendQty(locVo.getSendPickLocation().getPickQty());
			staticsVo.setSkuBarCode(sku.getSkuBarCode());
			staticsVo.setSkuName(sku.getSkuName());
			staticsVo.setMeasureUnit(sku.getMeasureUnit());
			//获取货主信息
			staticsVo.setMerchantName(FqDataUtils.getMerchantNameById(merchantService,sku.getOwner()));
			staticsList.add(staticsVo);
			i++;
		}
		return staticsList;
	}
	
	/**
	 * 查询发货明细
	 * @param detailVo
	 * @return
	 */
	public List<SendDeliveryDetail> qryDetails(DeliveryDetailVo detailVo){
		List<SendDeliveryDetail> deliveryDetailList = deliveryDetailDao.selectByExample(detailVo.getConditionExample());
		return deliveryDetailList;
	}

	/**
	 * @param list
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月24日 下午1:29:43<br/>
	 * @author 王通<br/>
	 */
	private List<SendDeliveryVo4Excel> vo2excelVo(List<SendDeliveryVo> list) {
		List<SendDeliveryVo4Excel> retList = new ArrayList<SendDeliveryVo4Excel>();
		if(list == null || list.isEmpty()) return null;
		for (int i = 0; i < list.size(); i++) {
			SendDeliveryVo4Excel vo4excel = new SendDeliveryVo4Excel(list.get(i));
			vo4excel.setIndex(String.valueOf(i+1));
			retList.add(vo4excel);
		}
		return retList;
	}
	
	/**
	 * 称重复核
	 * @param sendDeliveryVo
	 * @param p
	 */
	@Transactional(rollbackFor=Exception.class)
	public void checkPackageWeight(SendDeliveryVo sendDeliveryVo,Principal p) throws Exception{
		//检查是否打包状态
		checkStatus(sendDeliveryVo.getSendDelivery().getDeliveryId(),Constant.SEND_STATUS_PACKAGE,"delivery_status_is_not_packaged");		
		log.info("ScalageWeight========"+sendDeliveryVo.getSendDelivery().getScalageWeight());
		if(sendDeliveryVo.getSendDelivery().getScalageWeight() == null){
			return;
		}
		
		//更新订单重量与状态
		sendDeliveryVo.getSendDelivery().setUpdatePerson(p.getUserId());
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_CHECKWEIGHT);
		sendDeliveryVo.getSendDelivery().setIsCheckWeight(Constant.SCAN_HAS);
		deliveryDao.updateByPrimaryKeySelective(sendDeliveryVo.getSendDelivery());
		//增加订单操作明细
		deliveryLogService.addNewDeliveryLog(sendDeliveryVo.getSendDelivery().getDeliveryId(),
				p.getUserId(), Constant.DELIVERY_LOG_TYPE_CHECKWEIGHT, 
				p.getOrgId(), LoginUtil.getWareHouseId());
		
		//查询仓库配置
		MetaWarehouseSettingVo settingVo = warehouseSettingService.findByWarehouseId(LoginUtil.getWareHouseId());
		//检查仓库是否启用称重检查
		if(settingVo != null && settingVo.getEntity().getOutOfLimitRemind() != null && settingVo.getEntity().getOutOfLimitRemind()){
			double low = 0.0d;
			double high = 0.0d;
			//按数量计算误差值 
			if(settingVo.getEntity().getLimitType() == 1){
				low = sendDeliveryVo.getSendDelivery().getGrossWeight() - settingVo.getEntity().getLimitLow();
				high = sendDeliveryVo.getSendDelivery().getGrossWeight() + settingVo.getEntity().getLimitHigh();
			}
			//按百分比计算误差
			else{
				low = sendDeliveryVo.getSendDelivery().getGrossWeight() * (1-settingVo.getEntity().getLimitLow());
				high = sendDeliveryVo.getSendDelivery().getGrossWeight() * (1+settingVo.getEntity().getLimitHigh());
			}
			
			//若实际重量在范围以外，则异常记录
			if(sendDeliveryVo.getSendDelivery().getScalageWeight() < low || sendDeliveryVo.getSendDelivery().getScalageWeight() > high){
				ExceptionLog exceptionLog =  ExceptionLogUtil.createInstance(Constant.EXCEPTION_TYPE_SEND_ABNORMAL,
						sendDeliveryVo.getSendDelivery().getDeliveryNo(), 
						Constant.EXCEPTION_STATUS_TO_BE_HANDLED,
						Constant.EXCEPTION_LEVEL_SLIGHT,
						ServiceConstant.CHECKWEIGHT_EXECPTION);
				exceptionLogService.add(exceptionLog);
			}
		}
	
		//若是普通仓则自动发货确认并发送erp
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		if(Constant.WAREHOUSE_TYPE_NOTCUSTOMS == warehouseVo.getWarehouse().getWarehouseType()){
			confirmSend(sendDeliveryVo.getSendDelivery().getExpressBillNo(),
					p.getOrgId(),LoginUtil.getWareHouseId(),p.getUserId());
		}
	}
	
	/**
	 * 将所有仓库的出库数据发送到海关
	 * @param warehouseIdList
	 * @throws Exception
	 */
	public void findAndSendOutStockData(List<String> warehouseIdList)throws Exception{
		//查询所有完成发货的订单
		SendDeliveryVo sendDeliveryVo = new SendDeliveryVo();
		sendDeliveryVo.setWarehouseIdList(warehouseIdList);
		sendDeliveryVo.getSendDelivery().setDeliveryStatus(Constant.SEND_STATUS_ALLSHIP);
		List<SendDelivery> list = deliveryDao.selectByExample(sendDeliveryVo.getExample());
		List<String> idList = list.stream().map(t->t.getDeliveryId()).collect(Collectors.toList());
		
		//批量同步出库数据到海关辅助系统
		bachSyncOutStockData(idList);
	}
	
	/**
	 * 批量同步出库数据到海关辅助系统
	 * @param dataIdList
	 * @throws Exception
	 */
	public void bachSyncOutStockData(List<String> dataIdList) throws Exception{
		if(dataIdList == null || dataIdList.isEmpty()) return;
		for(String deliveryId:dataIdList){
			transmitOutStockXML(deliveryId);
		}
	}
	
	/**
	 * 传送出库数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
	public void transmitOutStockXML(String deliveryId) throws Exception{
		Principal loginUser = LoginUtil.getLoginUser();
		String messsageId = IdUtil.getUUID();
		String emsNo = paramService.getKey(CacheName.EMS_NO);
		String sendId = paramService.getKey(CacheName.TRAED_CODE);
		String receiveId = paramService.getKey(CacheName.CUSTOM_CODE);
		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
		String type_out_stock = paramService.getKey(CacheName.TYPE_OUTSTOCK_NONBOND);
		String messageType = paramService.getKey(CacheName.MSMQ_MESSAGE_TYPE_INOUTSTOCK);
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replace(" ", "T");
		String date = DateFormatUtils.format(today, "yyyy-MM-dd");
		//查询发货单信息
		SendDeliveryVo deliveryVo = view(deliveryId);
		if(deliveryVo == null) throw new BizException("delivery_is_not_exit");
		if(deliveryVo.getDeliveryDetailVoList() == null || deliveryVo.getDeliveryDetailVoList().isEmpty()) 
			throw new BizException("Delivery_detail_is_empty");
		//检查是否全部发货状态
		if(Constant.SEND_STATUS_ALLSHIP != deliveryVo.getSendDelivery().getDeliveryStatus().intValue()){
			throw new BizException("delivery_status_is_not_all_ship");
		}
		//创建出库报文
		MSMQ msmq = new MSMQ();
		//报文头参数
		Head head = new Head();
		head.setMESSAGE_ID(messsageId);
		head.setMESSAGE_TYPE(messageType);
		head.setEMS_NO(emsNo);
		head.setFUNCTION_CODE("A");//功能代码 新增
		head.setMESSAGE_DATE(dateTime);
		head.setSENDER_ID(sendId);
		head.setRECEIVER_ID(receiveId);
		head.setSEND_TYPE("0");
		msmq.setHead(head);
		//报文体
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		for(DeliveryDetailVo detailVo:deliveryVo.getDeliveryDetailVoList()){
			//查询货主
			MetaMerchant owner = FqDataUtils.getMerchantById(merchantService, detailVo.getSku().getOwner());
			Map<String,String> map = new HashMap<String,String>();
			for(SendPickLocationVo locationVo:detailVo.getRealPickLocations()){
				map.put("MESSAGE_ID", messsageId);
				map.put("EMS_NO", emsNo);
				map.put("IO_NO",deliveryVo.getSendDelivery().getDeliveryNo());
				map.put("GOODS_NATURE", detailVo.getSku().getGoodsNature()==null?"":detailVo.getSku().getGoodsNature()+"");
				map.put("IO_DATE", date);
				map.put("COP_G_NO", StringUtil.isBlank(detailVo.getSku().getHgGoodsNo())?detailVo.getSku().getSkuNo():detailVo.getSku().getHgGoodsNo());
				map.put("G_NO", detailVo.getSku().getgNo());
				map.put("HSCODE", detailVo.getSku().getHsCode());
				map.put("GNAME", detailVo.getSku().getSkuName());
				map.put("GMODEL", detailVo.getSku().getSpecModel());
				map.put("CURR", detailVo.getSku().getCurr());
				map.put("COUNTRY", detailVo.getSku().getOriginCountry());
				map.put("QTY", (-locationVo.getSendPickLocation().getPickQty())+"");
				map.put("UNIT", detailVo.getSku().getMeasureUnit());
				map.put("TYPE", type_out_stock);
				map.put("CHK_CODE", "01");
				map.put("GATEJOB_NO", deliveryVo.getSendDelivery().getGatejobNo());
				map.put("WHS_CODE", deliveryVo.getWarehouseNo());
				map.put("LOCATION_CODE", locationVo.getLocationComment());
				map.put("OWNER_CODE", owner.getHgMerchantNo());
				map.put("OWNER_NAME", owner.getMerchantName());
			}
			mapList.add(map);
		}
		msmq.setBodyMaps(mapList);
		//创建出库报文
		TransmitXmlUtil tranUtil = new TransmitXmlUtil(msmq);
		String xmlStr = tranUtil.formXml();
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messsageId+"_出库数据报文：["+xmlStr+"]");	

		//传送入库数据到仓储企业联网监管系统
		if(log.isInfoEnabled()) log.info("Queue名称：["+sendChnalName+"]");
		boolean result = MSMQUtil.send(sendChnalName, label, xmlStr, messsageId.getBytes());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messsageId+"_出库数据发送接口运行结果：["+result+"]");	
		
		//保存报文
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setMessageId(messsageId);
		messageVo.getEntity().setFunctionType(Constant.FUNCTION_TYPE_SEND);
		messageVo.getEntity().setOrderNo(deliveryId);
		messageVo.getEntity().setContent(xmlStr);
		messageVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
		messageVo.getEntity().setSendTime(new Date());
		messageVo.getEntity().setCreatePerson(loginUser.getUserId());
		messageVo.getEntity().setOrgId(loginUser.getOrgId());
		messageVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		msmqMessageService.add(messageVo);
		
		//修改发货单发送状态
		if(result){
			deliveryVo.getSendDelivery().setTransStatus(Constant.SYNCSTOCK_STATUS_SEND_SUCCESS);
			deliveryVo.getSendDelivery().setUpdatePerson(loginUser.getUserId());
			deliveryDao.updateByPrimaryKeySelective(deliveryVo.getSendDelivery());
		}
	}
	
	/**
	 * 更新快递单打印次数
	 * @param deliveryId
	 * @throws Exception
	 */
	public void updatePrintExpressTimes(String deliveryId) throws Exception{
		//更新快递单打印次数
		deliveryDao.incExpressPrintTimes(deliveryId);
	}

	@Override
//	@Transactional(rollbackFor=Exception.class)
	public SendDeliveryVo viewByEntity(SendDeliveryVo deliveryVo) throws Exception {
		if(deliveryVo == null) return null;
		//订单拦截
		boolean isSuccess = intercept(deliveryVo.getSendDelivery().getExpressBillNo());
		if(isSuccess) throw new BizException("delivery_intercept_billno");
		//查询发货单
		Principal loginUser = LoginUtil.getLoginUser();
		deliveryVo.getSendDelivery().setOrgId(loginUser.getOrgId());
		deliveryVo.getSendDelivery().setWarehouseId(LoginUtil.getWareHouseId());
		SendDelivery sendDelivery = deliveryDao.selectOne(deliveryVo.getSendDelivery());
		return chg(sendDelivery);
	}

	private SendDeliveryVo chg(SendDelivery sendDelivery) throws Exception {
		if (sendDelivery == null) {
			return null;
		}
		SendDeliveryVo deliveryVo = new SendDeliveryVo(sendDelivery);
		
		String deliveryId = sendDelivery.getDeliveryId();
		//发货方名称
		deliveryVo.setSenderName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getSender()));
		//收货方名称
		deliveryVo.setReceiverName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getReceiver()));
		//仓库寄件人（客商）名称 
		deliveryVo.setConsignor(FqDataUtils.getMerchantById(merchantService, sendDelivery.getConsignorId()));
		deliveryVo.setConsignorName(FqDataUtils.getMerchantNameById(merchantService, sendDelivery.getConsignor()));

		//查询仓库
		MetaWarehouse warehouse = wrehouseExtlService.findWareHouseById(sendDelivery.getWarehouseId());
		deliveryVo.setWarehouseName(FqDataUtils.getWarehouseNameById(wrehouseExtlService, sendDelivery.getWarehouseId()));
		deliveryVo.setWarehouseNo(warehouse.getWarehouseNo());
		//查询货主
		deliveryVo.setOwnerName(FqDataUtils.getMerchantNameById(merchantService,sendDelivery.getOwner()));
		//查询发货明细		
		List<DeliveryDetailVo> detailVoList = getDetailsInfoBydeliveryId(deliveryId);		
		deliveryVo.setSendDelivery(sendDelivery);
		deliveryVo.setDeliveryDetailVoList(detailVoList);
		
		//查询辅材明细
		SendDeliveryMaterialVo deliveryMaterialVo = new SendDeliveryMaterialVo();
		deliveryMaterialVo.getSendDeliveryMaterial().setOrgId(sendDelivery.getOrgId());
		deliveryMaterialVo.getSendDeliveryMaterial().setWarehouseId(sendDelivery.getWarehouseId());
		deliveryMaterialVo.getSendDeliveryMaterial().setDeliveryId(deliveryId);
		//查询发货单辅材
		List<SendDeliveryMaterial> materialList = deliveryMaterialDao.selectByExample(deliveryMaterialVo.getConditionExample());
		for(SendDeliveryMaterial sdm:materialList){
			SendDeliveryMaterialVo sdmVo = new SendDeliveryMaterialVo();
			sdmVo.setSendDeliveryMaterial(sdm);
			//根据id查询辅材信息
			MaterialVo materialVo = (MaterialVo) materialService.view(sdm.getMaterialId(), LoginUtil.getLoginUser()).getObj();
			sdmVo.setMaterialVo(materialVo);
			deliveryVo.getDeliveryMaterialVoList().add(sdmVo);
		}
		return deliveryVo;
	}
	

	@Override
	public SendDeliveryVo getBom(SendDeliveryVo deliveryVo) throws Exception {
		Principal p = LoginUtil.getLoginUser();
		StringBuffer skuInfo = new StringBuffer();
    	StringBuffer skuNoInfo = new StringBuffer();
        if(deliveryVo.getDeliveryDetailVoList() != null) {
        	deliveryVo.setDeliveryDetailVoList(sortOrder(deliveryVo.getDeliveryDetailVoList()));
        	for(int i = 0; i < deliveryVo.getDeliveryDetailVoList().size(); i++) {
        		DeliveryDetailVo detailVo = deliveryVo.getDeliveryDetailVoList().get(i);
        		
        		if (i == 0) {
        			skuInfo.append(detailVo.getSkuName()).append("X").append(detailVo.getDeliveryDetail().getOrderQty().intValue());
        			skuNoInfo.append(detailVo.getSkuNo());
            	} else {
            		skuInfo.append(",").append(detailVo.getSkuName()).append("X").append(detailVo.getDeliveryDetail().getOrderQty().intValue());
            		skuNoInfo.append(",").append(detailVo.getSkuNo());
        		}
        	}
        }
        String md5 = MD5Util.md5(skuInfo.toString() + LoginUtil.getWareHouseId() + p.getOrgId());
        SkuMaterialBomVo bomVo = new SkuMaterialBomVo();
        bomVo.getEntity().setIdentificationCode(md5);
        SkuMaterialBomVo retBomVo = bomService.selectOne(bomVo, LoginUtil.getLoginUser());
        if (retBomVo != null) {
        	List<SendDeliveryMaterialVo> deliveryMaterialVoList = new ArrayList<SendDeliveryMaterialVo>();//辅材列表
        	for (MaterialBomVo materialBomVo : retBomVo.getMaterialBomList()) {
        		SendDeliveryMaterialVo sendDeliveryMaterialVo = new SendDeliveryMaterialVo();
        		sendDeliveryMaterialVo.setMaterialVo(materialBomVo.getMaterialVo());
        		SendDeliveryMaterial sdm = sendDeliveryMaterialVo.getSendDeliveryMaterial();
        		sdm.setDeliveryId(deliveryVo.getSendDelivery().getDeliveryId());
        		sdm.setMaterialId(materialBomVo.getEntity().getMaterialId());
        		sdm.setQty(materialBomVo.getEntity().getNumber());
        		deliveryMaterialVoList.add(sendDeliveryMaterialVo);
        	}
        	deliveryVo.setDeliveryMaterialVoList(deliveryMaterialVoList);
        }
		return deliveryVo;
	}
	
	private List<DeliveryDetailVo> sortOrder(List<DeliveryDetailVo> deliveryDetailVoList) {
		for (int i = 0; i < deliveryDetailVoList.size(); i++) {
			DeliveryDetailVo detailVo1 = deliveryDetailVoList.get(i);
			for (int j = i + 1; j < deliveryDetailVoList.size(); j++) {
				DeliveryDetailVo detailVo2 = deliveryDetailVoList.get(j);
				String skuNo1 = detailVo1.getSkuNo();
				String skuNo2 = detailVo2.getSkuNo();
				if (skuNo1.compareTo(skuNo2) > 0) {
					DeliveryDetailVo temp1 = (DeliveryDetailVo) detailVo1.clone();
					DeliveryDetailVo temp2 = (DeliveryDetailVo) detailVo2.clone();
					deliveryDetailVoList.remove(j);
					deliveryDetailVoList.add(j, temp1);
					deliveryDetailVoList.remove(i);
					deliveryDetailVoList.add(i, temp2);
					i--;
					break;
				}
			}
		}
		return deliveryDetailVoList;
	}

	/**
	 * 运单号发送到物流公司及erp
	 * @param deliveryVo
	 */
//	public void send2ErpAndSaveStatus(SendDeliveryVo deliveryVo){
//		Thread thread = new Thread(new Runnable() {
//			public void run() {
//				//同步ERP
//				try {
//					ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliveryVo, "",Constant.CONFIRM_RETURN);
//					if(erpResult!=null && erpResult.getCode()==1){//成功
//						updateSendStatus(deliveryVo.getSendDelivery().getDeliveryId(),3);
//					}else{
//						if(log.isErrorEnabled()) log.error(erpResult.getMessage());
//					}
//				} catch (Exception e) {
//					if(log.isErrorEnabled()) log.error(e.getMessage(), e);
//				}
//			}
//		});
//		ThreadPoolUtils.getInstance().addThreadItem(thread);
//	}
	
	/**
	 * 发送订单拦截结果
	 * @param deliveryVo
	 */
//	public void interceptAndSend2ERP(SendDeliveryVo deliveryVo){
//		Thread thread = new Thread(new Runnable() {
//			public void run() {
//				//同步ERP
//				try {
//					String remark = paramService.getValue(CacheName.SEND_INTERCEPT_STATUS, deliveryVo.getSendDelivery().getInterceptStatus());
//					ErpResult erpResult = context.getStrategy4Erp().sendToERP(deliveryVo, remark,Constant.INTECEPT_RETURN);
//					if(erpResult != null) if(log.isInfoEnabled()) log.info(erpResult.getMessage());
//				} catch (Exception e) {
//					if(log.isErrorEnabled()) log.error(e.getMessage(), e);
//				}
//			}
//		});
//		ThreadPoolUtils.getInstance().addThreadItem(thread);
//	}

	@Override
	public int updateSendStatus(String deliveryId, int sendStatus, String bigchar, String gather_place) {
		SendDelivery entity = new SendDelivery();
		entity.setDeliveryId(deliveryId);
		entity.setSendStatus(sendStatus);
		entity.setExpressBigchar(bigchar);
		entity.setExpressGatheringPlace(gather_place);
		entity.setUpdateTime(new Date());
		int num = deliveryDao.updateByPrimaryKeySelective(entity);
		return num;
	}
	
	/**
	 * 更新发货单实体
	 * @param entity
	 * @return
	 */
	public int updateEntity(SendDelivery entity,Principal p)throws Exception{
		//发货单是否打开状态
		if(Constant.SEND_STATUS_OPEN != qryDeliveryStatus(entity.getDeliveryId()))
			throw new BizException(BizStatus.SEND_DELIVERY_STATUS_IS_NOT_OPEN.getReasonPhrase());
		
		entity.setWarehouseId(LoginUtil.getWareHouseId());
		entity.setOrgId(p.getOrgId());
		entity.setUpdatePerson(p.getUserId());
		int num = deliveryDao.updateByPrimaryKeySelective(entity);
		return num;
	}
	
	/**
	 * 更新实体并生效
	 * @param entity
	 * @param p
	 * @throws Exception
	 */
	public void updateEntityAndEnable(SendDelivery entity,Principal p)throws Exception{
		//1、保存发货单
		updateEntity(entity,p);
		//2、生效发货单,生成并生效拣货单
		enableAndCreatePick(entity.getDeliveryId(), p.getUserId());
	}
	

	@Override
	public ResultModel qryTotalOrder(SendDeliveryVo sendDeliveryVo) throws Exception {
		if(sendDeliveryVo == null || sendDeliveryVo.getSendDelivery() == null) return null;
		ResultModel result = new ResultModel();
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		//查询发货单列表
		sendDeliveryVo.getSendDelivery().setOrgId(orgId);
		sendDeliveryVo.getSendDelivery().setWarehouseId(wareHouseId);
		
		List<String> deliveryIdList = new ArrayList<String>();
		//根据货主查询发货单id列表
		if(StringUtils.isNotEmpty(sendDeliveryVo.getOwnerName())){
			//根据客商名称列表查询货品列表
			SkuVo skuVo = new SkuVo().setMerchant(new MetaMerchant().setMerchantName(sendDeliveryVo.getOwnerName()));
			List<MetaSku> skuList = skuService.qryAllList(skuVo,loginUser);
			if(skuList == null || skuList.isEmpty()) return result;
			List<String> skuIdList = skuList.stream().map(t->t.getSkuId()).collect(Collectors.toList());
			
			//在发货明细表中查询包含此货品id的发货id列表
			DeliveryDetailVo detailVo = new DeliveryDetailVo();
			detailVo.setSkuIdList(skuIdList);
			List<SendDeliveryDetail> detailList = deliveryDetailDao.selectByExample(detailVo.getConditionExample());
			if(detailList == null || detailList.isEmpty()) return result;
			deliveryIdList.addAll(detailList.stream().map(t->t.getDeliveryId()).collect(Collectors.toList()));
		}
		if(!deliveryIdList.isEmpty()){
			sendDeliveryVo.setLoadConfirmIds(deliveryIdList);
		}
		//发货单查询没有选就默认查询除取消以外的所有状态
		if(sendDeliveryVo.getSendDelivery().getDeliveryStatus() == null){
			sendDeliveryVo.setLessThanStatus(Constant.SEND_STATUS_CANCAL);
		}
		if (sendDeliveryVo.getBeginTime() != null && sendDeliveryVo.getEndTime() != null) {
			sendDeliveryVo.setBeginTime(sendDeliveryVo.getBeginTime() + " 00:00:00");
			sendDeliveryVo.setEndTime(sendDeliveryVo.getEndTime() + " 23:59:59");
		}
		if (StringUtils.isNoneBlank(sendDeliveryVo.getSendStartTime())) {
			sendDeliveryVo.setSendStartTime(sendDeliveryVo.getSendStartTime() + " 00:00:00");
		}
		if (StringUtils.isNoneBlank(sendDeliveryVo.getSendEndTime())) {
			sendDeliveryVo.setSendEndTime(sendDeliveryVo.getSendEndTime() + " 23:59:59");
		}
		sendDeliveryVo.setProvinceName(StringUtil.likeEscapeH(sendDeliveryVo.getProvinceName()));
		sendDeliveryVo.getSendDelivery().setReceiver(StringUtil.likeEscapeH(sendDeliveryVo.getSendDelivery().getReceiver()));
		sendDeliveryVo.setCityName(StringUtil.likeEscapeH(sendDeliveryVo.getCityName()));
		sendDeliveryVo.setSrcNoLike(StringUtil.likeEscapeH(sendDeliveryVo.getSrcNoLike()));
		sendDeliveryVo.setDeliveryNoLike(StringUtil.likeEscapeH(sendDeliveryVo.getDeliveryNoLike()));
		
		if (sendDeliveryVo.getLoadConfirmIds() == null || sendDeliveryVo.getLoadConfirmIds().isEmpty()) {
			sendDeliveryVo.setLoadConfirmIds(null);
		}
		//查询发货单列表
		TotalVo vo = deliveryDao.selectTotalOrder(sendDeliveryVo);

		result.setObj(vo);
		return result;
	}
	
	
	/**
	 * 查询出库统计
	 * @param vo
	 * @param p
	 * @param hasPage
	 * @return
	 * @throws Exception
	 */
	public ResultModel qryTotalSend(SendPickLocationVo vo, Principal p,Boolean hasPage) throws Exception{
		ResultModel result = new ResultModel();
		vo.setOrgId(p.getOrgId());
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		//是否用分页查询
		TotalVo ret = deliveryDao.selectTotalSend(vo);
		result.setObj(ret);
		return result;
	}
	
	/**
	 * 订单拦截处理
	 * 1)	发货单打开：发货单操作状态：取消，业务状态：拦截成功
	 * 2)	发货单生效/全部拣货/已打包：业务状态为订单拦截，爆款自动置为截留
	 * 3)	发货单在称重复核时拦截，扫描快递单号时，弹出订单拦截通知，2S后自动消失。发货单操作状态：取消，无法发货确认，业务状态：拦截成功，返回ERP拦截成功信息，自动生效移位单，将订单货品从发货区已到退货区。
	 * 4)	发货单全部发运：订单拦截失败，发货单进行下一步操作，业务状态：截留
	 * @param srcNo
	 * @return
	 */
	public Map<String,String> interceptHandle(String srcNo){
		Map<String,String> result = new HashMap<String,String>();
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		//查询发货单
		SendDelivery sendDelivery = getBySrcNo(srcNo, loginUser);
		//若订单已取消则提示异常
		if(Constant.SEND_STATUS_CANCAL == sendDelivery.getDeliveryStatus()) 
			throw new BizException("delivery_has_cancel");
		//1、若发货单状态打开，则取消，置为拦截成功
		if(Constant.SEND_STATUS_OPEN == sendDelivery.getDeliveryStatus()){
			sendDelivery.setDeliveryStatus(Constant.SEND_STATUS_CANCAL);
			sendDelivery.setInterceptStatus(Constant.INTERCEPT_SUCCESS);
			//新增发货操作日志
			deliveryLogService.addNewDeliveryLog(sendDelivery.getDeliveryId(),
					LoginUtil.getLoginUser().getUserId(),Constant.DELIVERY_LOG_TYPE_CANCEL,
					sendDelivery.getOrgId(),sendDelivery.getWarehouseId());
		}
		//2、若发货单是发货完成，或者爆款出库，则置为订单截留
		else if(Constant.SEND_STATUS_ALLSHIP == sendDelivery.getDeliveryStatus() || Constant.DELIVERY_TYPE_BURST_OUT == sendDelivery.getDocType()){
			sendDelivery.setInterceptStatus(Constant.INTERCEPT_TRAP);
		}
		//若发货单生效，拣货完成，打包复核，则置为订单拦截
		else{
			sendDelivery.setInterceptStatus(Constant.INTERCEPT_HOLD_OFF);
		}
		sendDelivery.setUpdatePerson(loginUser.getUserId());
		sendDelivery.setUpdateTime(new Date());
		deliveryDao.updateByPrimaryKeySelective(sendDelivery);

		//返回拦截结果
		String interceptStatusComment = paramService.getValue(CacheName.SEND_INTERCEPT_STATUS, sendDelivery.getInterceptStatus());
		result.put("srcNo",srcNo);
		result.put("result",interceptStatusComment);
		return result;
	}

	/** 
	* @Title: intercept 
	* @Description: 订单拦截接口（在称重复核阶段）
	* @auth tphe06
	* @time 2018 2018年8月28日 上午10:24:42
	* @param expressBilNo 运单号
	* @return 是否拦截成功
	 * @throws Exception 
	*/
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean intercept(String expressBilNo) throws Exception {
		Principal p = LoginUtil.getLoginUser();
		SendDelivery d = new SendDelivery();
		d.setExpressBillNo(expressBilNo);
		d.setOrgId(p.getOrgId());
		d.setWarehouseId(LoginUtil.getWareHouseId());
		d = deliveryDao.selectOne(d);
		//正式
		if(d == null || d.getDeliveryStatus() != Constant.SEND_STATUS_PACKAGE 
			|| d.getInterceptStatus() == null || d.getInterceptStatus() != Constant.INTERCEPT_HOLD_OFF) return false;
		String id = d.getDeliveryId();
		List<SendPickVo> list = null;
		if(StringUtils.isBlank(d.getWaveId())) {
			list = pickService.queryByDeliveryId(id);
		} else {
			list = pickService.queryByWaveId(d.getWaveId());
		}
//		if(list == null || list.isEmpty()) {
//			cancelDelivery(id);
//			return true;
//		}
		//退货区库位
		MetaLocation location = locExtlService.findLocByT();
		//移位单信息
		InvShiftVO vo = new InvShiftVO();
		vo.getInvShift().setShiftType(Constant.SHIFT_TYPE_REJECT);//退货移位
		vo.getInvShift().setInvolveBill(d.getDeliveryNo());
		//移位单详情
		List<InvShiftDetailVO> details = new ArrayList<InvShiftDetailVO>();
		for(SendPickVo pick : list) {
			for(SendPickDetailVo pd : pick.getSendPickDetailVoList()) {
				if(!d.getDeliveryId().equals(pd.getSendPickDetail().getDeliveryId())) continue;
				for(SendPickLocationVo lv : pd.getRealPickLocations()) {
					InvShiftDetailVO detail = new InvShiftDetailVO();
					detail.getInvShiftDetail().setSkuId(pd.getSendPickDetail().getSkuId());
					detail.getInvShiftDetail().setBatchNo(lv.getSendPickLocation().getBatchNo());
					detail.getInvShiftDetail().setOutLocation(lv.getSendPickLocation().getLocationId());
					detail.getInvShiftDetail().setInLocation(location.getLocationId());
					detail.getInvShiftDetail().setShiftQty(lv.getSendPickLocation().getPickQty());
					detail.getInvShiftDetail().setRealShiftQty(lv.getSendPickLocation().getPickQty());
					detail.getInvShiftDetail().setInDate(new Date());
					details.add(detail);
				}
			}
		}
		vo.setListInvShiftDetailVO(details);
		vo = shiftService.saveAndEnable(vo);
		//发货单/拣货单/波次单操作状态改成取消，业务状态改成拦截成功
		cancelDelivery(id);
//		for(SendPickVo pick : list) {
//			if(pick.getSendPickDetailVoList().size() > 0) {
//				SendPickDetailVo pd = pick.getSendPickDetailVoList().get(0);
//				if(!d.getDeliveryId().equals(pd.getSendPickDetail().getDeliveryId())) continue;
//				int r = pickService.updateStatus(pick.getSendPick().getPickId(), p.getUserId(), Constant.PICK_STATUS_CANCAL);
//				if(r == 0) throw new BizException("err_network_error");
//			}
//		}
		//把退货确认结果发送给ERP
//		send2ErpAndSaveStatus(view(id));
		return true;
	}
	
	@Transactional(rollbackFor=Exception.class)
	private void cancelDelivery(String id) {
		SendDelivery d = new SendDelivery();
		d.setDeliveryId(id);
		d.setDeliveryStatus(Constant.SEND_STATUS_CANCAL);
		d.setInterceptStatus(Constant.INTERCEPT_SUCCESS);
		int r = deliveryDao.updateByPrimaryKeySelective(d);
		//新增发货操作日志
		//获取登录用户
		deliveryLogService.addNewDeliveryLog(id,
				LoginUtil.getLoginUser().getUserId(),Constant.DELIVERY_LOG_TYPE_CANCEL,
				LoginUtil.getLoginUser().getOrgId(),LoginUtil.getWareHouseId());
		if(r == 0) throw new BizException("err_network_error");
	}
	
}