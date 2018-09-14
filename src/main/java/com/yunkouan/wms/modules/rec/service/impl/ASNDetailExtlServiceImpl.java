/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月15日 下午3:04:47<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.dao.IASNDetailDao;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.service.IASNDetailExtlService;
import com.yunkouan.wms.modules.rec.vo.RecAsnDetailVO;

import tk.mybatis.mapper.entity.Example;

/**
 * ASN单明细外部调用业务类<br/><br/>
 * @version 2017年2月15日 下午3:04:47<br/>
 * @author andy wang<br/>
 */
@Service
public class ASNDetailExtlServiceImpl extends BaseService implements IASNDetailExtlService {
	
	/**
	 * 日志对象
     * @author andy wang<br/>
	 */
	private Logger log = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * Asn单明细Dao
	 */
	@Autowired
	private IASNDetailDao asnDetailDao;
	
	/**
	 * 根据ASN单id集合，查询ASN单明细集合
	 * @param asnIds ASN单id集合
	 * @return ASN单明细集合
	 * @throws Exception
	 * @version 2017年5月4日 下午3:33:03<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listAsnDetailByAsnIds ( List<String> asnIds ) throws Exception {
		if ( PubUtil.isEmpty(asnIds) ) {
			log.error("listAsnDetailByAsnIds --> asnIds is null!");
			return null;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.setListAsnId(asnIds);
		return this.listAsnDetailByExample(recAsnDetailVO);
	}
	
	/**
	 * 根据条件，查询ASN单明细
	 * @param recAsnDetailVO ASN单明细
	 * @return ASN单明细集合
	 * @throws Exception
	 * @version 2017年3月20日 下午8:12:36<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listAsnDetailByExample ( RecAsnDetailVO recAsnDetailVO ) throws Exception {
		if ( recAsnDetailVO == null ) {
			log.error("listAsnDetailByExample --> recAsnDetailVO is null!");
			return null;
		}
		recAsnDetailVO.loginInfo();
		return this.asnDetailDao.selectByExample(recAsnDetailVO.getExample());
	}
	
	/**
	 * 根据Asn单明细id，查询Asn单
	 * @param asnDetailId Asn单明细id
	 * @return Asn单
	 * @throws Exception
	 * @version 2017年2月21日 上午10:36:58<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnDetail findByDetailId ( String asnDetailId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnDetailId) ) {
			log.error("findByDetailId --> asnDetailId is null!");
			return null;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.getAsnDetail().setAsnDetailId(asnDetailId);
		List<RecAsnDetail> listAsnDetail = this.listAsnDetailByExample(recAsnDetailVO);
		return listAsnDetail == null || listAsnDetail.isEmpty() ? null : listAsnDetail.get(0) ;
	}
	
	/**
	 * 批量保存Asn单明细
	 * @param listRecAsnDetail Asn单明细集合
	 * @return 保存后的Asn单名字集合
	 * @throws Exception
	 * @version 2017年2月20日 下午3:20:28<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> insertAsnDetail ( List<RecAsnDetail> listRecAsnDetail ) throws Exception {
		if ( PubUtil.isEmpty(listRecAsnDetail) ) {
			log.error("insertAsnDetail --> listRecAsnDetail is null!");
			return null;
		}
		for (int i = 0; i < listRecAsnDetail.size(); i++) {
			RecAsnDetail recAsnDetail = listRecAsnDetail.get(i);
			if ( StringUtil.isTrimEmpty(recAsnDetail.getBatchNo()) ) {
				recAsnDetail.setBatchNo("");
			}
			recAsnDetail.setReceiveQty(null);
			recAsnDetail.setReceiveWeight(null);
			recAsnDetail.setReceiveVolume(null);
			Principal loginUser = LoginUtil.getLoginUser();
			recAsnDetail.setCreatePerson(loginUser.getUserId());
			recAsnDetail.setUpdatePerson(loginUser.getUserId());
			recAsnDetail.setCreateTime(new Date());
			recAsnDetail.setUpdateTime(new Date());
			recAsnDetail.setAsnDetailId2(context.getStrategy4Id().getAsnDetailSeq());
		}
		for (RecAsnDetail recAsnDetail : listRecAsnDetail) {
			this.asnDetailDao.insertSelective(recAsnDetail);
		}
		return listRecAsnDetail;
	}
	/**
	 * 批量保存作业完成的明细
	 */
	public List<RecAsnDetail> insertAsnDetailComfirm ( List<RecAsnDetail> listRecAsnDetail ) throws Exception {
		if ( PubUtil.isEmpty(listRecAsnDetail) ) {
			log.error("insertAsnDetail --> listRecAsnDetail is null!");
			return null;
		}
		for (int i = 0; i < listRecAsnDetail.size(); i++) {
			RecAsnDetail recAsnDetail = listRecAsnDetail.get(i);
			if ( StringUtil.isTrimEmpty(recAsnDetail.getBatchNo()) ) {
				recAsnDetail.setBatchNo("");
			}
			Principal loginUser = LoginUtil.getLoginUser();
			recAsnDetail.setCreatePerson(loginUser.getUserId());
			recAsnDetail.setUpdatePerson(loginUser.getUserId());
			recAsnDetail.setCreateTime(new Date());
			recAsnDetail.setUpdateTime(new Date());
			recAsnDetail.setAsnDetailId2(context.getStrategy4Id().getAsnDetailSeq());
		}
		for (RecAsnDetail recAsnDetail : listRecAsnDetail) {
			this.asnDetailDao.insertSelective(recAsnDetail);
		}
		return listRecAsnDetail;
	}
	/**
	 * 根据Asn单Id，删除Asn单明细
	 * @param listAsnDetailId Asn单明细Id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午2:51:00<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int deleteListByAsnDetailId ( List<String> listAsnDetailId ) throws Exception {
		if ( listAsnDetailId == null || listAsnDetailId.isEmpty() ) {
			log.error("deleteListByAsnDetailId --> listAsnDetailId is null!");
			return 0;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.setListAsnDetailId(listAsnDetailId);
		recAsnDetailVO.loginInfo();
		return this.asnDetailDao.deleteByExample(recAsnDetailVO.getExample());
	}
	
	
	/**
	 * 更新Asn单明细
	 * @param recAsnDetail Asn单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月19日 上午6:31:13<br/>
	 * @author andy wang<br/>
	 */
	public int updateAsnDetail ( RecAsnDetail recAsnDetail ) throws Exception {
		if ( recAsnDetail == null ) {
			log.error("updateAsnDetail --> recAsnDetail is null!");
			return 0;
		}
		if ( StringUtil.isTrimEmpty(recAsnDetail.getBatchNo()) ) {
			recAsnDetail.setBatchNo("");
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.getAsnDetail().setAsnDetailId(recAsnDetail.getAsnDetailId());
		recAsnDetailVO.loginInfo();
		Example example = recAsnDetailVO.getExample();
		RecAsnDetail record = new RecAsnDetail();
		record.setSkuId(recAsnDetail.getSkuId());
		record.setBatchNo(recAsnDetail.getBatchNo());
		record.setMeasureUnit(recAsnDetail.getMeasureUnit());
		record.setOrderQty(recAsnDetail.getOrderQty());
		record.setOrderWeight(recAsnDetail.getOrderWeight());
		record.setOrderVolume(recAsnDetail.getOrderVolume());
		record.setPackId(recAsnDetail.getPackId());
		record.setLocationId(recAsnDetail.getLocationId());
		
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.asnDetailDao.updateByExampleSelective(record, example);
	}
	
	/**
	 * 根据Asn单id，查询Asn单明细
	 * @param asnId Asn单id
	 * @return
	 * @throws Exception
	 * @version 2017年2月17日 下午5:36:27<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listSkuByAsnId ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("listSkuByAsnId --> asnId is null!");
			return null;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.getAsnDetail().setAsnId(asnId);
		recAsnDetailVO.loginInfo();
		Example example = recAsnDetailVO.getExample();
		example.selectProperties("asnDetailId","skuId","packId","locationId","batchNo",
				"orderQty","orderWeight","orderVolume","measureUnit");
		return this.listAsnDetailByExample(recAsnDetailVO);
	}
	
	/**
	 * 保存Asn单明细（只适用于页面第一次保存Asn单）<br/>
	 *  —— 方法内对一些字段进行拦截，不能保存到数据库，如：parentAsnDeailId，receiveQty等;
	 *  —— 创建人、仓库、企业，自动使用当前登录人信息填充
	 * @param recAsnDetail Asn单明细
	 * @return 带有id的Asn单明细
	 * @throws Exception
	 * @version 2017年2月17日 下午4:29:35<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnDetail insertAsnDetail ( RecAsnDetail recAsnDetail ) throws Exception {
		if ( recAsnDetail == null ) {
			log.error("insertAsnDetail --> recAsn is null!");
			return null;
		}
		if ( StringUtil.isTrimEmpty(recAsnDetail.getAsnDetailId()) ) {
			recAsnDetail.setAsnDetailId(IdUtil.getUUID());
		}
		if ( StringUtil.isTrimEmpty(recAsnDetail.getBatchNo()) ) {
			recAsnDetail.setBatchNo("");
		}
		recAsnDetail.setParentAsnDetailId(null);
		recAsnDetail.setReceiveQty(null);
		recAsnDetail.setReceiveWeight(null);
		recAsnDetail.setReceiveVolume(null);
		Principal loginUser = LoginUtil.getLoginUser();
		recAsnDetail.setCreatePerson(loginUser.getUserId());
		recAsnDetail.setUpdatePerson(loginUser.getUserId());
		recAsnDetail.setOrgId(loginUser.getOrgId());
		recAsnDetail.setWarehouseId(LoginUtil.getWareHouseId());
		recAsnDetail.setCreateTime(null);
		recAsnDetail.setUpdateTime(null);
		recAsnDetail.setAsnDetailId2(context.getStrategy4Id().getAsnDetailSeq());
		this.asnDetailDao.insertSelective(recAsnDetail);
		return recAsnDetail;
	}
	
	/**
	 * 更新Asn单明细的收货确认信息
	 * @param recAsnDetail Asn单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月17日 下午3:13:33<br/>
	 * @author andy wang<br/>
	 */
	public int confirmAsnDetail ( RecAsnDetail recAsnDetail ) throws Exception {
		if ( recAsnDetail == null ) {
			log.error("confirmAsnDetail --> recAsnDetail is null!");
			return 0;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.getAsnDetail().setAsnDetailId(recAsnDetail.getAsnDetailId());
		recAsnDetailVO.loginInfo();
		Example example = recAsnDetailVO.getExample();
		
		RecAsnDetail record = new RecAsnDetail();
		record.setLocationId(recAsnDetail.getLocationId());
		record.setOrderQty(recAsnDetail.getOrderQty());
		record.setOrderWeight(recAsnDetail.getOrderWeight());
		record.setOrderVolume(recAsnDetail.getOrderVolume());
		record.setReceiveQty(recAsnDetail.getReceiveQty());
		record.setReceiveWeight(recAsnDetail.getReceiveWeight());
		record.setReceiveVolume(recAsnDetail.getReceiveVolume());
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.asnDetailDao.updateByExampleSelective(record, example);
	}
	
	/**
	 * 根据Asn单明细id集合，查询Asn单明细
	 * @param asnDetailId Asn单明细id集合
	 * @return 	key Asn单明细id<br/>
	 * 			value Asn单明细
	 * @throws Exception
	 * @version 2017年2月17日 上午11:43:25<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecAsnDetail> mapAsnDetailByDetailId ( List<String> listAsnDetailId ) throws Exception {
		if ( listAsnDetailId == null || listAsnDetailId.isEmpty() ) {
			log.error("mapAsnDetailByDetailId --> listAsnDetailId is null!");
			return null;
		}
		Map<String,RecAsnDetail> map_result = new HashMap<String,RecAsnDetail>();
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.setListAsnDetailId(listAsnDetailId);
		
		List<RecAsnDetail> listRecAsnDetail = this.listAsnDetailByExample(recAsnDetailVO);
		if ( listRecAsnDetail == null || listRecAsnDetail.isEmpty() ) {
			return map_result;
		}
		for (RecAsnDetail recAsnDetail : listRecAsnDetail) {
			map_result.put(recAsnDetail.getAsnDetailId(), recAsnDetail);
		}
		return map_result;
	}
	
	
	/**
	 * 根据Asn单Id，删除Asn单明细
	 * @param listAsnId Asn单Id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午2:51:00<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int deleteListByAsnId ( List<String> listAsnId ) throws Exception {
		if ( listAsnId == null || listAsnId.isEmpty() ) {
			log.error("deleteListByAsnId --> listAsnId is null!");
			return 0;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.setListAsnId(listAsnId);
		recAsnDetailVO.loginInfo();
		return this.asnDetailDao.deleteByExample(recAsnDetailVO.getExample());
	}
	
	/**
	 * 根据Asn单id，查询Asn单明细
	 * @param asnId Asn单id
	 * @return
	 * @throws Exception
	 * @version 2017年2月15日 下午5:01:22<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> listAsnDetailByAsnId ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("listAsnDetailByAsnId --> asnId is null!");
			return null;
		}
		RecAsnDetailVO recAsnDetailVO = new RecAsnDetailVO();
		recAsnDetailVO.getAsnDetail().setAsnId(asnId);
		recAsnDetailVO.loginInfo();
		Example example = recAsnDetailVO.getExample();
		example.setOrderByClause("asn_detail_id2 DESC");
		return this.asnDetailDao.selectByExample(example);
	}

	/**
	 * 根据Asn单id，查询Asn单明细，并使用Map返回
	 * @param asnId Asn单id
	 * @return 	key Asn单明细id<br/>
	 * 			value Asn单明细
	 * @throws Exception
	 * @version 2017年2月15日 下午3:09:12<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecAsnDetail> mapAsnDetailByAsnId ( String asnId ) throws Exception {
		List<RecAsnDetail> listRecAsnDetail = this.listAsnDetailByAsnId(asnId);
		Map<String,RecAsnDetail> map_result = new HashMap<String,RecAsnDetail>();
		if ( listRecAsnDetail == null || listRecAsnDetail.isEmpty() ) {
			return map_result;
		}
		for (RecAsnDetail recAsnDetail : listRecAsnDetail) {
			map_result.put(recAsnDetail.getAsnDetailId(), recAsnDetail);
		}
		return map_result;
	}
	
	/**
	 * 根据Asn单id集合，查询Asn单明细，并使用Map返回
	 * @param asnId Asn单id
	 * @return 	key Asn单明细id<br/>
	 * 			value Asn单明细
	 * @return
	 * @throws Exception
	 * @version 2017年5月4日 下午3:56:38<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecAsnDetail> mapAsnDetailByAsnIds ( List<String> asnIds ) throws Exception {
		List<RecAsnDetail> listRecAsnDetail = this.listAsnDetailByAsnIds(asnIds);
		Map<String,RecAsnDetail> map_result = new HashMap<String,RecAsnDetail>();
		if ( listRecAsnDetail == null || listRecAsnDetail.isEmpty() ) {
			return map_result;
		}
		for (RecAsnDetail recAsnDetail : listRecAsnDetail) {
			map_result.put(recAsnDetail.getAsnDetailId(), recAsnDetail);
		}
		return map_result;
	}
	
}
