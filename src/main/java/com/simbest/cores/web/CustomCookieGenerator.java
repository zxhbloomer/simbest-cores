/**
 * 
 */
package com.simbest.cores.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.CookieGenerator;

import com.simbest.cores.utils.configs.CoreConfig;

/**
 * @author lishuyi
 *
 */
@Component
public class CustomCookieGenerator {

	@Autowired
	protected CoreConfig config;
	
	private static CookieGenerator generator = null;
	
	static{
		generator = new CookieGenerator();
		//generator.setCookieMaxAge(CookieGenerator.DEFAULT_COOKIE_MAX_AGE);
		generator.setCookieMaxAge(99999);
	}
	
	public void createCookieName(HttpServletResponse response, String cookieName, String cookieValue){
		//开发调试期间不保存Cookie
		if(!Boolean.valueOf(config.getValue("app.debug"))){
			generator.setCookieName(cookieName);
            generator.setCookieHttpOnly(true);
            generator.setCookieSecure(true);
			generator.addCookie(response, cookieValue);		
			System.out.println("cookieName！+++"+cookieName);
			System.out.println("cookieValue+++"+cookieValue);
			System.out.println("response+++"+response);
		}
	}
}
