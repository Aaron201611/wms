package com.yunkouan.wms.common.shiro;

/**
* @Description: 登录（含用户，密码，验证码等）令牌类
* @author tphe06
* @date 2017年3月10日
*/
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {
	private static final long serialVersionUID = -7593630445121174421L;

	/**@Fields 验证码 */
	private String validateCode;
	/**@Fields 企业名称 */
	private String orgId;
	/**@Fields 登录类型【对应权限级别：1平台管理员，2企业管理员，3企业普通用户】 */
	private Boolean loginType;
	/**from:1电脑端；2手机端；其它未知**/
	private String from;

	public String getValidateCode() {
		return validateCode;
	}
	public String getOrgId() {
		return orgId;
	}
	public Boolean getLoginType() {
		return loginType;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setLoginType(Boolean loginType) {
		this.loginType = loginType;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
}