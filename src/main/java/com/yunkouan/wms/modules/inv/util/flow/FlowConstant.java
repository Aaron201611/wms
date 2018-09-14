/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午9:59:38<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.util.flow;

/**
 * 业务流程常量，用于流程上下文中存放数据的KEY <br/><br/>
 * @version 2017年3月5日 下午9:59:38<br/>
 * @author andy wang<br/>
 */
public class FlowConstant {

	/** 自动分配补货库位 - 待补货移位单 */
	public static final String AUTOREP_MAP_SHIFT = "AUTOREP_MAP_SHIFT";
	/** 自动分配补货库位 - 待补货货品 */
	public static final String AUTOREP_MAP_SKU = "AUTOREP_MAP_SKU";
	/** 自动分配补货库位 - 待补货总数 */
	public static final String AUTOREP_MAP_QTY = "AUTOREP_MAP_QTY";
	/** 自动分配补货库位 - 自动分配补货库位规则类 <br/> add by 王通 */
	public static final String AUTOREP_AUTOALLOT = "AUTOREP_AUTOALLOT";
}
