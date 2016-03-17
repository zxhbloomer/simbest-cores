/**
 * 
 */
package com.simbest.cores.web.filter;

import org.apache.shiro.authc.AuthenticationToken;

import com.simbest.cores.utils.enums.SNSLoginType;

/**
 * @author lishuyi
 *
 */
public class SNSAuthenticationToken implements AuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7408918486592156700L;
	
	private String ticket;
	private SNSLoginType loginType;
	
	public SNSAuthenticationToken(String ticket, SNSLoginType loginType) {
		super();
		this.ticket = ticket;
		this.loginType = loginType;
	}

	@Override
	public Object getPrincipal() {
		return this.ticket;
	}

	@Override
	public Object getCredentials() {
		return loginType;
	}
	
}
