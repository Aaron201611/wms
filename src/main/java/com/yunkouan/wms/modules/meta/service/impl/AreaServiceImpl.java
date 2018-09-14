package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.entity.MetaSkuType;
import com.yunkouan.wms.modules.meta.service.IAreaExtlService;
import com.yunkouan.wms.modules.meta.service.IAreaService;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.ISkuTypeService;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库区服务实现类
 */
@Service
public class AreaServiceImpl extends BaseService implements IAreaService {
	/** 日志对象 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/** 库区外调业务类 <br/> add by andy wang */
	@Autowired
	private IAreaExtlService areaExtlService;
	
	/** 仓库外调业务类 <br/> add by andy wang */
	@Autowired
	private IWarehouseExtlService wrhExtlService;
	
	/** 库位业务接口 <br/> add by andy wang */
	@Autowired
	private ILocationExtlService locationExtlService;
	
	/** 月台业务接口 <br/> add by andy wang */
//	@Autowired
//	private IDockExtlService dockExtlService;

	@Autowired
	private ISkuTypeService skuTypeService;

	/**
	 * 更新并生效库区
	 * @param param_areaVO 库区对象
	 * @return 生效后的库区
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO updateAndEnable ( MetaAreaVO param_areaVO ) throws Exception {
		if ( param_areaVO == null || param_areaVO.getArea() == null ) {
    		log.error("updateAndEnable --> param_areaVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		this.updateArea(param_areaVO);
		this.activeArea(Arrays.asList(param_areaVO.getArea().getAreaId()));
		param_areaVO.getArea().setAreaStatus(Constant.AREA_STATUS_ACTIVE);
		return param_areaVO;
	}
	
	/**
	 * 保存并生效库区
	 * @param param_areaVO 库区对象
	 * @return 生效后的库区
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO addAndEnable ( MetaAreaVO param_areaVO ) throws Exception {
		if ( param_areaVO == null || param_areaVO.getArea() == null ) {
    		log.error("addAndEnable --> param_areaVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		MetaAreaVO insertAreaVO = this.insertArea(param_areaVO);
		if ( insertAreaVO == null 
				|| StringUtil.isTrimEmpty(insertAreaVO.getArea().getAreaId()) ) {
			throw new NullPointerException("addAndEnable return is null!");
		}
		this.activeArea(Arrays.asList(param_areaVO.getArea().getAreaId()));
		param_areaVO.getArea().setAreaStatus(Constant.AREA_STATUS_ACTIVE);
		return insertAreaVO;
	}
	
	
	/**
	 * 页面显示库区
	 * @return
	 * @throws Exception
	 * @version 2017年4月4日 下午2:06:17<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaAreaVO> showArea ( MetaAreaVO param_areaVO ) throws Exception {
		List<MetaArea> listArea = this.areaExtlService.listAreaByExample(param_areaVO);
		if ( PubUtil.isEmpty(listArea) ) {
			return new ArrayList<MetaAreaVO>();
		}
		List<MetaAreaVO> listAreaVO = new ArrayList<MetaAreaVO>();
		for (MetaArea metaArea : listArea) {
			listAreaVO.add(new MetaAreaVO(metaArea).searchCache());
		}
		return listAreaVO;
	}
	
	/**
     * 更新库区
     * @param param_areaVO 库区信息
     * @throws Exception
     * @version 2017年2月18日 下午2:44:59<br/>
     * @author andy wang<br/>
     */
    public void updateArea ( MetaAreaVO param_areaVO ) throws Exception {
    	if ( param_areaVO == null || param_areaVO.getArea() == null ) {
			log.error("updateArea --> param_areaVO or area is null!");
    		throw new NullPointerException("parameter is null!");
		}
    	MetaArea existsArea = this.areaExtlService.findAreaByNo(param_areaVO.getArea().getAreaNo(),param_areaVO.getArea().getAreaId());
		if ( existsArea != null ) {
			throw new BizException("err_main_area_no_exists");
		}
    	MetaArea param_area = param_areaVO.getArea();
    	MetaArea area = this.areaExtlService.findAreaById(param_area.getAreaId());
    	if ( area.getAreaStatus() == null || Constant.AREA_STATUS_OPEN != area.getAreaStatus() ) {
    		throw new BizException("err_main_area_update_statusNotOpen");
    	}
    	
		// 20170621 林总决定如果库区已有所属库位，则该库区的库区类型不能修改。
    	if ( area.getAreaType().intValue() != param_area.getAreaType().intValue() ) {
    		MetaLocationVO p_locationVO = new MetaLocationVO();
        	p_locationVO.getLocation().setAreaId(param_areaVO.getArea().getAreaId());
        	List<MetaLocation> listLocation = this.locationExtlService.listLocByExample(p_locationVO);
        	if ( listLocation != null && listLocation.size() > 0 ) {
        		throw new BizException("err_main_area_update_typeHasLocation");
        	}
    	}
    	
    	// 更新仓库
    	this.areaExtlService.updateArea(param_areaVO);
    }
    
	
	/**
	 * 失效库区
	 * @param param_listAreaId 库区id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:17:57<br/>
	 * @author andy wang<br/>
	 */
    public void inactiveArea ( List<String> param_listAreaId ) throws Exception {
    	if ( param_listAreaId == null || param_listAreaId.isEmpty() ) {
    		log.error("inactiveArea --> param_listAreaId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
    	for (int i = 0; i < param_listAreaId.size(); i++) {
    		String areaId = param_listAreaId.get(i);
    		if ( StringUtil.isTrimEmpty(areaId) ) {
    			throw new NullPointerException("parameter id is null");
    		}
    		// 查询仓库
        	MetaArea area = this.areaExtlService.findAreaById(areaId);
        	if ( area == null ) {
        		throw new BizException("err_main_area_null");
        	}
        	
        	if ( Constant.AREA_STATUS_ACTIVE != area.getAreaStatus() ) {
        		throw new BizException("err_main_area_inactive_statusNotActive");
        	}

        	//20180508根据新增需求，默认库区不能修改
        	if ( area.getAreaType().intValue() == Constant.AREA_TYPE_SEND 
        			|| area.getAreaType().intValue() == Constant.AREA_TYPE_TEMPSTORAGE) {
        		throw new BizException("err_main_area_inactive_defaultCannotInactive");
        	}
        	
        	// 20170619 林总决定库区底下有库位时，不能进行失效库区操作
        	// 20170620 林总决定为方便用户操作，取消这段逻辑
//        	MetaLocationVO p_locationVO = new MetaLocationVO();
////        	p_locationVO.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
//        	p_locationVO.getLocation().setAreaId(areaId);
//        	List<MetaLocation> listLoc = this.locationExtlService.listLocByExample(p_locationVO);
//        	if ( listLoc == null || listLoc.size() > 0 ) {
//        		throw new BizException("err_main_area_inactive_hasLocation");
//        	}
//        	// 20170619 林总决定库区底下有月台时，不能进行失效库区操作
//        	MetaDockVO p_dock = new MetaDockVO();
////        	p_dock.getDock().setDockStatus(Constant.DOCK_STATUS_ACTIVE);
//        	p_dock.getDock().setAreaId(areaId);
//        	List<MetaDock> listDock = this.dockExtlService.listDockByExample(p_dock);
//        	if ( listDock == null || listDock.size() > 0 ) {
//        		throw new BizException("err_main_area_inactive_hasDock");
//        	}
        	
        	// 更新状态
        	this.areaExtlService.updateAreaStatusById(areaId, Constant.AREA_STATUS_OPEN);
		}
    }
	
	/**
	 * 生效库区
	 * @param param_listAreaId 库区id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:16:53<br/>
	 * @author andy wang<br/>
	 */
    public void activeArea ( List<String> param_listAreaId ) throws Exception{
    	if ( param_listAreaId == null || param_listAreaId.isEmpty() ) {
    		log.error("activeArea --> param_listAreaId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
    	
    	for (int i = 0; i < param_listAreaId.size(); i++) {
    		String areaId = param_listAreaId.get(i);
    		if ( StringUtil.isTrimEmpty(areaId) ) {
    			throw new NullPointerException("parameter id is null");
    		}
    		// 查询仓库
        	MetaArea area = this.areaExtlService.findAreaById(areaId);
        	if ( area == null ) {
        		throw new BizException("err_main_area_null");
        	}
        	
        	if ( Constant.AREA_STATUS_OPEN != area.getAreaStatus() ) {
        		throw new BizException("err_main_area_active_statusNotOpen");
        	}
        	
        	
        	// 20170619 林总决定所属仓库处于打开状态，库区不能进行生效
        	MetaWarehouse warehouse = this.wrhExtlService.findWareHouseById(area.getWarehouseId());
        	if ( warehouse == null ) {
        		throw new BizException ("err_main_warehouse_null");
        	}
        	if ( warehouse.getWarehouseStatus() == null || Constant.WAREHOUSE_STATUS_ACTIVE != warehouse.getWarehouseStatus() ) {
        		throw new BizException ("err_main_area_active_warehouseNotActive");
        	}
        	
        	// 更新状态
        	this.areaExtlService.updateAreaStatusById(areaId, Constant.AREA_STATUS_ACTIVE);
		}
    }
	
	/**
	 * 根据库区id，查询库区信息
	 * @param param_areaId 库区id
	 * @return 库区信息
	 * @throws Exception
	 * @version 2017年3月11日 下午1:57:06<br/>
	 * @author andy wang<br/>
	 */
	@Transactional(readOnly=true)
	public MetaAreaVO viewArea ( String param_areaId ) throws Exception {
		if ( StringUtil.isTrimEmpty(param_areaId) ) {
    		log.error("viewArea --> param_areaId is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		FqDataUtils fq = new FqDataUtils();
		// 根据id，查询库区
		MetaArea area = this.areaExtlService.findAreaById(param_areaId);
		if ( area == null ) {
			// 库区不存在
			throw new BizException("err_main_area_null");
		}
		MetaAreaVO metaAreaVO = new MetaAreaVO(area);
		metaAreaVO.searchCache();
		if ( !StringUtil.isTrimEmpty(area.getWarehouseId()) ) {
			metaAreaVO.setWarehouseComment(fq.getWarehouseNameById(this.wrhExtlService, area.getWarehouseId()));
		}
		// 补充货品类型名称
		MetaSkuType type = skuTypeService.get(area.getSkuTypeId());
		if(type != null) metaAreaVO.setSkuTypeName(type.getSkuTypeName());
		// 20170621 林总决定如果库区已有所属库位，则该库区的库区类型不能修改。
		MetaLocationVO p_locationVO = new MetaLocationVO();
    	p_locationVO.getLocation().setAreaId(area.getAreaId());
    	List<MetaLocation> listLocation = this.locationExtlService.listLocByExample(p_locationVO);
    	metaAreaVO.setHasLocation(listLocation != null && listLocation.size() > 0);
		return metaAreaVO;
	}

	/**
	 * 保存库区
	 * @param param_areaVO 要保存的库区
	 * @return 保存后的库区（包含id）
	 * @throws Exception
	 * @version 2017年3月11日 上午9:43:25<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO insertArea ( MetaAreaVO param_areaVO ) throws Exception {
		if ( param_areaVO == null || param_areaVO.getArea() == null ) {
    		log.error("insertArea --> param_areaVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		MetaArea area = param_areaVO.getArea();
		// 查询库区编号是否有重复
		MetaArea existsArea = this.areaExtlService.findAreaByNo(area.getAreaNo(),null);
		if ( existsArea != null ) {
			throw new BizException("err_main_area_no_exists");
		}
		// 设置id
		String areaId = IdUtil.getUUID();
		area.setAreaId(areaId);
		// 设置默认状态
		area.setAreaStatus(Constant.AREA_STATUS_OPEN);
		// 保存库区
		MetaArea metaArea = this.areaExtlService.insertArea(area);
		if ( metaArea == null ) {
			return null;
		}
		return new MetaAreaVO(metaArea);
	}
	
	/**
	 * 根据条件，分页查询库区
	 * @param param_metaAreaVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月11日 下午1:33:09<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Transactional(readOnly=true)
	public Page listArea ( MetaAreaVO param_metaAreaVO ) throws Exception {
		if ( param_metaAreaVO == null ) {
			param_metaAreaVO = new MetaAreaVO();
		}
		if ( param_metaAreaVO.getArea() == null ) {
			param_metaAreaVO.setArea(new MetaArea());
		}
		
		if ( !StringUtil.isTrimEmpty(param_metaAreaVO.getLikeWarehouseComment()) ) {
			List<MetaWarehouse> listWrh = this.wrhExtlService.listWareHouseByName(param_metaAreaVO.getLikeWarehouseComment(), true);
			if ( !PubUtil.isEmpty(listWrh) ) {
				List<String> listWrhId = new ArrayList<String>();
				param_metaAreaVO.setListWrhId(listWrhId);
				for (MetaWarehouse metaWarehouse : listWrh) {
					listWrhId.add(metaWarehouse.getWarehouseId());
				}
			}
		}
		
		Page page = this.areaExtlService.listAreaByPage(param_metaAreaVO);
		if ( PubUtil.isEmpty(page) ) {
			return null;
		}
		FqDataUtils fq = new FqDataUtils();
		List<MetaAreaVO> listWrhVO = new ArrayList<MetaAreaVO>();
		for (Object obj : page) {
			MetaArea metaArea = (MetaArea) obj;
			MetaAreaVO metaAreaVO = new MetaAreaVO(metaArea).searchCache();
			listWrhVO.add(metaAreaVO);
			// 获取仓库信息
			metaAreaVO.setWarehouseComment(fq.getWarehouseNameById(this.wrhExtlService, metaArea.getWarehouseId()));
			// 补充货品类型名称
			MetaSkuType type = skuTypeService.get(metaArea.getSkuTypeId());
			if(type != null) metaAreaVO.setSkuTypeName(type.getSkuTypeName());
		}
		page.clear();
		page.addAll(listWrhVO);
		return page;
	}

	@Override
	public void change(MetaAreaVO param_areaVO) throws Exception {
		if ( param_areaVO == null || param_areaVO.getArea() == null ) {
			log.error("updateArea --> param_areaVO or area is null!");
    		throw new NullPointerException("parameter is null!");
		}
    	MetaArea param_area = param_areaVO.getArea();
    	MetaArea area = this.areaExtlService.findAreaById(param_area.getAreaId());
    	if ( area.getAreaType().intValue() != param_area.getAreaType().intValue() ) {
    		MetaLocationVO p_locationVO = new MetaLocationVO();
        	p_locationVO.getLocation().setAreaId(param_areaVO.getArea().getAreaId());
        	List<MetaLocation> listLocation = this.locationExtlService.listLocByExample(p_locationVO);
        	if ( listLocation != null && listLocation.size() > 0 ) {
        		for (MetaLocation location : listLocation) {
        			location.setLocationType(param_area.getAreaType());
        			this.locationExtlService.updateLoc(new MetaLocationVO(location));
        		}
        	}
    	} else {
    		return;
    	}
    	// 更新仓库
    	this.areaExtlService.updateArea(param_areaVO);
	}

}