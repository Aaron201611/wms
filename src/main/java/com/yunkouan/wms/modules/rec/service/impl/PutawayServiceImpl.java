/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午3:58:48<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.service.IExceptionLogService;
import com.yunkouan.wms.modules.rec.dao.IPutawayDao;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayLocationExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayService;
import com.yunkouan.wms.modules.rec.util.RecUtil;
import com.yunkouan.wms.modules.rec.util.flow.autoptw.AutoPutawayFlow;
import com.yunkouan.wms.modules.rec.util.strategy.PutawaySplitStrategy;
import com.yunkouan.wms.modules.rec.util.strategy.SplitStrategy;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.ts.service.ITaskService;


/**
 * 上架单服务实现类<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午3:58:48<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class PutawayServiceImpl extends BaseService implements IPutawayService {
	/** 日志对象 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(getClass());
	
	
	/** 通用接口类 <br/> add by andy wang */
//	@Autowired
//	private ICommonService commonService;

	/** asn外调业务类 <br/> add by andy wang */
	@Autowired
	private IASNExtlService asnExtlService;
	
	/** asn明细外调业务类 <br/> add by andy wang */
	@Autowired
	private IASNDetailExtlService asnDetailExtlService;
	
	/** 库存业务类 <br/> add by andy wang */
	@Autowired
	private IStockService stockService;
	
	/** 库位外调业务类 <br/> add by andy wang */
	@Autowired
	private ILocationExtlService locationExtlService;
	
	/** 上架单外调业务类 <br/> add by andy wang */
	@Autowired
	private IPutawayExtlService ptwExtlService;
	@Autowired
	private ISysParamService paramService;
	
	/** 上架单明细外调业务类 <br/> add by andy wang */
	@Autowired
	private IPutawayDetailExtlService ptwDetailExtlService;
	
	/** 上架单操作明细外调业务类 <br/> add by andy wang */
	@Autowired
	private IPutawayLocationExtlService ptwLocExtlService;
	
	
	/** 异常日志业务类 <br/> add by andy wang */
	@Autowired
	private IExceptionLogService exceptionLogService;
	
	
	/** 仓库业务类 <br/> add by andy wang */
	@Autowired
	private IWarehouseExtlService warehouseExtlService;
	
	/** 货品业务类 <br/> add by andy wang */
	@Autowired
	private ISkuService skuService;
//	@Autowired
//	private ILocationService locationService;
	
	/** 包装业务类 <br/> add by andy wang */
	@Autowired
	private IPackService packService;
	
	/** 用户业务类 <br/> add by andy wang */
	@Autowired
	private IUserService userService;
	
	/** 客商业务类 <br/> add by andy wang */
	@Autowired
	private IMerchantService merchantService;

	/** 任务业务类 <br/> add by andy wang */
	@Autowired
	private ITaskService taskService;

	@Autowired
	private IAreaExtlService areaService;
	@Autowired
	private ISkuTypeService skuTypeService;
	
//	@Autowired
//	private IMsmqMessageService msmqMessageService;
	
	@Autowired
	private IPutawayDao putawayDao;
//	@Autowired
//	private IPutawayDetailDao putawayDetailDao;

	/* method *************************************************/

	/**
	 * 更新并生效上架单
	 * @param param_recPutawayVO 需要更新的上架单
	 * @throws Exception
	 * @version 2017年5月2日 下午4:16:16<br/>
	 * @author andy wang<br/>
	 */
	public void updateAndEnable ( RecPutawayVO param_recPutawayVO ) throws Exception {
		// 更新上架单
		this.update(param_recPutawayVO);
		// 生效上架单
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add(param_recPutawayVO.getPutaway().getPutawayId());
		param_recPutawayVO.setListPutawayId(listPutawayId);
		this.enable(param_recPutawayVO);
	}
	
	
	/**
	 * 保存并生效上架单
	 * @param param_recPutawayVO 需要保存的上架单
	 * @throws Exception
	 * @version 2017年5月2日 下午4:14:25<br/>
	 * @author andy wang<br/>
	 */
	public void addAndEnable ( RecPutawayVO param_recPutawayVO ) throws Exception {
		// 保存上架单
		RecPutawayVO insertPutaway = this.add(param_recPutawayVO);
//		insertPutaway.getPutaway().setPutawayId(null); // 测试事务
		if ( insertPutaway == null 
				|| StringUtil.isTrimEmpty(insertPutaway.getPutaway().getPutawayId()) ) {
			throw new NullPointerException("saveAndActive->insertPutaway return is null!");
		}
		// 生效上架单
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add(insertPutaway.getPutaway().getPutawayId());
		insertPutaway.setTsTaskVo(param_recPutawayVO.getTsTaskVo());
		insertPutaway.setListPutawayId(listPutawayId);
		this.enable(insertPutaway);
	}

	/**
	 * 自动分配上架库位
	 * @param param_recPutawayVO 上架信息
	 * @return 包含分配好上架库位的上架信息
	 * @throws Exception
	 * @version 2017年3月14日 下午6:12:56<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO auto ( RecPutawayVO param_recPutawayVO ) throws Exception {
		List<RecPutawayDetailVO> listSavePutawayDetailVO = param_recPutawayVO.getListSavePutawayDetailVO();
		if ( param_recPutawayVO == null || PubUtil.isEmpty(listSavePutawayDetailVO) ) {
			log.error("autoPutaway --> param_recPutawayVO or listSavePutawayDetailVO is null!");
    		throw new NullPointerException("请选择货品明细！");
		}
		for (RecPutawayDetailVO recPutawayDetailVO : listSavePutawayDetailVO) {
			recPutawayDetailVO.setListPlanLocationVO(new ArrayList<RecPutawayLocationVO>());
		}
		AutoPutawayFlow flow = new AutoPutawayFlow(listSavePutawayDetailVO, param_recPutawayVO.getPutaway());
		flow.startFlow();
		List<RecPutawayDetailVO> result = flow.getResult();
		param_recPutawayVO.setListPutawayDetailVO(result);
		param_recPutawayVO.setMessage("分配完毕");
		for (RecPutawayDetailVO recPutawayDetailVO : result) {
			List<RecPutawayLocationVO> listPlanLocationVO = recPutawayDetailVO.getListPlanLocationVO();
			boolean isBreak = false;
			for (RecPutawayLocationVO recPutawayLocationVO : listPlanLocationVO) {
				if ( StringUtil.isTrimEmpty(recPutawayLocationVO.getPutawayLocation().getLocationId()) ) {
					isBreak = true;
					break;
				}
			}
			if ( isBreak ) {
				param_recPutawayVO.setMessage("系统无法全部自动分配，请手工分配");
				break;
			}
		}
		return param_recPutawayVO;
	}
	
	/**
	 * 上架单批量确认
	 * @param param_recPutawayVO 上架单对象
	 * @throws Exception
	 * @version 2017年3月5日 上午7:56:48<br/>
	 * @author andy wang<br/>
	 */
	@Deprecated
	public void batchConfirmPutaway ( RecPutawayVO param_recPutawayVO ) throws Exception {
		if ( param_recPutawayVO == null || PubUtil.isEmpty(param_recPutawayVO.getListPutawayId()) ) {
			log.error("confirmPutaway --> param_recPutawayVO is null!");
    		throw new NullPointerException("parameter is null!");
		}
		List<String> listPutawayId = param_recPutawayVO.getListPutawayId();
		for (String putawayId : listPutawayId) {
			RecPutawayVO recPutawayVO = new RecPutawayVO(new RecPutaway());
			recPutawayVO.getPutaway().setPutawayId(putawayId);
			List<RecPutawayLocation> listSavePtwLoc = new ArrayList<RecPutawayLocation>();
			recPutawayVO.setListSavePutawayLocation(listSavePtwLoc);
			// 根据上架单id，查询上架单操作明细
			List<RecPutawayLocation> listPutawayLocation = this.ptwLocExtlService.listPutawayLocationByPtwId(putawayId, true);
			if ( !PubUtil.isEmpty(listPutawayLocation) ) {
				for ( RecPutawayLocation recPutawayLocation : listPutawayLocation ) {
					if ( recPutawayLocation.getPutawayQty() == null || recPutawayLocation.getPutawayQty() == 0 ) {
						continue;
					}
					RecPutawayLocation savePutawayLocation = (RecPutawayLocation) BeanUtils.cloneBean(recPutawayLocation);
					savePutawayLocation.setLocationId(null);
					savePutawayLocation.setPutawayType(null);
					listSavePtwLoc.add(savePutawayLocation);
				}
			} else {
				// 根据上架单id，查询上架单明细
				List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(putawayId);
				if ( PubUtil.isEmpty(listPutawayDetail) ) {
					throw new BizException("err_rec_putaway_confirm_detailEmpty");
				}
				for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
					if ( recPutawayDetail.getPlanPutawayQty() == null || recPutawayDetail.getPlanPutawayQty() == 0 ) {
						continue;
					}
					RecPutawayLocation savePutawayLocation = new RecPutawayLocation();
					savePutawayLocation.setMeasureUnit(recPutawayDetail.getMeasureUnit());
					savePutawayLocation.setPackId(recPutawayDetail.getPackId());
					savePutawayLocation.setPutawayQty(recPutawayDetail.getPlanPutawayQty());
					savePutawayLocation.setPutawayWeight(recPutawayDetail.getPlanPutawayWeight());
					savePutawayLocation.setPutawayVolume(recPutawayDetail.getPlanPutawayVolume());
					listSavePtwLoc.add(savePutawayLocation);
				}
			}
			this.complete(recPutawayVO);
		}
	}
	
	/**
	 * 上架单确认
	 * @param recPutaway 上架单
	 * @throws Exception
	 * @version 2017年3月2日 下午8:45:53<br/>
	 * @author andy wang<br/>
	 */
	public void complete( RecPutawayVO param_recPutawayVO ) throws Exception {
		if ( param_recPutawayVO == null || param_recPutawayVO.getPutaway() == null 
				|| StringUtil.isTrimEmpty(param_recPutawayVO.getPutaway().getPutawayId()) ) {
			log.error("confirmPutaway --> param_recPutawayVO or putaway or putawayId is null!");
    		throw new NullPointerException("parameter is null!");
		}
		// 查询上架单
		RecPutaway recPutaway = param_recPutawayVO.getPutaway();
		RecPutaway putaway = this.ptwExtlService.findPutawayById(recPutaway.getPutawayId());
		if ( putaway == null ) {
			throw new BizException("err_rec_putaway_null");
		}
		if ( Constant.PUTAWAY_STATUS_ACTIVE != putaway.getPutawayStatus() 
				&& Constant.PUTAWAY_STATUS_WORKING != putaway.getPutawayStatus() ) {
			throw new BizException("err_rec_putaway_confirm_statusNotActiveOrWorking");
		}
		RecUtil.defPtwQWV(putaway);
		List<RecPutawayLocationVO> listSavePutawayLocationVO = param_recPutawayVO.getListSavePutawayLocationVO();
		if ( PubUtil.isEmpty(listSavePutawayLocationVO) && !PubUtil.isEmpty(param_recPutawayVO.getListSavePutawayLocation())) {
			listSavePutawayLocationVO = new ArrayList<RecPutawayLocationVO>();
			for (RecPutawayLocation recPutawayLocation : param_recPutawayVO.getListSavePutawayLocation()) {
				listSavePutawayLocationVO.add(new RecPutawayLocationVO(recPutawayLocation));
			}
			param_recPutawayVO.setListSavePutawayLocationVO(listSavePutawayLocationVO);
		}
		// 根据上架单id，查询上架单明细
		Map<String, RecPutawayDetail> mapPutawayDetail = this.ptwDetailExtlService.mapPutawayDetailByPtwId(recPutaway.getPutawayId());
		if ( PubUtil.isEmpty(mapPutawayDetail) ) {
			throw new BizException("err_rec_putaway_confirm_detailEmpty");
		}
		// 校验数量
		Map<String, List<RecPutawayLocation>> mapRealPutawayLocation = new HashMap<String, List<RecPutawayLocation>>();
		for (int i = 0; i < listSavePutawayLocationVO.size(); i++) {
			RecPutawayLocationVO recPutawayLocationVO = listSavePutawayLocationVO.get(i);
			RecPutawayLocation recPutawayLocation = recPutawayLocationVO.getPutawayLocation();
			if ( StringUtil.isTrimEmpty(recPutawayLocation.getLocationId()) 
					&& !StringUtil.isTrimEmpty(recPutawayLocationVO.getRealLocationNo()) ) {
				MetaLocation location = FqDataUtils.getLocByNo(this.locationExtlService, recPutawayLocationVO.getRealLocationNo());
				if ( location == null ) {
					throw new BizException("err_main_location_null");
				}
				if ( Constant.LOCATION_TYPE_TEMPSTORAGE == location.getLocationType() ) {
					throw new BizException("err_rec_putaway_confirm_locIsTemp");
				}
				recPutawayLocation.setLocationId(location.getLocationId());
			}
			//
			if ( recPutawayLocation.getPutawayQty() != null && recPutawayLocation.getPutawayQty() > 0 ) {
				RecPutawayDetail recPutawayDetail = mapPutawayDetail.get(recPutawayLocation.getPutawayDetailId());
				if ( recPutawayLocation.getPutawayWeight() == null || recPutawayLocation.getPutawayWeight() <= 0 ) {
					Double perWeight = NumberUtil.div(recPutawayDetail.getPlanPutawayWeight(), recPutawayDetail.getPlanPutawayQty(), Constant.SCALE_WEIGHT).doubleValue();
					recPutawayLocation.setPutawayWeight(NumberUtil.mul(recPutawayLocation.getPutawayQty(), perWeight,Constant.SCALE_WEIGHT));
				}
				if ( recPutawayLocation.getPutawayVolume() == null || recPutawayLocation.getPutawayVolume() <= 0 ) {
					Double perVolume = NumberUtil.div(recPutawayDetail.getPlanPutawayVolume(), recPutawayDetail.getPlanPutawayQty(), Constant.SCALE_VOLUME).doubleValue();
					recPutawayLocation.setPutawayVolume(NumberUtil.mul(recPutawayLocation.getPutawayQty(), perVolume,Constant.SCALE_VOLUME));
				}
			}
			if ( recPutawayLocation == null 
					|| StringUtil.isTrimEmpty(recPutawayLocation.getLocationId())
					|| !mapPutawayDetail.containsKey(recPutawayLocation.getPutawayDetailId()) ) {
				listSavePutawayLocationVO.remove(i--);
				continue;
			}
			List<RecPutawayLocation> listRealLocation = mapRealPutawayLocation.get(recPutawayLocation.getPutawayDetailId());
			if ( listRealLocation == null ) {
				listRealLocation = new ArrayList<RecPutawayLocation>();
				mapRealPutawayLocation.put(recPutawayLocation.getPutawayDetailId(), listRealLocation);
			}
			listRealLocation.add(recPutawayLocation);
			RecUtil.defLocationQWV(recPutawayLocation);
			RecPutawayDetail recPutawayDetail = mapPutawayDetail.get(recPutawayLocation.getPutawayDetailId());
			RecUtil.defPutawayDetailRealQWV(recPutawayDetail);
			recPutawayDetail.setRealPutawayQty(recPutawayDetail.getRealPutawayQty()+recPutawayLocation.getPutawayQty());
			recPutawayDetail.setRealPutawayWeight(NumberUtil.add(recPutawayDetail.getRealPutawayWeight(),recPutawayLocation.getPutawayWeight()));
			recPutawayDetail.setRealPutawayVolume(NumberUtil.add(recPutawayDetail.getRealPutawayVolume(),recPutawayLocation.getPutawayVolume()));
			putaway.setRealQty(putaway.getRealQty()+recPutawayLocation.getPutawayQty());
			putaway.setRealWeight(NumberUtil.add(putaway.getRealWeight(),recPutawayLocation.getPutawayWeight()));
			putaway.setRealVolume(NumberUtil.add(putaway.getRealVolume(),recPutawayLocation.getPutawayVolume()));
			if ( recPutawayDetail.getPlanPutawayQty() < recPutawayDetail.getRealPutawayQty() 
					|| recPutawayDetail.getPlanPutawayWeight() < recPutawayDetail.getRealPutawayWeight() 
					|| recPutawayDetail.getPlanPutawayVolume() < recPutawayDetail.getRealPutawayVolume() ) {
				throw new BizException("err_rec_putaway_confirm_realOverPlan");
			}
		}
		//检查实际上架库位所属库区的货品类型跟货品所属货品类型是否一致 
		for(RecPutawayLocationVO vo : listSavePutawayLocationVO) {
			RecPutawayLocation recPutawayLocation = vo.getPutawayLocation();
			RecPutawayDetail recPutawayDetail = mapPutawayDetail.get(recPutawayLocation.getPutawayDetailId());
			checkSkuType(recPutawayDetail.getSkuId(), recPutawayLocation.getLocationId());
		}
		// 保存入库
		List<String> listAsnId = new ArrayList<String>();
		for (RecPutawayDetail recPtwDetail : mapPutawayDetail.values()) {
			// 查询上架单计划操作明细
			List<RecPutawayLocation> listPlanPtwLoc = this.ptwLocExtlService.listPtwLocByPtwDetailId(recPtwDetail.getPutawayDetailId(), true);
			Double totalPlanQty = 0d;
			if ( !PubUtil.isEmpty(listPlanPtwLoc) ) {
				// 释放计划上架库位预分配库容
				for (RecPutawayLocation recPtwLoc : listPlanPtwLoc) {
					this.locationExtlService.minusPreusedCapacity(recPtwDetail.getSkuId(), recPtwLoc.getLocationId(), recPtwDetail.getPackId(), recPtwLoc.getPutawayQty());
					totalPlanQty += recPtwLoc.getPutawayQty();
				}
			}
			// 获取上架单实际操作明细
			List<RecPutawayLocation> listRealPtwLoc = mapRealPutawayLocation.get(recPtwDetail.getPutawayDetailId());
			Double totalRealQty = 0d;
			if ( !PubUtil.isEmpty(listRealPtwLoc) ) {
				for (RecPutawayLocation recPtwLoc : listRealPtwLoc) {
					if ( recPtwLoc.getPutawayQty() == null || recPtwLoc.getPutawayQty() <= 0 ) {
						continue;
					}
					// 增加实际上架库位实际库容
					InvStockVO stockVO = RecUtil.createStock(putaway, recPtwDetail, recPtwLoc.getLocationId() 
							, recPtwLoc.getPutawayQty(), Constant.STOCK_LOG_OP_TYPE_IN);
					stockVO.getInvStock().setAsnId(null);
					stockVO.getInvStock().setAsnDetailId(null);
					this.stockService.inStock(stockVO);
					totalRealQty += recPtwLoc.getPutawayQty();
				}
			}
			
			if ( !listAsnId.contains(recPtwDetail.getAsnId()) ) {
				listAsnId.add(recPtwDetail.getAsnId());
			}
			// 释放暂存区
			if ( totalPlanQty != null && totalPlanQty > 0 ) {
				InvStockVO stockPlanVO = RecUtil.createStock(putaway, recPtwDetail, recPtwDetail.getLocationId() 
						, totalPlanQty, Constant.STOCK_LOG_OP_TYPE_OUT);
				this.stockService.unlockOutStock(stockPlanVO);
			}
			if ( totalRealQty != null && totalRealQty > 0 ) {
				InvStockVO stockRealVO = RecUtil.createStock(putaway, recPtwDetail, recPtwDetail.getLocationId() 
						, totalRealQty, Constant.STOCK_LOG_OP_TYPE_OUT);
				this.stockService.outStock(stockRealVO);
			}
		}
		// 更新上架单状态
		Principal loginUser = LoginUtil.getLoginUser();
		putaway.setPutawayStatus(Constant.PUTAWAY_STATUS_COMPLETE);
		putaway.setOpPerson(recPutaway.getOpPerson());
		if(StringUtils.isBlank(putaway.getOpPerson())) putaway.setOpPerson(loginUser.getUserId());
		putaway.setOpTime(new Date());
		this.ptwExtlService.confirmPtw(new RecPutawayVO(putaway));
		this.taskService.finishByOpId(putaway.getPutawayId(), loginUser.getUserId());
		this.ptwExtlService.updatePutawayStatus(putaway.getPutawayId(), Constant.PUTAWAY_STATUS_COMPLETE);
		// 保存实际上架操作明细
		for (RecPutawayLocationVO recPutawayLocationVO : listSavePutawayLocationVO) {
			RecPutawayLocation putawayLocation = recPutawayLocationVO.getPutawayLocation();
			putawayLocation.setPutawayLocationId(IdUtil.getUUID());
			putawayLocation.setPutawayLocationId2(null);
			putawayLocation.setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_REAL);
			this.ptwLocExtlService.insertLocation(putawayLocation);
		}
		for (RecPutawayDetail detail : mapPutawayDetail.values()) {
			this.ptwDetailExtlService.confirmPtwDetail(new RecPutawayDetailVO(detail));
		}
		
		// 更新Asn单状态
		boolean isExp = false;
		for (String asnId : listAsnId) {
			Integer status = Constant.ASN_STATUS_PUTAWAY;
			// 根据Asn单id，查询库存
			InvStockVO p_stockVo = new InvStockVO(new InvStock());
			p_stockVo.getInvStock().setAsnId(asnId);
			List<InvStock> listStock = this.stockService.list(p_stockVo );
			if ( !PubUtil.isEmpty(listStock) ) {
				status = Constant.ASN_STATUS_PARTPUTAWAY;
				isExp = true;
			}
			this.asnExtlService.updateAsnStatusById(asnId, status);
		}
		if ( isExp ) {
			StringBuffer msg = new StringBuffer("");
			for (RecPutawayDetail detail : mapPutawayDetail.values()) {
				if ( detail.getPlanPutawayQty().doubleValue() != detail.getRealPutawayQty().doubleValue() ) {
					MetaSku sku = FqDataUtils.getSkuById(this.skuService, detail.getSkuId());
					msg = msg.append(String.format("货品：%s %s（%s）实际拣货数量（%s）与计划拣货数量（%s）不一致\n"
							,sku.getSkuName() , detail.getBatchNo() , sku.getSkuNo()
							,detail.getPlanPutawayQty().doubleValue(),detail.getRealPutawayQty().doubleValue() ));
				}
			}
			if ( !StringUtil.isTrimEmpty(msg.toString()) ) {
				ExceptionLog expLog = new ExceptionLog();
				expLog.setExType(Constant.EXCEPTION_TYPE_PUT_ABNORMAL);
				expLog.setExLevel(Constant.EXCEPTION_LEVEL_NORMAL);
				expLog.setInvolveBill(recPutaway.getPutawayNo());
				expLog.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
				expLog.setExDesc(msg.toString());
				this.exceptionLogService.add(expLog);
			}
		}
		// 补货操作
		Set<String> skuIdSet = new HashSet<String>();
		mapPutawayDetail.values().forEach(d->{
			skuIdSet.add(d.getSkuId());
		});
		try {
			stockService.repPickSku(skuIdSet, 
					recPutaway.getPutawayNo());
		} catch (Exception e) {
			e.printStackTrace();
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}

		//TODO 发送海关辅助系统
    	try {
	    	RecPutawayVO vo = this.view(param_recPutawayVO.getPutaway().getPutawayId());
	    	context.getStrategy4Assis().request(vo);
		} catch(Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
	}
	// 检查实际上架库位所属库区的货品类型跟货品所属货品类型是否一致  TODO
	private void checkSkuType(String skuId, String locationId) throws Exception {
		//查询货品对应的货品类型
		MetaSku sku = skuService.get(skuId);
		if(sku == null) throw new BizException("err_rec_putaway_sku_no_exists");
		String typeid1 = sku.getSkuTypeId();
		MetaSkuType type1 = skuTypeService.get(typeid1);
		if(type1 == null) throw new BizException("err_rec_putaway_skuType_no_exists");
		String level1 = type1.getLevelCode();
		//查询库位对应库区的货品类型
		MetaLocation location = locationExtlService.findLocById(locationId);
		if(location == null) throw new BizException("err_rec_putaway_location_no_exists");
		String areaid = location.getAreaId();
		MetaArea area = areaService.findAreaById(areaid);
		if(area == null) throw new BizException("err_rec_putaway_area_no_exists");
		String typeid2 = area.getSkuTypeId();
		// 如果该库位未指定货品类型，则允许存放所有货品类型
		if(StringUtils.isBlank(typeid2)) return;
		MetaSkuType type2 = skuTypeService.get(typeid2);
		if(type2 == null) throw new BizException("err_rec_putaway_skuType_no_exists");
		String level2 = type2.getLevelCode();
		//如果不属于相同货品类型则提示用户
		if(!level1.substring(0, 3).equals(level2.substring(0, 3))) throw new BizException("err_rec_putaway_skuType_no_equals");
	}

	/**
	 * 取消拆分上架单
	 * @param param_listPutawayId 上架单id集合
	 * @throws Exception
	 * @version 2017年3月2日 上午11:53:32<br/>
	 * @author andy wang<br/>
	 */
	public void unsplit ( List<String> param_listPutawayId ) throws Exception {
		if ( PubUtil.isEmpty(param_listPutawayId) ) {
    		log.error("unsplitPutaway --> param_listPutawayId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	for (int i = 0; i < param_listPutawayId.size(); i++) {
    		String putawayId = param_listPutawayId.get(i);
    		// 查询要取消拆分的子上架单
    		RecPutaway putaway = this.ptwExtlService.findPutawayById(putawayId);
    		if ( putaway == null || StringUtil.isTrimEmpty(putaway.getParentPutawayId()) ) {
    			log.error("unsplitPutaway --> Putaway(putawayId:%s) parentPutawayId is null!",putawayId);
    			throw new BizException("err_rec_putaway_unsplit_hasNotParent");
    		}
    		
    		if ( Constant.PUTAWAY_STATUS_OPEN != putaway.getPutawayStatus() ) {
    			throw new BizException ("err_rec_putaway_unsplit_statusNotOpen");
    		}
    		
    		// 查询子单
    		List<RecPutaway> listSplitRecPutaway = this.ptwExtlService.listSplitPutaway(putaway.getParentPutawayId());
    		if ( PubUtil.isEmpty(listSplitRecPutaway) || listSplitRecPutaway.size() < 2 ) {
    			log.error("unsplitPutaway --> Putaway(putawayId:%s) Parent putaway's Children are miss ",putawayId);
    			throw new BizException("err_rec_putaway_unsplitFail_hasNotParent");
    		}
    		RecPutaway splitChildRecPutaway = listSplitRecPutaway.get(0);
    		RecPutaway surplusChildRecPutaway = listSplitRecPutaway.get(1);
    		RecPutaway brotherRecPutaway = listSplitRecPutaway.get(0);
    		if ( putawayId.equals(brotherRecPutaway.getPutawayId()) ) {
    			brotherRecPutaway = listSplitRecPutaway.get(1);
    		}
    		
    		if ( Constant.PUTAWAY_STATUS_OPEN != brotherRecPutaway.getPutawayStatus() ) {
				if ( Constant.PUTAWAY_STATUS_CANCEL == brotherRecPutaway.getPutawayStatus() ) {
					List<RecPutaway> listBrotherChildRecPutaway = this.ptwExtlService.listSplitPutaway(brotherRecPutaway.getPutawayId());
					if ( PubUtil.isEmpty(listBrotherChildRecPutaway) ) {
						throw new BizException("err_rec_putaway_unsplit_brotherHasCancel");
					} else {
						throw new BizException("err_rec_putaway_unsplit_brotherHasChild");
					}
				} else {
					throw new BizException("err_rec_putaway_unsplit_allChildOpen");
				}
			}
    		// 查询父单
    		RecPutaway parentRecPutaway = this.ptwExtlService.findPutawayById(putaway.getParentPutawayId());
    		if ( parentRecPutaway == null ) {
    			throw new BizException("err_rec_putaway_unsplitFail_hasNotParent");
    		}
    		List<String> listPutawayId = new ArrayList<String>();
    		listPutawayId.add(splitChildRecPutaway.getPutawayId());
    		listPutawayId.add(surplusChildRecPutaway.getPutawayId());
			// 删除子单
    		for (String recPutawayId : listPutawayId) {
        		List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(recPutawayId);
        		for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
        			this.ptwLocExtlService.deletePutawayLocationByPtwDetailId(recPutawayDetail.getPutawayDetailId());
				}
			}
    		this.ptwDetailExtlService.deletePutawayDetailByPtwId(listPutawayId);
    		this.ptwExtlService.deletePutawayById(listPutawayId);
    		// 恢复父单
    		this.ptwExtlService.updatePutawayStatus(parentRecPutaway.getPutawayId(), Constant.PUTAWAY_STATUS_OPEN);
		}
	}
	
	
	/**
	 * 拆分上架单
	 * @param putawayId 上架单id
	 * @throws Exception
	 * @version 2017年3月2日 上午8:42:13<br/>
	 * @author andy wang<br/>
	 */
	public void split ( RecPutawayVO param_recPutawayVO ) throws Exception {
		if ( param_recPutawayVO == null || param_recPutawayVO.getPutaway() == null ) {
    		log.error("splitPutaway --> param_recPutawayVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	String putawayId = param_recPutawayVO.getPutaway().getPutawayId();
		if ( putawayId == null ) {
    		log.error("splitPutaway --> putawayId is null!");
    		throw new NullPointerException("id is null!");
    	}
    	List<RecPutawayDetailVO> listSavePutawayDetailVO = param_recPutawayVO.getListSavePutawayDetailVO();
		if ( PubUtil.isEmpty(listSavePutawayDetailVO) ) {
    		log.error("splitPutaway --> listSavePutawayDetail is null!");
    		throw new BizException("err_rec_putaway_split_detailEmpty");
    	}
		List<RecPutawayDetail> listSavePutawayDetail = new ArrayList<RecPutawayDetail>();
		for (RecPutawayDetailVO recPutawayDetailVO : listSavePutawayDetailVO) {
			if ( recPutawayDetailVO != null && recPutawayDetailVO.getPutawayDetail() != null ) {
				listSavePutawayDetail.add(recPutawayDetailVO.getPutawayDetail());
			}
		}
    	// 查询上架单
    	RecPutawayVO p_recPutawayVO = new RecPutawayVO(new RecPutaway());
    	p_recPutawayVO.loginInfo();
    	RecPutaway putaway = p_recPutawayVO.getPutaway();
		putaway.setPutawayId(putawayId);
    	RecPutaway sourceRecPutaway = this.ptwExtlService.findPutawayById(putawayId);
    	// 查询上架单明细
    	List<RecPutawayDetail> listSourceRecPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(putawayId);
    	// 运行拆分策略
    	SplitStrategy<RecPutaway,RecPutawayDetail> putawaySplit = new PutawaySplitStrategy(sourceRecPutaway, listSourceRecPutawayDetail, listSavePutawayDetail).execute();
    	
    	// 201704191411 与何国贤、黄平息讨论后，决定拆分后的任意一子单的总数量为0，不能进行拆分操作，因为总数量为0的子单没有任何意义。
    	// 最后决定使用以下判断，如果拆分后的任意一子单的明细集合数量为0时不进行拆分
    	List<RecPutawayDetail> listSplitDetail = putawaySplit.getListSplitDetail();
		List<RecPutawayDetail> listSurplusDetail = putawaySplit.getListSurplusDetail();
		if ( PubUtil.isEmpty(listSplitDetail) 
    			|| PubUtil.isEmpty(listSurplusDetail) ) {
    		throw new BizException("err_split_splitQtyNotZero");
    	}
    	
    	// 拼接ASN单号、PO单号
    	RecPutaway splitObj = putawaySplit.getSplitObj();
		RecPutaway surplusObj = putawaySplit.getSurplusObj();
		for (RecPutawayDetail recPutawayDetail : listSplitDetail) {
			if ( !StringUtil.isEmpty(recPutawayDetail.getAsnId()) ) {
				RecAsn asn = FqDataUtils.getAsnById(this.asnExtlService, recPutawayDetail.getAsnId());
				String asnNo = splitObj.getAsnNo();
				if ( StringUtil.isTrimEmpty(asnNo) ) {
					splitObj.setAsnNo(asn.getAsnNo());
				} else {
					splitObj.setAsnNo(asnNo + "," + asn.getAsnNo());
				}
				String poNo = splitObj.getPoNo();
				if ( StringUtil.isTrimEmpty(poNo) ) {
					splitObj.setPoNo(asn.getPoNo());
				} else {
					splitObj.setPoNo(poNo + "," + asn.getPoNo());
				}
			}
		}

		for (RecPutawayDetail recPutawayDetail : listSurplusDetail) {
			if ( !StringUtil.isEmpty(recPutawayDetail.getAsnId()) ) {
				RecAsn asn = FqDataUtils.getAsnById(this.asnExtlService, recPutawayDetail.getAsnId());
				String asnNo = surplusObj.getAsnNo();
				if ( StringUtil.isTrimEmpty(asnNo) ) {
					surplusObj.setAsnNo(asn.getAsnNo());
				} else {
					surplusObj.setAsnNo(asnNo + "," + asn.getAsnNo());
				}
				String poNo = surplusObj.getPoNo();
				if ( StringUtil.isTrimEmpty(poNo) ) {
					surplusObj.setPoNo(asn.getPoNo());
				} else {
					surplusObj.setPoNo(poNo + "," + asn.getPoNo());
				}
			}
		}
    	
    	// 保存拆分对象
    	this.ptwExtlService.insertPutaway(splitObj);
    	// 保存剩余对象
    	this.ptwExtlService.insertPutaway(surplusObj);
    	// 更新原Asn单状态
    	this.ptwExtlService.updatePutawayStatus(sourceRecPutaway.getPutawayId(), Constant.PUTAWAY_STATUS_CANCEL);
    	// 保存拆分明细对象
    	this.ptwDetailExtlService.insertPutawayDetail(listSplitDetail);
    	// 保存剩余明细对象
    	this.ptwDetailExtlService.insertPutawayDetail(listSurplusDetail);
	}
	
	
	/**
	 * 打印上架单
	 * @param param_putawayId 上架单id
	 * @throws Exception
	 * @version 2017年3月1日 下午8:37:46<br/>
	 * @author andy wang<br/>
	 */
	@Deprecated
	public void printPutaway ( String param_putawayId ) throws Exception {
		if ( StringUtil.isTrimEmpty(param_putawayId) ) {
			log.error("printPutaway --> param_putawayId is null!");
    		throw new NullPointerException("parameter is null!");
		}
		// 根据上架单id，查询上架单
		RecPutaway putaway = this.ptwExtlService.findPutawayById(param_putawayId);
		if ( putaway == null ) {
			throw new BizException("err_rec_putaway_null");
		}
		if ( Constant.PUTAWAY_STATUS_ACTIVE != putaway.getPutawayStatus() ) {
			throw new BizException("err_rec_putaway_print_statusNotActive");
		}
		// 更改状态为工作中
		this.ptwExtlService.updatePutawayStatus(param_putawayId, Constant.PUTAWAY_STATUS_WORKING);
	}
	
	/**
	 * 根据id，显示上架单信息
	 * @param param_putawayId 上架单id
	 * @return 上架单信息
	 * @throws Exception
	 * @version 2017年3月1日 下午2:16:41<br/>
	 * @author andy wang<br/>
	 */
	@Transactional(readOnly=true)
	public RecPutawayVO view ( String param_putawayId ) throws Exception {
		if ( StringUtil.isTrimEmpty(param_putawayId) ) {
			log.error("viewPutaway --> param_putawayId is null!");
    		throw new NullPointerException("parameter is null!");
		}
		RecPutawayVO recPutawayVO = new RecPutawayVO();
		// 根据上架单id，查询上架单
		RecPutaway putaway = this.ptwExtlService.findPutawayById(param_putawayId);
		if ( putaway == null ) {
			throw new BizException("err_rec_putaway_null");
		}
		recPutawayVO.setPutaway(putaway);
		recPutawayVO.searchCache();
		RecUtil.defPtwQWV(putaway);
		//查询仓库
		MetaWarehouse warehouse = warehouseExtlService.findWareHouseById(putaway.getWarehouseId());
		recPutawayVO.setWarehouseComment(warehouse.getWarehouseName());
		recPutawayVO.setWarehouseNo(warehouse.getWarehouseNo());
		// 根据上架单id，查询上架单明细
		List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(param_putawayId);
		if ( PubUtil.isEmpty(listPutawayDetail) ) {
			return recPutawayVO;
		}
		List<RecPutawayDetailVO> listPutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		recPutawayVO.setListPutawayDetailVO(listPutawayDetailVO);
		Map<String,RecAsn> mapAsn = new HashMap<String,RecAsn>();
		Map<String,MetaLocation> mapLocation = new HashMap<String,MetaLocation>();
		for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
			recPutawayDetail.clear();
			RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO(recPutawayDetail).searchCache();
			listPutawayDetailVO.add(recPutawayDetailVO);
			RecUtil.defPutawayDetailPlanQWV(recPutawayDetail);
			RecUtil.defPutawayDetailRealQWV(recPutawayDetail);
			recPutawayDetailVO.setHandlingQty(recPutawayDetail.getPlanPutawayQty() - recPutawayDetail.getRealPutawayQty());
			recPutawayDetailVO.setHandlingWeight(NumberUtil.sub(recPutawayDetail.getPlanPutawayWeight(), recPutawayDetail.getRealPutawayWeight()));
			recPutawayDetailVO.setHandlingVolume(NumberUtil.sub(recPutawayDetail.getPlanPutawayVolume(), recPutawayDetail.getRealPutawayVolume()));
			// 补充货主名称
			String ownerComment = FqDataUtils.getMerchantNameById(this.merchantService, recPutawayDetail.getOwner());
			recPutawayDetailVO.setOwnerComment(ownerComment);
			// 根据上架单明细中的ASN单id，查询ASN单信息,设置ASN单号、PO单号
			RecAsn recAsn = FqDataUtils.getAsnById(this.asnExtlService, recPutawayDetail.getAsnId());
			if ( recAsn != null ) {
				recPutawayDetailVO.setAsnNo(recAsn.getAsnNo());
				recPutawayDetailVO.setPoNo(recAsn.getPoNo());
			}
			if ( !mapAsn.containsKey(recPutawayDetail.getAsnId()) ) {
				mapAsn.put(recPutawayDetail.getAsnId(), recAsn);
			}
			// 根据上架单明细中的货品id，查询货品信息(货品重复率较低暂不使用Map缓存),设置货品名称、规格型号
			if ( !StringUtil.isTrimEmpty(recPutawayDetail.getSkuId()) ) {
				MetaSku metaSku = FqDataUtils.getSkuById(this.skuService, recPutawayDetail.getSkuId());
				if ( metaSku != null ) {
					recPutawayDetailVO.setSku(metaSku);
					recPutawayDetailVO.setSkuComment(metaSku.getSkuName());
					recPutawayDetailVO.setSpecModelComment(metaSku.getSpecModel());
					recPutawayDetailVO.setSkuNo(metaSku.getSkuNo());
					recPutawayDetailVO.setPerVolume(metaSku.getPerVolume());
					recPutawayDetailVO.setPerWeight(metaSku.getPerWeight());
					recPutawayDetailVO.setSkuBarCode(metaSku.getSkuBarCode());
				}
			}
			if (recPutawayDetail.getSkuStatus() != null) {
				recPutawayDetailVO.setSkuStatusName(paramService.getValue("SKU_STATUS", recPutawayDetail.getSkuStatus()));
			}
			// 根据上架单明细中的包装id，查询包装信息
			if ( !StringUtil.isTrimEmpty(recPutawayDetail.getPackId()) ) {
				recPutawayDetailVO.setPackComment(FqDataUtils.getPackUnit(this.packService, recPutawayDetail.getPackId()));
			}
			
			// 根据上架单明细中的ASN单明细id，查询ASN单明细
			if ( !StringUtil.isTrimEmpty(recPutawayDetail.getAsnDetailId()) ) {
				RecAsnDetail recAsnDetail = FqDataUtils.getAsnDetailById(this.asnDetailExtlService, recPutawayDetail.getAsnDetailId());
				if ( recAsnDetail != null ) {
					RecUtil.defReceiveQWV(recAsnDetail);
					recPutawayDetailVO.setOrderQty(recAsnDetail.getOrderQty());
					recPutawayDetailVO.setOrderWeight(recAsnDetail.getOrderWeight());
					recPutawayDetailVO.setOrderVolume(recAsnDetail.getOrderVolume());
				}
			}
			recPutawayDetailVO.setReceiveQty(recPutawayDetail.getPlanPutawayQty());
			recPutawayDetailVO.setReceiveWeight(recPutawayDetail.getPlanPutawayWeight());
			recPutawayDetailVO.setReceiveVolume(recPutawayDetail.getPlanPutawayVolume());
			InvStockVO p_stockVo = new InvStockVO(new InvStock());
			p_stockVo.setContainTemp(true);
			p_stockVo.getInvStock().setAsnDetailId(recPutawayDetail.getAsnDetailId());
			p_stockVo.getInvStock().setLocationId(recPutawayDetail.getLocationId());
			List<InvStock> listStock = this.stockService.list(p_stockVo);
			if ( PubUtil.isEmpty(listStock) ) {
				recPutawayDetailVO.setStockQty(0d);
				recPutawayDetailVO.setStockWeight(0d);
				recPutawayDetailVO.setStockVolume(0d);
			} else {
				InvStock invStock = listStock.get(0);
				recPutawayDetailVO.setStockQty(invStock.getStockQty());
				recPutawayDetailVO.setStockWeight(invStock.getStockWeight());
				recPutawayDetailVO.setStockVolume(invStock.getStockVolume());
			}
			
			Double totalQty = 0d;
			Double totalWeight = 0d;
			Double totalVolume = 0d;
			// 根据上架单明细id，查询上架单计划操作明细
			List<RecPutawayLocation> listPlanLocation = this.ptwLocExtlService.listPtwLocByPtwDetailId(recPutawayDetail.getPutawayDetailId(),true);
			if ( !PubUtil.isEmpty(listPlanLocation) ) {
				List<RecPutawayLocationVO> listPlanLocationVO = new ArrayList<RecPutawayLocationVO>();
				for (RecPutawayLocation planLocation : listPlanLocation) {
					RecUtil.defLocationQWV(planLocation);
					totalQty += planLocation.getPutawayQty();
					totalWeight = NumberUtil.add(totalWeight, planLocation.getPutawayWeight());
					totalVolume = NumberUtil.add(totalVolume, planLocation.getPutawayVolume());
					RecPutawayLocationVO vo = new RecPutawayLocationVO(planLocation);
					vo.setPerVolume(recPutawayDetailVO.getPerVolume());
					vo.setPerWeight(recPutawayDetailVO.getPerWeight());
					
					// 查询库位信息
					MetaLocation location = mapLocation.get(planLocation.getLocationId());
					if ( location == null ) {
						location = this.locationExtlService.findLocById(planLocation.getLocationId());
					}
					if ( location != null ) {
						mapLocation.put(planLocation.getLocationId(), location);
						vo.setLocationComment(location.getLocationName());
						vo.setPlanLocationNo(location.getLocationNo());
					}
					listPlanLocationVO.add(vo);
				}
				recPutawayDetailVO.setListPlanLocationVO(listPlanLocationVO);
			}
			// 根据上架单明细id，查询上架单实际操作明细
			List<RecPutawayLocation> listRealLocation = this.ptwLocExtlService.listPtwLocByPtwDetailId(recPutawayDetail.getPutawayDetailId(),false);
			if ( !PubUtil.isEmpty(listRealLocation) ) {
				List<RecPutawayLocationVO> listRealLocationVO = new ArrayList<RecPutawayLocationVO>();
				for (RecPutawayLocation realLocation : listRealLocation) {
					RecUtil.defLocationQWV(realLocation);
					totalQty -= realLocation.getPutawayQty();
					totalWeight = NumberUtil.sub(totalWeight, realLocation.getPutawayWeight());
					totalVolume = NumberUtil.sub(totalVolume, realLocation.getPutawayVolume());
					RecPutawayLocationVO vo = new RecPutawayLocationVO(realLocation);
					// 查询库位信息
					MetaLocation location = mapLocation.get(realLocation.getLocationId());
					if ( location == null ) {
						location = this.locationExtlService.findLocById(realLocation.getLocationId());
					}
					if ( location != null ) {
						mapLocation.put(realLocation.getLocationId(), location);
						vo.setRealLocationNo(location.getLocationNo());
						vo.setLocationComment(location.getLocationName());
					}
					listRealLocationVO.add(vo);
				}
				recPutawayDetailVO.setListRealLocationVO(listRealLocationVO);
			}
			recPutawayDetail.clear();
		}
		
		recPutawayVO.setOrderQty(0d);
		recPutawayVO.setOrderWeight(0d);
		recPutawayVO.setOrderVolume(0d);
		recPutawayVO.setReceiveQty(0d);
		recPutawayVO.setReceiveWeight(0d);
		recPutawayVO.setReceiveVolume(0d);
		for (RecAsn recAsn : mapAsn.values()) {
			recPutawayVO.setOrderQty(recPutawayVO.getOrderQty() + recAsn.getOrderQty());
			recPutawayVO.setOrderWeight(NumberUtil.add(recPutawayVO.getOrderWeight(), recAsn.getOrderWeight()));
			recPutawayVO.setOrderVolume(NumberUtil.add(recPutawayVO.getOrderVolume(), recAsn.getOrderVolume()));
			recPutawayVO.setReceiveQty(recPutawayVO.getReceiveQty() + recAsn.getReceiveQty());
			recPutawayVO.setReceiveWeight(NumberUtil.add(recPutawayVO.getReceiveWeight(), recAsn.getReceiveWeight()));
			recPutawayVO.setReceiveVolume(NumberUtil.add(recPutawayVO.getReceiveVolume(), recAsn.getReceiveVolume()));
		}
		return recPutawayVO;
	}
	
	
	/**
	 * 根据条件，分页查询上架单
	 * @param param_recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午1:48:21<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly=true)
	public Page<RecPutawayVO> list ( RecPutawayVO param_recPutawayVO ) throws Exception {
		if ( param_recPutawayVO == null ) {
			param_recPutawayVO = new RecPutawayVO();
		}
		if ( param_recPutawayVO.getPutaway() == null ) {
			param_recPutawayVO.setPutaway(new RecPutaway());
		}
		
		// 获取当前用户信息
		param_recPutawayVO.loginInfo();

		if ( param_recPutawayVO.getPutaway().getPutawayStatus() == null ) {
			param_recPutawayVO.setShowCancel(false);
		}
		
		if ( !StringUtil.isTrimEmpty(param_recPutawayVO.getLikeOwnerName()) ) {
			List<String> listOwnerId = this.merchantService.list(param_recPutawayVO.getLikeOwnerName());
			if ( PubUtil.isEmpty(listOwnerId) ) {
				return null;
			}
			// 根据货主id，获取上架明细id
			RecPutawayDetailVO p_ptwDetailVO = new RecPutawayDetailVO();
			p_ptwDetailVO.setListOwnerId(listOwnerId);
			List<RecPutawayDetail> listPtwDetail = this.ptwDetailExtlService.listPutawayDetailByExample(p_ptwDetailVO);
			List<String> listPtwId = new ArrayList<String>();
			for (RecPutawayDetail ptwDetail : listPtwDetail) {
				listPtwId.add(ptwDetail.getPutawayId());
			}
			param_recPutawayVO.setListPutawayId(listPtwId);
		}
		
		
		// 根据ASN单号和PO单号获取上架单id
		if ( !StringUtil.isEmpty(param_recPutawayVO.getLikeAsnNo()) 
				|| !StringUtil.isEmpty(param_recPutawayVO.getLikePoNo()) 
				|| !StringUtil.isEmpty(param_recPutawayVO.getLikeOwnerName()) ) {
			List<String> listPtwId = this.ptwExtlService.listPtwByAsnPoNo(param_recPutawayVO.getLikeAsnNo()
					, param_recPutawayVO.getLikePoNo(), param_recPutawayVO.getLikeOwnerName());
			if ( PubUtil.isEmpty(listPtwId) ) {
				return new Page();
			}
			param_recPutawayVO.setListPutawayId(listPtwId);
		}
		// 查询上架单列表
		Page page = this.ptwExtlService.listPutawayByPage(param_recPutawayVO);
		if ( page == null || page.isEmpty() ) {
			return new Page();
		}
		// 组装信息
		List<RecPutawayVO> listVO = new ArrayList<RecPutawayVO>();
		for (int i = 0; i < page.size(); i++) {
			RecPutaway putaway = (RecPutaway) page.get(i);
			if ( putaway == null ) {
				continue;
			}
			RecPutawayVO recPutawayVO = new RecPutawayVO(putaway).searchCache();
			listVO.add(recPutawayVO);

			// 查询仓库信息
			String warehouseName = FqDataUtils.getWarehouseNameById(this.warehouseExtlService, putaway.getWarehouseId());
			if ( !StringUtil.isTrimEmpty(warehouseName) ) {
				recPutawayVO.setWarehouseComment(warehouseName);
			}
			recPutawayVO.setDocTypeComment(paramService.getValue("PUTAWAYDOCTYPE", putaway.getDocType()));
			// 查询创建人信息
			recPutawayVO.setCreatePersonComment(FqDataUtils.getUserNameById(this.userService, putaway.getCreatePerson()));
			// 查询修改人信息
			recPutawayVO.setUpdatePersonComment(FqDataUtils.getUserNameById(this.userService, putaway.getCreatePerson()));
			// 查询工作人员信息
			recPutawayVO.setOpPersonComment(FqDataUtils.getUserNameById(this.userService, putaway.getOpPerson()));
			// 查询上架单对应的上架单明细
			List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(putaway.getPutawayId());
			Double totalPlanQty = 0d;
			Double totalPlanWeight = 0d;
			Double totalPlanVolume = 0d;
			Double totalRealQty = 0d;
			Double totalRealWeight = 0d;
			Double totalRealVolume = 0d;
			Map<String,MetaMerchant> mapOwner = new HashMap<String,MetaMerchant>();
			for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
				// 统计货主名称
				MetaMerchant owner = FqDataUtils.getMerchantById(this.merchantService, recPutawayDetail.getOwner());
//				if ( !StringUtil.isTrimEmpty(ownerName) ) {
//					mapOwner.put(recPutawayDetail.getOwner(), ownerName);
//				}
				if(owner != null) mapOwner.put(recPutawayDetail.getOwner(), owner);
				RecUtil.defPutawayDetailPlanQWV(recPutawayDetail);
				RecUtil.defPutawayDetailRealQWV(recPutawayDetail);
				// 统计计划数量/重量/体积
				totalPlanQty += recPutawayDetail.getPlanPutawayQty();
				totalPlanWeight += recPutawayDetail.getPlanPutawayWeight();
				totalPlanVolume += recPutawayDetail.getPlanPutawayVolume();
				// 统计上架数量/重量/体积
				totalRealQty += recPutawayDetail.getRealPutawayQty();
				totalRealWeight += recPutawayDetail.getRealPutawayWeight();
				totalRealVolume += recPutawayDetail.getRealPutawayVolume();
			}
			for (String ownerId : mapOwner.keySet()) {
				MetaMerchant owner = mapOwner.get(ownerId);
//				if ( StringUtil.isTrimEmpty(recPutawayVO.getOwnerComment())) {
//					recPutawayVO.setOwnerComment(ownerName);
//				} else {
//					recPutawayVO.setOwnerComment(String.format("%s,%s",recPutawayVO.getOwnerComment(),ownerName));
//				}
				recPutawayVO.setOwnerMerchant(owner);
			}
			recPutawayVO.setTotalPlanComment(totalPlanQty, totalPlanWeight, totalPlanVolume);
			recPutawayVO.setTotalRealComment(totalRealQty, totalRealWeight, totalRealVolume);
		}
		page.clear();
		page.addAll(listVO);
		return page;
	}
	
	/**
	 * 取消上架单
	 * @param param_listPutawayId 上架单id集合
	 * @throws Exception
	 * @version 2017年2月28日 上午10:10:58<br/>
	 * @author andy wang<br/>
	 */
	public void cancel ( List<String> param_listPutawayId ) throws Exception {
		if ( PubUtil.isEmpty(param_listPutawayId) ) {
			log.error("cancelPutaway --> param_listPutawayId is null!");
    		throw new NullPointerException("parameter is null!");
		}
		
		// 根据id查询上架单信息
		RecPutawayVO p_recPutawayVO = new RecPutawayVO(new RecPutaway());
		p_recPutawayVO.setListPutawayId(param_listPutawayId);
		List<RecPutaway> listPutaway = this.ptwExtlService.listPutawayByExample(p_recPutawayVO);
		
		if ( PubUtil.isEmpty(listPutaway) || param_listPutawayId.size() != listPutaway.size() ) {
			throw new BizException("err_rec_putaway_null");
		}
		
		for ( RecPutaway recPutaway : listPutaway ) {
			// 判断上架单是否打开状态
			if ( Constant.PUTAWAY_STATUS_OPEN != recPutaway.getPutawayStatus() ) {
				throw new BizException("err_rec_putaway_cancel_statusNotOpen");
			}
			this.ptwExtlService.updatePutawayStatus(recPutaway.getPutawayId(), Constant.PUTAWAY_STATUS_CANCEL);

			// 新增异常
			ExceptionLog expLog = new ExceptionLog();
			expLog.setExType(Constant.EXCEPTION_TYPE_REC_ABNORMAL);
			expLog.setExLevel(Constant.EXCEPTION_LEVEL_NORMAL);
			expLog.setInvolveBill(recPutaway.getPutawayNo());
			expLog.setExStatus(Constant.EXCEPTION_STATUS_TO_BE_HANDLED);
			expLog.setExDesc(String.format("单号：%s 。上架单单被取消",recPutaway.getPutawayNo()));
			this.exceptionLogService.add(expLog);
		}
	}
	
	
	/**
	 * 更新上架单
	 * @param recPutawayVO 上架单VO
	 * @throws Exception
	 * @version 2017年2月27日 下午5:04:37<br/>
	 * @author andy wang<br/>
	 */
	public void update ( RecPutawayVO param_recPutawayVO ) throws Exception {
		if ( param_recPutawayVO == null || param_recPutawayVO.getPutaway() == null ) {
			log.error("updatePutaway --> param_recPutawayVO or Putaway is null!");
    		throw new NullPointerException("parameter is null!");
		}
		RecPutaway param_putaway = param_recPutawayVO.getPutaway();
		String param_putawayId = param_putaway.getPutawayId();
		if ( StringUtil.isTrimEmpty(param_putaway.getPutawayId()) ) {
			throw new BizException("valid_rec_putaway_id_notnull");
		}
		// 查询上架单:
		// * 并判断状态是否打开
		// * 上架单是否存在
		RecPutaway sourcePutaway = this.ptwExtlService.findPutawayById(param_putawayId);
		if ( sourcePutaway == null ) {
			throw new BizException("err_rec_putaway_null");
		}
		if ( Constant.PUTAWAY_STATUS_OPEN != sourcePutaway.getPutawayStatus() ) {
			throw new BizException("err_rec_putaway_update_statusNotOpen");
		}
		sourcePutaway.setNote(param_putaway.getNote());
		FqDataUtils fq = new FqDataUtils();
		Set<String> setAsnNo = new HashSet<String>();
		Set<String> setPoNo = new HashSet<String>();
		List<String> listAsnDetailId = new ArrayList<String>();
		// 统计并验证操作的上架单明细 
		List<String> listRemovePutawayLocation = param_recPutawayVO.getListRemovePutawayLocation();
		List<RecPutawayDetailVO> listSavePutawayDetailVO = param_recPutawayVO.getListSavePutawayDetailVO();
		List<String> listRemovePutawayDetail = param_recPutawayVO.getListRemovePutawayDetail();
		List<String> listUpdatePutawayLocationId = param_recPutawayVO.getListUpdatePutawayLocation();
		List<String> listUpdatePtwDetailId = new ArrayList<String>();
		
		List<RecPutawayDetailVO> listUpdatePtwDetailVO = new ArrayList<RecPutawayDetailVO>();
		List<RecPutawayLocationVO> listUpdatePtwLocVO = new ArrayList<RecPutawayLocationVO>();
		List<RecPutawayDetail> listInsertPutawayDetail = new ArrayList<RecPutawayDetail>();
		List<RecPutawayLocation> listInsertPutawayLocation = new ArrayList<RecPutawayLocation>();
		sourcePutaway.setPlanQty(0d);
		sourcePutaway.setPlanWeight(0d);
		sourcePutaway.setPlanVolume(0d);
		// 获取新增的上架单明细集合
		for ( int i = 0 ; i < listSavePutawayDetailVO.size() ; i++) {
			RecPutawayDetailVO recPutawayDetailVO = listSavePutawayDetailVO.get(i);
			if ( recPutawayDetailVO == null || recPutawayDetailVO.getPutawayDetail() == null  ) {
				continue;
			}
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			if (param_recPutawayVO.getPutaway().getDocType() != null 
					&& param_recPutawayVO.getPutaway().getDocType() == Constant.PUTAWAY_DOCTYPE_BAD ) {
				putawayDetail.setSkuStatus(20);
			} else {
				putawayDetail.setSkuStatus(10);
			}
			
			// 查询ASN单明细
			RecAsnDetail asnDetail = FqDataUtils.getAsnDetailById(this.asnDetailExtlService, putawayDetail.getAsnDetailId());
			if ( asnDetail == null) {
				throw new BizException("err_rec_asnDetail_null");
			}
			if ( StringUtil.isTrimEmpty(asnDetail.getLocationId()) ) {
				throw new BizException("err_rec_putaway_insert_locationNull");
			}
			
			// 查询ASN单
			RecAsn asn = FqDataUtils.getAsnById(this.asnExtlService, asnDetail.getAsnId());
			if ( asn == null ) {
				throw new BizException("err_rec_asn_null");
			}

			setPoNo.add(asn.getPoNo());
			setAsnNo.add(asn.getAsnNo());
			
			// 判断明细项的操作，是新增还是删除
			String putawayDetailId = null;
			if ( StringUtil.isTrimEmpty(putawayDetail.getPutawayDetailId()) ) {
				// 新增时，添加id
				listInsertPutawayDetail.add(putawayDetail);
				putawayDetailId = IdUtil.getUUID();
				putawayDetail.setPutawayDetailId(putawayDetailId);
				putawayDetail.setPutawayId(sourcePutaway.getPutawayId());
				RecUtil.createPtwDetail(putawayDetail, asnDetail);
			} else if ( listRemovePutawayDetail.contains(putawayDetail.getPutawayDetailId()) ) {
				// 删除或者没有更新操作时，没必要做下面的业务逻辑
//				listSavePutawayDetailVO.remove(i--);
				continue;
			}

			if ( listAsnDetailId.contains(getAsnDetailKey(asnDetail.getAsnDetailId(),putawayDetail.getLocationId())) ) {
				throw new BizException("err_rec_putaway_sameAsnDetail");
			}
			listAsnDetailId.add(getAsnDetailKey(asnDetail.getAsnDetailId(),putawayDetail.getLocationId()));
			
			List<RecPutawayLocationVO> listSavePutawayLocationVO = recPutawayDetailVO.getListSavePutawayLocationVO();
			if ( PubUtil.isEmpty(listSavePutawayLocationVO) ) {
				// 没有操作明细可以不需要继续下面的业务逻辑
				continue;
			}
			boolean isUpdate = false;
			
			putawayDetail.setPlanPutawayQty(0d);
			putawayDetail.setPlanPutawayWeight(0d);
			putawayDetail.setPlanPutawayVolume(0d);
			System.out.println("------------------------------------------------");
			// 获取新增的上架单操作明细集合
			for (RecPutawayLocationVO recPutawayLocationVO : listSavePutawayLocationVO) {
				if ( recPutawayLocationVO == null || recPutawayLocationVO.getPutawayLocation() == null ) {
					continue;
				}
				RecPutawayLocation putawayLocation = recPutawayLocationVO.getPutawayLocation();
				if ( StringUtil.isTrimEmpty(putawayLocation.getPutawayLocationId()) ) {
					// 上架单操作明细id为空或者前面逻辑有创建上架单明细id
					listInsertPutawayLocation.add(putawayLocation);
					String putawayLocationId = IdUtil.getUUID();
					putawayLocation.setPutawayLocationId(putawayLocationId);
					putawayLocation.setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
					RecUtil.createPtwLocation(putawayLocation, putawayDetail);
				} else if ( 
//						listUpdatePutawayLocationId.contains(putawayLocation.getPutawayLocationId()) &&
						 !listRemovePutawayLocation.contains(putawayLocation.getPutawayLocationId()) ) {
					// 有更新记录,进行更新
					listUpdatePtwLocVO.add(recPutawayLocationVO);
					isUpdate = true;
				} else {
					continue;
				}
				listUpdatePutawayLocationId.add(putawayLocation.getPutawayLocationId());
				RecUtil.defLocationQWV(putawayLocation);
				// 统计上架明细的计划上架单数量/重量/体积
				putawayDetail.setPlanPutawayQty(putawayDetail.getPlanPutawayQty() + putawayLocation.getPutawayQty());
				putawayDetail.setPlanPutawayWeight(NumberUtil.add(putawayDetail.getPlanPutawayWeight() , putawayLocation.getPutawayWeight()));
				putawayDetail.setPlanPutawayVolume(NumberUtil.add(putawayDetail.getPlanPutawayVolume() , putawayLocation.getPutawayVolume()));
				
				sourcePutaway.setPlanQty(sourcePutaway.getPlanQty() + putawayLocation.getPutawayQty());
				sourcePutaway.setPlanWeight(NumberUtil.add(sourcePutaway.getPlanWeight() , putawayLocation.getPutawayWeight()));
				sourcePutaway.setPlanVolume(NumberUtil.add(sourcePutaway.getPlanVolume() , putawayLocation.getPutawayVolume()));
			} // for end by listSavePutawayLocationVO
			if ( isUpdate ) {
				listUpdatePtwDetailVO.add(recPutawayDetailVO);
				listUpdatePtwDetailId.add(putawayDetail.getPutawayDetailId());
//				FqDataUtils.putPtwDetail(putawayDetail.getPutawayDetailId(), putawayDetail);
			}
			/* 验证库存（不包括预分配数量的判断）*/
			this.checkStockWithoutPick(asnDetail, putawayDetail);
		} // for end by listSavePutawayDetailVO
		
		sourcePutaway.setAsnNo(null);
		sourcePutaway.setPoNo(null);
		
		/* 查询原上架明细及上架操作明细 */
		this.calSourcePtwLoc(sourcePutaway, fq, param_recPutawayVO, listAsnDetailId , listUpdatePtwDetailId);
		
		/* 生成上架单的ASN和PO单号 */
		this.setAsnAndPoNo(sourcePutaway, setAsnNo, setPoNo);
		
		// 更新上架单
		this.ptwExtlService.updatePtw(new RecPutawayVO(sourcePutaway));
		
		// 保存上架单明细
		this.ptwDetailExtlService.insertPutawayDetail(listInsertPutawayDetail);
		// 更新上架单明细
		for (RecPutawayDetailVO recPutawayDetailVO : listUpdatePtwDetailVO) {
			this.ptwDetailExtlService.updatePtwDetail(recPutawayDetailVO);
		}
		
		// 保存上架单操作明细
		this.ptwLocExtlService.insertPtwLocation(listInsertPutawayLocation);
		// 更新上架单操作明细
		for (RecPutawayLocationVO ptwLocVO : listUpdatePtwLocVO) {
			this.ptwLocExtlService.updatePutawayLocation(ptwLocVO);
		}
		
		// 删除上架单明细
		if ( !PubUtil.isEmpty(listRemovePutawayDetail) ) {
			for (String removePutawayDetailId : listRemovePutawayDetail) {
				if ( StringUtil.isTrimEmpty(removePutawayDetailId) ) {
					continue;
				}
				// 根据上架单明细id，删除上架单操作明细
				this.ptwLocExtlService.deletePutawayLocationByPtwDetailId(removePutawayDetailId);
				// 根据上架单明细id，删除上架单明细
				this.ptwDetailExtlService.deletePutawayDetailById(removePutawayDetailId);
			}
		}
		// 删除上架单操作明细
		if ( !PubUtil.isEmpty(listRemovePutawayLocation) ) {
			this.ptwLocExtlService.deletePutawayLocationById(listRemovePutawayLocation);
		}
	}

	/**
	 * 
	 * @param asnDetailId
	 * @param locationId
	 * @return
	 * @throws Exception
	 * @version 2017年3月26日 下午3:29:57<br/>
	 * @author andy wang<br/>
	 */
	private String getAsnDetailKey ( String asnDetailId , String locationId ) throws Exception {
		return asnDetailId + "_" + locationId;
	}
	
	/**
	 * @param sourcePutaway
	 * @param setAsnNo
	 * @param setPoNo
	 * @version 2017年3月26日 下午2:33:10<br/>
	 * @author andy wang<br/>
	 */
	private void setAsnAndPoNo(RecPutaway sourcePutaway, Set<String> setAsnNo, Set<String> setPoNo) {
		for (String asnNo : setAsnNo) {
			if ( StringUtil.isTrimEmpty(sourcePutaway.getAsnNo()) ) {
				sourcePutaway.setAsnNo(asnNo);
			} else {
				sourcePutaway.setAsnNo(String.format("%s,%s", sourcePutaway.getAsnNo() , asnNo));
			}
		}
		for (String poNo : setPoNo) {
			if ( StringUtil.isTrimEmpty(sourcePutaway.getPoNo()) ) {
				sourcePutaway.setPoNo(poNo);
			} else {
				sourcePutaway.setPoNo(String.format("%s,%s", sourcePutaway.getPoNo() , poNo));
			}
		}
	}
	
	
	/**
	 * 统计上架单操作明细信息，设置上架单信息
	 * —— 
	 * @param sourcePutaway 原上架单
	 * @param fq 缓存查询
	 * @param param_recPutawayVO 提交参数
	 * @param listAsnDetailId 已操作过的ASN单明细id
	 * @throws Exception 有操作过的相同ASN单明细id，系统报错<br/>
	 * @version 2017年3月26日 下午2:27:11<br/>
	 * @author andy wang<br/>
	 */
	private void calSourcePtwLoc ( RecPutaway sourcePutaway , FqDataUtils fq , RecPutawayVO param_recPutawayVO 
			, List<String> listAsnDetailId , List<String> listUpdatePtwDetailId ) throws Exception {
		List<String> listRemovePutawayLocation = param_recPutawayVO.getListRemovePutawayLocation();
		List<String> listRemovePutawayDetail = param_recPutawayVO.getListRemovePutawayDetail();
		List<String> listUpdatePutawayLocationId = param_recPutawayVO.getListUpdatePutawayLocation();
		if ( fq == null ) {
			fq = new FqDataUtils();
		}
		List<RecPutawayLocation> listSourcePtwLoc = this.ptwLocExtlService.listPutawayLocationByPtwId(sourcePutaway.getPutawayId(), true);
		for (RecPutawayLocation recPutawayLocation : listSourcePtwLoc) {
			if ( listRemovePutawayDetail.contains(recPutawayLocation.getPutawayDetailId())
					|| listRemovePutawayLocation.contains(recPutawayLocation.getPutawayLocationId())
					|| listUpdatePutawayLocationId.contains(recPutawayLocation.getPutawayLocationId()) 
					) {
				continue;
			}
			RecPutawayDetail ptwDetail = FqDataUtils.getPtwDetailById(this.ptwDetailExtlService, recPutawayLocation.getPutawayDetailId());
			if ( ptwDetail == null ) {
				throw new BizException("err_rec_putawayDetail_null");
			}
			// 统计上架明细的计划上架单数量/重量/体积
			ptwDetail.setPlanPutawayQty(ptwDetail.getPlanPutawayQty() + recPutawayLocation.getPutawayQty());
			ptwDetail.setPlanPutawayWeight(NumberUtil.add(ptwDetail.getPlanPutawayWeight() , recPutawayLocation.getPutawayWeight()));
			ptwDetail.setPlanPutawayVolume(NumberUtil.add(ptwDetail.getPlanPutawayVolume() , recPutawayLocation.getPutawayVolume()));
//			if ( !listUpdatePtwDetailId.contains(ptwDetail.getPutawayDetailId()) 
//					&& listAsnDetailId.contains(getAsnDetailKey(ptwDetail.getAsnDetailId(),ptwDetail.getLocationId())) ) {
//				throw new BizException("err_rec_putaway_sameAsnDetail");
//			}
			RecAsn asn = FqDataUtils.getAsnById(this.asnExtlService, ptwDetail.getAsnId());
			if ( StringUtil.isTrimEmpty(sourcePutaway.getAsnNo()) ) {
				sourcePutaway.setAsnNo(asn.getAsnNo());
			} else {
				sourcePutaway.setAsnNo(String.format("%s,%s", sourcePutaway.getAsnNo() , asn.getAsnNo()));
			}
			if ( StringUtil.isTrimEmpty(sourcePutaway.getPoNo()) ) {
				sourcePutaway.setPoNo(asn.getPoNo());
			} else {
				sourcePutaway.setPoNo(String.format("%s,%s", sourcePutaway.getPoNo() , asn.getPoNo()));
			}
			sourcePutaway.setPlanQty(sourcePutaway.getPlanQty() + recPutawayLocation.getPutawayQty());
			sourcePutaway.setPlanWeight(NumberUtil.add(sourcePutaway.getPlanWeight() , recPutawayLocation.getPutawayWeight()));
			sourcePutaway.setPlanVolume(NumberUtil.add(sourcePutaway.getPlanVolume() , recPutawayLocation.getPutawayVolume()));
		}
	}
	
	/**
	 * 验证库存（不包括预分配数量的判断）
	 * @param asnDetail ASN单明细
	 * @param putawayDetail 上架单明细
	 * @throws Exception 分配数量超过库存数量抛出的异常
	 * @version 2017年3月24日 下午9:31:37<br/>
	 * @author andy wang<br/>
	 */
	private void checkStockWithoutPick ( RecAsnDetail asnDetail , RecPutawayDetail putawayDetail ) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
		InvStockVO stockVO = new InvStockVO(new InvStock());
		InvStock p_stock = stockVO.getInvStock();
		p_stock.setWarehouseId(LoginUtil.getWareHouseId());
		p_stock.setOrgId(loginUser.getOrgId());
		p_stock.setAsnDetailId(asnDetail.getAsnDetailId());
		p_stock.setLocationId(putawayDetail.getLocationId());
		stockVO.setContainTemp(true);
		List<InvStock> listInvStock = this.stockService.list(stockVO);
		if ( listInvStock == null || listInvStock.isEmpty() ) {
			throw new BizException("err_rec_putaway_insert_stockNull");
		}
		InvStock invStock = listInvStock.get(0);
		
		// 检查库位分配数量是否大于待上架数量
		invStock.setStockQty(invStock.getStockQty()==null?0:invStock.getStockQty());
		if ( invStock.getStockQty() < putawayDetail.getPlanPutawayQty() ) {
			// 分配数量超出库存范围
			throw new BizException("err_rec_putaway_insert_stockOver");
		}
	}
	
	/**
	 * 失效上架单
	 * @param listPutawayId 上架单id集合
	 * @throws Exception
	 * @version 2017年2月26日 下午6:04:52<br/>
	 * @author andy wang<br/>
	 */
	public void disable ( List<String> listPutawayId ) throws Exception {
		if ( PubUtil.isEmpty(listPutawayId) ) {
			log.error("inactivePutaway --> listPutawayId is null!");
    		throw new NullPointerException("parameter is null!");
		}
		Map<String,MetaLocationVO> mapLocationCapacity = new HashMap<String,MetaLocationVO>();
		Map<String,InvStockVO> mapStock = new HashMap<String,InvStockVO>();
		Map<String,MetaSku> mapSku = new HashMap<String,MetaSku>();
		Principal loginUser = LoginUtil.getLoginUser();
		for (String putawayId : listPutawayId) {
			// 查询上架单
			RecPutaway recPutaway = this.ptwExtlService.findPutawayById(putawayId);
			if ( recPutaway == null ) {
				throw new BizException("err_rec_putaway_null");
			}
			if ( Constant.PUTAWAY_STATUS_ACTIVE != recPutaway.getPutawayStatus() ) {
				throw new BizException("err_rec_putaway_inactive_statusNotActive");
			}
			// 查询上架单明细
			List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(putawayId);
			if ( PubUtil.isEmpty(listPutawayDetail) ) {
				throw new BizException("err_rec_putaway_active_detailEmpty");
			}
			for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
				// 查询暂存区库位
				RecAsnDetail asnDetail = this.asnDetailExtlService.findByDetailId(recPutawayDetail.getAsnDetailId());
				if ( asnDetail == null ) {
					throw new BizException("err_rec_asnDetail_null");
				}
				// 根据ASN单明细记录的库位id，查询暂存区库位
				MetaLocation tempLocation = this.locationExtlService.findLocById(asnDetail.getLocationId());
				LocationUtil.checkLocation(tempLocation, true);
				// 查询上架单操作明细
				RecPutawayLocationVO p_putawayLocation = new RecPutawayLocationVO(new RecPutawayLocation());
				p_putawayLocation.getPutawayLocation().setPutawayDetailId(recPutawayDetail.getPutawayDetailId());
				p_putawayLocation.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
				List<RecPutawayLocation> listPutawayLocation = this.ptwLocExtlService.listPutawayLocationByExample(p_putawayLocation);
				if ( PubUtil.isEmpty(listPutawayLocation) ) {
					throw new BizException("err_rec_putaway_active_locationEmpty");
				}
				for (RecPutawayLocation recPutawayLocation : listPutawayLocation) {
					MetaLocationVO metaLocationVO = mapLocationCapacity.get(recPutawayLocation.getLocationId());
					// 统计库容量
					MetaLocation location = null;
					if ( metaLocationVO == null ) {
						// 查询库位
						location = this.locationExtlService.findLocById(recPutawayLocation.getLocationId());
						LocationUtil.checkLocation(location, true);
						metaLocationVO = new MetaLocationVO(location);
						mapLocationCapacity.put(recPutawayLocation.getLocationId(), metaLocationVO);
					}
					metaLocationVO.setPutawayDetail(recPutawayDetail);
					// 判断库容量（叠加）
					RecUtil.defLocationQWV(recPutawayLocation);
					// 查询货品对象，进行库容换算
					MetaSku metaSku = mapSku.get(recPutawayDetail.getSkuId());
					if ( metaSku == null ) {
						metaSku = this.skuService.get(recPutawayDetail.getSkuId());
						if ( metaSku == null ) {
							throw new BizException("err_meta_sku_null");
						}
					}
					BigDecimal capacity = LocationUtil.capacityConvert(recPutawayLocation.getPutawayQty(), metaSku);
					metaLocationVO.setCapacity(metaLocationVO.getCapacity().add(capacity));
					mapLocationCapacity.put(metaLocationVO.getLocation().getLocationId(), metaLocationVO);
					// 统计库存
					InvStockVO stockVo = mapStock.get(asnDetail.getAsnDetailId());
					if ( stockVo == null ) {
						stockVo = new InvStockVO(new InvStock());
						stockVo.getInvStock().setSkuId(asnDetail.getSkuId());
						stockVo.getInvStock().setLocationId(asnDetail.getLocationId());
						stockVo.getInvStock().setBatchNo(asnDetail.getBatchNo());
						stockVo.setContainBatch(true);
						stockVo.getInvStock().setAsnDetailId(asnDetail.getAsnDetailId());
						stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
						stockVo.getInvStock().setOrgId(loginUser.getOrgId());
						mapStock.put(asnDetail.getAsnDetailId(),stockVo);
					}
					Double findNum = stockVo.getFindNum();
					if ( findNum == null ) {
						findNum = 0d;
					}
					stockVo.setFindNum(findNum + recPutawayLocation.getPutawayQty());
				}
			}
		}

		// 保存扣减库位预分配库容量
		for (MetaLocationVO metaLocationVO : mapLocationCapacity.values()) {
			RecPutawayDetail putawayDetail = metaLocationVO.getPutawayDetail();
			MetaLocation location = metaLocationVO.getLocation();
			this.locationExtlService.minusPreusedCapacity( putawayDetail.getSkuId(), location.getLocationId() , location.getPackId(), metaLocationVO.getCapacity());
		}
		// 判断库存
		for (InvStockVO stockVo : mapStock.values()) {
			// 保存增加暂存区库存预分配数量
			this.stockService.unlockOutStock(stockVo);
		}
		
		// 修改上架单状态
		for (int i = 0; i < listPutawayId.size(); i++) {
			this.taskService.cancelByOpId(listPutawayId.get(i), loginUser.getUserId());
			this.ptwExtlService.updatePutawayStatus(listPutawayId.get(i), Constant.PUTAWAY_STATUS_OPEN);
		}
	}

	/**
	 * 生效上架单
	 * @param param_putawayVO 上架单VO
	 * @version 2017年2月22日 下午5:45:23<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	public void enable ( RecPutawayVO param_putawayVO ) throws Exception {
		if ( param_putawayVO == null || PubUtil.isEmpty(param_putawayVO.getListPutawayId()) ) {
			log.error("activePutaway --> listPutawayId is null!");
    		throw new NullPointerException("parameter is null!");
		}
//		if ( param_putawayVO.getTsTaskVo() == null 
//				|| param_putawayVO.getTsTaskVo().getTsTask() == null 
//				|| StringUtil.isTrimEmpty(param_putawayVO.getTsTaskVo().getTsTask().getOpPerson()) ) {
//			log.error("activePutaway --> task is null!");
//    		throw new NullPointerException("parameter task is null!");
//		}
		List<String> listPutawayId = param_putawayVO.getListPutawayId();
		Map<String,MetaLocationVO> mapLocationCapacity = new HashMap<String,MetaLocationVO>();
		Map<String,InvStockVO> mapStock = new HashMap<String,InvStockVO>();
		Map<String,MetaSku> mapSku = new HashMap<String,MetaSku>();
		Principal loginUser = LoginUtil.getLoginUser();
		for (String putawayId : listPutawayId) {
			// 查询上架单
			RecPutaway recPutaway = this.ptwExtlService.findPutawayById(putawayId);
			if ( recPutaway == null ) {
				throw new BizException("err_rec_putaway_null");
			}
			if ( Constant.PUTAWAY_STATUS_OPEN != recPutaway.getPutawayStatus() ) {
				throw new BizException("err_rec_putaway_active_statusNotOpen");
			}
			// 查询上架单明细
			List<RecPutawayDetail> listPutawayDetail = this.ptwDetailExtlService.listPutawayDetailByPtwId(putawayId);
			if ( PubUtil.isEmpty(listPutawayDetail) ) {
				throw new BizException("err_rec_putaway_active_detailEmpty");
			}
			for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
				// 查询暂存区库位
				RecAsnDetail asnDetail = this.asnDetailExtlService.findByDetailId(recPutawayDetail.getAsnDetailId());
				if ( asnDetail == null ) {
					throw new BizException("err_rec_asnDetail_null");
				}
				// 根据ASN单明细记录的库位id，查询暂存区库位
				MetaLocation tempLocation = this.locationExtlService.findLocById(asnDetail.getLocationId());
				LocationUtil.checkLocation(tempLocation, true);
				// 查询上架单操作明细
				RecPutawayLocationVO p_putawayLocation = new RecPutawayLocationVO(new RecPutawayLocation());
				p_putawayLocation.getPutawayLocation().setPutawayDetailId(recPutawayDetail.getPutawayDetailId());
				p_putawayLocation.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
				List<RecPutawayLocation> listPutawayLocation = this.ptwLocExtlService.listPutawayLocationByExample(p_putawayLocation);
				if ( PubUtil.isEmpty(listPutawayLocation) ) {
					throw new BizException("err_rec_putaway_active_locationEmpty");
				}
				for (RecPutawayLocation recPutawayLocation : listPutawayLocation) {
					MetaLocationVO metaLocationVO = mapLocationCapacity.get(recPutawayLocation.getLocationId());
					if ( StringUtil.isTrimEmpty(recPutawayLocation.getLocationId()) ) {
						throw new BizException("err_main_location_choiceLocation");
					}
					// 统计库容量
					MetaLocation location = null;
					if ( metaLocationVO == null ) {
						// 查询库位
						location = FqDataUtils.getLocById(this.locationExtlService, recPutawayLocation.getLocationId());
						if ( location == null ) {
							throw new BizException("err_main_location_null");
						}
						LocationUtil.checkLocation(location, true);
						metaLocationVO = new MetaLocationVO(location);
						mapLocationCapacity.put(recPutawayLocation.getLocationId(), metaLocationVO);
					} else {
						location = metaLocationVO.getLocation();
					}
					// 库位被其他货主占用
					if ( !StringUtil.isTrimEmpty(recPutawayDetail.getOwner())
							&& !StringUtil.isTrimEmpty(location.getOwner())
							&& !recPutawayDetail.getOwner().equals(location.getOwner()) ) {
						throw new BizException("err_rec_putaway_locationOwnerOccupy");
					}
					metaLocationVO.setPutawayDetail(recPutawayDetail);
					// 判断库容量（叠加）
					RecUtil.defLocationQWV(recPutawayLocation);
					// 查询货品对象，进行库容换算
					MetaSku metaSku = mapSku.get(recPutawayDetail.getSkuId());
					if ( metaSku == null ) {
						metaSku = this.skuService.get(recPutawayDetail.getSkuId());
						if ( metaSku == null ) {
							throw new BizException("err_meta_sku_null");
						}
					}
					BigDecimal capacity = LocationUtil.capacityConvert(recPutawayLocation.getPutawayQty(), metaSku);
					metaLocationVO.setCapacity(metaLocationVO.getCapacity().add(capacity));
					mapLocationCapacity.put(metaLocationVO.getLocation().getLocationId(), metaLocationVO);
					// 统计库存
					InvStockVO stockVo = mapStock.get(asnDetail.getAsnDetailId());
					if ( stockVo == null ) {
						stockVo = new InvStockVO(new InvStock());
						stockVo.getInvStock().setSkuId(asnDetail.getSkuId());
						stockVo.getInvStock().setLocationId(asnDetail.getLocationId());
						stockVo.setContainBatch(true);
						stockVo.getInvStock().setBatchNo(asnDetail.getBatchNo());
						stockVo.getInvStock().setAsnDetailId(asnDetail.getAsnDetailId());
						stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
						stockVo.getInvStock().setOrgId(loginUser.getOrgId());
						mapStock.put(asnDetail.getAsnDetailId(),stockVo);
					}
					Double findNum = stockVo.getFindNum();
					if ( findNum == null ) {
						findNum = 0d;
					}
					stockVo.setFindNum(findNum + recPutawayLocation.getPutawayQty());
				}
			}
		}
		
		// 判断库容量
		for (MetaLocationVO metaLocationVO : mapLocationCapacity.values()) {
			if ( LocationUtil.checkCapacity(metaLocationVO) ) {
				throw new BizException("err_main_location_capacityNotEnough",metaLocationVO.getLocation().getLocationName());
			}
			RecPutawayDetail putawayDetail = metaLocationVO.getPutawayDetail();
			MetaLocation location = metaLocationVO.getLocation();
			// 保存增加库位预分配库容量
			this.locationExtlService.addPreusedCapacity( putawayDetail.getSkuId(), location.getLocationId() , location.getPackId(), metaLocationVO.getCapacity());
		}
		// 判断库存
		for (InvStockVO stockVo : mapStock.values()) {
			// 保存增加暂存区库存预分配数量
			this.stockService.lockOutStock(stockVo);
		}
		
		// 修改上架单状态
		for (int i = 0; i < listPutawayId.size(); i++) {
			this.ptwExtlService.updatePutawayStatus(listPutawayId.get(i), Constant.PUTAWAY_STATUS_ACTIVE);
			// 生成任务
//			this.taskService.create(param_putawayVO.getTsTaskVo().getTsTask().getOpPerson(),Constant.TASK_TYPE_PUTAWAY,listPutawayId.get(i));
			this.taskService.create(Constant.TASK_TYPE_PUTAWAY,listPutawayId.get(i));
		}
	}
	
	
	/**
	 * 保存上架单
	 * @param param_recPutawayVO 要保存的上架单
	 * @return 保存后的上架单对象
	 * @throws Exception
	 * @version 2017年2月20日 下午3:08:04<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO add ( RecPutawayVO param_recPutawayVO ) throws Exception {
		if ( param_recPutawayVO == null || param_recPutawayVO.getPutaway() == null ) {
			log.error("insertPutaway --> param_recPutawayVO or Putaway is null!");
    		throw new NullPointerException("parameter is null!");
		}
		List<RecPutawayDetailVO> listSavePutawayDetailVO = param_recPutawayVO.getListSavePutawayDetailVO();
		Double totalPlanQty = 0d;
		Double totalPlanWeight = 0d;
		Double totalPlanVolume = 0d;
		Principal loginUser = LoginUtil.getLoginUser();
		String putawayId = IdUtil.getUUID();
		List<RecPutawayDetail> listPutawayDetail = new ArrayList<RecPutawayDetail>();
		List<RecPutawayLocation> listPutawayLocation = new ArrayList<RecPutawayLocation>();
		Set<String> setPoNo = new HashSet<String>();
		Set<String> setAsnNo = new HashSet<String>();
		List<String> listAsnDetailId = new ArrayList<String>();
		for (int i = 0; i < listSavePutawayDetailVO.size(); i++) {
			RecPutawayDetailVO recPutawayDetailVO = listSavePutawayDetailVO.get(i);
			if ( recPutawayDetailVO == null 
					|| recPutawayDetailVO.getPutawayDetail() == null ) {
				listSavePutawayDetailVO.remove(i--);
				continue;
			}
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			if (param_recPutawayVO.getPutaway().getDocType() != null 
					&& param_recPutawayVO.getPutaway().getDocType() == Constant.PUTAWAY_DOCTYPE_BAD ) {
				putawayDetail.setSkuStatus(20);
			} else {
				putawayDetail.setSkuStatus(10);
			}

			String asnDetailId = putawayDetail.getAsnDetailId();
			putawayDetail.setPutawayId(putawayId);
			listPutawayDetail.add(putawayDetail);
			
			if ( listAsnDetailId.contains(this.getAsnDetailKey(asnDetailId, putawayDetail.getLocationId())) ) {
				throw new BizException("err_rec_putaway_sameAsnDetail");
			}
			listAsnDetailId.add(this.getAsnDetailKey(asnDetailId, putawayDetail.getLocationId()) );
			
			// 查询ASN单明细
			RecAsnDetail asnDetail = this.asnDetailExtlService.findByDetailId(asnDetailId);
			if ( asnDetail == null) {
				throw new BizException("err_rec_asnDetail_null");
			}
			if ( StringUtil.isTrimEmpty(asnDetail.getLocationId()) ) {
				throw new BizException("err_rec_putaway_insert_locationNull");
			}
			
			// 查询ASN单
			RecAsn asn = FqDataUtils.getAsnById(this.asnExtlService, asnDetail.getAsnId());
			if ( asn == null ) {
				throw new BizException("err_rec_asn_null");
			}
			setPoNo.add(asn.getPoNo());
			setAsnNo.add(asn.getAsnNo());
			
			String putawayDetailId = IdUtil.getUUID();
			putawayDetail.setPutawayDetailId(putawayDetailId);
			RecUtil.createPtwDetail(putawayDetail, asnDetail);
			// 统计计划上架数量
			List<RecPutawayLocationVO> listSavePutawayLocationVO = recPutawayDetailVO.getListSavePutawayLocationVO();
			Double planQty = 0d;
			Double planWeight = 0d;
			Double planVolume = 0d;
			if ( !PubUtil.isEmpty(listSavePutawayLocationVO) ) {
				for (int j = 0; j < listSavePutawayLocationVO.size(); j++) {
					RecPutawayLocationVO recPutawayLocationVO = listSavePutawayLocationVO.get(j);
					RecPutawayLocation putawayLocation = recPutawayLocationVO.getPutawayLocation();
//					// 请选择库位
//					if ( StringUtil.isTrimEmpty(putawayLocation.getLocationId()) ) {
//						throw new BizException("err_rec_putaway_insert_locationNull");
//					}
//					MetaLocation location = fq.getLocById(this.locationExtlService, putawayLocation.getLocationId());
//					// 计划上架的库位被其他货主占用
//					if ( !StringUtil.isTrimEmpty(location.getOwner())
//							&& !StringUtil.isTrimEmpty(putawayDetail.getOwner())
//							&& !putawayDetail.getOwner().equals(location.getOwner()) ) {
//						log.error(String.format("库位%s被货主%s占用，不能被货主%s使用",location.getLocationId(),location.getOwner(),putawayDetail.getOwner()));
//						throw new BizException("err_rec_putaway_locationOwnerOccupy");
//					}
					RecUtil.defLocationQWV(putawayLocation);
					if ( putawayLocation == null || putawayLocation.getPutawayQty() == 0 ) {
						listSavePutawayLocationVO.remove(j--);
						continue;
					}
					String putawayLocationId = IdUtil.getUUID();
					putawayLocation.setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
					putawayLocation.setPutawayLocationId(putawayLocationId);
					RecUtil.createPtwLocation(putawayLocation, putawayDetail);
					planQty += putawayLocation.getPutawayQty();
					planWeight = NumberUtil.add(planWeight,putawayLocation.getPutawayWeight());
					planVolume = NumberUtil.add(planVolume,putawayLocation.getPutawayVolume());
					listPutawayLocation.add(putawayLocation);
				}
			}
			totalPlanQty += planQty;
			totalPlanWeight = NumberUtil.add(totalPlanWeight , planWeight);
			totalPlanVolume = NumberUtil.add(totalPlanVolume , planVolume);
			putawayDetail.setPlanPutawayQty(planQty);
			putawayDetail.setPlanPutawayWeight(planWeight);
			putawayDetail.setPlanPutawayVolume(planVolume);
			
			// 验证库存（不包括预分配数量的判断）
			try {
				this.checkStockWithoutPick(asnDetail, putawayDetail);
			} catch (Exception e) {
				System.out.println("321");
				throw e;
			}
		}
		// 创建/保存上架单
		RecPutaway recPutaway = new RecPutaway();
//		CommonVo p_commonVO = new CommonVo( loginUser.getOrgId() , BillPrefix.DOCUMENT_PREFIX_PUTAWAY_RECEIPT );
		String ptwNo = this.context.getStrategy4No(loginUser.getOrgId(), LoginUtil.getWareHouseId()).getPutawayNo(loginUser.getOrgId());
		recPutaway.setPutawayId(putawayId);
		recPutaway.setPutawayNo(ptwNo);
		recPutaway.setPutawayStatus(Constant.PUTAWAY_STATUS_OPEN);
		if (recPutaway.getDocType() == null) {
			recPutaway.setDocType(Constant.PUTAWAY_DOCTYPE_RECEIVE);
		}
		recPutaway.setOrgId(loginUser.getOrgId());
		recPutaway.setWarehouseId(LoginUtil.getWareHouseId());
		recPutaway.setPlanQty(totalPlanQty);
		recPutaway.setPlanWeight(totalPlanWeight);
		recPutaway.setPlanVolume(totalPlanVolume);
		if (param_recPutawayVO.getPutaway().getDocType() != null) {
			recPutaway.setDocType(param_recPutawayVO.getPutaway().getDocType());
		} else {
			recPutaway.setDocType(Constant.PUTAWAY_DOCTYPE_RECEIVE);
		}
		recPutaway.setAutoStatus(param_recPutawayVO.getPutaway().getAutoStatus());
		recPutaway.setNote(param_recPutawayVO.getPutaway().getNote());
		
		// 拼接ASN单号和拼接PO单号
		this.setAsnAndPoNo(recPutaway, setAsnNo, setPoNo);
//		if ( 1==1 ) {
//			throw new BizException("HAHA");
//		}
		// 保存上架单
		this.ptwExtlService.insertPutaway(recPutaway);
		// 保存上架单明细
		this.ptwDetailExtlService.insertPutawayDetail(listPutawayDetail);
		// 保存上架单操作明细
		this.ptwLocExtlService.insertPtwLocation(listPutawayLocation);
		RecPutawayVO recPutawayVO = new RecPutawayVO(recPutaway);
//		recPutawayVO.setListPutawayDetailVO(listSavePutawayDetailVO);
		recPutawayVO.setListSavePutawayDetailVO(listSavePutawayDetailVO);
		return recPutawayVO;
	}
	
	/**
	 * 收货确认后，根据ASN单id，创建上架单
	 * @param listAsnId ASN单id
	 * @return 上架单
	 * @throws Exception
	 * @version 2017年5月4日 下午3:21:33<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayVO> createPutaway ( List<String> listAsnId ) throws Exception {
		List<RecPutawayVO> listPtwVO = new ArrayList<RecPutawayVO>();
		for (String asnId : listAsnId) {
			RecPutawayVO putawayVO = new RecPutawayVO();
			RecPutawayVO putawayVOBad = new RecPutawayVO();
			List<RecPutawayDetailVO> listSavePutawayDetailVO = putawayVO.getListSavePutawayDetailVO();
			List<RecPutawayDetailVO> listSavePutawayDetailVOBad = putawayVOBad.getListSavePutawayDetailVO();
			// 根据ASN单id，查询库存
			InvStockVO p_stockVo = new InvStockVO(new InvStock());
			p_stockVo.getInvStock().setAsnId(asnId);
			List<InvStock> listStock = this.stockService.list(p_stockVo);

			// 根据ASN单id，查询ASN单明细
			Map<String, RecAsnDetail> mapAsnDetail = this.asnDetailExtlService.mapAsnDetailByAsnId(asnId);
			
			String putawayId = IdUtil.getUUID();
			
			Principal loginUser = LoginUtil.getLoginUser();
			for (InvStock invStock : listStock) {
				RecPutawayDetailVO putawayDetailVO = new RecPutawayDetailVO();
				RecPutawayDetail putawayDetail = putawayDetailVO.getPutawayDetail();
				RecAsnDetail recAsnDetail = mapAsnDetail.get(invStock.getAsnDetailId());
				if ( recAsnDetail == null ) {
					continue;
				}
				putawayDetail.setAsnId(invStock.getAsnId());
				putawayDetail.setAsnDetailId(invStock.getAsnDetailId());
				putawayDetail.setBatchNo(invStock.getBatchNo());
				putawayDetail.setLocationId(invStock.getLocationId());
				putawayDetail.setCreatePerson(loginUser.getUserId());
				putawayDetail.setMeasureUnit(recAsnDetail.getMeasureUnit());
				putawayDetail.setOwner(invStock.getOwner());
				putawayDetail.setPackId(invStock.getPackId());
				putawayDetail.setPutawayId(putawayId);
				putawayDetail.setSkuId(invStock.getSkuId());
				putawayDetail.setPlanPutawayQty(invStock.getStockQty());
				putawayDetail.setPlanPutawayWeight(invStock.getStockWeight());
				putawayDetail.setPlanPutawayVolume(invStock.getStockVolume());
				putawayDetail.setSkuStatus(recAsnDetail.getSkuStatus());
				
				RecPutawayLocationVO putawayLocationVO = new RecPutawayLocationVO();
				RecPutawayLocation putawayLocation = putawayLocationVO.getPutawayLocation();
				putawayLocation.setPutawayQty(invStock.getStockQty());
				putawayLocation.setPutawayWeight(invStock.getStockWeight());
				putawayLocation.setPutawayVolume(invStock.getStockVolume());
				putawayLocation.setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);

				putawayDetailVO.getListSavePutawayLocationVO().add(putawayLocationVO);
				if (recAsnDetail.getSkuStatus() == Constant.STOCK_SKU_STATUS_ABNORMAL) {
					listSavePutawayDetailVOBad.add(putawayDetailVO);
				} else {
					listSavePutawayDetailVO.add(putawayDetailVO);
				}
			}
			// 保存良品上架单
			if (!PubUtil.isEmpty(putawayVO.getListSavePutawayDetailVO()) ) {
				RecPutawayVO auto = context.getStrategy4Putaway(loginUser.getOrgId(), LoginUtil.getWareHouseId()).auto(putawayVO);
				List<RecPutawayDetailVO> listPutawayDetailVO = auto.getListPutawayDetailVO();
				for (RecPutawayDetailVO recPutawayDetailVO : listPutawayDetailVO) {
					recPutawayDetailVO.setListSavePutawayLocationVO(recPutawayDetailVO.getListPlanLocationVO());
				}
				auto.setListSavePutawayDetailVO(listPutawayDetailVO);
				auto.getPutaway().setAutoStatus(1);
				putawayVO = this.add(auto);
			}
			// 保存不良品上架单
			if (!PubUtil.isEmpty(putawayVOBad.getListSavePutawayDetailVO()) ) {
				RecPutawayVO autoBad = context.getStrategy4Putaway(loginUser.getOrgId(), LoginUtil.getWareHouseId()).auto(putawayVOBad);
				List<RecPutawayDetailVO> listPutawayDetailVOBad = autoBad.getListPutawayDetailVO();
				for (RecPutawayDetailVO recPutawayDetailVO : listPutawayDetailVOBad) {
					recPutawayDetailVO.setListSavePutawayLocationVO(recPutawayDetailVO.getListPlanLocationVO());
				}
				autoBad.getPutaway().setAutoStatus(1);
				autoBad.getPutaway().setDocType(Constant.PUTAWAY_DOCTYPE_BAD);
				putawayVO = this.add(autoBad);
			}
			listPtwVO.add(putawayVO);
			
			//原逻辑
//			RecPutawayVO putawayVO = new RecPutawayVO();
//			List<RecPutawayDetailVO> listSavePutawayDetailVO = putawayVO.getListSavePutawayDetailVO();
//			// 根据ASN单id，查询库存
//			InvStockVO p_stockVo = new InvStockVO(new InvStock());
//			p_stockVo.getInvStock().setAsnId(asnId);
//			List<InvStock> listStock = this.stockService.list(p_stockVo);
//
//			// 根据ASN单id，查询ASN单明细
//			Map<String, RecAsnDetail> mapAsnDetail = this.asnDetailExtlService.mapAsnDetailByAsnId(asnId);
//			
//			String putawayId = IdUtil.getUUID();
//			
//			Principal loginUser = LoginUtil.getLoginUser();
//			for (InvStock invStock : listStock) {
//				RecPutawayDetailVO putawayDetailVO = new RecPutawayDetailVO();
//				listSavePutawayDetailVO.add(putawayDetailVO);
//				RecPutawayDetail putawayDetail = putawayDetailVO.getPutawayDetail();
//				RecAsnDetail recAsnDetail = mapAsnDetail.get(invStock.getAsnDetailId());
//				if ( recAsnDetail == null ) {
//					continue;
//				}
//				putawayDetail.setAsnId(invStock.getAsnId());
//				putawayDetail.setAsnDetailId(invStock.getAsnDetailId());
//				putawayDetail.setBatchNo(invStock.getBatchNo());
//				putawayDetail.setLocationId(invStock.getLocationId());
//				putawayDetail.setCreatePerson(loginUser.getUserId());
//				putawayDetail.setMeasureUnit(recAsnDetail.getMeasureUnit());
//				putawayDetail.setOwner(invStock.getOwner());
//				putawayDetail.setPackId(invStock.getPackId());
//				putawayDetail.setPutawayId(putawayId);
//				putawayDetail.setSkuId(invStock.getSkuId());
//				putawayDetail.setPlanPutawayQty(invStock.getStockQty());
//				putawayDetail.setPlanPutawayWeight(invStock.getStockWeight());
//				putawayDetail.setPlanPutawayVolume(invStock.getStockVolume());
//				
//				RecPutawayLocationVO putawayLocationVO = new RecPutawayLocationVO();
//				RecPutawayLocation putawayLocation = putawayLocationVO.getPutawayLocation();
//				putawayLocation.setPutawayQty(invStock.getStockQty());
//				putawayLocation.setPutawayWeight(invStock.getStockWeight());
//				putawayLocation.setPutawayVolume(invStock.getStockVolume());
//				putawayLocation.setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
//				putawayDetailVO.getListSavePutawayLocationVO().add(putawayLocationVO);
//			}
//			RecPutawayVO auto = context.getStrategy4Putaway(loginUser.getOrgId(), LoginUtil.getWareHouseId()).auto(putawayVO);
//			List<RecPutawayDetailVO> listPutawayDetailVO = auto.getListPutawayDetailVO();
//			for (RecPutawayDetailVO recPutawayDetailVO : listPutawayDetailVO) {
//				recPutawayDetailVO.setListSavePutawayLocationVO(recPutawayDetailVO.getListPlanLocationVO());
//			}
//			auto.setListSavePutawayDetailVO(listPutawayDetailVO);
//			auto.getPutaway().setAutoStatus(1);
//			putawayVO = this.add(auto);
//			listPtwVO.add(putawayVO);
		}
		return listPtwVO;
	}

	@Override
	public boolean isSameSkuType(String skuId, String locationId) {
		try {
			checkSkuType(skuId, locationId);
			return true;
		} catch (Throwable e) {
			if(log.isWarnEnabled()) log.warn(e.getMessage());
		}
		return false;
	}

	@Override
	public List<RecPutawayVO> list4print(RecPutawayVO param_recPutawayVO) throws Exception {
		if(param_recPutawayVO == null || PubUtil.isEmpty(param_recPutawayVO.getListPutawayId())) return null;
		List<RecPutawayVO> voList = new ArrayList<RecPutawayVO>();
		for(String id : param_recPutawayVO.getListPutawayId()) {
			voList.add(this.view(id));
		}
		return voList;
	}


	@Override
	public int updateAssisStatus(String putawayId, String status) {
		RecPutaway entity = new RecPutaway();
		entity.setPutawayId(putawayId);
		entity.setAssisStatus(status);
		entity.setUpdateTime(new Date());
		return putawayDao.updateByPrimaryKeySelective(entity);
	}

//	/**
//	 * 传送入库数据对接仓储企业联网监管系统
//	 * @throws Exception
//	 */
//	public void transmitInstockXML(String putawayId) throws Exception{
//		
//		Principal loginUser = LoginUtil.getLoginUser();
//		
//		String messsageId = IdUtil.getUUID();
//		String emsNo = paramService.getKey(CacheName.EMS_NO);
//		String sendId = paramService.getKey(CacheName.TRAED_CODE);
//		String receiveId = paramService.getKey(CacheName.CUSTOM_CODE);
//		String sendChnalName = paramService.getKey(CacheName.MSMQ_SEND_CHNALNAME);
//		String label = paramService.getKey(CacheName.PARAM_MESSAGE_LABEL);
//		String in_stock_type = paramService.getKey(CacheName.TYPE_INSTOCK_NONBOND);
//		Date today = new Date();
//		String dateTime = DateFormatUtils.format(today, "yyyy-MM-dd HH:MM:SS");
//		String date = DateFormatUtils.format(today, "yyyy-MM-dd");
//		FqDataUtils fq = new FqDataUtils();
//		//查询asn单信息
//		RecPutawayVO putawayVo = view(putawayId);
//		if(putawayVo == null) throw new BizException("err_rec_putaway_null");
//		if(putawayVo.getListPutawayDetailVO() == null || putawayVo.getListPutawayDetailVO().isEmpty()) 
//			throw new BizException("err_rec_putawayDetail_null");
//		//检查是否全部上架状态
//		if(Constant.PUTAWAY_STATUS_COMPLETE != putawayVo.getPutaway().getPutawayStatus()){
//			throw new BizException("err_rec_asn_trans_statusNotputaway");
//		}
//		//创建入库报文
//		MSMQ msmq = new MSMQ();
//		//报文头参数
//		Head head = new Head();
//		head.setMESSAGE_ID(messsageId);
//		head.setMESSAGE_TYPE(Constant.MESSAGE_TYPE_STORE_TRANS);
//		head.setEMS_NO(emsNo);
//		head.setFUNCTION_CODE("A");//功能代码 新增
//		head.setMESSAGE_DATE(dateTime);
//		head.setSENDER_ID(sendId);
//		head.setRECEIVER_ID(receiveId);
//		head.setSEND_TYPE("0");
//		msmq.setHead(head);
//		//报文体
//		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
//		for(RecPutawayDetailVO putawayDetailVo:putawayVo.getListPutawayDetailVO()){
//			//查询货主
//			MetaMerchant owner = fq.getMerchantById(merchantService, putawayDetailVo.getPutawayDetail().getSkuId());
//			Map<String,String> map = new HashMap<String,String>();
//			for(RecPutawayLocationVO locationVo:putawayDetailVo.getListRealLocationVO()){
//				map.put("MESSAGE_ID", messsageId);
//				map.put("EMS_NO", emsNo);
//				map.put("IO_NO",putawayVo.getPutaway().getPutawayNo());
//				map.put("GOODS_NATURE", putawayDetailVo.getSku().getGoodsNature().toString());
//				map.put("IO_DATE", date);
//				map.put("COP_G_NO", putawayDetailVo.getSku().getHgGoodsNo());
//				map.put("G_NO", date);
//				map.put("HSCODE", putawayDetailVo.getSku().getHsCode());
//				map.put("GNAME", putawayDetailVo.getSku().getSkuName());
//				map.put("GMODEL", putawayDetailVo.getSku().getSpecModel());
//				map.put("CURR", putawayDetailVo.getSku().getCurr());
//				map.put("COUNTRY", putawayDetailVo.getSku().getOriginCountry());
//				map.put("QTY", locationVo.getPutawayLocation().getPutawayQty().toString());
//				map.put("UNIT", putawayDetailVo.getSku().getMeasureUnit());
//				map.put("TYPE", in_stock_type);
//				map.put("CHK_CODE", "01");
//				map.put("GATEJOB_NO", putawayVo.getPutaway().getGatejobNo());
//				map.put("WHS_CODE", putawayVo.getWarehouseNo());
//				map.put("LOCATION_CODE", locationVo.getLocationComment());
//				map.put("NOTE", owner.getHgMerchantNo());
//			}
//			mapList.add(map);
//		}
//		msmq.setBodyMaps(mapList);
//		//创建入库报文
//		TransmitXmlUtil tranUtil = new TransmitXmlUtil(msmq);
//		String xmlStr = tranUtil.formXml();
//		
//		//传送入库数据到仓储企业联网监管系统
//		boolean result = MSMQUtil.send(sendChnalName, label, xmlStr, messsageId.getBytes());
//		if(log.isInfoEnabled()) log.info("MSMQ接口运行结果：["+result+"]");	
//		
//		//保存报文
//		MsmqMessageVo messageVo = new MsmqMessageVo();
//		messageVo.getEntity().setMessageId(messsageId);
//		messageVo.getEntity().setFunctionType(Constant.FUNCTION_TYPE_PUTAWAY);
//		messageVo.getEntity().setOrderNo(putawayId);
//		messageVo.getEntity().setContent(xmlStr);
//		messageVo.getEntity().setSender(Constant.SYSTEM_TYPE_WMS);
//		messageVo.getEntity().setSendTime(new Date());
//		messageVo.getEntity().setCreatePerson(loginUser.getUserId());
//		msmqMessageService.add(messageVo);
//		
//		//修改asn单发送状态
//		if(result){
//			putawayVo.getPutaway().setTransStatus(Constant.SYNCSTOCK_STATUS_SEND_SUCCESS);
//			putawayVo.getPutaway().setUpdatePerson(loginUser.getUserId());
//			putawayDao.updateByPrimaryKeySelective(putawayVo.getPutaway());
//		}
//	}
	
	

}