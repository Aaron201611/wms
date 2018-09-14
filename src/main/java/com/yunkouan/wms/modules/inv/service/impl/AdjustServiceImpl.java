package com.yunkouan.wms.modules.inv.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.exception.BizException;
import com.yunkouan.util.NumberUtil;

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
import com.yunkouan.wms.modules.inv.dao.IAdjustDao;
import com.yunkouan.wms.modules.inv.dao.IAdjustDetailDao;
import com.yunkouan.wms.modules.inv.entity.InvAdjust;
import com.yunkouan.wms.modules.inv.entity.InvAdjustDetail;
import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IAdjustService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvAdjustDetailVO;
import com.yunkouan.wms.modules.inv.vo.InvAdjustVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;

/**
 * 盈亏调整服务实现类
 */
@Service
public class AdjustServiceImpl extends BaseService implements IAdjustService {
	@Autowired
	private ISysParamService paramService;

	
	/**
     * 数据层接口
     */
	@Autowired
    private IAdjustDao dao;
	/**
     * 详情数据层接口
     */
	@Autowired
    private IAdjustDetailDao detailDao;
	/**
	 * 外部服务-包装
	 */
	@Autowired
	IPackService packService;
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
	 * 外部服务-货商
	 */
	@Autowired
	IMerchantService merchantService;
	/**
	 * 外部服务-库存
	 */
	@Autowired
	IStockService stockService;
	/**
	 * 外部服务-库位
	 */
	@Autowired
	ILocationExtlService locationService;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	/**
	 * 外部服务-仓库
	 */
	@Autowired
	IWarehouseExtlService warehouseExtlService;
//	/**
//	 * 外部服务-企业
//	 */
//	@Autowired
//	IOrgService orgService;
	/**
	 * 外部服务-货品
	 */
	@Autowired
	ISkuService skuService;
    /**
     * Default constructor
     */
    public AdjustServiceImpl() {
    }

	/**
	 * 根据ID获取调账单详情
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午1:14:12<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false)
    public InvAdjustVO view(String id) throws Exception {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( id == null ) {
			throw new BizException("err_adj_null");
		}
		// 查询盈亏调整单详情 
		InvAdjust adj = this.dao.selectByPrimaryKey(id);
		if (adj == null) {
			throw new BizException("err_adj_null");
		}
		//组装信息
		adj.setWarehouseId(LoginUtil.getWareHouseId());
		adj.setOrgId(loginUser.getOrgId());
		// 组装信息
		InvAdjustVO adjVo = new InvAdjustVO(adj).searchCache();
//		// 查询仓库信息
//		MetaWarehouse wareHouse = this.warehouseExtlService.findWareHouseById(adj.getWarehouseId());
//		if ( wareHouse != null ) {
//			adjVo.setWarehouseName(wareHouse.getWarehouseName());
//		}
		// 查询创建人信息
		SysUser createPerson = userService.get(adj.getCreatePerson());
		if ( createPerson != null ) {
			adjVo.setCreateUserName(createPerson.getUserName());
		}
		// 查询修改人信息
		SysUser updatePerson = null;
		if ( adj.getCreatePerson().equals(adj.getUpdatePerson()) ) {
			updatePerson = createPerson;
		} else {
			updatePerson = userService.get(adj.getUpdatePerson());
		}
		if ( updatePerson != null ) {
			adjVo.setUpdateUserName(updatePerson.getUserName());
		}
		//组装货主姓名
		if (!StringUtil.isTrimEmpty(adj.getOwner())) {
			MetaMerchant merchant = merchantService.get(adj.getOwner());
			if (merchant != null) {
				adjVo.setOwnerName(merchant.getMerchantName());
				adjVo.setMerchantShortName(merchant.getMerchantShortName());
			}
		}
		// 查询盈亏调整单明细
		List<InvAdjustDetailVO> listInvAdjustDetailVO = new ArrayList<InvAdjustDetailVO>();
		InvAdjustDetail record = new InvAdjustDetail();
		record.setAdjustId(id);
		List<InvAdjustDetail> listInvAdjustDetail = detailDao.select(record);
		if ( listInvAdjustDetail != null && !listInvAdjustDetail.isEmpty() ) {
			for (int i = 0; i < listInvAdjustDetail.size(); i++) {
				InvAdjustDetail detail = listInvAdjustDetail.get(i);
				InvAdjustDetailVO invAdjustDetailVO = new InvAdjustDetailVO(detail).searchCache();
				// 查询库位信息
				MetaLocation location = this.locationService.findLocById(detail.getLocationId());
				invAdjustDetailVO.setLocationName(location.getLocationName());
				MetaArea area = this.areaService.findAreaById(location.getAreaId());
				if (area != null) {
					invAdjustDetailVO.setAreaName(area.getAreaName());
				}
				// 查询货品信息
				MetaSku metaSku = skuService.get(detail.getSkuId());
				invAdjustDetailVO.setSkuName(metaSku.getSkuName());
				invAdjustDetailVO.setSkuNo(metaSku.getSkuNo());
				invAdjustDetailVO.setMeasureUnit(metaSku.getMeasureUnit());
				invAdjustDetailVO.setSpecModel(metaSku.getSpecModel());

				if (!StringUtil.isTrimEmpty(detail.getAsnId())) {
					RecAsn asn = asnService.findAsnById(detail.getAsnId());
					if (asn != null) {
						invAdjustDetailVO.setAsnNo(asn.getAsnNo());
					}
				}
				invAdjustDetailVO.setAdjustTypeName(paramService.getValue(CacheName.ADJUST_TYPE, detail.getAdjustType()));
				listInvAdjustDetailVO.add(invAdjustDetailVO);
			}
		}
		adjVo.setAdjDetailVoList(listInvAdjustDetailVO);
		return adjVo;
    }

    /**
     * 
     * @param adjust
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:24:45<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
    public InvAdjustVO add(InvAdjustVO vo) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if ( vo == null || vo.getInvAdjust() == null ) {
    		throw new BizException("valid_common_data_empty");
    	}
		InvAdjustVO invAdjustVo = new InvAdjustVO();
    	InvAdjust adjust = vo.getInvAdjust();
		// 新增方法
    	// 设置uuid
		String adjustId = IdUtil.getUUID();
    	adjust.setAdjustId(adjustId);
    	// 设置创建人/修改人/企业/仓库
    	String orgId = loginUser.getOrgId();
    	adjust.setCreatePerson(loginUser.getUserId());
    	adjust.setCreateTime(new Date());
    	adjust.setUpdatePerson(loginUser.getUserId());
    	adjust.setUpdateTime(new Date());
    	adjust.setOrgId(orgId);
    	adjust.setWarehouseId(LoginUtil.getWareHouseId());
    	// 设置调账单单号
    	if (StringUtil.isTrimEmpty(adjust.getAdjustNo())) {
    		adjust.setAdjustNo(context.getStrategy4No(orgId, LoginUtil.getWareHouseId()).getAdjustNo(orgId));
    	}
    	// 设置状态
    	Integer adjustStatus = adjust.getAdjustStatus();
    	if (adjustStatus == null || adjustStatus == 0) {
        	adjust.setAdjustStatus(Constant.STATUS_OPEN);
    	}
    	Integer adjustDateFrom = adjust.getDataFrom();
    	if (adjustDateFrom == null || adjustDateFrom == 0) {
        	adjust.setDataFrom(Constant.ADJUST_DATE_FROM_MANUAL);
    	}
    	adjust.setAdjustId2(context.getStrategy4Id().getAdjustSeq());
    	invAdjustVo.setInvAdjust(adjust);
    	this.dao.insertSelective(adjust);
    	List<InvAdjustDetailVO> listAdjustDetailVo = vo.getAdjDetailVoList();
    	if ( listAdjustDetailVo != null && !listAdjustDetailVo.isEmpty() ) {
    		List<InvAdjustDetail> listAdjustDetail = new ArrayList<InvAdjustDetail>();
    		//增加调账单明细
    		for (int i = 0; i < listAdjustDetailVo.size(); i++) {
    			InvAdjustDetailVO invAdjustDetailVO = listAdjustDetailVo.get(i);
    			InvAdjustDetail invAdjustDetail = invAdjustDetailVO.getInvAdjustDetail();
    			String skuId = invAdjustDetail.getSkuId();
    			String localtionId = invAdjustDetail.getLocationId();
    			//验证货品id和库位id是否存在
    			MetaLocation location = this.locationService.findLocById(localtionId);
    			MetaSku skuRet = this.skuService.get(skuId);
    			if (skuRet == null) {
    				throw new BizException("err_adjust_sku_null");
    			}
    			if (location == null) {
    				throw new BizException("err_adjust_location_null");
    			}
    			Double stockQty = invAdjustDetail.getStockQty();
    			Double readQty = invAdjustDetail.getRealQty();
    			invAdjustDetail.setDifferenceQty(readQty - stockQty);
    			invAdjustDetail.setAdjustType(readQty > stockQty ? Constant.ADJUST_TYPE_IN : Constant.ADJUST_TYPE_OUT);
    			//设置主单号
    			invAdjustDetail.setAdjustId(adjustId);
    			//设置自身唯一单号
    			invAdjustDetail.setAdjustDetailId(IdUtil.getUUID());
    			//其他设置
    			invAdjustDetail.setOrgId(loginUser.getOrgId());
    			invAdjustDetail.setWarehouseId(LoginUtil.getWareHouseId());
    			invAdjustDetail.setCreatePerson(loginUser.getUserId());
    			invAdjustDetail.setCreateTime(new Date());
    			invAdjustDetail.setUpdatePerson(loginUser.getUserId());
    			invAdjustDetail.setUpdateTime(new Date());
    			invAdjustDetail.setAdjustDetailId2(context.getStrategy4Id().getAdjustDetailSeq());
    			listAdjustDetail.add(invAdjustDetail);
    		}
        	this.detailDao.add(listAdjustDetail);
        	invAdjustVo.setAdjDetailVoList(listAdjustDetailVo);
		}
		return invAdjustVo;
    }

    /**
     * 调账单生效
     * @param id
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:24:47<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
    public void enable(List<String> adjustIdList) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (adjustIdList == null || adjustIdList.isEmpty()) {
			throw new BizException("err_adjust_null");
		}
    	//获取登录信息中的企业和仓库
    	String orgId = loginUser.getOrgId();
    	String warehouseId = LoginUtil.getWareHouseId();
		for (String adjustId : adjustIdList) {
			InvAdjustVO adjustVo = this.view(adjustId);
			if (adjustVo == null) {
				throw new BizException("err_adjust_null");
			}
			InvAdjust adjust = adjustVo.getInvAdjust();
			if (adjust.getAdjustStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_adjust_status_wrong");
			}
			List<InvAdjustDetailVO> adjustDetailVoList = adjustVo.getAdjDetailVoList();
			if (adjustDetailVoList == null || adjustDetailVoList.isEmpty()) {
				throw new BizException("err_adjust_detail_null");
			}
			List<MessageData> outList = new ArrayList<MessageData>();
			List<MessageData> inList = new ArrayList<MessageData>();
			for (InvAdjustDetailVO adjustDetailVo : adjustDetailVoList) {
				InvAdjustDetail adjustDetail = adjustDetailVo.getInvAdjustDetail();
				Double adjustQty = adjustDetail.getDifferenceQty();
				Double stockQty = adjustDetail.getStockQty();
    			String skuId = adjustDetail.getSkuId();
    			String locationId = adjustDetail.getLocationId();
    			String batchNo = adjustDetail.getBatchNo();
    			String asnId = adjustDetail.getAsnId();
    			String asnDetailId = adjustDetail.getAsnDetailId();
    			//验证盈亏调整单当前的状态是否与库存中的状态一致，如果不一致，提醒用户
    			InvStockVO searchStockVo = new InvStockVO();
    			InvStock searchStock = new InvStock();
    			searchStockVo.setInvStock(searchStock);
    			searchStockVo.setContainBatch(true);
    			searchStock.setSkuId(skuId);
    			searchStock.setLocationId(locationId);
    			searchStock.setBatchNo(batchNo);
    			searchStock.setAsnId(asnId);
    			searchStock.setAsnDetailId(asnDetailId);
    			List<InvStock> retStockList = stockService.list(searchStockVo);
    			if (retStockList != null && retStockList.size() == 1) {
    				if (retStockList.get(0).getStockQty().doubleValue() != stockQty.doubleValue()) {
	    				throw new BizException("err_adjust_stock_change");
    				}
    			} else {
    				throw new BizException("err_adjust_stock_null");
    			}
    			
    			InvStockVO stockVo = new InvStockVO();
    			InvStock stock = new InvStock();
    			stock.setLocationId(locationId);
    			stock.setSkuId(skuId);
    			stock.setBatchNo(batchNo);
    			stock.setOrgId(orgId);
    			stock.setWarehouseId(warehouseId);
    			stock.setAsnDetailId(adjustDetail.getAsnDetailId());
    			stockVo.setInvStock(stock);
    			//构建库存日志对象
    			InvLog invLog = new InvLog();
    			stockVo.setInvLog(invLog);
    			invLog.setOpPerson(loginUser.getUserId());
    			invLog.setInvoiceBill(adjust.getAdjustNo());
    			invLog.setLogType(Constant.STOCK_LOG_TYPE_ADJUST);
    			invLog.setLocationId(locationId);
    			invLog.setSkuId(skuId);
    			
    			MetaSku sku = skuService.get(skuId);
    			MetaLocation location = FqDataUtils.getLocById(locationService, locationId);
    			if (adjustQty > 0) {
    				//增加库存
	    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_IN);
	    			invLog.setQty(adjustQty);
	    			invLog.setVolume(NumberUtil.mul(adjustQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
	    			invLog.setWeight(NumberUtil.mul(adjustQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
	    			stockVo.setFindNum(adjustQty);
    				this.stockService.inStock(stockVo);
    				
    				//辅助系统
    				MessageData data = new MessageData(location.getLocationNo(), location.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(adjustQty, 0), sku.getMeasureUnit(), "I");
    				inList.add(data);
    			} else if (adjustQty < 0) {
    				//减少库存
	    			invLog.setOpType(Constant.STOCK_LOG_OP_TYPE_OUT);
	    			invLog.setQty(-adjustQty);
	    			invLog.setVolume(-NumberUtil.mul(adjustQty, sku.getPerVolume() == null ? 0 : sku.getPerVolume()));
	    			invLog.setWeight(-NumberUtil.mul(adjustQty, sku.getPerWeight() == null ? 0 : sku.getPerWeight()));
    				stockVo.setFindNum(-adjustQty);
    				this.stockService.outStock(stockVo);
    				
    				//辅助系统
    				MessageData data = new MessageData(location.getLocationNo(), location.getLocationColumn(), sku.getSkuNo(), NumberUtil.rounded(Math.abs(adjustQty), 0), sku.getMeasureUnit(), "E");
    				outList.add(data);
    			} else {
    				throw new BizException("err_adjust_qty_wrong");
    			}
			}
			//发送辅助系统
			//出库
//			context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjustId, true, outList);
			//入库
//			context.getStrategy4Assis().request(orgId, IAssisService.BILL_TYPE_ADJUST, adjustId, false, inList);
			//修改盈亏调整单
			InvAdjust adjustRecord = new InvAdjust();
			adjustRecord.setAdjustId(adjustId);
			adjustRecord.setAdjustStatus(Constant.STATUS_ACTIVE);
			this.dao.updateByPrimaryKeySelective(adjustRecord);
		}
    }

    /**
     * 
     * @param id
     * @required 
     * @optional  
     * @Description 
     * @version 2017年3月11日 下午1:24:53<br/>
     * @author 王通<br/>
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false)
    public void cancel(List<String> adjustIdList) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		if (adjustIdList == null || adjustIdList.isEmpty()) {
			throw new BizException("err_adjust_null");
		}
    	//获取登录信息中的企业和仓库
		for (String adjustId : adjustIdList) {
			InvAdjustVO adjustVo = this.view(adjustId);
			if (adjustVo == null) {
				throw new BizException("err_adjust_null");
			}
			InvAdjust adjust = adjustVo.getInvAdjust();
			if (adjust.getAdjustStatus() != Constant.STATUS_OPEN) {
				throw new BizException("err_adjust_status_wrong");
			}
			InvAdjust adjustRecord = new InvAdjust();
			adjustRecord.setAdjustId(adjustId);
			adjustRecord.setAdjustStatus(Constant.STATUS_CANCEL);
			this.dao.updateByPrimaryKeySelective(adjustRecord);
		}
    }

	/**
	 * @param adjust
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午1:27:08<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false)
	public InvAdjustVO update(InvAdjustVO vo) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
		if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null || vo.getInvAdjust() == null ) {
    		throw new BizException("err_adjust_null");
    	}
		InvAdjustVO invAdjustVo = new InvAdjustVO();
    	InvAdjust adjust = vo.getInvAdjust();
    	String adjustId = adjust.getAdjustId();
		
		// 修改方法
		// 检查调账单是否存在
		InvAdjust findAdjust = this.dao.selectByPrimaryKey(adjustId);
		if (findAdjust == null || StringUtil.isEmpty(findAdjust.getAdjustId())) {
			throw new BizException("err_adjust_null");
		} else if (!findAdjust.getAdjustStatus().equals(Constant.STATUS_OPEN)) {
			throw new BizException("err_adjust_status_wrong");
		} else if (!findAdjust.getDataFrom().equals(Constant.ADJUST_DATE_FROM_MANUAL)) {
			throw new BizException("err_adjust_from_wrong");
		}
		
		// 设置修改人/企业/仓库
    	adjust.setAdjustStatus(Constant.STATUS_OPEN);
    	adjust.setUpdatePerson(loginUser.getUserId());
    	adjust.setUpdateTime(new Date());
    	invAdjustVo.setInvAdjust(adjust);
    	this.dao.updateByPrimaryKeySelective(adjust);
    	List<InvAdjustDetailVO> listAdjustDetailVo = vo.getAdjDetailVoList();
    	if ( listAdjustDetailVo != null && !listAdjustDetailVo.isEmpty() ) {
    		List<InvAdjustDetail> listAdjustDetail = new ArrayList<InvAdjustDetail>();
    		//清空调账单明细
    		InvAdjustDetail delAdjDetail = new InvAdjustDetail();
    		delAdjDetail.setAdjustId(adjustId);
    		this.detailDao.delete(delAdjDetail);
    		//添加调账单明细
    		for (int i = 0; i < listAdjustDetailVo.size(); i++) {
    			InvAdjustDetailVO invAdjustDetailVO = listAdjustDetailVo.get(i);
    			InvAdjustDetail invAdjustDetail = invAdjustDetailVO.getInvAdjustDetail();
    			String locationId = invAdjustDetail.getLocationId();
    			String skuId = invAdjustDetail.getSkuId();
    			//验证货品id和库位id是否存在
    			MetaLocation location = this.locationService.findLocById(locationId);
    			MetaSku skuRet = this.skuService.get(skuId);
    			if (skuRet == null) {
    				throw new BizException("err_adjust_sku_null");
    			}
    			if (location == null) {
    				throw new BizException("err_adjust_location_null");
    			}
    			Double stockQty = invAdjustDetail.getStockQty();
    			Double readQty = invAdjustDetail.getRealQty();
    			invAdjustDetail.setDifferenceQty(readQty - stockQty);
    			invAdjustDetail.setAdjustType(readQty > stockQty ? Constant.ADJUST_TYPE_IN : Constant.ADJUST_TYPE_OUT);
    			//设置主单号
    			invAdjustDetail.setAdjustId(adjustId);
    			//设置自身唯一单号
    			invAdjustDetail.setAdjustDetailId(IdUtil.getUUID());
    			//其他设置
    			invAdjustDetail.setOrgId(loginUser.getOrgId());
    			invAdjustDetail.setWarehouseId(LoginUtil.getWareHouseId());
    			invAdjustDetail.setCreatePerson(loginUser.getUserId());
    			invAdjustDetail.setCreateTime(new Date());
    			invAdjustDetail.setUpdatePerson(loginUser.getUserId());
    			invAdjustDetail.setUpdateTime(new Date());
    			listAdjustDetail.add(invAdjustDetail);
    		}
        	this.detailDao.add(listAdjustDetail);
        	invAdjustVo.setAdjDetailVoList(listAdjustDetailVo);
		}
		return invAdjustVo ;
	}

	/**
	 * @param adjVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月11日 下午2:13:41<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Page<InvAdjustVO> listByPage(InvAdjustVO vo) {
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		if ( vo == null ) {
    		vo = new InvAdjustVO();
		}
		if ( vo.getInvAdjust() == null ) {
			vo.setInvAdjust(new InvAdjust());
		}
		Page<InvAdjustVO> page = null;
		InvAdjust adj = vo.getInvAdjust();
		//组装信息
		adj.setWarehouseId(LoginUtil.getWareHouseId());
		adj.setOrgId(loginUser.getOrgId());
		String countNo = vo.getCountNo();
		if (!StringUtil.isTrimEmpty(countNo)) {
			vo.setCountNo(StringUtil.likeEscapeH(countNo));
		}
		String adjustNo = adj.getAdjustNo();
		if (!StringUtil.isTrimEmpty(adjustNo)) {
			adj.setAdjustNo(StringUtil.likeEscapeH(adjustNo));
		}
		// 查询调账单列表 
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<InvAdjust> listInvAdjust = this.dao.listByPage(vo);
		if ( listInvAdjust == null || listInvAdjust.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<InvAdjustVO> listVO = new ArrayList<InvAdjustVO>();
		for (int i = 0; i < listInvAdjust.size(); i++) {
			InvAdjust adjust = listInvAdjust.get(i);
			if ( adjust == null ) {
				continue;
			}
			InvAdjustVO adjustVo = new InvAdjustVO(adjust).searchCache();
			listVO.add(adjustVo);
			// 查询创建人信息
			SysUser createPerson = userService.get(adjust.getCreatePerson());
			if ( createPerson != null ) {
				adjustVo.setCreateUserName(createPerson.getUserName());
			}
			// 查询修改人信息
			SysUser updatePerson = null;
			if ( adjust.getCreatePerson().equals(adjust.getUpdatePerson()) ) {
				updatePerson = createPerson;
			} else {
				updatePerson = userService.get(adjust.getUpdatePerson());
			}
			if ( updatePerson != null ) {
				adjustVo.setUpdateUserName(updatePerson.getUserName());
			}
			//组装货主姓名
			if (!StringUtil.isTrimEmpty(adjust.getOwner())) {
				MetaMerchant merchant = merchantService.get(adjust.getOwner());
				if (merchant != null) {
					adjustVo.setOwnerName(merchant.getMerchantName());
					adjustVo.setMerchantShortName(merchant.getMerchantShortName());
				}
			}
			//组装状态名称
			if (adjust.getAdjustStatus() != null) {
				adjustVo.setStatusName(paramService.getValue(CacheName.ADJUST_STATUS, adjust.getAdjustStatus()));
			}
			//组装来源名称
			if (adjust.getDataFrom() != null) {
				adjustVo.setDataFromName(paramService.getValue(CacheName.ADJUST_DATA_FROM, adjust.getDataFrom()));
			}
		}
		page.clear();
		page.addAll(listVO);
		return page;
	}

	/**
	 * @param adjust
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月19日 下午1:11:44<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public InvAdjustVO saveAndEnable(InvAdjustVO vo) throws Exception {
		String adjustId = vo.getInvAdjust().getAdjustId();
    	InvAdjustVO adjustVo = null;
    	if (StringUtil.isTrimEmpty(adjustId)) {
            adjustVo = this.add(vo);
            adjustId = adjustVo.getInvAdjust().getAdjustId();
    	} else {
            adjustVo = this.update(vo);
    	}
        List<String> idList = new ArrayList<String>();
        idList.add(adjustId);
        this.enable(idList);
        return adjustVo;
	}

	/**
	 * @param id
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午3:38:02<br/>
	 * @author 王通<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public Integer countByExample(InvAdjustVO vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	vo.getInvAdjust().setWarehouseId(LoginUtil.getWareHouseId());
    	vo.getInvAdjust().setOrgId(loginUser.getOrgId());
    	return dao.selectCountByExample(vo.getExample());
	}
	@Override
	@Transactional(readOnly = true)
	public Integer countByExample2(InvAdjustVO vo) throws Exception {
    	return dao.selectCountByExample(vo.getExample());
	}
	public List<String>getTask(String orgId){
		return dao.getTask(orgId);
	}
}