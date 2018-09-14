package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationDetailVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationVo;

public interface IDeliverGoodsExamineApplicationDetailService {

	/**
	 * 新增核放申报明细货品
	 * @param applyGoodsSkuVoList
	 */
	public DeliverGoodsExamineApplicationVo addExamineApplicationDetails(DeliverGoodsExamineApplicationVo exApVo,Principal p);
	
	/**
	 * 更新
	 * @param goodVo
	 * @param p
	 */
	public DeliverGoodsExamineApplicationVo update(DeliverGoodsExamineApplicationVo exApVo,Principal p);
	
	/**
	 * 查询核放申报货品列表
	 * @param goodsSkuVo
	 * @return
	 */
	public List<DeliverGoodsExamineApplicationDetailVo> qryList(DeliverGoodsExamineApplicationDetailVo detailVo);
}
