/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:38:10<br/>
 * @author andy wang<br/>
 */
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
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.saas.modules.sys.entity.MetaLocationSpec;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.INoRule;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationSpecExtlService;
import com.yunkouan.wms.modules.meta.service.ILocationSpecService;
import com.yunkouan.wms.modules.meta.vo.MetaLocationSpecVO;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位规格业务类<br/><br/>
 * @version 2017年3月13日下午2:38:10<br/>
 * @author andy wang<br/>
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class LocationSpecServiceImpl extends BaseService implements ILocationSpecService {

	/** 日志对象 <br/> add by andy wang */
	private Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);

	/** 库位规格外调业务类 <br/> add by andy */
	@Autowired
	private ILocationSpecExtlService locSpecExtlService;
	
	/** 库位外调业务类 <br/> add by andy wang */
	@Autowired
	private ILocationExtlService locExtlService;

	/** 仓库外调业务类 <br/> add by andy wang */
	@Autowired
	private IWarehouseExtlService warehouseExtlService;

	/** 公共业务类 <br/> add by andy wang */
//	private CommonServiceImpl commonServicel;

	/** 库位业务类 <br/> add by andy wang */
	@Autowired
	private ILocationExtlService locationExtlService;
	
	

	/**
	 * 更新并生效库位规格
	 * @param param_specVO 库位规格对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO updateAndEnable ( MetaLocationSpecVO param_specVO ) throws Exception {
		if ( param_specVO == null || param_specVO.getLocSpec() == null ) {
    		log.error("updateAndEnable --> param_specVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		this.updateLocSpec(param_specVO);
		this.activeLocSpec(Arrays.asList(param_specVO.getLocSpec().getSpecId()));
		param_specVO.getLocSpec().setSpecStatus(Constant.LOCATIONSPEC_STATUS_ACTIVE);
		return param_specVO;
	}
	
	/**
	 * 保存并生效库位规格
	 * @param param_specVO 库位规格对象
	 * @return 生效后的库位规格
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO addAndEnable ( MetaLocationSpecVO param_specVO ) throws Exception {
		if ( param_specVO == null || param_specVO.getLocSpec() == null ) {
    		log.error("addAndEnable --> param_specVO is null!");
    		throw new NullPointerException("parameter is null!");
    	}
		MetaLocationSpecVO insertSpecVO = this.insertLocSpec(param_specVO);
		if ( insertSpecVO == null 
				|| StringUtil.isTrimEmpty(insertSpecVO.getLocSpec().getSpecId()) ) {
			throw new NullPointerException("addAndEnable return is null!");
		}
		this.activeLocSpec(Arrays.asList(param_specVO.getLocSpec().getSpecId()));
		param_specVO.getLocSpec().setSpecStatus(Constant.LOCATIONSPEC_STATUS_ACTIVE);
		return insertSpecVO;
	}
	
	
	/**
	 * 页面显示库位规格
	 * @param param_locSpecVO 查询条件
	 * @return
	 * @throws Exception
	 * @version 2017年4月4日 下午2:25:12<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocationSpecVO> showLocSpec ( MetaLocationSpecVO param_locSpecVO ) throws Exception {
		param_locSpecVO.getLocSpec().setSpecStatus(Constant.LOCATIONSPEC_STATUS_ACTIVE);
		List<MetaLocationSpec> listLocSpec = this.locSpecExtlService.listLocSpecByExample(param_locSpecVO);
		if ( PubUtil.isEmpty(listLocSpec) ) {
			return null;
		}
		List<MetaLocationSpecVO> listLocSpecVO = new ArrayList<MetaLocationSpecVO>();
		for (MetaLocationSpec metaLocationSpec : listLocSpec) {
			listLocSpecVO.add(new MetaLocationSpecVO(metaLocationSpec));
		}
		return listLocSpecVO;
	}
	
	
	/**
	 * 更新库位规格
	 * @param param_locSpecVO 库位规格信息
	 * @throws Exception
	 * @version 2017年3月13日下午2:38:10<br/>
	 * @author andy wang<br/>
	 */
	public void updateLocSpec(MetaLocationSpecVO param_locSpecVO) throws Exception {
		if (param_locSpecVO == null || param_locSpecVO.getLocSpec() == null) {
			log.error("updateLocSpec --> param_locSpecVO or locSpec is null!");
			throw new NullPointerException("parameter is null!");
		}
		MetaLocationSpec param_locSpec = param_locSpecVO.getLocSpec();
		MetaLocationSpec locSpec = this.locSpecExtlService.findLocSpecById(param_locSpec.getSpecId());
		if (locSpec.getSpecStatus() == null || Constant.LOCATIONSPEC_STATUS_OPEN != locSpec.getSpecStatus()) {
			throw new BizException("err_main_locSpec_update_statusNotOpen");
		}
		// 更新库位规格
		this.locSpecExtlService.updateLocSpec(param_locSpecVO);
		if ( param_locSpec.getMaxCapacity() != null 
				&& locSpec.getMaxCapacity() != null 
				&& param_locSpec.getMaxCapacity().compareTo(locSpec.getMaxCapacity()) != 0 ) {
			// 最大库容发生变化时，需更新所有相关库位
			// 更新库位的最大库容
			this.locExtlService.updateMaxCapacity(locSpec.getSpecId(), param_locSpec.getMaxCapacity());
		}
	}

	/**
	 * 失效库位规格
	 * @param param_listLocSpecId 库位规格id集合
	 * @throws Exception
	 * @version 2017年3月13日下午2:38:10<br/>
	 * @author andy wang<br/>
	 */
	public void inactiveLocSpec(List<String> param_listLocSpecId) throws Exception {
		if (param_listLocSpecId == null || param_listLocSpecId.isEmpty()) {
			log.error("inactiveLocSpec --> param_listLocSpecId is null!");
			throw new NullPointerException("parameter is null!");
		}

		for (int i = 0; i < param_listLocSpecId.size(); i++) {
			String locSpecId = param_listLocSpecId.get(i);
			if (StringUtil.isTrimEmpty(locSpecId)) {
				throw new NullPointerException("parameter id is null");
			}
			// 查询仓库
			MetaLocationSpec locSpec = this.locSpecExtlService.findLocSpecById(locSpecId);
			if (locSpec == null) {
				throw new BizException("err_main_locSpec_null");
			}

			if (Constant.LOCATIONSPEC_STATUS_ACTIVE != locSpec.getSpecStatus()) {
				throw new BizException("err_main_locSpec_inactive_statusNotActive");
			}
			
			MetaLocationVO p_locVO = new MetaLocationVO();
			p_locVO.getLocation().setSpecId(locSpecId);
			List<MetaLocation> listLoc = this.locationExtlService.listLocByExample(p_locVO);
			if ( listLoc == null || listLoc.size() > 0 ) {
        		throw new BizException("err_main_locSpec_inactive_hasLocation");
        	}
			
			// 更新状态
			this.locSpecExtlService.updateLocSpecStatusById(locSpecId, Constant.LOCATIONSPEC_STATUS_OPEN);
		}
	}

	/**
	 * 生效库位规格
	 * @param param_listLocSpecId 库位规格id集合
	 * @throws Exception
	 * @version 2017年3月13日下午2:38:10<br/>
	 * @author andy wang<br/>
	 */
	public void activeLocSpec(List<String> param_listLocSpecId) throws Exception {
		if (param_listLocSpecId == null || param_listLocSpecId.isEmpty()) {
			log.error("activeLocSpec --> param_listLocSpecId is null!");
			throw new NullPointerException("parameter is null!");
		}

		for (int i = 0; i < param_listLocSpecId.size(); i++) {
			String locSpecId = param_listLocSpecId.get(i);
			if (StringUtil.isTrimEmpty(locSpecId)) {
				throw new NullPointerException("parameter id is null");
			}
			// 查询仓库
			MetaLocationSpec locSpec = this.locSpecExtlService.findLocSpecById(locSpecId);
			if (locSpec == null) {
				throw new BizException("err_main_locSpec_null");
			}

			if (Constant.LOCATIONSPEC_STATUS_OPEN != locSpec.getSpecStatus()) {
				throw new BizException("err_main_locSpec_active_statusNotOpen");
			}

			// 更新状态
			this.locSpecExtlService.updateLocSpecStatusById(locSpecId, Constant.LOCATIONSPEC_STATUS_ACTIVE);
		}
	}

	/**
	 * 根据库位规格id，查询库位规格信息
	 * @param param_locSpecId 库位规格id
	 * @return 库位规格信息
	 * @throws Exception
	 * @version 2017年3月13日下午2:38:10<br/>
	 * @author andy wang<br/>
	 */
	@Transactional(readOnly = true)
	public MetaLocationSpecVO viewLocSpec(String param_locSpecId) throws Exception {
		if (StringUtil.isTrimEmpty(param_locSpecId)) {
			log.error("viewLocSpec --> param_locSpecId is null!");
			throw new NullPointerException("parameter is null!");
		}
		// 根据id，查询库位规格
		MetaLocationSpec locSpec = this.locSpecExtlService.findLocSpecById(param_locSpecId);
		if (locSpec == null) {
			// 库位规格不存在
			throw new BizException("err_main_locSpec_null");
		}
		return new MetaLocationSpecVO(locSpec);
	}

	/**
	 * 保存库位规格
	 * @param param_locSpecVO 要保存的库位规格
	 * @return 保存后的库位规格（包含id）
	 * @throws Exception
	 * @version 2017年3月13日下午2:38:10<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO insertLocSpec(MetaLocationSpecVO param_locSpecVO) throws Exception {
		if (param_locSpecVO == null || param_locSpecVO.getLocSpec() == null) {
			log.error("insertLocSpec --> param_locSpecVO is null!");
			throw new NullPointerException("parameter is null!");
		}
		MetaLocationSpec locSpec = param_locSpecVO.getLocSpec();
		// 生成库位规格编号
		Principal loginUser = LoginUtil.getLoginUser();
//		CommonVo p_commonVO = new CommonVo(loginUser.getOrgId(), BillPrefix.DOCUMENT_ADJUST_BILL);
//		String no = this.commonServicel.getNo(p_commonVO);
//		locSpec.setSpecNo(no);
		INoRule rule = context.getStrategy4No(loginUser.getOrgId(), LoginUtil.getWareHouseId());
		locSpec.setSpecNo(rule.getLocationSpecNo(loginUser.getOrgId()));

//		// 查询库位规格编号是否有重复
//		MetaLocationSpec existsLocSpec = this.locSpecExtlService.findLocSpecByNo(locSpec.getSpecNo());
//		if (existsLocSpec != null) {
//			throw new BizException("err_main_locSpec_no_exists");
//		}
		// 设置id
		String locSpecId = IdUtil.getUUID();
		locSpec.setSpecId(locSpecId);
		// 设置默认状态
		locSpec.setSpecStatus(Constant.LOCATIONSPEC_STATUS_OPEN);
		// 保存库位规格
		MetaLocationSpec metaLocationSpec = this.locSpecExtlService.insertLocSpec(locSpec);
		if (metaLocationSpec == null) {
			return null;
		}
		return new MetaLocationSpecVO(metaLocationSpec);
	}

	/**
	 * 根据条件，分页查询库位规格
	 * @param param_metaLocationSpecVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日下午2:38:10<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public Page listLocSpec(MetaLocationSpecVO param_metaLocationSpecVO) throws Exception {
		if (param_metaLocationSpecVO == null) {
			param_metaLocationSpecVO = new MetaLocationSpecVO();
		}
		if (param_metaLocationSpecVO.getLocSpec() == null) {
			param_metaLocationSpecVO.setLocSpec(new MetaLocationSpec());
		}
		Page page = this.locSpecExtlService.listLocSpecByPage(param_metaLocationSpecVO);
		if (PubUtil.isEmpty(page)) {
			return null;
		}
		List<MetaLocationSpecVO> listLocSpecVO = new ArrayList<MetaLocationSpecVO>();
		for (Object obj : page) {
			MetaLocationSpec metaLocationSpec = (MetaLocationSpec) obj;
			MetaLocationSpecVO metaLocationSpecVO = new MetaLocationSpecVO(metaLocationSpec).searchCache();
			listLocSpecVO.add(metaLocationSpecVO);
			metaLocationSpecVO.setWarehouseComment(FqDataUtils.getWarehouseNameById(this.warehouseExtlService, metaLocationSpec.getWarehouseId()));
		}
		page.clear();
		page.addAll(listLocSpecVO);
		return page;
	}

}