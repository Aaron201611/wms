/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 下午1:27:26<br/>
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

import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.rec.dao.IPutawayLocationDao;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.service.IPutawayDetailExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayExtlService;
import com.yunkouan.wms.modules.rec.service.IPutawayLocationExtlService;
import com.yunkouan.wms.modules.rec.vo.RecPutawayDetailVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

import tk.mybatis.mapper.entity.Example;

/**
 * 上架单操作明细外调业务类<br/><br/>
 * @version 2017年2月22日 下午1:27:26<br/>
 * @author andy wang<br/>
 */
@Service
public class PutawayLocationExtlServiceImpl extends BaseService implements IPutawayLocationExtlService {
	
	/**
	 * 日志对象
	 * @version 2017年2月22日上午11:30:13<br/>
	 * @author andy wang<br/>
	 */
	private static Logger log = LoggerFactory.getLogger(Constant.LOG_SERVICE);
	
	/** 上架单操作明细Dao <br/> add by andy wang */
	@Autowired
	private IPutawayLocationDao putawayLocationDao;
	/** 上架单明细业务接口 <br/> add by andy wang */
	@Autowired
	private IPutawayDetailExtlService putawayDetailExtlService;
	/** 上架单业务接口 <br/> add by andy wang */
	@Autowired
	private IPutawayExtlService putawayExtlService;
	
	
	/* method *************************************************/
	

	/**
	 * 根据库位id，查询生效/作业中的上架单计划操作明细集合(库位库容度重算)
	 * @param param_locationId 库位id
	 * @return
	 * @throws Exception
	 * @version 2017年5月15日 下午4:58:05<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> listPtwLocRecalByLocId ( String param_locationId ) throws Exception {
		if ( StringUtil.isTrimEmpty(param_locationId) ) {
			log.error("listPtwLocByLocId --> param_locationId is null!");
			return null;
		}

		// 查询生效或作业中的上架单
		RecPutawayVO p_recPutawayVO = new RecPutawayVO();
		List<Integer> listPtwStatus = new ArrayList<Integer>();
		listPtwStatus.add(Constant.PUTAWAY_STATUS_ACTIVE);
		listPtwStatus.add(Constant.PUTAWAY_STATUS_WORKING);
		p_recPutawayVO.setListPtwStatus(listPtwStatus);
		List<RecPutaway> listPutaway = this.putawayExtlService.listPutawayByExample(p_recPutawayVO);
		if ( PubUtil.isEmpty(listPutaway) ) {
			return null;
		}
		List<String> listPutawayId = new ArrayList<String>();
		for (RecPutaway recPutaway : listPutaway) {
			listPutawayId.add(recPutaway.getPutawayId());
		}
		// 根据上架单id集合，查询上架单明细
		RecPutawayDetailVO p_recPutawayDetailVO = new RecPutawayDetailVO();
		p_recPutawayDetailVO.setListPutawayId(listPutawayId);
		List<RecPutawayDetail> listPutawayDetail = this.putawayDetailExtlService.listPutawayDetailByExample(p_recPutawayDetailVO);
		if ( PubUtil.isEmpty(listPutawayDetail) ) {
			return null;
		}

		List<String> listPutawayDetailId = new ArrayList<String>();
		Map<String,RecPutawayDetail> mapPutawayDetail = new HashMap<String,RecPutawayDetail>();
		for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
			listPutawayDetailId.add(recPutawayDetail.getPutawayDetailId());
			mapPutawayDetail.put(recPutawayDetail.getPutawayDetailId(), recPutawayDetail);
		}
		// 根据库位id，上架单明细id集合，查询计划上架操作明细
		RecPutawayLocationVO p_ptwLocationVO = new RecPutawayLocationVO();
		p_ptwLocationVO.setListPtwDetailId(listPutawayDetailId);
		p_ptwLocationVO.getPutawayLocation().setLocationId(param_locationId);
		p_ptwLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
		List<RecPutawayLocation> listPtwLocation = this.listPutawayLocationByExample(p_ptwLocationVO);
		if ( PubUtil.isEmpty(listPtwLocation) ) {
			return null;
		}
		List<RecPutawayLocationVO> listPtwLocVO = new ArrayList<RecPutawayLocationVO>();
		for (RecPutawayLocation recPutawayLocation : listPtwLocation) {
			RecPutawayLocationVO ptwLocationVO = new RecPutawayLocationVO(recPutawayLocation);
			RecPutawayDetail recPutawayDetail = mapPutawayDetail.get(recPutawayLocation.getPutawayDetailId());
			ptwLocationVO.setSkuId(recPutawayDetail.getSkuId());
			listPtwLocVO.add(ptwLocationVO);
		}
		return listPtwLocVO;
	}
	
	
	
	/**
	 * 根据上架单明细id，查询上架单操作明细
	 * @param listPtwDetailId 上架单明细id
	 * @param isPlan 是否计划操作明细
	 * —— true 查询计划操作明细
	 * —— false 查询实际操作明细
	 * —— null 查询所有类型的操作明细
	 * @return 上架单操作明细集合
	 * @throws Exception
	 * @version 2017年3月9日 下午8:14:02<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPtwLocationByDetailId ( List<String> listPtwDetailId , Boolean isPlan ) throws Exception {
		if ( PubUtil.isEmpty(listPtwDetailId) ) {
			log.error("listPtwLocationByDetailId --> listPtwDetailId is null!");
			return null;
		}
		RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO();
		recPutawayLocationVO.setListPtwDetailId(listPtwDetailId);
		if ( isPlan != null ) {
			if ( isPlan ) {
				recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
			} else {
				recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_REAL);
			}
		}
		return this.listPutawayLocationByExample(recPutawayLocationVO);
	}
	
	/**
	 * 根据上架单id，查询上架单操作明细
	 * @param putawayId 上架单id
	 * @param isPlan 是否计划操作明细
	 * —— true 查询计划操作明细
	 * —— false 查询实际操作明细
	 * —— null 查询所有类型的操作明细
	 * @return 上架单操作明细集合
	 * @throws Exception
	 * @version 2017年3月2日 下午10:35:25<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPutawayLocationByPtwId ( String putawayId , Boolean isPlan ) throws Exception {
		if ( StringUtil.isTrimEmpty(putawayId) ) {
			log.error("listPutawayLocationByPtwId --> putawayId is null!");
			return null;
		}
		RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO();
		recPutawayLocationVO.setPutawayId(putawayId);
		recPutawayLocationVO.loginInfo();
		if ( isPlan != null ) {
			if ( isPlan ) {
				recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
			} else {
				recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_REAL);
			}
		}
		return this.putawayLocationDao.selectPtwLocationByPtwId(recPutawayLocationVO);
	}
	
	/**
	 * 根据上架单明细id，查询上架单操作明细
	 * @param ptwDetailId 上架单明细id
	 * @param isPlan 是否计划操作明细
	 * —— true 查询计划操作明细
	 * —— false 查询实际操作明细
	 * —— null 查询所有类型的操作明细
	 * @return 上架单操作明细集合
	 * @throws Exception
	 * @version 2017年3月1日 下午2:37:41<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPtwLocByPtwDetailId ( String ptwDetailId , Boolean isPlan ) throws Exception {
		if ( StringUtil.isTrimEmpty(ptwDetailId) ) {
			log.error("listPutawayLocationByPtwDetailId --> ptwDetailId is null!");
			return null;
		}
		RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO(new RecPutawayLocation());
		recPutawayLocationVO.getPutawayLocation().setPutawayDetailId(ptwDetailId);
		if ( isPlan != null ) {
			if ( isPlan ) {
				recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_PLAN);
			} else {
				recPutawayLocationVO.getPutawayLocation().setPutawayType(Constant.PUTAWAY_LOCATIONTYPE_REAL);
			}
		}
		List<RecPutawayLocation> listPutawayLocation = this.listPutawayLocationByExample(recPutawayLocationVO);
		return listPutawayLocation;
	}
	
	
	/**
	 * 根据上架单操作明细id，删除上架单操作明细
	 * @param listPutawayLocationId 上架单操作明细id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午9:15:59<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayLocationById ( List<String> listPutawayLocationId ) throws Exception {
		if ( PubUtil.isEmpty(listPutawayLocationId) ) {
			log.error("deletePutawayLocationById --> listPutawayDetailId is null!");
			return 0;
		}
		RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO(new RecPutawayLocation());
		recPutawayLocationVO.setListPutawayLocationId(listPutawayLocationId);
		recPutawayLocationVO.loginInfo();
		Example example = recPutawayLocationVO.getExample();
		return this.putawayLocationDao.deleteByExample(example);
	}
	
	/**
	 * 根据上架单明细id，删除上架单操作明细
	 * @param ptwDetailId 上架单明细id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午9:08:32<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayLocationByPtwDetailId ( String ptwDetailId ) throws Exception {
		if ( StringUtil.isTrimEmpty(ptwDetailId) ) {
			log.error("deletePutawayLocationByPtwDetailId --> putawayDetailId is null!");
			return 0;
		}
		RecPutawayLocationVO recPutawayLocationVO = new RecPutawayLocationVO(new RecPutawayLocation());
		recPutawayLocationVO.getPutawayLocation().setPutawayDetailId(ptwDetailId);
		recPutawayLocationVO.loginInfo();
		Example example = recPutawayLocationVO.getExample();
		return this.putawayLocationDao.deleteByExample(example);
	}
	
	/**
	 * 更新上架单操作明细
	 * @param recPutawayLocationVO 上架单操作明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午7:53:35<br/>
	 * @author andy wang<br/>
	 */
	public int updatePutawayLocation ( RecPutawayLocationVO recPutawayLocationVO ) throws Exception {
		if ( recPutawayLocationVO == null || recPutawayLocationVO.getPutawayLocation() == null ) {
			log.error("updatePutawayLocation --> recPutawayLocationVO or putawayLocation is null!");
			return 0;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		RecPutawayLocation putawayLocation = recPutawayLocationVO.getPutawayLocation();
		// 组装条件
		RecPutawayLocationVO p_recPutawayLocationVO = new RecPutawayLocationVO(new RecPutawayLocation());
		p_recPutawayLocationVO.getPutawayLocation().setPutawayLocationId(putawayLocation.getPutawayLocationId());
		p_recPutawayLocationVO.loginInfo();
		Example example = p_recPutawayLocationVO.getExample();
		// 组装更新内容
		RecPutawayLocation record = new RecPutawayLocation();
		record.setPutawayQty(putawayLocation.getPutawayQty());
		record.setPutawayWeight(putawayLocation.getPutawayWeight());
		record.setPutawayVolume(putawayLocation.getPutawayVolume());
		record.setLocationId(putawayLocation.getLocationId());
		record.setUpdateTime(new Date());
		record.setUpdatePerson(loginUser.getUserId());
		return this.putawayLocationDao.updateByExampleSelective(record,example);
	}
	
	
	/**
	 * 根据条件，查询上架操作明细
	 * @param recPutawayLocationVO 查询条件
	 * @return 上架操作明细集合
	 * @throws Exception
	 * @version 2017年2月23日 上午11:07:22<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPutawayLocationByExample ( RecPutawayLocationVO recPutawayLocationVO ) throws Exception {
		if ( recPutawayLocationVO == null || recPutawayLocationVO.getPutawayLocation() == null ) {
			log.error("%s.listPutawayLocation --> recPutawayLocationVO is null!", getClass().getName());
			return null;
		}
		recPutawayLocationVO.loginInfo();
		Example example = recPutawayLocationVO.getExample();
		List<RecPutawayLocation> listLocation = this.putawayLocationDao.selectByExample(example);
		return listLocation;
	}
	
	/**
	 * 保存上架单操作明细集合
	 * @param listPtwLocation 上架单操作明细集合
	 * @return 保存后的上架单操作明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午2:01:00<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> insertPtwLocation ( List<RecPutawayLocation> listPtwLocation ) throws Exception {
		if ( listPtwLocation == null ) {
			log.error("%s.insertLocation --> listLocation is null!", getClass().getName());
			return null;
		}
		Principal loginUser = LoginUtil.getLoginUser();
		for (int i = 0; i < listPtwLocation.size(); i++) {
			RecPutawayLocation recPutawayLocation = listPtwLocation.get(i);
			if ( recPutawayLocation == null ) {
				continue;
			}
			if ( StringUtil.isTrimEmpty(recPutawayLocation.getPutawayLocationId()) ) {
				recPutawayLocation.setPutawayLocationId(IdUtil.getUUID());
			}
			recPutawayLocation.setCreateTime(new Date());
			recPutawayLocation.setUpdateTime(new Date());
			recPutawayLocation.setCreatePerson(loginUser.getUserId());
			recPutawayLocation.setUpdatePerson(loginUser.getUserId());
			recPutawayLocation.setWarehouseId(LoginUtil.getWareHouseId());
			recPutawayLocation.setOrgId(loginUser.getOrgId());
			recPutawayLocation.setPutawayLocationId2(context.getStrategy4Id().getPutawayLocationSeq());
			this.putawayLocationDao.insert(recPutawayLocation);
		}
		return listPtwLocation;
	}
	
	/**
	 * 保存上架单操作明细
	 * @param location 上架单操作明细
	 * @return 保存后的上架单操作明细
	 * @throws Exception
	 * @version 2017年2月22日 下午1:35:48<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayLocation insertLocation ( RecPutawayLocation location ) throws Exception {
		if ( location == null ) {
			log.error("%s.insertLocation --> location is null!", getClass().getName());
			return null;
		}
		location.setCreateTime(new Date());
		location.setUpdateTime(new Date());
		Principal loginUser = LoginUtil.getLoginUser();
		location.setCreatePerson(loginUser.getUserId());
		location.setUpdatePerson(loginUser.getUserId());
		location.setWarehouseId(LoginUtil.getWareHouseId());
		location.setOrgId(loginUser.getOrgId());
		location.setPutawayLocationId2(context.getStrategy4Id().getPutawayLocationSeq());
		this.putawayLocationDao.insertSelective(location);
		return location;
	}
	
}
