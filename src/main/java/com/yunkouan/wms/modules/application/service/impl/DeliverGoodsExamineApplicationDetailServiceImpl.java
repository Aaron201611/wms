package com.yunkouan.wms.modules.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsExamineApplicationDetailDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsExamineApplicationDetail;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodsSkuService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsExamineApplicationDetailService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationDetailVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationVo;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

@Service
public class DeliverGoodsExamineApplicationDetailServiceImpl extends BaseService implements IDeliverGoodsExamineApplicationDetailService{

	private Logger log = LoggerFactory.getLogger(DeliverGoodsExamineApplicationDetailServiceImpl.class);
	
	@Autowired
	private IDeliverGoodsExamineApplicationDetailDao exAppDetailDao;
	@Autowired
	private ISkuService skuService;
	
	@Autowired
	private IDeliverGoodsApplicationGoodsSkuService appGoodsSkuService;
	
	/**
	 * 新增核放申报明细货品
	 * @param applyGoodsSkuVoList
	 */
	public DeliverGoodsExamineApplicationVo addExamineApplicationDetails(DeliverGoodsExamineApplicationVo exAppVo,Principal p){
		if(exAppVo.getDetailVoList() == null || exAppVo.getDetailVoList().isEmpty()) return null;
		
		for(DeliverGoodsExamineApplicationDetailVo detailVo:exAppVo.getDetailVoList()){
			//保存货品明细
			String id = IdUtil.getUUID();
			detailVo.getEntity().setId(id);
			detailVo.getEntity().setExamineApplicationId(exAppVo.getEntity().getId());
			detailVo.getEntity().setOrgId(p.getOrgId());
			detailVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
			detailVo.getEntity().setCreatePerson(p.getUserId());
			detailVo.getEntity().setUpdatePerson(p.getUserId());
			exAppDetailDao.insertSelective(detailVo.getEntity());
		}
		return exAppVo;
	}
	
	/**
	 * 更新
	 * @param goodVo
	 * @param p
	 */
	public DeliverGoodsExamineApplicationVo update(DeliverGoodsExamineApplicationVo exApVo,Principal p){
		if(exApVo.getDetailVoList() == null || exApVo.getDetailVoList().isEmpty()) return null;
		
		//删除delDetailIdList的实体
		if(exApVo.getDelDetailIdList() != null && !exApVo.getDelDetailIdList().isEmpty()){
			for(String id:exApVo.getDelDetailIdList()){
				exAppDetailDao.deleteByPrimaryKey(id);
			}
		}
		BigDecimal goodsWt = new BigDecimal(0);
		for(DeliverGoodsExamineApplicationDetailVo detailVo:exApVo.getDetailVoList()){
			
			if(StringUtil.isNotBlank(detailVo.getEntity().getId())){
				//更新品明细
				detailVo.getEntity().setUpdatePerson(p.getUserId());
				exAppDetailDao.updateByPrimaryKeySelective(detailVo.getEntity());
			}
			else{
				//新增货品明细
				String id = IdUtil.getUUID();
				detailVo.getEntity().setId(id);
				detailVo.getEntity().setExamineApplicationId(detailVo.getEntity().getId());
				detailVo.getEntity().setOrgId(p.getOrgId());
				detailVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
				detailVo.getEntity().setCreatePerson(p.getUserId());
				detailVo.getEntity().setUpdatePerson(p.getUserId());
				exAppDetailDao.insertSelective(detailVo.getEntity());
			}
			goodsWt = goodsWt.add(detailVo.getEntity().getGrossWt());
		}
		exApVo.getEntity().setWeight(goodsWt);
		return exApVo;
	}
	
	/**
	 * 查询核放申报货品列表
	 * @param goodsSkuVo
	 * @return
	 */
	public List<DeliverGoodsExamineApplicationDetailVo> qryList(DeliverGoodsExamineApplicationDetailVo detailVo){
		List<DeliverGoodsExamineApplicationDetail> entityList = exAppDetailDao.selectByExample(detailVo.getExample());
		if(entityList == null || entityList.isEmpty()) return null;
		
		List<DeliverGoodsExamineApplicationDetailVo> recordList = new ArrayList<DeliverGoodsExamineApplicationDetailVo>();
		
		for(DeliverGoodsExamineApplicationDetail entity:entityList){
			DeliverGoodsExamineApplicationDetailVo recordVo = chg(entity);
			recordList.add(recordVo);
		}
		return recordList;
	}

	private DeliverGoodsExamineApplicationDetailVo chg(DeliverGoodsExamineApplicationDetail entity) {
		DeliverGoodsExamineApplicationDetailVo detailVo = new DeliverGoodsExamineApplicationDetailVo(entity);
		try {
			detailVo.setSkuVo((SkuVo) skuService.view(entity.getSkuId(), LoginUtil.getLoginUser()).getObj());
			detailVo.setAppGoodsSkuVo(appGoodsSkuService.view(entity.getGoodsSkuId()));
		} catch (DaoException | ServiceException e) {
			e.printStackTrace();
		}
		return detailVo;
	}
}
