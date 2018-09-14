/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 上午10:53:42<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.dao.IPutawayDetailDao;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.rec.util.RecUtil;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 上架单明细外部调用业务类<br/><br/>
 * @version 2017年2月22日 上午10:53:42<br/>
 * @author andy wang<br/>
 */
@Service
public class PutawayDetailExtlServiceImpl extends BaseService implements IPutawayDetailExtlService {
	
	/** 日志对象 <br/> add by andy wang */
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	/** 上架单明细Dao <br/> add by andy wang */
	@Autowired
	private IPutawayDetailDao putawayDetailDao;
	
	
	/**
	 * 根据上架单明细id，进行确认上架操作
	 * @param recPtwDetailVO 上架单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年5月2日 下午3:45:33<br/>
	 * @author andy wang<br/>
	 */
	public int confirmPtwDetail ( RecPutawayDetailVO recPtwDetailVO ) throws Exception {
		if ( recPtwDetailVO == null || recPtwDetailVO.getPutawayDetail() == null 
				|| StringUtil.isTrimEmpty(recPtwDetailVO.getPutawayDetail().getPutawayDetailId()) ) {
			log.error("confirmPtwDetail --> recPtwDetailVO is null!");
			return 0;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		RecPutawayDetail ptwDetail = recPtwDetailVO.getPutawayDetail();
		// 更新内容
		RecPutawayDetail record = new RecPutawayDetail();
		record.setRealPutawayQty(ptwDetail.getRealPutawayQty());
		record.setRealPutawayWeight(ptwDetail.getRealPutawayWeight());
		record.setRealPutawayVolume(ptwDetail.getRealPutawayVolume());
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		// 更新的条件
		RecPutawayDetailVO p_ptwDetailVO = new RecPutawayDetailVO();
		p_ptwDetailVO.loginInfo(loginUser);
		p_ptwDetailVO.getPutawayDetail().setPutawayDetailId(recPtwDetailVO.getPutawayDetail().getPutawayDetailId());
		return this.putawayDetailDao.updateByExampleSelective(record, p_ptwDetailVO.getExample());
	}
	
	/**
	 * 根据上架单明细id，更新上架单明细
	 * @param recPtwDetailVO 上架单明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月25日 上午11:10:48<br/>
	 * @author andy wang<br/>
	 */
	public int updatePtwDetail ( RecPutawayDetailVO recPtwDetailVO ) throws Exception {
		if ( recPtwDetailVO == null || recPtwDetailVO.getPutawayDetail() == null 
				|| StringUtil.isTrimEmpty(recPtwDetailVO.getPutawayDetail().getPutawayDetailId()) ) {
			log.error("updatePtw --> recPtwDetailVO is null!");
			return 0;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		RecPutawayDetail ptwDetail = recPtwDetailVO.getPutawayDetail();
		// 更新内容
		RecPutawayDetail record = new RecPutawayDetail();
		record.setPlanPutawayQty(ptwDetail.getPlanPutawayQty());
		record.setPlanPutawayWeight(ptwDetail.getPlanPutawayWeight());
		record.setPlanPutawayVolume(ptwDetail.getPlanPutawayVolume());
		record.setUpdatePerson(loginUser.getUserId());
		record.setUpdateTime(new Date());
		// 更新的条件
		RecPutawayDetailVO p_ptwDetailVO = new RecPutawayDetailVO();
		p_ptwDetailVO.loginInfo(loginUser);
		p_ptwDetailVO.getPutawayDetail().setPutawayDetailId(recPtwDetailVO.getPutawayDetail().getPutawayDetailId());
		return this.putawayDetailDao.updateByExampleSelective(record, p_ptwDetailVO.getExample());
	}
	
	
	/**
	 * 根据ASN单id集合，查询上架单明细
	 * @param listAsnId ASN单id集合
	 * @return 上架单明细集合
	 * @throws Exception
	 * @version 2017年3月23日 下午3:00:33<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPtwDetailByAsnId ( List<String> listAsnId ) throws Exception {
		if ( PubUtil.isEmpty(listAsnId) ) {
			log.error("listPtwDetailByAsnId --> listAsnId is null!");
			return null;
		}
		// 组装条件
		RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO();
		recPutawayDetailVO.setListAsnId(listAsnId);
		return this.listPutawayDetailByExample(recPutawayDetailVO);
	}
	
	
	/**
	 * 统计上架数/重/体
	 * @param asnId ASN单id
	 * @return 统计结果
	 * @throws Exception
	 * @version 2017年3月16日 上午11:13:06<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetailVO sumPtwDetailNumByAsnId ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("sumPtwDetailNumByAsnId --> asnId is null!");
			return null;
		} 
		RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO();
		recPutawayDetailVO.getPutawayDetail().setAsnId(asnId);
		Example example = recPutawayDetailVO.getExample();
		List<RecPutawayDetail> listDetail = this.putawayDetailDao.selectByExample(example);
		RecPutawayDetailVO result = new RecPutawayDetailVO();
		result.setSumRealQty(0d);
		result.setSumRealWeight(0d);
		result.setSumRealVolume(0d);
		if ( PubUtil.isEmpty(listDetail) ) {
			return result;
		}
		for (RecPutawayDetail recPutawayDetail : listDetail) {
			RecUtil.defPutawayDetailRealQWV(recPutawayDetail);
			result.setSumRealQty(result.getSumRealQty()+recPutawayDetail.getRealPutawayQty());
			result.setSumRealWeight(NumberUtil.add(result.getSumRealWeight(),recPutawayDetail.getRealPutawayWeight()));
			result.setSumRealVolume(NumberUtil.add(result.getSumRealVolume(),recPutawayDetail.getRealPutawayVolume()));
		}
		return result;
	}
	
	
	/**
	 * 根据Asn单id，查询上架明细
	 * @param asnId Asn单id
	 * @return Asn单对应的上架单明细
	 * @throws Exception
	 * @version 2017年3月9日 下午7:59:22<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPtwDetailByAsnId ( String asnId ) throws Exception {
		if ( StringUtil.isTrimEmpty(asnId) ) {
			log.error("listPtwDetailByAsnId --> asnId is null!");
			return null;
		}
		// 组装条件
		RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO();
		recPutawayDetailVO.getPutawayDetail().setAsnId(asnId);
		return this.listPutawayDetailByExample(recPutawayDetailVO);
	}
	
	/**
	 * 根据上架单id，查询上架单明细集合
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细Map集合
	 * @throws Exception
	 * @version 2017年2月22日 下午9:34:00<br/>
	 * @author andy wang<br/>
	 */
	public Map<String,RecPutawayDetail> mapPutawayDetailByPtwId ( String putawayId ) throws Exception {
		List<RecPutawayDetail> listPutawayDetail = this.listPutawayDetailByPtwId(putawayId);
		Map<String,RecPutawayDetail> mapPutawayDetail = new LinkedHashMap<String,RecPutawayDetail>();
		if ( !PubUtil.isEmpty(listPutawayDetail) ) {
			for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
				mapPutawayDetail.put(recPutawayDetail.getPutawayDetailId(), recPutawayDetail);
			}
		}
		return mapPutawayDetail;
	}
	
	/**
	 * 根据上架单id集合，删除上架单明细
	 * @param listPutawayId 上架单id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年3月2日 下午7:58:02<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayDetailByPtwId ( List<String> listPutawayId ) throws Exception {
		if ( PubUtil.isEmpty(listPutawayId) ) {
			log.error("deletePutawayDetailByPtwId --> listPutawayId is null!");
			return 0;
		}
		// 组装条件
		RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		recPutawayDetailVO.setListPutawayId(listPutawayId);
		recPutawayDetailVO.loginInfo();
		Example example = recPutawayDetailVO.getExample();
		return this.putawayDetailDao.deleteByExample(example);
	}
	
	/**
	 * 根据上架单id，删除上架单明细
	 * @param putawayId 上架单id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年3月2日 下午7:57:33<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayDetailByPtwId ( String putawayId ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayId) ) {
			log.error("deletePutawayDetailByPtwId --> putawayId is null!");
			return 0;
		}
		List<String> listPutawayId = new ArrayList<String>();
		listPutawayId.add(putawayId);
		return deletePutawayDetailByPtwId(listPutawayId);
	}
	
	/**
	 * 根据上架单明细id，删除上架单明细
	 * @param putawayDetailId 上架单明细id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午9:03:05<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayDetailById ( String putawayDetailId ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayDetailId) ) {
			log.error("deletePutawayDetail --> putawayDetailId is null!");
			return 0;
		}
		// 组装条件
		RecPutawayDetailVO recPutawayDetailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		recPutawayDetailVO.getPutawayDetail().setPutawayDetailId(putawayDetailId);
		recPutawayDetailVO.loginInfo();
		Example example = recPutawayDetailVO.getExample();
		return this.putawayDetailDao.deleteByExample(example);
	}
	
	/**
	 * 根据上架单id，查询上架单明细集合
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午9:34:00<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPutawayDetailByPtwId ( String putawayId ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayId) ) {
			log.error("listPutawayDetailByPtwId --> putawayId is null!");
			return null;
		}
		RecPutawayDetailVO  recPutawayDetailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		recPutawayDetailVO.getPutawayDetail().setPutawayId(putawayId);
		return this.listPutawayDetailByExample(recPutawayDetailVO);
	}
	
	
	/**
	 * 根据id，查询上架单明细
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细
	 * @throws Exception
	 * @version 2017年2月22日 下午9:26:20<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail findPtwDetailById ( String putawayDetailId ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayDetailId) ) {
			log.error("findPutawayDetailById --> putawayDetailId is null!");
			return null;
		}
		RecPutawayDetailVO  recPutawayDetailVO = new RecPutawayDetailVO(new RecPutawayDetail());
		recPutawayDetailVO.getPutawayDetail().setPutawayDetailId(putawayDetailId);
		return this.findPutawayDetailByExample(recPutawayDetailVO);
	}
	
	/**
	 * 查询上架单明细
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细
	 * @throws Exception
	 * @version 2017年2月22日 下午9:25:14<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail findPutawayDetailByExample ( RecPutawayDetailVO recPutawayDetailVO ) throws Exception {
		List<RecPutawayDetail> listPutawayDetail = this.listPutawayDetailByExample(recPutawayDetailVO);
		return PubUtil.isEmpty(listPutawayDetail) ? null : listPutawayDetail.get(0);
	}
	
	/**
	 * 查询上架单明细集合
	 * @param recPutawayDetailVO 查询条件
	 * @return 上架单明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午9:22:53<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> listPutawayDetailByExample ( RecPutawayDetailVO recPutawayDetailVO ) throws Exception {
		if ( recPutawayDetailVO == null ) {
			log.error("listPutaway --> recPutawayDetailVO is null!");
			return null;
		}
		recPutawayDetailVO.loginInfo();
		Example example = recPutawayDetailVO.getExample();
		return this.putawayDetailDao.selectByExample(example);
	}
	
	/**
	 * 保存上架单明细集合
	 * @param listRecPutawayDetail 要保存的上架单明细集合
	 * @return 保存后的上架单明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午1:22:18<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetail> insertPutawayDetail ( List<RecPutawayDetail> listRecPutawayDetail ) throws Exception {
		if ( listRecPutawayDetail == null ) {
			log.error("insertPutawayDetail --> listRecPutawayDetail is null!");
			return null;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		for (int i = 0; i < listRecPutawayDetail.size(); i++) {
			RecPutawayDetail recPutawayDetail = listRecPutawayDetail.get(i);
			if ( StringUtil.isTrimEmpty(recPutawayDetail.getPutawayDetailId()) ) {
				recPutawayDetail.setPutawayDetailId(IdUtil.getUUID());
			}
			recPutawayDetail.setRealPutawayQty(null);
			recPutawayDetail.setRealPutawayWeight(null);
			recPutawayDetail.setRealPutawayVolume(null);
			recPutawayDetail.setCreateTime(new Date());
			recPutawayDetail.setUpdateTime(new Date());
			recPutawayDetail.setCreatePerson(loginUser.getUserId());
			recPutawayDetail.setUpdatePerson(loginUser.getUserId());
			recPutawayDetail.setWarehouseId(LoginUtil.getWareHouseId());
			recPutawayDetail.setOrgId(loginUser.getOrgId());
			recPutawayDetail.setPutawayDetailId2(context.getStrategy4Id().getPutawayDetailSeq());
			this.putawayDetailDao.insert(recPutawayDetail);
		}
		return listRecPutawayDetail;
	}
	
	/**
	 * 保存上架单明细
	 * @param recPutawayDetail 要保存上架单明细
	 * @return 保存后的上架单明细
	 * @throws Exception
	 * @version 2017年2月22日 下午1:19:58<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayDetail insertPutawayDetail ( RecPutawayDetail recPutawayDetail ) throws Exception {
		if ( recPutawayDetail == null ) {
			log.error("insertPutawayDetail --> recPutawayDetail is null!");
			return null;
		}
		recPutawayDetail.setRealPutawayQty(null);
		recPutawayDetail.setRealPutawayWeight(null);
		recPutawayDetail.setRealPutawayVolume(null);
		recPutawayDetail.setCreateTime(new Date());
		recPutawayDetail.setUpdateTime(new Date());
		Principal loginUser = LoginUtil.getLoginUser();
		recPutawayDetail.setCreatePerson(loginUser.getUserId());
		recPutawayDetail.setUpdatePerson(loginUser.getUserId());
		recPutawayDetail.setPutawayDetailId2(context.getStrategy4Id().getPutawayDetailSeq());
		this.putawayDetailDao.insertSelective(recPutawayDetail);
		return recPutawayDetail;
	}
	
}
