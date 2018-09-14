package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.modules.meta.service.ILocationExtlService;
import com.yunkouan.wms.modules.send.dao.IPickLocationDao;
import com.yunkouan.wms.modules.send.entity.SendPickLocation;
import com.yunkouan.wms.modules.send.service.IPickLocationService;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;

@Service
public class PickLocationServiceImpl extends BaseService implements IPickLocationService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public IPickLocationDao pickLocationDao;
	
	@Autowired
	public ILocationExtlService locExtlService;
	
	/**
	 * 查询拣货库位列表
	 * @param pickDetailId
	 * @param pickType
	 * @return
	 * @version 2017年2月17日 下午3:46:36<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickLocationVo> qryPickLocations(SendPickLocationVo param) throws Exception{
		//查询计划拣货库位明细	
		if(param == null) return null;
		List<SendPickLocation> records = pickLocationDao.selectByExample(param.getConditionExample());
		List<SendPickLocationVo> results = new ArrayList<SendPickLocationVo>();
		
		for(SendPickLocation location:records){
			SendPickLocationVo vo = new SendPickLocationVo();
			MetaLocation l = FqDataUtils.getLocById(locExtlService, location.getLocationId());
			vo.setSendPickLocation(location);
			vo.setLocationComment(l.getLocationName());
			vo.setLocationNo(l.getLocationNo());
			results.add(vo);
		}
		
		return results;
	}
	
	/**
	 * 删除明细
	 * @param locationVo
	 * @throws Exception
	 */
	public void del(SendPickLocationVo locationVo)throws Exception{
		pickLocationDao.deleteByExample(locationVo.getConditionExample());
	}
}
