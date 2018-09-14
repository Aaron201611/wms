package com.yunkouan.wms.modules.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationGoodsSkuDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGoodsSku;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationGoodsSkuService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodsSkuVo;
import com.yunkouan.wms.modules.meta.service.ISkuService;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

@Service
public class DeliverGoodsApplicationGoodsSkuServiceImpl extends BaseService implements IDeliverGoodsApplicationGoodsSkuService{


	
	@Autowired
	private IDeliverGoodsApplicationGoodsSkuDao applicationGoodSkuDao;
	@Autowired
	private ISkuService skuService;
	
	/**
	 * 新增商品明细货品
	 * @param applyGoodsSkuVoList
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationGoodVo addApplyGoodsSkus(DeliverGoodsApplicationGoodVo goodVo,Principal p){
		if(goodVo.getApplicationGoodSkuVoList() == null || goodVo.getApplicationGoodSkuVoList().isEmpty()) return null;
		
//		BigDecimal dec_qty = new BigDecimal(0);
		BigDecimal qty_1 = new BigDecimal(0);
		BigDecimal qty_2 = new BigDecimal(0); 
		for(DeliverGoodsApplicationGoodsSkuVo gkVo:goodVo.getApplicationGoodSkuVoList()){
			
			BigDecimal decTotal = gkVo.getEntity().getDecPrice().multiply(goodVo.getEntity().getDecQty());
			//保存货品明细
			String id = IdUtil.getUUID();
			gkVo.getEntity().setId(id);
			gkVo.getEntity().setApplyGoodsId(goodVo.getEntity().getId());
			gkVo.getEntity().setDecTotal(decTotal);
			gkVo.getEntity().setAuditQty(new BigDecimal(0));
			gkVo.getEntity().setPassAuditQty(new BigDecimal(0));
			gkVo.getEntity().setRemainQty(gkVo.getEntity().getDecQty());
			gkVo.getEntity().setOrgId(p.getOrgId());
			gkVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
			gkVo.getEntity().setCreatePerson(p.getUserId());
			gkVo.getEntity().setUpdatePerson(p.getUserId());
			applicationGoodSkuDao.insertSelective(gkVo.getEntity());
//			BigDecimal qty = gkVo.getEntity().getDecQty();
//			dec_qty = dec_qty.add(qty == null?new BigDecimal(0) : qty);
			BigDecimal qty1 = gkVo.getEntity().getQty1();
			qty_1 = qty_1.add(qty1 == null ? new BigDecimal(0) : qty1);
			BigDecimal qty2 = gkVo.getEntity().getQty2();
			qty_2 = qty_2.add(qty2 == null ? new BigDecimal(0) : qty2);
		}
//		goodVo.getEntity().setDecQty(dec_qty);
		goodVo.getEntity().setQty1(qty_1);
		goodVo.getEntity().setQty2(qty_2);
		return goodVo;
	}
	
	/**
	 * 更新
	 * @param goodVo
	 * @param p
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationGoodVo update(DeliverGoodsApplicationGoodVo goodVo,Principal p){
		if(goodVo.getApplicationGoodSkuVoList() == null || goodVo.getApplicationGoodSkuVoList().isEmpty()) return null;
		
//		BigDecimal dec_qty = new BigDecimal(0);
		BigDecimal qty_1 = new BigDecimal(0);
		BigDecimal qty_2 = new BigDecimal(0);
		for(DeliverGoodsApplicationGoodsSkuVo gkVo:goodVo.getApplicationGoodSkuVoList()){
			
			BigDecimal decTotal = gkVo.getEntity().getDecPrice().multiply(goodVo.getEntity().getDecQty());
			gkVo.getEntity().setDecTotal(decTotal);
			//保存货品明细
			gkVo.getEntity().setUpdatePerson(p.getUserId());
			applicationGoodSkuDao.updateByPrimaryKeySelective(gkVo.getEntity());
//			dec_qty = dec_qty.add(gkVo.getEntity().getDecQty());
			BigDecimal qty1 = gkVo.getEntity().getQty1();
			qty_1 = qty_1.add(qty1 == null ? new BigDecimal(0) : qty1);
			BigDecimal qty2 = gkVo.getEntity().getQty2();
			qty_2 = qty_2.add(qty2 == null ? new BigDecimal(0) : qty2);
		}
//		goodVo.getEntity().setDecQty(dec_qty);
		goodVo.getEntity().setQty1(qty_1);
		goodVo.getEntity().setQty2(qty_2);
		return goodVo;
	}
	
	/**
	 * 查询申报货品列表
	 * @param goodsSkuVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationGoodsSkuVo> qryList(DeliverGoodsApplicationGoodsSkuVo goodsSkuVo) throws DaoException, ServiceException{
		Principal p = LoginUtil.getLoginUser();
		List<DeliverGoodsApplicationGoodsSku> entityList = applicationGoodSkuDao.selectByExample(goodsSkuVo.getExample());
		if(entityList == null || entityList.isEmpty()) return null;
		
		List<DeliverGoodsApplicationGoodsSkuVo> recordList = new ArrayList<DeliverGoodsApplicationGoodsSkuVo>();
		
		for(DeliverGoodsApplicationGoodsSku entity:entityList){
			DeliverGoodsApplicationGoodsSkuVo recordVo = chg(entity, p);
			recordList.add(recordVo);
		}
		return recordList;
	}
	
	private DeliverGoodsApplicationGoodsSkuVo chg(DeliverGoodsApplicationGoodsSku entity, Principal p) throws DaoException, ServiceException {
		DeliverGoodsApplicationGoodsSkuVo vo = new DeliverGoodsApplicationGoodsSkuVo(entity);
		vo.setSkuVo((SkuVo) skuService.view(entity.getSkuId(), p).getObj());
		return vo;
	}

	/**
	 * 查看
	 * @param goodsSkuId
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public DeliverGoodsApplicationGoodsSkuVo view (String goodsSkuId) throws DaoException, ServiceException{
		DeliverGoodsApplicationGoodsSku gsku = applicationGoodSkuDao.selectByPrimaryKey(goodsSkuId);
		Principal p = LoginUtil.getLoginUser();
		return chg(gsku, p);
	}
	
	/**
	 * 更新
	 */
	public void updateEntity(DeliverGoodsApplicationGoodsSku entity){
		applicationGoodSkuDao.updateByPrimaryKeySelective(entity);
	}
	
}
