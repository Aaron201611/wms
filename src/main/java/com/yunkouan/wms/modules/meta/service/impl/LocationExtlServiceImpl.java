/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月23日 下午2:28:54<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.dao.ILocationDao;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvShiftDetail;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.service.IShiftService;
import com.yunkouan.wms.modules.inv.service.IStockService;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.meta.util.strategy.CapacityAddPreusedStrategy;
import com.yunkouan.wms.modules.meta.util.strategy.CapacityAddStrategy;
import com.yunkouan.wms.modules.meta.util.strategy.CapacityStrategy;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.service.IPutawayLocationExtlService;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 库位外调业务类<br/><br/>
 * @version 2017年2月23日 下午2:28:54<br/>
 * @author andy wang<br/>
 */
@Service
public class LocationExtlServiceImpl extends BaseService implements ILocationExtlService {
	
	/**
	 * 日志对象
     * @author andy wang<br/>
	 */
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	/** 库位Dao <br/> add by andy wang */
	@Autowired
	private ILocationDao dao;
	
	/** 货品业务接口 <br/> add by andy wang */
	@Autowired
	private ISkuService skuService;
	
	
	/** 库存业务接口 <br/> add by andy wang */
	@Autowired
	private IStockService stockService;
	
	/** 移位单业务接口 <br/> add by andy wang */
	@Autowired
	private IShiftService shiftService;
	
	/** 上架单操作明细业务接口 <br/> add by andy wang */
	@Autowired
	private IPutawayLocationExtlService ptwLocService;

	/**
	 * 根据库位id，重算库容度
	 * @param param_locationId 库位id
	 * @throws Exception
	 * @version 2017年5月14日 下午2:58:27<br/>
	 * @author andy wang<br/>
	 */
	public void recalCapacity ( String param_locationId ) throws Exception {
		if ( StringUtil.isEmpty(param_locationId) ) {
			log.error("recalCapcity --> param_locationId is empty!");
    		throw new NullPointerException("parameter is null!");
		}
		List<String> listLocationId = new ArrayList<String>();
		listLocationId.add(param_locationId);
		this.recalCapacity(listLocationId);
	}

	/**
	 * 根据库位id，重算库容度
	 * @param param_listLocationId 库位id集合
	 * @throws Exception
	 * @version 2017年5月14日 下午4:55:04<br/>
	 * @author andy wang<br/>
	 */
	public void recalCapacity ( List<String> param_listLocationId ) throws Exception {
    	if ( PubUtil.isEmpty(param_listLocationId) ) {
			log.error("recalCapcity --> param_listLocationId is empty!");
    		throw new NullPointerException("parameter is null!");
		}
    	FqDataUtils fq = new FqDataUtils();
    	for (String locationId : param_listLocationId) {
    		
    		MetaLocation sourceLocation = this.findLocById(locationId);
    		if ( sourceLocation.getLocationType().equals(Constant.LOCATION_TYPE_TEMPSTORAGE) ) {
    			continue;
    		}
    		
    		MetaLocationVO p_locationVO = new MetaLocationVO();
    		p_locationVO.getLocation().setLocationId(locationId);
    		// 计算已用库容
    		BigDecimal usedCapacity = this.calUsedCapacity(fq, locationId);
    		p_locationVO.getLocation().setUsedCapacity(usedCapacity);
    		// 计算预分配库容
    		BigDecimal preCapacity = this.calPreCapacity(fq, locationId);
    		p_locationVO.getLocation().setPreusedCapacity(preCapacity);
    		// 更新库存
    		this.updateCapacity(p_locationVO);
		}
	}

	/**
	 * 统计预分配库容
	 * @param fq 常用数据查询工具类
	 * @param locationId 库位id
	 * @return 库位预分配的库容
	 * @throws Exception
	 * @version 2017年5月15日 下午6:02:18<br/>
	 * @author andy wang<br/>
	 */
	private BigDecimal calPreCapacity(FqDataUtils fq, String locationId) throws Exception {
		// 统计移位单预分配库容
		BigDecimal preCapacity = new BigDecimal(0);
		List<InvShiftDetail> listShift = this.shiftService.getInLocationOccupy(locationId, Constant.SHIFT_STATUS_ACTIVE,Constant.SHIFT_STATUS_WORKING);
		if ( !PubUtil.isEmpty(listShift) ) {
			for (InvShiftDetail invShiftDetail : listShift) {
				MetaSku sku = FqDataUtils.getSkuById(this.skuService, invShiftDetail.getSkuId());
				BigDecimal capacity = LocationUtil.capacityConvert(invShiftDetail.getShiftQty(), sku);;
				preCapacity = preCapacity.add(capacity);
			}
		}
		// 统计上架单预分配库容
		List<RecPutawayLocationVO> listPtwLoc = this.ptwLocService.listPtwLocRecalByLocId(locationId);
		if ( !PubUtil.isEmpty(listPtwLoc) ) {
			for (RecPutawayLocationVO putawayLocationVO : listPtwLoc) {
				RecPutawayLocation putawayLocation = putawayLocationVO.getPutawayLocation();
				MetaSku sku = FqDataUtils.getSkuById(this.skuService, putawayLocationVO.getSkuId());
				BigDecimal capacity = LocationUtil.capacityConvert(putawayLocation.getPutawayQty(), sku);;
				preCapacity = preCapacity.add(capacity);
			}
		}
		return preCapacity;
	}

	/**
	 * 统计已使用库容
	 * @param fq 常用数据查询工具类
	 * @param locationId 库位id
	 * @return 库位已使用库容
	 * @throws Exception
	 * @version 2017年5月15日 下午5:45:53<br/>
	 * @author andy wang<br/>
	 */
	private BigDecimal calUsedCapacity(FqDataUtils fq, String locationId) throws Exception {
		// 根据库位id，查询统计库存数量
		InvStockVO p_stockVo = new InvStockVO(new InvStock());
		p_stockVo.getInvStock().setLocationId(locationId);
		List<InvStock> listStock = this.stockService.list(p_stockVo);
		BigDecimal usedCapacity = new BigDecimal(0);
		for (InvStock invStock : listStock) {
			MetaSku sku = null;
			if ( !StringUtil.isTrimEmpty(invStock.getSkuId()) ) {
				sku = FqDataUtils.getSkuById(this.skuService, invStock.getSkuId());
			}
			if ( sku == null ) {
				continue;
			}
			BigDecimal usedCp = LocationUtil.capacityConvert(invStock.getStockQty(), sku);
			usedCapacity = usedCapacity.add(usedCp);
		}
		return usedCapacity;
	}
	
	/**
	 * 根据id，更新库容度
	 * @param metaLocationVO 库位VO
	 * @return
	 * @throws Exception
	 * @version 2017年5月14日 上午11:46:08<br/>
	 * @author andy wang<br/>
	 */
	public int updateCapacity ( MetaLocationVO metaLocationVO ) throws Exception {
		if ( metaLocationVO == null || metaLocationVO.getLocation() == null 
				|| StringUtil.isTrimEmpty(metaLocationVO.getLocation().getLocationId()) 
				|| metaLocationVO.getLocation().getUsedCapacity() == null 
				|| metaLocationVO.getLocation().getPreusedCapacity() == null ) {
			log.error("updateCapacity --> locationId is null!");
			return 0;
		}
		MetaLocation location = metaLocationVO.getLocation();
		// 设置更新条件
		MetaLocationVO metaLocVO = new MetaLocationVO();
		metaLocVO.getLocation().setLocationId(location.getLocationId());
		metaLocVO.loginInfo();
		Example example = metaLocVO.getExample();
		// 设置更新内容
		MetaLocation record = new MetaLocation();
		record.setUsedCapacity(location.getUsedCapacity());
		record.setPreusedCapacity(location.getPreusedCapacity());
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.dao.updateByExampleSelective(record, example);
	}
	
	/**
	 * 查询默认暂存区收货库位
	 * @return 默认暂存区收货库位
	 * @throws Exception
	 * @version 2017年3月22日 下午12:33:47<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findDefTempLoc () throws Exception {
		MetaLocationVO locVO = new MetaLocationVO();
		locVO.getLocation().setIsBlock(Constant.LOCATION_BLOCK_FALSE);
		locVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		locVO.getLocation().setLocationType(Constant.AREA_TYPE_TEMPSTORAGE);
		locVO.loginInfo();
		Example example = locVO.getExample();
		example.setOrderByClause("location_id2 ASC");
		List<MetaLocation> listLoc = this.dao.selectByExample(example);
		return PubUtil.isEmpty(listLoc)? null: listLoc.get(0);
	}
	
	
	/**
	 * 查询默认的收货暂存库位<br/>
	 * 查询数据库没有默认库位时，这个接口自动插入一条默认的收货暂存库位的数据。
	 * 改为使用{@link #findDefTempLoc}方法 —— 原为Asn单收货确认时用户没有选择库位调用这个接口，设置默认的收货库位，
	 * 但后来发觉库区等默认字段不好设置，暂时屏蔽调用。
	 * @return 默认的收货暂存库位
	 * @throws Exception
	 * @version 2017年3月22日 上午11:20:43<br/>
	 * @author andy wang<br/>
	 */
	@Deprecated
	public MetaLocation findDefLocation () throws Exception {
		MetaLocation defLoc = null;
		MetaLocationVO locVO = new MetaLocationVO();
		locVO.getLocation().setIsDefault(Constant.LOCATION_ISDEFAULT_TRUE);
		locVO.getLocation().setIsBlock(Constant.LOCATION_BLOCK_FALSE);
		locVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		locVO.getLocation().setLocationType(Constant.AREA_TYPE_TEMPSTORAGE);
		locVO.loginInfo();
		List<MetaLocation> listLoc = this.listLocByExample(locVO);
		if ( PubUtil.isEmpty(listLoc) ) {
			// 该企业仓库没有默认库位
			// 系统自动创建一个默认库位
			defLoc = new MetaLocation();
			defLoc.setIsDefault(Constant.LOCATION_ISDEFAULT_TRUE);
			defLoc.setIsBlock(Constant.LOCATION_BLOCK_FALSE);
			defLoc.setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
			defLoc.setLocationType(Constant.AREA_TYPE_TEMPSTORAGE);
			defLoc.setLocationNo("Default Location");
			defLoc.setLocationName(Constant.LOCATION_NAME_DEFAULT);
			Principal loginUser = LoginUtil.getLoginUser();
			defLoc.setWarehouseId(LoginUtil.getWareHouseId());
			defLoc.setOrgId(loginUser.getOrgId());
			defLoc.setMaxCapacity(BigDecimal.valueOf(99999999999999d));
			defLoc.setNote("System auto create default location");
			defLoc = this.insertLoc(defLoc);
		} else {
			defLoc = listLoc.get(0);
		}
		return defLoc;
	}
	
	
	/**
	 * 根据库位id，冻结库位
	 * @param locId 库位id
	 * @param isBlock 是否冻结
	 * —— 0 未冻结
	 * —— 1 冻结
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月14日 下午3:34:12<br/>
	 * @author andy wang<br/>
	 */
	public int blockLoc( String locId ,Integer isBlock) throws Exception {
		if ( StringUtil.isTrimEmpty(locId) ) {
			log.error("blockLoc --> locId is null!");
			return 0;
		}
		MetaLocationVO locationVO = new MetaLocationVO();
		locationVO.getLocation().setIsBlock(isBlock);
		locationVO.getLocation().setLocationId(locId);
		return this.updateLoc(locationVO);
	}
	
	
	/**
	 * 根据库位规格id，更新最大库容
	 * @param specId 库位规格id
	 * @param maxCapacity 更新的最大库容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月14日 上午11:52:02<br/>
	 * @author andy wang<br/>
	 */
	public double updateMaxCapacity(String specId , BigDecimal maxCapacity) throws Exception {
		if ( StringUtil.isTrimEmpty(specId) || maxCapacity == null ) {
			log.error("updateMaxCapacity --> specId or maxCapacity is null!");
			return 0;
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO();
		metaLocationVO.getLocation().setSpecId(specId);
		List<MetaLocation> list = dao.select(metaLocationVO.getLocation());
		if(list == null || list.isEmpty()) return 0;
		for(MetaLocation l : list) {
			l.setMaxCapacity(maxCapacity);
			BigDecimal rate = new BigDecimal(l.getUsedCapacity().doubleValue()*100/l.getMaxCapacity().doubleValue());
			l.setCapacityUseRate(NumberUtil.rounding(rate.doubleValue(), 2));
			dao.updateByPrimaryKeySelective(l);
		}
		return list.size();
	}

	/**
	 * 根据库位编号，查询库位
	 * @param locationNo 库位编号
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年3月12日 下午9:24:41<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findLocByNo ( String locationNo , String locationId ) throws Exception {
		if ( StringUtil.isTrimEmpty(locationNo) ) {
			log.error("findLocByNo --> locationNo is null!");
			return null;
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO();
		metaLocationVO.addNotId(locationId);
		metaLocationVO.getLocation().setLocationNo(locationNo);
		List<MetaLocation> listLocation = this.listLocByExample(metaLocationVO);
		return PubUtil.isEmpty(listLocation)?null:listLocation.get(0);
	}
	
	
	/**
	 * 更新库位状态
	 * @param locationId 库位id
	 * @param status 库位状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月12日 下午9:18:11<br/>
	 * @author andy wang<br/>
	 */
	public int updateLocStatusById ( String locationId , Integer status ) throws Exception {
		if ( StringUtil.isTrimEmpty(locationId) ) {
			log.error("updateLocStatusById --> locationId is null!");
			return 0;
		}
		// 设置更新条件
		MetaLocationVO metaLocationVO = new MetaLocationVO();
		metaLocationVO.getLocation().setLocationId(locationId);
		metaLocationVO.loginInfo();
		Example example = metaLocationVO.getExample();
		// 设置更新内容
		MetaLocation record = new MetaLocation();
		record.setLocationStatus(status);
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.dao.updateByExampleSelective(record, example);
	}
	
	/**
	 * 根据条件，分页查询库位
	 * @param metaLocationVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月12日 下午8:47:46<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listLocByPage ( MetaLocationVO metaLocationVO ) throws Exception {
		if ( metaLocationVO == null || metaLocationVO.getLocation() == null ) {
			log.error("listLocByPage --> metaLocationVO or location is null!");
			return null;
		}
		metaLocationVO.loginInfo();
		Page page = PageHelper.startPage(metaLocationVO.getCurrentPage()+1, metaLocationVO.getPageSize());
		Example example = metaLocationVO.getExample();
//		example.setOrderByClause("location_id2 DESC");
		this.dao.selectByExample(example);
		if ( !PubUtil.isEmpty(page) ) {
			for (Object obj : page) {
				MetaLocation metaLocation = (MetaLocation) obj;
				metaLocation.clear();
			}
		}
		return page;
	}
	
	/**
	 * 保存库位
	 * @param metaLocation 库位
	 * @return 保存后的库位对象
	 * @throws Exception
	 * @version 2017年3月12日 下午8:28:14<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation insertLoc ( MetaLocation metaLocation ) throws Exception {
		if ( metaLocation == null ) {
			log.error("insertLoc --> metaLocation is null!");
			return null;
		}
		if ( StringUtil.isTrimEmpty(metaLocation.getLocationId()) ) {
			metaLocation.setLocationId(IdUtil.getUUID());
		}
		Principal loginUser = LoginUtil.getLoginUser();
		metaLocation.setOrgId(loginUser.getOrgId());
		metaLocation.setWarehouseId(LoginUtil.getWareHouseId());
		metaLocation.setCreatePerson(loginUser.getUserId());
		metaLocation.setUpdatePerson(loginUser.getUserId());
		//设置默认值
		metaLocation.setLocationId2(null);
		Date d = new Date();
		metaLocation.setCreateTime(d);
		metaLocation.setUpdateTime(d);
		metaLocation.setIsBlock(0);
		if(metaLocation.getUsedCapacity() == null) metaLocation.setUsedCapacity(new BigDecimal(0));
		if(metaLocation.getPreusedCapacity() == null) metaLocation.setPreusedCapacity(new BigDecimal(0));
		if(StringUtils.isBlank(metaLocation.getLocationZone())) metaLocation.setLocationZone("0");
		if(StringUtils.isBlank(metaLocation.getLocationRow())) metaLocation.setLocationRow("0");
		if(StringUtils.isBlank(metaLocation.getLocationColumn())) metaLocation.setLocationColumn("0");
		if(StringUtils.isBlank(metaLocation.getLayer())) metaLocation.setLayer("0");
		if(metaLocation.getX() == null) metaLocation.setX(0);
		if(metaLocation.getY() == null) metaLocation.setY(0);
		if(metaLocation.getZ() == null) metaLocation.setZ(0);
		if(metaLocation.getIsDefault() == null) metaLocation.setIsDefault(0);
		metaLocation.setLocationId2(context.getStrategy4Id().getLocationSeq());
		this.dao.insertSelective(metaLocation);
//		this.dao.insert(metaLocation);
		return metaLocation;
	}
	
	
	/**
	 * 更新库位
	 * @param locationVO 库位对象
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月12日 下午7:55:06<br/>
	 * @author andy wang<br/>
	 */
	public int updateLoc ( MetaLocationVO locationVO ) throws Exception {
		if ( locationVO == null || locationVO.getLocation() == null ) {
			log.error("updateLoc --> locationVO or location is null!");
			return 0;
		}
		MetaLocation location = locationVO.getLocation();
		// 拼接Where条件
		MetaLocationVO metaLocationVO = new MetaLocationVO();
		metaLocationVO.getLocation().setLocationId(location.getLocationId());
		metaLocationVO.loginInfo();
		Example example = metaLocationVO.getExample();
		// 拼接更新内容
		Principal loginUser = LoginUtil.getLoginUser();
		location.setLocationId2(null);
		location.clear();
		location.setUpdatePerson(loginUser.getUserId());
		location.setUpdateTime(new Date());
		return this.dao.updateByExampleSelective(location, example);
	}
	
	
	/**
	 * 减少库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 减少库容度<br/>
	 * —— * 自动更新<strong>库容占比</strong><br/>
	 * @param skuId 移出货品id
	 * @param locationId 移出库位id
	 * @param packId 移出货品的包装id
	 * @param capacity 移出库容(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午5:04:33<br/>
	 * @author andy wang<br/>
	 */
	public void minusCapacity ( String skuId , String locationId , String packId , BigDecimal capacity , boolean isRelease ) throws Exception {
		this.addCapacity(skuId, locationId, packId, capacity.multiply(BigDecimal.valueOf(-1)), isRelease);
	}
	
	
	/**
	 * 增加库容度(方法内不进行数量与库容的转换。直接提供计算好的库容)
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加库容度<br/>
	 * —— * 释放预分配库容度<br/>
	 * —— * 自动更新<strong>库容占比</strong><br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param capacity 移入库容(使用正数)
	 * @param isRelease 是否释放预分配库存
	 * —— true 释放预分配库存
	 * —— false 不操作释放
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午5:04:33<br/>
	 * @author andy wang<br/>
	 */
	public void addCapacity ( String skuId , String locationId , String packId , BigDecimal capacity , boolean isRelease ) throws Exception {
		if ( StringUtil.isTrimEmpty(locationId) 
				|| capacity == null || capacity.compareTo(BigDecimal.ZERO) == 0 ) {
			log.error("addCapacity --> parameter is null!");
			return ;
		}
		// 查询库位
		MetaLocation location = this.findLocById(locationId);
		// 查询货品
		MetaSku sku = this.skuService.get(skuId);
		CapacityStrategy cs = new CapacityAddStrategy(location, sku, capacity, isRelease);
		MetaLocationVO metaLocationVO = cs.execute();
		if ( metaLocationVO == null ) {
			return ;
		}
		int count = this.addCapacity(metaLocationVO);
		if ( count <= 0 ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
//		this.recalCapacity(locationId);
//		location = this.findLocById(locationId);
	}
	
	
	/**
	 * 扣减预分配库容度(方法内不进行数量与库容的转换。直接提供计算好的库容)
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 扣减预分配库容度<br/>
	 * @param skuId 移出的货品货品id
	 * @param locationId 移出的库位id
	 * @param packId 移出货品的包装id
	 * @param capacity 移出库容(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月4日 下午6:00:03<br/>
	 * @author andy wang<br/>
	 */
	public void minusPreusedCapacity ( String skuId , String locationId , String packId , BigDecimal capacity ) throws Exception {
		this.addPreusedCapacity(skuId, locationId, packId, capacity.multiply(BigDecimal.valueOf(-1)));
	}
	
	
	/**
	 * 增加预分配库容度(方法内不进行数量与库容的转换。直接提供计算好的库容)
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加/扣减预分配库容度<br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param capacity 移入库容(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月4日 下午6:00:03<br/>
	 * @author andy wang<br/>
	 */
	public void addPreusedCapacity ( String skuId , String locationId , String packId , BigDecimal capacity ) throws Exception  {
		if ( StringUtil.isTrimEmpty(locationId)  
				|| capacity == null || capacity.compareTo(BigDecimal.ZERO) == 0 ) {
			log.error("addPreusedCapacity --> parameter is null!");
			return ;
		}
		// 查询库位
		MetaLocation location = this.findLocById(locationId);
		// 查询货品
		MetaSku sku = this.skuService.get(skuId);
		CapacityStrategy cs = new CapacityAddPreusedStrategy(location, sku, capacity);
		MetaLocationVO metaLocationVO = cs.execute();
		if ( metaLocationVO == null ) {
			return ;
		}
		int count = this.addPreusedCapacity(metaLocationVO);
		if ( count <= 0 ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
//		this.recalCapacity(locationId);
	}
	
	
	/**
	 * 减少预分配库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加/扣减预分配库容度<br/>
	 * @param skuId 移出的货品货品id
	 * @param locationId 移出的库位id
	 * @param packId 移出货品的包装id
	 * @param qty 移出数量(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午2:30:07<br/>
	 * @author andy wang<br/>
	 */
	public void minusPreusedCapacity ( String skuId , String locationId , String packId , Double qty ) throws Exception  {
		this.addPreusedCapacity(skuId, locationId, packId, qty*-1);
	}
	
	
	/**
	 * 增加预分配库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加/扣减预分配库容度<br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param qty 移入数量(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午2:30:07<br/>
	 * @author andy wang<br/>
	 */
	public void addPreusedCapacity ( String skuId , String locationId , String packId , Double qty ) throws Exception  {
		if ( StringUtil.isTrimEmpty(locationId)  
				|| qty == null || qty == 0 ) {
			log.error("addPreusedCapacity --> parameter is null!");
			return ;
		}
		// 查询库位
		MetaLocation location = this.findLocById(locationId);
		// 查询货品
		MetaSku sku = this.skuService.get(skuId);
		CapacityStrategy cs = new CapacityAddPreusedStrategy(location, sku, qty);
		MetaLocationVO metaLocationVO = cs.execute();
		if ( metaLocationVO == null ) {
			return ;
		}
		int count = addPreusedCapacity(metaLocationVO);
		if ( count <= 0 ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
//		this.recalCapacity(locationId);
	}
	/** 
	* @Title: addPreusedCapacity 
	* @Description: TODO
	<update id="addPreusedCapacity" parameterType="MetaLocationVO">
		UPDATE meta_location
		<set>
			<if test="location.preusedCapacity != null">
				preused_capacity = preused_capacity + #{location.preusedCapacity}
			</if>
		</set>
		WHERE
			location_id = #{location.locationId}
			<if test="location.preusedCapacity != null and location.preusedCapacity != 0">
				<if test="location.preusedCapacity &gt; 0">
					 and max_capacity - used_capacity - preused_capacity >= #{location.preusedCapacity}
				</if>
				<if test="location.usedCapacity &lt; 0">
					 and #{location.preusedCapacity} + preused_capacity >= 0 
				</if>
			</if>
	</update>
	* @auth tphe06
	* @time 2018 2018年8月9日 下午3:54:35
	* @param vo
	* @return
	* int
	*/
	private int addPreusedCapacity(MetaLocationVO vo) {
		MetaLocation l_new = vo.getLocation();
		if(l_new.getPreusedCapacity() == null) return 0;
		MetaLocation l_old = dao.selectByPrimaryKey(l_new.getLocationId());
		if(l_old == null) return 0;
		if(NumberUtil.round(l_new.getPreusedCapacity().doubleValue(), LENGTH) != 0) {
			if(NumberUtil.round(l_new.getPreusedCapacity().doubleValue(), LENGTH) > 0) {
				if(NumberUtil.round(l_old.getMaxCapacity().doubleValue()-l_old.getUsedCapacity().doubleValue()-l_old.getPreusedCapacity().doubleValue()-l_new.getPreusedCapacity().doubleValue(), LENGTH) < 0) return 0;
			}
			if(l_new.getUsedCapacity()!=null && NumberUtil.round(l_new.getUsedCapacity().doubleValue(), LENGTH) < 0) {
				if(NumberUtil.round(l_new.getPreusedCapacity().doubleValue()+l_old.getPreusedCapacity().doubleValue(), LENGTH) < 0) return 0;
			}
		}
		BigDecimal preused = new BigDecimal(l_old.getPreusedCapacity().doubleValue()+l_new.getPreusedCapacity().doubleValue());
		l_old.setPreusedCapacity(NumberUtil.rounding(preused.doubleValue(), LENGTH));
		return dao.updateByPrimaryKeySelective(l_old);
	}


	/**
	 * 减少库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 减少库容度<br/>
	 * —— * 自动更新<strong>库容占比</strong><br/>
	 * @param skuId 移出货品id
	 * @param locationId 移出库位id
	 * @param packId 移出货品的包装id
	 * @param qty 移出数量(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午5:04:33<br/>
	 * @author andy wang<br/>
	 */
	public void minusCapacity ( String skuId , String locationId , String packId , Double qty ) throws Exception {
		this.addCapacity(skuId, locationId, packId, qty*-1,false);
	}
	
	
	/**
	 * 增加库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加库容度<br/>
	 * —— * 释放预分配库容度<br/>
	 * —— * 自动更新<strong>库容占比</strong><br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param qty 移入数量(使用正数)
	 * @param isRelease 是否释放预分配库存
	 * —— true 释放预分配库存
	 * —— false 不操作释放
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午5:04:33<br/>
	 * @author andy wang<br/>
	 */
	public void addCapacity ( String skuId , String locationId , String packId , Double qty , boolean isRelease ) throws Exception {
		if ( StringUtil.isTrimEmpty(locationId) 
				|| qty == null || qty == 0 ) {
			log.error("addCapacity --> parameter is null!");
			return ;
		}
		// 查询库位
		MetaLocation location = this.findLocById(locationId);
		// 查询货品
		MetaSku sku = this.skuService.get(skuId);
		CapacityStrategy cs = new CapacityAddStrategy(location, sku, qty, isRelease);
		MetaLocationVO metaLocationVO = cs.execute();
		if ( metaLocationVO == null ) {
			return ;
		}
//		this.recalCapacity(locationId);
		int count = this.addCapacity(metaLocationVO);
		if ( count <= 0 ) {
			throw new BizException("err_main_location_capacityNotEnough",location.getLocationName());
		}
	}
	/** 
	* @Title: addCapacity 
	* @Description: TODO
	<update id="addCapacity" parameterType="MetaLocationVO">
		UPDATE meta_location
		<set>
			<if test="location.usedCapacity != null">
				used_capacity = used_capacity + #{location.usedCapacity}
			</if>
			<if test="location.preusedCapacity != null">
				, preused_capacity = preused_capacity + #{location.preusedCapacity}
			</if>
			 , capacity_use_rate=ROUND(used_capacity/max_capacity*100,2)
		</set>
		WHERE
			location_id = #{location.locationId}
			<if test="location.usedCapacity != null and location.usedCapacity != 0">
				<if test="location.usedCapacity &gt; 0">
					 and max_capacity - used_capacity - preused_capacity >= #{location.usedCapacity}
				</if>
				<if test="location.usedCapacity &lt; 0">
					 and #{location.usedCapacity} + used_capacity >= 0 
				</if>
			</if>
			<if test="location.preusedCapacity != null and location.preusedCapacity != 0">
				<if test="location.usedCapacity > 0">
					 and preused_capacity >= #{location.preusedCapacity}
				</if>
			</if>
	</update>
	* @auth tphe06
	* @time 2018 2018年8月9日 下午4:32:58
	* @param vo
	* @return
	* int
	*/
	private int addCapacity(MetaLocationVO vo) {
		MetaLocation l_new = vo.getLocation();
		MetaLocation l_old = dao.selectByPrimaryKey(l_new.getLocationId());
		if(l_old == null) return 0;
		if(l_new.getUsedCapacity() != null && NumberUtil.round(l_new.getUsedCapacity().doubleValue(), LENGTH) != 0) {
			if(NumberUtil.round(l_new.getUsedCapacity().doubleValue(), LENGTH) > 0) {
				if(NumberUtil.round(l_old.getMaxCapacity().doubleValue()-l_old.getUsedCapacity().doubleValue()-l_old.getPreusedCapacity().doubleValue()-l_new.getUsedCapacity().doubleValue(), LENGTH) < 0) return 0;
			}
			if(NumberUtil.round(l_new.getUsedCapacity().doubleValue(), LENGTH) < 0) {
				if(NumberUtil.round(l_new.getUsedCapacity().doubleValue()+l_old.getUsedCapacity().doubleValue(), LENGTH) < 0) return 0;
			}
		}
		if(l_new.getPreusedCapacity() != null && NumberUtil.round(l_new.getPreusedCapacity().doubleValue(), LENGTH) != 0) {
			if(NumberUtil.round(l_new.getUsedCapacity().doubleValue(), LENGTH) > 0) {
				if(NumberUtil.round(l_old.getPreusedCapacity().doubleValue()-l_new.getPreusedCapacity().doubleValue(), LENGTH) < 0) return 0;
			}
		}
		//已用库容度
		if(l_new.getUsedCapacity() != null) {
			BigDecimal used = new BigDecimal(l_old.getUsedCapacity().doubleValue()+l_new.getUsedCapacity().doubleValue());
			l_old.setUsedCapacity(NumberUtil.rounding(used.doubleValue(), LENGTH));
		}
		//占用库容度
		if(l_new.getPreusedCapacity() != null) {
			BigDecimal preused = new BigDecimal(l_old.getPreusedCapacity().doubleValue()+l_new.getPreusedCapacity().doubleValue());
			l_old.setPreusedCapacity(NumberUtil.rounding(preused.doubleValue(), LENGTH));
		}
		//库容占用率
		BigDecimal rate = new BigDecimal(l_old.getUsedCapacity().doubleValue()*100/l_old.getMaxCapacity().doubleValue());
		l_old.setCapacityUseRate(NumberUtil.rounding(rate.doubleValue(), 2));
		return dao.updateByPrimaryKey(l_old);
	}

	/**
	 * 根据id，查询库位信息
	 * @param locationId 库位id
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年2月23日 下午2:49:45<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findLocById ( String locationId ) throws Exception {
		if ( StringUtil.isTrimEmpty(locationId) ) {
			log.error("findLocById --> locationId is null!");
			return null;
		}
		return this.dao.selectByPrimaryKey(locationId);
//		MetaLocationVO metaLocationVO = new MetaLocationVO(new MetaLocation());
//		metaLocationVO.getLocation().setLocationId(locationId);
//		return this.findLoc(metaLocationVO);
	}
	
	
	/**
	 * 根据条件，查询库位
	 * @param metaLocationVO 查询条件
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年2月23日 下午2:45:34<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findLoc ( MetaLocationVO metaLocationVO ) throws Exception {
		List<MetaLocation> listLocation = this.listLocByExample(metaLocationVO);
		return PubUtil.isEmpty(listLocation) ? null : listLocation.get(0) ;
	}
	
	
	/**
	 * 根据条件，查询库位
	 * @param metaLocationVO 查询条件
	 * @return 库位集合
	 * @throws Exception
	 * @version 2017年2月23日 下午2:31:44<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocation> listLocByExample ( MetaLocationVO metaLocationVO ) throws Exception {
		if ( metaLocationVO == null || metaLocationVO.getLocation() == null ) {
			log.error("listLoc --> metaLocationVO is null!");
			return null;
		}
		metaLocationVO.loginInfo();
		Example example = metaLocationVO.getExample();
		return this.dao.selectByExample(example);
	}
	
	
	/**
	 * 根据库位id，查询库位信息
	 * @param listLocId 库位id集合
	 * @return 库位集合
	 * @throws Exception
	 * @version 2017年3月6日 上午11:51:49<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocation> listLocById ( List<String> listLocId ) throws Exception {
		if ( PubUtil.isEmpty(listLocId) ) {
			log.error("listLocById --> listLocId is null!");
			return null;
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO(new MetaLocation());
		metaLocationVO.setListLocationId(listLocId);
		return this.listLocByExample(metaLocationVO);
	}

	@Override
	public MetaLocation findLoc(MetaLocation entity) {
		return dao.selectOne(entity);
	}

	@Override
	public MetaLocation findLocByT() {
		Principal p = LoginUtil.getLoginUser();
		MetaLocation location = new MetaLocation();
		location.setOrgId(p.getOrgId());
		location.setWarehouseId(LoginUtil.getWareHouseId());
		location.setLocationNo("T");
		location = findLoc(location);
		return location;
	}
}