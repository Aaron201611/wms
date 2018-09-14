/**
 * Created on 2017年3月13日
 * ShelflifeWarningVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.monitor.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.monitor.entity.WarningHandler;

import tk.mybatis.mapper.entity.Example;

/**
 * 异常管理
 * <br/><br/>
 * @Description 
 * @version 2017年3月13日 下午1:51:54<br/>
 * @author 王通<br/>
 */
public class WarningHandlerVO extends BaseVO {

	private static final long serialVersionUID = 7539617122998544603L;

	/**
	 * 处理信息
	 */
	private WarningHandler warningHandler; 
	/**
	 * 处理状态名称
	 */
	private String handlerStatusName;
	/**
	 * 处理人姓名
	 */
	private String createPersonName;
	/**
	 * 库存总数量
	 */
	private int stockCount;
	
	private List<InvStockVO> stockVoList;
	
	/**
	 * 构造方法
	 * @version 2017年3月14日 下午1:48:28<br/>
	 * @author 王通<br/>
	 */
	public WarningHandlerVO() {
	}

	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:16:48<br/>
	 * @author 王通<br/>
	 */
	public WarningHandlerVO searchCache() {
		return this;
	}
	
	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月14日 下午1:31:23<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Example getExample() {
		return null;
	}

	/**
	 * 属性 stockCount getter方法
	 * @return 属性stockCount
	 * @author 王通<br/>
	 */
	public int getStockCount() {
		return stockCount;
	}

	/**
	 * 属性 stockCount setter方法
	 * @param stockCount 设置属性stockCount的值
	 * @author 王通<br/>
	 */
	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	/**
	 * 属性 stockVoList getter方法
	 * @return 属性stockVoList
	 * @author 王通<br/>
	 */
	public List<InvStockVO> getStockVoList() {
		return stockVoList;
	}

	/**
	 * 属性 stockVoList setter方法
	 * @param stockVoList 设置属性stockVoList的值
	 * @author 王通<br/>
	 */
	public void setStockVoList(List<InvStockVO> stockVoList) {
		this.stockVoList = stockVoList;
	}

	/**
	 * 属性 warningHandler getter方法
	 * @return 属性warningHandler
	 * @author 王通<br/>
	 */
	public WarningHandler getWarningHandler() {
		return warningHandler;
	}

	/**
	 * 属性 warningHandler setter方法
	 * @param warningHandler 设置属性warningHandler的值
	 * @author 王通<br/>
	 */
	public void setWarningHandler(WarningHandler warningHandler) {
		this.warningHandler = warningHandler;
	}

	/**
	 * 属性 handlerStatusName getter方法
	 * @return 属性handlerStatusName
	 * @author 王通<br/>
	 */
	public String getHandlerStatusName() {
		return handlerStatusName;
	}

	/**
	 * 属性 handlerStatusName setter方法
	 * @param handlerStatusName 设置属性handlerStatusName的值
	 * @author 王通<br/>
	 */
	public void setHandlerStatusName(String handlerStatusName) {
		this.handlerStatusName = handlerStatusName;
	}

	/**
	 * 属性 createPersonName getter方法
	 * @return 属性createPersonName
	 * @author 王通<br/>
	 */
	public String getCreatePersonName() {
		return createPersonName;
	}

	/**
	 * 属性 createPersonName setter方法
	 * @param createPersonName 设置属性createPersonName的值
	 * @author 王通<br/>
	 */
	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
	}
}
