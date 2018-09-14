/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月12日 下午9:00:36<br/>
 * @author 王通<br/>
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvInventoryStatistics;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
  * 库存统计明细VO
  * @author 王通
  * @date 2017年2月14日 上午11:47:36
  *
 */
public class InvInventoryStatisticsVO extends BaseVO {
	
	private static final long serialVersionUID = 2239128176535268345L;
	
	public InvInventoryStatisticsVO(){this.inventoryStatistics = new InvInventoryStatistics();}
	public InvInventoryStatisticsVO( InvInventoryStatistics inventoryStatistics ) {
		this.inventoryStatistics = inventoryStatistics;
	}
	
	/**
	 * 库存定义
	 */
	private InvInventoryStatistics inventoryStatistics;
	
	/*查询参数*/
	private String ownerNameLike;
	@JsonFormat(pattern="yyyy/MM",locale = "zh" , timezone="GMT+8")
	private Date startDate;
	private String skuNameLike;
	private String skuNoLike;
	private Integer circle;
	
	/*额外显示参数*/
	private SkuVo skuVo;
	@JsonFormat(pattern="yyyy/MM",locale = "zh" , timezone="GMT+8")
    private Date statisticsEndDate;
	
	/* method *****************************************************/
	
	/**
	 * 查询缓存
	 * @version 2017年2月12日 下午9:30:33<br/>
	 * @author andy wang<br/>
	 */
	public InvInventoryStatisticsVO searchCache() {
		return this;
	}
	
	/*
	  * <p>Title: getExample</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.yunkouan.base.BaseVO#getExample()
	  */
	@Override
	public Example getExample() {
		Example example = new Example(InvInventoryStatistics.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("warehouseId", this.inventoryStatistics.getWarehouseId());
		c.andEqualTo("orgId", this.inventoryStatistics.getOrgId());
		return example;
	}
	public InvInventoryStatistics getInventoryStatistics() {
		return inventoryStatistics;
	}
	
	public void setInventoryStatistics(InvInventoryStatistics inventoryStatistics) {
		this.inventoryStatistics = inventoryStatistics;
	}
	
	public String getOwnerNameLike() {
		return ownerNameLike;
	}
	
	public void setOwnerNameLike(String ownerNameLike) {
		this.ownerNameLike = ownerNameLike;
	}
	
	public String getSkuNameLike() {
		return skuNameLike;
	}
	
	public void setSkuNameLike(String skuNameLike) {
		this.skuNameLike = skuNameLike;
	}
	
	public String getSkuNoLike() {
		return skuNoLike;
	}
	
	public void setSkuNoLike(String skuNoLike) {
		this.skuNoLike = skuNoLike;
	}
	
	public SkuVo getSkuVo() {
		return skuVo;
	}
	
	public void setSkuVo(SkuVo skuVo) {
		this.skuVo = skuVo;
	}
	
	public Date getStatisticsEndDate() {
		return statisticsEndDate;
	}
	
	public void setStatisticsEndDate(Date statisticsEndDate) {
		this.statisticsEndDate = statisticsEndDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Integer getCircle() {
		return circle;
	}
	public void setCircle(Integer circle) {
		this.circle = circle;
	}
	
	//循环累加方法
	public void circleAdd(InvInventoryStatistics entity) {
		this.inventoryStatistics.setAdjustNum(this.inventoryStatistics.getAdjustNum() + entity.getAdjustNum());
		this.inventoryStatistics.setInStockNum(this.inventoryStatistics.getInStockNum() + entity.getInStockNum());
		this.inventoryStatistics.setOutStockNum(this.inventoryStatistics.getOutStockNum() + entity.getOutStockNum());
		this.inventoryStatistics.setPeriodEndNum(this.inventoryStatistics.getPeriodEndNum() 
				+ entity.getAdjustNum() 
				+ entity.getInStockNum() 
				+ entity.getOutStockNum());
	}
}
