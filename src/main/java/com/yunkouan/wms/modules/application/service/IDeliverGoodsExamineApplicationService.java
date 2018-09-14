package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineApplicationVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsExamineVo;

public interface IDeliverGoodsExamineApplicationService {

	/**
	 * 新增核放申报
	 * @param examineVo
	 */
	public DeliverGoodsExamineVo addExamineApplications(DeliverGoodsExamineVo examineVo,Principal p);
	
	/**
	 * 更新核放申报
	 * @param examineVo
	 * @param p
	 */
	public DeliverGoodsExamineVo update(DeliverGoodsExamineVo examineVo,Principal p);
	
	/**
	 * 查询核放申报列表
	 * @param examineApplicationVo
	 */
	public List<DeliverGoodsExamineApplicationVo> qryList(DeliverGoodsExamineApplicationVo examineApplicationVo) throws Exception;
}
