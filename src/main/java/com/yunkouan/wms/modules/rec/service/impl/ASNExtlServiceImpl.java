/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月15日 下午5:57:38<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.dao.IASNDao;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

import tk.mybatis.mapper.entity.Example;

/**
 * ASN单外部调用业务类<br/><br/>
 * @version 2017年2月15日 下午5:57:38<br/>
 * @author andy wang<br/>
 */
@Service
public class ASNExtlServiceImpl extends BaseService implements IASNExtlService {
	
	/** 日志对象 <br/> add by andy wang */
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	
	/** Asn单Dao <br/> add by andy wang */
	@Autowired
	private IASNDao asnDao;
	/* method ***************************************/
	
	
	/**
	 * 根据ASN单号、PO单号(<font color="red"><strong>模糊</strong></font>)，查询ASN单集合
	 * @param asnNo - ASN单号
	 * @param poNo - PO单号
	 * @return ASN单集合
	 * @throws Exception
	 * @version 2017年3月23日 下午2:50:24<br/>
	 * @author andy wang<br/>
	 */
	public List<String> listAsnIdByAsnPoNo ( String asnNo , String poNo ) throws Exception {
		if ( StringUtil.isEmpty(asnNo) && StringUtil.isEmpty(poNo) ) {
			// 两个条件都为空时，此次查询没有意义
			log.error("listAsnIdByAsnPoNo --> asnNo and poNo is null!");
			return new ArrayList<String>();
		}
		List<String> listAsnId = new ArrayList<String>();
		RecAsnVO asnVO = new RecAsnVO();
		asnVO.setLikePoNo(poNo);
		asnVO.setLikeAsnNo(asnNo);
		List<RecAsn> listAsn = this.listAsnByExample(asnVO);
		if ( !PubUtil.isEmpty(listAsn) ) {
			for (RecAsn recAsn : listAsn) {
				listAsnId.add(recAsn.getAsnId());
			}
		}
		return listAsnId;
	}
	
	
	/**
	 * 根据条件，统计数据数量
	 * @param recAsnVO 查询条件
	 * @return 数据数量
	 * @throws Exception
	 * @version 2017年3月19日 下午5:41:49<br/>
	 * @author andy wang<br/>
	 */
	public Integer countAsnByExample ( RecAsnVO recAsnVO ) throws Exception {
		if ( recAsnVO == null ) {
			log.error("listAsnByExample --> recAsnVO is null!");
			return null;
		}
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
		return this.asnDao.selectCountByExample(example);
	}
	/**根据条件，统计数据数量
	 * add by zwb
	 */
	public Integer countAsnByExample2 (RecAsnVO recAsnVO ) throws Exception{
		Example example = recAsnVO.getExample();
		return this.asnDao.selectCountByExample(example);
	}
	public List<String>getTask(String orgId){
		return asnDao.getTask(orgId);
	}
	
	/**
	 * 根据Asn单id，查询Asn单信息（注意：锁行）
	 * @param asnId Asn单id
	 * @return Asn单信息
	 * @throws Exception
	 * @version 2017年3月10日 上午11:11:47<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findAsnById4Lock ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("findAsnById4Lock --> asnId is null!");
			return null;
		}
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		return this.asnDao.selectAsnForUpdate(recAsnVO);
	}
	
	
	/**
	 * 根据条件，分页查询ASN单<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id
	 * @param recAsnVO 查询条件
	 * @return ASN单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午5:35:56<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listAsnByPage ( RecAsnVO recAsnVO ) throws Exception {
		if ( recAsnVO == null ) {
			log.error("selectAsnByPage --> recAsnVO is null!");
			return new Page();
		}
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
//		example.selectProperties("asnId","owner","warehouseId","asnNo","parentAsnId","docType","dataFrom",
//				"poNo","asnNo1","asnNo2","orderDate","orderQty","orderWeight","orderVolume","receiveQty"
//				,"receiveWeight","receiveVolume","asnStatus","createPerson","updatePerson","createTime","updateTime");
		example.setOrderByClause("asn_id2 DESC");
		Page page = PageHelper.startPage(recAsnVO.getCurrentPage()+1, recAsnVO.getPageSize());
		this.asnDao.selectByExample(example);
		return page;
	}
	
	
	/**
	 * 根据Asn单VO生成条件，获取Asn单<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id
	 * @param recAsnVO Asn单查询条件
	 * @return Asn单集合
	 * @throws Exception
	 * @version 2017年2月21日 下午2:16:32<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findAsnByExample ( RecAsnVO recAsnVO ) throws Exception {
		List<RecAsn> listRecAsn = this.listAsnByExample(recAsnVO);
		return PubUtil.isEmpty(listRecAsn) ? null : listRecAsn.get(0);
	}
	
	/**
	 * 根据Asn单VO生成条件，获取Asn单集合<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id
	 * @param recAsnVO Asn单查询条件
	 * @return Asn单集合
	 * @throws Exception
	 * @version 2017年2月15日 下午6:22:52<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsn> listAsnByExample ( RecAsnVO recAsnVO ) throws Exception {
		if ( recAsnVO == null ) {
			log.error("listAsnByExample --> recAsnVO is null!");
			return new ArrayList<RecAsn>();
		}
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
		return this.asnDao.selectByExample(example);
	}
	
	/**
	 * 更新Asn单(接口默认设置当前登录人为更新人，使用数据库时间为更新时间)
	 * @param asnVO Asn单
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月18日 下午11:25:18<br/>
	 * @author andy wang<br/>
	 */
	public int updateAsn ( RecAsnVO asnVO ) throws Exception {
		if ( asnVO == null || asnVO.getAsn() == null ) {
			log.error("updateASN --> asnVO or asn is null!");
			return 0;
		}
		// 拼接Where条件
		RecAsn asn = asnVO.getAsn();
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.loginInfo();
		recAsnVO.getAsn().setAsnId(asn.getAsnId());
		Example example = recAsnVO.getExample();
		// 拼接更新内容
		RecAsn record = new RecAsn();
		record.setPoNo(asn.getPoNo());
		record.setAsnNo1(asn.getAsnNo1());
		record.setAsnNo2(asn.getAsnNo2());
		record.setDocType(asn.getDocType());
		record.setOrderDate(asn.getOrderDate());
		record.setOwner(asn.getOwner());
		record.setSender(asn.getSender());
		record.setContactPerson(asn.getContactPerson());
		record.setContactPhone(asn.getContactPhone());
		record.setContactAddress(asn.getContactAddress());
		record.setNote(asn.getNote());
		record.setOrderQty(asn.getOrderQty());
		record.setOrderWeight(asn.getOrderWeight());
		record.setOrderVolume(asn.getOrderVolume());
		record.setAsnStatus(asn.getAsnStatus());
		record.setInterceptStatus(asn.getInterceptStatus());
		Principal loginUser = LoginUtil.getLoginUser();
		record.setCreatePerson(null);
		record.setCreateTime(null);
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		record.setAsnId2(null);
		return this.asnDao.updateByExampleSelective(record, example);
	}
	
	
	/**
	 * 查看Asn单信息（查看Asn单页面调用）<br/>
	 * —— 进行字段拦截<br/>
	 * —— 自带条件当前登录用户的企业id、仓库id<br/>
	 * @param asnId Asn单id
	 * @return Asn单信息
	 * @throws Exception
	 * @version 2017年2月17日 下午5:14:16<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn viewById ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("viewById --> asnId is null!");
			return null;
		}
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
//		example.selectProperties("asnId","warehouseId","asnNo","poNo","asnNo1","asnNo2","docType","receiveQty","receiveWeight",
//		 		 "receiveVolume","orderDate","owner","sender","contactPerson","contactPhone","contactAddress","note",
//		 		 "asnStatus","orderQty","orderWeight","orderVolume");
		List<RecAsn> listRecAsn = this.asnDao.selectByExample(example);
		return PubUtil.isEmpty(listRecAsn) ? null : listRecAsn.get(0);
	}
	
	/**
	 * 保存Asn单（只适用于页面第一次保存Asn单）<br/>
	 *  —— 方法内对一些字段进行拦截，不能保存到数据库，如：parentAsnId，receiveQty等;
	 *  —— 创建人、仓库、企业，自动使用当前登录人信息填充
	 * @param recAsn 要保存的Asn单
	 * @return 带有id的Asn单
	 * @throws Exception
	 * @version 2017年2月17日 下午4:21:33<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn insertAsn( RecAsn recAsn ) throws Exception {
		if ( recAsn == null ) {
			log.error("saveAsn --> recAsn is null!");
			return null;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		// 获取ASN编号
		if ( StringUtil.isTrimEmpty(recAsn.getAsnNo()) ) {
			String asnNo = context.getStrategy4No(loginUser.getOrgId(), LoginUtil.getWareHouseId()).getAsnNo(loginUser.getOrgId(), recAsn.getDocType());
			recAsn.setAsnNo(asnNo);
		}
		if ( StringUtil.isTrimEmpty(recAsn.getAsnId()) ) {
			recAsn.setAsnId(IdUtil.getUUID());
		}
		recAsn.setReceiveQty(null);
		recAsn.setReceiveWeight(null);
		recAsn.setReceiveVolume(null);
		recAsn.setCreatePerson(loginUser.getUserId());
		recAsn.setUpdatePerson(loginUser.getUserId());
		recAsn.setCreateTime(null);
		recAsn.setUpdateTime(null);
		recAsn.setOpPerson(null);
		recAsn.setOpTime(null);
		recAsn.setAsnId2(context.getStrategy4Id().getAsnSeq());
		this.asnDao.insertSelective(recAsn);
//		System.out.println(recAsn.getAsnId()+"*************************************************************");
		return recAsn;
	}

	/**
	 * 获取ASN编号
	 * @param docType Asn单来源 
	 * @return ASN编号
	 * @version 2017年3月8日 下午2:19:25<br/>
	 * @author andy wang<br/>
	 */
//	private String getAsnNo( Integer docType ) {
//		if ( docType == null ) {
//			return null;
//		}
//		// 设置asn单号
//		CommonVo p_commonVO = new CommonVo(LoginUtil.getLoginUser().getOrgId(), RecUtil.type2NoPrefix(docType));
//		return this.commonService.getNo(p_commonVO);
//	}
	
	
	/**
	 * 更新Asn单的收货确认信息
	 * @param recAsn Asn单
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月17日 下午3:30:15<br/>
	 * @author andy wang<br/>
	 */
	public int confirmAsn ( RecAsn recAsn ) throws Exception {
		if ( recAsn == null ) {
			log.error("confirmAsn --> recAsn is null!");
			return 0;
		}
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(recAsn.getAsnId());
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
		RecAsn record = new RecAsn();
		record.setAsnStatus(recAsn.getAsnStatus());
		record.setReceiveQty(recAsn.getReceiveQty());
		record.setReceiveWeight(recAsn.getReceiveWeight());
		record.setReceiveVolume(recAsn.getReceiveVolume());
		record.setOpPerson(recAsn.getOpPerson());
		record.setOpTime(recAsn.getOpTime());
		Principal loginUser = LoginUtil.getLoginUser();
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.asnDao.updateByExampleSelective(record, example);
	}
	
	/**
	 * 根据Asn单id集合，查询Asn单
	 * @param listAsnId Asn单id集合
	 * @return 	key Asn单id<br/>
	 * 			value Asn单对象
	 * @throws Exception
	 * @version 2017年2月17日 下午1:48:26<br/>
	 * @author andy wang<br/>
	 */
    public Map<String,RecAsn> mapAsnByIds ( List<String> listAsnId ) throws Exception {
    	if ( listAsnId == null || listAsnId.isEmpty() ) {
			log.error("findByIds --> listAsnId is null!");
			return new HashMap<String,RecAsn>();
    	}
    	RecAsnVO recAsnVO = new RecAsnVO();
    	recAsnVO.setListAsnId(listAsnId);
    	List<RecAsn> listRecAsn = this.listAsnByExample(recAsnVO);
		Map<String,RecAsn> mapRecAsn = new HashMap<String, RecAsn>();
		if ( PubUtil.isEmpty(listRecAsn) ) {
			return new HashMap<String,RecAsn>();
		}
		for (RecAsn recAsn : listRecAsn) {
			mapRecAsn.put(recAsn.getAsnId(), recAsn);
		}
		return mapRecAsn;
    }
	
	/**
	 * 根据Asn单id，更新状态
	 * @param asnId Asn单id
	 * @param status 状态<br/>
	 * 				- 输入常量中 【Constant.ASN_STATUS】 的值
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午3:29:55<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int updateAsnStatusById ( String asnId , Integer status ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("updateStatusById --> asnId is null!");
			return 0;
		}
		// 设置更新条件
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setAsnId(asnId);
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
		// 设置更新内容
		RecAsn record = new RecAsn();
		record.setAsnStatus(status);
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser != null){
			record.setUpdatePerson(loginUser.getUserId());
		}
		record.setUpdateTime(new Date());
		return this.asnDao.updateByExampleSelective(record, example);
	}
	
	/**
	 * 删除Asn单
	 * @param listAsnId Asn单Id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月16日 下午2:51:00<br/>
	 * @author andy wang<br/>
	 * @return 
	 */
	public int deleteAsnById ( List<String> listAsnId ) throws Exception {
		if ( listAsnId == null || listAsnId.isEmpty() ) {
			log.error("deleteList --> listAsnId is null!");
			return 0;
		}
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.setListAsnId(listAsnId);
		recAsnVO.loginInfo();
		Example example = recAsnVO.getExample();
		return this.asnDao.deleteByExample(example);
	}
	
	/**
	 * 根据Asn单id，获取Asn单
	 * @param asnId Asn单id
	 * @return Asn单
	 * @throws Exception
	 * @version 2017年2月15日 下午6:22:52<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn findAsnById ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("findById --> asnId is null!");
			return null;
		}
		return this.asnDao.selectByPrimaryKey(asnId);
//		RecAsnVO recAsnVO = new RecAsnVO();
//		recAsnVO.getAsn().setAsnId(asnId);
//		List<RecAsn> listRecAsn = this.listAsnByExample(recAsnVO);
//		return PubUtil.isEmpty(listRecAsn)?null:listRecAsn.get(0);
	}
	
	
	/**
	 * 根据父Asn单id，查询拆分的Asn单
	 * @param parentAsnId 父Asn单id
	 * @return 拆分的子单集合
	 * @throws Exception
	 * @version 2017年2月16日 下午1:51:20<br/>
	 * @author andy wang<br/>
	 */
	public List<RecAsn> listSplitAsn ( String parentAsnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(parentAsnId) ) {
			log.error("listSplitAsn --> parentAsnId is null!");
			return new ArrayList<RecAsn>();
		}
		RecAsnVO recAsnVO = new RecAsnVO();
		recAsnVO.getAsn().setParentAsnId(parentAsnId);
		List<RecAsn> listRecAsn = this.listAsnByExample(recAsnVO);
		return listRecAsn;
	}


	@Override
	public RecAsn findByAsnNo(String asnNo) throws Exception {
		if ( StringUtil.isTrimEmpty(asnNo) ) {
			log.error("findByAsnNo --> asnNo is null!");
			return null;
		}
		RecAsn param = new RecAsn();
		param.setAsnNo(asnNo);
		return this.asnDao.selectOne(param);
	}
	

	
}
