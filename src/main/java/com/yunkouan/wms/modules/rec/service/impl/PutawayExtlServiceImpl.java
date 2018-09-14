/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 上午10:53:21<br/>
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
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.rec.dao.IPutawayDao;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.service.IASNExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayExtlService;
import com.yunkouan.wms.modules.rec.util.RecUtil;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 上架单外部调用业务类<br/><br/>
 * @version 2017年2月22日 上午10:53:21<br/>
 * @author andy wang<br/>
 */
@Service
public class PutawayExtlServiceImpl extends BaseService implements IPutawayExtlService {

	/** 日志对象 <br/> add by andy wang */
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	/** 上架单Dao <br/> add by andy wang */
	@Autowired
	private IPutawayDao putawayDao;
	
	/** 上架单明细外调业务类 <br/> add by andy wang */
	@Autowired
	private IPutawayDetailExtlService ptwDetailExtlService;
	
	/** ASN单外调业务类 <br/> add by andy wang */
	@Autowired
	private IASNExtlService asnExtlService;
	
	/** 客商业务类 <br/> add by andy wang */
	@Autowired
	private IMerchantService merchantService;
	
	/**
	 * 根据上架单id，确认上架单
	 * @param recPtwVO 上架单对象
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年5月2日 下午3:57:07<br/>
	 * @author andy wang<br/>
	 */
	public int confirmPtw ( RecPutawayVO recPtwVO ) throws Exception {
		if ( recPtwVO == null || recPtwVO.getPutaway() == null 
				|| StringUtil.isTrimEmpty(recPtwVO.getPutaway().getPutawayId()) ) {
			log.error("confirmPtw --> recPutawayVO is null!");
			return 0;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		RecPutaway putaway = recPtwVO.getPutaway();
		// 更新内容
		RecPutaway record = new RecPutaway();
		record.setRealQty(putaway.getRealQty());
		record.setRealWeight(putaway.getRealWeight());
		record.setRealVolume(putaway.getRealVolume());
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		record.setOpPerson(putaway.getOpPerson());
		record.setOpTime(putaway.getOpTime());
		record.setPutawayStatus(putaway.getPutawayStatus());
		// 更新的条件
		RecPutawayVO p_ptwVO = new RecPutawayVO();
		p_ptwVO.loginInfo();
		p_ptwVO.getPutaway().setPutawayId(recPtwVO.getPutaway().getPutawayId());
		return this.putawayDao.updateByExampleSelective(record, p_ptwVO.getExample());
	}
	
	/**
	 * 更新上架单
	 * @param recPtwVO 上架单VO
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月25日 上午11:04:41<br/>
	 * @author andy wang<br/>
	 */
	public int updatePtw ( RecPutawayVO recPtwVO ) throws Exception {
		if ( recPtwVO == null || recPtwVO.getPutaway() == null 
				|| StringUtil.isTrimEmpty(recPtwVO.getPutaway().getPutawayId()) ) {
			log.error("updatePtw --> recPutawayVO is null!");
			return 0;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		RecPutaway putaway = recPtwVO.getPutaway();
		// 更新内容
		RecPutaway record = new RecPutaway();
		record.setAsnNo(putaway.getAsnNo());
		record.setPoNo(putaway.getPoNo());
		record.setPlanQty(putaway.getPlanQty());
		record.setPlanWeight(putaway.getPlanWeight());
		record.setPlanVolume(putaway.getPlanVolume());
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		record.setNote(putaway.getNote());
		// 更新的条件
		RecPutawayVO p_ptwVO = new RecPutawayVO();
		p_ptwVO.loginInfo();
		p_ptwVO.getPutaway().setPutawayId(recPtwVO.getPutaway().getPutawayId());
		return this.putawayDao.updateByExampleSelective(record, p_ptwVO.getExample());
	}
	
	
	 /**
	  * 根据ASN单号、PO单号，查询上架单id集合
	  * @param asnNo - ASN单号
	  * @param poNo - PO单号
	  * @param ownerComment - 货主名称
	  * @return 上架单id集合
	  * @throws Exception
	  * @version 2017年3月23日 下午2:53:29<br/>
	  * @author andy wang<br/>
	  */
	public List<String> listPtwByAsnPoNo ( String asnNo , String poNo , String ownerComment ) throws Exception {
		if ( StringUtil.isEmpty(asnNo) 
				&& StringUtil.isEmpty(poNo) 
				&& StringUtil.isEmpty(ownerComment) ) {
			// 两个条件都为空时，此次查询没有意义
			log.error("listPtwByAsnPoNo --> asnNo and poNo and ownerComment is null!");
			return new ArrayList<String>();
		}
		RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO();
		if ( !StringUtil.isTrimEmpty(asnNo) || !StringUtil.isTrimEmpty(poNo) ) {
			List<String> listAsnId = this.asnExtlService.listAsnIdByAsnPoNo(asnNo, poNo);
			if ( PubUtil.isEmpty(listAsnId) ) {
				return new ArrayList<String>();
			}
			recPutawayDetailVO.setListAsnId(listAsnId);
		}
		
		if ( !StringUtil.isTrimEmpty(ownerComment) ) {
			List<String> listOwnerId = this.merchantService.list(ownerComment);
			if ( PubUtil.isEmpty(listOwnerId) ) {
				return new ArrayList<String>();
			}
			recPutawayDetailVO.setListOwnerId(listOwnerId);
		}
		List<RecPutawayDetail> listPtwDetail = this.ptwDetailExtlService.listPutawayDetailByExample(recPutawayDetailVO );
		if ( PubUtil.isEmpty(listPtwDetail) ) {
			return new ArrayList<String>();
		}
		List<String> listPtwId = new ArrayList<String>();
		for (RecPutawayDetail recPutawayDetail : listPtwDetail) {
			listPtwId.add(recPutawayDetail.getPutawayId());
		}
		return listPtwId;
	}
	
	
	
	/**
	 * 根据库位、货品、批次，查询对应状态的上架单（库存模块使用）
	 * @param locationId 库位id
	 * @param skuId 货品id
	 * @param batchNo 货品批次
	 * @param status 上架单的状态
	 * @return 对应条件的上架单
	 * —— sumQty表示对应的数量
	 * @throws Exception
	 * @version 2017年3月20日 下午4:19:36<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayVO> listPtwByStock ( String locationId , String skuId , String batchNo , Integer... status ) throws Exception {
		if ( StringUtil.isTrimEmpty(locationId) || StringUtil.isTrimEmpty(skuId) 
				|| status == null || status.length == 0 ) {
			//此处改动原因：批次可为空 by 王通
			log.error("listPtwByStock --> locationId or skuId or status is null!");
			return new ArrayList<RecPutawayVO>();
		} 
		
		RecPutawayDetailVO ptwDetailVO = new RecPutawayDetailVO();
		ptwDetailVO.getPutawayDetail().setLocationId(locationId);
		ptwDetailVO.getPutawayDetail().setSkuId(skuId);
		ptwDetailVO.getPutawayDetail().setBatchNo(batchNo);
		List<RecPutawayDetail> listPtwDetail = this.ptwDetailExtlService.listPutawayDetailByExample(ptwDetailVO);
		
		if ( PubUtil.isEmpty(listPtwDetail) ) {
			return new ArrayList<RecPutawayVO>();
		}
		List<String> listPtwId = new ArrayList<String>();
		Map<String,Double> mapSumQty = new HashMap<String,Double>();
		for (RecPutawayDetail recPutawayDetail : listPtwDetail) {
			listPtwId.add(recPutawayDetail.getPutawayId());
			Double sumQty = mapSumQty.get(recPutawayDetail.getPutawayId());
			if ( sumQty == null ) {
				sumQty = 0d ;
			}
			sumQty+=recPutawayDetail.getPlanPutawayQty();
			mapSumQty.put(recPutawayDetail.getPutawayId(),sumQty);
		}
		
		RecPutawayVO ptwVO = new RecPutawayVO();
		ptwVO.setListPutawayId(listPtwId);
		List<Integer> listPtwStatus = ptwVO.getListPtwStatus();
		for (Integer stat : status) {
			listPtwStatus.add(stat);
		}
		ptwVO.setListPtwStatus(listPtwStatus);
		List<RecPutaway> listPtw = this.listPutawayByExample(ptwVO);
		if ( PubUtil.isEmpty(listPtwDetail) ) {
			return new ArrayList<RecPutawayVO>();
		}
		List<RecPutawayVO> listPtwVO = new ArrayList<RecPutawayVO>();
		for (RecPutaway recPutaway : listPtw) {
			RecPutawayVO recPutawayVO = new RecPutawayVO(recPutaway);
			recPutawayVO.setSumQty(mapSumQty.get(recPutaway.getPutawayId()));
			listPtwVO.add(recPutawayVO);
		}
		return listPtwVO;
	}
	
	
	
	/**
	 * 根据上架单id，删除上架单
	 * @param listPutawayId 上架单id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年3月2日 下午8:08:12<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayById ( List<String> listPutawayId ) throws Exception {
		if ( PubUtil.isEmpty(listPutawayId) ) {
			log.error("listSplitPutaway --> listPutawayId is null!");
			return 0;
		}
		RecPutawayVO recPutawayVO = new RecPutawayVO( new RecPutaway());
		recPutawayVO.setListPutawayId(listPutawayId);
		recPutawayVO.loginInfo();
		Example example = recPutawayVO.getExample();
		RecUtil.defWhere(example);
		return this.putawayDao.deleteByExample(example);
	}
	
	/**
	 * 根据父上架单id，查询拆分的上架单
	 * @param parentPutawayId 父上架单id
	 * @return 拆分的子单集合
	 * @throws Exception
	 * @version 2017年3月2日 下午7:35:41<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> listSplitPutaway ( String parentPutawayId ) throws Exception {
		if ( StringUtil.isTrimEmpty(parentPutawayId) ) {
			log.error("listSplitPutaway --> parentPutawayId is null!");
			return new ArrayList<RecPutaway>();
		}
		RecPutawayVO recPutawayVO = new RecPutawayVO(new RecPutaway());
		recPutawayVO.getPutaway().setParentPutawayId(parentPutawayId);
		return this.listPutawayByExample(recPutawayVO);
	}
	
	/**
	 * 根据条件，分页查询上架单信息
	 * @param recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午1:53:50<br/>
	 * @author andy wang<br/>
	 */
	public Page<RecPutaway> listPutawayByPage ( RecPutawayVO recPutawayVO ) throws Exception {
		if ( recPutawayVO == null ) {
			recPutawayVO = new RecPutawayVO();
		}
		if ( recPutawayVO.getPutaway() == null ) {
			recPutawayVO.setPutaway(new RecPutaway());
		}
		// 根据ASN单号、PO单号、客商名称查询对应的上架单id
		if ( !StringUtil.isEmpty(recPutawayVO.getLikeAsnNo()) 
				|| !StringUtil.isEmpty(recPutawayVO.getLikePoNo())
				|| !StringUtil.isEmpty(recPutawayVO.getOwnerComment())) {
			List<String> listPtwId = this.listPtwByAsnPoNo(recPutawayVO.getLikeAsnNo()
					, recPutawayVO.getLikePoNo() , recPutawayVO.getOwnerComment());
			if ( !PubUtil.isEmpty(listPtwId) ) {
				recPutawayVO.setListPutawayId(listPtwId);
			} else {
				return null;
			}
		}
		Example example = recPutawayVO.getExample();
		example.setOrderByClause("putaway_id2 DESC");
		Page<RecPutaway> page = PageHelper.startPage(recPutawayVO.getCurrentPage()+1, recPutawayVO.getPageSize());
		this.putawayDao.selectByExample(example);
		return page;
	}
	
	/**
	 * 根据条件，查询上架单信息
	 * @param recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午1:53:50<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> selectPutaway ( RecPutawayVO recPutawayVO ) throws Exception {
		if ( recPutawayVO == null ) {
			recPutawayVO = new RecPutawayVO();
		}
		if ( recPutawayVO.getPutaway() == null ) {
			recPutawayVO.setPutaway(new RecPutaway());
		}
		List<RecPutaway> listPutaway = this.putawayDao.selectPutaway(recPutawayVO);
		return listPutaway;
	}
	
	
	/**
	 * 更新上架单状态
	 * @param putawayId 上架单id
	 * @param status 上架单状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午1:37:01<br/>
	 * @author andy wang<br/>
	 */
	public int updatePutawayStatus ( String putawayId , Integer status ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayId) || status == null ) {
			log.error("updatePutawayStatus --> putawayId or status is null!");
			return 0;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		RecPutawayVO recPutawayVO = new RecPutawayVO(new RecPutaway());
		recPutawayVO.getPutaway().setPutawayId(putawayId);
		recPutawayVO.loginInfo();
		Example example = recPutawayVO.getExample();
		RecUtil.defWhere(example);
		RecPutaway record = new RecPutaway();
		record.setPutawayStatus(status);
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		return this.putawayDao.updateByExampleSelective(record , example);
	}
	
	
	/**
	 * 根据上架单id，查询上架单
	 * @param putawayId 上架单id
	 * @return 上架单
	 * @throws Exception
	 * @version 2017年2月22日 下午6:02:20<br/>
	 * @author andy wang<br/>
	 */
	public RecPutaway findPutawayById ( String putawayId ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayId) ) {
			log.error("findPutawayById --> putawayId is null!");
			return null;
		}
		RecPutawayVO recPutawayVO = new RecPutawayVO(new RecPutaway());
		recPutawayVO.getPutaway().setPutawayId(putawayId);
		return this.findPutawayByExample(recPutawayVO);
	}
	
	/**
	 * 根据条件查询上架单
	 * @param recPutawayVO 查询条件
	 * @return 上架单
	 * @throws Exception
	 * @version 2017年2月22日 下午6:01:50<br/>
	 * @author andy wang<br/>
	 */
	public RecPutaway findPutawayByExample ( RecPutawayVO recPutawayVO ) throws Exception {
		List<RecPutaway> list = this.listPutawayByExample(recPutawayVO);
		return PubUtil.isEmpty(list) ? null : list.get(0);
	}
	
	/**
	 * 保存上架单
	 * @param recPutaway 要保存的上架单
	 * @return 保存后的上架单
	 * @throws Exception
	 * @version 2017年2月22日 上午11:53:13<br/>
	 * @author andy wang<br/>
	 */
	public RecPutaway insertPutaway ( RecPutaway recPutaway ) throws Exception {
		if ( recPutaway == null ) {
			log.error("insertPutaway --> recPutaway is null!");
			return null;
		}
		recPutaway.setCreateTime(new Date());
		recPutaway.setUpdateTime(new Date());
		Principal loginUser = LoginUtil.getLoginUser();
		recPutaway.setCreatePerson(loginUser.getUserId());
		recPutaway.setUpdatePerson(loginUser.getUserId());
		recPutaway.setOpPerson(null);
		recPutaway.setOpTime(null);
		recPutaway.setRealQty(null);
		recPutaway.setRealWeight(null);
		recPutaway.setRealVolume(null);
		recPutaway.setPutawayId2(context.getStrategy4Id().getPutawaySeq());
		this.putawayDao.insertSelective(recPutaway);
		return recPutaway;
	}
	
	/**
	 * 根据条件，查询上架单集合
	 * @param recPutawayVO 上架单查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月22日 上午11:46:15<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> listPutawayByExample ( RecPutawayVO recPutawayVO ) throws Exception {
		if ( recPutawayVO == null ) {
			log.error("listPutaway --> recPutawayVO is null!");
			return new ArrayList<RecPutaway>();
		}
		recPutawayVO.loginInfo();
		Example example = recPutawayVO.getExample();
		return this.putawayDao.selectByExample(example);
	}
	/**
	 * 根据条件，查询上架单集合
	 * @param recPutawayVO 上架单查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月22日 上午11:46:15<br/>
	 * @author zwb<br/>
	 */
	public List<RecPutaway> listPutawayByExample2 ( RecPutawayVO recPutawayVO ) throws Exception {
		Example example = recPutawayVO.getExample();
		return this.putawayDao.selectByExample(example);
	}
	public List<String>getTask(String orgId){
		return putawayDao.getTask(orgId);
	}
}
