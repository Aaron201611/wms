/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月2日 上午8:49:04<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.util.RecUtil;

/**
 * 上架单拆分策略<br/><br/>
 * @version 2017年3月2日 上午8:49:04<br/>
 * @author andy wang<br/>
 */
public class PutawaySplitStrategy extends SplitStrategy<RecPutaway,RecPutawayDetail>  {
	
	/** 当前登录用户 <br/> add by andy */
	private Principal loginUser;
	
	/**
	 * 构造方法
	 * @param sourceRecPutaway 原上架单
	 * @param listSourceRecPutawayDetail 原上架单明细集合
	 * @param listSavePutawayDetail 传递参数拆分的上架单明细集合
	 * @version 2017年3月7日 下午6:15:41<br/>
	 * @author andy wang<br/>
	 */
	public PutawaySplitStrategy ( RecPutaway sourceRecPutaway , List<RecPutawayDetail> listSourceRecPutawayDetail , List<RecPutawayDetail> listSavePutawayDetail ) {
		super(sourceRecPutaway, listSourceRecPutawayDetail, listSavePutawayDetail,"err_rec");
		this.loginUser = LoginUtil.getLoginUser();
		listSplitDetail = new ArrayList<RecPutawayDetail>();
		listSurplusDetail = new ArrayList<RecPutawayDetail>();
		mapSaveDetail = new HashMap<String,RecPutawayDetail>();
	}
	
	
	/**
	 * 生成拆分对象
	 * @throws Exception
	 * @version 2017年2月16日 下午5:45:54<br/>
	 * @author andy wang<br/>
	 */
	@Override
	protected void create() throws Exception {
		List<String> listSplitPutawayNo = RecUtil.createSplitPutawayNo(source.getPutawayNo());
		super.splitObj = this.createSplitObj(listSplitPutawayNo.get(0));
		super.surplusObj = this.createSplitObj(listSplitPutawayNo.get(1));
	}
	
	
	/**
	 * 创建拆分对象
	 * @param splitNo 拆分的编号
	 * @return
	 * @version 2017年2月16日 下午5:49:50<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	protected RecPutaway createSplitObj ( String splitNo ) throws Exception {
		String splitAsnId = IdUtil.getUUID();
    	RecPutaway recPutaway = (RecPutaway) BeanUtils.cloneBean(source);
    	recPutaway.setAsnNo(null);
    	recPutaway.setPoNo(null);
    	recPutaway.setPutawayId(splitAsnId);
    	recPutaway.setParentPutawayId(source.getPutawayId());
    	recPutaway.setCreatePerson(loginUser.getUserId());
    	recPutaway.setUpdatePerson(loginUser.getUserId());
    	recPutaway.setPutawayNo(splitNo);
    	return recPutaway;
	}
	

	/**
	 * 创建拆分明细
	 * @param splitPutawayId 拆分Asn单id
	 * @param sourceDetail
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:47:33<br/>
	 * @author andy wang<br/>
	 */
	protected RecPutawayDetail createSplitDetail ( String splitPutawayId , RecPutawayDetail sourceDetail ) throws Exception {
		String splitDetailId = IdUtil.getUUID();
		RecPutawayDetail splitDetail = (RecPutawayDetail) BeanUtils.cloneBean(sourceDetail);
		splitDetail.setPutawayDetailId(splitDetailId);
		splitDetail.setPutawayId(splitPutawayId);
		splitDetail.setParentPutawayDetailId(sourceDetail.getPutawayDetailId());
		splitDetail.setCreateTime(null);
		splitDetail.setUpdateTime(null);
		splitDetail.setPutawayDetailId2(null);
		splitDetail.setCreatePerson(this.loginUser.getUserId());
		splitDetail.setUpdatePerson(loginUser.getOrgId());
		return splitDetail;
	}
	
	@Override
	protected boolean checkStatus(RecPutaway t) {
		return t.getPutawayStatus() == null || Constant.PUTAWAY_STATUS_OPEN != t.getPutawayStatus();
	}


	@Override
	protected String getDetailId(RecPutawayDetail k) {
		return k.getPutawayDetailId();
	}


	@Override
	protected Double getDetailQty(RecPutawayDetail k) {
		return k.getPlanPutawayQty();
	}


	@Override
	protected Double getDetailVolume(RecPutawayDetail k) {
		return k.getPlanPutawayVolume();
	}


	@Override
	protected Double getDetailWeight(RecPutawayDetail k) {
		return k.getPlanPutawayWeight();
	}


	@Override
	protected void defDetail(RecPutawayDetail k) {
		RecUtil.defPutawayDetailPlanQWV(k);
	}


	@Override
	protected String getId(RecPutaway t) {
		return t.getPutawayId();
	}


	@Override
	protected void setDetailQty(RecPutawayDetail k, Double qty) {
		k.setPlanPutawayQty(qty);
	}


	@Override
	protected void setDetailVolume(RecPutawayDetail k, Double volume) {
		k.setPlanPutawayVolume(volume);
	}


	@Override
	protected void setDetailWeight(RecPutawayDetail k, Double weight) {
		k.setPlanPutawayWeight(weight);
	}


	@Override
	protected void setQty(RecPutaway t, Double qty) {
		t.setPlanQty(qty);
	}


	@Override
	protected void setWeight(RecPutaway t, Double weight) {
		t.setPlanWeight(weight);
	}


	@Override
	protected void setVolume(RecPutaway t, Double volume) {
		t.setPlanVolume(volume);
	}

}
