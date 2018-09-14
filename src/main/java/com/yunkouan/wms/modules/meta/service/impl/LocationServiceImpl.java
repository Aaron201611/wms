/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日 上午10:45:32<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.yunkouan.saas.modules.sys.entity.MetaLocationSpec;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.vo.SysParamVO;
import com.yunkouan.util.DateUtil;
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
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationService;
import com.yunkouan.wms.modules.meta.service.ILocationSpecExtlService;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.service.IPackService;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

/**
 * 库位业务类<br/><br/>
 * @version 2017年3月13日 上午10:45:32<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class LocationServiceImpl extends BaseService implements ILocationService {

	/** 日志对象 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	
	/** 库位外调业务类 <br/> add by andy */
	@Autowired
	private ILocationExtlService locExtlService;
	
	/** 库区外调业务类 <br/> add by andy */
	@Autowired
	private IAreaExtlService areaExtlService;
	
	/** 库区业务类 <br/> add by andy wang */
	@Autowired
	private IStockService stockService;
	
	/** 包装业务类 <br/> add by andy wang */
	@Autowired
	private IPackService packService;
	
	/** 客商业务类 <br/> add by andy wang */
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private ISysParamService sysParamService;
	
	/** 库位规格业务类 <br/> add by andy wang */
	@Autowired
	private ILocationSpecExtlService locationSpecExtlService;

	@Autowired
	private ISkuService skuService;
	@Autowired
	private ISkuTypeService skuTypeService;

	/**
	 * 更新并生效库位
	 * @param param_locationVO 库位对象
	 * @return 生效后的库位
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO updateAndEnable ( MetaLocationVO param_locationVO ) throws Exception {
		if ( param_locationVO == null || param_locationVO.getLocation() == null ) {
    		log.error("updateAndEnable --> param_locationVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		this.updateLoc(param_locationVO);
		this.activeLoc(Arrays.asList(param_locationVO.getLocation().getLocationId()));
		param_locationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		return param_locationVO;
	}
	
	/**
	 * 保存并生效库位
	 * @param param_locationVO 库位对象
	 * @return 生效后的库位
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO addAndEnable ( MetaLocationVO param_locationVO ) throws Exception {
		if ( param_locationVO == null || param_locationVO.getLocation() == null ) {
    		log.error("addAndEnable --> param_locationVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		MetaLocationVO insertLocVO = this.insertLoc(param_locationVO);
		if ( insertLocVO == null 
				|| StringUtil.isTrimEmpty(insertLocVO.getLocation().getLocationId()) ) {
			throw new NullPointerException("addAndEnable return is null!");
		}
		this.activeLoc(Arrays.asList(param_locationVO.getLocation().getLocationId()));
		param_locationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		return insertLocVO;
	}
	
	
	
	
	/**
	 * 库位库容度重算
	 * @param param_listLocationId 库位id集合
	 * @throws Exception
	 * @version 2017年5月15日 下午6:43:26<br/>
	 * @author andy wang<br/>
	 */
	public void recalCapacity ( List<String> param_listLocationId ) throws Exception {
    	this.locExtlService.recalCapacity(param_listLocationId);
	}
	
	
	/**
     * 更新库区
     * @param param_locVO 库区信息
     * @throws Exception
     * @version 2017年2月18日 下午2:44:59<br/>
     * @author andy wang<br/>
     */
    public void updateLoc ( MetaLocationVO param_locVO ) throws Exception {
    	if ( param_locVO == null || param_locVO.getLocation() == null ) {
			log.error("updateLoc --> param_locVO or location is null!");
    		throw new NullPointerException("parameter is null!");
		}
		// 查询库区编号是否有重复
		MetaLocation existsLoc = this.locExtlService.findLocByNo(param_locVO.getLocation().getLocationNo(),param_locVO.getLocation().getLocationId());
		if ( existsLoc != null ) {
			throw new BizException("err_main_location_no_exists");
		}
    	MetaLocation param_location = param_locVO.getLocation();
    	MetaLocation location = this.locExtlService.findLocById(param_location.getLocationId());
    	if ( location.getLocationStatus() == null || Constant.LOCATION_STATUS_OPEN != location.getLocationStatus() ) {
    		throw new BizException("err_main_location_update_statusNotOpen");
    	}
    	if ( !location.getOwner().equals(param_location.getOwner()) ) {
    		InvStockVO p_stockVo = new InvStockVO(new InvStock());
    		p_stockVo.getInvStock().setLocationId(param_location.getLocationId());
			// 若库位上有库存货品时，该库位不能变更货主
    		List<InvStock> listStock = this.stockService.list(p_stockVo);
    		if ( !PubUtil.isEmpty(listStock) ) {
    			throw new BizException("err_main_location_ownerChange");
    		}
    	}
    	// 更新仓库
    	this.locExtlService.updateLoc(param_locVO);
    }
    
    /**
     * 更新仓库
     * @param param_locVO 库位信息
     * @throws Exception
     * @version 2018年5月30日15:04:00<br/>
     * @author 王通<br/>
     */
    public void changeLoc ( MetaLocationVO param_locVO ) throws Exception {
    	if ( param_locVO == null || param_locVO.getLocation() == null ) {
			log.error("updateLoc --> param_locVO or location is null!");
    		throw new NullPointerException("parameter is null!");
		}
    	if (StringUtil.isBlank(param_locVO.getLocation().getSkuId())) {
    		throw new NullPointerException("parameter is null!");
    	}
		// 查询库区编号是否有重复
		InvStockVO p_stockVo = new InvStockVO(new InvStock());
		p_stockVo.getInvStock().setLocationId(param_locVO.getLocation().getLocationId());
		// 若库位上有库存货品时，该库位不能变更为其他货品
		List<InvStock> listStock = this.stockService.list(p_stockVo);
		if ( !PubUtil.isEmpty(listStock)) {
			for (InvStock stock : listStock) {
				if (!stock.getSkuId().equals(param_locVO.getLocation().getSkuId())) {
					throw new BizException("err_main_location_skuChange");
				}
			}
		}
    	// 更新仓库
    	this.locExtlService.updateLoc(param_locVO);
    }
	
    /**
     * 更新仓库
     * @param param_locVO 库位信息
     * @throws Exception
     * @version 2018年5月30日15:04:00<br/>
     * @author 王通<br/>
     */
    @Override
    public void autoBind () throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	MetaLocationVO paramVo = new MetaLocationVO();
    	paramVo.setSkuIsNull(true);
    	paramVo.setIsEmpty(false);
    	List<Integer> listTypes = new ArrayList<Integer>();
    	listTypes.add(20);
    	listTypes.add(30);
    	paramVo.setListTypes(listTypes);
    	paramVo.getLocation().setWarehouseId(LoginUtil.getWareHouseId());
    	paramVo.getLocation().setOrgId(loginUser.getOrgId());
    	List<MetaLocation> listLoc = this.locExtlService.listLocByExample(paramVo);
		for (MetaLocation loc : listLoc) {
	    	InvStockVO paramStock = new InvStockVO(new InvStock());
	    	paramStock.getInvStock().setLocationId(loc.getLocationId());
			List<InvStock> listStock = this.stockService.list(paramStock);
			if ( !PubUtil.isEmpty(listStock)) {
				Set<String> skuSet = new HashSet<String>();
				for (InvStock stock : listStock) {
					skuSet.add(stock.getSkuId());
				}
				if (skuSet.size() == 1) {
					loc.setSkuId(skuSet.iterator().next());
			    	this.locExtlService.updateLoc(new MetaLocationVO(loc));
				}
			}
		}
    }
    
	/**
	 * 失效库区
	 * @param param_listLocId 库区id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:17:57<br/>
	 * @author andy wang<br/>
	 */
    public void inactiveLoc ( List<String> param_listLocId ) throws Exception {
    	if ( param_listLocId == null || param_listLocId.isEmpty() ) {
    		log.error("inactiveLoc --> param_listLocId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
    	for (int i = 0; i < param_listLocId.size(); i++) {
    		String locationId = param_listLocId.get(i);
    		if ( StringUtil.isTrimEmpty(locationId) ) {
    			throw new NullPointerException("parameter id is null");
    		}
    		// 查询仓库
        	MetaLocation location = this.locExtlService.findLocById(locationId);
        	if ( location == null ) {
        		throw new BizException("err_main_location_null");
        	}
        	
        	if ( Constant.LOCATION_STATUS_ACTIVE != location.getLocationStatus() ) {
        		throw new BizException("err_main_location_inactive_statusNotActive");
        	}
        	//20180508根据新增需求，默认库位不能修改
        	if ( location.getLocationType().intValue() == Constant.LOCATION_TYPE_SEND 
        			|| location.getLocationType().intValue() == Constant.LOCATION_TYPE_TEMPSTORAGE) {
        		throw new BizException("err_main_location_inactive_defaultCannotInactive");
        	}
        	// 如果库位有货品的情况，不能进行失效
        	InvStockVO p_stockVo = new InvStockVO(new InvStock());
        	p_stockVo.getInvStock().setLocationId(locationId);
			List<InvStock> listStock = this.stockService.list(p_stockVo);
        	if ( !PubUtil.isEmpty(listStock) ) {
        		throw new BizException("err_main_location_inactive_stockExists");
        	}
        	
        	// 更新状态
        	this.locExtlService.updateLocStatusById(locationId, Constant.LOCATION_STATUS_OPEN);
		}
    }
	
	/**
	 * 生效库区
	 * @param param_listLocId 库区id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:16:53<br/>
	 * @author andy wang<br/>
	 */
    public void activeLoc ( List<String> param_listLocId ) throws Exception{
    	if ( param_listLocId == null || param_listLocId.isEmpty() ) {
    		log.error("activeLoc --> param_listLocId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
    	for (int i = 0; i < param_listLocId.size(); i++) {
    		String locId = param_listLocId.get(i);
    		if ( StringUtil.isTrimEmpty(locId) ) {
    			throw new NullPointerException("parameter id is null");
    		}
    		// 查询仓库
        	MetaLocation location = this.locExtlService.findLocById(locId);
        	if ( location == null ) {
        		throw new BizException("err_main_location_null");
        	}
        	
        	if ( Constant.LOCATION_STATUS_OPEN != location.getLocationStatus() ) {
        		throw new BizException("err_main_location_active_statusNotOpen");
        	}
        	
        	// 201700620 增加逻辑判断库位所属库区状态，如果为非生效，不能进行生效操作
        	MetaArea area = this.areaExtlService.findAreaById(location.getAreaId());
        	if ( area == null ) {
        		throw new BizException("err_main_area_null");
        	}
        	if ( Constant.AREA_STATUS_ACTIVE != area.getAreaStatus() ) {
        		throw new BizException("err_main_location_active_areaNotActive");
        	}
        	
        	
        	// 更新状态
        	this.locExtlService.updateLocStatusById(locId, Constant.LOCATION_STATUS_ACTIVE);
		}
    }
	
	/**
	 * 根据库区id，查询库区信息
	 * @param param_locationId 库区id
	 * @return 库区信息
	 * @throws Exception
	 * @version 2017年3月11日 下午1:57:06<br/>
	 * @author andy wang<br/>
	 */
	@Transactional(readOnly=true)
	public MetaLocationVO viewLoc ( String param_locationId ) throws Exception {
		if ( StringUtil.isTrimEmpty(param_locationId) ) {
    		log.error("viewLoc --> param_locationId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		// 根据id，查询库区
		MetaLocation location = this.locExtlService.findLocById(param_locationId);
		if ( location == null ) {
			// 库区不存在
			throw new BizException("err_main_location_null");
		}
		MetaLocationVO metaLocationVO = new MetaLocationVO(location);
		if ( !StringUtil.isTrimEmpty(location.getOwner()) ) {
			metaLocationVO.setOwnerComment(FqDataUtils.getMerchantNameById(this.merchantService, location.getOwner()));
		}
		if ( !StringUtil.isTrimEmpty(location.getSpecId()) ) {
			MetaLocationSpec locSpec = this.locationSpecExtlService.findLocSpecById(location.getSpecId());
			if ( locSpec != null ) {
				metaLocationVO.setSpecComment(locSpec.getSpecName());
			}
		}
		if ( StringUtil.isNoneBlank(location.getSkuId()) ) {
			SkuVo skuVo = (SkuVo) this.skuService.view(location.getSkuId(), LoginUtil.getLoginUser()).getObj();
			if ( skuVo != null ) {
				metaLocationVO.setSkuVo(skuVo);
			}
		}
		MetaArea area = FqDataUtils.getAreaById(this.areaExtlService, location.getAreaId());
		metaLocationVO.setAreaComment(area.getAreaName());
		metaLocationVO.setAreaTypeComment(new MetaAreaVO(area).searchCache().getAreaTypeComment());
		metaLocationVO.setSkuTypeId(area.getSkuTypeId());
		return metaLocationVO;
	}
	
	/**
	 * 保存库区
	 * @param param_locationVO 要保存的库区
	 * @return 保存后的库区（包含id）
	 * @throws Exception
	 * @version 2017年3月11日 上午9:43:25<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO insertLoc ( MetaLocationVO param_locationVO ) throws Exception {
		if ( param_locationVO == null || param_locationVO.getLocation() == null ) {
    		log.error("insertLoc --> param_locationVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		MetaLocation location = param_locationVO.getLocation();
		// 查询库区编号是否有重复
		MetaLocation existsLoc = this.locExtlService.findLocByNo(location.getLocationNo(),null);
		if ( existsLoc != null ) {
			throw new BizException("err_main_location_no_exists");
		}
		// 设置id
		String locationId = IdUtil.getUUID();
		location.setLocationId(locationId);
		// 设置默认状态
		location.setLocationStatus(Constant.LOCATION_STATUS_OPEN);
		// 保存库区
		MetaLocation metaLocation = this.locExtlService.insertLoc(location);
		if ( metaLocation == null ) {
			return null;
		}
		return new MetaLocationVO(metaLocation);
	}
	
	/**
	 * 根据条件，分页查询库区
	 * @param param_metaLocationVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月11日 下午1:33:09<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Transactional(readOnly=true)
	public Page listLoc ( MetaLocationVO param_metaLocationVO, boolean isTouch ) throws Exception {
		if ( param_metaLocationVO == null ) {
			param_metaLocationVO = new MetaLocationVO();
		}
		if ( param_metaLocationVO.getLocation() == null ) {
			param_metaLocationVO.setLocation(new MetaLocation());
		}
		//根据货品id/库位名称查找相同货主和货品类型的库位 TODO
		Page page1 = new Page(param_metaLocationVO.getCurrentPage(), param_metaLocationVO.getPageSize());
		MetaAreaVO vo1 = new MetaAreaVO();
		if(StringUtils.isNoneBlank(param_metaLocationVO.getSkuId())) {
			MetaSku sku = skuService.get(param_metaLocationVO.getSkuId());
			if(sku == null) return page1;
			//设置货主
			param_metaLocationVO.getLocation().setOwner(sku.getOwner());
			MetaSkuType type = skuTypeService.get(sku.getSkuTypeId());
			if(type == null) return page1;
			//只支持二级
//			vo1.getArea().setSkuTypeId(type.getParentId());
			//支持多级
			String no = type.getLevelCode().substring(0, 3);
			MetaSkuType type1 = skuTypeService.get(new MetaSkuType().setLevelCode(no));
			if(type1 == null) return page1;
			vo1.getArea().setSkuTypeId(type1.getSkuTypeId());
			//新增，设置skuId匹配
			//改为配置方式 by 王通
			SysParamVO vo = sysParamService.viewSysParam("1196");
			if (vo.getSysParam().getParamKey().equals("1")) {
				param_metaLocationVO.getLocation().setSkuId(param_metaLocationVO.getSkuId());
			}
		}
		if(StringUtils.isNoneBlank(param_metaLocationVO.getSkuNoLike())) {
			List<MetaSku> skuList = skuService.qryAllList(new SkuVo().setSkuNoLike(param_metaLocationVO.getSkuNoLike()), LoginUtil.getLoginUser());
			if(skuList == null || skuList.isEmpty()) return page1;
			List<String> skuIdList = new ArrayList<String>();
			//新增，设置skuId匹配
			for (MetaSku sku : skuList) {
				skuIdList.add(sku.getSkuId());
			}
			param_metaLocationVO.setListSkuId(skuIdList);
		}
		vo1.setListTypes(param_metaLocationVO.getListTypes());
		vo1.setLikeAreaName(param_metaLocationVO.getLikeAreaName());
		List<MetaArea> listArea = areaExtlService.listAreaByExample(vo1);
		if(PubUtil.isEmpty(listArea)) return page1;
		List<String> listAreaId = new ArrayList<String>();
		for(MetaArea area : listArea) {
			listAreaId.add(area.getAreaId());
		}
		param_metaLocationVO.setListAreaId(listAreaId);
		// 模糊查找库区名称
//		if ( !StringUtil.isTrimEmpty(param_metaLocationVO.getLikeAreaName()) ) {
//			List<MetaArea> listArea = this.areaExtlService.listAreaByName(param_metaLocationVO.getLikeAreaName(), true);
//			if ( PubUtil.isEmpty(listArea) ) {
//				Page page = new Page();
//				page.setPageSize(param_metaLocationVO.getPageSize());
//				page.setPageNum(param_metaLocationVO.getCurrentPage());
//				return page;
//			} else {
//				List<String> listAreaId = new ArrayList<String>();
//				for (MetaArea area : listArea) {
//					listAreaId.add(area.getAreaId());
//				}
//				param_metaLocationVO.setListAreaId(listAreaId);
//			}
//		}
		//按条件分页查询库位
		param_metaLocationVO.setOrderByLocationNoAsc(true);
		Page page = this.locExtlService.listLocByPage(param_metaLocationVO);
		if ( PubUtil.isEmpty(page) ) return null;
		//封装库位扩展信息
		List<MetaLocationVO> listLocVO = new ArrayList<MetaLocationVO>();
		Date startTime = DateUtil.setStartTime(new Date());
		Date endTime = DateUtil.setEndTime(new Date());
		for (Object obj : page) {
			MetaLocation metaLocation = (MetaLocation) obj;
			MetaLocationVO metaLocationVO = new MetaLocationVO(metaLocation).searchCache();
			listLocVO.add(metaLocationVO);
			//增加货品对象
			if ( StringUtil.isNoneBlank(metaLocation.getSkuId()) ) {
				SkuVo skuVo = (SkuVo) this.skuService.view(metaLocation.getSkuId(), LoginUtil.getLoginUser()).getObj();
				if ( skuVo != null ) {
					metaLocationVO.setSkuVo(skuVo);
				}
			}
			// 获取货主信息
			metaLocationVO.setOwner(FqDataUtils.getMerchantById(this.merchantService, metaLocation.getOwner()));
			// 获取库区信息
			metaLocationVO.setAreaComment(FqDataUtils.getAreaNameById(this.areaExtlService, metaLocation.getAreaId()));
			// 获取包装信息
			metaLocationVO.setPackComment(FqDataUtils.getPackUnit(this.packService, metaLocation.getPackId()));
			if(isTouch) {
				// 库存日志动碰次数
				Integer touchTimes = this.stockService.countLocationTouch(metaLocation.getLocationId(), startTime, endTime);
				metaLocationVO.setTouchTimes(touchTimes);
			}
		}
		page.clear();
		page.addAll(listLocVO);
		return page;
	}

	@Override
	public MetaLocationVO findLoc(MetaLocationVO vo) {
		MetaLocation location = locExtlService.findLoc(vo.getLocation());
		return new MetaLocationVO(location);
	}
}