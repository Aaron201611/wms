package com.yunkouan.wms.common.aop.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AOP日志记录，自定义注解
 * @author tphe06
 */
@Target({ElementType.METHOD, ElementType.PARAMETER}) //表示此注解可以使用在参数（方法：ElementType.PARAMETER）上
@Retention(RetentionPolicy.RUNTIME) //表示可以在代码运行时被发现
@Documented
public @interface OpLog {
	/**MODEL_SYSTEM_CONFIG:模块标识-系统管理-角色管理**/
	public static final String MODEL_SYSTEM_ROLE = "角色管理";
	/**MODEL_SYSTEM_ORG_ACCOUNT:模块标识-系统管理-企业帐号管理**/
	public static final String MODEL_SYSTEM_ORG_ACCOUNT = "企业帐户管理";
	/**MODEL_SYSTEM_WAREHOUSE:模块标识-系统管理-仓库管理**/
	public static final String MODEL_SYSTEM_WAREHOUSE = "仓库管理";
	/**MODEL_SYSTEM_WAREHOUSE_LOG:模块标识-系统管理-库存日志**/
	public static final String MODEL_SYSTEM_WAREHOUSE_LOG = "库存日志";

	/**MODEL_SYSTEM_ORG:模块标识-平台管理-企业管理**/
	public static final String MODEL_SYSTEM_ORG = "企业管理";
	/**MODEL_SYSTEM_PLATFORM:模块标识-平台管理-平台帐户管理**/
	public static final String MODEL_SYSTEM_PLATFORM_ACCOUNT = "平台帐户管理";
	/**MODEL_SYSTEM_RIGHT:模块标识-平台管理-权限管理**/
	public static final String MODEL_SYSTEM_RIGHT = "权限管理";

	/**MODEL_META_SKU_TYPE:模块标识-基础数据-货品类型**/
	public static final String MODEL_META_SKU_TYPE = "货品类型";
	/**MODEL_META_SKU:模块标识-基础数据-货品管理**/
	public static final String MODEL_META_SKU = "货品管理";
	/**MODEL_META_SKU:模块标识-基础数据-货品辅材BOM管理**/
	public static final String MODEL_META_SKU_MATERIAL_BOM = "货品辅材BOM管理";

	/**MODEL_META_MERCHANTS:模块标识-基础数据-客商管理**/
	public static final String MODEL_META_MERCHANTS = "客商管理";
	/**MODEL_META_DOCK:模块标识-基础数据-月台管理**/
	public static final String MODEL_META_DOCK = "月台管理";
	/**MODEL_META_PACK:模块标识-基础数据-包装管理**/
	public static final String MODEL_META_PACK = "包装管理";
	/**MODEL_META_AREA:模块标识-基础数据-库区管理**/
	public static final String MODEL_META_AREA = "库区管理";
	/**MODEL_META_LOCATION_SPEC:模块标识-基础数据-库位规格**/
	public static final String MODEL_META_LOCATION_SPEC = "库位规格";
	/**MODEL_META_LOCATION:模块标识-基础数据-库位管理**/
	public static final String MODEL_META_LOCATION = "库位管理";

	/**MODEL_WAREHOUSE_ASN:模块标识-仓储管理-收货管理**/
	public static final String MODEL_WAREHOUSE_ASN = "收货管理";
	/**MODEL_WAREHOUSE_PUTAWAY:模块标识-仓储管理-上架管理**/
	public static final String MODEL_WAREHOUSE_PUTAWAY = "上架管理";
	/**MODEL_WAREHOUSE_DELIVERY:模块标识-仓储管理-发货管理**/
	public static final String MODEL_WAREHOUSE_DELIVERY = "发货管理";
	/**MODEL_WAREHOUSE_PICK:模块标识-仓储管理-拣货管理**/
	public static final String MODEL_WAREHOUSE_PICK = "拣货管理";
	/**MODEL_WAREHOUSE_WAVE:模块标识-仓储管理-波次管理**/
	public static final String MODEL_WAREHOUSE_WAVE = "波次管理";
	/**MODEL_WAREHOUSE_STOCK:模块标识-仓储管理-库存管理**/
	public static final String MODEL_WAREHOUSE_STOCK = "库存管理";
	/**MODEL_WAREHOUSE_COUNT:模块标识-仓储管理-盘点管理**/
	public static final String MODEL_WAREHOUSE_COUNT = "盘点管理";
	/**MODEL_WAREHOUSE_SHIFT:模块标识-仓储管理-移位管理**/
	public static final String MODEL_WAREHOUSE_SHIFT = "移位管理";
	/**MODEL_WAREHOUSE_ADJUST:模块标识-仓储管理-盈亏调整**/
	public static final String MODEL_WAREHOUSE_ADJUST = "盈亏调整";
	/**MODEL_WAREHOUSE_ADJUST:模块标识-仓储管理-不良品销毁**/
	public static final String MODEL_WAREHOUSE_REJECTS = "不良品销毁";

	/**MODEL_MONITOR_EXCEPTION:模块标识-监控中心-异常管理**/
	public static final String MODEL_MONITOR_EXCEPTION = "异常管理";
	/**MODEL_MONITOR_WARN:模块标识-监控中心-库存预警**/
	public static final String MODEL_MONITOR_WARN = "库存预警";

	/**MODEL_PARK_RENT:模块标识-园区管理-出租管理**/
	public static final String MODEL_PARK_RENT = "出租管理";
	/**MODEL_PARK_RENTWARN:模块标识-园区管理-租期预警**/
	public static final String MODEL_PARK_RENTWARN = "租期预警";
	/**MODEL_PARK_STATIC:模块标识-园区管理-业务统计**/
	public static final String MODEL_PARK_STATIC = "业务统计";

	/**MODEL_EXT_INTERFACE:模块标识-外部接口**/
	public static final String MODEL_EXT_INTERFACE = "外部接口";
	/**
	 * 日志标识，默认所处最小级别模块标识
	 * @return
	 */
	public String model() default "";


	/**OP_TYPE_LOGIN:操作类型-登录**/
	public static final String OP_TYPE_LOGIN = "登录";
	/**OP_TYPE_ADD:操作类型-添加**/
	public static final String OP_TYPE_ADD = "新增";
	/**OP_TYPE_UPDATE:操作类型-修改**/
	public static final String OP_TYPE_UPDATE = "修改";
	/**OP_TYPE_DELETE:操作类型-删除**/
	public static final String OP_TYPE_DELETE = "删除";
	/**OP_TYPE_QUERY:操作类型-查询**/
	public static final String OP_TYPE_QUERY = "查询";
	/**OP_TYPE_LOGOUT:操作类型-退出**/
	public static final String OP_TYPE_LOGOUT = "退出";
	/**OP_TYPE_ENABLE:操作类型-生效**/
	public static final String OP_TYPE_ENABLE = "生效";
	/**OP_TYPE_DISABLE:操作类型-失效**/
	public static final String OP_TYPE_DISABLE = "失效";
	/**OP_TYPE_CANCEL:操作类型-取消**/
	public static final String OP_TYPE_CANCEL = "取消";

	/**
	 * 操作类型，对应界面上的按钮
	 * @return
	 */
	public String type() default "查询";


	/**
	 * 保存的参数位置，如果入参有多个，只保存一个参数的时候用
	 * @return
	 */
	public int pos() default -1;
}