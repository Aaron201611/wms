package com.yunkouan.wms.common.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysAdmin;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.saas.modules.sys.entity.SysOrg;
import com.yunkouan.saas.modules.sys.service.IAdminService;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.saas.modules.sys.vo.AdminAuthVo;
import com.yunkouan.saas.modules.sys.vo.AdminVo;
import com.yunkouan.util.UserUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.wms.modules.sys.service.IAccountService;
import com.yunkouan.wms.modules.sys.vo.AccountVo;

/**
* @Description: 系统安全认证实现类
* @author tphe06
* @date 2017年3月10日
*/
@Service
@Component
public class SystemAuthorizingRealm extends AuthorizingRealm {
	protected static Log log = LogFactory.getLog(SystemAuthorizingRealm.class);

	/**@Fields 【企业普通用户帐号】服务接口 */
	@Autowired
	private IAccountService accountService;
	/**@Fields 【组织】服务接口 */
	@Autowired
	private IOrgService orgService;
	/**@Fields 【企业管理员帐号】服务接口 */
	@Autowired
	private IAdminService adminService;
	@Autowired
	private SessionDAO sessionDAO;

	/**
	* @Description: 认证回调函数【验证用户合法性/校验登录帐号密码是否正确】
	* @param authcToken
	* @return
	* @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		if(log.isInfoEnabled()) log.info("进入认证回调函数");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		// 登录参数值合法性校验
		validParam(token);
		// 校验组织
		SysOrg org = orgService.query(new SysOrg().setOrgNo(token.getOrgId()));
		if(org == null) throw new AuthenticationException("valid_login_orgid_not_exists");
		if(Constant.STATUS_ACTIVE != org.getOrgStatus()) throw new AuthenticationException("valid_org_orgStatus_not_valid");
		// 校验帐号【根据登录类型到企业管理员帐号表或者企业普通用户帐号表做认证】
//		int type = token.getLoginType()?SysAuth.AUTH_LEVEL_ORG_ADMIN:SysAuth.AUTH_LEVEL_ORG_USER;
		AccountVo vo;
		try {
			vo = getAccount(token.getLoginType(), token.getUsername(), org.getOrgId(), org.getOrgType());
		} catch (DaoException | ServiceException e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new AuthenticationException("valid_login_admin_not_exists");
		}
		// 校验密码，密码为【帐号uuid+密码】明文做SHA1加密
		SysAccount account = vo.getEntity();
		SysUser user = vo.getUser();
		String pwd = new String(token.getPassword());
		String password =  UserUtil.entryptPassword(account.getAccountId().concat(pwd));
		if(!password.equals(account.getAccountPwd())) throw new AuthenticationException("valid_login_pwd_error");
		if(log.isInfoEnabled()) log.info(account.getAccountNo()+"登录成功");
		//将该用户在其它地方登录的帐号踢出登录
//		try {
//			kill(user.getUserId());
//		} catch(Exception e) {
//			e.printStackTrace();
//			if(log.isErrorEnabled()) log.error(e.getMessage());
//		}
		// 封装登录用户信息
		Principal principal = new Principal(user.getUserId(), token.getUsername(), user.getUserNo(), user.getUserName(),
			org.getOrgId(), user.getEmail(), user.getUserStatus(), user.getIsEmployee(), user.getPhone(), vo.getAuthLevel(), account.getAccountId(), token.getFrom());
		return new SimpleAuthenticationInfo(principal, pwd, null, getName());
	}
	/**
	 * 将该用户在其它地方登录的帐号踢出登录
	 * @param userId
	 */
	public void kill(String userId) {
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for(Session session : sessions) {
			SimplePrincipalCollection c = (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if(c == null) continue;
			Principal p = (Principal)c.getPrimaryPrincipal();
			if(p == null) continue;
			if(p.getUserId().equals(userId)) {
				session.setTimeout(0);
				Serializable id = session.getId();
				String host = StringUtils.trimToEmpty(session.getHost());
				String user = p.getUserNo();
				if(log.isErrorEnabled()) log.error("会话【"+id+"】被踢出登录，地址【"+host+"】，用户【"+user+"】");
			}
		}
	}
	/**
	 * @throws ServiceException 
	 * @throws DaoException 
	* @Description: 根据登录类型到企业管理员帐号表或者企业普通用户帐号表做认证
	* @param authLevel
	* @param accountNo
	* @param orgId
	* @return AccountVo
	* @throws
	*/
	private AccountVo getAccount(Boolean isAdmin, String accountNo, String orgId, String orgType) throws DaoException, ServiceException {
		if(isAdmin != null && isAdmin) {
			AdminVo vo = adminService.queryRole(new SysAdmin().setAdminNo(accountNo).setOrgId(orgId));
			if(vo == null || vo.getEntity() == null || StringUtils.isBlank(vo.getEntity().getAdminId())) throw new AuthenticationException("valid_login_admin_not_exists");
			if(vo.getList() == null || vo.getList().size() != 1
					|| Constant.ROLE_PLATFORM.equals(vo.getList().get(0).getRoleNo())) throw new AuthenticationException("valid_login_account_error_right");
			// 检查帐号状态
			if(Constant.STATUS_ACTIVE != vo.getEntity().getAdminStatus()) throw new AuthenticationException("valid_login_account_locked");
//			if(Constant.STATUS_ACTIVE != vo.getUser().getUserStatus()) throw new AuthenticationException("valid_login_account_locked");
			// 转化成普通帐号Vo
			SysAccount entity = new SysAccount();
			entity.setAccountId(vo.getEntity().getAdminId());
			entity.setAccountNo(accountNo);
			entity.setAccountPwd(vo.getEntity().getLoginPwd());
			AccountVo r = new AccountVo(entity);
			SysUser u = new SysUser();
			BeanUtils.copyProperties(vo.getUser(), u);
			r.setUser(u);
			if(Constant.ROLE_ORG.equals(vo.getList().get(0).getRoleNo())) r.setAuthLevel(SysAuth.AUTH_LEVEL_ORG_ADMIN);
			if(Constant.ROLE_PARK.equals(vo.getList().get(0).getRoleNo())) r.setAuthLevel(SysAuth.AUTH_LEVEL_ORG_ADMIN);
			return r;
		} else {
			AccountVo vo = accountService.query(new SysAccount().setAccountNo(accountNo).setOrgId(orgId));
			if(vo == null || vo.getEntity() == null || StringUtils.isBlank(vo.getEntity().getAccountId()) || vo.getUser() == null) throw new AuthenticationException("valid_login_account_not_exists");
			//检查授权角色
			if(vo.getList() == null || vo.getList().size() == 0) throw new AuthenticationException("valid_login_account_error_right");
			// 检查帐号状态
			if(Constant.STATUS_ACTIVE != vo.getEntity().getAccountStatus()) throw new AuthenticationException("valid_login_account_locked");
//			if(Constant.STATUS_ACTIVE != vo.getUser().getUserStatus()) throw new AuthenticationException("valid_login_account_locked");
			if(SysOrg.TYPE_PARK.equals(orgType)) return vo.setAuthLevel(SysAuth.AUTH_LEVEL_PARK_USER);
			return vo.setAuthLevel(SysAuth.AUTH_LEVEL_ORG_USER);
		}
	}
	/**
	* @Description: 登录参数值合法性校验
	* @param token
	* @return void
	* @throws
	*/
	private void validParam(UsernamePasswordToken token) {
		if(token == null) throw new AuthenticationException("valid_login_pwd_error");
//		if(token.getLoginType() != SysAuth.AUTH_LEVEL_ORG_ADMIN
//			&& token.getLoginType() != SysAuth.AUTH_LEVEL_ORG_USER) throw new AuthenticationException("valid_login_pwd_error");
		if(token.getLoginType() == null) token.setLoginType(false);
		if(StringUtils.isBlank(token.getOrgId())) throw new AuthenticationException("valid_login_orgid_not_empty");
		if(StringUtils.isBlank(token.getUsername())) throw new AuthenticationException("valid_login_account_not_empty");
		if(token.getPassword() == null || StringUtils.isBlank(new String(token.getPassword()))) throw new AuthenticationException("valid_login_pwd_not_empty");
	}

	/**
	* @Description: 授权回调函数（每个登录帐号都加载一份到内存）
	* @see【查询用户角色和权限/进行鉴权但缓存中无用户的授权信息时调用】
	* @see（WMS系统企业管理员限2级权限，企业普通用户限3级权限）
	* @see 经测试:本例中该方法的调用时机为需授权资源被访问时
	* @param principals
	* @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(log.isInfoEnabled()) log.info("进入授权回调函数");
//		Principal p = (Principal) getAvailablePrincipal(principals);
		Principal p = LoginUtil.getLoginUser();
		if(log.isInfoEnabled()) log.info(p + " 登录成功");

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		/**查询当前登录帐号所拥有的权限，注意区别企业管理员帐号和企业普通用户帐号**/
		List<SysAuth> auths = null;
		if(SysAuth.AUTH_LEVEL_ORG_ADMIN == p.getAuthLevel()) {
			AdminAuthVo vo = new AdminAuthVo();
			vo.setAdmin(new SysAdmin().setAdminId(p.getAccountId()));
			vo.setAuth(new SysAuth().setAuthStatus(Constant.STATUS_ACTIVE).setAuthLevel(p.getAuthLevel()));
			auths = adminService.query4all(vo);
		} else if(SysAuth.AUTH_LEVEL_PARK_USER == p.getAuthLevel()) {
			auths = accountService.query4park(p.getAccountId());
		} else {
			auths = accountService.query(p.getAccountId(), LoginUtil.getWareHouseId());
		}
		/**登录用户所拥有的所有权限编号**/
		List<String> list = new ArrayList<String>();
		if(auths != null) for(int i=0; i<auths.size(); ++i) {
			list.add(auths.get(i).getAuthNo());
		}
		info.addStringPermissions(list);
		if(log.isInfoEnabled()) log.info(info.getStringPermissions());
		return info;
	}

	/**
	* @Description: 当前登录用户信息
	* @author tphe06
	* @date 2017年3月10日
	*/
	public static class Principal implements Serializable {
		private static final long serialVersionUID = 6562144790746698769L;

		/**
		 * 用户主键：
		 * 【人员主键，整个系统唯一，如果只涉及到人的信息而不涉及到登录帐号，尽量使用这个字段】
		 */
		private String userId;
		/**用户编号**/
	    private String userNo;
	    /**用户姓名**/
	    private String userName;
	    /**
	     * 帐号主键：
	     * 【uuid，系统设计一个人可以对应多个帐号，但通常一个人只有一个帐号】
	     * 【注意：可能是企业管理员帐号也可以是企业普通用户帐号，两者存的表不一样】
	     */
	    private String accountId;
	    /**
	     * 登录帐号：
	     * 【登录时候输入的帐号】
	     * 【注意：可能是企业管理员帐号也可以是企业普通用户帐号，两者存的表不一样】
	     */
		private String loginName;
		/**组织id**/
		private String orgId;
		/**仓库id【登录时候设置，其它时候只能使用不得修改】**/
		private String warehouseId;
		/**权限级别【0---园区管理员，1---平台管理员，2---企业管理员，3---企业普通用户】**/
		private int authLevel;

		/**邮箱地址**/
	    private String email;
	    /**用户状态**/
	    private Integer userStatus;
	    /**是否内部员工**/
	    private Integer isEmployee;
	    /**联系电话**/
	    private String phone;
		/**from:1电脑端；2手机端；其它未知**/
		private String from;


	    public Principal(){}
	    public Principal(String userId, String orgId, String warehouseId){
	    	this.userId = userId;
	    	this.orgId = orgId;
	    	this.warehouseId = warehouseId;
	    }
	    public Principal(String userId, String loginName, String userNo, String userName, 
			String orgId, String email, Integer userStatus, 
			Integer isEmployee, String phone, int authLevel, String accountId, String from) {
			this.userId = userId;
			this.loginName = loginName;
			this.userNo = userNo;
			this.userName = userName;
			this.orgId = orgId;
			this.from = from;
			this.email = email;
			this.userStatus = userStatus;
			this.isEmployee = isEmployee;
			this.phone = phone;
			this.authLevel = authLevel;
			this.accountId= accountId;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}

		/**
		 * 用户id：
		 * 【人员主键，整个系统唯一，如果只涉及到人的信息而不涉及到登录帐号，尽量使用这个字段】
		 */
		public String getUserId() {
			return userId;
		}

		/**用户编号【个人编码/工号】*/
		public String getUserNo() {
			return userNo;
		}

		/**用户姓名*/
		public String getUserName() {
			return userName;
		}

		/**
		 * 当前登录帐号：
		 * 【登录时候输入的帐号，注意：可能是企业管理员帐号也可以是企业普通用户帐号，两者存的表不一样】
		 */
		public String getLoginName() {
			return loginName;
		}

		/**组织id*/
		public String getOrgId() {
			return orgId;
		}

		/**仓库id*/
		@Deprecated
		public String getWarehouseId() {
			return LoginUtil.getWareHouseId();
		}

		/**邮箱地址*/
		public String getEmail() {
			return email;
		}

		/**用户状态*/
		public Integer getUserStatus() {
			return userStatus;
		}

		/**是否员工*/
		public Integer getIsEmployee() {
			return isEmployee;
		}

		/**联系电话*/
		public String getPhone() {
			return phone;
		}

		/**设置当前登录用户操作的仓库id【登录时候用，其它时候不得调用】**/
		public void setWarehouseId(String warehouseId) {
			this.warehouseId = warehouseId;
		}

		public String getFrom() {
			return from;
		}
		/**
		 * 当前登录帐号id：
		 * 【uuid，系统设计一个人可以对应多个帐号，但通常一个人只有一个帐号】
		 * 【注意：可能是企业管理员帐号也可以是企业普通用户帐号，两者存的表不一样】
		 */
		public String getAccountId() {
			return accountId;
		}

		/**权限级别【1---平台管理员，2---企业管理员，3---企业普通用户】**/
		public int getAuthLevel() {
			return authLevel;
		}
	}
}