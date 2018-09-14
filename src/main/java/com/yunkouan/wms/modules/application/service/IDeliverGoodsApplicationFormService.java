package com.yunkouan.wms.modules.application.service;

import java.util.List;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationFormVo;

public interface IDeliverGoodsApplicationFormService {

	/**
	 * 分页查询
	 * @param formVo
	 * @return
	 */
	public ResultModel pageList(DeliverGoodsApplicationFormVo formVo);
	/**
	 * 查询
	 * @param formVo
	 * @return
	 */
	public List<DeliverGoodsApplicationFormVo> qryList(DeliverGoodsApplicationFormVo formVo);
	/**
	 * 新增
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo add(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception;
	
	/**
	 * 检查申报表编号是否存在
	 * @param formNo
	 */
	public void formNoIsExist(String formNo);
	
	/**
	 * 新增生效
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo saveAndEnable(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception;
	
	/**
	 * 新增或更新
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo addOrUpdate(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception;
	/**
	 * 修改
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo update(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception;
	
	/**
	 * 查看
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo view(String formId);
	
	/**
	 * 生效
	 * @param formVo
	 * @return
	 */
	public void enable(String formId,Principal p)throws Exception;
	
	/**
	 * 失效
	 * @param formVo
	 * @return
	 */
	public void disable(String formId,Principal p)throws Exception;
	
	/**
	 * 取消
	 * @param formVo
	 * @return
	 */
	public void cancel(String formId,Principal p)throws Exception;
	
	
}
