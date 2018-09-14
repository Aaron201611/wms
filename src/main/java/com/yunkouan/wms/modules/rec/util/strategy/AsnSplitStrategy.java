/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月16日 下午4:42:56<br/>
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
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.util.RecUtil;

/**
 * ASN单拆分策略<br/><br/>
 * @version 2017年2月16日 下午4:42:56<br/>
 * @author andy wang<br/>
 */
public class AsnSplitStrategy extends SplitStrategy<RecAsn,RecAsnDetail> {
	
	/** 当前登录用户信息 <br/> add by andy */
	private Principal loginUser;
	
	/**
	 * 构造方法
	 * @param sourceRecAsn 原ASN单
	 * @param listSourceRecAsnDetail 原ASN单下的明细集合
	 * @param listSavetAsnDetail 传递过来的拆分ASN单明细集合
	 * @version 2017年3月7日 下午6:06:36<br/>
	 * @author andy wang<br/>
	 */
	public AsnSplitStrategy ( RecAsn sourceRecAsn , List<RecAsnDetail> listSourceRecAsnDetail , List<RecAsnDetail> listSavetAsnDetail ) {
		super(sourceRecAsn, listSourceRecAsnDetail, listSavetAsnDetail,"err_rec");
		this.loginUser = LoginUtil.getLoginUser();
		listSplitDetail = new ArrayList<RecAsnDetail>();
		listSurplusDetail = new ArrayList<RecAsnDetail>();
		mapSaveDetail = new HashMap<String,RecAsnDetail>();
	}
	
	@Override
	protected boolean checkStatus(RecAsn asn) {
		return asn.getAsnStatus() == null || Constant.ASN_STATUS_OPEN != asn.getAsnStatus() ;
	}
	
	
	/**
	 * 生成拆分对象
	 * @throws Exception
	 * @version 2017年2月16日 下午5:45:54<br/>
	 * @author andy wang<br/>
	 */
	@Override
	protected void create() throws Exception {
		List<String> listSplitAsnNo = RecUtil.createSplitAsnNo(source.getAsnNo());
		super.splitObj = this.createSplitObj(listSplitAsnNo.get(0));
		super.surplusObj = this.createSplitObj(listSplitAsnNo.get(1));
	}
	
	
	/**
	 * 创建拆分对象
	 * @param splitNo 拆分的编号
	 * @return
	 * @version 2017年2月16日 下午5:49:50<br/>
	 * @author andy wang<br/>
	 * @throws Exception 
	 */
	protected RecAsn createSplitObj ( String splitNo ) throws Exception {
		String splitAsnId = IdUtil.getUUID();
    	RecAsn recAsn = (RecAsn) BeanUtils.cloneBean(source);
    	recAsn.setAsnId(splitAsnId);
    	recAsn.setParentAsnId(source.getAsnId());
    	recAsn.setCreatePerson(loginUser.getUserId());
    	recAsn.setUpdatePerson(loginUser.getUserId());
    	recAsn.setAsnNo(splitNo);
    	return recAsn;
	}
	
	/**
	 * 创建拆分明细
	 * @param splitAsnId 拆分Asn单id
	 * @param sourceDetail
	 * @return
	 * @throws Exception
	 * @version 2017年2月16日 下午6:47:33<br/>
	 * @author andy wang<br/>
	 */
	@Override
	protected RecAsnDetail createSplitDetail ( String splitAsnId , RecAsnDetail sourceDetail ) throws Exception {
		String splitDetailId = IdUtil.getUUID();
		RecAsnDetail splitDetail = (RecAsnDetail) BeanUtils.cloneBean(sourceDetail);
		splitDetail.setAsnDetailId(splitDetailId);
		splitDetail.setAsnId(splitAsnId);
		splitDetail.setParentAsnDetailId(sourceDetail.getAsnDetailId());
		splitDetail.setCreateTime(null);
		splitDetail.setUpdateTime(null);
		splitDetail.setAsnDetailId2(null);
		splitDetail.setCreatePerson(this.loginUser.getUserId());
		splitDetail.setUpdatePerson(loginUser.getOrgId());
		return splitDetail;
	}
	

	@Override
	protected String getDetailId(RecAsnDetail detail) {
		return detail.getAsnDetailId();
	}

	@Override
	protected Double getDetailQty(RecAsnDetail detail) {
		return detail.getOrderQty();
	}

	@Override
	protected Double getDetailVolume(RecAsnDetail detail) {
		return detail.getOrderVolume();
	}

	@Override
	protected Double getDetailWeight(RecAsnDetail detail) {
		return detail.getOrderWeight();
	}

	@Override
	protected void defDetail(RecAsnDetail detail) {
		RecUtil.defOrderQWV(detail);
	}

	@Override
	protected String getId(RecAsn asn) {
		return asn.getAsnId();
	}

	@Override
	protected void setDetailQty(RecAsnDetail k , Double qty ) {
		k.setOrderQty(qty);
	}

	@Override
	protected void setDetailVolume(RecAsnDetail k , Double volume ) {
		k.setOrderVolume(volume);
	}

	@Override
	protected void setDetailWeight(RecAsnDetail k , Double weight ) {
		k.setOrderWeight(weight);
	}

	@Override
	protected void setQty(RecAsn t, Double qty) {
		t.setOrderQty(qty);
	}

	@Override
	protected void setWeight(RecAsn t, Double weight) {
		t.setOrderWeight(weight);
	}

	@Override
	protected void setVolume(RecAsn t, Double volume) {
		t.setOrderVolume(volume);
	}

}
