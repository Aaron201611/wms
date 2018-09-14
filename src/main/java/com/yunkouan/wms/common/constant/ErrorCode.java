package com.yunkouan.wms.common.constant;

/**
 * @author tphe06 2017年2月13日
 */
public class ErrorCode {
	/**超时或者无权限*/
	public static final String TIMEOUT_OR_NOPOWER = "valid_common_timeout";
	/**
	 * 编号不唯一
	 */
	public static final String NO_NOT_UNIQUE = "valid_common_no_not_unique";
	/**
	 * 无操作权限
	 */
	public static final String NO_POWER = "valid_common_no_power";
	/**
	 * 未知异常
	 */
	public static final String UNKNOW_ERROR = "valid_common_unknow_error";
	/**
	 * 旧密码不正确
	 */
	public static final String PWD_NOT_EQUAL = "valid_main_pwd_not_equal";
	/**
	 * 非法数据
	 */
	public static final String DATA_NO_EXISTS = "valid_common_data_no_exists";
	/**
	 * 数据不允许全空
	 */
	public static final String DATA_EMPTY = "valid_common_data_empty";
	/**
	 * 主键不允许为空
	 */
	public static final String KEY_EMPTY = "valid_common_key_empty";
	/**
	 * 数据库异常
	 */
	public static final String DB_EXCEPTION = "valid_common_db_exception";
	/**
	 * 状态不合法
	 */
	public static final String STATUS_NO_RIGHT = "valid_common_status_no_right";

}