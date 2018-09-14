/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:30:30<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.common.constant;

/**
 * 常量类<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:30:30<br/>
 * @author andy wang<br/>
 */
public class Constant {
	
	/* 公共验证 begin *************************************************************/
	public static final String DOUBLE_MAX = "999999999999999";
	public static final long INTEGER_MAX = 99999999999l;
	public static final String DECIMAL_MAX = "99999999999";
	/* 公共验证 end *************************************************************/
	
	/* 公共常量 begin *************************************************************/
	public static final String ROOT_CODE = "001";
	public static final String LOG_SERVICE = "com.yunkouan.wms.service";
	public static final String LOG_CONTROLLER = "com.yunkouan.wms.controller";
	public static final String LOG_UTIL = "com.yunkouan.wms.util";
	public static final int SCALE_WEIGHT = 2;
	public static final int SCALE_VOLUME = 6;
	public static final int STRATEGY_MODEL_BIND = 1;//库位货品策略模式-绑定
	public static final int STRATEGY_MODEL_AUTO = 0;//库位货品策略模式-自动
	/* 公共常量 end *************************************************************/
	
	/* 收货常量 begin add by andy wang *************************************************/
	/** ASN单状态 - 打开 <br/> add by andy wang */
	public static final int ASN_STATUS_OPEN = 10;
	/** ASN单状态 - 生效 <br/> add by andy wang */
	public static final int ASN_STATUS_ACTIVE = 20;
	/** ASN单状态 - 部分收货 <br/> add by andy wang */
	public static final int ASN_STATUS_PARTRECEIVE = 50;
	/** ASN单状态 - 全部收货 <br/> add by andy wang */
	public static final int ASN_STATUS_RECEIVED = 60;
	/** ASN单状态 - 部分上架 <br/> add by andy wang */
	public static final int ASN_STATUS_PARTPUTAWAY = 70;
	/** ASN单状态 - 全部上架 <br/> add by andy wang */
	public static final int ASN_STATUS_PUTAWAY = 80;
	/** ASN单状态 - 取消 <br/> add by andy wang */
	public static final int ASN_STATUS_CANCEL = 99;
	
	/** ASN单普通入库 <br/> add by andy wang */
	public static final int ASN_DOCTYPE_NORMAL = 100;
	/** ASN单转仓入库 <br/> add by andy wang */
	public static final int ASN_DOCTYPE_TRANSFER = 110;
	/** ASN单退货入库 <br/> add by andy wang */
	public static final int ASN_DOCTYPE_RETURNED = 120;
	/** ASN单拒收入库 <br/> add by andy wang */
	public static final int ASN_DOCTYPE_REJECT = 130;
	/** ASN单其他入库 <br/> add by andy wang */
	public static final int ASN_DOCTYPE_OTHER = 140;

	/** 手工 <br/> add by andy wang */
	public static final int ASN_DATAFROM_NORMAL = 100;
	/** 其他 <br/> add by andy wang */
	public static final int ASN_DATAFROM_OTHER = 120;
	/** 导入 <br/> add by andy wang */
	public static final int ASN_DATAFROM_IMPORT = 110;
	
	/** 上架单状态 - 取消 <br/> add by andy wang */
	public static final int PUTAWAY_STATUS_CANCEL = 99;
	/** 上架单状态 - 打开 <br/> add by andy wang */
	public static final int PUTAWAY_STATUS_OPEN = 10;
	/** 上架单状态 - 生效 <br/> add by andy wang */
	public static final int PUTAWAY_STATUS_ACTIVE = 20;	
	/** 上架单状态 - 作业中 <br/> add by andy wang */
	public static final int PUTAWAY_STATUS_WORKING = 30;
	/** 上架单状态 - 作业完成 <br/> add by andy wang */
	public static final int PUTAWAY_STATUS_COMPLETE = 40;
	/** 计划上架 <br/> add by andy wang */
	public static final int PUTAWAY_LOCATIONTYPE_PLAN = 10;
	/** 实际上架 <br/> add by andy wang */
	public static final int PUTAWAY_LOCATIONTYPE_REAL = 20;
	/** 收货上架单 <br/> add by andy wang */
	public static final int PUTAWAY_DOCTYPE_RECEIVE = 150;
	/** 加工上架单 <br/> add by andy wang */
	public static final int PUTAWAY_DOCTYPE_MACHINING= 160;
	/** 不良品上架单 <br/> add by andy wang */
	public static final int PUTAWAY_DOCTYPE_BAD = 170;
	/* 收货常量 end add by andy wang *************************************************/
	

	/* 仓库常量 begin add by andy wang ***********************************************/
	/** 仓库状态 - 打开 <br/> add by andy wang */
	public static final int WAREHOUSE_STATUS_OPEN = 10;
	/** 仓库状态 - 生效 <br/> add by andy wang */
	public static final int WAREHOUSE_STATUS_ACTIVE = 20;
	
	/** 仓库类型 - 海关监管仓库 <br/> add by andy wang */
	public static final int WAREHOUSE_TYPE_CUSTOMS = 800;
	/** 仓库类型 - 海关监管仓库 <br/> add by andy wang */
	public static final int WAREHOUSE_TYPE_NOTCUSTOMS = 810;//普通仓
	
	public static final int WAREHOUSE_TYPE_BS = 820;//保税仓库
	/* 仓库常量 end add by andy wang ***********************************************/
	
	
	/* 库位常量 begin add by andy wang ***********************************************/
	/** 库位冻结标识 - 冻结 <br/> add by andy wang */
	public static final int LOCATION_BLOCK_TRUE = 1;
	/** 库位冻结标识 - 未冻结 <br/> add by andy wang */
	public static final int LOCATION_BLOCK_FALSE = 0;
	
	/** 库位状态 - 打开 <br/> add by andy wang */
	public static final int LOCATION_STATUS_OPEN = 10;
	/** 库位状态 - 生效 <br/> add by andy wang */
	public static final int LOCATION_STATUS_ACTIVE = 20;
	/** 是否默认库位 - 是 <br/> add by andy wang */
	public static final int LOCATION_ISDEFAULT_TRUE = 1;
	/** 是否默认库位 - 否 <br/> add by andy wang */
	public static final int LOCATION_ISDEFAULT_FLASE = 0;
	/** 默认库位的名称 <br/> add by andy wang */
	public static final String LOCATION_NAME_DEFAULT = "默认暂存库位";

	/** 库位类型 - 收货暂存区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_TEMPSTORAGE = Constant.AREA_TYPE_TEMPSTORAGE;
	/** 库位类型 - 存储区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_STORAGE = Constant.AREA_TYPE_STORAGE;
	/** 库位类型 - 拣货区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_PICKUP = Constant.AREA_TYPE_PICKUP;
	/** 库位类型 - 二次分拣区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_TWICEPICKUP = Constant.AREA_TYPE_TWICEPICKUP;
	/** 库位类型 - 备货区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_BACKUP = Constant.AREA_TYPE_BACKUP;
	/** 库位类型 - 发货区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_SEND = Constant.AREA_TYPE_SEND;
	/** 库位类型 - 加工区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_BAD = Constant.AREA_TYPE_BAD;
	/** 库位类型 - 退货区 <br/> add by andy wang */
	public static final int LOCATION_TYPE_T = Constant.AREA_TYPE_T;
	/* 库位常量 end add by andy wang ***********************************************/

	/* 月台常量 begin add by andy wang ***********************************************/
	/** 月台状态 - 打开 <br/> add by andy wang */
	public static final int DOCK_STATUS_OPEN = 10;
	/** 月台状态 - 生效 <br/> add by andy wang */
	public static final int DOCK_STATUS_ACTIVE = 20;
	/** 月台 - 收货月台 <br/> add by andy wang */
	public static final int DOCK_ISREC_YES = 1;
	/** 月台状态 - 非收货月台 <br/> add by andy wang */
	public static final int DOCK_ISREC_NO = 0;
	/** 月台状态 - 发货月台 <br/> add by andy wang */
	public static final int DOCK_ISSEND_YES = 1;
	/** 月台状态 - 非发货月台 <br/> add by andy wang */
	public static final int DOCK_ISSEND_NO = 0;
	/* 月台常量 end add by andy wang ***********************************************/
	
	/* 库区常量 begin add by andy wang ***********************************************/
	/** 库区状态 - 打开 <br/> add by andy wang */
	public static final int AREA_STATUS_OPEN = 10;
	/** 库区状态 - 生效 <br/> add by andy wang */
	public static final int AREA_STATUS_ACTIVE = 20;

	/** 库区类型 - 收货暂存区 <br/> add by andy wang */
	public static final int AREA_TYPE_TEMPSTORAGE = 10;
	/** 库区类型 - 存储区 <br/> add by andy wang */
	public static final int AREA_TYPE_STORAGE = 20;
	/** 库区类型 - 拣货区 <br/> add by andy wang */
	public static final int AREA_TYPE_PICKUP = 30;
	/** 库区类型 - 二次分拣区 <br/> add by andy wang */
	public static final int AREA_TYPE_TWICEPICKUP = 40;
	/** 库区类型 - 备货区 <br/> add by andy wang */
	public static final int AREA_TYPE_BACKUP = 50;
	/** 库区类型 - 发货区 <br/> add by andy wang */
	public static final int AREA_TYPE_SEND = 60;
	/** 库区类型 - 不良品区 <br/> add by andy wang */
	public static final int AREA_TYPE_BAD = 70;
	/** 库区类型 - 退货区 <br/> add by andy wang */
	public static final int AREA_TYPE_T = 80;

	/** 库位规格状态 - 打开 <br/> add by andy wang */
	public static final Integer LOCATIONSPEC_STATUS_OPEN = 10;
	/** 库位规格状态 - 生效 <br/> add by andy wang */
	public static final Integer LOCATIONSPEC_STATUS_ACTIVE = 20;
	
	/** 参数状态 - 打开 <br/> add by andy wang */
	public static final Integer SYSPARAM_STATUS_OPEN = 10;
	/** 参数状态 - 生效 <br/> add by andy wang */
	public static final Integer SYSPARAM_STATUS_ACTIVE = 20;
	/* 库区常量 end add by andy wang ***********************************************/
	
	//发货状态
	public static final int SEND_STATUS_OPEN = 10;	//发货单打开
	public static final int SEND_STATUS_ACTIVE = 20; //发货单生效
	public static final int SEND_STATUS_PARTPICK = 90; //发货单部分拣货
	public static final int SEND_STATUS_ALLPICK = 91;//发货单全部拣货
	public static final int SEND_STATUS_REVIEW = 92;//发货单复核
	public static final int SEND_STATUS_PACKAGE = 93;//发货单打包
	public static final int SEND_STATUS_CHECKWEIGHT = 94;//发货单称重复核
//	public static final int SEND_STATUS_PARTSHIP = 94;//发货单部分发运
	public static final int SEND_STATUS_CHECK = 95;//发货单查验
	public static final int SEND_STATUS_ALLSHIP = 96; //发货单全部发运
	public static final int SEND_STATUS_DESTORY = 97;//发货单销毁
	public static final int SEND_STATUS_RETURN = 98; //发货单退货
	public static final int SEND_STATUS_CANCAL = 99;//发货单取消

	//拣货单状态
	public static final int PICK_STATUS_OPEN = 10;//拣货单打开
	public static final int PICK_STATUS_ACTIVE = 20;//拣货单生效
	public static final int PICK_STATUS_WORKING = 30;//拣货单作业中
	public static final int PICK_STATUS_FINISH = 40;//拣货单作业完成
	public static final int PICK_STATUS_CANCAL = 99;//拣货单取消

	//波次单状态
	public static final int WAVE_STATUS_OPEN = 10;//波次单打开
	public static final int WAVE_STATUS_ACTIVE = 20;//波次单生效	
	public static final int WAVE_STATUS_CANCAL = 99;//波次单取消

	//发货单单据类型
	public static final int DELIVERY_TYPE_NORMAL_OUT = 200;//普通出库
	public static final int DELIVERY_TYPE_SWITCH_OUT = 210;//转仓出库
	public static final int DELIVERY_TYPE_ORTHER_OUT = 220;//其他出库
	public static final int DELIVERY_TYPE_TAKE_OUT = 230;//领用出库
	public static final int DELIVERY_TYPE_SAMPLE_OUT = 240;//样品出库
	public static final int DELIVERY_TYPE_TRANSFER_OUT = 250;//调拨出库
	public static final int DELIVERY_TYPE_BURST_OUT = 260;//爆款出库

	//拣货单单据类型
	public static final int PICKTYPE_FORM_DELIVERY = 230;// 发货单拣货
	public static final int PICKTYPE_FORM_WAVE = 240;// 波次拣货单
	public static final int PICKTYPE_FORM_HANDLE = 250;// 加工拣货单
	
	public static final int PICK_TYPE_PLAN = 1;//拣货类型计划
	public static final int PICK_TYPE_REAL = 2;//拣货类型实际
	
	//移位单状态
	public static final int SHIFT_STATUS_OPEN = 10;//移位单打开
	public static final int SHIFT_STATUS_ACTIVE = 20;//移位单生效
	public static final int SHIFT_STATUS_WORKING = 30;//移位单作业中
	public static final int SHIFT_STATUS_FINISH = 40;//移位单作业完成
	public static final int SHIFT_STATUS_CANCEL = 99;//移位单作业完成

	//移位类型
	public static final int SHIFT_TYPE_INWAREHOUSE = 10;//库内移位
	public static final int SHIFT_TYPE_REPLENISHMENT = 20;//补货移位
	public static final int SHIFT_TYPE_PICK= 30;//拣货移位
	public static final int SHIFT_TYPE_REJECT = 40;//退货移位
	public static final int SHIFT_TYPE_REJECT_BACK = 41;//退货上架
	public static final int SHIFT_TYPE_IN_BAD_AREA = 50;//不良品移入
	public static final int SHIFT_TYPE_OUT_BAD_AREA = 60;//不良品移出
	
	//库存冻结状态
	public static final int STOCK_BLOCK_FALSE =0;
	public static final int STOCK_BLOCK_TRUE =1;
	
	//库存货品状态
	public static final int STOCK_SKU_STATUS_NORMAL =10;//正常
	public static final int STOCK_SKU_STATUS_ABNORMAL =20;//不合格
	public static final int STOCK_SKU_STATUS_TO_BE_CHECK =30;//待检
	
	//库存日志操作类型
	public static final int STOCK_LOG_OP_TYPE_IN =1;//入库
	public static final int STOCK_LOG_OP_TYPE_OUT =2;//出库
	
	//库存日志类型
	public static final int STOCK_LOG_TYPE_RECEIPT =1;//收货
	public static final int STOCK_LOG_TYPE_PUTAWAY =2;//上架
	public static final int STOCK_LOG_TYPE_PICKING =3;//拣货
	public static final int STOCK_LOG_TYPE_SHIFT =4;//移位
	public static final int STOCK_LOG_TYPE_CHANGE =5;//库存调整
	public static final int STOCK_LOG_TYPE_ADJUST =6;//盈亏调整
	public static final int STOCK_LOG_TYPE_IMPORT = 7;//导入
	public static final int STOCK_LOG_TYPE_DESTORY = 8;//销毁
	public static final int STOCK_LOG_TYPE_SEND = 9;//发货
	public static final int STOCK_LOG_TYPE_BAD_DESTORY =10;//不良品销毁
	
	
	//异常状态
	public static final int EXCEPTION_STATUS_TO_BE_HANDLED = 10;//待处理
	public static final int EXCEPTION_STATUS_HANDLED = 20;//已处理
	public static final int EXCEPTION_STATUS_CLOSED = 99;//已关闭

	//异常类型
	public static final int EXCEPTION_TYPE_NOT_CONFORM = 410;//计划与执行不符
	public static final int EXCEPTION_TYPE_STOCK_SIZE = 420;//库位低储高储报警
	public static final int EXCEPTION_TYPE_SAFE_STOCK = 430;//安全库存报警
	public static final int EXCEPTION_TYPE_REC_ABNORMAL = 440;//收货异常
	public static final int EXCEPTION_TYPE_PUT_ABNORMAL = 450;//上架异常
	public static final int EXCEPTION_TYPE_SEND_ABNORMAL = 460;//发货异常
	public static final int EXCEPTION_TYPE_COUNT_ABNORMAL = 470;//盘点异常
	public static final int EXCEPTION_TYPE_SHIFT_ABNORMAL = 480;//移位异常
	public static final int EXCEPTION_TYPE_OTHER_ABNORMAL = 490;//其他异常
	
	//异常级别
	public static final int EXCEPTION_LEVEL_NORMAL = 510;//一般
	public static final int EXCEPTION_LEVEL_IMPORTENT = 520;//重要
	public static final int EXCEPTION_LEVEL_SLIGHT = 530;//轻微
	public static final int EXCEPTION_LEVEL_WARN = 540;//险兆

	//异常级别
	public static final int EXCEPTION_DATE_FROM_AUTO = 10;//自动生成
	public static final int EXCEPTION_DATE_FROM_MANUAL = 20;//手动创建
	
	//租金收取方式
	public static final int PARK_RENT_STYLE_CASH = 460;//现金
	public static final int PARK_RENT_STYLE_TRANSFER = 470;//转账
			
	//计费方式
	public static final int PARK_FEE_STYLE_FLOW = 500;//按流量
	public static final int PARK_FEE_STYLE_DAY = 510;//按天
	public static final int PRAK_FEE_STYLE_MONTH =520;//按月
	
	//结算周期
	public static final int PARK_SETTLE_CYCLE_MONTH = 530;//按月
	public static final int PARK_SETTLE_CYCLE_SEASON = 540;//按季度
	public static final int PARK_SETTLE_CYCLE_YEAR = 550;//按年
	
	//预警频率
	public static final int WARN_FREQUENCY_EVERYDAY = 560;//每天
	public static final int WARN_FREQUENCY_EVERYWEEK = 570;//每周
	public static final int WARN_FREQUENCY_EVERYMONTH = 580;//每月
	
	//预警方式
	public static final int WARN_STYLE_SYSTEM = 600;//系统
	public static final int WARN_STYLE_SHORTMESSAGE = 610;//短信
	public static final int WARN_STYLE_EMAIL = 620;//邮件
	
	//仓库出租状态
	public static final int PARK_RENT_STATUS_OPEN = 10;//打开
	public static final int PARK_RENT_STATUS_ACTIVE = 20;//生效
	
	//租期预警状态
	public static final int RENT_WARN_STATUS_OPEN = 10;//打开
	public static final int RENT_WARN_STATUS_CLOSE = 98;//关闭

	//调整单来源
	public static final int ADJUST_DATE_FROM_MANUAL = 1;//手动
	public static final int ADJUST_DATE_FROM_COUNT = 2;//盘点
	public static final int ADJUST_DATE_FROM_CHANGE = 3;//库存调整
	public static final int ADJUST_DATE_FROM_BAD_MANUAL = 4;//不良品手动调整
	public static final int ADJUST_DATE_FROM_BAD_COUNT = 5;//不良品盘点
	public static final int ADJUST_DATE_FROM_BAD_CHANGE = 6;//不良品库存调整
	
	//调整单类型
	public static final int ADJUST_TYPE_IN = 10;//盘盈
	public static final int ADJUST_TYPE_OUT = 20;//盘亏
	public static final int ADJUST_TYPE_OTHER = 99;//其他
	
	//盘点单类型
	public static final int COUNT_TYPE_ALL = 1;//全盘
	public static final int COUNT_TYPE_SKU = 2;//货品盘点
	public static final int COUNT_TYPE_LOCATION = 3;//库位普通盘点
	public static final int COUNT_TYPE_CHANGE = 4;//库位动碰盘点
	public static final int COUNT_TYPE_BAD = 5;//不良品盘点
	
	//盘点结果
	public static final Integer COUNT_RESULT_NORMAL = 0;//正常
	public static final Integer COUNT_RESULT_ABNORMAL = 1;//异常
	
	//盘点单是否锁定库位
	public static final int COUNT_FREEZE = 1;//锁定
	public static final int COUNT_NO_FREEZE = 0;//不锁定

	//辅材日志类型（辅材来源）
	public static final int MATERIAL_LOG_TYPE_REPLENISHMENT = 1;//补货
	public static final int MATERIAL_LOG_TYPE_PACKAGE = 2;//打包
	public static final int MATERIAL_LOG_TYPE_COUNT = 3;//盘点
	public static final int MATERIAL_LOG_TYPE_REJECT = 4;//退货拆包
	public static final int MATERIAL_LOG_TYPE_OTHER = 5;//其他
	
	//车辆管理
	public static final int VEHICLE_STATUS_OPEN = 10;//车辆打开
	public static final int VEHICLE_STATUS_ACTIVE = 20;//车辆生效
	public static final int VEHICLE_STATUS_CANCAL = 99;//车辆取消
	
	//申报表管理
	public static final int APPLICATIONFORM_STATUS_OPEN = 10;//申报表打开
	public static final int APPLICATIONFORM_STATUS_ACTIVE = 20;//申报表生效
	public static final int APPLICATIONFORM_STATUS_CANCAL = 99;//申报表取消
	
	//申请单管理
	public static final int APPLICATION_STATUS_OPEN = 10;//申请单打开
	public static final int APPLICATION_STATUS_ACTIVE = 20;//申请单生效
	public static final int APPLICATION_STATUS_PART_EXMINEING = 35;//申请单部分核放中
	public static final int APPLICATION_STATUS_PART_EXMINE = 36;//申请单部分核放
	public static final int APPLICATION_STATUS_ALL_EXMINEING = 37;//申请单全部核放中
	public static final int APPLICATION_STATUS_ALL_EXMINE = 38;//申请单全部核放
	public static final int APPLICATION_STATUS_CANCAL = 99;//申请单取消
	
	//申请单审批结果
	public static final String APPLICATION_STATUS_NO_SEND = "TMP00";//申请单待发送
	public static final String APPLICATION_STATUS_SEND_SUCCESS = "TMP30";//申请单发送成功
	public static final String APPLICATION_STATUS_SEND_FAIL = "TMP31";//申请单发送失败
	public static final String APPLICATION_STATUS_CHECK_SUCCESS = "TMP32";//申请单检验通过
	public static final String APPLICATION_STATUS_CHECK_FAIL = "TMP33";//申请单校验失败
	public static final String APPLICATION_STATUS_PASS = "TMP100990";//申请单放行
	public static final String APPLICATION_STATUS_CHARGEBACK = "TMP100105";//退单
	public static final String APPLICATION_STATUS_TO_CHECK = "TMP100550";//申请单转查验
	public static final String APPLICATION_STATUS_HAS_AUDIT = "TMP100999";//申请单已审核
	
	//核放单状态
	public static final int EXAMINE_STATUS_OPEN = 10;//核放单打开
	public static final int EXAMINE_STATUS_ACTIVE = 20;//核放单生效
	public static final int EXAMINE_STATUS_CANCAL = 99;//核放单取消
	
	//核放单审批结果
	public static final String EXAMINE_STATUS_NO_SEND = "GAT00";//核放单待发送
	public static final String EXAMINE_STATUS_SEND_SUCCESS = "GAT30";//核放单发送成功
	public static final String EXAMINE_STATUS_SEND_FAIL = "GAT31";//核放单发送失败
	public static final String EXAMINE_STATUS_CHECK_SUCCESS = "GAT32";//核放单校验通过
	public static final String EXAMINE_STATUS_CHECK_FAIL = "GAT34";//核放单校验失败
	public static final String EXAMINE_STATUS_AUDIT_SUCCESS = "GAT100990";//核放单审批成功
	public static final String EXAMINE_STATUS_CHARGEBACK = "GAT100105";//核放单退单
	
	/**打开状态*/
	public static final int STATUS_OPEN = 10;
	/**生效状态*/
	public static final int STATUS_ACTIVE = 20;
	/**作业中*/
	public static final Integer STATUS_WORKING = 30;
	/**作业完成*/
	public static final Integer STATUS_FINISH = 40;
	/**取消状态*/
	public static final int STATUS_CANCEL = 99;

	/**平台管理员角色编号*/
	public static final String ROLE_PLATFORM = "superadmin";
	/**企业管理员角色编号*/
	public static final String ROLE_ORG = "admin";
	/**园区管理员角色编号*/
	public static final String ROLE_PARK = "yardadmin";

	/**当前登录用户结果*/
	public static final String LOGIN_RESULT = "login.result";
	
	/**企业类型：园区*/
	public static final String ORGTYPE_PARK = "0";
	/**企业类型：企业*/
	public static final String ORGTYPE_ORG = "1";

	/**任务打开状态*/
	public static final Integer TASK_STATUS_OPEN = 10;
	/**任务执行状态*/
	public static final Integer TASK_STATUS_EXEC = 20;
	/**任务完成状态*/
	public static final Integer TASK_STATUS_FINISH = 40;
	/**任务取消状态*/
	public static final Integer TASK_STATUS_CANCEL = 99;

	/**收货任务*/
	public static final Integer TASK_TYPE_ASN = 1;
	/**上架任务*/
	public static final Integer TASK_TYPE_PUTAWAY = 2;
	/**拣货任务*/
	public static final Integer TASK_TYPE_PICK = 3;
	/**盘点任务*/
	public static final Integer TASK_TYPE_COUNT = 4;
	/**移位任务*/
	public static final Integer TASK_TYPE_SHIFT = 5;

	/**面单打开状态*/
	public static final Integer EXPRESS_BILL_STATUS_OPEN = 10;
	/**面单生效状态*/
	public static final Integer EXPRESS_BILL_STATUS_ENABLE = 20;
	/**面单取消 状态*/
	public static final Integer EXPRESS_BILL_STATUS_CANCEL = 99;

	public static final String 	ERP = "ERP";

	/**LOGIN_WEB:登录方式-电脑端**/
	public static final String LOGIN_WEB = "1";
	/**LOGIN_PHONE:登录方式-手持终端**/
	public static final String LOGIN_PHONE = "2";
	
	/**ERP推送运单号接口*/
	public static String ERP_UPDATELOGISTICS = "ERP_UPDATELOGISTICS";//推送运单号
	
	/**ERP商品信息更新接口*/
	public static String ERP_UPDATEGOODS = "ERP_UPDATEGOODS";//商品信息更新
	
	/**ERP入库清单更新接口*/
	public static String ERP_UPDATEINVENTORY = "ERP_UPDATEINVENTORY";//商品信息更新
	
	/**同步erp状态 0 未同步*/
	public static Integer ASN_UNSYNC_STATUS = 0;
	
	/**同步erp状态 1 已同步*/
	public static Integer ASN_HASSYNC_STATUS = 1;
	
	public static String SYNCSTOCK_STATUS_UNSEND = "0"; //未发送
	
	public static String SYNCSTOCK_STATUS_SEND_FAIL = "4"; //发送失败
	
	public static String SYNCSTOCK_STATUS_SEND_SUCCESS = "10"; //发送成功
	
	public static String FUNCTION_TYPE_COUNT = "CLASS_MANAGE_COUNT";//分类监管实盘
	
	public static String FUNCTION_TYPE_ASN = "CLASS_MANAGE_ASN";//分类监管入库
	
	public static String FUNCTION_TYPE_SEND = "CLASS_MANAGE_SEND";//分类监管发货
	
	public static String FUNCTION_TYPE_APPLY = "APPLY";//申请单申报
	
	public static String FUNCTION_TYPE_EXAMINE = "EXAMINE";//核放单申报
	
	public static String SYSTEM_TYPE_WMS = "WMS";
	
	public static String SYSTEM_CUST_ID = "CUST";
	
	public static String MESSAGE_TYPE_HCHX_DATA = "HCHX_DATA";//出入库，盘点回执
	
	public static String MESSAGE_TYPE_HBT1001 = "HBT1001";//HBT1001回执报文
	
	public static String MESSAGE_TYPE_TMP1001 = "TMP1001";//申请单审批回执
	
	public static String MESSAGE_TYPE_GAT1001 = "GAT1001";//核放单审批回执
	
	public static String BIZTYPE_CODE_APPLICATION = "TMP"; //申请单类型代码
	
	public static String BIZTYPE_CODE_EXAMINE = "GAT";//核放单类型代码
	
	//进出标志
	public static String IN_FLAG = "I";//进
	
	public static String OUT_FLAG = "E";//出
	
	public static String APPLY_FROM_CLASS_MASSAGE = "CM";//分类监管
	
	public static String APPLY_FROM_DELIVER_GOODS = "DG";//分送集报
	
	//操作明细类型
	public static String DELIVERY_LOG_TYPE_ADD = "录入";//录入
	public static String DELIVERY_LOG_TYPE_UPDATE = "修改";//修改
	public static String DELIVERY_LOG_TYPE_ENABLE = "生效";//生效	
	public static String DELIVERY_LOG_TYPE_DISABLE = "失效";//失效	
	public static String DELIVERY_LOG_TYPE_CANCEL = "取消";//取消
	public static String DELIVERY_LOG_TYPE_RETURN = "退单";//退单
	public static String DELIVERY_LOG_TYPE_DESTROY = "销毁";//销毁
	public static String DELIVERY_LOG_TYPE_CHECHING = "查验";//查验
	public static String DELIVERY_LOG_TYPE_PICK = "拣货";//拣货
	public static String DELIVERY_LOG_TYPE_CHECKPACKET = "打包复核";//打包
	public static String DELIVERY_LOG_TYPE_CHECKWEIGHT = "称重复核";//称重
	public static String DELIVERY_LOG_TYPE_SHIP = "发货出库";//发运
	
	//发送erp状态
	public static String SEND_ERP_STATUS_UNSEND = "1";//未发送
	public static String SEND_ERP_STATUS_SUCESS = "2";//发送成功
	public static String SEND_ERP_STATUS_FAIL = "4";//发送失败
	
	//打包状态
	public static int PACKAGE_HAS = 1;//已打包
	public static int PACKAGE_UN = 0;//未打包
	
	//扫描
	public static int SCAN_HAS = 1;//已扫描
	public static int SCAN_UN = 0;//未扫描
	
	public static String EXPRESS_SERVICE_EMS = "EMS";
	public static String EXPRESS_SERVICE_ST = "ST";
	public static String EXPRESS_SERVICE_SF = "SF";
	public static String EXPRESS_SERVICE_YTO = "YTO";
	public static String EXPRESS_SERVICE_YD = "YD";
	public static String EXPRESS_SERVICE_HYLG = "HYLG";
	public static String EXPRESS_SERVICE_OTHER = "OTHER";
	
	/**
	 * 推送物流公司成功
	 */
	public static Integer SEND_WULIU_SUCCESS=2;
	/**
	 * 推送物流公司且推送erp成功
	 */
	public static Integer SEND_WULIU_ERP_SUCCESS=3;
	/**
	 * 只需要推送erp成功
	 */
	public static Integer SEND_JUST_ERP_SUCCESS=4;
	
	/**
	 * 是否拦截
	 */
	public static String CANCEL_DELIVERY = "CD";//拦截
	public static String CANCEL_INTERCEPT = "CI";//取消拦截
	
	public static Integer INTERCEPT_HOLD_OFF = 10;//拦截
	public static Integer INTERCEPT_SUCCESS = 20;//拦截成功
	public static Integer INTERCEPT_TRAP = 30;//截留
	
	/**
	 * 
	 */
	public static Integer INTECEPT_RETURN = 1;
	public static Integer CONFIRM_RETURN = 0;
	
	public static final String SYS_WMS = "WMS";
	public static final String SYS_ERP = "ERP";
	public static final String SYS_ST = "ST";
	public static final String SYS_EMS = "EMS";
	public static final String SYS_ISS = "ISS";
	
	public static final String ISS01010 = "ISS01010";//查验线ISS01010报文
	
	
}