package com.yunkouan.wms.modules.sys.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.saas.modules.sys.entity.SysAdmin;
import com.yunkouan.saas.modules.sys.entity.SysAdminRole;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.saas.modules.sys.service.IAdminService;
import com.yunkouan.saas.modules.sys.service.IAuthService;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.saas.modules.sys.vo.AdminVo;
import com.yunkouan.saas.modules.sys.vo.AuthVo;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.wms.modules.sys.service.IAccountService;
import com.yunkouan.wms.modules.sys.vo.AccountVo;
import com.yunkouan.wms.modules.sys.vo.MenuVo;
import com.yunkouan.wms.modules.sys.vo.RoleWarehouse;
import com.yunkouan.wms.modules.ts.service.ITaskService;

@Controller

@RequestMapping("${adminPath}/index")
public class IndexController extends BaseController {
	protected static Log log = LogFactory.getLog(IndexController.class);

	/**@Fields 权限服务接口**/
	@Autowired
	private IAuthService service;
	/**@Fields 企业普通用户帐号服务接口**/
	@Autowired
	private IAccountService accountService;
	/**@Fields 企业管理员帐号服务接口 */
	@Autowired
	private IAdminService adminService;
	/**@Fields 仓库服务接口 */
	@Autowired
	private IWarehouseExtlService whService;
	@Autowired
	private ITaskService taskService;

	@Autowired
	private SessionDAO sessionDAO;
	public void online() {
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for(Session session : sessions){
			System.out.println("登录ip:"+session.getHost());
			System.out.println("会话id:"+session.getId());
			SimplePrincipalCollection c = (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if(c != null) {
				Principal p = (Principal)c.getPrimaryPrincipal();
				if(p != null) System.out.println("登录帐号:"+p);
			}
			System.out.println("最后操作日期:"+session.getLastAccessTime());
		}
	}

	/**
	 * 加载菜单数据（含一二级）
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/loadMenu", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel loadMenu(@RequestBody MenuVo v, HttpServletRequest req) {
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			Principal p = LoginUtil.getLoginUser();
			SysAuth entity = new SysAuth()
			.setAuthStatus(Constant.STATUS_ACTIVE)
//			.setParentId(v.getParentId())
			.setAuthLevel(p.getAuthLevel());
			AuthVo vo = new AuthVo(entity);
			vo.setAccountId(p.getAccountId());
			vo.setOrgId(p.getOrgId())
			.setWarehouseId(LoginUtil.getWareHouseId());
			List<SysAuth> list = null;
			if(p.getAuthLevel() == SysAuth.AUTH_LEVEL_ORG_ADMIN) {
				list = service.query4admin(vo);
			} else {
				list = service.query(vo);
			}
			List<AuthVo> result = new ArrayList<AuthVo>();
			if(list != null) for(int i=0; i<list.size(); ++i) {
				//查询二级权限
				result.add(new AuthVo(list.get(i)).clearAllNull());
			}
			ResultModel r = new ResultModel().setList(result);
			String json = StringUtil.toJson(r);
			if(log.isDebugEnabled()) log.debug("===================="+json);
			return r;
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
	}

	/**
	 * 查询当前登录用户信息
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/loadUser", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel loadUser(HttpServletRequest req) {
		ResultModel r = new ResultModel();
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			Principal p = LoginUtil.getLoginUser();
			p.setWarehouseId(LoginUtil.getWareHouseId());
			if(Constant.LOGIN_PHONE.equals(p.getFrom())) {
				//自动分配作业人员
				taskService.autoAssignAll();
			}
			/** 如果是企业管理员，需要把AdminVo转换成AccountVo **/
			if(p.getAuthLevel() == SysAuth.AUTH_LEVEL_ORG_ADMIN) {
				AdminVo vo = adminService.load(p.getAccountId());
				SysAccount entity = new SysAccount();
				entity.setAccountNo(vo.getEntity().getAdminNo());
				entity.setAccountName(vo.getEntity().getAdminName());
				entity.setNote(vo.getEntity().getNote());
				AccountVo v = new AccountVo(entity);
				SysUser u = new SysUser();
				BeanUtils.copyProperties(vo.getUser(), u);
				v.setUser(u);
				v.setOrg(vo.getOrg());
				v.setList(copy(vo.getList()));
				v.setLoginUser(p);
				r.setObj(v);
			} else {
				AccountVo v = accountService.load(p.getAccountId());
				v.setWarehouse(whService.findWareHouseById(LoginUtil.getWareHouseId()));
				v.setLoginUser(p);
				r.setObj(v);
			}
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
		return r;
	}

	/**
	* @Description: 对象数据复制
	* @param admins
	* @return List<SysRole>
	* @throws
	*/
	private static List<RoleWarehouse> copy(List<SysAdminRole> admins) {
		List<RoleWarehouse> list = new ArrayList<RoleWarehouse>();
		if(admins != null) for(int i=0; i<admins.size(); ++i) {
			SysAdminRole a = admins.get(i);
			RoleWarehouse r = new RoleWarehouse();
			BeanUtils.copyProperties(a, r);
			list.add(r);
		}
		return list;
	}

	/**
	 * 修改当前登录用户信息
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@OpLog(model = "个人中心", type = OpLog.OP_TYPE_UPDATE, pos = 0)
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel updateUser(@Validated(value = { ValidSearch.class }) @RequestBody AccountVo vo, BindingResult br) {
		ResultModel r = new ResultModel();
		try {
			if ( br.hasErrors() ) {
				return super.handleValid(br);
			}
    		//当前登录用户信息
    		Principal p = LoginUtil.getLoginUser();
    		com.yunkouan.saas.common.vo.Principal p1 = new com.yunkouan.saas.common.vo.Principal();
    		BeanUtils.copyProperties(p, p1);
			/**如果是企业管理员，需要做参数转换**/
			if(p.getAuthLevel() == SysAuth.AUTH_LEVEL_ORG_ADMIN) {
				SysAdmin entity = new SysAdmin();
				if(vo.getEntity() != null) {
					entity.setLoginPwd(vo.getEntity().getAccountPwd());
					entity.setNote(vo.getEntity().getNote());
				}
				AdminVo v = new AdminVo(entity);
				v.setOldPwd(vo.getOldPwd());
				com.yunkouan.saas.modules.sys.entity.SysUser u = new com.yunkouan.saas.modules.sys.entity.SysUser();
				BeanUtils.copyProperties(vo.getUser(), u);
				v.setUser(u);
				adminService.updatePwd(v, p1);
			} else {
				accountService.updatePwd(vo);
			}
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			throw new BizException(e.getMessage());
		}
		return r;
	}
}