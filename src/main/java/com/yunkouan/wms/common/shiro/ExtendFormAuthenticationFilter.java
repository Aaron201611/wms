package com.yunkouan.wms.common.shiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yunkouan.exception.BizException;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.util.MessageUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.saas.modules.sys.dao.IWarehouseDao;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.wms.modules.sys.dao.IAccountRoleDao;
import com.yunkouan.wms.modules.sys.entity.SysAccountRole;

/**
* @Description: 表单验证（无权访问/获取登录参数/登录成功/登录失败处理）过滤类
* @author tphe06
* @date 2017年3月10日
*/
@Service
public class ExtendFormAuthenticationFilter extends FormAuthenticationFilter {
	protected static Log log = LogFactory.getLog(ExtendFormAuthenticationFilter.class);
	/**@Fields 为了安全起见，限制登陆参数值的长度 */
	private static final int PARAM_MAX_LENGTH = 128;

	/**帐号角色授权数据层接口**/
	@Autowired
	private IAccountRoleDao accRoleDao;
	/**仓库数据层接口**/
	@Autowired
	private IWarehouseDao warehouseDao;

    /**
    * @Description: 被权限拦截时候的回调方法
    * @param request
    * @param response
    * @return
    * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	/**判断是否登录页面地址loginUrl**/
    	HttpServletRequest r = (HttpServletRequest)request;
//    	if(log.isDebugEnabled()) log.debug("ContextPath:"+r.getContextPath());
//    	if(log.isDebugEnabled()) log.debug("SessionId:["+r.getSession().getId()+"]，URL:"+r.getRequestURL());
//    	if(log.isDebugEnabled()) log.debug("URI:"+r.getRequestURI());
//    	if(log.isDebugEnabled()) log.debug("param:"+r.getQueryString());
    	if(log.isDebugEnabled()) log.debug("SessionId:["+r.getSession().getId()+"]，URL:"+r.getRequestURL());
        if(this.isLoginRequest(request, response)) {
        	/**判断是否POST请求**/
//            if(this.isLoginSubmission(request, response)) {
                /**执行后续createToken,onLoginSuccess,onLoginFailure方法**/
                return this.executeLogin(request, response);
//            } else {
//            	if(log.isWarnEnabled()) log.warn("非post方式访问系统登录接口");
//                /**非法访问，输出错误信息**/
//            	if(isAjax(request)) {
//	                Token t = new Token();
//	                t.setStatus("0");
//	                t.setMessage("valid_common_nopower_error");
//	                printAjax(response, JSON.toJSONString(t));
//            	}
//                return false;
//            }
        } else {
        	if(log.isWarnEnabled()) log.warn("无权访问该URL地址：["+r.getRequestURL()+"]");
            /**非法访问，输出错误信息**/
        	if(LoginUtil.isAjax(request)) {
	            Token t = new Token();
	            t.setStatus("-1");
	            String message = MessageUtil.getMessage(r, ErrorCode.NO_POWER);
	            t.setMessage(message);
	            LoginUtil.printAjax(response, JSON.toJSONString(t));
        	} else {
        		LoginUtil.redirect(request, response, "/#!/login");
//        		this.saveRequestAndRedirectToLogin(request, response);
        	}
            return false;
        }
    }

	/**
	* @Description: 读取form表单参数值【暂无用】
	* @param request
	* @param response
	* @return
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		if(log.isInfoEnabled()) log.info(request.getRemoteAddr()+" 访问系统登录接口");
		HttpServletRequest req = (HttpServletRequest)request;
		if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
//		req.getSession(true);
//		if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
		UsernamePasswordToken token = new UsernamePasswordToken();
		try {
			/**@Fields 为了安全起见，限制登陆参数值的长度 */
			int length = request.getContentLength();
			if(length > PARAM_MAX_LENGTH) throw new Exception("登录参数值太长，可能是非法攻击");
			/**json数据存放在http协议的body里面，而非head或者parameter部分 **/
			byte[] b = new byte[length];
			request.getInputStream().read(b);
			/**把json格式字符串数据转化成同名属性的类实例，使用阿里巴巴开源的fastjson，速度较快**/
			String json = new String(b, "UTF-8");
			if(log.isInfoEnabled()) log.info(json);
			token = JSON.parseObject(json, UsernamePasswordToken.class);
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
			return new UsernamePasswordToken();
		}
		boolean rememberMe = isRememberMe(request);//rememberMe
		token.setRememberMe(rememberMe);
		String host = StringUtil.getRemoteAddr((HttpServletRequest) request);
		token.setHost(host);
		return token;
	}

	/**
	* @Description: 登录成功回调事件【给前端返回仓库列表供用户选择】【暂无用】
	* @param token 登录参数
	* @param subject 登录上下文
	* @param request
	* @param response
	* @return
	* @throws Exception
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest)request;
		if(log.isDebugEnabled()) log.debug("SessionId:["+req.getSession().getId()+"]，URL:"+req.getRequestURL());
        /**查询当前登录帐号所拥有权限的仓库列表**/
		Principal p = LoginUtil.getLoginUser();
		if(log.isDebugEnabled()) log.debug("当前登录帐号：["+p.getLoginName()+"]");
//		String perm = "warehouse";
//		boolean power = SecurityUtils.getSubject().isPermitted(perm);
//		if(log.isDebugEnabled()) log.debug("["+perm+"]权限：["+power+"]");
		List<MetaWarehouse> list = new ArrayList<MetaWarehouse>();
		/**只有普通企业用户帐号才能设置仓库，企业管理员无需设置仓库直接登陆到首页**/
		if(p.getAuthLevel() != SysAuth.AUTH_LEVEL_ORG_USER) {
			/**向浏览器输出结果**/
	        Token r = new Token();
	        r.setLevel(p.getAuthLevel());
	        r.setStatus("1");
//			printAjax(response, JSON.toJSONString(r));
	        req.setAttribute(Constant.LOGIN_RESULT, r);
	        return false;
		}
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
			list.add(n);
		}
		if(list.size() == 0) throw new BizException(BizStatus.WAREHOUSE_UNDEFINE.getReasonPhrase());
//		if(list.size() == 1) p.setWarehouseId(list.get(0).getWarehouseId());
		/**向浏览器输出结果**/
        Token r = new Token();
        r.setLevel(p.getAuthLevel());
        r.setStatus("1");
		r.setList(list);
//		printAjax(response, JSON.toJSONString(r));
		req.setAttribute(Constant.LOGIN_RESULT, r);
        return false;
	}

	/**
	* @Description: 登录失败回调事件【暂无用】
	* @param token 登陆参数
	* @param e 登录异常
	* @param request
	* @param response
	* @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        try {
        	HttpServletRequest req = (HttpServletRequest)request;
//        	if(!isAjax(request)) {
//        		HttpServletResponse rsp = (HttpServletResponse)response;
//        		HttpServletRequest req = (HttpServletRequest)request;
//        		rsp.sendRedirect(req.getRequestURL()+"/#!/login");
//        		return false;
//        	}
            Token r = new Token();
            r.setStatus("0");
            r.setMessage(e.getMessage());
            req.setAttribute(Constant.LOGIN_RESULT, r);
//        	printAjax(response, JSON.toJSONString(t));
        } catch (Exception e1) {
            if(log.isErrorEnabled()) log.error(e1.getMessage());
        }
        return false;
	}

	/**
	* @Description: 登录返回结果
	* @author tphe06
	* @date 2017年3月10日
	*/
	public static class Token {
		/**@Fields 状态：0失败，1成功 */
		private String status;
		/**@Fields 权限级别，
		 * 只有普通企业用户帐号才能设置仓库，
		 * 如果只有一个仓库自动设置仓库，
		 * 如果没有任何仓库报错，
		 * 企业管理员无需设置仓库直接登陆到首页 */
		private int level;
		/**@Fields 失败信息 */
		private String message;
		/**@Fields 仓库列表 */
		private Object list;

		public String getStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}

		public Object getList() {
			return list;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setList(Object list) {
			this.list = list;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}
	}
}