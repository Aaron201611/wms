package com.yunkouan.wms.modules.send.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.send.dao.IPickDetailDao;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.service.IPickDetailService;
import com.yunkouan.wms.modules.send.service.IPickLocationService;
import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;

@Service
public class PickDetailServiceImpl extends BaseService implements IPickDetailService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public IPickDetailDao pickDetailDao;
	
	@Autowired 
	public IPickLocationService pickLocationService;
	
	@Autowired 
	public ISkuService skuService;//货品接口
	
	/**
	 * 根据拣货id查询拣货明细
	 * @param pickId
	 * @param pickType
	 * @return
	 * @version 2017年2月17日 下午2:15:50<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickDetailVo> qryPickDetails(String pickId){
		//查询拣货单明细
		SendPickDetailVo param = new SendPickDetailVo();
		param.getSendPickDetail().setPickId(pickId);
		List<SendPickDetail> details = pickDetailDao.selectByExample(param.getConditionExample());
		List<SendPickDetailVo> detailVoList = new ArrayList<SendPickDetailVo>();
			
		details.stream().forEach(t->{
			SendPickDetailVo pickDetailVo = new SendPickDetailVo();						
			pickDetailVo.setSendPickDetail(t);
			detailVoList.add(pickDetailVo);			
		});
		return detailVoList;
	}
	
	/**
	 * 根据id获取拣货明细
	 * @param pickDetailId
	 * @return
	 */
	public SendPickDetailVo getVoById(String pickDetailId){
		if(StringUtils.isEmpty(pickDetailId)) return null;
		SendPickDetail detail = pickDetailDao.selectByPrimaryKey(pickDetailId);
		
		SendPickDetailVo detailVo = new SendPickDetailVo();
		detailVo.setSendPickDetail(detail);
		return detailVo;
	}
	
	/**
	 * 查找拣货明细及库位
	 * @param param
	 * @return
	 */
	public SendPickDetailVo qryDetailAndLocationById(String pickDetailId) throws Exception{
		if(StringUtils.isEmpty(pickDetailId)) return null;
		SendPickDetailVo detailVo = getVoById(pickDetailId);
		
		//货品信息 包装规格
		MetaSku sku = FqDataUtils.getSkuById(skuService, detailVo.getSendPickDetail().getSkuId());
		if(sku != null){
			detailVo.setSkuName(sku.getSkuName());
			detailVo.setSpecModel(sku.getSpecModel());
			detailVo.setSkuNo(sku.getSkuNo());
		}
		
		SendPickLocationVo param = new SendPickLocationVo();
		param.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
		param.getSendPickLocation().setPickDetailId(detailVo.getSendPickDetail().getPickDetailId());
		List<SendPickLocationVo> planLocations = pickLocationService.qryPickLocations(param);
		detailVo.setPlanPickLocations(planLocations);
		return detailVo;
		
	}
	
	/**
	 * 查询明细列表
	 * @param param
	 * @return
	 */
	public List<SendPickDetailVo> qryDetails(SendPickDetailVo param){
		
		List<SendPickDetail> details = pickDetailDao.selectByExample(param.getConditionExample());
		
		List<SendPickDetailVo> detailVoList = new ArrayList<SendPickDetailVo>();
		
		details.stream().forEach(t->{
			SendPickDetailVo pickDetailVo = new SendPickDetailVo();						
			pickDetailVo.setSendPickDetail(t);
			detailVoList.add(pickDetailVo);			
		});
		return detailVoList;
	}
	
	/**
	 * 查询拣货明细及拣货实际库位
	 * @param deliveryDetailId
	 * @return
	 */
	public List<SendPickDetailVo> qryDetailsAndLocation(SendPickDetailVo param,int pickType) throws Exception{
		List<SendPickDetail> details = pickDetailDao.selectByExample(param.getConditionExample());
		
		List<SendPickDetailVo> detailVoList = new ArrayList<SendPickDetailVo>();
		
		//查询实际库位
		for(SendPickDetail t:details){
			SendPickLocationVo loParam = new SendPickLocationVo();
			loParam.getSendPickLocation().setPickDetailId(t.getPickDetailId());
			loParam.getSendPickLocation().setPickType(pickType);
			List<SendPickLocationVo> realLocations = pickLocationService.qryPickLocations(loParam);
			SendPickDetailVo pickDetailVo = new SendPickDetailVo();						
			pickDetailVo.setSendPickDetail(t);
			pickDetailVo.setRealPickLocations(realLocations);
			detailVoList.add(pickDetailVo);	
		}
		return detailVoList;		
	}
	
	/**
	 * 删除记录
	 * @param pickDetailVo
	 * @throws Exception
	 */
	public void delEntity(SendPickDetailVo pickDetailVo)throws Exception{
		List<SendPickDetailVo> detailVoList = qryDetails(pickDetailVo);
		for(SendPickDetailVo detailVo:detailVoList){
			SendPickLocationVo locationVo = new SendPickLocationVo();
			locationVo.getSendPickLocation().setPickDetailId(detailVo.getSendPickDetail().getPickDetailId());
			pickLocationService.del(locationVo);
		}
		pickDetailDao.deleteByExample(pickDetailVo.getConditionExample());
		
	}
}
