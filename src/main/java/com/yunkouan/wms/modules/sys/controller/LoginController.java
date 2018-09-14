package com.yunkouan.wms.modules.sys.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.dao.IWarehouseDao;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.util.JsonUtil;
import com.yunkouan.wms.common.aop.log.OpLog;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.shiro.ExtendFormAuthenticationFilter.Token;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.shiro.UsernamePasswordToken;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.sys.dao.IAccountRoleDao;
import com.yunkouan.wms.modules.sys.entity.SysAccountRole;
import com.yunkouan.wms.modules.sys.vo.LoginVo;

@Controller
@RequestMapping("${adminPath}/adminlogin")
public class LoginController extends BaseController {
	protected static Log log = LogFactory.getLog(LoginController.class);

	/**帐号角色授权数据层接口**/
	@Autowired
	private IAccountRoleDao accRoleDao;
	/**仓库数据层接口**/
	@Autowired
	private IWarehouseDao warehouseDao;
//	@Autowired
//	private IUserService userService;
//	@Autowired
//	private ITaskService taskService;

	/**
	 * 登录功能
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@OpLog(model = OpLog.OP_TYPE_LOGIN, type = OpLog.OP_TYPE_LOGIN, pos=0)
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel doLogin(@RequestBody UsernamePasswordToken token, HttpServletRequest req) {
		ResultModel r = new ResultModel();
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			// 处理工作量,记录用户所有的单量
//			try {
//				Principal p = LoginUtil.getLoginUser();
//				if(Constant.LOGIN_PHONE.equals(p.getFrom())) {
//					SysUser user = userService.get(p.getUserId());
//					dealJob(user);
//				}
//			} catch (DaoException | ServiceException e) {
//				e.printStackTrace();
//				if(log.isErrorEnabled()) log.error(e.getMessage());
//			}
			Token t = queryWareHourse();
			r.setObj(t);
			return r;
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}

	/**
	 * 每次登陆时，登录接口查询用户今天的日期是否与最后作业日期相同，
     * 不相同则单量=0，最后作业日期=今日日期。
	 * 作业完成后，单量+1
	 * @param user
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
//	private void dealJob(SysUser user) throws DaoException, ServiceException {
//		if(user.getLastJobTime() == null || user.getJobQuantity() == null) {
//			SysUser onlineUser = new SysUser();
//			onlineUser.setUserId(user.getUserId());
//			onlineUser.setJobQuantity(0);
//			onlineUser.setLastJobTime(new Date());
//			userService.update(onlineUser);
//			return;
//		}
//		String jobTime  = DateFormatUtils.format(user.getLastJobTime(), "yyyy-MM-dd");
//		String nowTime  = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
//		if(!nowTime.equals(jobTime)) {
//			SysUser onlineUser = new SysUser();
//			onlineUser.setUserId(user.getUserId());
//			onlineUser.setJobQuantity(0);
//			onlineUser.setLastJobTime(new Date());
//			userService.update(onlineUser);
//			return;
//		}
//	}
	/**查询当前登录帐号所拥有权限的仓库列表**/
	private Token queryWareHourse() {
		Principal p = LoginUtil.getLoginUser();
		if(log.isDebugEnabled()) log.debug("当前登录帐号：["+p.getLoginName()+"]");
		/**只有普通企业用户帐号才能设置仓库，企业管理员无需设置仓库直接登陆到首页**/
		if(p.getAuthLevel() != SysAuth.AUTH_LEVEL_ORG_USER) {
			/**向浏览器输出结果**/
	        Token r = new Token();
	        r.setLevel(p.getAuthLevel());
	        r.setStatus("1");
	        return r;
		}
		Map<String, MetaWarehouse> map = new HashMap<String, MetaWarehouse>();
        List<SysAccountRole> alist = accRoleDao.select(new SysAccountRole().setAccountId(p.getAccountId()).setOrgId(p.getOrgId()));
		if(alist != null) for(int i=0; i<alist.size(); ++i) {
			if(StringUtils.isEmpty(alist.get(i).getWarehouseId())) continue;
			/**根据仓库主键查询仓库信息**/
			MetaWarehouse param = new MetaWarehouse();
			param.setWarehouseId(alist.get(i).getWarehouseId());
			param.setOrgId(p.getOrgId());
			param.setWarehouseStatus(Constant.STATUS_ACTIVE);
			MetaWarehouse entity = warehouseDao.selectOne(param);
			if(entity == null) continue;
			/**只给前端输出仓库id和名称**/
			MetaWarehouse n = new MetaWarehouse();
			n.setWarehouseId(entity.getWarehouseId());
			n.setWarehouseName(entity.getWarehouseName());
			map.put(entity.getWarehouseId(), n);
		}
		if(map.size() == 0) throw new BizException(BizStatus.WAREHOUSE_UNDEFINE.getReasonPhrase());
		if(map.size() == 1) {
//			p.setWarehouseId(map);
			LoginUtil.setWareHouseId(map.keySet().iterator().next());
		}
		/**向浏览器输出结果**/
        Token r = new Token();
        r.setLevel(p.getAuthLevel());
        r.setStatus("1");
		r.setList(map.values());
		return r;
	}
	/**
	 * 登录功能
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 * @throws IOException 
	 */
//	@OpLog(model = OpLog.OP_TYPE_LOGIN, type = OpLog.OP_TYPE_LOGIN, pos=0)
	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	@ResponseBody
	public void login(@RequestBody UsernamePasswordToken token, HttpServletRequest req, HttpServletResponse rsp) throws IOException {
		ResultModel r = new ResultModel();
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
//			SecurityUtils.getSubject().logout();
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			Token t = queryWareHourse();
			r.setObj(t);
			LoginUtil.printAjax(rsp, JsonUtil.toJson(r));
		} catch(Throwable e) {
			e.printStackTrace();
			r.setError();
			String msg = handleBizException(req, new BizException(e.getMessage()));
			r.addMessage(msg);
		}
		LoginUtil.printAjax(rsp, JsonUtil.toJson(r));
	}

	/**
	 * 退出功能
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@OpLog(model = OpLog.OP_TYPE_LOGOUT, type = OpLog.OP_TYPE_LOGOUT, pos = -2)
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel logout(HttpServletRequest req) {
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			SecurityUtils.getSubject().logout();
			if(log.isInfoEnabled()) log.info("您已经成功退出");
			return new ResultModel();
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}

	@OpLog(model = OpLog.OP_TYPE_LOGOUT, type = OpLog.OP_TYPE_LOGOUT, pos = -2)
	@RequestMapping(value = "/phoneLogout", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel phoneLogout(@RequestBody LoginVo vo, HttpServletRequest req) {
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			SecurityUtils.getSubject().logout();
			if(log.isInfoEnabled()) log.info("您已经成功退出");
			return new ResultModel();
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}

	/**
	 * 设置操作仓库
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/setWarehouse", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel setWarehouse(@RequestBody LoginVo vo) {
		ResultModel r = new ResultModel();
		try {
			if(StringUtils.isBlank(vo.getWarehouseId())) throw new BizException(ErrorCode.DATA_NO_EXISTS);
			/** 校验该帐号是否有该仓库权限，防止非法攻击 **/
			Principal p = LoginUtil.getLoginUser();
			if(p.getAuthLevel() != SysAuth.AUTH_LEVEL_ORG_USER) throw new BizException(ErrorCode.NO_POWER);
			List<SysAccountRole> list = accRoleDao.select(new SysAccountRole().setAccountId(p.getAccountId()).setOrgId(p.getOrgId()));
			if(list == null || list.size() == 0) throw new BizException(ErrorCode.NO_POWER);
			if(!hasWarehouse(vo.getWarehouseId(), list)) throw new BizException(ErrorCode.NO_POWER);
			/** 设置当前操作仓库 **/
//			LoginUtil.getLoginUser().setWarehouseId(vo.getWarehouseId());
			LoginUtil.setWareHouseId(vo.getWarehouseId());
			if(log.isInfoEnabled()) log.info("用户："+LoginUtil.getLoginUser().getUserId()+"，登录仓库："+StringUtils.trimToEmpty(vo.getWarehouseId()));
			return r;
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}
	/**
	* @Description: 查询列表中是否存在指定仓库id
	* @param id 指定仓库id
	* @param alist 仓库列表
	* @return boolean
	* @throws
	*/
	private static boolean hasWarehouse(String id, List<SysAccountRole> alist) {
		for(int i=0; i<alist.size(); ++i) {
			if(StringUtils.isEmpty(alist.get(i).getWarehouseId())) continue;
			if(id.equals(alist.get(i).getWarehouseId())) return true;
		}
		return false;
	}

	/**
	 * 检查是否有权限
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/isPermitted", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel isPermitted(@RequestBody LoginVo perm, HttpServletRequest req) {
		ResultModel r = new ResultModel();
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			Principal p1 = LoginUtil.getLoginUser();
			if(p1 != null && p1.getLoginName() != null) {
				if(log.isDebugEnabled()) log.debug("当前登录帐号：["+p1.getLoginName()+"]");
			} else {
				if(log.isErrorEnabled()) log.error("当前登录帐号为空");
			}
//			perm = perm.replaceAll("=", "");
			boolean p = SecurityUtils.getSubject().isPermitted(perm.getAuthNo());
			if(!p) {
				if(log.isErrorEnabled()) log.error("["+perm+"]无权限");
				throw new BizException(ErrorCode.NO_POWER);
			}
			if(log.isDebugEnabled()) log.debug(perm+"有权限");
			return r;
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}

	/**
	 * 检查是否登录或者超时
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/isTimeout", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel isTimeout(HttpServletRequest req) {
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			Principal p = LoginUtil.getLoginUser();
			if(p == null) throw new BizException(ErrorCode.TIMEOUT_OR_NOPOWER);
			return new ResultModel();
		} catch (Throwable e) {
			throw new BizException(ErrorCode.TIMEOUT_OR_NOPOWER);
		}
	}

	/**
	 * 重定向到的登录页面
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/toLogin", method = RequestMethod.GET)
	public void toLogin(HttpServletRequest req, HttpServletResponse rsp) {
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			if(log.isWarnEnabled()) log.warn("重定向到登录页面");
//			SecurityUtils.getSubject().logout();
//			rsp.sendRedirect(req.getRequestURL()+"/#!/login");
			LoginUtil.redirect(req, rsp, "/#!/login");
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}
	/**
	 * 登录成功，重定向到主页
	 * @author tphe06
	 * @date 2016年11月25日 下午1:12:20
	 * @return 
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public void success(HttpServletRequest req, HttpServletResponse rsp) {
		try {
			if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
			if(log.isWarnEnabled()) log.warn("登录成功，重定向到主页");
			LoginUtil.redirect(req, rsp, "/#!/warehouse/home");
		} catch (Throwable e) {
			throw new BizException(e.getMessage());
		}
	}

}