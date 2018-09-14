/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月1日 下午1:28:08<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.common.constant;

/**
 * 缓存名称常量<br/><br/>
 * @version 2017年3月1日 下午1:28:08<br/>
 * @author andy wang<br/>
 */
public class CacheName {
	public static final String COMMON_YES_NO = "COMMON_YES_NO";
	public static final String COMMON_STATUS = "COMMON_STATUS";

	/* 收货管理 add by andy wang begin ********************************************************/
	
	/** ASN单 - 状态<br/> add by andy */
	public static final String ASN_STATUS = "ASNSTATUS";
	/** ASN单 - 单据类型<br/> add by andy */
	public static final String ASN_DOCTYPE = "ASNDOCTYPE";
	/** ASN单 - 单据类型反向 <br/> add by andy wang */
	public static final String ASN_DOCTYPE_REVERSE = "ASNDOCTYPEREVERSE";
	/** ASN单 - 单据来源<br/> add by andy */
	public static final String ASN_DATAFROM = "ASNDATAFROM";
	/** 上架单 - 状态<br/> add by andy */
	public static final String PUTAWAY_STATUS = "PUTAWAYSTATUS";
	/** 上架单 - 类型<br/> add by andy */
	public static final String PUTAWAY_DOCTYPE = "PUTAWAYDOCTYPE";
	/** ASN单 - 同步erp状态<br/> add by andy */
	public static final String ASN_SYNC_ERP_STATUS = "ASNSYNCERPSTATUS";
	/* 收货管理 add by andy wang end ********************************************************/
	

	/* 主数据管理 add by andy wang begin ********************************************************/
	/** 库位 - 是否冻结<br/> add by andy */
	public static final String LOCATION_BLOCK = "LOCATIONBLOCK";
	/** 库位 - 状态<br/> add by andy */
	public static final String LOCATION_STATUS = "LOCATIONSTATUS";
	/** 仓库 - 类型 <br/> add by andy wang */
	public static final String WAREHOUSE_TYPE = "WAREHOUSETYPE";
	/** 仓库 - 状态 <br/> add by andy wang */
	public static final String WAREHOUSE_STATUS = "WAREHOUSESTATUS";
	/** 库区 - 状态 <br/> add by andy wang */
	public static final String AREA_STATUS = "AREASTATUS";
	/** 库区 - 类型 <br/> add by andy wang */
	public static final String AREA_TYPE = "AREATYPE";
	/** 月台 - 状态 <br/> add by andy wang */
	public static final String DOCK_STATUS = "DOCKSTATUS";
	/** 月台 - 是否收货月台 <br/> add by andy wang */
	public static final String DOCK_ISREC = "DOCKISREC";
	/** 月台 - 是否发货月台 <br/> add by andy wang */
	public static final String DOCK_ISSEND = "DOCKISSEND";
	/** 库位规格 - 库位规格状态 <br/> add by andy wang */
	public static final String LOCATIONSPEC_STATUS = "LOCATIONSPECSTATUS";

	/** 参数 - 状态 <br/> add by andy wang */
	public static final String PARAM_STATUS = "PARAMSTATUS";
	
	/* 主数据管理 add by andy wang end ********************************************************/
	
	/**
	 * 货品状态
	 * @version 2017年3月6日 下午4:38:45<br/>
	 * @author 王通<br/>
	 */
	public static final String SKU_STATUS = "SKU_STATUS";
	//货品状态反查
	public static final String SKU_STATUS_REVERSE = "SKU_STATUS_REVERSE";
	//发货单状态
	public static final String SEND_STATUS = "SENDSTATUS";
	//拣货单状态
	public static final String PICK_STATUS = "PICKSTATUS";
	//波次单状态
	public static final String WAVE_STATUS = "WAVESTATUS";
	//发货单单据类型
	public static final String DELIVERY_TYPE = "DELIVERYTYPE";
	//拣货单单据类型
	public static final String PICK_TYPE = "PICKTYPE";
	//出租状态
	public static final String PARK_RENT_STATUS = "PARKRENTSTATUS";
	//租金收取方式
	public static final String PARK_RENT_STYLE = "PARKRENTSTYLE";
	//计费方式
	public static final String PARK_FEE_STYLE = "PARKFEESTYLE";
	//结算周期
	public static final String PARK_SETTLE_CYCLE = "PARKSETTLECYCLE";
	//预警状态
	public static final String PARK_WARN_STATUS = "PARKWARNSTATUS";
	//预警频率
	public static final String WARN_FREQUENCY = "WARNFREQUENCY";
	//预警方式
	public static final String WARN_STYLE = "WARNSTYLE";
	//状态
	public static final String STATUS = "STATUS";
	//状态反查
	public static final String STATUS_REVERSE = "STATUS_REVERSE";
	//装车确认
	public static final String LOADCONFIRM = "LOADCONFIRM";
	//是否冻结
	public static final String ISBLOCK = "ISBLOCK";
	//盘点类型
	public static final String COUNT_TYPE = "COUNT_TYPE";
	//移位单类型
	public static final String SHIFT_TYPE = "SHIFT_TYPE";
	//盘点结果
	public static final String COUNT_RESULT = "COUNT_RESULT";
	//调账类型
	public static final String ADJUST_STATUS = "ADJUST_STATUS";
	//调账类型
	public static final String ADJUST_TYPE = "ADJUST_TYPE";
	//调账来源
	public static final String ADJUST_DATA_FROM = "ADJUST_DATA_FROM";
	//日志类型;
	public static final String LOG_TYPE = "LOG_TYPE";
	//日志增减类型;
	public static final String LOG_OP_TYPE = "LOG_OP_TYPE";
	//异常类型
	public static final String EXCEPTION_TYPE = "EXCEPTION_TYPE";
	//异常等级
	public static final String EXCEPTION_LEVEL = "EXCEPTION_LEVEL";
	//异常状态
	public static final String EXCEPTION_STATUS = "EXCEPTION_STATUS";
	//异常来源
	public static final String EXCEPTION_DATA_FROM = "EXCEPTION_DATA_FROM";
	//日志类型
	public static final String MATERIAL_LOG_TYPE = "MATERIAL_LOG_TYPE";
	/** 任务 - 状态 add by andy wang */
	public static final String TASK_STATUS = "TASK_STATUS";
	
	public static final String PARAM_EXT_KEY = "EXT_INTERFACE_KEY";
	
	public static final String PARAM_EMS_NO = "MSMQ_EMS_NO";
	public static final String PARAM_RECEIVER_ID = "MSMQ_RECEIVER_ID";
	public static final String PARAM_SENDER_ID = "MSMQ_SENDER_ID";

	public static final String PARAM_FULL_NAME = "MSMQ_FULL_NAME";
	public static final String PARAM_MESSAGE_LABEL = "MSMQ_MESSAGE_LABEL";

	public static final String PARAM_TIMER_LOG_RUNTIME = "TIMER_LOG_RUNTIME";
	public static final String PARAM_TIMER_LOG_OUTTIME = "TIMER_LOG_OUTTIME";
	public static final String PARAM_MSMQ_STOCK_PUSHTIME = "MSMQ_STOCK_PUSHTIME";

	public static final String MEASURE_UNIT = "MEASURE_UNIT";
	public static final String MEASURE_UNIT_REVERSE = "MEASURE_UNIT_REVERSE";
	public static final String MATERIAL_TYPE = "MATERIAL_TYPE";
	public static final String MATERIAL_TYPE_REVERSE = "MATERIAL_TYPE_REVERSE";
	public static final String EXPRESS_SERVICE_CODE = "EXPRESS_SERVICE_CODE";//物流公司
	
	public static final String PRINT_STATUS = "PRINT_STATUS";//打印状态
	public static final String SCAN_STATUS = "SCAN_STATUS";//扫描状态
	
	public static final String SEND_INTERCEPT_STATUS = "SEND_INTERCEPT_STATUS";//订单拦截状态
	
	public static final String INSPECT_RESULT = "INSPECT_RESULT";//查验结果
	
	/************************以下为 EMS相关 *********************************/
	public static final String EMS_INTERFACE_APPLY="EMS_INTERFACE_APPLY";
	public static final String EMS_INTERFACE_RCALL="EMS_INTERFACE_RCALL";
	public static final String EMS_INTERFACE_SENDER="EMS_INTERFACE_SENDER";
	public static final String EMS_INTERFACE_RECEIVER="EMS_INTERFACE_RECEIVER";
	public static final String EMS_LOGISTICS_CODE="EMS_LOGISTICS_CODE";
	public static final String EMS_LOGISTICS_NAME="EMS_LOGISTICS_NAME";
	public static final String EMS_EBP_CODE="EMS_EBP_CODE";
	public static final String EMS_GET_BILLNO_URL="EMS_GET_BILLNO_URL";
	
	//大客户号
	public static final String EMS_SYSACCOUNT="EMS_SYSACCOUNT";
	public static final String EMS_PASSWORD="EMS_PASSWORD";
	public static final String EMS_APPKEY="EMS_APPKEY";
	public static final String EMS_BUSINESSTYPE="EMS_BUSINESSTYPE";
	public static final String EMS_BILLNOAMOUNT="EMS_BILLNOAMOUNT";
	public static final String ERP_INTERFACE_UPDATE_BILLNO="ERP_INTERFACE_UPDATE_BILLNO";//erp更新运单号
	public static final String ERP_INTERFACE_UPDATE_GOODS="ERP_INTERFACE_UPDATE_GOODS";//erp商品信息更新接口
	public static final String ERP_INTERFACE_UPDATE_INVENTORY="ERP_INTERFACE_UPDATE_INVENTORY";//erp入库清单更新接口
	
	/************************分类监管*********************************/
	public static final String EMS_NO="EMS_NO";//账册号
	public static final String TRAED_CODE = "TRAED_CODE";//企业代码
	public static final String CUSTOM_CODE = "CUSTOM_CODE";//关区代码
	public static final String MSMQ_SEND_CHNALNAME = "MSMQ_SEND_CHNALNAME";//发送队列名称
	public static final String TYPE_INSTOCK_NONBOND = "TYPE_INSTOCK_NONBOND";//非保税入库
	public static final String TYPE_OUTSTOCK_NONBOND = "TYPE_OUTSTOCK_NONBOND";//非保税出库
	public static final String MSMQ_RECEIVE_QUE_NAME = "MSMQ_RECEIVE_QUE_NAME";//接收队列名称
	public static final String MSMQ_CHK_RESULT = "MSMQ_CHK_RESULT";//回执报文结果
	public static final String CURR_CODE = "CURR_CODE";//币制
	public static final String CURR_CODE_REVERSE = "CURR_CODE_REVERSE";//币制
	public static final String GOODS_NATURE = "GOODS_NATURE";//料件性质
	public static final String GOODS_NATURE_REVERSE = "GOODS_NATURE_REVERSE";//料件性质
	public static final String MSMQ_MESSAGE_TYPE_COUNT = "MSMQ_MESSAGE_TYPE_COUNT";//消息类型实盘数据
	public static final String MSMQ_MESSAGE_TYPE_INOUTSTOCK = "MSMQ_MESSAGE_TYPE_INOUTSTOCK";//消息类型实盘数据
	public static final String COUNTORY_CODE = "COUNTORY_CODE";//国家代码
	public static final String COUNTORY_CODE_REVERSE = "COUNTORY_CODE_REVERSE";//国家代码
														  
	public static final String CLASSMESSAGE_APPLY_TYPE = "CM_APPLY_TYPE";//分类监管申请类型
	public static final String CLASSMESSAGE_APPLY_BIZ_TYPE = "CLASSMESSAGE_APPLY_BIZ_TYPE";//分类监管申请业务类型
	public static final String CLASSMESSAGE_APPLY_BIZ_MODE = "CLASSMESSAGE_APPLY_BIZ_MODE";//分类监管申请类型
	public static final String APPLY_I_FLAG = "APPLY_I_FLAG";//进标志
	public static final String APPLY_E_FLAG = "APPLY_E_FLAG";//出标志
//	public static final String CLASSMESSAGE_ECI_EMS_NO = "CLASSMESSAGE_ECI_EMS_NO";//分类监管帐册编号
	public static final String CLASSMESSAGE_ECI_EMS_PROPERTY = "CLASSMESSAGE_ECI_EMS_PROPERTY";//分类监管帐册属性
	public static final String CLASSMESSAGE_ECI_EMS_LEVEL = "CLASSMESSAGE_ECI_EMS_LEVEL";//分类监管帐册级别
	public static final String CLASSMESSAGE_CHCL_TYPE = "CLASSMESSAGE_CHCL_TYPE";//分类监管核销类型
	public static final String CLASSMESSAGE_COLLECT_TYPE = "CLASSMESSAGE_COLLECT_TYPE";//分类监管集报标志
	public static final String CLASSMESSAGE_APP_STEP_ID = "CLASSMESSAGE_APP_STEP_ID";//当前环节
	public static final String CLASSMESSAGE_KERNEL_BIZ_MODE = "CLASSMESSAGE_KERNEL_BIZ_MODE";//分类监管
	public static final String KERNEL_BEG_AREA = "KERNEL_BEG_AREA";//起始地代码
	public static final String KERNEL_END_AREA = "KERNEL_END_AREA";//目的地代码
	public static final String CLASSMESSAGE_KERNEL_STEP_ID = "CLASSMESSAGE_KERNEL_STEP_ID";//当前环节
	public static final String ECI_MODIFY_MARK = "ECI_MODIFY_MARK";//辅助系统处理
	
	public static final String DECLARE_TYPE = "DECLARE_TYPE";//申请类型
	public static final String DECLARE_BIZ_TYPE = "DECLARE_BIZ_TYPE";//业务类型
	public static final String DECLARE_BIZ_MODE = "DECLARE_BIZ_MODE";//申请业务模式
	public static final String KERNELTYPE = "KERNELTYPE";//核放申请类型
	public static final String KERNEL_BIZTYPE = "KERNEL_BIZTYPE";//核放业务类型
	public static final String KERNEL_BIZMODE = "KERNEL_BIZMODE";//核放业务模式
	
	/************************分送集报*********************************/
	public static final String VEHICLE_STATUS = "VEHICLE_STATUS";//车辆状态
	public static final String APPLICATIONFORM_STATUS = "APPLICATIONFORM_STATUS";//申报表状态
	public static final String APPLICATION_STATUS = "APPLICATION_STATUS";//申报单状态
	public static final String EXAMINE_STATUS = "EXAMINE_STATUS";//核放单状态
	public static final String MESSAGE_TYPE_DECLARE = "MESSAGE_TYPE_DECLARE";//消息类型申请单申报
	public static final String MESSAGE_TYPE_KERNEL = "MESSAGE_TYPE_KERNEL";//消息类型申请单申报
	public static final String APPLICATION_TYPE_01 = "APPLICATION_TYPE_01";//申请单类型申请表申请单
	public static final String APPLICATION_BIZ_TYPE = "APPLICATION_BIZ_TYPE";//业务类型分送集报
	public static final String APPLICATION_BIZ_MODE = "APPLICATION_BIZ_MODE";//业务模式二线分送集报
	public static final String DIRECT_FLAG = "DIRECT_FLAG";//货物流向
	public static final String I_E_FLAG = "I_E_FLAG";//进出标志
	public static final String REL_TYPE = "REL_TYPE";//申请单关联类型
	public static final String DECLARE_CURRENT_STEP_ID = "DECLARE_CURRENT_STEP_ID";//当前环节
	public static final String AUDIT_STEP_GO = "AUDIT_STEP_GO";//放行
	public static final String AUDIT_STEP_BACK = "AUDIT_STEP_BACK";//退单
	public static final String AUDIT_STEP_TOCHECK = "AUDIT_STEP_TOCHECK";//转查验
	public static final String AUDIT_STEP_PASS = "AUDIT_STEP_PASS";//已审核
	public static final String KERNEL_BIZ_TYPE = "KERNEL_BIZ_TYPE";//业务类型分送集报
	public static final String KERNEL_BIZ_MODE = "KERNEL_BIZ_MODE";//业务模式二线分送集报
	public static final String KERNEL_TYPE = "KERNEL_TYPE";//核放单类型
	public static final String KERNEL_CURRENT_STEP_ID = "KERNEL_CURRENT_STEP_ID";//当前环节
	public static final String KERNEL_AUDIT_STEP_PASS = "KERNEL_AUDIT_STEP_PASS";//审批通过
	public static final String KERNEL_AUDIT_STEP_BACK = "KERNEL_AUDIT_STEP_BACK";//退单
	public static final String AREA_CODE = "AREA_CODE";//区域代码
	public static final String DUTY_MODE = "DUTY_MODE";//征免方式
	public static final String USE_TO = "USE_TO";//用途代码
	public static final String DECLARE_RECEIVER_ID = "DECLARE_RECEIVER_ID";//申请报文接收方系统编号 
	public static final String CHECK_IS_CIRCLE = "CHECK_IS_CIRCLE";//终结标志
	public static final String DETAIN_FLAG = "DETAIN_FLAG";//扣留标志
	public static final String WRAP_TYPE = "WRAP_TYPE";//包装种类
	public static final String MSMQ_DECLARE_CHNALNAME = "MSMQ_DECLARE_CHNALNAME";//申报单队列名称
	public static final String MSMQ_KENNEL_CHNALNAME = "MSMQ_KENNEL_CHNALNAME";//核放单队列名称
	public static final String MSMQ_OP_RESULT_CHNALNAME = "MSMQ_OP_RESULT_CHNALNAME";//校验结果队列名称
	public static final String MSMQ_DECLARE_AUDIT_CHNALNAME = "MSMQ_DECLARE_AUDIT_CHNALNAME";//申请单审核结果队列名称
	public static final String MSMQ_KENNEL_AUDIT_CHNALNAME = "MSMQ_KENNEL_AUDIT_CHNALNAME";//核放单审核结果队列名称

	//公司标志
	public static final String COPYRIGHT = "COPYRIGHT";//标题公司
	//申通
	public static final String ST_NO_POOL_MIN = "ST_NO_POOL_MIN";//数据库池中少于多少个运单号的时候获取一批数量的运单号
	public static final String ST_NO_POOL_BATH = "ST_NO_POOL_BATH";//一次从快递公司获取多少个运单号
	
	//哈尔滨海关辅助系统参数
	/**发送报文存放目录（远程）**/
	public static final String ASSIS_DOC_PATH_REQ = "ASSIS_DOC_PATH_REQ";
	/**响应报文存放目录（远程）**/
	public static final String ASSIS_DOC_PATH_RSP = "ASSIS_DOC_PATH_RSP";
	/**报文备份目录（本地）**/
	public static final String ASSIS_DOC_PATH_BACK = "ASSIS_DOC_PATH_BACK";
	/**报文备份目录（远程）**/
	public static final String ASSIS_DOC_PATH_REMOTE_BACK = "ASSIS_DOC_PATH_REMOTE_BACK";

	/**ASN导入报文存放目录（远程）**/
	public static final String ASN_IMPORT_PATH = "ASN_IMPORT_PATH";
	/**ASN导入报文备份目录（本地）**/
	public static final String ASN_IMPORT_BACK = "ASN_IMPORT_BACK";
	/**发货单导入报文存放目录（远程）**/
	public static final String DELIVER_IMPORT_PATH = "DELIVER_IMPORT_PATH";
	/**发货单导入报文备份目录（本地）**/
	public static final String DELIVER_IMPORT_BACK = "DELIVER_IMPORT_BACK";

	/**报文类型代码WMS**/
	public static final String ASSIS_MESSAGE_TYPE = "ASSIS_MESSAGE_TYPE";
	/**6位场站编号**/
	public static final String ASSIS_STATION_CODE = CUSTOM_CODE;
	/**10位企业代码**/
	public static final String ASSIS_TRADE_CODE = TRAED_CODE;
	/**帐册编号**/
	public static final String ASSIC_BOOK_NO = EMS_NO;
	/**发送方代码**/
	public static final String ASSIC_SOURCE_CODE = "WMS";
	/**接收方代码**/
	public static final String ASSIC_DEST_CODE = "FTZ";
	/**返回结果-成功**/
	public static final String ASSIC_RESULT_SUCCESS = "1";
}