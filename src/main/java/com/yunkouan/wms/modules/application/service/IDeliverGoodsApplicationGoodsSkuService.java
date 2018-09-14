package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationGoodsSku;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodsSkuVo;

public interface IDeliverGoodsApplicationGoodsSkuService {

	/**
	 * 新增商品明细货品
	 * @param applyGoodsSkuVoList
	 */
	public DeliverGoodsApplicationGoodVo addApplyGoodsSkus(DeliverGoodsApplicationGoodVo goodVo,Principal p);
	
	/**
	 * 更新
	 * @param goodVo
	 * @param p
	 */
	public DeliverGoodsApplicationGoodVo update(DeliverGoodsApplicationGoodVo goodVo,Principal p);
	
	/**
	 * 查询申报货品列表
	 * @param goodsSkuVo
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationGoodsSkuVo> qryList(DeliverGoodsApplicationGoodsSkuVo goodsSkuVo) throws DaoException, ServiceException;
	
	/**
	 * 查看
	 * @param goodsSkuId
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public DeliverGoodsApplicationGoodsSkuVo view (String goodsSkuId) throws DaoException, ServiceException;
	/**
	 * 更新
	 */
	public void updateEntity(DeliverGoodsApplicationGoodsSku entity);
}
