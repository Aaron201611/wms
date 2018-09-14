/**
 * Created on 2017年2月16日
 * InvShiftVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.sys.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;

/**
 * 首页VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class EventVO extends BaseVO {

	/**
	 * 
	 * @version 2017年2月16日 上午10:06:30<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7066852346579800875L;

	
	/**
	 * 待处理总数
	 * @version 2017年3月14日 上午9:19:16<br/>
	 * @author 王通<br/>
	 */
	private Integer eventNum;
	
	private String warehouseName;
	/**
	 * 详情列表
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private List<EventDetailVO> eventList;
	
	/* getset *************************************************/




	/* method ********************************************/
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public EventVO searchCache () {
		return this;
	}

	/**
	 * 属性 eventList getter方法
	 * @return 属性eventList
	 * @author 王通<br/>
	 */
	public List<EventDetailVO> getEventList() {
		return eventList;
	}

	/**
	 * 属性 eventList setter方法
	 * @param eventList 设置属性eventList的值
	 * @author 王通<br/>
	 */
	public void setEventList(List<EventDetailVO> eventList) {
		this.eventList = eventList;
	}

	/**
	 * 属性 eventNum getter方法
	 * @return 属性eventNum
	 * @author 王通<br/>
	 */
	public Integer getEventNum() {
		return eventNum;
	}

	/**
	 * 属性 eventNum setter方法
	 * @param eventNum 设置属性eventNum的值
	 * @author 王通<br/>
	 */
	public void setEventNum(Integer eventNum) {
		this.eventNum = eventNum;
	}

	public String getWarehouseName() {
		return warehouseName;
	}
	

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

}
