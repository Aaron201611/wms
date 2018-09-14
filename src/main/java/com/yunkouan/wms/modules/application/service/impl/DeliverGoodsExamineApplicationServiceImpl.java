package com.yunkouan.wms.modules.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsExamineApplicationDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamineApplication;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamineApplicationDetail;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineApplicationDetailService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineApplicationService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationDetailVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineVo;

@Service
public class DeliverGoodsExamineApplicationServiceImpl extends BaseService implements IDeliverGoodsExamineApplicationService{

	private Logger log = LoggerFactory.getLogger(DeliverGoodsExamineApplicationServiceImpl.class);
	
	@Autowired
	private IDeliverGoodsExamineApplicationDao exAppDao;
	
	@Autowired
	private IDeliverGoodsExamineApplicationDetailService detailService;
	
	@Autowired
	private IDeliverGoodsApplicationService applicationService;
	

	
	/**
	 * 新增核放申报
	 * @param examineVo
	 */
	public DeliverGoodsExamineVo addExamineApplications(DeliverGoodsExamineVo examineVo,Principal p){
		if(examineVo.getExamineApplicationVoList() == null || examineVo.getExamineApplicationVoList().isEmpty()) return null;
		
		Integer total_pack_no = new Integer(0);
		BigDecimal total_weight = new BigDecimal(0);
		for(DeliverGoodsExamineApplicationVo exAppVo:examineVo.getExamineApplicationVoList()){
			//保存货品明细
			String id = IdUtil.getUUID();
			exAppVo.getEntity().setId(id);
			exAppVo.getEntity().setExamineId(examineVo.getEntity().getId());
			exAppVo.getEntity().setOrgId(p.getOrgId());
			exAppVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
			exAppVo.getEntity().setCreatePerson(p.getUserId());
			exAppVo.getEntity().setUpdatePerson(p.getUserId());
			exAppDao.insertSelective(exAppVo.getEntity());
			
			total_pack_no = total_pack_no + exAppVo.getEntity().getPackNo();
			total_weight = total_weight.add(exAppVo.getEntity().getWeight());
			
			if(exAppVo.getDetailVoList()== null || exAppVo.getDetailVoList().isEmpty())
				throw new BizException("examine_application_detail_is_null");
			//增加
			detailService.addExamineApplicationDetails(exAppVo, p);
		}
		examineVo.getEntity().setTotalPackNo(total_pack_no);
		examineVo.getEntity().setGoodsWt(total_weight);
		return examineVo;
	}
	
	/**
	 * 更新核放申报
	 * @param examineVo
	 * @param p
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsExamineVo update(DeliverGoodsExamineVo examineVo,Principal p){
		if(examineVo.getExamineApplicationVoList() == null || examineVo.getExamineApplicationVoList().isEmpty()) return null;
		
		//删除原核放申请单
		DeliverGoodsExamineApplicationVo _delParam = new DeliverGoodsExamineApplicationVo(new DeliverGoodsExamineApplication());
		_delParam.getEntity().setExamineId(examineVo.getEntity().getId());
		exAppDao.deleteByExample(_delParam.getExample());
		//新增核放申请单
		examineVo = addExamineApplications(examineVo, p);
		
//		for(DeliverGoodsExamineApplicationVo exAppVo:examineVo.getExamineApplicationVoList()){
//		//更新货品明细
//		detailService.update(exAppVo, p);
//		
//		if(StringUtil.isNotBlank(exAppVo.getEntity().getId())){
//			exAppVo.getEntity().setUpdatePerson(p.getUserId());
//			exAppDao.updateByPrimaryKeySelective(exAppVo.getEntity());
//		}
//		else{
//			//保存货品明细
//			String id = IdUtil.getUUID();
//			exAppVo.getEntity().setId(id);
//			exAppVo.getEntity().setExamineId(examineVo.getEntity().getId());
//			exAppVo.getEntity().setOrgId(p.getOrgId());
//			exAppVo.getEntity().setWarehouseId(p.getWarehouseId());
//			exAppVo.getEntity().setCreatePerson(p.getUserId());
//			exAppVo.getEntity().setUpdatePerson(p.getUserId());
//			exAppDao.insertSelective(exAppVo.getEntity());
//		}
//		totalPackNo = totalPackNo + exAppVo.getEntity().getPackNo();
//		weight = weight.add(exAppVo.getEntity().getWeight());
//	}
//		if(examineVo.getDelAppIdList() != null && !examineVo.getDelAppIdList().isEmpty()){
//			for(String id:examineVo.getDelAppIdList()){
//				exAppDao.deleteByPrimaryKey(id);
//			}
//		}
		
//		BigDecimal weight = new BigDecimal(0);
//		Integer totalPackNo = new Integer(0);
//		for(DeliverGoodsExamineApplicationVo exAppVo:examineVo.getExamineApplicationVoList()){
//			//更新货品明细
//			detailService.update(exAppVo, p);
//			
//			if(StringUtil.isNotBlank(exAppVo.getEntity().getId())){
//				exAppVo.getEntity().setUpdatePerson(p.getUserId());
//				exAppDao.updateByPrimaryKeySelective(exAppVo.getEntity());
//			}
//			else{
//				//保存货品明细
//				String id = IdUtil.getUUID();
//				exAppVo.getEntity().setId(id);
//				exAppVo.getEntity().setExamineId(examineVo.getEntity().getId());
//				exAppVo.getEntity().setOrgId(p.getOrgId());
//				exAppVo.getEntity().setWarehouseId(p.getWarehouseId());
//				exAppVo.getEntity().setCreatePerson(p.getUserId());
//				exAppVo.getEntity().setUpdatePerson(p.getUserId());
//				exAppDao.insertSelective(exAppVo.getEntity());
//			}
//			totalPackNo = totalPackNo + exAppVo.getEntity().getPackNo();
//			weight = weight.add(exAppVo.getEntity().getWeight());
//		}
//		examineVo.getEntity().setTotalPackNo(totalPackNo);
//		examineVo.getEntity().setGoodsWt(weight);
		return examineVo;
	}
	
	/**
	 * 查询核放申报列表
	 * @param examineApplicationVo
	 */
	public List<DeliverGoodsExamineApplicationVo> qryList(DeliverGoodsExamineApplicationVo examineApplicationVo) throws Exception{
		//查询申请单列表
		List<DeliverGoodsExamineApplication> entityList = exAppDao.selectByExample(examineApplicationVo.getExample());
		if(entityList == null || entityList.isEmpty()) return null;
		
		List<DeliverGoodsExamineApplicationVo> recordList = new ArrayList<DeliverGoodsExamineApplicationVo>();
		
		for(DeliverGoodsExamineApplication entity:entityList){
			DeliverGoodsExamineApplicationVo recordVo = new DeliverGoodsExamineApplicationVo(entity);
			//查询申请单号
			DeliverGoodsApplicationVo applicationVo = applicationService.view(entity.getApplicationId());
			recordVo.setApplicationNo(applicationVo.getEntity().getApplicationNo());
			//查询明细
			DeliverGoodsExamineApplicationDetailVo detailVo = new DeliverGoodsExamineApplicationDetailVo(new DeliverGoodsExamineApplicationDetail());
			detailVo.getEntity().setExamineApplicationId(entity.getId());
			List<DeliverGoodsExamineApplicationDetailVo> detailVoList = detailService.qryList(detailVo);
			recordVo.setDetailVoList(detailVoList);
			
			recordList.add(recordVo);
		}
		return recordList;
	}

}
