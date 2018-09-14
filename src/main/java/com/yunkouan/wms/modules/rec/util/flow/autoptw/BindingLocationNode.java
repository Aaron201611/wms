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
 * 相同SKU、相同批次货品，按库位空余量从大到小排序自动分配，
 * 若有剩余货品无法分配，则进行下一节点的分配<br/><br/>
 * @version 2017年3月5日 下午5:31:09<br/>
 * @author andy wang<br/>
 */
public class BindingLocationNode implements FlowNode {
	
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
		
		// 查询有绑定同样货品的库位，并且有足够库容的库位 add by wt
		List<String> listSku = new ArrayList<String>();
		for (RecPutawayDetailVO recPutawayDetailVO : this.listCtxPtwDetail) {
			RecPutawayDetail putawayDetail = recPutawayDetailVO.getPutawayDetail();
			if ( putawayDetail == null ) {
				throw new BizException("err_rec_putawayDetail_null");
			}
			if ( !listSku.contains(putawayDetail.getSkuId()) ) {
				listSku.add(putawayDetail.getSkuId());
			}
			
			// 查询对应skuId的库位集合
			MetaLocationVO p_location = new MetaLocationVO(new MetaLocation());
			p_location.getLocation().setLocationType(Constant.LOCATION_TYPE_STORAGE);
			p_location.getLocation().setBatchNo(putawayDetail.getBatchNo());
			p_location.getLocation().setSkuId(putawayDetail.getSkuId());
			p_location.getLocation().setOrgId(loginUser.getOrgId());
			p_location.getLocation().setWarehouseId(LoginUtil.getWareHouseId());
			p_location.getLocation().setLocationStatus(Constant.LOCATION_STATUS_ACTIVE);
			List<MetaLocation> listLocation = this.locExtlService.listLocByExample(p_location);
			p_location.getLocation().setBatchNo(null);
			p_location.setBatchNoIsNull(true);
			List<MetaLocation> listLocationNoBatch = this.locExtlService.listLocByExample(p_location);
//			listLocation.addAll(listLocationNullBatch);
			if ( PubUtil.isEmpty(listLocation) && PubUtil.isEmpty(listLocationNoBatch)) {
				return true;
			}
			// 遍历库位，选择库容足够的库位，进行匹配
			// 获取货品
			MetaSku metaSku = this.mapSku.get(putawayDetail.getSkuId());
			if ( metaSku == null ) {
				throw new BizException("err_meta_sku_null");
			}
			log.error("规则0 -> 货品" + metaSku.getSkuName() + " , 批次：" + putawayDetail.getBatchNo() + "，进行匹配");
			// 分配上架货位
			// 计算所占库容
			BigDecimal capacity = LocationUtil.capacityConvert(putawayDetail.getPlanPutawayQty(), metaSku);
			recPutawayDetailVO.setCapacity(capacity);
			//遍历库位，将货品分配到绑定库位
			for (MetaLocation metaLocation : listLocation) {
				if ( putawayDetail.getPlanPutawayQty() <= 0 ) {
					// 已经分配完毕
					break;
				}
				if ( StringUtil.isTrimEmpty(metaLocation.getOwner()) 
						|| StringUtil.isTrimEmpty(putawayDetail.getOwner())
						|| !metaLocation.getOwner().equals(putawayDetail.getOwner()) ) {
					// 库位设置的货主与货品的货主一致，才能分配
					continue;
				}
				this.autoPtwAllot.execute(metaLocation, metaSku, recPutawayDetailVO);
				// 设置库位对象到上架单明细对象
				recPutawayDetailVO.addLocation(metaLocation);
				log.error("规则0 -> 货品" + metaSku.getSkuName() + " , 批次：" + putawayDetail.getBatchNo() + "，分配库位" + metaLocation.getLocationName());
			}
			for (MetaLocation metaLocation : listLocationNoBatch) {
				if ( putawayDetail.getPlanPutawayQty() <= 0 ) {
					// 已经分配完毕
					break;
				}
				if ( StringUtil.isTrimEmpty(metaLocation.getOwner()) 
						|| StringUtil.isTrimEmpty(putawayDetail.getOwner())
						|| !metaLocation.getOwner().equals(putawayDetail.getOwner()) ) {
					// 库位设置的货主与货品的货主一致，才能分配
					continue;
				}
				this.autoPtwAllot.execute(metaLocation, metaSku, recPutawayDetailVO);
				// 设置库位对象到上架单明细对象
				recPutawayDetailVO.addLocation(metaLocation);
				log.error("规则0 -> 货品" + metaSku.getSkuName() + " , 批次：" + putawayDetail.getBatchNo() + "，分配库位" + metaLocation.getLocationName());
			}
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