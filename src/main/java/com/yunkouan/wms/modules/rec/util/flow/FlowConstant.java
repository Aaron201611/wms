/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午9:59:38<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow;

/**
 * 业务流程常量，用于流程上下文中存放数据的KEY <br/><br/>
 * @version 2017年3月5日 下午9:59:38<br/>
 * @author andy wang<br/>
 */
public class FlowConstant {
	/** 自动分配上架库位 - 动碰上架库位Map <br/> add by andy */
	public static final String AUTOPTW_MAP_DTOUCH_LOC = "AUTOPTW_MAP_LOC_DTOUCH";
	/** 自动分配上架库位 - 上架单明细集合 <br/> add by andy */
	public static final String AUTOPTW_LIST_PTWDETAILVO = "AUTOPTW_LIST_PTWDETAIL";
	/** 自动分配上架库位 - 自动分配上架库位规则类 <br/> add by andy */
	public static final String AUTOPTW_AUTOALLOT = "AUTOPTW_AUTOALLOT";
	/** 自动分配上架库位 - 货品Map <br/> add by andy */
	public static final String AUTPTW_MAP_SKU = "AUTPTW_MAP_SKU";
}
