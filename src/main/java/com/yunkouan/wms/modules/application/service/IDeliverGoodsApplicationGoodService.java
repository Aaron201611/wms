package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationGoodVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;

public interface IDeliverGoodsApplicationGoodService {

	/**
	 * 新增申报商品
	 * @param applicationGoodVoList
	 */
	public void addApplicationGoods(DeliverGoodsApplicationVo applicationVo,Principal p);
	
	/**
	 * 更新
	 * @param applicationVo
	 * @param p
	 */
	public void update(DeliverGoodsApplicationVo applicationVo,Principal p);
	
	/**
	 * 查询商品列表
	 * @param goodsVo
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public List<DeliverGoodsApplicationGoodVo> qryList(DeliverGoodsApplicationGoodVo goodsVo) throws DaoException, ServiceException;
}
