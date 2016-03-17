/**
 * 
 */
package com.simbest.cores.web.filter;

import javax.servlet.http.HttpServletRequest;

import com.simbest.cores.exceptions.SSOLoginFailedException;

/**
 * SSO 单点登录认证
 * 
 * @author lishuyi
 *
 */
public interface SSOAuthentication{

	/**
	 * 从请求中验证单点登录，并返回用户登录帐号loginName
	 * @param request
	 * @return
	 * @throws SSOLoginFailedException
	 */
	String authenticate(HttpServletRequest request) throws SSOLoginFailedException;
}
