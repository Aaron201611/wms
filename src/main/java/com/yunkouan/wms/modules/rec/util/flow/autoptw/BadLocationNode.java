/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午5:31:09<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow.autoptw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.meta.service.impl.LocationExtlServiceImpl;
import com.yunkouan.wms.modules.meta.util.LocationUtil;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.util.flow.FlowConstant;
import com.yunkouan.wms.modules.rec.util.flow.FlowContext;
import com.yunkouan.wms.modules.rec.util.flow.FlowNode;
import com.yunkouan.wms.modules.rec.util.flow.autoptw.AutoPutawayFlow.AutoPtwAllot;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;

/**
 * 不良品直接分配第一个不良品库位
 * @version 2018年8月29日11:17:07<br/>
 * @author 王通<br/>
 */
public class BadLocationNode implements FlowNode {
	
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	/** 库位业务类 <br/> add by andy */
	private ILocationExtlService locExtlService;
	
	/** 货品Map <br/> add by andy */
	private Map<String,MetaSku> mapSku;
	
	/** 上下文上架单明细集合 <br/> add by andy */
	private List<RecPutawayDetailVO> listCtxPtwDetail;

	/** 自动分配规则 <br/> add by andy */
	private AutoPtwAllot autoPtwAllot;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean executeBefore(FlowContext fc) throws Exception {
		log.error("进入规则0");
		this.locExtlService = (ILocationExtlService) fc.getBean(LocationExtlServiceImpl.class);
		this.mapSku = (Map<String, MetaSku>) fc.get(FlowConstant.AUTPTW_MAP_SKU);
		this.listCtxPtwDetail = (List<RecPutawayDetailVO>) fc.get(FlowConstant.AUTOPTW_LIST_PTWDETAILVO);
		this.autoPtwAllot = (AutoPtwAllot) fc.get(FlowConstant.AUTOPTW_AUTOALLOT);
		return true;
	}

	@Override
	public Boolean execute(FlowContext fc) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();

		// 查询不良品区库位集合
		MetaLocationVO p_location = new MetaLocationVO(new MetaLocation());
		p_location.getLocation().setLocationType(Constant.LOCATION_TYPE_BAD);
		p_location.getLocation().setOrgId(loginUser.getOrgId());
		p_location.getLocation().setWarehouseId(LoginUtil.getWareHouseId());
		p_location.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
		List<MetaLocation> listLocation = this.locExtlService.listLocByExample(p_location);
		if ( listLocation == null || listLocation.isEmpty()) {
			throw new BizException("err_rec_putawayDetail_bad_location_null");
		}
		MetaLocation chooseLocation = listLocation.get(0);
		for (RecPutawayDetailVO recPutawayDetailVO : this.listCtxPtwDetail) {
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			if ( putawayDetail == null ) {
				throw new BizException("err_rec_putawayDetail_null");
			}
			if ( putawayDetail.getSkuStatus() == null || putawayDetail.getSkuStatus() != 20) {
				throw new BizException("err_rec_putawayDetail_bad");
//				continue;
			}
			MetaSku metaSku = this.mapSku.get(putawayDetail.getSkuId());
			// 计算所占库容
			BigDecimal capacity = LocationUtil.capacityConvert(putawayDetail.getPlanPutawayQty(), metaSku);
			recPutawayDetailVO.setCapacity(capacity);
			this.autoPtwAllot.execute(chooseLocation, metaSku, recPutawayDetailVO);
			// 设置库位对象到上架单明细对象
			recPutawayDetailVO.addLocation(chooseLocation);
			log.error("规则0 -> 货品" + metaSku.getSkuName() + " , 批次：" + putawayDetail.getBatchNo() + "，分配库位" + chooseLocation.getLocationName());
		}
		return true;
	}
	
	@Override
	public Boolean executeAfter(FlowContext fc1) throws Exception {
		// 验证是否全部分配完毕
		for (RecPutawayDetailVO recPutawayDetailVO : this.listCtxPtwDetail) {
			if ( recPutawayDetailVO.getPutawayDetail().getPlanPutawayQty() > 0 ) {
				// 还有未分配上架明细,流程继续
				log.error("规则0，未能完全分配");
				return true;
			}
		}
		log.error("规则0，分配完毕");
		return false;
	}
}