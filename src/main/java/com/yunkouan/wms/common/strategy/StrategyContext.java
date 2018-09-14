package com.yunkouan.wms.common.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.IStrategyService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.modules.assistance.service.IAssisService;
import com.yunkouan.wms.modules.send.service.IERPService;

/**
 * @function 策略/规则上下文（调用策略/规则的入口）
 * @author tphe06
 */
@Service
public class StrategyContext {
	@Autowired
	private IStrategyService service;

	/** 
	* @Title: getStrategy4Erp 
	* @Description: ERP系统接口
	* @auth tphe06
	* @time 2018 2018年8月10日 上午11:40:17
	* @return
	* IERPService
	*/
	public IERPService getStrategy4Erp() {
		IERPService bean = getBean(null, null, "06");
		return bean;
	}

	/** 
	* @Title: getStrategy4Assis 
	* @Description: 海关辅助系统接入实现
	* @auth tphe06
	* @time 2018 2018年8月6日 下午3:27:27
	* @return
	* IAssisService
	*/
	public IAssisService getStrategy4Assis() {
		IAssisService bean = getBean(null, null, "05");
		return bean;
	}

	/**
	 * 得到备份主键生成策略实现
	 * @return
	 */
	public IIdRule getStrategy4Id() {
		IIdRule bean = getBean(null, null, "04");
		if(bean == null) throw new BizException("valid_id_no_rule");
		return bean;
	}

	/**
	 * 得到单据编号生成策略实现
	 * @param orgid
	 * @return
	 */
	public INoRule getStrategy4No(String orgid, String warehouseid) {
		INoRule bean = getBean(orgid, warehouseid, "03");
		if(bean == null) throw new BizException("valid_datano_no_rule");
		return bean;
	}

	/**
	 * 得到上架策略实现
	 * @param orgid 组织id
	 * @return
	 */
	public IPutawayRule getStrategy4Putaway(String orgid, String warehouseid) {
		IPutawayRule bean = getBean(orgid, warehouseid, "01");
		if(bean == null) throw new BizException("valid_putaway_no_rule");
		return bean;
	}

	/**
	 * 得到拣货策略实现
	 * @param orgid 组织id
	 * @return
	 */
	public IPickupRule getStrategy4Pickup(String orgid, String warehouseid) {
		IPickupRule bean = getBean(orgid, warehouseid, "02");
		if(bean == null) throw new BizException("valid_pickup_no_rule");
		return bean;
	}

	/**
	 * 根据组织id和策略编号得到策略的实现（抽象类或者接口），通用
	 * 如果数据库未找到记录，则返回null
	 * @param orgid 组织id
	 * @param strategyNo 策略编号
	 * @return
	 */
	private <T> T getBean(String orgid, String warehouseid, String strategyNo) {
		String beanName = service.getStrategy(orgid, warehouseid, strategyNo);
		return SpringContextHolder.getBean(beanName);
	}
}