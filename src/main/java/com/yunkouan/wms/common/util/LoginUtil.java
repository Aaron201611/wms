package com.yunkouan.wms.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;

import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;

/**
 * 用户登录工具类
 * @author tphe06 2017年2月14日
 */
public final class LoginUtil {
	private static Log log = LogFactory.getLog(LoginUtil.class);

	/**
	 * 获取当前登陆用户信息
	 */
	public static Principal getLoginUser() {
		try {
			Principal user = (Principal) SecurityUtils.getSubject().getPrincipal();
			return user;
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
		return null;
	}
//	public static void setLoginUser(Principal p) {
//		try {
//			SecurityUtils.getSubject().getSession().setAttribute(key, value);
//		} catch(Exception e) {
//			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
//		}
//	}

	public static void setWareHouseId(String id) {
		try {
			SecurityUtils.getSubject().getSession().setAttribute("key.login.user.warehouse.id", id);
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
	}
	public static String getWareHouseId() {
		try {
			String id = (String) SecurityUtils.getSubject().getSession().getAttribute("key.login.user.warehouse.id");
			return id;
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取当前登陆用户信息
	 */
	public static Principal getLoginUser2() {
		try {
			Principal user  = new Principal("1", "D32DC3AD8A194ABF8D89BD36E5E60DFE", "12");
			return user;
		} catch(Exception e) {
			e.printStackTrace();
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
		return null;
	}

	/**
	* @Description: 重定向方法
	* @param request
	* @param response
	* @param l
	* @throws IOException
	* @throws ServletException
	*/
	public static void redirect(ServletRequest request, ServletResponse response, String l) throws IOException, ServletException {
		HttpServletRequest r = (HttpServletRequest)request;
		HttpServletResponse rsp = (HttpServletResponse)response;
//		if(log.isDebugEnabled()) log.debug("forard url:"+l);
//		String url = r.getRequestURL().toString().replaceFirst(r.getServletPath(), "")+l;
		String url1 = r.getRequestURL().toString();
//		if(log.isDebugEnabled()) log.debug("url1:"+url1);
		String url2 = r.getRequestURI();
//		if(log.isDebugEnabled()) log.debug("url2:"+url2);
//		String url3 = r.getServletPath();
//		if(log.isDebugEnabled()) log.debug("url3:"+url3);
//		String url4 = r.getServerName();
//		if(log.isDebugEnabled()) log.debug("url4:"+url4);
		String url5 = r.getContextPath();
//		if(log.isDebugEnabled()) log.debug("url5:"+url5);
		String url = url1.replaceFirst(url2, "")+url5+l;
		if(log.isDebugEnabled()) log.debug("redirect url:"+url);
		rsp.sendRedirect(url);
//		request.getRequestDispatcher(l).forward(request,response);
	}

	/**
	 * 判断是否ajax请求
	 */
	public static boolean isAjax(ServletRequest request) {
		HttpServletRequest r = (HttpServletRequest)request;
		String x = r.getHeader("x-requested-with");
//		if(log.isDebugEnabled()) log.debug("X-Requested-With:"+x);
		String t = r.getContentType();
//		if(log.isDebugEnabled()) log.debug("Content-Type:"+t);
        if (x != null && "XMLHttpRequest".equalsIgnoreCase(x) || t != null && t.indexOf("application/json") >= 0) {
            return true;
        }
        return false;
	}

	/**
	 * 向浏览器输出ajax响应信息
	 * @throws IOException 
	 */
	public static void printAjax(ServletResponse response, String json) throws IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			((HttpServletResponse)response).setHeader("Access-Control-Allow-Origin", "*"); //允许跨域名访问
			response.setContentType("application/json");
			out = response.getWriter();
	        out.println(json);
	        out.flush();
		} catch(IOException e) {
			throw e;
		} finally {
			if(out != null) out.close();
		}
	}
}