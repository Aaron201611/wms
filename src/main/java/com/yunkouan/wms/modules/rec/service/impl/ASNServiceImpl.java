/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:55:43<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
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
import com.yunkouan.util.DurationUtil;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
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
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.message.service.IMsmqMessageService;
import com.yunkouan.wms.modules.message.vo.MsmqMessageVo;
import com.yunkouan.wms.modules.meta.dao.ISkuDao;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.meta.vo.SkuVo;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.park.entity.ParkOrgBusiStas;
import com.yunkouan.wms.modules.park.service.IParkOrgBusiStasService;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.vo.ParkOrgBusiStasVo;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;
import com.yunkouan.wms.modules.rec.dao.IASNDao;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IASNService;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayLocationExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.util.RecUtil;
import com.yunkouan.wms.modules.rec.util.strategy.AsnSplitStrategy;
import com.yunkouan.wms.modules.rec.util.strategy.SplitStrategy;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO4Excel;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.send.util.TransmitXmlUtil;
import com.yunkouan.wms.modules.send.vo.ems.ErpResult;
import com.yunkouan.wms.modules.ts.service.ITaskService;

/**
 * ASN单服务实现类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:55:43<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ASNServiceImpl extends BaseService implements IASNService {
	/** 日志对象 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/** Asn单明细外调对象 <br/> add by andy wang */
    @Autowired
	private IASNDetailExtlService asnDetailExtlService;
    
    /** Asn单外调对象 <br/> add by andy wang */
    @Autowired
    private IASNExtlService asnExtlService;
    
    /** 在库业务类 <br/> add by andy wang */
    @Autowired
    private IStockService stockService;
    
    /** 异常业务类 <br/> add by andy wang */
    @Autowired
    private IExceptionLogService exceptionLogService;
    
    /** 仓库业务类 <br/> add by andy wang */
	@Autowired
	private IWarehouseExtlService warehouseExtlService;
    
	/** 客商业务类 <br/> add by andy wang */
	@Autowired
	private IMerchantService merchantService;
    
	/** 用户业务类 <br/> add by andy wang */
	@Autowired
	private IUserService userService;
	
	/** 货品业务类 <br/> add by andy wang */
	@Autowired
	private ISkuService skuService;
	
	/** 包装业务类 <br/> add by andy */
	@Autowired
	private IPackService packService;
	
	/** 库位业务类 <br/> add by andy wang */
	@Autowired
	private ILocationExtlService locExtlService;

	/** 上架单明细业务类 <br/> add by andy wang */
	@Autowired
	private IPutawayDetailExtlService ptwDetailExtlService;
	
	/** 上架单操作明细业务类 <br/> add by andy wang */
	@Autowired
	private IPutawayLocationExtlService ptwLocExtlService;
	
	/** 业务统计业务类 <br/> add by andy wang */
	@Autowired
	private IParkOrgBusiStasService parkOrgBusiStasService;
	
	/** 出租业务类 <br/> add by andy wang */
	@Autowired
	private IParkRentService parkRentService;
	
	/** 任务业务类 <br/> add by andy wang */
	@Autowired
	private ITaskService taskService;
	
	/**ptwServie:上架单服务接口**/
	@Autowired
	private IPutawayService ptwServie;
	
	@Autowired
	private IMsmqMessageService msmqMessageService;
    /* method *******************************************************/
	
	@Autowired
	private ISkuDao skuDao;
    
	@Autowired
	private IASNDao asnDao;

	@Autowired
	private IDeliverGoodsApplicationService deliverGoodsApplicationService;
	
	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private ISysParamService paramService;

	/**
	 * 更新并生效asn单
	 * @param param_warehouseVO asn单对象
	 * @return 生效后的asn单
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO updateAndEnable ( RecAsnVO param_asnVO ) throws Exception {
		if ( param_asnVO == null || param_asnVO.getAsn() == null ) {
    		log.error("updateAndEnable --> param_asnVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		RecAsnVO asnVO = this.update(param_asnVO);
		asnVO.addAsnId(asnVO.getAsn().getAsnId());
		this.enable(asnVO);
		asnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_ACTIVE);
		return asnVO;
	}
	
	/**
	 * 保存并生效仓库
	 * @param param_asnVO 仓库对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO addAndEnable ( RecAsnVO param_asnVO ) throws Exception {
		if ( param_asnVO == null || param_asnVO.getAsn() == null ) {
    		log.error("addAndEnable --> param_asnVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		RecAsnVO insertAsnVO = this.add(param_asnVO);
		if ( insertAsnVO == null 
				|| StringUtil.isTrimEmpty(insertAsnVO.getAsn().getAsnId()) ) {
			throw new NullPointerException("addAndEnable return is null!");
		}
		insertAsnVO.setTsTaskVo(param_asnVO.getTsTaskVo());
		insertAsnVO.addAsnId(insertAsnVO.getAsn().getAsnId());
		this.enable(insertAsnVO);
		param_asnVO.getAsn().setAsnStatus(Constant.ASN_STATUS_ACTIVE);
		return insertAsnVO;
	}
	
	/**
	 * 查询暂存区的ASN单列表
	 * @return 暂存区的ASN单列表
	 * @throws Exception
	 * @version 2017年3月30日 下午2:18:01<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnVO> listStock (Integer docType) throws Exception {
		DurationUtil dd = new DurationUtil();
		dd.start();
		InvStockVO stockVo = new InvStockVO( new InvStock() ); 
		stockVo.setOnlyTemp(true);
		stockVo.setContainTemp(true);
		stockVo.getInvStock().setIsBlock(Constant.STOCK_BLOCK_FALSE);
		List<InvStock> listStock = this.stockService.list(stockVo);
		if ( PubUtil.isEmpty(listStock) ) {
			return new ArrayList<RecAsnVO>();
		}
		List<RecAsnVO> listRecAsnVO = new ArrayList<RecAsnVO>();
		
		List<String> listAsnId = new ArrayList<String>();
		List<String> listAsnDetailId = new ArrayList<String>();
		for (InvStock invStock : listStock) {
			if ( invStock.getStockQty() == null || invStock.getStockQty() == 0 
					|| (invStock.getPickQty() != null && invStock.getStockQty() - invStock.getPickQty() <= 0) ) {
				continue;
			}
			
			String locationId = invStock.getLocationId();
			MetaLocation location = this.locExtlService.findLocById(locationId);
			if ( location == null || Constant.LOCATION_BLOCK_TRUE == location.getIsBlock().intValue() ) {
				continue;
			}
			
			if ( !StringUtil.isTrimEmpty(invStock.getAsnId()) 
					&& !listAsnId.contains(invStock.getAsnId()) ) {
				listAsnId.add(invStock.getAsnId());
			}
			if ( !StringUtil.isTrimEmpty(invStock.getAsnDetailId()) 
					&& !listAsnDetailId.contains(invStock.getAsnDetailId()) ) {
				listAsnDetailId.add(invStock.getAsnDetailId());
			}
		}
		Map<String, RecAsn> mapAsn = this.asnExtlService.mapAsnByIds(listAsnId);
		Map<String, RecAsnDetail> mapAsnDetail = this.asnDetailExtlService.mapAsnDetailByDetailId(listAsnDetailId);
//		dd.end(true);
//		dd.start();
		Map<String,RecAsnVO> mapExistsAsn = new HashMap<String,RecAsnVO>();
		for (InvStock invStock : listStock) {
			if ( StringUtil.isTrimEmpty(invStock.getAsnId()) 
					|| StringUtil.isTrimEmpty(invStock.getAsnDetailId()) ) {
				continue;
			}

			// 查询ASN单明细
			RecAsnDetail asnDetail = mapAsnDetail.get(invStock.getAsnDetailId());
			if ( asnDetail == null ) {
				continue;
			}
			//检查docType参数类型
			if ((asnDetail.getSkuStatus() != null && asnDetail.getSkuStatus() == Constant.STOCK_SKU_STATUS_ABNORMAL && docType != Constant.PUTAWAY_DOCTYPE_BAD)
					|| ((asnDetail.getSkuStatus() == null || asnDetail.getSkuStatus() == Constant.STOCK_SKU_STATUS_NORMAL) && docType != Constant.PUTAWAY_DOCTYPE_RECEIVE)) {
				continue;
			}
			// 查询ASN单
			RecAsnVO asnVO = mapExistsAsn.get(invStock.getAsnId());
			RecAsn asn = null;
			if ( asnVO == null ) {
				asn = mapAsn.get(invStock.getAsnId());
				if ( asn == null ) {
					continue;
				}
				asnVO = new RecAsnVO(asn);
			} else {
				asn = asnVO.getAsn();
			}
			if ( !mapExistsAsn.containsKey(asn.getAsnId()) ) {
				listRecAsnVO.add(asnVO);
				mapExistsAsn.put(asn.getAsnId(),asnVO);
			}
			List<RecAsnDetailVO> listAsnDetailVO = asnVO.getListAsnDetailVO();
			if ( asnVO.getStockQty() == null ) {
				asnVO.setStockQty(0.0);
			}
			RecAsnDetailVO asnDetailVO = new RecAsnDetailVO(asnDetail);
			
			// 查询货品
			MetaSku sku = FqDataUtils.getSkuById(this.skuService, asnDetail.getSkuId());
			// 查询货品
			if ( sku != null ) {
				asnDetailVO.setSkuComment(sku.getSkuName());
				asnDetailVO.setSkuNo(sku.getSkuNo());
				asnDetailVO.setPerVolume(sku.getPerVolume());
				asnDetailVO.setPerWeight(sku.getPerWeight());
			}
			if ( sku.getPerVolume() == null ) {
				sku.setPerVolume(0d);
			}
			if ( sku.getPerWeight() == null ) {
				sku.setPerWeight(0d);
			}
			
			asnDetail.setLocationId(invStock.getLocationId());
			asnVO.setStockQty(asnVO.getStockQty() + invStock.getStockQty()-invStock.getPickQty());
			asnDetailVO.setStockQty(invStock.getStockQty()-invStock.getPickQty());
			asnDetailVO.setStockWeight(invStock.getStockWeight()-(NumberUtil.mul(invStock.getPickQty(), sku.getPerWeight())));
			asnDetailVO.setStockVolume(invStock.getStockVolume()-(NumberUtil.mul(invStock.getPickQty(), sku.getPerVolume())));
			asnDetailVO.setAsnNo(asn.getAsnNo());
			asnDetailVO.setOwner(asn.getOwner());

			listAsnDetailVO.add(asnDetailVO);
			asnVO.setStockCount(listAsnDetailVO.size());

			// 查询其他信息描述
			// 查询货主
			asnVO.setOwnerComment(FqDataUtils.getMerchantNameById(this.merchantService, asn.getOwner()));
			// 查询货品的规格型号
			asnDetailVO.setSpecModelComment(sku.getSpecModel());

			// 查询包装单位
			asnDetailVO.setPackComment(FqDataUtils.getPackUnit(this.packService, asnDetail.getPackId()));
		}
//		dd.end(true);
//		dd.start();
		// 根据创建时间排序
		listRecAsnVO.sort( new Comparator<RecAsnVO>() {
			@Override
			public int compare(RecAsnVO o1, RecAsnVO o2) {
//				Integer result = 0;
//				if ( o1.getAsn().getAsnId2() < o2.getAsn().getAsnId2() ) {
//					result = 1;
//				} else if ( o1.getAsn().getAsnId2() == o2.getAsn().getAsnId2() ) {
//					result = 0;
//				} else {
//					result = -1;
//				}
				return o1.getAsn().getUpdateTime().compareTo(o2.getAsn().getUpdateTime())*-1;
			}
		});
		dd.end(true);
		return listRecAsnVO;
	}
	
    /**
     * 导入ASN单
     * @param param_listRecAsnVO 导入的asn单集合
     * @throws Exception
     * @version 2017年2月20日 上午11:46:18<br/>
     * @author andy wang<br/>
     */
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=false)
    public String upload ( List<RecAsnVO> param_listRecAsnVO ) throws Exception {
    	if ( PubUtil.isEmpty(param_listRecAsnVO) ) {
    		log.error("importASN --> param_listRecAsnVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	Map<String,RecAsnVO> mapRecAsn = new HashMap<String,RecAsnVO>();
    	
    	StringBuffer sb = new StringBuffer("");
    	for (int i = 0; i < param_listRecAsnVO.size(); i++) {
    		String msgFormat = "行" + (i+2) + "：%s<br/>";
    		RecAsnVO imptRecAsnVO = param_listRecAsnVO.get(i);
    		RecAsn imptAsn = imptRecAsnVO.getAsn();
    		RecUtil.defOrderQWV(imptAsn);
    		sb = sb.append(this.checkExcel(msgFormat,imptRecAsnVO));
    		String poNo = imptAsn.getPoNo();
			RecAsnDetail saveRecAsnDetail = imptRecAsnVO.getSaveRecAsnDetail();
			if ( StringUtil.isTrimEmpty(saveRecAsnDetail.getBatchNo()) ) {
				saveRecAsnDetail.setBatchNo("");
			}
			if ( saveRecAsnDetail.getOrderQty() <= 0 ) {
				sb = sb.append(String.format(msgFormat, "数量不能小于等于0"));
			}
			
			// 查询货主信息
			if ( !StringUtil.isTrimEmpty(imptRecAsnVO.getOwnerMerchant().getMerchantNo()) ) {
				MetaMerchant ownerMerchant = FqDataUtils.getMerchantByNo(this.merchantService, imptRecAsnVO.getOwnerMerchant().getMerchantNo());
				if ( ownerMerchant == null ) {
					sb = sb.append(String.format(msgFormat, "ASN导入模板上的货主不存在，请重新选择！"));
				} else {
					if ( ownerMerchant.getIsOwner() == null || !ownerMerchant.getIsOwner() ) {
						sb = sb.append(String.format(msgFormat, "ASN导入模板上的货主【" + ownerMerchant.getMerchantName() + "】不存在，请重新选择！"));
					}
					imptRecAsnVO.setOwnerMerchant(ownerMerchant);
					imptAsn.setOwner(ownerMerchant.getMerchantId());
				}
			}
			
			
			// 查询发货方信息
			if ( imptRecAsnVO.getSenderMerchant() != null 
					&& !StringUtil.isTrimEmpty(imptRecAsnVO.getSenderMerchant().getMerchantNo()) ) {
				MetaMerchant sendMerchant = FqDataUtils.getMerchantByNo(this.merchantService, imptRecAsnVO.getSenderMerchant().getMerchantNo());
				if ( sendMerchant == null ) {
					sb = sb.append(String.format(msgFormat, "ASN导入模板上的发货方不存在，请重新选择！"));
				} else {
					if ( sendMerchant.getIsSender() == null || !sendMerchant.getIsSender() ) {
						sb = sb.append(String.format(msgFormat, "ASN导入模板上的发货方【" + sendMerchant.getMerchantName() + "】不存在，请重新选择！"));
					}
					if ( StringUtil.isTrimEmpty(imptAsn.getContactPerson()) ) {
						imptAsn.setContactPerson(sendMerchant.getContactPerson());
					}
					if ( StringUtil.isTrimEmpty(imptAsn.getContactPhone()) ) {
						imptAsn.setContactPhone(sendMerchant.getContactPhone());
					}
					if ( StringUtil.isTrimEmpty(imptAsn.getContactAddress()) ) {
						imptAsn.setContactAddress(sendMerchant.getContactAddress());
					}
				}
			}
			
			
			// 查询货品信息
			if ( !StringUtil.isTrimEmpty(imptAsn.getOwner()) && !StringUtil.isTrimEmpty(imptRecAsnVO.getSkuNo()) ) {
				MetaSku p_sku = new MetaSku();
				p_sku.setOwner(imptAsn.getOwner());
				p_sku.setSkuNo(imptRecAsnVO.getSkuNo());
				Principal p = LoginUtil.getLoginUser();
				ResultModel rm = this.skuService.list(new SkuVo(p_sku), p);
				List<SkuVo> listSku = rm.getList();
				if ( PubUtil.isEmpty(listSku) ) {
					sb = sb.append(String.format(msgFormat, "系统中当前货主【" 
							+ imptRecAsnVO.getOwnerMerchant().getMerchantNo() + "】无此货品【" 
							+ imptRecAsnVO.getSkuNo() + "】，请重新选择！"));
				} else {
					MetaSku sku = listSku.get(0).getEntity();
					if ( imptRecAsnVO.getSaveRecAsnDetail() != null 
							&& imptRecAsnVO.getSaveRecAsnDetail().getMeasureUnit() != null 
							&& !imptRecAsnVO.getSaveRecAsnDetail().getMeasureUnit().equals(sku.getMeasureUnit()) ) {
						sb = sb.append(String.format(msgFormat, "ASN导入模板上的计量单位与货品计量单位不一致，请重新选择！"));
					}
					saveRecAsnDetail.setSkuId(sku.getSkuId());
				}
			}
			
			// 合并ASN单对象,源单号为空的情况无法判断以下校验
			if ( saveRecAsnDetail != null && !StringUtil.isTrimEmpty(poNo) ) {
    			RecAsnVO asnVO = mapRecAsn.get(poNo);
    			RecAsn asn = null;
    			if ( asnVO == null ) {
    				asnVO = imptRecAsnVO;
    				asn = imptRecAsnVO.getAsn();
    				mapRecAsn.put(poNo, asnVO);
    			} else {
    				asn = asnVO.getAsn();
    			}
    			// 校验相同明细，ASN单不能存在相同批次的货品
    			List<String> listSkuId = asnVO.getListSkuId();
    			if ( listSkuId.contains(saveRecAsnDetail.getSkuId()+"_"+saveRecAsnDetail.getBatchNo()) ) {
    				sb = sb.append(String.format(msgFormat, "ASN导入模板上同一个源单单号不能存在相同批次的货品，请重新选择！"));
    			}
    			listSkuId.add(saveRecAsnDetail.getSkuId()+"_"+saveRecAsnDetail.getBatchNo());
    			// 校验相同货主，同一个ASN不能存在两个不同的货主
    			List<String> listOwnerId = asnVO.getListOwnerId();
    			if ( !PubUtil.isEmpty(listOwnerId) && !listOwnerId.contains(imptAsn.getOwner()) ) {
    				sb = sb.append(String.format(msgFormat, "ASN导入模板上同一个源单单号不能存在两个不同的货主，请重新选择！"));
    			}
    			listOwnerId.add(imptAsn.getOwner());
    			// 根据PO单号判断是否已经存在
				RecAsnVO p_recAsnVO = new RecAsnVO();
    			p_recAsnVO.getAsn().setPoNo(poNo);
//    			p_recAsnVO.setShowCancel(false);
    			RecAsn existsRecAsn = this.asnExtlService.findAsnByExample(p_recAsnVO);
    			if ( existsRecAsn != null ) {
    				// 有相同PO单号的ASN存在时，不能进行导入
    				sb = sb.append(String.format(msgFormat, "ASN导入模板上的源单单号已存在，请重新选择！"));
    			}
    			// 合并Asn单明细
    			List<RecAsnDetail> listSaveAsnDetail = asnVO.getListSaveAsnDetail();
    			if ( listSaveAsnDetail == null ) {
    				listSaveAsnDetail = new ArrayList<RecAsnDetail>();
    				imptRecAsnVO.setListSaveAsnDetail(listSaveAsnDetail);
    			}
    			if ( StringUtil.isTrimEmpty(saveRecAsnDetail.getBatchNo()) ) {
    				saveRecAsnDetail.setBatchNo("");
    			}
				listSaveAsnDetail.add(saveRecAsnDetail);
				// 统计Asn数量/重量/体积
				RecUtil.defOrderQWV(saveRecAsnDetail);
				asn.setOrderQty(asn.getOrderQty()+saveRecAsnDetail.getOrderQty());
				asn.setOrderWeight(NumberUtil.add(asn.getOrderWeight(), saveRecAsnDetail.getOrderWeight()));
				asn.setOrderVolume(NumberUtil.add(asn.getOrderVolume(), saveRecAsnDetail.getOrderVolume()));
    		}
		}
    	if (sb.length() > 0 ) {
    		throw new BizException("{{"+sb.toString()+"}}");
    	}
    	Principal loginUser = LoginUtil.getLoginUser();
//		CommonVo p_commonVO = null;
    	for (RecAsnVO recAsnVO : mapRecAsn.values()) {
    		String asnId = IdUtil.getUUID();
    		RecAsn asn = recAsnVO.getAsn();
			asn.setAsnId(asnId);
			String docType = paramService.getValue(CacheName.ASN_DOCTYPE_REVERSE, recAsnVO.getDocTypeComment());
			if ( StringUtil.isTrimEmpty(docType) ) {
				throw new BizException("err_rec_asn_docType_unqualified");
			}
			asn.setDocType(Integer.valueOf(docType));
			// 生成编号
//			p_commonVO = new CommonVo(loginUser.getOrgId(), RecUtil.type2NoPrefix(asn.getDocType()));
			String asnNo = this.context.getStrategy4No(loginUser.getOrgId(), LoginUtil.getWareHouseId()).getAsnNo(loginUser.getOrgId(), asn.getDocType());
			asn.setWarehouseId(LoginUtil.getWareHouseId());
			asn.setOrgId(loginUser.getOrgId());
			asn.setAsnNo(asnNo);
			asn.setOrgId(loginUser.getOrgId());
			asn.setDataFrom(Constant.ASN_DATAFROM_IMPORT);
			asn.setAsnStatus(Constant.ASN_STATUS_OPEN);
			asn.setOrderDate(new Date());
			
    		// 保存ASN单
    		this.asnExtlService.insertAsn(asn);
    		List<RecAsnDetail> listSaveAsnDetail = recAsnVO.getListSaveAsnDetail();
    		for (int i = 0; i < listSaveAsnDetail.size(); i++) {
    			RecAsnDetail recAsnDetail = listSaveAsnDetail.get(i);
    			recAsnDetail.setAsnDetailId(IdUtil.getUUID());
    			recAsnDetail.setAsnId(asnId);
    			recAsnDetail.setWarehouseId(asn.getWarehouseId());
    			recAsnDetail.setOrgId(asn.getOrgId());
			}
    		// 保存ASN单明细
    		this.asnDetailExtlService.insertAsnDetail(recAsnVO.getListSaveAsnDetail());
		}
    	return "";
    }
    
    /**
     * 校验Excel导入的数据
	 * @param imptRecAsnVO
	 * @return
	 * @version 2017年5月16日 上午10:24:38<br/>
	 * @author andy wang<br/>
	 */
	private String checkExcel( String msgFormat , RecAsnVO imptRecAsnVO) {
		StringBuffer message = new StringBuffer("");
		if ( imptRecAsnVO.getAsn() == null ) {
			imptRecAsnVO.setAsn(new RecAsn());
		}
		if ( imptRecAsnVO.getSaveRecAsnDetail() == null ) {

			imptRecAsnVO.setSaveRecAsnDetail(new RecAsnDetail());
		}
		// 校验非空
		if ( imptRecAsnVO.getOwnerMerchant() == null 
				|| StringUtil.isTrimEmpty(imptRecAsnVO.getOwnerMerchant().getMerchantNo()) ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的货主不能为空，请重新选择！"));
		}
		if ( StringUtil.isTrimEmpty(imptRecAsnVO.getDocTypeComment()) ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的单据类型不能为空，请重新选择！"));
		}
		if ( imptRecAsnVO.getAsn().getPoNo() == null ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的源单号不能为空，请重新选择！"));
		}
		if ( imptRecAsnVO.getAsn().getOrderDate() == null ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的订单日期不能为空，且格式如：2017/6/12"));
		}
		if ( StringUtil.isTrimEmpty(imptRecAsnVO.getSkuNo())  ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的货品代码不能为空，请重新选择！"));
		}
		if ( imptRecAsnVO.getSaveRecAsnDetail().getOrderQty() == null  ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的订单数量不能为空，请重新选择！"));
		}
//		if ( StringUtil.isTrimEmpty(imptRecAsnVO.getSaveRecAsnDetail().getMeasureUnit())  ) {
//			message = message.append(String.format(msgFormat,"ASN导入模板上的计量单位不能为空，请重新选择！"));
//		}
		if ( StringUtils.isBlank(imptRecAsnVO.getSaveRecAsnDetail().getBatchNo())  ) {
			message = message.append(String.format(msgFormat,"ASN导入模板上的生产日期不能为空，请重新选择！"));
		}
		return message.toString();
	}

	/**
     * 更新Asn单
     * @param param_recAsnVO Asn单信息
     * @throws Exception
     * @version 2017年2月18日 下午2:44:59<br/>
     * @author andy wang<br/>
     * @return 
     */
    public RecAsnVO update ( RecAsnVO param_recAsnVO ) throws Exception {
    	if ( param_recAsnVO == null || param_recAsnVO.getAsn() == null ) {
			log.error("updateASN --> param_recAsnVO or asn is null!");
    		throw new NullPointerException("parameter is null!");
		}
    	
		//查询是否有关联的申请单
    	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
    	List<String> statusList = new ArrayList<String>();
    	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_ALL_EXMINE));
    	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
    	applicationVo.setStatusNotIn(statusList);
    	applicationVo.getEntity().setAsnId(param_recAsnVO.getAsn().getAsnId());
    	List<DeliverGoodsApplicationVo> qryList = deliverGoodsApplicationService.qryList(applicationVo);
    	if (qryList != null && !qryList.isEmpty()) {
    		throw new BizException("err_application_not_null");
    	}
    	
    	RecAsn param_asn = param_recAsnVO.getAsn();
    	RecAsn recAsn = this.asnExtlService.findAsnById(param_asn.getAsnId());
    	if ( recAsn == null ) {
    		throw new BizException("err_rec_asn_null");
    	}
    	if ( recAsn.getAsnStatus() == null || Constant.ASN_STATUS_OPEN != recAsn.getAsnStatus() ) {
    		throw new BizException("err_rec_asn_update_statusNotOpen");
    	}
    	// 20170621 黄经理决定有父编号的ASN单不能修改PO号
    	if ( !StringUtil.isTrimEmpty(recAsn.getParentAsnId()) && !recAsn.getPoNo().equals(param_recAsnVO.getAsn().getPoNo()) ) {
    		throw new BizException("err_rec_asn_update_splitCannotUpdatePoNo");
    	}
    	// 校验源单号唯一性
//    	if ( !StringUtil.isTrimEmpty(param_asn.getPoNo()) && StringUtil.isTrimEmpty(recAsn.getParentAsnId()) ) {
//    		RecAsnVO p_asnVO = new RecAsnVO();
//    		p_asnVO.getAsn().setPoNo(param_asn.getPoNo());
//    		p_asnVO.setShowCancel(false);
//    		List<RecAsn> listPoNoAsn = this.asnExtlService.listAsnByExample(p_asnVO);
//    		for (RecAsn recPoNoAsn : listPoNoAsn) {
//				if ( !recPoNoAsn.getAsnId().equals(recAsn.getAsnId())  ) {
//					throw new BizException("err_rec_asn_poNo_exists");
//				}
//			}
//    	}
    	List<String> listUpdateDetailId = param_recAsnVO.getListUpdateDetailId();
    	List<String> listDelDetailId = param_recAsnVO.getListDelDetailId();
    	List<RecAsnDetailVO> listAsnDetailVO = param_recAsnVO.getListAsnDetailVO();
//    	List<RecAsnDetail> listAsnDetailVO = param_recAsnVO.getListSaveAsnDetail();
    	// 查询Asn单明细
    	Map<String, RecAsnDetail> mapSourceAsnDetail = this.asnDetailExtlService.mapAsnDetailByAsnId(param_asn.getAsnId());
    	// 统计数量
    	Double totalQty = 0d;
    	Double totalVolume = 0d;
    	Double totalWeight = 0d;
    	
    	// 校验货品重复
    	// 抽取新增/更新的明细项
    	List<String> listOpera = new ArrayList<String>();
    	List<RecAsnDetail> listInsertDetail = new ArrayList<RecAsnDetail>();
    	List<RecAsnDetail> listUpdateDetail = new ArrayList<RecAsnDetail>();
    	for (int i = 0; i < listAsnDetailVO.size(); i++) {
    		RecAsnDetailVO saveRecAsnDetailVO = listAsnDetailVO.get(i);
    		RecAsnDetail saveRecAsnDetail = saveRecAsnDetailVO.getAsnDetail();
    		if ( saveRecAsnDetail == null ) {
    			listAsnDetailVO.remove(i--);
    			continue;
    		}
        	//批次号转换（界面上的是生产日期，数据库存的是批次号）
    		saveRecAsnDetail.setBatchNo(saveRecAsnDetail.getBatchNo().replaceAll("/", ""));
    		if ( StringUtil.isTrimEmpty(saveRecAsnDetail.getAsnDetailId())) {
    			// 新增的明细 - 保存Asn单明细
    			saveRecAsnDetail.setAsnId(recAsn.getAsnId());
    			listInsertDetail.add(saveRecAsnDetail);
    		} else if ( listUpdateDetailId.contains(saveRecAsnDetail.getAsnDetailId()) 
    				&& mapSourceAsnDetail.containsKey(saveRecAsnDetail.getAsnDetailId())
    				&& !listDelDetailId.contains(saveRecAsnDetail.getAsnDetailId()) ) {
    			listUpdateDetail.add(saveRecAsnDetail);
    		} else {
    			continue;
    		}
    		// 校验重复批次货品
    		if ( listOpera.contains(this.getDetailKey(saveRecAsnDetail)) ) {
    			throw new BizException("err_rec_asn_sameDetail");
    		}
			listOpera.add(this.getDetailKey(saveRecAsnDetail));
			RecUtil.defOrderQWV(saveRecAsnDetail);
			totalQty += saveRecAsnDetail.getOrderQty();
			totalVolume = NumberUtil.add(totalVolume, saveRecAsnDetail.getOrderVolume());
			totalWeight = NumberUtil.add(totalWeight, saveRecAsnDetail.getOrderWeight());
    	}

    	// 对此次没有操作的明细项的订单数/重/体进行统计
    	for (RecAsnDetail sourceRecAsnDetail : mapSourceAsnDetail.values()) {
			if ( !listUpdateDetailId.contains(sourceRecAsnDetail.getAsnDetailId())
					&& !listDelDetailId.contains(sourceRecAsnDetail.getAsnDetailId()) ) {
				// 校验重复批次货品
	    		if ( listOpera.contains(this.getDetailKey(sourceRecAsnDetail)) ) {
	    			throw new BizException("err_rec_asn_sameDetail");
	    		}
				listOpera.add(this.getDetailKey(sourceRecAsnDetail));
				// 此次操作，没有进行更新和删除操作的明细项，需进行统计
				RecUtil.defOrderQWV(sourceRecAsnDetail);
				totalQty += sourceRecAsnDetail.getOrderQty();
        		totalVolume = NumberUtil.add(totalVolume, sourceRecAsnDetail.getOrderVolume());
        		totalWeight = NumberUtil.add(totalWeight, sourceRecAsnDetail.getOrderWeight());
			}
		}
    	// 更新Asn单
    	param_asn.setOrderQty(totalQty);
    	param_asn.setOrderVolume(totalVolume);
    	param_asn.setOrderWeight(totalWeight);
    	this.asnExtlService.updateAsn(new RecAsnVO(param_asn));
    	// 删除Asn单明细
    	if ( !PubUtil.isEmpty(listDelDetailId) ) {
    		this.asnDetailExtlService.deleteListByAsnDetailId(listDelDetailId);
    	}
    	// 更新Asn单明细
    	for (RecAsnDetail recAsnDetail : listUpdateDetail) {
    		// 更新记录中有对应的明细id,更新Asn单明细
			this.asnDetailExtlService.updateAsnDetail(recAsnDetail);
		}
    	// 保存Asn单明细
    	for (RecAsnDetail recAsnDetail : listInsertDetail) {
    		RecAsnDetail insertAsnDetail = this.asnDetailExtlService.insertAsnDetail(recAsnDetail);
    		recAsnDetail.setAsnDetailId(insertAsnDetail.getAsnDetailId());
		}
    	return param_recAsnVO;
    }
    
    /**
     * 批量收货确认
     * @param param_recAsnVO 收货确认数据<br/>
     * —— 提供listAsnId，locationId
     * @throws Exception
     * @version 2017年2月18日 下午2:08:49<br/>
     * @author andy wang<br/>
     */
    @Transactional(readOnly=false)
	public void batch( RecAsnVO param_recAsnVO ) throws Exception {
    	if ( param_recAsnVO == null ) {
			log.error("batchConfirmASN --> param_recAsnVO is null!");
    		throw new NullPointerException("parameter is null!");
		}
    	List<String> listAsnId = param_recAsnVO.getListAsnId();
    	String locationId = param_recAsnVO.getLocationId();
    	for (int i = 0; i < listAsnId.size(); i++) {
    		String asnId = listAsnId.get(i);
    		RecAsnVO recAsnVO = new RecAsnVO();
    		recAsnVO.getAsn().setAsnId(asnId);
    		if(param_recAsnVO.getAsn() != null) recAsnVO.getAsn().setOpPerson(param_recAsnVO.getAsn().getOpPerson());
    		recAsnVO.getAsn().setOpTime(new Date());
    		List<RecAsnDetail> listAsnDetail = this.asnDetailExtlService.listAsnDetailByAsnId(asnId);
    		if ( listAsnDetail != null ) {
    			for (int j = 0; j < listAsnDetail.size(); j++) {
    				RecAsnDetail recAsnDetail = listAsnDetail.get(j);
    				RecUtil.defOrderQWV(recAsnDetail);
    				recAsnDetail.setReceiveQty(recAsnDetail.getOrderQty());
    				recAsnDetail.setReceiveWeight(recAsnDetail.getOrderWeight());
    				recAsnDetail.setReceiveVolume(recAsnDetail.getOrderVolume());
    				recAsnDetail.setLocationId(locationId);
				}
    			recAsnVO.setListSaveAsnDetail(listAsnDetail);
    		}
    		// 调用收货确认接口
    		this.complete(recAsnVO);
		}
    }
    
    
    /**
     * 收货确认
     * @param param_recAsnVO 确认收货的Asn单（asnId,listSavetAsnDetail不能为空）
     * @throws Exception
     * @version 2017年2月16日 下午4:10:04<br/>
     * @author andy wang<br/>
     */
    @Transactional(readOnly=false)
	public void complete( RecAsnVO param_recAsnVO ) throws Exception {
		if ( param_recAsnVO == null 
				|| param_recAsnVO.getListSaveAsnDetail() == null 
				|| param_recAsnVO.getListSaveAsnDetail().isEmpty()
				|| param_recAsnVO.getAsn() == null 
				|| StringUtil.isTrimEmpty(param_recAsnVO.getAsn().getAsnId()) ) {
			log.error("confirmASN --> param_recAsnVO or listSavetAsnDetail or AsnId is null!");
    		throw new NullPointerException("parameter is null!");
		}
		// 查询Asn单
		RecAsn param_asn = param_recAsnVO.getAsn();
		String asnId = param_recAsnVO.getAsn().getAsnId();
		RecAsn recAsn = this.asnExtlService.findAsnById(asnId);
		if ( recAsn == null ) {
			throw new BizException("err_rec_asn_null");
		}
		// 校验Asn单状态
		if ( Constant.ASN_STATUS_ACTIVE != recAsn.getAsnStatus() ) {
			throw new BizException("err_rec_asn_confirm_statusNotActive");
		}
		Principal loginUser = LoginUtil.getLoginUser();
		// 校验Asn单明细
		List<RecAsnDetailVO> listAsnDetailVO = param_recAsnVO.getListAsnDetailVO();
		if ( PubUtil.isEmpty(listAsnDetailVO) && !PubUtil.isEmpty(param_recAsnVO.getListSaveAsnDetail()) ) {
			listAsnDetailVO = new ArrayList<RecAsnDetailVO>();
			// 20170717 解决Web与PDA提交参数不一致的情况
			for (RecAsnDetail recAsnDetail : param_recAsnVO.getListSaveAsnDetail()) {
				listAsnDetailVO.add(new RecAsnDetailVO(recAsnDetail));
			}
			param_recAsnVO.setListAsnDetailVO(listAsnDetailVO);
		}
		// 收货数量/重量/体积不得大于订单数量/重量/体积
		for(RecAsnDetailVO vo : listAsnDetailVO) {
			if(vo.getAsnDetail().getReceiveQty() != null && vo.getAsnDetail().getOrderQty() != null 
				&& vo.getAsnDetail().getReceiveQty() > vo.getAsnDetail().getOrderQty() ) throw new BizException("err_rec_asn_rec_qty");
			if(vo.getAsnDetail().getReceiveWeight() != null && vo.getAsnDetail().getOrderWeight() != null 
					&& vo.getAsnDetail().getReceiveWeight() > vo.getAsnDetail().getOrderWeight() ) throw new BizException("err_rec_asn_rec_weight");
			if(vo.getAsnDetail().getReceiveVolume() != null && vo.getAsnDetail().getOrderVolume() != null 
					&& vo.getAsnDetail().getReceiveVolume() > vo.getAsnDetail().getOrderVolume() ) throw new BizException("err_rec_asn_rec_volume");
		}
		Map<String,RecAsnDetail> mapSaveRecAsnDetailId = new HashMap<String,RecAsnDetail>();
		MetaLocation defLoc = this.locExtlService.findDefTempLoc();
		for (int i = 0; i < listAsnDetailVO.size(); i++) {
			RecAsnDetailVO recAsnDetailVO = listAsnDetailVO.get(i);
			RecAsnDetail saveRecAsnDetail = recAsnDetailVO.getAsnDetail();
			if (saveRecAsnDetail.getSkuStatus() == Constant.STOCK_SKU_STATUS_ABNORMAL) {
				saveRecAsnDetail.setAsnDetailId(IdUtil.getUUID());
			}
			if ( saveRecAsnDetail == null || StringUtil.isTrimEmpty(saveRecAsnDetail.getAsnDetailId()) ) {
				log.error("confirmASN --> param_recAsnVO or listSavetAsnDetail is null!");
				throw new NullPointerException("saveDetail is null!");
			}
			RecUtil.defReceiveQWV(saveRecAsnDetail);
			if ( saveRecAsnDetail.getReceiveQty() < 0 
					|| saveRecAsnDetail.getReceiveWeight() < 0 
					|| saveRecAsnDetail.getReceiveVolume() < 0 ) {
				throw new BizException("err_rec_asn_confirm_canNotNegative");
			}
			if ( saveRecAsnDetail.getReceiveQty() != null && saveRecAsnDetail.getReceiveQty() > 0 ) {
				if ( saveRecAsnDetail.getReceiveWeight() == null || saveRecAsnDetail.getReceiveWeight() <= 0 ) {
					MetaSku sku = FqDataUtils.getSkuById(this.skuService, saveRecAsnDetail.getSkuId());
					if ( sku != null && sku.getPerWeight() != null ) {
						saveRecAsnDetail.setReceiveWeight(NumberUtil.mul(saveRecAsnDetail.getReceiveQty(), sku.getPerWeight(),2));
					}
				}
				if ( saveRecAsnDetail.getReceiveVolume() == null || saveRecAsnDetail.getReceiveVolume() <= 0 ) {
					MetaSku sku = FqDataUtils.getSkuById(this.skuService, saveRecAsnDetail.getSkuId());
					if ( sku != null && sku.getPerWeight() != null ) {
						saveRecAsnDetail.setReceiveVolume(NumberUtil.mul(saveRecAsnDetail.getReceiveQty(), sku.getPerVolume(),6));
					}
				}
			}
			// 没有选择收货库位时，默认选择DOCK-收货
			if ( StringUtil.isTrimEmpty(saveRecAsnDetail.getLocationId()) ) {
				if ( !StringUtil.isTrimEmpty(recAsnDetailVO.getLocationNo()) ) {
					MetaLocation loc = FqDataUtils.getLocByNo(this.locExtlService, recAsnDetailVO.getLocationNo());
					if ( loc == null ) {
						throw new BizException("err_main_location_null");
					}
					if ( loc.getLocationType() != Constant.LOCATION_TYPE_TEMPSTORAGE ) {
						throw new BizException("err_rec_asn_confirm_locNotTemp");
					}
					saveRecAsnDetail.setLocationId(loc.getLocationId());
				} else if ( defLoc == null ) {
					throw new BizException("err_main_location_choiceLocation");
				} else {
					saveRecAsnDetail.setLocationId(defLoc.getLocationId());
				}
			}
			mapSaveRecAsnDetailId.put(saveRecAsnDetail.getAsnDetailId(),saveRecAsnDetail);
		}
		// 查询原Asn单明细
		List<RecAsnDetail> listSourceAsnDetail = this.asnDetailExtlService.listAsnDetailByAsnId(asnId);
		// 明细不能为空
		if ( listSourceAsnDetail == null || listSourceAsnDetail.isEmpty() ) {
			throw new BizException("err_rec_asn_confirm_detailEmpty");
		}
		int status = Constant.ASN_STATUS_RECEIVED;
		Double totalReceiveQty = 0d ;
		Double totalReceiveWeight = 0d;
		Double totalReceiveVolume = 0d;
		for (int i = 0; i < listSourceAsnDetail.size(); i++) {
			RecAsnDetail sourceRecAsnDetail = listSourceAsnDetail.get(i);
			RecAsnDetail saveRecAsnDetail = mapSaveRecAsnDetailId.get(sourceRecAsnDetail.getAsnDetailId());
			RecUtil.defOrderQWV(sourceRecAsnDetail);
			if ( saveRecAsnDetail == null 
					|| saveRecAsnDetail.getReceiveQty() < saveRecAsnDetail.getOrderQty() ) {
				status = Constant.ASN_STATUS_PARTRECEIVE;
			}
			if ( saveRecAsnDetail == null ) {
				saveRecAsnDetail = new RecAsnDetail();
				saveRecAsnDetail.setAsnDetailId(sourceRecAsnDetail.getAsnDetailId());
				saveRecAsnDetail.setReceiveQty(0d);
				saveRecAsnDetail.setReceiveWeight(0d);
				saveRecAsnDetail.setReceiveVolume(0d);
			} else {
				mapSaveRecAsnDetailId.remove(sourceRecAsnDetail.getAsnDetailId());
			}
			totalReceiveQty = NumberUtil.add(totalReceiveQty, saveRecAsnDetail.getReceiveQty());
			totalReceiveWeight = NumberUtil.add(totalReceiveWeight, saveRecAsnDetail.getReceiveWeight());
			totalReceiveVolume = NumberUtil.add(totalReceiveVolume, saveRecAsnDetail.getReceiveVolume());
			if ( saveRecAsnDetail.getReceiveQty() != null && saveRecAsnDetail.getReceiveQty() > 0 ) {
				// 保存库存并记录库存日志
				InvStockVO invStockVO = RecUtil.createStock(recAsn, sourceRecAsnDetail
						,saveRecAsnDetail.getReceiveQty(),saveRecAsnDetail.getLocationId(),Constant.STOCK_LOG_OP_TYPE_IN);
				this.stockService.inStock(invStockVO);
			}
			// 保存明细
			this.asnDetailExtlService.confirmAsnDetail(saveRecAsnDetail);
		}
		List<RecAsnDetail> newDetailList = new ArrayList<RecAsnDetail>();
		for (String key : mapSaveRecAsnDetailId.keySet()) {
			RecAsnDetail saveRecAsnDetail = mapSaveRecAsnDetailId.get(key);
			newDetailList.add(saveRecAsnDetail);

			totalReceiveQty = NumberUtil.add(totalReceiveQty, saveRecAsnDetail.getReceiveQty());
			totalReceiveWeight = NumberUtil.add(totalReceiveWeight, saveRecAsnDetail.getReceiveWeight());
			totalReceiveVolume = NumberUtil.add(totalReceiveVolume, saveRecAsnDetail.getReceiveVolume());
			
			if ( saveRecAsnDetail.getReceiveQty() != null && saveRecAsnDetail.getReceiveQty() > 0 ) {
				// 保存库存并记录库存日志
				InvStockVO invStockVO = RecUtil.createStock(recAsn, saveRecAsnDetail
						,saveRecAsnDetail.getReceiveQty(),saveRecAsnDetail.getLocationId(),Constant.STOCK_LOG_OP_TYPE_IN);
				this.stockService.inStock(invStockVO);
			}
		}
		this.asnDetailExtlService.insertAsnDetailComfirm(newDetailList);
		recAsn.setReceiveQty(totalReceiveQty);
		recAsn.setReceiveWeight(totalReceiveWeight);
		recAsn.setReceiveVolume(totalReceiveVolume);
		// 保存ASN单
		recAsn.setAsnStatus(status);
		recAsn.setOpTime(new Date());
		if ( StringUtil.isTrimEmpty(param_asn.getOpPerson()) ) {
			// 没填写收货人员时，默认为当前登录人
			recAsn.setOpPerson(LoginUtil.getLoginUser().getUserId());
		} else {
			recAsn.setOpPerson(param_asn.getOpPerson());
		}
		this.asnExtlService.confirmAsn(recAsn);
		this.taskService.finishByOpId(recAsn.getAsnId(), loginUser.getUserId());
		if ( Constant.ASN_STATUS_RECEIVED != status ) {
			// 新增异常(ASN单不是全部收货时添加异常)
        	ExceptionLog expLog = new ExceptionLog();
			expLog.setExType(Constant.EXCEPTION_TYPE_REC_ABNORMAL);
			expLog.setExLevel(Constant.EXCEPTION_LEVEL_NORMAL);
			expLog.setInvolveBill(recAsn.getAsnNo());
			expLog.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
			expLog.setExDesc(String.format("单号：%s 。部分收货确认",recAsn.getAsnNo()));
			this.exceptionLogService.add(expLog);
		}
		
		// 保存企业业务统计信息
		ParkRentVo parkRentVo = new ParkRentVo();
		parkRentVo.getParkRent().setMerchantId(recAsn.getOrgId());//承租方
		parkRentVo.getParkRent().setWarehouseId(recAsn.getWarehouseId());
		parkRentVo.setOrderTime(new Date());
		List<ParkRentVo> rentList = this.parkRentService.qryList(parkRentVo);
		ParkRentVo rentVo = new ParkRentVo();
		if(rentList != null && !rentList.isEmpty()){
			rentVo = rentList.get(0);
		}
		
		ParkOrgBusiStasVo reqParam = new ParkOrgBusiStasVo();
		ParkOrgBusiStas parkOrgBusiStas = new ParkOrgBusiStas();
		reqParam.setParkOrgBusiStas(parkOrgBusiStas);
		parkOrgBusiStas.setMerchantId(recAsn.getOrgId());//承租方
		parkOrgBusiStas.setOrgId(rentVo.getParkRent().getOrgId());//出租方
		parkOrgBusiStas.setStasDate(new Date());
		parkOrgBusiStas.setDocNo(recAsn.getAsnNo());
		parkOrgBusiStas.setBusiType(recAsn.getDocType());
		parkOrgBusiStas.setStasQty(recAsn.getReceiveQty());
		parkOrgBusiStas.setStasWeight(recAsn.getReceiveWeight());
		parkOrgBusiStas.setStasVolume(recAsn.getReceiveVolume());
		parkOrgBusiStas.setCreatePerson(loginUser.getUserId());
		parkOrgBusiStas.setUpdatePerson(loginUser.getUserId());
		this.parkOrgBusiStasService.add(reqParam );
    }
    
	/**
	 * 更新ERP入库单
	 * @param _recAsnVo
	 * @throws Exception
	 */
	public ErpResult updateERPInventory(RecAsnVO _recAsnVo){
		//设置上送参数
		List<Map<String,String>> goodsList = new ArrayList<Map<String,String>>();
		if(_recAsnVo!= null && _recAsnVo.getListAsnDetailVO() != null && !_recAsnVo.getListAsnDetailVO().isEmpty()){
			for(RecAsnDetailVO asnDetailVo:_recAsnVo.getListAsnDetailVO()){
				if(!StringUtil.isTrimEmpty(asnDetailVo.getAsnDetail().getSkuId())){
					Map<String,String> skuMap = new HashMap<String,String>();
					//查询货品
					MetaSku sku = skuDao.selectByPrimaryKey(asnDetailVo.getAsnDetail().getSkuId());
					skuMap.put("hg_goods_no", sku.getSkuNo());
					skuMap.put("qty",asnDetailVo.getAsnDetail().getReceiveQty()+"");
					int quality = 1;
					if(Constant.STOCK_SKU_STATUS_ABNORMAL == asnDetailVo.getAsnDetail().getSkuStatus()) quality = 0;
					skuMap.put("quality",quality+"");
					goodsList.add(skuMap);
				}
			}
		}
		String goods = JsonUtil.toJson(goodsList);
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("inventory_no", _recAsnVo.getAsn().getPoNo());
		paramMap.put("goods", goods);
		paramMap.put("notify_time",new Date().getTime()+"");
		ErpResult result = new ErpResult();
		try {
			result = context.getStrategy4Erp().doInvoke(Constant.ERP_UPDATEINVENTORY,paramMap);
			//若同步失败，则asn单同步状态设为0
			Principal loginUser = LoginUtil.getLoginUser();
			RecAsn asn = new RecAsn();
			asn.setAsnId(_recAsnVo.getAsn().getAsnId());
			asn.setUpdatePerson(loginUser.getUserId());	
			if(result.getCode() != 1){
				asn.setSyncErpStatus(Constant.ASN_UNSYNC_STATUS);
			} else{
				asn.setSyncErpStatus(Constant.ASN_HASSYNC_STATUS);
			}
			asnDao.updateByPrimaryKeySelective(asn);
			//同步成功，则asn单同步状态为1
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
//			resMap.put("code", -1);
//			resMap.put("message", "SocketTimeoutException");
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
			throw new BizException("valid_rec_erp_error");
		}
		return result;
	}
	
    /**
	 * 查询ASN单列表
	 * @param param_recAsnVO 查询条件
	 * @return 分页对象（包含ASN单列表）
	 * @throws Exception
	 * 创建日期:<br/> 2017年2月9日 下午1:44:07<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly=true)
	public Page<RecAsnVO> list( RecAsnVO param_recAsnVO ) throws Exception {
		if ( param_recAsnVO == null ) {
			param_recAsnVO = new RecAsnVO();
		}
		if ( param_recAsnVO.getAsn() == null ) {
			param_recAsnVO.setAsn(new RecAsn());
		}
		// 根据客商名，模糊查询所有客商的id
		if ( !StringUtil.isTrimEmpty(param_recAsnVO.getLikeOwner()) ) {
			List<String> listOwnerId = this.merchantService.list(param_recAsnVO.getLikeOwner());
			if ( !PubUtil.isEmpty(listOwnerId) ) {
				param_recAsnVO.setListOwnerId(listOwnerId);
			} else {
				return null;
			}
		}
		// 根据创建人名，模糊查询所有人员的id
		if ( !StringUtil.isTrimEmpty(param_recAsnVO.getLikeCreatePerson()) ) {
			// 模糊查询所有客商的id
			List<String> listCPersonId = this.userService.list(param_recAsnVO.getLikeCreatePerson());
			if ( !PubUtil.isEmpty(listCPersonId) ) {
				param_recAsnVO.setListCPersonId(listCPersonId);
			} else {
				return null;
			}
		}
		// 默认不查询取消状态的数据
		if ( param_recAsnVO.getAsn().getAsnStatus() == null ) {
			param_recAsnVO.setShowCancel(false);
		}
		// 根据条件分页查询ASN单
		Page page = this.asnExtlService.listAsnByPage(param_recAsnVO);
		if ( PubUtil.isEmpty(page) ) return null;
		// 填充ASN单扩展信息
		List<RecAsnVO> listVO = new ArrayList<RecAsnVO>();
		Map<String,RecAsn> mapParentAsn = new HashMap<String,RecAsn>();
		for (int i = 0; i < page.size(); i++) {
			RecAsn asn = (RecAsn) page.get(i);
			// 查询Asn单缓存信息
			RecAsnVO recAsnVO = new RecAsnVO(asn).searchCache();
			recAsnVO.setSumRealPtwQty(0d);
			recAsnVO.setSumRealPtwWeight(0d);
			recAsnVO.setSumRealPtwVolume(0d);
			RecUtil.defOrderQWV(asn);
			RecUtil.defReceiveQWV(asn);
			listVO.add(recAsnVO);
			
	    	// 查询货主信息
			MetaMerchant owner = FqDataUtils.getMerchantById(merchantService, asn.getOwner());
			recAsnVO.setOwnerComment(owner.getMerchantName());
			recAsnVO.setHgMerchantNo(owner.getHgMerchantNo());
			// 查询客商信息
			recAsnVO.setOwnerMerchant(FqDataUtils.getMerchantById(this.merchantService, asn.getOwner()));
			// 查询仓库信息
			recAsnVO.setWarehouseComment(FqDataUtils.getWarehouseNameById(this.warehouseExtlService, asn.getWarehouseId()));
			// 查询创建人信息
			recAsnVO.setCreatePersonComment(FqDataUtils.getUserNameById(this.userService, asn.getCreatePerson()));
			// 查询修改人信息
			recAsnVO.setUpdatePersonComment(FqDataUtils.getUserNameById(this.userService, asn.getUpdatePerson()));
			// 查询作业人员信息
			recAsnVO.setOpPersonComment(FqDataUtils.getUserNameById(this.userService, asn.getOpPerson()));
			// 查询父ASN单编码
			if ( !StringUtil.isTrimEmpty(asn.getParentAsnId()) ) {
				RecAsn parentRecAsn = mapParentAsn.get(asn.getParentAsnId());
				if ( parentRecAsn == null ) {
					parentRecAsn = this.asnExtlService.findAsnById(asn.getParentAsnId());
				}
				if ( parentRecAsn != null ) {
					recAsnVO.setParentAsnComment(parentRecAsn.getAsnNo());
					mapParentAsn.put(asn.getParentAsnId(), parentRecAsn);
				}
			}
			RecUtil.defOrderQWV(asn);
			//汇总上架数量
			RecPutawayDetailVO sumPtwDetailVO = this.ptwDetailExtlService.sumPtwDetailNumByAsnId(asn.getAsnId());
			recAsnVO.setSumRealPtwQty(sumPtwDetailVO.getSumRealQty());
			recAsnVO.setSumRealPtwWeight(sumPtwDetailVO.getSumRealWeight());
			recAsnVO.setSumRealPtwVolume(sumPtwDetailVO.getSumRealVolume());
		}
		page.clear();
		page.addAll(listVO);
		return page;
	}
	
	/**
	 * 查找asn明细
	 * @param recAsnVO
	 * @throws Exception
	 */
	public void qryAsnDetail(RecAsnVO recAsnVO) throws Exception{
		// 查询ASN单明细
		List<RecAsnDetailVO> listAsnDetailVO = new ArrayList<RecAsnDetailVO>();
		List<RecAsnDetail> listAsnDetail = this.asnDetailExtlService.listSkuByAsnId(recAsnVO.getAsn().getAsnId());
		Map<String,RecAsnDetailVO> mapAsnDetailVO = new HashMap<String,RecAsnDetailVO>();
		if ( listAsnDetail != null && !listAsnDetail.isEmpty() ) {
			for (int i = 0; i < listAsnDetail.size(); i++) {
				RecAsnDetail detail = listAsnDetail.get(i);
				RecUtil.defOrderQWV(detail);
				RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO(detail).searchCache();
				mapAsnDetailVO.put(detail.getAsnDetailId(), recAsnDetailVO);
				listAsnDetailVO.add(recAsnDetailVO);
				// 查询货品信息/规格型号
				MetaSku sku = FqDataUtils.getSkuById(this.skuService, detail.getSkuId());
				if ( sku != null ) {
					recAsnDetailVO.setSku(sku);
					recAsnDetailVO.setSkuNo(sku.getSkuNo());
					recAsnDetailVO.setSkuComment(sku.getSkuName());
					recAsnDetailVO.setSpecModelComment(sku.getSpecModel());
					recAsnDetailVO.setPerVolume(sku.getPerVolume());
					recAsnDetailVO.setPerWeight(sku.getPerWeight());
					recAsnDetailVO.setSkuBarCode(sku.getSkuBarCode());
				}
				// 查询包装单位信息
				recAsnDetailVO.setPackComment(FqDataUtils.getPackUnit(this.packService, detail.getPackId()));
				// 查询库位
				MetaLocation loc = FqDataUtils.getLocById(this.locExtlService, detail.getLocationId());
				if ( loc != null ) {
					recAsnDetailVO.setLocationComment(loc.getLocationName());
					recAsnDetailVO.setLocationNo(loc.getLocationNo());
				}
				//金额
				if(detail.getReceiveQty() != null && sku.getPerPrice() != null) {
					Double m = NumberUtil.mul(detail.getReceiveQty(), sku.getPerPrice());
					recAsnDetailVO.setMoney(NumberUtil.rounded(m, 2));
				}
			}
		}
		recAsnVO.setListAsnDetailVO(listAsnDetailVO);
	}
    
	
	/**
	 * 根据asn单id查询asn信息
	 * @param asnId asn单id
	 * @return asn单信息
	 * @throws Exception
	 * 创建日期:<br/> 2017年2月10日 下午2:39:51<br/>
	 * @author andy wang<br/>
	 */
	@Transactional(readOnly=true)
    public RecAsnVO view ( String param_asnId ) throws Exception {
    	if ( StringUtil.isTrimEmpty(param_asnId) ) {
    		log.error("viewASN --> param_asnId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	// 根据asnId，查询asn单
    	RecAsn recAsn = this.asnExtlService.viewById(param_asnId);
    	if ( recAsn == null ) {
    		throw new BizException("err_rec_asn_null");
    	}
    	RecAsnVO recAsnVO = new RecAsnVO(recAsn).searchCache();
		// 查询仓库信息
    	MetaWarehouse warehouse = warehouseExtlService.findWareHouseById(recAsn.getWarehouseId());
    	recAsnVO.setWarehouseNo(warehouse.getWarehouseNo());
		recAsnVO.setWarehouseComment(FqDataUtils.getWarehouseNameById(this.warehouseExtlService, recAsn.getWarehouseId()));
    	// 查询货主信息
		MetaMerchant owner = FqDataUtils.getMerchantById(merchantService, recAsn.getOwner());
		recAsnVO.setOwnerComment(owner.getMerchantName());
		recAsnVO.setHgMerchantNo(owner.getHgMerchantNo());
		// 查询发货方信息
		recAsnVO.setSenderComment(FqDataUtils.getMerchantNameById(this.merchantService, recAsn.getSender()));
		// 查询ASN单明细
		List<RecAsnDetailVO> listAsnDetailVO = new ArrayList<RecAsnDetailVO>();
		List<RecAsnDetail> listAsnDetail = this.asnDetailExtlService.listSkuByAsnId(param_asnId);
		Map<String,RecAsnDetailVO> mapAsnDetailVO = new HashMap<String,RecAsnDetailVO>();
		if ( listAsnDetail != null && !listAsnDetail.isEmpty() ) {
			for (int i = 0; i < listAsnDetail.size(); i++) {
				RecAsnDetail detail = listAsnDetail.get(i);
				RecUtil.defOrderQWV(detail);
				RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO(detail).searchCache();
				mapAsnDetailVO.put(detail.getAsnDetailId(), recAsnDetailVO);
				listAsnDetailVO.add(recAsnDetailVO);
				// 查询货品信息/规格型号
				MetaSku sku = FqDataUtils.getSkuById(this.skuService, detail.getSkuId());
				if ( sku != null ) {
					recAsnDetailVO.setSku(sku);
					recAsnDetailVO.setSkuNo(sku.getSkuNo());
					recAsnDetailVO.setSkuComment(sku.getSkuName());
					recAsnDetailVO.setSpecModelComment(sku.getSpecModel());
					recAsnDetailVO.setPerVolume(sku.getPerVolume());
					recAsnDetailVO.setPerWeight(sku.getPerWeight());
					recAsnDetailVO.setSkuBarCode(sku.getSkuBarCode());
				}
				// 查询包装单位信息
				recAsnDetailVO.setPackComment(FqDataUtils.getPackUnit(this.packService, detail.getPackId()));
				// 查询库位
				MetaLocation loc = FqDataUtils.getLocById(this.locExtlService, detail.getLocationId());
				if ( loc != null ) {
					recAsnDetailVO.setLocationComment(loc.getLocationName());
					recAsnDetailVO.setLocationNo(loc.getLocationNo());
				}
				//金额
				if(detail.getReceiveQty() != null && sku.getPerPrice() != null) {
					Double m = NumberUtil.mul(detail.getReceiveQty(), sku.getPerPrice());
					recAsnDetailVO.setMoney(NumberUtil.rounded(m, 2));
				}
				recAsnDetailVO.setSkuStatusName(paramService.getValue(CacheName.SKU_STATUS, detail.getSkuStatus()));
			}
		}
		recAsnVO.setListAsnDetailVO(listAsnDetailVO);
		
		// 查询上架单操作明细
		// 根据Asn单id，查询上架明细
		List<RecPutawayDetail> listPtwDetail = this.ptwDetailExtlService.listPtwDetailByAsnId(recAsn.getAsnId());
		if ( PubUtil.isEmpty(listPtwDetail) ) {
			return recAsnVO;
		}
		List<String> listPtwDetailId = new ArrayList<String>();
		Map<String,RecPutawayDetail> mapPtwDetail = new HashMap<String,RecPutawayDetail>();
		for (RecPutawayDetail recPutawayDetail : listPtwDetail) {
			listPtwDetailId.add(recPutawayDetail.getPutawayDetailId());
			mapPtwDetail.put(recPutawayDetail.getPutawayDetailId(), recPutawayDetail);
		}
		// 根据上架单明细id查询上架单操作明细
		List<RecPutawayLocation> listPtwLocation = this.ptwLocExtlService.listPtwLocationByDetailId(listPtwDetailId,false);
		if ( PubUtil.isEmpty(listPtwLocation) ) {
			return recAsnVO;
		}
//		List<RecPutawayLocationVO> listPtwLocVO = new ArrayList<RecPutawayLocationVO>();
		for (RecPutawayLocation recPutawayLocation : listPtwLocation) {
			RecPutawayDetail recPutawayDetail = mapPtwDetail.get(recPutawayLocation.getPutawayDetailId());
			if ( recPutawayDetail == null ) {
				continue;
			}
			RecAsnDetailVO recAsnDetailVO = mapAsnDetailVO.get(recPutawayDetail.getAsnDetailId());
			if ( recAsnDetailVO == null ) {
				continue;
			}
			RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO(recPutawayLocation);
			// 查询库位信息
			recPutawayLocationVO.setLocationComment(FqDataUtils.getLocNameById(this.locExtlService, recPutawayLocation.getLocationId()));
			recAsnDetailVO.addPtwLocVO(recPutawayLocationVO);
//			listPtwLocVO.add(recPutawayLocationVO);
		}
//		recAsnVO.setListPtwLocVO(listPtwLocVO);
    	return recAsnVO;
    }
    
    /**
     * 新增Asn单
     * @param asnVO Asn单VO
     * @return
     * @throws Exception
     * @version 2017年2月14日 上午11:06:22<br/>
     * @author andy wang<br/>
     */
    public RecAsnVO add ( RecAsnVO param_recAsnVO ) throws Exception {
    	if ( param_recAsnVO == null || param_recAsnVO.getAsn() == null ) {
    		log.error("insertAsn --> param_recAsnVO or getAsn is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	RecAsnVO recAsnVO = new RecAsnVO();
    	RecAsn asn = param_recAsnVO.getAsn();
    	// 获取id
    	String asnId = IdUtil.getUUID();
		asn.setAsnId(asnId);
    	// 设置状态
    	asn.setAsnStatus(Constant.ASN_STATUS_OPEN);
    	// 设置单据来源
    	asn.setDataFrom(Constant.ASN_DATAFROM_NORMAL);
    	
    	// 校验源单号唯一性
//    	if ( !StringUtil.isTrimEmpty(asn.getPoNo()) ) {
//    		RecAsnVO p_asnVO = new RecAsnVO();
//    		p_asnVO.getAsn().setPoNo(asn.getPoNo());
//    		p_asnVO.setShowCancel(false);
//    		Integer count = this.asnExtlService.countAsnByExample(p_asnVO);
//    		if ( count > 0 ) {
//    			throw new BizException("err_rec_asn_poNo_exists");
//    		}
//    	}
    	
    	recAsnVO.setAsn(asn);
    	List<RecAsnDetailVO> listAsnDetailVO = param_recAsnVO.getListAsnDetailVO();
    	if ( PubUtil.isEmpty(listAsnDetailVO) ) {
    		listAsnDetailVO = new ArrayList<RecAsnDetailVO>();
    	}
    	//批次号转换（界面上的是生产日期，数据库存的是批次号）
    	listAsnDetailVO.forEach(d->{
    		//保存生产日期，add by 王通
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		String batchNo = d.getAsnDetail().getBatchNo();
    		try {
				d.getAsnDetail().setProduceDate(sdf.parse(batchNo));
			} catch (ParseException e) {
				throw new BizException("err_date_wrong");
			}
    		//批次号转换
    		d.getAsnDetail().setBatchNo(batchNo.replaceAll("/", ""));
    	});
    	// 设置Asn明细属性
    	Double totalQty = 0d;
    	Double totalVolume = 0d;
    	Double totalWeight = 0d;
    	List<String> list = new ArrayList<String>();
		for (int i = 0; i < listAsnDetailVO.size(); i++) {
			RecAsnDetailVO recAsnDetailVO = listAsnDetailVO.get(i);
			RecAsnDetail recAsnDetail = recAsnDetailVO.getAsnDetail();
    		if ( list.contains(this.getDetailKey(recAsnDetail)) ) {
    			throw new BizException("err_rec_asn_sameDetail");
    		}
    		recAsnDetail.setAsnId(asnId);
    		RecUtil.defOrderQWV(recAsnDetail);
    		totalQty += recAsnDetail.getOrderQty();
    		totalVolume = NumberUtil.add(totalVolume, recAsnDetail.getOrderVolume());
    		totalWeight = NumberUtil.add(totalWeight, recAsnDetail.getOrderWeight());
    		list.add(this.getDetailKey(recAsnDetail));
		}
    	asn.setOrderQty(totalQty);
    	asn.setOrderVolume(totalVolume);
    	asn.setOrderWeight(totalWeight);
    	recAsnVO.setListAsnDetailVO(listAsnDetailVO);
    	Principal loginUser = LoginUtil.getLoginUser();
    	asn.setOrgId(loginUser.getOrgId());
    	asn.setWarehouseId(LoginUtil.getWareHouseId());
    	// 保存Asn单
    	this.asnExtlService.insertAsn(asn);
    	// 保存Asn单明细
    	for (int i = 0; i < listAsnDetailVO.size(); i++) {
    		RecAsnDetailVO recAsnDetailVO = listAsnDetailVO.get(i);
    		this.asnDetailExtlService.insertAsnDetail(recAsnDetailVO.getAsnDetail());
		}
    	return recAsnVO;
    }
    
    /**
     * 导入Asn单
     * @param _recAsnVO
     * @return
     * @throws Exception
     */
    @Transactional(readOnly=false)
    public RecAsnVO importAsn(RecAsnVO _recAsnVO) throws Exception{
    	//查询货主信息
//		if ( !StringUtil.isTrimEmpty(_recAsnVO.getOwnerMerchant().getMerchantNo()) ) {
//			MetaMerchant ownerMerchant = FqDataUtils.getMerchantByNo(this.merchantService, _recAsnVO.getOwnerMerchant().getMerchantNo());
//			if ( ownerMerchant == null ) {
//				throw new BizException("err_rec_asn_owner_null");
//			}
//			_recAsnVO.setOwnerMerchant(ownerMerchant);
//			_recAsnVO.getAsn().setOwner(ownerMerchant.getMerchantId());
//		}else{
//			throw new BizException("err_rec_asn_owner_null");
//		}
		// 查询发货方信息
		if ( _recAsnVO.getSenderMerchant() != null 
				&& !StringUtil.isTrimEmpty(_recAsnVO.getSenderMerchant().getMerchantNo()) ) {
			MetaMerchant sendMerchant = FqDataUtils.getMerchantByNo(this.merchantService, _recAsnVO.getSenderMerchant().getMerchantNo());
			if ( sendMerchant != null ) {
				if ( StringUtil.isTrimEmpty(_recAsnVO.getAsn().getContactPerson()) ) {
					_recAsnVO.getAsn().setContactPerson(sendMerchant.getContactPerson());
				}
				if ( StringUtil.isTrimEmpty(_recAsnVO.getAsn().getContactPhone()) ) {
					_recAsnVO.getAsn().setContactPhone(sendMerchant.getContactPhone());
				}
				if ( StringUtil.isTrimEmpty(_recAsnVO.getAsn().getContactAddress()) ) {
					_recAsnVO.getAsn().setContactAddress(sendMerchant.getContactAddress());
				}
			} 
		}
		List<String> skuIdList = new ArrayList<String>();
    	//根据货品代码查询货品
    	if(_recAsnVO.getListAsnDetailVO() != null && _recAsnVO.getListAsnDetailVO().size() > 0){
    		for(RecAsnDetailVO asnDetailVo:_recAsnVO.getListAsnDetailVO()){
    			if (!StringUtil.isTrimEmpty(asnDetailVo.getSkuNo()) ) {
    				MetaSku p_sku = new MetaSku();
    				p_sku.setSkuNo(asnDetailVo.getSkuNo());
    				Principal p = LoginUtil.getLoginUser();
    				ResultModel rm = this.skuService.list(new SkuVo(p_sku), p);
    				@SuppressWarnings("unchecked")
					List<SkuVo> listSku = rm.getList();
    				if ( PubUtil.isEmpty(listSku) ) {
    					throw new BizException("err_rec_asn_owner_has_no_this_sku");
    				} else {
    					MetaSku sku = listSku.get(0).getEntity();
    					asnDetailVo.getAsnDetail().setSkuId(sku.getSkuId());
    					//TODO 只有生效状态的货品可以通过
    					if(Constant.STATUS_ACTIVE!=sku.getSkuStatus()){
    						return null;
    					}
    					// 校验相同明细，ASN单不能存在相同批次的货品
            			if ( skuIdList.contains(asnDetailVo.getAsnDetail().getSkuId()+"_"+asnDetailVo.getAsnDetail().getBatchNo()) ) {
            				throw new BizException("err_rec_asn_sameDetail");
            			}
            			skuIdList.add(asnDetailVo.getAsnDetail().getSkuId()+"_"+asnDetailVo.getAsnDetail().getBatchNo());
            			//检查是否同一货主
            			if(StringUtil.isTrimEmpty(_recAsnVO.getAsn().getOwner())){
            				_recAsnVO.getAsn().setOwner(sku.getOwner());
            			}else{
            				if(!sku.getOwner().equals(_recAsnVO.getAsn().getOwner())){
            					throw new BizException("err_rec_asn_notSameOwner");
            				}
            			}
    				}
    			}
    		}
    	}
		//数据类型默认是普通入库
		if(_recAsnVO.getAsn().getDocType() == null){
			_recAsnVO.getAsn().setDocType(Constant.ASN_DOCTYPE_NORMAL);
		}
		//由于数据可能通过导入而非手工添加进入系统，故再次校验一遍
		Set<ConstraintViolation<RecAsn>> vr = validator.validate(_recAsnVO.getAsn(), ValidSave.class);
		if(vr.size() > 0) {
			String err = vr.iterator().next().getMessage();
			throw new BizException(err.replaceFirst("\\{", "").replaceFirst("\\}", ""));
		}
		RecAsnVO record = add(_recAsnVO);
		//获取登录用户
//		Principal p = LoginUtil.getLoginUser();
//		if(p != null){
//			//获取仓库
//			MetaWarehouseVO warehouseVo = warehouseService.viewWrh(p.getWarehouseId());
//			
//			if(_recAsnVO.getApplicationVo() != null 
//					&& warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
//				//若是普通仓，则自动生成申请单
//				_recAsnVO.getApplicationVo().getEntity().setAsnId(record.getAsn().getAsnId());
//				DeliverGoodsApplicationVo applicationVo = addApplicationVo(_recAsnVO.getApplicationVo());
//				
//				//更新asn单的申请单号
//				RecAsn asn = new RecAsn();
//				asn.setAsnId(record.getAsn().getAsnId());
//				asn.setGatejobNo(applicationVo.getEntity().getApplicationNo());
//				asnDao.updateByPrimaryKeySelective(asn);
//			}
//		}

		
		return record;
    }
    
	/**
	 * 自动生成申请单
	 * @param applicationVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationVo addApplicationVo(DeliverGoodsApplicationVo applicationVo) throws Exception{
		Principal p = LoginUtil.getLoginUser();
		MetaWarehouseVO warehouseVo = warehouseService.viewWrh(LoginUtil.getWareHouseId());
		if(warehouseVo.getWarehouse().getWarehouseType().intValue() == Constant.WAREHOUSE_TYPE_NOTCUSTOMS){
			String i_flag = paramService.getKey(CacheName.APPLY_I_FLAG);
			applicationVo.getEntity().setiEFlag(i_flag);
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
	 * 根据ASN单明细项，组合货品唯一key值
	 * @param recAsnDetail ASN单明细项
	 * @return 货品id_货品批次
	 * @version 2017年3月20日 下午5:59:21<br/>
	 * @author andy wang<br/>
	 */
	private String getDetailKey(RecAsnDetail recAsnDetail) {
		return recAsnDetail.getSkuId()+"_"+recAsnDetail.getBatchNo()+"_"+recAsnDetail.getSkuStatus();
	}
    
    
    /**
     * 生效Asn单
     * @param param_asnVO Asn单VO
     * @throws Exception
     * @version 2017年2月14日 下午6:03:51<br/>
     * @author andy wang<br/>
     */
    @Transactional(readOnly=false)
    public void enable ( RecAsnVO param_asnVO ) throws Exception {
    	if ( param_asnVO == null || PubUtil.isEmpty(param_asnVO.getListAsnId()) ) {
    		log.error("activeASN --> param_listAsnId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
//    	if ( param_asnVO.getTsTaskVo() == null 
//    			|| param_asnVO.getTsTaskVo().getTsTask() == null 
//    			|| StringUtil.isTrimEmpty(param_asnVO.getTsTaskVo().getTsTask().getOpPerson()) ) {
//    		throw new NullPointerException("parameter task is null!");
//    	}
    	List<String> param_listAsnId = param_asnVO.getListAsnId();
    	for (int i = 0; i < param_listAsnId.size(); i++) {
    		String asnId = param_listAsnId.get(i);
        	//查询是否有关联的申请单
        	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
        	List<String> statusList = new ArrayList<String>();
        	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_ALL_EXMINE));
        	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
        	applicationVo.setStatusNotIn(statusList);
        	applicationVo.getEntity().setAsnId(asnId);
        	List<DeliverGoodsApplicationVo> qryList = deliverGoodsApplicationService.qryList(applicationVo);
        	
        	if (qryList != null && !qryList.isEmpty()) {
        		throw new BizException("err_application_not_null");
        	}
        	
    		if ( StringUtil.isTrimEmpty(asnId) ) {
    			throw new NullPointerException("parameter id is null");
    		}
    		// 查询Asn单
        	RecAsn recAsn = this.asnExtlService.findAsnById(asnId);
        	if ( recAsn == null ) {
        		throw new BizException("err_rec_asn_null");
        	}
        	
        	if ( Constant.ASN_STATUS_OPEN != recAsn.getAsnStatus() ) {
        		throw new BizException("err_rec_asn_active_statusNotOpen");
        	}
        	
        	// 查询Asn单明细
        	List<RecAsnDetail> listAsnDetail = this.asnDetailExtlService.listAsnDetailByAsnId(asnId);
        	if ( listAsnDetail == null || listAsnDetail.isEmpty() ) {
        		throw new BizException("err_rec_asn_active_detailEmpty");
        	}
        	//新增功能--自动指定暂存区库位--王通
        	for (RecAsnDetail detail : listAsnDetail) {
        		MetaLocationVO param = new MetaLocationVO();
        		param.getLocation().setLocationType(Constant.LOCATION_TYPE_TEMPSTORAGE);
				MetaLocation location = locExtlService.findLoc(param);
				detail.setLocationId(location.getLocationId());
				this.asnDetailExtlService.updateAsnDetail(detail);
        	}
        	// 更新状态
        	this.asnExtlService.updateAsnStatusById(asnId, Constant.ASN_STATUS_ACTIVE);
        	
        	// 生成任务
//			this.taskService.create(param_asnVO.getTsTaskVo().getTsTask().getOpPerson(),Constant.TASK_TYPE_ASN,asnId);
        	this.taskService.create(Constant.TASK_TYPE_ASN,asnId);
		}
    	
    }
    
    /**
     * 失效Asn单
     * @param param_listAsnId Asn单id集合
     * @throws Exception
     * @version 2017年2月15日 下午1:32:50<br/>
     * @author andy wang<br/>
     */
    @Transactional(readOnly=false)
    public void disable ( List<String> param_listAsnId ) throws Exception {
    	if ( param_listAsnId == null || param_listAsnId.isEmpty() ) {
    		log.error("inactiveASN --> param_listAsnId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
    	Principal loginUser = LoginUtil.getLoginUser();
    	for (int i = 0; i < param_listAsnId.size(); i++) {
    		String param_asnId = param_listAsnId.get(i);
    		if ( StringUtil.isTrimEmpty(param_asnId) ) {
    			throw new NullPointerException("parameter id is null");
    		}

        	//查询是否有关联的申请单
        	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
        	List<String> statusList = new ArrayList<String>();
        	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
        	applicationVo.setStatusNotIn(statusList);
        	applicationVo.getEntity().setAsnId(param_asnId);
        	List<DeliverGoodsApplicationVo> qryList = deliverGoodsApplicationService.qryList(applicationVo);
        	if (qryList != null && !qryList.isEmpty()) {
        		throw new BizException("err_application_not_null");
        	}
    		// 查询Asn单
        	RecAsn recAsn = this.asnExtlService.findAsnById(param_asnId);
        	if ( recAsn == null ) {
        		throw new BizException("err_rec_asn_null");
        	}
        	if ( recAsn.getAsnStatus() == null || Constant.ASN_STATUS_ACTIVE != recAsn.getAsnStatus() ) {
        		throw new BizException("err_rec_asn_inactive_statusNotActive");
        	}

        	this.taskService.cancelByOpId(param_asnId, loginUser.getUserId());
        	// 更新状态
        	this.asnExtlService.updateAsnStatusById(param_asnId, Constant.ASN_STATUS_OPEN);
        	
		}
    }
    
    /**
     * 取消ASN单
     * @param param_listAsnId Asn单id集合
     * @throws Exception
     * @version 2017年2月15日 下午2:00:11<br/>
     * @author andy wang<br/>
     */
    public void cancel ( List<String> param_listAsnId ) throws Exception {
    	if ( param_listAsnId == null || param_listAsnId.isEmpty() ) {
    		log.error("cancelASN --> param_listAsnId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
    	for (int i = 0; i < param_listAsnId.size(); i++) {
    		String param_asnId = param_listAsnId.get(i);
    		if ( StringUtil.isTrimEmpty(param_asnId) ) {
    			throw new NullPointerException("parameter id is null");
    		}
    		// 查询Asn单
        	RecAsn recAsn = this.asnExtlService.findAsnById(param_asnId);
        	if ( recAsn == null ) {
        		throw new BizException("err_rec_asn_null");
        	}
        	if ( recAsn.getAsnStatus() == null || Constant.ASN_STATUS_OPEN != recAsn.getAsnStatus() ) {
        		throw new BizException("err_rec_asn_cancel_statusNotOpen");
        	}
        	//查询是否有关联的申请单
//        	DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
//        	List<String> statusList = new ArrayList<String>();
//        	statusList.add(String.valueOf(Constant.APPLICATION_STATUS_CANCAL));
//        	applicationVo.setStatusNotIn(statusList);
//        	applicationVo.getEntity().setAsnId(recAsn.getAsnId());
//        	List<DeliverGoodsApplicationVo> qryList = deliverGoodsApplicationService.qryList(applicationVo);
//        	
//        	if (qryList != null && !qryList.isEmpty()) {
//        		throw new BizException("err_application_not_null");
//        	}
        	
        	// 更新状态
        	this.asnExtlService.updateAsnStatusById(param_asnId, Constant.ASN_STATUS_CANCEL);
        	
        	// 新增异常
        	ExceptionLog expLog = new ExceptionLog();
			expLog.setExType(Constant.EXCEPTION_TYPE_REC_ABNORMAL);
			expLog.setExLevel(Constant.EXCEPTION_LEVEL_NORMAL);
			expLog.setInvolveBill(recAsn.getAsnNo());
			expLog.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
			expLog.setExDesc(String.format("单号：%s 。ASN单被取消",recAsn.getAsnNo()));
			this.exceptionLogService.add(expLog);
		}
    }
    
    /**
     * 收货单拦截
     * @param asnVo
     * @throws Exception
     */
    public void cancelByNo(RecAsnVO asnVo)throws Exception{
    	
    	RecAsn recAsn = asnExtlService.findAsnByExample(asnVo);
    	
    	if ( recAsn == null ) {
    		throw new BizException("err_rec_asn_null");
    	}
    	//ASN单生效或打开，都可以拦截
    	if (recAsn.getAsnStatus() == null || (Constant.ASN_STATUS_OPEN != recAsn.getAsnStatus() &&  Constant.ASN_STATUS_ACTIVE !=recAsn.getAsnStatus())) {
    		throw new BizException("err_asn_intercept_fail");
    	}
    	RecAsnVO vo = new RecAsnVO();
    	recAsn.setAsnStatus(Constant.ASN_STATUS_CANCEL);
    	recAsn.setInterceptStatus(Constant.INTERCEPT_SUCCESS);
    	vo.setAsn(recAsn);
    	asnExtlService.updateAsn(vo);
    }
    
    
    /**
     * 拆分ASN单
     * @param param_recAsnVO 需要拆分的ASN单
     * @throws Exception
     * @version 2017年2月15日 下午2:00:11<br/>
     * @author andy wang<br/>
     */
    @Transactional(readOnly=false)
    public void split ( RecAsnVO param_recAsnVO ) throws Exception {
    	if ( param_recAsnVO == null || param_recAsnVO.getAsn() == null ) {
    		log.error("splitASN --> param_recAsnVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	String asnId = param_recAsnVO.getAsn().getAsnId();
		if ( asnId == null ) {
    		log.error("splitASN --> AsnId is null!");
    		throw new NullPointerException("id is null!");
    	}
		List<RecAsnDetailVO> listAsnDetailVO = param_recAsnVO.getListAsnDetailVO();
		if ( PubUtil.isEmpty(listAsnDetailVO) ) {
    		log.error("splitASN --> listAsnDetailVO is null!");
    		throw new BizException("err_rec_asn_split_detailEmpty");
    	}
		List<RecAsnDetail> listSavetAsnDetail = new ArrayList<RecAsnDetail>();
		for (RecAsnDetailVO recAsnDetailVO : listAsnDetailVO) {
			RecAsnDetail asnDetail = recAsnDetailVO.getAsnDetail();
			asnDetail.setOrderQty(recAsnDetailVO.getSplitQty());
			asnDetail.setOrderWeight(recAsnDetailVO.getSplitWeight());
			asnDetail.setOrderVolume(recAsnDetailVO.getSplitVolume());
			listSavetAsnDetail.add(asnDetail);
		}
    	// 查询Asn单
    	RecAsnVO p_recAsnVO = new RecAsnVO();
    	p_recAsnVO.loginInfo();
    	RecAsn asn = p_recAsnVO.getAsn();
		asn.setAsnId(asnId);
    	RecAsn sourceRecAsn = this.asnExtlService.findAsnById(asnId);
    	// 查询Asn单明细
    	List<RecAsnDetail> listSourceRecAsnDetail = this.asnDetailExtlService.listAsnDetailByAsnId(asnId);
    	// 运行拆分规则
    	SplitStrategy<RecAsn,RecAsnDetail> asnSplit = new AsnSplitStrategy(sourceRecAsn, listSourceRecAsnDetail, listSavetAsnDetail).execute();
    	
    	// 201704191411 与何国贤、黄平息讨论后，决定拆分后的任意一子单的总数量为0，不能进行拆分操作，因为总数量为0的子单没有任何意义。
    	// 最后决定使用以下判断，如果拆分后的任意一子单的明细集合数量为0时不进行拆分
    	if ( PubUtil.isEmpty(asnSplit.getListSplitDetail()) 
    			|| PubUtil.isEmpty(asnSplit.getListSurplusDetail()) ) {
    		throw new BizException("err_split_splitQtyNotZero");
    	}
    	
    	// 保存拆分对象
    	this.asnExtlService.insertAsn(asnSplit.getSplitObj());
    	// 保存剩余对象
    	this.asnExtlService.insertAsn(asnSplit.getSurplusObj());
    	// 更新原Asn单状态
    	this.asnExtlService.updateAsnStatusById(sourceRecAsn.getAsnId(), Constant.ASN_STATUS_CANCEL);
    	// 保存拆分明细对象
    	this.asnDetailExtlService.insertAsnDetail(asnSplit.getListSplitDetail());
    	// 保存剩余明细对象
    	this.asnDetailExtlService.insertAsnDetail(asnSplit.getListSurplusDetail());
    }
    
    /**
     * 取消拆分Asn单
     * @param param_listAsnId 要被恢复的Asn单 子单id
     * @throws Exception
     * @version 2017年2月16日 下午1:44:58<br/>
     * @author andy wang<br/>
     */
    @Transactional(readOnly=false)
    public void unsplit ( List<String> param_listAsnId ) throws Exception {
    	if ( param_listAsnId == null || param_listAsnId.isEmpty() ) {
    		log.error("unsplitASN --> param_listAsnId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	for (int i = 0; i < param_listAsnId.size(); i++) {
    		String asnId = param_listAsnId.get(i);
    		RecAsn asn = this.asnExtlService.findAsnById(asnId);
    		if ( asn == null ) {
    			log.error("unsplitASN --> Asn(asnId:%s) is null!",asnId);
    			throw new BizException("err_rec_asn_null");
    		}
    		if ( StringUtil.isTrimEmpty(asn.getParentAsnId()) ) {
    			log.error("unsplitASN --> Asn(asnId:%s) parentAsnId is null!",asnId);
    			throw new BizException("err_rec_asn_unsplit_hasNotParent");
    		}
    		// 校验状态
    		if ( Constant.ASN_STATUS_OPEN != asn.getAsnStatus() ) {
    			throw new BizException("err_rec_asn_unsplit_statusNotOpen");
    		}
    		// 查询相邻子单
    		List<RecAsn> listSplitRecAsn = this.asnExtlService.listSplitAsn(asn.getParentAsnId());
    		if ( PubUtil.isEmpty(listSplitRecAsn) || listSplitRecAsn.size() < 2 ) {
    			log.error("unsplitASN --> Asn(asnId:%s) Parent Asn's Children are miss ",asnId);
    			throw new BizException("err_rec_asn_unsplit_hasNotParent");
    		}

    		RecAsn splitChildRecAsn = listSplitRecAsn.get(0);
    		RecAsn surplusChildRecAsn = listSplitRecAsn.get(1);
    		RecAsn brotherAsn = listSplitRecAsn.get(0);
    		if ( asnId.equals(brotherAsn.getAsnId()) ) {
    			brotherAsn = listSplitRecAsn.get(1);
    		}

			if ( Constant.ASN_STATUS_OPEN != brotherAsn.getAsnStatus() ) {
				if ( Constant.ASN_STATUS_CANCEL == brotherAsn.getAsnStatus() ) {
					List<RecAsn> listBrotherChildRecAsn = this.asnExtlService.listSplitAsn(brotherAsn.getAsnId());
					if ( PubUtil.isEmpty(listBrotherChildRecAsn) ) {
						throw new BizException("err_rec_asn_unsplit_brotherHasCancel");
					} else {
						throw new BizException("err_rec_asn_unsplit_brotherHasChild");
					}
				} else {
					throw new BizException("err_rec_asn_unsplit_allChildOpen",brotherAsn.getAsnNo());
				}
			}
    		// 查询父单
    		RecAsn parentRecAsn = this.asnExtlService.findAsnById(asn.getParentAsnId());
    		if ( parentRecAsn == null ) {
    			throw new BizException("err_rec_asn_unsplit_hasNotParent");
    		}
    		List<String> listAsnId = new ArrayList<String>();
			listAsnId.add(splitChildRecAsn.getAsnId());
			listAsnId.add(surplusChildRecAsn.getAsnId());
			// 删除子单
    		this.asnDetailExtlService.deleteListByAsnId(listAsnId);
			this.asnExtlService.deleteAsnById(listAsnId);
    		// 恢复父单
    		this.asnExtlService.updateAsnStatusById(parentRecAsn.getAsnId(), Constant.ASN_STATUS_OPEN);
		}
    }

	@Transactional(readOnly=true)
	@Override
	public List<RecAsnVO> list4print(RecAsnVO param_recAsnVO) throws Exception {
		if (param_recAsnVO == null || PubUtil.isEmpty(param_recAsnVO.getListAsnId())) return null;
		List<RecAsnVO> listVO = new ArrayList<RecAsnVO>();
		for (String asnId : param_recAsnVO.getListAsnId()) {
			listVO.add(this.view(asnId));
		}
		return listVO;
	}

	@Override
	public void completeAndPutaway(RecAsnVO param_recAsnVO) throws Exception {
		//收货确认
		this.complete(param_recAsnVO);
		//自动生成上架单
		String asnId = param_recAsnVO.getAsn().getAsnId();
		List<String> listAsnId = new ArrayList<String>();
		listAsnId.add(asnId);
		ptwServie.createPutaway(listAsnId);
//		if(1 == 1) throw new Exception("test transaction");
//		return list;
	}

	@Override
	public void batchAndAddPtw(RecAsnVO param_recAsnVO) throws Exception {
		this.batch(param_recAsnVO);
		ptwServie.createPutaway(param_recAsnVO.getListAsnId());
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
	@Override
	public ResponseEntity<byte[]> downloadExcel(RecAsnVO vo) throws Exception {
		// 导出入库清单  
		ExcelExport<RecAsnVO4Excel> ex = new ExcelExport<RecAsnVO4Excel>();  
		String[] headers = { 
    		  "序号", "货主简称", "PO单号", 
    		  "ASN单号", "订单类型", "单据来源", 
    		  "订单日期", "订单数量/公斤/立方", 
    		  "收货数量/公斤/立方", "上架数量/公斤/立方", 
    		  "状态", "同步状态", "创建人员", "作业人员",
    		  "发货方","商品名称","商品条码"};  
      
		Page<RecAsnVO> page = list(vo);
		for(RecAsnVO asnVo:page.getResult()){
			qryAsnDetail(asnVo);
		}
		List<RecAsnVO4Excel> dataset = vo2excelVo(page);
		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("入库清单", headers, dataset, "yyyy-MM-dd", "入库清单.xls");
		return bytes;
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
	private List<RecAsnVO4Excel> vo2excelVo(Page<RecAsnVO> list) {
		List<RecAsnVO4Excel> retList = new ArrayList<RecAsnVO4Excel>();
		for (int i = 0; i < list.size(); i++) {
			RecAsnVO asnVo = list.get(i);
			for(int j=0;j < asnVo.getListAsnDetailVO().size();j++){
				RecAsnDetailVO detailVo = asnVo.getListAsnDetailVO().get(j);
				RecAsnVO4Excel vo4excel = null;
				if(j==0){
					vo4excel = new RecAsnVO4Excel(asnVo,detailVo,i+1);
				}else{
					vo4excel = new RecAsnVO4Excel(null,detailVo,i+1);
				}
				retList.add(vo4excel);
			}
//			RecAsnVO4Excel vo4excel = new RecAsnVO4Excel(list.get(i));
//			vo4excel.setIndex(String.valueOf(i+1));
//			retList.add(vo4excel);
		}
		return retList;
	}
	
	/**
	 * 将所有仓库的出库数据发送到海关
	 * @param warehouseIdList
	 * @throws Exception
	 */
	public void findAndSendInStockData(List<String> warehouseIdList)throws Exception{
		//查询所有完成上架的订单
		RecAsnVO asnVo = new RecAsnVO();
		asnVo.setWarehouseIdList(warehouseIdList);
		asnVo.getAsn().setAsnStatus(Constant.ASN_STATUS_PUTAWAY);
		
		List<RecAsn> list = asnDao.selectByExample(asnVo.getExample());
		List<String> asnIdList = list.stream().map(t->t.getAsnId()).collect(Collectors.toList());
		
		//批量同步入库数据
		batchSyncInstockData(asnIdList);
	}
	
	/**
	 * 批量同步入库数据
	 * @param idList
	 * @throws Exception
	 */
	public void batchSyncInstockData(List<String> idList) throws Exception{
		if(idList == null || idList.isEmpty()) return;
		for(String asnId:idList){
			transmitInstockXML(asnId);
		}
	}
	
	/**
	 * 传送入库数据对接仓储企业联网监管系统
	 * @throws Exception
	 */
	public void transmitInstockXML(String asnId) throws Exception{
		
		Principal loginUser = LoginUtil.getLoginUser();
		
		String messsageId = IdUtil.getUUID();
		String emsNo = paramService.getKey(CacheName.EMS_NO);
		String sendId = paramService.getKey(CacheName.TRAED_CODE);
		String receiveId = paramService.getKey(CacheName.CUSTOM_CODE);
		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
		String in_stock_type = paramService.getKey(CacheName.TYPE_INSTOCK_NONBOND);
		String messageType = paramService.getKey(CacheName.MSMQ_MESSAGE_TYPE_INOUTSTOCK);
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		String date = DateFormatUtils.format(today, "yyyy-MM-dd");
		//查询asn单信息
		RecAsnVO asnVo = view(asnId);
		if(asnVo == null) throw new BizException("err_rec_asn_null");
		if(asnVo.getListAsnDetailVO() == null || asnVo.getListAsnDetailVO().isEmpty()) 
			throw new BizException("err_rec_asnDetail_null");
		//检查是否全部上架状态
		if(Constant.ASN_STATUS_PUTAWAY != asnVo.getAsn().getAsnStatus().intValue()){
			throw new BizException("err_rec_asn_trans_statusNotputaway");
		}
		//创建入库报文
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
		for(RecAsnDetailVO asnDetailVo:asnVo.getListAsnDetailVO()){
			MetaMerchant owner = FqDataUtils.getMerchantById(merchantService, asnDetailVo.getSku().getOwner());
			Map<String,String> map = new HashMap<String,String>();
			for(RecPutawayLocationVO putawayLocationVo:asnDetailVo.getListPtwLocVO()){
				map.put("MESSAGE_ID", messsageId);
				map.put("EMS_NO", emsNo);
				map.put("IO_NO",asnVo.getAsn().getAsnNo());
				map.put("GOODS_NATURE", asnDetailVo.getSku().getGoodsNature()==null?"":asnDetailVo.getSku().getGoodsNature()+"");
				map.put("IO_DATE", date);
				map.put("COP_G_NO", StringUtil.isBlank(asnDetailVo.getSku().getHgGoodsNo())?asnDetailVo.getSku().getSkuNo():asnDetailVo.getSku().getHgGoodsNo());
				map.put("G_NO", asnDetailVo.getSku().getgNo());
				map.put("HSCODE", asnDetailVo.getSku().getHsCode());
				map.put("GNAME", asnDetailVo.getSku().getSkuName());
				map.put("GMODEL", asnDetailVo.getSku().getSpecModel());
				map.put("CURR", asnDetailVo.getSku().getCurr());
				map.put("COUNTRY", asnDetailVo.getSku().getOriginCountry());
				map.put("QTY", putawayLocationVo.getPutawayLocation().getPutawayQty().toString());
				map.put("UNIT", asnDetailVo.getSku().getMeasureUnit());
				map.put("TYPE", in_stock_type);
				map.put("CHK_CODE", "01");
				map.put("GATEJOB_NO", asnVo.getAsn().getGatejobNo());
				map.put("WHS_CODE", asnVo.getWarehouseNo());
				map.put("LOCATION_CODE", putawayLocationVo.getLocationComment());
				map.put("OWNER_CODE", owner.getHgMerchantNo());
				map.put("OWNER_NAME", owner.getMerchantName());
			}
			mapList.add(map);
		}
		msmq.setBodyMaps(mapList);
		//创建入库报文
		TransmitXmlUtil tranUtil = new TransmitXmlUtil(msmq);
		String xmlStr = tranUtil.formXml();
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messsageId+"_入库数据报文：["+xmlStr+"]");
		
		//传送入库数据到仓储企业联网监管系统
		boolean result = false;
		if(log.isInfoEnabled()) log.info("Queue名称：["+sendChnalName+"]");
		result = MSMQUtil.send(sendChnalName, label, xmlStr, messsageId.getBytes());
		if(log.isInfoEnabled()) log.info("MSMQ_Message_"+messsageId+"_入库发送接口运行结果：["+result+"]");	
		
		//保存报文
		MsmqMessageVo messageVo = new MsmqMessageVo();
		messageVo.getEntity().setMessageId(messsageId);
		messageVo.getEntity().setFunctionType(Constant.FUNCTION_TYPE_ASN);
		messageVo.getEntity().setOrderNo(asnId);
		messageVo.getEntity().setContent(xmlStr);
		messageVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
		messageVo.getEntity().setSendTime(new Date());
		messageVo.getEntity().setCreatePerson(loginUser.getUserId());
		messageVo.getEntity().setOrgId(loginUser.getOrgId());
		messageVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		msmqMessageService.add(messageVo);
		
		//修改asn单发送状态
		if(result){
			asnVo.getAsn().setTransStatus(Constant.SYNCSTOCK_STATUS_SEND_SUCCESS);
			asnVo.getAsn().setUpdatePerson(loginUser.getUserId());
			asnDao.updateByPrimaryKeySelective(asnVo.getAsn());
		}
	}
	
	public static void main(String[] args) {
		Date today = new Date();
		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "T");
		System.out.println(dateTime);
	}
}