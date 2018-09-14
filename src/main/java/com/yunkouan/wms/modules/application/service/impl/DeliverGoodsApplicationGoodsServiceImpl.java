package com.yunkouan.wms.modules.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationGoodDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGood;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGoodsSku;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodsSkuService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodsSkuVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;

@Service
public class DeliverGoodsApplicationGoodsServiceImpl extends BaseService implements IDeliverGoodsApplicationGoodService{
	@Autowired
	private IDeliverGoodsApplicationGoodDao applicationGoodDao;

	@Autowired
	private IDeliverGoodsApplicationGoodsSkuService goodsSkuService;

	@Autowired
	private ISysParamService paramService;

	/**
	 * 新增申报商品
	 * @param applicationGoodVoList
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addApplicationGoods(DeliverGoodsApplicationVo applicationVo,Principal p){
		if(applicationVo.getApplicationGoodVoList() == null || applicationVo.getApplicationGoodVoList().isEmpty()) return;
		
		
		for(DeliverGoodsApplicationGoodVo goodVo:applicationVo.getApplicationGoodVoList()){
			
			BigDecimal decTotal = goodVo.getEntity().getDecPrice().multiply(goodVo.getEntity().getDecQty());
			//保存申报商品
			String id = IdUtil.getUUID();
			goodVo.getEntity().setId(id);
			goodVo.getEntity().setDecTotal(decTotal);
			goodVo.getEntity().setApplicationId(applicationVo.getEntity().getId());
			goodVo.getEntity().setOrgId(p.getOrgId());
			goodVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
			goodVo.getEntity().setCreatePerson(p.getUserId());
			goodVo.getEntity().setUpdatePerson(p.getUserId());
			//保存申报货品
			goodVo = goodsSkuService.addApplyGoodsSkus(goodVo,p);
			
			applicationGoodDao.insertSelective(goodVo.getEntity());
		}
	}
	
	/**
	 * 更新
	 * @param applicationVo
	 * @param p
	 */
	public void update(DeliverGoodsApplicationVo applicationVo,Principal p){
		if(applicationVo.getApplicationGoodVoList() == null || applicationVo.getApplicationGoodVoList().isEmpty()) return;
		
		for(DeliverGoodsApplicationGoodVo goodVo:applicationVo.getApplicationGoodVoList()){
			
			BigDecimal decTotal = goodVo.getEntity().getDecPrice().multiply(goodVo.getEntity().getDecQty());
			goodVo.getEntity().setDecTotal(decTotal);
			//保存申报货品
			goodVo = goodsSkuService.update(goodVo, p);
			//保存申报商品
			goodVo.getEntity().setUpdatePerson(p.getUserId());
			
			
			applicationGoodDao.updateByPrimaryKeySelective(goodVo.getEntity());
		}
	}
	
	/**
	 * 查询商品列表
	 * @param goodsVo
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationGoodVo> qryList(DeliverGoodsApplicationGoodVo goodsVo) throws DaoException, ServiceException{
		
		List<DeliverGoodsApplicationGood> recordList = applicationGoodDao.selectByExample(goodsVo.getExample());
		if(recordList == null || recordList.isEmpty()) return null;
		
		List<DeliverGoodsApplicationGoodVo> voList = new ArrayList<DeliverGoodsApplicationGoodVo>();
		for(DeliverGoodsApplicationGood good:recordList){
			DeliverGoodsApplicationGoodVo vo = chg(good);
			//查询申报货品
			DeliverGoodsApplicationGoodsSkuVo goodsSkuVo = new DeliverGoodsApplicationGoodsSkuVo(new DeliverGoodsApplicationGoodsSku());
			goodsSkuVo.getEntity().setApplyGoodsId(good.getId());
			List<DeliverGoodsApplicationGoodsSkuVo> skuVoList = goodsSkuService.qryList(goodsSkuVo);
			vo.setApplicationGoodSkuVoList(skuVoList);
			voList.add(vo);
		}
		return voList;
	}

	private DeliverGoodsApplicationGoodVo chg(DeliverGoodsApplicationGood entity) {
		DeliverGoodsApplicationGoodVo vo = new DeliverGoodsApplicationGoodVo(entity);
		vo.setOriginCountryName(paramService.getValue("COUNTORY_CODE", entity.getOriginCountry()));
		vo.setDutyModeName(paramService.getValue("DUTY_MODE", entity.getDutyMode()));
		vo.setUseToName(paramService.getValue("USE_TO", entity.getUseTo()));
		return vo;
	}

}
