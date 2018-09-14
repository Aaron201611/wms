package com.yunkouan.wms.modules.inv.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.excel.ExcelUtil;
import com.yunkouan.excel.impt.ExcelExport;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.FileUtil;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.assistance.service.IAssisService;
import com.yunkouan.wms.modules.assistance.vo.MessageData;
import com.yunkouan.wms.modules.inv.dao.IStockDao;
import com.yunkouan.wms.modules.inv.dao.IStockLogDao;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.inv.entity.InvAdjustDetail;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvShift;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IAdjustService;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.util.flow.autorep.AutoRepStockFlow;
import com.yunkouan.wms.modules.inv.vo.InStockVO;
import com.yunkouan.wms.modules.inv.vo.InStockVO4Excel;
import com.yunkouan.wms.modules.inv.vo.InvAdjustDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;
import com.yunkouan.wms.modules.inv.vo.InvLogVoExcel;
import com.yunkouan.wms.modules.inv.vo.InvOutLockDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvShiftVO;
import com.yunkouan.wms.modules.inv.vo.InvSkuStockVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO4Excel;
import com.yunkouan.wms.modules.inv.vo.InvWarnVO;
import com.yunkouan.wms.modules.inv.vo.StockVo2ERP;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayExtlService;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;
import com.yunkouan.wms.modules.send.dao.IDeliveryDao;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.service.IDeliveryService;
import com.yunkouan.wms.modules.send.service.IPickDetailService;
import com.yunkouan.wms.modules.send.service.IPickService;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.wms.modules.send.vo.TotalVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 在库管理服务实现类
 */
@Service
public class StockServiceImpl extends BaseService implements IStockService {
	protected static Log log = LogFactory.getLog(StockServiceImpl.class);
	/**
	 * 外部服务-移位
	 */
	@Autowired
	IShiftService shiftService;
	@Autowired
	private ISkuTypeService skuTypeService;
	/** 库区外调业务类 <br/> add by andy */
	@Autowired
	private IAreaExtlService areaExtlService;
	/**
	 * 外部服务-仓库
	 */
	@Autowired
	private IWarehouseExtlService warehouseExtlService;
	/**
	 * 外部服务-货商
	 */
	@Autowired
	IMerchantService merchantService;
//	/**
//	 * 外部服务-企业
//	 */
//	@Reference
//	IOrgService orgService;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	/**
	 * 外部服务-货品
	 */
	@Autowired
	ISkuService skuService;
	/**
	 * 外部服务-库位
	 */
	@Autowired
	ILocationExtlService locationService;
	/**
	 * 外部服务-库位
	 */
	@Autowired
	ILocationService iLocationService;
	@Autowired
	private IDeliveryDao deliveryDao;
	/**
	 * 外部服务-调账单
	 */
	@Autowired
	IAdjustService adjustService;
	/**
	 * 外部服务-库区
	 */
	@Autowired
	IAreaExtlService areaService;
	/**
	 * 外部服务-收货单
	 */
	@Autowired
	IASNExtlService asnService;
	/**
	 * 外部服务-上架单
	 */
	@Autowired
	IPutawayExtlService putawayExtlService;
	/**
	 * 外部服务-拣货单
	 */
	@Autowired
	IPickService pickService;
	/**
	 * 外部服务-发货单
	 */
	@Autowired
	IDeliveryService sendService;
	/**
	 * 外部服务-拣货单详情
	 */
	@Autowired
	IPickDetailService pickDetailService;
    /**
     * 在库管理持久化类
     * @author 王通<br/>
     */
	@Autowired
    private IStockDao stockDao;
	@Autowired
    private IStockLogDao stockLogDao;

	@Autowired
	private ISysParamService paramService;

	/**
     * Default constructor
     */
    public StockServiceImpl() {
    }

    /**
     * 查看库存详情，供内部调用
     * @param asnDetailId 
     * @param id 
     * @return
     * @throws Exception 
     */
    @Transactional(readOnly=false)
    public InvStock view(String skuId, String locationId, String batchNo, String asnDetailId) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = new InvStock();
		stockVo.setInvStock(stock);
		stock.setSkuId(skuId);
		stock.setLocationId(locationId);
		stock.setBatchNo(batchNo);
		stock.setAsnDetailId(asnDetailId);
		stock.setWarehouseId(LoginUtil.getWareHouseId());
		stock.setOrgId(loginUser.getOrgId());
		stockVo.setContainBatch(true);
		stockVo.setContainTemp(true);
		stockVo.setOnlyTemp(false);
		List<InvStock> list = this.list(stockVo);
		if (list.size() > 1) {
			throw new BizException("parameter_error");
		} else if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
		
//		Example example = new Example(InvStock.class);
//		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("skuId", skuId);
//		criteria.andEqualTo("locationId", locationId);
//		criteria.andEqualTo("warehouseId", LoginUtil.getWareHouseId());
//		criteria.andEqualTo("orgId", loginUser.getOrgId());
//		if (batchNo != null) {
//			criteria.andEqualTo("batchNo", batchNo);
//		} else {
//			criteria.andIsNull("batchNo");
//		}
//		if (!StringUtil.isTrimEmpty(asnDetailId)) {
//			criteria.andEqualTo("asnDetailId", asnDetailId);
//		}
//		List<InvStock> stockList = this.stockDao.selectByExample(example);
//        return stockList.get(0);
    }

    /**
     * 库存冻结
     * @param id
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月2日 上午10:22:08<br/>
     * @author 王通<br/>
     */
    @Override
    @Transactional(readOnly=false)
    public void freezeList(List<String> idList) {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	for (String id : idList) {
    		InvStock stock = new InvStock();
    		stock.setStockId(id);
    		stock.setIsBlock(Constant.STOCK_BLOCK_TRUE);
    		int ret = this.stockDao.updateByPrimaryKeySelective(stock);
            if (ret < 1) {
            	throw new BizException("err_stock_id_wrong");
            }
    	}
    }

    /**
     * 货品解冻
     * @param id 
     * @return
     */
    @Override
    @Transactional(readOnly=false)
    public void unfreezeList(List<String> idList) {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	for (String id : idList) {
    		InvStock stock = new InvStock();
    		stock.setStockId(id);
    		stock.setIsBlock(Constant.STOCK_BLOCK_FALSE);
            int ret = this.stockDao.updateByPrimaryKeySelective(stock);
            if (ret < 1) {
            	throw new BizException("err_stock_id_wrong");
            }
    	}
    }

    /**
     * 库存日志列表数据查询
     * @param page 
     * @return
     * @throws Exception 
     */
    @Override
    @Transactional(readOnly=false)
    public Page<InvLogVO> logList(InvLogVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Page<InvLogVO> page = null;
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		if (vo.getInvLog() == null) {
			InvLog invLog = new InvLog();
			vo.setInvLog(invLog);
		} else {
			// 查询日志列表 
			vo.getInvLog().setInvoiceBill(StringUtil.likeEscapeH(vo.getInvLog().getInvoiceBill()));
			vo.getInvLog().setBatchNo(StringUtil.likeEscapeH(vo.getInvLog().getBatchNo()));
		}
		vo.setOwnerName(StringUtil.likeEscapeH(vo.getOwnerName()));
		vo.setSkuName(StringUtil.likeEscapeH(vo.getSkuName()));
		//增加库位名称模糊查询
		vo.setLocationName(StringUtil.likeEscapeH(vo.getLocationName()));
		Date startDate = vo.getStartDate();
		Date endDate = vo.getEndDate();
		if (endDate != null) {
			if (startDate != null) {
				if (startDate.after(endDate)) {
					throw new BizException("err_stock_log_time_wrong");
				}
				if (endDate.after(new Date())) {
					throw new BizException("err_stock_log_time_limit");
				}
			}
			endDate = DateUtil.setEndTime(endDate);
		}
		vo.getInvLog().setWarehouseId(LoginUtil.getWareHouseId());
		vo.getInvLog().setOrgId(loginUser.getOrgId());
		List<InvLog> listInvLog = this.stockDao.listLog(vo);
		if ( listInvLog == null || listInvLog.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvLogVO> listVO = new ArrayList<InvLogVO>();
		for (int i = 0; i < listInvLog.size(); i++) {
			InvLog log = listInvLog.get(i);
			if ( log == null ) {
				continue;
			}
			InvLogVO logVo = new InvLogVO(log).searchCache();
			listVO.add(logVo);
			//  查询仓库信息
			MetaWarehouse wareHouse = this.warehouseExtlService.findWareHouseById(log.getWarehouseId());
			if ( wareHouse != null ) {
				logVo.setWarehouseName(wareHouse.getWarehouseName());
			}
			// 查询货品信息
			MetaSku metaSku = skuService.get(log.getSkuId());
			if (metaSku != null) {
				logVo.setSkuName(metaSku.getSkuName());
				logVo.setSkuNo(metaSku.getSkuNo());
				logVo.setSpecModel(metaSku.getSpecModel());
				logVo.setMeasureUnit(metaSku.getMeasureUnit());
				//  查询货主信息
				MetaMerchant merchant = merchantService.get(metaSku.getOwner());
				if (merchant != null) {
					logVo.setOwnerName(merchant.getMerchantName());
				}
			}
			logVo.setSkuStatusName(paramService.getValue("SKU_STATUS", log.getSkuStatus()));
			//查询库位信息
			MetaLocation location = locationService.findLocById(log.getLocationId());
			if (location != null) {
				logVo.setLocationName(location.getLocationName());
			}
			// 查询创建人信息
			SysUser createPerson = userService.get(log.getCreatePerson());
			if ( createPerson != null ) {
				logVo.setCreateUserName(createPerson.getUserName());
			}
			// 查询修改人信息
			SysUser updatePerson = null;
			if ( log.getCreatePerson().equals(log.getUpdatePerson()) ) {
				updatePerson = createPerson;
			} else {
				updatePerson = userService.get(log.getUpdatePerson());
			}
			if ( updatePerson != null ) {
				logVo.setUpdateUserName(updatePerson.getUserName());
			}
			// 查询作业人信息
			SysUser opPerson = userService.get(log.getOpPerson());
			if ( opPerson != null ) {
				logVo.setOpPersonName(opPerson.getUserName());
			}
			// 查询日志类型
			if (log.getLogType() != null) {
				logVo.setLogTypeName(paramService.getValue(CacheName.LOG_TYPE, log.getLogType()));
			}
			// 查询增减方式
			if (log.getOpType() != null) {
				logVo.setOpTypeName(paramService.getValue(CacheName.LOG_OP_TYPE, log.getOpType()));
			}
		}
		page.clear();
		page.addAll(listVO);
//		rm.setPage(page);
//		rm.setList(listVO);
		return page;
    }

    /**
	 * 查询货品库存是否足够
	  * @Description 
	  * 发货单生效时，系统自动判断当前发货仓库所有正常库位可分配库存（[可分配库存=库存表中发货仓库
	  * 该批次SKU库存数量 – 拣货分配数量]）是否有相应数量、相应批次的SKU。
	  * 若有，系统提示是否自动生成相应拣货单记录，由操作人员确认是否自动生成。
	  * 当选择系统自动生成拣货单记录时，系统在生成拣货单记录的同时自动分配拣货库位
	  * （系统初始拣货规则：排除收货区暂存库位库存及当前仓库被冻结的库位库位，
	  * 对正常库位库存按先进先出FIFO处理，分配拣货库位后的拣货单处于“打开”状态），
	  * 同时在拣货单明细中显示“分配数量”，且将此“分配数量”累加至拣货库位对应的库存记录“拣货分配数量”字段；
	  * 当选择不由系统自动生成拣货单记录时，后续可由人工创建拣货单。
	  * 当对发货单进行生效、库存不足时，系统提示没有相应数量、批次的SKU ，
	  * 需手工先对发货单进行拆分，确保部分子发货单库存审核通过，
	  * 或取消发货单（由实际业务而定）。
	  * @param stockVo skuId findNum batchNo
	  * @return Boolean false-无对应数量批次的库存 true-有库存
	  * @throws Exception
	  * @anthor 王通
	  * @date 2017年2月13日 下午4:51:44
	 */
    @Override
    @Transactional(readOnly=false)
	public void checkStock(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( stockVo == null || stockVo.getFindNum() <= 0) {
			throw new BizException("err_stock_param_null");
		}
		String skuId = stockVo.getInvStock().getSkuId();
		String locationId = stockVo.getInvStock().getLocationId();
		MetaLocation locationEntity = null;
		MetaSku skuEntity = null;
		if (!StringUtil.isTrimEmpty(locationId)) {
			locationEntity = locationService.findLocById(locationId);
			if (locationEntity == null) {
				throw new BizException("err_main_location_null");
			}
		}
		if (!StringUtil.isTrimEmpty(skuId)) {
			skuEntity = skuService.get(skuId);
			if (skuEntity == null) {
				throw new BizException("err_meta_sku_null");
			}
		}
		List<InvStock> findStockList = this.list(stockVo);
		Double totalQty = 0.0;
		//循环结果列表，累加数量
		if (findStockList != null && !findStockList.isEmpty()) {
			for (InvStock stock : findStockList) {
				totalQty += stock.getStockQty() - stock.getPickQty();
			}
		}
		if (totalQty < stockVo.getFindNum()) {
			String msg = "";
			if (locationEntity != null) {
				msg += "库位：" + locationEntity.getLocationName() + "<br />";
			}
			if (skuEntity != null) {
				msg += "货品：" + skuEntity.getSkuName() + "<br />";
			}
			msg += "实际库存：" + totalQty + "<br />";
			msg += "需求库存：" + stockVo.getFindNum();
			throw new BizException("err_stock_size",msg);
		}
	}
    
    /**
	 * 查询货品库存是否足够
	  * @Description 
	  * 发货单生效时，系统自动判断当前发货仓库所有正常库位可分配库存（[可分配库存=库存表中发货仓库
	  * 该批次SKU库存数量 – 拣货分配数量]）是否有相应数量、相应批次的SKU。
	  * 若有，系统提示是否自动生成相应拣货单记录，由操作人员确认是否自动生成。
	  * 当选择系统自动生成拣货单记录时，系统在生成拣货单记录的同时自动分配拣货库位
	  * （系统初始拣货规则：排除收货区暂存库位库存及当前仓库被冻结的库位库位，
	  * 对正常库位库存按先进先出FIFO处理，分配拣货库位后的拣货单处于“打开”状态），
	  * 同时在拣货单明细中显示“分配数量”，且将此“分配数量”累加至拣货库位对应的库存记录“拣货分配数量”字段；
	  * 当选择不由系统自动生成拣货单记录时，后续可由人工创建拣货单。
	  * 当对发货单进行生效、库存不足时，系统提示没有相应数量、批次的SKU ，
	  * 需手工先对发货单进行拆分，确保部分子发货单库存审核通过，
	  * 或取消发货单（由实际业务而定）。
	  * @param stockVo skuId findNum batchNo
	  * @return Boolean false-无对应数量批次的库存 true-有库存
	  * @throws Exception
	  * @anthor 王通
	  * @date 2017年2月13日 下午4:51:44
	 */
    @Override
    @Transactional(readOnly=false)
    public boolean checkStockRet(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( stockVo == null || stockVo.getFindNum() <= 0) {
			throw new BizException("err_stock_param_null");
		}
		String skuId = stockVo.getInvStock().getSkuId();
		String locationId = stockVo.getInvStock().getLocationId();
		MetaLocation locationEntity = null;
		MetaSku skuEntity = null;
		if (!StringUtil.isTrimEmpty(locationId)) {
			locationEntity = locationService.findLocById(locationId);
			if (locationEntity == null) {
				throw new BizException("err_main_location_null");
			}
		}
		if (!StringUtil.isTrimEmpty(skuId)) {
			skuEntity = skuService.get(skuId);
			if (skuEntity == null) {
				throw new BizException("err_meta_sku_null");
			}
		}
		List<InvStock> findStockList = this.list(stockVo);
		Double totalQty = 0.0;
		//循环结果列表，累加数量
		if (findStockList != null && !findStockList.isEmpty()) {
			for (InvStock stock : findStockList) {
				totalQty += stock.getStockQty() - stock.getPickQty();
			}
		}
		if (totalQty < stockVo.getFindNum()) {
			return false;
		}
		return true;
	}
    
    @Transactional(readOnly=false)
    private double getStockQty(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		List<InvStock> findStockList = this.list(stockVo);
		Double totalQty = 0.0;
		//循环结果列表，累加数量
		if (findStockList != null && !findStockList.isEmpty()) {
			for (InvStock stock : findStockList) {
				totalQty += stock.getStockQty() - stock.getPickQty();
			}
		}
		return totalQty;
	}
    
	/**
	 * 查询sku库存列表(未冻结)
	 * @Description 
	 * 1.          手动点击“自动分配”时先删除拣货单明细，系统根据发货单明细触发自动分配。
2.          拣货单自动分配库位后，拣货单明细中相应货品的“分配数量”即产生数值（分配数量=已分配拣货库位的SKU对应的数量）。
3.          系统初始拣货规则：排除收货区暂存库位库存，对正常库位库存按先进先出FIFO处理。
4.          系统按照拣货策略分配拣货库位，默认不分配暂存区库位库存，人工可指定从暂存区库位拣货，但保存时，系统需做提醒，告知有暂存区某库位参与拣选。
5.          拣货规则，在v2.0或后续版本中，将在规则管理模块与其他规则一起，集中进行管理、配置。
6.    拣货单状态：打开、生效、作业中（打印拣货单触发）、作业完成（作业确认时触发，作业确认时，作业人员可以根据实际拣货情况进行拣货库位、拣货数量信息的更新，包括删增拣货单明细信息）。
	 * @version 2017年2月14日 下午1:40:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public Page<InvStockVO> listByPage(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	Page<InvStockVO> page = null;
    	if (stockVo == null) {
			throw new BizException("err_stock_param_null");
		}
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	if (stockVo.getInvStock() == null) {
    		stockVo.setInvStock(new InvStock());
    	}
    	stockVo.getInvStock().setOrgId(orgId);
    	stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
    	// 如果填了批次作为条件，则设含批次为true
    	if (!StringUtil.isTrimEmpty(stockVo.getInvStock().getBatchNo())) {
    		stockVo.setContainBatch(true);
    	}
    	// 包装查询条件 货主\库位\货品
    	stockVo.setOwnerName(StringUtil.likeEscapeH(stockVo.getOwnerName()));
    	stockVo.setLocationName(StringUtil.likeEscapeH(stockVo.getLocationName()));
    	stockVo.setSkuNo(StringUtil.likeEscapeH(stockVo.getSkuNo()));
    	// 设置退货库位
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_TEMPSTORAGE);
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_STORAGE);
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_PICKUP);
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_TWICEPICKUP);
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_BACKUP);
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_SEND);
    	stockVo.addLocationTypeList(Constant.LOCATION_TYPE_BAD);
		// 查询库存单列表 
		page = PageHelper.startPage(stockVo.getCurrentPage()+1, stockVo.getPageSize());
		List<InvStock> stockList = this.stockDao.findStockList(stockVo);
		List<InvStockVO> stockVoList = chg(stockList);
		page.clear();
		page.addAll(stockVoList);
		return page;
	}
    /**
	 * 查询sku库存列表(未冻结)
	 * @Description 
	 * 1.          手动点击“自动分配”时先删除拣货单明细，系统根据发货单明细触发自动分配。
2.          拣货单自动分配库位后，拣货单明细中相应货品的“分配数量”即产生数值（分配数量=已分配拣货库位的SKU对应的数量）。
3.          系统初始拣货规则：排除收货区暂存库位库存，对正常库位库存按先进先出FIFO处理。
4.          系统按照拣货策略分配拣货库位，默认不分配暂存区库位库存，人工可指定从暂存区库位拣货，但保存时，系统需做提醒，告知有暂存区某库位参与拣选。
5.          拣货规则，在v2.0或后续版本中，将在规则管理模块与其他规则一起，集中进行管理、配置。
6.    拣货单状态：打开、生效、作业中（打印拣货单触发）、作业完成（作业确认时触发，作业确认时，作业人员可以根据实际拣货情况进行拣货库位、拣货数量信息的更新，包括删增拣货单明细信息）。
	 * @version 2017年2月14日 下午1:40:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public Page<InvStockVO> listByPageNoLogin(InvStockVO stockVo) throws Exception {
    	Page<InvStockVO> page = null;
    	if (stockVo == null) {
			throw new BizException("err_stock_param_null");
		}
    	// 设置创建人/修改人/企业/仓库
    	if (stockVo.getInvStock() == null) {
    		stockVo.setInvStock(new InvStock());
    	}
    	// 如果填了批次作为条件，则设含批次为true
    	if (!StringUtil.isTrimEmpty(stockVo.getInvStock().getBatchNo())) {
    		stockVo.setContainBatch(true);
    	}
    	// 包装查询条件 货主\库位\货品
    	stockVo.setOwnerName(StringUtil.likeEscapeH(stockVo.getOwnerName()));
    	stockVo.setLocationName(StringUtil.likeEscapeH(stockVo.getLocationName()));
    	stockVo.setSkuNo(StringUtil.likeEscapeH(stockVo.getSkuNo()));
		// 查询库存单列表 
		page = PageHelper.startPage(stockVo.getCurrentPage()+1, stockVo.getPageSize());
		List<InvStock> stockList = this.stockDao.findStockList(stockVo);
		List<InvStockVO> stockVoList = chg(stockList);
		page.clear();
		page.addAll(stockVoList);
		return page;
	}
    
    /**
	 * 查询sku库存列表(未冻结)
	 * @Description 
	 * 1.          手动点击“自动分配”时先删除拣货单明细，系统根据发货单明细触发自动分配。
2.          拣货单自动分配库位后，拣货单明细中相应货品的“分配数量”即产生数值（分配数量=已分配拣货库位的SKU对应的数量）。
3.          系统初始拣货规则：排除收货区暂存库位库存，对正常库位库存按先进先出FIFO处理。
4.          系统按照拣货策略分配拣货库位，默认不分配暂存区库位库存，人工可指定从暂存区库位拣货，但保存时，系统需做提醒，告知有暂存区某库位参与拣选。
5.          拣货规则，在v2.0或后续版本中，将在规则管理模块与其他规则一起，集中进行管理、配置。
6.    拣货单状态：打开、生效、作业中（打印拣货单触发）、作业完成（作业确认时触发，作业确认时，作业人员可以根据实际拣货情况进行拣货库位、拣货数量信息的更新，包括删增拣货单明细信息）。
	 * @version 2017年2月14日 下午1:40:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public List<InvStock> list(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if (stockVo == null) {
			throw new BizException("err_stock_param_null");
		}
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	stockVo.getInvStock().setOrgId(orgId);
    	stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());

    	// 如果填了批次作为条件，则设含批次为true
    	if (!StringUtil.isTrimEmpty(stockVo.getInvStock().getBatchNo())) {
    		stockVo.setContainBatch(true);
    	}
		// 查询库存单列表 
		List<InvStock> stockList = this.stockDao.findStockList(stockVo);
		return stockList;
	}
	
	/**
	 * 锁定并累加库存出库预分配数量
	 * @param stockVo
	 * @throws Exception
	 * @required invStock.skuId,invStock.localtionId,findNum
	 * @optional invStock.packId,invStock.batchNo
	 * @Description 
	 * 1、校验库存是否冻结（不校验库位是否冻结）
	 * 2、校验库存数量并直接更新
	 * @version 2017年2月28日 下午2:39:40<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public void lockOutStock(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//字段校验
		InvStock stock = stockVo.getInvStock();
		Double findNum = stockVo.getFindNum();
		String skuId = stock.getSkuId();
		String locationId = stock.getLocationId();
		String batchNo = stock.getBatchNo();
		String asnDetailId = stock.getAsnDetailId();
		if (findNum == null || findNum <= 0 || StringUtil.isTrimEmpty(skuId) || StringUtil.isTrimEmpty(locationId)) {
			//必填项未填时报错
			throw new BizException("err_stock_param_null");
		}

    	// 如果填了批次作为条件，则设含批次为true
    	if (!StringUtil.isTrimEmpty(batchNo)) {
    		stockVo.setContainBatch(true);
    	}
		// 获取当前用户信息
		stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
		stockVo.getInvStock().setOrgId(loginUser.getOrgId());
		//校验库存是否冻结
		this.checkStockFreeze(stockVo);
		//校验库存数量并直接更新
//		int ret = this.stockDao.lockOutStock(stockVo);
//		if (ret < 1) {
//			//更新无结果时报错
//			throw new BizException("err_stock_update_size");
//		}
		InvStock retStock = view(skuId, locationId, batchNo, asnDetailId);
		if (retStock == null) {
			throw new BizException("err_stock_param_null");
		} else {
			Double stockQty = retStock.getStockQty();
			Double pickQty = retStock.getPickQty();
			if(stockQty - pickQty - findNum < 0) {
				throw new BizException("err_stock_update_size");
			}
			InvStock newStock = new InvStock();
			newStock.setStockId(retStock.getStockId());
			newStock.setPickQty(pickQty + findNum);
			this.stockDao.updateByPrimaryKeySelective(newStock);
		}
	}

	/**
	 * 解锁预分配库存
	 * @param stockVo
	 * @required invStock.skuId,invStock.localtionId,findNum
	 * @optional invStock.packId,invStock.batchNo
	 * @Description 
	 * @version 2017年2月24日 下午5:59:35<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public void unlockOutStock(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//字段校验
		InvStock stock = stockVo.getInvStock();
		Double findNum = stockVo.getFindNum();
		String skuId = stock.getSkuId();
		String locationId = stock.getLocationId();
		String batchNo = stock.getBatchNo();
		String asnDetailId = stock.getAsnDetailId();
		if (findNum == null || findNum <= 0 || StringUtil.isTrimEmpty(skuId) || StringUtil.isTrimEmpty(locationId)) {
			//必填项未填时报错
			throw new BizException("err_stock_param_null");
		}
    	// 如果填了批次作为条件，则设含批次为true
    	if (!StringUtil.isTrimEmpty(batchNo)) {
    		stockVo.setContainBatch(true);
    	}
		stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
		stockVo.getInvStock().setOrgId(loginUser.getOrgId());
		//校验库存数量并直接更新
//		int ret = this.stockDao.unlockOutStock(stockVo);
//		if (ret != 1) {
//			//更新无结果时报错
//			throw new BizException("err_stock_update_locked_size");
//		}
		InvStock retStock = view(skuId, locationId, batchNo, asnDetailId);
		if (retStock == null) {
			throw new BizException("err_stock_param_null");
		} else {
			Double pickQty = retStock.getPickQty();
			if(pickQty - findNum < 0) {
				throw new BizException("err_stock_update_locked_size");
			}
			InvStock newStock = new InvStock();
			newStock.setStockId(retStock.getStockId());
			newStock.setPickQty(pickQty - findNum);
			this.stockDao.updateByPrimaryKeySelective(newStock);
		}
	}
	/**
	 * 库存入库
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required invStock.skuId,invStock.locationId,invStock.findNum,
	 * invLog.opPerson,invLog.opType,invLog.billType,invLog.invoiceBill,
	 * invLog.logType,invLog.localtionId,invLog.skuId
	 * @optional invStock.packId,invStock.batchNo,invStock.asnDetailId,
	 *  invStock.proDate,invLog.note,invLog.batchNo
	 * @Description 
	 * 1、新增库位使用库容(同时不释放预分配库容,判断库容是否足够,判断库位是否冻结)	
	 * 2、判断是否有相同库存，是则更新库存数量
	 * 3、否则直接新增库存
	 * 4、保存库存日志
	 * 5、自动创建一条调账单及明细
	 * @version 2017年2月28日 下午3:16:03<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public InvStock inStock(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//字段校验
		if (stockVo == null || stockVo.getInvStock()==null) {
			throw new BizException("err_stock_param_null");
		}
		InvStock stock = stockVo.getInvStock();
		// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	stock.setUpdatePerson(loginUser.getUserId());
    	stock.setUpdateTime(new Date());
    	stock.setOrgId(orgId);
    	stock.setWarehouseId(LoginUtil.getWareHouseId());
		String skuId = stock.getSkuId();
		if (StringUtil.isTrimEmpty(skuId)) {
			throw new BizException("err_param_sku_null");
		}
		String locationId = stock.getLocationId();
		if (StringUtil.isTrimEmpty(locationId)) {
			throw new BizException("err_param_location_null");
		}
		//判断库位是否在暂存区，如果在暂存区且是导入/新增模块调用，禁止导入
		MetaLocation location = locationService.findLocById(locationId);
		if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
			if (StringUtil.isTrimEmpty(stock.getAsnDetailId())) {
				throw new BizException("err_stock_param_location_import");
			}
		}
		//判断库位是否在不良品区，如果在不良品区，则货品状态标记为不合格
		if (location.getLocationType() == Constant.LOCATION_TYPE_BAD) {
			stock.setSkuStatus(Constant.STOCK_SKU_STATUS_ABNORMAL);
			stockVo.getInvLog().setSkuStatus(Constant.STOCK_SKU_STATUS_ABNORMAL);
		} else {
			stock.setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
			stockVo.getInvLog().setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
		}
		Double qty = stockVo.getFindNum();
		if (qty == null || qty <= 0) {
			throw new BizException("err_param_qty");
		}
		String packId = stock.getPackId();
		String batchNo = stock.getBatchNo();
		String asnDetailId = stock.getAsnDetailId();
		//不释放库存预占用库容，判断库容是否足够，同时增加库容，同时判断库位是否冻结
		this.locationService.addCapacity(skuId, locationId, packId, qty, false);
		MetaSku sku = skuService.get(skuId);
		if (sku == null) {
			throw new BizException("err_stock_sku_null");
		}
		String owner = sku.getOwner();
		Date now = new Date();
		//批次号为各种空值时，默认为空串
		if (StringUtil.isTrimEmpty(batchNo)) {
			batchNo = "";
			stock.setBatchNo(batchNo);
		}
		//判断是否有相同库存，是则更新库存数量
		InvStock inStock = this.view(skuId, locationId, batchNo, asnDetailId);
		if (inStock != null) {
			inStock.setStockQty(inStock.getStockQty() + qty);
			inStock.setStockVolume(NumberUtil.add(inStock.getStockVolume() == null ? 0 : inStock.getStockVolume(), NumberUtil.mul(qty, sku.getPerVolume() == null ? 0 : sku.getPerVolume())));
			inStock.setStockWeight(NumberUtil.add(inStock.getStockWeight() == null ? 0 : inStock.getStockWeight(), NumberUtil.mul(qty, sku.getPerWeight() == null ? 0 : sku.getPerWeight())));
			if (stock.getInDate() != null) {
				inStock.setInDate(stock.getInDate());
			}
			inStock.setUpdateTime(now);
			inStock.setUpdatePerson(loginUser.getUserId());
			this.stockDao.updateByPrimaryKeySelective(inStock);
			//保存库存日志
			this.insertLog(stockVo.getInvLog(), loginUser);
			return inStock;
		} else {
			//无相同库存，新增库存
			//新增时设置ID
			stock.setStockId(IdUtil.getUUID());
			stock.setStockQty(qty);
			stock.setOwner(owner);
			stock.setStockVolume(NumberUtil.mul(qty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
			stock.setStockWeight(NumberUtil.mul(qty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    		//设置入库时间
			if (stock.getInDate() == null) {
				stock.setInDate(now);
			}
	    	stock.setCreatePerson(loginUser.getUserId());
	    	stock.setCreateTime(now);
	    	stock.setUpdatePerson(loginUser.getUserId());
	    	stock.setUpdateTime(now);
			Integer life = sku.getShelflife();
			Date proDate = stock.getProduceDate();
			//设置过期日期
			if (proDate != null) {
				if (life != null && life != 0) {
					stock.setExpiredDate(DateUtil.passDate(proDate, null, life));
				}
			} else if (StringUtil.isNoneBlank(batchNo)) {
				try {
					proDate = new SimpleDateFormat("yyyyMMdd").parse(batchNo);
				}catch (ParseException e) {
					throw new BizException("err_stock_batch_parse");
				}
				stock.setProduceDate(proDate);
				if (life != null && life != 0) {
					stock.setExpiredDate(DateUtil.passDate(proDate, null, life));
				}
			}
			//设置默认库存货品状态
			if (stock.getSkuStatus() == null) {
				stock.setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
				stockVo.getInvLog().setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
			} else {
				stockVo.getInvLog().setSkuStatus(stock.getSkuStatus());
			}
			stock.setStockId2(context.getStrategy4Id().getStockSeq());
			this.stockDao.insertSelective(stock);
			//保存库存日志
			this.insertLog(stockVo.getInvLog(), loginUser);
			return stock;
		}
	}

	/**
	 * 库存出库
	 * @param stockVo
	 * @throws Exception
	 * @required invStock.skuId,invStock.locationId,invStock.findNum,
	 * invLog.opPerson,invLog.opType,invLog.invoiceBill,
	 * invLog.logType,invLog.localtionId,invLog.skuId,invLog.qty
	 * @optional invStock.packId,invStock.batchNo,invStock.asnDetailId,
	 *  invLog.note,invLog.batchNo
	 * @Description 
	 * 1、不释放预分配库存
	 * 2、减少库位使用库容(判断库容是否足够,判断库位是否冻结)	
	 * 3、判断是否有库存，数量小于库存总数则更新库存数量
	 * 4、数量等于库存总数则删除库存
	 * 5、大于则报错
	 * 6、保存库存日志
	 * 7、新增调账单
	 * @version 2017年2月24日 下午5:59:35<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly=false)
	public void outStock(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//字段校验
		if (stockVo == null || stockVo.getInvStock()==null) {
			throw new BizException("err_stock_param_null");
		}
		//校验库存字段
		InvStock stock = stockVo.getInvStock();
		// 设置创建人/修改人/企业/仓库
//    	String orgId = loginUser.getOrgId();
//    	stock.setCreatePerson(loginUser.getUserId());
//    	stock.setCreateTime(new Date());
//    	stock.setUpdatePerson(loginUser.getUserId());
//    	stock.setUpdateTime(new Date());
//    	stock.setOrgId(orgId);
//    	stock.setWarehouseId(LoginUtil.getWareHouseId());
		String skuId = stock.getSkuId();
		String locationId = stock.getLocationId();
		String packId = stock.getPackId();
		String batchNo = stock.getBatchNo();
		//批次号为各种空值时，默认为空串
		if (StringUtil.isTrimEmpty(batchNo)) {
			batchNo = "";
			stock.setBatchNo(batchNo);
		}
		String asnDetailId = stock.getAsnDetailId();
		Double qty = stockVo.getFindNum();
		if (StringUtil.isTrimEmpty(skuId) || StringUtil.isTrimEmpty(locationId) || qty == null || qty <= 0) {
			throw new BizException("err_stock_param_null");
		}
//		//释放预分配库存
//		this.unlockOutStock(stockVo);
		MetaLocation locationEntity = locationService.findLocById(locationId);
		if (locationEntity == null) {
			throw new BizException("err_main_location_null");
		}
		MetaSku skuEntity = skuService.get(skuId);
		if (skuEntity == null) {
			throw new BizException("err_meta_sku_null");
		}
		//判断是否有库存
		InvStock inStock = this.view(skuId, locationId, batchNo, asnDetailId);
		if (inStock != null) {
			if (inStock.getIsBlock() != null && inStock.getIsBlock() == Constant.STOCK_BLOCK_TRUE) {
				throw new BizException("err_stock_block", "，不能移出");
			}
			double stockQty = inStock.getStockQty();
			double pickQty = inStock.getPickQty();
			//如果调整数量小于预分配数量，不允许调整
			if (stockQty - qty < pickQty) {
				throw new BizException("err_stock_change_pick");
			}
			if (stockQty > qty) {
				//减少库位使用库容(判断库容是否足够,判断库位是否冻结)	
				this.locationService.minusCapacity(skuId, locationId, packId, qty);
				//数量小于库存总数则更新库存数量
				inStock.setStockQty(inStock.getStockQty() - qty);
				if (inStock.getStockVolume() != null) {
					Double retVolume = NumberUtil.sub(inStock.getStockVolume(), NumberUtil.mul(qty, skuEntity.getPerVolume() == null ? 0 : skuEntity.getPerVolume()));
					retVolume = retVolume < 0 ? 0 : retVolume;
					inStock.setStockVolume(retVolume);
				} else {
					inStock.setStockVolume(0.0);
				}
				if (inStock.getStockWeight() != null) {
					Double retWeight = NumberUtil.sub(inStock.getStockWeight(), NumberUtil.mul(qty, skuEntity.getPerWeight() == null ? 0 : skuEntity.getPerWeight()));
					retWeight = retWeight < 0 ? 0 : retWeight;
					inStock.setStockWeight(retWeight);
				} else {
					inStock.setStockWeight(0.0);
				}
	        	stock.setUpdatePerson(loginUser.getUserId());
	        	stock.setUpdateTime(new Date());
				this.stockDao.updateByPrimaryKeySelective(inStock);
				//保存库存日志
				this.insertLog(stockVo.getInvLog(), loginUser);
			} else if (stockQty == qty) {
				//减少库位使用库容(判断库容是否足够,判断库位是否冻结)	
				this.locationService.minusCapacity(skuId, locationId, packId, qty);
				//数量等于库存总数则删除库存
				this.stockDao.delete(inStock);
				//保存库存日志
				this.insertLog(stockVo.getInvLog(), loginUser);
			} else {
				//大于则报错
				String msg = "库位：" + locationEntity.getLocationName() + ",货品：" + skuEntity.getSkuName() + ",库存数量：" + stockQty + ",需求数量：" + qty;
				throw new BizException("err_stock_size", msg);
			}
		} else {
			//无库存则报错
			String msg = "库位：" + locationEntity.getLocationName() + ",货品：" + skuEntity.getSkuName() + ",库存数量：" + 0 + ",需求数量：" + qty;
			throw new BizException("err_stock_size", msg);
		}
	}
	
	/**
	 * 内部方法调用
	 * @param invLog
	 * @param loginUser
	 * @required opPerson,opType,logType,invoiceBill,locationId,skuId,qty
	 * @optional  
	 * @Description 
	 * @version 2017年2月28日 上午10:19:11<br/>
	 * @author 王通<br/>
	 * @param loginUser 
	 */
	public void insertLog(InvLog invLog, Principal loginUser) {
		if (invLog == null) {
			throw new BizException("err_stock_log_param_err");
		}
		String opPerson = invLog.getOpPerson();
		Integer opType = invLog.getOpType();
		Integer logType = invLog.getLogType();
		String invoiceBill = invLog.getInvoiceBill();
		String locationId = invLog.getLocationId();
		String skuId = invLog.getSkuId();
		Double qty = invLog.getQty();
		if (StringUtil.isTrimEmpty(opPerson) || StringUtil.isTrimEmpty(invoiceBill)
				|| StringUtil.isTrimEmpty(locationId) ||  StringUtil.isTrimEmpty(skuId)
				|| opType == null || opType < 0 ||  logType == null || logType < 0
				|| qty == null) {
			throw new BizException("err_stock_log_param_err");
		}
		Double volumn = invLog.getVolume();
		Double weight = invLog.getWeight();
		MetaSku sku = skuService.get(skuId);
		if (volumn == null) {
			volumn = NumberUtil.mul(qty, sku.getPerVolume() == null ? 0 : sku.getPerVolume());
			invLog.setVolume(volumn);
		}
		if (weight == null) {
			weight = NumberUtil.mul(qty, sku.getPerWeight() == null ? 0 : sku.getPerWeight());
			invLog.setWeight(weight);
		}
		//出库时，统一设为负数
		if (opType == Constant.STOCK_LOG_OP_TYPE_OUT) {
			invLog.setQty(qty > 0 ? -qty : qty);
			invLog.setVolume(volumn > 0 ? -volumn : volumn);
			invLog.setWeight(weight > 0 ? -weight : weight);
		}
		invLog.setLogId(IdUtil.getUUID());
		invLog.setCreatePerson(loginUser.getUserId());
		invLog.setCreateTime(new Date());
		invLog.setUpdatePerson(loginUser.getUserId());
		invLog.setUpdateTime(new Date());
		invLog.setOrgId(loginUser.getOrgId());
		invLog.setWarehouseId(LoginUtil.getWareHouseId());
		invLog.setLogId2(context.getStrategy4Id().getInvLogSeq());
		this.stockLogDao.insertSelective(invLog);
	}

	/**
	 * 快速移位
	 * @param vo
	 * @required outStockVo.findNum, outStockVo.invStock.stockId,
	 * outStockVo.invLog.opPerson,inStockVo.invStock.locationId
	 * @optional
	 * @Description 
	 * 1、获取库存信息
	 * 2、比较库存数量
	 * 3、出库存（包括生成日志）
	 * 4、入库存（包括生成日志）
	 * 5、生成移位单
	 * @version 2017年2月24日 下午5:59:35<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public void shift(InvStockVO outStockVo) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	//字段校验
    	if (outStockVo == null || outStockVo.getInvStock() == null || 
    			outStockVo.getInvLog() == null) {
    		throw new BizException("err_stock_param_null");
    	}
    	String outStockId = outStockVo.getInvStock().getStockId();
    	Double num = outStockVo.getFindNum();
    	String inLocation = outStockVo.getInLocationId();
//    	String inLocationName = outStockVo.getInLocationName();
//    	MetaLocationVO locationVo = new MetaLocationVO();
//		MetaLocation metaLocation = new MetaLocation();
//		metaLocation.setLocationName(inLocationName);
//		locationVo.setLocation(metaLocation);
//		MetaLocation locationRet = this.locationService.findLoc(locationVo);
//		if (locationRet != null) {
//			inLocation = locationRet.getLocationId();
//		} else {
//			throw new BizException("err_stock_imp_location", inLocationName);
//		}
    	String opPerson = outStockVo.getInvLog().getOpPerson();
    	if (StringUtil.isTrimEmpty(outStockId) || StringUtil.isTrimEmpty(inLocation) || StringUtil.isTrimEmpty(opPerson) || num == null || num <= 0) {
    		throw new BizException("err_stock_param_null");
    	}
    	String orgId = loginUser.getOrgId();
    	//获取移位单编号
//    	String shiftNo = this.commonService.getNo(new CommonVo(orgId, BillPrefix.DOCUMENT_PREFIX_MOVE_INTERNAL));
    	String shiftNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftInternalNo(orgId);
    	//调整出库内容
    	InvStock stock = this.stockDao.selectByPrimaryKey(outStockId);
    	if (stock == null) {
    		throw new BizException("err_stock_expire");
    	}
    	if (StringUtil.equals(inLocation, stock.getLocationId())) {
    		throw new BizException("err_shift_location_same");
    	}
    	outStockVo.setInvStock(stock);
    	//提取字段
    	String outLocation = stock.getLocationId();
    	//判断库位是否在暂存区，如果在暂存区，禁止移出
		MetaLocation location = locationService.findLocById(outLocation);
		if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
			throw new BizException("err_stock_param_location_shift");
		}
    	String skuId = stock.getSkuId();
    	String batchNo = stock.getBatchNo();
    	Date inDate = stock.getInDate();
		//预设日志对象
		InvLog invLog = new InvLog();
		outStockVo.setInvLog(invLog);
		invLog.setOpPerson(opPerson);
		invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
		invLog.setInvoiceBill(shiftNo);
		invLog.setSkuId(stock.getSkuId());
		MetaSku sku = skuService.get(skuId);
		
//		Integer stockQty = stock.getStockQty();
//		//设置体积和重量
//		Double stockVolume = stock.getStockVolume();
//		Double changeVolume = 0.0;
//		if (stockVolume != null && stockVolume != 0.0) {
//			invLog.setVolume(changeVolume);
//			changeVolume =  num / (stockQty * stockVolume);
//			stock.setStockVolume(stockVolume - changeVolume);
//		}
//		Double stockWeight = stock.getStockVolume();
//		Double changeWeight = 0.0;
//		if (stockWeight != null && stockWeight != 0.0) {
//			invLog.setWeight(changeWeight);
//			changeWeight = num / (stockQty * stockWeight);
//			stock.setStockWeight(stockWeight - changeWeight);
//		}
    	//调整出库内容
    	invLog.setQty(-num);
    	invLog.setVolume(NumberUtil.mul(-num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
		invLog.setWeight(NumberUtil.mul(-num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
		
		invLog.setLocationId(stock.getLocationId());
		invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
		//出库
    	this.outStock(outStockVo);
    	//调整入库内容
		stock.setLocationId(inLocation);
    	invLog.setQty(num);
    	invLog.setVolume(NumberUtil.mul(num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
		invLog.setWeight(NumberUtil.mul(num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
		
		invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
		invLog.setLocationId(inLocation);
    	//入库
		this.inStock(outStockVo);
		//新增移位单
		InvShiftVO shiftVo = new InvShiftVO();
		InvShift shift = new InvShift();
		shiftVo.setInvShift(shift);
		String shiftId = IdUtil.getUUID();
		shift.setShiftId(shiftId);
		shift.setShiftNo(shiftNo);
		shift.setShiftStatus(Constant.SHIFT_STATUS_FINISH);
		shift.setCreatePerson(loginUser.getUserId());
		shift.setCreateTime(new Date());
		shift.setUpdatePerson(loginUser.getUserId());
		shift.setUpdateTime(new Date());
		shift.setOrgId(orgId);
		shift.setWarehouseId(LoginUtil.getWareHouseId());
		shift.setOpPerson(opPerson);
		shift.setShiftType(Constant.SHIFT_TYPE_INWAREHOUSE);
		shift.setNote("自动生成单据---快速移位");
		List<InvShiftDetailVO> detailVoList = new ArrayList<InvShiftDetailVO>();	
		InvShiftDetailVO detailVo = new InvShiftDetailVO();
		detailVoList.add(detailVo);
		InvShiftDetail detail = new InvShiftDetail();
		detailVo.setInvShiftDetail(detail);
		//移位单明细字段
		detail.setShiftDetailId(IdUtil.getUUID());
		detail.setBatchNo(stock.getBatchNo());
		detail.setCreatePerson(loginUser.getUserId());
		detail.setCreateTime(new Date());
		detail.setUpdatePerson(loginUser.getUserId());
		detail.setUpdateTime(new Date());
		detail.setOrgId(orgId);
		detail.setWarehouseId(LoginUtil.getWareHouseId());
		detail.setInLocation(inLocation);
		detail.setOutLocation(outLocation);
		detail.setRealShiftQty(num);
		detail.setShiftQty(num);
		detail.setShiftId(shiftId);
		detail.setSkuId(skuId);
		detail.setBatchNo(batchNo);
		detail.setInDate(inDate);
		shiftVo.setListInvShiftDetailVO(detailVoList);
		this.shiftService.quickSave(shiftVo);

		//发送辅助系统
		//出库
		MetaLocation out = FqDataUtils.getLocById(locationService, outLocation);
		MessageData data1 = new MessageData(out.getLocationNo(), out.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(num, 0), sku.getMeasureUnit(), "E");
//		context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, true, data1);
		//入库
		MetaLocation in = FqDataUtils.getLocById(locationService, inLocation);
		MessageData data2 = new MessageData(in.getLocationNo(), in.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(num, 0), sku.getMeasureUnit(), "I");
//		context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_SHIFT, shiftId, false, data2);
    }
    
    /**
	 * 移位至发货区
	 * @param vo
	 * @required outStockVo.findNum, outStockVo.invStock.locationId,outStockVo.invStock.skuId,outStockVo.invStock.batchNo,
	 * outStockVo.invLog.opPerson,inStockVo.invStock.locationId
	 * @optional
	 * @Description 
	 * 1、获取库存信息
	 * 2、比较库存数量
	 * 3、出库存（包括生成日志）
	 * 4、入库存（包括生成日志）
	 * 5、生成移位单，直接完成状态
	 * @version 2017年8月24日11:35:52<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
    @Override
    @Transactional(readOnly=false)
	public void shiftToSend(String deliveryId) throws Exception {
    	Date now = new Date();
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	//字段校验
    	if (deliveryId == null) {
    		throw new BizException("err_stock_param_null");
    	}
    	//先查出入库库位（发货区第一个库位）
    	MetaLocationVO mateLocationVo = new MetaLocationVO();
		MetaLocation metaLocation = new MetaLocation();
		metaLocation.setLocationType(Constant.LOCATION_TYPE_SEND);
		mateLocationVo.setLocation(metaLocation);
		MetaLocation locationRet = this.locationService.findLoc(mateLocationVo);
    	String inLocation = "";
		if (locationRet != null) {
			inLocation = locationRet.getLocationId();
		} else {
			throw new BizException("err_shift_send_location_null");
		}
		//新增移位单
    	String opPerson = loginUser.getUserId();
    	String orgId = loginUser.getOrgId();
    	//获取移位单编号
    	String shiftNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftPickNo(orgId);
		InvShiftVO shiftVo = new InvShiftVO();
		InvShift shift = new InvShift();
		shiftVo.setInvShift(shift);
		String shiftId = IdUtil.getUUID();
		shift.setShiftId(shiftId);
		shift.setShiftNo(shiftNo);
		shift.setShiftStatus(Constant.SHIFT_STATUS_FINISH);
		shift.setCreatePerson(loginUser.getUserId());
		shift.setCreateTime(now);
		shift.setUpdatePerson(loginUser.getUserId());
		shift.setUpdateTime(now);
		shift.setOrgId(orgId);
		shift.setWarehouseId(LoginUtil.getWareHouseId());
		shift.setOpPerson(opPerson);
		shift.setShiftType(Constant.SHIFT_TYPE_PICK);
		shift.setNote("自动生成单据---拣货移位");
		List<InvShiftDetailVO> detailVoList = new ArrayList<InvShiftDetailVO>();	
		
    	SendPickDetailVo sendVoParam = new SendPickDetailVo(); 
    	sendVoParam.getSendPickDetail().setDeliveryId(deliveryId);
//    	List<SendPickVo> listSendPick = pickService.qryListByParam(sendVoParam);
    	List<SendPickDetailVo> sendPickDetailVoList = pickDetailService.qryDetails(sendVoParam);
		//获取第一条拣货单的拣货明细
    	SendPickVo pick = null;
    	for (SendPickDetailVo sendPickDetailVo : sendPickDetailVoList) {
    		pick = pickService.view(sendPickDetailVo.getSendPickDetail().getPickId());
    		if (pick.getSendPick().getPickStatus().equals(20)) {
    			break;
    		}
    	}
    	String pickNo = pick.getSendPick().getPickNo();
    	shift.setInvolveBill(pickNo);
    	for (SendPickDetailVo sendDetailVo : pick.getSendPickDetailVoList()) {
    		//获取拣货明细的实际拣货库位
    		List<SendPickLocationVo> locationVoList = sendDetailVo.getRealPickLocations();
    		String skuId = sendDetailVo.getSendPickDetail().getSkuId();
    		for (SendPickLocationVo locationVo : locationVoList) {
    			SendPickLocation location = locationVo.getSendPickLocation();
    			Double num = location.getPickQty();
    			String outLocation = location.getLocationId();
    			String batchNo = location.getBatchNo();
    			
    			//拼凑移位单详情数据
    			InvShiftDetailVO detailVo = new InvShiftDetailVO();
    			detailVoList.add(detailVo);
    			InvShiftDetail detail = new InvShiftDetail();
    			detailVo.setInvShiftDetail(detail);
    			//移位单明细字段
    			detail.setShiftDetailId(IdUtil.getUUID());
    			detail.setBatchNo(batchNo);
    			detail.setCreatePerson(loginUser.getUserId());
    			detail.setCreateTime(now);
    			detail.setUpdatePerson(loginUser.getUserId());
    			detail.setUpdateTime(now);
    			detail.setOrgId(orgId);
    			detail.setWarehouseId(LoginUtil.getWareHouseId());
    			detail.setInLocation(inLocation);
    			detail.setOutLocation(outLocation);
    			detail.setRealShiftQty(num);
    			detail.setShiftQty(num);
    			detail.setShiftId(shiftId);
    			detail.setSkuId(skuId);
    			detail.setBatchNo(batchNo);
    			detail.setInDate(now);
    			shiftVo.setListInvShiftDetailVO(detailVoList);
    			
    			//预设库存对象
    			InvStockVO outStockVo = new InvStockVO();
    			InvStock stock = new InvStock();
    			outStockVo.setInvStock(stock);
    			stock.setSkuId(skuId);
    			stock.setLocationId(outLocation);
    			stock.setBatchNo(batchNo);
    			outStockVo.setFindNum(num);
    			//预设日志对象
    			InvLog invLog = new InvLog();
    			outStockVo.setInvLog(invLog);
    			invLog.setOpPerson(opPerson);
    			invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
    			invLog.setInvoiceBill(pickNo);
    			invLog.setSkuId(skuId);
    			MetaSku sku = skuService.get(skuId);
//    			Integer stockQty = stock.getStockQty();
//    			//设置体积和重量
//    			Double stockVolume = stock.getStockVolume();
//    			Double changeVolume = 0.0;
//    			if (stockVolume != null && stockVolume != 0.0) {
//    				invLog.setVolume(changeVolume);
//    				changeVolume =  num / (stockQty * stockVolume);
//    				stock.setStockVolume(stockVolume - changeVolume);
//    			}
//    			Double stockWeight = stock.getStockVolume();
//    			Double changeWeight = 0.0;
//    			if (stockWeight != null && stockWeight != 0.0) {
//    				invLog.setWeight(changeWeight);
//    				changeWeight = num / (stockQty * stockWeight);
//    				stock.setStockWeight(stockWeight - changeWeight);
//    			}
    	    	//调整出库内容
    	    	invLog.setQty(-num);
    	    	invLog.setVolume(NumberUtil.mul(-num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(-num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    			
    			invLog.setLocationId(outLocation);
    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
    			//出库
    	    	this.outStock(outStockVo);
    	    	//调整入库内容
    			stock.setLocationId(inLocation);
    	    	invLog.setQty(num);
    	    	invLog.setVolume(NumberUtil.mul(num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    			
    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
    			invLog.setLocationId(inLocation);
    	    	//入库
    			this.inStock(outStockVo);
    		}
    	}
		//保存移位单
		this.shiftService.quickSave(shiftVo);
	}

    /**
	 * 移位至发货区
	 * @param vo
	 * @required outStockVo.findNum, outStockVo.invStock.locationId,outStockVo.invStock.skuId,outStockVo.invStock.batchNo,
	 * outStockVo.invLog.opPerson,inStockVo.invStock.locationId
	 * @optional
	 * @Description 
	 * 1、获取库存信息
	 * 2、比较库存数量
	 * 3、出库存（包括生成日志）
	 * 4、入库存（包括生成日志）
	 * 5、生成移位单，直接完成状态
	 * @version 2017年8月24日11:35:52<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
//    @Override
//    @Transactional(readOnly=false)
//	public void shiftToSend(InvStockVO outStockVo) throws Exception {
//    	Date now = new Date();
//    	Principal loginUser = LoginUtil.getLoginUser();
//    	// 验证用户是否登录
//    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
//    		throw new BizException("valid_common_user_no_login");
//    	}
//    	//字段校验
//    	if (outStockVo == null || outStockVo.getInvStock() == null || 
//    			outStockVo.getInvLog() == null) {
//    		throw new BizException("err_stock_param_null");
//    	}
////    	String outStockId = outStockVo.getInvStock().getStockId();
//    	Integer num = outStockVo.getFindNum();
//    	String outLocation = outStockVo.getInvStock().getLocationId();
//    	String skuId = outStockVo.getInvStock().getSkuId();
//    	String batchNo = outStockVo.getInvStock().getBatchNo();
//    	String inLocation = "";
//    	MetaLocationVO locationVo = new MetaLocationVO();
//		MetaLocation metaLocation = new MetaLocation();
//		metaLocation.setLocationType(Constant.LOCATION_TYPE_SEND);
//		locationVo.setLocation(metaLocation);
//		MetaLocation locationRet = this.locationService.findLoc(locationVo);
//		if (locationRet != null) {
//			inLocation = locationRet.getLocationId();
//		}
//    	String opPerson = outStockVo.getInvLog().getOpPerson();
//    	if (StringUtil.isTrimEmpty(outLocation) || StringUtil.isTrimEmpty(inLocation) || StringUtil.isTrimEmpty(opPerson) || num == null || num <= 0) {
//    		throw new BizException("err_stock_param_null");
//    	}
//    	String orgId = loginUser.getOrgId();
//    	//获取移位单编号
////    	String shiftNo = this.commonService.getNo(new CommonVo(orgId, BillPrefix.DOCUMENT_PREFIX_MOVE_INTERNAL));
//    	String shiftNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftPickNo(orgId);
//    	//调整出库内容
//    	if (StringUtil.equals(inLocation, outLocation)) {
//    		throw new BizException("err_shift_location_same");
//    	}
//    	//提取字段
//    	//判断库位是否是拣货区，如果不是，禁止移位
//		MetaLocation location = locationService.findLocById(outLocation);
//		if (location.getLocationType() != Constant.LOCATION_TYPE_PICKUP && location.getLocationType() != Constant.LOCATION_TYPE_STORAGE) {
//			throw new BizException("err_stock_param_location_pick");
//		}
////    	Date inDate = stock.getInDate();
//		//预设日志对象
//		InvLog invLog = new InvLog();
//		outStockVo.setInvLog(invLog);
//		invLog.setOpPerson(opPerson);
//		invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
//		invLog.setInvoiceBill(shiftNo);
//		invLog.setSkuId(skuId);
//		MetaSku sku = skuService.get(skuId);
//		InvStock stock = outStockVo.getInvStock();
////		Integer stockQty = stock.getStockQty();
////		//设置体积和重量
////		Double stockVolume = stock.getStockVolume();
////		Double changeVolume = 0.0;
////		if (stockVolume != null && stockVolume != 0.0) {
////			invLog.setVolume(changeVolume);
////			changeVolume =  num / (stockQty * stockVolume);
////			stock.setStockVolume(stockVolume - changeVolume);
////		}
////		Double stockWeight = stock.getStockVolume();
////		Double changeWeight = 0.0;
////		if (stockWeight != null && stockWeight != 0.0) {
////			invLog.setWeight(changeWeight);
////			changeWeight = num / (stockQty * stockWeight);
////			stock.setStockWeight(stockWeight - changeWeight);
////		}
//    	//调整出库内容
//    	invLog.setQty(-num);
//    	invLog.setVolume(NumberUtil.mul(-num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
//		invLog.setWeight(NumberUtil.mul(-num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
//		
//		invLog.setLocationId(outLocation);
//		invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
//		//出库
//    	this.outStock(outStockVo);
//    	//调整入库内容
//		stock.setLocationId(inLocation);
//    	invLog.setQty(num);
//    	invLog.setVolume(NumberUtil.mul(num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
//		invLog.setWeight(NumberUtil.mul(num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
//		
//		invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
//		invLog.setLocationId(inLocation);
//    	//入库
//		this.inStock(outStockVo);
//		//新增移位单
//		InvShiftVO shiftVo = new InvShiftVO();
//		InvShift shift = new InvShift();
//		shiftVo.setInvShift(shift);
//		String shiftId = IdUtil.getUUID();
//		shift.setShiftId(shiftId);
//		shift.setShiftNo(shiftNo);
//		shift.setShiftStatus(Constant.SHIFT_STATUS_FINISH);
//		shift.setCreatePerson(loginUser.getUserId());
//		shift.setCreateTime(now);
//		shift.setUpdatePerson(loginUser.getUserId());
//		shift.setUpdateTime(now);
//		shift.setOrgId(orgId);
//		shift.setWarehouseId(LoginUtil.getWareHouseId());
//		shift.setOpPerson(opPerson);
//		shift.setShiftType(Constant.SHIFT_TYPE_PICK);
//		shift.setNote("自动生成单据---拣货移位");
//		List<InvShiftDetailVO> detailVoList = new ArrayList<InvShiftDetailVO>();	
//		InvShiftDetailVO detailVo = new InvShiftDetailVO();
//		detailVoList.add(detailVo);
//		InvShiftDetail detail = new InvShiftDetail();
//		detailVo.setInvShiftDetail(detail);
//		//移位单明细字段
//		detail.setShiftDetailId(IdUtil.getUUID());
//		detail.setBatchNo(batchNo);
//		detail.setCreatePerson(loginUser.getUserId());
//		detail.setCreateTime(now);
//		detail.setUpdatePerson(loginUser.getUserId());
//		detail.setUpdateTime(now);
//		detail.setOrgId(orgId);
//		detail.setWarehouseId(LoginUtil.getWareHouseId());
//		detail.setInLocation(inLocation);
//		detail.setOutLocation(outLocation);
//		detail.setRealShiftQty(num);
//		detail.setShiftQty(num);
//		detail.setShiftId(shiftId);
//		detail.setSkuId(skuId);
//		detail.setBatchNo(batchNo);
//		detail.setInDate(now);
//		shiftVo.setListInvShiftDetailVO(detailVoList);
//		this.shiftService.quickSave(shiftVo);
//	}
    /**
	 * 发货区库存移位至拣货区
	 * @param vo
	 * @required inStockVo.findNum, inStockVo.invStock.locationId,inStockVo.invStock.skuId,inStockVo.invStock.batchNo,
	 * inStockVo.invLog.opPerson,inStockVo.invStock.locationId
	 * @optional
	 * @Description 
	 * 1、获取库存信息
	 * 2、比较库存数量
	 * 3、出库存（包括生成日志）
	 * 4、入库存（包括生成日志）
	 * 5、生成移位单，直接完成状态
	 * @version 2017年8月24日11:35:52<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
//    @Override
//    @Transactional(readOnly=false)
//	public void shiftToPick(InvStockVO inStockVo) throws Exception {
//    	Date now = new Date();
//    	Principal loginUser = LoginUtil.getLoginUser();
//    	// 验证用户是否登录
//    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
//    		throw new BizException("valid_common_user_no_login");
//    	}
//    	//字段校验
//    	if (inStockVo == null || inStockVo.getInvStock() == null || 
//    			inStockVo.getInvLog() == null) {
//    		throw new BizException("err_stock_param_null");
//    	}
////    	String outStockId = outStockVo.getInvStock().getStockId();
//    	Integer num = inStockVo.getFindNum();
//    	String inLocation = inStockVo.getInvStock().getLocationId();
//    	String skuId = inStockVo.getInvStock().getSkuId();
//    	String batchNo = inStockVo.getInvStock().getBatchNo();
//    	String outLocation = "";
//    	MetaLocationVO locationVo = new MetaLocationVO();
//		MetaLocation metaLocation = new MetaLocation();
//		metaLocation.setLocationType(Constant.LOCATION_TYPE_SEND);
//		locationVo.setLocation(metaLocation);
//		MetaLocation locationRet = this.locationService.findLoc(locationVo);
//		if (locationRet != null) {
//			outLocation = locationRet.getLocationId();
//		}
//    	String opPerson = inStockVo.getInvLog().getOpPerson();
//    	if (StringUtil.isTrimEmpty(outLocation) || StringUtil.isTrimEmpty(inLocation) || StringUtil.isTrimEmpty(opPerson) || num == null || num <= 0) {
//    		throw new BizException("err_stock_param_null");
//    	}
//    	String orgId = loginUser.getOrgId();
//    	//获取移位单编号
////    	String shiftNo = this.commonService.getNo(new CommonVo(orgId, BillPrefix.DOCUMENT_PREFIX_MOVE_INTERNAL));
//    	String shiftNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftPickNo(orgId);
//    	//调整出库内容
//    	if (StringUtil.equals(inLocation, outLocation)) {
//    		throw new BizException("err_shift_location_same");
//    	}
//    	//提取字段
//    	//判断库位是否在暂存区，如果在暂存区，禁止移出
//		MetaLocation location = locationService.findLocById(outLocation);
//		if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
//			throw new BizException("err_stock_param_location_shift");
//		}
////    	Date inDate = stock.getInDate();
//		//预设日志对象
//		InvLog invLog = new InvLog();
//		inStockVo.setInvLog(invLog);
//		invLog.setOpPerson(opPerson);
//		invLog.setLogType(Constant.STOCK_LOG_TYPE_SHIFT);
//		invLog.setInvoiceBill(shiftNo);
//		invLog.setSkuId(skuId);
//		MetaSku sku = skuService.get(skuId);
//		InvStock stock = inStockVo.getInvStock();
////		Integer stockQty = stock.getStockQty();
////		//设置体积和重量
////		Double stockVolume = stock.getStockVolume();
////		Double changeVolume = 0.0;
////		if (stockVolume != null && stockVolume != 0.0) {
////			invLog.setVolume(changeVolume);
////			changeVolume =  num / (stockQty * stockVolume);
////			stock.setStockVolume(stockVolume - changeVolume);
////		}
////		Double stockWeight = stock.getStockVolume();
////		Double changeWeight = 0.0;
////		if (stockWeight != null && stockWeight != 0.0) {
////			invLog.setWeight(changeWeight);
////			changeWeight = num / (stockQty * stockWeight);
////			stock.setStockWeight(stockWeight - changeWeight);
////		}
//    	//调整出库内容
//    	invLog.setQty(-num);
//    	invLog.setVolume(NumberUtil.mul(-num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
//		invLog.setWeight(NumberUtil.mul(-num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
//		
//		invLog.setLocationId(outLocation);
//		invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
//		//出库
//    	this.outStock(inStockVo);
//    	//调整入库内容
//		stock.setLocationId(inLocation);
//    	invLog.setQty(num);
//    	invLog.setVolume(NumberUtil.mul(num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
//		invLog.setWeight(NumberUtil.mul(num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
//		
//		invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
//		invLog.setLocationId(inLocation);
//    	//入库
//		this.inStock(inStockVo);
//		//新增移位单
//		InvShiftVO shiftVo = new InvShiftVO();
//		InvShift shift = new InvShift();
//		shiftVo.setInvShift(shift);
//		String shiftId = IdUtil.getUUID();
//		shift.setShiftId(shiftId);
//		shift.setShiftNo(shiftNo);
//		shift.setShiftStatus(Constant.SHIFT_STATUS_FINISH);
//		shift.setCreatePerson(loginUser.getUserId());
//		shift.setCreateTime(now);
//		shift.setUpdatePerson(loginUser.getUserId());
//		shift.setUpdateTime(now);
//		shift.setOrgId(orgId);
//		shift.setWarehouseId(LoginUtil.getWareHouseId());
//		shift.setOpPerson(opPerson);
//		shift.setShiftType(Constant.SHIFT_TYPE_REJECT);
//		shift.setNote("自动生成单据---退货移位");
//		List<InvShiftDetailVO> detailVoList = new ArrayList<InvShiftDetailVO>();	
//		InvShiftDetailVO detailVo = new InvShiftDetailVO();
//		detailVoList.add(detailVo);
//		InvShiftDetail detail = new InvShiftDetail();
//		detailVo.setInvShiftDetail(detail);
//		//移位单明细字段
//		detail.setShiftDetailId(IdUtil.getUUID());
//		detail.setBatchNo(batchNo);
//		detail.setCreatePerson(loginUser.getUserId());
//		detail.setCreateTime(now);
//		detail.setUpdatePerson(loginUser.getUserId());
//		detail.setUpdateTime(now);
//		detail.setOrgId(orgId);
//		detail.setWarehouseId(LoginUtil.getWareHouseId());
//		detail.setInLocation(inLocation);
//		detail.setOutLocation(outLocation);
//		detail.setRealShiftQty(num);
//		detail.setShiftQty(num);
//		detail.setShiftId(shiftId);
//		detail.setSkuId(skuId);
//		detail.setBatchNo(batchNo);
//		detail.setInDate(now);
//		shiftVo.setListInvShiftDetailVO(detailVoList);
//		this.shiftService.quickSave(shiftVo);
//	}

    /**
   	 * 移位至拣出库位，生成打开状态的移位单
   	 * @param vo
   	 * @optional
   	 * @Description 
   	 * @version 2017年8月24日11:35:52<br/>
   	 * @author 王通<br/>
   	 * @throws Exception 
   	 */
       @Override
       @Transactional(readOnly=false)
   	public void shiftToPick(String deliveryId) throws Exception {
       	Date now = new Date();
       	Principal loginUser = LoginUtil.getLoginUser();
       	// 验证用户是否登录
       	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
       		throw new BizException("valid_common_user_no_login");
       	}
       	//字段校验
       	if (deliveryId == null) {
       		throw new BizException("err_stock_param_null");
       	}
       	//先查出出库库位（发货区第一个库位）
       	MetaLocationVO mateLocationVo = new MetaLocationVO();
   		MetaLocation metaLocation = new MetaLocation();
   		metaLocation.setLocationType(Constant.LOCATION_TYPE_SEND);
   		mateLocationVo.setLocation(metaLocation);
   		MetaLocation locationRet = this.locationService.findLoc(mateLocationVo);
       	String outLocation = "";
   		if (locationRet != null) {
   			outLocation = locationRet.getLocationId();
   		} else {
   			throw new BizException("err_shift_send_location_null");
   		}
   		//新增移位单
       	String opPerson = loginUser.getUserId();
       	String orgId = loginUser.getOrgId();
       	//获取移位单编号
       	String shiftNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getShiftPickNo(orgId);
   		InvShiftVO shiftVo = new InvShiftVO();
   		InvShift shift = new InvShift();
   		shiftVo.setInvShift(shift);
   		String shiftId = IdUtil.getUUID();
   		shift.setShiftId(shiftId);
   		shift.setShiftNo(shiftNo);
   		shift.setShiftStatus(Constant.SHIFT_STATUS_OPEN);
   		shift.setCreatePerson(loginUser.getUserId());
   		shift.setCreateTime(now);
   		shift.setUpdatePerson(loginUser.getUserId());
   		shift.setUpdateTime(now);
   		shift.setOrgId(orgId);
   		shift.setWarehouseId(LoginUtil.getWareHouseId());
   		shift.setOpPerson(opPerson);
   		shift.setShiftType(Constant.SHIFT_TYPE_REJECT);
   		shift.setNote("自动生成单据---退货移位");
   		List<InvShiftDetailVO> detailVoList = new ArrayList<InvShiftDetailVO>();	
   		//查看是否波次单，是的话，不记录移入库位
   		SendDeliveryVo send = sendService.view(deliveryId);
   		if (StringUtil.isNoneBlank(send.getSendDelivery().getWaveId())) {
   			for (DeliveryDetailVo ddv : send.getDeliveryDetailVoList()) {
   				String skuId = ddv.getDeliveryDetail().getSkuId();
   				//根据发货单详情id查找拣货单详情id
   				String deliveryDetailId = ddv.getDeliveryDetail().getDeliveryDetailId();
   				SendPickDetailVo paramSendPickDetailVo = new SendPickDetailVo();
   				paramSendPickDetailVo.getSendPickDetail().setDeliveryDetailId(deliveryDetailId);
   				List<SendPickDetailVo> pickDetailVoList = pickDetailService.qryDetailsAndLocation(paramSendPickDetailVo,Constant.PICK_TYPE_REAL);
   				for (SendPickLocationVo locationVo :  pickDetailVoList.get(0).getRealPickLocations()) {
   	       			Double num = locationVo.getSendPickLocation().getPickQty();
   	       			String inLocation = locationVo.getSendPickLocation().getLocationId();
   	       			String batchNo = locationVo.getSendPickLocation().getBatchNo();
   	       			
   	       			//拼凑移位单详情数据
   	       			InvShiftDetailVO detailVo = new InvShiftDetailVO();
   	       			detailVoList.add(detailVo);
   	       			InvShiftDetail detail = new InvShiftDetail();
   	       			detailVo.setInvShiftDetail(detail);
   	       			//移位单明细字段
   	       			detail.setShiftDetailId(IdUtil.getUUID());
   	       			detail.setBatchNo(batchNo);
   	       			detail.setCreatePerson(loginUser.getUserId());
   	       			detail.setCreateTime(now);
   	       			detail.setUpdatePerson(loginUser.getUserId());
   	       			detail.setUpdateTime(now);
   	       			detail.setOrgId(orgId);
   	       			detail.setWarehouseId(LoginUtil.getWareHouseId());
   	       			detail.setInLocation(inLocation);
   	       			detail.setOutLocation(outLocation);
   	       			detail.setRealShiftQty(num);
   	       			detail.setShiftQty(num);
   	       			detail.setShiftId(shiftId);
   	       			detail.setSkuId(skuId);
   	       			detail.setBatchNo(batchNo);
   	       			detail.setInDate(now);
   				}
   			}
   		} else {
	   		//不是波次单的情况下，查询拣货单
	       	SendPickVo sendVoParam = new SendPickVo(); 
	       	sendVoParam.getSendPick().setDeliveryId(deliveryId);
	       	List<SendPickVo> listSendPick = pickService.qryListByParam(sendVoParam);
	    	SendPickVo pick =  pickService.view(listSendPick.get(0).getSendPick().getPickId());
	    	String pickNo = pick.getSendPick().getPickNo();
	    	shift.setInvolveBill(pickNo);
	       	//获取第一条拣货单的拣货明细
	       	for (SendPickDetailVo sendDetailVo : pick.getSendPickDetailVoList()) {
	       		//获取拣货明细的实际拣货库位
	       		List<SendPickLocationVo> locationVoList = sendDetailVo.getRealPickLocations();
	       		String skuId = sendDetailVo.getSendPickDetail().getSkuId();
	       		for (SendPickLocationVo locationVo : locationVoList) {
	       			SendPickLocation location = locationVo.getSendPickLocation();
	       			Double num = location.getPickQty();
	       			String inLocation = location.getLocationId();
	       			String batchNo = location.getBatchNo();
	       			
	       			//拼凑移位单详情数据
	       			InvShiftDetailVO detailVo = new InvShiftDetailVO();
	       			detailVoList.add(detailVo);
	       			InvShiftDetail detail = new InvShiftDetail();
	       			detailVo.setInvShiftDetail(detail);
	       			//移位单明细字段
	       			detail.setShiftDetailId(IdUtil.getUUID());
	       			detail.setBatchNo(batchNo);
	       			detail.setCreatePerson(loginUser.getUserId());
	       			detail.setCreateTime(now);
	       			detail.setUpdatePerson(loginUser.getUserId());
	       			detail.setUpdateTime(now);
	       			detail.setOrgId(orgId);
	       			detail.setWarehouseId(LoginUtil.getWareHouseId());
	       			detail.setInLocation(inLocation);
	       			detail.setOutLocation(outLocation);
	       			detail.setRealShiftQty(num);
	       			detail.setShiftQty(num);
	       			detail.setShiftId(shiftId);
	       			detail.setSkuId(skuId);
	       			detail.setBatchNo(batchNo);
	       			detail.setInDate(now);
	       		}
	       	}
   		}
		shiftVo.setListInvShiftDetailVO(detailVoList);
   		//保存移位单
   		this.shiftService.quickSave(shiftVo);
   	}
   /**
  	 * 发运确认，减少库存
  	 * @param vo
  	 * @optional
  	 * @Description 
  	 * @version 2017年8月24日11:35:52<br/>
  	 * @author 王通<br/>
  	 * @throws Exception 
  	 */
      @Override
      @Transactional(readOnly=false)
  	public void outStock(String deliveryId) throws Exception {
      	Principal loginUser = LoginUtil.getLoginUser();
      	// 验证用户是否登录
      	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
      		throw new BizException("valid_common_user_no_login");
      	}
      	//字段校验
      	if (deliveryId == null) {
      		throw new BizException("err_stock_param_null");
      	}
    	String deliveryNo = deliveryDao.selectByPrimaryKey(deliveryId).getDeliveryNo();
      	//先查出出库库位（发货区第一个库位）
      	MetaLocationVO mateLocationVo = new MetaLocationVO();
  		MetaLocation metaLocation = new MetaLocation();
  		metaLocation.setLocationType(Constant.LOCATION_TYPE_SEND);
  		metaLocation.setLocationStatus(Constant.STATUS_ACTIVE);
  		mateLocationVo.setLocation(metaLocation);
  		MetaLocation locationRet = this.locationService.findLoc(mateLocationVo);
      	String outLocation = "";
  		if (locationRet != null) {
  			outLocation = locationRet.getLocationId();
  		} else {
  			throw new BizException("err_shift_send_location_null");
  		}
      	String opPerson = loginUser.getUserId();
//      	SendPickVo sendVoParam = new SendPickVo(); 
//      	sendVoParam.getSendPick().setDeliveryId(deliveryId);
//      	List<SendPickVo> listSendPick = pickService.qryListByParam(sendVoParam);
//      	//获取第一条拣货单的拣货明细
//      	SendPickVo sendPickVo = pickService.view(listSendPick.get(0).getSendPick().getPickId());
//  		String pickNo = sendPickVo.getSendPick().getPickNo();
      	SendPickDetailVo param = new SendPickDetailVo();
	    param.getSendPickDetail().setDeliveryId(deliveryId);
      	List<SendPickDetailVo> listDetailVo = pickDetailService.qryDetails(param);
      	for (SendPickDetailVo sendDetailVo : listDetailVo) {
      		//获取拣货明细的实际拣货库位
      		List<SendPickLocationVo> locationVoList = sendDetailVo.getRealPickLocations();
      		String skuId = sendDetailVo.getSendPickDetail().getSkuId();
      		for (SendPickLocationVo locationVo : locationVoList) {
      			SendPickLocation location = locationVo.getSendPickLocation();
      			Double num = location.getPickQty();
      			String batchNo = location.getBatchNo();

    			//预设库存对象
    			InvStockVO outStockVo = new InvStockVO();
    			InvStock stock = new InvStock();
    			outStockVo.setInvStock(stock);
    			stock.setSkuId(skuId);
    			stock.setLocationId(outLocation);
    			stock.setBatchNo(batchNo);
    			outStockVo.setFindNum(num);
    			//预设日志对象
    			InvLog invLog = new InvLog();
    			outStockVo.setInvLog(invLog);
    			invLog.setOpPerson(opPerson);
    			invLog.setLogType(Constant.STOCK_LOG_TYPE_SEND);
    			invLog.setInvoiceBill(deliveryNo);
    			invLog.setSkuId(skuId);
    			MetaSku sku = skuService.get(skuId);
//    			Integer stockQty = stock.getStockQty();
//    			//设置体积和重量
//    			Double stockVolume = stock.getStockVolume();
//    			Double changeVolume = 0.0;
//    			if (stockVolume != null && stockVolume != 0.0) {
//    				invLog.setVolume(changeVolume);
//    				changeVolume =  num / (stockQty * stockVolume);
//    				stock.setStockVolume(stockVolume - changeVolume);
//    			}
//    			Double stockWeight = stock.getStockVolume();
//    			Double changeWeight = 0.0;
//    			if (stockWeight != null && stockWeight != 0.0) {
//    				invLog.setWeight(changeWeight);
//    				changeWeight = num / (stockQty * stockWeight);
//    				stock.setStockWeight(stockWeight - changeWeight);
//    			}
    	    	//调整出库内容
    	    	invLog.setQty(-num);
    	    	invLog.setVolume(NumberUtil.mul(-num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(-num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    			
    			invLog.setLocationId(outLocation);
    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
    			//出库
    	    	this.outStock(outStockVo);
      		}
      	}
  	}
    /**
  	 * 补货（上架单完成时）停用
  	 * @param vo
  	 * @required skuId 货品id, num 补货数量
  	 * @optional
  	 * @Description 
  	 * 1、获取出库库位（可能多个，根据拣货规则）
  	 * 2、获取入库库位（可能多个，根据上架规则）
  	 * 3、生成移位单，若无推荐库位则是打开状态，若有推荐库位则是生效状态且推送工作人员
  	 * @version 2017年8月24日11:35:52<br/>
  	 * @author 王通<br/>
  	 * @throws Exception 
  	 */
//      @Override
//      @Transactional(readOnly=false)
//  	public void repSendSku(Set<String> skuIdSet, int sendQty, String involveBill) throws Exception {
//      	Date now = new Date();
//      	Principal loginUser = LoginUtil.getLoginUser();
//      	
//      	// 验证用户是否登录
//      	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
//      		throw new BizException("valid_common_user_no_login");
//      	}
//      	//字段校验
//      	if (skuIdSet == null || skuIdSet.isEmpty() ) {
//      		throw new BizException("valid_common_data_empty");
//      	}
//      	String orgId = loginUser.getOrgId();
//		String warehouseId = LoginUtil.getWareHouseId();
//  		String shiftId = IdUtil.getUUID();
//      	//获取移位单编号
//      	String shiftNo = context.getStrategy4No(orgId, warehouseId).getShiftReplenishmentNo(orgId);
//  		//新增移位单
//  		InvShiftVO shiftVo = new InvShiftVO();
//  		InvShift shift = new InvShift();
//  		shiftVo.setInvShift(shift);
//  		shift.setShiftId(shiftId);
//  		shift.setShiftNo(shiftNo);
//  		shift.setInvolveBill(involveBill);
//  		shift.setShiftStatus(Constant.SHIFT_STATUS_OPEN);
//  		shift.setCreatePerson(loginUser.getUserId());
//  		shift.setCreateTime(now);
//  		shift.setUpdatePerson(loginUser.getUserId());
//  		shift.setUpdateTime(now);
//  		shift.setOrgId(orgId);
//  		shift.setWarehouseId(warehouseId);
//  		shift.setShiftType(Constant.SHIFT_TYPE_REPLENISHMENT);
//  		shift.setNote("自动生成单据---补货移位");
//      	List<InvShiftDetailVO> shiftList = new ArrayList<InvShiftDetailVO>();
//      	for (String skuId : skuIdSet) {
//          	//确定移位数量
//          	MetaSku sku = skuService.get(skuId);
//          	if (sku == null) {
//          		throw new BizException("err_stock_imp_sku", skuId);
//          	}
//          	Integer maxReplenishStock = sku.getMaxReplenishStock();
//          	//无上限数量，不补货
//          	if (maxReplenishStock == null || maxReplenishStock == 0) {
//          		return;
//          	}
//          	InvStockVO stockVo = new InvStockVO();
//          	InvStock invStock = new InvStock();
//          	stockVo.setInvStock(invStock);
//          	invStock.setSkuId(skuId);
//          	List<Integer> locationTypeList = new ArrayList<Integer>();
//          	locationTypeList.add(Constant.LOCATION_TYPE_PICKUP);
//          	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
//          	stockVo.setLocationTypeList(locationTypeList);
//          	int nowStock = getStockQty(stockVo);
//          	//判断当前库存是否不小于发货单订单数量
//          	if (nowStock < sendQty) {
//          		throw new BizException("err_stock_size");
//          	}
//          	locationTypeList.clear();
//          	locationTypeList.add(Constant.LOCATION_TYPE_PICKUP);
//          	int nowPickStock = getStockQty(stockVo);
//          	//判断当前拣货区库存是否大于发货单订单数量
//          	if (nowPickStock >= sendQty) {
//          		return;
//          	}
//          	//判断补货上限数量是否满足订单数量
//          	if (maxReplenishStock < sendQty) {
//          		throw new BizException("err_stock_max_rep");
//          	}
//          	//需移位数量为补货上限数量与当前拣货区库存数量之差
//          	int shiftQty = maxReplenishStock - nowPickStock;
//          	//根据库存获取出库库位（可能多个，根据拣货规则）
//          	//规则：1、先进先出
//          	//先查询存储区库存
//          	locationTypeList.clear();
//          	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
//          	stockVo.setResultOrder("t1.batch_no asc, t1.in_date asc");
//          	List<InvStock> stockList = list(stockVo);
//          	int tempShiftQty = shiftQty;
//      		String inLocation = null;
//    		for (InvStock stock : stockList) {
//    			InvShiftDetailVO shiftDetailVo = new InvShiftDetailVO();
//          		InvShiftDetail shiftDetail = new InvShiftDetail();
//          		shiftDetail.setShiftDetailId(UUID.randomUUID().toString());
//          		shiftDetail.setShiftId(shiftId);
//          		shiftDetailVo.setInvShiftDetail(shiftDetail);
//      			shiftList.add(shiftDetailVo);
//          		shiftDetail.setOutLocation(stock.getLocationId());
//          		shiftDetail.setSkuId(skuId);
//          		String batchNo = stock.getBatchNo();
//          		shiftDetail.setBatchNo(batchNo);
//          		int stockQty = stock.getStockQty();
//          		if (inLocation == null) {
//    	      		//根据货品和批次获取入库库位
//    	      		InvStockVO pickStockVo = new InvStockVO();
//    	      		InvStock pickStock = new InvStock();
//    	      		pickStockVo.setInvStock(pickStock);
//    	          	List<Integer> locTypeList = new ArrayList<Integer>();
//    	          	locTypeList.add(Constant.LOCATION_TYPE_PICKUP);
//    	          	//优先选择库存少的库位
//    	          	pickStockVo.setResultOrder("t1.stock_qty asc");
//    	      		pickStockVo.setLocationTypeList(locTypeList);
//    	      		pickStock.setSkuId(skuId);
//    	      		pickStock.setBatchNo(batchNo);
//    	      		List<InvStock> stockListInPick = list(pickStockVo);
//    	      		if (stockListInPick != null && !stockListInPick.isEmpty()) {
//    	      			//有库存时，取第一个库位
//    	      			inLocation = stockListInPick.get(0).getLocationId();
//    	      			//**这里没判断库存溢出的问题，以后考虑
//    	      		} else {
//    	      			//无库存时，获取空库位
//    	      			//先按货品类型查询库区列表
//    	      			MetaAreaVO vo1 = new MetaAreaVO();
//          				MetaSkuType type = skuTypeService.get(sku.getSkuTypeId());
//          				//只支持二级
////    	      				vo1.getArea().setSkuTypeId(type.getParentId());
//          				//支持多级
//          				String no = type.getLevelCode().substring(0, 3);
//          				MetaSkuType type1 = skuTypeService.get(new MetaSkuType().setLevelCode(no));
//          				vo1.getArea().setSkuTypeId(type1.getSkuTypeId());
//          				List<Integer> listTypes = new ArrayList<Integer>();
//          				listTypes.add(Constant.LOCATION_TYPE_PICKUP);
//    	      			vo1.setListTypes(listTypes);
//    	      			List<MetaArea> listArea = areaExtlService.listAreaByExample(vo1);
//    	      			List<String> listAreaId = new ArrayList<String>();
//    	      			for(MetaArea area : listArea) {
//    	      				listAreaId.add(area.getAreaId());
//    	      			}
//    	      			//组装库位查询条件
//    	      			MetaLocationVO metaLocationVO = new MetaLocationVO();
//    	      			MetaLocation metaLocation = new MetaLocation();
//    	      			//相同货品类型
//    	      			metaLocationVO.setListAreaId(listAreaId);
//    	      			metaLocationVO.setLocation(metaLocation);
//    	      			metaLocationVO.setSkuId(skuId);
//    	      			//相同货主
//    	      			metaLocationVO.setListOwnerId(Arrays.asList(sku.getOwner()));
//    	      			metaLocation.setLocationStatus(Constant.STATUS_ACTIVE);
//    	      			metaLocation.setUsedCapacity(new BigDecimal(0));
//    	      			metaLocation.setPreusedCapacity(new BigDecimal(0));
//    	      			metaLocation.setLocationType(Constant.LOCATION_TYPE_PICKUP);
//    	      			inLocation = locationService.findLoc(metaLocationVO).getLocationId();
//    	      			if (inLocation == null) {
//    	      				//无空库位，不补货
////    	      				throw new BizException("err_stock_no_pick_location");
//    	      				return;
//    	      			}
//    	      			//**这里没判断库存溢出的问题，以后考虑
//    	      		}
//          		}
//          		shiftDetail.setInLocation(inLocation);
//          		//补充移位单详情的其他字段
//          		shiftDetail.setCreatePerson(loginUser.getUserId());
//          		shiftDetail.setCreateTime(now);
//          		shiftDetail.setOrgId(orgId);
//          		shiftDetail.setWarehouseId(warehouseId);
//          		//判断是否满足移位数量，如果满足则直接扣取订单数量，不满足则取完且进入下一个。
//          		if (stockQty >= tempShiftQty) {
//          			shiftDetail.setShiftQty(tempShiftQty);
//          			//清除当前使用的入库库位
//          			inLocation = null;
//          			break;
//          		} else {
//          			shiftDetail.setShiftQty(stockQty);
//          			tempShiftQty -= stockQty;
//          			//继续使用上一个入库库位
//          		}
//          	}
//      	}
//  		//移位单明细字段
//  		shiftVo.setListInvShiftDetailVO(shiftList);
//  		this.shiftService.quickSave(shiftVo);
//  	}
    
      /**
    	 * 补货（对外接口）
    	 * @param vo
    	 * @required skuId 货品id
    	 * @optional
    	 * @Description 
    	 * 1、检查拣货区库存是否低于预警数量
    	 * 2、存储区获取出库库位（可能多个，根据拣货规则）
    	 * 3、拣货区获取入库库位（可能多个，根据上架规则）
    	 * 4、生成移位单，若无推荐库位则是打开状态，若有推荐库位则是生效状态且推送工作人员
    	 * @version 2017年8月24日11:35:52<br/>
    	 * @author 王通<br/>
    	 * @throws Exception 
    	 */
        @Override
        @Transactional(readOnly=false)
    	public void repSkuInterface(String skuNo, Double number) throws Exception {
        	Date now = new Date();
          	Principal loginUser = LoginUtil.getLoginUser();
          	
          	// 验证用户是否登录
          	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
          		throw new BizException("valid_common_user_no_login");
          	}
          	//字段校验
          	if (skuNo == null) {
          		throw new BizException("valid_common_data_empty");
          	}

          	String orgId = loginUser.getOrgId();
    		String warehouseId = LoginUtil.getWareHouseId();
      		String shiftId = IdUtil.getUUID();
          	//获取移位单编号
          	String shiftNo = context.getStrategy4No(orgId, warehouseId).getShiftReplenishmentNo(orgId);
      		//新增移位单
      		InvShiftVO shiftVo = new InvShiftVO();
      		InvShift shift = new InvShift();
      		shiftVo.setInvShift(shift);
      		shift.setShiftId(shiftId);
      		shift.setShiftNo(shiftNo);
      		shift.setInvolveBill("");
      		shift.setShiftStatus(Constant.SHIFT_STATUS_OPEN);
      		shift.setCreatePerson(loginUser.getUserId());
      		shift.setCreateTime(now);
      		shift.setUpdatePerson(loginUser.getUserId());
      		shift.setUpdateTime(now);
      		shift.setOrgId(orgId);
      		shift.setWarehouseId(warehouseId);
      		shift.setShiftType(Constant.SHIFT_TYPE_REPLENISHMENT);
      		shift.setNote("自动生成单据---接口推送补货移位");
          	shiftVo.setListInvShiftDetailVO(new ArrayList<InvShiftDetailVO>());

          	//确定移位数量
          	MetaSku sku = skuService.getBySkuNo(skuNo);
          	if (sku == null) {
          		throw new BizException("err_stock_imp_sku", skuNo);
          	}
//          	Integer maxReplenishStock = sku.getMaxReplenishStock();
//          	Integer minReplenishStock = sku.getMinReplenishStock();
//          	//无上限下限数量，不补货
//          	if (maxReplenishStock == null || maxReplenishStock == 0 || minReplenishStock == null || minReplenishStock == 0) {
////	          		return;
//          		//继续下一个货品
//          		continue;
//          	}
//          	InvStockVO stockVo = new InvStockVO();
//          	InvStock invStock = new InvStock();
//          	stockVo.setInvStock(invStock);
//          	invStock.setSkuId(skuId);
//          	List<Integer> locationTypeList = new ArrayList<Integer>();
//          	locationTypeList.add(Constant.LOCATION_TYPE_PICKUP);
//	          	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
//          	stockVo.setLocationTypeList(locationTypeList);
//          	double nowStock = getStockQty(stockVo);
          	//判断当前库存是否不小于预警数量
//          	if (nowStock >= minReplenishStock) {
////	          		return;
//          		//继续下一个货品
//          		continue;
//          	}
          	//需移位数量为补货上限数量与当前拣货区库存数量之差
//          	double shiftQty = maxReplenishStock - nowStock;
          	double shiftQty = number;
          	//根据库存获取出库库位（可能多个，根据拣货规则）
          	//规则：1、先进先出 2、空库位 ---已停用
          	//新规则：1、绑定货品批次库位 2、绑定货品空批次库位
          	
          	//旧方法先独立出去-查询存储区库存生成库位
//	          	getLocationByStock(shiftVo, sku, shiftQty);
//	          	getLocationByBinding(shiftVo, sku, shiftQty);
          	getLocationByStrategy(shiftVo, sku, shiftQty);
      		//移位单明细字段
      		if (!shiftVo.getListInvShiftDetailVO().isEmpty()) {
          		this.shiftService.quickSave(shiftVo);
      		}
    	}
        /**
    	 * 补货（拣货单完成后）
    	 * @param vo
    	 * @required skuId 货品id
    	 * @optional
    	 * @Description 
    	 * 1、检查拣货区库存是否低于预警数量
    	 * 2、存储区获取出库库位（可能多个，根据拣货规则）
    	 * 3、拣货区获取入库库位（可能多个，根据上架规则）
    	 * 4、生成移位单，若无推荐库位则是打开状态，若有推荐库位则是生效状态且推送工作人员
    	 * @version 2017年8月24日11:35:52<br/>
    	 * @author 王通<br/>
    	 * @throws Exception 
    	 */
        @Override
        @Transactional(readOnly=false)
    	public void repPickSku(Set<String> skuIdSet, String involveBill) throws Exception {
        	Date now = new Date();
          	Principal loginUser = LoginUtil.getLoginUser();
          	
          	// 验证用户是否登录
          	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
          		throw new BizException("valid_common_user_no_login");
          	}
          	//字段校验
          	if (skuIdSet == null || skuIdSet.isEmpty()) {
          		throw new BizException("valid_common_data_empty");
          	}

          	String orgId = loginUser.getOrgId();
    		String warehouseId = LoginUtil.getWareHouseId();
      		String shiftId = IdUtil.getUUID();
          	//获取移位单编号
          	String shiftNo = context.getStrategy4No(orgId, warehouseId).getShiftReplenishmentNo(orgId);
      		//新增移位单
      		InvShiftVO shiftVo = new InvShiftVO();
      		InvShift shift = new InvShift();
      		shiftVo.setInvShift(shift);
      		shift.setShiftId(shiftId);
      		shift.setShiftNo(shiftNo);
      		shift.setInvolveBill(involveBill);
      		shift.setShiftStatus(Constant.SHIFT_STATUS_OPEN);
      		shift.setCreatePerson(loginUser.getUserId());
      		shift.setCreateTime(now);
      		shift.setUpdatePerson(loginUser.getUserId());
      		shift.setUpdateTime(now);
      		shift.setOrgId(orgId);
      		shift.setWarehouseId(warehouseId);
      		shift.setShiftType(Constant.SHIFT_TYPE_REPLENISHMENT);
      		shift.setNote("自动生成单据---补货移位");
          	shiftVo.setListInvShiftDetailVO(new ArrayList<InvShiftDetailVO>());
          	for (String skuId : skuIdSet) {
	          	//确定移位数量
	          	MetaSku sku = skuService.get(skuId);
	          	if (sku == null) {
	          		throw new BizException("err_stock_imp_sku", skuId);
	          	}
	          	Integer maxReplenishStock = sku.getMaxReplenishStock();
	          	Integer minReplenishStock = sku.getMinReplenishStock();
	          	//无上限下限数量，不补货
	          	if (maxReplenishStock == null || maxReplenishStock == 0 || minReplenishStock == null || minReplenishStock == 0) {
//	          		return;
	          		//继续下一个货品
	          		continue;
	          	}
	          	InvStockVO stockVo = new InvStockVO();
	          	InvStock invStock = new InvStock();
	          	stockVo.setInvStock(invStock);
	          	invStock.setSkuId(skuId);
	          	List<Integer> locationTypeList = new ArrayList<Integer>();
	          	locationTypeList.add(Constant.LOCATION_TYPE_PICKUP);
//	          	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
	          	stockVo.setLocationTypeList(locationTypeList);
	          	double nowStock = getStockQty(stockVo);
	          	//判断当前库存是否不小于预警数量
	          	if (nowStock >= minReplenishStock) {
//	          		return;
	          		//继续下一个货品
	          		continue;
	          	}
	          	//需移位数量为补货上限数量与当前拣货区库存数量之差
	          	double shiftQty = maxReplenishStock - nowStock;
	          	//根据库存获取出库库位（可能多个，根据拣货规则）
	          	//规则：1、先进先出 2、空库位 ---已停用
	          	//新规则：1、绑定货品批次库位 2、绑定货品空批次库位
	          	
	          	//旧方法先独立出去-查询存储区库存生成库位
//	          	getLocationByStock(shiftVo, sku, shiftQty);
//	          	getLocationByBinding(shiftVo, sku, shiftQty);
	          	getLocationByStrategy(shiftVo, sku, shiftQty);
          	}
      		//移位单明细字段
      		if (!shiftVo.getListInvShiftDetailVO().isEmpty()) {
          		this.shiftService.quickSave(shiftVo);
      		}
    	}
    	/**
    	 * 根据策略选择库位
    	 * @param shiftVo
    	 * @param sku
    	 * @param qty
    	 * @throws Exception
    	 */
        public void getLocationByStrategy(InvShiftVO shiftVo, MetaSku sku, double qty) throws Exception {
	        AutoRepStockFlow flow = new AutoRepStockFlow(shiftVo, sku, qty);
			flow.startFlow();
        }
    	/**
    	 * 根据绑定库位选择库位
    	 * @param shiftVo
    	 * @param sku
    	 * @param qty
    	 * @throws Exception
    	 */
        public void getLocationByBinding(InvShiftVO shiftVo, MetaSku sku, double qty) throws Exception {
	        AutoRepStockFlow flow = new AutoRepStockFlow(shiftVo, sku, qty);
			flow.startFlow();
        }
        /**
         * 根据相同批次库存选择库位，其次空库位
         * @param shiftVo
         * @param sku
         * @param qty
         * @throws Exception
         */
        public void getLocationByStock(InvShiftVO shiftVo, MetaSku sku, double qty) throws Exception {
        	InvStockVO stockVo = new InvStockVO();
			InvStock invStock = new InvStock();
			stockVo.setInvStock(invStock);
			invStock.setSkuId(sku.getSkuId());
			List<Integer> locationTypeList = new ArrayList<Integer>();
          	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
			stockVo.setLocationTypeList(locationTypeList);
          	stockVo.setResultOrder("t1.batch_no asc, t1.in_date asc");
          	List<InvStock> stockList = list(stockVo);
          	double tempShiftQty = qty;
      		String inLocation = null;
      		List<String> emptyLocationList = new ArrayList<String>();
      		if (stockList == null || stockList.size() == 0) return;
    		for (InvStock stock : stockList) {
    			InvShiftDetailVO shiftDetailVo = new InvShiftDetailVO();
          		InvShiftDetail shiftDetail = new InvShiftDetail();
          		shiftDetail.setShiftDetailId(UUID.randomUUID().toString());
          		shiftDetail.setShiftId(shiftVo.getInvShift().getShiftId());
          		shiftDetailVo.setInvShiftDetail(shiftDetail);
          		shiftDetail.setOutLocation(stock.getLocationId());
          		shiftDetail.setSkuId(sku.getSkuId());
          		String batchNo = stock.getBatchNo();
          		shiftDetail.setBatchNo(batchNo);
          		double stockQty = stock.getStockQty();
          		if (inLocation == null) {
    	      		//根据货品和批次获取入库库位
    	      		InvStockVO pickStockVo = new InvStockVO();
    	      		InvStock pickStock = new InvStock();
    	      		pickStockVo.setInvStock(pickStock);
    	          	List<Integer> locTypeList = new ArrayList<Integer>();
    	          	locTypeList.add(Constant.LOCATION_TYPE_PICKUP);
    	          	//优先选择库存少的库位
    	          	pickStockVo.setResultOrder("t1.stock_qty asc");
    	      		pickStockVo.setLocationTypeList(locTypeList);
    	      		pickStock.setSkuId(sku.getSkuId());
    	      		pickStock.setBatchNo(batchNo);
    	      		List<InvStock> stockListInPick = list(pickStockVo);
    	      		if (stockListInPick != null && !stockListInPick.isEmpty()) {
    	      			//有库存时，取第一个库位
    	      			inLocation = stockListInPick.get(0).getLocationId();
    	      			//**这里没判断库存溢出的问题，以后考虑
    	      		} else {
    	      			//无库存时，获取空库位
    	      			//先按货品类型查询库区列表
    	      			MetaAreaVO vo1 = new MetaAreaVO();
          				MetaSkuType type = skuTypeService.get(sku.getSkuTypeId());
          				//只支持二级
//    	      				vo1.getArea().setSkuTypeId(type.getParentId());
          				//支持多级
          				String no = type.getLevelCode().substring(0, 3);
          				MetaSkuType type1 = skuTypeService.get(new MetaSkuType().setLevelCode(no));
          				vo1.getArea().setSkuTypeId(type1.getSkuTypeId());
          				List<Integer> listTypes = new ArrayList<Integer>();
          				listTypes.add(Constant.LOCATION_TYPE_PICKUP);
    	      			vo1.setListTypes(listTypes);
    	      			List<MetaArea> listArea = areaExtlService.listAreaByExample(vo1);
    	      			List<String> listAreaId = new ArrayList<String>();
    	      			for(MetaArea area : listArea) {
    	      				listAreaId.add(area.getAreaId());
    	      			}
    	      			//组装库位查询条件
    	      			MetaLocationVO metaLocationVO = new MetaLocationVO();
    	      			MetaLocation metaLocation = new MetaLocation();
    	      			metaLocationVO.setLocation(metaLocation);
    	      			//相同货品类型
    	      			metaLocationVO.setListAreaId(listAreaId);
    	      			//相同货主
    	      			metaLocationVO.setListOwnerId(Arrays.asList(sku.getOwner()));
    	      			metaLocationVO.setSkuId(sku.getSkuId());
    	      			metaLocation.setUsedCapacity(new BigDecimal(0));
    	      			metaLocation.setPreusedCapacity(new BigDecimal(0));
    	      			metaLocation.setLocationType(Constant.LOCATION_TYPE_PICKUP);
    	      			//加入判断条件，不同货品详情分配不同的库位
    	      			metaLocationVO.setNoListLocationId(emptyLocationList);
    	      			MetaLocation location = locationService.findLoc(metaLocationVO);
    	      			if (location != null) {
        	      			inLocation = location.getLocationId();
        	      			emptyLocationList.add(inLocation);
    	      			} else {
    	      				//没有空库位，当前记录不补货，继续下一个货品
//    	      				return;
    		          		//继续下一个货品
    		          		break;
    	      			}
    	      			//**这里没判断库存溢出的问题，以后考虑
    	      		}
          		}
          		shiftDetail.setInLocation(inLocation);
          		//补充移位单详情的其他字段
          		shiftDetail.setCreatePerson(LoginUtil.getLoginUser().getUserId());
          		shiftDetail.setCreateTime(new Date());
          		shiftDetail.setOrgId(LoginUtil.getLoginUser().getOrgId());
          		shiftDetail.setWarehouseId(LoginUtil.getWareHouseId());
      			shiftVo.getListInvShiftDetailVO().add(shiftDetailVo);
          		//判断是否满足移位数量，如果满足则直接扣取订单数量，不满足则取完且进入下一个。
          		if (stockQty >= tempShiftQty) {
          			shiftDetail.setShiftQty(tempShiftQty);
          			//清除当前使用的入库库位
          			inLocation = null;
          			break;
          		} else {
          			shiftDetail.setShiftQty(stockQty);
          			tempShiftQty -= stockQty;
          			//继续使用上一个入库库位
          		}
          	}
        }
	/**
	 * 验证库存是否冻结
	 * @param stockVo
	 * @throws Exception
	 * @required skuId,localtionId
	 * @optional batchNo
	 * @Description 
	 * @version 2017年2月24日 下午5:59:35<br/>
	 * @author 王通<br/>
	 */
	private void checkStockFreeze(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		stockVo.setContainTemp(true);
		List<InvStock> list = this.stockDao.findStockList(stockVo);
		if (list == null || list.isEmpty()) {
			throw new BizException("err_stock_location_freeze");
		}
	}

	/**
	 * 快速调整库存
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required 库存ID:invStock.stockId，调整原因:invLog.note,批次号:invStock.batchNo,调整后数量:findNum
	 * @optional  
	 * @Description 
	 * 1、批次相同时，修改库存并新增调账单和库存日志
	 * 2、批次不相同时，修改库存并新增2个调整单和2个库存日志
	 * 3、中间加入了各种判断
	 * @version 2017年2月27日 下午4:37:04<br/>
	 * @author 王通<br/> 
	 */
	@Override
	@Transactional(readOnly = false)
	public void changeStock(InvStockVO stockVo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		//字段校验
		if (stockVo == null || stockVo.getInvStock()==null) {
			throw new BizException("err_stock_param_null");
		}
		Boolean changeStock = stockVo.getChangeStock();
		if (changeStock == null) {
			//默认修改库存
			changeStock = true;
		}
		InvStock paramStock = stockVo.getInvStock();
		// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	String stockId = paramStock.getStockId();
    	String batchNo = paramStock.getBatchNo();
		String locationId = paramStock.getLocationId();
		MetaLocation location = locationService.findLocById(locationId);
		//批次号为各种空值时，默认为空串
		if (StringUtil.isTrimEmpty(batchNo)) {
			batchNo = "";
			paramStock.setBatchNo(batchNo);
		}
    	String note = stockVo.getInvLog().getNote();
		Double qty = stockVo.getFindNum();
		if (qty == null) {
//			throw new BizException("err_stock_param_null");
			throw new BizException("err_stock_qty_null");
		}
		if (StringUtil.isTrimEmpty(note)) {
			throw new BizException("err_stock_note_null");
		}
//		if (StringUtil.isTrimEmpty(stockId)) {
//			throw new BizException("err_stock_add");
//		}
		if (StringUtil.isTrimEmpty(stockId)) {
			//新增库存方法
			String skuId = paramStock.getSkuId();
			String packId = paramStock.getPackId();
			if (StringUtil.isTrimEmpty(skuId) || StringUtil.isTrimEmpty(locationId)) {
				throw new BizException("err_stock_add");
			}
    		//准备调账单字段
			InvAdjustVO adjustVo = new InvAdjustVO();
			InvAdjust adjust = new InvAdjust();
			adjustVo.setInvAdjust(adjust);
			if (location.getLocationType() == Constant.LOCATION_TYPE_BAD) {
				adjust.setDataFrom(Constant.ADJUST_DATE_FROM_BAD_CHANGE);
			} else {
				adjust.setDataFrom(Constant.ADJUST_DATE_FROM_CHANGE);
			}
			adjust.setAdjustStatus(Constant.STATUS_OPEN);
			adjust.setNote(note);
			List<InvAdjustDetailVO> adjDetailVoList = new ArrayList<InvAdjustDetailVO>();
			adjustVo.setAdjDetailVoList(adjDetailVoList);
			InvAdjustDetailVO adjDetailVo = new InvAdjustDetailVO();
			adjDetailVoList.add(adjDetailVo);
			InvAdjustDetail adjDetail = new InvAdjustDetail();
			adjDetailVo.setInvAdjustDetail(adjDetail);
			adjDetail.setAdjustType(Constant.ADJUST_TYPE_IN);
			adjDetail.setStockQty(0.0);
			adjDetail.setDifferenceQty(qty);
			adjDetail.setRealQty(qty);
			adjDetail.setSkuId(skuId);
			adjDetail.setLocationId(locationId);
			adjDetail.setBatchNo(batchNo);
//			String adjNo = commonService.getNo(new CommonVo(orgId, BillPrefix.DOCUMENT_ADJUST_BILL));
			String adjNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getAdjustNo(orgId);
			adjust.setAdjustNo(adjNo);
			MetaSku sku = skuService.get(skuId);
			if (sku == null) {
				throw new BizException("err_stock_sku_null");
			}
			//判断是否调整库存
			if (changeStock) {
				//调整库存
				//判断库位是否在暂存区，如果在暂存区，禁止导入
				InvStock findStock = null;
				if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
					throw new BizException("err_stock_param_location_import");
				} else {
					findStock = this.view(skuId, locationId, batchNo, null);
				}
				if (qty == 0) {
    				//如果库存数量为0，参数错误
					return;
				} else if (findStock != null) {
					//判断是否有相同库存，如果有的话，根据需求，直接提示用户不合并
					throw new BizException("err_stock_change_same");
					//以下是库存合并，需求暂定为不需要
//					MetaSku sku = skuService.get(skuId);
//					if (sku == null) {
//						throw new BizException("err_stock_sku_null");
//					}
//					InvStock updateStock = new InvStock();
//					updateStock.setStockId(findStock.getStockId());
//					updateStock.setUpdatePerson(loginUser.getUserId());
//					updateStock.setUpdateTime(new Date());
//					updateStock.setStockQty(findStock.getStockQty() + qty);
//					updateStock.setStockVolume(NumberUtil.add(findStock.getStockVolume() == null ? 0 : findStock.getStockVolume(), NumberUtil.mul(qty, sku.getPerVolume() == null ? 0 : sku.getPerVolume())));
//					updateStock.setStockWeight(NumberUtil.add(findStock.getStockWeight() == null ? 0 : findStock.getStockWeight(), NumberUtil.mul(qty, sku.getPerWeight() == null ? 0 : sku.getPerWeight())));
//					adjDetail.setStockQty(findStock.getStockQty());
//					adjDetail.setDifferenceQty(qty);
//					adjDetail.setRealQty(findStock.getStockQty() + qty);
//					this.stockDao.updateByPrimaryKeySelective(updateStock);
				} else {
    				//其他情况，新增库存
					paramStock.setOrgId(loginUser.getOrgId());
					paramStock.setWarehouseId(LoginUtil.getWareHouseId());
					paramStock.setCreatePerson(loginUser.getUserId());
					paramStock.setCreateTime(new Date());
					paramStock.setStockQty(qty);
					//准备重量体积字段
					paramStock.setOwner(sku.getOwner());
					paramStock.setStockVolume(NumberUtil.mul(qty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
					paramStock.setStockWeight(NumberUtil.mul(qty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
					paramStock.setStockId(IdUtil.getUUID());
					paramStock.setInDate(new Date());
					//若未填货品状态，则默认货品状态
					if (paramStock.getSkuStatus() == null) {
						paramStock.setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
						stockVo.getInvLog().setSkuStatus(Constant.STOCK_SKU_STATUS_NORMAL);
					} else {
						stockVo.getInvLog().setSkuStatus(paramStock.getSkuStatus());
					}
					paramStock.setStockId2(context.getStrategy4Id().getStockSeq());
					this.stockDao.insertSelective(paramStock);
				}
				//调整库容
	    		this.locationService.addCapacity(skuId, locationId, packId, qty, false);
				//调整调账单状态为生效
    			adjust.setAdjustStatus(Constant.STATUS_ACTIVE);
    			//保存调账单
    			this.adjustService.add(adjustVo);
    			//准备库存日志字段
    			InvLog invLog = new InvLog();
    			invLog.setNote(note);
    			invLog.setOpPerson(loginUser.getUserId());
    			invLog.setLogType(Constant.STOCK_LOG_TYPE_CHANGE);
    			invLog.setInvoiceBill(adjNo);
    			invLog.setLocationId(locationId);
    			invLog.setSkuId(skuId);
    			invLog.setBatchNo(batchNo);
    			invLog.setQty(qty);
    			invLog.setSkuStatus(paramStock.getSkuStatus());
    			//需求定为：保存导入时的重量体积
    			invLog.setVolume(paramStock.getStockVolume());
    			invLog.setWeight(paramStock.getStockWeight());
    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
    			//保存库存日志
            	this.insertLog(invLog, loginUser);

            	//辅助系统
				MessageData data = new MessageData(location.getLocationNo(), location.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(Math.abs(qty), 0), sku.getMeasureUnit(), "I");
				//入库
//				context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjust.getAdjustId(), false, data);
			} else {
    			//保存未生效调账单
    			this.adjustService.add(adjustVo);
			}
		} else {
			//非直接新增库存，可能修改可能删除可能删除并新增
			InvStock stock = this.stockDao.selectByPrimaryKey(stockId);
			String skuId = stock.getSkuId();
			locationId = stock.getLocationId();
			location = locationService.findLocById(locationId);
			Double stockQty = stock.getStockQty();
			String packId = stock.getPackId();
	    	String oldBatchNo = stock.getBatchNo();
	    	Double pickQty = stock.getPickQty();
	    	MetaSku sku = skuService.get(skuId);
			if (sku == null) {
				throw new BizException("err_stock_sku_null");
			}
//	    	location = FqDataUtils.getLocById(locationService, locationId);
			//判断批次号，如果批次号不同，则需新增1个调账单2条详情，2个库存日志
	    	if (StringUtil.equals(oldBatchNo, batchNo)) {
	    		//批次号相同，只调整数量
	    		if (stockQty == qty) {
	        		//数量相同，不进行任何调整
	    		} else {
	    			//如果调整数量小于预分配数量，不允许调整
	    			if (qty < pickQty) {
	    				throw new BizException("err_stock_change_pick");
	    			}
	    			//准备调账单字段
	    			InvAdjustVO adjustVo = new InvAdjustVO();
	    			InvAdjust adjust = new InvAdjust();
	    			adjustVo.setInvAdjust(adjust);
	    			if (location.getLocationType() == Constant.LOCATION_TYPE_BAD) {
		    			adjust.setDataFrom(Constant.ADJUST_DATE_FROM_BAD_CHANGE);
	    			} else {
		    			adjust.setDataFrom(Constant.ADJUST_DATE_FROM_CHANGE);
	    			}
	    			adjust.setAdjustStatus(Constant.STATUS_OPEN);
	    			adjust.setNote(note);
	    			List<InvAdjustDetailVO> adjDetailVoList = new ArrayList<InvAdjustDetailVO>();
	    			adjustVo.setAdjDetailVoList(adjDetailVoList);
	    			InvAdjustDetailVO adjDetailVo = new InvAdjustDetailVO();
	    			adjDetailVoList.add(adjDetailVo);
	    			InvAdjustDetail adjDetail = new InvAdjustDetail();
	    			adjDetailVo.setInvAdjustDetail(adjDetail);
	    			adjDetail.setAdjustType(qty > stockQty ? Constant.ADJUST_TYPE_IN : Constant.ADJUST_TYPE_OUT);
	    			adjDetail.setStockQty(stockQty);
	    			adjDetail.setDifferenceQty(qty - stockQty);
	    			adjDetail.setRealQty(qty);
	    			adjDetail.setSkuId(skuId);
	    			adjDetail.setLocationId(locationId);
	    			adjDetail.setBatchNo(batchNo);
	    			MessageData data = new MessageData();
		    		data.setLocationNo(location.getLocationNo());
		    		data.setLocationComment(location.getLocationName());
		    		data.setMeasureUnit(sku.getMeasureUnit());
		    		data.setSkuNo(sku.getSkuNo());
	    			boolean isOut = false;
	    			String adjNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getAdjustNo(orgId);
	    			//判断是否调整库存
	    			if (changeStock) {
	    				//调整库存
	    				if (qty == 0) {
	        				//如果库存数量为0，删除该库存
	    		    		this.stockDao.deleteByPrimaryKey(stockId);
	    		    		isOut = true;
	    		    		data.setQty(NumberUtil.rounded(stockQty, 0));
	    				} else {
	        				//其他情况，修改数量
	    					InvStock reqStock = new InvStock();
	    					reqStock.setStockId(stockId);
	    					reqStock.setStockQty(qty);
	    					//修改重量体积
    						reqStock.setStockVolume(NumberUtil.add(stock.getStockVolume() == null ? 0 : stock.getStockVolume(), NumberUtil.mul(qty - stockQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume())));
	    					reqStock.setStockWeight(NumberUtil.add(stock.getStockWeight() == null ? 0 : stock.getStockWeight(), NumberUtil.mul(qty - stockQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight())));
	    					//可能调整库存货品状态
	    					reqStock.setSkuStatus(paramStock.getSkuStatus());
	    					this.stockDao.updateByPrimaryKeySelective(reqStock);
	    					isOut = (qty - stockQty)>0?false:true;
	    					data.setQty(NumberUtil.rounded(Math.abs(qty-stockQty), 0));
	    				}
	    				//调整库容
	    	    		this.locationService.addCapacity(skuId, locationId, packId, qty - stockQty, false);
	    				//调整调账单状态为生效
	        			adjust.setAdjustStatus(Constant.STATUS_ACTIVE);
	        			//保存调账单
	        			this.adjustService.add(adjustVo);
	        			//准备库存日志字段
	        			InvLog invLog = new InvLog();
	        			invLog.setNote(note);
	        			invLog.setOpPerson(loginUser.getUserId());
	        			invLog.setLogType(Constant.STOCK_LOG_TYPE_CHANGE);
	        			invLog.setInvoiceBill(adjNo);
	        			invLog.setLocationId(locationId);
	        			invLog.setSkuId(skuId);
	        			invLog.setBatchNo(batchNo);
	        			invLog.setQty(qty - stockQty);
	        			invLog.setVolume(NumberUtil.mul(qty - stockQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
	        			invLog.setWeight(NumberUtil.mul(qty - stockQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
	        			invLog.setOpType(qty > stockQty ? Constant.STOCK_LOG_OP_TYPE_IN: Constant.STOCK_LOG_OP_TYPE_OUT);
	        			//保存库存日志
	                	this.insertLog(invLog, loginUser);

	                	//推送辅助系统
	    				//入库
	    				if(isOut) {
//	    					context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjust.getAdjustId(), true, data);
	    				} else {
//	    					context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjust.getAdjustId(), false, data);
	    				}
	    			} else {
	        			//保存未生效调账单
	        			this.adjustService.add(adjustVo);
	    			}
	    		}
	    	} else {
	    		//批次号不同，新增调账单1个减少货品详单1个增加货品详单，1个新增库存日志,1个减少库存日志
	    		//判断数量是否为0
	    		if (qty == 0) {
	    			//用户输入错误，不进行任何操作
	    			throw new BizException("err_stock_change_param_qty");
	    		}
	    		//判断是否为暂存区，暂存区不允许修改批次
				if (location.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
					throw new BizException("err_stock_change_batch_location");
				}
	    		//先判断库存是否有相同记录
				InvStock findStock = this.view(skuId, locationId, batchNo, null);
				if (findStock != null) {
					throw new BizException("err_stock_change_same");
				}
	    		
	    		//准备调账单字段(新增)
	    		InvAdjustVO adjustVo = new InvAdjustVO();
				InvAdjust adjust = new InvAdjust();
				adjustVo.setInvAdjust(adjust);
				if (location.getLocationType() == Constant.LOCATION_TYPE_BAD) {
	    			adjust.setDataFrom(Constant.ADJUST_DATE_FROM_BAD_CHANGE);
    			} else {
	    			adjust.setDataFrom(Constant.ADJUST_DATE_FROM_CHANGE);
    			}
				adjust.setAdjustStatus(Constant.STATUS_OPEN);
				adjust.setNote(note);
				List<InvAdjustDetailVO> adjDetailVoList = new ArrayList<InvAdjustDetailVO>();
				adjustVo.setAdjDetailVoList(adjDetailVoList);
    			//调账单(详情)属性改为(新增)
				InvAdjustDetailVO adjDetailVo1 = new InvAdjustDetailVO();
				adjDetailVoList.add(adjDetailVo1);
				InvAdjustDetail adjDetail1 = new InvAdjustDetail();
				adjDetailVo1.setInvAdjustDetail(adjDetail1);
				adjDetail1.setAdjustType(Constant.ADJUST_TYPE_IN);
				adjDetail1.setStockQty(0.0);
				adjDetail1.setDifferenceQty(qty);
				adjDetail1.setRealQty(qty);
				adjDetail1.setSkuId(skuId);
				adjDetail1.setLocationId(locationId);
				adjDetail1.setBatchNo(batchNo);
    			//调账单(详情)属性改为(删除)
				InvAdjustDetailVO adjDetailVo2 = new InvAdjustDetailVO();
				adjDetailVoList.add(adjDetailVo2);
				InvAdjustDetail adjDetail2 = new InvAdjustDetail();
				adjDetailVo2.setInvAdjustDetail(adjDetail2);
				adjDetail2.setAdjustType(Constant.ADJUST_TYPE_OUT);
				adjDetail2.setStockQty(stockQty);
				adjDetail2.setDifferenceQty(-stockQty);
				adjDetail2.setRealQty(0.0);
				adjDetail2.setSkuId(skuId);
				adjDetail2.setLocationId(locationId);
				adjDetail2.setBatchNo(oldBatchNo);
    			MessageData data = new MessageData();
	    		data.setLocationNo(location.getLocationNo());
	    		data.setLocationComment(location.getLocationName());
	    		data.setMeasureUnit(sku.getMeasureUnit());
	    		data.setSkuNo(sku.getSkuNo());
    			boolean isOut = false;
				String adjNo = context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getAdjustNo(orgId);
				adjust.setAdjustNo(adjNo);
				if (changeStock) {
	    			//调整库存，修改批次及数量为新值			
	    			InvStock reqStock = new InvStock();
					reqStock.setStockId(stockId);
					reqStock.setBatchNo(batchNo);
					reqStock.setStockQty(qty);
					this.stockDao.updateByPrimaryKeySelective(reqStock);
					isOut = (qty - stockQty)>0?false:true;
					data.setQty(NumberUtil.rounded(Math.abs(qty-stockQty), 0));
					//如果数量有变，调整库容
	    			if (stockQty != qty) {
	    	    		this.locationService.addCapacity(skuId, locationId, packId, qty - stockQty, false);
	    			}
	    			//调整调账单(新增)状态为生效
	    			adjust.setAdjustStatus(Constant.STATUS_ACTIVE);
	        		//保存调账单
	    			this.adjustService.add(adjustVo);
	    			//准备库存日志字段(新增)
	    			InvLog invLog = new InvLog();
	    			invLog.setNote(note);
	    			invLog.setOpPerson(loginUser.getUserId());
	    			invLog.setLogType(Constant.STOCK_LOG_TYPE_CHANGE);
	    			invLog.setInvoiceBill(adjNo);
	    			invLog.setLocationId(locationId);
	    			invLog.setSkuId(skuId);
	    			invLog.setBatchNo(batchNo);
	    			invLog.setQty(qty);
//	    			MetaSku sku = skuService.get(skuId);
        			invLog.setVolume(NumberUtil.mul(qty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
        			invLog.setWeight(NumberUtil.mul(qty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
	    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
	    			//保存库存日志
	            	this.insertLog(invLog, loginUser);
	        		//准备库存日志字段(新增)改为(删除)
	    			invLog.setInvoiceBill(adjNo);
	    			invLog.setQty(-stockQty);
        			invLog.setVolume(NumberUtil.mul(-stockQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
        			invLog.setWeight(NumberUtil.mul(-stockQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
	    			invLog.setBatchNo(oldBatchNo);
	    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
	    			//保存库存日志
	            	this.insertLog(invLog, loginUser);

                	//推送辅助系统
    				//入库
    				if(isOut) {
//    					context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjust.getAdjustId(), true, data);
    				} else {
//    					context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjust.getAdjustId(), false, data);
    				}
				} else {
	    			//保存未生效调账单1
	    			this.adjustService.add(adjustVo);
	    		}
	    	}
		}
	}

	/**
	 * 库存入库
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月27日 下午4:37:04<br/>
	 * @author 王通<br/>
	 */
    @Override
    @Transactional(readOnly=false)
	public void importStock(MultipartFile fileLicense) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	// 验证是否已经有库存，如果有库存则不允许导入
    	InvStockVO stockVO = new InvStockVO();
    	stockVO.setInvStock(new InvStock());
    	if (!this.list(stockVO).isEmpty()) {
    		throw new BizException("err_stock_is_not_empty");
    	}
    	if (fileLicense == null) {
    		throw new BizException("parameter_is_null");
    	}
    	File newFile = FileUtil.createNewFile(fileLicense);
    	List<InvStockVO> stockVoList = null;
    	if ( newFile != null ) {
    		try {
				stockVoList = new ArrayList<InvStockVO>();
				List<Object> listImport = ExcelUtil.parseExcel("stock", newFile.getAbsolutePath());
				for (int i = 0; i < listImport.size(); i++) {
					Object obj = listImport.get(i);
					if (obj != null) {
						stockVoList.add((InvStockVO) obj);
					} else {
						InvStockVO invStockVO = new InvStockVO();
						invStockVO.setInvStock(new InvStock());
						stockVoList.add(invStockVO);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				// 删除文件
				newFile.delete();
			}
    	} else {
    		throw new BizException("err_stock_imp_no_file");
    	}
    	String ret = "";
    	if ( PubUtil.isEmpty(stockVoList) ) {
    		throw new BizException("err_stock_imp_no_date");
    	}
    	Map<String,MetaSku> skuMap = new HashMap<String,MetaSku>();
    	Map<String,MetaLocation> locationNameMap = new HashMap<String,MetaLocation>();
    	
    	List<Integer> repeatIndexList = new ArrayList<Integer>();
		for (int i = 0; i < stockVoList.size(); i++) {
			if (repeatIndexList.contains(i)) {
				continue;
			}
    		boolean currect = true;
    		String lineResult = "";
			InvStockVO invStockVO = stockVoList.get(i);
			InvStock invStock = invStockVO.getInvStock();
			if (invStock == null) {
				currect = false;
				lineResult = "行" + (i + 2) + "无数据!\r\n<br />";
    			ret += lineResult;
    			continue;
			}
			String skuNo = invStockVO.getSkuNo();
			String locationName = invStockVO.getLocationName();
			String batchNo = invStock.getBatchNo();
			if (StringUtil.isTrimEmpty(batchNo)) {
				batchNo = "";
				invStock.setBatchNo("");
			}
    		String skuStatusName = invStockVO.getSkuStatusName();
    		String measureUnit = invStockVO.getMeasureUnit();
    		Double stockQty = invStock.getStockQty();
			//检测是否信息不全，即无效
    		if (StringUtil.isTrimEmpty(skuNo)) {
    			currect = false;
    			lineResult += "'货品代码'不能为空！";
    		}
    		if (StringUtil.isTrimEmpty(skuStatusName)) {
    			currect = false;
    			lineResult += "'库存状态'不能为空！";
    		}
    		if (StringUtil.isTrimEmpty(locationName)) {
    			currect = false;
    			lineResult += "'库位名称'不能为空！";
    		}
    		if (StringUtil.isTrimEmpty(measureUnit)) {
    			currect = false;
    			lineResult += "'计量单位'不能为空！";
    		}
    		if (stockQty == null) {
    			currect = false;
    			lineResult += "'库存数量'不能为空！";
    		} else if (stockQty < 1) {
    			currect = false;
    			lineResult += "'库存数量'不能小于1！";
    		}
    		if (!currect) {
    			lineResult = "行" + (i + 2) + ":" + lineResult + "\r\n<br />";
    			ret += lineResult;
    			continue;
    		}
        	//检查是否有无效或重复数据，并提示
			//依次查找后续对象是否有重复
			for (int j = i+1; j < stockVoList.size(); j++) {
				InvStockVO invStockVO2 = stockVoList.get(j);
				InvStock invStock2 = invStockVO2.getInvStock();
				if (invStock2 == null) {
					continue;
				}
				String skuNo2 = invStockVO2.getSkuNo();
				String locationName2 = invStockVO2.getLocationName();
				String batchNo2 = invStock2.getBatchNo();
				if (StringUtil.isTrimEmpty(batchNo2)) {
					batchNo2 = "";
				}
    			//判断重复
    			if (StringUtil.equals(skuNo,skuNo2) && StringUtil.equals(locationName,locationName2) && StringUtil.equals(batchNo,batchNo2)) {
        			currect = false;
        			lineResult += "与行" + (j + 2) + "数据重复！";
        			repeatIndexList.add(j);
    			}
			}
			//开始库存导入
			InvStockVO imptInvStockVO = stockVoList.get(i);
    		InvStock impStock = imptInvStockVO.getInvStock();
    		//设置入库数量
    		imptInvStockVO.setFindNum(stockQty);
    		//找到库位id
    		MetaLocation location = locationNameMap.get(locationName);
    		if (location == null) {
    			MetaLocationVO locationVo = new MetaLocationVO();
    			MetaLocation metaLocation = new MetaLocation();
    			metaLocation.setLocationName(locationName);
    			locationVo.setLocation(metaLocation);
    			List<MetaLocation> locationList = this.locationService.listLocByExample(locationVo);
    			if (locationList != null && !locationList.isEmpty()) {
    				if (locationList.size() > 1) {
            			currect = false;
            			lineResult += "根据'库位名称'查询库位有多条记录！";
    				} else {
    					MetaLocation locationRet = locationList.get(0);
	    				//判断库位是否在暂存区，如果在暂存区，禁止导入
	    				if (locationRet.getLocationType() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
							currect = false;
							lineResult += "库存导入时，不能输入暂存区!";
			    			lineResult = "行" + (i + 2) + ":" + lineResult + "\r\n<br />";
			    			ret += lineResult;
			    			continue;
						} else {
							location = locationRet;
		    				locationNameMap.put(locationName, location);
		    				impStock.setLocationId(location.getLocationId());
						}
    				}
    			} else {
        			currect = false;
        			lineResult += "根据'库位名称'查询库位失败！";
    			}
    		} else {
    			impStock.setLocationId(location.getLocationId());
    		}
			impStock.setOrgId(loginUser.getOrgId());
			impStock.setWarehouseId(LoginUtil.getWareHouseId());
    		//找到货品id,根据货品编号
    		MetaSku sku = skuMap.get(skuNo);
    		if (sku == null) {
    			MetaSku skuEntity = new MetaSku();
    			skuEntity.setSkuNo(skuNo);
//    			skuEntity.setMeasureUnit(measureUnit);
    			MetaSku skuRet = this.skuService.query(skuEntity);
    			if (skuRet != null) {
    				if (StringUtil.equals(skuRet.getMeasureUnit(), measureUnit)) {
    					sku = skuRet;
        				skuMap.put(skuNo, sku);
        				//设置库存货品的重量体积
        				impStock.setStockVolume(NumberUtil.mul(impStock.getStockQty(), skuRet.getPerVolume() == null ? 0 : skuRet.getPerVolume()));
        				impStock.setStockWeight(NumberUtil.mul(impStock.getStockQty(), skuRet.getPerWeight() == null ? 0 : skuRet.getPerWeight()));
    				} else {
    	    			currect = false;
            			lineResult += "'计量单位'输入错误！";
    				}
				} else {
	    			currect = false;
	    			lineResult += "根据'货品代码'查询货品失败！";
    			}
    		}
			//加入校验，不同货主的库位货品不能导入
    		if (currect) {
    			if (!sku.getOwner().equals(location.getOwner())) {
    				currect = false;
        			lineResult += "'库位名称'货主与'货品代码'货主不一致！";
    			}
    		}
			impStock.setSkuId(sku.getSkuId());
    		//找到货品状态编码，根据货品状态中文
			String skuStatus = paramService.getValue(CacheName.SKU_STATUS_REVERSE, skuStatusName);
			if (skuStatus == null) {
    			currect = false;
    			lineResult += "根据'货品状态'查询货品状态代码失败！";
			}
			impStock.setSkuStatus(Integer.parseInt(skuStatus));
			if (currect) {
				//字段校验完毕，开始入库操作
				InvLog invLog = new InvLog();
				invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
				invLog.setLogType(Constant.STOCK_LOG_TYPE_IMPORT);
				invLog.setLocationId(location.getLocationId());
				invLog.setSkuId(sku.getSkuId());
				invLog.setBatchNo(batchNo);
				invLog.setOpPerson(loginUser.getUserId());
				invLog.setInvoiceBill("库存导入");
				invLog.setQty(impStock.getStockQty());
				invLog.setVolume(impStock.getStockVolume());
				invLog.setWeight(impStock.getStockWeight());
				imptInvStockVO.setInvLog(invLog);
				this.inStock(imptInvStockVO);
			} else {
    			lineResult = "行" + (i + 2) + "：" + lineResult + "\n\r<br />";
    			ret += lineResult;
			}
		}
		if (!StringUtil.isTrimEmpty(ret)) {
			//这里是为了回滚事物，不提交有失败的数据
			throw new BizException("err_import_stock_msg", ret);
		}
	}

	/**
	 * 内部调用，查询单个库存
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月7日 上午11:24:06<br/>
	 * @author 王通<br/>
	 */
	@Override
	public InvStock view(String id) {
		return this.stockDao.selectByPrimaryKey(id);
	}

	/**
	 * 内部调用，查询库位动碰数量
	 * @param locationId
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 上午9:29:39<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Integer countLocationTouch(String locationId, Date startDate, Date endDate) throws Exception {
		Example example = new Example(InvLog.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("locationId", locationId);
		criteria.andBetween("createTime", startDate, endDate);
		int invLogCount = stockLogDao.selectCountByExample(example);
		return invLogCount;
	}

	/**
	 * 库存预警列表
	 * @param stockVo
	 * @return
	 * @required 
	 * @optional  currentPage,pageSize,owner,locationName,skuNo,skuStatus,isFreeze
	 * @Description 
	 * @version 2017年3月13日 下午4:21:29<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public Page<InvWarnVO> warnList(InvWarnVO warnVo) throws Exception {
    	// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Page<InvWarnVO> page = null;
    	if (warnVo == null) {
    		warnVo = new InvWarnVO();
		}
    	// 备用字段
		warnVo.setLocationName(StringUtil.likeEscapeH(warnVo.getLocationName()));
		warnVo.setSkuNo(StringUtil.likeEscapeH(warnVo.getSkuNo()));
		warnVo.setOwner(StringUtil.likeEscapeH(warnVo.getOwner()));
		warnVo.setSkuName(StringUtil.likeEscapeH(warnVo.getSkuName()));
		
		warnVo.setWarehouseId(LoginUtil.getWareHouseId());
		warnVo.setOrgId(loginUser.getOrgId());
    	page = PageHelper.startPage(warnVo.getCurrentPage()+1, warnVo.getPageSize());
    	// 原计划是查询所有库存货品
//    	this.stockDao.selectWarnCountAll(warnVo);
    	// 修改方案是只列出预警的货品
//    	this.stockDao.selectWarnCount(warnVo);
    	//最新修改方案列出所有生效货品及对应的库存（只列出预警）
    	this.stockDao.selectWarnFromSku(warnVo);
		return page;
	}

	/**
	 * 预分配详情
	 * @param id
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、检查移位单预分配详情
	 * 2、检查拣货单预分配详情
	 * 3、检查上架单预分配详情
	 * @version 2017年3月20日 下午2:49:48<br/>
	 * @author 王通<br/>
	 */
	@Override
	public InvStockVO outLockDetail(String id) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		InvStockVO stockVo = new InvStockVO();
		InvStock stock = this.stockDao.selectByPrimaryKey(id);
		stockVo.setInvStock(stock);
		String locationId = stock.getLocationId();
		String skuId = stock.getSkuId();
		String batchNo = stock.getBatchNo();
		MetaSku sku = this.skuService.get(skuId);
		if (sku == null) {
			throw new BizException("err_stock_imp_sku", skuId);
		}
		String skuName = sku.getSkuName();
		String measureUnit = sku.getMeasureUnit();
		stockVo.setSkuName(skuName);
		//检查移位单预分配详情
		List<InvOutLockDetailVO> outLockDetailList = this.shiftService.getOutLockDetail(locationId, skuId, batchNo);
		stockVo.setOutLockDetailList(outLockDetailList);
		//检查拣货单预分配详情
		SendPickDetailVo sendVo = new SendPickDetailVo();
		List<SendPickLocationVo> planPickLocations = new ArrayList<SendPickLocationVo>();
		SendPickLocationVo locVo = new SendPickLocationVo();
		planPickLocations.add(locVo);
		SendPickLocation sendPickLocation = new SendPickLocation();
		locVo.setSendPickLocation(sendPickLocation);
		sendPickLocation.setLocationId(locationId);
		sendPickLocation.setBatchNo(batchNo);
		sendVo.setPlanPickLocations(planPickLocations);
		sendVo.getSendPickDetail().setSkuId(skuId);
		List<SendPickVo>  retVo = this.pickService.qryPicksByPlanLocation(sendVo);
		if (retVo == null) retVo = new ArrayList<SendPickVo>();
		for (SendPickVo sendPickVo : retVo) {
			InvOutLockDetailVO invOutLockDetailVO = new InvOutLockDetailVO();
			outLockDetailList.add(invOutLockDetailVO);
			invOutLockDetailVO.setBill(sendPickVo.getSendPick().getPickNo());
			invOutLockDetailVO.setBillType("拣货单");
			invOutLockDetailVO.setBillStatus(paramService.getValue(CacheName.STATUS, sendPickVo.getSendPick().getPickStatus()));
			invOutLockDetailVO.setOutLockQty(sendPickVo.getSendPick().getPlanPickQty());
			invOutLockDetailVO.setMeasureUnit(measureUnit);
		}
		//检查上架单预分配详情
		List<RecPutawayVO> putList = this.putawayExtlService.listPtwByStock(locationId, skuId, batchNo, Constant.STATUS_ACTIVE, Constant.STATUS_WORKING);
		for (RecPutawayVO put : putList) {
			InvOutLockDetailVO invOutLockDetailVO = new InvOutLockDetailVO();
			outLockDetailList.add(invOutLockDetailVO);
			invOutLockDetailVO.setBill(put.getPutaway().getPutawayNo());
			invOutLockDetailVO.setBillType("上架单");
			invOutLockDetailVO.setBillStatus(paramService.getValue(CacheName.STATUS,put.getPutaway().getPutawayStatus()));
			invOutLockDetailVO.setOutLockQty(put.getSumQty());
			invOutLockDetailVO.setMeasureUnit(measureUnit);
		}
		return stockVo;
	}

	/**
	 * 预分配重算
	 * @param id
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、检查拣货单预分配详情
	 * 2、检查移位单预分配详情
	 * 3、检查上架单预分配详情
	 * 4、更新预分配数量
	 * @version 2017年3月20日 下午2:49:48<br/>
	 * @author 王通<br/>
	 * @return 
	 * @throws Exception 
	 */
	@Override
    @Transactional(readOnly=false)
	public Double refreshOutLock(String id) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Double totalQty = 0.0;
		InvStock stock = this.stockDao.selectByPrimaryKey(id);
		String locationId = stock.getLocationId();
		String skuId = stock.getSkuId();
		String batchNo = stock.getBatchNo();
		MetaSku sku = this.skuService.get(skuId);
		if (sku == null) {
			throw new BizException("err_stock_imp_sku", skuId);
		}
		//检查移位单预分配详情
		List<InvOutLockDetailVO> outLockDetailList = this.shiftService.getOutLockDetail(locationId, skuId, batchNo);
		for (InvOutLockDetailVO outLockDetail : outLockDetailList) {
			totalQty += outLockDetail.getOutLockQty();
		}
		//检查拣货单预分配详情
		SendPickDetailVo sendVo = new SendPickDetailVo();
		List<SendPickLocationVo> planPickLocations = new ArrayList<SendPickLocationVo>();
		SendPickLocationVo locVo = new SendPickLocationVo();
		planPickLocations.add(locVo);
		SendPickLocation sendPickLocation = new SendPickLocation();
		locVo.setSendPickLocation(sendPickLocation);
		if (StringUtil.isTrimEmpty(batchNo)) {
			locVo.setBatchNoIsNull(true);
		}
		sendPickLocation.setLocationId(locationId);
		sendPickLocation.setBatchNo(batchNo);
		sendVo.setPlanPickLocations(planPickLocations);
		sendVo.getSendPickDetail().setSkuId(skuId);
		List<SendPickVo>  retVo = this.pickService.qryPicksByPlanLocation(sendVo);
		if (retVo != null) {
			for (SendPickVo sendPickVo : retVo) {
				totalQty += sendPickVo.getSendPick().getPlanPickQty();
			}
		}
		//检查上架单预分配详情
		List<RecPutawayVO> putList = this.putawayExtlService.listPtwByStock(locationId, skuId, batchNo, Constant.STATUS_ACTIVE, Constant.STATUS_WORKING);
		if (putList != null) {
			for (RecPutawayVO put : putList) {
				totalQty += put.getSumQty();
			}
		}
		//更新预分配数量
		InvStock updateStock = new InvStock();
		updateStock.setStockId(id);
		updateStock.setPickQty(totalQty);
		this.stockDao.updateByPrimaryKeySelective(updateStock);
		return totalQty;
	}

	/**
	 * 冻结
	 * @param vo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月29日 上午11:07:05<br/>
	 * @author 王通<br/>
	 */
	@Override
    @Transactional(readOnly=false)
	public void freeze(InvStockVO vo) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Integer freezeType = vo.getFreezeType();
		if (freezeType == null) {
			throw new BizException("valid_common_data_empty");
		}
		//判断冻结类型 0-库位 1-货品 2-货品状态
		switch (freezeType) {
			case 0:
				String locationName = vo.getLocationName();
				if (StringUtil.isTrimEmpty(locationName)) {
					throw new BizException("err_stock_imp_location");
				}
				MetaLocationVO locationVo = new MetaLocationVO();
    			MetaLocation metaLocation = new MetaLocation();
    			metaLocation.setLocationName(locationName);
    			locationVo.setLocation(metaLocation);
    			MetaLocation locationRet = this.locationService.findLoc(locationVo);
    			if (locationRet != null) {
    				String locationId = locationRet.getLocationId();
    				//冻结库位
    				this.locationService.blockLoc(locationId, 1);
    				//冻结库存
    				Example example = new Example(InvStock.class);
    				Criteria criteria = example.createCriteria();
    				criteria.andEqualTo("locationId", locationId);
    				InvStock record = new InvStock();
    				record.setLocationId(locationId);
    				record.setIsBlock(1);
    				this.stockDao.updateByExampleSelective(record, example);
    			} else {
    				throw new BizException("err_stock_imp_location");
    			}
				break;
			case 1:
				if (vo.getInvStock() == null) {
					throw new BizException("valid_common_data_empty");
				}
				String skuId = vo.getInvStock().getSkuId();
				if (StringUtil.isTrimEmpty(skuId)) {
					throw new BizException("err_stock_imp_sku");
				}
				String batchNo = vo.getInvStock().getBatchNo();
				Boolean containBatch = vo.getContainBatch();
				MetaSku skuRet = this.skuService.get(skuId);
				if (skuRet == null) {
					throw new BizException("err_stock_imp_sku");
				} else {
					//冻结库存
					Example example = new Example(InvStock.class);
					Criteria criteria = example.createCriteria();
					criteria.andEqualTo("skuId", skuId);
					if (containBatch) {
						criteria.andEqualTo("batchNo", batchNo);
					}
					InvStock record = new InvStock();
					record.setSkuId(skuId);
					record.setIsBlock(1);
					this.stockDao.updateByExampleSelective(record, example);
				}
				break;
			case 2:
				if (vo.getInvStock() == null) {
					throw new BizException("valid_common_data_empty");
				}
				Integer skuStatus = vo.getInvStock().getSkuStatus();
				if (skuStatus == null) {
					throw new BizException("valid_common_data_empty");
				} else {
					//冻结库存
					Example example = new Example(InvStock.class);
					Criteria criteria = example.createCriteria();
					criteria.andEqualTo("skuStatus", skuStatus);
					InvStock record = new InvStock();
					record.setSkuStatus(skuStatus);
					record.setIsBlock(1);
					this.stockDao.updateByExampleSelective(record, example);
				}
				break;
			default:
				throw new BizException("valid_common_data_empty");
		}
	}

	/**
	 * @param vo
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月29日 上午11:07:05<br/>
	 * @author 王通<br/>
	 */
	@Override
    @Transactional(readOnly=false)
	public void unfreeze(InvStockVO vo) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Integer freezeType = vo.getFreezeType();
		if (freezeType == null) {
			throw new BizException("valid_common_data_empty");
		}
		//判断冻结类型 0-库位 1-货品 2-货品状态
		switch (freezeType) {
			case 0:
				String locationName = vo.getLocationName();
				if (StringUtil.isTrimEmpty(locationName)) {
					throw new BizException("err_stock_imp_location");
				}
				MetaLocationVO locationVo = new MetaLocationVO();
    			MetaLocation metaLocation = new MetaLocation();
    			metaLocation.setLocationName(locationName);
    			locationVo.setLocation(metaLocation);
    			MetaLocation locationRet = this.locationService.findLoc(locationVo);
    			if (locationRet != null) {
    				String locationId = locationRet.getLocationId();
    				//解冻库位
    				this.locationService.blockLoc(locationId, 0);
    				//解冻库存
    				Example example = new Example(InvStock.class);
    				Criteria criteria = example.createCriteria();
    				criteria.andEqualTo("locationId", locationId);
    				InvStock record = new InvStock();
    				record.setLocationId(locationId);
    				record.setIsBlock(0);
    				this.stockDao.updateByExampleSelective(record, example);
    			} else {
    				throw new BizException("err_stock_imp_location");
    			}
				break;
			case 1:
				if (vo.getInvStock() == null) {
					throw new BizException("valid_common_data_empty");
				}
				String skuId = vo.getInvStock().getSkuId();
				if (StringUtil.isTrimEmpty(skuId)) {
					throw new BizException("err_stock_imp_sku");
				}
				MetaSku skuRet = this.skuService.get(skuId);
				String batchNo = vo.getInvStock().getBatchNo();
				Boolean containBatch = vo.getContainBatch();
				if (skuRet == null) {
					throw new BizException("err_stock_imp_sku");
				} else {
					//解冻库存
					Example example = new Example(InvStock.class);
					Criteria criteria = example.createCriteria();
					criteria.andEqualTo("skuId", skuId);
					if (containBatch) {
						criteria.andEqualTo("batchNo", batchNo);
					}
					InvStock record = new InvStock();
					record.setSkuId(skuId);
					record.setIsBlock(0);
					this.stockDao.updateByExampleSelective(record, example);
				}
				break;
			case 2:
				if (vo.getInvStock() == null) {
					throw new BizException("valid_common_data_empty");
				}
				Integer skuStatus = vo.getInvStock().getSkuStatus();
				if (skuStatus == null) {
					throw new BizException("valid_common_data_empty");
				} else {
					//解冻库存
					Example example = new Example(InvStock.class);
					Criteria criteria = example.createCriteria();
					criteria.andEqualTo("skuStatus", skuStatus);
					InvStock record = new InvStock();
					record.setSkuStatus(skuStatus);
					record.setIsBlock(0);
					this.stockDao.updateByExampleSelective(record, example);
				}
				break;
			default:
				throw new BizException("valid_common_data_empty");
		}
	}
	
	public static void main(String[] args) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = "{\"locationName\":\"库位100\",\"inLocationName\":\"\",\"skuName\":\"\",\"specModel\":\"\",\"measureUnit\":\"\",\"findNum\":null,\"freezeType\":0,\"invStock\":{\"stockId\":\"\",\"batchNo\":\"\",\"skuStatus\":null,\"stockQty\":null,\"skuId\":\"\",\"locationId\":\"\"},\"invLog\":{\"note\":\"\",\"opPerson\":\"\"}}";
//		InvStockVO readValue = null;
//		try {
//			readValue = objectMapper.readValue(json, InvStockVO.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			System.out.println(objectMapper.writeValueAsString(readValue));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 根据盘点条件获取库存
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月1日 上午9:45:19<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public List<InvStockVO> listByCount(InvCountVO vo) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if (vo == null || vo.getInvCount() == null) {
			throw new BizException("err_stock_param_null");
		}
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	vo.getInvCount().setOrgId(orgId);
    	vo.getInvCount().setWarehouseId(LoginUtil.getWareHouseId());
    	// 判断查询条件是否对应
    	Integer countType = vo.getInvCount().getCountType();
    	if (countType == null) {
			throw new BizException("err_param_count_type");
    	}
    	switch (countType) {
    	case Constant.COUNT_TYPE_ALL:
    		break;
    	case Constant.COUNT_TYPE_SKU:
    		if (StringUtil.isTrimEmpty(vo.getInvCount().getSkuId())) {
    			throw new BizException("err_param_sku_null");
    		}
    		break;
    	case Constant.COUNT_TYPE_LOCATION:
    		if (StringUtil.isTrimEmpty(vo.getInvCount().getLocationId())) {
    			throw new BizException("err_param_location_null");
    		}
    		break;
    	case Constant.COUNT_TYPE_CHANGE:
    		break;
    	case Constant.COUNT_TYPE_BAD:
    		break;
		default :
			throw new BizException("err_param_count_type");
    	}
		// 查询库存单列表 
		List<InvStock> stockList = this.stockDao.findStockByCount(vo);
		List<InvStockVO> stockVoList = chg(stockList);
		return stockVoList;
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
	public ResponseEntity<byte[]> download(String fileName, File file) throws Exception {
		return FileUtil.excel2Stream(file.getPath(), fileName);
////		String dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.setContentDispositionFormData("attachment", fileName);
//		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}
//	    HttpHeaders responseHeaders = httpHeaderExcelFileAttachment(fileName,
//	                                    file.length());
//	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
//	                                      responseHeaders, HttpStatus.OK);
//	}
//
//	public static HttpHeaders httpHeaderExcelFileAttachment(final String fileName,
//	        final long fileSize) {
//	    String encodedFileName = fileName.replace('"', ' ').replace(' ', '_');
//
//	    HttpHeaders responseHeaders = new HttpHeaders();
//	    responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
//	    responseHeaders.setContentLength(fileSize);
//	    responseHeaders.set("Content-Disposition", "attachment");
//	    responseHeaders.add("Content-Disposition", "filename=\"" + encodedFileName + '\"');
//	    return responseHeaders;
//	}
	
	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:20:51<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public ResponseEntity<byte[]> downloadStockDemo() throws Exception {
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource("excel/template/stock_template.xls").getPath());
		return download("stock_template.xls", file);
	}

	/**
	 * 库存货品批次数据列表查询
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月28日 下午1:58:40<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Page<InvStockVO> listStockSku(InvStockVO vo) throws Exception {
		// 获取当前用户信息
    		Page<InvStockVO> page = null;
			Principal loginUser = LoginUtil.getLoginUser();
	    	// 验证用户是否登录
	    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
	    		throw new BizException("valid_common_user_no_login");
	    	}
	    	if (vo == null || vo.getInvStock() == null) {
				throw new BizException("err_stock_param_null");
			}
	    	// 设置创建人/修改人/企业/仓库
	    	String orgId = loginUser.getOrgId();
	    	vo.getInvStock().setOrgId(orgId);
	    	vo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
	    	vo.getInvStock().setBatchNo(StringUtil.likeEscapeH(vo.getInvStock().getBatchNo()));
	    	vo.setSkuNo(StringUtil.likeEscapeH(vo.getSkuNo()));
	    	vo.setSkuName(StringUtil.likeEscapeH(vo.getSkuName()));
	    	vo.setOwnerName(StringUtil.likeEscapeH(vo.getOwnerName()));
			// 查询库存单列表 
	    	page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
			
			List<InvStock> stockList = this.stockDao.selectStockSkuBatch(vo);
			List<InvStockVO> stockVoList = chg(stockList);
			page.clear();
			page.addAll(stockVoList);
			return page;
	}
	
	/**
	 * 货品库存查询（对外接口）
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 仅查询暂存区、存储区、拣货区
	 * @version 2017年6月28日 下午1:58:40<br/>
	 * @author 王通<br/>
	 */
	@Override
	public List<InvSkuStockVO> stockSkuCount(List<String> skuNoList) throws Exception {
		List<InvSkuStockVO> stockSkuCountList = new ArrayList<InvSkuStockVO>();
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
//    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
//    		throw new BizException("valid_common_user_no_login");
//    	}
    	if (skuNoList == null || skuNoList.isEmpty()) {
			throw new BizException("err_stock_param_null");
		}
    	List<Integer> locationTypeList = new ArrayList<Integer>();
    	locationTypeList.add(Constant.LOCATION_TYPE_STORAGE);
    	locationTypeList.add(Constant.LOCATION_TYPE_PICKUP);
    	locationTypeList.add(Constant.LOCATION_TYPE_TEMPSTORAGE);
    	for (String skuNo : skuNoList) {
    		InvSkuStockVO skuStockVo = new InvSkuStockVO();
    		InvStockVO stockVo = new InvStockVO();
    		InvStock stock = new InvStock();
    		stockVo.setSkuNo(skuNo);
        	stockVo.setInvStock(stock);
        	String orgId = loginUser.getOrgId();
        	stock.setOrgId(orgId);
        	stock.setWarehouseId(LoginUtil.getWareHouseId());
        	stockVo.setSkuNo(StringUtil.likeEscapeH(skuNo));
        	stockVo.setLocationTypeList(locationTypeList);
        	double qty = getStockQty(stockVo);
        	skuStockVo.setSkuNo(skuNo);
        	skuStockVo.setStockQty(qty);
        	stockSkuCountList.add(skuStockVo);
    	}
    	return stockSkuCountList;
	}

	/**
	 * @param paramVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 上午10:54:51<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Integer countWarnByExample(InvWarnVO warnVo) {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if (warnVo == null) {
    		warnVo = new InvWarnVO();
		}
    	// 备用字段
		warnVo.setWarehouseId(LoginUtil.getWareHouseId());
		warnVo.setOrgId(loginUser.getOrgId());
    	// 原计划是查询所有库存货品
//    	this.stockDao.selectWarnCountAll(warnVo);
    	// 修改方案是只列出预警的货品
//    	this.stockDao.selectWarnCount(warnVo);
    	//最新修改方案列出所有生效货品及对应的库存（只列出预警）
    	List<InvWarnVO> selectWarnFromSku = this.stockDao.selectWarnFromSku(warnVo);
    	if (selectWarnFromSku == null) {
    		return 0;
    	} else {
    		return selectWarnFromSku.size();
    	}
	}
	@Override
	public List<InvWarnVO> countWarnByExample2(InvWarnVO warnVo) {
    	List<InvWarnVO> selectWarnFromSku = this.stockDao.selectWarnFromSku(warnVo);
    	return selectWarnFromSku;
	}

	/**
	 * 销毁货品
	 * @param deliveryId
	 * @required 
	 * @optional  
	 * @Description 
	 * 1、根据发货单查找货品库存
	 * 2、生成已调账状态的调账单
	 * 3、出库操作
	 * @version 2017年9月8日 上午9:37:45<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public void destory(String deliveryId) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
      	// 验证用户是否登录
      	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
      		throw new BizException("valid_common_user_no_login");
      	}
      	//字段校验
      	if (deliveryId == null) {
      		throw new BizException("err_stock_param_null");
      	}
      	//先查出出库库位（发货区第一个库位）
      	MetaLocationVO mateLocationVo = new MetaLocationVO();
  		MetaLocation metaLocation = new MetaLocation();
  		metaLocation.setLocationType(Constant.LOCATION_TYPE_SEND);
  		mateLocationVo.setLocation(metaLocation);
  		MetaLocation locationRet = this.locationService.findLoc(mateLocationVo);
      	String outLocation = "";
  		if (locationRet != null) {
  			outLocation = locationRet.getLocationId();
  		} else {
  			throw new BizException("err_shift_send_location_null");
  		}
      	String opPerson = loginUser.getUserId();
      	String deliveryNo = deliveryDao.selectByPrimaryKey(deliveryId).getDeliveryNo();
      	SendPickVo sendVoParam = new SendPickVo(); 
      	sendVoParam.getSendPick().setDeliveryId(deliveryId);
      	List<SendPickVo> listSendPick = pickService.qryListByParam(sendVoParam);
      	//获取第一条拣货单的拣货明细
      	SendPickVo sendPickVo = pickService.view(listSendPick.get(0).getSendPick().getPickId());
//  		String pickNo = sendPickVo.getSendPick().getPickNo();
      	for (SendPickDetailVo sendDetailVo : sendPickVo.getSendPickDetailVoList()) {
      		//获取拣货明细的实际拣货批次
      		List<SendPickLocationVo> locationVoList = sendDetailVo.getRealPickLocations();
      		String skuId = sendDetailVo.getSendPickDetail().getSkuId();
      		for (SendPickLocationVo locationVo : locationVoList) {
      			SendPickLocation location = locationVo.getSendPickLocation();
      			Double num = location.getPickQty();
      			String batchNo = location.getBatchNo();

    			//预设库存对象
    			InvStockVO outStockVo = new InvStockVO();
    			InvStock stock = new InvStock();
    			outStockVo.setInvStock(stock);
    			stock.setSkuId(skuId);
    			stock.setLocationId(outLocation);
    			stock.setBatchNo(batchNo);
    			outStockVo.setFindNum(num);
    			//预设日志对象
    			InvLog invLog = new InvLog();
    			outStockVo.setInvLog(invLog);
    			invLog.setOpPerson(opPerson);
    			invLog.setLogType(Constant.STOCK_LOG_TYPE_DESTORY);
    			invLog.setInvoiceBill(deliveryNo);
    			invLog.setSkuId(skuId);
    			MetaSku sku = skuService.get(skuId);
//    			Integer stockQty = stock.getStockQty();
//    			//设置体积和重量
//    			Double stockVolume = stock.getStockVolume();
//    			Double changeVolume = 0.0;
//    			if (stockVolume != null && stockVolume != 0.0) {
//    				invLog.setVolume(changeVolume);
//    				changeVolume =  num / (stockQty * stockVolume);
//    				stock.setStockVolume(stockVolume - changeVolume);
//    			}
//    			Double stockWeight = stock.getStockVolume();
//    			Double changeWeight = 0.0;
//    			if (stockWeight != null && stockWeight != 0.0) {
//    				invLog.setWeight(changeWeight);
//    				changeWeight = num / (stockQty * stockWeight);
//    				stock.setStockWeight(stockWeight - changeWeight);
//    			}
    	    	//调整出库内容
    	    	invLog.setQty(-num);
    	    	invLog.setVolume(NumberUtil.mul(-num, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
    			invLog.setWeight(NumberUtil.mul(-num, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    			
    			invLog.setLocationId(outLocation);
    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
    			//出库
    	    	this.outStock(outStockVo);
      		}
      	}
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
	public ResponseEntity<byte[]> downloadExcel(InvStockVO vo) throws Exception {
		//修改排序方式为 货品、批次
	  vo.setResultOrder("t1.sku_id,t1.batch_no");
		// 导出库存  
      ExcelExport<InvStockVO4Excel> ex = new ExcelExport<InvStockVO4Excel>();  
//      String[] headers =  InvStockVO4Excel.headers;
//      String[] headers = { 
//  			"序号", "货主简称", "库区", "库位", 
//  			"收货单号", "货品代码", "货品名称", "批次", 
//  			"规格", "计量单位", "数量/公斤/立方", "预分配数量", 
//  			"状态", "是否冻结", "收货日期"};  
      String[] headers = { 
  			"序号", "货主简称", "库区", "库位", 
  			"收货单号", "货品代码", "货品名称","货品条码","批次", 
  			"规格", "计量单位", "剩余有效期(天)","数量","合计","公斤","立方", "预分配数量", 
  			"状态", "是否冻结", "收货日期"}; 
      List<InvStockVO4Excel> dataset = vo2excelVo(listByPage(vo));
      ResponseEntity<byte[]> bytes = ex.exportExcel2Array("库存清单", headers, dataset, "yyyy-MM-dd", "库存清单.xls");
      return bytes;
	}

	/**
	 * 普通vo转换为excel格式化后vo
	 * @param listByPage
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月23日 下午1:40:02<br/>
	 * @author 王通<br/>
	 */
	private List<InvStockVO4Excel> vo2excelVo(Page<InvStockVO> list) {
		List<InvStockVO4Excel> retList = new ArrayList<InvStockVO4Excel>();
		for (int i = 0; i < list.size(); i++) {
			InvStockVO4Excel vo4excel = new InvStockVO4Excel(list.get(i));
			vo4excel.setIndex(String.valueOf(i+1));
			retList.add(vo4excel);
		}
		//货品合计特殊化处理
		Double nowSkuCount = 0d;
		String lastSkuNo = "";
		int lastIndex = 0;
		for (int i = 0; i < retList.size(); i++) {
			InvStockVO4Excel vo4excel = retList.get(i);
			String skuNo = vo4excel.getSkuNo();
			if (skuNo.equals(lastSkuNo)) {
				Double qty = Double.parseDouble(vo4excel.getStockQty());
				nowSkuCount = NumberUtil.add(nowSkuCount, qty);
				vo4excel.setCountQty("");
			} else {
				retList.get(lastIndex).setCountQty(new BigDecimal(nowSkuCount.toString()).toString());
				nowSkuCount = Double.parseDouble(vo4excel.getStockQty());
				lastIndex = i;
				lastSkuNo = skuNo;
			}
			if (i == (retList.size() - 1)) {
				retList.get(lastIndex).setCountQty(new BigDecimal(nowSkuCount.toString()).toString());
			}
		}
		return retList;
	}
	


	@Override
	public Page<InStockVO> listInStock(InStockVO inStockVO) throws Exception {
		Page<InStockVO> page = PageHelper.startPage(inStockVO.getCurrentPage()+1, inStockVO.getPageSize());
		inStockVO.loginInfo();
		if (StringUtil.isNoneBlank(inStockVO.getLocationNoLike())) {
			inStockVO.setLocationNoLike(StringUtil.likeEscapeH(inStockVO.getLocationNoLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getSkuNameLike())) {
			inStockVO.setSkuNameLike(StringUtil.likeEscapeH(inStockVO.getSkuNameLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getSkuBarCodeLike())) {
			inStockVO.setSkuBarCodeLike(StringUtil.likeEscapeH(inStockVO.getSkuBarCodeLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getSkuNoLike())) {
			inStockVO.setSkuNoLike(StringUtil.likeEscapeH(inStockVO.getSkuNoLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getPoNoLike())) {
			inStockVO.setPoNoLike(StringUtil.likeEscapeH(inStockVO.getPoNoLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getOwnerLike())) {
			inStockVO.setOwnerLike(StringUtil.likeEscapeH(inStockVO.getOwnerLike()));
		}
		if (inStockVO.getInDateStart() != null ) {
			inStockVO.setInDateStart(DateUtil.getStartTime(inStockVO.getInDateStart()));
		}
		if (inStockVO.getInDateEnd() != null ) {
			inStockVO.setInDateEnd(DateUtil.getEndTime(inStockVO.getInDateEnd()));
		}
		this.stockDao.selectInStock(inStockVO);
		return page;
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
	public ResponseEntity<byte[]> downloadInStockExcel(InStockVO vo) throws Exception {
		// 导出入库清单  
		ExcelExport<InStockVO4Excel> ex = new ExcelExport<InStockVO4Excel>();  
		String[] headers = { 
    		  "序号", "库位号","商品名称",
    		  "商品条码","PO单号（源单号）",
    		  "订单日期","收货数量","实际收货数量",
    		  "货主","送货方","联系方式","入库日期"};  
      
		Page<InStockVO> page = listInStock(vo);
		List<InStockVO4Excel> dataset = inStockVO2excelVo(page);
		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("入库明细", headers, dataset, "yyyy-MM-dd", "入库明细.xls");
		return bytes;
	}
	@Override
	public ResponseEntity<byte[]> adjustmentLogExcel(Page<InvLogVO> page) throws Exception {
		// 导出入库清单  
		ExcelExport<InvLogVoExcel> ex = new ExcelExport<InvLogVoExcel>();  
		String[] headers = { 
    		  "序号", "日志类型","创建时间",
    		  "增减方式","货主","相关单号",
    		  "库位名称","货品代码","货品名称",
    		  "批次","包装单位","规格型号","数量/公斤/立方","备注","操作人员"};  
      
		List<InvLogVoExcel> dataset = invLogVo2excelVo(page);
		ResponseEntity<byte[]> bytes = ex.exportExcel2Array("库存调整明细", headers, dataset, "yyyy-MM-dd", "库存调整明细.xls");
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
	private List<InStockVO4Excel> inStockVO2excelVo(Page<InStockVO> list) {
		List<InStockVO4Excel> retList = new ArrayList<InStockVO4Excel>();
		for (int i = 0; i < list.size(); i++) {
			InStockVO vo = list.get(i);
			InStockVO4Excel voExcel = new InStockVO4Excel(vo, i);
			retList.add(voExcel);
		}
		return retList;
	}
	/**
	 * 将日志对象转为excle格式对象
	 * @param list
	 * @return
	 */
	private List<InvLogVoExcel> invLogVo2excelVo(Page<InvLogVO> list) {
		List<InvLogVoExcel> retList = new ArrayList<InvLogVoExcel>();
		for (int i = 0; i < list.size(); i++) {
			InvLogVO vo = list.get(i);
			InvLogVoExcel voExcel = new InvLogVoExcel(vo, i+1);
			retList.add(voExcel);
		}
		return retList;
	}
	@Override
	public TotalVo totalInStock(InStockVO inStockVO) {
		inStockVO.loginInfo();
		if (StringUtil.isNoneBlank(inStockVO.getLocationNoLike())) {
			inStockVO.setLocationNoLike(StringUtil.likeEscapeH(inStockVO.getLocationNoLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getSkuNameLike())) {
			inStockVO.setSkuNameLike(StringUtil.likeEscapeH(inStockVO.getSkuNameLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getSkuNoLike())) {
			inStockVO.setSkuNoLike(StringUtil.likeEscapeH(inStockVO.getSkuNoLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getSkuBarCodeLike())) {
			inStockVO.setSkuBarCodeLike(StringUtil.likeEscapeH(inStockVO.getSkuBarCodeLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getPoNoLike())) {
			inStockVO.setPoNoLike(StringUtil.likeEscapeH(inStockVO.getPoNoLike()));
		}
		if (StringUtil.isNoneBlank(inStockVO.getOwnerLike())) {
			inStockVO.setOwnerLike(StringUtil.likeEscapeH(inStockVO.getOwnerLike()));
		}
		TotalVo vo = this.stockDao.selectTotalInStock(inStockVO);
		return vo;
	}

	@Override
	public Map<String,Object> stockList2ERP(InvStockVO vo,Principal p){
		Map<String,Object>map=new HashMap<>();
		try {
			Page<InvStockVO> page = null;
			vo.setContainTemp(true);//是否包含暂存区- 是
			vo.setOnlyTemp(false);//是否只查暂存区- 否
			String orgId = p.getOrgId();
	    	if (vo.getInvStock() == null) {
	    		vo.setInvStock(new InvStock());
	    	}
	    	vo.getInvStock().setOrgId(orgId);
	    	vo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
	    	// 如果填了批次作为条件，则设含批次为true
	    	if (!StringUtil.isTrimEmpty(vo.getInvStock().getBatchNo())) {
	    		vo.setContainBatch(true);
	    	}
	    	// 包装查询条件 货主\库位\货品
	    	vo.setOwnerName(StringUtil.likeEscapeH(vo.getOwnerName()));
	    	vo.setLocationName(StringUtil.likeEscapeH(vo.getLocationName()));
	    	vo.setSkuNo(StringUtil.likeEscapeH(vo.getSkuNo()));
			// 查询库存单列表 
			page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
			List<InvStock> stockList = this.stockDao.findStockList(vo);
			List<InvStockVO> stockVoList = new ArrayList<InvStockVO>();
			if (stockList != null) {
				for (InvStock stock : stockList) {
					InvStockVO entityVo = new InvStockVO();
					stockVoList.add(entityVo);
					entityVo.setInvStock(stock);
					MetaWarehouse warehouse = warehouseExtlService.findWareHouseById(LoginUtil.getWareHouseId());
					if (warehouse != null) {
						entityVo.setWarehouseNo(warehouse.getWarehouseNo());
						entityVo.setWarehouseName(warehouse.getWarehouseName());
					}
					String locationId = stock.getLocationId();
					MetaLocation location = this.locationService.findLocById(locationId);
					if (location != null) {
						entityVo.setLocationNo(location.getLocationNo());
						entityVo.setLocationName(location.getLocationName());
						entityVo.setLocationTypeName(paramService.getValue(CacheName.AREA_TYPE, location.getLocationType()));
						MetaArea area = this.areaService.findAreaById(location.getAreaId());
						if (area != null) {
							entityVo.setAreaName(area.getAreaName());
						}
					}
					MetaSku sku = this.skuService.get(stock.getSkuId());
					if (sku != null) {
						entityVo.setSkuNo(sku.getSkuNo());
						entityVo.setSkuName(sku.getSkuName());
						entityVo.setSpecModel(sku.getSpecModel());
						entityVo.setMeasureUnit(sku.getMeasureUnit());
						entityVo.setSkuBarCode(sku.getSkuBarCode());
						entityVo.setSku(sku);
						MetaMerchant m =merchantService.get(sku.getOwner());
						if (m != null) {
							entityVo.setHgMerchantNo(m.getHgMerchantNo());
							entityVo.setOwnerName(m.getMerchantName());
							entityVo.setMerchantShortName(m.getMerchantShortName());
						}
					}
					if (!StringUtil.isTrimEmpty(stock.getAsnId())) {
						RecAsn asn = asnService.findAsnById(stock.getAsnId());
						if (asn != null) {
							entityVo.setAsnNo(asn.getAsnNo());
						}
					}
					entityVo.setSkuStatusName(paramService.getValue(CacheName.SKU_STATUS, entityVo.getInvStock().getSkuStatus()));
					entityVo.setIsBlockName(paramService.getValue(CacheName.ISBLOCK, entityVo.getInvStock().getIsBlock()));
				}
			}
			List<StockVo2ERP>list=new ArrayList<>();
			if(!stockVoList.isEmpty()){
				for(InvStockVO invStockVO:stockVoList){
					list.add(ch2ERPVo(invStockVO));
				}
			}
			map.put("status", 1);
			map.put("totalCount", page.getTotal());
			Map<String,Object>map2=new HashMap<>();
			map2.put("list", list);
			map.put("result", map2);
			return map;
		} catch (Exception e) {
			log.debug("ERP查询库存失败:"+e.getMessage());
			map.put("status", 0);
			return map;
		}
		
	}
	private StockVo2ERP ch2ERPVo(InvStockVO invStockVO){
		StockVo2ERP stockVo2ERP=new StockVo2ERP();
		//TODO
		stockVo2ERP.setAreaName(invStockVO.getAreaName());
		stockVo2ERP.setExpiredDate(invStockVO.getInvStock().getExpiredDate());
		stockVo2ERP.setInDate(invStockVO.getInvStock().getInDate());
		stockVo2ERP.setProduceDate(invStockVO.getInvStock().getProduceDate());
		stockVo2ERP.setSkuName(invStockVO.getSkuName());
		stockVo2ERP.setMeasureUnit(invStockVO.getMeasureUnit());
		stockVo2ERP.setSpecModel(invStockVO.getSpecModel());
		stockVo2ERP.setWarehouseNo(invStockVO.getWarehouseNo());
		stockVo2ERP.setWarehouseName(invStockVO.getWarehouseName());
		stockVo2ERP.setOwnerName(invStockVO.getOwnerName());
		stockVo2ERP.setMerchantShortName(invStockVO.getMerchantShortName());
		stockVo2ERP.setSkuName(invStockVO.getSkuStatusName());
		stockVo2ERP.setIsBlockName(invStockVO.getIsBlockName());
		stockVo2ERP.setAreaName(invStockVO.getAreaName());
		stockVo2ERP.setLocationName(invStockVO.getLocationName());
		stockVo2ERP.setLocationName(invStockVO.getLocationTypeName());
		stockVo2ERP.setStockQty(invStockVO.getInvStock().getStockQty());
		return stockVo2ERP;
	}

	@Override
	public List<InvStockVO> listByT(InvStockVO vo, Principal p) throws Exception {
		Page<InvStockVO> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<InvStockVO> list = select(vo, p);
		vo.setPageCount(page.getPages());//返回分页参数
		vo.setTotalCount(page.getTotal());
		return list;
	}

	public List<InvStockVO> select(InvStockVO stockVo, Principal loginUser) throws Exception {
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if (stockVo == null) {
			throw new BizException("err_stock_param_null");
		}
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	if (stockVo.getInvStock() == null) {
    		stockVo.setInvStock(new InvStock());
    	}
    	stockVo.getInvStock().setOrgId(orgId);
    	stockVo.getInvStock().setWarehouseId(LoginUtil.getWareHouseId());
    	// 如果填了批次作为条件，则设含批次为true
    	if (!StringUtil.isTrimEmpty(stockVo.getInvStock().getBatchNo())) {
    		stockVo.setContainBatch(true);
    	}
    	// 包装查询条件 货主\库位\货品
    	stockVo.setOwnerName(StringUtil.likeEscapeH(stockVo.getOwnerName()));
    	stockVo.setLocationName(StringUtil.likeEscapeH(stockVo.getLocationName()));
    	stockVo.setSkuNo(StringUtil.likeEscapeH(stockVo.getSkuNo()));
    	// 设置退货库位
    	MetaLocation location = locationService.findLocByT();
    	stockVo.getInvStock().setLocationId(location.getLocationId());
		// 查询库存单列表 
		List<InvStock> stockList = this.stockDao.findStockList(stockVo);
		List<InvStockVO> stockVoList = chg(stockList);
		return stockVoList;
	}
	private List<InvStockVO> chg(List<InvStock> stockList) throws Exception {
		List<InvStockVO> stockVoList = new ArrayList<InvStockVO>();
		if(stockList == null || stockList.isEmpty()) return stockVoList;
		for (InvStock stock : stockList) {
			InvStockVO entityVo = new InvStockVO(stock);
			MetaWarehouse warehouse = warehouseExtlService.findWareHouseById(LoginUtil.getWareHouseId());
			if (warehouse != null) {
				entityVo.setWarehouseNo(warehouse.getWarehouseNo());
				entityVo.setWarehouseName(warehouse.getWarehouseName());
			}
			String locationId = stock.getLocationId();
			MetaLocation location = this.locationService.findLocById(locationId);
			if (location != null) {
				entityVo.setLocationNo(location.getLocationNo());
				entityVo.setLocationName(location.getLocationName());
				entityVo.setLocationTypeName(paramService.getValue(CacheName.AREA_TYPE, location.getLocationType()));
				MetaArea area = this.areaService.findAreaById(location.getAreaId());
				if (area != null) {
					entityVo.setAreaName(area.getAreaName());
				}
			}
			MetaSku sku = this.skuService.get(stock.getSkuId());
			if (sku != null) {
				entityVo.setSkuNo(sku.getSkuNo());
				entityVo.setSkuName(sku.getSkuName());
				entityVo.setSpecModel(sku.getSpecModel());
				entityVo.setMeasureUnit(sku.getMeasureUnit());
				entityVo.setSkuBarCode(sku.getSkuBarCode());
				entityVo.setSku(sku);
				MetaMerchant m =merchantService.get(sku.getOwner());
				if (m != null) {
					entityVo.setHgMerchantNo(m.getHgMerchantNo());
					entityVo.setOwnerName(m.getMerchantName());
					entityVo.setMerchantShortName(m.getMerchantShortName());
				}
			}
			if (!StringUtil.isTrimEmpty(stock.getAsnId())) {
				RecAsn asn = asnService.findAsnById(stock.getAsnId());
				if (asn != null) {
					entityVo.setAsnNo(asn.getAsnNo());
				}
			}
			if (sku.getShelflife() != null) {
				entityVo.setLimitDays(String.valueOf(sku.getShelflife() - DateUtil.getIntervalDays(stock.getProduceDate(), new Date())));
			} else {
				entityVo.setLimitDays("未设有效期");
			}
			entityVo.setSkuStatusName(paramService.getValue(CacheName.SKU_STATUS, entityVo.getInvStock().getSkuStatus()));
			entityVo.setIsBlockName(paramService.getValue(CacheName.ISBLOCK, entityVo.getInvStock().getIsBlock()));
			stockVoList.add(entityVo);
		}
		return stockVoList;
	}

}